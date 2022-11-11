package com.example.sms.service;

import com.example.sms.dto.*;
import com.example.sms.miscellaneous.Status;
import com.example.sms.model.*;
import com.example.sms.repository.*;
import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AdminService {

    private static final Logger logger= LoggerFactory.getLogger(AdminService.class);

    @Autowired
    VehicleRepository vehicleRepo;

    @Autowired
    QuotaRepository quotaRepo;

    @Autowired
    AdminRepository adminRepo;

    @Autowired
    SalespersonRepository salespersonRepo;

    @Autowired
    SalesCsvRepository salesCsvRepo;

    @Autowired
    CommissionRepository commissionRepo;

    @Autowired
    MonthlyReportRepository mrr;

    @Autowired
    SalespersonService salespersonService;

    public boolean ValidateLogin(AdminDto adminDto) {
        String email=adminDto.getEmail();
        String password= adminDto.getPassword();
        String getPass=adminRepo.validate(email);
        return password.equals(getPass);
    }


    public boolean addNewAdmin(AdminDto adminDto) {
        Admin admin=new Admin();
        admin.setEmail(adminDto.getEmail());
        admin.setContact(adminDto.getContact());
        admin.setName(adminDto.getName());
        admin.setPassword("123456");
        adminRepo.save(admin);
        return true;
    }

    public boolean addNewSalesperson(SalespersonDto salespersonDto) throws Exception{
        Salesperson s=salespersonRepo.findDetailsBySuperiorId(salespersonDto.getSuperiorId());
        if(s==null||s.getStatus().equals(Status.Inactive)){
            return false;
        }
        Salesperson salesperson=new Salesperson();
        salesperson.setName(salespersonDto.getName());
        salesperson.setEmail(salespersonDto.getEmail());
        salesperson.setPassword("123456");
        salesperson.setContact(salespersonDto.getContact());
        salesperson.setStatus(Status.Active);
        salesperson.setBranch(salespersonDto.getBranch());
        salesperson.setCommission(0);
        salesperson.setState(salespersonDto.getState());
        salesperson.setSuperiorId(salespersonDto.getSuperiorId());
        salesperson.setMonthlySales(0);
        salespersonRepo.save(salesperson);
        logger.info("Sales Person Saved Successfully ");
        return true;
    }

    public boolean addVehicle(VehicleDto vehicleDto) {
        Vehicle vehicle=new Vehicle();
        vehicle.setName(vehicleDto.getName());
        vehicle.setCost(vehicleDto.getCost());
        vehicle.setType(vehicleDto.getType());
        vehicle.setVariant(vehicleDto.getVariant());
        vehicleRepo.save(vehicle);
        return true;
    }

    public boolean ModifyQuota(QuotaDto quotaDto) {
        Quota q=quotaRepo.findByBranch(quotaDto.getBranch());
        if(q==null){
            Quota quota=new Quota();
            quota.setBranch(quotaDto.getBranch());
            quota.setTarget(quotaDto.getTarget());
            quotaRepo.save(quota);
        }
        else{
            int id=q.getId();
            long lim=quotaDto.getTarget();
            quotaRepo.updateLimit(id,lim);
        }
        return true;
    }

    public boolean UpdateCommission(CommissionDto commissionDto) {
        Commission c = commissionRepo.findByVehicleId(commissionDto.getVehicleId());
        if(c==null){
            Commission d=new Commission();
            d.setVehicleId(commissionDto.getVehicleId());
            d.setMinRange(commissionDto.getMinRange());
            d.setMaxRange(commissionDto.getMaxRange());
            d.setPercentage(commissionDto.getPercentage());
            commissionRepo.save(d);
        }
        else{
            int id=c.getId();
            int percent=commissionDto.getPercentage();
            long min=commissionDto.getMinRange();
            long max=commissionDto.getMaxRange();
            commissionRepo.updateRowValues(id,percent,min,max);
        }
        return true;
    }

    public long ForcedCommissionCalculation(int id) {
        long c = 0;
        try {
        	Salesperson sc=salespersonRepo.findById(id).get();
        	c = sc.getCommission();
        } catch (Exception e){
        	c=-1;
        } finally {
        	 logger.info("c: "+c);
             return c;
        }
    }
    
    public long MonthlyCalculation(int id) {
        long c = 0;
        try {
        	Salesperson sc=salespersonRepo.findById(id).get();
        	c = sc.getMonthlySales();
        } catch (Exception e){
        	c=-1;
        } finally {
        	 logger.info("c: "+c);
             return c;
        }
    }

    public List<MonthlyReport> monthEndCal() {
        List<Salesperson> salespersonArray=salespersonRepo.findAll();
        for(Salesperson s:salespersonArray) {
            long totalSum = s.getMonthlySales();
            long totalCommission = s.getCommission();
            double val=salespersonService.calculateLeft(s.getEmail());
            if(val>=100)
                totalCommission=(long)((double)totalCommission*(1.2));
            SimpleDateFormat simpleFormat = new SimpleDateFormat("MMMM");
            String strMonth = simpleFormat.format(new Date());
            simpleFormat = new SimpleDateFormat("yyyy");
            String year = simpleFormat.format(new Date());
            MonthlyReport k = new MonthlyReport();
            k.setMonth(strMonth);
            k.setYear(year);
            k.setEmail(s.getEmail());
            k.setCommissionValue(totalCommission);
            k.setSalesValue(totalSum);
            mrr.save(k);
            logger.info("Month Report of " + s.getEmail() + " is saved");
        }
        return mrr.findAll();
    }

    public boolean SaveSalesData(MultipartFile file) throws Exception{
        List<String[]> r=new ArrayList<>();
//        try(CSVReader reader =new CSVReader(new FileReader(file))) {
//            r=reader.readAll();
//        }
        try {
            String line;
            InputStream is = file.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                String[] st = line.split(",");
                r.add(st);
            }
        }
        catch (IOException e){
            logger.error("File read failed");
        }
        for( int i=1;i<r.size();i++){
            String[] data =r.get(i);
            logger.info("Saving the data "+r.size());
            String sno=data[1];   //
            DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
            Date date=df.parse(data[1]);
            int salespersonId=Integer.parseInt(data[2]);
            String vehicleType=data[3];
            String vehicleName=data[4];
            String vehicleVariant=data[5];
            long cost=Long.parseLong(data[6]);
            boolean b=saveToDB(date,salespersonId,vehicleType,vehicleName,vehicleVariant,cost);
            if(!b){
                logger.info("Saving the data failed for "+sno);
            }
        }
        return true;
    }

    private boolean saveToDB(Date date, int salespersonId, String vehicleType, String vehicleName, String vehicleVariant, long cost) {
        SalesCsv sc=new SalesCsv();
        sc.setCost(cost);
        sc.setSalespersonId(salespersonId);
        sc.setDate(date);
        Vehicle vc=vehicleRepo.findByNameType(vehicleName,vehicleType,vehicleVariant);
        if(vc!=null)
            sc.setVehicleId(vc.getId());
        salesCsvRepo.save(sc);
        boolean b=ChangeCommissionValue(sc);
        if(b){
            logger.info("Update of commission and sales successful");
            return true;
        }
        else{
            logger.info("Change commission and monthly sales failed for "+ salespersonId);
            return false;
        }

    }

    private boolean ChangeCommissionValue(SalesCsv sc) {
        Commission cm=commissionRepo.findByVehicleIdCost(sc.getVehicleId(),sc.getCost());
        int percent=cm.getPercentage();
        long com=(percent*sc.getCost())/100;
        Salesperson sp=salespersonRepo.findById(sc.getSalespersonId()).get();
        long presentVal=sp.getCommission()+com;
        boolean check=true;
        double[] level=new double[3];
        level[0]=(double) 1/10;
        level[1]=(double) 1/20;
        level[2]=(double) 1/50;
        int i=0;
        int id=sc.getSalespersonId();
        while(check & i<=2){
            int superiorId=salespersonRepo.findSuperiorIdBySalespersonId(id);
            if(superiorId==0 ){
                check=false;
                break;
            }
            Salesperson sup=null;
            try {
                sup = salespersonRepo.findById(superiorId).get();
                long sup_com=sup.getCommission() + (long)(level[i]*com);
                salespersonRepo.setCommission(superiorId,sup_com,sup.getMonthlySales());
            }
            catch (NoSuchElementException e) {
                logger.error(e.getMessage());
            }
            i++;
            id=superiorId;
        }
        long monthlySales=sp.getMonthlySales()+ sc.getCost();
        salespersonRepo.setCommission(sc.getSalespersonId(), presentVal, monthlySales);
        return true;
    }

    public boolean ChangePassword(SalespersonDto salespersonDto) {
        String newPass=salespersonDto.getPassword();
        String email=salespersonDto.getEmail();
        adminRepo.changePass(email,newPass);
        logger.info("Password Changed Successfully");
        return true;
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepo.findAll();
    }

    public List<SalespersonReturnDto> getAllSalespersons() {
        List<Salesperson> ls=salespersonRepo.findAll();
        List<SalespersonReturnDto> spr=new ArrayList<>();
        for (Salesperson s:ls) {
            SalespersonReturnDto st=new SalespersonReturnDto();
            st.setId(s.getId());
            st.setName(s.getName());
            st.setEmail(s.getEmail());
            st.setBranch(s.getBranch());
            st.setState(s.getState());
            st.setCommission(s.getCommission());
            st.setMonthlySales(s.getMonthlySales());
            st.setContact(s.getContact());
            st.setSuperiorId(s.getSuperiorId());
            spr.add(st);
        }
        return spr;
    }

    public List<Commission> GetAllCommission() {
        return commissionRepo.findAll();
    }

    public List<Quota> GetAllQuotas() {
        return quotaRepo.findAll();
    }

    public List<SalesCsv> getAllSales() {
        return salesCsvRepo.findAll();
    }
}
