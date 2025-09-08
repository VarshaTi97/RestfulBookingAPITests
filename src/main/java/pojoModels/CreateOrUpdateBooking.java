package pojoModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrUpdateBooking {
    private String firstname;
    private String lastname;
    private int totalprice;
    private  boolean depositpaid;
    private BookingDates bookingdates;
    private String additionalneeds;
}
