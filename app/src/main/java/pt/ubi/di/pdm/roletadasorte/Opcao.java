package pt.ubi.di.pdm.roletadasorte;

public class Opcao {
    private String texto;
    private int numero;

    public Opcao(String texto, int numero) {
        this.texto = texto;
        this.numero = numero;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
}