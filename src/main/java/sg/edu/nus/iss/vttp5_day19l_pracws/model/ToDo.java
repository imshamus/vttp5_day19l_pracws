package sg.edu.nus.iss.vttp5_day19l_pracws.model;

import java.util.Date;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Todo {

    // @Max(value = 50, message = "Max length is 50.") // max is for numeric not for string
    // id is generated w UUID which is always a 36-character String, hence fulfilling the requirement of being under 50 characters.
    private String id;

    @NotEmpty(message = "Name must not be empty.")
    @Size(min = 10, max = 50, message = "Name must be 10 to 50 characters long.")
    private String name;

    @NotEmpty(message = "Description must not be empty.")
    @Size(max = 255, message = "Description must be less than 255 characters long.")
    private String description;

    @NotNull(message = "Date must not be null.")
    @FutureOrPresent(message = "Date must be equals to or greater than today.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dueDate;

    @NotEmpty(message = "Priority level must not be empty.")
    @Pattern(regexp = "low|medium|high", message = "Priority must be low, medium, or high.")
    private String priorityLevel;

    @NotEmpty(message = "Status must not be empty.")
    @Pattern(regexp = "Pending|Started|Progress|Completed", message = "Status must be Pending, Started, Progress, or Completed.")
    private String status;

    @NotNull(message = "Date must not be null.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    @NotNull(message = "Date must not be null.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;
    
    // CONSTRUCTORS
    // Default Constructor for Todo Obj
    public Todo() {
        this.id = UUID.randomUUID().toString(); // generate Random ID
        
        Date current = new Date(System.currentTimeMillis()); // returns the current time in epoch milliseconds
        // new Date(1733565600000) would represent December 7, 2024, 10:00:00 AM UTC.
        // you could simply use this.createdAt = new Date(); and it would work the same way
        // System.currentTimeMillis() makes the intent explicit to show date is based on current system time.

        this.createdAt = current;
        this.updatedAt = current;
    }

    // Constructor for updating or rebuilding Todo object from extracted data stored in Redis
    public Todo(String id, String name, String description, Date dueDate, String priorityLevel, String status, Date createdAt, Date updatedAt) 
    {
        this.id = id; 
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.priorityLevel = priorityLevel;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Constructor for creating new Todo object
    public Todo(String name, String description, Date dueDate, String priorityLevel, String status) 
    {
        this.id = UUID.randomUUID().toString(); // generate Random ID
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.priorityLevel = priorityLevel;
        this.status = status;

        Date current = new Date(System.currentTimeMillis()); // returns the current time in epoch milliseconds
        // makes it clear that we are basing Date on epoch millisecs
        // helpful for working with databases like Redis which often store dates as epoch millisecs
        this.createdAt = current;
        this.updatedAt = current;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(String priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return id + "," + name + "," + description + "," + dueDate
                + "," + priorityLevel + "," + status + "," + createdAt
                + "," + updatedAt;
    }

    
    
}
