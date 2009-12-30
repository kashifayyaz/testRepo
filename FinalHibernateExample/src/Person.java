/**
 *
 * @author kashif
 */
import javax.persistence.Entity;
import javax.persistence.Id;
import org.hibernate.search.annotations.Field;
import java.io.Serializable;
import org.hibernate.search.annotations.Indexed;

@Indexed
public class Person implements Serializable {
    @Id
    private int id;
    @Field
    private String name;

    protected Person() {
    }

    public Person(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
