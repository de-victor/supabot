package br.com.malabar.supabot.service;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

import org.springframework.stereotype.Service;

import br.com.malabar.supabot.entity.Configurations;
import br.com.malabar.supabot.repository.ConfigurationRepository;

@Service
public class TwitchService {

	
	private final ConfigurationRepository configurationRepository;
	private final BotCommandProcessor botCommandProcessor;

	private TwitchClient tc;
	private Configurations configurations;

	public TwitchService(ConfigurationRepository configurationRepository, BotCommandProcessor botCommandProcessor) {
		this.configurationRepository = configurationRepository;
		this.botCommandProcessor = botCommandProcessor;
	}

	public void start() {
		this.configurations = this.configurationRepository.findById(1l).get();

		tc = TwitchClientBuilder.builder().withEnableHelix(true)
										  .withEnableChat(true)
										  .withChatAccount(new OAuth2Credential("twitch4j", this.configurations.getChatToken()))
										  .build();

		tc.getChat().joinChannel(this.configurations.getTwitchName());

		tc.getEventManager().onEvent(ChannelMessageEvent.class, event ->{
			printChatMsg(event);
			this.botCommandProcessor.processEvenForCommands(event, tc);
		});
	}

	private void printChatMsg(ChannelMessageEvent event) {
		System.out.println("[#" + event.getChannel().getName() + "]["+event.getPermissions().toString()+"] " + event.getUser().getName() + ": " + event.getMessage());
	}
}
