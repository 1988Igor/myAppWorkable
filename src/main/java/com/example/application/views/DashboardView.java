package com.example.application.views;



import com.example.application.data.service.ProjectsService;
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

@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard | Vaadin CRM")
public class DashboardView extends VerticalLayout {
    private final ProjectsService service;

    public DashboardView(ProjectsService service) {
        this.service = service;
        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(getContactStats());
    }

    private Component getContactStats() {
        Span stats = new Span(service.countContacts() + " contacts");
             stats.addClassNames("text-xl", "mt-m");
        //stats.addClassNames(
                //LumoUtility.FontSize.XLARGE,
                //LumoUtility.Margin.Top.MEDIUM);
        return stats;
    }


}