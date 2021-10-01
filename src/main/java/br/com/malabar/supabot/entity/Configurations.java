package br.com.malabar.supabot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "configurations")
public class Configurations {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "chat_token")
    private String chatToken;
    
    @Column(name = "client_id_token")
    private String cliendIdToken;
    
    @Column(name = "secret_id_token")
    private String secretIdToken;
    
    @Column(name = "twitch_name")
    private String twitchName;
    
    @Column(name = "speed_run_name")
    private String speedRunName;


    public Configurations(String chatToken, String cliendIdToken, String secretIdToken, String twitchName, String speedRunName){
        this.chatToken = chatToken;
        this.cliendIdToken = cliendIdToken;
        this.secretIdToken = secretIdToken;
        this.twitchName = twitchName;
        this.speedRunName = speedRunName;
    }

    public Configurations(String chatToken, String twitchName, String speedRunName){
        this.chatToken = chatToken;
        this.twitchName = twitchName;
        this.speedRunName = speedRunName;
    }

}
