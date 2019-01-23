package ABR.lucene

import com.outr.lucene4s._
import com.outr.lucene4s.field.Field
import com.outr.lucene4s.mapper.Searchable
import com.outr.lucene4s.query.SearchTerm

trait searchableBusiness extends Searchable[luceneBusiness] {
  // This is necessary for update and delete to reference the correct document.
  override def idSearchTerms(business: luceneBusiness): List[SearchTerm] = List(exact(ABN(business.ABN)))

  /*
    Though at compile-time all fields will be generated from the params in `Person`, for code-completion we can define
    an unimplemented method in order to properly reference the field. This will still compile without this definition,
    but most IDEs will complain.
   */
  def ABN: Field[Int]
}