import java.io.Serializable;

public class WorkoutTask implements Serializable {
    private String details;

    public WorkoutTask(String details) {
        this.details = details;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}