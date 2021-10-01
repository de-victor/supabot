package br.com.malabar.supabot.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;

import br.com.malabar.supabot.entity.SystemGame;
import br.com.malabar.supabot.utilitarios.UrlUtil;

@Component
public class SystemGameMapper {

	
	private UrlUtil urlUtil;
	private RestTemplateBuilder builder;
	private final String uri = "/platforms";
	private List<SystemGame> listSystem;
	
	
	@Autowired
	public SystemGameMapper(UrlUtil urlUtil, RestTemplateBuilder builder) {
		this.urlUtil = urlUtil;
		this.builder = builder;
	}
	
	
	public List<SystemGame> listSystem(){
		
		this.listLoad();
		
		
		return this.listSystem;
	}
	
	public SystemGame getByFromCacheId(String id) {
		this.listLoad();
		
		Optional<SystemGame> findAny = this.listSystem.stream().filter(item ->{
			return item.getIdOriginal().contains(id);
		}).findAny();
		
		if(findAny.isPresent()) {
			return findAny.get();
		}
		
		return null;
	}
	
	public SystemGame getById(String id) {
		String strJson = builder.build().getForObject(urlUtil.buildUrl(uri)+"/"+id, String.class);
		
		JSONObject json = new JSONObject(strJson);
		
		JSONObject data = json.getJSONObject("data");
		
		SystemGame systemGame = new SystemGame();
		systemGame.setIdOriginal(id);
		systemGame.setName(data.getString("name"));
		systemGame.setReleaseYear(data.getInt("released"));
		
		return systemGame;
		
	}
	
	private void listLoad() {
		if(this.listSystem == null) {
			int limit = 0;
			this.listSystem = new ArrayList<SystemGame>();
			
			while(limit <= 160) {
				String link = urlUtil.buildUrl(uri);
				if(limit != 0) {
					link = urlUtil.buildUrl(uri)+"?offset="+limit;
				}
				
				String strJson = builder.build().getForObject(link, String.class);
				
				JSONObject json = new JSONObject(strJson);
				
				JSONArray data = json.getJSONArray("data");
				
				for (int i = 0; i < data.length(); i++) {
					SystemGame systemGame = new SystemGame();
					JSONObject item = data.getJSONObject(i);
					
					systemGame.setIdOriginal(item.getString("id"));
					systemGame.setName(item.getString("name"));
					systemGame.setReleaseYear(item.getInt("released"));
					
					
					listSystem.add(systemGame);
					
				}
				
				limit += 20;
			}
			
		}
		
		
	}
	
	
}
