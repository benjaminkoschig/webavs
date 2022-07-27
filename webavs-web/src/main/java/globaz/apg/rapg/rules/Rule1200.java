package globaz.apg.rapg.rules;

import ch.globaz.common.properties.PropertiesException;
import ch.globaz.pyxis.domaine.constantes.CodeIsoPays;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.droits.APSituationProfessionnelleManager;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.exceptions.APWebserviceException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.globall.db.BManager;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIPays;
import globaz.pyxis.db.tiers.TITiers;

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
            if(Boolean.FALSE.equals(droit.getIsSoumisImpotSource())){
                TITiers tiers = new TITiers();
                tiers.setSession(getSession());
                tiers.setIdTiers(droit.loadDemande().getIdTiers());
                tiers.retrieve();

                TIPays paysNationalite = new TIPays();
                paysNationalite.setSession(getSession());
                paysNationalite.setIdPays(tiers.getIdPays());
                paysNationalite.retrieve();
                if(!CodeIsoPays.SUISSE.getCodeIso().equals(paysNationalite.getCodeIso())
                        && !IConstantes.ID_PAYS_SUISSE.equals(droit.getPays())
                        && hasEmployeAsEmployeurAdressePaiement(champsAnnonce.getIdDroit())){
                    return false;
                }
            }
        } catch (Exception e) {
            throwRuleExecutionException(e);
        }
        return true;
    }

    private boolean hasEmployeAsEmployeurAdressePaiement(String idDroit) throws Exception {
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
}
