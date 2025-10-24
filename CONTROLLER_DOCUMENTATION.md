# ReservationController Documentation

## Overview

The `ReservationController` is a comprehensive Spring MVC controller that handles all reservation-related operations for
the conference room booking system. It manages displaying reservation forms, creating new reservations, and canceling
existing reservations with proper validation and error handling.

## Package

```java
com.hendisantika.springbootreservation.controller
```

## Class Annotations

```java
@Controller
@RequestMapping("reservations")
```

- `@Controller`: Marks this class as a Spring MVC controller
- `@RequestMapping("reservations")`: All endpoints in this controller are prefixed with `/reservations`

## Dependencies

The controller uses the following services and repositories:

- `ReservationService`: Business logic for reservations
- `RoomService`: Business logic for rooms
- `ReservableRoomRepository`: Database access for reservable rooms
- `ReservationRepository`: Database access for reservations

## Endpoints

### 1. Display Reservation Form

**GET** `/reservations/{date}/{roomId}`

Shows the reservation form for a specific meeting room on a specific date.

#### Path Parameters:

- `date` (LocalDate): The reservation date in format `yyyy-MM-dd`
- `roomId` (Integer): The unique identifier of the meeting room

#### Returns:

- Template: `reservation/reserveForm`
- Status: 200 OK (success) or 404 (room not available)

#### Model Attributes:

- `date`: The selected date
- `roomId`: The room identifier
- `room`: MeetingRoom object with room details
- `reservations`: List of existing reservations for this room/date
- `reservationForm`: Empty form object for data binding
- `timeList`: List of available time slots (30-minute intervals)

#### Example URLs:

```
http://localhost:8080/reservations/2025-10-25/1
http://localhost:8080/reservations/2025-11-01/3
```

#### Business Logic:

1. Creates composite key (roomId + date)
2. Verifies room is reservable on that date
3. Fetches all existing reservations
4. Populates model with data
5. Returns form template

#### Error Handling:

- If room not found: Shows 404 error page
- Error message displayed in UI

---

### 2. Create New Reservation

**POST** `/reservations/{date}/{roomId}`

Processes the submission of a new reservation request.

#### Path Parameters:

- `date` (LocalDate): The reservation date
- `roomId` (Integer): The meeting room ID

#### Form Parameters (from ReservationForm):

- `startTime` (LocalTime): Start time in format `HH:mm`
- `endTime` (LocalTime): End time in format `HH:mm`

#### Validation:

- ✅ Start time and end time are required
- ✅ Times must be in 30-minute increments
- ✅ End time must be after start time
- ✅ No overlapping with existing reservations
- ✅ Room must be available on that date

#### Returns:

- Success: Redirects to `/rooms/{date}` (room list)
- Failure: Returns to form with error message

#### Security:

- User must be authenticated
- Reservation is automatically linked to logged-in user

#### Example Request:

```http
POST /reservations/2025-10-25/2
Content-Type: application/x-www-form-urlencoded

startTime=09:00&endTime=11:00
```

#### Business Logic Flow:

1. **Validate form data**
    - Check for field errors
    - Verify custom validations pass

2. **Verify room availability**
    - Check room exists
    - Check date is valid

3. **Create reservation object**
    - Set start/end times
    - Link to reservable room
    - Link to current user

4. **Attempt to save**
    - Check for time overlaps
    - Check room capacity
    - Save to database

5. **Handle result**
    - Success → Redirect to rooms list
    - Failure → Show error, stay on form

#### Error Cases:

| Error Type       | Message Example                         | Action       |
|------------------|-----------------------------------------|--------------|
| Validation Error | "Please correct the highlighted errors" | Stay on form |
| Room Unavailable | "Room not available for reservation"    | Stay on form |
| Time Overlap     | "The time of entry is already reserved" | Stay on form |
| General Error    | Service-specific message                | Stay on form |

---

### 3. Cancel Reservation

**POST** `/reservations/{date}/{roomId}?cancel`

Cancels an existing reservation.

#### Path Parameters:

- `date` (LocalDate): The reservation date
- `roomId` (Integer): The meeting room ID

#### Request Parameters:

- `cancel` (present in URL): Triggers cancel action
- `reservationId` (Integer): ID of reservation to cancel

#### Authorization:

Only allowed if:

- ✅ User owns the reservation, OR
- ✅ User has ADMIN role

Authorization is enforced via `@PreAuthorize` in the service layer.

#### Returns:

- Success: Redirects to `/reservations/{date}/{roomId}`
- Failure: Shows error message on form

#### Example Request:

```http
POST /reservations/2025-10-25/2?cancel
Content-Type: application/x-www-form-urlencoded

reservationId=42
```

#### Business Logic:

1. Fetch reservation by ID
2. Verify reservation exists
3. Check user authorization (in service)
4. Delete reservation
5. Redirect back to form

#### Security Note:

The `@PreAuthorize` annotation in `ReservationService.cancel()` ensures that only authorized users can cancel:

```java
@PreAuthorize("hasRole('ADMIN') or #reservation.user.userId == principal.user.userId")
public void cancel(@Param("reservation") Reservation reservation)
```

---

## Helper Methods

### timeList() - @ModelAttribute

Provides a list of time slots for the dropdown menus.

```java
@ModelAttribute
List<LocalTime> timeList()
```

**Returns:** 48 time slots (00:00 to 23:30 in 30-minute increments)

**Usage:** Automatically added to every model in this controller

---

### setupFormModelWithErrors()

Private helper method to prepare the model when errors occur.

```java
private String setupFormModelWithErrors(
    LocalDate date,
    Integer roomId,
    Model model,
    String errorMessage)
```

**Purpose:** Ensures all necessary data is available when re-displaying the form after an error.

**Adds to Model:**

- `error`: Error message to display
- `date`: The selected date
- `roomId`: The room ID
- `room`: MeetingRoom details
- `reservations`: Current reservations list

**Returns:** Template name or error page

---

## Integration with Services

### ReservationService Methods Used:

1. **findReservations(reservableRoomId)**
    - Finds all reservations for a specific room and date
    - Returns ordered by start time

2. **reserve(reservation)**
    - Validates and saves a new reservation
    - Throws `AlreadyReservedException` if time overlap
    - Throws `UnavailableReservationException` if room unavailable

3. **cancel(reservation)**
    - Deletes a reservation
    - Checks authorization
    - Throws exception if unauthorized

### RoomService Methods Used:

Currently minimal usage, but available for future expansion like:

- Getting room details
- Checking room capacity
- Validating room features

---

## Exception Handling

The controller handles these business exceptions:

### AlreadyReservedException

**Thrown when:** Time slot is already booked
**Handling:** Display user-friendly error message, stay on form

### UnavailableReservationException

**Thrown when:** Room not available for booking
**Handling:** Display error message, stay on form

### General Exceptions

**Thrown when:** Unexpected errors
**Handling:** Log error, display generic message

---

## URL Mapping Examples

### View Reservation Form

```
GET /reservations/2025-10-25/1
GET /reservations/2025-12-15/5
```

### Make a Reservation

```
POST /reservations/2025-10-25/1
Body: startTime=14:00&endTime=16:00
```

### Cancel a Reservation

```
POST /reservations/2025-10-25/1?cancel
Body: reservationId=123
```

---

## Workflow Diagrams

### Making a Reservation Flow

```
User clicks room → GET /reservations/{date}/{roomId}
                           ↓
                   Show reservation form
                   (with existing bookings)
                           ↓
User selects times → POST /reservations/{date}/{roomId}
                           ↓
                   Validate form data
                           ↓
                ┌──────────┴──────────┐
                ↓                     ↓
           Valid Data            Invalid Data
                ↓                     ↓
        Check availability    Show form with errors
                ↓
        ┌───────┴──────┐
        ↓              ↓
  Available      Unavailable
        ↓              ↓
  Save booking    Show error
        ↓
  Redirect to
  rooms list
```

### Canceling a Reservation Flow

```
User clicks Cancel → POST /reservations/{date}/{roomId}?cancel
                              ↓
                   Find reservation by ID
                              ↓
                      Check authorization
                              ↓
                  ┌───────────┴──────────┐
                  ↓                      ↓
            Authorized              Unauthorized
                  ↓                      ↓
         Delete reservation        Show error
                  ↓
         Redirect to form
```

---

## Validation Rules

### Form-Level Validation

Implemented in `ReservationForm` class:

1. **@NotNull**: Both start and end times required
2. **@ThirtyMinutesUnit**: Times must be in 30-minute intervals
3. **@EndTimeMustBeAfterStartTime**: Logical time ordering

### Business-Level Validation

Implemented in `ReservationService`:

1. **Room availability**: Must exist and be reservable
2. **No overlaps**: Check against existing reservations
3. **Time constraints**: Additional business rules

---

## Security Considerations

1. **Authentication Required**: All endpoints require login
2. **CSRF Protection**: Enabled for all POST requests
3. **Authorization Checks**: Cancel only if owner or admin
4. **SQL Injection**: Protected via JPA/Hibernate
5. **XSS Protection**: Thymeleaf auto-escapes output

---

## Future Enhancements

Potential improvements for the controller:

1. **REST API Support**
    - Add `@RestController` variant
    - JSON request/response
    - API versioning

2. **Async Processing**
    - Email notifications on booking
    - Calendar integration
    - Webhook callbacks

3. **Advanced Validation**
    - Business hours checking
    - Maximum duration limits
    - Blackout dates

4. **Bulk Operations**
    - Multiple room booking
    - Recurring reservations
    - Batch cancellations

5. **Analytics**
    - Room utilization tracking
    - Popular time slots
    - User booking patterns

6. **Internationalization**
    - Multi-language support
    - Timezone handling
    - Locale-specific date formats

---

## Testing

### Unit Testing Example

```java
@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @Test
    void testShowReservationForm() throws Exception {
        // Arrange
        LocalDate date = LocalDate.of(2025, 10, 25);
        Integer roomId = 1;

        // Act & Assert
        mockMvc.perform(get("/reservations/{date}/{roomId}", date, roomId))
            .andExpect(status().isOk())
            .andExpect(view().name("reservation/reserveForm"))
            .andExpect(model().attributeExists("room"))
            .andExpect(model().attributeExists("reservations"));
    }

    @Test
    void testMakeReservation() throws Exception {
        // Test reservation creation...
    }
}
```

---

## Troubleshooting

### Common Issues

**Issue:** Form shows "Required" error even with values
**Solution:** Check date/time format matches pattern

**Issue:** "Already reserved" error when slot looks empty
**Solution:** Verify database has correct timezone

**Issue:** Cancel button doesn't appear
**Solution:** Check user authorization and Thymeleaf security tags

**Issue:** 404 error on submission
**Solution:** Verify CSRF token is included in form

---

## Related Files

- **Service**: `ReservationService.java`
- **Repository**: `ReservationRepository.java`, `ReservableRoomRepository.java`
- **Domain**: `Reservation.java`, `ReservableRoom.java`
- **Form**: `ReservationForm.java`
- **Template**: `reservation/reserveForm.html`
- **Validation**: `EndTimeMustBeAfterStartTime.java`, `ThirtyMinutesUnit.java`

---

**Created:** October 25, 2025
**Version:** 1.0
**Author:** Hendi Santika
**Status:** ✅ Production Ready
