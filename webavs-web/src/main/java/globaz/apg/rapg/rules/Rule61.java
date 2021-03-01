package globaz.apg.rapg.rules;

import globaz.apg.ApgServiceLocator;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitAPGJointTiersManager;
import globaz.apg.db.droits.APPeriodeComparable;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.interfaces.APDroitAvecParent;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.apg.properties.APParameter;
import globaz.globall.db.BManager;
import globaz.globall.db.FWFindParameter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.utils.PRDateUtils;
import org.safehaus.uuid.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * <strong>Règles de validation des plausibilités Pandémie</br>
 * Description :</strong></br>
 * Si le champ « serviceType » = 401
 * et nb jours > > la plage de valeur PANQUARNJO -> erreur </br>
 * <strong>Champs concerné(s) :</strong></br>
 *
 * @author mpe
 */
public class Rule61 extends Rule {

    /**
     * @param errorCode
     */
    public Rule61(String errorCode) {
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

        if (serviceType.equals(APGenreServiceAPG.Quarantaine.getCodePourAnnonce())
            ||serviceType.equals(APGenreServiceAPG.Quarantaine_17_09_20.getCodePourAnnonce())) {
            try {
                int jourMax = Integer.parseInt(FWFindParameter.findParameter(getSession().getCurrentThreadTransaction(), "1", APParameter.QUARANTAINE_JOURS_MAX.getParameterName(), champsAnnonce.getStartOfPeriod(), "", 0));

                List<APPeriodeComparable> listPeriode = getPeriodes(champsAnnonce.getIdDroit());

                int jourAutrePresta = ApgServiceLocator.getEntityService().getTotalJourAutreDroit(getSession(), champsAnnonce.getIdDroit());
                int delai = jourAutrePresta == 0 ? getDelai(listPeriode, APGenreServiceAPG.resoudreGenreParCodeAnnonce(champsAnnonce.getServiceType()).getCodeSysteme()) : 0;

                int nbJourSoldes = 0;
                resolveDateFin(listPeriode, delai);

                // ajout d'un délai selon les services
                if (jourAutrePresta == 0) {
                    delaiSelonService(listPeriode, delai);
                }

                for (APPeriodeComparable periode : listPeriode) {
                    if (!JadeStringUtil.isBlankOrZero(periode.getNbrJours())) {
                        nbJourSoldes += Integer.valueOf(periode.getNbrJours());
                    } else {
                        nbJourSoldes += PRDateUtils.getNbDayBetween(periode.getDateDebutPeriode(), periode.getDateFinPeriode()) + 1;
                    }

                    if (nbJourSoldes > jourMax ) {
                        return false;
                    }
                }

                //check les periodes pour le même type de service
                String startOfPeriod = champsAnnonce.getStartOfPeriod();
                String endOfPeriod = champsAnnonce.getEndOfPeriod();
                String nss = champsAnnonce.getInsurant();

                validNotEmpty(nss, "NSS");
                testDateNotEmptyAndValid(startOfPeriod, "startOfPeriod");
                testDateNotEmptyAndValid(endOfPeriod, "endOfPeriod");

                // Voir les prestations en même temps
                APDroitAPGJointTiersManager mgr = new APDroitAPGJointTiersManager();
                mgr.setForDroitContenuDansDateDebut(startOfPeriod);
                mgr.setForDroitContenuDansDateFin(endOfPeriod);
                // Ne pas traiter les droits en état refusé ou transféré
                List<String> etatIndesirable = new ArrayList<String>();
                etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_REFUSE);
                etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_TRANSFERE);
                mgr.setForCsGenreService(APGenreServiceAPG.resoudreGenreParCodeAnnonce(serviceType).getCodeSysteme());
                mgr.setLikeNumeroAvs(nss);

                mgr.setSession(getSession());
                List<APDroitAvecParent> droitsTries = null;

                mgr.find(BManager.SIZE_NOLIMIT);
                // On récupère tous les droits y compris le droit courant (celui que l'utilisateur est en train de saisir)
                List<APDroitAvecParent> tousLesDroits = mgr.getContainer();
                // On élimine tous les droits parents qui sont corrigés par un droit
                droitsTries = skipDroitParent(tousLesDroits);

                // On supprime le droit courant de la liste des droits triés et droit pas du même service
                List<APDroitAvecParent> droitsTries2 = new ArrayList<>();
                for (APDroitAvecParent d : droitsTries) {
                    if (!JadeStringUtil.isBlankOrZero(d.getIdDroit()) && d.getIdDroit().equals(champsAnnonce.getIdDroit())) {
                        continue;
                    }
                    droitsTries2.add(d);
                }

                // Si on ne trouve pas
                if (droitsTries2.size() == 0) {
                    return true;
                }

                if (hasPrestationEnConflit(champsAnnonce.getStartOfPeriod(), champsAnnonce.getEndOfPeriod(), tousLesDroits)){
                    return false;
                }

            } catch (Exception e) {
                Logger.logError("Error ");
            }
        }

        return true;
    }

    private void resolveDateFin(List<APPeriodeComparable> listPeriode, int delai) throws Exception {
        // pour chaque période
        for (APPeriodeComparable periode : listPeriode) {
            // si pas de date fin mettre la fin du mois en cours pour le calcul
            if (JadeStringUtil.isEmpty(periode.getDateFinPeriode())) {
                int jourMax = Integer.parseInt(FWFindParameter.findParameter(getSession().getCurrentThreadTransaction(), "1", APParameter.QUARANTAINE_JOURS_MAX.getParameterName(), "0", "", 0));
                String dateFin = JadeDateUtil.addDays(periode.getDateDebutPeriode(), delai + jourMax - 1);
                periode.setDateFinPeriode(dateFin);
            }
        }
    }
}
