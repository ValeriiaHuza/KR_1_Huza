package org.example.controller;

import lombok.Getter;
import lombok.Setter;
import org.example.SouvenirList;
import org.example.schema.Souvenir;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//singleton
@Getter
@Setter
public class SouvenirController {

    private SouvenirList souvenirList;

    public SouvenirController(){
        ArrayList<Souvenir> al = (ArrayList<Souvenir>) readSouvenirsData();
        souvenirList = new SouvenirList(al);
    }

    public boolean saveSouvenir(Souvenir souvenir) throws IOException {

        String mainFolderName = "souvenir";
        Files.createDirectories(Paths.get(mainFolderName));

        //check unique

        if(souvenirList.contains(souvenir)){
            System.out.println("This souvenir is already exists!");
            return false;
        }

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(mainFolderName,souvenir.getName().toLowerCase().replace(" ","-")+"_"+souvenir.getSouvenir_id()+".txt"), StandardCharsets.UTF_8)) {

            writer.write(String.valueOf(souvenir.getSouvenir_id()));
            writer.newLine();
            writer.write(souvenir.getName());
            writer.newLine();
            writer.write(souvenir.getManufacture_id());
            writer.newLine();
            writer.write(souvenir.getDate_of_manufacture().toString());
            writer.newLine();
            writer.write(String.valueOf(souvenir.getPrice()));

            souvenirList.add(souvenir);

            saveSouvenirIDTOFile(souvenir);

            System.out.println("Souvenir object saved to file successfully.");
            return true;

        } catch (IOException e) {
            System.out.println("Can't save souvenir");
            return false;
        }
    }

    private void saveSouvenirIDTOFile(Souvenir souvenir) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("souvenir_id.txt"))) {
                writer.write(String.valueOf(souvenir.getSouvenir_id()));
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public boolean updateSouvenir(Souvenir newSouvenir){

        //check before update

        for (Souvenir s : souvenirList.getSouvenirList()){
            if(s.getSouvenir_id()!=(newSouvenir.getSouvenir_id())){
                //якщо айді не співпадають, але імена і виробник такі вже є, то оновлювати не можна
                if (s.getName().equals(newSouvenir.getName()) && s.getManufacture_id().equals(newSouvenir.getManufacture_id())){
                    System.out.println("Such souvenir name exists");
                    return false;
                }
            }
        }

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get("souvenir"))) {

            Path souvenirFile = null;

            for (Path filePath : directoryStream){
                String s = filePath.getFileName().toString();
                String [] name = s.split("_");

                if (name[1].equals(newSouvenir.getSouvenir_id() +".txt")){
                    souvenirFile = filePath;
                    break;
                }
            }

            if(souvenirFile==null){
                System.out.println("Such souvenir doesn't exist");
                return false;
            }

            try (BufferedWriter writer = Files.newBufferedWriter(souvenirFile, StandardCharsets.UTF_8)) {

                writer.write(String.valueOf(newSouvenir.getSouvenir_id()));
                writer.newLine();
                writer.write(newSouvenir.getName());
                writer.newLine();
                writer.write(newSouvenir.getManufacture_id());
                writer.newLine();
                writer.write(newSouvenir.getDate_of_manufacture().toString());
                writer.newLine();
                writer.write(String.valueOf(newSouvenir.getPrice()));

                souvenirList.update(newSouvenir);
            }

            System.out.println("Souvenir object updated!");
            return true;

        } catch (IOException e) {
            System.out.println("Souvenir can't update");
            return false;
        }
    }

    //create method for creatinf file name

    public boolean deleteSouvenir(Souvenir souvenir){

        Path filePath = Paths.get("souvenir",souvenir.getName().toLowerCase()+"_"+souvenir.getSouvenir_id()+".txt");

        try {
            Files.delete(filePath);
            souvenirList.delete(souvenir);
        } catch (IOException e) {
            System.out.println("Error deleting file: " + e.getMessage());
        }

        return true;
    }

    private List<Souvenir> readSouvenirsData(){

        String mainFolderName = "souvenir";

        ArrayList<Souvenir> result = new ArrayList<>();

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(mainFolderName))) {
            for (Path filePath : directoryStream) {
                if (Files.isRegularFile(filePath) && filePath.toString().endsWith(".txt")) {
                    Souvenir souvenir = readSouvenirFromFile(filePath);

                    //check if exists???

                    result.add(souvenir);
                }
            }
        } catch (IOException e) {
            System.out.println("Error while reading data from files");
        }

        // Print the read Souvenirs
        for (Souvenir souvenir : result) {
            System.out.println("Souvenir object read from file: " + souvenir);
        }

        return result;
    }

    private Souvenir readSouvenirFromFile(Path filePath) {
        Souvenir souvenir = new Souvenir();

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            souvenir.setSouvenir_id(Long.parseLong(reader.readLine()));
            souvenir.setName(reader.readLine());
            souvenir.setManufacture_id(reader.readLine());
            String date = reader.readLine();

            LocalDate dateDate = LocalDate.parse(date);
            souvenir.setDate_of_manufacture(dateDate);
            souvenir.setPrice(Double.parseDouble(reader.readLine()));

        } catch (IOException e) {
            System.err.println("Error reading Souvenir from file: " + e.getMessage());
        }
        return souvenir;
    }

}
