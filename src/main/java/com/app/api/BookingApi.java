package com.app.api;

import com.app.entity.BlogReaction;
import com.app.entity.Booking;
import com.app.payload.request.BookingQueryParam;
import com.app.payload.response.APIResponse;
import com.app.service.BookingServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BookingApi {
    @Autowired
    BookingServices bookingService;

    @GetMapping("/user/bookings")
    public ResponseEntity<?> filterBooking(BookingQueryParam bookingQueryParam) {
        return ResponseEntity.ok(bookingService.filterBooking(bookingQueryParam));
    }

    @PostMapping("/user/bookings")
    public ResponseEntity<?> createBooking(@RequestPart(name = "booking") Booking booking) {
        APIResponse response = bookingService.create(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/user/bookings")
    public ResponseEntity<?> updateBooking(@RequestPart(name = "booking") Booking booking) {
        APIResponse response = bookingService.update(booking);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/user/bookings")
    public ResponseEntity<?> deleteBooking(@RequestParam("id") Integer id) {
        APIResponse response = bookingService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/user/bookings/upload-excel")
    public ResponseEntity<?> uploadExcel(@RequestPart(name = "excel") MultipartFile excel) {
        APIResponse response = bookingService.uploadExcel(excel);
        return ResponseEntity.ok().body(response);
    }
    @PostMapping("/user/bookings/batch")
    public ResponseEntity<?> createBookingsBatch(@RequestBody List<Booking> bookings) {
        APIResponse response = bookingService.createBatch(bookings);
        return ResponseEntity.ok().body(response);
    }
}
