package com.guilima.pedra_papel_tesoura;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView txtResultado;
    TextView txtCombinacao;
    TextView txtJogada;
    ImageView btnPedra;
    ImageView btnPapel;
    ImageView btnTesoura;
    ImageView imgResult;
    VideoView videoJogada;
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
        txtCombinacao = findViewById(R.id.txtCombinacao);
        txtJogada = findViewById(R.id.txtJogada);
        btnPedra = findViewById(R.id.imgPedra);
        btnPapel = findViewById(R.id.imgPapel);
        btnTesoura = findViewById(R.id.imgTesoura);
        imgResult = findViewById(R.id.imgResult);
        videoJogada = findViewById(R.id.videoJogada);

        //deixando o texto de resultado invisivel, pois so vai aparecer depois de fazer a jogada
        txtResultado.setVisibility(View.INVISIBLE);

    }

    void imprimeVitoria(TextView view) {

        view.setText("VITÓRIA");
        view.setTextColor(Color.parseColor(getString(R.string.green_victory)));

    }
    void imprimeDerrota(TextView view) {

        view.setText("DERROTA");
        view.setTextColor(Color.parseColor(getString(R.string.red_defeat)));

    }
    void imprimeEmpate(TextView view) {

        view.setText("EMPATE");
        view.setTextColor(Color.parseColor(getString(R.string.gray_draw)));
    }


    public void cliqueBtnPedra(View view) {
        txtJogada.setText("Você escolheu: \nPEDRA");
        String resultado = fazerJogada();

        /* girar.setDuration(1500);
        girar.setInterpolator(new LinearInterpolator()); */

        if (resultado.equals("Pedra")) {

            txtCombinacao.setText("Pedra COM Pedra");
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pedra_pedra);
            videoJogada.setVideoURI(uri);

            videoJogada.setVisibility(View.VISIBLE);
            videoJogada.start();

            videoJogada.setOnCompletionListener(mp -> {
                videoJogada.pause();
                txtResultado.setVisibility(View.VISIBLE);
                txtCombinacao.setVisibility(View.VISIBLE);
                imprimeEmpate(txtResultado);
            });

        } else if (resultado.equals("Papel")) {

            txtCombinacao.setText("Pedra é ENGOLIDA por PAPEL");
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pedra_papel);
            videoJogada.setVideoURI(uri);

            videoJogada.setVisibility(View.VISIBLE);
            videoJogada.start();

            videoJogada.setOnCompletionListener(mp -> {
                videoJogada.pause();
                txtResultado.setVisibility(View.VISIBLE);
                txtCombinacao.setVisibility(View.VISIBLE);
                imprimeDerrota(txtResultado);
            });

        } else if (resultado.equals("Tesoura")) {

            txtCombinacao.setText("Pedra QUEBRA Tesoura");
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pedra_tesoura);
            videoJogada.setVideoURI(uri);

            videoJogada.setVisibility(View.VISIBLE);
            videoJogada.start();

            videoJogada.setOnCompletionListener(mp -> {
                videoJogada.pause();
                txtResultado.setVisibility(View.VISIBLE);
                txtCombinacao.setVisibility(View.VISIBLE);
                imprimeVitoria(txtResultado);
            });
            
        }
    }
    public void cliqueBtnPapel(View view) {

        txtJogada.setText("Você escolheu Papel");
        String resultado = fazerJogada();

        if (resultado.equals("Pedra")) {

            txtCombinacao.setText("Papel ENGOLE Pedra");
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.papel_pedra);
            videoJogada.setVideoURI(uri);

            videoJogada.setVisibility(View.VISIBLE);
            videoJogada.start();

            videoJogada.setOnCompletionListener(mp -> {
                videoJogada.pause();
                txtResultado.setVisibility(View.VISIBLE);
                txtCombinacao.setVisibility(View.VISIBLE);
                imprimeVitoria(txtResultado);
            });

        } else if (resultado.equals("Papel")) {

            txtCombinacao.setText("Papel COM Papel");
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.papel_papel);
            videoJogada.setVideoURI(uri);

            videoJogada.setVisibility(View.VISIBLE);
            videoJogada.start();

            videoJogada.setOnCompletionListener(mp -> {
                videoJogada.pause();
                txtResultado.setVisibility(View.VISIBLE);
                txtCombinacao.setVisibility(View.VISIBLE);
                imprimeEmpate(txtResultado);
            });

        } else if (resultado.equals("Tesoura")) {

            txtCombinacao.setText("Papel é CORTADO por Tesoura");
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.papel_tesoura);
            videoJogada.setVideoURI(uri);

            videoJogada.setVisibility(View.VISIBLE);
            videoJogada.start();

            videoJogada.setOnCompletionListener(mp -> {
                videoJogada.pause();
                txtResultado.setVisibility(View.VISIBLE);
                txtCombinacao.setVisibility(View.VISIBLE);
                imprimeDerrota(txtResultado);
            });

        }
    }
    public void cliqueBtnTesoura(View view) {

        txtJogada.setText("Você escolheu Tesoura");
        String resultado = fazerJogada();

        if (resultado.equals("Pedra")) {

            txtCombinacao.setText("Tesoura é QUEBRADA por Pedra");
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tesoura_pedra);
            videoJogada.setVideoURI(uri);

            videoJogada.setVisibility(View.VISIBLE);
            videoJogada.start();

            videoJogada.setOnCompletionListener(mp -> {
                videoJogada.pause();
                txtResultado.setVisibility(View.VISIBLE);
                txtCombinacao.setVisibility(View.VISIBLE);
                imprimeDerrota(txtResultado);
            });

        } else if (resultado.equals("Papel")) {

            txtCombinacao.setText("Tesoura CORTA Papel");
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tesoura_papel);
            videoJogada.setVideoURI(uri);

            videoJogada.setVisibility(View.VISIBLE);
            videoJogada.start();

            videoJogada.setOnCompletionListener(mp -> {
                videoJogada.pause();
                txtResultado.setVisibility(View.VISIBLE);
                txtCombinacao.setVisibility(View.VISIBLE);
                imprimeVitoria(txtResultado);
            });

        } else if (resultado.equals("Tesoura")) {
            txtCombinacao.setText("Tesoura COM Tesoura");

            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tesoura_tesoura);
            videoJogada.setVideoURI(uri);

            videoJogada.setVisibility(View.VISIBLE);
            videoJogada.start();

            videoJogada.setOnCompletionListener(mp -> {
                videoJogada.pause();
                txtResultado.setVisibility(View.VISIBLE);
                txtCombinacao.setVisibility(View.VISIBLE);
                imprimeEmpate(txtResultado);
            });
        }
    }
    public String fazerJogada() {

        imgResult.setVisibility(View.INVISIBLE);
        videoJogada.setVisibility(View.INVISIBLE);
        txtCombinacao.setVisibility(View.INVISIBLE);

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
