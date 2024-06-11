package com.amedvedev.taskmanager.service;

import com.amedvedev.taskmanager.dto.TaskDTO;
import com.amedvedev.taskmanager.entitiy.Task;
import com.amedvedev.taskmanager.entitiy.User;
import com.amedvedev.taskmanager.repository.TaskRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task find(Long id) {
        return taskRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public List<TaskDTO> findAll() {
        return findAll(null, null);
    }

    public List<TaskDTO> findAll(String sort, String order) {
        Sort.Direction direction =
                (order == null || order.equalsIgnoreCase("asc")) ? Sort.Direction.ASC : Sort.Direction.DESC;

        if (sort == null) {
            sort = "id";
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Task> tasks = taskRepository.findAllByUserUsername(user.getUsername(), Sort.by(direction, sort));
        return tasks.stream()
                .map(this::convertToDTO)
                .toList();
    }

    private TaskDTO convertToDTO(Task task) {
        return TaskDTO.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .dateCreated(task.getDateCreated())
                .dueDate(task.getDueDate())
                .build();
    }

    public void save(Task task) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        task.setUser(user);
        taskRepository.save(task);
    }

    public void deleteAll() {
        taskRepository.deleteAll();
    }

    public void delete(Long id) {
        taskRepository.delete(find(id));
    }
}
