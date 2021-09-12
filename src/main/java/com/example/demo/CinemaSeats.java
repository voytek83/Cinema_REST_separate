package com.example.demo;


import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CinemaSeats {

    private static List<Seats> cinema = new CopyOnWriteArrayList<>();


    public static int getRows() {
        return rows;
    }

    public static int getSeats() {
        return seats;
    }

    public static int rows = 9;
    public static int seats = 9;

    public static List<Seats> getCinema() {
        return cinema;
    }

    public void setCinema(List<Seats> cinema) {
        this.cinema = cinema;
    }

    static void setCinemaSeats() {
        for (int row = 1; row <= CinemaSeats.rows; row++) {
            for (int column = 1; column <= CinemaSeats.seats; column++) {

                Seats seat = new Seats();
                if (row > 4) {
                    seat.setPrice(8);
                } else {
                    seat.setPrice(10);
                }
                seat.setRow(row);
                seat.setColumn(column);
                cinema.add(seat);
            }
        }
    }

    static String statistics() {
        int totalSeats = CinemaSeats.seats * CinemaSeats.seats;
        int currentIncome = 0;
        int soldTickets = 0;
        for (int row = 1; row <= CinemaSeats.rows; row++) {
            for (int column = 1; column <= CinemaSeats.seats; column++) {

                if (row > 4 && SeatsSold.isSeatSold(row, column)) {
                    currentIncome += 8;
                    soldTickets++;
                } else if (SeatsSold.isSeatSold(row, column)) {
                    currentIncome += 10;
                    soldTickets++;
                }

            }
        }
        int seatsNumber = totalSeats - soldTickets;
        StringBuilder stats = new StringBuilder("{ \n \t \"current_income\":");
        stats.append(currentIncome);
        stats.append(",\n \t \"number_of_available_seats\":");
        stats.append(seatsNumber);
        stats.append(",\n \t \"number_of_purchased_tickets\":");
        stats.append(soldTickets);
        stats.append("\n }");
        return stats.toString();
    }
}
