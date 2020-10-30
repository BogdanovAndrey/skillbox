package main.controllers;

import main.model.Task;
import main.model.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TaskController {
    private final TaskRepository taskRepository;

    public TaskController(@Autowired TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/tasks/")
    public List<Task> list() {
        List<Task> taskList = new ArrayList<>();
        taskRepository.findAll().forEach(taskList::add);
        return taskList;
    }

    @PostMapping("/tasks/")
    public int add(Task task) {
        Task addedTask = taskRepository.save(task);
        return addedTask.getId();
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> get(@PathVariable int id) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity<Task>(task, HttpStatus.OK);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity delete(@PathVariable int id) {

        if (taskRepository.existsById(id)) {
            taskRepository.delete(taskRepository.findById(id).get());
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @DeleteMapping("/tasks/")
    public ResponseEntity deleteAll() {
        taskRepository.deleteAll();
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity updateTask(@PathVariable int id, Task task) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Task updatedTask = taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.OK).body(updatedTask.getId());
    }

    @PutMapping("/tasks/")
    public ResponseEntity updateTasks(@RequestBody List<Task> tasks) {

        if (tasks == null || tasks.size() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        ArrayList<Task> updatedTasks = (ArrayList<Task>) taskRepository.saveAll(tasks);

        return ResponseEntity.status(HttpStatus.OK).body(updatedTasks);
    }
}
