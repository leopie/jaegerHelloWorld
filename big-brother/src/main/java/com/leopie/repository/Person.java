package com.leopie.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "People")
public class Person {
    @Id
    private String name;

    @Column(nullable = false)
    private String title = "";

    @Column(nullable = false)
    private String description = "";

    public Person() {}

    public Person(String name) {
        this.name = name;
    }

    public Person(String name, String title, String description) {
        this.name = name;
        this.title = title;
        this.description = description;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "{\"name\":\"%s\",\"title\":\"%s\",\"description\":\"%s\"}".formatted(this.name, this.title, this.description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return name.equals(person.name) && Objects.equals(title, person.title) && Objects.equals(description, person.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, title, description);
    }
}
