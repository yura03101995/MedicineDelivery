package sprinwood.medicinedelivery;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Юра on 09.11.2016.
 */
public class ShoppingCartObj implements Parcelable {
    List<Drug> drugs;
    String namePharma;
    String idPharma;
    ShoppingCartObj(String namePharma, String idPharma){
        this.namePharma = namePharma;
        this.idPharma = idPharma;
        drugs = new ArrayList<Drug>();
    }

    protected ShoppingCartObj(Parcel in) {
        namePharma = in.readString();
        idPharma = in.readString();
        drugs = new ArrayList<Drug>();
        in.readList(drugs,getClass().getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(namePharma);
        dest.writeString(idPharma);
        dest.writeList(drugs);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShoppingCartObj> CREATOR = new Creator<ShoppingCartObj>() {
        @Override
        public ShoppingCartObj createFromParcel(Parcel in) {
            return new ShoppingCartObj(in);
        }

        @Override
        public ShoppingCartObj[] newArray(int size) {
            return new ShoppingCartObj[size];
        }
    };

    public void add(Drug drug){
        if(drugs == null){
            drugs = new ArrayList<Drug>();
        }
        drugs.add(drug);
    }

    public Drug get(int position){
        return drugs.get(position);
    }

    public int size(){
        return drugs.size();
    }
}
