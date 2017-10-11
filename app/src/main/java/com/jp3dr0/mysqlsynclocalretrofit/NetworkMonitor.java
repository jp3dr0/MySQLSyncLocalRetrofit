package com.jp3dr0.mysqlsynclocalretrofit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NetworkMonitor extends BroadcastReceiver {

    final String LOG = "LOG2";

    @Override
    public void onReceive(final Context context, Intent intent) {
        if(checkNetworkConnection(context)) {
            final DbHelper dbHelper = new DbHelper(context);
            final SQLiteDatabase database = dbHelper.getWritableDatabase();

            Cursor cursor = dbHelper.readFromLocalDatabase(database);

            while (cursor.moveToNext()) {
                int sync_status = cursor.getInt(cursor.getColumnIndex(DbContract.SYNC_STATUS));
                if(sync_status == DbContract.SYNC_STATUS_FAILED) {
                    final String name = cursor.getString(cursor.getColumnIndex(DbContract.NAME));
                    Retrofit retrofit = InterfaceAPI.api_builder;
                    InterfaceAPI api = retrofit.create(InterfaceAPI.class);
                    Call<ResponseBody> call = api.existeContato(name);

                    call.enqueue(new Callback<ResponseBody>() {

                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.d(LOG, "onResponse: header de resposta do servidor: " + response.toString());
                            try{
                                String json = response.body().string();
                                Log.d(LOG, "onResponse: resposta do servidor sem formatacao: " + json);
                                JSONObject data = new JSONObject(json);
                                Log.d(LOG, "onResponse: resposta do servidor declarada como JSONObject: " + data);
                                String resposta = data.getString("response");
                                Log.d(LOG, "onResponse: valor do objeto \"response\" do JSONObject de resposta do servidor: " + resposta);
                                if(resposta.equals("OK")){
                                    dbHelper.updateLocalDatabase(database, name, DbContract.SYNC_STATUS_OK);
                                    Intent intent = new Intent(DbContract.UI_UPDATE_BROADCAST);
                                    context.sendBroadcast(intent);
                                    //context.sendBroadcast(new Intent(DbContract.UI_UPDATE_BROADCAST));
                                }
                            }
                            catch (JSONException e){
                                Log.e(LOG, "onResponse: JSONException: " + e.getMessage());
                            } catch (IOException e) {
                                Log.e(LOG, "onResponse: IOException: " + e.getMessage());
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e(LOG, "onFailure: deu merda " + t.getMessage());
                        }

                    });
                }
            }

            if(cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

        }
    }

    // metodo para checar se o usuario esta com internet
    private boolean checkNetworkConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
