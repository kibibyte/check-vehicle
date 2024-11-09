package com.myapp.usecase.check

import com.myapp.exceptions.InvalidArgumentException
import spock.lang.Specification

import static com.myapp.usecase.check.CheckCarFeature.ACCIDENT_FREE
import static com.myapp.usecase.check.CheckCarFeature.MAINTENANCE
import static java.util.Arrays.asList

class CheckCarQueryTest extends Specification {

  def "should create CheckCarQuery"() {
    when:
    new CheckCarQuery(vin, checkCarFeatures)

    then:
    noExceptionThrown()

    where:
    vin    | checkCarFeatures
    "1234" | new HashSet<>(asList(ACCIDENT_FREE));
    "1234" | new HashSet<>(asList(ACCIDENT_FREE, MAINTENANCE));
  }

  def "should throw validation exception"() {
    when:
    new CheckCarQuery(vin, checkCarFeatures)

    then:
    def e = thrown(InvalidArgumentException)
    e.message == expectedMessage

    where:
    vin    | checkCarFeatures                     || expectedMessage
    "1234" | null                                 || "Features cannot be empty"
    "1234" | new HashSet<CheckCarFeature>()       || "Features cannot be empty"
    null   | new HashSet<>(asList(ACCIDENT_FREE)) || "Vin cannot be empty"
    ""     | new HashSet<>(asList(ACCIDENT_FREE)) || "Vin cannot be empty"
  }
}