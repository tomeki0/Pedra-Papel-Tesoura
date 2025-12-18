package com.guilima.pedra_papel_tesoura;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView txtResultado;
    TextView txtJogada;
    ImageView btnPedra;
    ImageView btnPapel;
    ImageView btnTesoura;
    ImageView imgResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtResultado = findViewById(R.id.txtResultado);
        txtJogada = findViewById(R.id.txtJogada);
        btnPedra = findViewById(R.id.imgPedra);
        btnPapel = findViewById(R.id.imgPapel);
        btnTesoura = findViewById(R.id.imgTesoura);
        imgResult = findViewById(R.id.imgResult);
    }

    public void cliqueBtnPedra(View view) {
        txtJogada.setText("Você escolheu Pedra");
        String resultado = fazerJogada();

        if (resultado.equals("Pedra")) {
            txtResultado.setText("Pedra com Pedra = EMPATE");
            imgResult.setImageDrawable(getResources().getDrawable(R.drawable.pedra_pedra));
            imgResult.setVisibility(View.VISIBLE);

        } else if (resultado.equals("Papel")) {
            txtResultado.setText("Papel engole Pedra = Você PERDEU");
            imgResult.setImageDrawable(getResources().getDrawable(R.drawable.papel_pedra));
            imgResult.setVisibility(View.VISIBLE);

        } else if (resultado.equals("Tesoura")) {
            txtResultado.setText("Pedra quebra Tesoura = Você GANHOU");
            imgResult.setImageDrawable(getResources().getDrawable(R.drawable.pedra_tesoura));
            imgResult.setVisibility(View.VISIBLE);
        }
    }
    public void cliqueBtnPapel(View view) {

        txtJogada.setText("Você escolheu Papel");
        String resultado = fazerJogada();

        if (resultado.equals("Pedra")) {
            txtResultado.setText("Papel engole Pedra = Você ganhou");
            imgResult.setVisibility(View.INVISIBLE);

        } else if (resultado.equals("Papel")) {
            txtResultado.setText("Papel com Papel = Empate");
            imgResult.setVisibility(View.INVISIBLE);

        } else if (resultado.equals("Tesoura")) {
            txtResultado.setText("Tesoura corta Papel = Você perdeu");
            imgResult.setVisibility(View.INVISIBLE);
        }
    }
    public void cliqueBtnTesoura(View view) {

        txtJogada.setText("Você escolheu Tesoura");
        String resultado = fazerJogada();

        if (resultado.equals("Pedra")) {
            txtResultado.setText("Pedra quebra Tesoura = Você perdeu");
            imgResult.setVisibility(View.INVISIBLE);

        } else if (resultado.equals("Papel")) {
            txtResultado.setText("Tesoura corta Papel = Você ganhou");
            imgResult.setVisibility(View.INVISIBLE);

        } else if (resultado.equals("Tesoura")) {
            txtResultado.setText("Tesoura com Tesoura = Empate");
            imgResult.setVisibility(View.INVISIBLE);
        }

    }
    public String fazerJogada() {
        Random random = new Random();
        int numero = random.nextInt(3);
        String resultadoSorteio = "";

        switch (numero) {
            case 0:
                resultadoSorteio = "Pedra";
                break;
            case 1:
                resultadoSorteio = "Papel";
                break;
            case 2:
                resultadoSorteio = "Tesoura";
                break;
        }

        return resultadoSorteio;
    }
}