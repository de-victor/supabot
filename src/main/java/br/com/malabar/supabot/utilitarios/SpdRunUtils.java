package br.com.malabar.supabot.utilitarios;

import org.springframework.stereotype.Component;

@Component
public class SpdRunUtils {
	
	public String spdRunTimerToStrTime(String time) {
		return time.replace("PT", "").replace("H", "Hr:").replace("M", "m:").replace("S", "s");
	}

}
