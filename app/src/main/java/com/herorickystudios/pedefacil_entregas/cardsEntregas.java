package com.herorickystudios.pedefacil_entregas;

//Programado por HeroRickyGames


public class cardsEntregas {
    private String titulo;
    private String nome;
    private String local;
    private String localentrega;
    private String textDistanciadvc;
    private String preco;

    public cardsEntregas(String titulo, String nome , String local, String localentrega, String textDistanciadvc, String preco){
        this.titulo = String.valueOf(titulo);
        this.nome = String.valueOf(nome);
        this.local = String.valueOf(local);
        this.localentrega = String.valueOf(localentrega);
        this.textDistanciadvc = String.valueOf(textDistanciadvc);
        this.preco = String.valueOf(preco);
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
