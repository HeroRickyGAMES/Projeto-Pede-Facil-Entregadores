package com.herorickystudios.pedefacil_entregas;

//Programado por HeroRickyGames


public class cardsEntregas {
    private String titulo;
    private String nome;
    private String local;
    private String localentrega;
    private String textDistanciadvc;
    private String preco;
    private String estaAtivo;
    private String entreguePor;
    private String uidEntregaor;
    private String productID;

    public cardsEntregas(String titulo, String nome , String local, String localentrega, String textDistanciadvc, String preco, String estaAtivo, String entreguePor, String uidEntregaor, String productID){
        this.titulo = String.valueOf(titulo);
        this.nome = String.valueOf(nome);
        this.local = String.valueOf(local);
        this.localentrega = String.valueOf(localentrega);
        this.textDistanciadvc = String.valueOf(textDistanciadvc);
        this.preco = String.valueOf(preco);
        this.estaAtivo = String.valueOf(estaAtivo);
        this.entreguePor = String.valueOf(entreguePor);
        this.uidEntregaor = String.valueOf(uidEntregaor);
        this.productID = String.valueOf(productID);
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getUidEntregaor() {
        return uidEntregaor;
    }

    public void setUidEntregaor(String uidEntregaor) {
        this.uidEntregaor = uidEntregaor;
    }

    public String getEntreguePor() {
        return entreguePor;
    }

    public void setEntreguePor(String entreguePor) {
        this.entreguePor = entreguePor;
    }

    public String getEstaAtivo() {
        return estaAtivo;
    }

    public void setEstaAtivo(String estaAtivo) {
        this.estaAtivo = estaAtivo;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public String getTextDistanciadvc() {
        return textDistanciadvc;
    }

    public void setTextDistanciadvc(String textDistanciadvc) {
        this.textDistanciadvc = textDistanciadvc;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getLocalentrega() {
        return localentrega;
    }

    public void setLocalentrega(String localentrega) {
        this.localentrega = localentrega;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
