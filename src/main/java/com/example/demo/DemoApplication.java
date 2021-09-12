package com.example.demo;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        CinemaSeats.setCinemaSeats();
        SeatsSold.seatsSold();

    }
    @GetMapping("/seats")
    public String seats() throws JsonProcessingException {
        ObjectMapper Obj = new ObjectMapper();
        String jsonStr = Obj.writeValueAsString(CinemaSeats.getCinema());
        StringBuilder jsonOut;
        jsonOut = new StringBuilder("{ \n \t \"total_rows\":" + CinemaSeats.getRows() + "," +
                "\n \t \"total_columns\":" + CinemaSeats.getSeats() + "," +
                "\n \t \"available_seats\":");
        jsonOut.append(jsonStr);
        jsonOut.append("\n }");
        return jsonOut.toString();
    }

    @PostMapping("/purchase")
    public Object purchase(@RequestBody String data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(data);
        int row = jsonNode.get("row").asInt();
        int column = jsonNode.get("column").asInt();
        if (SeatsSold.seatOutBounds(row, column)) {
            return new ResponseEntity<>("{ \n \t \"error\": " +
                    "\"The number of a row or a column is out of bounds!\"\n}", HttpStatus.BAD_REQUEST);
        } else if (SeatsSold.isSeatSold(row, column)) {
            return new ResponseEntity<>("{ \n \t \"error\": " +
                    "\"The ticket has been already purchased!\"\n}", HttpStatus.BAD_REQUEST);
        } else {
            Token token = new Token();
            StringBuilder ticketBuilder = new StringBuilder("{\n \t \"token\": \"");
            Seats seat = CinemaSeats.getCinema().stream()
                    .filter(p -> p.getRow() == row)
                    .filter(r -> r.getColumn() == column).findFirst().get();
            String tempToken = token.getToken();
            ticketBuilder.append(tempToken);
            ticketBuilder.append("\",\n \t \"ticket\":");
            ticketBuilder.append(seat);
            SeatsSold.setSeatSold(row, column, tempToken);
            return ticketBuilder.toString();
        }
    }

    @PostMapping("/return")
    public Object refundTicket(@RequestBody String data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(data);
        String token = jsonNode.get("token").toString();
        token = token.substring(1, 37);
        String[][] seatsSold = SeatsSold.getSeatsSold();
        StringBuilder ticketBuilder = new StringBuilder("{\n \t \"returned_ticket\":");
        boolean notFound = true;
        for (int row = 1; row <= CinemaSeats.rows; row++) {
            for (int column = 1; column <= CinemaSeats.seats; column++) {
                if (seatsSold[row][column].equals(token)) {
                    SeatsSold.setSeatSold(row, column, "");

                    int finalRow = row;
                    int finalColumn = column;
                    Seats seat = CinemaSeats.getCinema().stream()
                            .filter(p -> p.getRow() == finalRow)
                            .filter(r -> r.getColumn() == finalColumn).findFirst().get();
                    ticketBuilder.append(seat);
                    notFound = false;

                }
            }
        }
        if (notFound) {
            return new ResponseEntity<>("{ \n \t \"error\": " +
                    "\"Wrong token!\"\n}", HttpStatus.BAD_REQUEST);
        } else {
            return ticketBuilder.toString();
        }
    }

    @PostMapping("/stats")
    public Object cinemaStats(@RequestParam(required = false, defaultValue = "") String password) {
        if (!password.equals("super_secret")) {
            return new ResponseEntity<>("{ \n \t \"error\": " +
                    "\"The password is wrong!\"\n}", HttpStatus.UNAUTHORIZED);
        } else {
            return CinemaSeats.statistics();
        }
    }


}