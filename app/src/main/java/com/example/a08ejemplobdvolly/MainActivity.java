package com.example.a08ejemplobdvolly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_SELECT_FOTO1 = 1;
    ArrayList<String> arrLista;
    String strBMP;
    String imgString2;

    ImageView img1;
    EditText caja1, caja2, caja3, caja4;
    Button btn1, btn2;
    Spinner spn1;

    ArrayAdapter<String> adaptador;

    RequestQueue mRequestQueue;
    StringRequest mStringRequest;

    String url = "http://volly.atwebpages.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        caja1 = findViewById(R.id.editText1);
        caja2 = findViewById(R.id.editText2);
        caja3 = findViewById(R.id.editText3);
        caja4 = findViewById(R.id.editText4);

        img1 = findViewById(R.id.imageView1);

        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);

        spn1 = findViewById(R.id.spinner1);

        adaptador = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item);
        spn1.setAdapter(adaptador);

        arrLista = new ArrayList<>();

        llenarSpinner();

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarExplorador();
            }
        });

        spn1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                consultaNombre();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRequestQueue = Volley.newRequestQueue(MainActivity.this);

                mStringRequest = new StringRequest(Request.Method.POST, url + "consultar3.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject objetoJSON = new JSONObject(response);

                                    JSONArray vectorJSON = objetoJSON.getJSONArray("datos");

                                    caja1.setText(vectorJSON.getJSONObject(0).getString("nombre").toString());
                                    caja2.setText(vectorJSON.getJSONObject(0).getString("carrera").toString());
                                    caja3.setText(String.valueOf(vectorJSON.getJSONObject(0).getInt("semestre")));
                                    caja4.setText(String.valueOf(vectorJSON.getJSONObject(0).getInt("aula")));

                                } catch (JSONException e) {}
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {}
                }){
                    @Override
                    protected HashMap<String, String> getParams() {
                        HashMap<String, String> parametros = new HashMap<>();

                        return(parametros);
                    }
                };

                mRequestQueue.add(mStringRequest);
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mRequestQueue = Volley.newRequestQueue(MainActivity.this);

                mStringRequest = new StringRequest(Request.Method.POST, url + "registro3.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected HashMap<String, String> getParams() {
                        HashMap<String, String> parametros;
                        parametros = new HashMap<>();

                        parametros.put("nom", caja1.getText().toString());
                        parametros.put("carr", caja2.getText().toString());
                        parametros.put("sem", caja3.getText().toString());
                        parametros.put("aula", caja4.getText().toString());
                        parametros.put("imagen", strBMP.toString());
                        return parametros;
                    }
                };

                mRequestQueue.add(mStringRequest);
                Toast.makeText(MainActivity.this, "Datos guardados en AwardsPace correctamente!", Toast.LENGTH_LONG).show();

            }
        });

    }

    private void consultaNombre() {
        mRequestQueue = Volley.newRequestQueue(MainActivity.this);

        mStringRequest = new StringRequest(Request.Method.POST, url + "consultarNom.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject objetoJSON = new JSONObject(response);

                            JSONArray vectorJSON = objetoJSON.getJSONArray("datos");

                            caja1.setText(vectorJSON.getJSONObject(0).getString("nombre").toString());
                            caja2.setText(vectorJSON.getJSONObject(0).getString("carrera").toString());
                            caja3.setText(String.valueOf(vectorJSON.getJSONObject(0).getInt("semestre")));
                            caja4.setText(String.valueOf(vectorJSON.getJSONObject(0).getInt("aula")));
                            imgString2 = vectorJSON.getJSONObject(0).getString("imagen").toString();

                            Bitmap bmp2 = stringToBitMap(imgString2);

                            img1.setImageBitmap(bmp2);

                        } catch (JSONException e) {}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        }){
            @Override
            protected HashMap<String, String> getParams() {
                HashMap<String, String> parametros = new HashMap<>();

                parametros.put("nom", spn1.getSelectedItem().toString());
                return(parametros);
            }
        };

        mRequestQueue.add(mStringRequest);
    }

    private void llenarSpinner() {
        mRequestQueue = Volley.newRequestQueue(MainActivity.this);

        mStringRequest = new StringRequest(Request.Method.POST, url + "consultar3.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject objetoJSON = new JSONObject(response);

                            JSONArray vectorJSON = objetoJSON.getJSONArray("datos");

                            for (int i=0; i<vectorJSON.length(); i++) {
                                adaptador.add(vectorJSON.getJSONObject(i).getString("nombre").toString());
                            }
                            adaptador.notifyDataSetChanged();
                        } catch (JSONException e) {}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        }){
            @Override
            protected HashMap<String, String> getParams() {
                HashMap<String, String> parametros = new HashMap<>();

                return(parametros);
            }
        };

        mRequestQueue.add(mStringRequest);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_SELECT_FOTO1:
                //Se verifica que Intent mandó llamar y que se haya dado OK
                //al seleccionar la imagen
                if (requestCode == REQUEST_SELECT_FOTO1 && resultCode == RESULT_OK
                        && null != data) {

                    //Se extrae la información de la imagen seleccionada
                    Uri selectedImage = data.getData();

                    //Se crea una columna para la ruta y se le asigna la información
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    //Se crea el cursor que apuntará a la imagen seleccionada
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);

                    if (cursor == null || cursor.getCount() < 1) {
                        return; //Si el cursor no se crea o está vacio termina el proceso
                    }

                    //Se posiciona el cursor para leer la ruta de la imagen
                    cursor.moveToFirst();
                    //Se extrae el número de la columna en donde está la ruta de la imagen
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                    if(columnIndex < 0)
                        return; //Si no existe ruta valida de la imagen termina el proceso

                    //Se extrae la ruta del cursor y se guarda en una variable String
                    String picturePath = cursor.getString(columnIndex);

                    //arrLista.add(picturePath.toString());

                    //Utilizando la función redimensinar imagen se crea un Bitmap
                    //para cargarlo en el ImageView

                    Bitmap imgBMP = redimensionarImagen(picturePath.toString());

                    strBMP = bitMapToString(imgBMP);

                    //caja1.setText(strBMP.toString());

                    img1.setImageBitmap(imgBMP);
                    cursor.close(); //Se cierra el cursor
                }
            default: break;
        }
    }

    private void llamarExplorador() {
        //Se configura el Intent que tendrá como tarea mandar llamar la galería de imágenes
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        //Instrucción que ejecuta el Intent configurado y envía una constante para
        //identificar el código a procesar.
        startActivityForResult(i, REQUEST_SELECT_FOTO1);
    }

    //Función que reduce la resolución de una imagen para que pueda ser procesada
    //adecuadamente sin desbordamientos de memoria.
    public Bitmap redimensionarImagen(String absolutePath){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(absolutePath, options);
        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;

        int ratio;
        options.inJustDecodeBounds = false;

        if (imageWidth>imageHeight) {
            ratio = imageWidth/250;
        }
        else {
            ratio = imageHeight/250;
        }

        options.inSampleSize = ratio;

        return BitmapFactory.decodeFile(absolutePath, options);
    }

    public String bitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap stringToBitMap(String encodedString){
        try{
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
}