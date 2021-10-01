package br.com.malabar.supabot.entity;

import java.util.List;

import javax.persistence.Column;
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
@Table(name = "system_games")
public class SystemGame {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "id_original", unique = true, nullable = false)
	private String idOriginal;
	
	private String name;
	
	@Column(name = "release_year")
	private Integer releaseYear;
	
	private Boolean emulated;

	@OneToMany(mappedBy = "system")
	private List<Run> runs;
}
