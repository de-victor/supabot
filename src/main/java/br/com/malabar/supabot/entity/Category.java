package br.com.malabar.supabot.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "categories")
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "id_original", unique = true, nullable = false)
	private String idOriginal;
	
	private String name;
	
	@Column(name = "web_link")
	private String webLink;
	
	@Column(length = 5000)
	private String rules;
	
	@OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
	private List<Variables> variables;
}
