package ideas.pl.pl_data.Service;

import ideas.pl.pl_data.DTO.BookingDTO;
import ideas.pl.pl_data.Entity.Booking;
import ideas.pl.pl_data.Entity.Property;
import ideas.pl.pl_data.Exception.PropertyAlreadyBookedException;
import ideas.pl.pl_data.Repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private PropertyService propertyService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByUserId_ShouldReturnBookings_WhenFound() {
        int userId = 1;
        List<BookingDTO> mockBookings = new ArrayList<>();
        mockBookings.add(mock(BookingDTO.class)); // Add a mock BookingDTO

        when(bookingRepository.findByUser_UserId(userId)).thenReturn(mockBookings);

        List<BookingDTO> result = bookingService.findByUserId(userId);

        assertEquals(mockBookings, result);
        verify(bookingRepository, times(1)).findByUser_UserId(userId);
    }

    @Test
    void bookProperty_ShouldThrowException_WhenPropertyAlreadyBooked() {

        Booking bookingRequest = new Booking();
        Property mockProperty = mock(Property.class);
        bookingRequest.setProperty(mockProperty); // Mock Property
        when(mockProperty.getPropertyId()).thenReturn(1); // Set a sample property ID
        bookingRequest.setStartDate(LocalDate.now());
        bookingRequest.setEndDate(LocalDate.now().plusDays(2));

        // Simulate existing booking for the same property and dates
        List<Booking> existingBookings = new ArrayList<>();
        existingBookings.add(mock(Booking.class)); // Simulate existing booking

        // When the repository method is called, return the existing bookings
        when(bookingRepository.findByPropertyId(1, bookingRequest.getStartDate(), bookingRequest.getEndDate()))
                .thenReturn(existingBookings);

        // Assert that the exception is thrown
        assertThrows(PropertyAlreadyBookedException.class, () -> bookingService.bookProperty(bookingRequest));

        // Verify that the repository method was called and save was never called
        verify(bookingRepository, times(1)).findByPropertyId(1, bookingRequest.getStartDate(), bookingRequest.getEndDate());
        verify(bookingRepository, never()).save(any(Booking.class)); // Ensure save was never called
    }


    @Test
    void bookProperty_ShouldReturnSuccess_WhenBookingIsValid() {
        Booking bookingRequest = new Booking();
        bookingRequest.setProperty(mock(Property.class)); // Mock Property
        bookingRequest.getProperty().setPropertyId(1); // Set a sample property ID
        bookingRequest.setStartDate(LocalDate.now());
        bookingRequest.setEndDate(LocalDate.now().plusDays(2));

        when(bookingRepository.findByPropertyId(1, bookingRequest.getStartDate(), bookingRequest.getEndDate()))
                .thenReturn(new ArrayList<>()); // No existing bookings

        String result = bookingService.bookProperty(bookingRequest);

        assertEquals("Booking successful", result);
        verify(bookingRepository, times(1)).save(bookingRequest); // Ensure save was called
    }
}