package ch.globaz.naos.ree.tools;

import globaz.prestation.utils.PRStringFormatter;
import org.apache.commons.lang.time.DurationFormatUtils;

public class ProtocolUtils {

    public static String formatDuration(long duration) {
        return DurationFormatUtils.formatDurationWords(duration, true, true);
    }

    public static String indent(String value) {
        return PRStringFormatter.indentRight(value, 50);
    }
}
