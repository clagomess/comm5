import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

public class Ui {
    private JFrame getJanela(int w, int h){
        JFrame returnJanela = new JFrame();
        returnJanela.setTitle("COMM5 - Gomes");
        returnJanela.setSize(w, h);
        returnJanela.setResizable(false);
        returnJanela.setLocationRelativeTo(null);
        returnJanela.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        returnJanela.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent w) {
                System.exit(0);
            }
        });

        return returnJanela;
    }

    public void home(){
        final Comm5 comm5 = new Comm5();

        // ### Inicia UI ###
        JFrame janela = this.getJanela(220, 300);

        // Field IP
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));
        panel.setPreferredSize(new Dimension(200, 80));
        final JTextField tIp = new JTextField("192.168.25.25");
        panel.add(tIp);
        JButton bConectar = new JButton("Conectar");
        panel.add(bConectar);

        janela.add(panel);
        // End Field IP

        // Sensores
        panel = new JPanel();
        panel.setLayout(new GridLayout(0, 4));
        panel.setPreferredSize(new Dimension(200, 50));
        final JPanel lS1 = new JPanel();
        final JPanel lS2 = new JPanel();
        final JPanel lS3 = new JPanel();
        final JPanel lS4 = new JPanel();
        panel.add(lS1);
        panel.add(lS2);
        panel.add(lS3);
        panel.add(lS4);
        janela.add(panel);
        // End sensores

        // Botões
        panel = new JPanel();
        panel.setLayout(new GridLayout(0, 4));
        panel.setPreferredSize(new Dimension(200, 50));
        final JButton bB1 = new JButton("R1");
        final JButton bB2 = new JButton("R2");
        final JButton bB3 = new JButton("R3");
        final JButton bB4 = new JButton("R4");
        panel.add(bB1);
        panel.add(bB2);
        panel.add(bB3);
        panel.add(bB4);
        janela.add(panel);
        // Fim Botões

        bConectar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if(!comm5.abreConexao(tIp.getText())){
                    JOptionPane.showMessageDialog(null, "Não foi possível conectar!");
                }else{
                    Thread sensorRun = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                                lS1.setBackground(Color.RED);
                                lS2.setBackground(Color.RED);
                                lS3.setBackground(Color.RED);
                                lS4.setBackground(Color.RED);

                                if(Comm5.sensor == null){
                                    continue;
                                }

                                if (Comm5.sensor.containsKey(1)) {
                                    if (Comm5.sensor.get(1)) {
                                        lS1.setBackground(Color.GREEN);
                                    }
                                }

                                if (Comm5.sensor.containsKey(2)) {
                                    if (Comm5.sensor.get(2)) {
                                        lS2.setBackground(Color.GREEN);
                                    }
                                }

                                if (Comm5.sensor.containsKey(3)) {
                                    if (Comm5.sensor.get(3)) {
                                        lS3.setBackground(Color.GREEN);
                                    }
                                }

                                if (Comm5.sensor.containsKey(4)) {
                                    if (Comm5.sensor.get(4)) {
                                        lS4.setBackground(Color.GREEN);
                                    }
                                }

                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.getMessage();
                                }
                            }
                        }
                    });

                    sensorRun.start();

                    JOptionPane.showMessageDialog(null, "Conectado!");
                }
            }
        });

        final HashMap<Integer, Boolean> releStatus = new HashMap<>();
        releStatus.put(1, true);
        releStatus.put(2, true);
        releStatus.put(3, true);
        releStatus.put(4, true);

        bB1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                bB1.setBackground((releStatus.get(1) ? Color.YELLOW : null));
                comm5.rele(1, releStatus.get(1));
                releStatus.put(1, !releStatus.get(1));
            }
        });

        bB2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                bB2.setBackground((releStatus.get(2) ? Color.YELLOW : null));
                comm5.rele(2, releStatus.get(2));
                releStatus.put(2, !releStatus.get(2));
            }
        });

        bB3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                bB3.setBackground((releStatus.get(3) ? Color.YELLOW : null));
                comm5.rele(3, releStatus.get(3));
                releStatus.put(3, !releStatus.get(3));
            }
        });

        bB4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                bB4.setBackground((releStatus.get(4) ? Color.YELLOW : null));
                comm5.rele(4, releStatus.get(4));
                releStatus.put(4, !releStatus.get(4));
            }
        });

        janela.setVisible(true);
    }
}