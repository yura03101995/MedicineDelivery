package sprinwood.medicinedelivery;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Юра on 09.11.2016.
 */
public class Drug implements Parcelable {
    String nameDrug;
    String vendor;
    String cost;
    String count;
    Drug(String nameDrug, String  vendor, String cost, String count){
        this.nameDrug = nameDrug;
        this.vendor = vendor;
        this.cost = cost;
        this.count = count;
    }

    protected Drug(Parcel in) {
        nameDrug = in.readString();
        vendor = in.readString();
        cost = in.readString();
        count = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nameDrug);
        dest.writeString(vendor);
        dest.writeString(cost);
        dest.writeString(count);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Drug> CREATOR = new Creator<Drug>() {
        @Override
        public Drug createFromParcel(Parcel in) {
            return new Drug(in);
        }

        @Override
        public Drug[] newArray(int size) {
            return new Drug[size];
        }
    };

    public double getCost(){
        return Float.valueOf(this.cost);
    }

    public int getCount(){
        return Integer.valueOf(this.count);
    }

    public String getNameDrug(){
        return this.nameDrug;
    }

    public String getVendor(){
        return this.vendor;
    }
}
