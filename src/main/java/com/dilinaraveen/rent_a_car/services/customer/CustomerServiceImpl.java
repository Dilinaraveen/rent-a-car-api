package com.dilinaraveen.rent_a_car.services.customer;

import com.dilinaraveen.rent_a_car.dtos.BookACarDto;
import com.dilinaraveen.rent_a_car.dtos.CarDto;
import com.dilinaraveen.rent_a_car.entities.BookACar;
import com.dilinaraveen.rent_a_car.entities.Car;
import com.dilinaraveen.rent_a_car.entities.User;
import com.dilinaraveen.rent_a_car.enums.BookCarStatus;
import com.dilinaraveen.rent_a_car.repositories.BookACarRepository;
import com.dilinaraveen.rent_a_car.repositories.CarRepository;
import com.dilinaraveen.rent_a_car.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{

    private final CarRepository carRepository;

    private final UserRepository userRepository;

    private final BookACarRepository bookACarRepository;

    @Override
    public List<CarDto> getAllCars() {
        return carRepository.findAll().stream().map(Car::getCarDto).collect(Collectors.toList());
    }

    @Override
    public boolean bookACar(BookACarDto bookACarDto) {
        Optional<Car> optionalCar = carRepository.findById(bookACarDto.getCarId());
        Optional<User> optionalUser = userRepository.findById(bookACarDto.getUserId());
        if (optionalCar.isPresent() && optionalUser.isPresent()){
            Car existingCar = optionalCar.get();
            BookACar bookACar = new BookACar();
            bookACar.setUser(optionalUser.get());
            bookACar.setCar(optionalCar.get());
            bookACar.setBookCarStatus(BookCarStatus.PENDING);

            long diffInMilliSeconds = bookACarDto.getToDate().getTime()-bookACarDto.getFromDate().getTime();

            long days = TimeUnit.MICROSECONDS.toDays(diffInMilliSeconds);

            bookACar.setDays(days);
            bookACar.setPrice(existingCar.getPrice() * days);

            bookACarRepository.save(bookACar);

            return true;
        }
        return false;
    }

    @Override
    public CarDto getCarById(Long carId) {
        Optional<Car> optionalCar = carRepository.findById(carId);
        return optionalCar.map(Car::getCarDto).orElse(null);
    }

}
