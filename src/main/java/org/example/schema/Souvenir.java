package org.example.schema;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Souvenir implements Serializable {
    private long souvenir_id;
    private String name;
    private long manufacture_id;
    private LocalDate date_of_manufacture;
    private double price;

    public Souvenir(String name, long manufacture_id, LocalDate date_of_manufacture, double price) {
        this.name = name;
        this.manufacture_id = manufacture_id;
        this.date_of_manufacture = date_of_manufacture;
        this.price = price;

//        String combinedAttributes = name + manufacture_id;
//        UUID objectId = UUID.nameUUIDFromBytes(combinedAttributes.getBytes());

        long prevID = readIDFromFile();

        this.souvenir_id = prevID + 1;

    }

    private long readIDFromFile() {
        try (BufferedReader reader = Files.newBufferedReader(Path.of("souvenir_id.txt"), StandardCharsets.UTF_8)) {
            String line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                return Long.parseLong(line);
            } else {
                return 0;
            }
        } catch (IOException e) {
            return 0;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        Souvenir other = (Souvenir) obj;
        return name.equalsIgnoreCase(other.getName()) &&
                Objects.equals(manufacture_id, other.manufacture_id);
    }

    @Override
    public String toString() {
        return "Souvenir_id='" + souvenir_id + '\'' +
                ", name='" + name + '\'' +
                ", manufacture_id='" + manufacture_id + '\'' +
                ", date_of_manufacture=" + date_of_manufacture +
                ", price=" + price;
    }
}
