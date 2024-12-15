package com.example.demo.controller;

import com.example.demo.model.Driver;
import com.example.demo.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/drivers")
public class DriverController {

    private DriverService driverService;

    @Autowired
    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping("/")
    public String list(Model model){
        model.addAttribute("drivers", driverService.getAllDrivers());
        return "driver_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable long id){
        Driver driver = driverService.getDriverById(id);
        if(driver != null){
            model.addAttribute("driver", driver);
            return "driver_detail";
        }
        return "redirect:/drivers/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable long id){
        driverService.deleteDriverById(id);
        return "redirect:/drivers/";
    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("driver", new Driver());
        model.addAttribute("edit", false);
        return "driver_edit";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable long id){
        Driver driver = driverService.getDriverById(id);
        if(driver != null){
            driver.setId(id);
            model.addAttribute("driver", driver);
            model.addAttribute("edit", true);
            return "driver_edit";
        }
        return "redirect:/drivers/";
    }

    @PostMapping("/save")
    public String save(@Valid Driver driver, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "driver_edit";
        }
        driverService.saveDriver(driver);
        return "redirect:/drivers/";
    }

}
