package kz.danik.yel.view.project;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;
import kz.danik.yel.entity.Project;
import kz.danik.yel.view.main.MainView;

@Route(value = "projects", layout = MainView.class)
@ViewController("yel_Project.list")
@ViewDescriptor("project-list-view.xml")
@LookupComponent("projectsDataGrid")
@DialogMode(width = "64em")
public class ProjectListView extends StandardListView<Project> {
}