package com.guilima.pedra_papel_tesoura;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {



    //Serve para passar como parametro para a funcao de exibirResultado e assim saber qual o resultado da jogada
    private final int ID_EMPATE = 0;
    private final int ID_VITORIA = 1;
    private final int ID_DERROTA = 2;



    //Variavel que vai receber o codigo do audio do lofi aleatorio
    private int mp_lofi_atual_ID = 0;
    private int mp_lofi_anterior_ID = 0;


    //Somente pra nomenclatura e clareza de codigo
    private static final int ID_LOFI_1 = 1;
    private static final int ID_LOFI_2 = 2;
    private static final int ID_LOFI_3 = 3;
    private static final int ID_LOFI_4 = 4;
    private static final int ID_LOFI_5 = 5;
    private static final int ID_LOFI_6 = 6;
    private static final int ID_LOFI_7 = 7;




    //Variavel de empates
    private int empates;

    //Variavel de vitorias
    private int vitorias;

    //Variavel de derrotas
    private int derrotas;
    private boolean tocouAudioStreak;
    private final int QTDE_VITORIAS_PARA_WIN_STREAK1 = 3;
    private final int QTDE_VITORIAS_PARA_WIN_STREAK2 = 6;
    private final int QTDE_VITORIAS_PARA_ZERAR = 10;



    private TextView txtResultado;
    private TextView txtCombinacao;
    private TextView txtJogada;

    private TextView txtEmpates;
    private TextView txtVitorias;
    private TextView txtDerrotas;

    private ImageView btnPedra;
    private ImageView btnPapel;
    private ImageView btnTesoura;

    private VideoView videoJogada;
    private FrameLayout videoContainer;

    //Audio da jogada
    MediaPlayer mpAudio;

    //Musia lofi loop
    MediaPlayer mpLofi;

    //Looper
    Handler handler = new Handler(Looper.getMainLooper());


    //Milisegundos da duracao da musica e do fade out e fade entre faixas
    final int TEMPO_TOTAL = 15000;     // 1 minuto
    final int FADE_TEMPO = 5000;       // 4 segundos

    Random aleatorio = new Random();

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

        txtEmpates = findViewById(R.id.qtdeEmpates);
        txtVitorias = findViewById(R.id.qtdeVitorias);
        txtDerrotas = findViewById(R.id.qtdeDerrotas);

        btnPedra = findViewById(R.id.imgPedra);
        btnPapel = findViewById(R.id.imgPapel);
        btnTesoura = findViewById(R.id.imgTesoura);
        videoJogada = findViewById(R.id.videoJogada);
        videoContainer = findViewById(R.id.videoContainer);

        //deixando o texto de resultado invisivel, pois so vai aparecer depois de fazer a jogada
        txtResultado.setVisibility(View.INVISIBLE);

        //Start na qtde dos resultados
        empates = 0;
        vitorias = 0;
        derrotas = 0;

        tocouAudioStreak = false;

        tocarLofiAleatorio();
    }

    //Funcao pra limpar mp ao sair da tela (nesse caso app pq so tem ua main activity)
    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (videoJogada != null) {
            videoJogada.stopPlayback();
        }

        handler.removeCallbacksAndMessages(null);

        if (mpLofi != null) {
            mpLofi.stop();
            mpLofi.release();
            mpLofi = null;
        }
    }

    //Funcoes para modificar o txtResultado de acordo com o resultado da jogada
    void imprimeVitoria(TextView view) {

        String txtVitoria = getResources().getString(R.string.txt_VITORIA);
        view.setText(txtVitoria);
        view.setTextColor(ContextCompat.getColor(this, R.color.green_victory));

    }

    void imprimeDerrota(TextView view) {

        String txtDerrota = getResources().getString(R.string.txt_DERROTA);
        view.setText(txtDerrota);
        view.setTextColor(ContextCompat.getColor(this, R.color.red_defeat));

    }

    void imprimeEmpate(TextView view) {

        String txtEmpate = getResources().getString(R.string.txt_EMPATE);
        view.setText(txtEmpate);
        view.setTextColor(ContextCompat.getColor(this, R.color.yellow_draw));

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

            txtResultado.setVisibility(View.VISIBLE);
            txtCombinacao.setVisibility(View.VISIBLE);

            if (ID_RESULTADO == ID_EMPATE) {

                imprimeEmpate(txtResultado);
                mpAudio = MediaPlayer.create(this, R.raw.audio_empate);
                mpAudio.start();

                txtEmpates.setText(String.valueOf(empates));

            } else if (ID_RESULTADO == ID_VITORIA) {

                imprimeVitoria(txtResultado);
                mpAudio = MediaPlayer.create(this, R.raw.audio_vitoria);
                mpAudio.start();

                txtVitorias.setText(String.valueOf(vitorias));

            } else if (ID_RESULTADO == ID_DERROTA) {

                imprimeDerrota(txtResultado);
                mpAudio = MediaPlayer.create(this, R.raw.audio_derrota);
                mpAudio.start();

                txtDerrotas.setText(String.valueOf(derrotas));

            }

            if (vitorias == QTDE_VITORIAS_PARA_ZERAR) {

                videoJogada.setVideoURI(
                        Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.easter_egg_parabains)
                );
                playVideo(videoJogada);

                vitorias = 0;
                txtVitorias.setText(String.valueOf(vitorias));

                empates = 0;
                txtEmpates.setText(String.valueOf(empates));

                derrotas = 0;
                txtDerrotas.setText(String.valueOf(derrotas));

                return;
            }

            winStreakSound(mpAudio);
            videoJogada.stopPlayback();

        });
    }

    //Funcoes para fazera jogada de acordo com o botao clicado
    public void cliqueBtnPedra(View view) {

        //Se o video estiver sendo executado e o botao for pressionado, nao faz nada
        if (videoJogada.isPlaying() && btnPedra.isPressed()) {
            return;
        } else if (!videoJogada.isPlaying() && btnPedra.isPressed()) {

            txtJogada.setText("Você escolheu: \nPEDRA");
            String resultado = fazerJogada();

            //EMPATE
            if (resultado.equals("Pedra")) {

                //Definindo texto de combinacao da jogada
                txtCombinacao.setText("Pedra COM Pedra");
                empates = empates + 1;

                //Definindo caminho do video de jogada a ser exibido
                Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video_pedra_pedra);
                videoJogada.setVideoURI(uri);

                //Exibir video e resultado
                playVideo(videoJogada);
                exibirResultadoAposVideoParar(videoJogada, txtResultado, txtCombinacao, ID_EMPATE);

                //VITORIA
            } else if (resultado.equals("Tesoura")) {

                //Definindo texto de combinacao da jogada
                txtCombinacao.setText("Pedra QUEBRA Tesoura");
                vitorias = vitorias + 1;

                //Definindo caminho do video de jogada a ser exibido
                Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video_pedra_tesoura);
                videoJogada.setVideoURI(uri);

                //Exibir video e resultado
                playVideo(videoJogada);
                exibirResultadoAposVideoParar(videoJogada, txtResultado, txtCombinacao, ID_VITORIA);

                //DERROTA
            } else if (resultado.equals("Papel")) {

                //Definindo texto de combinacao da jogada
                txtCombinacao.setText("Pedra é ENGOLIDA por PAPEL");
                derrotas = derrotas + 1;

                //Definindo caminho do video de jogada a ser exibido
                Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video_pedra_papel);
                videoJogada.setVideoURI(uri);

                //Exibir video e resultado
                playVideo(videoJogada);
                exibirResultadoAposVideoParar(videoJogada, txtResultado, txtCombinacao, ID_DERROTA);
            }
        }
    }

    public void cliqueBtnPapel(View view) {

        if (videoJogada.isPlaying() && btnPapel.isPressed()) {
            return;
        } else if (!videoJogada.isPlaying() && btnPapel.isPressed()) {

            txtJogada.setText("Você escolheu: \nPapel");
            String resultado = fazerJogada();

            //EMPATE
            if (resultado.equals("Papel")) {

                //Definindo texto de combinacao da jogada
                txtCombinacao.setText("Papel COM Papel");
                empates = empates + 1;

                //Definindo caminho do video de jogada a ser exibido
                Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video_papel_papel);
                videoJogada.setVideoURI(uri);

                //Exibir video e resultado
                playVideo(videoJogada);
                exibirResultadoAposVideoParar(videoJogada, txtResultado, txtCombinacao, ID_EMPATE);

                //VITORIA
            } else if (resultado.equals("Pedra")) {

                //Definindo texto de combinacao da jogada
                txtCombinacao.setText("Papel ENGOLE Pedra");
                vitorias = vitorias + 1;

                //Definindo caminho do video de jogada a ser exibido
                Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video_papel_pedra);
                videoJogada.setVideoURI(uri);

                //Exibir video e resultado
                playVideo(videoJogada);
                exibirResultadoAposVideoParar(videoJogada, txtResultado, txtCombinacao, ID_VITORIA);

                //DERROTA
            } else if (resultado.equals("Tesoura")) {

                //Definindo texto de combinacao da jogada
                txtCombinacao.setText("Papel é CORTADO por Tesoura");
                derrotas = derrotas + 1;

                //Definindo caminho do video de jogada a ser exibido
                Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video_papel_tesoura);
                videoJogada.setVideoURI(uri);

                //Exibir video e resultado
                playVideo(videoJogada);
                exibirResultadoAposVideoParar(videoJogada, txtResultado, txtCombinacao, ID_DERROTA);
            }
        }
    }

    public void cliqueBtnTesoura(View view) {

        if (videoJogada.isPlaying() && btnTesoura.isPressed()) {
            return;
        } else if (!videoJogada.isPlaying() && btnTesoura.isPressed()) {

            txtJogada.setText("Você escolheu: \nTesoura");
            String resultado = fazerJogada();

            //EMPATE
            if (resultado.equals("Tesoura")) {

                //Definindo texto de combinacao da jogada
                txtCombinacao.setText("Tesoura COM Tesoura");
                empates = empates + 1;

                //Definindo caminho do video de jogada a ser exibido
                Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video_tesoura_tesoura);
                videoJogada.setVideoURI(uri);

                //Exibir video e resultado
                playVideo(videoJogada);
                exibirResultadoAposVideoParar(videoJogada, txtResultado, txtCombinacao, ID_EMPATE);

                //VITORIA
            } else if (resultado.equals("Papel")) {

                //Definindo texto de combinacao da jogada
                txtCombinacao.setText("Tesoura CORTA Papel");
                vitorias = vitorias + 1;

                //Definindo caminho do video de jogada a ser exibido
                Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video_tesoura_papel);
                videoJogada.setVideoURI(uri);

                //Exibir video e resultado
                playVideo(videoJogada);
                exibirResultadoAposVideoParar(videoJogada, txtResultado, txtCombinacao, ID_VITORIA);

                //DERROTA
            } else if (resultado.equals("Pedra")) {

                //Definindo texto de combinacao da jogada
                txtCombinacao.setText("Tesoura é QUEBRADA por Pedra");
                derrotas = derrotas + 1;

                //Definindo caminho do video de jogada a ser exibido
                Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video_tesoura_pedra);
                videoJogada.setVideoURI(uri);

                //Exibir video e resultado
                playVideo(videoJogada);
                exibirResultadoAposVideoParar(videoJogada, txtResultado, txtCombinacao, ID_DERROTA);
            }
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

    void winStreakSound(MediaPlayer mpAudio) {

        if (vitorias == QTDE_VITORIAS_PARA_WIN_STREAK1 || vitorias == QTDE_VITORIAS_PARA_WIN_STREAK2) {

            if (tocouAudioStreak == false) {

                mpAudio = MediaPlayer.create(this, R.raw.audio_win_streak);
                mpAudio.start();
                tocouAudioStreak = true;

            } else if (tocouAudioStreak == true) {
                return;
            }
        } else {
            tocouAudioStreak = false;
        }
    }

    void fadeIn() {

        final int interval = 100;
        final int steps = FADE_TEMPO / interval;
        final float delta = 1f / steps;

        final float[] volume = {0f};

        handler.post(new Runnable() {
            @Override
            public void run() {
                volume[0] += delta;
                if (volume[0] < 1f && mpLofi != null) {
                    mpLofi.setVolume(volume[0], volume[0]);
                    handler.postDelayed(this, interval);
                }
            }
        });
    }

    void fadeOutERodarProxima() {

        final int interval = 100;
        final int steps = FADE_TEMPO / interval;
        final float delta = 1f / steps;

        final float[] volume = {1f};

        handler.post(new Runnable() {
            @Override
            public void run() {
                volume[0] -= delta;
                if (volume[0] > 0f && mpLofi != null) {
                    mpLofi.setVolume(volume[0], volume[0]);
                    handler.postDelayed(this, interval);
                } else {
                    tocarLofiAleatorio();
                }
            }
        });
    }

    void agendarTroca() {
        handler.postDelayed(() -> fadeOutERodarProxima(),
                TEMPO_TOTAL - FADE_TEMPO);
    }

    void tocarLofiAleatorio() {

        mp_lofi_anterior_ID = mp_lofi_atual_ID;

        do {
            mp_lofi_atual_ID = aleatorio.nextInt(7) + 1;
        }
        while (mp_lofi_atual_ID == mp_lofi_anterior_ID);


        if (mpLofi != null) {
            mpLofi.stop();
            mpLofi.release();
            mpLofi = null;
        }

        switch (mp_lofi_atual_ID) {
            case ID_LOFI_1: mpLofi = MediaPlayer.create(this, R.raw.lofi_01); break;
            case ID_LOFI_2: mpLofi = MediaPlayer.create(this, R.raw.lofi_02); break;
            case ID_LOFI_3: mpLofi = MediaPlayer.create(this, R.raw.lofi_03); break;
            case ID_LOFI_4: mpLofi = MediaPlayer.create(this, R.raw.lofi_04); break;
            case ID_LOFI_5: mpLofi = MediaPlayer.create(this, R.raw.lofi_05); break;
            case ID_LOFI_6: mpLofi = MediaPlayer.create(this, R.raw.lofi_06); break;
            case ID_LOFI_7: mpLofi = MediaPlayer.create(this, R.raw.lofi_07); break;
        }

        if (mpLofi != null) {
            mpLofi.setVolume(0f, 0f);
            mpLofi.start();
            fadeIn();
            agendarTroca();
        }
    }
}




