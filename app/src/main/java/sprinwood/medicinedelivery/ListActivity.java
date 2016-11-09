package sprinwood.medicinedelivery;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    FirebaseDatabase database;
    ListView lvGlobalList;
    List<StringsThree> namesAndCosts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        database = FirebaseDatabase.getInstance();
        DatabaseReference myFirebaseRef = database.getReference();
        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                namesAndCosts = new ArrayList<StringsThree>();
                List<String> tmp = new ArrayList<String>();
                DataSnapshot pharma_drug_cost = snapshot.child("pharma_drug_cost");
                DataSnapshot drugs_pharmacy   = snapshot.child("drugs_pharmacy");
                DataSnapshot pharmacy_addr    = snapshot.child("pharmacy_addr");
                for (DataSnapshot dsp : pharma_drug_cost.getChildren()) {
                    String idPharma = String.valueOf(dsp.child("id_pharma").getValue());
                    String idDrug   = String.valueOf(dsp.child("id_drug").getValue());
                    tmp.add(String.valueOf(pharmacy_addr.child(idPharma).child("name").getValue()));
                    tmp.add(String.valueOf(drugs_pharmacy.child(idDrug).child("name").getValue()));
                    tmp.add(String.valueOf(dsp.child("cost").getValue()));
                    tmp.add(idPharma);
                    tmp.add(idDrug);
                    namesAndCosts.add(new StringsThree(tmp.get(0), tmp.get(1), tmp.get(2), tmp.get(3), tmp.get(4)));
                    tmp.clear();
                }


                // находим список
                lvGlobalList = (ListView) findViewById(R.id.lvInformation);

                // создаем адаптер
                ArrayAdapter<StringsThree> adapter = new ArrayAdapter<StringsThree>(getBaseContext(),
                        R.layout.list_item_global_search, R.id.tvDrugOnMyItem, namesAndCosts) {
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        StringsThree entry = namesAndCosts.get(position);
                        TextView tvPrice = (TextView) view.findViewById(R.id.tvPriceOnMyItem);
                        TextView tvName = (TextView) view.findViewById(R.id.tvDrugOnMyItem);
                        TextView tvPharma = (TextView) view.findViewById(R.id.tvPharmaOnMyItem);
                        tvPrice.setText(entry.Cost + " р.");
                        tvName.setText(entry.Drug);
                        tvPharma.setText(entry.Pharma);
                        return view;
                    }
                };

                // присваиваем адаптер списку
                lvGlobalList.setAdapter(adapter);
                //
                lvGlobalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent intent = new Intent(getApplicationContext(), Information.class);
                        StringsThree selectedFromList = (StringsThree) lvGlobalList.getItemAtPosition(position);
                        intent.putExtra("namePharma", selectedFromList.Pharma);
                        intent.putExtra("nameDrug", selectedFromList.Drug);
                        intent.putExtra("idDrug", selectedFromList.idDrug);
                        intent.putExtra("idPharma", selectedFromList.idPharma);
                        intent.putExtra("Cost", selectedFromList.Cost);
                        intent.putExtra("ShoppingCart", new ShoppingCartObj(selectedFromList.Pharma,selectedFromList.idPharma));
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("The read failed: ", databaseError.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_global_list, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        final List<StringsThree> findingNames = new ArrayList<StringsThree>();
        for(StringsThree s : namesAndCosts){
            if(s.Drug.toLowerCase().contains(query)){
                findingNames.add(s);
            }
        }
        Collections.sort(findingNames);
        lvGlobalList = (ListView) findViewById(R.id.lvInformation);
        ArrayAdapter<StringsThree> adapter = new ArrayAdapter<StringsThree> (getBaseContext(),
                R.layout.list_item_global_search, R.id.tvDrugOnMyItem, findingNames){
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                StringsThree entry = findingNames.get(position);
                TextView tvPrice = (TextView) view.findViewById(R.id.tvPriceOnMyItem);
                TextView tvName = (TextView) view.findViewById(R.id.tvDrugOnMyItem);
                TextView tvPharma = (TextView) view.findViewById(R.id.tvPharmaOnMyItem);
                tvPrice.setText(entry.Cost + " р.");
                tvName.setText(entry.Drug);
                tvPharma.setText(entry.Pharma);
                return view;
            }
        };
        lvGlobalList.setAdapter(adapter);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<StringsThree> findingNames = new ArrayList<StringsThree>();
        for(StringsThree s : namesAndCosts){
            if(s.Drug.toLowerCase().contains(newText)){
                findingNames.add(s);
            }
        }
        Collections.sort(findingNames);
        lvGlobalList = (ListView) findViewById(R.id.lvInformation);
        ArrayAdapter<StringsThree> adapter = new ArrayAdapter<StringsThree> (getBaseContext(),
                R.layout.list_item_global_search, R.id.tvDrugOnMyItem, findingNames){
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                StringsThree entry = findingNames.get(position);
                TextView tvPrice = (TextView) view.findViewById(R.id.tvPriceOnMyItem);
                TextView tvName = (TextView) view.findViewById(R.id.tvDrugOnMyItem);
                TextView tvPharma = (TextView) view.findViewById(R.id.tvPharmaOnMyItem);
                tvPrice.setText(entry.Cost + " р.");
                tvName.setText(entry.Drug);
                tvPharma.setText(entry.Pharma);
                return view;
            }
        };
        lvGlobalList.setAdapter(adapter);
        return false;
    }
}
