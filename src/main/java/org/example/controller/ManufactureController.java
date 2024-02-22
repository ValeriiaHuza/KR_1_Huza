package org.example.controller;

import lombok.Getter;
import lombok.Setter;
import org.example.ManufactureList;
import org.example.schema.Manufacture;
import org.example.schema.Souvenir;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ManufactureController {

    private ManufactureList manufactureList;

    public ManufactureController(){
        ArrayList<Manufacture> ml = (ArrayList<Manufacture>) readManufacturesData();
        manufactureList = new ManufactureList(ml);
    }

    public boolean saveManufacture(Manufacture manufacture) throws IOException {

        String mainFolderName = "manufacture";
        Files.createDirectories(Paths.get(mainFolderName));

        //check unique

        if(manufactureList.contains(manufacture)){
            System.out.println("This manufacture is already exists!");
            return false;
        }

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(mainFolderName,manufacture.getName().toLowerCase().replace(" ","-")+"_"+manufacture.getManufacture_id()+".txt"), StandardCharsets.UTF_8)) {

            writer.write(String.valueOf(manufacture.getManufacture_id()));
            writer.newLine();
            writer.write(manufacture.getName());
            writer.newLine();
            writer.write(manufacture.getCountry());

            manufactureList.add(manufacture);

            saveManufactureIDTOFile(manufacture);

            System.out.println("Manufacture object saved to file successfully.");
            return true;

        } catch (IOException e) {
            System.out.println("Can't save manufacture");
            return false;
        }
    }

    public boolean updateManufacture(Manufacture newManufacture){

        //check before update
        for (Manufacture m : manufactureList.getManufactureList()){
            if(m.getManufacture_id()!=(newManufacture.getManufacture_id())){
                //якщо айді не співпадають, але імена такі вже є, то оновлювати не можна
                if (m.getName().equals(newManufacture.getName())){
                    System.out.println("Such manufactore name exists");
                    return false;
                }
            }
        }

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get("manufacture"))) {

            Path manufactureFile = null;

            for (Path filePath : directoryStream){
                String s = filePath.getFileName().toString();
                String [] name = s.split("_");

                if (name[1].equals(newManufacture.getManufacture_id() +".txt")){
                    manufactureFile = filePath;
                    break;
                }
            }

            if(manufactureFile==null){
                System.out.println("Such manufacture doesn't exist");
                return false;
            }

            try (BufferedWriter writer = Files.newBufferedWriter(manufactureFile, StandardCharsets.UTF_8)) {

                writer.write(String.valueOf(newManufacture.getManufacture_id()));
                writer.newLine();
                writer.write(newManufacture.getName());
                writer.newLine();
                writer.write(newManufacture.getCountry());

                manufactureList.update(newManufacture);
            }

            System.out.println("Manufacture object updated!");
            return true;

        } catch (IOException e) {
            System.out.println("Manufacture can't update");
            return false;
        }
    }

    private void saveManufactureIDTOFile(Manufacture manufacture) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("manufacture_id.txt"))) {
            writer.write(String.valueOf(manufacture.getManufacture_id()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Manufacture> readManufacturesData() {
        String mainFolderName = "manufacture";

        ArrayList<Manufacture> result = new ArrayList<>();

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(mainFolderName))) {
            for (Path filePath : directoryStream) {
                if (Files.isRegularFile(filePath) && filePath.toString().endsWith(".txt")) {
                    Manufacture manufacture = readManufactureFromFile(filePath);
                    result.add(manufacture);
                }
            }
        } catch (IOException e) {
            System.out.println("No manufacture data in files");
        }

        // Print the read Souvenirs
        for (Manufacture manufacture : result) {
            System.out.println("Manufacture object read from file: " + manufacture);
        }

        return result;
    }

    private Manufacture readManufactureFromFile(Path filePath) {
        Manufacture manufacture = new Manufacture();

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            manufacture.setManufacture_id(Long.parseLong(reader.readLine()));
            manufacture.setName(reader.readLine());
            manufacture.setCountry(reader.readLine());
        } catch (IOException e) {
            System.err.println("Error reading Manufacture from file: " + e.getMessage());
        }
        return manufacture;
    }
}
