package sg.edu.nus.iss.vttp5_day19l_pracws.controller;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import sg.edu.nus.iss.vttp5_day19l_pracws.model.Todo;
import sg.edu.nus.iss.vttp5_day19l_pracws.service.TodoListService;

@Controller
@RequestMapping("/list-todos")
public class TodoListController {

    @Autowired
    private TodoListService todoService;

    // Display the list of Todos for the logged-in user.
    @GetMapping("/list")
    public String listTodos(@RequestParam(value = "status", required = false) String status,
                            Model model,
                            HttpSession session) throws ParseException {
        String userName = (String) session.getAttribute("userName"); // Retrieve logged-in user's name.

        if (userName == null) {
            return "redirect:/refused"; // If not logged in, redirect to refused page.
        }

        List<Todo> todos = todoService.getAllTodos(userName); // Get all todos for the user.

        // Filter todos by status if provided
        if (status != null && !status.isEmpty()) {
            todos = todos.stream()
                    .filter(todo -> todo.getStatus().equalsIgnoreCase(status))
                    .collect(Collectors.toList());
        }

        model.addAttribute("todos", todos); // Add the todos to the model to display in the view.
        return "listing";
    }

    // Show the form to add a new Todo.
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("todo", new Todo()); // Create a blank `Todo` object for the form.
        return "add";
    }

    // Handle form submission for adding a new Todo.
    @PostMapping("/add")
    public String addTodo(@ModelAttribute("todo") Todo todo, HttpSession session) throws ParseException {
        String userName = (String) session.getAttribute("userName");

        if (userName == null) {
            return "redirect:/refused"; // Redirect if not logged in.
        }

        todoService.addTodo(userName, todo); // Add the new todo to the user's list.
        return "redirect:/todos/list"; // Redirect back to the list view.
    }

    // Show the form to update an existing Todo.
    @GetMapping("/update")
    public String showUpdateForm(@RequestParam String id, Model model, HttpSession session) throws ParseException {
        String userName = (String) session.getAttribute("userName");

        if (userName == null) {
            return "redirect:/refused"; // Redirect if not logged in.
        }

        List<Todo> todos = todoService.getAllTodos(userName); // Get all todos for the user.
        Todo todoToUpdate = todos.stream()
                .filter(todo -> todo.getId().equals(id))
                .findFirst()
                .orElse(null); // Find the todo with the matching ID.

        model.addAttribute("todo", todoToUpdate); // Add the `Todo` object to the model.
        return "update";
    }

    // Handle form submission for updating an existing Todo.
    @PostMapping("/update")
    public String updateTodo(@ModelAttribute("todo") Todo todo, HttpSession session) throws ParseException {
        String userName = (String) session.getAttribute("userName");

        if (userName == null) {
            return "redirect:/refused"; // Redirect if not logged in.
        }

        List<Todo> todos = todoService.getAllTodos(userName); // Get all todos for the user.
        Todo oldTodo = todos.stream()
                .filter(t -> t.getId().equals(todo.getId()))
                .findFirst()
                .orElse(null); // Find the existing todo to replace.

        if (oldTodo != null) {
            todoService.updateTodo(userName, oldTodo, todo); // Update the todo.
        }

        return "redirect:/todos/list"; // Redirect back to the list view.
    }

    // Handle deleting a Todo.
    @PostMapping("/delete")
    public String deleteTodo(@RequestParam String id, HttpSession session) throws ParseException {
        String userName = (String) session.getAttribute("userName");

        if (userName == null) {
            return "redirect:/refused"; // Redirect if not logged in.
        }

        List<Todo> todos = todoService.getAllTodos(userName); // Get all todos for the user.
        Todo todoToDelete = todos.stream()
                .filter(todo -> todo.getId().equals(id))
                .findFirst()
                .orElse(null); // Find the todo with the matching ID.

        if (todoToDelete != null) {
            todoService.deleteTodo(userName, todoToDelete); // Delete the todo.
        }

        return "redirect:/todos/list"; // Redirect back to the list view.
    }
}
