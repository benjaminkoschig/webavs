package globaz.helios.db.modeles;

import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGExerciceComptableManager;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import globaz.jade.client.util.JadeStringUtil;

public class CGVBeanLigneModeleEcritureViewBean implements globaz.framework.bean.FWViewBeanInterface,
        java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private java.lang.String coursMonnaie = new String();

    private java.lang.String idCentreCharge = new String();
    private java.lang.String idCentreChargeCptCredit = new String();
    private java.lang.String idCentreChargeCptDebit = new String();
    // Ecriture collective
    private java.lang.String idCompte = new String();
    private java.lang.String idCompteCredite = new String();
    // Ecriture double
    private java.lang.String idCompteDebite = new String();
    private java.lang.String idConstantBouclement = new String();

    private java.lang.String idEnteteModeleEcriture = new String();
    private java.lang.String idLigneModeleEcriture = new String();
    private String idMandat = new String();

    private String idModeleEcriture = new String();
    private boolean isEcritureDouble = false;
    private java.lang.String libelle = new String();
    private String message = "";

    private java.lang.String montant = new String();
    private java.lang.String montantMonnaie = new String();

    private String msgType = "";
    // Commun
    private java.lang.String piece = new String();
    private BSession session = null;

    /**
     * Commentaire relatif au constructeur CGModeleEcriture
     */
    public CGVBeanLigneModeleEcritureViewBean() {
        super();
    }

    /**
     * Returns the coursMonnaie.
     * 
     * @return java.lang.String
     */
    public java.lang.String getCoursMonnaie() {
        return coursMonnaie;
    }

    /**
     * Returns the idCentreCharge.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdCentreCharge() {
        return idCentreCharge;
    }

    /**
     * Returns the idCentreChargeCptCredit.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdCentreChargeCptCredit() {
        return idCentreChargeCptCredit;
    }

    /**
     * Returns the idCentreChargeCptDebit.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdCentreChargeCptDebit() {
        return idCentreChargeCptDebit;
    }

    /**
     * Returns the idCompte.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdCompte() {
        return idCompte;
    }

    /**
     * Returns the idCompteCredite.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdCompteCredite() {
        return idCompteCredite;
    }

    /**
     * Returns the idCompteDebite.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdCompteDebite() {
        return idCompteDebite;
    }

    /**
     * Returns the idConstantBouclement.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdConstantBouclement() {
        return idConstantBouclement;
    }

    /**
     * Returns the idEnteteModeleEcriture.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdEnteteModeleEcriture() {
        return idEnteteModeleEcriture;
    }

    public String getIdExterne() {
        try {
            if (!JadeStringUtil.isIntegerEmpty(getIdCompte())) {
                CGPlanComptableViewBean pc = new CGPlanComptableViewBean();
                pc.setSession((BSession) getISession());

                CGEnteteModeleEcriture enteteModele = new CGEnteteModeleEcriture();
                enteteModele.setSession((BSession) getISession());
                enteteModele.setIdEnteteModeleEcriture(getIdEnteteModeleEcriture());
                enteteModele.retrieve();
                if (enteteModele.isNew()) {
                    return null;
                }

                CGExerciceComptableManager exManager = new CGExerciceComptableManager();
                exManager.setSession((BSession) getISession());
                exManager.setForIdMandat(enteteModele.getIdMandat());
                exManager.setOrderBy(CGExerciceComptableManager.TRI_DATE_FIN_DESC);
                exManager.find(null, 1);
                if (exManager.size() == 0) {
                    return null;
                }

                CGExerciceComptable ex = (CGExerciceComptable) exManager.getEntity(0);
                pc.setIdExerciceComptable(ex.getIdExerciceComptable());
                pc.setIdCompte(getIdCompte());
                pc.retrieve();
                return pc.getIdExterne();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getIdExterneCompteCredite() {
        try {
            if (!JadeStringUtil.isIntegerEmpty(getIdCompte())) {
                CGPlanComptableViewBean pc = new CGPlanComptableViewBean();
                pc.setSession((BSession) getISession());

                CGEnteteModeleEcriture enteteModele = new CGEnteteModeleEcriture();
                enteteModele.setSession((BSession) getISession());
                enteteModele.setIdEnteteModeleEcriture(getIdEnteteModeleEcriture());
                enteteModele.retrieve();
                if (enteteModele.isNew()) {
                    return null;
                }

                CGExerciceComptableManager exManager = new CGExerciceComptableManager();
                exManager.setSession((BSession) getISession());
                exManager.setForIdMandat(enteteModele.getIdMandat());
                exManager.setOrderBy(CGExerciceComptableManager.TRI_DATE_FIN_DESC);
                exManager.find(null, 1);
                if (exManager.size() == 0) {
                    return null;
                }

                CGExerciceComptable ex = (CGExerciceComptable) exManager.getEntity(0);
                pc.setIdExerciceComptable(ex.getIdExerciceComptable());
                pc.setIdCompte(getIdCompteCredite());
                pc.retrieve();
                return pc.getIdExterne();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getIdExterneCompteDebit() {
        try {
            if (!JadeStringUtil.isIntegerEmpty(getIdCompte())) {
                CGPlanComptableViewBean pc = new CGPlanComptableViewBean();
                pc.setSession((BSession) getISession());

                CGEnteteModeleEcriture enteteModele = new CGEnteteModeleEcriture();
                enteteModele.setSession((BSession) getISession());
                enteteModele.setIdEnteteModeleEcriture(getIdEnteteModeleEcriture());
                enteteModele.retrieve();
                if (enteteModele.isNew()) {
                    return null;
                }

                CGExerciceComptableManager exManager = new CGExerciceComptableManager();
                exManager.setSession((BSession) getISession());
                exManager.setForIdMandat(enteteModele.getIdMandat());
                exManager.setOrderBy(CGExerciceComptableManager.TRI_DATE_FIN_DESC);
                exManager.find(null, 1);
                if (exManager.size() == 0) {
                    return null;
                }

                CGExerciceComptable ex = (CGExerciceComptable) exManager.getEntity(0);
                pc.setIdExerciceComptable(ex.getIdExerciceComptable());
                pc.setIdCompte(getIdCompteDebite());
                pc.retrieve();
                return pc.getIdExterne();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns the idLigneModeleEcriture.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdLigneModeleEcriture() {
        return idLigneModeleEcriture;
    }

    /**
     * Returns the idMandat.
     * 
     * @return String
     */
    public String getIdMandat() {
        return idMandat;
    }

    /**
     * Returns the idModeleEcriture.
     * 
     * @return String
     */
    public String getIdModeleEcriture() {
        return idModeleEcriture;
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#getISession()
     */
    @Override
    public BISession getISession() {
        return session;
    }

    public CGExerciceComptable getLastExercice() {
        CGExerciceComptableManager mgr = new CGExerciceComptableManager();
        mgr.setForIdMandat(getIdMandat());
        mgr.setSession((BSession) getISession());
        mgr.setOrderBy(CGExerciceComptableManager.TRI_DATE_FIN_DESC);
        try {
            mgr.find(null, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mgr.size() > 0) {
            return (CGExerciceComptable) mgr.getEntity(0);
        } else {
            return null;
        }
    }

    /**
     * Returns the libelle.
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelle() {
        return libelle;
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#getMessage()
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * Returns the montant.
     * 
     * @return java.lang.String
     */
    public java.lang.String getMontant() {
        return montant;
    }

    /**
     * Returns the montantMonnaie.
     * 
     * @return java.lang.String
     */
    public java.lang.String getMontantMonnaie() {
        return montantMonnaie;
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#getMsgType()
     */
    @Override
    public String getMsgType() {
        return msgType;
    }

    /**
     * Returns the piece.
     * 
     * @return java.lang.String
     */
    public java.lang.String getPiece() {
        return piece;
    }

    /**
     * Returns the session.
     * 
     * @return BSession
     */
    public BSession getSession() {
        return session;
    }

    public BSpy getSpy() {
        return null;
    }

    /**
     * Returns the isEcritureDouble.
     * 
     * @return boolean
     */
    public boolean isEcritureDouble() {
        return isEcritureDouble;
    }

    public CGModeleEcriture retrieveModeleEcriture() {

        if (JadeStringUtil.isBlank(getIdModeleEcriture())) {
            return null;
        } else if (JadeStringUtil.isBlank(getIdMandat())) {
            return null;
        } else {
            CGModeleEcriture modele = new CGModeleEcriture();
            modele.setSession((BSession) getISession());
            modele.setIdModeleEcriture(getIdModeleEcriture());
            modele.setIdMandat(getIdMandat());
            try {
                modele.retrieve();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return modele;
        }
    }

    /**
     * Sets the coursMonnaie.
     * 
     * @param coursMonnaie
     *            The coursMonnaie to set
     */
    public void setCoursMonnaie(java.lang.String coursMonnaie) {
        this.coursMonnaie = coursMonnaie;
    }

    /**
     * Sets the idCentreCharge.
     * 
     * @param idCentreCharge
     *            The idCentreCharge to set
     */
    public void setIdCentreCharge(java.lang.String idCentreCharge) {
        this.idCentreCharge = idCentreCharge;
    }

    /**
     * Sets the idCentreChargeCptCredit.
     * 
     * @param idCentreChargeCptCredit
     *            The idCentreChargeCptCredit to set
     */
    public void setIdCentreChargeCptCredit(java.lang.String idCentreChargeCptCredit) {
        this.idCentreChargeCptCredit = idCentreChargeCptCredit;
    }

    /**
     * Sets the idCentreChargeCptDebit.
     * 
     * @param idCentreChargeCptDebit
     *            The idCentreChargeCptDebit to set
     */
    public void setIdCentreChargeCptDebit(java.lang.String idCentreChargeCptDebit) {
        this.idCentreChargeCptDebit = idCentreChargeCptDebit;
    }

    /**
     * Sets the idCompte.
     * 
     * @param idCompte
     *            The idCompte to set
     */
    public void setIdCompte(java.lang.String idCompte) {
        this.idCompte = idCompte;
    }

    /**
     * Sets the idCompteCredite.
     * 
     * @param idCompteCredite
     *            The idCompteCredite to set
     */
    public void setIdCompteCredite(java.lang.String idCompteCredite) {
        this.idCompteCredite = idCompteCredite;
    }

    /**
     * Sets the idCompteDebite.
     * 
     * @param idCompteDebite
     *            The idCompteDebite to set
     */
    public void setIdCompteDebite(java.lang.String idCompteDebite) {
        this.idCompteDebite = idCompteDebite;
    }

    /**
     * Sets the idConstantBouclement.
     * 
     * @param idConstantBouclement
     *            The idConstantBouclement to set
     */
    public void setIdConstantBouclement(java.lang.String idConstantBouclement) {
        this.idConstantBouclement = idConstantBouclement;
    }

    /**
     * Sets the idEnteteModeleEcriture.
     * 
     * @param idEnteteModeleEcriture
     *            The idEnteteModeleEcriture to set
     */
    public void setIdEnteteModeleEcriture(java.lang.String idEnteteModeleEcriture) {
        this.idEnteteModeleEcriture = idEnteteModeleEcriture;
    }

    /**
     * Sets the idLigneModeleEcriture.
     * 
     * @param idLigneModeleEcriture
     *            The idLigneModeleEcriture to set
     */
    public void setIdLigneModeleEcriture(java.lang.String idLigneModeleEcriture) {
        this.idLigneModeleEcriture = idLigneModeleEcriture;
    }

    /**
     * Sets the idMandat.
     * 
     * @param idMandat
     *            The idMandat to set
     */
    public void setIdMandat(String idMandat) {
        this.idMandat = idMandat;
    }

    /**
     * Sets the idModeleEcriture.
     * 
     * @param idModeleEcriture
     *            The idModeleEcriture to set
     */
    public void setIdModeleEcriture(String idModeleEcriture) {
        this.idModeleEcriture = idModeleEcriture;
    }

    /**
     * Sets the isEcritureDouble.
     * 
     * @param isEcritureDouble
     *            The isEcritureDouble to set
     */
    public void setIsEcritureDouble(boolean isEcritureDouble) {
        this.isEcritureDouble = isEcritureDouble;
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#setISession(BISession)
     */
    @Override
    public void setISession(BISession newSession) {
        if (newSession instanceof BSession) {
            setSession((BSession) newSession);
        } else {
            try {
                setSession(new BSession(newSession.getApplicationId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Sets the libelle.
     * 
     * @param libelle
     *            The libelle to set
     */
    public void setLibelle(java.lang.String libelle) {
        this.libelle = libelle;
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#setMessage(String)
     */
    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Sets the montant.
     * 
     * @param montant
     *            The montant to set
     */
    public void setMontant(java.lang.String montant) {
        this.montant = montant;
    }

    /**
     * Sets the montantMonnaie.
     * 
     * @param montantMonnaie
     *            The montantMonnaie to set
     */
    public void setMontantMonnaie(java.lang.String montantMonnaie) {
        this.montantMonnaie = montantMonnaie;
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#setMsgType(String)
     */
    @Override
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    /**
     * Sets the piece.
     * 
     * @param piece
     *            The piece to set
     */
    public void setPiece(java.lang.String piece) {
        this.piece = piece;
    }

    /**
     * Modifie la session en cours
     * 
     * @param newSession
     *            la nouvelle session
     */
    public void setSession(BSession newSession) {
        session = newSession;
    }

}
