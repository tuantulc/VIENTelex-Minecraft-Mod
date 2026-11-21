package net.clot.vientelex.engine;

import net.clot.vientelex.VietnameseIME;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class InteractiveConsole {

    public static void main(String[] args) throws Exception {
        System.out.println("==================================================");
        System.out.println("    VI-EN TELEX TYPING - INTERACTIVE CONSOLE      ");
        System.out.println("==================================================");
        System.out.println("Huong dan:");
        System.out.println("- Go thu bat ky tu hoac cau theo kieu Telex.");
        System.out.println("- Vi du: 'Tooi ddang choi Minecraft', 'Xin chaof'");
        System.out.println("- Go 'exit' hoac 'quit' de thoat.");
        System.out.println("==================================================\n");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

        while (true) {
            System.out.print("Gõ Telex: ");
            String line = reader.readLine();
            if (line == null || line.equalsIgnoreCase("exit") || line.equalsIgnoreCase("quit")) {
                break;
            }

            String resultText = "";
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                VietnameseIME.ProcessResult res = VietnameseIME.processInput(resultText, resultText.length(), c);
                if (res != null) {
                    resultText = res.newText;
                } else {
                    resultText += c;
                }
            }

            System.out.println("-> Ket qua: " + resultText + "\n");
        }
        System.out.println("Tam biet!");
    }
}
