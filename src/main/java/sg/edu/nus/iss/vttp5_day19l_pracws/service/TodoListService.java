package sg.edu.nus.iss.vttp5_day19l_pracws.service;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.vttp5_day19l_pracws.model.Todo;
import sg.edu.nus.iss.vttp5_day19l_pracws.repository.ListRepo;

@Service
public class TodoListService {

    @Autowired
    private ListRepo listRepo;

    // Helper Method: Serialize a `Todo` object into a JSON String for storing in Redis.
    private String serializeTodo(Todo todo) {
        return Json.createObjectBuilder()
                .add("id", todo.getId())
                .add("name", todo.getName())
                .add("description", todo.getDescription())
                .add("due_date", todo.getDueDate().toString())
                .add("priority_level", todo.getPriorityLevel())
                .add("status", todo.getStatus())
                .add("created_at", todo.getCreatedAt().toString())
                .add("updated_at", todo.getUpdatedAt().toString())
                .build()
                .toString();
    }

    // Helper Method: Deserialize a JSON String from Redis into a `Todo` object.
    private Todo deserializeTodo(String todoJson) throws ParseException {
        JsonReader reader = Json.createReader(new StringReader(todoJson));
        JsonObject jsonObject = reader.readObject();

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MM/dd/yyyy");

        return new Todo(
                jsonObject.getString("id"),
                jsonObject.getString("name"),
                jsonObject.getString("description"),
                sdf.parse(jsonObject.getString("due_date")),
                jsonObject.getString("priority_level"),
                jsonObject.getString("status"),
                sdf.parse(jsonObject.getString("created_at")),
                sdf.parse(jsonObject.getString("updated_at"))
        );
    }

    // Retrieve all todos for a specific user from Redis.
    public List<Todo> getAllTodos(String userName) throws ParseException {
        String redisKey = "todos:" + userName; // Key structure: todos:<userName>
        List<String> todoStrings = listRepo.getList(redisKey); // Retrieve all JSON strings from Redis list.

        List<Todo> todos = new ArrayList<>();
        for (String todoJson : todoStrings) {
            todos.add(deserializeTodo(todoJson)); // Convert each JSON string back to a `Todo` object.
        }

        return todos; // Return the list of `Todo` objects.
    }

    // Add a new Todo for a specific user.
    public void addTodo(String userName, Todo todo) {
        String redisKey = "todos:" + userName; // Key structure: todos:<userName>
        String todoJson = serializeTodo(todo); // Convert `Todo` object to JSON string.
        listRepo.rightPush(redisKey, todoJson); // Add the new `Todo` JSON to the end of the list.
    }

    // Delete a specific Todo for a user.
    public void deleteTodo(String userName, Todo todo) {
        String redisKey = "todos:" + userName; // Key structure: todos:<userName>
        String todoJson = serializeTodo(todo); // Convert `Todo` object to JSON string.
        listRepo.remove(redisKey, 1, todoJson); // Remove one occurrence of the matching `Todo` JSON.
    }

    // Update an existing Todo for a user by replacing the old version with the updated one.
    public void updateTodo(String userName, Todo oldTodo, Todo updatedTodo) {
        String redisKey = "todos:" + userName; // Key structure: todos:<userName>
        String oldTodoJson = serializeTodo(oldTodo); // Convert old `Todo` to JSON string.
        String updatedTodoJson = serializeTodo(updatedTodo); // Convert updated `Todo` to JSON string.

        // Remove the old Todo
        listRepo.remove(redisKey, 1, oldTodoJson);

        // Add the updated Todo
        listRepo.rightPush(redisKey, updatedTodoJson);
    }
}
