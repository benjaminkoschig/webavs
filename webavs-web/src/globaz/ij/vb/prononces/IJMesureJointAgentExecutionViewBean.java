/*
 * Créé le 26 sept. 05
 */
package globaz.ij.vb.prononces;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.ij.db.prononces.IJMesureJointAgentExecution;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRImagesConstants;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJMesureJointAgentExecutionViewBean extends IJMesureJointAgentExecution implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final Object[] METHODES_SEL_AGENTEXECUTION = new Object[] {
            new String[] { "idTiersAgentExecutionDepuisPyxis", "idTiers" },
            new String[] { "nomAgentExecution", "nom" }, new String[] { "codeIsoLangueAttestation", "langue" } };

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csTypeIJ = "";
    private String dateDebutPrononce = "";
    private String detailRequerant = "";
    private boolean modifierPermis;
    private String noAVS = "";

    private String prenomNom = "";
    private boolean retourDepuisPyxis = false;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut cs type IJ.
     * 
     * @return la valeur courante de l'attribut cs type IJ
     */
    public String getCsTypeIJ() {
        return csTypeIJ;
    }

    /**
     * getter pour l'attribut date debut prononce.
     * 
     * @return la valeur courante de l'attribut date debut prononce
     */
    public String getDateDebutPrononce() {
        return dateDebutPrononce;
    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les détails
     * 
     * @return le détail du requérant formaté
     * @throws Exception
     */
    public String getDetailRequerantDetail() throws Exception {

        PRTiersWrapper tiers = PRTiersHelper.getTiersParId(
                getSession(),
                loadPrononce(getSession().getCurrentThreadTransaction()).loadDemande(
                        getSession().getCurrentThreadTransaction()).getIdTiers());

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
     * @return l'url de l'image
     */
    public String getImageAttestationGroupee() {
        if (getAttestationsGroupees().booleanValue()) {
            return PRImagesConstants.IMAGE_OK;
        } else {
            return PRImagesConstants.IMAGE_ERREUR;
        }
    }

    /**
     * getter pour l'attribut libelle langue.
     * 
     * @return la valeur courante de l'attribut libelle langue
     */
    public String getLibelleLangue() {
        return getSession().getCodeLibelle(getCodeIsoLangueAttestation());
    }

    /**
     * getter pour l'attribut libelle type attestation.
     * 
     * @return la valeur courante de l'attribut libelle type attestation
     */
    public String getLibelleTypeAttestation() {
        return getSession().getCodeLibelle(getCsTypeAttestation());
    }

    /**
     * getter pour l'attribut methodes selecteur agent execution.
     * 
     * @return la valeur courante de l'attribut methodes selecteur agent execution
     */
    public Object[] getMethodesSelecteurAgentExecution() {
        return METHODES_SEL_AGENTEXECUTION;
    }

    /**
     * getter pour l'attribut no AVS.
     * 
     * @return la valeur courante de l'attribut no AVS
     */
    public String getNoAVS() {
        return noAVS;
    }

    /**
     * getter pour l'attribut prenom nom.
     * 
     * @return la valeur courante de l'attribut prenom nom
     */
    public String getPrenomNom() {
        return prenomNom;
    }

    /**
     * getter pour l'attribut modifier permis.
     * 
     * @return la valeur courante de l'attribut modifier permis
     */
    public boolean isModifierPermis() {
        return modifierPermis || isNew();
    }

    /**
     * getter pour l'attribut retour depuis pyxis.
     * 
     * @return la valeur courante de l'attribut retour depuis pyxis
     */
    public boolean isRetourDepuisPyxis() {
        return retourDepuisPyxis;
    }

    /**
     * setter pour l'attribut cs type IJ.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsTypeIJ(String string) {
        csTypeIJ = string;
    }

    /**
     * setter pour l'attribut date debut prononce.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebutPrononce(String string) {
        dateDebutPrononce = string;
    }

    /**
     * setter pour l'attribut id tiers agent execution depuis pyxis.
     * 
     * @param idTiers
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTiersAgentExecutionDepuisPyxis(String idTiers) {
        setIdTiers(idTiers);
        retourDepuisPyxis = true;
    }

    /**
     * setter pour l'attribut modifier permis.
     * 
     * @param modifierPermis
     *            une nouvelle valeur pour cet attribut
     */
    public void setModifierPermis(boolean modifierPermis) {
        this.modifierPermis = modifierPermis;
    }

    /**
     * setter pour l'attribut no AVS.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoAVS(String string) {
        noAVS = string;
    }

    /**
     * setter pour l'attribut prenom nom.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setPrenomNom(String string) {
        prenomNom = string;
    }

    /**
     * setter pour l'attribut retour depuis pyxis.
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setRetourDepuisPyxis(boolean b) {
        retourDepuisPyxis = b;
    }

}
