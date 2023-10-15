package dto;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode(exclude = "residents")
public class ApartmentBlock implements Serializable {

    private int blockId;
    private String blockName;
    private List<Resident> residents;

    public void addResident(Resident resident) {
        if (residents == null) {
            residents = new ArrayList<>();
        }
        residents.add(resident);
    }

    @Override
    public String toString() {
        return "blockId: " + blockId + "\n" +
                "blockName: " + blockName + "\n";
    }

}
