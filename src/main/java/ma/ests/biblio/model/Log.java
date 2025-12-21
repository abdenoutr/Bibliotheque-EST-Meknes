package ma.ests.biblio.model;

import java.time.LocalDateTime;

public class Log {

    private int id;
    private String action;
    private LocalDateTime dateAction;

    public Log() {
        this.dateAction = LocalDateTime.now();
    }

    public Log(String action) {
        this.action = action;
        this.dateAction = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDateTime getDateAction() {
        return dateAction;
    }
    public void setDateAction(LocalDateTime localDateTime) {
        this.dateAction = localDateTime;
    }
	
}