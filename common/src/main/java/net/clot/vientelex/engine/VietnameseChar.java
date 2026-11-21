package net.clot.vientelex.engine;

import java.util.HashMap;
import java.util.Map;

public class VietnameseChar {

    public enum Modifier {
        NONE,
        HAT,
        BREVE,
        HORN,
        STROKE
    }

    public enum Tone {
        NONE,
        ACUTE,
        GRAVE,
        HOOK,
        TILDE,
        DOT
    }

    public static class CharData {
        public final char base;
        public final Modifier modifier;
        public final Tone tone;
        public final boolean upper;

        public CharData(char base, Modifier modifier, Tone tone, boolean upper) {
            this.base = base;
            this.modifier = modifier;
            this.tone = tone;
            this.upper = upper;
        }
    }

    private static final Map<Character, CharData> DECOMPOSE_MAP = new HashMap<>();
    private static final Map<String, Character> COMPOSE_MAP = new HashMap<>();

    static {
        register('a', Modifier.NONE, Tone.NONE, 'a', 'A');
        register('a', Modifier.NONE, Tone.ACUTE, 'á', 'Á');
        register('a', Modifier.NONE, Tone.GRAVE, 'à', 'À');
        register('a', Modifier.NONE, Tone.HOOK, 'ả', 'Ả');
        register('a', Modifier.NONE, Tone.TILDE, 'ã', 'Ã');
        register('a', Modifier.NONE, Tone.DOT, 'ạ', 'Ạ');

        register('a', Modifier.BREVE, Tone.NONE, 'ă', 'Ă');
        register('a', Modifier.BREVE, Tone.ACUTE, 'ắ', 'Ắ');
        register('a', Modifier.BREVE, Tone.GRAVE, 'ằ', 'Ằ');
        register('a', Modifier.BREVE, Tone.HOOK, 'ẳ', 'Ẳ');
        register('a', Modifier.BREVE, Tone.TILDE, 'ẵ', 'Ẵ');
        register('a', Modifier.BREVE, Tone.DOT, 'ặ', 'Ặ');

        register('a', Modifier.HAT, Tone.NONE, 'â', 'Â');
        register('a', Modifier.HAT, Tone.ACUTE, 'ấ', 'Ấ');
        register('a', Modifier.HAT, Tone.GRAVE, 'ầ', 'Ầ');
        register('a', Modifier.HAT, Tone.HOOK, 'ẩ', 'Ẩ');
        register('a', Modifier.HAT, Tone.TILDE, 'ẫ', 'Ẫ');
        register('a', Modifier.HAT, Tone.DOT, 'ậ', 'Ậ');

        register('e', Modifier.NONE, Tone.NONE, 'e', 'E');
        register('e', Modifier.NONE, Tone.ACUTE, 'é', 'É');
        register('e', Modifier.NONE, Tone.GRAVE, 'è', 'È');
        register('e', Modifier.NONE, Tone.HOOK, 'ẻ', 'Ẻ');
        register('e', Modifier.NONE, Tone.TILDE, 'ẽ', 'Ẽ');
        register('e', Modifier.NONE, Tone.DOT, 'ẹ', 'Ẹ');

        register('e', Modifier.HAT, Tone.NONE, 'ê', 'Ê');
        register('e', Modifier.HAT, Tone.ACUTE, 'ế', 'Ế');
        register('e', Modifier.HAT, Tone.GRAVE, 'ề', 'Ề');
        register('e', Modifier.HAT, Tone.HOOK, 'ể', 'Ể');
        register('e', Modifier.HAT, Tone.TILDE, 'ễ', 'Ễ');
        register('e', Modifier.HAT, Tone.DOT, 'ệ', 'Ệ');

        register('i', Modifier.NONE, Tone.NONE, 'i', 'I');
        register('i', Modifier.NONE, Tone.ACUTE, 'í', 'Í');
        register('i', Modifier.NONE, Tone.GRAVE, 'ì', 'Ì');
        register('i', Modifier.NONE, Tone.HOOK, 'ỉ', 'Ỉ');
        register('i', Modifier.NONE, Tone.TILDE, 'ĩ', 'Ĩ');
        register('i', Modifier.NONE, Tone.DOT, 'ị', 'Ị');

        register('o', Modifier.NONE, Tone.NONE, 'o', 'O');
        register('o', Modifier.NONE, Tone.ACUTE, 'ó', 'Ó');
        register('o', Modifier.NONE, Tone.GRAVE, 'ò', 'Ò');
        register('o', Modifier.NONE, Tone.HOOK, 'ỏ', 'Ỏ');
        register('o', Modifier.NONE, Tone.TILDE, 'õ', 'Õ');
        register('o', Modifier.NONE, Tone.DOT, 'ọ', 'Ọ');

        register('o', Modifier.HAT, Tone.NONE, 'ô', 'Ô');
        register('o', Modifier.HAT, Tone.ACUTE, 'ố', 'Ố');
        register('o', Modifier.HAT, Tone.GRAVE, 'ồ', 'Ồ');
        register('o', Modifier.HAT, Tone.HOOK, 'ổ', 'Ổ');
        register('o', Modifier.HAT, Tone.TILDE, 'ỗ', 'Ỗ');
        register('o', Modifier.HAT, Tone.DOT, 'ộ', 'Ộ');

        register('o', Modifier.HORN, Tone.NONE, 'ơ', 'Ơ');
        register('o', Modifier.HORN, Tone.ACUTE, 'ớ', 'Ớ');
        register('o', Modifier.HORN, Tone.GRAVE, 'ờ', 'Ờ');
        register('o', Modifier.HORN, Tone.HOOK, 'ở', 'Ở');
        register('o', Modifier.HORN, Tone.TILDE, 'ỡ', 'Ỡ');
        register('o', Modifier.HORN, Tone.DOT, 'ợ', 'Ợ');

        register('u', Modifier.NONE, Tone.NONE, 'u', 'U');
        register('u', Modifier.NONE, Tone.ACUTE, 'ú', 'Ú');
        register('u', Modifier.NONE, Tone.GRAVE, 'ù', 'Ù');
        register('u', Modifier.NONE, Tone.HOOK, 'ủ', 'Ủ');
        register('u', Modifier.NONE, Tone.TILDE, 'ũ', 'Ũ');
        register('u', Modifier.NONE, Tone.DOT, 'ụ', 'Ụ');

        register('u', Modifier.HORN, Tone.NONE, 'ư', 'Ư');
        register('u', Modifier.HORN, Tone.ACUTE, 'ứ', 'Ứ');
        register('u', Modifier.HORN, Tone.GRAVE, 'ừ', 'Ừ');
        register('u', Modifier.HORN, Tone.HOOK, 'ử', 'Ử');
        register('u', Modifier.HORN, Tone.TILDE, 'ữ', 'Ữ');
        register('u', Modifier.HORN, Tone.DOT, 'ự', 'Ự');

        register('y', Modifier.NONE, Tone.NONE, 'y', 'Y');
        register('y', Modifier.NONE, Tone.ACUTE, 'ý', 'Ý');
        register('y', Modifier.NONE, Tone.GRAVE, 'ỳ', 'Ỳ');
        register('y', Modifier.NONE, Tone.HOOK, 'ỷ', 'Ỷ');
        register('y', Modifier.NONE, Tone.TILDE, 'ỹ', 'Ỹ');
        register('y', Modifier.NONE, Tone.DOT, 'ỵ', 'Ỵ');

        register('d', Modifier.NONE, Tone.NONE, 'd', 'D');
        register('d', Modifier.STROKE, Tone.NONE, 'đ', 'Đ');
    }

    private static void register(char base, Modifier mod, Tone tone, char lower, char upper) {
        CharData lowerData = new CharData(base, mod, tone, false);
        CharData upperData = new CharData(base, mod, tone, true);
        DECOMPOSE_MAP.put(lower, lowerData);
        DECOMPOSE_MAP.put(upper, upperData);
        COMPOSE_MAP.put(key(base, mod, tone, false), lower);
        COMPOSE_MAP.put(key(base, mod, tone, true), upper);
    }

    private static String key(char base, Modifier mod, Tone tone, boolean upper) {
        return Character.toLowerCase(base) + "_" + mod.name() + "_" + tone.name() + "_" + upper;
    }

    public static CharData decompose(char c) {
        return DECOMPOSE_MAP.get(c);
    }

    public static Character compose(char base, Modifier mod, Tone tone, boolean upper) {
        return COMPOSE_MAP.get(key(base, mod, tone, upper));
    }

    public static boolean isVowel(char c) {
        CharData data = decompose(c);
        if (data == null) {
            char lower = Character.toLowerCase(c);
            return lower == 'a' || lower == 'e' || lower == 'i' || lower == 'o' || lower == 'u' || lower == 'y';
        }
        return data.base != 'd';
    }
}
