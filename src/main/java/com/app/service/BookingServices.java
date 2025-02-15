package com.app.service;

import com.app.entity.Booking;
import com.app.entity.Voucher;
import com.app.payload.request.BookingQueryParam;
import com.app.payload.response.APIResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookingServices {
    APIResponse filterBooking (BookingQueryParam bookingQueryParam);
    APIResponse create(Booking booking);
    APIResponse update(Booking booking);
    APIResponse delete(Integer id);

    APIResponse uploadExcel(MultipartFile excel);
    APIResponse createBatch(List<Booking> bookings);

}
