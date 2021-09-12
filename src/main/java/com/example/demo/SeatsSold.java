package com.example.demo;

import java.util.Objects;

public class SeatsSold {

    public static String[][] getSeatsSold() {
        return seatsSold;
    }

    private static String[][] seatsSold = new String[CinemaSeats.rows + 1][CinemaSeats.seats + 1];

    static void seatsSold() {

        for (int row = 1; row <= CinemaSeats.rows; row++) {
            for (int column = 1; column <= CinemaSeats.seats; column++) {
                seatsSold[row][column] = "";
            }
        }
    }

    public static boolean isSeatSold(int row, int column) {
        if (Objects.equals(seatsSold[row][column], "")) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean seatOutBounds(int row, int column) {
        if (row <= 0 || row > CinemaSeats.rows || column <= 0 || column > CinemaSeats.seats) {
            return true;
        } else {
            return false;
        }
    }

    public static void setSeatSold(int row, int column, String token) {
        seatsSold[row][column] = token;
    }




}
