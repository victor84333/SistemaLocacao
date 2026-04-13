import view.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Usa look and feel do sistema operacional
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            try {
                new MainFrame();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Erro ao iniciar o sistema:\n" + e.getMessage() +
                        "\n\nVerifique se o XAMPP está ativo e o banco foi criado.",
                        "Erro de Inicialização",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
}
