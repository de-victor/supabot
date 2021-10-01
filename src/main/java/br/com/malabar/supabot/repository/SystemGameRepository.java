package br.com.malabar.supabot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.malabar.supabot.entity.SystemGame;

@Repository
public interface SystemGameRepository extends CrudRepository<SystemGame, Long> {

    public SystemGame findByIdOriginal(String idOriginal);
    
}
