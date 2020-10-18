package main;

import main.reponses.Task;
import main.reponses.TaskList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ToDoListController {

    @GetMapping("/tasks/")
    public List<Task> list() {
        return TaskList.getTaskList();
    }

    @PostMapping("/tasks/")
    public int add(Task task) {
        return TaskList.addTask(task);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> get(@PathVariable int id) {
        Task task = TaskList.getTask(id);
        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity<Task>(task, HttpStatus.OK);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity delete(@PathVariable int id) {

        if (!TaskList.deleteTask(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping("/tasks/")
    public ResponseEntity deleteAll() {
        if (!TaskList.deleteAllTasks()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity updateTask(@PathVariable int id, Task task) {
        if (!TaskList.updateTask(id, task)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }

}
