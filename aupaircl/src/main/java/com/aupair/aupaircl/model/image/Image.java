package com.aupair.aupaircl.model.image;

import com.aupair.aupaircl.model.profile.Profile;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.PrePersist;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.print.attribute.standard.MediaSize;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "apc_images")
public class Image {

    @Id
    @Column(name = "id_image",nullable = false)
    @GeneratedValue(generator = "UUID")
    private UUID imageId;

    @Column(name = "image_name",nullable = false)
    private String imageName;

    @Column(name = "image_label",nullable = false)
    private String imageLabel;

    @ManyToOne
    @JoinColumn(name = "fk_profile", nullable = false)
    private Profile profile;
    @PrePersist
    private void generateUUID(){
        if(this.imageId==null){
            this.imageId= UUID.randomUUID();
        }
    }
}
