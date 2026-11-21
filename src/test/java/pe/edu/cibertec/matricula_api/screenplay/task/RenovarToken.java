package pe.edu.cibertec.matricula_api.screenplay.task;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

import java.util.Map;

public class RenovarToken implements Task {
    private final String claveMemoria;

    public RenovarToken(String claveMemoria) {
        this.claveMemoria = claveMemoria;
    }

    public static RenovarToken con(String claveMemoria) {
        return new RenovarToken(claveMemoria);
    }

    @Override
    @Step("{0} renueva sus tokens con el refresh token guardado como #claveMemoria")
    public <T extends Actor> void performAs(T actor) {
        String refreshToken = actor.recall(claveMemoria);
        actor.attemptsTo(
                Post.to("/api/auth/refresh")
                        .with(req -> req.contentType(ContentType.JSON)
                                .body(Map.of("refreshToken", refreshToken)))
        );

        Response respuesta = SerenityRest.lastResponse();
        if (respuesta.statusCode() == 200) {
            actor.remember("token", respuesta.jsonPath().getString("accessToken"));
            actor.remember("refreshToken", respuesta.jsonPath().getString("refreshToken"));
        }
    }
}
