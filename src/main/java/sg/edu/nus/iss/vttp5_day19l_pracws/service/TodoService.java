package sg.edu.nus.iss.vttp5_day19l_pracws.service;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.vttp5_day19l_pracws.constant.Constant;
import sg.edu.nus.iss.vttp5_day19l_pracws.model.Todo;
import sg.edu.nus.iss.vttp5_day19l_pracws.repository.MapRepo;

@Service
public class TodoService {

    private static final Logger logger = LoggerFactory.getLogger(TodoService.class);

    @Autowired
    private MapRepo mapRepo;

    public List<Todo> getAllTodos() throws ParseException
    {
        Map<Object, Object> todosObject = mapRepo.getAll(Constant.todoKey);

        List<Todo> todos = new ArrayList<>();

        // Map methods
        // map.ketSet() returns all keys in the map
        // map.values() returns all the values in the map
        // map.entrySet() returns both keys and values together

        // Entry<Object, Object> represents a key-value pair in the Map, entry.getKey = f1, entry .getValue = v1 or the JSON String in this case

        // entrySet() returns a Set of Map.Entry objects. Each entry represents one key-value pair
        // entrySet() is used to iterate over all key-value pairs in the Map

        for(Entry<Object, Object> entry : todosObject.entrySet()) 
        {
            String value = entry.getValue().toString(); // value = JSON String

            // StringReader converts JSON String into a readable stream for JsonReader
            JsonReader jsonReader = Json.createReader(new StringReader(value)); 

            // Parse the JSON String and convert ito a JsonObject which is java rep of JSON data
            JsonObject jsonObject = jsonReader.readObject();


            // Standardised Deserialisation (Reading Data): When retrieving and parsing data from Redis, use format:

            // Parses the String Date (e.g., "Sun, 10/22/2024") into a Java Date object.
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, MM/dd/yyyy"); // Matches the date format stored in Redis (e.g., "Sun, 10/22/2024")

            Date dueDate = sdf.parse(jsonObject.getString("due_date"));
            logger.debug("Raw Redis date string: {}", jsonObject.getString("due_date"));

            Date createdAt = sdf.parse(jsonObject.getString("created_at"));
            logger.debug("Raw Redis date string: {}", jsonObject.getString("created_at"));

            Date updatedAt = sdf.parse(jsonObject.getString("updated_at"));
            logger.debug("Raw Redis date string: {}", jsonObject.getString("updated_at"));



            Todo todo = new Todo();

            todo.setId(jsonObject.getString("id"));
            todo.setName(jsonObject.getString("name"));
            todo.setDescription(jsonObject.getString("description"));
            todo.setDueDate(dueDate);
            todo.setPriorityLevel(jsonObject.getString("priority_level"));
            todo.setStatus(jsonObject.getString("status"));
            todo.setCreatedAt(createdAt);
            todo.setUpdatedAt(updatedAt);

            todos.add(todo);
        }

        return todos;
    }
    
    public void addTodo(Todo todo) throws ParseException
    {
        // In date, parsing converts a String into a Date
        // formating does the opposite, converts Date into String
        // default .toString() method on Date object uses the system default format which is ("EEE MMM dd HH:mm:ss zz yyyy")

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MM/dd/yyyy"); // Matches the date format stored in Redis (e.g., "Sun, 10/22/2024")

        // Standardize Serialization (Saving Data): When adding a new Todo to Redis, use SimpleDateFormat to format the date:
        String dueDateString = sdf.format(todo.getDueDate());
        String createdAtString = sdf.format(todo.getCreatedAt());
        String updatedAtString = sdf.format(todo.getUpdatedAt());        

        // Serialised the todo object using JSON-P to JsonObject
        JsonObject jsonTodo = Json.createObjectBuilder()
        .add("id", todo.getId())
        .add("name", todo.getName())
        .add("description", todo.getDescription())
        .add("due_date", dueDateString)
        .add("priority_level", todo.getPriorityLevel())
        .add("status", todo.getStatus())
        .add("created_at", createdAtString)
        .add("updated_at", updatedAtString)
        .build(); // naming must be same as JSON structure in the doc
    
        // Save the JsonObject as string into redis map: Key named "todos"
        mapRepo.put(Constant.todoKey, todo.getId().toString(), jsonTodo.toString());
    }
}
