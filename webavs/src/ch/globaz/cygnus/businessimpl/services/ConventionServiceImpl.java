package ch.globaz.cygnus.businessimpl.services;

import globaz.cygnus.db.conventions.RFConventionJointAssConFouTsJointFournisseurJointMontantManager;
import globaz.globall.db.BSessionUtil;
import ch.globaz.cygnus.business.services.ConventionService;

public class ConventionServiceImpl implements ConventionService {

    @Override
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
