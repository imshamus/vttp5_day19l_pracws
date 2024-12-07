package sg.edu.nus.iss.vttp5_day19l_pracws.model;

import java.util.Date;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ToDo {

    // @Max(value = 50, message = "Max length is 50.")
    private String id;

    @NotEmpty(message = "Name must not be empty.")
    @Size(min = 10, max = 50, message = "Name must be 10 to 50 characters long.")
    private String name;

    @NotEmpty(message = "Name must not be empty.")
    @Max(value = 255, message = "Description must be less than 255 characters long.")
    private String description;

    @NotNull(message = "Date must not be null.")
    @FutureOrPresent(message = "Date must be equals to or greater than today.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dueDate;

    @NotEmpty(message = "Name must not be empty.")
    private String priorityLevel;

    @NotEmpty(message = "Name must not be empty.")
    private String status;

    @NotNull(message = "Date must not be null.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    @NotNull(message = "Date must not be null.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;
    
    public ToDo() {
        this.id = UUID.randomUUID().toString(); // generate Random ID
        this.createdAt = new Date(System.currentTimeMillis());
        this.updatedAt = new Date(System.currentTimeMillis());
    }

    public ToDo(String name, String description, Date dueDate, String priorityLevel, String status, Date createdAt, Date updatedAt) 
    {
        this.id = UUID.randomUUID().toString(); // generate Random ID
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.priorityLevel = priorityLevel;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public ToDo(String name, String description, Date dueDate, String priorityLevel, String status) 
    {
        this.id = UUID.randomUUID().toString(); // generate Random ID
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.priorityLevel = priorityLevel;
        this.status = status;
        this.createdAt = new Date(System.currentTimeMillis());
        this.updatedAt = new Date(System.currentTimeMillis());
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
