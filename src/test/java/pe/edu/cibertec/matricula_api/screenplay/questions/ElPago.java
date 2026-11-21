package pe.edu.cibertec.matricula_api.screenplay.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Question;

public class ElPago {
    public static Question<String> numeroDeOperacion(long idMatricula) {
        return Question.about("el numero de operacion del pago de la matricula " + idMatricula)
                .answeredBy(actor -> SerenityRest.lastResponse().jsonPath()
                        .getString("find { it.idMatricula == " + idMatricula + " }.numeroOperacion"));
    }

    public static Question<String> metodo(long idMatricula) {
        return Question.about("el metodo del pago de la matricula " + idMatricula)
                .answeredBy(actor -> SerenityRest.lastResponse().jsonPath()
                        .getString("find { it.idMatricula == " + idMatricula + " }.metodo"));
    }
}
