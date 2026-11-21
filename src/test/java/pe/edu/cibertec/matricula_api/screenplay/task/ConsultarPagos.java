package pe.edu.cibertec.matricula_api.screenplay.task;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

public class ConsultarPagos implements Task {
    public static ConsultarPagos propios() {
        return new ConsultarPagos();
    }

    @Override
    @Step("{0} consulta sus pagos")
    public <T extends Actor> void performAs(T actor) {
        String token = actor.recall("token");
        actor.attemptsTo(
                Get.resource("/api/pagos")
                        .with(req -> req.header("Authorization", "Bearer " + token))
        );
    }
}
