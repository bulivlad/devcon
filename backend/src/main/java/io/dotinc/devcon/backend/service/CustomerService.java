package io.dotinc.devcon.backend.service;

import io.dotinc.devcon.backend.dto.CustomerDto;
import io.dotinc.devcon.backend.exception.CustomerExistException;
import io.dotinc.devcon.backend.model.Customer;
import io.dotinc.devcon.backend.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author vlabulim1 on 23.10.2023.
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Customer mapCustomer(CustomerDto customerDto) {
        log.info("Mapping customerDto {} to customer", customerDto);
        return new Customer(customerDto);
    }

    public Customer saveCustomer(Customer customer) throws CustomerExistException {
        Customer existingCustomer = customerRepository.findBySsn(customer.getSsn());
        if (existingCustomer != null) {
            throw new CustomerExistException();
        }

        log.info("Saving customer {} to db", customer);
        return customerRepository.save(customer);
    }

    public CustomerDto findBySSN(String ssn) {
        Customer customer = customerRepository.findBySsn(ssn);
        return new CustomerDto(customer);
    }
}
