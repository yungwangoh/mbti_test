package mbti.mbti_test.dto;

import lombok.Data;
import mbti.mbti_test.domain.Address;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
