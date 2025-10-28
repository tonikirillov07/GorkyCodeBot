package org.ds;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Test {
    private static final Log log = LogFactory.getLog(Test.class);

    public static void main(String[] args) {
        int[] nums = {170, 240, 15, 170, 240, 15, 170, 240, 15};
        int counter = 0;

        StringBuilder stringBuilder = new StringBuilder();

        for (int num : nums) {
            stringBuilder.append(Integer.toBinaryString(num));
        }

        for (int i = 0; i < stringBuilder.length(); i++) {
            if (i == stringBuilder.length() - 1)
                continue;

            int first = Integer.parseInt(String.valueOf(stringBuilder.charAt(i)));
            int second = Integer.parseInt(String.valueOf(stringBuilder.charAt(i + 1)));

            if (first != second)
                counter++;
        }

        System.out.println(counter);
    }
}
