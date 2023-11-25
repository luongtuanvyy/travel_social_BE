package com.app.payload.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewQueryParam extends BaseQueryRequest {
    Float rating;
    Integer tour_id;
}
