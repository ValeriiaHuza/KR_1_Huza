package org.example;

import org.example.controller.ManufactureController;
import org.example.controller.SouvenirController;
import org.example.schema.Manufacture;
import org.example.schema.Souvenir;

import java.io.IOException;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) throws IOException {

        Manufacture m1 = new Manufacture("Farm1","Ukraine");

        ManufactureController manCont = new ManufactureController();

        manCont.saveManufacture(m1);

        Souvenir s1 = new Souvenir("Green Apple",1,LocalDate.of(2001,10,1),22.0);

        SouvenirController cont = new SouvenirController(manCont);

        cont.saveSouvenir(s1);

        Souvenir s2 = new Souvenir("Apple", 2, LocalDate.of(20013,05,2),25.5);

        cont.saveSouvenir(s2);
    }
}