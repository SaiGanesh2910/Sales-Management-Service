package com.example.sms.repository;

import com.example.sms.model.Commission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface CommissionRepository extends JpaRepository<Commission,Integer> {
    @Query(nativeQuery = true,value= "select * from commission where vehicle_id=?1 and ?2<=max_range and ?2>=min_range")
    Commission findByVehicleIdCost(int vehicleId, long cost);

    @Query(nativeQuery = true,value= "select * from commission where vehicle_id=?1")
    Commission findByVehicleId(int vehicleId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true , value = "update commission set percentage=?2, min_range=?3,max_range=?4 where id=?1 ")
    void updateRowValues(int id, int percent, long min, long max);
}
