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

    private List<Opcao> opcoes = new ArrayList<>();
    private Paint paintCirculo;
    private Paint paintTexto;
    private Paint paintLinha;
    private RectF rectF;
    private List<Integer> cores;

    public RoletaView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paintCirculo = new Paint();
        paintCirculo.setAntiAlias(true);
        paintCirculo.setStyle(Paint.Style.FILL);

        paintTexto = new Paint();
        paintTexto.setAntiAlias(true);
        paintTexto.setColor(Color.WHITE);
        paintTexto.setTextSize(30);
        paintTexto.setTextAlign(Paint.Align.CENTER);

        paintLinha = new Paint();
        paintLinha.setAntiAlias(true);
        paintLinha.setColor(Color.WHITE);
        paintLinha.setStrokeWidth(5);

        rectF = new RectF();

        // Paleta de cores com bom contraste para texto branco
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

    public void setOpcoes(List<Opcao> opcoes) {
        this.opcoes = opcoes;
        invalidate(); // Redesenha a view
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (opcoes == null || opcoes.isEmpty()) {
            // Se não houver opções, desenha um círculo simples
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

        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float radius = getWidth() / 2f;

        rectF.set(0, 0, getWidth(), getHeight());

        float anguloPorFatia = 360f / numOpcoes;
        float anguloInicio = 0;

        for (int i = 0; i < numOpcoes; i++) {
            // Define a cor da fatia
            paintCirculo.setColor(cores.get(i % cores.size()));

            // Desenha a fatia
            canvas.drawArc(rectF, anguloInicio, anguloPorFatia, true, paintCirculo);

            // Calcula a posição do texto
            float anguloTexto = anguloInicio + (anguloPorFatia / 2);
            float x = (float) (centerX + (radius * 0.6) * Math.cos(Math.toRadians(anguloTexto)));
            float y = (float) (centerY + (radius * 0.6) * Math.sin(Math.toRadians(anguloTexto)));

            // Desenha o texto sem rotação
            canvas.drawText(opcoes.get(i).getTexto(), x, y - ((paintTexto.descent() + paintTexto.ascent()) / 2), paintTexto);

            // Desenha a linha separadora
            float xLinha = (float) (centerX + radius * Math.cos(Math.toRadians(anguloInicio)));
            float yLinha = (float) (centerY + radius * Math.sin(Math.toRadians(anguloInicio)));
            canvas.drawLine(centerX, centerY, xLinha, yLinha, paintLinha);

            anguloInicio += anguloPorFatia;
        }
    }
}
