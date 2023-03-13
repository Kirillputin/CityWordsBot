package ru.genby.genbycitywordsbotwh.utils;

import org.jetbrains.annotations.NotNull;

public class Letter {
    private static final String exceptAlphabet = "ЁЙЪЫЬ";

    public static String getCurrLastLetter(@NotNull String currCity) {
        String currLastLetterCity = String.valueOf(currCity.toUpperCase().charAt(currCity.length() - 1));

        if (exceptAlphabet.contains(currLastLetterCity)) {
            currLastLetterCity = String.valueOf(currCity.toUpperCase().charAt(currCity.length() - 2));

            //Исключим "Ы"
            if (exceptAlphabet.contains(currLastLetterCity)) {//сделать в вайле
                currLastLetterCity = String.valueOf(currCity.toUpperCase().charAt(currCity.length() - 3));
            }
        }
        return currLastLetterCity;
    }

    public static String getCurrFirstLetter(@NotNull String currCity) {
        return String.valueOf(currCity.toUpperCase().charAt(0));
    }
}