package pe.edu.cibertec.matricula_api.screenplay.task;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

public class ConsultarMatriculas implements Task {
    public static ConsultarMatriculas propias() {
        return new ConsultarMatriculas();
    }

    @Override
    @Step("{0} consulta sus matriculas")
    public <T extends Actor> void performAs(T actor) {
        String token = actor.recall("token");
        actor.attemptsTo(
                Get.resource("/api/matriculas")
                        .with(req -> req.header("Authorization", "Bearer " + token))
        );
    }
}
