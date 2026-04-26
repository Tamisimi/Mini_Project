package org.example.project.controller;

import jakarta.validation.Valid;
import org.example.project.entity.Todo;
import org.example.project.repository.TodoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/")
public class TodoController {

    private final TodoRepository todoRepository;

    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @GetMapping
    public String listTodos(Model model, @ModelAttribute("message") String message) {
        model.addAttribute("todos", todoRepository.findAll());
        model.addAttribute("todo", new Todo());
        return "index";
    }

    @PostMapping("/add")
    public String addTodo(@Valid @ModelAttribute("todo") Todo todo,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "index";
        }

        todoRepository.save(todo);
        redirectAttributes.addFlashAttribute("message", "Thêm thành công");
        return "redirect:/";
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Todo> todoOpt = todoRepository.findById(id);

        if (todoOpt.isPresent()) {
            model.addAttribute("todo", todoOpt.get());
            model.addAttribute("todos", todoRepository.findAll());
            return "index";
        } else {
            model.addAttribute("message", "Không tìm thấy");
            model.addAttribute("todo", new Todo());
            model.addAttribute("todos", todoRepository.findAll());
            return "index";
        }
    }

    @PostMapping("/update")
    public String updateTodo(@Valid @ModelAttribute("todo") Todo todo,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "index";
        }

        todoRepository.save(todo);   // Hibernate tự update vì có id
        redirectAttributes.addFlashAttribute("message", "Cập nhật công việc thành công!");
        return "redirect:/";
    }


    @GetMapping("/delete/{id}")
    public String deleteTodo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            todoRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Xóa công việc thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Xóa thất bại! Công việc không tồn tại.");
        }
        return "redirect:/";
    }
}