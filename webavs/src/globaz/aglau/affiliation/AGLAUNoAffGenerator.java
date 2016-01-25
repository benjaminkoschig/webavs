package globaz.aglau.affiliation;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.affiliation.INumberGenerator;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.translation.CodeSystem;

public class AGLAUNoAffGenerator implements INumberGenerator {

    @Override
    public String generateBeforeAdd(AFAffiliation affiliation) throws Exception {
        if (affiliation == null || JadeStringUtil.isEmpty(affiliation.getIdTiers())) {
            return "";
        }
        if (!affiliation.isNew()) {
            return affiliation.getAffilieNumero();
        }
        // ancien tiers de 100'001 à 200'000
        // nouveau tiers de 200'001 à 999'999
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
        } else if ((CodeSystem.TYPE_AFFILI_TSE.equals(genreAffiliation))
                || (CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE.equals(genreAffiliation))) {
            result += "2";
        } else if ((CodeSystem.TYPE_AFFILI_NON_ACTIF.equals(genreAffiliation))
                || (CodeSystem.TYPE_AFFILI_BENEF_AF.equals(genreAffiliation))) {
            if (CodeSystem.BRANCHE_ECO_ETUDIANTS.equals(affiliation.getBrancheEconomique())) {
                result += "5";
            } else if (CaisseHelperFactory.BRANCHE_ECO_ASSISTE.equals(affiliation.getBrancheEconomique())) {
                result += "9";
            } else {
                result += "4";
            }
        } else if (CodeSystem.TYPE_AFFILI_LTN.equals(genreAffiliation)) {
            result += "0";
        }
        IFormatData affilieFormater = ((AFApplication) affiliation.getSession().getApplication()).getAffileFormater();
        if (affilieFormater != null) {
            return affilieFormater.format(result);
        } else {
            return result;
        }
    }

    @Override
    public String generateBeforeDisplay(AFAffiliation affiliation) throws Exception {
        return "";
    }

    @Override
    public boolean isEditable(AFAffiliation affiliation) throws Exception {
        return true;
    }

}
