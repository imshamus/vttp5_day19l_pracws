package sg.edu.nus.iss.vttp5_day19l_pracws;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.vttp5_day19l_pracws.constant.Constant;
import sg.edu.nus.iss.vttp5_day19l_pracws.repository.MapRepo;

@SpringBootApplication
public class Vttp5Day19lPracwsApplication implements CommandLineRunner{

	@Autowired
	private MapRepo mapRepo;

	@Autowired
	private ResourceLoader resourceLoader;

	public static void main(String[] args) {
		SpringApplication.run(Vttp5Day19lPracwsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// STEP 1: Read the file from resources (but not portable)
		// Path p = Paths.get("src/main/resources/static/todos.txt"); 
		

		// For portability can use ClassLoader, ResourceLoader, @Value annotation


		// option 1: Using ClassLoader
		// InputStream is = getClass().getClassLoader().getResourceAsStream("data/todos.txt");

		// if (is == null) {
        //     throw new RuntimeException("File not found: data/todos.txt");
        // }


		// option 2: Using ResourceLoader
		Resource resource = resourceLoader.getResource("classpath:data/todos.txt");

		try(InputStream is = resource.getInputStream())
		{
			// Step 2: Parse the file content using JSON-P
			JsonReader jsonReader = Json.createReader(is);
			JsonArray todosArray = jsonReader.readArray();

			// Step 3: Store the data in Redis using MapRepo
			for (JsonObject todo : todosArray.getValuesAs(JsonObject.class)) {
				String id = todo.getString("id");
				mapRepo.create(Constant.todoKey, id, todo.toString());
			}
		}

		// Log the result
		System.out.println("Todos have been loaded into Redis.");

		// Put the data into Redis Map
	}
}
