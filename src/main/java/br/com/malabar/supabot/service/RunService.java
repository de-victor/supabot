package br.com.malabar.supabot.service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import br.com.malabar.supabot.dto.RunDTO;
import br.com.malabar.supabot.entity.Run;
import br.com.malabar.supabot.entity.SearchElement;
import br.com.malabar.supabot.entity.Variables;
import br.com.malabar.supabot.enums.SearchOperation;
import br.com.malabar.supabot.especification.RunSpecification;
import br.com.malabar.supabot.mapper.VariableMapper;
import br.com.malabar.supabot.repository.RunRepository;
import br.com.malabar.supabot.repository.VariableRepository;

@Service
public class RunService {
    
    private final RunRepository runRepository;
    private final VariableRepository variableRepository;
    private final VariableMapper variableMapper;

    public RunService(RunRepository runRepository, VariableRepository variableRepository, VariableMapper variableMapper){
        this.runRepository = runRepository;
        this.variableRepository = variableRepository;
        this.variableMapper = variableMapper;
    }

    public List<Run> getTop3Runs(String runnerName){
        List<Run> list = runRepository.findRunByRunnerLimit3(runnerName);

        list.forEach(item -> checkAndBuildCategoryVariables(item));

        return list;
    }

    public List<Run> getRunsBySpecification(RunDTO runDto){

        RunSpecification runSpecification = new RunSpecification();

        validateDto(runSpecification, runDto);

        List<Run> list = runRepository.findAll(runSpecification);

        list.forEach(item -> checkAndBuildCategoryVariables(item));
        
        return list;
    }

    public Boolean runValid(Long id){
        Optional<Run> run = runRepository.findById(id);
        
        if(run.isPresent()){
            return true;
        }

        return false;
    }

    public Run getRandomRun(String runnerName){
        Random random = new Random();
        RunDTO dto = new RunDTO();
        RunSpecification runSpecification = new RunSpecification();
        
        dto.setRunnerName(runnerName);

        validateDto(runSpecification, dto);

        List<Run> list = runRepository.findAll(runSpecification);
        
        Run run = list.get(random.nextInt(list.size()));

        checkAndBuildCategoryVariables(run);
        
        return run;
    }

    public List<Run> getDefaultRun(Boolean defaultRun){
        RunDTO dto = new RunDTO();
        RunSpecification runSpecification = new RunSpecification();
        
        dto.setDefaultRun(defaultRun);

        validateDto(runSpecification, dto);

        List<Run> list = runRepository.findAll(runSpecification);

        list.forEach(item -> checkAndBuildCategoryVariables(item));

        return list;
    }

    public void setDefaultRun(Long id){
        runRepository.disableDefaultRuns();
        runRepository.updateDefaulByRunId(id);
    }

    private void checkAndBuildCategoryVariables(Run run){
        run.getCategory().getVariables().forEach(vari ->{
            if(vari.getName() == null){
                Variables variable = variableMapper.getVariableList(vari.getIdOriginal())
                                                   .stream().filter(va -> va.getValueKey().equals(vari.getValueKey()))
                                                   .findFirst()
                                                   .get();
                vari.setName(variable.getName());
                vari.setIsSubCategory(variable.getIsSubCategory());
                variableRepository.save(vari);
            }
        });
    }


    private void validateDto(RunSpecification runSpecification, RunDTO runDto) {
        if(runDto.getDefaultRun() != null){
            runSpecification.addFilter(new SearchElement("defaultRun", runDto.getDefaultRun(), SearchOperation.EQUAL));
        }
        if(runDto.getRunID() != null){
            runSpecification.addFilter(new SearchElement("id", runDto.getRunID(), SearchOperation.EQUAL));
        }
        if(runDto.getRunnerName() != null){
            runSpecification.addFilter(new SearchElement("runner.name", runDto.getRunnerName(), SearchOperation.MATCH));
        }
        if(runDto.getGameName() != null){
            runSpecification.addFilter(new SearchElement("game.names.twitchGameName", runDto.getGameName(), SearchOperation.MATCH));
        }
    }


}
