package com.hachiyae.jpa.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hachiyae.jpa.entity.Customer;

@Component
public class CacheData {
    private List<Customer> data = new ArrayList<>();


    public void put(List<Customer> list){
        this.data.addAll(list);
    }
    public void put(Customer data){
        this.data.add(data);
    }
    public List<Customer> get(){
        return data;
    }
}
