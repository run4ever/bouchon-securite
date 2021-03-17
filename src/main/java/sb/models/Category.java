package sb.models;

public class Category {
    private long id;
    private String label;

    public Category(long id, String content) {
        this.id = id;
        this.label = content;
    }

    public Category() {
        this.id = 0;
        this.label = "";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
       this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String content) {
         this.label = content;
    } 
}
