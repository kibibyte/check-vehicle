package com.myapp.usecase.check

import spock.lang.Specification

import static com.myapp.usecase.check.MaintenanceFrequency.*
import static com.myapp.usecase.check.MaintenanceScore.*

class CheckCarResultTest extends Specification {

  def "should create CheckCarResult using numberOfAccidents"() {
    when:
    def vin = "1234";
    def result = new CheckCarResult(vin, numberOfAccidents, null)

    then:
    result.getVin() == vin
    result.getAccidentFree() == expectedResult
    result.getMaintenanceScore() == Optional.empty()

    where:
    numberOfAccidents || expectedResult
    null              || Optional.empty()
    0                 || Optional.of(true)
    1                 || Optional.of(false)
  }

  def "should create CheckCarResult using maintenanceFrequency"() {
    when:
    def vin = "1234";
    def result = new CheckCarResult(vin, null, maintenanceFrequency)

    then:
    result.getVin() == vin
    result.getMaintenanceScore() == expectedResult
    result.getAccidentFree() == Optional.empty()

    where:
    maintenanceFrequency || expectedResult
    null                 || Optional.empty()
    LOW                  || Optional.of(POOR)
    VERY_LOW             || Optional.of(POOR)
    MEDIUM               || Optional.of(AVERAGE)
    HIGH                 || Optional.of(GOOD)
  }
}