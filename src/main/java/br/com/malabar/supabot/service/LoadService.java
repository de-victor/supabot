package br.com.malabar.supabot.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.malabar.supabot.entity.Category;
import br.com.malabar.supabot.entity.CommandLine;
import br.com.malabar.supabot.entity.Configurations;
import br.com.malabar.supabot.entity.Game;
import br.com.malabar.supabot.entity.Runner;
import br.com.malabar.supabot.entity.SystemGame;
import br.com.malabar.supabot.enums.CommandLineEnum;
import br.com.malabar.supabot.mapper.RunnerMapper;
import br.com.malabar.supabot.repository.CategoryRepository;
import br.com.malabar.supabot.repository.ConfigurationRepository;
import br.com.malabar.supabot.repository.GamesRepository;
import br.com.malabar.supabot.repository.RunRepository;
import br.com.malabar.supabot.repository.RunnerRepository;
import br.com.malabar.supabot.repository.SystemGameRepository;
import br.com.malabar.supabot.repository.VariableRepository;
import lombok.extern.java.Log;

@Service
@Log
public class LoadService {

    private final RunnerMapper runnerMapper;
    private final GamesRepository gamesRepository;
    private final CategoryRepository categoryRepository;
    private final SystemGameRepository systemGameRepository;
    private final RunnerRepository runnerRepository;
    private final RunRepository runRepository;
    private final VariableRepository variableRepository;
    private final ConfigurationRepository configurationRepository;

    public LoadService(RunnerMapper runnerMapper, GamesRepository gamesRepository, CategoryRepository categoryRepository, 
                       SystemGameRepository systemGameRepository, RunnerRepository runnerRepository, RunRepository runRepository, 
                       VariableRepository variableRepository, ConfigurationRepository configurationRepository){

        this.runnerMapper = runnerMapper;
        this.gamesRepository = gamesRepository;
        this.categoryRepository = categoryRepository;
        this.systemGameRepository = systemGameRepository;
        this.runnerRepository = runnerRepository;
        this.runRepository = runRepository;
        this.variableRepository = variableRepository;
        this.configurationRepository = configurationRepository;

    }

    public void fullCheckDataLoad(List<String> args){
        List<Configurations> list = (List<Configurations>)configurationRepository.findAll();
        
        if(list.isEmpty()){
            firsTimeRun(args);
        }
        else{
            checkUpdateConfiguration(args, list);
           
        }
    }

    private void checkUpdateConfiguration(List<String> args, List<Configurations> list) {
        if(args.size() > 0){
            Configurations conf = list.get(0);
            args.forEach(arg ->{
                try {
                    String[] split = arg.split("=");
                    CommandLine commandLine = new CommandLine(split);
                    if(commandLine.getCommand().equals(CommandLineEnum.CHATACCESSTOKEN.value())){
                        conf.setChatToken(commandLine.getValue());
                    }
                    if(commandLine.getCommand().equals(CommandLineEnum.TWITCHNAME.value())){
                        if(!conf.getTwitchName().equals(commandLine.getValue())){
                            conf.setTwitchName(commandLine.getValue());
                        }
                        
                    }
                } catch (Exception e) {
                    log.info("Erro: "+e.getMessage());
                    e.getStackTrace();
                    System.exit(0);
                }
            });
            if(!conf.getTwitchName().equals(list.get(0).getTwitchName())){
                log.info("Detectado mudança no nome do usuario da twitch, forçando update dos dados para o novo nome");
                configurationRepository.save(conf);
                this.updateByResent();
            }
            else{
                log.info("Atualizando as configurações na base");
                configurationRepository.save(conf);
            }
        }
    }

    private void firsTimeRun(List<String> args) {
        log.info("primeira execução da base de dados");
        log.info("Carregando configurações");
        this.configDataLoad(args);
        
        log.info("Carregando dados do runner");
        this.runnerDataLoad();
    }
    

    private void configDataLoad(List<String> args){
        List<Configurations> list = (List<Configurations>)configurationRepository.findAll();
        if(list.isEmpty()){
            commandLineProcessor(args);
        }
    }

    private void commandLineProcessor(List<String> args) {
        List<CommandLineEnum> mandadoryCommands = Arrays.asList(CommandLineEnum.CHATACCESSTOKEN, CommandLineEnum.TWITCHNAME);

        List<CommandLine> argsCommands = args.stream().map(item -> {
                                                    String[] split = item.split("=");
                                                    if(split.length != mandadoryCommands.size()){
                                                        System.out.println(String.format("Obrigatório informar %s", mandadoryCommands.stream()
                                                                                                                                      .map(man -> man.value())
                                                                                                                                      .collect(Collectors.joining(" "))
                                                                                        )
                                                                          );
                                                        System.exit(0);
                                                    }
                                                    return new CommandLine(split[0], split[1]);
                                                }).collect(Collectors.toList());

        if(argsCommands.size() != mandadoryCommands.size()){
            mandadoryCommands.forEach(man ->{
                List<CommandLine> filter = argsCommands.stream().filter(ar -> ar.getCommand().equals(man.value())).collect(Collectors.toList());
                if(filter.isEmpty()){
                    System.out.println(String.format("Necessário informar %s", man.value()));
                    System.exit(0);
                }
            });
        }

        Configurations configurations = new Configurations();
        argsCommands.forEach(arg ->{
            if(arg.getCommand().equals(CommandLineEnum.CHATACCESSTOKEN.value())){
                configurations.setChatToken(arg.getValue());
            }
            if(arg.getCommand().equals(CommandLineEnum.TWITCHNAME.value())){
                configurations.setTwitchName(arg.getValue());
            }
        });

        Runner runner = runnerMapper.getByTwitchName(configurations.getTwitchName());

        configurations.setSpeedRunName(runner.getName());
        configurationRepository.save(configurations);

    }

    private void runnerDataLoad(){
        Optional<Configurations> maybeConfig = configurationRepository.findById(1l);
        Runner runner = runnerRepository.findByName(maybeConfig.get().getSpeedRunName());
        if(runner == null){
            if(maybeConfig.isPresent()){
                Runner speedUser = runnerMapper.getFullUserByName(maybeConfig.get().getSpeedRunName());
                final Runner runnerSaved = runnerRepository.save(speedUser);
                speedUser.getRuns().forEach(item ->{
                    Game game = gamesRepository.findByIdOriginal(item.getGame().getIdOriginal());
                    if(game == null){
                        item.setGame(gamesRepository.save(item.getGame()));
                    }
                    else{
                        item.setGame(game);
                    }

                    Category category = categoryRepository.findByIdOriginal(item.getCategory().getIdOriginal());
                    if(category == null){
                        item.setCategory(categoryRepository.save(item.getCategory()));
                        if(item.getCategory().getVariables() != null){
                            item.getCategory().getVariables().forEach(v ->{
                                v.setCategory(item.getCategory());
                                variableRepository.save(v);
                            });
                        }
                    }
                    else{
                        item.setCategory(category);
                    }

                    if(item.getSystem() != null){
                        SystemGame systemGame = systemGameRepository.findByIdOriginal(item.getSystem().getIdOriginal());
                        if(systemGame == null){
                            item.setSystem(systemGameRepository.save(item.getSystem()));
                        }
                        else{
                            item.setSystem(systemGame);
                        }
                    }
                    item.setRunner(runnerSaved);
                    runRepository.save(item);
                });
            }
            
        }
        
    }

    public void updateByResent(){
        runRepository.deleteAll();
        runnerRepository.deleteAll();
        this.runnerDataLoad();

    }

}
