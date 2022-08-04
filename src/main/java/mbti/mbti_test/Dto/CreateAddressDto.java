package mbti.mbti_test.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mbti.mbti_test.domain.Address;

@Data
@Getter @Setter
public class CreateAddressDto { // AddressDto 생성 -> createMemberDto 참 0804

    private String city;
    private String street;
    private String zipcode;

    public CreateAddressDto(Address address) {
        this.city = address.getCity();
        this.street = address.getStreet();
        this.zipcode = address.getZipcode();
    }
}
