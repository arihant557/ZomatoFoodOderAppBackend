package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.businness.ItemService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
// in this contoller we are fetching the data of restaurant.
public class RestaurantController {

    @Autowired
    private RestaurantService rs;
    @Autowired
    private CategoryService CSI;
    @Autowired
    private ItemService ISI;
    @RequestMapping(value="/restaurant/name/{reastaurant_name}",method= RequestMethod.GET)
    public ResponseEntity <List<RestaurantList>> restaurantsByName(@PathVariable("reastaurant_name") String name)throws Exception{
        List<RestaurantEntity>ls= rs.restaurantsByName(name);
        List<RestaurantList> lr=new ArrayList<RestaurantList>();
        for(int i=0;i<ls.size();i++){
            List<CategoryEntity>ce= CSI.getCategoriesByRestaurant(ls.get(i).getUuid());
            String cat="";
            for(int j=0;j<ce.size()-1;j++){
                cat=cat+ce.get(j).getCategoryName()+", ";
            }
            cat=cat+ce.get(ce.size()-1).getCategoryName();
            RestaurantDetailsResponseAddressState ras=new RestaurantDetailsResponseAddressState().id(UUID.fromString(ls.get(i).getAddress().getState().getUuid())).stateName(ls.get(i).getAddress().getState().getState_name());
            RestaurantDetailsResponseAddress ra=new RestaurantDetailsResponseAddress().id(UUID.fromString(ls.get(i).getAddress().getUuid())).flatBuildingName(ls.get(i).getAddress().getFlatBuilNo()).locality(ls.get(i).getAddress().getLocality()).city(ls.get(i).getAddress().getCity()).pincode(ls.get(i).getAddress().getPincode()).state(ras);
            RestaurantList r=new RestaurantList().id(UUID.fromString(ls.get(i).getUuid())).restaurantName(ls.get(i).getRestaurantName()).photoURL(ls.get(i).getPhotoUrl()).customerRating(new BigDecimal(ls.get(i).getCustomerRating())).averagePrice(ls.get(i).getAvgPrice()).numberCustomersRated(ls.get(i).getNumberCustomersRated()).address(ra).categories(cat);
            lr.add(r);
        }
        return new ResponseEntity<List<RestaurantList>>(lr, HttpStatus.OK);
    }
    @RequestMapping(value="/api/restaurant/{restaurant_id}",method=RequestMethod.GET )
    public ResponseEntity <RestaurantDetailsResponse> restaurantByUUID(@PathVariable("restaurant_id")String id)throws Exception{
        RestaurantEntity ls= rs.restaurantByUUID(id);
        //RestaurantDetailsResponse lr=null;
        List<CategoryList>cs=new ArrayList<>();
        List<CategoryEntity>ce= CSI.getCategoriesByRestaurant(id);
        for(int j=0;j<ce.size();j++){
            List<ItemList>il=new ArrayList<>();
            List<ItemEntity>ie= ISI.getItemsByCategoryAndRestaurant(ls.getUuid(),ce.get(j).getUuid());
            for(int i=0;i<ie.size();i++){
                ItemList ill=new ItemList().id(UUID.fromString(ie.get(i).getUuid())).itemName(ie.get(i).getItemName()).itemType(ItemList.ItemTypeEnum.fromValue(ie.get(i).getType().toString())).price(ie.get(i).getPrice());
                il.add(ill);
            }
            CategoryList cll=new CategoryList().id(UUID.fromString(ce.get(j).getUuid())).categoryName(ce.get(j).getCategoryName()).itemList(il);
            cs.add(cll);
        }
        RestaurantDetailsResponseAddressState ras=new RestaurantDetailsResponseAddressState().id(UUID.fromString(ls.getAddress().getState().getUuid())).stateName(ls.getAddress().getState().getState_name());
        RestaurantDetailsResponseAddress ra=new RestaurantDetailsResponseAddress().id(UUID.fromString(ls.getAddress().getUuid())).flatBuildingName(ls.getAddress().getFlatBuilNo()).locality(ls.getAddress().getLocality()).city(ls.getAddress().getCity()).pincode(ls.getAddress().getPincode()).state(ras);
        RestaurantDetailsResponse r=new RestaurantDetailsResponse().id(UUID.fromString(ls.getUuid())).restaurantName(ls.getRestaurantName()).photoURL(ls.getPhotoUrl()).customerRating(new BigDecimal(ls.getCustomerRating())).averagePrice(ls.getAvgPrice()).numberCustomersRated(ls.getNumberCustomersRated()).address(ra).categories(cs);
        return new ResponseEntity<RestaurantDetailsResponse>(r,HttpStatus.OK);
    }
    @RequestMapping(value="/restaurant/category/{category_id}",method= RequestMethod.GET)
    public ResponseEntity <List<RestaurantList>> restaurantByCategory(@PathVariable("category_id") String id)throws Exception{
        List<RestaurantEntity>ls= rs.restaurantByCategory(id);
        List<RestaurantList> lr=new ArrayList<>();
        for(int i=0;i<ls.size();i++){
            String cat="";
            List<CategoryEntity>ce= CSI.getCategoriesByRestaurant(ls.get(i).getUuid());
            for(int j=0;j<ce.size();j++){
                cat=cat+ce.get(j).getCategoryName()+", ";
            }
            cat=cat+ce.get(ce.size()-1).getCategoryName();

            RestaurantDetailsResponseAddressState ras=new RestaurantDetailsResponseAddressState().id(UUID.fromString(ls.get(i).getAddress().getState().getUuid())).stateName(ls.get(i).getAddress().getState().getState_name());
            RestaurantDetailsResponseAddress ra=new RestaurantDetailsResponseAddress().id(UUID.fromString(ls.get(i).getAddress().getUuid())).flatBuildingName(ls.get(i).getAddress().getFlatBuilNo()).locality(ls.get(i).getAddress().getLocality()).city(ls.get(i).getAddress().getCity()).pincode(ls.get(i).getAddress().getPincode()).state(ras);
            RestaurantList r=new RestaurantList().id(UUID.fromString(ls.get(i).getUuid())).restaurantName(ls.get(i).getRestaurantName()).photoURL(ls.get(i).getPhotoUrl()).customerRating(new BigDecimal(ls.get(i).getCustomerRating())).averagePrice(ls.get(i).getAvgPrice()).numberCustomersRated(ls.get(i).getNumberCustomersRated()).address(ra).categories(cat);
            lr.add(r);
        }
        return new ResponseEntity<List<RestaurantList>>(lr,HttpStatus.OK);
    }

}
