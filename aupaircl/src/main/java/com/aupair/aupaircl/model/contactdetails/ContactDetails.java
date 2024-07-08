package com.aupair.aupaircl.model.contactdetails;

import com.aupair.aupaircl.model.profile.Profile;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "apc_contact_details")
public class ContactDetails {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id_contact_details",length = 16)
    private UUID idContactDetails;
    @Column(name = "street")
    private String street;
    @Column(name = "zip_code")
    private String zipCode;
    @Column(name = "city")
    private String city;
    @Column(name = "province")
    private String province;
    @Column (name = "phone")
    private String phone;
    @Column(name = "created_at")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Date createdAt;
    @OneToOne
    @JoinColumn(name = "fk_profile", referencedColumnName = "id_profile")
    private Profile profile;
    @PrePersist
    private void generateUUID(){
        Date currentDateAddress = new Date();
        if (this.createdAt==null){
            this.createdAt=currentDateAddress;
        }

        if(this.idContactDetails==null){
            this.idContactDetails= UUID.randomUUID();
        }
    }

}
