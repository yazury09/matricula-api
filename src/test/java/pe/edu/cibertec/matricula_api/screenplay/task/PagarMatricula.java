package pe.edu.cibertec.matricula_api.screenplay.task;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import java.util.Map;
import io.restassured.http.ContentType;

import net.serenitybdd.screenplay.rest.interactions.Patch;

public class PagarMatricula implements Task {
    private final long id;
    private final String metodo;

    public PagarMatricula(long id, String metodo) {
        this.id = id;
        this.metodo = metodo;
    }

    public static PagarMatricula con(long id, String metodo) {
        return new PagarMatricula(id, metodo);
    }

    @Override
    @Step("{0} paga la matricula #id con #metodo")
    public <T extends Actor> void performAs(T actor) {
        String token = actor.recall("token");
        actor.attemptsTo(
                Patch.to("/api/matriculas/{id}/pagar")
                        .with(req -> req.pathParam("id", id)
                                .contentType(ContentType.JSON)
                                .header("Authorization", "Bearer " + token)
                                .body(Map.of("metodo", metodo)))
        );
    }
}
