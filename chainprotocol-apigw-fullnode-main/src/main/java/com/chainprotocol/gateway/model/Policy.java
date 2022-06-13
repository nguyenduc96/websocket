package com.chainprotocol.gateway.model;

import lombok.Data;

@Data
public class Policy {

    private String serviceId;
    
    private Long limit;
    
    private Long refreshInterval;
}
