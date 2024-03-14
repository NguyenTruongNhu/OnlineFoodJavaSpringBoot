package com.ntndev.onlinefoodordering.dto.request;


import com.ntndev.onlinefoodordering.model.Address;
import lombok.Data;

@Data
public class OrderRequest {

    private Long restaurantId;
    private Address deliveryAddress;


}
