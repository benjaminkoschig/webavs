package globaz.cygnus.services.saisieDemande;

import globaz.cygnus.db.conventions.RFConventionJointAssConFouTsJointFournisseurJointMontantManager;
import globaz.globall.db.BSessionUtil;

public class RFFournisseurConventionneService {

    /**
     * Methode qui recherche si le fournisseur pass� en param�tre, est au b�n�fice d'une convention
     * 
     * @param vb
     * @return TRUE si convention trouv�e, FALSE si aucune convention trouv�e
     * @throws Exception
     */
    public Boolean getfournisseurConventionne(String idTiers, String codeTypeDeSoin, String codeSousTypeDeSoin)
            throws Exception {
        RFConventionJointAssConFouTsJointFournisseurJointMontantManager rfConvJointAssConFouTsJointFouJointMntMgr = new RFConventionJointAssConFouTsJointFournisseurJointMontantManager();

        rfConvJointAssConFouTsJointFouJointMntMgr.setSession(BSessionUtil.getSessionFromThreadContext());
        rfConvJointAssConFouTsJointFouJointMntMgr.setForCodeTypeDeSoin(codeTypeDeSoin);
        rfConvJointAssConFouTsJointFouJointMntMgr.setForCodeSousTypeDeSoin(codeSousTypeDeSoin);
        rfConvJointAssConFouTsJointFouJointMntMgr.setForActif(true);
        rfConvJointAssConFouTsJointFouJointMntMgr.setForIdFournisseur(idTiers);
        rfConvJointAssConFouTsJointFouJointMntMgr.changeManagerSize(0);
        rfConvJointAssConFouTsJointFouJointMntMgr.find();

        if (rfConvJointAssConFouTsJointFouJointMntMgr.size() > 0) {
            return true;
        }

        return false;
    }
}
