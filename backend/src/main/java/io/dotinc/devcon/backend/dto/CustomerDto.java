package io.dotinc.devcon.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.dotinc.devcon.backend.model.Customer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 * @author vlabulim1 on 23.10.2023.
 */

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerDto {
    private String ssn;
    private String firstName;
    private String lastName;
    private Integer age;
    private Double grossIncome;
    private String city;
    private String email;
    private String phoneNumber;

    public CustomerDto(Customer customer) {
        this.ssn = customer.getSsn();
        this.firstName = customer.getFirstName();
        this.lastName = customer.getLastName();
        this.age = customer.getAge();
        this.grossIncome = customer.getGrossIncome();
        this.city = customer.getCity();
        this.email = customer.getEmail();
        this.phoneNumber = customer.getPhoneNumber();
    }
}
