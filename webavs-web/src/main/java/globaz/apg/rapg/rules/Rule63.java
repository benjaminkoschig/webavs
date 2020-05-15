package globaz.apg.rapg.rules;

import globaz.apg.ApgServiceLocator;
import globaz.apg.db.droits.APPeriodeComparable;
import globaz.apg.db.droits.APSitProJointEmployeur;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.apg.properties.APParameter;
import globaz.globall.db.FWFindParameter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.utils.PRDateUtils;
import org.safehaus.uuid.Logger;

import java.util.Calendar;
import java.util.List;

/**
 * <strong>Règles de validation des plausibilités Pandémie</br>
 * Description :</strong></br>
 * Si le champ « serviceType » = 400
 * et indépendant et nb jours > > la plage de valeur PANGARDIND (30) -> erreur </br>
 * <strong>Champs concerné(s) :</strong></br>
 *
 * @author mpe
 */
public class Rule63 extends Rule {

    /**
     * @param errorCode
     */
    public Rule63(String errorCode) {
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

        if (serviceType.equals(APGenreServiceAPG.GardeParentale.getCodePourAnnonce())
            || serviceType.equals(APGenreServiceAPG.GardeParentaleHandicap.getCodePourAnnonce())) {
            try {
                if (!isIndependant(champsAnnonce)) {
                    return true;
                }

                int jourMax = Integer.parseInt(FWFindParameter.findParameter(getSession().getCurrentThreadTransaction(), "1", APParameter.GARDE_PARENTAL_INDE_JOURS_MAX.getParameterName(), "0", "", 0));

                List<APPeriodeComparable> listPeriode = getPeriodes(champsAnnonce.getIdDroit());

                int jourAutrePresta = ApgServiceLocator.getEntityService().getTotalJourAutreDroit(getSession(), champsAnnonce.getIdDroit());
                int delai = jourAutrePresta == 0 ? getDelai(listPeriode, APGenreServiceAPG.resoudreGenreParCodeAnnonce(champsAnnonce.getServiceType()).getCodeSysteme()) : 0;

                int nbJourSoldes = 0;
                resolveDateFin(listPeriode);

                // ajout d'un délai selon les services
                if (jourAutrePresta == 0) {
                    delaiSelonService(listPeriode, delai);
                }

                if (testPeriode(jourMax, listPeriode, nbJourSoldes)) return false;

            } catch (Exception e) {
                Logger.logError("Error ");
            }
        }

        return true;
    }

    private boolean testPeriode(int jourMax, List<APPeriodeComparable> listPeriode, int nbJourSoldes) {
        for (APPeriodeComparable periode : listPeriode) {
            if (!JadeStringUtil.isBlankOrZero(periode.getNbrJours())) {
                nbJourSoldes += Integer.valueOf(periode.getNbrJours());
            } else {
                nbJourSoldes += PRDateUtils.getNbDayBetween(periode.getDateDebutPeriode(), periode.getDateFinPeriode()) + 1;
            }

            if (nbJourSoldes > jourMax) {
                return true;
            }
        }
        return false;
    }

    private boolean isIndependant(APChampsAnnonce champsAnnonce) throws Exception {
        boolean isIndependant = false;
        List<APSitProJointEmployeur> list = ApgServiceLocator.getEntityService().getSituationProfJointEmployeur(getSession(), null, champsAnnonce.getIdDroit());
        for (APSitProJointEmployeur employeur : list) {
            if(employeur.getIndependant()){
                isIndependant = true;
            }
        }

        return isIndependant;
    }

    private void resolveDateFin(List<APPeriodeComparable> listPeriode) {
        // pour chaque période
        for (APPeriodeComparable periode : listPeriode) {
            // si pas de date fin mettre la fin du mois en cours pour le calcul
            if (JadeStringUtil.isEmpty(periode.getDateFinPeriode())) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
                periode.setDateFinPeriode(JadeDateUtil.getGlobazFormattedDate(cal.getTime()));
            }
        }
    }

}
