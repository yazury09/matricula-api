package pe.edu.cibertec.matricula_api.screenplay.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import pe.edu.cibertec.matricula_api.screenplay.questions.ElHistorial;
import pe.edu.cibertec.matricula_api.screenplay.task.CalificarMatricula;
import pe.edu.cibertec.matricula_api.screenplay.task.ConsultarHistorial;
import pe.edu.cibertec.matricula_api.screenplay.task.ConsultarMatriculas;
import pe.edu.cibertec.matricula_api.screenplay.task.IniciarSesion;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.equalTo;

public class CalificacionStepDefinitions {
    private static final String DOCENTE = "Docente";
    private static final String ALUMNA = "Alumna";

    private final String baseUrl = EnvironmentSpecificConfiguration
            .from(SystemEnvironmentVariables.currentEnvironmentVariables())
            .getProperty("restapi.baseurl");

    @Given("el docente {string} inicia sesion")
    public void elDocenteIniciaSesion(String codigo) {
        theActorCalled(DOCENTE).whoCan(CallAnApi.at(baseUrl));
        theActorInTheSpotlight().attemptsTo(IniciarSesion.como(codigo, "Cibertec2026"));
    }

    @Given("la alumna {string} inicia sesion")
    public void laAlumnaIniciaSesion(String codigo) {
        theActorCalled(ALUMNA).whoCan(CallAnApi.at(baseUrl));
        theActorInTheSpotlight().remember("codigoAlumna", codigo);
        theActorInTheSpotlight().attemptsTo(IniciarSesion.como(codigo, "Cibertec2026"));
    }

    @When("la alumna consulta sus matriculas para obtener el id de su matricula del curso {string}")
    public void laAlumnaGuardaElIdDeSuMatricula(String curso) {
        Actor alumna = theActorCalled(ALUMNA);
        alumna.attemptsTo(ConsultarMatriculas.propias());
        Integer id = SerenityRest.lastResponse().path("find { it.codigoCurso == '" + curso + "' }.id");
        alumna.remember("idMatricula", id);
    }

    @When("el docente registra la nota {double} en esa matricula")
    public void elDocenteRegistraLaNota(double nota) {
        Integer id = theActorCalled(ALUMNA).recall("idMatricula");
        theActorCalled(DOCENTE).attemptsTo(CalificarMatricula.de(id, nota));
    }

    @When("la alumna consulta su historial")
    public void laAlumnaConsultaSuHistorial() {
        Actor alumna = theActorCalled(ALUMNA);
        String codigo = alumna.recall("codigoAlumna");
        alumna.attemptsTo(ConsultarHistorial.de(codigo));
    }

    @Then("el historial muestra el curso {string} como {string}")
    public void elHistorialMuestraElCursoComo(String curso, String condicion) {
        theActorInTheSpotlight().should(
                seeThat(ElHistorial.condicionDe(curso), equalTo(condicion)));
    }

    @Then("el historial tiene {int} creditos aprobados y promedio ponderado {double}")
    public void elHistorialTiene(int creditos, double promedio) {
        theActorInTheSpotlight().should(
                seeThat(ElHistorial.creditosAprobados(), equalTo(creditos)),
                seeThat(ElHistorial.promedioPonderado(), equalTo(promedio)));
    }
}
