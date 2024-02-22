package org.example;

import lombok.Getter;
import lombok.Setter;
import org.example.schema.Manufacture;
import org.example.schema.Souvenir;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
public final class ManufactureList {
    private static ManufactureList manInstance;
    private List<Manufacture> manufactureList;

    private ManufactureList(List<Manufacture> manufactureList) {
        this.manufactureList = manufactureList;
    }

    private ManufactureList() {
        manufactureList = new ArrayList<>();
    }

    public static ManufactureList getInstance(List<Manufacture> list) {
        if (manInstance == null) {
            manInstance = new ManufactureList(list);
        }
        return manInstance;
    }

    public static ManufactureList getInstance() {
        if (manInstance == null) {
            manInstance = new ManufactureList();
        }
        return manInstance;
    }

    public boolean contains(Manufacture manufacture) {
        return manufactureList.contains(manufacture);
    }

    public void update(Manufacture newMan) {
        int index = -1;
        for (int i = 0; i < manufactureList.size(); i++) {
            if (manufactureList.get(i).getManufacture_id() == newMan.getManufacture_id()) {
                index = i;
                break;
            }
        }
        if (index!=-1) {
            manufactureList.set(index, newMan);
        }
    }

    public void add(Manufacture manufacture) {
        manufactureList.add(manufacture);
    }

    public List<Long> getAllManufactureID() {
        return manufactureList.stream()
                .map(Manufacture::getManufacture_id)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (Manufacture m : manufactureList) {
            res.append(m.toString());
            res.append("\n");
        }
        res.deleteCharAt(res.length() - 1);
        return res.toString();
    }

    public Manufacture getManufactureByID(long id) {
        for (Manufacture m : manufactureList) {
            if (m.getManufacture_id() == id) {
                return m;
            }
        }
        return null;
    }

    public void delete(Manufacture manufacture) {
        manufactureList.remove(manufacture);
    }

    public List<Manufacture> getManufacturesPriceDesc(double price, SouvenirList souvenirList) {
        List<Long> manufacturesWithLessPrice = souvenirList.getSouvenirList().stream()
                .filter(s -> s.getPrice() < price)
                .map(Souvenir::getManufacture_id).toList();

        return manufactureList.stream()
                .filter(m -> manufacturesWithLessPrice.contains(m.getManufacture_id()))
                .distinct()
                .collect(Collectors.toList());

    }

    public Map<Manufacture,  List<Souvenir>> getAllSouvenirsAndManufactures(SouvenirList souvenirList) {

        //without streams
        //        Map<Manufacture, List<Souvenir>> res = new HashMap<>();
//
//        for (Manufacture m : manufactureList){
//            res.put(m, new ArrayList<>());
//        }
//
//        for (Souvenir s : souvenirList.getSouvenirList()){
//            Manufacture m = getManufactureByID(s.getManufacture_id());
//            res.get(m).add(s);
//        }
        return manufactureList.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        m -> souvenirList.getSouvenirList().stream()
                                .filter(s -> s.getManufacture_id() == m.getManufacture_id())
                                .collect(Collectors.toList())
                ));
    }

    public List<Manufacture> getManufacturesBySouvenirAndYear(String name, int year, SouvenirList souvenirList) {

        List<Long> manufacturesID = souvenirList.getSouvenirList().stream()
                .filter(s -> s.getName().equalsIgnoreCase(name) && s.getDate_of_manufacture().getYear() == year)
                .map(Souvenir::getManufacture_id)
                .toList();

        return manufactureList.stream()
                .filter(m -> manufacturesID.contains(m.getManufacture_id()))
                .collect(Collectors.toList());
    }
}
