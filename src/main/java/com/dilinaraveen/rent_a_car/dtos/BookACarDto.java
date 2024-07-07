package com.dilinaraveen.rent_a_car.dtos;

import com.dilinaraveen.rent_a_car.enums.BookCarStatus;
import lombok.Data;

import java.util.Date;

@Data
public class BookACarDto {

    private Long id;

    private Date fromDate;

    private Date toDate;

    private Long days;

    private Long price;

    private BookCarStatus bookCarStatus;

    private Long carId;

    private  Long userId;
}
