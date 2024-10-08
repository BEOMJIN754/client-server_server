import java.io.Serializable;

public class Information implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String id;
    protected String name; // Student: name, Course: professor

    public Information(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return id + " " + name;
    }
}
