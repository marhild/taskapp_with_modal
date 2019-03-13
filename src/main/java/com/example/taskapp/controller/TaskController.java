package com.example.taskapp.controller;

import com.example.taskapp.model.Status;
import com.example.taskapp.model.Task;
import com.example.taskapp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;


/**
 * @author platoiscoding.com
 */
@Controller
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * GET all tasks from Database
     * @return template view for all tasks
     */
    @RequestMapping(value = {"/tasks", "/"}, method=RequestMethod.GET)
    public String dashboard(Model model) {
        //display all Tasks
        Set<Task> tasks = taskService.getTasks();
        model.addAttribute("tasks", tasks);

        Set<Status> statusList = new HashSet<>();
        Status.stream().forEach(statusList::add);
        model.addAttribute("statusList", statusList);

        return "index";
    }

    /**
     * Shows Tasks by Status
     * @param model         contains TaskObject
     * @param taskStatus    may have the values "open/closed/reopened"
     * @return              Set of Tasks with specific status
     */
    @RequestMapping(value = "/{status}", method=RequestMethod.GET)
    public String displayByStatus(Model model, @PathVariable("status") String taskStatus) {

        if(taskStatus.equals(Status.OPEN.getTypeOfStatus())){
            model.addAttribute("tasks", taskService.getTasksByStatus(Status.OPEN));
        } else if(taskStatus.equals(Status.CLOSED.getTypeOfStatus())){
            model.addAttribute("tasks", taskService.getTasksByStatus(Status.CLOSED));
        } else if(taskStatus.equals(Status.REOPENED.getTypeOfStatus())){
            model.addAttribute("tasks", taskService.getTasksByStatus(Status.REOPENED));
        }

        Set<Status> statusList = new HashSet<>();
        Status.stream().forEach(statusList::add);
        model.addAttribute("statusList", statusList);

        return "index";
    }

    /**
     * handles Status Changes
     * @param taskId        Task Id
     * @param action        may contain "close/open/reopen"
     * @param request       helps redirect to previous site
     * @return              redirection
     */
    @RequestMapping(value = "/task/{id}/{action}", method=RequestMethod.GET)
    public String handleStatus(@PathVariable("id") Long taskId,
                               @PathVariable("action") String action,
                               HttpServletRequest request){
        Task task = taskService.findById(taskId);

        if (action.equals("close")) {
            if(task.getStatus() == Status.OPEN) {taskService.closeTask(taskId);}
            if(task.getStatus() == Status.REOPENED) {taskService.closeTask(taskId);}
        }
        if(action.equals("reopen") && task.getStatus() == Status.CLOSED) {taskService.reopenTask(taskId);}

        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    /**
     * Save NEW Task in Database
     * @param taskDetails   field values
     * @return              redirect to Dashboard
     */
    @RequestMapping(path = "/task/create", method = RequestMethod.POST)
    public String createTask(Task taskDetails) {
        Task newTask = taskService.createTask(taskDetails);
        return "redirect:/";
    }

    /**
     * updates Task in DB wirh field Values from EDIT Modal
     * @param taskDetails   Task Object with field Values from EDIT Modal
     * @return              redirect to dashboard
     */
    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public String updateTaskWithModal(Task taskDetails) {
        taskService.updateTask(taskDetails.getId(), taskDetails);
        return "redirect:/";
    }

    /**
     * @ResponseBody: object returned is automatically serialized
     * into JSON and passed back into the HttpResponse object
     * (Source: https://www.baeldung.com/spring-request-response-body)
     *
     * @param taskId    taskId
     * @return      Task from DB
     */
    @RequestMapping(path = "/findTask/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Task findTask(@PathVariable("id") long taskId){
        return taskService.findById(taskId);
    }


    /**
     * Deletes Task from Database
     * @param taskId    TaskId
     * @return          redirect to Dashboard
     */
    @RequestMapping(path = "/task/{id}/delete", method = RequestMethod.GET)
    public String deleteTask(@PathVariable("id") long taskId, HttpServletRequest request) {
        taskService.deleteTask(taskId);
        return "redirect:/";
    }

}
