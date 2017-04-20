package org.jikolp.security;

import org.glassfish.jersey.internal.util.Base64;
import org.jikolp.database.MySQLConnection;
import org.jikolp.model.ServerAnswer;

import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Класс считывание принимаемой информации от пользователя при регистрации
 * либо аутентификации
 */


@Provider
@Produces(MediaType.APPLICATION_JSON)
public class SecurityFilter implements ContainerRequestFilter {
    //ключевое слово при авторизации
    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    //ключевое слово Base Auth - аутентификации
    private static final String AUTHORIZATION_HEADER_PREFIX = "Basic ";
    //ключевой префикс при регистрации и авторизации
    private static final String AUTH_URL_PREFIX = "auth";
    //ключевое слово при авторизации
    private static final String LOGIN_URL_PREFIX = "login";
    //поиск юзера по логину
    private static final String SELECT_USER = "SELECT * FROM users WHERE login=";
    //регистрация юзера
    private static final String REGISTER_COMMAND = "INSERT INTO users(login,password) VALUES";
    //кавычки
    private static final String QUOTES = "\"";
    //левая скобка
    private static final String LEFT_BRACKET = "(";
    //правая скобка
    private static final String RIGHT_BRACKET = ")";
    //запятая
    private static final String COMMA = ",";
    //коннектор к базе
    private Connection connection = null;


    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        //проверка, есть ли префикс AUTH
        if (containerRequestContext.getUriInfo().getPath().contains(AUTH_URL_PREFIX)) {
            //считывание передаваемой юзером информации (логин, пароль)
            List<String> authHeader = containerRequestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);
            //проверка если он передает не пустое значение...
            if (authHeader != null && authHeader.size() > 0) {
                //..то считать данные
                String authToken = authHeader.get(0);
                authToken = authToken.replace(AUTHORIZATION_HEADER_PREFIX, "");
                String decodedString = Base64.decodeAsString(authToken);
                StringTokenizer tokenizer = new StringTokenizer(decodedString, ":");

                //в итоге получим логин и пароль юзера
                String username = tokenizer.nextToken();
                String password = tokenizer.nextToken();
                connection = MySQLConnection.dbConnector();
                try {
                    //устанавливаем соединение с базой
                    final Statement stmt = connection.createStatement();
                    final ResultSet rs = stmt.executeQuery(SELECT_USER + QUOTES + username + QUOTES);
                    //если пользователь хочет войти...
                    if (containerRequestContext.getUriInfo().getPath().contains(LOGIN_URL_PREFIX)) {
                        //..и такой пользователь существует..
                        if (rs != null && rs.next()) {
                            String receivedPassword = rs.getString("password");
                            if (receivedPassword.equals(password)) {
                                //..то закрываем соединение и возврашаем к класс SecuredResource
                                stmt.close();
                                return;
                            }
                        }
                        //а если пользователь хочет зарегистрироваться..
                    } else {
                        //..и таких данных нет в базе..
                        if (!rs.next()) {
                            //..то регистрируем его в базе
                            String finalCommand = REGISTER_COMMAND + LEFT_BRACKET + QUOTES + username + QUOTES + COMMA + QUOTES + password + QUOTES + RIGHT_BRACKET;
                            stmt.executeUpdate(finalCommand);
                            //закрываем соединение и возврашаем к класс SecuredResource
                            stmt.close();
                            return;
                        }
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                // если где-то произошла ошибка, то формируем ошибку и передаем ее как ответ пользователю
                final ServerAnswer serverAnswer = new ServerAnswer("Cannot access the resource", 0);
                final Response unAuthorizedStatus = Response.status(Response.Status.UNAUTHORIZED)
                        .entity(serverAnswer)
                        .build();
                containerRequestContext.abortWith(unAuthorizedStatus);
            }
        }
    }
}
