package com.guilima.pedra_papel_tesoura;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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


    //VOLUME BASE DA MUSICA (Inicial tambem)
    private float volumeBaseMusica = 1.0f;


    //Variavel que vai receber o codigo do audio do lofi aleatorio
    private int mp_lofi_atual_ID = 0;
    private int mp_lofi_anterior_ID = 0; //serve pra nao tocar a mesma musica duas vezes seguidas


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



    private int pontuacaoMaxima; //guardar pontuacaoMaxima
    private int qtdeJogadas; //variavel que guarda qtde de jogadas para mostrar mensagem botao V
    private boolean tocouAudioStreak; // nao repetir o audio do win streak enquanto nao mudar o resultado que toca o audio
    private boolean easterEggJaRodou; //variavel pra nao deixar o video easter egg entrar em loop
    private boolean tocandoEasterEgg; //variavel pra saber quando o video estar tocando e assim baixar o volume a musica e sair de um loop


    private final int QTDE_VITORIAS_PARA_WIN_STREAK1 = 3; //qtde para tocar audio win streak 1 vez
    private final int QTDE_VITORIAS_PARA_WIN_STREAK2 = 6; //qtde para tocar audio win streak 2 vez
    private final int QTDE_VITORIAS_PARA_ZERAR = 10; //qtde para exibir video easter_egg


    private int contadorCliquesReset = 0; // variavel para receber a qtde de clicks no botao D para resetar a pontuacao maxima
    private final int QTDE_CLICKS_PARA_RESET = 3; //qtde de clicks necessarias para resetar a pontuacao maxima

    //TEXTOS
    private TextView txtResultado;
    private TextView txtCombinacao;
    private TextView txtJogada;
    private TextView txtPontuacaoMax;
    private TextView txtExibirRecorde;
    private TextView txtEmpates;
    private TextView txtVitorias;
    private TextView txtDerrotas;

    //IMAGENS
    private ImageView btnPedra;
    private ImageView btnPapel;
    private ImageView btnTesoura;
    private ImageView btnResetPontuacaoMax;

    //VIDEO
    private VideoView videoJogada;
    private FrameLayout videoContainer; //CONTAINER DO VIDEO

    //Audio da jogada
    MediaPlayer mpAudio;

    //Musica lofi loop
    MediaPlayer mpLofi;

    //Looper
    Handler handler = new Handler(Looper.getMainLooper());

    //Looper para fade out e fade in
    Handler handlerLofi = new Handler(Looper.getMainLooper());


    //RESETAR PONTUACAO MAXIMA
    Handler handlerReset = new Handler(Looper.getMainLooper());
    Runnable resetContador = () -> contadorCliquesReset = 0; //resetar pontuacao maxima


    //Milisegundos da duracao da musica e do fade out e fade entre faixas
    final int TEMPO_TOTAL = 100000;     // 1 minuto
    final int FADE_TEMPO = 6000;       // 6 segundos


    // PARA NUMERO ALEATORIO (E NAO TER QUE CRIAR TODA VEZ DENTRO DA FUNCAO)
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


        //SETANDO OS COMPOMENTES
        txtResultado = findViewById(R.id.txtResultado);
        txtCombinacao = findViewById(R.id.txtCombinacao);
        txtJogada = findViewById(R.id.txtJogada);
        txtPontuacaoMax = findViewById(R.id.txtPontuacaoMax);
        txtExibirRecorde = findViewById(R.id.txtExibirRecorde);

        txtEmpates = findViewById(R.id.qtdeEmpates);
        txtVitorias = findViewById(R.id.qtdeVitorias);
        txtDerrotas = findViewById(R.id.qtdeDerrotas);

        btnPedra = findViewById(R.id.imgPedra);
        btnPapel = findViewById(R.id.imgPapel);
        btnTesoura = findViewById(R.id.imgTesoura);
        btnResetPontuacaoMax = findViewById(R.id.letraD);

        videoJogada = findViewById(R.id.videoJogada);
        videoContainer = findViewById(R.id.videoContainer);

        //deixando o texto de resultado invisivel, pois so vai aparecer depois de fazer a jogada
        txtResultado.setVisibility(View.INVISIBLE);

        //Start na qtde dos resultados
        empates = 0;
        vitorias = 0;
        derrotas = 0;

        //Definindo logica para evitar loop do video de estaer egg e audio wimn streak
        tocouAudioStreak = false;
        easterEggJaRodou = false;
        tocandoEasterEgg = false;

        //variavel qtde jogadas
        qtdeJogadas = 0;

        //tocar musica logo quando o app eh aberto
        tocarLofiAleatorio();

        //carregando pontuacao maxima
        pontuacaoMaxima = carregarPontuacaoMaxima();

        //botao que reseta pontuacao maxima
        btnResetPontuacaoMax.setOnClickListener(v -> resetarPontuacaoMaxima());

    }

    //Funcao pra limpar mp ao sair da tela /activity (nesse caso app pq so tem uma activity -> main activity)
    @Override
    protected void onDestroy() {

        super.onDestroy();

        //se tiver um video setado ele para de tocar e limpa a memoria

        if (videoJogada != null) {
            videoJogada.stopPlayback();
        }

        //para a musica e os loops
        handler.removeCallbacksAndMessages(null);
        handlerLofi.removeCallbacksAndMessages(null);

        //define a musica como nula
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


    //Funcao para dar play no video e exibir o container com bordas arredondadas ao redor do video
    void playVideo(VideoView videoJogada) {

        //ecapsulamento
        this.videoJogada = videoJogada;

        //exibe container com bordas arredondadas ao redor do video
        videoContainer.setVisibility(View.VISIBLE);

        //reproduz o video
        videoJogada.start();
    }


    //Funcao para exibir o resultado da jogada apos o video parar de tocar
    void exibirResultadoAposVideoParar(VideoView videoJogada, TextView txtResultado, TextView txtCombinacao, int ID_RESULTADO) {

        //apenas usa encasulamente para acessar as views
        this.videoJogada = videoJogada;
        this.txtResultado = txtResultado;
        this.txtCombinacao = txtCombinacao;

        //entra aqui
        videoJogada.setOnCompletionListener(mp -> {

            //se o video estiver tocando (pela variavel que definimos), mas aqui ele ja parou
            if (tocandoEasterEgg) {

                //definimos que ele acabou
                tocandoEasterEgg = false;

                //definimos volume base inicial, volume normal
                volumeBaseMusica = 1.0f;

                //se tiver uma musica definida, ja setamos para o volume voltar ao normal
                if (mpLofi != null) {
                    mpLofi.setVolume(volumeBaseMusica, volumeBaseMusica);
                }

                //volta a tocar normalmente
                agendarTroca();

                //para o video para nao ocasionar um loop
                videoJogada.stopPlayback();

                //exibe textde selecionar nova jogada
                txtJogada.setText(getString(R.string.txt_field_explain));

                //sai da funcao
                return;
            }

            //exibe resultado
            txtResultado.setVisibility(View.VISIBLE);
            txtCombinacao.setVisibility(View.VISIBLE);

            //se for empate
            if (ID_RESULTADO == ID_EMPATE) {

                //exibe txt de empate
                imprimeEmpate(txtResultado);

                mpAudio = MediaPlayer.create(this, R.raw.audio_empate);
                mpAudio.setOnCompletionListener(MediaPlayer::release);
                mpAudio.start();

                //atualiza qtde de empates
                txtEmpates.setText(String.valueOf(empates));

            //se for vitoria
            } else if (ID_RESULTADO == ID_VITORIA) {

                //exibe txt de vitoria
                imprimeVitoria(txtResultado);

                //toca audio de vitoria
                mpAudio = MediaPlayer.create(this, R.raw.audio_vitoria);
                mpAudio.setOnCompletionListener(MediaPlayer::release);
                mpAudio.start();

                //atualiza a qtde de vitorias
                txtVitorias.setText(String.valueOf(vitorias));

            //se for derrota
            } else if (ID_RESULTADO == ID_DERROTA) {

                //exibe texto de derrota
                imprimeDerrota(txtResultado);

                //toca audio de derrota
                mpAudio = MediaPlayer.create(this, R.raw.audio_derrota);
                mpAudio.setOnCompletionListener(MediaPlayer::release);
                mpAudio.start();

                //atualiza qtde de derrotas
                txtDerrotas.setText(String.valueOf(derrotas));

            }

            //logica para exibir o easter egg
            if (vitorias == QTDE_VITORIAS_PARA_ZERAR) {

                //se o video nao estiver sido reproduzido ainda
                if (!easterEggJaRodou) {

                    //agora define que reproduziu para evitar loop
                    easterEggJaRodou = true;

                    //define que o video esta sendo reproduzindo
                    tocandoEasterEgg = true;

                    //e define o volume da musica lofi pra 0, muta o audio
                    volumeBaseMusica = 0f;

                    // PARA QUALQUER FADE do logi EM ANDAMENTO
                    handlerLofi.removeCallbacksAndMessages(null);

                    // se tiver uma musica definida, ele muta o audio da musica
                    if (mpLofi != null) {
                        mpLofi.setVolume(0f, 0f);
                    }

                    //define o video do easter egg pra reproduzido
                    videoJogada.setVideoURI(
                            Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.easter_egg_parabains)
                    );

                    //reproduz o video do easter egg
                    playVideo(videoJogada);

                    //sai do videoJogada.setOnCompletionListener(mp -> {
                    return;

                }

                /*vitorias = 0;
                txtVitorias.setText(String.valueOf(vitorias));

                empates = 0;
                txtEmpates.setText(String.valueOf(empates));

                derrotas = 0;
                txtDerrotas.setText(String.valueOf(derrotas));*/

            }

            //so vem aqui quando nao eh o video de easter egg

            //ai ele verifica se eh win streak e executa o audio
            winStreakSound(mpAudio);

            //para o video da jogada
            videoJogada.stopPlayback();

            //e exibe txt para selecionar nova jogada
            txtJogada.setText(getResources().getString(R.string.txt_field_explain));

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

                //logica pra verificar se a pontuacao atual eh maior que a pontuacaoMax
                //e assim definir uma nova pontuacaoMaxima

                if (vitorias > pontuacaoMaxima) {
                    pontuacaoMaxima = vitorias;
                    salvarPontuacaoMaxima(pontuacaoMaxima);
                }

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

                //logica pra verificar se a pontuacao atual eh maior que a pontuacaoMax
                //e assim definir uma nova pontuacaoMaxima
                if (vitorias > pontuacaoMaxima) {
                    pontuacaoMaxima = vitorias;
                    salvarPontuacaoMaxima(pontuacaoMaxima);
                }

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

        //logica que permite receber o click no botao somente apos o video ser reproduzido

        if (videoJogada.isPlaying() && btnTesoura.isPressed()) {

            return; //se o video estiver sendo reproduzido retorna sem fazer nada

        } else if (!videoJogada.isPlaying() && btnTesoura.isPressed()) { //se nao

            //define texto da jogada e faz a jogada
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

                //logica pra verificar se a pontuacao atual eh maior que a pontuacaoMax
                //e assim definir uma nova pontuacaoMaxima
                if (vitorias > pontuacaoMaxima) {
                    pontuacaoMaxima = vitorias;
                    salvarPontuacaoMaxima(pontuacaoMaxima);
                }

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

        //toda vez que fazer umjogada, ele da o suspense dele
        //deixando a combinacao e o resultado invisivel
        //para ser exibido somente depois de fazer a jogada
        txtCombinacao.setVisibility(View.INVISIBLE);
        txtResultado.setVisibility(View.INVISIBLE);

        //define numero aleatorio, entre 0 e 2
        Random random = new Random();
        int numero = random.nextInt(3);
        String resultadoSorteio = ""; //cria a variavel local para receber o resultado

        //define resultado
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

        //incrementa qtde de jogadas
        qtdeJogadas = qtdeJogadas + 1;

        //logica para exibir mensagem de exibir pontuacao maxima
        if (qtdeJogadas == 5 && pontuacaoMaxima >= 0) {
            txtExibirRecorde.setVisibility(View.VISIBLE);
        }
        else {
            txtExibirRecorde.setVisibility(View.INVISIBLE);
        }

        //retorna resultado
        return resultadoSorteio;
    }

    //funcao que reproduz o audio a cada win streak (que nao eh bem win streak)
    //executa a cada x vitorias (nao precisando serem vitorias seguidas)
    void winStreakSound(MediaPlayer mpAudio) {

        if (vitorias == QTDE_VITORIAS_PARA_WIN_STREAK1 || vitorias == QTDE_VITORIAS_PARA_WIN_STREAK2) {

            if (!tocouAudioStreak) {

                mpAudio = MediaPlayer.create(this, R.raw.audio_win_streak);
                mpAudio.setOnCompletionListener(MediaPlayer::release);
                mpAudio.start();
                tocouAudioStreak = true;

            } else if (tocouAudioStreak) {
                return;
            }
        } else {
            tocouAudioStreak = false;
        }
    }

    //funcao para executar o fadein
    void fadeIn() {

        final int interval = 100;
        final int steps = FADE_TEMPO / interval;
        final float volumeFinal = volumeBaseMusica;
        final float delta = volumeFinal / steps;

        final float[] volume = {0f};

        handlerLofi.post(new Runnable() {
            @Override
            public void run() {
                volume[0] += delta;
                if (volume[0] < volumeFinal && mpLofi != null) {
                    mpLofi.setVolume(volume[0], volume[0]);
                    handlerLofi.postDelayed(this, interval);
                }
            }
        });

    }

    //autoexplicativo
    void fadeOutERodarProxima() {

        //feito com ajuda do assistente de IA - CHAT gpt, entao nao sei explicar
        //muito bem a logica dese trecho, apenas que o volume vai incrementando ate chegar
        //no volume final

        final int interval = 100;
        final int steps = FADE_TEMPO / interval;
        final float delta = 1f / steps;

        final float[] volume = {volumeBaseMusica};

        handlerLofi.post(new Runnable() {
            @Override
            public void run() {
                volume[0] -= delta;
                if (volume[0] > 0f && mpLofi != null) {
                    mpLofi.setVolume(volume[0], volume[0]);
                    handlerLofi.postDelayed(this, interval);
                } else {
                    tocarLofiAleatorio();
                }
            }
        });
    }


    //funcao que agenda troca para a proxima musica
    void agendarTroca() {

        //assim que a musica acaba, ele chama a funcao faz o fade out
        //e troca pra proxima musica
        handlerLofi.postDelayed(
                () -> fadeOutERodarProxima(),
                TEMPO_TOTAL - FADE_TEMPO
        );
    }


    //funcao que toca o Lofi
    void tocarLofiAleatorio() {

        //recebe o codigo do audio anterior que tocou para fazer nao repetir
        mp_lofi_anterior_ID = mp_lofi_atual_ID;

        //enquanto o codigo do audio atual for igual ao anterior, ele roda um novo numero aleatorio
        do {
            mp_lofi_atual_ID = aleatorio.nextInt(7) + 1;
        }
        while (mp_lofi_atual_ID == mp_lofi_anterior_ID);

        //se tiver audio setado ele para e prepara para definir um novo audio
        if (mpLofi != null) {
            mpLofi.stop();
            mpLofi.release();
            mpLofi = null;
        }

        //aqui ele define qual audio ira ser reproduzido
        switch (mp_lofi_atual_ID) {
            case ID_LOFI_1: mpLofi = MediaPlayer.create(this, R.raw.lofi_01); break;
            case ID_LOFI_2: mpLofi = MediaPlayer.create(this, R.raw.lofi_02); break;
            case ID_LOFI_3: mpLofi = MediaPlayer.create(this, R.raw.lofi_03); break;
            case ID_LOFI_4: mpLofi = MediaPlayer.create(this, R.raw.lofi_04); break;
            case ID_LOFI_5: mpLofi = MediaPlayer.create(this, R.raw.lofi_05); break;
            case ID_LOFI_6: mpLofi = MediaPlayer.create(this, R.raw.lofi_06); break;
            case ID_LOFI_7: mpLofi = MediaPlayer.create(this, R.raw.lofi_07); break;
        }

        //se o audio estiver setado, ele define o volume e dar start no audio
        if (mpLofi != null) {

            mpLofi.setVolume(0f, 0f);
            mpLofi.start();

            fadeIn(); //funcao que da fade in no proxima audio
            agendarTroca(); //agenda a troca pro proximo audio
        }
    }

    //salva pontuacao maxima sempre que houver uma nova
    void salvarPontuacaoMaxima(int pontuacao) {
        SharedPreferences prefs = getSharedPreferences("ranking", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("pontuacao_maxima", pontuacao);
        editor.apply(); // assíncrono e seguro
    }

    //carrega pontuacao maxima do ranking
    int carregarPontuacaoMaxima() {
        SharedPreferences prefs = getSharedPreferences("ranking", MODE_PRIVATE);
        return prefs.getInt("pontuacao_maxima", 0);
    }


    //exibe um texto com a pontuacao maxima com o fundo roxo (como se fosse um popup)
    public void exibirPontuacaoMaxima(View view) {

        //exibe a mensagem abaixo
        txtPontuacaoMax.setText("Seu recorde de vitória: \n" + String.valueOf(pontuacaoMaxima));

        //logica para somente exibir o txt se ele estiver invisivel
        //e "fechar" o pop up se estiver visivel
        if (txtPontuacaoMax.getVisibility() == View.INVISIBLE) {
            txtPontuacaoMax.setVisibility(View.VISIBLE);
            return;
        }
        else {
            txtPontuacaoMax.setVisibility(View.INVISIBLE);
        }

    }


    //FUNCAO para resetar a pontuacao maxima
    void resetarPontuacaoMaxima() {

        //incrementa a qtde de click toda vez que houve click no botao D
        contadorCliquesReset++;

        // reinicia a janela de tempo a cada clique para ser 3 clicks em 2segundos e nao captar toques em diferentes tempos
        handlerReset.removeCallbacks(resetContador);
        handlerReset.postDelayed(resetContador, 2000); // 2 segundos

        //se os clicks no botao forem a qtde de clicks necessarios pra resetar entao reseta
        if (contadorCliquesReset == QTDE_CLICKS_PARA_RESET) {

            //define a pontuacao maxima pra 0
            SharedPreferences prefs = getSharedPreferences("ranking", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("pontuacao_maxima", 0);
            editor.apply();

            pontuacaoMaxima = 0;

            //reseta clicks
            contadorCliquesReset = 0;

            //exibe mensagem para informar que a pontuacao maxima foi redefinida
            Toast.makeText(
                    MainActivity.this,
                    "Pontuação máxima redefinida",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}
