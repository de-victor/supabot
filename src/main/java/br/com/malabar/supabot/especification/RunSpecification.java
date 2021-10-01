package br.com.malabar.supabot.especification;

import br.com.malabar.supabot.entity.Run;
import br.com.malabar.supabot.generics.GenericSpecification;

public class RunSpecification extends GenericSpecification<Run> {

    /*
    public static Specification<Run> runnerName(String name){
        return (root, criteriaQuery, criterriaBuilder) -> 
        criterriaBuilder.like(root.get("runner").get("name"), "%"+name+"%");
    }

    public static Specification<Run> gameName(String name){
        return (root, criteriaQuery, criterriaBuilder) -> 
        criterriaBuilder.like(root.get("game").get("names").get("international"), "%"+name+"%");
    }

    public static Specification<Run> defatulRun(Boolean defaultRun){
        return (root, criteriaQuery, criterriaBuilder) -> 
        criterriaBuilder.equal(root.get("defaultRun"), defaultRun);
    }
    */
    
}
