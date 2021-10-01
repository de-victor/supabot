package br.com.malabar.supabot.entity;

import br.com.malabar.supabot.exceptions.InvalidCommandValueSize;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommandLine {
    private String command;
    private String value;

    public CommandLine(String command, String value){
        this.command = command;
        this.value = value;
    }

    public CommandLine(String ...commands) throws InvalidCommandValueSize{
        if(commands.length > 2){
            throw new InvalidCommandValueSize("Quantidade de valores superior a 2");
        }
        this.command = commands[0];
        this.value = commands[1];
    }
}
