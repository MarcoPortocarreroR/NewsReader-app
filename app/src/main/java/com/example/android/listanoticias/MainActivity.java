package com.example.android.listanoticias;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    ArrayList<Noticia> noticias;
    RequestQueue queue;

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 1100;

    String source = "ars-technica";
    int numero_fuente = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button cargar = findViewById(R.id.cargar);
        Button frase = findViewById(R.id.frase);
        ListView listatNoticias = findViewById(R.id.listaNoticias);
        Button sugerir = findViewById(R.id.sugerencias);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

        sugerir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SugerenciaActivity.class);
                startActivity(intent);
            }
        });

        cargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizarLista(source);
            }
        });



        frase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),FraseActivity.class);
                startActivity(intent);
            }
        });

        queue = Volley.newRequestQueue(this);
    }

    private void actualizarLista(String source) {
        noticias = new ArrayList<>();
        String url = "https://newsapi.org/v2/top-headlines?sources=" + source + "&apiKey=1bb40afc28f94031a189a8cbf4f49e82";


        JsonObjectRequest noticiasRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray datos = response.getJSONArray("articles");
                            for (int i = 0; i < datos.length(); i++) {
                                Noticia noticia = new Noticia();

                                JSONObject source = datos.getJSONObject(i).getJSONObject("source");

                                noticia.setId(source.getString("id"));
                                noticia.setNombre(source.getString("name"));
                                noticia.setAutor(datos.getJSONObject(i).getString("author"));
                                noticia.setTitulo(datos.getJSONObject(i).getString("title"));

                                noticias.add(noticia);
                            }

                            ListView listatNoticias = findViewById(R.id.listaNoticias);
                            NoticiaAdapter noticiaAdapter = new NoticiaAdapter(getApplicationContext(), noticias);
                            listatNoticias.setAdapter(noticiaAdapter);

                        } catch (Exception e) {
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

    @Override
    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this,senAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {

                    Log.d("sensor", "Cambio de noticia");
                    actualizarLista("cnn-es");
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
