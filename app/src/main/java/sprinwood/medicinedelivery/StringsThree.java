package sprinwood.medicinedelivery;

/**
 * Created by Юра on 08.11.2016.
 */
public class StringsThree implements Comparable<StringsThree> {
    String Pharma;
    String Drug;
    String Cost;
    String idPharma;
    String idDrug;

    public StringsThree(String Pharma, String Drug, String Cost, String idPharma, String idDrug){
        this.Pharma = Pharma;
        this.Drug = Drug;
        this.Cost = Cost;
        this.idPharma = idPharma;
        this.idDrug = idDrug;
    }

    @Override
    public int compareTo(StringsThree f) {

        if (Float.valueOf(Cost) > Float.valueOf(f.Cost)) {
            return 1;
        }
        else if (Float.valueOf(Cost) < Float.valueOf(f.Cost)) {
            return -1;
        }
        else {
            return 0;
        }

    }
}
