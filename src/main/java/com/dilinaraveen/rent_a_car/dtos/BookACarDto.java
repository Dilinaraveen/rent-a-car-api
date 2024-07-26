package com.dilinaraveen.rent_a_car.dtos;

import com.dilinaraveen.rent_a_car.enums.BookCarStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
public class BookACarDto {

    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fromDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate toDate;

    private Long days;

    private Long price;

    private String pickupLocation;

    private LocalTime pickupTime;

    private LocalTime dropTime;

    private String contactNumber;

    private BookCarStatus bookCarStatus;

    private Long carId;

    private  Long userId;

    private String username;

    private String email;
}
