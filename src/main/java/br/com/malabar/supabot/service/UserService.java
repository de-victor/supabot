package br.com.malabar.supabot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.malabar.supabot.entity.Runner;
import br.com.malabar.supabot.mapper.RunnerMapper;

@Service
public class UserService {
	
	
	private RunnerMapper userMapper;
	private Runner userCached;
	
	@Autowired
	public UserService(RunnerMapper userMapper) {
		this.userMapper = userMapper;
	}
	
	public Runner getUser(String id) {
		if(userCached == null) {
			this.userCached = userMapper.getFullUserByName(id);
		}
		return userCached;
	}
	
	
	public void userRunInfo(String id) {
		
		Runner user = userMapper.getFullUserByName(id);
		
		System.out.println("Analisando PBs de "+user.getName()+" pagina na speedrun: "+user.getWebLink());
		
		System.out.println("Qtd de runs: "+user.getRuns().size());
		
		user.getRuns().forEach(item->{
			System.out.println("===================================");
			System.out.println("Jogo: "+item.getGame().getNames().getTwitchGameName());
			if(item.getSystem() != null && item.getSystem().getName() != null) {
				System.out.println("Plataforma: "+item.getSystem().getName());
			}
			System.out.println("Posição: "+item.getPlace());
			System.out.println("Categoria: "+item.getCategory().getName());
			System.out.println("Tempo: "+ spdRunTimerToStrTime(item.getRunTime().getStrPrimaryTime()));
			if(!item.getRunTime().getStrRealTime().equals("null")) {
				System.out.println("Tempo sem loads: "+spdRunTimerToStrTime(item.getRunTime().getStrRealTime()));
			}
			System.out.println("===================================");
		});
		
	}
	
	private String spdRunTimerToStrTime(String time) {
		return time.replace("PT", "").replace("H", ":").replace("M", ":").replace("S", "");
	}
	
	
	
	
	
	

}
