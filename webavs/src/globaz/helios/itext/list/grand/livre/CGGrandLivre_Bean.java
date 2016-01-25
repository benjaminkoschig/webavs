package globaz.helios.itext.list.grand.livre;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAUtil;
import globaz.helios.db.comptes.CGCompte;
import globaz.helios.db.comptes.CGExtendedGrandLivre;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.db.comptes.CGSolde;
import globaz.helios.translation.CodeSystem;
import java.math.BigDecimal;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class CGGrandLivre_Bean {

    protected String col1 = null;
    protected Double col10 = null;
    protected Double col10Italic = null;
    protected Double col11 = null;
    protected Double col11Italic = null;

    protected String col12 = null;
    protected String col13 = null;
    protected String col14 = null;
    protected Double col15 = null;
    protected String col2 = null;
    protected String col3 = null;
    protected String col4 = null;
    protected String col5 = null;
    protected String col7 = null;
    protected Double col8 = null;
    protected String col8a = null;
    protected String col9 = null;
    protected Boolean colDisplayOnlySoldeANouveau = null;

    protected Boolean colProvisoire = null;

    private String idPeriodeForSoldeANouveau;

    /**
     * Constructor for CGGrandLivre_Bean.
     */
    public CGGrandLivre_Bean() {
        super();
    }

    /**
     * Returns the col1.
     * 
     * @return String
     */
    public String getCOL_1() {
        return col1;
    }

    /**
     * Returns the col10.
     * 
     * @return Double
     */
    public Double getCOL_10() {
        return col10;
    }

    /**
     * Returns the col10Italic.
     * 
     * @return Double
     */
    public Double getCOL_10_ITALIC() {
        return col10Italic;
    }

    /**
     * Returns the col11.
     * 
     * @return Double
     */
    public Double getCOL_11() {
        return col11;
    }

    /**
     * Returns the col11Italic.
     * 
     * @return Double
     */
    public Double getCOL_11_ITALIC() {
        return col11Italic;
    }

    /**
     * Returns the col12.
     * 
     * @return String
     */
    public String getCOL_12() {
        return col12;
    }

    /**
     * Returns the col13.
     * 
     * @return String
     */
    public String getCOL_13() {
        return col13;
    }

    /**
     * Returns the col14.
     * 
     * @return String
     */
    public String getCOL_14() {
        return col14;
    }

    /**
     * Returns the col15.
     * 
     * @return String
     */
    public Double getCOL_15() {
        return col15;
    }

    /**
     * Returns the col2.
     * 
     * @return String
     */
    public String getCOL_2() {
        return col2;
    }

    /**
     * Returns the col3.
     * 
     * @return String
     */
    public String getCOL_3() {
        return col3;
    }

    /**
     * Returns the col4.
     * 
     * @return String
     */
    public String getCOL_4() {
        return col4;
    }

    /**
     * Returns the col5.
     * 
     * @return String
     */
    public String getCOL_5() {
        return col5;
    }

    /**
     * Returns the col7.
     * 
     * @return String
     */
    public String getCOL_7() {
        return col7;
    }

    /**
     * Returns the col8.
     * 
     * @return String
     */
    public Double getCOL_8() {
        return col8;
    }

    public String getCOL_8A() {
        return col8a;
    }

    /**
     * Returns the col9.
     * 
     * @return String
     */
    public String getCOL_9() {
        return col9;
    }

    /**
     * Returns the colProvisoire.
     * 
     * @return String
     */
    public Boolean getCOL_PROVISOIRE() {
        return colProvisoire;
    }

    public Boolean getDISPLAY_ONLY_SOLDE_A_NOUVEAU() {
        return colDisplayOnlySoldeANouveau;
    }

    public String getIdPeriodeForSoldeANouveau() {
        return idPeriodeForSoldeANouveau;
    }

    private String getMontantAffiche(String montant, String codeDebitCredit) {
        BigDecimal bdMontant = JAUtil.createBigDecimal(montant);
        if (bdMontant != null) {
            if (CodeSystem.CS_DEBIT.equals(codeDebitCredit) || CodeSystem.CS_EXTOURNE_DEBIT.equals(codeDebitCredit)) {
                String result = bdMontant.toString();
                return result;
            } else {
                String result = bdMontant.negate().toString();
                return result;
            }
        } else {
            return null;
        }
    }

    private String getMontantContreEcritureAffiche(String montant, String codeDebitCredit) {
        BigDecimal bdMontant = JAUtil.createBigDecimal(montant);
        if (bdMontant != null) {
            if (CodeSystem.CS_DEBIT.equals(codeDebitCredit) || CodeSystem.CS_EXTOURNE_DEBIT.equals(codeDebitCredit)) {
                String result = bdMontant.negate().toString();
                return result;
            } else {
                String result = bdMontant.toString();
                return result;
            }
        } else {
            return null;
        }
    }

    public boolean prepareValue(BEntity bEntity, BTransaction transaction, BSession session) {

        CGExtendedGrandLivre entity = (CGExtendedGrandLivre) bEntity;

        boolean isEcritureMultiple = false;
        boolean isEcritureMissing = false;

        if (Integer.parseInt(entity.getNombreAvoir()) > 1 || Integer.parseInt(entity.getNombreDoit()) > 1) {
            isEcritureMultiple = true;
        }

        if (Integer.parseInt(entity.getNombreAvoir()) == 0 || Integer.parseInt(entity.getNombreDoit()) == 0) {
            isEcritureMissing = true;
        }

        if (entity.getDate() == null || entity.getDate().length() != 8) {
            col1 = entity.getDate();
        } else {
            col1 = entity.getDate().substring(6, 8) + "." + entity.getDate().substring(4, 6) + "."
                    + entity.getDate().substring(0, 4);
        }

        col2 = entity.getNumero() + "/" + entity.getIdEcriture();

        // Info de contre écriture
        String result = null;
        if (isEcritureMultiple) {
            result = session.getLabel("MULTIPLE");
        } else if (isEcritureMissing) {
            result = session.getLabel("AUCUNE");
        }

        if (result != null) {
            col3 = result;
        } else {
            if (CGCompte.CS_GENRE_CHARGE.equals(entity.getIdGenre())) {
                String codeDebitCredit = entity.getCodeDebitCredit();
                if (CodeSystem.CS_DEBIT.equals(codeDebitCredit) || CodeSystem.CS_EXTOURNE_DEBIT.equals(codeDebitCredit)) {
                    if (new BigDecimal(entity.getNombreDoit()).intValue() > 1) {
                        result = session.getLabel("GRAND_LIVRE_ECRITURE_COLLECTIVE");
                    }
                } else {
                    if (new BigDecimal(entity.getNombreAvoir()).intValue() > 1) {
                        result = session.getLabel("GRAND_LIVRE_ECRITURE_COLLECTIVE");
                    }
                }

                result = entity.getIdExtCpteContrEcri() + "/" + entity.getIdCentreCharge();
            } else {
                result = entity.getIdExtCpteContrEcri();
            }

            if (result == null || result.trim().length() == 0) {
                result = session.getLabel("AUCUNE");
            }
        }
        col3 = result;

        col4 = entity.getLibelle();

        col5 = entity.getPiece();

        col7 = entity.getCode();

        try {
            if (CGCompte.CS_MONNAIE_ETRANGERE.equals(entity.getIdNature())) {
                String resultMontant = getMontantAffiche(entity.getMontantMonnaie(), entity.getCodeDebitCredit());
                if (resultMontant != null) {
                    col8 = new Double(resultMontant);
                    col8a = entity.getCodeIsoMonnaie();
                }
            } else if (!isEcritureMultiple && !isEcritureMissing
                    && CGCompte.CS_MONNAIE_ETRANGERE.equals(entity.getIdNatureContreCompte())) {
                String resultMontant = getMontantContreEcritureAffiche(entity.getMontantMonnaieContreEcriture(),
                        entity.getCodeDebitCredit());
                if (resultMontant != null) {
                    col8 = new Double(resultMontant);
                    col8a = entity.getCodeIsoContreCompte();
                }

            } else {
                col8 = null;
                col8a = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            col8 = null;
            col8a = null;
        }

        try {
            if (CGCompte.CS_MONNAIE_ETRANGERE.equals(entity.getIdNature())) {
                col9 = entity.getCoursMonnaie();
            } else if (!isEcritureMultiple && !isEcritureMissing
                    && CGCompte.CS_MONNAIE_ETRANGERE.equals(entity.getIdNatureContreCompte())) {
                col9 = entity.getCoursMonnaieContreEcriture();
            } else {
                col9 = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            col9 = null;
        }

        col10 = null;
        String codeDebitCredit = entity.getCodeDebitCredit();
        if (!entity.isProvisoire().booleanValue()
                && (CodeSystem.CS_DEBIT.equals(codeDebitCredit) || CodeSystem.CS_EXTOURNE_DEBIT.equals(codeDebitCredit))) {
            String resultMontant = getMontantAffiche(entity.getMontant(), codeDebitCredit);
            if (resultMontant != null) {
                col10 = new Double(resultMontant);
            }
        }

        col11 = null;
        if (!entity.isProvisoire().booleanValue()
                && (CodeSystem.CS_CREDIT.equals(codeDebitCredit) || CodeSystem.CS_EXTOURNE_CREDIT
                        .equals(codeDebitCredit))) {
            String resultMontant = getMontantAffiche(entity.getMontant(), codeDebitCredit);
            if (resultMontant != null) {
                col11 = new Double(resultMontant);
            }
        }

        col10Italic = null;
        if (entity.isProvisoire().booleanValue()
                && (CodeSystem.CS_DEBIT.equals(codeDebitCredit) || CodeSystem.CS_EXTOURNE_DEBIT.equals(codeDebitCredit))) {
            String resultMontant = getMontantAffiche(entity.getMontant(), codeDebitCredit);
            if (resultMontant != null) {
                col10Italic = new Double(resultMontant);
            }
        }

        col11Italic = null;
        if (entity.isProvisoire().booleanValue()
                && (CodeSystem.CS_CREDIT.equals(codeDebitCredit) || CodeSystem.CS_EXTOURNE_CREDIT
                        .equals(codeDebitCredit))) {
            String resultMontant = getMontantAffiche(entity.getMontant(), codeDebitCredit);
            if (resultMontant != null) {
                col11Italic = new Double(resultMontant);
            }
        }

        String langue = session.getIdLangueISO();
        String libelle = "";
        if ("IT".equalsIgnoreCase(langue)) {
            libelle = entity.getLibelleIt();
        } else if ("DE".equalsIgnoreCase(langue)) {
            libelle = entity.getLibelleDe();
        } else {
            libelle = entity.getLibelleFr();
        }

        if (CGCompte.CS_MONNAIE_ETRANGERE.equals(entity.getIdNature())) {
            col13 = entity.getIdExterne() + " " + libelle + " €";
        } else {
            col13 = entity.getIdExterne() + " " + libelle;
        }

        // Label Solde A Nouveau
        // calcul du solde à nouveau
        // if (idPreviousPeriodesComptable!=null &&
        // idPreviousPeriodesComptable.size()>0) {
        if (getIdPeriodeForSoldeANouveau() != null) {
            col14 = session.getLabel("GRAND_LIVRE_SOLDE_NOUVEAU");
        } else {
            col14 = null;
        }

        // Solde A Nouveau
        col15 = null;
        // calcul du solde à nouveau
        if (getIdPeriodeForSoldeANouveau() != null) {
            try {
                boolean isProvisoire = true;

                if (entity.isProvisoire() != null && entity.isProvisoire().booleanValue() == false) {
                    isProvisoire = false;
                }

                CGPeriodeComptable periode = new CGPeriodeComptable();
                periode.setSession(session);
                // Une seule dans notre cas
                periode.setIdPeriodeComptable(getIdPeriodeForSoldeANouveau());
                periode.retrieve(transaction);

                // Calcul du solde à nouveau jusqu'à la période précédente à
                // celle sélectionnée.
                if (periode != null && !periode.isNew()) {
                    CGPeriodeComptable periodePrecedente = periode.retrieveLastPeriode(transaction);
                    FWCurrency resultMontant = CGSolde
                            .computeSoldeCumule(entity.getIdExerciceComptable(), entity.getIdCompte(),
                                    periodePrecedente.getIdPeriodeComptable(), "0", session, isProvisoire);

                    if (resultMontant.compareTo(new FWCurrency(0)) != 0) {
                        col15 = new Double(resultMontant.doubleValue());
                    }
                }

            } catch (Exception e) {
                col15 = null;
            }
        }

        if (entity.isProvisoire().booleanValue()) {
            colProvisoire = new Boolean(true);
        } else {
            colProvisoire = new Boolean(false);
        }

        colDisplayOnlySoldeANouveau = new Boolean(false);

        // n'affiche que les comptes ayant des mouvements.
        return (col10 != null || col11 != null || col10Italic != null || col11Italic != null);
    }

    public void setIdPeriodeForSoldeANouveau(String idPeriodeForSoldeANouveau) {
        this.idPeriodeForSoldeANouveau = idPeriodeForSoldeANouveau;
    }

}
