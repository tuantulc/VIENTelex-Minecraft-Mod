package net.clot.vientelex;

import net.clot.vientelex.engine.TelexEngine;
import net.clot.vientelex.engine.TelexResult;

public class VietnameseIME {

    private static boolean enabled = true;

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean value) {
        enabled = value;
    }

    public static boolean toggle() {
        enabled = !enabled;
        return enabled;
    }

    public static int findWordStart(String text, int cursor) {
        if (text == null || text.isEmpty() || cursor <= 0) return 0;
        int idx = Math.min(cursor - 1, text.length() - 1);

        while (idx >= 0) {
            char c = text.charAt(idx);
            if (Character.isWhitespace(c) || isWordDelimiter(c)) {
                return idx + 1;
            }
            idx--;
        }
        return 0;
    }

    public static boolean isWordDelimiter(char c) {
        return ",.!?;:/-+*=<>()[]{}\"'\n\r\t§&".indexOf(c) >= 0;
    }

    public static class ProcessResult {
        public final String newText;
        public final int newCursor;

        public ProcessResult(String newText, int newCursor) {
            this.newText = newText;
            this.newCursor = newCursor;
        }
    }

    public static ProcessResult processInput(String fullText, int cursor, char typedChar) {
        if (!enabled) return null;

        if (Character.isWhitespace(typedChar) || isWordDelimiter(typedChar)) {
            TelexEngine.reset();
            return null;
        }

        int safeCursor = Math.max(0, Math.min(cursor, fullText.length()));
        int wordStart = findWordStart(fullText, safeCursor);
        String currentWord = fullText.substring(wordStart, safeCursor);

        TelexResult result = TelexEngine.process(currentWord, typedChar);
        if (result.isHandled()) {
            String beforeWord = fullText.substring(0, wordStart);
            String afterCursor = fullText.substring(safeCursor);
            String newWord = result.getNewWord();
            String newText = beforeWord + newWord + afterCursor;
            int newCursor = wordStart + newWord.length();
            return new ProcessResult(newText, newCursor);
        }

        return null;
    }
}
