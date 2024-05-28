package com.airbnb.controller;

import com.airbnb.entity.Booking;
import com.airbnb.entity.Property;
import com.airbnb.entity.PropertyUser;
import com.airbnb.repository.BookingRepository;
import com.airbnb.repository.PropertyRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {
    private BookingRepository bookingRepository;
    private PropertyRepository propertyRepository;

    public BookingController(BookingRepository bookingRepository, PropertyRepository propertyRepository) {
        this.bookingRepository = bookingRepository;
        this.propertyRepository = propertyRepository;
    }
    @PostMapping("/createBooking/{propertyId}")
    public ResponseEntity<Booking>createBooking(@RequestBody Booking booking,
                                                @AuthenticationPrincipal PropertyUser user,
                                                @PathVariable long propertyId,
                                                @RequestParam("checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
                                                @RequestParam("checkInTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime checkInTime,
                                                @RequestParam("checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
                                                @RequestParam("checkOutTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime checkOutTime) {

        booking.setPropertyUser(user);
        Property property = propertyRepository.findById(propertyId).get();
        int nightlyPrice = property.getNightlyPrice();
        int totalNights = (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        int totalPrice= nightlyPrice*totalNights;
        booking.setProperty(property);
        booking.setTotalPrice(totalPrice);
        booking.setCheckInDate(checkInDate);
        booking.setCheckOutDate(checkOutDate);
        booking.setCheckInTime(checkInTime);
        booking.setCheckOutTime(checkOutTime);

        Booking createdBooking = bookingRepository.save(booking);

        //create pdf with booking confirmation

        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);

    }
}
