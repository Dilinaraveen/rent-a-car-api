package com.dilinaraveen.rent_a_car.entities;

import com.dilinaraveen.rent_a_car.dtos.CarDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;


@Entity
@Data
@NoArgsConstructor
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;

    private String color;

    private String name;

    private String type;

    private  String transmission;

    private String description;

    private Long price;

    private Long carAvg;

    private String seats;

    private String year;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "longblob")
    private byte[] image;

    public CarDto getCarDto(){
        CarDto carDto = new CarDto();

        carDto.setId(id);
        carDto.setName(name);
        carDto.setBrand(brand);
        carDto.setColor(color);
        carDto.setPrice(price);
        carDto.setDescription(description);
        carDto.setType(type);
        carDto.setTransmission(transmission);
        carDto.setYear(year);
        carDto.setCarAvg(carAvg);
        carDto.setSeats(seats);
        carDto.setReturnedImage(Base64.getEncoder().encodeToString(image));

        return carDto;
    }

}
