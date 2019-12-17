import org.specs2.Specification
import org.specs2.matcher.Matchers
import org.specs2.specification.core.SpecStructure
import RecursionSchemes.jsonToDataToJson

object RecursionSchemesSpec extends Specification with Matchers {
  override def is: SpecStructure =
    s2"""
      jsonToDataToJson should parse a json string,
      build a dataF, then generate a new json string $run
      """

  private val jsonString: String =
    """
      |{
      |  "field1": {
      |    "hi": "hello"
      |  },
      |  "field2": {
      |  },
      |  "field3": 5,
      |  "field4": "value1",
      |  "field5": [1, 2, 3],
      |  "field6": null,
      |  "field7": false
      |}
      |""".stripMargin

  def run = jsonToDataToJson(jsonString) should beRight(jsonString.filter(_ > ' '))
}
