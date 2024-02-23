package org.example.controller;

import lombok.Getter;
import lombok.Setter;
import org.example.ManufactureList;
import org.example.SouvenirList;
import org.example.schema.Souvenir;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//singleton
@Getter
@Setter
public final class SouvenirController {
    private static final String MAIN_FOLDER_NAME = "souvenir";
    private static final String FILE_EXTENSION = ".txt";
    private static SouvenirController souvenirController;
    private SouvenirList souvenirList;

    private SouvenirController() {
        ArrayList<Souvenir> al = (ArrayList<Souvenir>) readSouvenirsData();
        souvenirList = SouvenirList.getInstance(al);
    }

    public static SouvenirController getInstance() {
        if (souvenirController == null) {
            souvenirController = new SouvenirController();
        }
        return souvenirController;
    }

    public boolean saveSouvenir(Souvenir souvenir, ManufactureList manufactureList) throws IOException {

        Files.createDirectories(Paths.get(MAIN_FOLDER_NAME));

        //check unique
        if (souvenirList.contains(souvenir)) {
            System.out.println("This souvenir is already exists!");
            return false;
        }

        List<Long> allID = manufactureList.getAllManufactureID();

        if (!allID.contains(souvenir.getManufacture_id())) {
            System.out.println("Such manufacture_id doesn't exist!");
            return false;
        }

        String fileName = generateFileName(souvenir);
        Path filePath = Paths.get(MAIN_FOLDER_NAME, fileName);

        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {

            writer.write(String.valueOf(souvenir.getSouvenir_id()));
            writer.newLine();
            writer.write(souvenir.getName());
            writer.newLine();
            writer.write(String.valueOf(souvenir.getManufacture_id()));
            writer.newLine();
            writer.write(souvenir.getDate_of_manufacture().toString());
            writer.newLine();
            writer.write(String.valueOf(souvenir.getPrice()));

            souvenirList.add(souvenir);
            saveSouvenirIDTOFile(souvenir);
            System.out.println("Souvenir - " + souvenir + " - saved to file successfully.");
            return true;
        } catch (IOException e) {
            System.out.println("Can't save souvenir");
            return false;
        }
    }

    private String generateFileName(Souvenir souvenir) {
        return souvenir.getName().toLowerCase().replace(" ", "-") + "_" + souvenir.getSouvenir_id() + FILE_EXTENSION;
    }

    private void saveSouvenirIDTOFile(Souvenir souvenir) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("souvenir_id.txt"))) {
            writer.write(String.valueOf(souvenir.getSouvenir_id()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean updateSouvenir(Souvenir newSouvenir, ManufactureList manufactureList) {

        //check before update

        for (Souvenir s : souvenirList.getSouvenirList()) {
            if (s.getSouvenir_id() != (newSouvenir.getSouvenir_id())) {
                //якщо айді не співпадають, але імена і виробник такі вже є, то оновлювати не можна
                if (s.getName().trim().equals(newSouvenir.getName().trim()) && s.getManufacture_id() == newSouvenir.getManufacture_id()) {
                    System.out.println(newSouvenir.getName() + " souvenir name exists");
                    return false;
                }
            }
        }

        if (!manufactureList.getAllManufactureID().contains(newSouvenir.getManufacture_id())) {
            System.out.println("Such manufacture id doesn't exist");
            return false;
        }

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(MAIN_FOLDER_NAME))) {
            Path souvenirFile = null;

            for (Path filePath : directoryStream) {
                String s = filePath.getFileName().toString();
                String[] name = s.split("_");

                if (name[1].equals(newSouvenir.getSouvenir_id() + FILE_EXTENSION)) {
                    souvenirFile = filePath;
                    break;
                }
            }

            if (souvenirFile == null) {
                System.out.println("Such souvenir doesn't exist");
                return false;
            }

            try (BufferedWriter writer = Files.newBufferedWriter(souvenirFile, StandardCharsets.UTF_8)) {
                writer.write(String.valueOf(newSouvenir.getSouvenir_id()));
                writer.newLine();
                writer.write(newSouvenir.getName());
                writer.newLine();
                writer.write(String.valueOf(newSouvenir.getManufacture_id()));
                writer.newLine();
                writer.write(newSouvenir.getDate_of_manufacture().toString());
                writer.newLine();
                writer.write(String.valueOf(newSouvenir.getPrice()));
                writer.close();

                souvenirList.update(newSouvenir);

                Path newPath = Paths.get(MAIN_FOLDER_NAME, generateFileName(newSouvenir));
                Files.move(souvenirFile, newPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Souvenir object updated!");
                System.out.println("New souvenir - " + newSouvenir);

                return true;
            }
        } catch (IOException e) {
            System.out.println("Souvenir can't update");
            return false;
        }
    }

    public boolean deleteSouvenir(long id) {

        Souvenir souvenir = souvenirList.getSouvenirByID(id);

        Path filePath = Paths.get(MAIN_FOLDER_NAME, generateFileName(souvenir));
        try {
            Files.delete(filePath);
            souvenirList.delete(souvenir);
            System.out.println(souvenir + " successfully deleted!");
        } catch (IOException e) {
            System.out.println("Error deleting file: " + e.getMessage());
        }
        return true;
    }

    private List<Souvenir> readSouvenirsData() {
        ArrayList<Souvenir> result = new ArrayList<>();

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(MAIN_FOLDER_NAME))) {
            for (Path filePath : directoryStream) {
                if (Files.isRegularFile(filePath) && filePath.toString().endsWith(".txt")) {
                    Souvenir souvenir = readSouvenirFromFile(filePath);
                    //check if exists???
                    result.add(souvenir);
                }
            }
        } catch (IOException e) {
            System.out.println("No souvenir data in files");
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
            souvenir.setManufacture_id(Long.parseLong(reader.readLine()));
            String date = reader.readLine();

            LocalDate dateDate = LocalDate.parse(date);
            souvenir.setDate_of_manufacture(dateDate);
            souvenir.setPrice(Double.parseDouble(reader.readLine()));
        } catch (IOException e) {
            System.out.println("Error reading Souvenir from file: " + e.getMessage());
        }
        return souvenir;
    }

    public List<Long> getAllSouvenirsID() {
        return souvenirList.getAllSouvenirsID();
    }

    public Souvenir getSouvenirByID(long id) {
        return souvenirList.getSouvenirByID(id);
    }

    public List<Souvenir> getSouvenirsByManufacture(long manufacture_id) {
        return souvenirList.getSouvenirsByManufacture(manufacture_id);
    }

    public List<Souvenir> getSouvenirsByCountry(String country, ManufactureList manufactureList) {
        return souvenirList.getSouvenirsByCountry(country, manufactureList);
    }

    public Map<Integer, List<Souvenir>> getSouvenirsByYear() {
        return souvenirList.getSouvenirsByYear();
    }
}
