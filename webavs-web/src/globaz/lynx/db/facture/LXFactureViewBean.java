package globaz.lynx.db.facture;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSpy;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.globall.util.JANumberFormatter;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.application.LXApplication;
import globaz.lynx.db.journal.LXJournal;
import globaz.lynx.db.societesdebitrice.LXSocieteDebitriceViewBean;
import globaz.lynx.helpers.utils.LXHelperUtils;
import globaz.lynx.service.tiers.LXTiersService;
import globaz.lynx.utils.LXUtils;
import globaz.pyxis.adresse.datasource.TIAbstractAdressePaiementDataSource;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.TIAdresseFormater;

public class LXFactureViewBean extends LXFacture implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final Object[] METHODES_ADRESSE_PAIEMENT = new Object[] { new String[] {
            "setIdAdressePaiementDepuisPyxis", "idAdressePaiement" } };

    private static final String PROPERTY_MAX_ROWS = "gestionEcrituresMaxRows";

    private static final String PROPERTY_SHOW_ROWS = "gestionEcrituresShowRows";
    private String adresseBanque = "";

    private String adressePaiementFournisseur = "";
    private String ccpFournisseur = "";

    private String clearingBanque = "";
    private String compte = "";
    private String idExterneFournisseur = "";
    private LXJournal journal = null;
    private String lectureOptique = "";
    private int maxRows = 0;

    private String nomBanque = "";

    private boolean retourDepuisPyxis = false;

    private int showRows = 0;

    private LXSocieteDebitriceViewBean societe = null;
    private FWParametersUserCode ucEtat = null;

    /**
     * Parse le bean des informations spécifiques de l'adresse de paiement.
     * 
     * @param idAdressePaiement
     */
    public void fillWithSpecificAdressePaiement(String idAdressePaiement) {
        try {
            TIAdressePaiementDataSource dataSource = LXTiersService.getAdresseFournisseurPaiementAsDataSource(
                    getSession(), idAdressePaiement);

            if (dataSource == null) {
                throw new Exception("Erreur ! L'adresse de paiement n'existe pas !! " + idAdressePaiement);
            }

            String tmpCCP = "" + dataSource.getData().get(TIAbstractAdressePaiementDataSource.ADRESSEP_VAR_CCP);
            if (JadeStringUtil.isBlank(tmpCCP)
                    && !JadeStringUtil.isBlank(""
                            + dataSource.getData().get(TIAbstractAdressePaiementDataSource.ADRESSEP_VAR_BANQUE_CCP))) {
                tmpCCP = "" + dataSource.getData().get(TIAbstractAdressePaiementDataSource.ADRESSEP_VAR_BANQUE_CCP);
            }
            setCcpFournisseur(tmpCCP);

            TIAdresseFormater formater = new TIAdresseFormater();

            setAdressePaiementFournisseur(formater.format(dataSource));

            setAdresseBanque(LXUtils.formateBanque(dataSource));
            setClearingBanque(dataSource.getData().get(TIAbstractAdressePaiementDataSource.ADRESSEP_VAR_CLEARING));
            setCompte(dataSource.getData().get(TIAbstractAdressePaiementDataSource.ADRESSEP_VAR_COMPTE));

            setNomBanque(dataSource.getData().get(TIAbstractAdressePaiementDataSource.ADRESSEP_VAR_BANQUE_D1) + " \n"
                    + dataSource.getData().get(TIAbstractAdressePaiementDataSource.ADRESSEP_VAR_COMPTE));
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
            return (getVentilations().get(i)).getIdVentilation();
        } else {
            return "";
        }
    }

    public LXJournal getJournal() {
        if ((journal == null) && !JadeStringUtil.isIntegerEmpty(getIdJournal())) {
            retrieveJournal();
        }
        return journal;
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
                        .getProperty(LXFactureViewBean.PROPERTY_MAX_ROWS).trim());
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
        return LXFactureViewBean.METHODES_ADRESSE_PAIEMENT;
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

    public String getNomBanque() {
        return nomBanque;
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
                        .getProperty(LXFactureViewBean.PROPERTY_SHOW_ROWS).trim());
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
    public LXSocieteDebitriceViewBean getSociete() {
        retrieveSociete();

        return societe;
    }

    /**
     * Return le spy pour l'affichage de l'écran.
     */
    @Override
    public BSpy getSpy() {
        if (!JadeStringUtil.isIntegerEmpty(getIdOperation())) {
            try {
                return LXHelperUtils.getOperation(getSession(), null, getIdOperation()).getSpy();
            } catch (Exception e) {
                // Do nothing.
            }
        }

        return null;
    }

    /**
     * Get libellé de l'état de la facture
     * 
     * @return
     */
    public FWParametersUserCode getUcEtat() {
        return this.getUcEtat(null);
    }

    /**
     * Get libellé de l'état de la facture
     * 
     * @param code
     * @return
     */
    public FWParametersUserCode getUcEtat(String code) {

        if ((ucEtat == null) || ((ucEtat != null) && !JadeStringUtil.isIntegerEmpty(code))) {
            // liste pas encore chargee, on la charge
            ucEtat = new FWParametersUserCode();
            ucEtat.setSession(getSession());
            // Récupérer le code système dans la langue de l'utilisateur
            if (!JadeStringUtil.isIntegerEmpty(getCsEtat()) || !JadeStringUtil.isIntegerEmpty(code)) {
                ucEtat.setIdCodeSysteme(JadeStringUtil.isIntegerEmpty(code) ? getCsEtat() : code);
                ucEtat.setIdLangue(getSession().getIdLangue());
                try {
                    ucEtat.retrieve();
                    if (ucEtat.isNew()) {
                        _addError(null, getSession().getLabel("CHARGEMENT_CODE_UTILISATEUR"));
                    }
                } catch (Exception e) {
                    _addError(null, getSession().getLabel("CHARGEMENT_CODE_UTILISATEUR"));
                }
            }
        }

        return ucEtat;
    }

    public boolean isJournalAnnule() {
        try {
            return ((getJournal() != null) && getJournal().isAnnule());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isJournalEditable() {
        try {
            return ((getJournal() != null) && !getJournal().isNew() && getJournal().isOuvert());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isRetourDepuisPyxis() {
        return retourDepuisPyxis;
    }

    public LXJournal retrieveJournal() {
        if ((journal == null) && !JadeStringUtil.isIntegerEmpty(getIdJournal())) {
            journal = new LXJournal();
            journal.setSession(getSession());

            journal.setIdJournal(getIdJournal());

            try {
                journal.retrieve();
            } catch (Exception e) {
                // Do nothing;
            }
        }

        return journal;
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

    public void setAdresseBanque(String adresseBanque) {
        this.adresseBanque = adresseBanque;
    }

    public void setAdressePaiementFournisseur(String adressePaiementFournisseur) {
        this.adressePaiementFournisseur = adressePaiementFournisseur;
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

    public void setJournal(LXJournal journal) {
        this.journal = journal;
    }

    public void setLectureOptique(String lectureOptique) {
        this.lectureOptique = lectureOptique;
    }

    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

    public void setNomBanque(String nomBanque) {
        this.nomBanque = nomBanque;
    }

    public void setRetourDepuisPyxis(boolean retourDepuisPyxis) {
        this.retourDepuisPyxis = retourDepuisPyxis;
    }

    public void setShowRows(int showRows) {
        this.showRows = showRows;
    }

}
