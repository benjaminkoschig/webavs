package globaz.corvus.utils;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeManager;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationInvalidite;
import java.util.ArrayList;
import java.util.List;

public class REDemandeUtils {

    /**
     * Recherche la dernière rente invalidité du tiers c. a. d. celle qui n'a pas de date de fin ou celle qui à la date
     * de fin la plus récente. Cette méthode est utilisée dans le cadre de la copie d'une demande pour la création d'une
     * prestation transitoire
     * 
     * @param idTiers
     */
    public static RERenteAccordee getDerniereRenteInvaliditeDuTiers(BSession session, BITransaction transaction,
            String idTiersRequerant) throws Exception {
        RERenteAccordee rente50DuTiers = null;

        RERenteAccordeeManager renteAccordeeManager = new RERenteAccordeeManager();
        renteAccordeeManager.setSession(session);
        renteAccordeeManager.setForIdTiersBeneficiaire(idTiersRequerant);
        renteAccordeeManager.find(transaction, BManager.SIZE_NOLIMIT);
        if (renteAccordeeManager.size() == 0) {
            throw new Exception(REDemandeUtils.translate(session,
                    "PRESTATION_TRANSITOIRE_AUCUNE_RENTE_TROUVEE_POUR_TIERS_REQUERANT"));
        }

        List<RERenteAccordee> listRentes50 = new ArrayList<RERenteAccordee>();
        for (int ctr = 0; ctr < renteAccordeeManager.size(); ctr++) {
            RERenteAccordee ra = (RERenteAccordee) renteAccordeeManager.get(ctr);
            if ((ra.getCodePrestation() != null)
                    && PRCodePrestationInvalidite.isPrestationPrincipale(ra.getCodePrestation())) {
                // Ok, c'est bien une prestation invalidité principale
                if (IREPrestationAccordee.CS_ETAT_DIMINUE.equals(ra.getCsEtat())
                        || IREPrestationAccordee.CS_ETAT_VALIDE.equals(ra.getCsEtat())
                        || IREPrestationAccordee.CS_ETAT_PARTIEL.equals(ra.getCsEtat())) {
                    listRentes50.add(ra);
                }
            }
        }
        // Si il n'y a aucune rente 50 ce n'est pas normal donc error
        if (listRentes50.size() == 0) {
            throw new Exception(REDemandeUtils.translate(session,
                    "PRESTATION_TRANSITOIRE_AUCUNE_RENTE_INVALIDITE_TROUVEE_POUR_TIERS_REQUERANT"));
        }
        // S'il y en a qu'une, on prend celle là
        else if (listRentes50.size() == 1) {
            rente50DuTiers = listRentes50.get(0);
        }
        // S'il y en a plusieurs on cherche la dernière
        else {
            // 1er passage : on en cherche une qui n'a pas de date de fin de droit (format MM.AAAA).
            // en même temps on test s'il n'y en a pas 2 sans date de fin
            for (RERenteAccordee rente50 : listRentes50) {
                if (!JadeDateUtil.isGlobazDateMonthYear(rente50.getDateFinDroit())) {
                    if (rente50DuTiers == null) {
                        rente50DuTiers = rente50;
                    } else {
                        throw new Exception(REDemandeUtils.translate(session,
                                "PRESTATION_TRANSITOIRE_PLUSIEURS_RENTE_INVALIDITE_TROUVEE_SANS_DATE_FIN_DROIT"));
                    }
                }
            }
            if (rente50DuTiers == null) {
                // Si on en a pas trouver sans date de fin on cherche celle avec
                // la date de fin la plus récente
                String date = null;
                for (RERenteAccordee rente50 : listRentes50) {
                    if (date == null) {
                        date = rente50.getDateFinDroit();
                    } else {
                        if (JadeDateUtil.isDateBefore(rente50.getDateFinDroit(), date)) {
                            date = rente50.getDateFinDroit();
                        }
                    }
                }
                // Ok, en théorie notre date doit contenir la date la plus récente. En pratique aussi..
                if (date == null) {
                    throw new Exception(REDemandeUtils.translate(session,
                            "PRESTATION_TRANSITOIRE_ERREUR_INTERNE_IMPOSSIBLE_RECUPERER_DATE_FIN_DROIT"));
                } else {
                    // On va recherche la rente 50 avec cette date de fin de droit
                    for (RERenteAccordee rente50 : listRentes50) {
                        if (date.equals(rente50.getDateFinDroit())) {
                            rente50DuTiers = rente50;
                        }
                    }
                }
            }
        }
        return rente50DuTiers;
    }

    /**
     * Insère le code cas spécial à la première position libre. Ce qui veut dire : On à 5 position pour insérer un code
     * cas spécial, on vas chercher la premier position vide et renseigner ce code Si le code cas spécial est déjà
     * présent, il ne sera pas inséré une 2ème fois
     * 
     * @param renteAccordee
     *            la rente dans laquelle doit être inséré le code cas spécial 84
     */
    public static void insertCodeCasSpecial(RERenteAccordee renteAccordee, int codeCasSpecial) throws Exception {
        String ccs = String.valueOf(codeCasSpecial);
        if (JadeStringUtil.isBlankOrZero(renteAccordee.getCodeCasSpeciaux1())) {
            renteAccordee.setCodeCasSpeciaux1(ccs);
        } else if (ccs.equals(renteAccordee.getCodeCasSpeciaux1())) {
            return;
        } else if (JadeStringUtil.isBlankOrZero(renteAccordee.getCodeCasSpeciaux2())) {
            renteAccordee.setCodeCasSpeciaux2(ccs);
        } else if (ccs.equals(renteAccordee.getCodeCasSpeciaux2())) {
            return;
        } else if (JadeStringUtil.isBlankOrZero(renteAccordee.getCodeCasSpeciaux3())) {
            renteAccordee.setCodeCasSpeciaux3(ccs);
        } else if (ccs.equals(renteAccordee.getCodeCasSpeciaux3())) {
            return;
        } else if (JadeStringUtil.isBlankOrZero(renteAccordee.getCodeCasSpeciaux4())) {
            renteAccordee.setCodeCasSpeciaux4(ccs);
        } else if (ccs.equals(renteAccordee.getCodeCasSpeciaux4())) {
            return;
        } else if (JadeStringUtil.isBlankOrZero(renteAccordee.getCodeCasSpeciaux5())) {
            renteAccordee.setCodeCasSpeciaux4(ccs);
        } else if (ccs.equals(renteAccordee.getCodeCasSpeciaux5())) {
            return;
        } else {
            // Toute les position sont déjà utilisées et le code n'est pas présent ==> Exception
            throw new Exception("All 'codeCasSpeciaux' position are used.. Can't not add 'codeCasSpecial' ["
                    + codeCasSpecial + "]");
        }
    }

    /**
     * Return the translated label from the provided textKey
     * 
     * @param session
     * @param textKey
     * @return
     */
    private static String translate(BSession session, String textKey) {
        return session.getLabel(textKey);
    }
}
