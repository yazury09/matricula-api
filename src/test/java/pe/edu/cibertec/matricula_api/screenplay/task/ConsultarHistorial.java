package pe.edu.cibertec.matricula_api.screenplay.task;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

public class ConsultarHistorial implements Task {
    private final String codigo;

    public ConsultarHistorial(String codigo) {
        this.codigo = codigo;
    }

    public static ConsultarHistorial de(String codigo) {
        return new ConsultarHistorial(codigo);
    }

    @Override
    @Step("{0} consulta el historial de #codigo")
    public <T extends Actor> void performAs(T actor) {
        String token = actor.recall("token");
        actor.attemptsTo(
                Get.resource("/api/alumnos/{codigo}/historial")
                        .with(req -> req.pathParam("codigo", codigo)
                                .header("Authorization", "Bearer " + token))
        );
    }
}
