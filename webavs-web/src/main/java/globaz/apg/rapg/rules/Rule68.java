package globaz.apg.rapg.rules;

import globaz.apg.ApgServiceLocator;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APDroitLAPGManager;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.droits.APSituationProfessionnelleManager;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.interfaces.SituationProfessionnelle;
import globaz.apg.module.calcul.salaire.APSalaireJournalierCalculateur;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.apg.properties.APParameter;
import globaz.globall.db.BManager;
import globaz.globall.db.FWFindParameter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import org.safehaus.uuid.Logger;

import java.math.BigDecimal;

/**
 * <strong>R?gles de validation des plausibilit?s Pand?mie</br>
 * Description :</strong></br>
 * Si le champ ? serviceType ? = 403
 * et le revenu n'est pas compris dans l'interval PANINREVMI et PANINREVMA -> Erreur </br>
 * <strong>Champs concern?(s) :</strong></br>
 *
 */
public class Rule68 extends Rule {

    private static final String dateMax = "16.09.2020";

    /**
     * @param errorCode
     */
    public Rule68(String errorCode) {
        super(errorCode, true);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.apg.businessimpl.plausibilites.Rule#check(ch.globaz.apg.business.models.plausibilites.ChampsAnnonce)
     */
    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException {
        String serviceType = champsAnnonce.getServiceType();

        if (serviceType.equals(APGenreServiceAPG.SalarieEvenementiel.getCodePourAnnonce())) {
            try {

                APDroitLAPGManager mng = new APDroitLAPGManager();
                mng.setSession(getSession());
                mng.setForIdDroit(champsAnnonce.getIdDroit());
                mng.find(BManager.SIZE_USEDEFAULT);
                String dateDebut = mng.size() > 0 ? ((APDroitLAPG) mng.get(0)).getDateDebutDroit() : champsAnnonce.getStartOfPeriod();

                // ne doit plus ?tre v?rifi? apr?s le 16.09.2020
                if(JadeDateUtil.isDateBefore(dateMax, dateDebut)) {
                    return true;
                }

                final APSituationProfessionnelleManager manager = new APSituationProfessionnelleManager();
                manager.setSession(getSession());
                manager.setForIdDroit(champsAnnonce.getIdDroit());
                manager.find(BManager.SIZE_NOLIMIT);

                for (int idSitPro = 0; idSitPro < manager.size(); ++idSitPro) {
                    APSituationProfessionnelle sitPro = (APSituationProfessionnelle) manager.get(idSitPro);
                    BigDecimal salaireJournalier = APSalaireJournalierCalculateur.getSalaireJournalierMoyen(sitPro, false);
                    if(!isDansInterval(BigDecimal.valueOf(360).multiply(salaireJournalier))){
                        return false;
                    };
                }
            } catch(Exception e){
                Logger.logError("Error lors de l'execution de la rule 68 :"+e.getMessage());
            }

        }
        return true;
    }

    private boolean isDansInterval(BigDecimal revenu) throws Exception {
        BigDecimal min = new BigDecimal(
                FWFindParameter.findParameter(getSession().getCurrentThreadTransaction(), "1",
                        APParameter.SALARIE_EVENEMENTIEL_REVENU_MIN.getParameterName(),
                        "0", "", 0));
        BigDecimal max = new BigDecimal(
                FWFindParameter.findParameter(getSession().getCurrentThreadTransaction(), "1",
                        APParameter.SALARIE_EVENEMENTIEL_REVENU_MAX.getParameterName(),
                        "0", "", 0));

        return revenu.compareTo(min) >= 0
                && revenu.compareTo(max) <= 0 ;

    }

}
