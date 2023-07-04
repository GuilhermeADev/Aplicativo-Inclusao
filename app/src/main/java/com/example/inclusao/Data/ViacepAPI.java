package com.example.inclusao.Data;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ViacepAPI
{
    @GET("{cep}/json/")
    Call<Endereco> buscarEnderecoPorCEP(@Path("cep") String cep);
}
