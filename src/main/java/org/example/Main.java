package org.example;

import org.example.controller.ManufactureController;
import org.example.controller.SouvenirController;
import org.example.schema.Manufacture;
import org.example.schema.Souvenir;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final ManufactureController manufactureController = ManufactureController.getInstance();
    private static final SouvenirController souvenirController = SouvenirController.getInstance();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy[-M][-d]");

    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    private void run() throws IOException {
        Scanner in = new Scanner(System.in);
        System.out.println("-------");
        while (true) {
            printMainMenu();
            String input = in.nextLine().trim();
            switch (input) {
                case "1":
                    crudMenu(in);
                    break;
                case "2":
                    getManufacturesSouvenirs(in);
                    System.out.println("-------");
                    break;
                case "3":
                    getSouvenirsByCountry(in);
                    System.out.println("-------");
                    break;
                case "4":
                    getManufacturesPriceDesc(in);
                    System.out.println("-------");
                    break;
                case "5":
                    getAllSouvenirsAndManufactures();
                    System.out.println("-------");
                    break;
                case "6":
                    getManufacturesBySouvenirAndYear(in);
                    System.out.println("-------");
                    break;
                case "7":
                    getSouvenirsByYear();
                    break;
                case "0":
                default:
                    return;
            }
        }
    }

    private void getSouvenirsByYear() {
        // Для кожного року вивести список сувенірів, зроблених цього року.
        Map<Integer, List<Souvenir>> res = souvenirController.getSouvenirsByYear();

        if (res.size() == 0) {
            System.out.println("There are no elements in database");
        } else {
            res.forEach(((year, souvenirs) -> {
                System.out.println("Year : " + year);
                souvenirs.forEach(souvenir -> System.out.println("        Souvenir: " + souvenir));
            }));
        }


    }

    private void getManufacturesBySouvenirAndYear(Scanner in) {
        //Вивести інформацію про виробників заданого сувеніру, виробленого у заданому року.
        System.out.println("Write souvenir name:");

        String name = in.nextLine().trim();

        while (name.length() == 0) {
            System.out.println("Incorrect name. Try again");
            name = in.nextLine().trim();
        }

        System.out.println("Write year:");
        String year = in.nextLine().trim();

        while (!year.matches("\\d*") || year.length() == 0) {
            System.out.println("Incorrect year. Try again");
            year = in.nextLine().trim();
        }

        List<Manufacture> res = manufactureController.getManufacturesBySouvenirAndYear(name, Integer.parseInt(year), souvenirController.getSouvenirList());

        if (res.size() == 0) {
            System.out.println("There are no such manufactures");
        } else {
            System.out.println("Manufactures :");
            res.forEach(System.out::println);
        }
    }

    private void getAllSouvenirsAndManufactures() {
        Map<Manufacture, List<Souvenir>> res = manufactureController.getAllSouvenirsAndManufactures(souvenirController.getSouvenirList());

        if (res.size() == 0) {
            System.out.println("There are no elements in database");
        } else {
            res.forEach(((manufacture, souvenirs) -> {
                System.out.println("Manufacturer : " + manufacture);
                souvenirs.forEach(souvenir -> System.out.println("        Souvenir: " + souvenir));
            }));
        }
    }

    private void getManufacturesPriceDesc(Scanner in) {
        //Вивести інформацію про виробників, чиї ціни на сувеніри менше заданої.
        System.out.println("Write price:");

        String sPrice = in.nextLine().trim();

        while (!sPrice.matches("\\d*\\.?\\d+")) {
            System.out.println("Incorrect price. Try again");
            sPrice = in.nextLine().trim();
        }

        double dPrice = Double.parseDouble(sPrice);

        List<Manufacture> res = manufactureController.getManufacturePriceDesc(dPrice, souvenirController.getSouvenirList());

        if (res.size() == 0) {
            System.out.println("There are no such manufactures");
        } else {
            System.out.println("Manufactures :");
            res.forEach(System.out::println);
        }
    }

    private void getSouvenirsByCountry(Scanner in) {
        System.out.println("Write country for which you would like to get souvenirs:");

        String country = in.nextLine().trim();

        List<Souvenir> res = souvenirController.getSouvenirsByCountry(country, manufactureController.getManufactureList());

        if (res.size() == 0) {
            System.out.println("There are no souvenirs from this country");
        } else {
            System.out.println("Souvenirs from " + country + " :");
            res.forEach(System.out::println);
        }
    }

    private void getManufacturesSouvenirs(Scanner in) {
        System.out.println("Write manufacture id which souvenirs you want to get:");

        String findId = in.nextLine().trim();
        while (!findId.matches("\\d*")) {
            System.out.println("Incorrect id. Try again");
            findId = in.nextLine().trim();
        }
        long findIDLong = Long.parseLong(findId);

        if (manufactureController.getAllManufactureID().contains(findIDLong)) {

            List<Souvenir> res = souvenirController.getSouvenirsByManufacture(findIDLong);

            if (res.size() == 0) {
                System.out.println("This manufacture doesn't have souvenirs");
            } else {
                System.out.println(findId + " souvenirs :");
                res.forEach(System.out::println);
            }
        } else {
            System.out.println("Such manufacture id doesn't exist. Please, try again");
        }
    }

    private void printMainMenu() {
        System.out.println("Press 1 to add, read, update, delete Manufacture and Souvenir");
        System.out.println("Press 2 to view information about souvenirs of a specific manufacturer");
        System.out.println("Press 3 to view information about souvenirs made in a specific country");
        System.out.println("Press 4 to view information about manufacturers whose souvenir prices are less than a specified amount");
        System.out.println("Press 5 to view information about all manufacturers and their souvenirs");
        System.out.println("Press 6 to view information about manufacturers of a specific souvenir made in a specific year");
        System.out.println("Press 7 to view information about souvenirs made in each year");
        System.out.println("If you want to exit - press 0");
    }

    private void crudMenu(Scanner in) throws IOException {
        while (true) {
            printCRUDMenu();
            String input = in.nextLine().trim();
            switch (input) {
                case "1":
                    createManufacture(in);
                    System.out.println("-------");
                    break;
                case "2":
                    readAllManufacture();
                    System.out.println("-------");
                    break;
                case "3":
                    updateManufacture(in);
                    System.out.println("-------");
                    break;
                case "4":
                    deleteManufacture(in);
                    break;
                case "5":
                    createSouvenir(in);
                    System.out.println("-------");
                    break;
                case "6":
                    readAllSouvenirs();
                    System.out.println("-------");
                    break;
                case "7":
                    updateSouvenir(in);
                    System.out.println("-------");
                    break;
                case "8":
                    deleteSouvenir(in);
                    System.out.println("-------");
                    break;
                case "0":
                default:
                    return;
            }
        }
    }

    private void deleteManufacture(Scanner in) {
        System.out.println("Write manufacture id which you want to delete:");

        String deleteId = in.nextLine().trim();
        while (!deleteId.matches("\\d*")) {
            System.out.println("Incorrect id. Try again");
            deleteId = in.nextLine().trim();
        }
        long deleteIdLong = Long.parseLong(deleteId);

        if (manufactureController.getAllManufactureID().contains(deleteIdLong)) {

            manufactureController.deleteManufacture(deleteIdLong, souvenirController);
        } else {
            System.out.println("Such manufacture id doesn't exist. Please, try again");
        }
    }

    private void deleteSouvenir(Scanner in) {
        System.out.println("Write souvenir id which you want to delete:");

        String deleteId = in.nextLine().trim();
        while (!deleteId.matches("\\d*")) {
            System.out.println("Incorrect id. Try again");
            deleteId = in.nextLine().trim();
        }
        long deleteIdLong = Long.parseLong(deleteId);

        if (souvenirController.getAllSouvenirsID().contains(deleteIdLong)) {
            souvenirController.deleteSouvenir(deleteIdLong);
        } else {
            System.out.println("Such souvenir id doesn't exist. Please, try again");
        }
    }

    private void updateSouvenir(Scanner in) {
        System.out.println("Write souvenir id which you want to update:");

        String supdateID = in.nextLine().trim();
        while (!supdateID.matches("\\d*")) {
            System.out.println("Incorrect id. Try again");
            supdateID = in.nextLine().trim();
        }
        long supdateIDlong = Long.parseLong(supdateID);

        if (souvenirController.getAllSouvenirsID().contains(supdateIDlong)) {

            System.out.println("If you don't want update field - just press enter");

            while (true) {
                Souvenir oldSouvenir = souvenirController.getSouvenirByID(supdateIDlong);

                System.out.println("This souvenir - " + oldSouvenir.toString());
                System.out.println("Write NEW name");
                String sName = in.nextLine().trim();
                System.out.println("Write NEW manufacture_id");
                String manId = in.nextLine().trim();
                while (!manId.matches("\\d*") && manId.length() != 0) {
                    System.out.println("Incorrect id. Try again");
                    manId = in.nextLine().trim();
                }
                System.out.println("Write NEW date of manufacture in such format - YYYY-MM-DD");
                String sDate = in.nextLine().trim();
                while (!isValidDate(sDate) && sDate.length() != 0) {
                    System.out.println("Incorrect date. Try again");
                    sDate = in.nextLine().trim();
                }
                System.out.println("Write NEW price");
                String sPrice = in.nextLine().trim();
                while (!sPrice.matches("\\d*\\.?\\d+") && sPrice.length() != 0) {
                    System.out.println("Incorrect price. Try again");
                    sPrice = in.nextLine().trim();
                }

                if (sName.length() > 0) {
                    oldSouvenir.setName(sName);
                }

                if (manId.length() > 0) {
                    oldSouvenir.setManufacture_id(Long.parseLong(manId));
                }

                if (sDate.length() > 0) {
                    oldSouvenir.setDate_of_manufacture(LocalDate.parse(sDate, formatter));
                }

                if (sPrice.length() > 0) {
                    oldSouvenir.setPrice(Double.parseDouble(sPrice));
                }

                boolean success = souvenirController.updateSouvenir(oldSouvenir, manufactureController.getManufactureList());
                if (!success) {
                    System.out.println("Try to update the souvenir again - press 1. If you don't want - press something else.");
                    String input = in.nextLine().trim();
                    if (!input.equals("1")) {
                        break;
                    }
                } else {
                    break;
                }
            }
        } else {
            System.out.println("Such souvenir id doesn't exist. Please, try again");
        }

    }

    private void readAllSouvenirs() {
        System.out.println("All souvenirs:");
        System.out.println(souvenirController.getSouvenirList().toString());
    }

    private void createSouvenir(Scanner in) throws IOException {
        while (true) {
            System.out.println("Creating new Souvenir!");
            System.out.println("Write name");
            String sName = in.nextLine().trim();
            System.out.println("Write manufacture_id");
            String manId = in.nextLine().trim();

            while (!manId.matches("\\d*")) {
                System.out.println("Incorrect id. Try again");
                manId = in.nextLine().trim();
            }

            long manIDLong = Long.parseLong(manId);
            System.out.println("Write date of manufacture in such format - YYYY-MM-DD");
            String sDate = in.nextLine().trim();

            while (!isValidDate(sDate)) {
                System.out.println("Incorrect date. Try again");
                sDate = in.nextLine().trim();
            }

            LocalDate localDate = LocalDate.parse(sDate, formatter);
            System.out.println("Write price");
            String sPrice = in.nextLine().trim();

            while (!sPrice.matches("\\d*\\.?\\d+")) {
                System.out.println("Incorrect price. Try again");
                sPrice = in.nextLine().trim();
            }

            double dPrice = Double.parseDouble(sPrice);
            Souvenir newSouvenir = new Souvenir(sName, manIDLong, localDate, dPrice);

            boolean success = souvenirController.saveSouvenir(newSouvenir, manufactureController.getManufactureList());
            if (!success) {
                System.out.println("Try to create the souvenir again - press 1. If you don't want - press something else.");
                String input = in.nextLine().trim();
                if (!input.equals("1")) {
                    break;
                }
            } else {
                break;
            }
        }
    }

    private void updateManufacture(Scanner in) {
        System.out.println("Update manufacture");
        System.out.println("Write manufacture id which you want to update:");

        String updateID = in.nextLine().trim();
        while (!updateID.matches("\\d*")) {
            System.out.println("Incorrect id. Try again");
            updateID = in.nextLine().trim();
        }
        long updateIDlong = Long.parseLong(updateID);

        if (manufactureController.getAllManufactureID().contains(updateIDlong)) {

            System.out.println("If you don't want update field - just press enter");
            while (true) {

                Manufacture oldManufacture = manufactureController.getManufactureByID(updateIDlong);
                System.out.println("This manufacture - " + oldManufacture.toString());
                System.out.println("Write NEW name");
                String manName = in.nextLine().trim();
                System.out.println("Write NEW country");
                String manCountry = in.nextLine().trim();

                if (manName.length() > 0) {
                    oldManufacture.setName(manName);
                }

                if (manCountry.length() > 0) {
                    oldManufacture.setCountry(manCountry);
                }

                boolean success = manufactureController.updateManufacture(oldManufacture);
                if (!success) {
                    System.out.println("Try to update the manufacture again - press 1. If you don't want - press something else.");
                    String input = in.nextLine().trim();
                    if (!input.equals("1")) {
                        break;
                    }
                } else {
                    break;
                }
            }
        } else {
            System.out.println("Such manufacture id doesn't exist. Try again, please. ");
        }
    }

    private void readAllManufacture() {
        System.out.println("All manufactures:");
        System.out.println(manufactureController.getManufactureList().toString());
    }

    private void createManufacture(Scanner in) throws IOException {
        while (true) {
            System.out.println("Creating new Manufacture!");
            System.out.println("Write name");
            String manName = in.nextLine().trim();
            System.out.println("Write country");
            String manCountry = in.nextLine().trim();

            Manufacture newManufacture = new Manufacture(manName, manCountry);

            boolean success = manufactureController.saveManufacture(newManufacture);
            if (!success) {
                System.out.println("Try to create the manufacture again - press 1. If you don't want - press something else.");
                String input = in.nextLine().trim();
                if (!input.equals("1")) {
                    break;
                }
            } else {
                break;
            }
        }
    }

    private void printCRUDMenu() {
        System.out.println("Press 1 to CREATE new Manufacture.");
        System.out.println("Press 2 to READ all Manufactures.");
        System.out.println("Press 3 to UPDATE Manufacture.");
        System.out.println("Press 4 to DELETE Manufacture (and all his souvenirs).");
        System.out.println("-------");
        System.out.println("Press 5 to CREATE new Souvenir.");
        System.out.println("Press 6 to READ all Souvenirs.");
        System.out.println("Press 7 to UPDATE Souvenir.");
        System.out.println("Press 8 to DELETE Souvenir.");
        System.out.println("If you want to go to main menu - press 0");
    }

    private boolean isValidDate(String sDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy[-M][-d]");

        try {
            // Attempt to parse the date
            LocalDate.parse(sDate, formatter);
            return true; // Parsing succeeded, the date is valid
        } catch (DateTimeParseException e) {
            return false; // Parsing failed, the date is invalid
        }
    }
}