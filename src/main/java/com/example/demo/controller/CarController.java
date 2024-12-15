package com.example.demo.controller;

import com.example.demo.model.Car;
import com.example.demo.service.CarService;
import com.example.demo.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cars")
public class CarController {

    private CarService carService;
    private DriverService driverService;

    @Autowired
    public CarController(CarService carService, DriverService driverService) {
        this.carService = carService;
        this.driverService = driverService;
    }

    @GetMapping("/")
    public String list(Model model){
        model.addAttribute("cars", carService.getAllCars());
        return "car_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable long id){
        Car car = carService.getCarById(id);
        if(car != null){
            model.addAttribute("car", car);
            return "car_detail";
        }
        return "redirect:/cars/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable long id){
        carService.deleteCarById(id);
        return "redirect:/cars/";
    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("car", new Car());
        model.addAttribute("edit", false);
        model.addAttribute("drivers", driverService.getAllDrivers());
        return "car_edit";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable long id){
        Car car = carService.getCarById(id);
        if(car != null){
            car.setId(id);
            model.addAttribute("car", car);
            model.addAttribute("edit", true);
            model.addAttribute("drivers", driverService.getAllDrivers());
            return "car_edit";
        }
        return "redirect:/cars/";
    }

    @PostMapping("/save")
    public String save(@Valid Car car, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "car_edit";
        }
        carService.saveCar(car);
        return "redirect:/cars/";
    }

}
