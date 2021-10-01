package br.com.malabar.supabot.service;

import org.springframework.stereotype.Service;

import br.com.malabar.supabot.entity.Runner;
import br.com.malabar.supabot.generics.GenericService;
import br.com.malabar.supabot.repository.RunnerRepository;

@Service
public class RunnerService extends GenericService<Runner, RunnerRepository>{
    
    public Runner findByName(String name){
        return repository.findByName(name);
    }

}
