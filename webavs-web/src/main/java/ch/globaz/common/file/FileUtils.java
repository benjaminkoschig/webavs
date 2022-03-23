package ch.globaz.common.file;

import java.io.File;
import java.text.DecimalFormat;

public abstract class FileUtils {
    public static String OCTECTS = "O";
    public static String KILO_OCTECTS = "Ko";
    public static String MEGA_OCTECTS = "Mo";
    public static String GIGA_OCTECTS = "Go";
    public static String TERA_OCTECTS = "To";

    public static String formatTaille(File file, boolean round) {
        return formatTaille(file.length(), round);
    }

    public static String formatTaille(byte[] file, boolean round) {
        return formatTaille(file.length / 8, round);
    }

    private static String formatTaille(long octectSizefile, boolean round) {
        StringBuilder formatSizeFile = new StringBuilder();
        int index = 0;
        double sizeFile = octectSizefile;
        String pattern = "###.##";

        double calculSize = sizeFile / 1024;
        while(calculSize > 1) {
            index++;
            sizeFile = calculSize;
            calculSize = calculSize / 1024;
        }
        if (round) {
            pattern = "###";
        }
        DecimalFormat df = new DecimalFormat(pattern);
        formatSizeFile.append(df.format(sizeFile));


        switch (index) {
            case 1:
                formatSizeFile.append(KILO_OCTECTS);
                break;
            case 2:
                formatSizeFile.append(MEGA_OCTECTS);
                break;
            case 3:
                formatSizeFile.append(GIGA_OCTECTS);
                break;
            case 4:
                formatSizeFile.append(TERA_OCTECTS);
                break;
            default:
                formatSizeFile.append(OCTECTS);
        }

        return formatSizeFile.toString();
    }
}
