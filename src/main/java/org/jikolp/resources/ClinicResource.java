package org.jikolp.resources;

import com.owlike.genson.Genson;
import org.jikolp.database.MySQLConnection;
import org.jikolp.model.Clinic;
import org.jikolp.model.Doctor;
import org.jikolp.model.Image;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

/**
 * Основной класс нашей API.
 * Здесь представлены методы, к котором мы обращаемся через ссылку,
 * а они выдают нам информацию из базы данных MySQL.
 */



//входная точка /clinics/
@Path("/clinics")
//выдает ответы в Json-формате
@Produces(MediaType.APPLICATION_JSON)
public class ClinicResource {
    //объявление коннектора
    private Connection connection = null;

    //в зависимости от города, выдает список клиник
    @GET
    @Path("/{city_id}")
    public String getClinics(@PathParam("city_id") int city_id) {
        //запрос на выбор клиник в определенном городе
        final String clinicsSelectCommand = "SELECT * FROM clinics WHERE city_id=" + city_id;
        //запрос на выбор фотографий определенной клиники
        final String imagesSelectCommand = "SELECT * FROM images WHERE clinic_id=";
        //запрос на выбор докторов определенной клиники
        final String doctorsSelectCommand = "SELECT * FROM doctors WHERE clinic_id=";
        //объявление пустого списка клиник
        final List<Clinic> clinicList = new ArrayList<>();

        //библиотека Genson нужна для сериализации объектов.
        final Genson genson = new Genson();

        //установление соединения в базой
        connection = MySQLConnection.dbConnector();

        try {
            final Statement stmt = connection.createStatement();
            final ResultSet rs = stmt.executeQuery(clinicsSelectCommand);
            //занесение всех клиник в пустой список, объявленный выше
            while (rs.next()) {
                if (rs.getInt("city_id") == city_id) {
                    clinicList.add(new Clinic(rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getString("phone"),
                            rs.getString("address"),
                            rs.getFloat("lat"),
                            rs.getFloat("lon"),
                            null,
                            null,
                            rs.getInt("city_id")));
                }
            }

            //выдача фотографий клиники
            for (Clinic clinic : clinicList) {
                final ResultSet rss = stmt.executeQuery(imagesSelectCommand + clinic.getClinic_id());
                final List<Image> images = new ArrayList<>();
                while (rss.next()) {
                    images.add(new Image(rss.getInt("image_id"),
                            rss.getString("image"),
                            rss.getInt("clinic_id")));
                }
                clinic.setImages(images);
            }

            //выдача докторов клиники
            for (Clinic clinic : clinicList) {
                final ResultSet rss = stmt.executeQuery(doctorsSelectCommand + clinic.getClinic_id());
                final List<Doctor> doctors = new ArrayList<>();
                while (rss.next()) {
                    doctors.add(new Doctor(rss.getInt("doctor_id"),
                            rss.getString("name"),
                            rss.getString("phone"),
                            rss.getInt("clinic_id")));
                }
                clinic.setDoctors(doctors);
            }
            //закрытие соединения
            stmt.close();

        } catch (Exception e) {
            System.out.println("Exception: " + e.toString());
        }
        //возвращение подготовленного ответа
        return genson.serialize(clinicList);
    }

}