package io.dotinc.devcon.backend.listener;

import io.dotinc.devcon.backend.dto.CustomerDto;
import io.dotinc.devcon.backend.dto.MailDto;
import io.dotinc.devcon.backend.exception.CustomerExistException;
import io.dotinc.devcon.backend.model.Customer;
import io.dotinc.devcon.backend.service.CustomerService;
import io.dotinc.devcon.backend.service.MailService;
import io.dotinc.devcon.backend.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author vlabulim1 on 23.10.2023.
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerQueueListener {
    private final CustomerService customerService;
    private final RegistrationService registrationService;
    private final MailService mailService;

    @RabbitListener(queues = {"customer"})
    public void onUserRegistration(CustomerDto customerDto) {
        log.info("Customer Registration Event Received: {}", customerDto);

        if (customerDto.getAge() != null && customerDto.getAge() > 18) {
            Customer customer = customerService.mapCustomer(customerDto);
            try {
                customer = customerService.saveCustomer(customer);
                registrationService.sendAck(customer.getSsn(), customer.getId() != null ? "success" : "failure");
            } catch (CustomerExistException e) {
                registrationService.sendAck(customer.getSsn(), "existent");
            }
        } else {
            registrationService.sendAck(customerDto.getSsn(), "failure");
        }
    }

    @RabbitListener(queues = {"customer-mail"})
    public void onCustomerMail(MailDto mailDto) {
        log.info("Customer Mail Event Received: {}", mailDto);

        mailService.sendCustomerMailAck(mailDto.getSsn(), "success");
    }

}
