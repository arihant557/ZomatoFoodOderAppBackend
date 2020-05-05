package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
// class AddressDao implements Serializable
@Repository
public class AddressDao implements Serializable {
    @PersistenceContext
    private EntityManager entityManager;
    @PersistenceUnit
    private EntityManagerFactory emf;
    public AddressEntity addAddress(AddressEntity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
    }
    public StateEntity getState(String uuid){
        EntityManager em=emf.createEntityManager();
        TypedQuery<StateEntity> query=em.createQuery("Select p from StateEntity p where p.uuid Like :uuid",StateEntity.class).setParameter("uuid",uuid);
        StateEntity result=query.getSingleResult();
        em.close();
        return result;
    }
    public List<AddressEntity> getAllAddress(CustomerEntity c){

        EntityManager em=emf.createEntityManager();
        TypedQuery<AddressEntity> query=em.createQuery("Select p from AddressEntity p where p.uuid Like :uuid",AddressEntity.class).setParameter("uuid",c.getUuid());
        List<AddressEntity> resultList=query.getResultList();
        return resultList;
    }
    public AddressEntity delete(AddressEntity id)throws AddressNotFoundException{
        AddressEntity s=null;
        AddressEntity p=null;
        EntityManager em=emf.createEntityManager();
        s=em.find(AddressEntity.class,id);
        s=p;
        if(s==null)
            throw new AddressNotFoundException("ANF-003","No address by this id");
        em.remove(s);
        em.close();
        return p;
    }
    public AddressEntity getAddress(String id)throws AddressNotFoundException{
        AddressEntity s=null;
        EntityManager em=emf.createEntityManager();
        s=em.find(AddressEntity.class,id);
        if(s==null)
            throw new AddressNotFoundException("ANF-003","No address by this id");
        em.close();
        return s;
    }
}
