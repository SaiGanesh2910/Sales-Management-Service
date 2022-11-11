package com.example.sms.repository;

import com.example.sms.model.MonthlyReport;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MonthlyReportRepository extends JpaRepository<MonthlyReport,Integer> {
	
	@Query(nativeQuery = true,value= "select * from monthly_report where email=?1")
	List<MonthlyReport> findAllBymail(String id);
}
