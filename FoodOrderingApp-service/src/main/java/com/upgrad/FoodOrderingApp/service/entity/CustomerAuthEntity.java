package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;
import java.time.ZonedDateTime;
import java.util.Date;

@Entity
@Table(name="customer_auth")
public class CustomerAuthEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Size(max = 200)
    private String uuid;
    @NotNull
    @OneToOne
    @JoinColumn(name="customer_id")
    private CustomerEntity customer_id;

    @NotNull
    @Size(max = 500)
    private String access_token;
    @NotNull
    private ZonedDateTime login_at;

    private ZonedDateTime logout_at;
    @NotNull
    private ZonedDateTime expires_at;

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

    public CustomerEntity getCustomer() {
        return customer_id;
    }

    public void setCustomer(CustomerEntity customer_id) {
        this.customer_id = customer_id;
    }

    public String getAccessToken() {
        return access_token;
    }

    public void setAccessToken(String access_token) {
        this.access_token = access_token;
    }

    public ZonedDateTime getLogin_at() {
        return login_at;
    }

    public void setLogin_at(ZonedDateTime login_at) {
        this.login_at = login_at;
    }

    public ZonedDateTime getLogout_at() {
        return logout_at;
    }

    public void setLogout_at(ZonedDateTime logout_at) {
        this.logout_at = logout_at;
    }

    public ZonedDateTime getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(ZonedDateTime expires_at) {
        this.expires_at = expires_at;
    }
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
