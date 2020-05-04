package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Repository
public class categoryDao implements Serializable {
    @PersistenceUnit
    private EntityManagerFactory emf;

    public List<CategoryEntity> getAllCategory() {
        System.out.println("HELLO");
        EntityManager em = emf.createEntityManager();
        TypedQuery<CategoryEntity> query = em.createQuery("Select p from CategoryEntity p order by p.category_name", CategoryEntity.class);
        List<CategoryEntity> resultList = query.getResultList();
        return resultList;
    }
    public CategoryEntity getCategoryId(String id){
        CategoryEntity s = null;
        try {

            EntityManager em = emf.createEntityManager();
            TypedQuery<CategoryEntity> query = em.createQuery("SELECT p FROM CategoryEntity p WHERE p.uuid =:uid", CategoryEntity.class).setParameter("uid", id);
            s = query.getSingleResult();
            if(s==null)
                throw new CategoryNotFoundException("CNF-002","No category by this id");
            em.close();
            //return s;
        }
        catch(CategoryNotFoundException e){
            System.out.println(e.getCode()+" "+e.getErrorMessage());

        }
        return s;
    }
    public List<ItemEntity> getItemsByCategory(String cat_id){

        EntityManager em = emf.createEntityManager();
        TypedQuery<ItemEntity> query = em.createNamedQuery("SELECT p from ItemEntity p where p.id:=uid", ItemEntity.class).setParameter("uid",1);
        List<ItemEntity> resultList = query.getResultList();
        return resultList;
    }


}
