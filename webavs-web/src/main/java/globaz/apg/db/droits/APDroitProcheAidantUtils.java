package globaz.apg.db.droits;

import ch.globaz.common.util.CaisseInfoPropertiesWrapper;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;

import java.util.ArrayList;
import java.util.List;

public class APDroitProcheAidantUtils {

    public static List<APPrestation> getPrestationForCareLeaveEventIdEtNssEnfant(String careLeaveEventId, String nss, BSession session) throws Exception {
        APEnfantPatManager enfantPatManager = new APEnfantPatManager();
        enfantPatManager.setSession(session);
        List<APPrestation> prestations = new ArrayList<>();
            enfantPatManager.find(BManager.SIZE_NOLIMIT);

            ArrayList<String> idDroits = new ArrayList<>();
            for(APEnfantPat enfantPat : enfantPatManager.<APEnfantPat>getContainerAsList()){
                if(nss.equals(enfantPat.getNoAVS())){
                    idDroits.add(enfantPat.getIdDroitPaternite());
                }
            }

            APPrestationManager prestationManager = new APPrestationManager();
            prestationManager.setSession(session);
            prestationManager.setForIdDroitIn(idDroits);
            prestationManager.find(BManager.SIZE_NOLIMIT);

            int nbJours = 0;

            for (APPrestation prestation : prestationManager.<APPrestation>getContainerAsList()) {
                if(isLastVersionDroit(prestation.getIdDroit(), session)){
                    APDroitProcheAidant apDroitProcheAidant = new APDroitProcheAidant();
                    apDroitProcheAidant.setIdDroit(prestation.getIdDroit());
                    apDroitProcheAidant.setSession(session);
                    apDroitProcheAidant.retrieve();

                    if(careLeaveEventId.equals(CaisseInfoPropertiesWrapper.noCaisseNoAgence() +
                            apDroitProcheAidant.getCareLeaveEventID())
                            && APGenreServiceAPG.ProcheAidant.getCodeSysteme().equals(
                            apDroitProcheAidant.getGenreService())){
                        prestations.add(prestation);
                    }
                }
            }
            return prestations;
    }

    private static boolean isLastVersionDroit(String idDroit, BSession session) throws Exception {
        APDroitProcheAidant d = new APDroitProcheAidant();
        d.setIdDroit(idDroit);
        d.setSession(session);
        d.retrieve();
        if(JadeStringUtil.isBlankOrZero(d.getIdDroitParent())){
            return true;
        }
        return idDroit.equals(getLastVersionDroit(d.getIdDroitParent(), session));
    }

    private static String getLastVersionDroit(String idDroitParent, BSession session) throws Exception{
        APDroitProcheAidantManager m = new APDroitProcheAidantManager();
        m.setForIdDroitParent(idDroitParent);
        m.setSession(session);
        m.find(BManager.SIZE_NOLIMIT);

        String latestDroitId = "00";
        for(APDroitProcheAidant d : m.<APDroitProcheAidant>getContainerAsList()){
            if(Integer.parseInt(d.getIdDroit()) > Integer.parseInt(latestDroitId)){
                latestDroitId = d.getIdDroit();
            }
        }
        return latestDroitId;
    }
}
