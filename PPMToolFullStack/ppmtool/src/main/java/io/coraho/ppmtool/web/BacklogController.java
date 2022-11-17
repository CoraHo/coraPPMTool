package io.coraho.ppmtool.web;

import io.coraho.ppmtool.domain.Project;
import io.coraho.ppmtool.domain.ProjectTask;
import io.coraho.ppmtool.services.ErrorMapValidationService;
import io.coraho.ppmtool.services.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.ws.Response;
import java.util.List;

@Controller
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private ErrorMapValidationService mapValidationService;

    @PostMapping("/{backlog_id}")
    public ResponseEntity<?> addProjectTaskToBacklog(@Valid @RequestBody ProjectTask projectTask,
                                                     BindingResult result, @PathVariable String backlog_id) {
        ResponseEntity<?> errorMap = mapValidationService.ErrorMapValidation(result);
        if (errorMap != null) {
            return errorMap;
        }

        ProjectTask projectTask1 = projectTaskService.addProjectTask(backlog_id, projectTask);

        return new ResponseEntity<ProjectTask>(projectTask1, HttpStatus.CREATED);
    }

    @GetMapping("/{backlog_id}")
    public ResponseEntity<?> getProjectBacklog(@PathVariable String backlog_id) {
        Iterable<ProjectTask> tasks = projectTaskService.findBacklogById(backlog_id);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/{backlog_id}/{ptSequence}")
    public ResponseEntity<?> getProjectTaskBySeqeunce(@PathVariable String backlog_id, @PathVariable String ptSequence) {
        ProjectTask projectTask = projectTaskService.findProjectTaskByProjectSequence(backlog_id, ptSequence);
        return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.OK);
    }

    @PatchMapping("/{backlog_id}/{ptSequence}")
    public ResponseEntity<?> updateByProjectTaskSequence(@Valid @RequestBody ProjectTask updateProject, BindingResult result,
                                                         @PathVariable String backlog_id, @PathVariable String ptSequence) {
        ResponseEntity<?> errorMap = mapValidationService.ErrorMapValidation(result);
        if (errorMap != null) {
            return errorMap;
        }

        ProjectTask updatedTask = projectTaskService.updateByProjectSequence(updateProject, backlog_id, ptSequence);

        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/{backlog_id}/{ptSequence}")
    public ResponseEntity<?> deleteTaskByProjectSequence(@PathVariable String backlog_id, @PathVariable String ptSequence) {
        projectTaskService.deleteTaskByProjectSequence(backlog_id, ptSequence);

        return new ResponseEntity<String>("Project task with sequence '" + ptSequence + "' was deleted", HttpStatus.OK);
    }
}















