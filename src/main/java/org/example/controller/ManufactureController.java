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
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public final class ManufactureController {
    private static final String MAIN_FOLDER_NAME = "manufacture";
    private static final String FILE_EXTENSION = ".txt";
    private static ManufactureController manufactureController;
    private ManufactureList manufactureList;

    private ManufactureController() {
        ArrayList<Manufacture> ml = (ArrayList<Manufacture>) readManufacturesData();
        manufactureList = ManufactureList.getInstance(ml);
    }

    //add singleton
    public static ManufactureController getInstance() {
        if (manufactureController == null) {
            manufactureController = new ManufactureController();
        }
        return manufactureController;
    }

    public boolean saveManufacture(Manufacture manufacture) throws IOException {
        Files.createDirectories(Paths.get(MAIN_FOLDER_NAME));

        //check unique
        if (manufactureList.contains(manufacture)) {
            System.out.println("This manufacture already exists!");
            return false;
        }

        String fileName = generateFileName(manufacture);
        Path filePath = Paths.get(MAIN_FOLDER_NAME, fileName);

        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
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

    private String generateFileName(Manufacture manufacture) {
        return manufacture.getName().toLowerCase().replace(" ", "-") + "_" + manufacture.getManufacture_id() + FILE_EXTENSION;
    }

    public boolean updateManufacture(Manufacture newManufacture) {
        //check before update
        for (Manufacture m : manufactureList.getManufactureList()) {
            if (m.getManufacture_id() != (newManufacture.getManufacture_id())) {
                //якщо айді не співпадають, але імена такі вже є, то оновлювати не можна
                if (m.getName().equals(newManufacture.getName())) {
                    System.out.println("Such manufacture name exists");
                    return false;
                }
            }
        }

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(MAIN_FOLDER_NAME))) {
            Path manufactureFile = null;
            for (Path filePath : directoryStream) {
                String s = filePath.getFileName().toString();
                String[] name = s.split("_");

                if (name[1].equals(newManufacture.getManufacture_id() + FILE_EXTENSION)) {
                    manufactureFile = filePath;
                    break;
                }
            }

            if (manufactureFile == null) {
                System.out.println("Such manufacture doesn't exist");
                return false;
            }

            try (BufferedWriter writer = Files.newBufferedWriter(manufactureFile, StandardCharsets.UTF_8)) {
                writer.write(String.valueOf(newManufacture.getManufacture_id()));
                writer.newLine();
                writer.write(newManufacture.getName());
                writer.newLine();
                writer.write(newManufacture.getCountry());
                writer.close();

                manufactureList.update(newManufacture);

                Path newPath = Paths.get(MAIN_FOLDER_NAME, generateFileName(newManufacture));
                Files.move(manufactureFile, newPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Manufacture object updated!");
                return true;
            }
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
        ArrayList<Manufacture> result = new ArrayList<>();

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(MAIN_FOLDER_NAME))) {
            for (Path filePath : directoryStream) {
                if (Files.isRegularFile(filePath) && filePath.toString().endsWith(FILE_EXTENSION)) {
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

    public List<Long> getAllManufactureID() {
        return manufactureList.getAllManufactureID();
    }

    public Manufacture getManufactureByID(long id) {
        return manufactureList.getManufactureByID(id);
    }

    public boolean deleteManufacture(long id, SouvenirController souvenirController) {
        Manufacture manufacture = manufactureList.getManufactureByID(id);

        Path filePath = Paths.get(MAIN_FOLDER_NAME, generateFileName(manufacture));
        try {
            Files.delete(filePath);
            manufactureList.delete(manufacture);

            List<Long> allToDelete = new ArrayList<>();

            for (Souvenir s : souvenirController.getSouvenirList().getSouvenirList()){
                if(s.getManufacture_id()==manufacture.getManufacture_id()){
                   allToDelete.add(s.getSouvenir_id());
                }
            }

            for (long l : allToDelete){
                souvenirController.deleteSouvenir(l);
            }


        } catch (IOException e) {
            System.out.println("Error deleting file: " + e.getMessage());
        }
        return true;
    }
}
