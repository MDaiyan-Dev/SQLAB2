package com.ontariotechu.sofe3980U;

/**
 * Represents an unsigned integer Binary variable.
 * Provides methods for bitwise operations and multiplication.
 */
public class Binary {
    private String number = "0";  // Stores the binary value as a string

    /**
     * Constructor that validates and initializes a binary object.
     *
     * @param number A string containing only '0' or '1'. Invalid inputs default to "0".
     */
    public Binary(String number) {
        if (number == null || number.isEmpty()) {
            this.number = "0";
            return;
        }

        for (char ch : number.toCharArray()) {
            if (ch != '0' && ch != '1') {
                this.number = "0";
                return;
            }
        }

        // Remove leading zeros, keeping at least one digit
        this.number = number.replaceFirst("^0+", "");
        if (this.number.isEmpty()) {
            this.number = "0";
        }
    }

    /**
     * Returns the binary value as a string.
     *
     * @return The binary number as a string.
     */
    public String getValue() {
        return this.number;
    }

    /**
     * Adds two binary numbers and returns the result as a new Binary object.
     *
     * @param num1 First binary number.
     * @param num2 Second binary number.
     * @return A new Binary object representing the sum.
     */
    public static Binary add(Binary num1, Binary num2) {
        int carry = 0;
        StringBuilder result = new StringBuilder();

        int i = num1.number.length() - 1;
        int j = num2.number.length() - 1;

        while (i >= 0 || j >= 0 || carry > 0) {
            int sum = carry;
            if (i >= 0) sum += num1.number.charAt(i--) - '0';
            if (j >= 0) sum += num2.number.charAt(j--) - '0';
            
            carry = sum / 2;
            result.insert(0, sum % 2);
        }
        
        return new Binary(result.toString());
    }

    /**
     * Performs bitwise OR on two binary numbers.
     *
     * @param num1 First binary number.
     * @param num2 Second binary number.
     * @return A new Binary object representing the OR result.
     */
    public static Binary or(Binary num1, Binary num2) {
        int len = Math.max(num1.number.length(), num2.number.length());
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < len; i++) {
            char bit1 = i < len - num1.number.length() ? '0' : num1.number.charAt(i - (len - num1.number.length()));
            char bit2 = i < len - num2.number.length() ? '0' : num2.number.charAt(i - (len - num2.number.length()));
            result.append(bit1 == '1' || bit2 == '1' ? '1' : '0');
        }
        
        return new Binary(result.toString());
    }

    /**
     * Performs bitwise AND on two binary numbers.
     *
     * @param num1 First binary number.
     * @param num2 Second binary number.
     * @return A new Binary object representing the AND result.
     */
    public static Binary and(Binary num1, Binary num2) {
        int len1 = num1.number.length();
        int len2 = num2.number.length();
        int maxLen = Math.max(len1, len2);
    
        // Ensure both binary numbers are right-aligned by padding with leading zeros
        String bin1 = String.format("%" + maxLen + "s", num1.number).replace(' ', '0');
        String bin2 = String.format("%" + maxLen + "s", num2.number).replace(' ', '0');
    
        StringBuilder result = new StringBuilder();
    
        // Perform bitwise AND on each bit
        for (int i = 0; i < maxLen; i++) {
            char bit1 = bin1.charAt(i);
            char bit2 = bin2.charAt(i);
            result.append((bit1 == '1' && bit2 == '1') ? '1' : '0');
        }
    
        // Remove any unnecessary leading zeros (but keep at least one digit)
        return new Binary(result.toString().replaceFirst("^0+(?!$)", ""));
    }
    
    /**
     * Multiplies two binary numbers using repeated addition.
     *
     * @param num1 First binary number.
     * @param num2 Second binary number.
     * @return A new Binary object representing the product.
     */
    public static Binary multiply(Binary num1, Binary num2) {
        Binary result = new Binary("0");

        for (int i = num2.number.length() - 1, shift = 0; i >= 0; i--, shift++) {
            if (num2.number.charAt(i) == '1') {
                String shiftedNum1 = num1.number + "0".repeat(shift);
                result = add(result, new Binary(shiftedNum1));
            }
        }
        
        return result;
    }
    
    /**
     * Performs bitwise implication on two binary numbers.
     * For each bit: if the bit in num1 is '0', the result is '1';
     * if the bit in num1 is '1', the result is the corresponding bit in num2.
     *
     * @param num1 First binary number.
     * @param num2 Second binary number.
     * @return A new Binary object representing the implication result.
     */
    public static Binary implication(Binary num1, Binary num2) {
        int maxLen = Math.max(num1.number.length(), num2.number.length());
        // Pad both numbers with leading zeros to ensure they're the same length
        String bin1 = String.format("%" + maxLen + "s", num1.number).replace(' ', '0');
        String bin2 = String.format("%" + maxLen + "s", num2.number).replace(' ', '0');
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < maxLen; i++) {
            char a = bin1.charAt(i);
            char b = bin2.charAt(i);
            // Bitwise implication: if a is '0', then result is '1'; if a is '1', then result is b.
            result.append(a == '0' ? '1' : b);
        }

        return new Binary(result.toString().replaceFirst("^0+(?!$)", ""));
    }
}
