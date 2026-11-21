Feature: Calificacion de matriculas
  Como docente de CIBERTEC
  Quiero registrar la nota de una matricula pagada
  Para que el historial academico de la alumna se actualice

  Scenario Outline: El docente registra la nota y el historial se actualiza
    Given la alumna "i202610005" inicia sesion
    And la alumna consulta sus matriculas para obtener el id de su matricula del curso "PI5620"
    And el docente "D0001" inicia sesion
    When el docente registra la nota <nota> en esa matricula
    Then el codigo de respuesta es 200
    When la alumna consulta su historial
    Then la respuesta cumple el esquema "historial.json"
    And el historial muestra el curso "PI5620" como "<condicion>"
    And el historial tiene <creditos_aprobados> creditos aprobados y promedio ponderado <promedio_ponderado>

    Examples:
      | nota | condicion   | creditos_aprobados | promedio_ponderado |
      | 12   | DESAPROBADO | 7                  | 13.85              |
      | 12.5 | DESAPROBADO | 7                  | 14.08              |

  Scenario: Un docente que no dicta el curso intenta registrar la nota
    Given la alumna "i202610005" inicia sesion
    And la alumna consulta sus matriculas para obtener el id de su matricula del curso "PI5620"
    And el docente "D0002" inicia sesion
    When el docente registra la nota 12 en esa matricula
    Then el codigo de respuesta es 403
    And el codigo de error es "ACCESO_DENEGADO"
    And la respuesta cumple el esquema "error.json"

  Scenario: No se puede calificar una matricula que no esta pagada
    Given la alumna "i202610005" inicia sesion
    And la alumna consulta sus matriculas para obtener el id de su matricula del curso "PS5511"
    And el docente "D0001" inicia sesion
    When el docente registra la nota 12 en esa matricula
    Then el codigo de respuesta es 409
    And el codigo de error es "MATRICULA_NO_PAGADA"
    And la respuesta cumple el esquema "error.json"