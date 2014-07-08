package com.hachiyae.jpa.helper;

import com.hachiyae.jpa.entity.Customer;

public class CustomerHelper extends TestDataHelper<Customer> {
    @Override
    public Customer defaultData() {
        Customer customer = new Customer();
        customer.setFirstName("foo");
        customer.setLastName("bar");
        return customer;
    }
}
