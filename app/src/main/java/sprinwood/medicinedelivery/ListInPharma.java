package sprinwood.medicinedelivery;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListInPharma extends AppCompatActivity {

    String namePharma;
    String idPharma;
    List<StringsThree> namesAndCosts;
    ListView lvListInPharma;
    FirebaseDatabase database;
    ShoppingCartObj scInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        database = FirebaseDatabase.getInstance();
        Intent intent = getIntent();

        namePharma = intent.getStringExtra("namePharma");
        idPharma = intent.getStringExtra("idPharma");
        scInfo = (ShoppingCartObj) intent.getParcelableExtra("ShoppingCart");

        getSupportActionBar().setTitle(namePharma);

        DatabaseReference refGlob = database.getReference();
        refGlob.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot pharma_drug_cost = dataSnapshot.child("pharma_drug_cost");
                DataSnapshot drugs_pharmacy = dataSnapshot.child("drugs_pharmacy");
                List<String> tmp = new ArrayList<String>();
                namesAndCosts = new ArrayList<StringsThree>();
                for(DataSnapshot dsp : pharma_drug_cost.getChildren()){
                    if(String.valueOf(dsp.child("id_pharma").getValue()).equals(idPharma)){
                        String idDrug   = String.valueOf(dsp.child("id_drug").getValue());
                        tmp.add(namePharma);
                        tmp.add(String.valueOf(drugs_pharmacy.child(idDrug).child("name").getValue()));
                        tmp.add(String.valueOf(dsp.child("cost").getValue()));
                        tmp.add(idPharma);
                        tmp.add(idDrug);
                        namesAndCosts.add(new StringsThree(tmp.get(0), tmp.get(1), tmp.get(2), tmp.get(3), tmp.get(4)));
                        tmp.clear();
                    }
                }
                lvListInPharma = (ListView) findViewById(R.id.lvInformation);

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
                        tvPharma.setText("");
                        return view;
                    }
                };

                // присваиваем адаптер списку
                lvListInPharma.setAdapter(adapter);
                //
                lvListInPharma.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent intent = new Intent(getApplicationContext(), Information.class);
                        StringsThree selectedFromList = (StringsThree) lvListInPharma.getItemAtPosition(position);
                        intent.putExtra("namePharma", selectedFromList.Pharma);
                        intent.putExtra("nameDrug", selectedFromList.Drug);
                        intent.putExtra("idDrug", selectedFromList.idDrug);
                        intent.putExtra("idPharma", selectedFromList.idPharma);
                        intent.putExtra("Cost", selectedFromList.Cost);
                        intent.putExtra("ShoppingCart", scInfo);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_in_pharma, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        MenuItem shoppingCart = menu.findItem(R.id.ShoppingCartInListPharma);
        shoppingCart.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent myIntent = new Intent(getApplicationContext(), ShoppingCart.class);
                myIntent.putExtra("ShoppingCart", scInfo);
                startActivity(myIntent);
                return true;
            }
        });
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                final List<StringsThree> findingNames = new ArrayList<StringsThree>();
                for(StringsThree s : namesAndCosts){
                    if(s.Drug.toLowerCase().contains(query)){
                        findingNames.add(s);
                    }
                }
                Collections.sort(findingNames);
                lvListInPharma = (ListView) findViewById(R.id.lvInformation);
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
                        tvPharma.setText("");
                        return view;
                    }
                };
                lvListInPharma.setAdapter(adapter);
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
                lvListInPharma = (ListView) findViewById(R.id.lvInformation);
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
                        tvPharma.setText("");
                        return view;
                    }
                };
                lvListInPharma.setAdapter(adapter);
                return false;
            }
        });

        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                Intent myIntent = new Intent(getApplicationContext(), ListActivity.class);
                startActivityForResult(myIntent, 0);
                return true;
            default:
                return true;
        }
    }
}
