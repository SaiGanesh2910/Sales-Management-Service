package com.example.sms.repository;

import com.example.sms.model.Salesperson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface SalespersonRepository extends JpaRepository<Salesperson,Integer> {

    @Query(nativeQuery = true , value = "select commission_value from salesperson where id=?1")
    long findCommission(int id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update salesperson set commission_value=?2 , monthly_sales=?3 where id=?1")
    void setCommission(int id,long presentVal,long monthlySales);

    @Query(nativeQuery = true , value = "select password from salesperson where email=?1")
    String validate(String email);

    @Modifying
    @Transactional
    @Query(nativeQuery = true , value = "update salesperson set password=?2 where email=?1")
    void updatePassword(String email , String pass);

    @Query(nativeQuery = true , value = "select * from salesperson where superior_id=?1")
    List<Salesperson> findAllBySuperiorId(int id);

    @Query(nativeQuery = true , value = "select * from salesperson where email=?1")
    Salesperson findByEmailId(String email);

    @Query(nativeQuery = true , value = "select superior_id from salesperson where id=?1")
    int findSuperiorIdBySalespersonId(int id);

    @Query(nativeQuery = true , value = "select * from salesperson where id=?1")
    Salesperson findDetailsBySuperiorId(int superiorId);
}
