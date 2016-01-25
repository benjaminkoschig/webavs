package globaz.apg.rapg.rules;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitAPGJointTiers;
import globaz.apg.db.droits.APDroitAPGJointTiersManager;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.interfaces.APDroitAvecParent;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * <strong>R�gles de validation des plausibilit�s RAPG</br> Description :</strong></br> P�riode de contr�le : une ann�e
 * civile</br> Si, au cours d'une m�me ann�e civile (p. ex. 2012), les codes 10 et 41 ou les codes 20, 40 et 41 sont
 * utilis�s en association, la caisse doit examiner le cas.</br> 1) code 10 et code 41 -> erreur (� examiner) </br>2)
 * code 41 et code 10 -> erreur (� examiner) </br>3) code 20 et codes 40, 41 -> erreur (� examiner) </br>4) code 40 et
 * codes 20 -> erreur (� examiner) </br>5) code 41 et codes 20 -> erreur (� examiner) </br><strong>Champs concern�(s)
 * :</strong></br>
 * 
 * @author lga
 */
public class Rule503 extends Rule {

    public Rule503(String errorCode) {
        super(errorCode, true);
    }

    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException {
        /**
         * Note : on ne se pose pas la question de savoir si on est � cheval sur 2 ann�es car ce n'est pas possible. Si
         * une p�riode chevauche 2 ann�es, elle sera coup� en 2 � la date du 31.12
         */
        String serviceType = champsAnnonce.getServiceType();
        String nss = champsAnnonce.getInsurant();
        String startOfPeriod = champsAnnonce.getStartOfPeriod();
        int typeAnnonce = getTypeAnnonce(champsAnnonce);
        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
        }
        if ("10".equals(serviceType) || "20".equals(serviceType) || "40".equals(serviceType)
                || "41".equals(serviceType)) {
            testDateNotEmptyAndValid(startOfPeriod, "startOfPeriod");
            validNotEmpty(nss, "nss");

            // R�cup�ration des codes syst�mes pour les genre de services recherch�s
            String csGenreService_10 = APGenreServiceAPG.MilitaireServiceNormal.getCodeSysteme();
            String csGenreService_20 = APGenreServiceAPG.ProtectionCivileServiceNormale.getCodeSysteme();
            String csGenreService_40 = APGenreServiceAPG.ServiceCivilNormal.getCodeSysteme();
            String csGenreService_41 = APGenreServiceAPG.ServiceCivilTauxRecrue.getCodeSysteme();

            // Genre de service recherch�
            List<String> forIn = new ArrayList<String>();
            forIn.add(csGenreService_10);
            forIn.add(csGenreService_20);
            forIn.add(csGenreService_40);
            forIn.add(csGenreService_41);

            // Ne pas traiter les droits en �tat refus� ou transf�r�
            List<String> etatIndesirable = new ArrayList<String>();
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_REFUSE);
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_TRANSFERE);

            String annee = startOfPeriod.substring(6);
            String dateDebutPeriode = "01.01." + annee;
            String dateFinPeriode = "31.12." + annee;

            APDroitAPGJointTiersManager manager = new APDroitAPGJointTiersManager();
            manager.setSession(getSession());
            manager.setForCsGenreServiceIn(forIn);
            manager.setForEtatDroitNotIn(etatIndesirable);
            manager.setLikeNumeroAvs(nss);
            manager.setForDroitContenuDansDateDebut(dateDebutPeriode);
            manager.setForDroitContenuDansDateFin(dateFinPeriode);

            try {
                // On recherche tous les droits li�s � ce NSS pour la p�riode donn�e
                manager.find();
                // On � r�cup�r� tous les droits avec leurs parents
                List<APDroitAvecParent> droitsAvecParents = manager.getContainer();
                // On vas filter les parents des droits
                List<APDroitAvecParent> droitsSansParents = skipDroitParent(droitsAvecParents);
                List<String> genreServicesTrouves = new ArrayList<String>();
                // On rescence tous les genre de services pr�sent dans la p�riode
                for (int i = 0; i < droitsSansParents.size(); i++) {
                    APDroitAPGJointTiers droit = (APDroitAPGJointTiers) droitsSansParents.get(i);
                    if (!JadeStringUtil.isEmpty(droit.getCsGenreService())) {
                        if (!genreServicesTrouves.contains(droit.getCsGenreService())) {
                            genreServicesTrouves.add(droit.getCsGenreService());
                        }
                    }
                }
                if (genreServicesTrouves.contains(csGenreService_10)
                        && genreServicesTrouves.contains(csGenreService_41)) {
                    return false;
                }
                if (genreServicesTrouves.contains(csGenreService_20)
                        && genreServicesTrouves.contains(csGenreService_40)) {
                    return false;
                }
                if (genreServicesTrouves.contains(csGenreService_20)
                        && genreServicesTrouves.contains(csGenreService_41)) {
                    return false;
                }

            } catch (Exception e) {
                throwRuleExecutionException(e);
            }
        }
        return true;
    }
}
