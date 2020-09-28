package com.bot;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.junit.*;

import java.io.File;
import java.util.ArrayList;

public class mainTest {

    private static final Logger log = LogManager.getLogger(mainTest.class);

    @BeforeClass
    public static void setupClass() {
        Configurator.setRootLevel(Level.DEBUG);
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testParseInput() throws Exception {

        String input = "1 book at 12.49";
        Item expected = new Item(1, "book", 12.49f);
        Item parsed = Main.parseInputLine(input);
        log.debug("parsed: " + parsed);
        Assert.assertTrue(expected.equals(parsed));

        input = "1 music CD at 14.99";
        expected = new Item(1, "music CD", 14.99f);
        parsed = Main.parseInputLine(input);
        log.debug("parsed: " + parsed);
        Assert.assertTrue(expected.equals(parsed));

        input = "12 chocolate bar at 0.85";
        expected = new Item(12, "chocolate bar", 0.85f);
        parsed = Main.parseInputLine(input);
        log.debug("parsed: " + parsed);
        Assert.assertTrue(expected.equals(parsed));

        input = "1 imported box of chocolates at 10.00";
        expected = new Item(1, "imported box of chocolates", 10.00f);
        parsed = Main.parseInputLine(input);
        log.debug("parsed: " + parsed);
        Assert.assertTrue(expected.equals(parsed));

        input = "1 imported bottle of perfume at 47.50";
        expected = new Item(1, "imported bottle of perfume", 47.50f);
        parsed = Main.parseInputLine(input);
        log.debug("parsed: " + parsed);
        Assert.assertTrue(expected.equals(parsed));

        input = "1 packet of headache pills at 9.75";
        expected = new Item(1, "packet of headache pills", 9.75f);
        parsed = Main.parseInputLine(input);
        log.debug("parsed: " + parsed);
        Assert.assertTrue(expected.equals(parsed));

        input = "1 box of imported chocolates at 11.25";
        expected = new Item(1, "box of imported chocolates", 11.25f);
        parsed = Main.parseInputLine(input);
        log.debug("parsed: " + parsed);
        Assert.assertTrue(expected.equals(parsed));

    }

    @Test(expected = Exception.class)
    public void testParseInputException() throws Exception {
        String input = "This is bad data";
        Item parsed = Main.parseInputLine(input);
        //Should throw exception and not get to next line
        Assert.fail("Should throw exception before this point");

    }


    @Test
    public void testOutputReciept() {

        ArrayList<Item> itemList = new ArrayList<>();

        Item item1 = new Item(1, "book", 12.49f);
        Item item2 = new Item(1, "music CD", 14.99f);
        Item item3 = new Item(1, "chocolate bar", 0.85f);

        itemList.add(item1);
        itemList.add(item2);
        itemList.add(item3);

        String receipt = Main.OutputReciept(itemList);

        String expected = "1 book: 12.49\n" +
                "1 music CD: 16.49\n" +
                "1 chocolate bar: 0.85\n" +
                "Sales Taxes: 1.50\n" +
                "Total: 29.83\n";

        Assert.assertEquals(expected, receipt);

    }

    @Test
    public void testParseFile() throws Exception {

        ArrayList<Item> itemList = new ArrayList<>();

        Item item1 = new Item(1, "book", 12.49f);
        Item item2 = new Item(1, "music CD", 14.99f);
        Item item3 = new Item(1, "chocolate bar", 0.85f);

        itemList.add(item1);
        itemList.add(item2);
        itemList.add(item3);

        String basePath = "src/test/resources/";

        File file1 = new File(basePath + "input1.txt");

        Assert.assertEquals(itemList, Main.parseFile(file1));

    }

    @Test(expected = Exception.class)
    public void testParseBadFileName() throws Exception {

        File badfile = new File("badfile");

        Main.parseFile(badfile);

        Assert.fail("Should throw exception before this point");
    }

    @Test(expected = Exception.class)
    public void testParseBadFileContents() throws Exception {

        String basePath = "src/test/resources/";

        File badfile = new File(basePath + "badInput.txt");

        Main.parseFile(badfile);

        Assert.fail("Should throw exception before this point");
    }
}