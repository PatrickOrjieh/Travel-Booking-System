package Application.DAOs;

import Application.DAOs.MySqlDao;
import Application.DTOs.Airport;
import Application.Exceptions.DaoException;
import Application.DAOs.CustomerDaoInterface;
import Application.DTOs.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class MySqlAirportDao extends MySqlDao implements AirportDaoInterface{
    @Override
    public List<Airport> findAllAirports() throws DaoException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<Airport> airports = new ArrayList<>();

        try {
            connection = getConnection();
            String query = "SELECT * FROM airport";
            ps = connection.prepareStatement(query);

            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                int airportId = resultSet.getInt("airport_id");
                String airportName = resultSet.getString("airport_name");
                String airportLocation = resultSet.getString("airport_location");

                Airport a = new Airport(airportId,airportName,airportLocation);
                airports.add(a);
            }
        }
        catch (SQLException e) {
            throw new DaoException("findAllAirportsresultSet() " + e.getMessage());
        }
        finally {
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
            }
            catch (SQLException e) {
                throw new DaoException("findAllAirports() " + e.getMessage());
            }
        }
        return airports;
    }

    @Override
    public Airport findAirportById(int airportId) throws DaoException {
        Connection  connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        Airport airport = null;

        try {
            connection = getConnection();
            String query = "SELECT * FROM airport WHERE airport_id = ?";
            ps = connection.prepareStatement(query);
            ps.setInt(1, airportId);
            resultSet = ps.executeQuery();

            if (resultSet.next()) {
                String airportName = resultSet.getString("airport_name");
                String airportLocation = resultSet.getString("airport_location");

                airport = new Airport(airportId,airportName,airportLocation);
            }
        }
        catch (SQLException e) {
            throw new DaoException("findAirportByIdresultSet() " + e.getMessage());
        }
        finally {
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
            }
            catch (SQLException e) {
                throw new DaoException("findAirportById() " + e.getMessage());
            }
        }
        return airport;
    }

    @Override
    public boolean deleteAirportById(int airportId) throws DaoException {
        Connection connection = null;
        PreparedStatement ps = null;
        boolean deleted = false;

        try {
            connection = getConnection();
            String query = "DELETE FROM airport WHERE airport_id = ?";
            ps = connection.prepareStatement(query);
            ps.setInt(1, airportId);
            int rows = ps.executeUpdate();
            if (rows == 1) {
                deleted = true;
            }
        }
        catch (SQLException e) {
            throw new DaoException("deleteAirportById() " + e.getMessage());
        }
        finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    freeConnection(connection);
                }
            }
            catch (SQLException e) {
                throw new DaoException("deleteAirportById() " + e.getMessage());
            }
        }
        return deleted;
    }

    @Override
    public Airport insertAirport(Airport airport) throws DaoException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        Airport newAirport = null;

        try {
            connection = getConnection();
            String query = "INSERT INTO airport (airport_name, airport_location) VALUES (?, ?)";
            ps = connection.prepareStatement(query);
            ps.setString(1, airport.getAirport_name());
            ps.setString(2, airport.getAirport_location());
            int result = ps.executeUpdate();

            if (result == 1) {
                newAirport = airport;
            }
        }
        catch (SQLException e) {
            throw new DaoException("insertAirportresultSet() " + e.getMessage());
        }
        finally {
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
            }
            catch (SQLException e) {
                throw new DaoException("insertAirport() " + e.getMessage());
            }
        }
        return newAirport;
    }
}
