package chjees.tools.test.algorithms.components;

import javax.annotation.Nonnull;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.util.function.Consumer;

public class AlgorithmPainter  extends JPanel {
    private final Consumer<Graphics> painter;

    public AlgorithmPainter(@Nonnull Consumer<Graphics> painter) {
        this.painter = painter;
    }

    @Override
    public Border getBorder() {
        return BorderFactory.createBevelBorder(BevelBorder.LOWERED);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        painter.accept(g);
    }
}
