package com.myapp.usecase.check

import com.github.tomakehurst.wiremock.junit5.WireMockExtension
import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpStatus
import io.micronaut.runtime.server.EmbeddedServer
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.testcontainers.junit.jupiter.Testcontainers

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import static com.myapp.usecase.check.CheckCarFeature.ACCIDENT_FREE
import static com.myapp.usecase.check.CheckCarFeature.MAINTENANCE
import static com.myapp.usecase.check.MaintenanceScore.POOR
import static io.micronaut.http.MediaType.APPLICATION_JSON
import static io.restassured.RestAssured.given
import static java.util.Arrays.asList
import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.CoreMatchers.notNullValue

@Testcontainers(disabledWithoutDocker = true)
class CheckCarQueryControllerTest {

  @RegisterExtension
  static WireMockExtension wireMock = WireMockExtension.newInstance()
      .options(wireMockConfig().dynamicPort().usingFilesUnderClasspath("wiremock"))
      .build();

  Map<String, Object> getProperties() {
    Map.of(
        "micronaut.http.services.insurance.url", wireMock.baseUrl(),
        "micronaut.http.services.maintenance.url", wireMock.baseUrl()
    )
  }

  @Test
  void shouldCheckCar() {
    EmbeddedServer server = ApplicationContext.run(EmbeddedServer.class, getProperties())
    RestAssured.port = server.getPort()
    def vinToCheck = "existing_vin"
    def request = new CheckCarRequest(
        vinToCheck,
        new HashSet<CheckCarFeature>(asList(ACCIDENT_FREE, MAINTENANCE))
    )

    given().header("Content-Type", APPLICATION_JSON)
        .body(request)
        .when()
        .post("/check")
        .then()
        .statusCode(HttpStatus.OK.code)
        .body(
            "vin", is(vinToCheck),
            "requestId", notNullValue(),
            "accidentFree", is(false),
            "maintenanceScore", is(POOR.name())
        )
  }
}
