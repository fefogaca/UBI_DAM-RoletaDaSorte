package pt.ubi.di.pdm.roletadasorte;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RoletaView extends View {

    // Declaração das variáveis de desenho
    private List<Opcao> opcoes = new ArrayList<>(); // A lista de opções a serem desenhadas na roleta
    private Paint paintCirculo; // Paint para desenhar as fatias coloridas da roleta
    private Paint paintTexto; // Paint para desenhar o texto das opções
    private Paint paintLinha; // Paint para desenhar as linhas separadoras
    private RectF rectF; // Retângulo usado para desenhar as fatias (arcos)
    private List<Integer> cores; // Lista de cores para as fatias da roleta

    // Construtor da View
    public RoletaView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(); // Inicializa os objetos de desenho
    }

    // Método de inicialização
    private void init() {
        // Configuração do Paint para as fatias da roleta
        paintCirculo = new Paint();
        paintCirculo.setAntiAlias(true); // Suaviza as bordas
        paintCirculo.setStyle(Paint.Style.FILL); // Preenche as formas

        // Configuração do Paint para o texto
        paintTexto = new Paint();
        paintTexto.setAntiAlias(true);
        paintTexto.setColor(Color.WHITE); // Cor do texto
        paintTexto.setTextSize(30); // Tamanho do texto
        paintTexto.setTextAlign(Paint.Align.CENTER); // Alinhamento do texto

        // Configuração do Paint para as linhas separadoras
        paintLinha = new Paint();
        paintLinha.setAntiAlias(true);
        paintLinha.setColor(Color.WHITE); // Cor da linha
        paintLinha.setStrokeWidth(5); // Largura da linha

        rectF = new RectF(); // Inicializa o retângulo

        // Paleta de cores para as fatias da roleta
        cores = Arrays.asList(
                Color.parseColor("#E6194B"), Color.parseColor("#3CB44B"),
                Color.parseColor("#FFE119"), Color.parseColor("#4363D8"),
                Color.parseColor("#F58231"), Color.parseColor("#911EB4"),
                Color.parseColor("#46F0F0"), Color.parseColor("#F032E6"),
                Color.parseColor("#BCF60C"), Color.parseColor("#FABEBE"),
                Color.parseColor("#008080"), Color.parseColor("#E6BEFF"),
                Color.parseColor("#9A6324"), Color.parseColor("#FFFAC8"),
                Color.parseColor("#800000"), Color.parseColor("#AAFFC3")
        );
    }

    // Método para definir as opções da roleta e redesenhar a view
    public void setOpcoes(List<Opcao> opcoes) {
        this.opcoes = opcoes;
        invalidate(); // Força a view a ser redesenhada
    }

    // Método principal de desenho da view
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Se não houver opções, desenha um círculo cinza simples
        if (opcoes == null || opcoes.isEmpty()) {
            paintCirculo.setColor(Color.LTGRAY);
            canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, getWidth() / 2f, paintCirculo);
            return;
        }

        // Ajusta o tamanho do texto com base no número de opções
        int numOpcoes = opcoes.size();
        if (numOpcoes <= 4) {
            paintTexto.setTextSize(30);
        } else if (numOpcoes <= 8) {
            paintTexto.setTextSize(24);
        } else {
            paintTexto.setTextSize(18);
        }

        // Calcula as dimensões da roleta
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float radius = getWidth() / 2f;

        rectF.set(0, 0, getWidth(), getHeight());

        // Calcula o ângulo de cada fatia
        float anguloPorFatia = 360f / numOpcoes;
        float anguloInicio = 0;

        // Itera sobre as opções para desenhar cada fatia
        for (int i = 0; i < numOpcoes; i++) {
            // Define a cor da fatia, ciclando pela lista de cores
            paintCirculo.setColor(cores.get(i % cores.size()));

            // Desenha a fatia como um arco
            canvas.drawArc(rectF, anguloInicio, anguloPorFatia, true, paintCirculo);

            // Calcula a posição do texto no centro da fatia
            float anguloTexto = anguloInicio + (anguloPorFatia / 2);
            float x = (float) (centerX + (radius * 0.6) * Math.cos(Math.toRadians(anguloTexto)));
            float y = (float) (centerY + (radius * 0.6) * Math.sin(Math.toRadians(anguloTexto)));

            // Desenha o texto da opção
            canvas.drawText(opcoes.get(i).getTexto(), x, y - ((paintTexto.descent() + paintTexto.ascent()) / 2), paintTexto);

            // Desenha a linha separadora entre as fatias
            float xLinha = (float) (centerX + radius * Math.cos(Math.toRadians(anguloInicio)));
            float yLinha = (float) (centerY + radius * Math.sin(Math.toRadians(anguloInicio)));
            canvas.drawLine(centerX, centerY, xLinha, yLinha, paintLinha);

            // Atualiza o ângulo de início para a próxima fatia
            anguloInicio += anguloPorFatia;
        }
    }
}
