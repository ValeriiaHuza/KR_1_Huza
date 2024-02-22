package org.example;

import lombok.Getter;
import lombok.Setter;
import org.example.schema.Souvenir;
import java.util.ArrayList;

@Getter
@Setter
public final class SouvenirList {

    private static SouvenirList souvInstance;
    private ArrayList<Souvenir> souvenirList;

    private SouvenirList(ArrayList<Souvenir> list){
        souvenirList = list;
    }

    public static SouvenirList getInstance(ArrayList<Souvenir> list){
        if(souvInstance==null){
            souvInstance = new SouvenirList(list);
        }
        return souvInstance;
    }
    public static SouvenirList getInstance(){
        if(souvInstance==null){
            souvInstance = new SouvenirList();
        }
        return souvInstance;
    }

    public SouvenirList(){
        souvenirList = new ArrayList<>();
    }

    public boolean contains(Souvenir souvenir){

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
        for ( int i = 0; i < souvenirList.size(); i++){
            if (souvenirList.get(i).getSouvenir_id()==newSouvenir.getSouvenir_id()){
                index = i;
                break;
            }
        }
        souvenirList.set(index,newSouvenir);
    }

    public void delete(Souvenir souvenir) {
        souvenirList.remove(souvenir);
    }

    public void add(Souvenir souvenir) {
        souvenirList.add(souvenir);
    }

    public long getID(Souvenir s2) {

        for (Souvenir s : souvenirList){
            if (s.equals(s2)){
                return s.getSouvenir_id();
            }
        }

        return -1;
    }
}
