package org.jikolp.database;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

/**
 * Класс MySQLConnection - служит для первоначальной установки связи к базе данных.
 * Драйвер, используемый для подключения - com.mysql.jdbc.Driver.
 */
public class MySQLConnection {
    //адрес нахождения базы данных. "localhost:3306" - означает, что он лежит
    //локальном компьютере и доступ к нему осуществяется через порт 3306.
    private final static String URL = "jdbc:mysql://localhost:3306/ClinicaDB";
    //данные для подлючения к базе
    private final static String USERNAME = "root";
    private final static String PASSWORD = "qwer12";

    //само подключение
    public static Connection dbConnector() {
        try {
            //определяем драйвер
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            //устанавливаем соединение
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            //выводим сообщение о том, что соединение успешно установлено
            System.out.println("Connection to SQLite has been established.");
            return connection;
        } catch (Exception e) {
            // в случаем ошибки конструкция try/catch отловит ошибку
            System.out.println("Exception: " + e.toString());
            return null;
        }
    }

}
