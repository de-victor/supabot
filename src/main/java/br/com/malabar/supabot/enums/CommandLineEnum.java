package br.com.malabar.supabot.enums;

/**
 * Enum com os parametros usados na inicialização da aplicação
 * 
 * @author = Atherion
 */
public enum CommandLineEnum {

    CHATACCESSTOKEN("chat-access-token"),
    UPDATE("update"),
    TWITCHNAME("twitch-name");

    private String value;

    CommandLineEnum(String value){
        this.value = value;
    }

    public String value(){
        return this.value;
    }
    
}
