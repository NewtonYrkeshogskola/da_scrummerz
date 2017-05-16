package se.newton.scrummerz.model;


public class Courses {
    private String status;
    private String courseCode;
    private String description;
    private String name;
    private String teacher;

    public Courses() {
        // Default constructor
    }

    public Courses(String status, String courseCode, String description, String name, String teacher) {
        this.teacher = teacher;
        this.name = name;
        this.description = description;
        this.status = status;
        this.courseCode = courseCode;
    }

    public Courses(String courseCode, String name) {
        this.name = name;
        this.courseCode = courseCode;
    }

    public String getStatus() {
        return status;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getTeacher() {
        return teacher;
    }
}
