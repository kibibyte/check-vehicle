package com.myapp.usecase.check

import spock.lang.Specification

import static com.myapp.usecase.check.MaintenanceFrequency.*
import static com.myapp.usecase.check.MaintenanceScore.*

class CheckCarResultTest extends Specification {

  def "should create CheckCarResult using numberOfAccidents"() {
    when:
    def result = new CheckCarResult(numberOfAccidents, null)

    then:
    result.getAccidentFree() == expectedResult

    where:
    numberOfAccidents || expectedResult
    null              || Optional.empty()
    0                 || Optional.of(true)
    1                 || Optional.of(false)
  }

  def "should create CheckCarResult using maintenanceFrequency"() {
    when:
    def result = new CheckCarResult(null, maintenanceFrequency)

    then:
    result.getMaintenanceScore() == expectedResult

    where:
    maintenanceFrequency || expectedResult
    null                 || Optional.empty()
    LOW                  || Optional.of(POOR)
    VERY_LOW             || Optional.of(POOR)
    MEDIUM               || Optional.of(AVERAGE)
    HIGH                 || Optional.of(GOOD)
  }
}