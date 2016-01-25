package ch.globaz.amal.businessimpl.utils.parametres;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.context.JadeThread;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.Map;
import java.util.TreeMap;
import ch.globaz.amal.business.exceptions.models.parametreannuel.ParametreAnnuelException;
import ch.globaz.amal.business.models.parametreannuel.SimpleParametreAnnuelSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

public class ParametresAnnuels {
    private ContainerParametres containerParametres = null;

    private String csTypeParametreAnnuel = new String();
    private TreeMap<String, String> parametresAnnuels = new TreeMap<String, String>();

    public ParametresAnnuels() {
    }

    public String getCsTypeParametreAnnuel() {
        return csTypeParametreAnnuel;
    }

    public String getFormatedLastValue(boolean wantQuote, boolean wantDecimalsIfZero, boolean wantBlankIfZero,
            int nDecimals) {
        return JANumberFormatter.fmt(getLastValue(), wantQuote, wantDecimalsIfZero, wantBlankIfZero, nDecimals);
    }

    public String getFormatedValueByYear(String year, String defaultValue, boolean wantQuote,
            boolean wantDecimalsIfZero, boolean wantBlankIfZero, int nDecimals) throws ParametreAnnuelException {
        String value = this.getValueByYear(year, defaultValue);

        return JANumberFormatter.fmt(value, wantQuote, wantDecimalsIfZero, wantBlankIfZero, nDecimals);
    }

    public String getFormatedValueByYear(String year, String defaultValue, int nDecimals)
            throws ParametreAnnuelException {
        return this.getFormatedValueByYear(year, defaultValue, false, false, false, nDecimals);
    }

    /**
     * @return La dernière valeur de la map (la plus récente)
     */
    public String getLastValue() {
        return parametresAnnuels.get(parametresAnnuels.lastKey());
    }

    public Map<String, String> getParametresAnnuels() {
        return parametresAnnuels;
    }

    public String getValueByYear(String year) throws ParametreAnnuelException {
        return this.getValueByYear(year, "0");
    }

    /**
     * 
     * @param year
     * @param defaultValue
     *            La valeur par défaut si pas trouvé. Si defaultValue==null alors on lève une exception
     * @return
     * @throws ParametreAnnuelException
     */
    public String getValueByYear(String year, String defaultValue) throws ParametreAnnuelException {
        if (parametresAnnuels.containsKey(year)) {
            return parametresAnnuels.get(year);
        } else {
            if (defaultValue == null) {
                // JadeThread.logError(this.getClass().getName(), "Parameter '" + this.csTypeParametreAnnuel
                // + "' for year '" + year + "' not exist !");
                BSession currentSession = BSessionUtil.getSessionFromThreadContext();
                JadeThread.logError("Parameter", "Parameter '" + currentSession.getCodeLibelle(csTypeParametreAnnuel)
                        + " (" + csTypeParametreAnnuel + ") ' for year '" + year + "' not found !");
                throw new ParametreAnnuelException("Parameter '" + currentSession.getCodeLibelle(csTypeParametreAnnuel)
                        + " (" + csTypeParametreAnnuel + ") ' for year '" + year + "' not found !");
                // return null;
            }
            return defaultValue;
        }
    }

    public void initParamAnnuels() {
        try {
            SimpleParametreAnnuelSearch simpleParametreAnnuelSearch = new SimpleParametreAnnuelSearch();
            simpleParametreAnnuelSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            simpleParametreAnnuelSearch = AmalServiceLocator.getParametreAnnuelService().search(
                    simpleParametreAnnuelSearch);
            containerParametres
                    .setParametresAnnuelsProvider(new ParametresAnnuelsProvider(simpleParametreAnnuelSearch));
        } catch (Exception e) {
            JadeLogger.error(this, "Error loading parametre annuels --> " + e.getMessage());
        }
    }

    public void setCsTypeParametreAnnuel(String csTypeParametreAnnuel) {
        this.csTypeParametreAnnuel = csTypeParametreAnnuel;
    }

    public void setParametresAnnuels(TreeMap<String, String> parametresAnnuels) {
        this.parametresAnnuels = parametresAnnuels;
    }

}
