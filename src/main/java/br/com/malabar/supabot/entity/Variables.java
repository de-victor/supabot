package br.com.malabar.supabot.entity;

import javax.persistence.Column;
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
@Table(name = "variables")
public class Variables {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "id_original", nullable = false)
	private String idOriginal;
	
	@Column(name = "value_key")
	private String valueKey;

	@Column(name = "is_sub_category")
	private Boolean isSubCategory;

	@ManyToOne
	private Category category;
	
	private String name;

	public Variables(String idOriginal, String valueKey, String name, Boolean isSubCategory){
		this.idOriginal = idOriginal;
		this.valueKey = valueKey;
		this.name = name;
		this.isSubCategory = isSubCategory;
	}

	public Variables(String valueKey, String name){
		this.valueKey = valueKey;
		this.name = name;
	}
	
	
}
