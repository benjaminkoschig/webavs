package globaz.corvus.api.arc.downloader;

import ch.globaz.common.util.Dates;
import globaz.jade.client.util.JadeStringUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

public class REAnnoncesDateAugmentation5153 {

    public static final String SEPARATEUR = "/";
    @Setter
    @Getter
    private String label;
    @Setter
    @Getter
    private String datetraitement;
    @Setter
    @Getter
    private String value;

    public static String retrieveDateAugmentationFromValue(String value) {
        return splitValue(value).get(0);
    }

    public static String retrieveDateTraitementFromValue(String value) {
        return splitValue(value).size() > 1 ? splitValue(value).get(1) : "";
    }

    private static List<String> splitValue(String value) {
        return Arrays.asList(value.split(SEPARATEUR));
    }

    public void convertAnnonce5153() {
        String date = Dates.formatSwiss(Dates.toDateFromDb(value));
        label = date+" - "+datetraitement+" - ("+label+")";
        if(!JadeStringUtil.isEmpty(datetraitement)) {
            value = value + SEPARATEUR + datetraitement;
        }
    }
}
