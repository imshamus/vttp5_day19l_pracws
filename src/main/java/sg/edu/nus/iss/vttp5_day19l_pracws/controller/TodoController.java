package sg.edu.nus.iss.vttp5_day19l_pracws.controller;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import sg.edu.nus.iss.vttp5_day19l_pracws.model.Todo;
import sg.edu.nus.iss.vttp5_day19l_pracws.model.User;
import sg.edu.nus.iss.vttp5_day19l_pracws.service.TodoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequestMapping("/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    // @GetMapping("/list")
    // public String ListTodos(@RequestParam(value = "status", required = false) String status, Model model) throws ParseException 
    // // "status" is used for filtering
    // // when status is not provided as not required, status is null
    // {

    //     // BEFORE SESSION 
    //     List<Todo> todos = todoService.getAllTodos();

    //     // Filter todos by status if provided
    //     if (status != null && !status.isEmpty())
    //     {
    //         todos = todos.stream().filter(todo -> todo.getStatus().equalsIgnoreCase(status))
    //         .collect(Collectors.toList()); 
    //         // check that status is equals to the status parse in (e.g. pending) but ignores whether upper or lowercase like pEnDinG.
    //         // collect() terminates the stream pipeline and converts the processed stream back into List<Todo>
    //         // Collectors.toList tells collect() to gather the elements into a new List<Todo>, converts the stream back into a collection
    //     }

    //     // add todos to the model
    //     model.addAttribute("todos", todos);

    //     return "listing";
    // }
    
    // @GetMapping("/add")
    // public String showAddForm(Model model) {
    //     model.addAttribute("todo", new Todo()); // Invokes the default constructor - random id, current time as created & updated.
    //     // other attributes are null for strings and 0 for numbers
    //     return "add";
    // }

    // @PostMapping("/add")
    // public String postAddForm(@Valid @ModelAttribute("todo") Todo todo, BindingResult bindingResult) throws ParseException {
        
    //     if (bindingResult.hasErrors())
    //     {
    //         bindingResult.getAllErrors();
    //         return "add";
    //     }

    //     todoService.addTodo(todo);

    //     return "redirect:/todos/list";
    // }

     // SESSION IMPLEMENTATION (Task 6)
    @GetMapping("/list")
    public String listTodos(@RequestParam(value = "status", required = false) String status,
                            Model model,
                            HttpSession session) throws ParseException {
        // Get the user data from the session
        String userName = (String) session.getAttribute("userName");
        Integer userAge = (Integer) session.getAttribute("userAge");
        // "userName" & "userAge" value 's is null before initialised
        // cast to String as the getAttribute method returns an Object

        if (userName == null || userAge == null) {
            // Redirect to refused if no session exists > refused has login
            return "redirect:/refused";
        }

        // Retrieve todos for this user
        List<Todo> todos = todoService.getAllTodos(userName);

        // Filter todos by status if provided
        if (status != null && !status.isEmpty()) {
            todos = todos.stream()
                    .filter(todo -> todo.getStatus().equalsIgnoreCase(status))
                    .collect(Collectors.toList());
        }

        // Add todos and username to the model
        model.addAttribute("todos", todos);
        // Add username and age for "Welcome" message
        model.addAttribute("user", new User(userName, userAge)); 

        return "listing";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("todo", new Todo());
        return "add";
    }

    @PostMapping("/add")
    public String postAddForm(@Valid @ModelAttribute("todo") Todo todo,
                              BindingResult bindingResult,
                              HttpSession session) throws ParseException 
    {
        // Retrieve user name from session
        String userName = (String) session.getAttribute("userName");

        if (userName == null) {
            return "redirect:/refused";
        }

        if (bindingResult.hasErrors()) {
            return "add";
        }

        // Add todo to the user-specific Redis data
        todoService.addTodo(userName, todo);

        return "redirect:/todos/list";
    }

    @GetMapping("/delete")
    public String delGetMethod(@RequestParam String id, HttpSession session) 
    // requested an id
    // /todos/delete?id=123, this id was mapped from the brackets, (id=${todo.id}), below
    // <a th:href="@{/todos/delete(id=${todo.id})}" class="btn btn-danger">Delete</a> 
    {
        // Retrieve user name from session
        String userName = (String) session.getAttribute("userName");

        if (userName == null) {
            return "redirect:/refused";
        }

        todoService.deleteTodo(userName, id);

        return "redirect:/todos/list";
    }

    @PostMapping("/delete")
    public String delPostMethod(@RequestParam String id, HttpSession session) 
    // <form method="post" th:action="@{/todos/delete}">
        // <input type="hidden" name="id" th:value="${todo.id}" />
        // <button type="submit" class="btn btn-danger">Delete</button>
    // </form>
    // requestparam's id is mapped to name = "id" = th:value, th:action is mapped to this method
    {
    String userName = (String) session.getAttribute("userName");
    
    if (userName == null) {
        return "redirect:/refused";
    }

    todoService.deleteTodo(userName, id);

    return "redirect:/todos/list";
    }


    @GetMapping("/update")
    // Requested an id
    // /todos/update?id=123
    public String showUpdateForm(@RequestParam String id, HttpSession session, Model model) throws ParseException 
    {
        // Retrieve user name from session
        String userName = (String) session.getAttribute("userName");

        if (userName == null) {
            return "redirect:/refused";
        }

        Todo todo = todoService.getTodoById(userName, id);
        model.addAttribute("todo", todo);

        return "update";
    }
    

    @PostMapping("/update")
    public String updateTodo(@Valid @ModelAttribute("todo") Todo todo,
                              BindingResult bindingResult,
                              HttpSession session) throws ParseException 
    {
        // Retrieve user name from session
        String userName = (String) session.getAttribute("userName");

        if (userName == null) {
            return "redirect:/refused";
        }

        if (bindingResult.hasErrors()) {
            return "update";
        }

        // Add todo to the user-specific Redis data
        todoService.updateTodo(userName, todo);

        return "redirect:/todos/list";
    }

    
    
}
