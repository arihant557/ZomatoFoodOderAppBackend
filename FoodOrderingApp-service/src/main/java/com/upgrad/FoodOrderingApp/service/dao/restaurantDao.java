package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class restaurantDao implements Serializable {
    @PersistenceUnit
    private EntityManagerFactory emf;
    public List<RestaurantEntity> resByName(String name){
        EntityManager em=emf.createEntityManager();
        String l="%"+name+"%";
        TypedQuery<RestaurantEntity> query = em.createQuery("SELECT p FROM RestaurantEntity p WHERE lower(p.restaurant_name) LIKE :uname order by p.restaurant_name", RestaurantEntity.class).setParameter("uname",l.toLowerCase());
        List<RestaurantEntity>s=query.getResultList();
        em.close();
        return s;
    }
    public RestaurantEntity resById(String id){
        EntityManager em=emf.createEntityManager();
        TypedQuery<RestaurantEntity> query = em.createQuery("SELECT p FROM RestaurantEntity p WHERE p.uuid LIKE :uid", RestaurantEntity.class).setParameter("uid",id);
        RestaurantEntity s=query.getSingleResult();
        em.close();
        return s;
    }
    public List<CategoryEntity> catByResId(String id){
        EntityManager em=emf.createEntityManager();
        TypedQuery<RestaurantEntity> query = em.createQuery("SELECT p FROM RestaurantEntity p WHERE p.uuid=:uid", RestaurantEntity.class).setParameter("uid",id);
        RestaurantEntity re=query.getSingleResult();
        TypedQuery<RestaurantCategoryEntity> q=em.createQuery("FROM RestaurantCategoryEntity p where p.restaurant_id=:uid ",RestaurantCategoryEntity.class).setParameter("uid",re.getId());
        List<RestaurantCategoryEntity> res = q.getResultList();

        List<CategoryEntity>it=new ArrayList<CategoryEntity>();
        for(int i=0;i<res.size();i++) {
            TypedQuery<CategoryEntity> que = em.createQuery("FROM CategoryEntity p where p.id=:uid order by category_name", CategoryEntity.class).setParameter("uid", res.get(i).getCategory_id());
            CategoryEntity rep = que.getSingleResult();
            it.add(rep);
        }

        return it;

    }
    public List<RestaurantEntity>getRestaurantbycategory(String id){
        EntityManager em=emf.createEntityManager();
        TypedQuery<CategoryEntity> que = em.createQuery("FROM CategoryEntity p where p.uuid=:uid ", CategoryEntity.class).setParameter("uid", id);
        CategoryEntity rep = que.getSingleResult();
        TypedQuery<RestaurantCategoryEntity> q=em.createQuery("FROM RestaurantCategoryEntity p where p.category_id=:uid ",RestaurantCategoryEntity.class).setParameter("uid",rep.getId());
        List<RestaurantCategoryEntity> res = q.getResultList();
        List<RestaurantEntity> it = new ArrayList<>();
        for(int i=0;i<res.size();i++) {
            TypedQuery<RestaurantEntity> query = em.createQuery("SELECT p FROM RestaurantEntity p where p.id =:uid", RestaurantEntity.class).setParameter("uid", res.get(i).getRestaurant_id());
            RestaurantEntity s = query.getSingleResult();
            it.add(s);
        }
        return it;
    }
    public List<RestaurantEntity>restaurantsByRating(){
        EntityManager em=emf.createEntityManager();
        TypedQuery<RestaurantEntity> query = em.createQuery("SELECT p FROM RestaurantEntity p order by number_of_customers_rated", RestaurantEntity.class);
        List<RestaurantEntity> s=query.getResultList();
        return s;
    }
}
