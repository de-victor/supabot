package br.com.malabar.supabot.enums;

/**
 * Enum com os comandos usados pelo BOT na twitch
 * 
 * @author = Atherion
 */
public enum BotCommandsEnum {

    TOP("!top"),
    RANDOMPB("!randompb"),
    PB("!pbs"),
    SETPBGAME("!setpbgame"),
    SETPBBYID("!setpbbyid"),
    COMLIST("!comlist"),
    UPDATE("!update");

    private String value;


    BotCommandsEnum(String value){
        this.value = value;
    }

    public String value(){
        return this.value;
    }


    
}
