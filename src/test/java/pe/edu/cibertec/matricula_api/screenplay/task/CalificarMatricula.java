package pe.edu.cibertec.matricula_api.screenplay.task;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Patch;
import java.util.Map;
import io.restassured.http.ContentType;

public class CalificarMatricula implements Task {
    private final long id;
    private final double nota;

    public CalificarMatricula(long id, double nota) {
        this.id = id;
        this.nota = nota;
    }

    public static CalificarMatricula de(long id, double nota) {
        return new CalificarMatricula(id, nota);
    }

    @Override
    @Step("{0} registra la nota #nota en la matricula #id")
    public <T extends Actor> void performAs(T actor) {
        String token = actor.recall("token");
        actor.attemptsTo(
                Patch.to("/api/matriculas/{id}/nota")
                        .with(req -> req.pathParam("id", id)
                                .contentType(ContentType.JSON)
                                .header("Authorization", "Bearer " + token)
                                .body(Map.of("nota", nota)))
        );
    }
}
