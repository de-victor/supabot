package br.com.malabar.supabot.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "runners")
public class Runner {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "id_original", unique = true, nullable = false)
	private String idOriginal;
	
	@Column(name = "web_link")
	private String webLink;
	
	@Column(nullable = false)
	private String name;
	
	@OneToMany(mappedBy = "runner")
	private List<Run> runs;

	public Runner(String idOriginal, String webLink, String name, List<Run> runs){
		this.idOriginal = idOriginal;
		this.webLink = webLink;
		this.name = name;
		this.runs = runs;
	}

	public Runner(){
		this.runs = new ArrayList<Run>();
	}
	

}
