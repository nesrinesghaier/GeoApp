package eniso.bac.nesrine.mapapi.ui.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import eniso.bac.nesrine.mapapi.R;
import eniso.bac.nesrine.mapapi.ui.map.MapsActivity;

public class LoginActivity extends AppCompatActivity {
    Button login;
    EditText email, password;
    private FirebaseAuth mAuth;
    public FirebaseUser currentUser = null, user;
    Boolean test;
    String emailString, passwordString, checkLoginResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = (Button) findViewById(R.id.loginButton);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        test = false;
        if ((currentUser = mAuth.getCurrentUser()) != null) {
            checkLoginResult = currentUser.getEmail();
            startActivity(new Intent(getApplicationContext(), MapsActivity.class).putExtra("email", checkLoginResult));
        } else {
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    emailString = email.getText().toString().trim();
                    passwordString = password.getText().toString().trim();
                    if (!emailString.isEmpty() && !passwordString.isEmpty()) {
                        checkSignIn();
                    } else
                        Toast.makeText(getApplicationContext(), "Please enter email and Password", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void checkSignIn() {
        mAuth.signInWithEmailAndPassword(emailString, passwordString).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            test = true;
                            user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "login successful", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), MapsActivity.class).putExtra("email", user.getEmail()));
                        } else {
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            test = false;
                        }
                    }
                });

    }


}
