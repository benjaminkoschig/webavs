/*
 * Créé le 15 janv. 07
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.ccvd.affiliation;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.affiliation.INumberGenerator;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.translation.CodeSystem;
import ch.globaz.naos.business.constantes.AFAffiliationType;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CCVDNoAffGenerator implements INumberGenerator {
    @Override
    public String generateBeforeAdd(AFAffiliation affiliation) throws Exception {
        if ((affiliation == null) || JadeStringUtil.isEmpty(affiliation.getIdTiers())) {
            return "";
        }
        if (!affiliation.isNew()) {
            return affiliation.getAffilieNumero();
        }

        // tiers depuis 1'000'000
        String result = affiliation.getIdTiers();
        String genreAffiliation = affiliation.getTypeAffiliation();

        if (CodeSystem.TYPE_AFFILI_EMPLOY.equals(genreAffiliation)) {
            if (CodeSystem.BRANCHE_ECO_PERSONNEL_MAISON.equals(affiliation.getBrancheEconomique())) {
                result += "6";
            } else {
                result += "1";
            }
            if (CodeSystem.TYPE_AFFILI_EMPLOY_D_F.equals(genreAffiliation)) {

                result += "7";
            }
        } else if (CodeSystem.TYPE_AFFILI_INDEP.equals(genreAffiliation)) {
            if (CaisseHelperFactory.BRANCHE_ECO_ASSISTE.equals(affiliation.getBrancheEconomique())) {
                result += "8";
            } else {
                result += "3";
            }
        } else if (CodeSystem.TYPE_AFFILI_TSE.equals(genreAffiliation)) {
            result += "2";
        } else if (CodeSystem.TYPE_AFFILI_NON_ACTIF.equals(genreAffiliation)) {
            if (CodeSystem.BRANCHE_ECO_ETUDIANTS.equals(affiliation.getBrancheEconomique())) {
                result += "5";
            } else if (CaisseHelperFactory.BRANCHE_ECO_ASSISTE.equals(affiliation.getBrancheEconomique())) {
                result += "9";
            } else {
                result += "4";
            }
        } else if (CodeSystem.TYPE_AFFILI_LTN.equals(genreAffiliation)) {
            result += "0";
        } else if (CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE.equals(genreAffiliation)) {
            result += "7";
        } else if (AFAffiliationType.isTypeCAPCGASEmployeur(genreAffiliation)) {
            result += "16";
        } else if (AFAffiliationType.isTypeCAPCGASIndependant(genreAffiliation)) {
            result += "36";
        }

        IFormatData affilieFormater = ((AFApplication) affiliation.getSession().getApplication()).getAffileFormater();
        if (affilieFormater != null) {
            result = affilieFormater.format(result);
        }

        if (!AFAffiliationType.isTypeCAPCGAS(genreAffiliation)) {
            // Il faut encore voir si il y a déjà une affiliation pour ce tiers
            result += getDernierChiffre(affiliation, result);
        }

        return result;
    }

    @Override
    public String generateBeforeDisplay(AFAffiliation affiliation) throws Exception {
        return "";
    }

    private String getDernierChiffre(AFAffiliation affiliation, String result) throws Exception {
        /*
         * AFAffiliationManager mgr = new AFAffiliationManager(); mgr.setSession(affiliation.getSession());
         * mgr.setLikeAffilieNumero(result); mgr.find(affiliation.getSession().getCurrentThreadTransaction()); return
         * String.valueOf(mgr.size());
         */
        return "0";
    }

    @Override
    public boolean isEditable(AFAffiliation affiliation) throws Exception {
        return true;
    }
}
