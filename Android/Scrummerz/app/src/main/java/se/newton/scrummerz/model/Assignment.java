package se.newton.scrummerz.model;

public class Assignment {
    private Long duedate;
    private String info;

    public Assignment(){
    }

    public Assignment(String info, Long duedate ) {
        this.duedate = duedate;
        this.info = info;
    }

    public Long getDuedate() {
        return duedate;
    }

    public String getInfo() {
        return info;
    }
}
