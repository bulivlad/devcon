package io.dotinc.devcon.backend.controller;

import io.dotinc.devcon.backend.dto.CustomerDto;
import io.dotinc.devcon.backend.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author vlabulim1 on 24.10.2023.
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("")
    public CustomerDto retrieveCustomer(@RequestParam String ssn) {
        return customerService.findBySSN(ssn);
    }
}
