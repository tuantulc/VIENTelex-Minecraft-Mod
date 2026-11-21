package net.clot.vientelex.engine;

public class TelexResult {
    private final boolean handled;
    private final String newWord;

    public TelexResult(boolean handled, String newWord) {
        this.handled = handled;
        this.newWord = newWord;
    }

    public boolean isHandled() {
        return handled;
    }

    public String getNewWord() {
        return newWord;
    }
}
