package sg.edu.nus.iss.vttp5_day19l_pracws;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(Vttp5Day19lPracwsApplication.class);


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
		logger.info("Starting to load todos from todos.txt...");
		Resource resource = resourceLoader.getResource("classpath:data/todos.txt"); // file is now a resource obj, not read yet, just referenced
		
		// opens the file as a stream of bytes, allows us to process the file byte-by-byte or convert to other formats like JSON
		try(InputStream is = resource.getInputStream())
		{
			// Step 2: Parse the file content using JSON-P
			
			// prepatory step, initiliases JsonReader w InputStream as its input. Doesn't process immediately, jsut a setup step that tells the library, "I'll process this stream as JSON soon."
			JsonReader jsonReader = Json.createReader(is); 
			
			// Specifying how to interpret the JSON content (in this case, as a JSON array)
			JsonArray todosArray = jsonReader.readArray();


			// Check if the array is empty
            if (todosArray.isEmpty()) {
                // System.out.println("No todos found in the file.");
				logger.warn("No todos found in the file.");
                return;
            }

			

			// Step 3: Store the data in Redis using MapRepo
			for (JsonObject todo : todosArray.getValuesAs(JsonObject.class)) // getValuesAs converts each element in the JsonArray into a JsonObject
			{
				String id = todo.getString("id");
				


				// Check if the key already exists in Redis
                if (mapRepo.get(Constant.todoKey, id) != null) 
				{
					// System.out.println("Todo with ID " + id + " already exists. Skipping.");
                    logger.info("Todo with ID {} already exists. Skipping.", id);
					
                    continue;
                }


				// Add to Redis if it doesn't exist
				mapRepo.put(Constant.todoKey, id, todo.toString());
				// System.out.println("Added todo with ID " + id + " to Redis.");
				logger.debug("Added todo with ID {} to Redis.", id);


			}

			// Log success
			// System.out.println("Todos have been loaded into Redis.");
			logger.info("Todos have been successfully loaded into Redis.");
		} 
		catch(Exception e)
		{
			// Log the error and prevent the application from crashing
			// System.err.println("Error occured while processing todos.txt: " + e.getMessage());
			// e.printStackTrace();

			logger.error("Error occurred while processing todos.txt: {}", e.getMessage(), e);
		}
	}
}
