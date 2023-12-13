package com.app.api;

import com.app.entity.Review;
import com.app.entity.Tour;
import com.app.payload.request.TourQueryParam;
import com.app.payload.response.APIResponse;
import com.app.service.TourServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TourApi {

    @Autowired
    TourServices tourServices;

    @GetMapping("/public/tours")
    public ResponseEntity<?> filter(TourQueryParam tourQueryParam) {
        return ResponseEntity.ok(tourServices.filterTour(tourQueryParam));
    }

    @GetMapping("/public/tours/findById")
    public ResponseEntity<?> findById(@RequestParam("id") Integer id) {
        return ResponseEntity.ok(tourServices.findbyid(id));
    }

    @GetMapping("/public/tours/getAccount")
    public ResponseEntity<?> getAccount(@RequestParam("id") Integer id, TourQueryParam tourQueryParam) {
        return ResponseEntity.ok(tourServices.getAccountByTourId(id, tourQueryParam));
    }

    @GetMapping("/public/tours/nullDiscount")
    public ResponseEntity<?> filterTourDiscount(TourQueryParam tourQueryParam) {
        return ResponseEntity.ok(tourServices.filterTourDiscount(tourQueryParam));
    }

    @GetMapping("/public/tours/filterNewlyPosted")
    public ResponseEntity<?> filterNewlyPosted(TourQueryParam tourQueryParam) {
        return ResponseEntity.ok(tourServices.filterNewlyPosted(tourQueryParam));
    }

    @PostMapping("/company/tours")
    public ResponseEntity<?> createTour(@RequestPart(name = "tour") Tour tour) {
        APIResponse response = tourServices.create(tour);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/company/tours")
    public ResponseEntity<?> updateTour(@RequestPart(name = "tour") Tour tour) {
        APIResponse response = tourServices.update(tour);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/company/tours")
    public ResponseEntity<?> deleteTour(@RequestParam("id") Integer id) {
        APIResponse response = tourServices.delete(id);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/company/tours/upload-excel")
    public ResponseEntity<?> uploadExcel(@RequestPart(name = "excel") MultipartFile excel) {
        APIResponse response = tourServices.uploadExcel(excel);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/company/tours/batch")
    public ResponseEntity<?> createToursBatch(@RequestBody List<Tour> tours) {
        APIResponse response = tourServices.createBatch(tours);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/public/tours/getRevenue")
    public ResponseEntity<?> getRevenueTour(@RequestParam("id") String id) {
        try {
            int tourId = Integer.parseInt(id);
            if (tourId < 0) {
                return ResponseEntity.badRequest().body("Invalid tour id");
            }
            APIResponse response = tourServices.getRevenueTour(tourId);
            return ResponseEntity.ok().body(response);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid tour id");
        }
    }

}
