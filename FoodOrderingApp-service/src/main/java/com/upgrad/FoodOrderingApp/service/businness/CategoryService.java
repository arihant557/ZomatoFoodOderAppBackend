package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.categoryDao;
import com.upgrad.FoodOrderingApp.service.dao.itemDao;
import com.upgrad.FoodOrderingApp.service.dao.restaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.aspectj.weaver.patterns.TypeCategoryTypePattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;



@Service
// this service used for the category service which tells the categories
public class CategoryService {
    @Autowired
    private categoryDao ct;
    @Autowired
    private restaurantDao rt;
    @Autowired
    private itemDao ido;
    public List<CategoryEntity> getAllCategoriesOrderedByName(){
        List<CategoryEntity>lt=ct.getAllCategory();
        System.out.println("hi"+lt);
        return lt;
    }
    public CategoryEntity getCategoryById(String id)throws CategoryNotFoundException{
        if(id.length()==0)
            throw new CategoryNotFoundException("CNF-001","Category id field should not be empty");
        if(ct.getCategoryId(id)==null)
            throw new CategoryNotFoundException("CNF-002","No category by this id");
        return ct.getCategoryId(id);
    }
    public List<CategoryEntity>getCategoriesByRestaurant(String id){
        return rt.catByResId(id);
    }
//    public List<ItemEntity> getItemByCategoryId(String id)throws CategoryNotFoundException{
//
//
//        List<ItemEntity> it=ido.getItemsByCategory(id);
//
//        return it;
//    }
}
