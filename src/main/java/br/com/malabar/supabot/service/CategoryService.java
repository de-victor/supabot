package br.com.malabar.supabot.service;

import org.springframework.stereotype.Service;

import br.com.malabar.supabot.entity.Category;
import br.com.malabar.supabot.generics.GenericService;
import br.com.malabar.supabot.repository.CategoryRepository;

@Service
public class CategoryService extends GenericService<Category, CategoryRepository> {
    
}
