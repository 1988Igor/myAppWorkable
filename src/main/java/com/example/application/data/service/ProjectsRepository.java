package com.example.application.data.service;

import com.example.application.data.entity.Projects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectsRepository extends JpaRepository<Projects, Long>, JpaSpecificationExecutor<Projects> {


    @Query("select c from Projects c " +
            "where lower(c.projectName) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.statusOfProject) like lower(concat('%', :searchTerm, '%'))" + "or lower(c.comments) like lower(concat('%', :searchTerm, '%'))")
    List<Projects> search(@Param("searchTerm") String searchTerm);
}
