package br.com.malabar.supabot.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable
public class RunTime {
	
	@Column(name = "primary_time")
	private Long primaryTime;
	
	@Column(name = "real_time")
	private Long realTime;
	
	@Column(name = "str_primary_time")
	private String strPrimaryTime;
	
	@Column(name = "str_real_time")
	private String strRealTime;

	public RunTime(Long primaryTime, Long realTime, String strPrimaryTime, String strRealTime){
		this.primaryTime = primaryTime;
		this.realTime = realTime;
		this.strPrimaryTime = strPrimaryTime;
		this.strRealTime = strRealTime;
	}

}
