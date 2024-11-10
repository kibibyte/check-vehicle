package com.myapp.usecase.check

import com.myapp.exceptions.InvalidArgumentException
import spock.lang.Specification

import static com.myapp.usecase.check.CheckCarFeature.ACCIDENT_FREE
import static com.myapp.usecase.check.CheckCarFeature.MAINTENANCE

class CheckCarQueryTest extends Specification {

  def "should create CheckCarQuery"() {
    when:
    def query = new CheckCarQuery(vin, checkCarFeatures)

    then:
    noExceptionThrown()

    where:
    vin    | checkCarFeatures
    "1234" | [ACCIDENT_FREE]
    "1234" | [ACCIDENT_FREE, MAINTENANCE]
  }

  def "should throw validation exception"() {
    when:
    new CheckCarQuery(vin, checkCarFeatures)

    then:
    def e = thrown(InvalidArgumentException)
    e.message == expectedResult

    where:
    vin    | checkCarFeatures || expectedResult
    "1234" | null             || "Features cannot be empty"
    "1234" | []               || "Features cannot be empty"
    null   | [ACCIDENT_FREE]  || "Vin cannot be empty"
    ""     | [ACCIDENT_FREE]  || "Vin cannot be empty"
  }
}