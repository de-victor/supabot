package br.com.malabar.supabot.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "games")
public class Game {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "id_original", unique = true, nullable = false)
	private String idOriginal;
	
	@Embedded
	private GameName names;

	private String abreviation;
	
	@Column(name = "web_link")
	private String webLink;
	
	@Column(name = "release_year")
	private Integer releaseYear;

	@OneToMany(mappedBy = "game")
	private List<Run> runs;

	public Game(String id, GameName names, String abreviation, String webLink, Integer releaseYear){
		this.idOriginal = id;
		this.names = names;
		this.abreviation = abreviation;
		this.webLink = webLink;
		this.releaseYear = releaseYear;
	}
	

}
