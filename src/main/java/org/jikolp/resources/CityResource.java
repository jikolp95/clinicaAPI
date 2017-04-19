package org.jikolp.resources;

import com.owlike.genson.Genson;
import org.jikolp.database.MySQLConnection;
import org.jikolp.model.City;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yeldos on 4/19/17.
 */

@Path("cities")
@Produces(MediaType.APPLICATION_JSON)
public class CityResource {
    private Connection connection = null;

    @GET
    public String getCities() {
        //запрос на предоставления списка городов
        final String citiesSelectCommand = "SELECT * FROM cities";
        final List<City> cities = new ArrayList<>();
        connection = MySQLConnection.dbConnector();
        try {
            final Statement stmt = connection.createStatement();
            final ResultSet rs = stmt.executeQuery(citiesSelectCommand);
            while (rs.next()) {
                cities.add(new City(rs.getInt("city_id"),
                        rs.getString("name")));
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Exception: " + e.toString());
        }
        final Genson genson = new Genson();
        return genson.serialize(cities);

    }
}
