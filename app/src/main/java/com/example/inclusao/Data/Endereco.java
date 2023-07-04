package com.example.inclusao.Data;



                // CRIAR AS VARIAVEIS DE ENDEREÇO

public class Endereco {
    private String cep;
    private String logradouro;
    private String localidade;
    private String uf;
    private String bairro;
    public String getCep() {
        return cep;
    }
    public String getLogradouro() {
        return logradouro;
    }
    public String getCidade() {
        return localidade;
    }
    public void setCep(String cep) {
        this.cep = cep;
    }
    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }
    public void setCidade(String cidade) {
        this.localidade = cidade;
    }
    public String getEstado() {
        return uf;
    }
    public void setEstado(String estado){
        this.uf = estado;
    }
    public String getBairro(){
    return bairro;
    }
    public void setBairro(java.lang.String bairro) {
        this.bairro = bairro;
    }
}