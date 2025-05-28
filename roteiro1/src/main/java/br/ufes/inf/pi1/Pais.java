package br.ufes.inf.pi1;

public class Pais {
    private String nome;
    private Double expec_vida;

    public Pais(String nome, Double expec_vida) {
        this.nome = nome;
        this.expec_vida = expec_vida;
    }

    public String getNome() {
        return nome;
    }

    public Double getExpecVida() {
        return expec_vida;
    }
    
    public int compareTo(Pais outro) {
        return this.expec_vida.compareTo(outro.expec_vida);
    }
}
