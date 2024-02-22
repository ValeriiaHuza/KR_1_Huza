package org.example.schema;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Manufacture {

    private long manufacture_id;
    private String name;
    private String country;

    public Manufacture(String name, String country) {
        this.name = name;
        this.country = country;

        long prevID = readIDFromFile();

        this.manufacture_id = prevID + 1;
    }

    private long readIDFromFile() {
        try (BufferedReader reader = Files.newBufferedReader(Path.of("manufacture_id.txt"), StandardCharsets.UTF_8)) {
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
        Manufacture other = (Manufacture) obj;
        return Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return
                "Manufacture_id=" + manufacture_id +
                        ", name='" + name + '\'' +
                        ", country='" + country + '\'';
    }
}
