package fpt.poly.thanhddph23154.asmapi;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DangKi extends AppCompatActivity {

    EditText edU, edPass, edPass1;
    Button btndk,btnsdt;

    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser curentUser = mAuth.getCurrentUser();
        if(curentUser != null){

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_ki);
        mAuth = FirebaseAuth.getInstance();

        edU = findViewById(R.id.edtU);
        edPass = findViewById(R.id.edtPass);
        edPass1 = findViewById(R.id.edtPass1);
        btndk = findViewById(R.id.btndk);
        btnsdt = findViewById(R.id.btnsdt);


        btndk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, pass, pass1;
                email = edU.getText().toString().trim();
                pass = edPass.getText().toString().trim();
                pass1 = edPass1.getText().toString().trim();


                String regexPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

                if (TextUtils.isEmpty(email) || !email.matches(regexPattern)) {
                    Toast.makeText(getApplicationContext(), "Email không đúng định dạng", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(getApplicationContext(), "Nhập mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pass1)) {
                    Toast.makeText(getApplicationContext(), "Nhập lại mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pass.length()>6){
                    Toast.makeText(getApplicationContext(), "Mật khẩu dài hơn 6 kí tự", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pass1.length()>6){
                    Toast.makeText(getApplicationContext(), "Mật khẩu dài hơn 6 kí tự", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pass.equals(pass1)) {
                    Toast.makeText(getApplicationContext(), "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email,pass)
                        .addOnCompleteListener(DangKi.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(getApplicationContext(), "Đăng ký thành công: " + user.getEmail(), Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(DangKi.this, MainActivity.class);
                                    intent.putExtra("email", user.getEmail()); // Truyền email
                                    intent.putExtra("pass",pass);     // truyền pass
                                    startActivity(intent);
                                    finish(); // Đóng màn hình đăng ký

                                }else {
                                    Toast.makeText(getApplicationContext(), "Đăng kí thất bại.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
}