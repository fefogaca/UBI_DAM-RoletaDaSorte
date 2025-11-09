package pt.ubi.di.pdm.roletadasorte;

import java.io.Serializable;

/**
 * @author Felipe Fogaça
 * Representa uma única opção de texto na roleta
 * Esta é uma classe de dados simples que contém apenas o texto da opção
 * Implementa Serializable para que possa ser passada entre atividades e salva em arquivos
 */
public class Opcao implements Serializable {
    private String texto;

    public Opcao(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}