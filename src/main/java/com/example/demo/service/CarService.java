package com.example.demo.service;

import com.example.demo.model.Car;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface CarService {

    List<Car> getAllCars();
    Car getCarById(long id);
    void deleteCarById(long id);
    void saveCar(Car car);
}
