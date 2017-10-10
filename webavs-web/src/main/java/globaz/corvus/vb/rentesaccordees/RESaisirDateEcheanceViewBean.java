package globaz.corvus.vb.rentesaccordees;

import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.utils.beneficiaire.principal.REBeneficiairePrincipal;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.nnss.PRNSSUtil;
import javax.resource.spi.IllegalStateException;

public class RESaisirDateEcheanceViewBean extends REPrestationsAccordees implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateNaissanceRequerant;
    private int groupLevelRenteRequerant;

    /**
     * Méthode qui retourne le détail du requérant formaté pour les détails
     * 
     * @return le détail du requérant formaté
     * @throws Exception
     */
    public String getDetailRequerantDetail() throws Exception {

        PRTiersWrapper tiers = PRTiersHelper.getTiersParId(getSession(), getIdTiersBeneficiaire());

        if (tiers != null) {

            String nationalite = "";

            if (!"999".equals(getSession()
                    .getCode(
                            getSession().getSystemCode("CIPAYORI",
                                    tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE))))) {
                nationalite = getSession().getCodeLibelle(
                        getSession().getSystemCode("CIPAYORI",
                                tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE)));
            }

            setDateNaissanceRequerant(tiers.getDateNaissance());
            int groupLevel = REBeneficiairePrincipal.getGroupLevel(getSession(), getSession()
                    .getCurrentThreadTransaction(), getIdPrestationAccordee());
            setGroupLevelRenteRequerant(groupLevel);

            return PRNSSUtil.formatDetailRequerantDetail(
                    tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                    getSession().getCodeLibelle(tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE)), nationalite);

        } else {
            return "";
        }
    }

    /**
     * Retourne la date de l'âge des 25 ans du tiers (au format MM.YYYY) -> uniquement dans le cas ou la rente du tiers
     * est dans le groupe 5
     * 
     * @return date de l'âge des 25 ans  (au format MM.YYYY) si level 5 | sinon retourne vide
     * @throws IllegalStateException
     */
    public String getDateAge25Requerant() throws IllegalStateException {
        if (getGroupLevelRenteRequerant() == 5) {
            try {
                // dans le cas ou la date de naissance comment par 00.00, on remplace cela par 01.06
                if (getDateNaissanceRequerant().startsWith("00.00")) {

                    setDateNaissanceRequerant(dateNaissanceRequerant.replace("00.00", "01.06"));
                }

                return JadeStringUtil.substring(JadeDateUtil.addYears(getDateNaissanceRequerant(), 25), 3);
            } catch (Exception e) {
                String errorMessage = java.text.MessageFormat.format(
                        getSession().getLabel("ERROR_AGE_TIERS_TRAITEMENT_DATE_NAISSANCE"),
                        new Object[] { getDateNaissanceRequerant() });

                throw new IllegalStateException(errorMessage, e);
            }

        }
        // Si le tiers n'est pas dans le groupe level 5, sa date de naissance + 25 ans ne nous importe pas.
        return "";
    }
    /**
     * Permet d'obtenir le label de warning de l'âge des 25 ans dépassés pour le tiers
     * @return  label de warning de l'âge des 25 ans dépassés
     * @throws IllegalStateException
     */
    public String get25AnsWarningLabel() throws IllegalStateException {

        return java.text.MessageFormat.format(getSession().getLabel("WARNING_AGE_25_ANS_DEPASSES"),
                new Object[] { getDateAge25Requerant() });

    }

    public String getDateNaissanceRequerant() {
        return dateNaissanceRequerant;
    }

    public void setDateNaissanceRequerant(String dateNaissanceRequerant) {
        this.dateNaissanceRequerant = dateNaissanceRequerant;
    }

    public int getGroupLevelRenteRequerant() {
        return groupLevelRenteRequerant;
    }

    public void setGroupLevelRenteRequerant(int groupLevelRenteRequerant) {
        this.groupLevelRenteRequerant = groupLevelRenteRequerant;
    }

}
