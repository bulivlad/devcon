package io.dotinc.devcon.workers.dto.process;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author vlabulim1 on 23.10.2023.
 */

@Getter
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
}
