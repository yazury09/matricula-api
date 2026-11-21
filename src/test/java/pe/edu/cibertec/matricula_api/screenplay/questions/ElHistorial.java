package pe.edu.cibertec.matricula_api.screenplay.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Question;

public class ElHistorial {
    public static Question<String> condicionDe(String codigoCurso) {
        return Question.about("la condicion de " + codigoCurso + " en el historial")
                .answeredBy(actor -> SerenityRest.lastResponse().jsonPath()
                        .getString("cursos.find { it.codigoCurso == '" + codigoCurso + "' }.condicion"));
    }

    public static Question<Integer> creditosAprobados() {
        return Question.about("los creditos aprobados")
                .answeredBy(actor -> SerenityRest.lastResponse().jsonPath().getInt("creditosAprobados"));
    }

    public static Question<Double> promedioPonderado() {
        return Question.about("el promedio ponderado")
                .answeredBy(actor -> SerenityRest.lastResponse().jsonPath().getDouble("promedioPonderado"));
    }
}
