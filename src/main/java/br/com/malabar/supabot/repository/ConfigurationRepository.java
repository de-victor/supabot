package br.com.malabar.supabot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.malabar.supabot.entity.Configurations;

@Repository
public interface ConfigurationRepository extends CrudRepository<Configurations, Long> {
    
}
