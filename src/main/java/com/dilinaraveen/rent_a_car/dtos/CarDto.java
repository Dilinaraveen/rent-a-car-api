package com.dilinaraveen.rent_a_car.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class CarDto {
    private Long id;

    private String brand;

    private String color;

    private String name;

    private String type;

    private  String transmission;

    private String description;

    private Long price;

    private String year;

    private Long carAvg;

    private String seats;

    private MultipartFile image;

    private String returnedImage;

}
