package com.example.alergiasapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;

public class MainActivity extends AppCompatActivity {
    EditText NombreUsuario, Password;
    Button Ingresar, Registro;

    // Referencia a la base de datos de Firebase
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar la referencia de la base de datos
        databaseReference = FirebaseDatabase.getInstance("https://dbalergias-default-rtdb.firebaseio.com/").getReference("usuarios");

        NombreUsuario = findViewById(R.id.etNombreUsuario);
        Password = findViewById(R.id.etPassword);
        Ingresar = findViewById(R.id.btnIngresar);
        Registro = findViewById(R.id.btnRegistro);

        Ingresar.setOnClickListener(view -> {
            String nombreUsuario = NombreUsuario.getText().toString();
            String password = Password.getText().toString();

            if (!nombreUsuario.isEmpty() && !password.isEmpty()) {
                // Verificar el usuario en la base de datos Firebase
                databaseReference.orderByChild("nombre").equalTo(nombreUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot usuarioSnapshot : dataSnapshot.getChildren()) {
                                String passwordFirebase = usuarioSnapshot.child("password").getValue(String.class);
                                if (passwordFirebase != null && passwordFirebase.equals(password)) {
                                    Toast.makeText(getApplicationContext(), "Login exitoso", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(MainActivity.this, SeTuAlergia.class);
                                    startActivity(intent);
                                    return;
                                }
                            }
                            Toast.makeText(getApplicationContext(), "Contraseña incorrecta", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Usuario no encontrado", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Error al acceder a la base de datos", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "Debe ingresar el nombre de usuario y la contraseña", Toast.LENGTH_LONG).show();
            }
        });

        Registro.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
