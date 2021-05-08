package hu.zpb.quoridor;

import hu.zpb.quoridor.data.GUI;

import javax.swing.*;
import java.awt.*;


public class Main {


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame();//creating instance of JFrame

//        JButton b = new JButton("click");//creating instance of JButton
//        b.setBounds(130, 100, 100, 40);//x axis, y axis, width, height
//
//        f.add(b);//adding button in JFrame


            f.setSize(900, 600);//400 width and 500 height
            var panel = new GUI();
            panel.setBackground(Color.WHITE);
            f.getContentPane().add(panel, BorderLayout.CENTER);

            f.setVisible(true);//making the frame visible
            f.setResizable(false);
            f.setLocationRelativeTo(null);
        });
    }
}
