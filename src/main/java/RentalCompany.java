import java.time.LocalDateTime;
import java.util.*;


//Assumtions:
// Rental has one branch
//4 Suvs, 6 economy care and 2 Luxury cars
//using Ids to define objects (car,reservation and custtomers) for simplicity however the attributes are there for future use
//Rental Company is singlton class

public class RentalCompany {

    private List<Car> cars;
    private List<Customer> customers;
    private HashMap<SUV,List<Reservation>> currentSUVReservations;
    private HashMap<Economy,List<Reservation>> currentEconomyReservations;
    private HashMap<Luxury,List<Reservation>> currentLuxuryReservations;
    private static RentalCompany instance = null;
    private RentalCompany()
    {
        cars= new ArrayList<Car>();
        currentSUVReservations= new HashMap<SUV, List<Reservation>>();
        currentEconomyReservations= new HashMap<Economy, List<Reservation>>();
        currentLuxuryReservations= new HashMap<Luxury, List<Reservation>>();
        //Add 4 suvs ,6 economy and 2 luxury
        for(int i=0;i<4;i++){
            SUV suvCar=new SUV();
            cars.add(suvCar);
            currentSUVReservations.put(suvCar, new ArrayList<Reservation>() );
        }
        for(int i=0;i<6;i++){
            Economy economyCar= new Economy();
            cars.add(economyCar);
            currentEconomyReservations.put(economyCar,new ArrayList<Reservation>());
        }
        for(int i=0;i<2;i++){
            Luxury luxuryCar= new Luxury();
            cars.add(luxuryCar);
            currentLuxuryReservations.put(luxuryCar,new ArrayList<Reservation>());
        }


    }

    public static RentalCompany getInstance()
    {
        if(instance==null)
        {
            instance = new RentalCompany();
        }
        return instance;
    }

    public int confirmReservation(Customer customer, Car car, Date pickup , int days){
        Reservation reservation= new Reservation();
        reservation.setCar(car);
        reservation.setCustomer(customer);
        reservation.setDays(days);
        reservation.setPickupDate(pickup);
        if( car instanceof  SUV){
            currentSUVReservations.get(car).add(reservation);
            return 1;
        }
        else if( car instanceof  Luxury){
            currentLuxuryReservations.get(car).add(reservation);
            return 1;
        }
        else if( car instanceof  Economy){
            currentEconomyReservations.get(car).add(reservation);
            return 1;
        }
        return 0;
    }
    public Car checkAvilability(Date pickup , int days, String Type){
        if("SUV".equals(Type)){
            Iterator it = currentSUVReservations.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                if(checkNoOverlapPeriod((ArrayList<Reservation>)pair.getValue(),pickup,days)== true)
                    return (SUV)pair.getKey();
            }
        }
        else if("Luxury".equals(Type)){
            Iterator it = currentLuxuryReservations.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                if(checkNoOverlapPeriod((ArrayList<Reservation>)pair.getValue(),pickup,days)== true)
                    return (Luxury)pair.getKey();
            }
        }
        else if("Economy".equals(Type)){
            Iterator it = currentEconomyReservations.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                if(checkNoOverlapPeriod((ArrayList<Reservation>)pair.getValue(),pickup,days)== true)
                    return (Economy)pair.getKey();
            }
        }
        return null;
    }
    private boolean checkNoOverlapPeriod(List<Reservation>  reservations,Date pickup , int days){

        Calendar c = Calendar.getInstance();
        for (Reservation r: reservations){
                c.setTime(r.getPickupDate());
                c.add(Calendar.DATE, r.getDays());
                Date currentDropoff=    c.getTime();
                if(r.getPickupDate().before(pickup) && currentDropoff.after(pickup)){
                    return false;
                }
                c.setTime(pickup);
                c.add(Calendar.DATE, days);
                Date newDropoff=    c.getTime();
                if(pickup.before(r.getPickupDate()) && newDropoff.after(r.getPickupDate())){
                    return false;
                }

        }
        return true;
    }

    public boolean checkCustomerHasExitingReservation(Customer customer,Date pickup , int days){
        Iterator it = currentSUVReservations.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if(checkCustomerperTypeReservation((ArrayList<Reservation>)pair.getValue(),customer,pickup,days)== true)
                return true;
        }
         it = currentLuxuryReservations.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if(checkCustomerperTypeReservation((ArrayList<Reservation>)pair.getValue(),customer,pickup,days)== true)
                return true;
        }
        it = currentEconomyReservations.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if(checkCustomerperTypeReservation((ArrayList<Reservation>)pair.getValue(),customer,pickup,days)== true)
                return true;
        }
        return false;
    }
    private boolean checkCustomerperTypeReservation(List<Reservation>  reservations,Customer customer,Date pickup , int days){
        Calendar c = Calendar.getInstance();
        for (Reservation r: reservations){
            if(r.getCustomer().getCustomerId()==customer.getCustomerId()){
                c.setTime(r.getPickupDate());
                c.add(Calendar.DATE, r.getDays());
                Date currentDropoff=    c.getTime();
                if(r.getPickupDate().before(pickup) && currentDropoff.after(pickup)){
                    return true;
                }
                c.setTime(pickup);
                c.add(Calendar.DATE, days);
                Date newDropoff=    c.getTime();
                if(pickup.before(r.getPickupDate()) && newDropoff.after(r.getPickupDate())){
                    return true;
                }
            }
        }
        return false;
    }
    public void clearOldReservationsfromAllTypes(){
        Iterator it = currentSUVReservations.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            deleteOldReservations((ArrayList<Reservation>)pair.getValue());

        }
        it = currentLuxuryReservations.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            deleteOldReservations((ArrayList<Reservation>)pair.getValue());

        }
        it = currentEconomyReservations.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            deleteOldReservations((ArrayList<Reservation>)pair.getValue());

        }
    }
    private void deleteOldReservations(List<Reservation>  reservations){
        Calendar c = Calendar.getInstance();
        List<Reservation>  updateReservationsList= new ArrayList<Reservation>();
        for (int i=0;i<reservations.size();i++){
                Reservation r= reservations.get(i);
                c.setTime(r.getPickupDate());
                c.add(Calendar.DATE, r.getDays());
                Date currentDropoff=    c.getTime();
                if( currentDropoff.before(new Date())){
                    reservations.remove(r);
                }

        }
    }
    public List<Reservation> getReservationsByCar(Car car){
        if( car instanceof  SUV){
            return currentSUVReservations.get(car);

        }
        else if( car instanceof  Luxury){
            return currentLuxuryReservations.get(car);

        }
        else if( car instanceof  Economy){
            return currentEconomyReservations.get(car);

        }
        return null;
    }
    public int addCustomer(Customer c){
        try {
            customers.add(c);
            return 1;
        }
        catch (Exception e){
            System.out.println(e);
        }
        return 0;
    }
    public Customer findCustomer(int customerId){
        for(int i=0;i<customers.size();i++){
            if(customers.get(i).getCustomerId()==customerId)
                return customers.get(i);
        }
        return null;
    }


}
