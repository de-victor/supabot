package br.com.malabar.supabot.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;

import br.com.malabar.supabot.entity.Run;
import br.com.malabar.supabot.entity.RunTime;
import br.com.malabar.supabot.entity.Runner;
import br.com.malabar.supabot.utilitarios.UrlUtil;

/**
 * acesso a api leaderboard deve informar /{game}/category/{vategory}
 * @param urlUtil
 * @param builder
 */

@Component
public class LeaderBoardMapper {
    
    private final UrlUtil urlUtil;	
	private final RestTemplateBuilder builder;
    private final RunnerMapper runnerMapper;
	private final String uri = "/leaderboards";
    private DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public LeaderBoardMapper(UrlUtil urlUtil, RestTemplateBuilder builder, RunnerMapper runnerMapper){
        this.urlUtil = urlUtil;
        this.builder = builder;
        this.runnerMapper = runnerMapper;
    }

    public List<Run> getLeaderBoardTop(Run pbRun, int nbr){
        String buildUrl = this.buildUrl(pbRun, nbr);
        System.out.println(buildUrl);
        String strJson = builder.build().getForObject(buildUrl, String.class);
        JSONObject json = new JSONObject(strJson);
		
		
        JSONObject data = (JSONObject) json.get("data");
        JSONArray dataArray = data.getJSONArray("runs");


        List<Run> runs = new ArrayList<Run>();

        for (int i = 0; i < dataArray.length(); i++) {
			Run run = new Run();
			RunTime runTime = new RunTime();
			
			JSONObject item = dataArray.getJSONObject(i);
			JSONObject runItem = item.getJSONObject("run");
			JSONObject times = runItem.getJSONObject("times");
			
			//comum run data
			run.setPlace(item.getInt("place"));
			run.setIdOriginal(runItem.getString("id"));
			run.setWebLink(runItem.getString("weblink"));
			run.setRunVideoUri(runItem.getJSONObject("videos").getJSONArray("links").getJSONObject(0).getString("uri"));
			run.setSubmitDate(LocalDate.from(format.parse(runItem.getString("date"))));
			
			
			//run time data
			runTime.setPrimaryTime(times.getLong("primary_t"));
			runTime.setRealTime(times.getLong("realtime_noloads_t"));
			runTime.setStrPrimaryTime(times.getString("primary"));
			runTime.setStrRealTime(times.get("realtime_noloads")+"");
			run.setRunTime(runTime);
			
			Runner runner = runnerMapper.getById(runItem.getJSONArray("players").getJSONObject(0).getString("id"));
            
            run.setRunner(runner);
			
			runs.add(run);
			
		}
        
        return runs;
    }

    private String buildUrl(Run run, int nbr){
        StringBuilder url = new StringBuilder();

        if(run.getCategory().getVariables().size() > 0){
            url.append(urlUtil.buildUrl(uri))
           .append("/"+run.getGame().getIdOriginal())
           .append("/category")
           .append("/"+run.getCategory().getIdOriginal())
           .append("?top="+nbr+"&")
           .append(run.getCategory().getVariables()
                                    .stream()
                                    .filter(v -> v.getIsSubCategory())
                                    .map(item -> String.format("var-%s=%s", item.getIdOriginal(), item.getValueKey()))
                                    .collect(Collectors.joining("&")));
            //.append("&embed=players");
        }
        else{
            url.append(urlUtil.buildUrl(uri))
           .append("/"+run.getGame().getIdOriginal())
           .append("/category")
           .append("/"+run.getCategory().getIdOriginal())
           .append("?top="+nbr+"&");
           //.append("&embed=players");
        }
        
        return url.toString();
    }

}
