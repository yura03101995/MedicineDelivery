package sprinwood.medicinedelivery;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    EditText etEmail;
    EditText etPass;
    Button btnRegistration;
    Button btnBackOnRegistraion;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        btnRegistration = (Button) findViewById(R.id.btnRegistrationOnRegistration);
        btnBackOnRegistraion = (Button) findViewById(R.id.btnBackOnRegistration);
        etEmail = (EditText) findViewById(R.id.etEmailOnRegistration);
        etPass = (EditText) findViewById(R.id.etPassOnRegistration);
        mAuth = FirebaseAuth.getInstance();
        btnBackOnRegistraion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SignIn.class);
                startActivity(intent);
            }
        });

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(String.valueOf(etEmail.getText()).equals("") || String.valueOf(etPass.getText()).equals("")){
                Toast.makeText(getApplicationContext(), "Field 'E-mail' or 'Password' is empty",Toast.LENGTH_SHORT).show();
            }
            else{
                String email = String.valueOf(etEmail.getText());
                String password = String.valueOf(etPass.getText());
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("TAG", "createUserWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.e("TAG", "signInWithEmail:failed", task.getException());
                                String str = String.valueOf(task.getException());
                                String out = "";
                                for(int i = 0; i < str.length(); i++){
                                    if(str.charAt(i) == '['){
                                        for(int j = i + 1; j < str.length(); j++){
                                            if(str.charAt(j) == ']'){
                                                break;
                                            }
                                            out += str.charAt(j);
                                        }
                                        break;
                                    }
                                }
                                Toast.makeText(RegistrationActivity.this, "Failed:" + out,
                                        Toast.LENGTH_LONG).show();
                            }
                            else{
                                Intent intent = new Intent(RegistrationActivity.this,ListActivity.class);
                                startActivity(intent);
                            }
                        // ...
                        }
                    });
            }
            }
        });
    }
}
