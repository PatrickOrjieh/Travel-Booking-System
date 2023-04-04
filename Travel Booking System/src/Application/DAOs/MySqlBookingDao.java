package Application.DAOs;

import Application.DTOs.Booking;
import Application.Exceptions.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class MySqlBookingDao extends MySqlDao implements BookingDaoInterface {
    private static TreeSet<String> bookingNumbersCache = new TreeSet<>();

    public void populateBookingCache() throws DaoException{
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        bookingNumbersCache.clear();

        try{
            connection = getConnection();
            String query = "SELECT * FROM booking";
            ps = connection.prepareStatement(query);

            resultSet = ps.executeQuery();

            while(resultSet.next()){
                String bookingNumber = resultSet.getString("booking_number");
                //add the booking number to the cache
                bookingNumbersCache.add(bookingNumber.toLowerCase());
            }

        }catch(SQLException e){
            throw new DaoException("populateBookingCache() " + e.getMessage());
        }
        finally{
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    freeConnection(connection);
                }
            } catch (SQLException e) {
                throw new DaoException("populateBookingCache() " + e.getMessage());
            }
        }
    }
    @Override
    public List<Booking> findAllBookings() throws DaoException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<Booking> bookings = new ArrayList<>();

        try{
            connection = getConnection();
            String query = "SELECT * FROM booking";
            ps = connection.prepareStatement(query);

            resultSet = ps.executeQuery();

            while(resultSet.next()){
                int bookingId = resultSet.getInt("booking_id");
                String bookingNumber = resultSet.getString("booking_number");
                String flightNumber = resultSet.getString("flight_number");
                String customerNumber = resultSet.getString("customer_number");
                String travelDate = resultSet.getString("travel_date");
                String seatNumber = resultSet.getString("seat_number");

                Booking b = new Booking(bookingId, bookingNumber, flightNumber, customerNumber, travelDate, seatNumber);
                bookings.add(b);
            }
        } catch(SQLException e){
            throw new DaoException("findAllBookings() " + e.getMessage());
        } finally{
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    freeConnection(connection);
                }
            } catch (SQLException e) {
                throw new DaoException("findAllBookings() " + e.getMessage());
            }
        }
        return bookings;
    }

    @Override
    public Booking findBookingByNumber(String bookingNumber) throws DaoException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        Booking b = null;

        //check if the booking number is in the cache in both upper and lower case
        if(!bookingNumbersCache.contains(bookingNumber.toLowerCase()) && !bookingNumbersCache.contains(bookingNumber.toUpperCase())){
            //if it is NOT in the cache, then it is not in the database
            return null;
        }

        try{
            connection = getConnection();
            String query = "SELECT * FROM booking WHERE LOWER(booking_number) = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1, bookingNumber.toLowerCase());

            resultSet = ps.executeQuery();

            if(resultSet.next()){
                int bookingId = resultSet.getInt("booking_id");
                String flightNumber = resultSet.getString("flight_number");
                String customerNumber = resultSet.getString("customer_number");
                String travelDate = resultSet.getString("travel_date");
                String seatNumber = resultSet.getString("seat_number");

                b = new Booking(bookingId, bookingNumber, flightNumber, customerNumber, travelDate, seatNumber);
            }
        } catch(SQLException e){
            throw new DaoException("findBookingByNumber() " + e.getMessage());
        } finally{
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    freeConnection(connection);
                }
            } catch (SQLException e) {
                throw new DaoException("findBookingByNumber() " + e.getMessage());
            }
        }
        return b;
    }

    @Override
    public boolean deleteBookingByNumber(String bookingNumber) throws DaoException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        boolean deleted = false;

        try{
            connection = getConnection();
            String query = "DELETE FROM booking WHERE LOWER(booking_number) = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1, bookingNumber.toLowerCase());

            int result = ps.executeUpdate();
            if(result == 1){
                deleted = true;

                //remove the booking number from the cache
                bookingNumbersCache.remove(bookingNumber.toLowerCase());
            }
        } catch(SQLException e){
            throw new DaoException("deleteBookingByNumber() " + e.getMessage());
        } finally{
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    freeConnection(connection);
                }
            } catch (SQLException e) {
                throw new DaoException("deleteBookingByNumber() " + e.getMessage());
            }
        }
        return deleted;
    }

    @Override
    public Booking insertBooking(Booking booking) throws DaoException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        Booking b = null;

        try{
            connection = getConnection();
            String query = "INSERT INTO booking (booking_number, flight_number, customer_number, travel_date, seat_number) VALUES (?,?,?,?,?)";
            ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, booking.getBooking_number());
            ps.setString(2, booking.getFlight_number());
            ps.setString(3, booking.getCustomer_number());
            ps.setString(4, booking.getTravel_date());
            ps.setString(5, booking.getSeat_number());

            int result = ps.executeUpdate();
            if(result == 1){
                resultSet = ps.getGeneratedKeys();
                if(resultSet.next()){
                    int bookingId = resultSet.getInt(1);
                    b = new Booking(bookingId, booking.getBooking_number().toLowerCase(), booking.getFlight_number(), booking.getCustomer_number(), booking.getTravel_date(), booking.getSeat_number());
                }
            }
        } catch(SQLException e){
            throw new DaoException("insertBooking() " + e.getMessage());
        } finally{
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    freeConnection(connection);
                }
            } catch (SQLException e) {
                throw new DaoException("insertBooking() " + e.getMessage());
            }
        }
        return b;
    }

    @Override
    public List<Booking> findAllBookingsByCustomerNumber(String customerNumber) throws DaoException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<Booking> bookings = new ArrayList<>();

        try{
            connection = getConnection();
            String query = "SELECT * FROM booking WHERE LOWER(customer_number) = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1, customerNumber.toLowerCase());

            resultSet = ps.executeQuery();

            while(resultSet.next()){
                int bookingId = resultSet.getInt("booking_id");
                String bookingNumber = resultSet.getString("booking_number");
                String flightNumber = resultSet.getString("flight_number");
                String travelDate = resultSet.getString("travel_date");
                String seatNumber = resultSet.getString("seat_number");

                Booking b = new Booking(bookingId, bookingNumber, flightNumber, customerNumber, travelDate, seatNumber);
                bookings.add(b);
            }
        } catch(SQLException e){
            throw new DaoException("findAllBookingsByCustomerNumber() " + e.getMessage());
        } finally{
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    freeConnection(connection);
                }
            } catch (SQLException e) {
                throw new DaoException("findAllBookingsByCustomerNumber() " + e.getMessage());
            }
        }
        return bookings;
    }

    @Override
    public List<Booking> findAllBookingsByFlightNumber(String flightNumber) throws DaoException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<Booking> bookings = new ArrayList<>();

        try{
            connection = getConnection();
            String query = "SELECT * FROM booking WHERE LOWER(flight_number) = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1, flightNumber.toLowerCase());

            resultSet = ps.executeQuery();

            while(resultSet.next()){
                int bookingId = resultSet.getInt("booking_id");
                String bookingNumber = resultSet.getString("booking_number");
                String customerNumber = resultSet.getString("customer_number");
                String travelDate = resultSet.getString("travel_date");
                String seatNumber = resultSet.getString("seat_number");

                Booking b = new Booking(bookingId, bookingNumber, flightNumber, customerNumber, travelDate, seatNumber);
                bookings.add(b);
            }
        } catch(SQLException e){
            throw new DaoException("findAllBookingsByFlightNumber() " + e.getMessage());
        } finally{
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    freeConnection(connection);
                }
            } catch (SQLException e) {
                throw new DaoException("findAllBookingsByFlightNumber() " + e.getMessage());
            }
        }
        return bookings;
    }
}

