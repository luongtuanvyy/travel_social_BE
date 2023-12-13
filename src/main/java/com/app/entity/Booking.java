package com.app.entity;

import com.app.type.EBooking;
import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "BOOKING")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Booking extends BaseEntity {

    private String note;

    private Integer adult;

    private Integer children;

    private Integer baby;

    private String qr;

    private EBooking status;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "ID")
    private Account accountId;

    @ManyToOne
    @JoinColumn(name = "TOUR_ID", referencedColumnName = "ID")
    private Tour tourId;

    @ManyToOne
    @JoinColumn(name = "PAYMENT_ID", referencedColumnName = "ID")
    private Payment paymentId;
}
