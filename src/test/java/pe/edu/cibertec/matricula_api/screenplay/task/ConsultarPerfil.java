package pe.edu.cibertec.matricula_api.screenplay.task;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

public class ConsultarPerfil implements Task {
    public static ConsultarPerfil propio() {
        return new ConsultarPerfil();
    }

    @Override
    @Step("{0} consulta su perfil con su access token")
    public <T extends Actor> void performAs(T actor) {
        String token = actor.recall("token");
        actor.attemptsTo(
                Get.resource("/api/auth/perfil")
                        .with(req -> req.header("Authorization", "Bearer " + token))
        );
    }
}
