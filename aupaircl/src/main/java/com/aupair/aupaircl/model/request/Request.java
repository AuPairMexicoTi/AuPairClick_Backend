package com.aupair.aupaircl.model.request;

import com.aupair.aupaircl.model.requeststatus.RequestStatus;
import com.aupair.aupaircl.model.user.User;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "apc_requests")
public class Request {

    @Id
    @Column(name = "id_request",nullable = false)
    @GeneratedValue(generator = "UUID")
    private UUID requestId;

    @ManyToOne
    @JoinColumn(name = "fk_au_pair", nullable = false)
    private User auPair;

    @ManyToOne
    @JoinColumn(name = "fk_host_family", nullable = false)
    private User hostFamily;
    @OneToOne
    @JoinColumn(name = "fk_request_status")
    private RequestStatus requestStatus;
    @Column(nullable = false)
    private Date requestDate;
    @PrePersist
    private void generateUUID(){
        if(requestDate == null){
            requestDate=new Date();
        }
        if(this.requestId == null){
            this.requestId = UUID.randomUUID();
        }
    }
}
