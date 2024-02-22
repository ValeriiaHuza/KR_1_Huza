package org.example;

import lombok.Getter;
import lombok.Setter;
import org.example.schema.Souvenir;

import java.util.ArrayList;

//made_singleton

@Getter
@Setter
public class SouvenirList {

    private ArrayList<Souvenir> souvenirList;

    public SouvenirList(ArrayList<Souvenir> list){
        souvenirList = list;
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
