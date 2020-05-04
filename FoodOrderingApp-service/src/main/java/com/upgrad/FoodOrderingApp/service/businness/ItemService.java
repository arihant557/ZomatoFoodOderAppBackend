package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.itemDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//for getting item service this service is used
public class ItemService {
    @Autowired
    private itemDao i;
    @Autowired
    private CategoryService cl;
    @Autowired
    private RestaurantService rl;

    public List<ItemEntity> getItemByCategoryId(String id, CategoryEntity cs){
        System.out.println("YUYUYUY"+id);

        List<ItemEntity> it=i.getItemsByCategory(id,cs);

        return it;
    }
    public List<ItemEntity> getItemsByCategoryAndRestaurant(String Rid,String Cid)throws Exception{
        RestaurantEntity r=rl.restaurantByUUID(Rid);
        CategoryEntity c=cl.getCategoryById(Cid);
        return i.getItemsByCategoryandRestaurant(r,c);
    }
}
