import ui.TrackerFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarculaLaf());

        } catch (Exception ex) {
            System.err.println("Не удалось запустить FlatLaf");
        }
        SwingUtilities.invokeLater(() -> {
            TrackerFrame frame = new TrackerFrame();
            frame.setVisible(true);
        });
    }
}
