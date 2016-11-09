package sprinwood.medicinedelivery;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

public class ShoppingCart extends AppCompatActivity {
    ListView lvProducts;
    Button btnChoose;
    TextView tvResult;
    ShoppingCartObj scInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        Intent intent = getIntent();
        getSupportActionBar().setTitle("Корзина");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        scInfo = (ShoppingCartObj) intent.getParcelableExtra("ShoppingCart");

        lvProducts = (ListView) findViewById(R.id.lvInformation);
        btnChoose = (Button) findViewById(R.id.btnChooseDrugSC);
        tvResult = (TextView) findViewById(R.id.tvResultSumSC);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), ListInPharma.class);
                myIntent.putExtra("namePharma", scInfo.namePharma);
                myIntent.putExtra("idPharma", scInfo.idPharma);
                myIntent.putExtra("ShoppingCart", scInfo);
                startActivity(myIntent);
            }
        });

        double summ = 0;
        for(Drug d : scInfo.drugs){
            summ += Float.valueOf(d.cost) * Integer.valueOf(d.count);
        }
        tvResult.setText("Итого: " + String.valueOf(summ) + " р.");


        final List<Drug> forAdapter = scInfo.drugs;
        if (forAdapter == null) {
            Log.d("mytag", scInfo.namePharma);
        }
        // создаем адаптер
        ArrayAdapter<Drug> adapter = new ArrayAdapter<Drug>(getBaseContext(),
                R.layout.list_item_shopping_cart, R.id.tvNameDrugItem, forAdapter) {
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                final ArrayAdapter<Drug> adp = this;
                Drug entry = forAdapter.get(position);
                final int positionToRemove = position;
                final TextView tvPrice = (TextView) view.findViewById(R.id.tvPriceInfoItem);
                TextView tvName = (TextView) view.findViewById(R.id.tvNameDrugItem);
                TextView tvVendor = (TextView) view.findViewById(R.id.tvVendorItem);
                final TextView tvCounter = (TextView) view.findViewById(R.id.tvCounterInfoItem);
                ImageView ivPlus = (ImageView) view.findViewById(R.id.ivPlusItem);
                ImageView ivMinus = (ImageView) view.findViewById(R.id.ivMinusItem);
                ImageView ivCross = (ImageView) view.findViewById(R.id.ivCrossItem);
                tvPrice.setText(entry.cost + " р.");
                tvName.setText(entry.nameDrug);
                tvVendor.setText(entry.vendor);
                tvCounter.setText(entry.count);
                ivCross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Drug dr_del = forAdapter.get(positionToRemove);
                        double summ = 0;
                        for(Drug d : scInfo.drugs){
                            summ += Float.valueOf(d.cost) * Integer.valueOf(d.count);
                        }
                        summ -= Float.valueOf(dr_del.cost) * Integer.valueOf(dr_del.count);
                        tvResult.setText("Итого: " + String.valueOf(summ) + " р.");
                        forAdapter.remove(positionToRemove);
                        adp.notifyDataSetChanged();
                    }
                });
                ivPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int counter = Integer.valueOf(String.valueOf(tvCounter.getText()));
                        forAdapter.get(positionToRemove).count = String.valueOf(++counter);
                        scInfo.get(positionToRemove).count = String.valueOf(counter);
                        double summ = 0;
                        for(Drug d : scInfo.drugs){
                            summ += Float.valueOf(d.cost) * Integer.valueOf(d.count);
                        }
                        tvResult.setText("Итого: " + String.valueOf(summ) + " р.");
                        tvCounter.setText(String.valueOf(counter));
                    }
                });
                ivMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int counter = Integer.valueOf(String.valueOf(tvCounter.getText()));
                        if (counter > 1) {
                            forAdapter.get(positionToRemove).count = String.valueOf(--counter);
                            scInfo.get(positionToRemove).count = String.valueOf(counter);
                            double summ = 0;
                            for(Drug d : scInfo.drugs){
                                summ += Float.valueOf(d.cost) * Integer.valueOf(d.count);
                            }
                            tvResult.setText("Итого: " + String.valueOf(summ) + " р.");
                            tvCounter.setText(String.valueOf(counter));
                        }
                    }
                });

                return view;
            }
        };
        // присваиваем адаптер списку
        lvProducts.setAdapter(adapter);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), ListActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}
