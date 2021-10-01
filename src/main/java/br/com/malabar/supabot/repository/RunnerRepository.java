package br.com.malabar.supabot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.malabar.supabot.entity.Runner;

@Repository
public interface RunnerRepository extends CrudRepository<Runner, Long> {

    public Runner findByName(String name);
    
}
