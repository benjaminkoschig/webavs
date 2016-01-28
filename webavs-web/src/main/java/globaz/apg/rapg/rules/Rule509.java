package globaz.apg.rapg.rules;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitLAPGJointTiersManager;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.interfaces.APDroitAvecParent;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * <strong>R�gles de validation des plausibilit�s RAPG</br> Description :</strong></br> Double paiement : Si les
 * p�riodes d'indemnisation de deux APG consolid�es ou plus se recoupent -> erreur </br><strong>Champs concern�(s)
 * :</strong></br>
 * 
 * @author lga
 */
public class Rule509 extends Rule {

    /**
     * @param errorCode
     */
    public Rule509(String errorCode) {
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
        String startOfPeriod = champsAnnonce.getStartOfPeriod();
        String endOfPeriod = champsAnnonce.getEndOfPeriod();
        String nss = champsAnnonce.getInsurant();

        validNotEmpty(nss, "NSS");
        testDateNotEmptyAndValid(startOfPeriod, "startOfPeriod");
        testDateNotEmptyAndValid(endOfPeriod, "endOfPeriod");

        // Voir les prestations en m�me temps
        APDroitLAPGJointTiersManager mgr = new APDroitLAPGJointTiersManager();
        mgr.setForDroitContenuDansDateDebut(startOfPeriod);
        mgr.setForDroitContenuDansDateFin(endOfPeriod);
        // Ne pas traiter les droits en �tat refus� ou transf�r�
        List<String> etatIndesirable = new ArrayList<String>();
        etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_REFUSE);
        etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_TRANSFERE);
        mgr.setForEtatDroitNotIn(etatIndesirable);
        mgr.setLikeNumeroAvs(nss);
        // Ne pas exclure le droit courant lors de la recherche sinon la m�thode 'skipDroitParent() renverra un r�sultat
        // faux
        // if (!JadeStringUtil.isEmpty(champsAnnonce.getIdDroit())) {
        // mgr.getForIdDroitNotIn().add(champsAnnonce.getIdDroit());
        // }

        mgr.setSession(getSession());
        List<APDroitAvecParent> droitsTries = null;
        try {
            mgr.find();
            // On r�cup�re tous les droits y compris le droit courant (celui que l'utilisateur est en train de saisir)
            List<APDroitAvecParent> tousLesDroits = mgr.getContainer();
            // On �limine tous les droits parents qui sont corrig�s par un droit
            droitsTries = skipDroitParent(tousLesDroits);

            // On supprime le droit courant de la liste des droits tri�s
            List<APDroitAvecParent> droitsTries2 = new ArrayList<APDroitAvecParent>();
            for (APDroitAvecParent d : droitsTries) {
                if (!JadeStringUtil.isBlankOrZero(d.getIdDroit()) && d.getIdDroit().equals(champsAnnonce.getIdDroit())) {
                    continue;
                }
                droitsTries2.add(d);
            }

            // Si on ne trouve
            if (droitsTries2.size() == 0) {
                return true;
            }
            if (hasPrestationEnConflit(startOfPeriod, endOfPeriod, droitsTries2)) {
                return false;
            }
        } catch (Exception e) {
            throwRuleExecutionException(e);
        }
        return true;
    }

}
