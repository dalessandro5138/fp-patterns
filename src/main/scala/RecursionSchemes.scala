import cats.Functor
import higherkindness.droste._
import io.circe.{ parser, Json, ParsingFailure }

object RecursionSchemes {

  private trait DataF[A]
  private case class StructF[A](fields: Vector[(String, A)]) extends DataF[A]
  private case class ArrayF[A](as: Vector[A])                extends DataF[A]
  private case class NumberF[A](value: BigDecimal)           extends DataF[A]
  private case class StringF[A](value: String)               extends DataF[A]
  private case class BooleanF[A](value: Boolean)             extends DataF[A]
  private case class NullF[A]()                              extends DataF[A]

  private implicit val dataFunctor: Functor[DataF] = new Functor[DataF] {
    override def map[A, B](fa: DataF[A])(f: A => B): DataF[B] = fa match {
      case StructF(fields) => StructF(fields.map { case (k, v) => (k, f(v)) })
      case ArrayF(as)      => ArrayF[B](as.map(f))
      case NumberF(n)      => NumberF[B](n)
      case StringF(s)      => StringF[B](s)
      case BooleanF(b)     => BooleanF[B](b)
      case NullF()         => NullF[B]()
    }
  }

  private val jsonToData: Coalgebra[DataF, Json] = Coalgebra { json =>
    json.name match {
      case "Null"    => NullF()
      case "Boolean" => BooleanF(json.asBoolean.get)
      case "Number"  => NumberF(BigDecimal(json.asNumber.get.toString))
      case "String"  => StringF(json.asString.get)
      case "Array"   => ArrayF(json.asArray.get)
      case "Object"  => StructF(json.asObject.get.toVector)
    }
  }

  private val dataToJsonString: Algebra[DataF, String] = Algebra {
    case NullF()         => "null"
    case BooleanF(b)     => b.toString
    case NumberF(n)      => n.toString
    case StringF(s)      => s""""$s""""
    case ArrayF(as)      => as.mkString("[", ",", "]")
    case StructF(fields) => fields.map { case (k, v) => s""""$k":$v""" }.mkString("{", ",", "}")
  }

  def jsonToDataToJson(json: String): Either[ParsingFailure, String] =
    parser.parse(json).map { s =>
      scheme.hylo(dataToJsonString, jsonToData).apply(s)
    }

}
