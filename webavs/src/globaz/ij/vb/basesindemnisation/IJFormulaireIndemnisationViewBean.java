package globaz.ij.vb.basesindemnisation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.ij.db.basesindemnisation.IJFormulaireIndemnisation;
import globaz.ij.db.prononces.IJAgentExecution;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.util.Vector;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJFormulaireIndemnisationViewBean extends IJFormulaireIndemnisation implements FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String nomAgent;
    private Vector nomsAgents;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String getCsTypeIJ() throws Exception {
        return loadBaseIndemnisation().getCsTypeIJ();
    }

    /**
     * getter pour l'attribut date debut base.
     * 
     * @return la valeur courante de l'attribut date debut base
     */
    public String getDateDebutBase() {
        try {
            return loadBaseIndemnisation().getDateDebutPeriode();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * getter pour l'attribut date fin base.
     * 
     * @return la valeur courante de l'attribut date fin base
     */
    public String getDateFinBase() {
        try {
            return loadBaseIndemnisation().getDateFinPeriode();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les détails
     * 
     * @return le détail du requérant formaté
     * @throws Exception
     */
    public String getDetailRequerant() {

        String nationalite = "";
        String result = "";

        try {

            if (!"999".equals(getSession().getCode(
                    getSession().getSystemCode(
                            "CIPAYORI",
                            loadBaseIndemnisation().loadPrononce(null).loadDemande(null).loadTiers()
                                    .getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE))))) {
                nationalite = getSession().getCodeLibelle(
                        getSession().getSystemCode(
                                "CIPAYORI",
                                loadBaseIndemnisation().loadPrononce(null).loadDemande(null).loadTiers()
                                        .getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE)));
            }

            result = loadBaseIndemnisation().loadPrononce(null).loadDemande(null).loadTiers()
                    .getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)
                    + " / "
                    + loadBaseIndemnisation().loadPrononce(null).loadDemande(null).loadTiers()
                            .getProperty(PRTiersWrapper.PROPERTY_NOM)
                    + " "
                    + loadBaseIndemnisation().loadPrononce(null).loadDemande(null).loadTiers()
                            .getProperty(PRTiersWrapper.PROPERTY_PRENOM)
                    + " / "
                    + loadBaseIndemnisation().loadPrononce(null).loadDemande(null).loadTiers()
                            .getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE)
                    + " / "
                    + getSession().getCodeLibelle(
                            loadBaseIndemnisation().loadPrononce(null).loadDemande(null).loadTiers()
                                    .getProperty(PRTiersWrapper.PROPERTY_SEXE)) + " / " + nationalite;

        } catch (Exception e) {
        }

        return result;
    }

    /**
     * getter pour l'attribut description prononce.
     * 
     * @return la valeur courante de l'attribut description prononce
     */
    public String[] getFullDescriptionPrononce() {
        try {
            return loadBaseIndemnisation().getFullDescriptionPrononce();
        } catch (Exception e) {
            return new String[2];
        }
    }

    public String getIdPrononce() throws Exception {
        return loadBaseIndemnisation().getIdPrononce();
    }

    /**
     * getter pour l'attribut libelle etat.
     * 
     * @return la valeur courante de l'attribut libelle etat
     */
    public String getLibelleEtat() {
        return getSession().getCodeLibelle(getEtat());
    }

    /**
     * getter pour l'attribut nom agent.
     * 
     * @return la valeur courante de l'attribut nom agent
     */
    public String getNomAgent() {
        if (JadeStringUtil.isEmpty(nomAgent) && !JadeStringUtil.isIntegerEmpty(getIdInstitutionResponsable())) {
            try {
                // charger l'agnet
                IJAgentExecution agent = new IJAgentExecution();

                agent.setIdAgentExecution(getIdInstitutionResponsable());
                agent.setSession(getSession());
                agent.retrieve();

                // charger le tiers si c'est un tiers
                PRTiersWrapper tiers = PRTiersHelper.getTiersParId(getISession(), agent.getIdTiers());

                // si c'est null, essayer de chercher si c'est une
                // administration
                if (tiers == null) {
                    tiers = PRTiersHelper.getAdministrationParId(getISession(), agent.getIdTiers());
                }

                nomAgent = tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM) + " "
                        + tiers.getProperty(PRTiersWrapper.PROPERTY_NOM);
            } catch (Exception e) {
            }
        }

        return nomAgent;
    }

    /**
     * getter pour l'attribut noms agents.
     * 
     * @return la valeur courante de l'attribut noms agents
     */
    public Vector getNomsAgents() {
        return nomsAgents;
    }

    /**
     * setter pour l'attribut nom agent.
     * 
     * @param nomAgent
     *            une nouvelle valeur pour cet attribut
     */
    public void setNomAgent(String nomAgent) {
        this.nomAgent = nomAgent;
    }

    /**
     * setter pour l'attribut noms agents.
     * 
     * @param nomsAgents
     *            une nouvelle valeur pour cet attribut
     */
    public void setNomsAgents(Vector nomsAgents) {
        this.nomsAgents = nomsAgents;
    }
}
