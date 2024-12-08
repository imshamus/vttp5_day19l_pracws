package sg.edu.nus.iss.vttp5_day19l_pracws.controller;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sg.edu.nus.iss.vttp5_day19l_pracws.model.Todo;
import sg.edu.nus.iss.vttp5_day19l_pracws.service.TodoService;


@Controller
@RequestMapping("/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping("/list")
    public String ListTodos(@RequestParam(value = "status", required = false) String status, Model model) throws ParseException // when status is not provided as not required, status is null
    {
        List<Todo> todos = todoService.getAllTodos();

        // Filter todos by status if provided
        if (status != null && !status.isEmpty())
        {
            todos = todos.stream().filter(todo -> todo.getStatus().equalsIgnoreCase(status))
            .collect(Collectors.toList()); // check that status is equals to the status parse in (e.g. pending) but ignores whether upper or lowercase like pEnDinG.
            // collect() terminates the stream pipeline and converts the processed stream back into List<Todo>
            // Collectors.toList tells collect() to gather the elements into a new List<Todo>, converts the stream back into a collection
        }

        // add todos to the model
        model.addAttribute("todos", todos);

        return "listing";
    }
    
    
}
