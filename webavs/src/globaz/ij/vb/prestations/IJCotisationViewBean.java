package globaz.ij.vb.prestations;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JANumberFormatter;
import globaz.ij.db.prestations.IJCotisation;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJCotisationViewBean extends IJCotisation implements FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebutBaseIndemnisation = "";
    private String dateFinBaseIndemnisation = "";
    private String datePrononce = "";
    private String idBaseIndemnisation = "";
    private String idPrestation = "";
    private String montantBrutRepartition = "";
    private String noAVSAssure = "";
    private String nomBeneficiaireRepartition = "";
    private String nomPrenomAssure = "";

    /**
     * Par défaut false.
     * La cotisation est éditable si elle n'est pas en état Annulé ou Définitif
     */
    private boolean isEditable = false;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public boolean getIsEditable() {
        return isEditable;
    }

    public void setIsEditable(boolean value) {
        isEditable = value;
    }

    /**
     * getter pour l'attribut date debut base indemnisation
     * 
     * @return la valeur courante de l'attribut date debut base indemnisation
     */
    public String getDateDebutBaseIndemnisation() {
        return dateDebutBaseIndemnisation;
    }

    /**
     * getter pour l'attribut date fin base indemnisation
     * 
     * @return la valeur courante de l'attribut date fin base indemnisation
     */
    public String getDateFinBaseIndemnisation() {
        return dateFinBaseIndemnisation;
    }

    /**
     * getter pour l'attribut date prononce
     * 
     * @return la valeur courante de l'attribut date prononce
     */
    public String getDatePrononce() {
        return datePrononce;
    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les détails
     * 
     * @return le détail du requérant formaté
     * @throws Exception
     */
    public String getDetailRequerantDetail() throws Exception {

        PRTiersWrapper tiers = PRTiersHelper.getTiers(getSession(), getNoAVSAssure());

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
                    getNoAVSAssure(),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                    getSession().getCodeLibelle(tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE)), nationalite);

        } else {
            return "";
        }
    }

    /**
     * getter pour l'attribut id base indemnisation
     * 
     * @return la valeur courante de l'attribut id base indemnisation
     */
    public String getIdBaseIndemnisation() {
        return idBaseIndemnisation;
    }

    /**
     * getter pour l'attribut id prestation
     * 
     * @return la valeur courante de l'attribut id prestation
     */
    public String getIdPrestation() {
        return idPrestation;
    }

    /**
     * getter pour l'attribut montant
     * 
     * @return la valeur courante de l'attribut montant
     */
    @Override
    public String getMontant() {
        return JANumberFormatter.format(super.getMontant(), 0.05, 2, JANumberFormatter.NEAR);
    }

    /**
     * getter pour l'attribut montant brut
     * 
     * @return la valeur courante de l'attribut montant brut
     */
    @Override
    public String getMontantBrut() {
        return JANumberFormatter.format(super.getMontantBrut(), 0.05, 2, JANumberFormatter.NEAR);
    }

    /**
     * getter pour l'attribut montant brut repartition
     * 
     * @return la valeur courante de l'attribut montant brut repartition
     */
    public String getMontantBrutRepartition() {
        return montantBrutRepartition;
    }

    /**
     * getter pour l'attribut no AVSAssure
     * 
     * @return la valeur courante de l'attribut no AVSAssure
     */
    public String getNoAVSAssure() {
        return noAVSAssure;
    }

    /**
     * getter pour l'attribut nom beneficiaire repartition
     * 
     * @return la valeur courante de l'attribut nom beneficiaire repartition
     */
    public String getNomBeneficiaireRepartition() {
        return nomBeneficiaireRepartition;
    }

    /**
     * @return
     */
    public String getNomPrenomAssure() {
        return nomPrenomAssure;
    }

    /**
     * setter pour l'attribut date debut base indemnisation
     * 
     * @param dateDebutBaseIndemnisation
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebutBaseIndemnisation(String dateDebutBaseIndemnisation) {
        this.dateDebutBaseIndemnisation = dateDebutBaseIndemnisation;
    }

    /**
     * setter pour l'attribut date fin base indemnisation
     * 
     * @param dateFinBaseIndemnisation
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateFinBaseIndemnisation(String dateFinBaseIndemnisation) {
        this.dateFinBaseIndemnisation = dateFinBaseIndemnisation;
    }

    /**
     * setter pour l'attribut date prononce
     * 
     * @param datePrononce
     *            une nouvelle valeur pour cet attribut
     */
    public void setDatePrononce(String datePrononce) {
        this.datePrononce = datePrononce;
    }

    /**
     * setter pour l'attribut id base indemnisation
     * 
     * @param idBaseIndemnisation
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdBaseIndemnisation(String idBaseIndemnisation) {
        this.idBaseIndemnisation = idBaseIndemnisation;
    }

    /**
     * setter pour l'attribut id prestation
     * 
     * @param idPrestation
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdPrestation(String idPrestation) {
        this.idPrestation = idPrestation;
    }

    /**
     * setter pour l'attribut montant
     * 
     * @param montant
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setMontant(String montant) {
        super.setMontant(JANumberFormatter.deQuote(montant));
    }

    /**
     * setter pour l'attribut montant brut
     * 
     * @param montantBrut
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setMontantBrut(String montantBrut) {
        super.setMontantBrut(JANumberFormatter.deQuote(montantBrut));
    }

    /**
     * setter pour l'attribut montant brut repartition
     * 
     * @param montantBrutRepartition
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantBrutRepartition(String montantBrutRepartition) {
        this.montantBrutRepartition = montantBrutRepartition;
    }

    /**
     * setter pour l'attribut no AVSAssure
     * 
     * @param noAVSAssure
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoAVSAssure(String noAVSAssure) {
        this.noAVSAssure = noAVSAssure;
    }

    /**
     * setter pour l'attribut nom beneficiaire repartition
     * 
     * @param nomBeneficiaireRepartition
     *            une nouvelle valeur pour cet attribut
     */
    public void setNomBeneficiaireRepartition(String nomBeneficiaireRepartition) {
        this.nomBeneficiaireRepartition = nomBeneficiaireRepartition;
    }

    /**
     * @param string
     */
    public void setNomPrenomAssure(String string) {
        nomPrenomAssure = string;
    }

}
