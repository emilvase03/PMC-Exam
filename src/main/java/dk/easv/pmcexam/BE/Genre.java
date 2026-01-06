package dk.easv.pmcexam.BE;

public class Genre {

    private int  id=-1;
    private String name;
public Genre(int id, String name){
    setId(id);
    setName(name);
}

    public int getId() {
        return id;
    }

    private void setId(int id) {
       if(id!=-1)
        this.id = id;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
       if(name != null && !name.isBlank())
        this.name = name;
    }




}
