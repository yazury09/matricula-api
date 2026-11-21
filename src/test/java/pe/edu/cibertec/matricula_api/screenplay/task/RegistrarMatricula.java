package pe.edu.cibertec.matricula_api.screenplay.task;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;
import java.util.Map;

public class RegistrarMatricula implements Task {
    private final String alumno;
    private final String curso;

    public RegistrarMatricula(String alumno, String curso) {
        this.alumno = alumno;
        this.curso = curso;
    }

    public static RegistrarMatricula de(String alumno, String curso) {
        return new RegistrarMatricula(alumno, curso);
    }

    @Override
    @Step("{0} se matricula en el curso #curso")
    public <T extends Actor> void performAs(T actor) {
        String token = actor.recall("token");
        actor.attemptsTo(
                Post.to("/api/matriculas")
                        .with(req -> req.contentType(ContentType.JSON)
                                .header("Authorization", "Bearer " + token)
                                .body(Map.of("codigoAlumno", alumno, "codigoCurso", curso)))
        );
    }
}
