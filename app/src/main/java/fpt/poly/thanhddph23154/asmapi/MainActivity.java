package fpt.poly.thanhddph23154.asmapi;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    TextView tvdk,tvF;

    EditText eduser,edpass;
    Button btndn ;

    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        tvdk = findViewById(R.id.tvdk);
        eduser= findViewById(R.id.edtk);
        edpass = findViewById(R.id.edpas);
        tvF= findViewById(R.id.tvF);


        btndn = findViewById(R.id.btndn);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("pass");

        // Hiển thị email & mật khẩu vào ô nhập
        if (email != null) {
            eduser.setText(email);
        }
        if (password != null) {
            edpass.setText(password);
        }



        tvdk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , DangKi.class);
                startActivity(intent);
            }
        });

        btndn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email, pass;
                email = eduser.getText().toString().trim();
                pass = edpass.getText().toString().trim();

                String regexPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

                if (TextUtils.isEmpty(email) || !email.matches(regexPattern)) {
                    Toast.makeText(getApplicationContext(), "Email không đúng định dạng", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    Toast.makeText(MainActivity.this, "Nhập mật  khẩu", Toast.LENGTH_SHORT).show();
                }

                mAuth.signInWithEmailAndPassword(email,pass).
                        addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information


                                    Toast.makeText(MainActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                    Intent intent1 = new Intent(MainActivity.this, HomeAct.class);
                                    startActivity(intent1);


                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(MainActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });





    }
}