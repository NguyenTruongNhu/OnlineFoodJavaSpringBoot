package com.ntndev.onlinefoodordering.repository;

import com.ntndev.onlinefoodordering.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
