package chjees.tools.test.algorithms;

import chjees.tools.test.Algorithms;
import chjees.tools.test.algorithms.components.AlgorithmPainter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChunkTestcase extends AlgorithmTestcase  implements ActionListener {
    public AlgorithmPainter painter;

    public ChunkTestcase(String name) {
        super(name);
    }

    @Override
    public JPanel makeContent() {
        //Make content.
        JPanel content = new JPanel(new BorderLayout(2,2));
        AlgorithmPainter painter = new AlgorithmPainter(this::paint);
        painter.setPreferredSize(new Dimension(640, 400));
        painter.setMinimumSize(new Dimension(640, 400));
        this.painter = painter;
        content.add(painter, BorderLayout.CENTER);

        JPanel controls = new JPanel();
        content.add(controls, BorderLayout.SOUTH);
        return content;
    }

    private void paint(Graphics graphics){
        //Paint the algorithm here.
        //Paint grid
        int cellSize = 64;
        for (int y = 0; y < 464 / cellSize; y++) {
            for (int x = 0; x < 704 / cellSize; x++) {
                int xOffset = x * cellSize;
                int yOffset = y * cellSize;
                graphics.setColor(Color.BLACK);
                graphics.drawRect(xOffset, yOffset , cellSize, cellSize);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
