package ABR.lucene

import java.nio.file.Paths

import ABR.businessData
import com.outr.lucene4s._


/*object Lucene {

  val directory = Paths.get("index")
  val lucene = new Lucene(directory = Option(directory), defaultFullTextSearchable = true)
  //val writer = new IndexWriter(directory, new SimpleAnalyzer)
  //new DirectLucene()
  val businesses = lucene.create.searchable[searchableBusiness]

  businessData.processedBusinesses.foreach(x => businesses.insert(x).index())

  /*val ABN = lucene.create.field[String]("abn")
  val BN_NAME = lucene.create.field[String]("bn_name")*/

  /*businessData.processedBusinesses.foreach(x => if(x.ABN != "" && x.BN_NAME != "")
    lucene.doc().fields(ABN(x.ABN), BN_NAME(x.BN_NAME)).index())*/

}*/
