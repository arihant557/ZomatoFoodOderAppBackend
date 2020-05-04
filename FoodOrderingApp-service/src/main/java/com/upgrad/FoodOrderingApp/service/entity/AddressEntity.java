package com.upgrad.FoodOrderingApp.service.entity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="address")
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Size(max = 200)
    private String uuid;
    @NotNull
    @Size(max = 255)
    private String flat_buil_number;
    @NotNull
    @Size(max = 255)
    private String locality;
    @NotNull
    @Size(max = 30)
    private String city;
    @NotNull
    @Size(max = 30)
    private String pincode;
    @NotNull
    @ManyToOne
    @JoinColumn(name="state_id")
    private StateEntity state_id;
    public AddressEntity(){
        this.id=null;
        this.uuid=null;
        this.flat_buil_number=null;
        this.locality=null;
        this.city=null;
        this.pincode=null;
        this.state_id=null;
    }
    public AddressEntity(String addressId, String f, String locality,String city,String pincode,StateEntity stateEntity){
        this.uuid=addressId;
        this.locality=locality;
        this.city=city;
        this.state_id=stateEntity;
        this.flat_buil_number=f;
        this.pincode=pincode;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFlatBuilNo() {
        return flat_buil_number;
    }

    public void setFlatBuilNo(String flat_build_number) {
        this.flat_buil_number = flat_build_number;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public StateEntity getState() {
        return state_id;
    }

    public void setState(StateEntity state_id) {
        this.state_id = state_id;
    }
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}

