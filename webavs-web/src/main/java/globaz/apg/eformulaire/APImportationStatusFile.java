package globaz.apg.eformulaire;

import globaz.osiris.file.paiement.exception.LineNotFoundException;
import globaz.prestation.api.IPRDemande;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class APImportationStatusFile {
    private static final String FORMAT_TITLE_INFO  = "- Informations : ";
    private static final String FORMAT_TITLE_ERROR = "-  Erreurs : ";
    private static final String INCONNU = "Inconnu";
    public static final String LINE_SEP = "\n";

    @Getter
    private final List<String> informations = new ArrayList<>();
    @Getter
    private final List<String> errors = new ArrayList<>();
    @Getter
    private final String type;
    @Getter
    private final String fileName;
    @Getter
    private final String nss;
    @Setter @Getter
    private String fileFullPath;
    @Setter @Getter
    private String nom = INCONNU;
    @Setter @Getter
    private String prenom = INCONNU;
    @Setter @Getter
    private String numeroAffilie = INCONNU;
    @Setter @Getter
    private boolean succeed;


    public String buildStatusMessage(){
        StringBuilder builder = new StringBuilder();
        if(!StringUtils.equals(fileName, StringUtils.EMPTY)) {
            builder.append(String.format("- Nom du fichier trait�: %s %s", fileName, LINE_SEP));
            if(!StringUtils.equals(fileName, StringUtils.EMPTY)) {
                builder.append(String.format("- Assur� concern�: %s - %s %s %s", nss, nom, prenom, LINE_SEP));
                builder.append(String.format("- N� affili�: %s %s", numeroAffilie, LINE_SEP));
                if (!informations.isEmpty()) {
                    builder.append(FORMAT_TITLE_INFO + LINE_SEP);
                    informations.forEach(info -> builder.append(info + LINE_SEP));
                }
                if (!errors.isEmpty()) {
                    builder.append(FORMAT_TITLE_ERROR + LINE_SEP);
                    errors.forEach(error -> builder.append(error + LINE_SEP));
                }
            } else {
                builder.append(String.format("- Aucun assur� n'a �t� trait�. %s", LINE_SEP));
            }
        }
        else{
            builder.append("Erreur lors du processus d'importation. Aucun fichier n'a �t� trait�.");
        }
        return builder.toString();
    }

    public String getEMailObjectCaisse(){
        StringBuilder builder = new StringBuilder(getFormattedType());
        if(succeed){
            builder.append(getObjectFichierTraite());
        }else{
            builder.append(getObjectFichierNonTraite());
        }
        return builder.toString();
    }

    private String getFormattedType(){
        String apgNom = "APG ";
        if(type.equals(IPRDemande.CS_TYPE_MATERNITE)){
            return apgNom + " Maternit�";
        } else if(type.equals(IPRDemande.CS_TYPE_PATERNITE)) {
            return apgNom + " Paternit�";
        }
        return apgNom;
    }

    private String getObjectFichierTraite(){
        return String.format(" - Demande trait� par la caisse pour l'assur� %s", nss);
    }

    private String getObjectFichierNonTraite(){
        if(!StringUtils.equals(fileName, StringUtils.EMPTY)){
            if(!StringUtils.equals(nss, StringUtils.EMPTY)){
                return String.format(" - Demande non trait� par la caisse pour l'assur� %s", nss);
            }
            return String.format(" - Demande non trait� par la caisse pour le fichier %s", fileName);
        }
        return " - Aucune demande trait� par la caisse.";

    }
}
