package io.coraho.ppmtool.services;

import io.coraho.ppmtool.domain.Backlog;
import io.coraho.ppmtool.domain.Project;
import io.coraho.ppmtool.domain.ProjectTask;
import io.coraho.ppmtool.exceptions.ProjectNotFoundException;
import io.coraho.ppmtool.repositories.BacklogRepository;
import io.coraho.ppmtool.repositories.ProjectRepository;
import io.coraho.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {

        //project not found
        try {
            Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);

            projectTask.setBacklog(backlog);

            Integer backlogSequence = backlog.getPTSequence();

            backlogSequence += 1;

            backlog.setPTSequence(backlogSequence);

            projectTask.setProjectSequence(projectIdentifier + "_" + backlogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            if (projectTask.getPriority() == 0 || projectTask.getPriority() == null) { // priority is defaulted to be 0.
                projectTask.setPriority(3);
            }

            if (projectTask.getStatus() == null || projectTask.getStatus() == "") {
                projectTask.setStatus("To_Do");
            }

            return projectTaskRepository.save(projectTask);
        }catch (Exception e) {
            throw new ProjectNotFoundException("Project Not Found");
        }


    }

    public Iterable<ProjectTask> findBacklogById(String id) {
        Project project = projectRepository.findByProjectIdentifier(id);

        if (project == null) {
            throw new ProjectNotFoundException("Project with ID:'" + id + "' does not exist");
        }
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findProjectTaskByProjectSequence(String backlog_id, String ptSequence) {
        //make sure project does exist
        Backlog backlog = backlogRepository.findByProjectIdentifier((backlog_id));
        if (backlog == null) {
            throw new ProjectNotFoundException("Project with ID:'" + backlog_id + "' does not exist");
        }
        //make sure project task does exist
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(ptSequence);
        if (projectTask == null) {
            throw new ProjectNotFoundException("Project task with Sequence '" + ptSequence + "' not found");
        }
        //make sure project and project task are relevant
        if (!projectTask.getProjectIdentifier().equals(backlog_id)) {
            throw new ProjectNotFoundException("Project task with Sequence '"+ ptSequence + "' does not" +
                    "exist in Project '" + backlog_id + "'");
        }
        return projectTask;
    }


    public ProjectTask updateByProjectSequence(ProjectTask updateProject, String backlog_id, String ptSequence) {
        ProjectTask projectTask = findProjectTaskByProjectSequence(backlog_id, ptSequence);
        projectTask = updateProject;
        return projectTaskRepository.save(projectTask);
    }

    public void deleteTaskByProjectSequence(String backlog_id, String ptSequence) {
        ProjectTask taskToDelete = findProjectTaskByProjectSequence(backlog_id, ptSequence);

        projectTaskRepository.delete(taskToDelete);
    }
}














