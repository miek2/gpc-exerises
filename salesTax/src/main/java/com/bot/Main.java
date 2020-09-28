package com.bot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Main {

    private static final Logger log = LogManager.getLogger(Main.class);
    private static final Pattern inputPattern = Pattern.compile("(\\d*)\\s*([\\w\\s*]*)\\s+at\\s+(\\d*.\\d\\d)");

    public static void main(String[] args) {

        String userDirLocation = System.getProperty("user.dir");
        File userDir = new File(userDirLocation);

        JFileChooser jfc = new JFileChooser(userDir);
        jfc.setDialogTitle("Please select a txt file to use as input: ");
        jfc.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files (*.txt)", "txt");
        jfc.addChoosableFileFilter(filter);

        int returnValue = jfc.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();

            try {
                List<Item> itemsList = parseFile(selectedFile);
                System.out.println("Printing Reciept: ");
                System.out.print(OutputReciept(itemsList));
            } catch (Exception e) {
                System.out.println("Error parsing file, Exiting...");
                return;
            }

        } else {
            System.out.println("File not selected, exiting ...");
            return;
        }

    }

    public static List<Item> parseFile(File selectedFile) throws Exception {
        List<Item> itemsList = new ArrayList<>();


        List<String> lines = Collections.emptyList();
        System.out.println("Trying to open " + Paths.get(selectedFile.getAbsolutePath()));
        try {
            lines = Files.readAllLines(Paths.get(selectedFile.getAbsolutePath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Error reading file.");
            throw e;
        }

        Iterator<String> itr = lines.iterator();

        while (itr.hasNext()) {
            try {
                itemsList.add(parseInputLine(itr.next()));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw e;
            }
        }

        return itemsList;

    }

    public static Item parseInputLine(String inputLine) throws Exception {
        Scanner scanner = new Scanner(inputLine);


        if (scanner.findInLine(inputPattern) == null) {
            log.error("error parsing input line");
            throw new Exception("Unable to parse input: " + inputLine);
        }
        MatchResult result = scanner.match();


        int quantity = Integer.parseInt(result.group(1));
        String item = result.group(2);
        float price = Float.parseFloat(result.group(3));



        for (int i=1; i<=result.groupCount(); i++) {
            log.debug("match: " + result.group(i));
        }
        scanner.close();

        return new Item(quantity, item, price);
    }

    public static String OutputReciept(List<Item> itemsList) {
        StringBuffer sb = new StringBuffer();

        for (Item i : itemsList) {
            sb.append(i.outputReceiptItem());
            sb.append(System.lineSeparator());
        }

        sb.append("Sales Taxes: ");

        BigDecimal totalTax = new BigDecimal(0.00).setScale(2, RoundingMode.HALF_UP);
        for (Item i : itemsList) {
            totalTax = totalTax.add(i.taxAmount);
        }
        sb.append(totalTax);
        sb.append(System.lineSeparator());
        sb.append("Total: ");

        BigDecimal total = new BigDecimal(0.00).setScale(2, RoundingMode.HALF_UP);
        for (Item i : itemsList) {
            total = total.add(i.priceAfterTax);
        }
        sb.append(total);
        sb.append(System.lineSeparator());

        return sb.toString();
    }



}
