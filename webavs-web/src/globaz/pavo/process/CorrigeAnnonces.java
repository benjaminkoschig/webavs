package globaz.pavo.process;

import java.io.File;
import java.io.FileWriter;

public class CorrigeAnnonces {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String line = null;
        String toSave = null;
        try {
            String file = "CIANNCEN20121130120120Online";
            java.io.BufferedReader fileIn = new java.io.BufferedReader(new java.io.FileReader("d://trax311/" + file));
            FileWriter out = null;

            File f = new File("d://trax311/" + file + "finale");

            line = fileIn.readLine();
            System.out.println(line.length());
            String lineFirst = line.substring(0, 120);
            String lineSecond = line.substring(120, 240);
            out = new FileWriter(f);
            line = line.substring(241);
            out.write(lineFirst + "\n");
            out.write(lineSecond + "\n");
            int toCute = line.indexOf("3201023000");
            boolean wantRetour = false;
            while ((toCute > 0) && line.startsWith("201023")) {
                toSave = "3" + line.substring(0, toCute);
                line = line.substring(toCute + 1);
                if (toSave.startsWith("3201023000-0551919372")) {
                    System.out.println("Erreur : ");
                }
                if (toSave.length() == 61) {
                    toSave = toSave.substring(0, 23) + toSave.substring(24);
                } else if (toSave.length() == 62) {
                    System.out.println(toSave);
                }
                if (wantRetour) {
                    out.write(toSave + "\n");
                    wantRetour = false;
                } else {
                    out.write(toSave);
                    wantRetour = true;
                }
                toCute = line.indexOf("3201023000");

            }
            if (!wantRetour) {
                out.write("                                                            ");
            }
            out.flush();
            out.close();

        } catch (Exception e) {
            System.out.println(toSave);
            e.printStackTrace();
        }

    }
}
