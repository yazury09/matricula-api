Feature: Matricula del alumno

  Background:
    Given que el alumno "i202610002" inicia sesion

  Scenario: Cotizar el curso DM5512
    When cotiza el curso "DM5512"
    Then el codigo de respuesta es 200
    And la cotizacion tiene 4 creditos, subtotal 480.0, descuento 10 y total 432.0

  Scenario: Matricularse y pagar el curso DM5512
      When se matricula en el curso "DM5512"
      Then el codigo de respuesta es 201
      And la matricula queda en estado "REGISTRADA" con monto 432.0
      And la respuesta cumple el esquema "matricula.json"
      When paga la matricula con "TRANSFERENCIA"
      Then el codigo de respuesta es 200
      And la matricula pagada tiene estado "PAGADA" y fecha de pago
      When consulta sus pagos
      Then el pago de la matricula figura con metodo "TRANSFERENCIA" y un numero de operacion con formato OP-000000

      Scenario: Regla de negocio que debe fallar, matricula duplicada en PS5511
          When se matricula en el curso "PS5511"
          Then el codigo de respuesta es 409
          And el codigo de error es "MATRICULA_DUPLICADA"
          And la respuesta cumple el esquema "error.json"