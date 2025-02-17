package jogo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int LARGURA_TELA = 600;
    static final int ALTURA_TELA = 600;
    static final int TAMANHO_BLOCO = 25;
    static final int UNIDADES = (LARGURA_TELA * ALTURA_TELA) / (TAMANHO_BLOCO * TAMANHO_BLOCO);
    static final int VELOCIDADE = 100;
    final int[] x = new int[UNIDADES];
    final int[] y = new int[UNIDADES];
    int partesCobra = 5;
    int pontos;
    int comidaX;
    int comidaY;
    char direcao = 'P';
    boolean rodando = false;
    Timer timer;
    Random random;
    JButton reiniciarButton;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(LARGURA_TELA, ALTURA_TELA));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new ControlesTeclado());
        iniciarJogo();
    }

    public void iniciarJogo() {
        gerarComida();
        rodando = false;
        direcao = 'P';
        pontos = 0;
        partesCobra = 5;
        x[0] = 0;
        y[0] = 0;
        timer = new Timer(VELOCIDADE, this);
        timer.start();
        requestFocus();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        desenhar(g);
    }

    public void desenhar(Graphics g) {
        if (rodando || direcao == 'P') {
            g.setColor(Color.RED);
            g.fillOval(comidaX, comidaY, TAMANHO_BLOCO, TAMANHO_BLOCO);

            for (int i = 0; i < partesCobra; i++) {
                g.setColor(i == 0 ? Color.GREEN : new Color(45, 180, 0));
                g.fillRect(x[i], y[i], TAMANHO_BLOCO, TAMANHO_BLOCO);
            }

            g.setColor(Color.WHITE);
            g.setFont(new Font("Ink Free", Font.BOLD, 30));
            g.drawString("Pontos: " + pontos, 10, 30);
        } else {
            gameOver(g);
        }
    }

    public void gerarComida() {
        comidaX = random.nextInt(LARGURA_TELA / TAMANHO_BLOCO) * TAMANHO_BLOCO;
        comidaY = random.nextInt(ALTURA_TELA / TAMANHO_BLOCO) * TAMANHO_BLOCO;
    }

    public void mover() {
        if (direcao == 'P') return;

        for (int i = partesCobra; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direcao) {
            case 'C': y[0] -= TAMANHO_BLOCO; break;
            case 'B': y[0] += TAMANHO_BLOCO; break;
            case 'E': x[0] -= TAMANHO_BLOCO; break;
            case 'D': x[0] += TAMANHO_BLOCO; break;
        }
    }

    public void checarComida() {
        if (x[0] == comidaX && y[0] == comidaY) {
            partesCobra++;
            pontos++;
            gerarComida();
        }
    }

    public void checarColisao() {
        for (int i = partesCobra; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                rodando = false;
            }
        }

        if (x[0] < 0 || x[0] >= LARGURA_TELA || y[0] < 0 || y[0] >= ALTURA_TELA) {
            rodando = false;
        }

        if (!rodando) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 50));
        g.drawString("Game Over", 200, 250);

        criarBotaoReiniciar();
    }

    private void criarBotaoReiniciar() {
        if (reiniciarButton == null) {
            reiniciarButton = new JButton("Reiniciar");
            reiniciarButton.setBounds(250, 350, 100, 40);
            reiniciarButton.setFocusable(false);
            reiniciarButton.addActionListener(e -> reiniciarJogo());

            this.setLayout(null);
            this.add(reiniciarButton);
            this.revalidate();
            this.repaint();
        }
    }

    public void reiniciarJogo() {
        this.remove(reiniciarButton);
        reiniciarButton = null;

        // Gerar posição aleatória para a cabeça da cobra
        int startX = random.nextInt(LARGURA_TELA / TAMANHO_BLOCO) * TAMANHO_BLOCO;
        int startY = random.nextInt(ALTURA_TELA / TAMANHO_BLOCO) * TAMANHO_BLOCO;

        // Reposicionar toda a cobra no mesmo eixo da cabeça
        for (int i = 0; i < partesCobra; i++) {
            x[i] = startX - (i * TAMANHO_BLOCO);
            y[i] = startY;
        }

        direcao = 'P';
        rodando = false;
        pontos = 0;
        partesCobra = 5;

        gerarComida();
        timer.start();
        this.revalidate();
        this.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (rodando) {
            mover();
            checarComida();
            checarColisao();
        }
        repaint();
    }

    public class ControlesTeclado extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (direcao == 'P') rodando = true;

            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT: if (direcao != 'D') direcao = 'E'; break;
                case KeyEvent.VK_RIGHT: if (direcao != 'E') direcao = 'D'; break;
                case KeyEvent.VK_UP: if (direcao != 'B') direcao = 'C'; break;
                case KeyEvent.VK_DOWN: if (direcao != 'C') direcao = 'B'; break;
            }
        }
    }
}
