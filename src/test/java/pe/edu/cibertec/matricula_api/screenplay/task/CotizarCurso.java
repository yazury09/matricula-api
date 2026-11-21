package pe.edu.cibertec.matricula_api.screenplay.task;

import io.restassured.http.ContentType;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

import java.util.List;
import java.util.Map;

public class CotizarCurso implements Task {
    private final String alumno;
    private final String curso;

    public CotizarCurso(String alumno, String curso) {
        this.alumno = alumno;
        this.curso = curso;
    }

    public static CotizarCurso de(String alumno, String curso) {
        return new CotizarCurso(alumno, curso);
    }

    @Override
    @Step("{0} cotiza el curso #curso")
    public <T extends Actor> void performAs(T actor) {
        String token = SerenityRest.lastResponse().path("accessToken");
        actor.remember("token", token);
        actor.attemptsTo(
                Post.to("/api/matriculas/cotizacion")
                        .with(req -> req.contentType(ContentType.JSON)
                                .header("Authorization", "Bearer " + token)
                                .body(Map.of("codigoAlumno", alumno, "cursos", List.of(curso))))
        );
    }
}
