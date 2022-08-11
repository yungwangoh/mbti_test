package mbti.mbti_test.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import mbti.mbti_test.domain.Address;

@Data
public class CreateAddressDto {

    private String city;
    private String street;
    private String zipcode;

    public CreateAddressDto(Address address) {
        this.city = address.getCity();
        this.street = address.getStreet();
        this.zipcode = address.getZipcode();
    }
}
