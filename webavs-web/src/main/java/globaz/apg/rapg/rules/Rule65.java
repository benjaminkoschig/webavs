package globaz.apg.rapg.rules;

import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APDroitLAPGManager;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.droits.APSituationProfessionnelleManager;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.apg.properties.APParameter;
import globaz.globall.db.BManager;
import globaz.globall.db.FWFindParameter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import org.safehaus.uuid.Logger;

import java.math.BigDecimal;

/**
 * <strong>Règles de validation des plausibilités Pandémie</br>
 * Description :</strong></br>
 * Si le champ « serviceType » = 403
 * et le revenu n'est pas compris dans l'interval PANINREVMI et PANINREVMA -> Erreur </br>
 * <strong>Champs concerné(s) :</strong></br>
 *
 */
public class Rule65 extends Rule {

    private static final String dateMax = "16.09.2020";

    /**
     * @param errorCode
     */
    public Rule65(String errorCode) {
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
        boolean isTestUniquementMin = false;

        if (serviceType.equals(APGenreServiceAPG.IndependantPerteGains.getCodePourAnnonce())
        || serviceType.equals(APGenreServiceAPG.IndependantLimitationActivite.getCodePourAnnonce())) {
            try {

                APDroitLAPGManager mng = new APDroitLAPGManager();
                mng.setSession(getSession());
                mng.setForIdDroit(champsAnnonce.getIdDroit());
                mng.find(BManager.SIZE_USEDEFAULT);
                String dateDebut = mng.size() > 0 ? ((APDroitLAPG) mng.get(0)).getDateDebutDroit() : champsAnnonce.getStartOfPeriod();

                // le max ne doit plus être vérifié aprés le 16.09.2020
                if(JadeDateUtil.isDateBefore(dateMax, dateDebut)) {
                    isTestUniquementMin = true ;
                }


                final APSituationProfessionnelleManager manager = new APSituationProfessionnelleManager();
                manager.setSession(getSession());
                manager.setForIdDroit(champsAnnonce.getIdDroit());
                manager.find(BManager.SIZE_NOLIMIT);

                for (int idSitPro = 0; idSitPro < manager.size(); ++idSitPro) {
                    APSituationProfessionnelle sitPro = (APSituationProfessionnelle) manager.get(idSitPro);
                    if(sitPro.getIsIndependant()
                            && (JadeStringUtil.isBlankOrZero(sitPro.getRevenuIndependant())
                            || !isDansInterval(new BigDecimal(sitPro.getRevenuIndependant()), isTestUniquementMin))) {
                        return false;
                    }
                }
            } catch(Exception e){
                Logger.logError("Error ");
            }

        }
        return true;
    }

    private boolean isDansInterval(BigDecimal revenu, boolean isTestUniquementMin) throws Exception {
        BigDecimal min = new BigDecimal(
                FWFindParameter.findParameter(getSession().getCurrentThreadTransaction(), "1",
                        APParameter.REVENU_INDEPENDANT_MIN.getParameterName(),
                        "0", "", 0));
        BigDecimal max = new BigDecimal(
                FWFindParameter.findParameter(getSession().getCurrentThreadTransaction(), "1",
                        APParameter.REVENU_INDEPENDANT_MAX.getParameterName(),
                        "0", "", 0));

        if(isTestUniquementMin){
            return revenu.compareTo(min) >= 0;
        } else {
        return revenu.compareTo(min) >= 0
                && revenu.compareTo(max) <= 0 ;
        }

    }

}
