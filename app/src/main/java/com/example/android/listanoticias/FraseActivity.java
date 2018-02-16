package com.example.android.listanoticias;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FraseActivity extends AppCompatActivity {

    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frase);

        final TextView frase = findViewById(R.id.frase);
        Button regresar = findViewById(R.id.regresar);
        Button obtener = findViewById(R.id.obtener);

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        obtener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizarLista();
            }
        });

        queue = Volley.newRequestQueue(this);
    }

    private void actualizarLista() {

        String url = "http://quotes.rest/qod.json";

        JsonObjectRequest noticiasRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            TextView frase = findViewById(R.id.frase);

                            JSONObject contenido = new JSONObject();
                            contenido = response.getJSONObject("contents");

                            JSONArray frases = new JSONArray();
                            frases = contenido.getJSONArray("quotes");
                            String texto = frases.getJSONObject(0).getString("quote");
                            Log.d("exito",texto);
                            frase.setText(texto);

                        } catch (Exception e) {
                            Log.d("error","fallo");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY", error.toString());
                    }

                }
        );
        queue.add(noticiasRequest);
    }
}
