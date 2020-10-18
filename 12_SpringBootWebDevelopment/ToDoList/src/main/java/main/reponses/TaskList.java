package main.reponses;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TaskList {
    private static final Map<Integer, Task> taskList = new ConcurrentHashMap<Integer, Task>();

    public static List<Task> getTaskList() {
        return new ArrayList<Task>(taskList.values());
    }

    public static Task getTask(int id) {
        return taskList.get(id);
    }

    public static boolean deleteAllTasks() {
        if (!taskList.isEmpty()) {
            taskList.forEach(taskList::remove);
            return true;
        } else {
            return false;
        }
    }

    public static int addTask(Task task) {
        int id = taskList.size() + 1;
        task.setId(id);
        taskList.put(id, task);
        return id;
    }

    public static boolean deleteTask(int id) {
        if (taskList.containsKey(id)) {
            taskList.remove(id);
            return true;
        } else {
            return false;
        }
    }

    public static boolean updateTask(int id, Task task) {
        if (taskList.containsKey(id)) {
            taskList.put(id, task);
            return true;
        }
        return false;
    }

}
