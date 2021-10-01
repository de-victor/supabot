package br.com.malabar.supabot.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;

import br.com.malabar.supabot.entity.Category;
import br.com.malabar.supabot.entity.Game;
import br.com.malabar.supabot.entity.GameName;
import br.com.malabar.supabot.entity.Run;
import br.com.malabar.supabot.entity.RunTime;
import br.com.malabar.supabot.entity.Runner;
import br.com.malabar.supabot.entity.SystemGame;
import br.com.malabar.supabot.entity.Variables;
import br.com.malabar.supabot.repository.ConfigurationRepository;
import br.com.malabar.supabot.utilitarios.UrlUtil;

@Component
public class RunnerMapper {
	

	// @Value("${speedrun.name}")
	// private String userSpdRun;
	
	private final UrlUtil urlUtil;	
	private final RestTemplateBuilder builder;
	private final String uri = "/users";
	private final String pbs = "/personal-bests";
	private final ConfigurationRepository configurationRepository;

	private DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	public RunnerMapper(UrlUtil urlUtil, RestTemplateBuilder builder, ConfigurationRepository configurationRepository) {
		this.builder = builder;
		this.urlUtil = urlUtil;
		this.configurationRepository = configurationRepository;
		
	}
	
	public Runner getSpeedUser() {
		return this.getFullUserByName(this.configurationRepository.findById(1l).get().getSpeedRunName());
	}
	
	public Runner getById(String id) {
		String strJson = builder.build().getForObject(urlUtil.buildUrl(uri)+"/"+id, String.class);
		
		JSONObject json = new JSONObject(strJson);
		
		JSONObject data = (JSONObject) json.get("data");
		
		Runner user = new Runner();
		
		user.setIdOriginal(data.getString("id"));
		user.setName(data.getJSONObject("names").getString("international"));
		user.setWebLink(data.getString("weblink"));
		
		
		return user;
	}

	public Runner getByTwitchName(String twitchName) {
		String strJson = builder.build().getForObject(urlUtil.buildUrl(uri)+"?twitch="+twitchName, String.class);
		
		JSONObject json = new JSONObject(strJson);
		
		JSONObject data = json.getJSONArray("data").getJSONObject(0);
		
		Runner user = new Runner();
		
		user.setIdOriginal(data.getString("id"));
		user.setName(data.getJSONObject("names").getString("international"));
		user.setWebLink(data.getString("weblink"));
		
		
		return user;
	}
	
	public Runner getFullUserByName(String id) {
		Runner user = this.getById(id);
		user.setRuns(this.personalBestById(user.getIdOriginal()));
		
		
		return user;
	}
	
	public List<Run> personalBestById(String id) {
		String strJson = builder.build().getForObject(urlUtil.buildUrl(uri)+"/"+id+pbs+"?embed=game,category", String.class);
		JSONObject json = new JSONObject(strJson);
		
		
		List<Run> runs = new ArrayList<Run>();
		
		
		JSONArray dataArray = json.getJSONArray("data");
		
		for (int i = 0; i < dataArray.length(); i++) {
			Run run = new Run();
			Game game = new Game();
			Category category = new Category();
			RunTime runTime = new RunTime();
			
			JSONObject item = dataArray.getJSONObject(i);
			JSONObject runItem = item.getJSONObject("run");
			JSONObject times = runItem.getJSONObject("times");
			JSONObject gameItem = item.getJSONObject("game").getJSONObject("data");
			JSONObject categoryItem = item.getJSONObject("category").getJSONObject("data");
			JSONObject values = runItem.getJSONObject("values");
			
			//JSONArray links = runItem.getJSONObject("videos").getJSONArray("links");
			
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
			
			//game data
			game.setIdOriginal(gameItem.getString("id"));
			game.setAbreviation(gameItem.getString("abbreviation"));
			game.setReleaseYear(gameItem.getInt("released"));
			game.setWebLink(gameItem.getString("weblink"));
			game.setNames(new GameName(gameItem.getJSONObject("names").get("international")+"", 
									   gameItem.getJSONObject("names").get("japanese")+"", 
									   gameItem.getJSONObject("names").get("twitch")+""));
			run.setGame(game);
			
			//category data
			category.setIdOriginal(categoryItem.getString("id"));
			category.setName(categoryItem.getString("name"));
			category.setWebLink(categoryItem.getString("weblink"));
			category.setRules(categoryItem.get("rules")+"");
			
			run.setCategory(category);
			
			//plataform data
			SystemGame systemGame = new SystemGame();
			systemGame.setIdOriginal(runItem.getJSONObject("system").get("platform")+"");
			//run.setSystem(systemGameMapper.getByFromCacheId(runItem.getJSONObject("system").get("platform")+""));
			
			if(values.length() > 0) {
				run.getCategory().setVariables(new ArrayList<Variables>());
				
				values.keySet().forEach(key ->{
					Variables variable = new Variables();
					variable.setIdOriginal(key);
					variable.setValueKey(values.getString(key));
					run.getCategory().getVariables().add(variable);
					
				});
			}
			
			
			
			runs.add(run);
			
		}
		
		
		return runs;
	}
	
}
