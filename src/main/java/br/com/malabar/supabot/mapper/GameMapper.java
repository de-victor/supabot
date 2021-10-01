package br.com.malabar.supabot.mapper;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;

import br.com.malabar.supabot.entity.Game;
import br.com.malabar.supabot.entity.GameName;
import br.com.malabar.supabot.utilitarios.UrlUtil;

@Component
public class GameMapper{

	private UrlUtil urlUtil;
	private RestTemplateBuilder builder;
	private final String uri = "/games";

	@Autowired
	public GameMapper(UrlUtil urlUtil, RestTemplateBuilder builder) {
		this.urlUtil = urlUtil;
		this.builder = builder;
	}

	public Game getById(String id) {

		String strJson = builder.build().getForObject(urlUtil.buildUrl(uri) + "/" + id, String.class);

		JSONObject json = new JSONObject(strJson);

		JSONObject data = (JSONObject) json.get("data");

		GameName gameName = new GameName(data.getJSONObject("names").getString("international"),
				data.getJSONObject("names").getString("japanese"),
				data.getJSONObject("names").getString("twitchGameName"));

		return new Game(id, 
					    gameName, 
						data.getString("abbreviation"),
						data.getString("weblink"), 
						data.getInt("released"));
	}

}
