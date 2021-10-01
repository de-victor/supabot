package br.com.malabar.supabot.mapper;

import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.malabar.supabot.entity.Run;
import br.com.malabar.supabot.entity.SystemGame;

@Component
public class RunMapper {
	
	private final RunnerMapper userMapper;
	private final VariableMapper variableMapper;
	private final SystemGameMapper systemGameMapper;
	
	public RunMapper(RunnerMapper userMapper, VariableMapper variableMapper, SystemGameMapper systemGameMapper) {
		this.userMapper = userMapper;
		this.variableMapper = variableMapper;
		this.systemGameMapper = systemGameMapper;
	}
	
	
	public Run getRunByGame(String gameName) {
		Optional<Run> pbOption = userMapper.getSpeedUser().getRuns().stream().filter(item ->{
			return item.getGame().getNames().getTwitchGameName().equals(gameName);
		}).findFirst();
		
		if(pbOption.isPresent()) {
			Run run = buildVariables(buildSystemGame(pbOption.get()));
			return run;
		}
		return null;
	}
	
	private Run buildVariables(Run run) {
		if(run.getCategory().getVariables().size() > 0) {
			run.getCategory().getVariables().forEach(item ->{
				if(item.getName() == null) {
					String name = variableMapper.getVariableList(item.getIdOriginal())
													   .stream().filter(va -> va.getValueKey().equals(item.getValueKey()))
													   .findFirst()
													   .get()
													   .getName();
					
					item.setName(name);
				}
			});
			
			
		}
		return run;
	}
	
	private Run buildSystemGame(Run run) {
		SystemGame systemGame = new SystemGame();
		if(run.getSystem().getId() != null && run.getSystem().getName() != null) {
			systemGame = systemGameMapper.getById(run.getSystem().getIdOriginal());
		}
		run.setSystem(systemGame);
		
		return run;
	}

}
