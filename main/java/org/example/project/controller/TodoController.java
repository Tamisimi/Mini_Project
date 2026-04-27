package org.example.project.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.project.model.Todo;
import org.example.project.repository.TodoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class TodoController {

    private final TodoRepository todoRepository;

    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }


    @GetMapping("/welcome")
    public String showWelcomePage() {
        return "welcome";
    }

    @PostMapping("/setOwner")
    public String setOwnerName(@RequestParam String ownerName,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        if (ownerName == null || ownerName.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Tên không được để trống!");
            return "redirect:/welcome";
        }

        session.setAttribute("ownerName", ownerName.trim());
        return "redirect:/todos";
    }


    @GetMapping("/todos")
    public String listTodos(Model model, HttpSession session, RedirectAttributes redirectAttributes) {

        String ownerName = (String) session.getAttribute("ownerName");
        if (ownerName == null || ownerName.trim().isEmpty()) {
            return "redirect:/welcome";
        }

        model.addAttribute("todos", todoRepository.findAll());
        model.addAttribute("todo", new Todo());
        model.addAttribute("ownerName", ownerName);

        return "index";
    }


    @PostMapping("/add")
    public String addTodo(@Valid @ModelAttribute("todo") Todo todo,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          HttpSession session) {

        if (session.getAttribute("ownerName") == null) {
            return "redirect:/welcome";
        }

        if (bindingResult.hasErrors()) {
            return "index";
        }

        todoRepository.save(todo);
        redirectAttributes.addFlashAttribute("message", "Thêm công việc thành công!");
        return "redirect:/todos";
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id,
                               Model model,
                               HttpSession session) {

        if (session.getAttribute("ownerName") == null) {
            return "redirect:/welcome";
        }

        Optional<Todo> todoOpt = todoRepository.findById(id);

        if (todoOpt.isPresent()) {
            model.addAttribute("todo", todoOpt.get());
            model.addAttribute("todos", todoRepository.findAll());
            model.addAttribute("ownerName", session.getAttribute("ownerName"));
            return "index";
        } else {
            model.addAttribute("message", "Không tìm thấy công việc!");
            model.addAttribute("todo", new Todo());
            model.addAttribute("todos", todoRepository.findAll());
            model.addAttribute("ownerName", session.getAttribute("ownerName"));
            return "index";
        }
    }

    @PostMapping("/update")
    public String updateTodo(@Valid @ModelAttribute("todo") Todo todo,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             HttpSession session) {

        if (session.getAttribute("ownerName") == null) {
            return "redirect:/welcome";
        }

        if (bindingResult.hasErrors()) {
            return "index";
        }

        todoRepository.save(todo);
        redirectAttributes.addFlashAttribute("message", "Cập nhật công việc thành công!");
        return "redirect:/todos";
    }


    @GetMapping("/delete/{id}")
    public String deleteTodo(@PathVariable Long id,
                             RedirectAttributes redirectAttributes,
                             HttpSession session) {

        if (session.getAttribute("ownerName") == null) {
            return "redirect:/welcome";
        }

        try {
            todoRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Xóa công việc thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Xóa thất bại! Công việc không tồn tại.");
        }
        return "redirect:/todos";
    }
}