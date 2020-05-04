package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.Serializable;

@Repository
public class customerAuthTokenDao implements Serializable {
    @PersistenceContext
    private EntityManager entityManager;
    @PersistenceUnit
    private EntityManagerFactory emf;
    @Transactional
    public CustomerAuthEntity create(final CustomerAuthEntity newToken) {
        EntityManager em=emf.createEntityManager();
        EntityTransaction tx= em.getTransaction();
        try{
            tx.begin();
            em.persist(newToken);
            tx.commit();

        }catch (Exception e){
            tx.rollback();
            System.out.println(e);
        }
        return newToken;
    }

    public CustomerAuthEntity getAuthTokenByAccessToken(String authToken) {
        try {
            EntityManager em=emf.createEntityManager();
            TypedQuery<CustomerAuthEntity> query= em.createQuery("From CustomerAuthEntity p where p.access_token=:accessToken", CustomerAuthEntity.class).setParameter("accessToken", authToken);
            CustomerAuthEntity cae=query.getSingleResult();
            return cae;
        } catch (NoResultException e) {

            return null;

        }
    }
    @Transactional
    public void updatedCustomer(CustomerAuthEntity updatedUser) {
        EntityManager em=emf.createEntityManager();
        EntityTransaction tx= em.getTransaction();
        try{
            tx.begin();
            entityManager.merge(updatedUser);
            tx.commit();

        }catch (Exception e){
            tx.rollback();
            System.out.println(e);
        }

    }
}
