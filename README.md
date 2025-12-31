# 🪨📄✂️ Pedra Papel Tesoura (Android)

Um **jogo casual infinito de Pedra Papel Tesoura**, desenvolvido em **Android com Java**, embalado com **lofi em loop** e focado em estudo prático da plataforma Android.

---

## 🎮 Sobre o jogo

Este projeto é um **Player vs CPU**, onde o jogador pode jogar indefinidamente, sem telas de “game over”.  
O desafio não é vencer o jogo, e sim **superar sua maior pontuação**.

### Principais características:
- ♾️ **Jogo infinito** (sem limite de partidas)
- 🧠 **Player vs CPU**
- 🏆 **Sistema de pontuação máxima (high score)**
- 🎧 **Trilha lofi em loop**, trocando de faixa automaticamente a cada 1 minuto
- 🎥 **Vídeos animados para cada jogada**
- 🥚 **Easter egg desbloqueado após um número específico de vitórias**

---

## 🧠 Objetivo do projeto

Este app foi criado com dois propósitos principais:

1. **Projeto de estudo**
   - Explorar recursos nativos do Android
   - Trabalhar com mídia (áudio e vídeo)
   - Entender melhor o ciclo de vida das Activities
   - Praticar persistência simples de dados

2. **Jogo casual**
   - Experiência leve, sem pressão
   - Interface simples
   - Ideal para partidas rápidas (ou longas demais…)

---

## 🛠️ Tecnologias e recursos utilizados

- **Linguagem:** Java
- **Plataforma:** Android
- **API mínima:** 24 (Android 7.0)
- **Target SDK:** 36

### Recursos Android explorados:
- `MediaPlayer`  
  → reprodução de música lofi em loop  
- `VideoView`  
  → animações em vídeo para cada jogada  
- **Ciclo de vida da Activity**
  - `onCreate`
  - `onDestroy`
- **Persistência de dados**
  - `SharedPreferences` para salvar a pontuação máxima
- **Gerenciamento de recursos**
  - `raw` (áudios e vídeos)
  - `drawable` (imagens)

Toda a lógica principal do jogo está concentrada na **MainActivity**, mantendo o projeto simples e direto, como convém a um jogo casual.

---

## 📱 Compatibilidade

- Funciona em dispositivos Android a partir da API 24
- Testado em smartphones
- Interface pensada para uso simples e direto

---

## 🚧 Status do projeto

🟢 **Funcional**  
🛠️ **Aberto a melhorias**

Possíveis evoluções planejadas:
- Melhor organização da arquitetura
- Novos efeitos visuais
- Expansão do sistema de áudio
- Ajustes de UX
- Publicação futura na **Google Play Store** (como projeto de estudo)

---

## 📦 Como rodar o projeto

1. Clone este repositório:
   ```bash
   git clone https://github.com/seu-usuario/pedra-papel-tesoura-android.git

2. Abra no Android Studio

3. Aguarde o Gradle sincronizar

4. Execute em um emulador ou dispositivo físico

