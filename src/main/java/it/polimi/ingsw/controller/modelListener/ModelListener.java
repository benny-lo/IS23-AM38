package it.polimi.ingsw.controller.modelListener;

public abstract class ModelListener {
    /**
     * Boolean flag. It is set only if {@code this} has updates registered.
     */
    protected boolean changed;

    /**
     * Constructor for class. The state of {@code this} is set to empty.
     */
    public ModelListener() {
        changed = false;
    }

    /**
     * Answers whether the state of {@code this} has changed, and therefore is non-empty.
     * @return {@code true} iff the state has changed.
     */
    public boolean hasChanged() {
        return changed;
    }
}
