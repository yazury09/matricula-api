package pe.edu.cibertec.matricula_api.screenplay.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import pe.edu.cibertec.matricula_api.screenplay.questions.ElPago;
import pe.edu.cibertec.matricula_api.screenplay.questions.LaCotizacion;
import pe.edu.cibertec.matricula_api.screenplay.questions.LaRespuesta;
import pe.edu.cibertec.matricula_api.screenplay.task.*;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.*;

public class MatriculaStepDefinitions {

    private final String baseUrl = EnvironmentSpecificConfiguration
            .from(SystemEnvironmentVariables.currentEnvironmentVariables())
            .getProperty("restapi.baseurl");

    @Given("que el alumno {string} inicia sesion")
    public void queElAlumnoIniciaSesion(String alumno) {
        theActorCalled(alumno).whoCan(CallAnApi.at(baseUrl));
        theActorInTheSpotlight().remember("alumno", alumno);
        theActorInTheSpotlight().attemptsTo(IniciarSesion.como(alumno, "Cibertec2026"));
    }

    @When("cotiza el curso {string}")
    public void cotizaElCurso(String curso) {
        Actor actor = theActorInTheSpotlight();
        actor.attemptsTo(CotizarCurso.de(actor.recall("alumno"), curso));
    }

    @Then("la cotizacion tiene {int} creditos, subtotal {double}, descuento {int} y total {double}")
    public void laCotizacionTiene(int creditos, double subtotal, int descuento, double total) {
        theActorInTheSpotlight().should(
                seeThat(LaCotizacion.numero("creditos"), equalTo((double) creditos)),
                seeThat(LaCotizacion.numero("subtotal"), equalTo(subtotal)),
                seeThat(LaCotizacion.numero("descuento"), equalTo((double) descuento)),
                seeThat(LaCotizacion.numero("total"), equalTo(total)));
    }

    //ESCENARIO 2-----
    @When("se matricula en el curso {string}")
    public void seMatriculaEnElCurso(String curso) {
        Actor actor = theActorInTheSpotlight();
        actor.attemptsTo(RegistrarMatricula.de(actor.recall("alumno"), curso));
        if (SerenityRest.lastResponse().statusCode() == 201) {
            Integer id = SerenityRest.lastResponse().path("id");
            actor.remember("idMatricula", id);
        }
    }

    @Then("la matricula queda en estado {string} con monto {double}")
    public void laMatriculaQuedaEnEstado(String estado, double monto) {
        theActorInTheSpotlight().should(
                seeThat(LaRespuesta.campo("estado"), equalTo(estado)),
                seeThat(LaCotizacion.numero("monto"), equalTo(monto)));
    }

    @When("paga la matricula con {string}")
    public void pagaLaMatriculaCon(String metodo) {
        Actor actor = theActorInTheSpotlight();
        Integer id = actor.recall("idMatricula");
        actor.attemptsTo(PagarMatricula.con(id, metodo));
    }

    @Then("la matricula pagada tiene estado {string} y fecha de pago")
    public void laMatriculaPagadaTiene(String estado) {
        theActorInTheSpotlight().should(
                seeThat(LaRespuesta.campo("estado"), equalTo(estado)),
                seeThat(LaRespuesta.campo("fechaPago"), notNullValue(String.class)));
    }

    @When("consulta sus pagos")
    public void consultaSusPagos() {
        theActorInTheSpotlight().attemptsTo(ConsultarPagos.propios());
    }

    @Then("el pago de la matricula figura con metodo {string} y un numero de operacion con formato OP-000000")
    public void elPagoFigura(String metodo) {
        Actor actor = theActorInTheSpotlight();
        Integer id = actor.recall("idMatricula");
        actor.should(
                seeThat(ElPago.metodo(id), equalTo(metodo)),
                seeThat(ElPago.numeroDeOperacion(id), matchesPattern("OP-\\d{6}")));
    }
}
