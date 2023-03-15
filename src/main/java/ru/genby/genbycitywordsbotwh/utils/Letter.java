package ru.genby.genbycitywordsbotwh.utils;

import org.jetbrains.annotations.NotNull;

public class Letter {
    private static final String exceptAlphabet = "ЁЙЪЫЬ";

    public static String getCurrLastLetter(@NotNull String currCity) {
        String currLastLetterCity = String.valueOf(currCity.toUpperCase().charAt(currCity.length() - 1));
        int i = 1;

        while (exceptAlphabet.contains(currLastLetterCity)) {
            i++;
            currLastLetterCity = String.valueOf(currCity.toUpperCase().charAt(currCity.length() - i));
        }
        return currLastLetterCity;
    }

    public static String getCurrFirstLetter(@NotNull String currCity) {
        return String.valueOf(currCity.toUpperCase().charAt(0));
    }
}