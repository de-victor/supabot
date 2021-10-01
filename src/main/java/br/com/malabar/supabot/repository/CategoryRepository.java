package br.com.malabar.supabot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.malabar.supabot.entity.Category;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long>{

    public Category findByIdOriginal(String idOriginal);
    
}
