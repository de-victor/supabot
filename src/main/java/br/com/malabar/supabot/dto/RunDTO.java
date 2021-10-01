package br.com.malabar.supabot.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RunDTO {

    private Long runID;
    private String runnerName;
    private String gameName;
    private Boolean defaultRun;

    
}
