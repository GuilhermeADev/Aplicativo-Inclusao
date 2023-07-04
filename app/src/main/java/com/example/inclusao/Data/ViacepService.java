package com.example.inclusao.Data;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViacepService {
    private static final String BASE_URL = "https://viacep.com.br/ws/";

    private Retrofit retrofit;
    private ViacepAPI viacepAPI;

    public ViacepService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        viacepAPI = retrofit.create(ViacepAPI.class);
    }

    public ViacepAPI getViacepAPI() {
        return viacepAPI;
    }
}

