package sprinwood.medicinedelivery;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Information extends AppCompatActivity {

    String namePharma;
    String nameDrug;
    String Cost;
    String idPharma;
    String idDrug;
    String vendor;
    TextView tvNameDrug;
    TextView tvVendor;
    TextView tvPriceInfo;
    ImageView ivMinus;
    ImageView ivPlus;
    TextView tvCounter;
    Button btnAdd;
    Button btnChooseDrug;
    FirebaseDatabase database;
    ShoppingCartObj scInfo;
    int counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_information);
        database = FirebaseDatabase.getInstance();
        Intent intent = getIntent();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        namePharma = intent.getStringExtra("namePharma");
        nameDrug = intent.getStringExtra("nameDrug");
        idDrug = intent.getStringExtra("idDrug");
        Cost = intent.getStringExtra("Cost");
        idPharma = intent.getStringExtra("idPharma");
        scInfo = (ShoppingCartObj) intent.getParcelableExtra("ShoppingCart");

        getSupportActionBar().setTitle(namePharma);
        tvNameDrug = (TextView) findViewById(R.id.tvNameDrug);
        tvVendor = (TextView) findViewById(R.id.tvVendor);
        tvPriceInfo = (TextView) findViewById(R.id.tvPriceInfo);
        tvCounter = (TextView) findViewById(R.id.tvCounterInfo);
        ivMinus = (ImageView) findViewById(R.id.ivMinus);
        ivPlus = (ImageView) findViewById(R.id.ivPlus);
        btnAdd = (Button) findViewById(R.id.btnAddInfo);
        btnChooseDrug = (Button) findViewById(R.id.btnChooseDrugInfo);
        counter = 1;

        tvNameDrug.setText(nameDrug);
        tvPriceInfo.setText(Cost + " р.");
        tvCounter.setText("1");

        btnChooseDrug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), ListInPharma.class);
                myIntent.putExtra("namePharma", namePharma);
                myIntent.putExtra("idPharma", idPharma);
                myIntent.putExtra("ShoppingCart", scInfo);
                startActivity(myIntent);
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scInfo.add(new Drug(nameDrug,vendor,Cost,String.valueOf(counter)));
                Toast.makeText(Information.this, "Добавлено",
                        Toast.LENGTH_LONG).show();
            }
        });

        ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter > 1) {
                    tvCounter.setText(String.valueOf(--counter));
                }
            }
        });

        ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvCounter.setText(String.valueOf(++counter));
            }
        });

        DatabaseReference refToDrug = database.getReference("drugs_pharmacy/" + idDrug);
        refToDrug.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                vendor = String.valueOf(dataSnapshot.child("vendor_code").getValue());
                tvVendor.setText(vendor);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("The read failed: ", databaseError.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_information, menu);

        MenuItem ShoppingCartItem = menu.findItem(R.id.ShoppingCart);
        ShoppingCartItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent myIntent = new Intent(getApplicationContext(), ShoppingCart.class);
                myIntent.putExtra("ShoppingCart", scInfo);
                startActivity(myIntent);
                return true;
            }
        });
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), ListActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}
