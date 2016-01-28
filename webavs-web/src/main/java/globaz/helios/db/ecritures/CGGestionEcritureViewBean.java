package globaz.helios.db.ecritures;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSpy;
import globaz.globall.util.JANumberFormatter;
import globaz.helios.db.comptes.CGEcritureViewBean;
import globaz.helios.db.comptes.CGEnteteEcritureViewBean;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import globaz.helios.db.interfaces.CGLibelle;
import globaz.helios.db.lynx.fournisseur.CGLXFournisseur;
import globaz.helios.db.lynx.section.CGLXSection;
import globaz.helios.helpers.ecritures.utils.CGGestionEcritureUtils;
import globaz.jade.client.util.JadeStringUtil;

public class CGGestionEcritureViewBean extends CGGestionEcriture implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_MAX_ROWS = 19;
    private static final int DEFAULT_SHOW_ROWS = 2;
    private static final String PROPERTY_MAX_ROWS = "gestionEcrituresMaxRows";

    private static final String PROPERTY_SHOW_CENTRE_CHARGE = "gestionEcrituresShowRows";
    private static final String PROPERTY_SHOW_ROWS = "gestionEcrituresShowRows";

    private boolean centreChargeAffiche = false;
    private int maxRows = 0;

    private boolean montantEtrangerAffiche = false;
    private int showRows = 0;

    /**
     * @return the centreChargeAffiche
     */
    public boolean getCentreChargeAffiche() {
        return centreChargeAffiche;
    }

    /**
     * @param i
     * @return
     */
    public String getCours(int i) {
        String result = "0.00000";

        if (getEcritures().size() > i) {
            if (!JadeStringUtil.isDecimalEmpty(((CGEcritureViewBean) getEcritures().get(i)).getCoursMonnaie())) {
                result = ((CGEcritureViewBean) getEcritures().get(i)).getCoursMonnaie();
            }
        }

        return JANumberFormatter.fmt(result, false, true, false, 5);
    }

    /**
     * @param i
     * @return
     */
    public String getIdCompte(int i) {
        if (getEcritures().size() > i) {
            return ((CGEcritureViewBean) getEcritures().get(i)).getIdCompte();
        } else {
            return "";
        }
    }

    /**
     * @param i
     * @return
     */
    public String getIdCompteCharge(int i) {
        if (getEcritures().size() > i) {
            return ((CGEcritureViewBean) getEcritures().get(i)).getIdCentreCharge();
        } else {
            return "";
        }
    }

    /**
     * @param i
     * @return
     */
    public String getIdEcriture(int i) {
        if (getEcritures().size() > i) {
            return ((CGEcritureViewBean) getEcritures().get(i)).getIdEcriture();
        } else {
            return "";
        }
    }

    /**
     * @param i
     * @return
     */
    public String getIdExt(int i) {
        if (getEcritures().size() > i) {
            return ((CGEcritureViewBean) getEcritures().get(i)).getIdExterneCompte();
        } else {
            return "";
        }
    }

    /**
     * Return l'id externe du fournisseur si l'entête d'écriture contient une id fournisseur. <br/>
     * Si non, return un string vide.
     * 
     * @return
     */
    public String getIdExterneFournisseur() {
        if (JadeStringUtil.isIntegerEmpty(getIdFournisseur())) {
            return "";
        }

        try {
            CGLXFournisseur fournisseur = new CGLXFournisseur();
            fournisseur.setSession(getSession());

            fournisseur.setIdFournisseur(getIdFournisseur());

            fournisseur.retrieve();

            if (fournisseur.hasErrors() || fournisseur.isNew()) {
                return "";
            }

            return fournisseur.getIdExterne();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Return l'id externe de la section du fournisseur si l'entête d'écriture contient une id section. <br/>
     * Si non, return un string vide.
     * 
     * @return
     */
    public String getIdExterneSection() {
        if (JadeStringUtil.isIntegerEmpty(getIdSection())) {
            return "";
        }

        try {
            CGLXSection section = new CGLXSection();
            section.setSession(getSession());

            section.setIdSection(getIdSection());

            section.retrieve();

            if (section.hasErrors() || section.isNew()) {
                return "";
            }

            return section.getIdExterne();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @param i
     * @return
     */
    public String getLibelle(int i) {
        if (getEcritures().size() > i) {
            String result = ((CGEcritureViewBean) getEcritures().get(i)).getLibelle();
            result = JadeStringUtil.change(result, "\"", "&quot;");
            return result;
        } else {
            return "";
        }
    }

    /**
     * @param i
     * @return
     */
    public String getLibelleCompte(int i) {
        String retour = "";

        if (getEcritures().size() > i) {
            String result = ((CGEcritureViewBean) getEcritures().get(i)).getLibelleCompte();
            result = JadeStringUtil.change(result, "\"", "&quot;");
            retour = result;

            if (JadeStringUtil.isBlank(retour)) {
                CGPlanComptableViewBean compte = new CGPlanComptableViewBean();
                compte.setSession(getSession());
                compte.setIdCompte(getIdCompte(i));
                compte.setIdExerciceComptable(((CGEcritureViewBean) getEcritures().get(i)).getIdExerciceComptable());
                try {
                    compte.retrieve();
                } catch (Exception e) {
                    return "";
                }
                if (!compte.hasErrors() && !compte.isNew()) {
                    retour = CGLibelle.getLibelleApp(compte);
                }
            }
        }
        return retour;
    }

    /**
     * @return
     */
    public int getMaxRows() {
        try {
            if (maxRows == 0) {
                maxRows = Integer.parseInt(getSession().getApplication().getProperty(PROPERTY_MAX_ROWS).trim());
            }
        } catch (Exception e) {
            return DEFAULT_MAX_ROWS;
        }

        return maxRows;
    }

    /**
     * @param i
     * @return
     */
    public String getMontantCrebit(int i) {
        String result = "";
        if (i == 1) {
            result = "0.00";
        }

        if (getEcritures().size() > i) {
            if (((CGEcritureViewBean) getEcritures().get(i)).isAvoir()
                    && !JadeStringUtil.isDecimalEmpty(((CGEcritureViewBean) getEcritures().get(i)).getMontantBase())) {
                result = ((CGEcritureViewBean) getEcritures().get(i)).getMontantAffiche();
            }
        }

        return JANumberFormatter.fmt(result, true, true, false, 2);
    }

    /**
     * @param i
     * @return
     */
    public String getMontantDebit(int i) {
        String result = "";
        if (i == 0) {
            result = "0.00";
        }

        if (getEcritures().size() > i) {
            if (((CGEcritureViewBean) getEcritures().get(i)).isDoit()
                    && !JadeStringUtil.isDecimalEmpty(((CGEcritureViewBean) getEcritures().get(i)).getMontantBase())) {
                result = ((CGEcritureViewBean) getEcritures().get(i)).getMontantBase();
            }
        }

        return JANumberFormatter.fmt(result, true, true, false, 2);
    }

    /**
     * @param i
     * @return
     */
    public String getMontantEtranger(int i) {
        String result = "0.00";

        if (getEcritures().size() > i) {
            if (!JadeStringUtil.isDecimalEmpty(((CGEcritureViewBean) getEcritures().get(i)).getMontantBaseMonnaie())) {
                result = ((CGEcritureViewBean) getEcritures().get(i)).getMontantAfficheMonnaie();
            }
        }

        return JANumberFormatter.fmt(result, true, true, false, 2);
    }

    /**
     * @return the montantEtrangerAffiche
     */
    public boolean getMontantEtrangerAffiche() {
        return montantEtrangerAffiche;
    }

    /**
     * @return
     */
    public int getShowRows() {
        try {
            if (showRows == 0) {
                showRows = Integer.parseInt(getSession().getApplication().getProperty(PROPERTY_SHOW_ROWS).trim());
            }
        } catch (Exception e) {
            return DEFAULT_SHOW_ROWS;
        }

        return showRows;
    }

    /**
     * Return le spy pour l'affichage de l'écran.
     */
    @Override
    public BSpy getSpy() {
        if (!JadeStringUtil.isIntegerEmpty(getIdEnteteEcriture())) {
            try {
                return CGGestionEcritureUtils.getEntete(getSession(), null, getIdEnteteEcriture()).getSpy();
            } catch (Exception e) {
                // Do nothing.
            }
        }

        return null;
    }

    /**
     * Lors de l'édition des écritures ne pas permettre l'édition d'écritures de dette et avoir.
     * 
     * @return
     */
    public boolean isDetteAvoir() {
        try {
            if (!JadeStringUtil.isIntegerEmpty(getIdEnteteEcriture())) {
                CGEnteteEcritureViewBean entete = new CGEnteteEcritureViewBean();
                entete.setSession(getSession());

                entete.setIdEnteteEcriture(getIdEnteteEcriture());

                entete.retrieve();

                if (entete.hasErrors() || entete.isNew()) {
                    return false;
                }

                return CGEcritureViewBean.CS_TYPE_ECRITURE_DETTE_AVOIR.equals(entete.getIdTypeEcriture());
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @return
     */
    public boolean isJournalEditable() {
        try {
            retrieveJournal();

            try {
                CGGestionEcritureUtils.testSaisieAutresUtilisateurs(getSession(), getJournal());
            } catch (Exception e) {
                return false;
            }

            return (getJournal() != null && !getJournal().isNew() && getJournal().isOuvert());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @return
     */
    public boolean isShowCentreCharge() {
        try {
            return Boolean.valueOf(getSession().getApplication().getProperty(PROPERTY_SHOW_CENTRE_CHARGE, "false"))
                    .booleanValue();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param centreChargeAffiche
     *            the centreChargeAffiche to set
     */
    public void setCentreChargeAffiche(boolean centreChargeAffiche) {
        this.centreChargeAffiche = centreChargeAffiche;
    }

    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

    /**
     * @param montantEtrangerAffiche
     *            the montantEtrangerAffiche to set
     */
    public void setMontantEtrangerAffiche(boolean montantEtrangerAffiche) {
        this.montantEtrangerAffiche = montantEtrangerAffiche;
    }

    public void setShowRows(int showRows) {
        this.showRows = showRows;
    }
}
