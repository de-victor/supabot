package br.com.malabar.supabot;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.malabar.supabot.service.LoadService;
import br.com.malabar.supabot.service.TwitchService;
import lombok.extern.java.Log;

@Log
@SpringBootApplication
public class App implements CommandLineRunner{
	
	
	private final TwitchService twitchService;
	private final LoadService loadService;

	public App(TwitchService twitchService, LoadService loadService){
		this.twitchService = twitchService;
		this.loadService = loadService;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		log.info("INICIANDO APLICAÇÃO!");	
		loadService.fullCheckDataLoad(Arrays.asList(args));
		twitchService.start();
	}

	

}
