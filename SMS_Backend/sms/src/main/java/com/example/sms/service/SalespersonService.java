package com.example.sms.service;

import com.example.sms.dto.SalespersonDto;
import com.example.sms.dto.SalespersonReturnDto;
import com.example.sms.model.MonthlyReport;
import com.example.sms.model.Quota;
import com.example.sms.model.Salesperson;
import com.example.sms.repository.MonthlyReportRepository;
import com.example.sms.repository.QuotaRepository;
import com.example.sms.repository.SalesCsvRepository;
import com.example.sms.repository.SalespersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SalespersonService {

    @Autowired
    MonthlyReportRepository monthlyReportRepository;

    @Autowired
    SalespersonRepository salespersonRepository;

    @Autowired
    QuotaRepository quotaRepo;

    public List<MonthlyReport> monthlySalesReport(String id) {
//        List<String> ls=new ArrayList<>();
//        ls.add(id);
        return monthlyReportRepository.findAllBymail(id);
    }

    public boolean ValidateLogin(SalespersonDto salespersonDto) {
        String email=salespersonDto.getEmail();
        String password= salespersonDto.getPassword();
        Salesperson s=salespersonRepository.findByEmailId(email);
        return password.equals(s.getPassword());
    }


    public boolean ChangePassword(SalespersonDto salespersonDto) {
        salespersonRepository.updatePassword(salespersonDto.getEmail(),salespersonDto.getPassword());
        return true;
    }

    public List<SalespersonReturnDto> getReportingSales(String email) {
    	Salesperson ss = salespersonRepository.findByEmailId(email);
        List<Salesperson> returnList=salespersonRepository.findAllBySuperiorId(ss.getId());
        List<SalespersonReturnDto> spl=new ArrayList<>();
        for (Salesperson s:returnList) {
            SalespersonReturnDto st=new SalespersonReturnDto();
            st.setId(s.getId());
            st.setName(s.getName());
            st.setEmail(s.getEmail());
            st.setContact(s.getContact());
            st.setState(s.getState());
            st.setBranch(s.getBranch());
            st.setMonthlySales(s.getMonthlySales());
            st.setCommission(s.getCommission());
            spl.add(st);
        }
        return spl;
    }

    public double calculateLeft(String email) {
        Salesperson sp=salespersonRepository.findByEmailId(email);
        Quota q=quotaRepo.findByBranch(sp.getBranch());
        double percentage=(double) ((sp.getMonthlySales()*100)/q.getTarget());
        return percentage;
    }
}
