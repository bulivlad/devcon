package io.dotinc.devcon.backend.model;

import io.dotinc.devcon.backend.dto.CustomerDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 * @author vlabulim1 on 23.10.2023.
 */

@Entity(name = "customer")
@Getter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer {
    @Id
    @GeneratedValue
    private Long id;
    @Version
    private Long version;
    private String ssn;
    private String firstName;
    private String lastName;
    private Integer age;
    private Double grossIncome;
    private String city;

    public Customer(CustomerDto customerDto) {
        this.ssn = customerDto.getSsn();
        this.firstName = customerDto.getFirstName();
        this.lastName = customerDto.getLastName();
        this.age = customerDto.getAge();
        this.grossIncome = customerDto.getGrossIncome();
        this.city = customerDto.getCity();
    }

}
