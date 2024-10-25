package com.example.alergiasapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class SeTuAlergia extends AppCompatActivity {

    private final ArrayList<String> alergiasSeleccionadas = new ArrayList<>();
    private LinearLayout llAlergiasSeleccionadas;
    private Spinner spinnerAlergias;
    private boolean isFirstSelection = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_se_tu_alergia);

        Button botonSeleccionarAlergia = findViewById(R.id.btnSeleccionarAlergias);
        Button botonContinuar = findViewById(R.id.btnContinuar);
        llAlergiasSeleccionadas = findViewById(R.id.llAlergiasSeleccionadas);
        spinnerAlergias = findViewById(R.id.spinnerAlergias);

        // Configura el Spinner con un adaptador personalizado
        ArrayAdapter<String> spinnerAdapter = createSpinnerAdapter();
        spinnerAlergias.setAdapter(spinnerAdapter);

        // Muestra la última opción como predeterminada en la vista inicial
        spinnerAlergias.setSelection(spinnerAdapter.getCount());

        // Configura el botón para desplegar el Spinner
        botonSeleccionarAlergia.setOnClickListener(view -> {
            // Muestra el Spinner cuando se hace clic en el botón
            spinnerAlergias.setVisibility(View.VISIBLE);
            spinnerAlergias.performClick();
        });

        spinnerAlergias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirstSelection) {
                    isFirstSelection = false;
                    return;
                }

                String alergiaSeleccionada = (String) parent.getItemAtPosition(position);
                if (!alergiasSeleccionadas.contains(alergiaSeleccionada) && !alergiaSeleccionada.equals(getString(R.string.stSeleccionarAlergias))) {
                    alergiasSeleccionadas.add(alergiaSeleccionada);
                    actualizarListaDeAlergias();
                }
                // Oculta el Spinner después de seleccionar una alergia
                spinnerAlergias.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });

        botonContinuar.setOnClickListener(view -> {
            Random random = new Random();
            int randomActivity = random.nextInt(3);
            Intent intent;

            switch (randomActivity) {
                case 0:
                    intent = new Intent(SeTuAlergia.this, BaseDatosNo.class);
                    break;
                case 1:
                    intent = new Intent(SeTuAlergia.this, NoAlergia.class);
                    break;
                case 2:
                default:
                    intent = new Intent(SeTuAlergia.this, SiAlergia.class);
                    break;
            }

            startActivity(intent);
        });
    }

    private ArrayAdapter<String> createSpinnerAdapter() {
        ArrayList<String> alergiasFullList = new ArrayList<>();
        alergiasFullList.add(getString(R.string.stSeleccionarAlergias));
        alergiasFullList.add(getString(R.string.stGluten));
        alergiasFullList.add(getString(R.string.stCrustaceos));
        alergiasFullList.add(getString(R.string.stHuevos));
        alergiasFullList.add(getString(R.string.stPescados));
        alergiasFullList.add(getString(R.string.stMani));
        alergiasFullList.add(getString(R.string.stLeche));
        alergiasFullList.add(getString(R.string.stNueces));
        alergiasFullList.add(getString(R.string.stSulfito));

        return new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, alergiasFullList) {
            @Override
            public int getCount() {
                // Excluye la última opción ("Seleccionar alergia") de la lista desplegable
                return super.getCount() - 1;
            }
        };
    }

    private void actualizarListaDeAlergias() {
        llAlergiasSeleccionadas.removeAllViews();
        for (String alergia : alergiasSeleccionadas) {
            LinearLayout itemLayout = new LinearLayout(this);
            itemLayout.setOrientation(LinearLayout.HORIZONTAL);

            TextView textoAlergia = new TextView(this);
            textoAlergia.setText(alergia);
            textoAlergia.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1
            ));
            textoAlergia.setPadding(16, 0, 16, 0);

            Button botonEliminar = new Button(this);
            botonEliminar.setText(R.string.stEliminar);
            botonEliminar.setOnClickListener(view -> {
                alergiasSeleccionadas.remove(alergia);
                actualizarListaDeAlergias();
            });

            itemLayout.addView(textoAlergia);
            itemLayout.addView(botonEliminar);

            llAlergiasSeleccionadas.addView(itemLayout);
        }
    }
}