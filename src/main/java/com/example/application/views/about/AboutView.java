package com.example.application.views.about;

import com.example.application.data.entity.ContactForm;
import com.example.application.data.entity.Projects;
import com.example.application.data.service.ProjectsRepository;
import com.example.application.data.service.ProjectsService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.Optional;

import jakarta.annotation.security.PermitAll;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("CRM")
@Route(value = "about/:projectsID?/:action?(edit)", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PermitAll
public class AboutView extends Div implements BeforeEnterObserver {

    private final String PROJECTS_ID = "projectsID";
    private final String PROJECTS_EDIT_ROUTE_TEMPLATE = "about/%s/edit";
    private  final ProjectsRepository projectsRepository;
    private final ProjectsService projectsService;

    private final Grid<Projects> grid = new Grid<>(Projects.class, false);
    TextField filterText = new TextField();

    private TextField projectNumber;
    private TextField projectName;
    private DatePicker dateOfBeginn;

    private TextField priceNetto;
    private TextField priceBrutto;
    private TextField statusOfProject;
    private TextField comments;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");

    private final BeanValidationBinder<Projects> binder;

    private Projects projects;

    ContactForm form;




    public AboutView(ProjectsRepository projectsRepository, ProjectsService projectsService) {
        this.projectsRepository = projectsRepository;
        this.projectsService = projectsService;
        addClassNames("about-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();




        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);


        add(splitLayout);
        add(getToolbar());


        updateList();






        // Configure Grid
        grid.addClassNames("contact-grid");
        grid.addColumn("projectNumber").setAutoWidth(true);
        grid.addColumn("projectName").setAutoWidth(true);
        grid.addColumn("dateOfBeginn").setAutoWidth(true);
        grid.addColumn("priceNetto").setAutoWidth(true);
        grid.addColumn("priceBrutto").setAutoWidth(true);
        grid.addColumn("statusOfProject").setAutoWidth(true);
        grid.addColumn("comments").setAutoWidth(true);
        grid.setItems(query -> projectsService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(PROJECTS_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(AboutView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Projects.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(projectNumber).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("projectNumber");
        binder.forField(priceNetto).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("priceNetto");
        binder.forField(priceBrutto).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("priceBrutto");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.projects == null) {
                    this.projects = new Projects();
                }
                binder.writeBean(this.projects);
                projectsService.update(this.projects);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(AboutView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });

        delete.addClickListener(event ->{
            deleteProject(this.projects);
            refreshGrid();
            Notification.show("Data deleted");
            UI.getCurrent().navigate(AboutView.class);


        } );



    }


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> projectsId = event.getRouteParameters().get(PROJECTS_ID).map(Long::parseLong);
        if (projectsId.isPresent()) {
            Optional<Projects> projectsFromBackend = projectsService.get(projectsId.get());
            if (projectsFromBackend.isPresent()) {
                populateForm(projectsFromBackend.get());
            } else {
                Notification.show(String.format("The requested projects was not found, ID = %s", projectsId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(AboutView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        projectNumber = new TextField("Project Number");
        projectName = new TextField("Project Name");
        dateOfBeginn = new DatePicker("Date Of Beginn");
        priceNetto = new TextField("Price Netto");
        priceBrutto = new TextField("Price Brutto");
        statusOfProject = new TextField("Status Of Project");
        comments = new TextField("Comments");
        formLayout.add(projectNumber, projectName, dateOfBeginn, priceNetto, priceBrutto, statusOfProject,
                comments);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        buttonLayout.add(save, cancel, delete);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Projects value) {
        this.projects = value;
        binder.readBean(this.projects);

    }
    private void  deleteProject(Projects projects){
        projectsRepository.delete(projects);

    }



    private HorizontalLayout getToolbar() {
        addClassName("tool-view");
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());


        Button addContactButton = new Button("Add contact");
        addContactButton.addClickListener(click -> addContact());


        var toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        toolbar.setVerticalComponentAlignment(FlexComponent.Alignment.END);

        return toolbar;
    }

    private void addContact() {
        grid.asSingleSelect().clear();
        editContact(new Projects());
    }

    public void editContact(Projects contact) {
        if (contact == null) {
            closeEditor();
        } else {
            form.setContact(contact);
            form.setVisible(true);
            addClassName("editing");
        }
    }
    private void closeEditor() {
        form.setContact(null);
        form.setVisible(false);
        removeClassName("editing");
    }





    private void updateList(){
        grid.setItems(projectsService.findAllContacts(filterText.getValue()));


    }




}

