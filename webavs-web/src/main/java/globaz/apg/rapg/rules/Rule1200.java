package globaz.apg.rapg.rules;

import ch.globaz.common.properties.PropertiesException;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APPeriodeAPG;
import globaz.apg.db.droits.APPeriodeAPGManager;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.droits.APSituationProfessionnelleManager;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.exceptions.APWebserviceException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.globall.db.BManager;
import globaz.pyxis.constantes.IConstantes;


import java.util.List;

/**
 * Règle de validation des plausibilités Imposition à la source selon spécificités définies dans S220407_004.
 * Ne laisse pas passer un calcul sur un droit non imposé à la source payé à l'assuré dont la nationalité et
 * le pays du droit ne sont pas la Suisse.
 */
public class Rule1200 extends Rule{

    public Rule1200(String errorCode) {
        super(errorCode, false);
    }

    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException, APWebserviceException, PropertiesException {
        try {
            APDroitLAPG droit = new APDroitLAPG();
            droit.setSession(getSession());
            droit.setIdDroit(champsAnnonce.getIdDroit());
            droit.retrieve();
            if (IAPDroitLAPG.CS_ALLOCATION_DE_PATERNITE.equals(droit.getGenreService())) {
                droit.setIsSoumisImpotSource(isImpotSourcePeriodePaternite(droit.getIdDroit()));
            }
            if (Boolean.FALSE.equals(droit.getIsSoumisImpotSource())
                    && !IConstantes.ID_PAYS_SUISSE.equals(droit.getPays())
                    && checkAdressePaiementSetToAffilieForAnySituationProfessionnelle(champsAnnonce.getIdDroit())){
                return false;
            }
        } catch (Exception e) {
            throwRuleExecutionException(e);
        }
        return true;
    }
    private boolean checkAdressePaiementSetToAffilieForAnySituationProfessionnelle(String idDroit) throws Exception {
        APSituationProfessionnelleManager situationProfessionnelleManager = new APSituationProfessionnelleManager();
        situationProfessionnelleManager.setSession(getSession());
        situationProfessionnelleManager.setForIdDroit(idDroit);
        situationProfessionnelleManager.find(BManager.SIZE_USEDEFAULT);
        for(int i = 0; i<situationProfessionnelleManager.size(); i++) {
            if(Boolean.FALSE.equals(((APSituationProfessionnelle)situationProfessionnelleManager.getEntity(i)).getIsVersementEmployeur())){
                return true;
            }
        }
        return false;
    }

    private boolean isImpotSourcePeriodePaternite(String idDroit) throws Exception {
        APPeriodeAPGManager managerPeriodes = new APPeriodeAPGManager();
        managerPeriodes.setSession(getSession());
        managerPeriodes.setForIdDroit(idDroit);
        managerPeriodes.find(BManager.SIZE_USEDEFAULT);

        if (managerPeriodes.size() > 0) {
            List<Object> containerAsList = managerPeriodes.getContainerAsList();
            for (Object obj : containerAsList) {
                if (obj instanceof APPeriodeAPG) {
                    APPeriodeAPG periodeAPG = (APPeriodeAPG) obj;
                    if (!periodeAPG.getCantonImposition().equals("0")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
