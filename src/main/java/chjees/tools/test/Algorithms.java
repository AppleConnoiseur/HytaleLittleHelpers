package chjees.tools.test;

import javax.annotation.Nonnull;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import chjees.tools.algorithm.SpiralPattern;
import org.joml.Vector3i;

public class Algorithms {
    //Swing
    private static JList<TestCase> testsListBox;
    private static final TestCase[] testCases = new TestCase[]{
            new TestCase("Spiral Pattern", makeSpiralContent()),
            new TestCase("Chunk Traverser", makeChunksContent())};
    private static JPanel testPanel;

    //State
    private static int currentTest;
    private static SpiralState spiralState;
    private static ChunksState chunksState;

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("chjees.tools.test: Algorithms");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(2,2));

        //Add the content panel.
        testPanel = new JPanel();
        testPanel.setMinimumSize(new Dimension(640, 480));
        testPanel.setPreferredSize(new Dimension(640, 480));
        frame.getContentPane().add(testPanel, BorderLayout.WEST);

        //Make UI
        testsListBox = new JList<>(new AbstractListModel<>() {
            @Override
            public int getSize() {
                return testCases.length;
            }

            @Override
            public TestCase getElementAt(int index) {
                return testCases[index];
            }
        });
        testsListBox.addListSelectionListener(e -> {
            if(e.getValueIsAdjusting())
            {
                int index = testsListBox.getSelectedIndex();
                //Swap out content.
                if(index != currentTest)
                {
                    //Make content changes after all events are done.
                    SwingUtilities.invokeLater(() -> {
                        testPanel.removeAll();
                        testPanel.add(testCases[index].content);
                        testPanel.validate();
                        testPanel.repaint();
                    });
                }

                currentTest = index;
            }
        });
        testsListBox.setSelectedIndex(0);
        testPanel.add(testCases[0].content); //Add first test.
        frame.getContentPane().add(testsListBox, BorderLayout.CENTER);

        //Display the window.
        frame.pack();
        frame.setVisible(true);

        frame.setSize(896,480);
        frame.setLocationRelativeTo(null);
    }

    static void main() {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(Algorithms::createAndShowGUI);
    }

    private static class SpiralState implements ActionListener, MouseInputListener, ChangeListener {
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
    }

    private static JPanel makeSpiralContent()
    {
        //Setup state.
        spiralState = new SpiralState();

        //Make content.
        JPanel content = new JPanel(new BorderLayout(2,2));
        AlgorithmPainter painter = new AlgorithmPainter(graphics -> {
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
            if(spiralState.origin != null)
            {
                graphics2D.setColor(Color.BLACK);
                graphics2D.fillRect(
                        spiralState.origin.x * cellSize + 2,
                        spiralState.origin.z * cellSize + 2,
                        cellSize - 4, cellSize - 4);
                AtomicInteger step = new AtomicInteger();
                SpiralPattern.generate(spiralState.range, spiralState.origin, vector3i -> {
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
                    (spiralState.mousePosition.x - (spiralState.mousePosition.x % cellSize)) / cellSize,
                    (spiralState.mousePosition.y - (spiralState.mousePosition.y % cellSize)) / cellSize);
            graphics2D.setColor(Color.RED);
            graphics2D.setStroke(new BasicStroke(3));
            graphics2D.drawRect(mouseCell.x * cellSize, mouseCell.y * cellSize , cellSize, cellSize);
        });
        spiralState.painter = painter;
        painter.setPreferredSize(new Dimension(640, 400));
        painter.setMinimumSize(new Dimension(640, 400));
        painter.addMouseListener(spiralState);
        painter.addMouseMotionListener(spiralState);
        content.add(painter, BorderLayout.CENTER);

        JPanel controls = new JPanel();
        controls.add(new JLabel("<html><font color=#0000ff>Help:</font>    <span> Click on the grid to start.</span></html>"));

        JButton resetButton = new JButton("Reset");
        resetButton.setActionCommand("reset");
        resetButton.addActionListener(spiralState);
        controls.add(resetButton);

        controls.add(new JLabel("Steps:"));

        JSpinner rangeSpinner = new JSpinner(new SpinnerNumberModel(24, 1, 100, 1));
        rangeSpinner.addChangeListener(spiralState);
        spiralState.rangeSpinner = rangeSpinner;
        controls.add(rangeSpinner);

        content.add(controls, BorderLayout.SOUTH);
        return content;
    }

    private static class ChunksState implements ActionListener {
        public AlgorithmPainter painter;
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    private static JPanel makeChunksContent()
    {
        //Setup state.
        chunksState = new ChunksState();

        //Make content.
        JPanel content = new JPanel(new BorderLayout(2,2));
        AlgorithmPainter painter = new AlgorithmPainter(graphics -> {
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
        });
        chunksState.painter = painter;
        painter.setPreferredSize(new Dimension(640, 400));
        painter.setMinimumSize(new Dimension(640, 400));
        content.add(painter, BorderLayout.CENTER);

        JPanel controls = new JPanel();
        content.add(controls, BorderLayout.SOUTH);
        return content;
    }

    private static class TestCase {
        private final String name;
        private final JPanel content;

        private TestCase(String name, JPanel content) {
            this.name = name;
            this.content = content;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private static class AlgorithmPainter extends JPanel {
        private final Consumer<Graphics> painter;

        private AlgorithmPainter(@Nonnull Consumer<Graphics> painter) {
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
}
