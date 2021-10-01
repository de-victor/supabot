package br.com.malabar.supabot.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.common.enums.CommandPermission;

import org.springframework.stereotype.Service;

import br.com.malabar.supabot.dto.RunDTO;
import br.com.malabar.supabot.entity.Configurations;
import br.com.malabar.supabot.entity.Run;
import br.com.malabar.supabot.entity.Variables;
import br.com.malabar.supabot.enums.BotCommandsEnum;
import br.com.malabar.supabot.mapper.LeaderBoardMapper;
import br.com.malabar.supabot.repository.ConfigurationRepository;
import br.com.malabar.supabot.utilitarios.SpdRunUtils;
import lombok.extern.java.Log;

@Log
@Service
public class BotCommandProcessor {

    private final RunService runService;
    
    private final SpdRunUtils spdRunUtils;
    private final LeaderBoardMapper leaderBoardMapper;
    private final LoadService loadService;
    private final ConfigurationRepository configurationRepository;
    private TwitchClient tc;
    private Configurations configurations;


    public BotCommandProcessor(RunService runService, SpdRunUtils spdRunUtils, LeaderBoardMapper leaderBoardMapper, LoadService loadService, ConfigurationRepository configurationRepository){
        this.runService = runService;
        this.loadService = loadService;
        this.spdRunUtils = spdRunUtils;
        this.leaderBoardMapper = leaderBoardMapper;
        this.configurationRepository = configurationRepository;
    }

    public void processEvenForCommands(ChannelMessageEvent event, TwitchClient tc){
        this.tc = tc;
        if(event.getMessage().startsWith("!")){
            if(!publicCommands(event)){
                privateCommands(event);
            }
        }
    }

    private void privateCommands(ChannelMessageEvent event){
        String msg = event.getMessage().toLowerCase();
        if(checkForSuperUser(event)){
            if(msg.startsWith(BotCommandsEnum.SETPBGAME.value())){
                this.setPbGame(event);
            }
            else if(msg.startsWith(BotCommandsEnum.SETPBBYID.value())){
                this.setPbById(event);
            }
            else if(msg.startsWith(BotCommandsEnum.UPDATE.value())){
                this.updateBase(event);
            }
        }
    }

    private void updateBase(ChannelMessageEvent event){
        event.getTwitchChat().sendMessage(event.getChannel().getName(), "Sincronizando a base :|");
		this.loadService.updateByResent();
    }

    private void setPbById(ChannelMessageEvent event){
        List<String> comString = Arrays.asList(event.getMessage().split(" "));
        comString = comString.subList(1, comString.size());
        if(comString.size() == 1){
            event.getTwitchChat().sendMessage(event.getChannel().getName(), "Informe o número da run!");
        }
        else if(comString.size() == 2){
            comString = comString.subList(1, comString.size());
            try {
                Long nbr = Long.parseLong(comString.get(0));
                if(runService.runValid(nbr)){
                    runService.setDefaultRun(nbr);
                    sendMsg(event, "Pb run setado com sucesso!");
                }
                else{
                    sendMsg(event, "Número informado não existe!");
                }
            } catch (Exception e) {
                sendMsg(event, "Informe um número válido porfavor");
            }
        }
    }

    private void setPbGame(ChannelMessageEvent event){
        log.info("processando set de pb por "+event.getUser().getName());
        
        List<String> comString = Arrays.asList(event.getMessage().split(" "));
        comString = comString.subList(1, comString.size());

        RunDTO dto = new RunDTO();
        dto.setGameName(comString.stream().collect(Collectors.joining(" ")));
        dto.setRunnerName(getConfig().getSpeedRunName());

        List<Run> runs = runService.getRunsBySpecification(dto);

        if(runs.isEmpty()){
            sendMsg(event, String.format("Nenhuma run localizada para o jogo %s", dto.getGameName()));
        }
        else if(runs.size() == 1){
            runService.setDefaultRun(runs.get(0).getId());
            sendMsg(event, String.format("Pb run definido para o jogo %s", runs.get(0).getGame().getNames().getInternational()));
        }
        else if(runs.size() > 1){
            String m = String.format("Você corre %s categorias use % [NÚMERO]. %s", runs.size()+""
                                                                                    , BotCommandsEnum.SETPBBYID.value()
                                                                                    , concatCategoriesVariablesFromRuns(runs));
            
            sendMsg(event, m);
        }
    }

    private Boolean publicCommands(ChannelMessageEvent event){
        String msg = event.getMessage().toLowerCase();

        if(msg.startsWith(BotCommandsEnum.PB.value())){
            this.pbCommand(event);
            return true;
        }
        else if(msg.startsWith(BotCommandsEnum.TOP.value())){
            this.topLeaderBoard(event);
            return true;
        }
        else if(msg.startsWith(BotCommandsEnum.RANDOMPB.value())){
            this.randomPB(event);
            return true;
        }
        else if(msg.startsWith(BotCommandsEnum.COMLIST.value())){
            this.commandList(event);
        }
        
        return false;
    }

    private void commandList(ChannelMessageEvent event){
        log.info("enviando lista de comandos para "+event.getUser().getName());
        this.tc.getChat().sendPrivateMessage(event.getUser().getName(), "!pbs, !randompb, !top");
    }

    private void topLeaderBoard(ChannelMessageEvent event){
        log.info("Processando top runs por "+event.getUser().getName());
			List<Run> runs = runService.getDefaultRun(true);
			
			if(runs == null){
                sendMsg(event, String.format("Necessário informar pb para pesquisar a leaderboard utilize o comando %s", BotCommandsEnum.SETPBGAME.value()));
			}
			else if(runs.size() == 1){
				Run run = runs.get(0);
				List<Run> leaderBoardTop = leaderBoardMapper.getLeaderBoardTop(run, 3);

				String runnersBoard = leaderBoardTop.stream().map(item -> String.format("%s-%s(%s)", item.getPlace()+""
																								   , item.getRunner().getName()
																								   , spdRunUtils.spdRunTimerToStrTime(item.getRunTime().getStrPrimaryTime())))
									   						 .collect(Collectors.joining(","));
                sendMsg(event, String.format("Top 3 na leadboard -> %s", runnersBoard));
			}
    }

    private void randomPB(ChannelMessageEvent event){
        log.info("Processando random runs por "+event.getUser().getName());
        sendMsg(event, buildRandomMsg(runService.getRandomRun(getConfig().getSpeedRunName())));
    }

    private void pbCommand(ChannelMessageEvent event){
        log.info("Processando pb do runner por "+event.getUser().getName());

        List<Run> runs = runService.getDefaultRun(true);

        if(runs.isEmpty()){
            sendMsg(event, "Não existe PB setado, porvafor use comando !setGame [NOME]");
        }
        else if(runs.size() == 1){
            sendMsg(event, buildPbMsg(runs.get(0)));
            
        }
        else if(runs.size() > 1){
            sendMsg(event,"Detectado várias categorias de um mesmo jogo, necessário informar a categoria");
        }
    }

    private String buildPbMsg(Run run) {
		String str = String.format("Meu PB atual é %s na categoria %s e estou em %dº na leaderboard! Confira o video da minha run %s", spdRunUtils.spdRunTimerToStrTime(run.getRunTime().getStrPrimaryTime()), 
				   run.getCategory().getName(), 
				   run.getPlace(), 
				   run.getRunVideoUri());
		if(run.getCategory().getVariables().size() > 0) {
			str = String.format("Meu PB atual é %s na categoria %s(%s) e estou em %dº na leaderboard! Confira o video da minha run %s", spdRunUtils.spdRunTimerToStrTime(run.getRunTime().getStrPrimaryTime()), 
					   run.getCategory().getName(),
					   run.getCategory().getVariables().stream().map(Variables::getName).collect(Collectors.joining("-")),
					   run.getPlace(), 
					   run.getRunVideoUri());
		}
		
		return str;
	}

    private String buildRandomMsg(Run runByGame) {
		String str = String.format("Meu PB atual é %s no jogo %s na categoria %s e estou em %dº na leaderboard! Confira o video da minha run %s", spdRunUtils.spdRunTimerToStrTime(runByGame.getRunTime().getStrPrimaryTime()), 
				runByGame.getGame().getNames().getTwitchGameName(),
				runByGame.getCategory().getName(), 
				runByGame.getPlace(), 
				runByGame.getRunVideoUri());
		if(runByGame.getCategory().getVariables().size() > 0) {
			str = String.format("Meu PB atual é %s no jogo %s na categoria %s(%s) e estou em %dº na leaderboard! Confira o video da minha run %s", spdRunUtils.spdRunTimerToStrTime(runByGame.getRunTime().getStrPrimaryTime()), 
					   runByGame.getGame().getNames().getTwitchGameName(),
					   runByGame.getCategory().getName(),
					   runByGame.getCategory().getVariables().stream().map(Variables::getName).collect(Collectors.joining("-")),
					   runByGame.getPlace(), 
					   runByGame.getRunVideoUri());
		}
		
		return str;
	}

    private void sendMsg(ChannelMessageEvent event, String msg){
        event.getTwitchChat().sendMessage(event.getChannel().getName(), msg);
    }

    private Boolean checkForSuperUser(ChannelMessageEvent event){
		List<CommandPermission> list = event.getPermissions().stream().filter(item ->{
			return item.name().equals(CommandPermission.MODERATOR.name()) || item.name().equals(CommandPermission.OWNER.name());
		}).collect(Collectors.toList());
		if(list.isEmpty()){
			return false;
		}
		return true;
	}

    private String concatCategoriesVariablesFromRuns(List<Run> runs) {
		return runs.stream().map(item -> String.format("(%s - [%s %s])", item.getId()+""
														   , item.getCategory().getName()
														   , item.getCategory().getVariables().stream()
																							  .map(Variables::getName)
																							  .collect(Collectors.joining("|"))
											)
					).collect(Collectors.joining(" "));
	}

    private Configurations getConfig(){
        if(this.configurations == null){
            this.configurations = configurationRepository.findById(1l).get();
        }
        return this.configurations;
    }
    
}
