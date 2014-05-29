package com.hachiyae.jpa.helper;

import com.hachiyae.jpa.entity.Customer;

import java.util.HashMap;
import java.util.Map;

public class CustomerHelper extends TestDataHelper<Customer> {
    @Override
    public Map<String, Object> defaultData() {
        Map<String, Object> map = new HashMap<>();
        map.put("firstName", "あああああ");
        map.put("lastName", "あああああ");
        return map;
    }
}
