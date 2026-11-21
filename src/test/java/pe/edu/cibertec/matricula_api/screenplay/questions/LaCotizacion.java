package pe.edu.cibertec.matricula_api.screenplay.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Question;

public class LaCotizacion {
    public static Question<Double> numero(String ruta) {
        return Question.about("el valor de " + ruta)
                .answeredBy(actor -> SerenityRest.lastResponse().jsonPath().getDouble(ruta));
    }
}
