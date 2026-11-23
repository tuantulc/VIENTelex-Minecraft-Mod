package net.clot.vientelex.engine;

public class TelexTestRunner {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=== RUNNING TELEX ENGINE TESTS ===");

        test("Basic â", "aa", "â");
        test("Basic ă", "aw", "ă");
        test("Basic ê", "ee", "ê");
        test("Basic ô", "oo", "ô");
        test("Basic ơ", "ow", "ơ");
        test("Basic ư (uw)", "uw", "ư");
        test("Basic ư (w)", "w", "ư");
        test("Basic đ", "dd", "đ");

        test("Tone sắc", "mas", "má");
        test("Tone huyền", "maf", "mà");
        test("Tone hỏi", "mar", "mả");
        test("Tone ngã", "max", "mã");
        test("Tone nặng", "maj", "mạ");

        test("Undo â -> aa", "aaa", "aa");
        test("Undo ê -> ee", "eee", "ee");
        test("Undo ô -> oo", "ooo", "oo");
        test("Undo đ -> dd", "ddd", "dd");
        test("Undo ă -> aw", "aww", "aw");
        test("Undo w -> ww -> w", "ww", "w");
        test("Undo w -> www -> ww", "www", "ww");
        test("Undo w -> wwww -> www", "wwww", "www");
        test("Undo ư (uw) -> uww -> uw", "uww", "uw");
        test("Meme uwu", "uwwu", "uwu");
        test("Undo sw -> sww -> sw", "sww", "sw");
        test("English what", "what", "what");
        test("English write", "write", "write");
        test("Undo tone má -> mas", "mass", "mas");
        test("Undo tone toan -> toans", "toanss", "toans");
        test("Direct English word class", "class", "class");
        test("Undo english word pass", "passs", "pass");

        test("Late D: dend -> đen", "dend", "đen");
        test("Late D undo: dendd -> dend", "dendd", "dend");
        test("Late D: did -> đi", "did", "đi");
        test("Late D undo: didd -> did", "didd", "did");
        test("Late D: dad -> đa", "dad", "đa");
        test("Late D undo: dadd -> dad", "dadd", "dad");
        test("Late D: dangd -> đang", "dangd", "đang");

        test("tiếng", "tieengs", "tiếng");
        test("Việt", "Vieetj", "Việt");
        test("đường", "dduowngf", "đường");
        test("người", "nguowif", "người");
        test("toán", "toans", "toán");
        test("hoàn", "hoanf", "hoàn");
        test("hoá", "hoas", "hoá");
        test("thuý", "thuys", "thuý");
        test("quán", "quans", "quán");
        test("giá", "gias", "giá");
        test("mía", "mias", "mía");
        test("của", "cuar", "của");
        test("mượn", "muonwj", "mượn");
        test("luôn", "luoon", "luôn");
        test("khuyên", "khuyeen", "khuyên");
        test("khuyến", "khuyeens", "khuyến");
        test("chào", "chaof", "chào");
        test("hoài", "hoaif", "hoài");
        test("hoái", "hoaifs", "hoái");
        test("muối (muoois)", "muoois", "muối");
        test("muói (muois)", "muois", "muói");
        test("người (nguowif)", "nguowif", "người");
        test("người (nguoifw)", "nguoifw", "người");
        test("tiền (tieenf)", "tieenf", "tiền");

        test("giới (gioiws)", "gioiws", "giới");
        test("giới (gioisw)", "gioisw", "giới");
        test("giới (giowis)", "giowis", "giới");
        test("trời (troiwf)", "troiwf", "trời");
        test("chơi (choiw)", "choiw", "chơi");
        test("nơi (noiw)", "noiw", "nơi");
        test("tôi (toio)", "toio", "tôi");
        test("tối (toios)", "toios", "tối");
        test("cây (caya)", "caya", "cây");
        test("câu (caua)", "caua", "câu");
        test("kêu (keue)", "keue", "kêu");
        test("gửi (guiwr)", "guiwr", "gửi");
        test("mưa (muaw)", "muaw", "mưa");
        test("chưa (chuaw)", "chuaw", "chưa");
        test("Undo chơi (choiww)", "choiww", "choiw");
        test("Undo mưa (muaww)", "muaww", "muaw");

        test("Free typing: cốc (cocos)", "cocos", "cốc");
        test("Free typing: nêm (neme)", "neme", "nêm");
        test("Free typing: đất (ddatas)", "ddatas", "đất");

        test("Edge case: haha (not hâh)", "haha", "haha");
        test("Edge case: gacha (not gâch)", "gacha", "gacha");
        test("Edge case: bigger (not biggẻ)", "bigger", "bigger");
        test("Edge case: dater (not datẻ/dảte)", "dater", "dater");
        test("Edge case: water (not watẻ)", "water", "water");
        test("Edge case: teams (not téam)", "teams", "teams");
        test("Edge case: craft (not crát)", "craft", "craft");
        test("Edge case: player (not playẻ)", "player", "player");
        test("Edge case: super (not supẻ)", "super", "super");
        test("Checked tone: actf (no huyền on t)", "actf", "actf");
        test("Checked tone: chatr (no hỏi on t)", "chatr", "chatr");

        test("Sentence 1", "Xin chaof Vieetj Nam", "Xin chào Việt Nam");
        test("Sentence 2", "Cafi mod gox tieengs Vieetj", "Cài mod gõ tiếng Việt");

        System.out.println("\n=== TEST RESULTS: " + passed + " PASSED, " + failed + " FAILED ===");
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void test(String name, String input, String expected) {
        String actual = typeSequence(input);
        if (expected.equals(actual)) {
            System.out.println(" [PASS] " + name + " -> \"" + actual + "\"");
            passed++;
        } else {
            System.out.println(" [FAIL] " + name + " | Expected: \"" + expected + "\", Actual: \"" + actual + "\"");
            failed++;
        }
    }

    private static String typeSequence(String input) {
        TelexEngine.reset();
        StringBuilder fullText = new StringBuilder();
        int wordStart = 0;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isWhitespace(c) || ",.!?;:/-()[]{}".indexOf(c) >= 0) {
                TelexEngine.reset();
                fullText.append(c);
                wordStart = fullText.length();
            } else {
                String currentWord = fullText.substring(wordStart);
                TelexResult result = TelexEngine.process(currentWord, c);
                if (result.isHandled()) {
                    fullText.replace(wordStart, fullText.length(), result.getNewWord());
                } else {
                    fullText.append(c);
                }
            }
        }
        return fullText.toString();
    }
}
