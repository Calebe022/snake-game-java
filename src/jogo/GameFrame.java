package jogo;

import javax.swing.*;

public class GameFrame extends JFrame {
    private GamePanel gamePanel;

    GameFrame() {
        this.setTitle("Jogo da Cobrinha");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        JButton startButton = new JButton("Iniciar Jogo");
        startButton.setFocusable(false);
        startButton.addActionListener(e -> iniciarJogo());

        JPanel startPanel = new JPanel();
        startPanel.add(startButton);

        this.add(startPanel);
        this.pack();
        this.setVisible(true);
    }

    public void iniciarJogo() {
        this.getContentPane().removeAll();
        gamePanel = new GamePanel();
        this.add(gamePanel);
        this.pack();
        this.revalidate();
        this.repaint();
        gamePanel.requestFocus();
    }
}
