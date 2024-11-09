package com.myapp.usecase.check

import spock.lang.Specification

import static com.myapp.usecase.check.MaintenanceFrequency.*
import static com.myapp.usecase.check.MaintenanceScore.*

class CheckCarResultTest extends Specification {

  def "should create CheckCarResult using numberOfAccidents"() {
    when:
    def result = new CheckCarResult(numberOfAccidents, null)

    then:
    result.getAccidentFree() == accidentFree

    where:
    numberOfAccidents || accidentFree
    null              || Optional.empty()
    0                 || Optional.of(true)
    1                 || Optional.of(false)
  }

  def "should create CheckCarResult using maintenanceFrequency"() {
    when:
    def result = new CheckCarResult(null, maintenanceFrequency)

    then:
    result.getMaintenanceScore() == maintenanceScore

    where:
    maintenanceFrequency || maintenanceScore
    null                 || Optional.empty()
    LOW                  || Optional.of(POOR)
    VERY_LOW             || Optional.of(POOR)
    MEDIUM               || Optional.of(AVERAGE)
    HIGH                 || Optional.of(GOOD)
  }
}