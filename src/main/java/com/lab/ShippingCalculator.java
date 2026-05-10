package com.lab;

/**
 * Calculates shipping fees for standard and express shipping types.
 */
public class ShippingCalculator {

    private static final double EXPRESS_RATE = 5000.0;
    private static final double EXPRESS_BASE_FEE = 20000.0;
    private static final double STANDARD_RATE = 3000.0;
    private static final String EXPRESS = "EXPRESS";
    private static final String STANDARD = "STANDARD";

    /**
     * Calculates shipping fee based on weight and shipping type.
     *
     * @param weight package weight
     * @param type shipping type, either STANDARD or EXPRESS
     * @return calculated shipping fee
     */
    public double calculate(double weight, String type) {
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight must be positive");
        }

        if (EXPRESS.equals(type)) {
            return weight * EXPRESS_RATE + EXPRESS_BASE_FEE;
        }

        if (STANDARD.equals(type)) {
            return weight * STANDARD_RATE;
        }

        throw new IllegalArgumentException("Unknown type: " + type);
    }
}
