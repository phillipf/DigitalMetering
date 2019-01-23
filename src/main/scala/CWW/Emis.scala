package CWW

import java.io.File

import com.univocity.parsers.csv.{CsvParser, CsvParserSettings}

import scala.collection.JavaConversions._

case class Emis(CONSUMER_NUMBER: Long,
                INSTALL_NUMBER: Long,
                ANSZIC_CODE: Int,
                ANSZIC_DESCRIPTION: String) {

}

object Emis {

  val anszic: Map[String, (Int, String)] = Map(
    ("NO INDUSTRY" -> (0, "NA")),
    ("AGRICULTURE - PLANTS" -> (100, "Agriculture")),
    ("FOOD PROCESSING" -> (1100, "Food Product Manufacturing")),
    ("MEAT PRODUCTS" -> (1111, "Meat Processing")),
    ("POULTRY" -> (1112, "Poultry Processing")),
    ("DAIRY PRODUCTS" -> (1130, "Dairy Product Manufacturing")),
    ("FAT & OIL PROCESSING" -> (1150, "Oil and Fat Manufacturing")),
    ("RENDERER" -> (1150, "Oil and Fat Manufacturing")),
    ("MALTSTERS" -> (1160, "Grain Mill and Cereal Product Manufacturing")),
    ("BAKERY" -> (1174, "Bakery Product Manufacturing (Non-factory based)")),
    ("BUILDING PRODUCTS" -> (1181, "Sugar Manufacturing")),
    ("RAW SUGAR" -> (1181, "Sugar Manufacturing")),
    ("CONFECTIONARY" -> (1182, "Confectionery Manufacturing")),
    ("FROZEN CONFECTIONARY" -> (1182, "Confectionery Manufacturing")),
    ("ANIMAL PRODUCTS" -> (1192, "Prepared Animal and Bird Feed Manufacturing")),
    ("BEVERAGES" -> (1210, "Beverage Manufacturing")),
    ("WOOL SCOURING" -> (1311, "Wool Scouring")),
    ("TANNING & RELATED INDUSTRIES" -> (1320, "Leather Tanning, Fur Dressing and Leather Product Manufacturing")),
    ("TEXTILES" -> (1330, "Textile Product Manufacturing")),
    ("WOOD PRODUCTS" -> (1400, "Wood Product Manufacturing")),
    ("PAPER MANUFACTURE" -> (1500, "Pulp, Paper and Converted Paper Product Manufacturing")),
    ("PAPER PRODUCTS" -> (1500, "Pulp, Paper and Converted Paper Product Manufacturing")),
    ("PRINTING" -> (1600, "Printing (including the Reproduction of Recorded Media)")),
    ("PETROCHEMICAL" -> (1701, "Petroleum Refining and Petroleum Fuel Manufacturing")),
    ("PETROLEUM PRODUCTS" -> (1709, "Other Petroleum and Coal Product Manufacturing")),
    ("CHEMICAL MANUFACTURE" -> (1800, "Basic Chemical and Chemical Product Manufacturing")),
    ("INDUSTRIAL GASES" -> (1811, "Industrial Gas Manufacturing")),
    ("PLASTIC, POLYMERS & SYNTHETIC RESINS" -> (1821, "Synthetic Resin and Synthetic Rubber Manufacturing")),
    ("RUBBER" -> (1821, "Synthetic Resin and Synthetic Rubber Manufacturing")),
    ("FERTILISERS" -> (1830, "Fertiliser and Pesticide Manufacturing")),
    ("BIOTECHNOLOGY" -> (1841, "Human Pharmaceutical and Medicinal Product Manufacturing")),
    ("PHARMACEUTICAL & VETERINARY PRODUCTS" -> (1842, "Veterinary Pharmaceutical and Medicinal Product Manufacturing")),
    ("CHEMICAL PRODUCTS" -> (1890, "Other Basic Chemical Product Manufacturing")),
    ("TYRE MANUFACTURE" -> (1914, "Tyre Manufacturing")),
    ("ADHESIVES/GLUES" -> (1915, "Adhesive Manufacturing")),
    ("INKS, PIGMENTS AND DYES" -> (1916, "Paint and Coatings Manufacturing")),
    ("PAINT" -> (1916, "Paint and Coatings Manufacturing")),
    ("PAINTING" -> (1916, "Paint and Coatings Manufacturing")),
    ("PLASTIC PRODUCTS" -> (1910, "Polymer Product Manufacturing")),
    ("CERAMIC, CEMENT & CONCRETE" -> (2030, "Cement, Lime, Plaster and Concrete Product Manufacturing")),
    ("METAL PRODUCTS" -> (2100, "Primary Metal and Metal Product Manufacturing")),
    ("METAL FINISHING" -> (2293, "Metal Coating and Finishing")),
    ("PARTS MANUFACTURE" -> (2310, "Motor Vehicle and Motor Vehicle Part Manufacturing")),
    ("VEHICLE ASSEMBLY & MANUFACTURE" -> (2310, "Motor Vehicle and Motor Vehicle Part Manufacturing")),
    ("RADIATORS" -> (2319, "Other Motor Vehicle Parts Manufacturing")),
    ("COMMUNICATION PRODUCTS" -> (2422, "Communication Equipment Manufacturing")),
    ("STONE" -> (2590, "Other Manufacturing")),
    ("ENERGY GENERATION" -> (2600, "Electricity Supply")),
    ("UTILITIES" -> (2600, "Electricity Supply")),
    ("INDUSTRIAL WASTE TREATERS" -> (2900, "Waste Collection, Treatment and Disposal Services")),
    ("SANITARY DISPOSAL SERVICE" -> (2900, "Waste Collection, Treatment and Disposal Services")),
    ("WATER TREATMENT AND RECYCLING" -> (2900, "Waste Collection, Treatment and Disposal Services")),
    ("RECYCLING" -> (2910, "Waste Collection Services")),
    ("CONTAMINATED SITE CLEANUP" -> (2920, "Waste Treatment, Disposal and Remediation Services")),
    ("CONSTRUCTION" -> (3000, "Building Construction")),
    ("RESTORATION" -> (3244, "Painting and Decorating Services")),
    ("CUTTING SERVICES" -> (3290, "Other Construction Services")),
    ("WOOL PRODUCTS" -> (3311, "Wool Wholesaling")),
    ("MARKET" -> (3601, "General Line Grocery Wholesaling")),
    ("SEAFOOD PRODUCTS" -> (3604, "Fish and Seafood Wholesaling")),
    ("VEHICLE SALES" -> (3900, "Motor Vehicle and Motor Vehicle Parts Retailing")),
    ("SERVICE STATION" -> (4000, "Fuel Retailing")),
    ("SUPERMARKET" -> (4110, "Supermarket and Grocery Stores")),
    ("BAKERY RETAIL" -> (4120, "Specialised Food Retailing")),
    ("BUTCHER RETAIL" -> (4121, "Fresh Meat, Fish and Poultry Retailing")),
    ("SEAFOOD RETAIL" -> (4121, "Fresh Meat, Fish and Poultry Retailing")),
    ("SOAP AND OTHER DETERGENTS" -> (4271, "Pharmaceutical, Cosmetic and Toiletry Goods Retailing")),
    ("FLORIST" -> (4274, "Flower Retailing")),
    ("ACCOMMODATION" -> (4400, "Accommodation")),
    ("CARAVAN PARK" -> (4400, "Accommodation")),
    ("HOTEL/MOTEL" -> (4400, "Accommodation")),
    ("RESTAURANT" -> (4511, "Cafes, Restaurants and Takeaway Food Services")),
    ("TAKE AWAY" -> (4512, "Takeaway Food Services")),
    ("CATERING" -> (4513, "Catering Services")),
    ("HOME CATERING" -> (4513, "Catering Services")),
    ("TRANSPORT" -> (4600, "Road Transport")),
    ("TRANSFER FACILITY" -> (4800, "Water Transport")),
    ("COMMUNICATIONS" -> (5100, "Postal and Courier Pick-up and Delivery Services")),
    ("DISTRIBUTION CENTRE" -> (5300, "Warehousing and Storage Services")),
    ("STORAGE FACILITY" -> (5300, "Warehousing and Storage Services")),
    ("HIRE SERVICES" -> (6600, "Rental and Hiring Services (except Real Estate)")),
    ("OFFICE/COMMERCIAL" -> (6712, "Non-Residential Property Operators")),
    ("RETAIL OUTLET" -> (6712, "Non-Residential Property Operators")),
    ("ENVIRONMENTAL MANAGEMENT" -> (6925, "Scientific Testing and Analysis Services")),
    ("LABORATORY" -> (6925, "Scientific Testing and Analysis Services")),
    ("PRODUCT TESTING" -> (6925, "Scientific Testing and Analysis Services")),
    ("RESEARCH & SCIENTIFIC INSTITUTION" -> (6925, "Scientific Testing and Analysis Services")),
    ("SIGNS & ADVERTISING DISPLAYS" -> (6940, "Advertising Services")),
    ("VETERINARY CLINIC" -> (6970, "Veterinary Services")),
    ("PHOTOGRAPHY" -> (6991, "Professional Photographic Services")),
    ("CONTAINER WASHING" -> (7311, "Building and Other Industrial Cleaning Services")),
    ("DRUM RECONDITIONER" -> (7311, "Building and Other Industrial Cleaning Services")),
    ("APPLIANCE MANUFACTURE" -> (7320, "Packaging Services")),
    ("GOVERNMENT/COMMUNITY SERVICES" -> (7500, "Public Administration")),
    ("QUARANTINE STATION" -> (7520, "State Government Administration")),
    ("COUNCIL" -> (7550, "Government Representation")),
    ("DEFENCE" -> (7600, "Defence")),
    ("EMERGENCY SERVICES" -> (7713, "Fire Protection and Other Emergency Services")),
    ("EDUCATION - PRIMARY" -> (8021, "Primary Education")),
    ("EDUCATION -SECONDARY" -> (8022, "Secondary Education")),
    ("EDUCATION - POST SECONDARY" -> (8100, "Tertiary Education")),
    ("COMMUNITY HALL" -> (8210, "Adult, Community and Other Education")),
    ("HOSPITAL/MENTAL INSTITUTION" -> (8401, "Hospitals (Except Psychiatric Hospitals)")),
    ("MEDICAL FACILITY" -> (8401, "Hospitals (Except Psychiatric Hospitals)")),
    ("NURSING HOME" -> (8601, "Aged Care Residential Services")),
    ("CHILDCARE" -> (8710, "Child Care Services")),
    ("RECREATION" -> (9100, "Sports and Recreation Activities")),
    ("ABRASIVES" -> (9410, "Automotive Repair and Maintenance")),
    ("AUTO BODY REPAIR" -> (9410, "Automotive Repair and Maintenance")),
    ("MAINTENANCE & SERVICING" -> (9410, "Automotive Repair and Maintenance")),
    ("VEHICLE WASHING & DETAILING" -> (9410, "Automotive Repair and Maintenance")),
    ("MORTUARY" -> (9520, "Funeral, Crematorium and Cemetery Services")),
    ("ANIMAL CARE" -> (9530, "Other Personal Services")),
    ("GARMENT WASHING & CLEANING" -> (9531, "Laundry and Dry-Cleaning Services")),
    ("CAR PARK" -> (9533, "Parking Services")),
    ("RELIGIOUS INSTITUTIONS" -> (9540, "Religious Services")),
    ("PESTICIDES" -> (1832, "Pesticide Manufacturing")),
    ("COSMETICS & TOILETRIES" -> (1852, "Cosmetic and Toiletry Preparation Manufacturing")),
    ("LANDFILL" -> (2921, "Waste Treatment and Disposal Services"))

  )

  val settings = new CsvParserSettings()
  settings.setNumberOfRowsToSkip(7)
  val reader = new CsvParser(settings)

  val data: List[Emis] = reader.parseAll(new File("N:/ABR/TradeWasteCustomers/05072018_ActiveInactive.csv"))
    .map(values => {
      val consumerNo = values(0).toLong
      val installNo = values(3).toLong
      val (anszicCode, anszicIndustry) = anszic(values(14))


      Emis(consumerNo,
        installNo,
        anszicCode,
        anszicIndustry)
    }).toList

  val emisMap: Map[Int,(Int, String)] = this.data.map(customer =>
    customer.INSTALL_NUMBER.toString.drop(2).toInt ->
      (customer.ANSZIC_CODE, customer.ANSZIC_DESCRIPTION)
  ).toMap


}