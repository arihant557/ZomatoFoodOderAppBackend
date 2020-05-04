package com.upgrad.FoodOrderingApp.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.businness.ItemService;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class  CategoryController {
    @Autowired
    private CategoryService cs;
    @Autowired
    private ItemService ISI;

    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public ResponseEntity<CategoriesListResponse> getAllCategoriesOrderedByName() throws Exception {
        List<CategoryEntity> list = cs.getAllCategoriesOrderedByName();
        List<CategoryListResponse> lt = new ArrayList<CategoryListResponse>();
        for (int i = 0; i < list.size(); i++) {
            CategoryListResponse cvd = new CategoryListResponse().id(UUID.fromString(list.get(i).getUuid())).categoryName(list.get(i).getCategoryName());
            lt.add(cvd);
        }

        final CategoriesListResponse categoryLists = new CategoriesListResponse().categories(lt);
        return new ResponseEntity<CategoriesListResponse>(categoryLists, HttpStatus.OK);
    }

    @RequestMapping(value = "/category/{category_id}", method = RequestMethod.GET)
    public ResponseEntity<CategoryDetailsResponse> getCategoryById(@PathVariable("category_id") String category_id) throws Exception {
        CategoryEntity p = cs.getCategoryById(category_id);
        List<ItemEntity> list = ISI.getItemByCategoryId(category_id, p);
        final List<ItemList> itemLists = list.stream()
                .map(developer -> new ItemList().id(UUID.fromString(developer.getUuid())).itemName(developer.getItemName()).itemType(ItemList.ItemTypeEnum.fromValue(developer.getType().toString())).price(developer.getPrice())).collect(Collectors.toList());

        CategoryDetailsResponse cl = new CategoryDetailsResponse().id(UUID.fromString(p.getUuid())).categoryName(p.getCategoryName()).itemList(itemLists);
        return new ResponseEntity<CategoryDetailsResponse>(cl, HttpStatus.OK);
    }
}