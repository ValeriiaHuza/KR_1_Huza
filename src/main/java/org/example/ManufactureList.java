package org.example;

import lombok.Getter;
import lombok.Setter;
import org.example.schema.Manufacture;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public final class ManufactureList {

    private static ManufactureList manInstance;
    private ArrayList<Manufacture> manufactureList;

    private ManufactureList(ArrayList<Manufacture> manufactureList) {
        this.manufactureList = manufactureList;
    }

    private ManufactureList(){
        manufactureList = new ArrayList<>();
    }

    public static ManufactureList getInstance(ArrayList<Manufacture> list){
        if(manInstance == null){
            manInstance = new ManufactureList(list);
        }
        return manInstance;
    }

    public static ManufactureList getInstance(){
        if(manInstance == null){
            manInstance = new ManufactureList();
        }
        return manInstance;
    }

    public boolean contains(Manufacture manufacture){
        return manufactureList.contains(manufacture);
    }

    public void update(Manufacture newMan) {
        int index = -1;
        for ( int i = 0; i < manufactureList.size(); i++){
            if (manufactureList.get(i).getManufacture_id()==newMan.getManufacture_id()){
                index = i;
                break;
            }
        }
        manufactureList.set(index,newMan);
    }

    public void add(Manufacture manufacture) {
        manufactureList.add(manufacture);
    }

    public List<Long> getAllManufactureID(){
        return manufactureList.stream()
                .map(Manufacture::getManufacture_id)
                .collect(Collectors.toList());
    }

}
