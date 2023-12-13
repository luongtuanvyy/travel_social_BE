package com.app.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "TOUR")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter

public class Tour extends BaseEntity {
    private String name;
    private String departure;
    private String image;
    private Integer size;
    private Integer registered;
    private String vehicle;
    private BigDecimal adult;
    private BigDecimal children;
    private BigDecimal baby;
    private Integer discount;
    @Column(name = "CLOUDINARY_ID")
    private String cloudinaryId;
    @Column(name = "START_DATE_BOOKING")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime startDateBooking;
    @Column(name = "END_DATE_BOOKING")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime endDateBooking;
    @Column(name = "START_DATE")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime startDate;
    @Column(name = "END_DATE")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "TOUR_GUIDE_ID", referencedColumnName = "ID")
    private TourGuide tourGuideId;

//    @ManyToOne
//    @JoinColumn(name = "TOUR_TEMPLATE_ID", referencedColumnName = "ID")
//    private TourTemplate tourTemplateId;


//    @OneToMany(mappedBy = "tour", fetch = FetchType.EAGER)
//    @JsonManagedReference
//    @Fetch(FetchMode.SUBSELECT)
//    private List<Favorite> favoriteList;
//
//    @OneToMany(mappedBy = "tour", fetch = FetchType.EAGER)
//    @JsonManagedReference
//    @Fetch(FetchMode.SUBSELECT)
//    private List<Review> reviewList;
//
//    @OneToMany(mappedBy = "tour", fetch = FetchType.EAGER)
//    @JsonManagedReference
//    @Fetch(FetchMode.SUBSELECT)
//    private List<TourDetail> tourDetailList;

}























