package com.example.application.data.service;

import com.example.application.data.entity.Projects;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.annotations.Formula;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProjectsService {


    private final ProjectsRepository repository;

    public ProjectsService(ProjectsRepository repository) {
        this.repository = repository;
    }

    public Optional<Projects> get(Long id) {
        return repository.findById(id);
    }

    public Projects update(Projects entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public void deleteProject(Projects projects){repository.delete(projects);
    }

    public Page<Projects> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Projects> list(Pageable pageable, Specification<Projects> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public List<Projects> listByName(String name, PageRequest of) {
        return repository.findAll();
    }



    public  long countContacts(){
        return repository.count();
    }





    public List<Projects> findAllContacts(String filterText){
        if(filterText == null || filterText.isEmpty()){
            return  repository.findAll();
        } else{
            return repository.search(filterText);
        }
    }





    public List<Projects> getAllProjects() {
        return repository.findAll();
    }
}
