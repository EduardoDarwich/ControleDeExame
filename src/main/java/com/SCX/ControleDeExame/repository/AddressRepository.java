package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.domain.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
}
