package models;

import com.sun.istack.NotNull;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "field")
public class Field implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Integer id;

    @Column(name = "name", nullable = false)
    @NotNull
    private String name;

    @Column(name = "selector", nullable = false)
    @NotNull
    private String selector;

    @Column(name = "weight", nullable = false)
    @NotNull
    private Double weight;

    public Field(){}

    public Field(Integer id, String name, String selector, Double weight) {
        this.id = id;
        this.name = name;
        this.selector = selector;
        this.weight = weight;
    }

    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return id.equals(field.id) && name.equals(field.name) && selector.equals(field.selector) && weight.equals(field.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, selector, weight);
    }

    @Override
    public String toString() {
        return "Relevance{" +
                "id= " + id +
                ", name= '" + name + '\'' +
                ", selector=' " + selector + '\'' +
                ", weight= " + weight +
                '}';
    }


}
