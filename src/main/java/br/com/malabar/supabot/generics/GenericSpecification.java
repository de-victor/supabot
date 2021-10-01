package br.com.malabar.supabot.generics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.malabar.supabot.entity.SearchElement;
import br.com.malabar.supabot.enums.SearchOperation;

public class GenericSpecification<E> implements Specification<E> {

    private List<SearchElement> list = new ArrayList<>();

    public void addFilter(SearchElement searchElement) {
        this.list.add(searchElement);
    }

    @Override
    public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();
        
        list.forEach(searchElement ->{
            if (searchElement.getOperation().equals(SearchOperation.GREATER_THAN)) {
                predicates.add(criteriaBuilder.greaterThan(rootPath(root, searchElement), searchElement.getValue().toString()));
            } 
            else if (searchElement.getOperation().equals(SearchOperation.LESS_THAN)) {
                predicates.add(criteriaBuilder.lessThan(rootPath(root, searchElement), searchElement.getValue().toString()));
            } 
            else if (searchElement.getOperation().equals(SearchOperation.GREATER_THAN_EQUAL)) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(rootPath(root, searchElement), searchElement.getValue().toString()));
            } 
            else if (searchElement.getOperation().equals(SearchOperation.LESS_THAN_EQUAL)) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(rootPath(root, searchElement), searchElement.getValue().toString()));
            } 
            else if (searchElement.getOperation().equals(SearchOperation.NOT_EQUAL)) {
                predicates.add(criteriaBuilder.notEqual(root.get(searchElement.getKey()), searchElement.getValue()));
            } 
            else if (searchElement.getOperation().equals(SearchOperation.EQUAL)) {
                predicates.add(criteriaBuilder.equal(root.get(searchElement.getKey()), searchElement.getValue()));
            } 
            else if (searchElement.getOperation().equals(SearchOperation.MATCH)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(rootPath(root, searchElement)),
                        "%" + searchElement.getValue().toString().toLowerCase() + "%"));
            } 
            else if (searchElement.getOperation().equals(SearchOperation.MATCH_END)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(rootPath(root, searchElement)),
                searchElement.getValue().toString().toLowerCase() + "%"));
            } 
            else if (searchElement.getOperation().equals(SearchOperation.MATCH_START)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(rootPath(root, searchElement)),
                        "%" + searchElement.getValue().toString().toLowerCase()));
            } 
            else if (searchElement.getOperation().equals(SearchOperation.IN)) {
                predicates.add(criteriaBuilder.in(root.get(searchElement.getKey())).value(searchElement.getValue()));
            } 
            else if (searchElement.getOperation().equals(SearchOperation.NOT_IN)) {
                predicates.add(criteriaBuilder.not(root.get(searchElement.getKey())).in(searchElement.getValue()));
            }
        });

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private Path<String> rootPath(Root<E> root, SearchElement searchElement) {
        if(searchElement.getKey().contains(".")){
            Path<String> path = null;
            List<String> list = Arrays.asList(searchElement.getKey().split("\\."));

            for(int i = 0;  i < list.size(); i++){
                if(i == 0){
                    path = root.get(list.get(i));
                }
                else{
                    path = path.get(list.get(i));
                }
            }

            return path;
        }
        else{
            return root.get(searchElement.getKey());
        }
    }
}
