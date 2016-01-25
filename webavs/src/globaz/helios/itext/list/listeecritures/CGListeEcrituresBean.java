package globaz.helios.itext.list.listeecritures;

import globaz.helios.db.comptes.CGCompte;
import globaz.helios.db.comptes.CGExtendedEcriture;
import globaz.helios.translation.CodeSystem;
import globaz.jade.log.JadeLogger;

/**
 * Classe : CGListeEcrituresBean
 * 
 * Description :Classe de mapping entre les paramètres du document et les attributs JAVA.
 * 
 * Date de création: 28 oct. 03
 * 
 * @author scr
 * 
 */
public class CGListeEcrituresBean {

    private String COL_1 = null;
    private Double COL_10 = null;
    private Double COL_11 = null;
    private String COL_2 = null;
    private String COL_3 = null;
    private String COL_4 = null;
    private String COL_5 = null;
    private String COL_6 = null;
    private String COL_7 = null;
    private String COL_8 = null;
    private String COL_9 = null;

    private String idEcriture = null;

    public CGListeEcrituresBean() {
    }

    public CGListeEcrituresBean(CGExtendedEcriture entity, String langue, boolean isUtiliserLivre) {
        initValues(entity, langue, isUtiliserLivre);
    }

    /**
     * Retourne la date de l'écriture
     * 
     * @return String la date de l'écriture
     */
    public String getCOL_1() {
        return COL_1;
    }

    /**
     * Returns the cOL_10.
     * 
     * @return Double
     */
    public Double getCOL_10() {
        return COL_10;
    }

    /**
     * Returns the cOL_11.
     * 
     * @return Double
     */
    public Double getCOL_11() {
        return COL_11;
    }

    /**
     * Retourne Numéro du journal
     * 
     * @return String Numéro du journal
     */
    public String getCOL_2() {
        return COL_2;
    }

    /**
     * Retourne description du compte
     * 
     * @return String description du compte
     */
    public String getCOL_3() {
        return COL_3;
    }

    /**
     * Retourne libellé de l'écriture
     * 
     * @return String libellé de l'écriture
     */
    public String getCOL_4() {
        return COL_4;
    }

    /**
     * Retourne numéro de pièce
     * 
     * @return String numéro de pièce
     */
    public String getCOL_5() {
        return COL_5;
    }

    /**
     * Retourne le livre
     * 
     * @return String le livre
     */
    public String getCOL_6() {
        return COL_6;
    }

    /**
     * Retourne la période comptable
     * 
     * @return String la période comptable
     */
    public String getCOL_7() {
        return COL_7;
    }

    /**
     * Retourne le code iso de la monnaie étrangère
     * 
     * @return String le code iso de la monnaie étrangère
     */
    public String getCOL_8() {
        return COL_8;
    }

    /**
     * Retourne le cours
     * 
     * @return String le cours
     */
    public String getCOL_9() {
        return COL_9;
    }

    /**
     * Retourne idEcriture.
     * 
     * @return String idEcriture
     */
    public String getIdEcriture() {
        return idEcriture;
    }

    public void initValues(CGExtendedEcriture entity, String langue, boolean isUtiliserLivre) {
        try {
            setCOL_1(entity.getDate());
            setCOL_2(entity.getNumeroJournal() + "/" + entity.getIdEcriture());

            // Le compte est un compte de genre Centre de Charge
            if (CGCompte.CS_GENRE_CHARGE.equals(entity.getIdGenre())) {

                String libellePlanComptable = "";
                String libelleCentreCharge = "";
                if ("IT".equalsIgnoreCase(langue)) {
                    libelleCentreCharge = entity.getLibelleItCentreCharge();
                    libellePlanComptable = entity.getLibelleItPlanComptable();
                } else if ("DE".equalsIgnoreCase(langue)) {
                    libelleCentreCharge = entity.getLibelleDeCentreCharge();
                    libellePlanComptable = entity.getLibelleDePlanComptable();
                } else {
                    libelleCentreCharge = entity.getLibelleFrCentreCharge();
                    libellePlanComptable = entity.getLibelleFrPlanComptable();
                }
                setCOL_3(entity.getIdExternePlanComptable() + "/" + entity.getIdCentreCharge() + " "
                        + libellePlanComptable + "-" + libelleCentreCharge);

            } else {
                setCOL_3(entity.getIdExternePlanComptable() + "/" + entity.getIdEcriture());
            }

            setCOL_4(entity.getLibelle());
            setCOL_5(entity.getPiece());
            if (isUtiliserLivre) {
                setCOL_6(entity.getIdLivre());
            }

            setCOL_7(entity.getCodePeriodeComptable());

            if (CGCompte.CS_MONNAIE_ETRANGERE.equals(entity.getIdNature())) {
                setCOL_8(entity.getCodeIsoMonnaieCompte());
                setCOL_9(entity.getCoursMonnaie());
            }

            String codeDebitCredit = entity.getCodeDebitCredit();
            if (CodeSystem.CS_DEBIT.equals(codeDebitCredit) || CodeSystem.CS_EXTOURNE_DEBIT.equals(codeDebitCredit)) {
                setCOL_10(new Double(entity.getMontant()));
            }

            if (CodeSystem.CS_CREDIT.equals(codeDebitCredit) || CodeSystem.CS_EXTOURNE_CREDIT.equals(codeDebitCredit)) {
                setCOL_11(new Double(entity.getMontant()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JadeLogger.error(this, e);
        }
    }

    /**
     * Sets la date de l'écriture
     * 
     * @param cOL1
     *            Date de l'écriture
     */
    public void setCOL_1(String cOL1) {
        COL_1 = cOL1;
    }

    /**
     * Sets the cOL_10.
     * 
     * @param cOL_10
     *            The cOL_10 to set
     */
    public void setCOL_10(Double cOL_10) {
        COL_10 = cOL_10;
    }

    /**
     * Sets the cOL_11.
     * 
     * @param cOL_11
     *            The cOL_11 to set
     */
    public void setCOL_11(Double cOL_11) {
        COL_11 = cOL_11;
    }

    /**
     * Sets Numéro du journal
     * 
     * @param cOL_2
     *            Numéro du journal à setté
     */
    public void setCOL_2(String cOL_2) {
        COL_2 = cOL_2;
    }

    /**
     * Sets La description du compte
     * 
     * @param cOL_3
     *            La description du compte à setter
     */
    public void setCOL_3(String cOL_3) {
        COL_3 = cOL_3;
    }

    /**
     * Sets Le libellé de l'écriture
     * 
     * @param cOL_4
     *            Le libellé de l'écriture à setter
     */
    public void setCOL_4(String cOL_4) {
        COL_4 = cOL_4;
    }

    /**
     * Sets Le numéro de pièce à setter
     * 
     * @param cOL_5
     *            Le numéro de pièce à setter
     */
    public void setCOL_5(String cOL_5) {
        COL_5 = cOL_5;
    }

    /**
     * Sets Le livre
     * 
     * @param cOL_6
     *            Le livre à setter
     */
    public void setCOL_6(String cOL_6) {
        COL_6 = cOL_6;
    }

    /**
     * Sets La période comptable
     * 
     * @param cOL_7
     *            La période comptable à setter
     */
    public void setCOL_7(String cOL_7) {
        COL_7 = cOL_7;
    }

    /**
     * Sets Le code iso de la monnaie étrangère
     * 
     * @param cOL_8
     *            Le code iso de la monnaie étrangère à setter
     */
    public void setCOL_8(String cOL_8) {
        COL_8 = cOL_8;
    }

    /**
     * Sets le cours
     * 
     * @param cOL_9
     *            Le cours à setter
     */
    public void setCOL_9(String cOL_9) {
        COL_9 = cOL_9;
    }

    /**
     * Sets idEcriture.
     * 
     * @param idEcriture
     *            L'idEcriture to set
     */
    public void setIdEcriture(String idEcriture) {
        this.idEcriture = idEcriture;
    }

}
