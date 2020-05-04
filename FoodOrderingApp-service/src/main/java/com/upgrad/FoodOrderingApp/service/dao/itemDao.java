package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.businness.ItemService;
import com.upgrad.FoodOrderingApp.service.entity.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Repository
public class itemDao implements Serializable {
    @PersistenceUnit
    private EntityManagerFactory emf;
    public List<ItemEntity> getItemsByCategory(String cat_id,CategoryEntity cs){
        System.out.println("heeeeep"+cat_id);
        EntityManager em = emf.createEntityManager();
        TypedQuery<CategoryItemEntity> q=em.createQuery("FROM CategoryItemEntity p where p.category_id=:uid ",CategoryItemEntity.class).setParameter("uid",cs.getId());
        List<CategoryItemEntity> res = q.getResultList();
        List<ItemEntity>it=new ArrayList<ItemEntity>();
        for(int i=0;i<res.size();i++) {
            TypedQuery<ItemEntity> que = em.createQuery("FROM ItemEntity p where p.id=:uid ", ItemEntity.class).setParameter("uid", res.get(i).getItem_id());
            ItemEntity re = que.getSingleResult();
            it.add(re);
        }

        return it;
    }
    public List<ItemEntity> getItemsByCategoryandRestaurant(RestaurantEntity re,CategoryEntity res){
        EntityManager em = emf.createEntityManager();
        TypedQuery<CategoryItemEntity> q=em.createQuery("FROM CategoryItemEntity p where p.category_id=:uid ",CategoryItemEntity.class).setParameter("uid",res.getId());
        List<CategoryItemEntity> rept=q.getResultList();
        TypedQuery<RestaurantItemEntity> qu=em.createQuery("FROM RestaurantItemEntity p where p.restaurant_id=:uid",RestaurantItemEntity.class).setParameter("uid",re.getId());
        List<RestaurantItemEntity> rep=qu.getResultList();
        List<RestaurantItemEntity>ptp=new ArrayList<>();
        for(int i=0;i<rept.size();i++) {
            try {
                TypedQuery<RestaurantItemEntity> qur = em.createQuery("FROM RestaurantItemEntity p where p.restaurant_id=:rid and p.Item_id=:uid ", RestaurantItemEntity.class).setParameter("uid", rept.get(i).getItem_id()).setParameter("rid", re.getId());
                RestaurantItemEntity rpq = qur.getSingleResult();
                ptp.add(rpq);
            }
            catch(Exception e){
                continue;
            }

        }
        List<ItemEntity>itp=new ArrayList<>();
        for(int i=0;i<ptp.size();i++) {
            TypedQuery<ItemEntity> qurp = em.createQuery("SELECT p FROM ItemEntity p where p.id=:uid", ItemEntity.class).setParameter("uid", ptp.get(i).getItem_id());
            ItemEntity rpp = qurp.getSingleResult();
            itp.add(rpp);
        }
        return itp;
    }
}
