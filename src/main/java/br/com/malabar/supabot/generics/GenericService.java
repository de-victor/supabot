package br.com.malabar.supabot.generics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

/**
 * Generic abstract class intented to be used has default method in annoted class has service
 * @author Victor
 */

public abstract class GenericService<E, R extends CrudRepository<E, Long>> {
    
    @Autowired
    protected R repository;

    public void save(E entity){
        repository.save(entity);
    }

    public void update(E entity){
        repository.save(entity);
    }

    public void delete(Long id){
        repository.deleteById(id);
    }

    public void delete(E entity){
        repository.delete(entity);
    }

}
