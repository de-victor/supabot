package br.com.malabar.supabot.entity;

import br.com.malabar.supabot.enums.SearchOperation;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchElement {
    private String key;
    private Object value;
    private SearchOperation operation;



    public SearchElement(String key, Object value, SearchOperation operation){
        this.key = key;
        this.value = value;
        this.operation = operation;
    }

}
