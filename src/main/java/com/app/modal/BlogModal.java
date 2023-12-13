package com.app.modal;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogModal {
    private int id;
    private Date createdAt;
    private String createdBy;
    private boolean isActivated;
    private Date modifiedAt;
    private String modifiedBy;
    private String cloudinaryId;
    private String description;
    private String image;
    private String avatar;
    private String name;
    private boolean isVerify;
    private long totalLike;
    private long totalComment;
    private long totalShare;
}
