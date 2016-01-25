package globaz.lynx.db.canevas;

import globaz.globall.api.GlobazSystem;
import globaz.globall.util.JANumberFormatter;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.application.LXApplication;
import globaz.lynx.db.societesdebitrice.LXSocieteDebitrice;
import globaz.lynx.db.societesdebitrice.LXSocieteDebitriceViewBean;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementViewBean;
import globaz.pyxis.db.adressepaiement.TIAvoirPaiement;

public class LXCanevasViewBean extends LXCanevas {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final Object[] METHODES_ADRESSE_PAIEMENT = new Object[] { new String[] {
            "setIdAdressePaiementDepuisPyxis", "idAvoirPaiementUnique" } };

    private static final String PROPERTY_MAX_ROWS = "gestionEcrituresMaxRows";
    private static final String PROPERTY_SHOW_ROWS = "gestionEcrituresShowRows";

    private String adresseBanque = "";

    private String adressePaiementFournisseur = "";
    private Boolean canevasPourcentage = false;

    private String ccpFournisseur = "";

    private String clearingBanque = "";
    private String compte = "";
    private String idExterneFournisseur = "";
    private String lectureOptique = "";
    private int maxRows = 0;
    private boolean retourDepuisPyxis = false;

    private int showRows = 0;
    private LXSocieteDebitriceViewBean societe = null;

    /**
     * Parse le bean des informations spécifiques de l'adresse de paiement.
     * 
     * @param idAdressePaiement
     */
    public void fillWithSpecificAdressePaiement(String idAdressePaiement) {
        try {
            TIAvoirPaiement paiement = new TIAvoirPaiement();
            paiement.setSession(getSession());

            paiement.setIdAdrPmtIntUnique(idAdressePaiement);

            paiement.retrieve();

            if (!paiement.isNew()) {
                TIAdressePaiementViewBean adressePaiement = new TIAdressePaiementViewBean();
                adressePaiement.setSession(getSession());
                adressePaiement.setIdAdressePmtUnique(paiement.getIdAdressePaiement());

                adressePaiement.retrieve();

                if (!adressePaiement.isNew()) {
                    setCcpFournisseur(adressePaiement.getNumCcp());

                    setCompte(adressePaiement.getNumCompteBancaire());

                    setAdresseBanque(adressePaiement.getAdresseBanque());

                    setClearingBanque(adressePaiement.getClearing());

                    setAdressePaiementFournisseur(adressePaiement.getDetailAdresse());
                }
            }
        } catch (Exception e) {
            // Do nothing
        }
    }

    public String getAdresseBanque() {
        return adresseBanque;
    }

    public String getAdressePaiementFournisseur() {
        return adressePaiementFournisseur;
    }

    public Boolean getCanevasPourcentage() {
        return canevasPourcentage;
    }

    public String getCcpFournisseur() {
        return ccpFournisseur;
    }

    public String getClearingBanque() {
        return clearingBanque;
    }

    public String getCompte() {
        return compte;
    }

    public String getCours(int i) {
        String result = "0.00000";

        if (getVentilations().size() > i) {
            if (!JadeStringUtil.isDecimalEmpty((getVentilations().get(i)).getCoursMonnaie())) {
                result = (getVentilations().get(i)).getCoursMonnaie();
            }
        }

        return JANumberFormatter.fmt(result, false, true, false, 5);
    }

    public String getIdCompte(int i) {
        if (getVentilations().size() > i) {
            return (getVentilations().get(i)).getIdCompte();
        } else {
            return "";
        }
    }

    public String getIdCompteCharge(int i) {
        if (getVentilations().size() > i) {
            return (getVentilations().get(i)).getIdCentreCharge();
        } else {
            return "";
        }
    }

    public String getIdExt(int i) {
        if (getVentilations().size() > i) {
            return (getVentilations().get(i)).getIdExterneCompte();
        } else {
            return "";
        }
    }

    @Override
    public String getIdExterneFournisseur() {
        return idExterneFournisseur;
    }

    /**
     * Return l'id mandat de la société. Nécessaire pour l'écran de détail, lien vers compta gen recherche compte.
     * 
     * @return
     */
    public String getIdMandat() {
        retrieveSociete();

        if (societe != null) {
            return societe.getIdMandat();
        } else {
            return "";
        }
    }

    public String getIdVentilation(int i) {
        if (getVentilations().size() > i) {
            return (getVentilations().get(i)).getIdVentilationCanevas();
        } else {
            return "";
        }
    }

    public String getLectureOptique() {
        return lectureOptique;
    }

    public String getLibelle(int i) {
        if (getVentilations().size() > i) {
            String result = (getVentilations().get(i)).getLibelle();
            result = JadeStringUtil.change(result, "\"", "&quot;");
            return result;
        } else {
            return "";
        }
    }

    /**
     * Return le nombre max de ligne à afficher
     * 
     * @return
     */
    public int getMaxRows() {
        try {
            if (maxRows == 0) {
                maxRows = Integer.parseInt(GlobazSystem.getApplication(LXApplication.DEFAULT_APPLICATION_LYNX)
                        .getProperty(LXCanevasViewBean.PROPERTY_MAX_ROWS).trim());
            }
        } catch (Exception e) {
            return LXApplication.DEFAULT_MAX_ROWS;
        }

        return maxRows;
    }

    /**
     * getter pour l'attribut methodes selection adresse
     * 
     * @return la valeur courante de l'attribut methodes selection adresse
     */
    public Object[] getMethodesSelectionAdressePaiement() {
        return LXCanevasViewBean.METHODES_ADRESSE_PAIEMENT;
    }

    public String getMontantCredit(int i) {
        String result = "";
        if (i == 0) {
            result = "0.00";
        }

        if (getVentilations().size() > i) {

            if ((CodeSystem.CS_CREDIT.equals((getVentilations().get(i)).getCodeDebitCredit()) || (CodeSystem.CS_EXTOURNE_CREDIT
                    .equals((getVentilations().get(i)).getCodeDebitCredit())))
                    && !JadeStringUtil.isDecimalEmpty((getVentilations().get(i)).getMontant())) {
                result = (getVentilations().get(i)).getMontant();
            }
        }

        return JANumberFormatter.fmt(result, true, true, false, 2);
    }

    public String getMontantDebit(int i) {
        String result = "";
        if ((i == 1) || (i == 2)) {
            result = "0.00";
        }

        if (getVentilations().size() > i) {
            if ((CodeSystem.CS_DEBIT.equals((getVentilations().get(i)).getCodeDebitCredit()) || (CodeSystem.CS_EXTOURNE_DEBIT
                    .equals((getVentilations().get(i)).getCodeDebitCredit())))
                    && !JadeStringUtil.isDecimalEmpty((getVentilations().get(i)).getMontant())) {
                result = (getVentilations().get(i)).getMontant();
            }
        }

        return JANumberFormatter.fmt(result, true, true, false, 2);
    }

    public String getMontantEtranger(int i) {
        String result = "0.00";

        if (getVentilations().size() > i) {
            if (!JadeStringUtil.isDecimalEmpty((getVentilations().get(i)).getMontantMonnaie())) {
                result = (getVentilations().get(i)).getMontantMonnaie();
            }
        }

        return JANumberFormatter.fmt(result, true, true, false, 2);
    }

    public String getPourcentage(int i) {
        String result = "0.00";

        if (getVentilations().size() > i) {
            if (!JadeStringUtil.isDecimalEmpty((getVentilations().get(i)).getPourcentage())) {
                result = (getVentilations().get(i)).getPourcentage();
            }
        }

        return JANumberFormatter.fmt(result, true, true, false, 2);
    }

    /**
     * Return le nombre de ligne à afficher
     * 
     * @return
     */
    public int getShowRows() {
        try {
            if (showRows == 0) {
                showRows = Integer.parseInt(GlobazSystem.getApplication(LXApplication.DEFAULT_APPLICATION_LYNX)
                        .getProperty(LXCanevasViewBean.PROPERTY_SHOW_ROWS).trim());
            }
        } catch (Exception e) {
            return LXApplication.DEFAULT_SHOW_ROWS;
        }

        return showRows;
    }

    /**
     * Return la société débitrice.
     * 
     * @return
     */
    public LXSocieteDebitrice getSociete() {
        retrieveSociete();

        return societe;
    }

    /**
     * Retourne l'adresse de la societe
     * 
     * @return
     */
    public String getSocieteAdresse() {
        if (!JadeStringUtil.isIntegerEmpty(getIdSociete())) {
            retrieveSociete();

            return societe.getAdresse();
        }

        return "";
    }

    public boolean isCanevasPourcentage() {
        return canevasPourcentage;
    }

    /**
     * Retourne true si la ventilation est de type débit, false sinon
     * 
     * @param i
     *            Index de la ventilation
     * @return
     */
    public boolean isDebit(int i) {

        if (getVentilations().size() > i) {
            if (CodeSystem.CS_CREDIT.equals((getVentilations().get(i)).getCodeDebitCredit())) {
                return true;
            }
        }

        return false;
    }

    public boolean isRetourDepuisPyxis() {
        return retourDepuisPyxis;
    }

    /**
     * Retrouve la societe si pas encore chargée.
     */
    private void retrieveSociete() {
        if (!JadeStringUtil.isIntegerEmpty(getIdSociete()) && (societe == null)) {
            try {
                societe = new LXSocieteDebitriceViewBean();
                societe.setSession(getSession());
                societe.setIdSociete(getIdSociete());
                societe.retrieve();

                if (societe.hasErrors() || societe.isNew()) {
                    societe = null;
                    return;
                }
            } catch (Exception e) {
                // nothing
            }
        }
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setAdresseBanque(String adresseBanque) {
        this.adresseBanque = adresseBanque;
    }

    public void setAdressePaiementFournisseur(String adressePaiementFournisseur) {
        this.adressePaiementFournisseur = adressePaiementFournisseur;
    }

    public void setCanevasPourcentage(Boolean canevasPourcentage) {
        this.canevasPourcentage = canevasPourcentage;
    }

    public void setCcpFournisseur(String ccpFournisseur) {
        this.ccpFournisseur = ccpFournisseur;
    }

    public void setClearingBanque(String clearingBanque) {
        this.clearingBanque = clearingBanque;
    }

    public void setCompte(String compte) {
        this.compte = compte;
    }

    /**
     * Mise à jour de l'id adresse paiement depuis le module pyxis.
     * 
     * @param idAvoirPaiementUnique
     */
    public void setIdAdressePaiementDepuisPyxis(String idAvoirPaiementUnique) {
        setIdAdressePaiement(idAvoirPaiementUnique);
        retourDepuisPyxis = true;

        fillWithSpecificAdressePaiement(idAvoirPaiementUnique);

    }

    @Override
    public void setIdExterneFournisseur(String idExterneFournisseur) {
        this.idExterneFournisseur = idExterneFournisseur;
    }

    public void setLectureOptique(String lectureOptique) {
        this.lectureOptique = lectureOptique;
    }

    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

    public void setShowRows(int showRows) {
        this.showRows = showRows;
    }
}
