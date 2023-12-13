package com.app.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.app.entity.Tour;
import com.app.payload.request.TourQueryParam;
import com.app.payload.response.APIResponse;

public interface TourServices {
    APIResponse filterTour(TourQueryParam tourQueryParam);

    APIResponse filterTourDiscount(TourQueryParam tourQueryParam);

    APIResponse filterNewlyPosted(TourQueryParam tourQueryParam);

    APIResponse getAccountByTourId(Integer id, TourQueryParam tourQueryParam);

    APIResponse findbyid(Integer id);

    APIResponse create(Tour tour);

    APIResponse update(Tour tour);

    APIResponse delete(Integer id);

    APIResponse uploadExcel(MultipartFile excel);

    APIResponse createBatch(List<Tour> tours);

    APIResponse getRevenueTour(Integer id);
}
