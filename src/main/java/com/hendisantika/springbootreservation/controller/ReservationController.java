package com.hendisantika.springbootreservation.controller;

import com.hendisantika.springbootreservation.annotation.ReservationForm;
import com.hendisantika.springbootreservation.domain.ReservableRoom;
import com.hendisantika.springbootreservation.domain.ReservableRoomId;
import com.hendisantika.springbootreservation.domain.Reservation;
import com.hendisantika.springbootreservation.exception.AlreadyReservedException;
import com.hendisantika.springbootreservation.exception.UnavailableReservationException;
import com.hendisantika.springbootreservation.repository.ReservableRoomRepository;
import com.hendisantika.springbootreservation.repository.ReservationRepository;
import com.hendisantika.springbootreservation.service.ReservationService;
import com.hendisantika.springbootreservation.service.ReservationUserDetails;
import com.hendisantika.springbootreservation.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Controller for handling conference room reservations
 * <p>
 * This controller manages:
 * - Displaying the reservation form for a specific room and date
 * - Creating new reservations
 * - Canceling existing reservations
 * - Validating reservation time slots
 * <p>
 * Uses constructor injection via Lombok's @RequiredArgsConstructor for better testability
 * and immutability of dependencies.
 * <p>
 * Created by IntelliJ IDEA.
 * Project : springboot-reservation
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 2025-10-25
 */
@Controller
@RequestMapping("reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final RoomService roomService;
    private final ReservableRoomRepository reservableRoomRepository;
    private final ReservationRepository reservationRepository;

    /**
     * Provides a list of available time slots for the reservation form
     * Time slots are in 30-minute increments from 00:00 to 23:30
     *
     * @return List of LocalTime objects representing available time slots
     */
    @ModelAttribute
    List<LocalTime> timeList() {
        return Stream.iterate(LocalTime.of(0, 0), t -> t.plusMinutes(30))
                .limit(24 * 2) // 48 slots (24 hours * 2 slots per hour)
                .collect(Collectors.toList());
    }

    /**
     * Display the reservation form for a specific room and date
     * <p>
     * GET /reservations/{date}/{roomId}
     *
     * @param date   The date for the reservation (format: yyyy-MM-dd)
     * @param roomId The ID of the meeting room
     * @param model  Spring MVC model for passing data to the view
     * @return The name of the Thymeleaf template to render
     */
    @GetMapping(path = "{date}/{roomId}")
    String reserveForm(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PathVariable("date") LocalDate date,
            @PathVariable("roomId") Integer roomId,
            Model model) {

        // Create composite key for the reservable room
        ReservableRoomId reservableRoomId = new ReservableRoomId(roomId, date);

        // Fetch the reservable room from the database
        Optional<ReservableRoom> reservableRoomOptional = reservableRoomRepository.findById(reservableRoomId);

        // Check if the room is available for the specified date
        if (reservableRoomOptional.isEmpty()) {
            model.addAttribute("error", "This room is not available for the selected date.");
            return "error/404"; // You can create a custom error page
        }

        ReservableRoom reservableRoom = reservableRoomOptional.get();

        // Fetch all existing reservations for this room and date
        List<Reservation> reservations = reservationService.findReservations(reservableRoomId);

        // Add attributes to the model for the Thymeleaf template
        model.addAttribute("date", date);
        model.addAttribute("roomId", roomId);
        model.addAttribute("room", reservableRoom.getMeetingRoom());
        model.addAttribute("reservations", reservations);
        model.addAttribute("reservationForm", new ReservationForm());

        return "reservation/reserveForm";
    }

    /**
     * Handle the submission of a new reservation
     * <p>
     * POST /reservations/{date}/{roomId}
     *
     * @param reservationForm The form data containing start and end times
     * @param bindingResult   Validation results for the form
     * @param date            The date for the reservation
     * @param roomId          The ID of the meeting room
     * @param userDetails     The currently authenticated user
     * @param model           Spring MVC model for passing data to the view
     * @return Redirect to the room list or back to the form with errors
     */
    @PostMapping(path = "{date}/{roomId}")
    String reserve(
            @Valid @ModelAttribute ReservationForm reservationForm,
            BindingResult bindingResult,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PathVariable("date") LocalDate date,
            @PathVariable("roomId") Integer roomId,
            @AuthenticationPrincipal ReservationUserDetails userDetails,
            Model model) {

        // Check for validation errors
        if (bindingResult.hasErrors()) {
            return setupFormModelWithErrors(date, roomId, model, "Please correct the highlighted errors.");
        }

        // Create composite key for the reservable room
        ReservableRoomId reservableRoomId = new ReservableRoomId(roomId, date);

        // Verify the room is available
        Optional<ReservableRoom> reservableRoomOptional = reservableRoomRepository.findById(reservableRoomId);
        if (reservableRoomOptional.isEmpty()) {
            return setupFormModelWithErrors(date, roomId, model,
                    "This room is not available for reservation on the selected date.");
        }

        ReservableRoom reservableRoom = reservableRoomOptional.get();

        // Create new reservation object
        Reservation reservation = new Reservation();
        reservation.setStartTime(reservationForm.getStartTime());
        reservation.setEndTime(reservationForm.getEndTime());
        reservation.setReservableRoom(reservableRoom);
        reservation.setUser(userDetails.getUser());

        // Attempt to save the reservation
        try {
            reservationService.reserve(reservation);
        } catch (UnavailableReservationException | AlreadyReservedException e) {
            // Handle business logic errors
            return setupFormModelWithErrors(date, roomId, model, e.getMessage());
        }

        // Success - redirect to the rooms list for this date
        return "redirect:/rooms/" + date;
    }

    /**
     * Handle cancellation of an existing reservation
     * <p>
     * POST /reservations/{date}/{roomId}?cancel
     *
     * @param reservationId The ID of the reservation to cancel
     * @param roomId        The ID of the meeting room
     * @param date          The date of the reservation
     * @param model         Spring MVC model for passing data to the view
     * @return Redirect back to the reservation form
     */
    @PostMapping(path = "{date}/{roomId}", params = "cancel")
    String cancel(
            @RequestParam("reservationId") Integer reservationId,
            @PathVariable("roomId") Integer roomId,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PathVariable("date") LocalDate date,
            Model model) {

        // Fetch the reservation to cancel
        Optional<Reservation> reservationOptional = reservationRepository.findById(reservationId);

        if (reservationOptional.isEmpty()) {
            return setupFormModelWithErrors(date, roomId, model, "Reservation not found.");
        }

        Reservation reservation = reservationOptional.get();

        // Attempt to cancel the reservation (authorization is checked in the service)
        try {
            reservationService.cancel(reservation);
        } catch (Exception e) {
            return setupFormModelWithErrors(date, roomId, model,
                    "Unable to cancel reservation: " + e.getMessage());
        }

        // Success - redirect back to the reservation form
        return "redirect:/reservations/" + date + "/" + roomId;
    }

    /**
     * Helper method to setup the form model when there's an error
     * This ensures all necessary data is available for re-rendering the form
     *
     * @param date         The reservation date
     * @param roomId       The room ID
     * @param model        The Spring MVC model
     * @param errorMessage The error message to display
     * @return The name of the template to render
     */
    private String setupFormModelWithErrors(LocalDate date, Integer roomId, Model model, String errorMessage) {
        ReservableRoomId reservableRoomId = new ReservableRoomId(roomId, date);

        Optional<ReservableRoom> reservableRoomOptional = reservableRoomRepository.findById(reservableRoomId);

        if (reservableRoomOptional.isEmpty()) {
            model.addAttribute("error", "Room not found.");
            return "error/404";
        }

        ReservableRoom reservableRoom = reservableRoomOptional.get();
        List<Reservation> reservations = reservationService.findReservations(reservableRoomId);

        // Add all necessary attributes
        model.addAttribute("error", errorMessage);
        model.addAttribute("date", date);
        model.addAttribute("roomId", roomId);
        model.addAttribute("room", reservableRoom.getMeetingRoom());
        model.addAttribute("reservations", reservations);

        return "reservation/reserveForm";
    }
}
