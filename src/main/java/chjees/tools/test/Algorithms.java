package chjees.tools.test;

import javax.swing.*;
import java.awt.*;
import chjees.tools.test.algorithms.AlgorithmTestcase;
import chjees.tools.test.algorithms.ChunkTestcase;
import chjees.tools.test.algorithms.SpiralTestcase;

public class Algorithms {
    //Swing
    private static JList<AlgorithmTestcase> testsListBox;
    private static final AlgorithmTestcase[] testCases = new AlgorithmTestcase[]{
            new SpiralTestcase("Spiral Pattern"),
            new ChunkTestcase("Chunk Traverser")};
    private static JPanel testPanel;

    //State
    private static int currentTest;

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

        //Setup tests.
        for (AlgorithmTestcase testCase : testCases) {
            testCase.setup();
        }

        //Make UI
        testsListBox = new JList<>(new AbstractListModel<>() {
            @Override
            public int getSize() {
                return testCases.length;
            }

            @Override
            public AlgorithmTestcase getElementAt(int index) {
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
                        testPanel.add(testCases[index].getContent());
                        testPanel.validate();
                        testPanel.repaint();
                    });
                }

                currentTest = index;
            }
        });
        testsListBox.setSelectedIndex(0);
        testPanel.add(testCases[0].getContent()); //Add first test.
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
}
