package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@Entity
@Table(name="restaurant")
public class RestaurantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Size(max = 200)
    private String uuid;
    @NotNull
    @Size(max = 50)
    private String restaurant_name;
    @NotNull
    @Size(max = 255)
    private String photo_url;
    @NotNull
    private Double customer_rating;
    @NotNull
    private Integer average_price_for_two;
    @NotNull
    private Integer number_of_customers_rated;
    @NotNull
    @ManyToOne
    @JoinColumn(name="address_id")
    private AddressEntity address_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRestaurantName() {
        return restaurant_name;
    }

    public void setRestaurantName(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getPhotoUrl() {
        return photo_url;
    }

    public void setPhotoUrl(String photo_url) {
        this.photo_url = photo_url;
    }

    public double getCustomerRating() {
        return customer_rating;
    }

    public void setCustomerRating(double customer_rating) {
        this.customer_rating = customer_rating;
    }

    public int getAvgPrice() {
        return average_price_for_two;
    }

    public void setAvgPrice(int average_price_for_two) {
        this.average_price_for_two = average_price_for_two;
    }

    public int getNumberCustomersRated() {
        return number_of_customers_rated;
    }

    public void setNumberCustomersRated(int number_of_customers_rated) {
        this.number_of_customers_rated = number_of_customers_rated;
    }

    public AddressEntity getAddress() {
        return address_id;
    }

    public void setAddress(AddressEntity address_id) {
        this.address_id = address_id;
    }
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}


