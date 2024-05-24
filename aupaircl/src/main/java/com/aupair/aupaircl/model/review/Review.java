package com.aupair.aupaircl.model.review;

import com.aupair.aupaircl.model.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "apc_reviews")
public class Review {

    @Id
    @Column(name = "id_review")
    @GeneratedValue(generator = "UUID")
    private UUID reviewId;

    @ManyToOne
    @JoinColumn(name = "fk_reviewer", nullable = false)
    private User reviewer;

    @ManyToOne
    @JoinColumn(name = "fk_reviewee", nullable = false)
    private User reviewee;

    @Column(name = "rating",nullable = false)
    private Integer rating;
    @Column(name = "comment")
    private String comment;

    @Column(name = "review_date",nullable = false)
    private Date reviewDate;

    @PrePersist
    private void generateUUID(){
        if(this.reviewDate==null){
            this.reviewDate = new Date();
        }
        if(this.reviewId==null){
            this.reviewId = UUID.randomUUID();
        }
    }
}
