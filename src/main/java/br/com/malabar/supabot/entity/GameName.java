package br.com.malabar.supabot.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable
public class GameName {
	private String international;
	
	private String japanese;
	
	@Column(name = "twitch_game_name")
	private String twitchGameName;
	
	public GameName(String international, String japanese, String twitchGameName) {
		this.international = international;
		this.japanese = japanese;
		this.twitchGameName = twitchGameName;
	}

}
