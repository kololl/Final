package com.example.afinal.Model;

import java.util.HashMap;
import java.util.Map;

public class Category {
    private  String  name;
    private String price;
    public Category() {
    }

    public Category(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

   public Map<String, Object> toMap(){
       HashMap<String, Object> result= new HashMap<>();
       result.put("name", name);
       return  result;
   }
    @Override
    public  String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                '}';
    }


}

