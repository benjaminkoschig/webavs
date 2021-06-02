package globaz.apg.eformulaire;

import globaz.osiris.file.paiement.exception.LineNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class APImportationStatusFile {
    private static String FORMAT_TITLE       = "- ";
    private static String FORMAT_TITLE_INFO  = ">> Informations : ";
    private static String FORMAT_TITLE_ERROR = ">> Erreurs : ";
    private static String FORMAT_LINE        = "+++ ";
    public static String LINE_SEP = "\n";
    @Getter
    private final List<String> informations = new ArrayList<>();
    @Getter
    private final List<String> errors = new ArrayList<>();
    @Setter @Getter
    private String fileName;
    @Setter @Getter
    private String nss;
    @Setter @Getter
    private boolean succeed;

    public String BuildStatusMessage(){
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%s Traitement Assuré : %s, fichier : %s %s", FORMAT_TITLE, nss, fileName, LINE_SEP));
        if(!informations.isEmpty()) {
            builder.append(FORMAT_TITLE_INFO + LINE_SEP);
            informations.forEach(info -> builder.append(FORMAT_LINE + info + LINE_SEP));
        }
        if(!errors.isEmpty()) {
            builder.append(FORMAT_TITLE_ERROR + LINE_SEP);
            errors.forEach(error -> builder.append(FORMAT_LINE + error + LINE_SEP));
        }
        return builder.toString();
    }
}
