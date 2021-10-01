package br.com.malabar.supabot.mapper;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;

import br.com.malabar.supabot.entity.Variables;
import br.com.malabar.supabot.utilitarios.UrlUtil;

@Component
public class VariableMapper {
	
	
	private final UrlUtil urlUtil;
	private final RestTemplateBuilder builder;
	private final String uri = "/variables";
	
	@Autowired
	public VariableMapper(UrlUtil urlUtil, RestTemplateBuilder builder) {
		this.urlUtil = urlUtil;
		this.builder = builder;
	}
		
	public List<Variables> getVariableList(String id){
		List<Variables> list = new ArrayList<Variables>();
		String strJson = builder.build().getForObject(urlUtil.buildUrl(uri)+"/"+id, String.class);
		
		JSONObject json = new JSONObject(strJson);
		
		JSONObject data = (JSONObject) json.get("data");
		
		JSONObject values = data.getJSONObject("values").getJSONObject("values");
		
		values.keySet().forEach(key ->{
			list.add(new Variables(id, key, values.getJSONObject(key).getString("label"), data.getBoolean("is-subcategory")));
		});
		
		return list;
	}

}
