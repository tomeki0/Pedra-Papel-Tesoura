package com.guilima.pedra_papel_tesoura;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
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
    //RotateAnimation girar = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.6f, Animation.RELATIVE_TO_SELF, 0.6f);

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

        /* girar.setDuration(1500);
        girar.setInterpolator(new LinearInterpolator()); */

        if (resultado.equals("Pedra")) {
            txtResultado.setText("Pedra com Pedra = EMPATE");
            imgResult.setImageDrawable(getResources().getDrawable(R.drawable.img_pedra_pedra));
            imgResult.setVisibility(View.VISIBLE);
            //imgResult.startAnimation(girar);

        } else if (resultado.equals("Papel")) {
            txtResultado.setText("Papel engole Pedra = Você PERDEU");
            imgResult.setImageDrawable(getResources().getDrawable(R.drawable.img_papel_pedra));
            imgResult.setVisibility(View.VISIBLE);

        } else if (resultado.equals("Tesoura")) {
            txtResultado.setText("Pedra quebra Tesoura = Você GANHOU");
            imgResult.setImageDrawable(getResources().getDrawable(R.drawable.img_pedra_tesoura));
            imgResult.setVisibility(View.VISIBLE);
        }
    }
    public void cliqueBtnPapel(View view) {

        txtJogada.setText("Você escolheu Papel");
        String resultado = fazerJogada();

        if (resultado.equals("Pedra")) {
            txtResultado.setText("Papel engole Pedra = Você ganhou");
            imgResult.setImageDrawable(getResources().getDrawable(R.drawable.img_papel_pedra));
            imgResult.setVisibility(View.VISIBLE);

        } else if (resultado.equals("Papel")) {
            txtResultado.setText("Papel com Papel = Empate");
            imgResult.setImageDrawable(getResources().getDrawable(R.drawable.img_papel_papel));
            imgResult.setVisibility(View.VISIBLE);

        } else if (resultado.equals("Tesoura")) {
            txtResultado.setText("Tesoura corta Papel = Você perdeu");
            imgResult.setImageDrawable(getResources().getDrawable(R.drawable.img_tesoura_corta_papel));
            imgResult.setVisibility(View.VISIBLE);
        }
    }
    public void cliqueBtnTesoura(View view) {

        txtJogada.setText("Você escolheu Tesoura");
        String resultado = fazerJogada();

        if (resultado.equals("Pedra")) {
            txtResultado.setText("Pedra quebra Tesoura = Você perdeu");
            imgResult.setImageDrawable(getResources().getDrawable(R.drawable.img_pedra_tesoura));
            imgResult.setVisibility(View.VISIBLE);

        } else if (resultado.equals("Papel")) {
            txtResultado.setText("Tesoura corta Papel = Você ganhou");
            imgResult.setImageDrawable(getResources().getDrawable(R.drawable.img_tesoura_corta_papel));
            imgResult.setVisibility(View.VISIBLE);

        } else if (resultado.equals("Tesoura")) {
            txtResultado.setText("Tesoura com Tesoura = Empate");
            imgResult.setImageDrawable(getResources().getDrawable(R.drawable.img_tesoura_tesoura));
            imgResult.setVisibility(View.VISIBLE);
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