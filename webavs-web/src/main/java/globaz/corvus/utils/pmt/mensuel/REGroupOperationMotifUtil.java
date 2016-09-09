package globaz.corvus.utils.pmt.mensuel;

import globaz.globall.db.BSession;
import globaz.prestation.enums.codeprestation.PRTypeCodePrestation;
import globaz.prestation.utils.codeprestation.PRCodePrestationResolver;
import java.util.HashSet;
import java.util.Set;
import ch.globaz.common.util.prestations.MotifVersementUtil;

public class REGroupOperationMotifUtil {

    private String nss;
    private String nomPrenom;
    private String refPmt;
    private String datePmt;

    private Set<PRTypeCodePrestation> multiGenres;

    private String codeISOLangue;

    public REGroupOperationMotifUtil(String nss, String datePmt, String nom, String prenom, String refPmt,
            String genrePrestation, String codeISOLangue) {
        this.nss = nss;
        nomPrenom = nom + " " + prenom;
        this.refPmt = refPmt;
        this.datePmt = datePmt;
        this.codeISOLangue = codeISOLangue;
        multiGenres = new HashSet<PRTypeCodePrestation>();
        addCodePrest(genrePrestation);
    }

    public String getMotif(BSession session) {
        return MotifVersementUtil.formatPaiementMensuel(nss, nomPrenom, refPmt, resolveGenrePrestForMotif(session),
                datePmt);
    }

    private String resolveGenrePrestForMotif(BSession session) {
        // les afficher dans un ordre donne
        StringBuilder result = new StringBuilder();
        boolean hasPrevious = false;
        if (multiGenres.contains(PRTypeCodePrestation.INVALIDITE)
                || multiGenres.contains(PRTypeCodePrestation.VIEILLESSE)
                || multiGenres.contains(PRTypeCodePrestation.SURVIVANT)) {
            result.append(MotifVersementUtil.getTranslatedLabelFromIsolangue(codeISOLangue, "RENTE_AVS_AI", session));
            hasPrevious = true;
        }
        if (multiGenres.contains(PRTypeCodePrestation.API)) {
            if (hasPrevious) {
                result.append("/");
            }
            result.append(MotifVersementUtil.getTranslatedLabelFromIsolangue(codeISOLangue, "RENTE_API", session));
            hasPrevious = true;
        }
        if (multiGenres.contains(PRTypeCodePrestation.PC)) {
            if (hasPrevious) {
                result.append("/");
            }
            result.append(MotifVersementUtil.getTranslatedLabelFromIsolangue(codeISOLangue, "RENTE_PC_AVS_AI", session));
            hasPrevious = true;
        }
        if (multiGenres.contains(PRTypeCodePrestation.RFM)) {
            if (hasPrevious) {
                result.append("/");
            }
            result.append(MotifVersementUtil
                    .getTranslatedLabelFromIsolangue(codeISOLangue, "RENTE_RFM_AVS_AI", session));
            hasPrevious = true;
        }

        return result.toString();
    }

    public void addCodePrest(String codePrestation) {
        multiGenres.add(PRCodePrestationResolver.getGenreDePrestation(codePrestation));

    }

}
