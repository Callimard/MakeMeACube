package org.callimard.makemeacube.models.dto;

import org.callimard.makemeacube.models.sql.UserAddress;

public record UserAddressDTO(int id, String address, String city, String country, String postalCode) {

    public UserAddressDTO(UserAddress userAddress) {
        this(userAddress.getId(), userAddress.getAddress(), userAddress.getCity(), userAddress.getCountry(), userAddress.getPostalCode());
    }
}
