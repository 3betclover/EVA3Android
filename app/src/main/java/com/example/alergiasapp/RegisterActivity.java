package com.example.alergiasapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText Nombre, Rut, Password;
    Button Aceptar, Volver;

    // Referencia a la base de datos de Firebase
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializar la referencia de la base de datos
        databaseReference = FirebaseDatabase.getInstance("https://dbalergias-default-rtdb.firebaseio.com/").getReference("usuarios");

        Nombre = findViewById(R.id.etNombre);
        Rut = findViewById(R.id.etRut);
        Password = findViewById(R.id.etPassword);
        Aceptar = findViewById(R.id.btnAceptar);
        Volver = findViewById(R.id.btnVolver);

        Aceptar.setOnClickListener(view -> {
            String nombre = Nombre.getText().toString();
            String rut = Rut.getText().toString();
            String password = Password.getText().toString();

            if (!nombre.isEmpty() && !rut.isEmpty() && !password.isEmpty()) {
                // Crear un objeto de usuario con los valores ingresados
                Usuario nuevoUsuario = new Usuario(nombre, rut, password);

                // Generar una clave única para cada usuario
                String userId = databaseReference.push().getKey();

                // Guardar los datos del usuario en Firebase
                if (userId != null) {
                    databaseReference.child(userId).setValue(nuevoUsuario)
                            .addOnSuccessListener(aVoid -> {
                                // Limpiar los campos y mostrar un mensaje de éxito
                                Nombre.setText("");
                                Rut.setText("");
                                Password.setText("");
                                Toast.makeText(getApplicationContext(), "Registro exitoso", Toast.LENGTH_LONG).show();
                            })
                            .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Error al registrar", Toast.LENGTH_LONG).show());
                }
            } else {
                Toast.makeText(getApplicationContext(), "Debe llenar todos los campos", Toast.LENGTH_LONG).show();
            }
        });

        Volver.setOnClickListener(view -> finish());
    }

    // Clase para representar el objeto Usuario
    public static class Usuario {
        public String nombre;
        public String rut;
        public String password;

        // Constructor vacío requerido para Firebase
        public Usuario() {
        }

        public Usuario(String nombre, String rut, String password) {
            this.nombre = nombre;
            this.rut = rut;
            this.password = password;
        }
    }
}
