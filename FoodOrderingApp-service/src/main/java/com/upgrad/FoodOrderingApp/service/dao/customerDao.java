package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.Serializable;

@Repository
public class customerDao implements Serializable {
    @PersistenceContext
    private EntityManager entityManager;
    @PersistenceUnit
    private EntityManagerFactory emf;
    @Transactional
    public CustomerEntity createUser(CustomerEntity userEntity) {
        EntityManager em=emf.createEntityManager();
        EntityTransaction tx= em.getTransaction();
        try{
            tx.begin();
            em.persist(userEntity);
            tx.commit();

        }catch (Exception e){
            tx.rollback();
            System.out.println(e);
        }
        return userEntity;
    }
    public boolean checkContact(String contact_number){

        EntityManager em=emf.createEntityManager();
        TypedQuery<CustomerEntity> query= em.createQuery("From CustomerEntity p where p.contact_number=:contact_number",CustomerEntity.class)
                .setParameter("contact_number",contact_number);
        try {
            CustomerEntity userEntity = query.getSingleResult();
            em.close();
            return true;
        } catch(Exception e) {
            return false;
        }
    }
    public CustomerEntity getUserByContact(String contact_number) {
        //Integer cn=Integer.parseInt(username);
        EntityManager em=emf.createEntityManager();
        TypedQuery<CustomerEntity> query= em.createQuery("From CustomerEntity p where p.contact_number=:contact_number",CustomerEntity.class)
                .setParameter("contact_number",contact_number);
        try {
            CustomerEntity userEntity = query.getSingleResult();
            em.close();
            return userEntity;
        } catch(Exception e) {
            return null;
        }

    }

    public void updateCustomer(CustomerEntity updatedUser) {
        EntityManager em=emf.createEntityManager();

        EntityTransaction tx= em.getTransaction();
        try{
            tx.begin();
            em.merge(updatedUser);
            tx.commit();

        }catch (Exception e){
            tx.rollback();
            System.out.println(e);
        }
    }

    public CustomerEntity getCustomer(final String access_token) {
        try {
            EntityManager em=emf.createEntityManager();
            CustomerEntity userEntity = em.createNamedQuery("userByToken", CustomerEntity.class)
                    .setParameter("access_token", access_token).getSingleResult();
            return userEntity;
        }catch (NoResultException e){
            return null;
        }
    }
    public CustomerEntity getCustomerByUUid(String id){
        EntityManager em=emf.createEntityManager();
        TypedQuery<CustomerEntity> query = em.createQuery("SELECT p FROM CustomerEntity p WHERE p.uuid =:uid", CustomerEntity.class).setParameter("uid",id);
        CustomerEntity s=query.getSingleResult();
        em.close();
        return s;
    }
}


