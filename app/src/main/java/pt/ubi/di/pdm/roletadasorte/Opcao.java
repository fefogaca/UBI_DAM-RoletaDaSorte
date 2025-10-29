package pt.ubi.di.pdm.roletadasorte;

// Classe que representa uma opção na roleta.
public class Opcao {
    private String texto; // O texto da opção
    private int numero; // O número da opção na lista

    // Construtor da classe
    public Opcao(String texto, int numero) {
        this.texto = texto;
        this.numero = numero;
    }

    // Getter para o texto da opção
    public String getTexto() {
        return texto;
    }

    // Setter para o texto da opção
    public void setTexto(String texto) {
        this.texto = texto;
    }

    // Getter para o número da opção
    public int getNumero() {
        return numero;
    }

    // Setter para o número da opção
    public void setNumero(int numero) {
        this.numero = numero;
    }
}