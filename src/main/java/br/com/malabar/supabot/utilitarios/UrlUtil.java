package br.com.malabar.supabot.utilitarios;

import org.springframework.stereotype.Component;

@Component
public class UrlUtil {
	
	private final String urlBase = "https://www.speedrun.com/api/v1";

	
	public String buildUrl(String url) {
		return this.urlBase+url;
	}
	
}
