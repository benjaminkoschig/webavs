package globaz.osiris.print.itext.list;

import globaz.globall.util.JACCP;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.utils.CAAdressePaiementFormatter;
import globaz.osiris.external.IntAdressePaiement;
import globaz.osiris.formatter.CAAdresseCourrierFormatter;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import java.math.BigDecimal;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class CAIListOrdreGroupeBean {

    private TIAdresseDataSource adresseCourrier = null;
    private java.lang.String compteAnnexe = "";
    private boolean empty = true;
    private CAAdressePaiementFormatter fmtAdPmt = null;
    private String m_adresseVersement = new String();
    private String m_beneficiaire = new String();
    private BigDecimal m_montant = new BigDecimal(0);
    private String m_natureVersement = new String();
    private String m_numeroTransaction = new String();

    public CAIListOrdreGroupeBean() {
    }

    public CAIListOrdreGroupeBean(CAOperation entity) {
        add(entity);
    }

    /**
     * Cette méthode formate l'adresse de paiement
     */
    private void _formaterAdressePaiement(CAOperation entity) {
        // Récupérer l'ordre de versement
        // CAOperationOrdreVersement oper = (CAOperationOrdreVersement) entity;

        // Formatter l'adresse de paiement
        try {
            fmtAdPmt = new CAAdressePaiementFormatter(entity.getAdressePaiement());
        } catch (Exception e) {
            fmtAdPmt = new CAAdressePaiementFormatter(null);
        }

    }

    /**
     * Cette méthode récupère l'adresse de versement
     * 
     * @return java.lang.String
     */
    private String _getAdresseVersement() {
        if (fmtAdPmt != null) {
            // Compte
            String sCompte = new String();
            if (fmtAdPmt.getTypeAdresse().equals(IntAdressePaiement.CCP)) {
                try {
                    sCompte = JACCP.formatWithDash(fmtAdPmt.getNumCompte());
                } catch (Exception e) {
                    sCompte = fmtAdPmt.getNumCompte();
                }
            } else {
                sCompte = fmtAdPmt.getNumCompte();
            }

            // Banque
            if (fmtAdPmt.getTypeAdresse().equals(IntAdressePaiement.BANQUE)
                    || fmtAdPmt.getTypeAdresse().equals(IntAdressePaiement.BANQUE_INTERNATIONAL)) {

                if (sCompte.length() > 0) {
                    sCompte = sCompte + ", ";
                }

                // Clearing ou code swift
                if (!JadeStringUtil.isBlank(fmtAdPmt.getClearing())) {
                    sCompte = sCompte + fmtAdPmt.getClearing() + " ";
                } else {
                    sCompte = sCompte + fmtAdPmt.getCodeSwift() + " ";
                }

                // Nom de la banque
                sCompte = sCompte + fmtAdPmt.getTiersBanque().getNom();
            }
            return sCompte;
        } else {
            return adresseCourrier.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE) + " "
                    + adresseCourrier.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO) + " "
                    + adresseCourrier.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA) + " "
                    + adresseCourrier.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
        }
    }

    /**
     * Cette méthode récupère la description du bénéficiaire
     * 
     * @return java.lang.String
     */
    private String _getBeneficiaire(CAOperation entity) throws Exception {
        // Formater l'adresse de paiement
        if (entity.getAdressePaiement() != null) {
            _formaterAdressePaiement(entity);

            // Bénéficiaire
            CAAdresseCourrierFormatter fmtAdBen = new CAAdresseCourrierFormatter(fmtAdPmt.getTiersBeneficiaire(),
                    fmtAdPmt.getAdresseCourrierBeneficiaire());
            fmtAdBen.setUseTitle(false);
            fmtAdBen.setUseCountry(true);
            String[] sAdBen = fmtAdBen.getAdresseLines(2);
            return sAdBen[0] + ", " + sAdBen[1];

        } else {
            adresseCourrier = entity.getCompteAnnexe().getTiers().getDataSourceAdresseCourrier();
            return entity.getCompteAnnexe().getTiers().getNomPrenom();
        }
    }

    /**
     * Cette méthode récupère la description du compte annexe
     * 
     * @return java.lang.String
     */
    private String _getCompteAnnexe(CAOperation entity) {
        try {
            compteAnnexe = entity.getCompteAnnexe().getCARole().getDescription() + " "
                    + entity.getCompteAnnexe().getIdExterneRole() + " " + entity.getCompteAnnexe().getTiers().getNom();
        } catch (Exception e) {
            compteAnnexe = null;
        }
        return compteAnnexe;
    }

    /**
     * Cette méthode permet d'ajouter des valeurs dans le bean
     */
    public void add(CAOperation entity) {
        try {
            setBeneficiaire(_getBeneficiaire(entity));
            setCompteAnnexe(_getCompteAnnexe(entity));
            setAdresseVersement(_getAdresseVersement());
            setNatureVersement(entity.getSection().getDescription());
            setMontant(new BigDecimal(JANumberFormatter.deQuote(entity.getMontant())));
            setNumeroTransaction(entity.getNumTransaction());
            setEmpty(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retourne l'adresse de versement (m_adresseVersement).
     * 
     * @return String
     */
    protected String getAdresseVersement() {
        return m_adresseVersement;
    }

    /**
     * Retourne le bénéficiare.
     * 
     * @return String
     */
    protected String getBeneficiaire() {
        return m_beneficiaire;
    }

    /**
     * Retourne le genre de virement.
     * 
     * @return String
     */
    public String getCOL_1() {
        return getBeneficiaire();
    }

    /**
     * Retourne le compte annexe.
     * 
     * @return String
     */
    public String getCOL_2() {
        return getCompteAnnexe();
    }

    /**
     * Retourne l'adresse de versement.
     * 
     * @return String
     */
    public String getCOL_3() {
        return getAdresseVersement();
    }

    /**
     * Retourne la nature du versement.
     * 
     * @return String
     */
    public String getCOL_4() {
        return getNatureVersement();
    }

    /**
     * Retourne le montant groupé.
     * 
     * @return BigDecimal
     */
    public BigDecimal getCOL_5() {
        return getMontant();
    }

    /**
     * Retourne le numéro de transaction.
     * 
     * @return String
     */
    public String getCOL_6() {
        return getNumeroTransaction();
    }

    /**
     * Retourne le compte annexe (compteAnnexe).
     * 
     * @return String
     */
    protected String getCompteAnnexe() {
        return compteAnnexe;
    }

    /**
     * Returns the m_montant.
     * 
     * @return BigDecimal
     */
    protected BigDecimal getMontant() {
        return m_montant;
    }

    /**
     * Returns the m_natureVersement.
     * 
     * @return String
     */
    protected String getNatureVersement() {
        return m_natureVersement;
    }

    /**
     * Returns the m_numeroTransaction.
     * 
     * @return String
     */
    protected String getNumeroTransaction() {
        return m_numeroTransaction;
    }

    /**
     * Returns the empty.
     * 
     * @return boolean
     */
    public boolean isEmpty() {
        return empty;
    }

    /**
     * Sets the m_adresseVersement.
     * 
     * @param m_adresseVersement
     *            The m_adresseVersement to set
     */
    protected void setAdresseVersement(String adresseVersement) {
        m_adresseVersement = adresseVersement;
        setEmpty(false);
    }

    /**
     * Sets the bénéficiaire.
     * 
     * @param bénéficiaire
     *            The bénéficiaire to set
     */
    protected void setBeneficiaire(String beneficiaire) {
        m_beneficiaire = beneficiaire;
        setEmpty(false);
    }

    /**
     * Sets the compteAnnexe.
     * 
     * @param compteAnnexe
     *            The compteAnnexe to set
     */
    protected void setCompteAnnexe(String cptAnnexe) {
        compteAnnexe = cptAnnexe;
        setEmpty(false);
    }

    /**
     * Sets the empty.
     * 
     * @param empty
     *            The empty to set
     */
    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    /**
     * Sets the m_montant.
     * 
     * @param m_montant
     *            The m_montant to set
     */
    protected void setMontant(BigDecimal montant) {
        m_montant = montant;
        setEmpty(false);

    }

    /**
     * Sets the m_natureVersement.
     * 
     * @param m_natureVersement
     *            The natureVersement to set
     */
    protected void setNatureVersement(String natureVersement) {
        m_natureVersement = natureVersement;
        setEmpty(false);
    }

    /**
     * Sets the m_numeroTransaction.
     * 
     * @param m_numeroTransaction
     *            The m_numeroTransaction to set
     */
    protected void setNumeroTransaction(String numeroTransaction) {
        m_numeroTransaction = numeroTransaction;
        setEmpty(false);
    }

}