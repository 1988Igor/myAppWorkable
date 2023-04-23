package com.example.application.views;



import com.example.application.data.entity.Projects;
import com.example.application.data.service.ProjectsService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

import java.util.List;

@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard | Projects CRM")
@PermitAll

public class DashboardView extends VerticalLayout {
    private final ProjectsService service;
    private final Projects projects;

    public DashboardView(ProjectsService service, Projects projects) {
        this.service = service;
        this.projects = projects;
        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(getContactStats(), getProjectsChart());
    }

    private Component getContactStats() {
        Span stats = new Span(service.countContacts() + " Projects");
             stats.addClassNames("text-xl", "mt-m");
        stats.addClassNames(
                LumoUtility.FontSize.XLARGE,
                LumoUtility.Margin.Top.MEDIUM);
        return stats;
    }

    private Chart getProjectsChart() {
        Chart chart = new Chart(ChartType.PIE);

        DataSeries dataSeries = new DataSeries();
        for (Projects project : service.getAllProjects()) {
            dataSeries.add(new DataSeriesItem(project.getProjectName(), project.getStatusOfProject().length()));
        }
        chart.getConfiguration().setSeries(dataSeries);
        return chart;
    }





}