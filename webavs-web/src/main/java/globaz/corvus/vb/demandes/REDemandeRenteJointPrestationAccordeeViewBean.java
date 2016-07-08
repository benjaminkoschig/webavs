package globaz.corvus.vb.demandes;

import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteJointPrestationAccordee;
import globaz.corvus.utils.decisions.REDecisionsUtil;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author PBA
 * 
 */
public class REDemandeRenteJointPrestationAccordeeViewBean extends REDemandeRenteJointPrestationAccordee {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<String> codesPrestations = null;
    private List<String> datesFinDroit = null;

    public REDemandeRenteJointPrestationAccordeeViewBean() {
        codesPrestations = new ArrayList<String>();
        datesFinDroit = new ArrayList<String>();
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        codesPrestations.add(getCodePrestation());
        datesFinDroit.add(getDateFinDroit());
    }

    public void addCodePrestation(String codePrestation) {
        this.addCodePrestation(codePrestation, "");
    }

    public void addCodePrestation(String codePrestation, String dateFinDroit) {
        codesPrestations.add(codePrestation);
        datesFinDroit.add(dateFinDroit);
    }

    /**
     * Retourne les des code de prestation de la demande
     * 
     * @return les codes de prestation
     */
    public List<String> getCodesPrestation() {
        return codesPrestations;
    }

    public List<String> getDatesFinDroit() {
        return datesFinDroit;
    }

    /**
     * Retourne le détails d'un requérant (avec la ligne concernant le décès si elle a lieu d'être)<br/>
     * Utilise les méthodes de {@link globaz.prestation.tools.nnss.PRNSSUtil PRNSSUtil}
     * 
     * @return le code HTML généré, sous forme de {@link java.lang.String String}
     * @throws Exception
     *             Lève une exception si un code système de l'entité est invalide
     */
    public String getDetailRequerant() throws Exception {
        if (JadeStringUtil.isEmpty(getDateDeces())) {
            return PRNSSUtil.formatDetailRequerantListe(getNoAVS(), getNom() + " " + getPrenom(), getDateNaissance(),
                    getISession().getCodeLibelle(getCsSexe()), getISession().getCodeLibelle(getCsNationalite()));
        } else {
            return PRNSSUtil.formatDetailRequerantListeDecede(getNoAVS(), getNom() + " " + getPrenom(),
                    getDateNaissance(), getISession().getCodeLibelle(getCsSexe()),
                    getISession().getCodeLibelle(getCsNationalite()), getDateDeces());
        }
    }

    public String getIdTierRequerant() throws Exception {
        // normalement on doit avoir la valeur idtiersRequerant mais, on la cherche avec le NSS ???
        // Modifie comme ceci pour minimiser les impacts et surtout diminuer les requêtes...
        if (JadeStringUtil.isIntegerEmpty(getIdTiersRequerant())) {
            PRTiersWrapper tier = PRTiersHelper.getTiers(getSession(), getNoAVS());

            if (tier != null) {
                return tier.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
            } else {
                return "";
            }
        } else {
            return getIdTiersRequerant();
        }
    }

    public String getLibelleCourtSexe() {
        if (PRACORConst.CS_HOMME.equals(getCsSexe())) {
            return getSession().getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(getCsSexe())) {
            return getSession().getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }
    }

    public String getLibellePays() throws Exception {
        if ("999".equals(getSession().getCode(getSession().getSystemCode("CIPAYORI", getCsNationalite())))) {
            return "";
        } else {
            return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", getCsNationalite()));
        }
    }

    /**
     * BZ 5198, teste si le document à éditer est rente de veuve perdure
     * 
     * @return <strong><code>true</code></strong> si l'info complémentaire est de type "Rente de veuve perdure", sinon
     *         <strong><code>false</code></strong>
     */
    public boolean isInfoComplRenteVeuvePerdure() {
        if (IREDemandeRente.CS_TYPE_INFORMATION_COMPLEMENTAIRE_RENTE_VEUVE_PERDURE
                .equals(getCsTypeInfoComplementaire())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isInfoComplRefus() {
        if (IREDemandeRente.CS_TYPE_INFORMATION_COMPLEMENTAIRE_REFUS.equals(getCsTypeInfoComplementaire())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isPreparationDecisionValide(String dateDernierPaiement) {
        return REDecisionsUtil.isPreparationDecisionAuthorise(getSession(), getIdDemandeRente());
    }


    public void setCodePrestations(List<String> codesPrestations) {
        this.codesPrestations.clear();
        this.codesPrestations.addAll(codesPrestations);
    }

    public void setDateFinDroit(List<String> datesFinDroit) {
        this.datesFinDroit = datesFinDroit;
    }
}
