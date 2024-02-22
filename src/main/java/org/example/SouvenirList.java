package org.example;

import lombok.Getter;
import lombok.Setter;
import org.example.schema.Manufacture;
import org.example.schema.Souvenir;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public final class SouvenirList {

    private static SouvenirList souvInstance;
    private List<Souvenir> souvenirList;

    private SouvenirList(List<Souvenir> list) {
        souvenirList = list;
    }

    public static SouvenirList getInstance(List<Souvenir> list) {
        if (souvInstance == null) {
            souvInstance = new SouvenirList(list);
        }
        return souvInstance;
    }

    public static SouvenirList getInstance() {
        if (souvInstance == null) {
            souvInstance = new SouvenirList();
        }
        return souvInstance;
    }

    public SouvenirList() {
        souvenirList = new ArrayList<>();
    }

    public boolean contains(Souvenir souvenir) {

//        for (Souvenir s : souvenirList){
//            //or check only id?
//            if (s.getName().equals(souvenir.getName()) && s.getManufacture_id().equals(souvenir.getManufacture_id())){
//                return true;
//            }
//        }

        return souvenirList.contains(souvenir);
    }

    public void update(Souvenir newSouvenir) {
        int index = -1;
        for (int i = 0; i < souvenirList.size(); i++) {
            if (souvenirList.get(i).getSouvenir_id() == newSouvenir.getSouvenir_id()) {
                index = i;
                break;
            }
        }
        souvenirList.set(index, newSouvenir);
    }

    public void delete(Souvenir souvenir) {
        souvenirList.remove(souvenir);
    }

    public void add(Souvenir souvenir) {
        souvenirList.add(souvenir);
    }

    public long getID(Souvenir s2) {

        for (Souvenir s : souvenirList) {
            if (s.equals(s2)) {
                return s.getSouvenir_id();
            }
        }

        return -1;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (Souvenir s : souvenirList) {
            res.append(s.toString());
            res.append("\n");
        }
        res.deleteCharAt(res.length() - 1);
        return res.toString();
    }

    public List<Long> getAllSouvenirsID() {
        return souvenirList.stream()
                .map(Souvenir::getSouvenir_id)
                .collect(Collectors.toList());
    }

    public Souvenir getSouvenirByID(long id) {
        return souvenirList.stream()
                .filter(s -> s.getSouvenir_id() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Souvenir> getSouvenirsByManufacture(long manufacture_id) {
        return souvenirList.stream()
                .filter(s -> s.getManufacture_id() == manufacture_id)
                .collect(Collectors.toList());
    }

    public List<Souvenir> getSouvenirsByCountry(String country, ManufactureList manufactureList) {

        List<Long> manufacturesFromCountry = manufactureList.getManufactureList().stream()
                .filter(m -> m.getCountry().equalsIgnoreCase(country))
                .map(Manufacture::getManufacture_id)
                .toList();

        return souvenirList.stream()
                .filter(s -> manufacturesFromCountry.contains(s.getManufacture_id()))
                .collect(Collectors.toList());
    }

    public Map<Integer, List<Souvenir>> getSouvenirsByYear() {
//without streams
//        Map<Integer,List<Souvenir>> res = new HashMap<>();
//
//        for (Souvenir s : souvenirList){
//
//            int year = s.getDate_of_manufacture().getYear();
//            List<Souvenir> list;
//            if(res.containsKey(year)){
//                list = res.get(year);
//            }
//            else {
//                list = new ArrayList<>();
//            }
//            list.add(s);
//            res.put(year, list);
//        }

        //with stream
        return souvenirList.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getDate_of_manufacture().getYear(),
                        Collectors.toList()
                ));
    }
}
