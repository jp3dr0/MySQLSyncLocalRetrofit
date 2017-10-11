package com.jp3dr0.mysqlsynclocalretrofit;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface InterfaceAPI {
    // @FormUrlEncoded pois minha API Node+MySQL espera chave-valor no body (caso esperasse JSON, eu usaria um @Body Cliente como parâmetro).
    // Métodos com esta annotation devem possuir parâmetros com @Field indicando o nome de cada chave que será enviada no body.
    // Para saber os fields corretos, consulte a documentação da API
    @FormUrlEncoded
    @POST("syncinfo.php")
    Call<ResponseBody> existeContato(@Field("name") String name);

    //@POST("syncinfo.php")
    //Call<ResponseBody> existeContato(@Query("name") String name);

    @FormUrlEncoded
    @POST("syncinfo.php")
    Call<List<Contact>> getContatos(@Field("metodo") String metodo);

    //@POST("syncinfo.php")
    //Call<ResponseBody> getContatos(@Query("metodo") String metodo);

    public static final Retrofit api_builder = new Retrofit.Builder()
            .baseUrl(DbContract.SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
