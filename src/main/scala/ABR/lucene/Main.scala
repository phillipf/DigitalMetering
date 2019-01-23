package ABR.lucene

import ABR.lucene
import com.outr.lucene4s._

/*object Main {

  def main(args: Array[String]): Unit = {
    //println(businessData.processedBusinesses.size)
    val paged = lucene.Lucene.lucene.query().search()
    /*paged.entries.foreach { business =>
      //val ID  =business.id
      println(s"Business: $business")
    }*/
    //Lucene.businesses.query().highlight()
    /*val paged2 = Lucene.businesses.query().filter(parseFuzzy("sunny seafood~")).search()
    paged2.entries.foreach { business =>
      val name = business.BN_NAME
      println(s"business: $name")
    }

    val paged3 = Lucene.businesses.query().filter(parseFuzzy("jayroy richmond~")).search()
    paged3.entries.foreach { business =>
      val name = business.BN_NAME
      println(s"business: $name")
    }*/

    val paged4 = lucene.Lucene.businesses.query().filter(parseFuzzy("royal saxon~")).search()
    paged4.entries.foreach { business =>
      val name = business.BN_NAME
      println(s"business: $name")
    }


  }

}*/
