package globaz.ccvs.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class ConvertDSCantonVS {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String line = null;
        int count = 0;
        try {
            if ((args == null) || (args.length != 2) || (args[0].length() != 4)) {
                throw new IllegalArgumentException("Usage: ConvertDSCantonVS [annee] [filename]");
            }
            File fi = new File(args[1]);
            File fo = new File(args[1] + "_out");
            BufferedReader r = new BufferedReader(new FileReader(fi));
            BufferedWriter w = new BufferedWriter(new FileWriter(fo));
            char[] c = new char[160];
            while (r.read(c) != -1) {
                count++;
                line = new String(c);
                if (line.length() < 160) {
                    System.out.println("Ligne ignorée " + count + ": " + line);
                }
                String affilie = line.substring(0, 10);
                String nom = line.substring(41, 61);
                String nss = line.substring(61, 72);
                String mdebut = line.substring(74, 76);
                String mfin = line.substring(78, 80);
                String revenu = line.substring(101, 107) + "." + line.substring(107, 109);
                String navs = line.substring(136, 147);
                if ((affilie != null) && !affilie.trim().equals("")) {
                    StringBuffer sb = new StringBuffer();
                    sb.append(affilie.substring(0, 6) + "00" + affilie.substring(6));
                    if ((nss != null) && (nss.length() == 11) && nss.startsWith("-")) {
                        if ("-          ".equals(nss)) {
                            sb.append("00000000000");
                        } else {
                            sb.append(nss);
                        }
                    } else if ((navs != null) && (navs.length() == 11)) {
                        sb.append(navs);
                    } else {
                        sb.append("00000000000");
                    }
                    sb.append(nom);
                    sb.append(args[0]);
                    sb.append(mdebut);
                    sb.append(mfin);
                    sb.append(revenu);
                    sb.append('\n');
                    w.write(sb.toString());
                } else {
                    System.out.println("Ligne ignorée " + count + ": " + line);
                }
            }
            w.flush();
            w.close();
            r.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(count + ">" + line);
            System.exit(-1);
        }
        System.exit(0);
    }
}
