package kz.danik.yel.app;

import io.jmix.core.DataManager;
import io.jmix.core.FileRef;
import kz.danik.yel.entity.Project;
import kz.danik.yel.entity.ProjectDto;
import kz.danik.yel.entity.ProjectStatus;
import kz.danik.yel.entity.TelegramUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("yel_ProjectServiceBean")
public class ProjectServiceBean {

    @Autowired
    private DataManager dataManager;

    public void createProject(TelegramUser telegramUser,String link,String text){
        Project project = dataManager.create(Project.class);
        project.setStatus(ProjectStatus.NEW_REQUEST);
        project.setLink(link);
        project.setTextFromRequest(text);
        dataManager.save(project);
    }
    public List<ProjectDto> getProjects(){
        List<Project> projects = dataManager.load(Project.class)
                .query("select e from yel_Project e where e.status = 'ACTIVE'")
                .list();
        List<ProjectDto>  dtos = new ArrayList<>();
        projects.forEach(e->dtos.add(new ProjectDto(e.getId(),getFilePath(e.getIcon()),e.getTextRu(),e.getLink())));
        return dtos;
    }

    public String getFilePath(FileRef fileRef){
        return "";
    }
}