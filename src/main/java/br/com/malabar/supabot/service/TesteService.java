package br.com.malabar.supabot.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.malabar.supabot.dto.RunDTO;
import br.com.malabar.supabot.entity.Run;

@Service
public class TesteService {

    private final RunService runService;

    public TesteService(RunService runService){
        this.runService = runService;
    }


    public void testeConsulta(){
        RunDTO dto = new RunDTO();
        dto.setDefaultRun(true);
        dto.setRunnerName("Shime");

        List<Run> list = runService.getRunsBySpecification(dto);

        list.forEach(item -> System.out.println(item.getGame().getNames().getInternational()));

    }
}
