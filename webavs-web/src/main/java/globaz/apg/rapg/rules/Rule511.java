package globaz.apg.rapg.rules;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.application.APApplication;
import globaz.apg.db.droits.APDroitMaterniteJointTiers;
import globaz.apg.db.droits.APDroitMaterniteJointTiersManager;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.interfaces.APDroitAvecParent;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.apg.properties.APParameter;
import globaz.globall.db.FWFindParameter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.utils.PRDateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <strong>R�gles de validation des plausibilit�s RAPG</br> Description :</strong></br>
 * Champ � serviceType � = 90 </br> Une maternit� doit durer entre 1 et 98 jours ou en cas de prolongation entre 112 et 154 jours.
 * </br><strong>Champs concern�(s) :</strong></br>
 * 
 * @author lga
 */
public class Rule511 extends Rule {

    /**
     * @param errorCode
     */
    public Rule511(String errorCode) {
        super(errorCode, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.apg.businessimpl.plausibilites.Rule#check(ch.globaz.apg.business.models.plausibilites.ChampsAnnonce)
     */
    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException {
        String serviceType = champsAnnonce.getServiceType();
        String startOfPeriod = champsAnnonce.getStartOfPeriod();
        String endOfPeriod = champsAnnonce.getEndOfPeriod();
        String nss = champsAnnonce.getInsurant();
        int typeAnnonce = getTypeAnnonce(champsAnnonce);
        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
        }

        List<String> services = new ArrayList<String>();
        services.add(APGenreServiceAPG.Maternite.getCodePourAnnonce());

        if (services.contains(serviceType)) {
            int totalDeJours = 0;
            validNotEmpty(nss, "NSS");
            testDateNotEmptyAndValid(startOfPeriod, "startOfPeriod");
            testDateNotEmptyAndValid(endOfPeriod, "endOfPeriod");

            // Voir les prestations en m�me temps
            APDroitMaterniteJointTiersManager mgr = new APDroitMaterniteJointTiersManager();
            mgr.setForDroitContenuDansDateDebut(startOfPeriod);
            mgr.setForDroitContenuDansDateFin(endOfPeriod);
            mgr.setLikeNumeroAvs(nss);

            // Ne pas traiter les droits en �tat refus� ou transf�r�
            List<String> etatIndesirable = new ArrayList<String>();
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_REFUSE);
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_TRANSFERE);
            mgr.setForEtatDroitNotIn(etatIndesirable);
            mgr.setSession(getSession());

            List<APDroitAvecParent> droitsSansParents = null;
            try {
                mgr.find();
                List<APDroitAvecParent> tousLesDroits = mgr.getContainer();
                droitsSansParents = skipDroitParent(tousLesDroits);
            } catch (Exception e) {
                throwRuleExecutionException(e);
            }

            try {
                int matExtMin = Integer.parseInt(FWFindParameter.findParameter(getSession().getCurrentThreadTransaction(), "1", APParameter.MATERNITE_EXT_JOUR_MIN.getParameterName(), champsAnnonce.getStartOfPeriod(), "", 0));
                int dureeMatFederale = Integer.parseInt(getSession().getApplication().getProperty(APApplication.PROPERTY_DROIT_MAT_DUREE_JOURS));

                for (Object d : droitsSansParents) {
                    APDroitMaterniteJointTiers droit = (APDroitMaterniteJointTiers) d;
                    if (!JadeStringUtil.isBlankOrZero(droit.getUneDateDebutPeriode())
                            && droit.getIdDroit().equals(champsAnnonce.getIdDroit())) {
                        String dateDebutMat = droit.getUneDateDebutPeriode();
                        String dateFinMat = droit.getUneDateFinPeriode();
                        totalDeJours += PRDateUtils.getNbDayBetween(dateDebutMat, dateFinMat) + 1;
                    }
                }

                if (totalDeJours > dureeMatFederale && totalDeJours < matExtMin) {
                    return false;
                }
            } catch (Exception e) {
                throw new APRuleExecutionException(e);
            }

        }

        return true;
    }
}
