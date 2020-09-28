package com.bot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

public class Item {
    int quantity;
    String origItemName;
    String modifiedItemName;
    boolean imported;
    BigDecimal price;
    BigDecimal taxRate;
    BigDecimal taxAmount;
    BigDecimal priceAfterTax;

    private static final Logger log = LogManager.getLogger(Item.class);

    private List<String> exemptItems = Arrays.asList("book", "chocolate", "pill", "medical", "food");

    public Item(int quantity, String item, float price) {
        this.quantity = quantity;
        this.origItemName = item;
        this.price = new BigDecimal(price).setScale(2, RoundingMode.HALF_UP);
        this.imported = isImported();
        this.taxRate = calculateTaxRate();
        this.modifiedItemName = modifyItemName();
        this.taxAmount = calculateTaxAmount();
        this.priceAfterTax = calculatePriceAfterTax();
        log.debug(this);
    }

    boolean isImported() {
        if (this.origItemName.toLowerCase().contains("imported")) {
            return true;
        }
        return false;
    }

    String modifyItemName() {
        String regex = "\\bimported\\b\\s*";
        return origItemName.replaceAll(regex, "");
    }

    BigDecimal calculateTaxRate() {
        BigDecimal taxRate = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);

        //if the item is not tax exempt
        if (!exemptItems.stream().anyMatch(s -> this.origItemName.toLowerCase().contains(s))) {
            log.debug("non exempted item");
            taxRate = taxRate.add(BigDecimal.valueOf(0.1));
        } else {
            log.debug("exempted item");
        }

        //check if item is imported
        if (this.imported) {
            log.debug("Imported: " +imported);
            taxRate = taxRate.add(BigDecimal.valueOf(0.05));
        }

        return taxRate;
    }

    BigDecimal calculateTaxAmount() {
        log.debug("taxRate: " + taxRate + ", price: " + price);
        BigDecimal afterTaxPrice = price.multiply(taxRate);//.setScale(2, RoundingMode.DOWN);
        log.debug("afterTax preRounding: " + afterTaxPrice);

        //round to nearest 0.05
        BigDecimal rounded1 = afterTaxPrice.multiply(new BigDecimal(20));
        BigDecimal rounded2 = rounded1.setScale(0,RoundingMode.UP);
        BigDecimal rounded = rounded2.divide(new BigDecimal(20));
        log.debug("afterTax postRounding1: " + rounded1);
        log.debug("afterTax postRounding2: " + rounded2);
        log.debug("afterTax postRounding3: " + rounded);
        return rounded.setScale(2, RoundingMode.HALF_UP).multiply(new BigDecimal(quantity));
    }

    BigDecimal calculatePriceAfterTax() {
        return price.multiply(new BigDecimal(quantity)).add(taxAmount).setScale(2, RoundingMode.HALF_UP);
    }

    public String outputReceiptItem() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.quantity);
        sb.append(" ");
        if (this.imported) {
            sb.append("imported ");
        }
        sb.append(this.modifiedItemName);
        sb.append(": ");
        sb.append(this.priceAfterTax);
        return sb.toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item1 = (Item) o;
        o.toString();
        return quantity == item1.quantity &&
                item1.price.compareTo(price) == 0 &&
                origItemName.equalsIgnoreCase(item1.origItemName);
    }

    public boolean equals(Item o) {
        o.toString();
        return quantity == o.quantity &&
                o.price.compareTo(price) == 0 &&
                origItemName.equalsIgnoreCase(o.origItemName);
    }

    @Override
    public String toString() {
        return "Item{" +
                "quantity=" + quantity +
                ", origItemName='" + origItemName + '\'' +
                ", modifiedItemName='" + modifiedItemName + '\'' +
                ", imported=" + imported +
                ", price=" + price +
                ", taxRate=" + taxRate +
                '}';
    }
}
