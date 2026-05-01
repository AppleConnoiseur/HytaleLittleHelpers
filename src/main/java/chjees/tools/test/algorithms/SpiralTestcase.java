package chjees.tools.test.algorithms;

import chjees.tools.algorithm.SpiralPattern;
import chjees.tools.test.algorithms.components.AlgorithmPainter;
import org.joml.Vector3i;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicInteger;

public class SpiralTestcase extends AlgorithmTestcase  implements ActionListener, MouseInputListener, ChangeListener {
    public SpiralTestcase(String name) {
        super(name);
    }

    public AlgorithmPainter painter;
    public Point mousePosition = new Point();
    public JSpinner rangeSpinner;
    public int range = 24;
    public Vector3i origin = null;

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("reset"))
        {
            origin = null;
            rangeSpinner.setValue(24);
            range = 24;

            painter.repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //Not used
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePosition = e.getPoint();
        e.consume();
        painter.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int cellSize = 32;
        origin = new Vector3i(
                (e.getX() - (e.getX() % cellSize)) / cellSize,
                0,
                (e.getY() - (e.getY() % cellSize)) / cellSize);
        painter.repaint();
        e.consume();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //Not used
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //Not used
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //Not used
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //Not used
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if(e.getSource() == rangeSpinner)
        {
            range = (int)rangeSpinner.getValue();
            painter.repaint();
        }
    }

    @Override
    public JPanel makeContent() {
        //Make content.
        JPanel content = new JPanel(new BorderLayout(2,2));
        AlgorithmPainter painter = new AlgorithmPainter(this::paint);
        painter.setPreferredSize(new Dimension(640, 400));
        painter.setMinimumSize(new Dimension(640, 400));
        painter.addMouseListener(this);
        painter.addMouseMotionListener(this);
        this.painter = painter;
        content.add(painter, BorderLayout.CENTER);

        JPanel controls = new JPanel();
        controls.add(new JLabel("<html><font color=#0000ff>Help:</font>    <span> Click on the grid to start.</span></html>"));

        JButton resetButton = new JButton("Reset");
        resetButton.setActionCommand("reset");
        resetButton.addActionListener(this);
        controls.add(resetButton);

        controls.add(new JLabel("Steps:"));

        JSpinner rangeSpinner = new JSpinner(new SpinnerNumberModel(24, 1, 100, 1));
        this.rangeSpinner = rangeSpinner;
        rangeSpinner.addChangeListener(this);
        controls.add(rangeSpinner);

        content.add(controls, BorderLayout.SOUTH);
        return content;
    }

    private void paint(Graphics graphics) {
        //Paint the algorithm here.
        Graphics2D graphics2D = (Graphics2D) graphics;

        //Paint grid.
        int cellSize = 32;
        int halfCellSize = cellSize / 2;

        for (int y = 0; y < 432 / cellSize; y++) {
            for (int x = 0; x < 672 / cellSize; x++) {
                int xOffset = x * cellSize;
                int yOffset = y * cellSize;

                graphics2D.setColor(Color.BLACK);
                graphics2D.drawRect(xOffset, yOffset , cellSize, cellSize);
            }
        }

        //Paint algorithm.
        if(origin != null)
        {
            graphics2D.setColor(Color.BLACK);
            graphics2D.fillRect(
                    origin.x * cellSize + 2,
                    origin.z * cellSize + 2,
                    cellSize - 4, cellSize - 4);
            AtomicInteger step = new AtomicInteger();
            SpiralPattern.generate(range, origin, vector3i -> {
                //Draw cell.
                int xOffset = vector3i.x * cellSize;
                int yOffset = vector3i.z * cellSize;
                graphics2D.setColor(Color.BLUE);
                graphics2D.fillRect(xOffset + 2, yOffset + 2, cellSize - 4, cellSize - 4);

                //Draw step text.
                graphics2D.setColor(Color.WHITE);
                graphics2D.drawString("" + step.get(), xOffset + halfCellSize - 4, yOffset + halfCellSize + 4);
                step.getAndIncrement();
                return false;
            });
        }

        //Paint mouse cell.
        Point mouseCell = new Point(
                (mousePosition.x - (mousePosition.x % cellSize)) / cellSize,
                (mousePosition.y - (mousePosition.y % cellSize)) / cellSize);
        graphics2D.setColor(Color.RED);
        graphics2D.setStroke(new BasicStroke(3));
        graphics2D.drawRect(mouseCell.x * cellSize, mouseCell.y * cellSize , cellSize, cellSize);
    }
}
