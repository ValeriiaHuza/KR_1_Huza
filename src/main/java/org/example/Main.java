package org.example;

import org.example.controller.SouvenirController;
import org.example.schema.Souvenir;

import java.io.IOException;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) throws IOException {

        Souvenir s1 = new Souvenir("Green Apple","Farm1",LocalDate.of(2001,10,1),22.0);

        SouvenirController cont = new SouvenirController();

        cont.saveSouvenir(s1);

        Souvenir s2 = new Souvenir(3,"Apple","Farm22",LocalDate.of(2005,11,1),25.0);

        cont.updateSouvenir(s2);

        cont.deleteSouvenir(s2);

        Souvenir s3 = new Souvenir("Apple","Farm22",LocalDate.of(2005,11,1),25.0);

        cont.saveSouvenir(s3);

        System.out.println(cont.getSouvenirList().getSouvenirList().size());

        cont.deleteSouvenir(new Souvenir(4,"Apple","Farm22",LocalDate.of(2005,11,1),25.0));

        System.out.println(cont.getSouvenirList().getSouvenirList().size());
    }
}