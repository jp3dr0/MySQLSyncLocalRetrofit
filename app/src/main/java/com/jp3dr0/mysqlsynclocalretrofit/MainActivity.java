package com.jp3dr0.mysqlsynclocalretrofit;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    final String LOG = "LOG1";

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ContactAdapter adapter;
    List<Contact> contacts = new ArrayList<Contact>();
    BroadcastReceiver broadcastReceiver;
    public static DbHelper dbHelper; // SQLite

    EditText texto;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        texto = (EditText) findViewById(R.id.name);
        submit = (Button) findViewById(R.id.btnSubmit);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new ContactAdapter(contacts);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //readFromLocalStorage();

        //downloadDataFromAppServer();

        dbHelper = DbHelper.getInstance(this);

        // instanciando o broadcast receiver
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(getBaseContext(), "recebi o broadcast", Toast.LENGTH_LONG).show();
                downloadDataFromAppServer();
                readFromLocalStorage();
            }
        };

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitName(view);
            }
        });

        dbHelper.close();

        downloadDataFromAppServer();
        readFromLocalStorage();

        Log.d(LOG, "onCreate()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(broadcastReceiver, new IntentFilter(DbContract.UI_UPDATE_BROADCAST));
        //downloadDataFromAppServer();
        //readFromLocalStorage();
        Log.d(LOG, "onStart()");
        Log.d(LOG, "x - tamanho da lista de contatos locais: " + contacts.size());
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(DbContract.UI_UPDATE_BROADCAST));
        //downloadDataFromAppServer();
        //readFromLocalStorage();
        Log.d(LOG, "onResume()");
        Log.d(LOG, "x - tamanho da lista de contatos locais: " + contacts.size());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        registerReceiver(broadcastReceiver, new IntentFilter(DbContract.UI_UPDATE_BROADCAST));
        downloadDataFromAppServer();
        readFromLocalStorage();
        Log.d(LOG, "onRestart()");
        Log.d(LOG, "x - tamanho da lista de contatos locais: " + contacts.size());
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(broadcastReceiver);
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        Log.d(LOG, "onPause()");
        Log.d(LOG, "x - tamanho da lista de contatos locais: " + contacts.size());
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(broadcastReceiver);
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        Log.d(LOG, "onStop()");
        Log.d(LOG, "x - tamanho da lista de contatos locais: " + contacts.size());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(broadcastReceiver);
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        Log.d(LOG, "onDestroy()");
    }

    private void downloadDataFromAppServer() {
        final String metodo = "get-contacts";
        if (checkNetworkConnection()) {
            Retrofit retrofit = InterfaceAPI.api_builder;
            InterfaceAPI api = retrofit.create(InterfaceAPI.class);
            Call<List<Contact>> call = api.getContatos(metodo);

            call.enqueue(new Callback<List<Contact>>() {
                @Override
                public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                    Log.d(LOG, "getContatos(): onResponse: header de resposta do servidor: " + response.toString());
                    int code = response.code();
                    if (code == 200) {
                        List<Contact> lista = response.body();
                        Toast.makeText(getBaseContext(), "Total de contatos: " + lista.size(), Toast.LENGTH_LONG).show();
                        
                        contacts.clear();
                        
                        DbHelper dbHelper = new DbHelper(getApplicationContext());
                        SQLiteDatabase database = dbHelper.getReadableDatabase();
                        
                        Cursor cursor = dbHelper.readFromLocalDatabase(database);
                        
                        while (cursor.moveToNext()) {
                            int id = cursor.getInt(cursor.getColumnIndex(DbContract.ID));
                            String name = cursor.getString(cursor.getColumnIndex(DbContract.NAME));
                            int sync_status = cursor.getInt(cursor.getColumnIndex(DbContract.SYNC_STATUS));
                            contacts.add(new Contact(id, name, sync_status));
                            Log.d(LOG, "x - tamanho da lista de contatos locais: " + contacts.size());
                        }
                        
                        Log.d(LOG, "getContatos(): onResponse: 1 - tamanho da lista de contatos puxados do servidor: " + lista.size());
                        Log.d(LOG, "getContatos(): onResponse: 1 - tamanho da lista de contatos locais: " + contacts.size()):

                        int ts = lista.size();
                        int tl = contacts.size();
    
                        if (ts == 0) {                            
                            contacts.clear();
                        }
                        else if (tl == 0) {
                            for (int i = 0; i < s.size(); i++){
                                l.add(s.get(i));
                            }
                        }                       
                        else {
                            int maior_valor = -1;
                            int menor_valor = -1;
                            List<Contact> maior;
                            List<Contact> menor;
                            
                            if (ts > tl) {
                                maior = lista;
                                menor = contacts;
                                maior_valor = ts;
                                menor_valor = tl;
                            }
                            else {
                                maior = contacts;
                                menor = lista;
                                maior_valor = tl;
                                menor_valor = ts;
                            }
                            
                            if (menor_valor != 0) {
                                for (int i = 0; i <= maior_valor; i++) {                                    
                                    try { 
                                        if(maior.get(i).getId() != menor.get(i).getId()) {              
                                            boolean sucess = false;
                                            for (int y = 0; y < maior_valor; y++) {
                                                if (maior.get(y).getId() == menor.get(i).getId()){                  
                                                    sucess = true;
                                                    maior.set(y, menor.get(i)); 
                                                }
                                            }
                                            if (!sucess){                
                                                contacts.add(lista.get(i));
                                            }
                                        }
                                        else {
                                            System.out.println("chocolate");
                                            l.get(i).setName(s.get(i).getName());
                                        }
                                    } 
                                    catch (Exception e){                                        
                                        try{
                                            menor.add(maior.get(i));           
                                        }
                                        catch(Exception p){
                                            menor.add(maior.get(i - 1));
                                        }
                                    }         
                                }      
                            }    
                        } 
                        
                        Collections.sort(contacts);

                        Log.d(LOG, "getContatos(): onResponse: 2 - tamanho da lista de contatos puxados do servidor: " + lista.size());
                        Log.d(LOG, "getContatos(): onResponse: 2 - tamanho da lista de contatos locais: " + contacts.size());

                        cursor.close();
                        dbHelper.close();

                        adapter.notifyDataSetChanged(); // dá um refresh no recycler view
                    }
                    else {
                        Toast.makeText(getBaseContext(), "Falha: " + String.valueOf(code), Toast.LENGTH_LONG).show();

                    }

                }

                @Override
                public void onFailure(Call<List<Contact>> call, Throwable t) {
                    Log.e(LOG, "getContatos(): onFailure: deu merda: " + t.getMessage());
                    Log.e(LOG, "getContatos(): onFailure: Provavelmente não tem nada no banco de dados, então vou limpar o banco local.");
                    contacts.clear();
                    DbHelper dbHelper = new DbHelper(getApplicationContext());
                    SQLiteDatabase database = dbHelper.getReadableDatabase();
                    dbHelper.truncateContacts(database);
                    dbHelper.close();
                    adapter.notifyDataSetChanged();
                }
            });

        }
        else {
            Toast.makeText(getApplicationContext(), "Você está sem internet. Não deu pra baixar os dados da internet", Toast.LENGTH_LONG).show();
        }
    }

    // metodo para adicionar dados ao banco de dados do servidor (CREATE)
    private void saveToAppServer(final String name) {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        final int id = dbHelper.retornarUltimoId(database) + 1;
        dbHelper.close();
        // se tiver conecao com a internet, vai salvar no banco na internet
        if(checkNetworkConnection()) {
            Retrofit retrofit = InterfaceAPI.api_builder;
            InterfaceAPI api = retrofit.create(InterfaceAPI.class);
            Call<ResponseBody> call = api.existeContato(name);

            call.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d(LOG, "existeContato(): onResponse: header de resposta do servidor: " + response.toString());
                    Log.d(LOG, "existeContato(): onResponse: id: " + id + " size contatos locais: " + contacts.size());
                    try{
                        String json = response.body().string();
                        Log.d(LOG, "existeContato(): onResponse: resposta do servidor sem formatacao: " + json);
                        JSONObject data = new JSONObject(json);
                        Log.d(LOG, "existeContato(): onResponse: resposta do servidor declarada como JSONObject: " + data);
                        String resposta = data.getString("response");
                        Log.d(LOG, "existeContato(): onResponse: valor do objeto \"response\" do JSONObject de resposta do servidor: " + resposta);
                        if(resposta.equals("OK")){
                            saveToLocalStorage(id, name, DbContract.SYNC_STATUS_OK);
                        }
                        else {
                            saveToLocalStorage(id, name, DbContract.SYNC_STATUS_FAILED);
                        }
                    }
                    catch (JSONException e){
                        Log.e(LOG, "existeContato(): onResponse: JSONException: " + e.getMessage());
                    } catch (IOException e) {
                        Log.e(LOG, "existeContato(): onResponse: IOException: " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(LOG, "existeContato(): onFailure: deu merda " + t.getMessage());
                }

            });
        }
        // se nao tiver, vai salvar apenas no banco local
        else {
            saveToLocalStorage(id, name, DbContract.SYNC_STATUS_FAILED);
        }
        //readFromLocalStorage(); // consulta todos os dados no banco de novo, dando um refresh após adicionar
    }

    // metodo para adicionar dados ao banco de dados local (CREATE)
    private boolean saveToLocalStorage(int id, String name, int sync){
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        boolean sucess = dbHelper.saveToLocalDatabase(database, id, name, sync); // adiciona a coluna nome e o sync status
        readFromLocalStorage();
        dbHelper.close();
        return sucess;
    }

    // metodo para consultar o banco de dados local (READ)
    private void readFromLocalStorage() {
        contacts.clear();

        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = dbHelper.readFromLocalDatabase(database);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(DbContract.ID));
            String name = cursor.getString(cursor.getColumnIndex(DbContract.NAME));
            int sync_status = cursor.getInt(cursor.getColumnIndex(DbContract.SYNC_STATUS));
            contacts.add(new Contact(id, name, sync_status));
        }

        adapter.notifyDataSetChanged(); // dá um refresh no recycler view

        cursor.close();
        dbHelper.close();
    }

    // metodo para pegar o nome do layout, salvar no banco e resetar o campo
    public void submitName(View view) {
        String name = texto.getText().toString();
        //saveToLocalStorage(contacts.size() + 1, name, DbContract.SYNC_STATUS_FAILED);
        saveToAppServer(name);
        downloadDataFromAppServer();
        readFromLocalStorage();
        texto.setText("");
    }

    // metodo para checar se o usuario esta com internet
    private boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}
