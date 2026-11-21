Feature: Seguridad de la sesion
  Como usuario de la API de matricula
  Quiero que mi sesion este protegida
  Para que nadie use mis credenciales de forma indebida

  Background:
    Given que Usuario consulta la API de matricula

   Scenario: Rotacion del refresh token
      When inicia sesion con el usuario "i202610002" y la clave "Cibertec2026"
      And renueva su sesion con el refresh token
      Then el codigo de respuesta es 200
      And recibe tokens nuevos distintos a los originales
      When intenta renovar otra vez con el refresh token original
      Then el codigo de respuesta es 401
      And el error devuelto tiene el codigo "REFRESH_REUTILIZADO"
      And la respuesta cumple el esquema "error.json"
      When consulta su perfil con su access token
      Then el codigo de respuesta es 401
      And el error devuelto tiene el codigo "TOKEN_INVALIDO"
      And la respuesta cumple el esquema "error.json"

  Scenario: Cierre de sesion
    When inicia sesion con el usuario "i202610002" y la clave "Cibertec2026"
    Then el codigo de respuesta es 200
    When cierra sesion
    Then el codigo de respuesta es 204
    When consulta su perfil con su access token
    Then el codigo de respuesta es 401
    And el error devuelto tiene el codigo "TOKEN_INVALIDO"
    And la respuesta cumple el esquema "error.json"

  Scenario: Bloqueo de cuenta por intentos fallidos
    When intenta ingresar con el usuario "i202610004" con estas claves incorrectas
      | intento | clave   | status | codigo                 |
      | 1       | Mala001 | 401    | CREDENCIALES_INVALIDAS |
      | 2       | Mala002 | 401    | CREDENCIALES_INVALIDAS |
      | 3       | Mala003 | 401    | CREDENCIALES_INVALIDAS |
      | 4       | Mala004 | 401    | CREDENCIALES_INVALIDAS |
      | 5       | Mala005 | 423    | CUENTA_BLOQUEADA       |
    And inicia sesion con el usuario "i202610004" y la clave "Cibertec2026"
    Then el codigo de respuesta es 423
    And el error devuelto tiene el codigo "CUENTA_BLOQUEADA"
    And la respuesta cumple el esquema "error.json"

  Scenario: Alumno sin permiso de administrador
    When inicia sesion con el usuario "i202610002" y la clave "Cibertec2026"
    Then el codigo de respuesta es 200
    When intenta listar todos los alumnos
    Then el codigo de respuesta es 403
    And el error devuelto tiene el codigo "ACCESO_DENEGADO"
    And la respuesta cumple el esquema "error.json"