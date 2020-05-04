package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.categoryDao;
import com.upgrad.FoodOrderingApp.service.dao.restaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
// this service is used for the restaurant service only
public class RestaurantService {
    @Autowired
    private restaurantDao rd;
    public List<RestaurantEntity> restaurantsByName(String name)throws RestaurantNotFoundException{
        if(name.length()==0)
            throw new RestaurantNotFoundException("RNF-003","Restaurant name field should not be empty");

        List<RestaurantEntity>ls=rd.resByName(name);

        return ls;
    }
    public RestaurantEntity restaurantByUUID(String id)throws RestaurantNotFoundException{
        if(id.length()==0)
            throw new RestaurantNotFoundException("RNF-002","Restaurant id field should not be empty");
        RestaurantEntity ls=rd.resById(id);
        return ls;
    }
    public List<RestaurantEntity>restaurantByCategory(String id)throws CategoryNotFoundException {
        if(id.length()==0)
            throw new CategoryNotFoundException("CNF-001","Category id field should not be empty");
        List<RestaurantEntity>lr= rd.getRestaurantbycategory(id);
        if(lr.size()==0)
            throw new CategoryNotFoundException("CNF-002","No category by this id");
        return lr;

    }
    public List<RestaurantEntity>restaurantsByRating(){
        return rd.restaurantsByRating();
    }
}
