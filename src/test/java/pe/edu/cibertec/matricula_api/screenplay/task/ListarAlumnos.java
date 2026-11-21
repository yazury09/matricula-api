package pe.edu.cibertec.matricula_api.screenplay.task;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

public class ListarAlumnos implements Task {
    public static ListarAlumnos todos() {
        return new ListarAlumnos();
    }

    @Override
    @Step("{0} lista todos los alumnos")
    public <T extends Actor> void performAs(T actor) {
        String token = actor.recall("token");
        actor.attemptsTo(
                Get.resource("/api/alumnos")
                        .with(req -> req.header("Authorization", "Bearer " + token))
        );
    }
}
