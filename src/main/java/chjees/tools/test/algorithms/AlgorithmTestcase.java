package chjees.tools.test.algorithms;

import javax.swing.*;

public abstract class AlgorithmTestcase {
    private final String name;
    private JPanel content;

    public AlgorithmTestcase(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    public String getName() {
        return name;
    }

    public JPanel getContent() {
        return content;
    }

    public void setup()
    {
        this.content = makeContent();
    }

    public abstract JPanel makeContent();
}
