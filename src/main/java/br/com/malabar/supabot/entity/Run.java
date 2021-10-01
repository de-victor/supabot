package br.com.malabar.supabot.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "runs")
public class Run {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "id_original", unique = true, nullable = false)
	private String idOriginal;

	private Integer place;
	
	private String link;
	
	@Embedded
	private RunTime runTime;
	
	@Column(name = "submit_date")
	private LocalDate submitDate;
	
	@Column(name = "web_link")
	private String webLink;
	
	@Column(name = "run_video_uri")
	private String runVideoUri;

	@Column(name = "default_run")
	private Boolean defaultRun = false;

	@ManyToOne
	private Game game;
	
	@ManyToOne
	private Category category;
	
	@ManyToOne(optional = true)
	private SystemGame system;

	@ManyToOne
	private Runner runner;

	public Run(String idOriginal, Integer place, String link, RunTime runTime, LocalDate submitDate, String webLink, String runVideoUri, Game game, Category category, SystemGame system){
		this.idOriginal = idOriginal;
		this.place = place;
		this.link = link;
		this.runTime = runTime;
		this.submitDate = submitDate;
		this.webLink = webLink;
		this.game = game;
		this.category = category;
		this.system = system;
	}
}
