package globaz.apg.itext.decompte.utils;

import globaz.apg.enums.APGenreServiceAPG;
import globaz.globall.db.BSession;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;

public class APPrestationLibelleCodeSystem {
    
    public static final String CODE_TYPE_JOURS_ISOLES = "51000001";
    public static final String CODE_USER_COMPCIAB = "COMPCIAB";
    
    public static String getLibelleJourIsole(BSession session, String idCodeSystem, String langue) throws Exception {
        APGenreServiceAPG genre = APGenreServiceAPG.resoudreGenreParCodeSystem(idCodeSystem);
        FWParametersSystemCodeManager mgr = new FWParametersSystemCodeManager();
        mgr.setSession(session);
        mgr.setForIdTypeCode(CODE_TYPE_JOURS_ISOLES);
        mgr.setForCodeUtilisateur(genre.getCodePourAnnonce());
        mgr.setForIdLangue(langue.toUpperCase().substring(0, 1));
        mgr.find(1);
        if (mgr.size() != 0) {
            FWParametersSystemCode codeSysteme = (FWParametersSystemCode) mgr.getEntity(0);
            return codeSysteme.getCurrentCodeUtilisateur().getLibelle();
        } else {
            return "";
        }
    }
    
    public static String getLibelleComplement(BSession session, String langue) throws Exception {
        FWParametersSystemCodeManager mgr = new FWParametersSystemCodeManager();
        mgr.setSession(session);
        mgr.setForCodeUtilisateur(CODE_USER_COMPCIAB);
        mgr.setForIdLangue(langue.toUpperCase().substring(0, 1));
        mgr.find(1);
        if (mgr.size() != 0) {
            FWParametersSystemCode codeSysteme = (FWParametersSystemCode) mgr.getEntity(0);
            return codeSysteme.getCurrentCodeUtilisateur().getLibelle();
        } else {
            return "";
        }
    }

}
