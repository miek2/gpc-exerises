package com.bot;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.junit.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static org.junit.Assert.*;

public class ItemTest {

    private static final Logger log = LogManager.getLogger(ItemTest.class);

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
    public void modifyItemName() {

        Item input = new Item(1, "book", 12.49f);
        assertEquals("book", input.modifiedItemName);

        input = new Item(1, "music CD", 14.99f);
        assertEquals("music CD", input.modifiedItemName);

        input = new Item(1, "box of imported chocolates", 10.00f);
        assertEquals("box of chocolates", input.modifiedItemName);

        input = new Item(1, "imported box of chocolates", 10.00f);
        assertEquals("box of chocolates", input.modifiedItemName);

        input = new Item(1, "packet of headache pills", 10.00f);
        assertEquals("packet of headache pills", input.modifiedItemName);

        input = new Item(1, "imported bottle of perfume", 10.00f);
        assertEquals("bottle of perfume", input.modifiedItemName);

    }

    @Test
    public void calculateTaxRate() {

        Item input = new Item(1, "book", 12.49f);
        assertEquals(new BigDecimal(0.00).setScale(2, RoundingMode.HALF_UP), input.calculateTaxRate());

        input = new Item(1, "music CD", 14.99f);
        assertEquals(new BigDecimal(0.10).setScale(2, RoundingMode.HALF_UP), input.calculateTaxRate());

        input = new Item(1, "box of imported chocolates", 10.00f);
        assertEquals(new BigDecimal(0.05).setScale(2, RoundingMode.HALF_UP), input.calculateTaxRate());

        input = new Item(1, "imported box of chocolates", 10.00f);
        assertEquals(new BigDecimal(0.05).setScale(2, RoundingMode.HALF_UP), input.calculateTaxRate());

        input = new Item(1, "packet of headache pills", 10.00f);
        assertEquals(new BigDecimal(0.00).setScale(2, RoundingMode.HALF_UP), input.calculateTaxRate());

        input = new Item(1, "imported bottle of perfume", 10.00f);
        assertEquals(new BigDecimal(0.15).setScale(2, RoundingMode.HALF_UP), input.calculateTaxRate());
    }

    @Test
    public void calculatePriceAfterTax() {

        Item input = new Item(1, "book", 12.49f);
        assertEquals(new BigDecimal(12.49).setScale(2, RoundingMode.HALF_UP), input.calculatePriceAfterTax());

        input = new Item(1, "music CD", 14.99f);
        assertEquals(new BigDecimal(16.49).setScale(2, RoundingMode.HALF_UP), input.calculatePriceAfterTax());

        input = new Item(1, "box of imported chocolates", 10.00f);
        assertEquals(new BigDecimal(10.50).setScale(2, RoundingMode.HALF_UP), input.calculatePriceAfterTax());

        input = new Item(1, "imported box of chocolates", 10.00f);
        assertEquals(new BigDecimal(10.50).setScale(2, RoundingMode.HALF_UP), input.calculatePriceAfterTax());

        input = new Item(1, "packet of headache pills", 9.75f);
        assertEquals(new BigDecimal(9.75).setScale(2, RoundingMode.HALF_UP), input.calculatePriceAfterTax());

        input = new Item(1, "imported bottle of perfume", 47.50f);
        assertEquals(new BigDecimal(54.65).setScale(2, RoundingMode.HALF_UP), input.calculatePriceAfterTax());

        input = new Item(10, "book", 12.49f);
        assertEquals(new BigDecimal(124.90).setScale(2, RoundingMode.HALF_UP), input.calculatePriceAfterTax());
    }
}