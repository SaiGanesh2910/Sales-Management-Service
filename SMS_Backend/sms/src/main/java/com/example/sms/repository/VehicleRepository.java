package com.example.sms.repository;

import com.example.sms.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VehicleRepository extends JpaRepository<Vehicle,Integer> {

    @Query(nativeQuery = true, value = "select * from vehicle where name=?1 and type=?2 and variant=?3")
    Vehicle findByNameType(String vehicleName, String vehicleType, String vehicleVariant);
}
