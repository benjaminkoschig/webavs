package globaz.helios.db.comptes;

import globaz.globall.db.BStatement;
import globaz.globall.util.JAUtil;
import globaz.helios.db.interfaces.CGLibelle;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;

/**
 * juste une vue pour l'ecran des mouvement de compte, il n'est pas prevu de travailler avec cette objet Date de
 * création : (17.12.2002 13:13:01)
 * 
 * @author: Administrator
 */
public class CGExtendedMvtCompteViewBean extends CGMouvementCompteViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String codeIsoCompteContreEcriture = new String();
    private String idContreEcriture = new String();
    private String idContrePartieAvoir = new String();
    private String idContrePartieDoit = new String();
    private String idExtCtrCompte = new String();
    private String idNatureCompteContreEcriture = new String();
    private String libelleDeCtrEcrit = new String();
    private String libelleFrCtrEcrit = new String();
    private String libelleItCtrEcrit = new String();
    private String montantContreEcriture = new String();

    private String montantMonnaieContreEcriture = new String();
    private String nombreAvoir = new String();
    private String nombreDoit = new String();

    /**
     * Commentaire relatif au constructeur CGMouvementCompteViewBean.
     */
    public CGExtendedMvtCompteViewBean() {
        super();
    }

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        String result = super._getFields(statement) + ", ";

        result += _getCollection() + "CGPLANP.idExterne idExtCtrCompte, ";
        result += _getCollection() + "CGPLANP.libelleFr libelleFrCtrEcrit, ";
        result += _getCollection() + "CGPLANP.libelleDe libelleDeCtrEcrit, ";
        result += _getCollection() + "CGPLANP.libelleIt libelleItCtrEcrit, ";
        result += "CONTRE_ECRITURE.idEcriture idContreEcriture, ";
        result += "CONTRE_ECRITURE.montant montantContreEcr, ";
        result += "CONTRE_ECRITURE.montantMonnaie montantMEContreEcr, ";
        result += "CPT_CONTRE_ECRIT.idNature idNaturComptCtrEcr, ";
        result += "CPT_CONTRE_ECRIT.codeIsoMonnaie codeIsoComptCtrEcr, ";
        result += _getCollection() + "CGECREP.nombreAvoir, ";
        result += _getCollection() + "CGECREP.nombreDoit, ";
        result += _getCollection() + "CGECREP.idContrePartieDoit, ";
        result += _getCollection() + "CGECREP.idContrePartieAvoi ";

        return result;
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        super._readProperties(statement);
        idExtCtrCompte = statement.dbReadString("IDEXTCTRCOMPTE");
        libelleFrCtrEcrit = statement.dbReadString("LIBELLEFRCTRECRIT");
        libelleDeCtrEcrit = statement.dbReadString("LIBELLEDECTRECRIT");
        libelleItCtrEcrit = statement.dbReadString("LIBELLEITCTRECRIT");
        idContreEcriture = statement.dbReadNumeric("IDCONTREECRITURE");
        nombreAvoir = statement.dbReadNumeric("NOMBREAVOIR");
        nombreDoit = statement.dbReadNumeric("NOMBREDOIT");
        idContrePartieDoit = statement.dbReadNumeric("IDCONTREPARTIEDOIT");
        idContrePartieAvoir = statement.dbReadNumeric("IDCONTREPARTIEAVOI");
        idNatureCompteContreEcriture = statement.dbReadNumeric("IDNATURCOMPTCTRECR");
        codeIsoCompteContreEcriture = statement.dbReadString("CODEISOCOMPTCTRECR");
        montantContreEcriture = statement.dbReadNumeric("montantContreEcr");
        montantMonnaieContreEcriture = statement.dbReadNumeric("montantMEContreEcr");
    }

    public String getAvoirContreEcriture() {
        if (CodeSystem.CS_CREDIT.equals(getCodeDebitCredit())
                || CodeSystem.CS_EXTOURNE_CREDIT.equals(getCodeDebitCredit())) {
            return "";
        } else {
            return getMontantContreEcritureAffiche();
        }
    }

    public String getAvoirContreEcritureMonnaie() {
        if (CodeSystem.CS_CREDIT.equals(getCodeDebitCredit())
                || CodeSystem.CS_EXTOURNE_CREDIT.equals(getCodeDebitCredit())) {
            return getMontantContreEcritureAfficheMonnaie();
        } else {
            return "";
        }
    }

    /**
     * Returns the codeIsoCompteContreEcriture.
     * 
     * @return String
     */
    public String getCodeIsoCompteContreEcriture() {
        return codeIsoCompteContreEcriture;
    }

    /**
     * Renvoies la contre écriture si il ni a qu'une seule contre écriture
     * 
     * @return
     */
    public CGEcritureViewBean getContreEcriture() {
        if (getCodeDebitCredit().equals(CodeSystem.CS_CREDIT) && !JadeStringUtil.isIntegerEmpty(getNombreDoit())
                && getNombreDoit().equals("1")) {
            return getContreEcritureForCode(CodeSystem.CS_DEBIT);
        } else if (getCodeDebitCredit().equals(CodeSystem.CS_DEBIT) && !JadeStringUtil.isIntegerEmpty(getNombreAvoir())
                && getNombreAvoir().equals("1")) {
            return getContreEcritureForCode(CodeSystem.CS_CREDIT);
        } else if (getCodeDebitCredit().equals(CodeSystem.CS_EXTOURNE_CREDIT)
                && !JadeStringUtil.isIntegerEmpty(getNombreDoit()) && getNombreDoit().equals("1")) {
            return getContreEcritureForCode(CodeSystem.CS_EXTOURNE_DEBIT);
        } else if (getCodeDebitCredit().equals(CodeSystem.CS_EXTOURNE_DEBIT)
                && !JadeStringUtil.isIntegerEmpty(getNombreAvoir()) && getNombreAvoir().equals("1")) {
            return getContreEcritureForCode(CodeSystem.CS_EXTOURNE_CREDIT);
        } else {
            return null;
        }
    }

    /**
     * Retourne l'écriture de contre écriture de type débit ou crédit.
     * 
     * @param forIdCodeDebitCredit
     * @return
     */
    private CGEcritureViewBean getContreEcritureForCode(String forIdCodeDebitCredit) {
        CGEcritureListViewBean manager = new CGEcritureListViewBean();
        manager.setSession(getSession());
        manager.setForIdEnteteEcriture(getIdEnteteEcriture());
        manager.setForCodeDebitCredit(forIdCodeDebitCredit);
        try {
            manager.find();
        } catch (Exception e) {
            return null;
        }

        if (manager.hasErrors() || manager.isEmpty()) {
            return null;
        }

        return (CGEcritureViewBean) manager.getFirstEntity();
    }

    public String getDoitContreEcriture() {
        if (CodeSystem.CS_DEBIT.equals(getCodeDebitCredit())
                || CodeSystem.CS_EXTOURNE_DEBIT.equals(getCodeDebitCredit())) {
            return "";
        } else {
            return getMontantContreEcritureAffiche();
        }
    }

    public String getDoitContreEcritureMonnaie() {
        if (CodeSystem.CS_DEBIT.equals(getCodeDebitCredit())
                || CodeSystem.CS_EXTOURNE_DEBIT.equals(getCodeDebitCredit())) {
            return getMontantContreEcritureAfficheMonnaie();
        } else {
            return "";
        }
    }

    /**
     * Returns the idContreEcriture.
     * 
     * @return String
     */
    public String getIdContreEcriture() {
        return idContreEcriture;
    }

    /**
     * Returns the idContrePartieAvoir.
     * 
     * @return String
     */
    public String getIdContrePartieAvoir() {
        return idContrePartieAvoir;
    }

    /**
     * Returns the idContrePartieDoit.
     * 
     * @return String
     */
    public String getIdContrePartieDoit() {
        return idContrePartieDoit;
    }

    /**
     * Returns the idExtCtrCompte.
     * 
     * @return String
     */
    public String getIdExtCtrCompte() {
        return idExtCtrCompte;
    }

    /**
     * Returns the idNatureCompteContreEcriture.
     * 
     * @return String
     */
    public String getIdNatureCompteContreEcriture() {
        return idNatureCompteContreEcriture;
    }

    public String getLibelleCtrEcrit() {
        String langue = getSession().getIdLangueISO();
        if ("IT".equalsIgnoreCase(langue)) {
            return getLibelleItCtrEcrit();
        } else if ("DE".equalsIgnoreCase(langue)) {
            return getLibelleDeCtrEcrit();
        } else {
            return getLibelleFrCtrEcrit();
        }

    }

    /**
     * Returns the libelleDeCtrEcrit.
     * 
     * @return String
     */
    public String getLibelleDeCtrEcrit() {
        return libelleDeCtrEcrit;
    }

    /**
     * Returns the libelleFrCtrEcrit.
     * 
     * @return String
     */
    public String getLibelleFrCtrEcrit() {
        return libelleFrCtrEcrit;
    }

    /**
     * Returns the libelleItCtrEcrit.
     * 
     * @return String
     */
    public String getLibelleItCtrEcrit() {
        return libelleItCtrEcrit;
    }

    /**
     * Returns the montantContreEcriture.
     * 
     * @return String
     */
    public String getMontantContreEcriture() {
        return montantContreEcriture;
    }

    public java.lang.String getMontantContreEcritureAffiche() {
        BigDecimal bdMontant = JAUtil.createBigDecimal(montantContreEcriture);
        if (bdMontant != null) {
            if (CodeSystem.CS_DEBIT.equals(getCodeDebitCredit())
                    || CodeSystem.CS_EXTOURNE_DEBIT.equals(getCodeDebitCredit())) {
                return bdMontant.negate().toString();
            } else {
                return bdMontant.toString();
            }
        } else {
            return CGLibelle.LIBELLE_ERROR;
        }
    }

    public java.lang.String getMontantContreEcritureAfficheMonnaie() {
        BigDecimal bdMontant = JAUtil.createBigDecimal(montantMonnaieContreEcriture);
        if (bdMontant != null) {
            if (CodeSystem.CS_DEBIT.equals(getCodeDebitCredit())
                    || CodeSystem.CS_EXTOURNE_DEBIT.equals(getCodeDebitCredit())) {
                return bdMontant.negate().toString();
            } else {
                return bdMontant.toString();
            }
        } else {
            return CGLibelle.LIBELLE_ERROR;
        }
    }

    /**
     * Returns the montantMonnaieContreEcriture.
     * 
     * @return String
     */
    public String getMontantMonnaieContreEcriture() {
        return montantMonnaieContreEcriture;
    }

    /**
     * Returns the nombreAvoir.
     * 
     * @return String
     */
    public String getNombreAvoir() {
        return nombreAvoir;
    }

    /**
     * Returns the nombreDoit.
     * 
     * @return String
     */
    public String getNombreDoit() {
        return nombreDoit;
    }

    /**
     * Sets the codeIsoCompteContreEcriture.
     * 
     * @param codeIsoCompteContreEcriture
     *            The codeIsoCompteContreEcriture to set
     */
    public void setCodeIsoCompteContreEcriture(String codeIsoCompteContreEcriture) {
        this.codeIsoCompteContreEcriture = codeIsoCompteContreEcriture;
    }

    /**
     * Sets the idContreEcriture.
     * 
     * @param idContreEcriture
     *            The idContreEcriture to set
     */
    public void setIdContreEcriture(String idContreEcriture) {
        this.idContreEcriture = idContreEcriture;
    }

    /**
     * Sets the idContrePartieAvoir.
     * 
     * @param idContrePartieAvoir
     *            The idContrePartieAvoir to set
     */
    public void setIdContrePartieAvoir(String idContrePartieAvoir) {
        this.idContrePartieAvoir = idContrePartieAvoir;
    }

    /**
     * Sets the idContrePartieDoit.
     * 
     * @param idContrePartieDoit
     *            The idContrePartieDoit to set
     */
    public void setIdContrePartieDoit(String idContrePartieDoit) {
        this.idContrePartieDoit = idContrePartieDoit;
    }

    /**
     * Sets the idExtCtrCompte.
     * 
     * @param idExtCtrCompte
     *            The idExtCtrCompte to set
     */
    public void setIdExtCtrCompte(String idExtCtrCompte) {
        this.idExtCtrCompte = idExtCtrCompte;
    }

    /**
     * Sets the idNatureCompteContreEcriture.
     * 
     * @param idNatureCompteContreEcriture
     *            The idNatureCompteContreEcriture to set
     */
    public void setIdNatureCompteContreEcriture(String idNatureCompteContreEcriture) {
        this.idNatureCompteContreEcriture = idNatureCompteContreEcriture;
    }

    /**
     * Sets the libelleDeCtrEcrit.
     * 
     * @param libelleDeCtrEcrit
     *            The libelleDeCtrEcrit to set
     */
    public void setLibelleDeCtrEcrit(String libelleDeCtrEcrit) {
        this.libelleDeCtrEcrit = libelleDeCtrEcrit;
    }

    /**
     * Sets the libelleFrCtrEcrit.
     * 
     * @param libelleFrCtrEcrit
     *            The libelleFrCtrEcrit to set
     */
    public void setLibelleFrCtrEcrit(String libelleFrCtrEcrit) {
        this.libelleFrCtrEcrit = libelleFrCtrEcrit;
    }

    /**
     * Sets the libelleItCtrEcrit.
     * 
     * @param libelleItCtrEcrit
     *            The libelleItCtrEcrit to set
     */
    public void setLibelleItCtrEcrit(String libelleItCtrEcrit) {
        this.libelleItCtrEcrit = libelleItCtrEcrit;
    }

    /**
     * Sets the montantContreEcriture.
     * 
     * @param montantContreEcriture
     *            The montantContreEcriture to set
     */
    public void setMontantContreEcriture(String montantContreEcriture) {
        this.montantContreEcriture = montantContreEcriture;
    }

    /**
     * Sets the montantMonnaieContreEcriture.
     * 
     * @param montantMonnaieContreEcriture
     *            The montantMonnaieContreEcriture to set
     */
    public void setMontantMonnaieContreEcriture(String montantMonnaieContreEcriture) {
        this.montantMonnaieContreEcriture = montantMonnaieContreEcriture;
    }

    /**
     * Sets the nombreAvoir.
     * 
     * @param nombreAvoir
     *            The nombreAvoir to set
     */
    public void setNombreAvoir(String nombreAvoir) {
        this.nombreAvoir = nombreAvoir;
    }

    /**
     * Sets the nombreDoit.
     * 
     * @param nombreDoit
     *            The nombreDoit to set
     */
    public void setNombreDoit(String nombreDoit) {
        this.nombreDoit = nombreDoit;
    }

}
