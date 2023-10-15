package dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class Resident implements Comparable<Resident>, Serializable {

    private String pesel;
    private String name;
    private String surname;
    private int age;
    private int flatNumber;
    private ApartmentBlock apartment;

    public DTO createDTO() {
        return new DTO(this);
    }

    @Override
    public String toString() {
        return "Name: " + name + ", "
                + "Surname: "+ surname + ", "
                + "Age: " + age;
    }

    @Override
    public int compareTo(Resident other) {
        int nameComparison = this.name.compareTo(other.getName());
        if (nameComparison != 0) {
            return nameComparison;
        }

        // If names are equal, compare by surname
        return this.surname.compareTo(other.getSurname());

    }
}
