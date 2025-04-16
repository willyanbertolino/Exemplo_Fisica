import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Domina_Bola implements ActionListener {

    // Variáveis globais
    JFrame frame;
    JButton playBtn, resetBtn;
    JTextField initSpeedInput, angleInput, distanceInput,
            finalDistanceOutput, timeOutput, speedOutput;
    Double initSpeed, angle, distance;
    String playerDistanceDescription, playerTimeDescription,
            playerSpeedDescription;

    // Construtor
    Domina_Bola() {

        // Cria a caixa da calculadora.
        frame = new JFrame("Calcula Velocidade Média do Jogador");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(430, 400);

        frame.setLayout(null);

        // Cria os títulos e input para as variáveis
        // Velocidade inicial
        JLabel initSpeedText = createLabel(
                "Velocidade inicial (m/s):", 40, 25, 160, 30);
        initSpeedInput = createTextField(
                "", 190, 25, 70, 30, true, true);

        // Angulo
        JLabel angleText = createLabel("Angulo (graus):", 40, 60, 160, 30);
        angleInput = createTextField("", 190, 60, 70, 30, true, true);

        // Distância
        JLabel distanceText = createLabel("Distância (m):", 40, 95, 160, 30);
        distanceInput = createTextField("", 190, 95, 70, 30, true, true);

        // Botão para calcular
        playBtn = new JButton("Calcular");
        playBtn.setBounds(290, 60, 100, 30);
        playBtn.addActionListener(this);
        playBtn.setFocusable(false);

        // Cria as variáveis de saída
        // Distância que o jogador precisa correr, tempo de corrida e velocidade média
        playerDistanceDescription = "Distância a percorrer (m): ";
        finalDistanceOutput = createTextField(playerDistanceDescription, 40, 150, 350, 30, false, false);

        playerTimeDescription = "Tempo de corrida (s): ";
        timeOutput = createTextField(playerTimeDescription, 40, 190, 350, 30, false, false);

        playerSpeedDescription = "Velocidade média do jogador (m/s): ";
        speedOutput = createTextField(playerSpeedDescription, 40, 230, 350, 30, false, false);

        // Botão para resetar
        resetBtn = new JButton("Reset");
        resetBtn.setBounds(190, 290, 80, 30);
        resetBtn.addActionListener(this);
        resetBtn.setFocusable(false);

        // Adicionar elementos ao frame para mostrar na tela
        frame.add(initSpeedText);
        frame.add(angleText);
        frame.add(initSpeedInput);
        frame.add(angleInput);
        frame.add(distanceText);
        frame.add(distanceInput);
        frame.add(finalDistanceOutput);
        frame.add(timeOutput);
        frame.add(speedOutput);
        frame.add(playBtn);
        frame.add(resetBtn);
        frame.setVisible(true);

    }

    // Função Principal que roda o programa
    public static void main(String[] args) {
        new Domina_Bola();
    }

    // Executa a função do botão "Calcular"
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playBtn) {

            try {
                // Transforma valores do tipo string para double
                initSpeed = Double.parseDouble(initSpeedInput.getText());
                angle = Double.parseDouble(angleInput.getText());
                distance = Double.parseDouble(distanceInput.getText());

                // **** Restrições das variáveis ****
                if (initSpeed < 0) {
                    // Velocidade inicial da bola não pode ser negativa
                    errorMsg("A velocidade inicial deve ser um número positivo.", "Erro de entrada");
                    return;
                }
                if (initSpeed > 45) {
                    // Velocidade inicial a bola não pode ser maior que 45m/s pois já é quase um
                    // recorde.
                    errorMsg("A velocidade inicial deve ser menor que 45 m/s (162 Km/h).", "Erro de entrada");
                    return;
                }

                // O angulo não pode ser negativo nem maior que 180º pois isso indicaria uma
                // direção para dentro da grama!
                if (angle < 0 || angle > 180) {
                    errorMsg("O angulo deve ser maior que 0º e menor que 180º.", "Erro de Entrada");
                    return;
                }

                // As dimensões do campo de futebol são aprox. 105m x 68m e a diagonal mede
                // 125m. Essa a maior distância dentro do campo.
                if (Math.abs(distance) > 125) {
                    errorMsg("A maior distância em módulo que os jogadores podem ter é 125m.", "Erro de Entrada");
                    return;
                }

                // Calcula o tempo que o jogador precisa para chagar até a bola
                Double time = calculateTimeToReachTheBall(initSpeed, angle);
                String playerTime = createVetorString(playerTimeDescription, time);
                timeOutput.setText(playerTime);

                // Calcula a distancia que o jogador deve correr para alcançar a bola antes de
                // tocar no chão
                Double playerDistance = calculatePlayerDistanceOfTheBall(initSpeed, angle, time);
                String playerDistanceString = createVetorString(playerDistanceDescription, playerDistance);
                finalDistanceOutput.setText(playerDistanceString);

                // Calcula a velocidade média do jogador
                Double speed = playerDistance / time;
                String speedText = createVetorString(playerSpeedDescription, speed);
                speedOutput.setText(speedText);

                // Limite de velocidade de um jogador em módulo.
                if (Math.abs(speed) > 11) {
                    errorMsg("Um jogador pode correr até 11m/s (~40Km/h).", "Impossível!");
                }

            } catch (NumberFormatException ex) {
                // Se o valor digitado não for um número, mostra uma mensagem de erro.
                errorMsg("Por favor, digite um número válido.", "Erro de entrada");
            }
        }

        // Executa a função do botão "Reset"
        if (e.getSource() == resetBtn) {

            // Limpar as variáveis
            initSpeed = null;
            angle = null;
            distance = null;

            // Limpar as caixas de texto
            initSpeedInput.setText("");
            angleInput.setText("");
            distanceInput.setText("");

            timeOutput.setText("");
            finalDistanceOutput.setText("");
            speedOutput.setText("");
        }
    }

    // Método: Cria as caixas de input
    private JTextField createTextField(String label, int x, int y, int w, int h, boolean edit, boolean focus) {
        JTextField textField = new JTextField(label);
        textField.setBounds(x, y, w, h);
        textField.setEditable(edit);
        textField.setFocusable(focus);

        return textField;
    }

    // Método: Cria as Labels(titulos)
    private JLabel createLabel(String label, int x, int y, int w, int h) {
        JLabel labelText = new JLabel(label);
        labelText.setBounds(x, y, w, h);

        return labelText;
    }

    // Método: Cria a frase de resposta a partir de uma descrição e um número
    private String createVetorString(String description, Double x) {
        String string = description + String.format("%.1f", x);
        return string;
    }

    // Método: Calcula o tempo que a bola demora para chegar ao solo, que é o mesmo
    // tempo que o jogador tem para chegar até a bola antes de tocar o solo.
    private Double calculateTimeToReachTheBall(Double v0, Double teta) {
        double time = 2 * v0 * Math.sin(Math.toRadians(teta)) / 9.8;
        return time;
    }

    // Método: Calcula a distancia que o jogador deve correr para alcançar a bola.
    private Double calculatePlayerDistanceOfTheBall(Double v0, Double teta, Double t) {
        double ballFinalPos = v0 * Math.cos(Math.toRadians(teta)) * t;
        double playerDistance = ballFinalPos - distance;

        return playerDistance;
    }

    // Método: Mostra a mensagem de erro.
    private void errorMsg(String msg, String title) {
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE);
    }
}