package com.example.sms.controller;

import com.example.sms.dto.*;
import com.example.sms.model.Commission;
import com.example.sms.model.Quota;
import com.example.sms.model.SalesCsv;
import com.example.sms.model.Vehicle;
import com.example.sms.service.AdminService;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@RestController
@RequestMapping(value = "/admin")
@CrossOrigin(origins = "http://localhost:3000")
@SuppressWarnings({"unused"})
public class AdminController {

    @Autowired
    AdminService adminService;

    private static final Logger logger= LoggerFactory.getLogger(AdminController.class);

    @PostMapping(value = "/addAdmin")
    public boolean addAdmin(@RequestBody AdminDto adminDto)throws Exception{
        boolean b=adminService.addNewAdmin(adminDto);
        if(b)
            logger.info("Add Admin Successful");
        else
            logger.info("Add Admin Failed");
        return b;
    }
    @PostMapping(value = "/login")
    public boolean loginValidate(@RequestBody AdminDto adminDto) throws Exception{
        boolean b=adminService.ValidateLogin(adminDto);
        if(b)
            logger.info("Login Successful");
        else
            logger.info("Login Failed");
        return b;
    }
    @PostMapping("/addSalesperson")
    public boolean addSalesperson(@RequestBody SalespersonDto salespersonDto) throws Exception{
        boolean b=adminService.addNewSalesperson(salespersonDto);
        if(b)
            logger.info("Salesperson added Successful");
        else
            logger.info("Salesperson add Failed");
        return b;
    }

    @PostMapping("/addVehicle")
    public boolean addVehicle(@RequestBody VehicleDto vehicleDto) throws Exception{
        boolean b=adminService.addVehicle(vehicleDto);
        if(b)
            logger.info("Vehicle added");
        else
            logger.info("Vehicle add failed");
        return b;
    }
    @PostMapping("/quota")
    public boolean changeQuota(@RequestBody QuotaDto quotaDto) throws Exception{
        boolean b= adminService.ModifyQuota(quotaDto);
        if(b)
            logger.info("Quota Modified/ Added");
        else
            logger.info("Quota modified failed");
        return b;
    }

    @PostMapping("/updateCommission")
    public boolean UpdateCommission(@RequestBody CommissionDto commissionDto) throws Exception{
        boolean b= adminService.UpdateCommission(commissionDto);
        if(b)
            logger.info("Commission updated");
        else
            logger.info("Update failed");
        return b;
    }
    
    
    @GetMapping("/forcedCommission/{foo}")
    public long ForcedCommission(@PathVariable(value="foo") int id) throws Exception{
        long  commission=adminService.ForcedCommissionCalculation(id);
        if(commission>=0)
            logger.info("Forced Commission Calculated value = "+commission);
        else
            logger.info("Failed to calculate commission");
        return commission;
    }

    @PostMapping("/changePassword")
    public boolean ChangePassword(@RequestBody SalespersonDto salespersonDto) throws Exception{
        boolean b= adminService.ChangePassword(salespersonDto);
        if(b)
            return true;
        else
            return false;
    }

    @GetMapping("/month")
    public void MonthEnd() throws Exception{
        adminService.monthEndCal();
    }
    
    @GetMapping("/monthEndCal/{foo}")
    public long MonthEndCalculation(@PathVariable(value="foo") int id) throws Exception{
        long b=adminService.MonthlyCalculation(id);
        if(b>=0){
            logger.info("Month End Calculation successful");
        }
        else{
            logger.info("Month End Calculation Failed");
        }
        return b;
    }

    @PostMapping("/saveReport")
    public boolean SaveSalesReport(@RequestParam("file") MultipartFile file) throws Exception{
//        Date date= Calendar.getInstance().getTime();
//        DateFormat format=new SimpleDateFormat("ddMMyyyy");
//        String today=format.format(date);
        String fileName=file.getOriginalFilename();
        logger.info("File name = "+fileName);
        String list[]=fileName.split("_");
        String strings[]= list[1].split("\\.");
        String date=strings[0];
        logger.info("Date = "+date);
        DateFormat df=new SimpleDateFormat("ddMMyyyy");
        String date1=df.format(new Date());
        logger.info("Date formatted current date "+date1 );
        if(date1.equals(date))
            return adminService.SaveSalesData(file);
        else {
            logger.info("Wrong file uploaded");
            return false;
        }
    }

    @GetMapping("/getAllVehicles")
    public List<Vehicle> AllVehicles(){
        return adminService.getAllVehicles();
    }
    
    @GetMapping("/getAllSales")
    public List<SalesCsv> AllSales(){
        return adminService.getAllSales();
    }

    @GetMapping("/getAllSalespersons")
    public List<SalespersonReturnDto> AllSalesperson(){
        return adminService.getAllSalespersons();
    }

    @GetMapping("/getAllCommissions")
    public List<Commission> AllCommissions(){
        return adminService.GetAllCommission();
    }

    @GetMapping("/getAllQuotas")
    public List<Quota> AllQuota(){
        return adminService.GetAllQuotas();
    }
}
