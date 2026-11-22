package net.clot.vientelex.engine;

import net.clot.vientelex.engine.VietnameseChar.CharData;
import net.clot.vientelex.engine.VietnameseChar.Modifier;
import net.clot.vientelex.engine.VietnameseChar.Tone;

import java.util.ArrayList;
import java.util.List;

public class TelexEngine {

    public static TelexResult process(String currentWord, char keyChar) {
        if (currentWord == null) currentWord = "";
        char lowerKey = Character.toLowerCase(keyChar);

        if ((currentWord.equals("ư") || currentWord.equals("Ư")) && (lowerKey == 'a' || lowerKey == 'e' || lowerKey == 'i' || lowerKey == 'o')) {
            char wChar = currentWord.equals("Ư") ? 'W' : 'w';
            return new TelexResult(true, String.valueOf(wChar) + keyChar);
        }

        if (lowerKey == 'd') {
            TelexResult dResult = processD(currentWord, keyChar);
            if (dResult.isHandled()) return dResult;
        }

        if (lowerKey == 'a' || lowerKey == 'e' || lowerKey == 'o' || lowerKey == 'w') {
            TelexResult vResult = processVowelModifier(currentWord, keyChar);
            if (vResult.isHandled()) return vResult;
        }

        Tone targetTone = getToneFromKey(lowerKey);
        if (targetTone != null || lowerKey == 'z') {
            TelexResult tResult = processTone(currentWord, keyChar, targetTone != null ? targetTone : Tone.NONE, lowerKey == 'z');
            if (tResult.isHandled()) return tResult;
        }

        return new TelexResult(false, currentWord);
    }

    private static Tone getToneFromKey(char lowerKey) {
        return switch (lowerKey) {
            case 's' -> Tone.ACUTE;
            case 'f' -> Tone.GRAVE;
            case 'r' -> Tone.HOOK;
            case 'x' -> Tone.TILDE;
            case 'j' -> Tone.DOT;
            default -> null;
        };
    }

    private static TelexResult processD(String word, char keyChar) {
        if (word.isEmpty()) return new TelexResult(false, word);

        int lastIdx = word.length() - 1;
        char lastChar = word.charAt(lastIdx);
        CharData data = VietnameseChar.decompose(lastChar);

        if (data != null && data.base == 'd') {
            if (data.modifier == Modifier.NONE) {
                char composed = VietnameseChar.compose('d', Modifier.STROKE, Tone.NONE, Character.isUpperCase(lastChar) || Character.isUpperCase(keyChar));
                return new TelexResult(true, word.substring(0, lastIdx) + composed);
            } else if (data.modifier == Modifier.STROKE) {
                char c1 = data.upper ? 'D' : 'd';
                return new TelexResult(true, word.substring(0, lastIdx) + c1 + keyChar);
            }
        }

        boolean hasVowel = false;
        for (int i = 0; i < word.length(); i++) {
            if (VietnameseChar.isVowel(word.charAt(i))) {
                hasVowel = true;
                break;
            }
        }

        if (hasVowel && !isNonVietnameseWord(word)) {
            char firstChar = word.charAt(0);
            CharData firstData = VietnameseChar.decompose(firstChar);

            if (firstData != null && firstData.base == 'd') {
                if (firstData.modifier == Modifier.NONE) {
                    char composed = VietnameseChar.compose('d', Modifier.STROKE, Tone.NONE, firstData.upper);
                    return new TelexResult(true, composed + word.substring(1));
                } else if (firstData.modifier == Modifier.STROKE) {
                    char c1 = firstData.upper ? 'D' : 'd';
                    return new TelexResult(true, c1 + word.substring(1) + keyChar);
                }
            }
        }

        return new TelexResult(false, word);
    }

    public static boolean isNonVietnameseWord(String word) {
        if (word == null || word.length() < 2) return false;
        String lower = word.toLowerCase();

        for (int i = 0; i < lower.length() - 1; i++) {
            char c1 = lower.charAt(i);
            char c2 = lower.charAt(i + 1);
            if (c1 == c2 && !VietnameseChar.isVowel(c1) && c1 != 'd') {
                return true;
            }
        }

        if (lower.contains("ea") || lower.contains("ou") || lower.contains("ae")) {
            return true;
        }

        for (int i = 0; i < lower.length(); i++) {
            char c = lower.charAt(i);
            if (c == 'f' || c == 'j' || c == 'z') {
                return true;
            }
        }

        if (lower.length() >= 2) {
            String prefix2 = lower.substring(0, 2);
            switch (prefix2) {
                case "bl", "br", "cl", "cr", "dr", "fl", "fr", "gl", "gr", "pl", "pr",
                     "sk", "sl", "sm", "sn", "sp", "st", "sw", "tw" -> {
                    return true;
                }
            }
            if (lower.startsWith("str")) {
                return true;
            }
        }

        // cau truc V-C-V (tieng viet khong co nguyen am bi tach boi phu am nhu dater, water)
        int firstVowel = -1;
        int firstConsAfterVowel = -1;
        for (int i = 0; i < lower.length(); i++) {
            char c = lower.charAt(i);
            if (VietnameseChar.isVowel(c)) {
                if (firstConsAfterVowel != -1) {
                    return true;
                }
                if (firstVowel == -1) {
                    firstVowel = i;
                }
            } else {
                if (firstVowel != -1) {
                    firstConsAfterVowel = i;
                }
            }
        }

        return false;
    }

    // chan van khong ton tai trong tieng viet (vd: â khong di voi ch)
    private static boolean isValidVowelCodaPair(char base, Modifier mod, String coda) {
        if (coda.isEmpty()) return true;

        if (base == 'a' && mod == Modifier.HAT) {
            return switch (coda) {
                case "c", "t", "p", "m", "n", "ng", "y", "u" -> true;
                default -> false;
            };
        }

        if (base == 'a' && mod == Modifier.BREVE) {
            return switch (coda) {
                case "c", "t", "p", "m", "n", "ng" -> true;
                default -> false;
            };
        }

        if (base == 'e' && mod == Modifier.HAT) {
            return switch (coda) {
                case "ch", "nh", "t", "p", "m", "n", "ng", "u" -> true;
                default -> false;
            };
        }

        if (base == 'o' && mod == Modifier.HAT) {
            return switch (coda) {
                case "c", "t", "p", "m", "n", "ng", "i" -> true;
                default -> false;
            };
        }

        if (base == 'o' && mod == Modifier.HORN) {
            return switch (coda) {
                case "i", "m", "n", "p", "t" -> true;
                default -> false;
            };
        }

        if (base == 'u' && mod == Modifier.HORN) {
            return switch (coda) {
                case "i", "u", "a", "c", "t", "ng" -> true;
                default -> false;
            };
        }

        return isValidCoda(coda);
    }

    private static TelexResult processVowelModifier(String word, char keyChar) {
        char lowerKey = Character.toLowerCase(keyChar);

        if (word.isEmpty()) {
            if (lowerKey == 'w') {
                char composed = Character.isUpperCase(keyChar) ? 'Ư' : 'ư';
                return new TelexResult(true, String.valueOf(composed));
            }
            return new TelexResult(false, word);
        }

        if (lowerKey == 'w') {
            TelexResult uoResult = processHornUO(word, keyChar);
            if (uoResult.isHandled()) return uoResult;

            TelexResult uaResult = processHornUA(word, keyChar);
            if (uaResult.isHandled()) return uaResult;
        }

        int candidateIdx = -1;
        for (int i = word.length() - 1; i >= 0; i--) {
            char c = word.charAt(i);
            CharData data = VietnameseChar.decompose(c);
            char base = (data != null) ? data.base : Character.toLowerCase(c);

            if (lowerKey == 'a' && base == 'a') { candidateIdx = i; break; }
            if (lowerKey == 'e' && base == 'e') { candidateIdx = i; break; }
            if (lowerKey == 'o' && base == 'o') { candidateIdx = i; break; }
            if (lowerKey == 'w') {
                if (base == 'u' || base == 'o' || base == 'a') {
                    candidateIdx = i;
                    break;
                }
            }
        }

        if (candidateIdx >= 0) {
            String tail = word.substring(candidateIdx + 1).toLowerCase();

            if (!tail.isEmpty() && !isValidCoda(tail)) {
                return new TelexResult(false, word);
            }

            char c = word.charAt(candidateIdx);
            CharData data = VietnameseChar.decompose(c);
            char base = (data != null) ? data.base : Character.toLowerCase(c);
            Modifier currentMod = (data != null) ? data.modifier : Modifier.NONE;
            Tone currentTone = (data != null) ? data.tone : Tone.NONE;
            boolean isUpper = Character.isUpperCase(c);

            Modifier targetMod = null;
            if (lowerKey == 'a' && base == 'a') targetMod = Modifier.HAT;
            else if (lowerKey == 'e' && base == 'e') targetMod = Modifier.HAT;
            else if (lowerKey == 'o' && base == 'o') targetMod = Modifier.HAT;
            else if (lowerKey == 'w') {
                if (base == 'a') targetMod = Modifier.BREVE;
                else if (base == 'o' || base == 'u') targetMod = Modifier.HORN;
            }

            if (targetMod != null) {
                if (!tail.isEmpty() && !isValidVowelCodaPair(base, targetMod, tail)) {
                    return new TelexResult(false, word);
                }

                if (currentMod == Modifier.NONE) {
                    Character newChar = VietnameseChar.compose(base, targetMod, currentTone, isUpper);
                    if (newChar != null) {
                        return new TelexResult(true, replaceCharAt(word, candidateIdx, newChar));
                    }
                } else if (currentMod == targetMod) {
                    Character baseChar = VietnameseChar.compose(base, Modifier.NONE, currentTone, isUpper);
                    if (baseChar != null) {
                        String restored = replaceCharAt(word, candidateIdx, baseChar);
                        return new TelexResult(true, restored + keyChar);
                    }
                }
            }
        }

        if (lowerKey == 'w') {
            char composed = Character.isUpperCase(keyChar) ? 'Ư' : 'ư';
            return new TelexResult(true, word + composed);
        }

        return new TelexResult(false, word);
    }

    private static TelexResult processHornUO(String word, char keyChar) {
        int uIdx = -1;
        int oIdx = -1;

        for (int i = 0; i < word.length() - 1; i++) {
            char c1 = word.charAt(i);
            char c2 = word.charAt(i + 1);
            CharData d1 = VietnameseChar.decompose(c1);
            CharData d2 = VietnameseChar.decompose(c2);
            char b1 = (d1 != null) ? d1.base : Character.toLowerCase(c1);
            char b2 = (d2 != null) ? d2.base : Character.toLowerCase(c2);

            if (b1 == 'u' && b2 == 'o') {
                uIdx = i;
                oIdx = i + 1;
                break;
            }
        }

        if (uIdx >= 0) {
            char cu = word.charAt(uIdx);
            char co = word.charAt(oIdx);
            CharData du = VietnameseChar.decompose(cu);
            CharData doData = VietnameseChar.decompose(co);

            Modifier mu = (du != null) ? du.modifier : Modifier.NONE;
            Modifier mo = (doData != null) ? doData.modifier : Modifier.NONE;
            Tone tu = (du != null) ? du.tone : Tone.NONE;
            Tone to = (doData != null) ? doData.tone : Tone.NONE;

            if (mu == Modifier.HORN && mo == Modifier.HORN) {
                Character rawU = VietnameseChar.compose('u', Modifier.NONE, tu, Character.isUpperCase(cu));
                Character rawO = VietnameseChar.compose('o', Modifier.NONE, to, Character.isUpperCase(co));
                StringBuilder sb = new StringBuilder(word);
                sb.setCharAt(uIdx, rawU != null ? rawU : 'u');
                sb.setCharAt(oIdx, rawO != null ? rawO : 'o');
                sb.append(keyChar);
                return new TelexResult(true, sb.toString());
            } else {
                Character newU = VietnameseChar.compose('u', Modifier.HORN, tu, Character.isUpperCase(cu));
                Character newO = VietnameseChar.compose('o', Modifier.HORN, to, Character.isUpperCase(co));
                if (newU != null && newO != null) {
                    StringBuilder sb = new StringBuilder(word);
                    sb.setCharAt(uIdx, newU);
                    sb.setCharAt(oIdx, newO);
                    return new TelexResult(true, sb.toString());
                }
            }
        }
        return new TelexResult(false, word);
    }

    private static TelexResult processHornUA(String word, char keyChar) {
        int uIdx = -1;
        int aIdx = -1;

        for (int i = 0; i < word.length() - 1; i++) {
            char c1 = word.charAt(i);
            char c2 = word.charAt(i + 1);
            CharData d1 = VietnameseChar.decompose(c1);
            CharData d2 = VietnameseChar.decompose(c2);
            char b1 = (d1 != null) ? d1.base : Character.toLowerCase(c1);
            char b2 = (d2 != null) ? d2.base : Character.toLowerCase(c2);

            if (b1 == 'u' && b2 == 'a') {
                if (i > 0 && Character.toLowerCase(word.charAt(i - 1)) == 'q') {
                    // "qu" is a consonant cluster, 'a' can take breve (ă) as in "quăn", "quắt"
                    continue;
                }
                uIdx = i;
                aIdx = i + 1;
                break;
            }
        }

        if (uIdx >= 0) {
            // "ua" -> "ưa" only when there's no coda after 'a'
            if (aIdx == word.length() - 1) {
                char cu = word.charAt(uIdx);
                CharData du = VietnameseChar.decompose(cu);
                Modifier mu = (du != null) ? du.modifier : Modifier.NONE;
                Tone tu = (du != null) ? du.tone : Tone.NONE;

                if (mu == Modifier.HORN) {
                    Character rawU = VietnameseChar.compose('u', Modifier.NONE, tu, Character.isUpperCase(cu));
                    StringBuilder sb = new StringBuilder(word);
                    sb.setCharAt(uIdx, rawU != null ? rawU : 'u');
                    sb.append(keyChar);
                    return new TelexResult(true, sb.toString());
                } else {
                    Character newU = VietnameseChar.compose('u', Modifier.HORN, tu, Character.isUpperCase(cu));
                    if (newU != null) {
                        StringBuilder sb = new StringBuilder(word);
                        sb.setCharAt(uIdx, newU);
                        return new TelexResult(true, sb.toString());
                    }
                }
            }
        }
        return new TelexResult(false, word);
    }

    private static boolean isValidCoda(String coda) {
        if (coda.isEmpty()) return true;
        return switch (coda) {
            case "c", "ch", "m", "n", "nh", "ng", "p", "t", "i", "y", "u", "o", "a" -> true;
            default -> false;
        };
    }

    private static TelexResult processTone(String word, char keyChar, Tone targetTone, boolean isClearKey) {
        if (word.isEmpty()) return new TelexResult(false, word);

        if (!isClearKey && isNonVietnameseWord(word)) {
            return new TelexResult(false, word);
        }

        List<Integer> vowelIndices = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            if (VietnameseChar.isVowel(word.charAt(i))) {
                vowelIndices.add(i);
            }
        }

        if (vowelIndices.isEmpty()) {
            return new TelexResult(false, word);
        }

        int currentToneIndex = -1;
        Tone currentExistingTone = Tone.NONE;
        for (int idx : vowelIndices) {
            CharData data = VietnameseChar.decompose(word.charAt(idx));
            if (data != null && data.tone != Tone.NONE) {
                currentToneIndex = idx;
                currentExistingTone = data.tone;
                break;
            }
        }

        if (!isClearKey && currentExistingTone != Tone.NONE && currentExistingTone == targetTone) {
            char c = word.charAt(currentToneIndex);
            CharData data = VietnameseChar.decompose(c);
            Character stripped = VietnameseChar.compose(data.base, data.modifier, Tone.NONE, data.upper);
            if (stripped != null) {
                String restored = replaceCharAt(word, currentToneIndex, stripped);
                return new TelexResult(true, restored + keyChar);
            }
        }

        int lastVowelIdx = vowelIndices.get(vowelIndices.size() - 1);
        String coda = "";
        if (lastVowelIdx < word.length() - 1) {
            coda = word.substring(lastVowelIdx + 1).toLowerCase();
            if (!isValidCoda(coda)) {
                return new TelexResult(false, word);
            }
        }

        // am tac c, ch, p, t chi di voi sac hoac nang
        if (!isClearKey && (coda.equals("c") || coda.equals("ch") || coda.equals("p") || coda.equals("t"))) {
            if (targetTone == Tone.GRAVE || targetTone == Tone.HOOK || targetTone == Tone.TILDE) {
                return new TelexResult(false, word);
            }
        }

        if (isClearKey) {
            if (currentToneIndex >= 0) {
                char c = word.charAt(currentToneIndex);
                CharData data = VietnameseChar.decompose(c);
                Character stripped = VietnameseChar.compose(data.base, data.modifier, Tone.NONE, data.upper);
                if (stripped != null) {
                    return new TelexResult(true, replaceCharAt(word, currentToneIndex, stripped));
                }
            }
            return new TelexResult(false, word);
        }

        int targetVowelIndex = determineTonePosition(word, vowelIndices);
        if (targetVowelIndex < 0) return new TelexResult(false, word);

        String workingWord = word;
        if (currentToneIndex >= 0 && currentToneIndex != targetVowelIndex) {
            char oldChar = workingWord.charAt(currentToneIndex);
            CharData oldData = VietnameseChar.decompose(oldChar);
            Character oldStripped = VietnameseChar.compose(oldData.base, oldData.modifier, Tone.NONE, oldData.upper);
            if (oldStripped != null) {
                workingWord = replaceCharAt(workingWord, currentToneIndex, oldStripped);
            }
        }

        char targetChar = workingWord.charAt(targetVowelIndex);
        CharData targetData = VietnameseChar.decompose(targetChar);
        char base = (targetData != null) ? targetData.base : Character.toLowerCase(targetChar);
        Modifier mod = (targetData != null) ? targetData.modifier : Modifier.NONE;
        boolean isUpper = (targetData != null) ? targetData.upper : Character.isUpperCase(targetChar);

        Character newChar = VietnameseChar.compose(base, mod, targetTone, isUpper);
        if (newChar != null) {
            return new TelexResult(true, replaceCharAt(workingWord, targetVowelIndex, newChar));
        }

        return new TelexResult(false, word);
    }

    private static int determineTonePosition(String word, List<Integer> vowelIndices) {
        if (vowelIndices.isEmpty()) return -1;
        if (vowelIndices.size() == 1) return vowelIndices.get(0);

        List<Integer> actualVowels = new ArrayList<>(vowelIndices);
        String lower = word.toLowerCase();

        int quIdx = lower.indexOf("qu");
        if (quIdx >= 0 && actualVowels.contains(quIdx + 1) && actualVowels.size() > 1) {
            actualVowels.remove(Integer.valueOf(quIdx + 1));
        }

        int giIdx = lower.indexOf("gi");
        if (giIdx >= 0 && actualVowels.contains(giIdx + 1) && actualVowels.size() > 1) {
            actualVowels.remove(Integer.valueOf(giIdx + 1));
        }

        if (actualVowels.size() == 1) return actualVowels.get(0);
        if (actualVowels.isEmpty()) return vowelIndices.get(0);

        for (int idx : actualVowels) {
            CharData d = VietnameseChar.decompose(word.charAt(idx));
            if (d != null && d.modifier != Modifier.NONE) {
                if (d.modifier == Modifier.HORN && d.base == 'u' && actualVowels.contains(idx + 1)) {
                    CharData nextD = VietnameseChar.decompose(word.charAt(idx + 1));
                    if (nextD != null && nextD.base == 'o' && nextD.modifier == Modifier.HORN) {
                        return idx + 1;
                    }
                }
                return idx;
            }
        }

        int lastVowelIdx = actualVowels.get(actualVowels.size() - 1);
        boolean hasCoda = (lastVowelIdx < word.length() - 1);

        if (hasCoda) {
            return actualVowels.get(1);
        } else {
            char v1 = Character.toLowerCase(word.charAt(actualVowels.get(0)));
            char v2 = Character.toLowerCase(word.charAt(actualVowels.get(1)));
            if ((v1 == 'o' && (v2 == 'a' || v2 == 'e')) || (v1 == 'u' && v2 == 'y')) {
                return actualVowels.get(1);
            }
            return actualVowels.get(0);
        }
    }

    private static String replaceCharAt(String str, int index, char replacement) {
        StringBuilder sb = new StringBuilder(str);
        sb.setCharAt(index, replacement);
        return sb.toString();
    }
}
