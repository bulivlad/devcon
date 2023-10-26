package io.dotinc.devcon.backend.repository;

import io.dotinc.devcon.backend.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author vlabulim1 on 23.10.2023.
 */

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findBySsn(String ssn);
}
