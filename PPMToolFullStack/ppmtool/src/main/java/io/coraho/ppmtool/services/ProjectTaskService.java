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

            if (projectTask.getPriority() == null) { // priority is defaulted to be 0.
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
}
