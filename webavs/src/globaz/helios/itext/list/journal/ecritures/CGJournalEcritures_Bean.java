package globaz.helios.itext.list.journal.ecritures;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.helios.db.comptes.CGAdvancedEcritureViewBean;
import globaz.helios.db.comptes.CGCentreCharge;
import globaz.helios.db.comptes.CGCompte;
import globaz.helios.db.comptes.CGMandat;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CGJournalEcritures_Bean {
    protected String col1 = null;
    protected Double col10 = null;
    protected Double col11 = null;
    protected String col12 = null;
    protected String col2 = null;
    protected String col3 = null;
    protected String col4 = null;
    protected String col5 = null;
    protected String col6 = null;
    protected String col7 = null;
    protected String col8 = null;
    protected String col9 = null;
    protected String colId = null;

    /**
     * Constructor for CGGrandLivre_Bean.
     */
    public CGJournalEcritures_Bean() {
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
     * Returns the col11.
     * 
     * @return Double
     */
    public Double getCOL_11() {
        return col11;
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
     * Returns the col6.
     * 
     * @return String
     */
    public String getCOL_6() {
        return col6;
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
    public String getCOL_8() {
        return col8;
    }

    /**
     * Returns the col9.
     * 
     * @return String
     */
    public String getCOL_9() {
        return col9;
    }

    public boolean prepareValue(BEntity bEntity, CGMandat mandat, CGPeriodeComptable periodeComptable,
            BTransaction transaction, BSession session) {

        CGCompte compte = null;
        CGAdvancedEcritureViewBean entity = (CGAdvancedEcritureViewBean) bEntity;

        try {
            compte = new CGCompte();
            compte.setSession(session);
            compte.setIdCompte(entity.getIdCompte());
            compte.retrieve(transaction);
        } catch (Exception e) {
            e.printStackTrace();
            compte = null;
            return false;
        }

        /*
         * if(jrField.getName().equals("COL_ID")) return new Integer(index);
         */

        // Date ecriture
        col1 = entity.getDate();

        // Numéro écriture
        col2 = entity.getIdEcriture();

        try {
            CGPlanComptableViewBean planComptable = new CGPlanComptableViewBean();
            planComptable.setSession(session);
            planComptable.setIdCompte(entity.getIdCompte());
            planComptable.setIdExerciceComptable(entity.getIdExerciceComptable());
            planComptable.retrieve(transaction);

            if (CGCompte.CS_GENRE_CHARGE.equals(compte.getIdGenre())
                    && !JadeStringUtil.isIntegerEmpty(entity.getIdCentreCharge())) {
                CGCentreCharge centreCharge = new CGCentreCharge();
                centreCharge.setIdCentreCharge(entity.getIdCentreCharge());
                centreCharge.setSession(session);
                centreCharge.retrieve(transaction);

                String langue = session.getIdLangueISO();
                String libelle = "";
                if ("IT".equalsIgnoreCase(langue)) {
                    libelle = centreCharge.getLibelleIt();
                } else if ("DE".equalsIgnoreCase(langue)) {
                    libelle = centreCharge.getLibelleDe();
                } else {
                    libelle = centreCharge.getLibelleFr();
                }

                col3 = planComptable.getIdExterne() + " / " + entity.getIdCentreCharge() + " "
                        + planComptable.getLibelle() + " - " + libelle;
            } else {
                col3 = planComptable.getIdExterne() + " " + planComptable.getLibelle();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            col3 = null;
        }

        // Libellé
        col4 = entity.getLibelle();

        // Numéro de pièce
        col5 = entity.getPiece();

        // Numéro Livre
        if (mandat.isUtiliseLivres().booleanValue()) {
            col6 = entity.getIdLivre();
        } else {
            col6 = null;
        }

        // Période Comptable
        col7 = periodeComptable.getCode();

        if (CGCompte.CS_MONNAIE_ETRANGERE.equals(compte.getIdNature())) {
            col8 = entity.getMontantAfficheMonnaie() + " " + compte.getCodeISOMonnaie();
        }

        // Cours
        if (CGCompte.CS_MONNAIE_ETRANGERE.equals(compte.getIdNature())) {
            col9 = entity.getCoursMonnaie();
        }

        // Débit
        if (entity.isDoit()) {
            col10 = new Double((new FWCurrency(entity.getMontantAffiche())).doubleValue());
        } else {
            col10 = null;
        }

        // Crédit
        if (entity.isAvoir()) {
            col11 = new Double((new FWCurrency(entity.getMontantAffiche())).doubleValue());
        } else {
            col11 = null;
        }

        // Remarques
        if (entity.isEstErreur().booleanValue()) {
            col12 = "Err";
        } else if (!entity.isEstActive().booleanValue()) {
            col12 = "Inactive";
        } else {
            col12 = null;
        }

        return true;
    }
}
