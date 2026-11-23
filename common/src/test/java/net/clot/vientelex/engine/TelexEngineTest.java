package net.clot.vientelex.engine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TelexEngineTest {

    private String typeSequence(String input) {
        StringBuilder fullText = new StringBuilder();
        int wordStart = 0;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isWhitespace(c) || ",.!?;:/-()[]{}".indexOf(c) >= 0) {
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

    @Test
    public void testBasicVowelModifiers() {
        assertEquals("â", typeSequence("aa"));
        assertEquals("ă", typeSequence("aw"));
        assertEquals("ê", typeSequence("ee"));
        assertEquals("ô", typeSequence("oo"));
        assertEquals("ơ", typeSequence("ow"));
        assertEquals("ư", typeSequence("uw"));
        assertEquals("ư", typeSequence("w"));
        assertEquals("đ", typeSequence("dd"));
    }

    @Test
    public void testToneMarks() {
        assertEquals("má", typeSequence("mas"));
        assertEquals("mà", typeSequence("maf"));
        assertEquals("mả", typeSequence("mar"));
        assertEquals("mã", typeSequence("max"));
        assertEquals("mạ", typeSequence("maj"));
    }

    @Test
    public void testUndo() {
        assertEquals("aa", typeSequence("aaa"));
        assertEquals("ee", typeSequence("eee"));
        assertEquals("oo", typeSequence("ooo"));
        assertEquals("dd", typeSequence("ddd"));
        assertEquals("aw", typeSequence("aww"));
        assertEquals("w", typeSequence("ww"));
        assertEquals("ww", typeSequence("www"));
        assertEquals("www", typeSequence("wwww"));
        assertEquals("w", typeSequence("uww"));
        assertEquals("sw", typeSequence("sww"));
        assertEquals("what", typeSequence("what"));
        assertEquals("write", typeSequence("write"));

        assertEquals("toans", typeSequence("toanss"));
        assertEquals("mas", typeSequence("mass"));
        assertEquals("class", typeSequence("classs"));
    }

    @Test
    public void testComplexVietnameseWords() {
        assertEquals("tiếng", typeSequence("tieengs"));
        assertEquals("Việt", typeSequence("Vieetj"));
        assertEquals("đường", typeSequence("dduowngf"));
        assertEquals("người", typeSequence("nguowif"));
        assertEquals("toán", typeSequence("toans"));
        assertEquals("hoàn", typeSequence("hoanf"));
        assertEquals("hóa", typeSequence("hoas"));
        assertEquals("thúy", typeSequence("thuys"));
        assertEquals("quán", typeSequence("quans"));
        assertEquals("giá", typeSequence("gias"));
        assertEquals("mía", typeSequence("mias"));
        assertEquals("của", typeSequence("cuar"));
        assertEquals("mượn", typeSequence("muonwj"));
        assertEquals("giới", typeSequence("gioiws"));
        assertEquals("giới", typeSequence("gioisw"));
        assertEquals("giới", typeSequence("giowis"));
        assertEquals("trời", typeSequence("troiwf"));
        assertEquals("chơi", typeSequence("choiw"));
        assertEquals("nơi", typeSequence("noiw"));
        assertEquals("tôi", typeSequence("toio"));
        assertEquals("tối", typeSequence("toios"));
        assertEquals("cây", typeSequence("caya"));
        assertEquals("câu", typeSequence("caua"));
        assertEquals("kêu", typeSequence("keue"));
        assertEquals("gửi", typeSequence("guiwr"));
        assertEquals("mưa", typeSequence("muaw"));
        assertEquals("chưa", typeSequence("chuaw"));
    }

    @Test
    public void testFullSentences() {
        assertEquals("Xin chào Việt Nam", typeSequence("Xin chaof Vieetj Nam"));
        assertEquals("Tôi đang chơi Minecraft", typeSequence("Tooi ddang choi Minecraft"));
    }
}
