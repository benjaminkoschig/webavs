package globaz.apg.rapg.rules;

import ch.globaz.common.util.Dates;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitAPGJointTiers;
import globaz.apg.db.droits.APDroitAPGJointTiersManager;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.interfaces.APDroitAvecParent;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeStringUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * <strong>R?gles de validation des plausibilit?s RAPG</br> Description :</strong></br> Si le champ ? serviceType ? = 11
 * (?cole de recrues) et le champ ? numberOfDays ? > 145 (sans que la p?riode chevauche No?l) ou 159 (si la p?riode
 * chevauche No?l) -> erreur </br><strong>Champs concern?(s) :</strong></br> </br> Info compl?mentaire : il faut
 * rechercher tous le droit de la personne car c'est l'ensemble des jours d'?cole de recrue que l'on veut contr?ller
 * 
 * @author lga
 */
public class Rule504 extends Rule {

    private static final int NB_JOUR_MAX = 124;
    private static LocalDate dateFinRule = Dates.toDate("31.12.2021");

    /**
     * @param errorCode
     */
    public Rule504(String errorCode) {
        super(errorCode, true);
    }

    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException {
        /**
         * Note : Normalement les service 11 ?cole de recrue sont saisit sur une seule carte APG. Mais, pas acquis de
         * conscience et pour pr?venir les cas particuliers, on effectue une requ?te pour remonter tous les droits du
         * tiers avec genre de service 11
         */

        String serviceType = champsAnnonce.getServiceType();
        int typeAnnonce = getTypeAnnonce(champsAnnonce);
        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
        }

        if (APGenreServiceAPG.MilitaireEcoleDeRecrue.getCodePourAnnonce().equals(serviceType)
            && !Dates.toDate(champsAnnonce.getStartOfPeriod()).isAfter(dateFinRule)) {
            String nss = champsAnnonce.getInsurant();
            validNotEmpty(nss, "NSS");

            List<String> forIn = new ArrayList<String>();
            forIn.add(APGenreServiceAPG.MilitaireEcoleDeRecrue.getCodeSysteme());

            APDroitAPGJointTiersManager manager = new APDroitAPGJointTiersManager();
            manager.setSession(getSession());
            manager.setForCsGenreServiceIn(forIn);
            manager.setLikeNumeroAvs(nss);

            // Ne pas traiter les droits en ?tat refus? ou transf?r?
            List<String> etatIndesirable = new ArrayList<String>();
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_REFUSE);
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_TRANSFERE);
            manager.setForEtatDroitNotIn(etatIndesirable);

            List<APDroitAvecParent> droitsTries = null;
            try {
                manager.find();
                List<APDroitAvecParent> tousLesDroits = manager.getContainer();
                droitsTries = skipDroitParent(tousLesDroits);
            } catch (Exception e) {
                throwRuleExecutionException(e);
            }

            int nombreJoursEffectue = 0;
            int nombreJoursMax = NB_JOUR_MAX;

            // On va trouver tous les droits, m?me celui qu'on vient de cr?er,
            // on additionne tous les jours sold?es
            for (int i = 0; i < droitsTries.size(); i++) {
                APDroitAPGJointTiers droit = (APDroitAPGJointTiers) droitsTries.get(i);

                // On cumul les jours sold?s
                if (!JadeStringUtil.isBlank(droit.getNbrJourSoldes())
                        && droit.getIdDroit().equals(champsAnnonce.getIdDroit())) {
                    nombreJoursEffectue += Integer.parseInt(droit.getNbrJourSoldes());
                }
            }

            if (nombreJoursEffectue > nombreJoursMax) {
                return false;
            }
        }
        return true;
    }
}
