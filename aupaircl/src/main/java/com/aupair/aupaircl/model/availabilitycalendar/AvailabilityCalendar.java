package com.aupair.aupaircl.model.availabilitycalendar;

import com.aupair.aupaircl.model.availabilitystatus.AvailabilityStatus;
import com.aupair.aupaircl.model.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "apc_availability_calendar")
public class AvailabilityCalendar {

    @Id
    @Column(name = "id_calendar",nullable = false)
    @GeneratedValue(generator = "UUID")
    private UUID calendarId;

    @ManyToOne
    @JoinColumn(name = "fk_user", nullable = false)
    private User user;

    @Column(name = "available_date",nullable = false)
    private Date availableDate;

    @ManyToOne
    @JoinColumn(name = "fk_availability_status", nullable = false)
    private AvailabilityStatus availabilityStatus;
    @PrePersist
    private void generateUUID(){
        if(this.calendarId==null){
            this.calendarId=UUID.randomUUID();
        }
    }

}
