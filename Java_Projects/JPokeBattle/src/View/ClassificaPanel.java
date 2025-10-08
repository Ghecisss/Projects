package View;

import Model.Classifica;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * gestisce la parte grafica della classifica della top 10
 * @author Jacopo De Crescenzo
 * @version 1.0
 */
public class ClassificaPanel extends JPanel {
    /**
     * crea il panel della classifica
     * @param onClose una funzione da eseguire quando viene chiusa la classifica
     */
    public ClassificaPanel(Runnable onClose) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(0, 0, 0, 200));
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        setBounds(200, 100, 400, 350);

        JLabel titolo = new JLabel("TOP 10 PARTITE");
        titolo.setForeground(Color.WHITE);
        titolo.setFont(new Font("Arial", Font.BOLD, 22));
        titolo.setAlignmentX(CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(20));
        add(titolo);
        add(Box.createVerticalStrut(20));

        List<Map.Entry<String, Integer>> top10 = Classifica.getInstance().getTop10();

        if (top10.isEmpty()) {
            JLabel noData = new JLabel("Nessuna partita registrata.");
            noData.setForeground(Color.LIGHT_GRAY);
            noData.setAlignmentX(CENTER_ALIGNMENT);
            add(noData);
        } else {
            for (int i = 0; i < top10.size(); i++) {
                Map.Entry<String, Integer> entry = top10.get(i);
                JLabel label = new JLabel((i + 1) + ". " + entry.getKey() + " - " + entry.getValue() + " vittorie");
                label.setForeground(Color.WHITE);
                label.setFont(new Font("Monospaced", Font.PLAIN, 16));
                label.setAlignmentX(CENTER_ALIGNMENT);
                add(label);
            }
        }

        add(Box.createVerticalStrut(20));

        JButton chiudi = new JButton("Chiudi");
        chiudi.setAlignmentX(CENTER_ALIGNMENT);
        chiudi.addActionListener(e -> {
            Container parent = getParent();
            if (parent != null) {
                parent.remove(this);
                parent.revalidate();
                parent.repaint();
            }
            onClose.run();
        });
        add(chiudi);
    }
}
