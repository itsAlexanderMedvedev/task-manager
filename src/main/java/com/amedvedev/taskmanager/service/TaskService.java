package com.amedvedev.taskmanager.service;

import com.amedvedev.taskmanager.dto.FilterDTO;
import com.amedvedev.taskmanager.dto.TaskDTO;
import com.amedvedev.taskmanager.dto.TaskSummaryDTO;
import com.amedvedev.taskmanager.entitiy.Category;
import com.amedvedev.taskmanager.entitiy.Task;
import com.amedvedev.taskmanager.entitiy.User;
import com.amedvedev.taskmanager.repository.CategoryRepository;
import com.amedvedev.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;


    public TaskDTO find(Long id) {
        return convertToDTO(taskRepository.findById(id).orElseThrow(NoSuchElementException::new));
    }

    public List<TaskSummaryDTO> findAll() {
        return findAll(null, null, FilterDTO.builder().build());
    }

    public List<TaskSummaryDTO> findAll(FilterDTO filterDTO) {
        return findAll(null, null, filterDTO);
    }

    public List<TaskSummaryDTO> findAll(String sort, String order, FilterDTO filterDTO) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Sort.Direction direction =
                (order == null || order.equalsIgnoreCase("asc")) ? Sort.Direction.ASC : Sort.Direction.DESC;

        if (sort == null) {
            sort = "id";
        }

        List<Task> tasks;

        if (filterDTO.getCategories() == null && filterDTO.getPriorities() == null) {
            tasks = taskRepository.findAllByUser(user, Sort.by(direction, sort));
        } else if (filterDTO.getCategories() != null && filterDTO.getPriorities() == null) {
            tasks = taskRepository.findAllByUserAndCategoriesIn(user, filterDTO.getCategories(), Sort.by(direction, sort));
        } else if (filterDTO.getCategories() == null && filterDTO.getPriorities() != null) {
            tasks = taskRepository.findAllByUserAndPriorityIn(user, filterDTO.getPriorities(), Sort.by(direction, sort));
        } else {
            tasks = taskRepository.findAllByUserAndCategoriesOrPriorityIn(
                    user,
                    filterDTO.getCategories(),
                    filterDTO.getPriorities(),
                    Sort.by(direction, sort)
            );
        }


//        List<Category> categories = filterDTO.getCategories() == null ?
//                categoryRepository.findAllByUser(user) :
//                categoryRepository.findAllByUserAndNames(user, filterDTO.getCategories());
//
//        List<String> priorities = filterDTO.getPriorities() == null ?
//                List.of("High", "Medium", "Low") :
//                filterDTO.getPriorities();
//
//        List<Task> tasks = taskRepository.findAllByUserAndCategoriesOrPriorityIn(
//                user,
//                categories.stream().map(Category::getName).collect(Collectors.toList()),
//                priorities,
//                Sort.by(direction, sort)
//        );

        System.out.println(filterDTO.getCategories());
        System.out.println(filterDTO.getPriorities());
        System.out.println("______________");
        System.out.println(tasks);
//        System.out.println(categories);
//        System.out.println(priorities);
        return tasks.stream()
                .map(this::convertToSummaryDTO)
                .toList();
    }

    public void save(TaskDTO taskDTO) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Task task = new Task();
        task.setId(taskDTO.getId());
        task.setName(taskDTO.getName());
        task.setDescription(taskDTO.getDescription());
        task.setDueDate(taskDTO.getDueDate());

        List<Category> categories = new ArrayList<>();
        for (String categoryName : taskDTO.getCategories()) {
            categories.add(categoryRepository.findByUserAndName(user, categoryName));
        }
        task.setCategories(categories);
        task.setPriority(taskDTO.getPriority());
        task.setUser(user);

        Task savedTask = taskRepository.save(task);
        taskDTO.setId(savedTask.getId());
    }

    public void deleteAll() {
        taskRepository.deleteAll();
    }

    public void delete(Long id) {
        taskRepository.delete(taskRepository.findById(id).orElseThrow(NoSuchElementException::new));
    }

    public TaskSummaryDTO convertToSummaryDTO(Task task) {
        return TaskSummaryDTO.builder()
                .id(task.getId())
                .name(task.getName())
                .dueDate(task.getDueDate())
                .categories(task.getCategories().stream().map(Category::getName).collect(Collectors.joining(", ")))
                .priority(task.getPriority() == null ? "" : task.getPriority())
                .build();
    }

    public TaskDTO convertToDTO(Task task) {
        return TaskDTO.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .dateCreated(task.getDateCreated())
                .dueDate(task.getDueDate())
                .categories(task.getCategories().stream().map(Category::getName).collect(Collectors.toList()))
                .priority(task.getPriority() == null ? "" : task.getPriority())
                .build();
    }

    public List<TaskSummaryDTO> search(String term) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Task> tasks = taskRepository.findAllByUserAndNameOrDescriptionContainingIgnoreCase(user, term.trim());
        return tasks.stream()
                .map(this::convertToSummaryDTO)
                .toList();
    }
}
