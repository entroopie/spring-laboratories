package dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class DTO implements Comparable<DTO> {

    private String pesel;
    private String name;
    private String surname;
    private int age;
    private int flatNumber;
    private int blockId;

    public DTO(Resident res) {
        this.pesel = res.getPesel();
        this.name = res.getName();
        this.surname = res.getSurname();
        this.age = res.getAge();
        this.flatNumber = res.getFlatNumber();
        this.blockId = res.getApartment().getBlockId();
    }

    @Override
    public String toString() {
        return "Name: " + name + ", "
                + "Surname: "+ surname + ", "
                + "Age: " + age + ", "
                + "block ID: " + blockId;
    }

    @Override
    public int compareTo(DTO other) {
        int nameComparison = this.name.compareTo(other.getName());
        if (nameComparison != 0) {
            return nameComparison;
        }

        // If names are equal, compare by surname
        return this.surname.compareTo(other.getSurname());
    }
}
