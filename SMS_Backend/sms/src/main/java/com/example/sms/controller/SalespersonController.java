package com.example.sms.controller;

import com.example.sms.dto.SalespersonDto;
import com.example.sms.dto.SalespersonReturnDto;
import com.example.sms.model.MonthlyReport;
import com.example.sms.model.Salesperson;
import com.example.sms.service.SalespersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/salesperson")
@CrossOrigin(origins = "http://localhost:3000")
public class SalespersonController {

    @Autowired
    SalespersonService salespersonService;

    private static final Logger logger= LoggerFactory.getLogger(SalespersonController.class);

    @PostMapping("/login")
    public boolean loginValidate(@RequestBody SalespersonDto salespersonDto) throws Exception{
        boolean s=salespersonService.ValidateLogin(salespersonDto);
        if(s){
            logger.info("Login Successful");
            return s;
        }
        else{
            logger.info("Login Failed");
            return false;
        }
    }

    @GetMapping("/monthlySales/{foo}")
    public List<MonthlyReport> MonthlySalesReport(@PathVariable(value="foo") String id){
    	logger.info(id);
        return salespersonService.monthlySalesReport(id);
    }

    @PostMapping("/changePassword")
    public boolean ChangePassword(@RequestBody SalespersonDto salespersonDto){
        return salespersonService.ChangePassword(salespersonDto);
    }

    @GetMapping("/reportingSales/{foo}")
    public List<SalespersonReturnDto> reportingSalesOfSuperior(@PathVariable(value="foo") String id){
        return salespersonService.getReportingSales(id);
    }

    @GetMapping("/quotaLeft/{foo}")
    public double LeftPart(@PathVariable(value="foo") String id){
        return salespersonService.calculateLeft(id);
    }
}
