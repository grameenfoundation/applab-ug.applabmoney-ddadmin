package org.applab.digdata.client.mob.admin;

/**
 *
 *
 */
public class PassKeyGenerator {

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        while (i <= count) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
            i++;
        }
        return builder.toString();
    }
}
