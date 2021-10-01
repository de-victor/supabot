package br.com.malabar.supabot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.malabar.supabot.entity.Game;

@Repository
public interface GamesRepository extends CrudRepository<Game, Long> {

    public Game findByIdOriginal(String idOriginal);
    
}
