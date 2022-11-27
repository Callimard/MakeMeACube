package org.callimard.makemeacube.entities.dto;

import org.callimard.makemeacube.entities.sql.UserAddress;

public record UserAddressDTO(int id, String address, String city, String country, String postalCode) {

    public UserAddressDTO(UserAddress userAddress) {
        this(userAddress.getId(), userAddress.getAddress(), userAddress.getCity(), userAddress.getCountry(), userAddress.getPostalCode());
    }
}
