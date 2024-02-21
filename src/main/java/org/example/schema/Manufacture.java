package org.example.schema;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Manufacture {

    private String manufacture_id;
    private String name;
    private String country;

}
