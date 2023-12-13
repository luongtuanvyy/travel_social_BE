package com.app.payload.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlogModalQueryParam extends BaseQueryRequest{
    Integer id;
    String createdBy;
}
