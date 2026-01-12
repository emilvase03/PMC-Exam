package dk.easv.pmcexam.BE;

public class Genre {

    private int id = -1;
    private String name;

    public Genre(String name) {
        setName(name);
    }

    public Genre(int id, String name) {
        setId(id);
        setName(name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id != -1) {
            this.id = id;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
