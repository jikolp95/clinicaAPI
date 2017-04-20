package org.jikolp.security;


import com.owlike.genson.Genson;
import org.jikolp.model.ServerAnswer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
/**
 * Класс аутентификация пользователя.
 * Когда пользователь регистрируется или пытается войти в приложение пройсходит сначала вызов
 * класса SecuredFilter, затем возвращается в этот класс.
 */

//входная точка /auth/
@Path("auth")
public class SecuredResource {
    private Genson genson;

    //вход пользователя
    @GET
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public String securedMethod() {
        genson = new Genson();
        ServerAnswer serverAnswer = new ServerAnswer("This API is Secured", 1);
        return genson.serialize(serverAnswer);
    }

    //регистрация пользователя
    @GET
    @Path("registration")
    public String register() {
        genson = new Genson();
        ServerAnswer serverAnswer = new ServerAnswer("Registration is success", 1);
        return genson.serialize(serverAnswer);
    }
}
