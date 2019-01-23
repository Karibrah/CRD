import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RentalCompanyTest {
    @Test
    public void checkAvilabilitySUVTest() throws ParseException {
        RentalCompany rentalCompany= RentalCompany.getInstance();
        SimpleDateFormat dateformat3 = new SimpleDateFormat("dd/MM/yyyy");

        Car car= rentalCompany.checkAvilability(dateformat3.parse("21/12/2018"),10,"SUV");
        assert(car!=null);

    }
    @Test
    public void checkAvilabilityEconomyTest() throws ParseException {
        RentalCompany rentalCompany= RentalCompany.getInstance();

        SimpleDateFormat dateformat3 = new SimpleDateFormat("dd/MM/yyyy");

        Car car= rentalCompany.checkAvilability(dateformat3.parse("21/12/2018"),10,"Economy");
        assert(car!=null);

    }
    @Test
    public void checkAvilabilityLuxuryTest() throws ParseException {
        RentalCompany rentalCompany= RentalCompany.getInstance();
        SimpleDateFormat dateformat3 = new SimpleDateFormat("dd/MM/yyyy");

        Car car= rentalCompany.checkAvilability(dateformat3.parse("21/12/2018"),10,"Luxury");
        int result =rentalCompany.confirmReservation(new Customer(),car,dateformat3.parse("21/12/2018"),10);

        Car car2= rentalCompany.checkAvilability(dateformat3.parse("22/12/2018"),10,"Luxury");
        result =rentalCompany.confirmReservation(new Customer(),car2,dateformat3.parse("21/12/2018"),10);


        Car car3= rentalCompany.checkAvilability(dateformat3.parse("23/12/2018"),10,"Luxury");
        assert(car3==null);

    }
    @Test
    public void confirmReservationTest() throws ParseException {
        RentalCompany rentalCompany= RentalCompany.getInstance();
        SimpleDateFormat dateformat3 = new SimpleDateFormat("dd/MM/yyyy");

        Car car= rentalCompany.checkAvilability(dateformat3.parse("21/12/2018"),10,"Luxury");
        int result =rentalCompany.confirmReservation(new Customer(),car,dateformat3.parse("21/12/2018"),10);
        assert (result==1);

    }
    @Test
    public void getReservationsByCarTest() throws ParseException {
        RentalCompany rentalCompany= RentalCompany.getInstance();
        SimpleDateFormat dateformat3 = new SimpleDateFormat("dd/MM/yyyy");

        Car car= rentalCompany.checkAvilability(dateformat3.parse("21/12/2018"),10,"Luxury");
        int result =rentalCompany.confirmReservation(new Customer(),car,dateformat3.parse("21/12/2018"),10);
        List<Reservation> reservations= rentalCompany.getReservationsByCar(car);
        assert (reservations.size()==1);

    }
    @Test
    public void clearOldReservationsfromAllTypesTest() throws ParseException {
        RentalCompany rentalCompany= RentalCompany.getInstance();
        SimpleDateFormat dateformat3 = new SimpleDateFormat("dd/MM/yyyy");

        Car car= rentalCompany.checkAvilability(dateformat3.parse("21/12/2018"),10,"Luxury");
        int result =rentalCompany.confirmReservation(new Customer(),car,dateformat3.parse("21/12/2018"),10);
        rentalCompany.clearOldReservationsfromAllTypes();
        List<Reservation> reservations= rentalCompany.getReservationsByCar(car);
        assert (reservations.size()==0);

    }
    @Test
    public void checkCustomerHasExitingReservationTest() throws ParseException {
        Customer customer= new Customer();

        RentalCompany rentalCompany= RentalCompany.getInstance();
        SimpleDateFormat dateformat3 = new SimpleDateFormat("dd/MM/yyyy");

        Car car= rentalCompany.checkAvilability(dateformat3.parse("21/12/2018"),10,"Luxury");
        rentalCompany.confirmReservation(customer,car,dateformat3.parse("21/12/2018"),10);
        boolean result=rentalCompany.checkCustomerHasExitingReservation(customer,dateformat3.parse("23/12/2018"),5);
        assert (result==true);
    }

}
