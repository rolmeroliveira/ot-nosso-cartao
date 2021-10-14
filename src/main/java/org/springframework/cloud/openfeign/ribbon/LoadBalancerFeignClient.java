package org.springframework.cloud.openfeign.ribbon;

import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import feign.Client;


public class LoadBalancerFeignClient {
    public LoadBalancerFeignClient(Client delegate, CachingSpringLoadBalancerFactory lbClientFactory, SpringClientFactory clientFactory) {
        throw new UnsupportedOperationException();
    }

    public Client getDelegate() {
        throw new UnsupportedOperationException();
    }
}