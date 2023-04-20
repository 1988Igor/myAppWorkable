package com.example.application.data.entity;

import jakarta.persistence.Entity;
import java.time.LocalDate;

@Entity
public class Projects extends AbstractEntity {

    private Integer projectNumber;
    private String projectName;
    private LocalDate dateOfBeginn;
    private String phone;
    private Integer priceNetto;
    private Integer priceBrutto;
    private String statusOfProject;
    private String comments;

    public Integer getProjectNumber() {
        return projectNumber;
    }
    public void setProjectNumber(Integer projectNumber) {
        this.projectNumber = projectNumber;
    }
    public String getProjectName() {
        return projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    public LocalDate getDateOfBeginn() {
        return dateOfBeginn;
    }
    public void setDateOfBeginn(LocalDate dateOfBeginn) {
        this.dateOfBeginn = dateOfBeginn;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public Integer getPriceNetto() {
        return priceNetto;
    }
    public void setPriceNetto(Integer priceNetto) {
        this.priceNetto = priceNetto;
    }
    public Integer getPriceBrutto() {
        return priceBrutto;
    }
    public void setPriceBrutto(Integer priceBrutto) {
        this.priceBrutto = priceBrutto;
    }
    public String getStatusOfProject() {
        return statusOfProject;
    }
    public void setStatusOfProject(String statusOfProject) {
        this.statusOfProject = statusOfProject;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }

}
