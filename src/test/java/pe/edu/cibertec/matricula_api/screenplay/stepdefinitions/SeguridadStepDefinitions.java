package pe.edu.cibertec.matricula_api.screenplay.stepdefinitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import pe.edu.cibertec.matricula_api.screenplay.questions.ElCodigoDeError;
import pe.edu.cibertec.matricula_api.screenplay.questions.LaRespuesta;
import pe.edu.cibertec.matricula_api.screenplay.task.CerrarSesion;
import pe.edu.cibertec.matricula_api.screenplay.task.ConsultarPerfil;
import pe.edu.cibertec.matricula_api.screenplay.task.IniciarSesion;
import pe.edu.cibertec.matricula_api.screenplay.task.ListarAlumnos;
import pe.edu.cibertec.matricula_api.screenplay.task.RenovarToken;

import java.util.List;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class SeguridadStepDefinitions {
    @When("inicia sesion con el usuario {string} y la clave {string}")
    public void iniciaSesion(String usuario, String clave) {
        theActorInTheSpotlight().attemptsTo(IniciarSesion.como(usuario, clave));
    }

    @When("renueva su sesion con el refresh token")
    public void renuevaSuSesion() {
        Actor actor = theActorInTheSpotlight();
        // se guardan los tokens originales para compararlos y reutilizarlos despues
        actor.remember("tokenOriginal", actor.<String>recall("token"));
        actor.remember("refreshTokenOriginal", actor.<String>recall("refreshToken"));
        actor.attemptsTo(RenovarToken.con("refreshToken"));
    }

    @Then("recibe tokens nuevos distintos a los originales")
    public void recibeTokensNuevos() {
        Actor actor = theActorInTheSpotlight();
        actor.should(
                seeThat("el access token es nuevo", tokenGuardado("token"),
                        not(equalTo(actor.<String>recall("tokenOriginal")))));
        actor.should(
                seeThat("el refresh token es nuevo", tokenGuardado("refreshToken"),
                        not(equalTo(actor.<String>recall("refreshTokenOriginal")))));
    }

    @When("intenta renovar otra vez con el refresh token original")
    public void intentaRenovarConElOriginal() {
        theActorInTheSpotlight().attemptsTo(RenovarToken.con("refreshTokenOriginal"));
    }

    @When("consulta su perfil con su access token")
    public void consultaSuPerfil() {
        theActorInTheSpotlight().attemptsTo(ConsultarPerfil.propio());
    }

    @When("cierra sesion")
    public void cierraSesion() {
        theActorInTheSpotlight().attemptsTo(CerrarSesion.actual());
    }

    @When("intenta ingresar con el usuario {string} con estas claves incorrectas")
    public void intentaIngresarConClavesIncorrectas(String usuario, List<Map<String, String>> intentos) {
        Actor actor = theActorInTheSpotlight();
        for (Map<String, String> fila : intentos) {
            String n = fila.get("intento");
            actor.attemptsTo(IniciarSesion.como(usuario, fila.get("clave")));
            actor.should(seeThat("intento " + n + ": codigo HTTP",
                    LaRespuesta.codigoDeEstado(), equalTo(Integer.parseInt(fila.get("status")))));
            actor.should(seeThat("intento " + n + ": codigo de error",
                    ElCodigoDeError.deLaRespuesta(), equalTo(fila.get("codigo"))));
            actor.should(seeThatResponse("intento " + n + ": cumple error.json",
                    res -> res.body(matchesJsonSchemaInClasspath("schemas/error.json"))));
        }
    }

    @When("intenta listar todos los alumnos")
    public void intentaListarTodosLosAlumnos() {
        theActorInTheSpotlight().attemptsTo(ListarAlumnos.todos());
    }

    @Then("el error devuelto tiene el codigo {string}")
    public void elErrorTieneElCodigo(String codigo) {
        theActorInTheSpotlight().should(seeThat(ElCodigoDeError.deLaRespuesta(), equalTo(codigo)));
    }

    private Question<String> tokenGuardado(String clave) {
        return Question.about("el " + clave + " guardado en la memoria del actor")
                .answeredBy(a -> a.<String>recall(clave));
    }
}
