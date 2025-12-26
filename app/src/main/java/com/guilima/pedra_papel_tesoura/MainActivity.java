package com.guilima.pedra_papel_tesoura;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
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


    //Serve para passar como parametro para a funcao de exibirResultado e assim saber qual o resultado da jogada
    private final int ID_EMPATE = 0;
    private final int ID_VITORIA = 1;
    private final int ID_DERROTA = 2;


    private TextView txtResultado;
    private TextView txtCombinacao;
    private TextView txtJogada;
    private ImageView btnPedra;
    private ImageView btnPapel;
    private ImageView btnTesoura;
    private VideoView videoJogada;
    private FrameLayout videoContainer;

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
        videoJogada = findViewById(R.id.videoJogada);
        videoContainer = findViewById(R.id.videoContainer);

        //deixando o texto de resultado invisivel, pois so vai aparecer depois de fazer a jogada
        txtResultado.setVisibility(View.INVISIBLE);

    }

    //Funcoes para modificar o txtResultado de acordo com o resultado da jogada
    void imprimeVitoria(TextView view) {

        String txtVitoria = getResources().getString(R.string.txt_VITORIA);
        view.setText(txtVitoria);
        view.setTextColor(Color.parseColor(getString(R.string.green_victory)));

    }
    void imprimeDerrota(TextView view) {

        String txtDerrota = getResources().getString(R.string.txt_DERROTA);
        view.setText(txtDerrota);
        view.setTextColor(Color.parseColor(getString(R.string.red_defeat)));

    }
    void imprimeEmpate(TextView view) {

        String txtEmpate = getResources().getString(R.string.txt_EMPATE);
        view.setText(txtEmpate);
        view.setTextColor(Color.parseColor(getString(R.string.yellow_draw)));
    }

    //Funcao para dar play no video e e exibir o container com bordas arredondadas ao redor do video
    void playVideo(VideoView videoJogada) {

        this.videoJogada = videoJogada;
        videoContainer.setVisibility(View.VISIBLE);
        videoJogada.start();
    }

    //Funcao para exibir o resultado da jogada apos o video parar de tocar
    void exibirResultadoAposVideoParar(VideoView videoJogada, TextView txtResultado, TextView txtCombinacao, int ID_RESULTADO) {

        this.videoJogada = videoJogada;
        this.txtResultado = txtResultado;
        this.txtCombinacao = txtCombinacao;

        videoJogada.setOnCompletionListener(mp -> {
            videoJogada.pause();
            txtResultado.setVisibility(View.VISIBLE);
            txtCombinacao.setVisibility(View.VISIBLE);
        });

        if (ID_RESULTADO == ID_VITORIA) {
            imprimeVitoria(txtResultado);
        } else if (ID_RESULTADO == ID_DERROTA) {
            imprimeDerrota(txtResultado);
        } else if (ID_RESULTADO == ID_EMPATE) {
            imprimeEmpate(txtResultado);
        }
    }


    //Funcoes para fazera jogada de acordo com o botao clicado
    public void cliqueBtnPedra(View view) {

        //COLOCAR LOGICA DE SE O VIDEO ESTIVER TOCANDO OU SEJA, FOI INICIADO UMA JOGADA, SO PODE INICIAR OUTRA JOGADA DEPOIS DO VIDEO/JOGADA ATUAL PARAR

        txtJogada.setText("Você escolheu: \nPEDRA");
        String resultado = fazerJogada();

        //EMPATE
        if (resultado.equals("Pedra")) {

            //Definindo texto de combinacao da jogada
            txtCombinacao.setText("Pedra COM Pedra");

            //Definindo caminho do video de jogada a ser exibido
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pedra_pedra);
            videoJogada.setVideoURI(uri);

            //Exibir video e resultado
            playVideo(videoJogada);
            exibirResultadoAposVideoParar(videoJogada, txtResultado, txtCombinacao, ID_EMPATE);

        //VITORIA
        } else if (resultado.equals("Tesoura")) {

            //Definindo texto de combinacao da jogada
            txtCombinacao.setText("Pedra QUEBRA Tesoura");

            //Definindo caminho do video de jogada a ser exibido
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pedra_tesoura);
            videoJogada.setVideoURI(uri);

            //Exibir video e resultado
            playVideo(videoJogada);
            exibirResultadoAposVideoParar(videoJogada, txtResultado, txtCombinacao, ID_VITORIA);

        //DERROTA
        } else if (resultado.equals("Papel")) {

            //Definindo texto de combinacao da jogada
            txtCombinacao.setText("Pedra é ENGOLIDA por PAPEL");

            //Definindo caminho do video de jogada a ser exibido
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pedra_papel);
            videoJogada.setVideoURI(uri);

            //Exibir video e resultado
            playVideo(videoJogada);
            exibirResultadoAposVideoParar(videoJogada, txtResultado, txtCombinacao, ID_DERROTA);

        }
    }
    public void cliqueBtnPapel(View view) {

        txtJogada.setText("Você escolheu: \nPapel");
        String resultado = fazerJogada();

        //EMPATE
        if (resultado.equals("Papel")) {

            //Definindo texto de combinacao da jogada
            txtCombinacao.setText("Papel COM Papel");

            //Definindo caminho do video de jogada a ser exibido
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.papel_papel);
            videoJogada.setVideoURI(uri);

            //Exibir video e resultado
            playVideo(videoJogada);
            exibirResultadoAposVideoParar(videoJogada, txtResultado, txtCombinacao, ID_EMPATE);

        //VITORIA
        } else if (resultado.equals("Pedra")) {

            //Definindo texto de combinacao da jogada
            txtCombinacao.setText("Papel ENGOLE Pedra");

            //Definindo caminho do video de jogada a ser exibido
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.papel_pedra);
            videoJogada.setVideoURI(uri);

            //Exibir video e resultado
            playVideo(videoJogada);
            exibirResultadoAposVideoParar(videoJogada, txtResultado, txtCombinacao, ID_VITORIA);

        //DERROTA
        } else if (resultado.equals("Tesoura")) {

            //Definindo texto de combinacao da jogada
            txtCombinacao.setText("Papel é CORTADO por Tesoura");

            //Definindo caminho do video de jogada a ser exibido
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.papel_tesoura);
            videoJogada.setVideoURI(uri);

            //Exibir video e resultado
            playVideo(videoJogada);
            exibirResultadoAposVideoParar(videoJogada, txtResultado, txtCombinacao, ID_DERROTA);
        }
    }
    public void cliqueBtnTesoura(View view) {

        txtJogada.setText("Você escolheu: \nTesoura");
        String resultado = fazerJogada();

        //EMPATE
        if (resultado.equals("Tesoura")) {

            //Definindo texto de combinacao da jogada
            txtCombinacao.setText("Tesoura COM Tesoura");

            //Definindo caminho do video de jogada a ser exibido
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tesoura_tesoura);
            videoJogada.setVideoURI(uri);

            //Exibir video e resultado
            playVideo(videoJogada);
            exibirResultadoAposVideoParar(videoJogada, txtResultado, txtCombinacao, ID_EMPATE);

        //VITORIA
        } else if (resultado.equals("Papel")) {

            //Definindo texto de combinacao da jogada
            txtCombinacao.setText("Tesoura CORTA Papel");

            //Definindo caminho do video de jogada a ser exibido
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tesoura_papel);
            videoJogada.setVideoURI(uri);

            //Exibir video e resultado
            playVideo(videoJogada);
            exibirResultadoAposVideoParar(videoJogada, txtResultado, txtCombinacao, ID_VITORIA);

        //DERROTA
        } else if (resultado.equals("Pedra")) {

            //Definindo texto de combinacao da jogada
            txtCombinacao.setText("Tesoura é QUEBRADA por Pedra");

            //Definindo caminho do video de jogada a ser exibido
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tesoura_pedra);
            videoJogada.setVideoURI(uri);

            //Exibir video e resultado
            playVideo(videoJogada);
            exibirResultadoAposVideoParar(videoJogada, txtResultado, txtCombinacao, ID_DERROTA);
        }
    }

    //Funcao para realizar a jogada do computador e retornar o resultado
    public String fazerJogada() {

        txtCombinacao.setVisibility(View.INVISIBLE);
        txtResultado.setVisibility(View.INVISIBLE);

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
