package globaz.osiris.impl.print.itext.list;

import globaz.framework.util.FWCurrency;
import globaz.globall.util.JANumberFormatter;
import globaz.osiris.db.comptes.CACompteAnnexe;
import java.math.BigDecimal;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CAImprJournalContentieuxCSCBean {

    private String col1 = null;
    // Lignes de récapitulation
    private String col10 = null;
    private String col11 = null;
    private String col12 = null;
    private String col13 = null;
    private String col2 = null;
    private String col3 = null;
    private String col4 = null;

    private String col5 = null;
    private String col6 = null;
    private String col7 = null;
    private String col8 = null;

    private int counter = 0;

    private boolean empty = true;

    public CAImprJournalContentieuxCSCBean() {
    }

    public CAImprJournalContentieuxCSCBean(globaz.osiris.db.comptes.CASection section,
            globaz.osiris.db.contentieux.CAParametreEtape etape, String date, String taxe, String montant,
            String remarque) {

        add(section, etape, date, taxe, montant, remarque);

    }

    /**
     * Cette méthode permet d'ajouter des valeurs dans le bean
     */
    public void add(globaz.osiris.db.comptes.CASection section, globaz.osiris.db.contentieux.CAParametreEtape etape,
            String date, String taxe, String montant, String remarque) {
        try {

            counter++;

            // Compte annexe
            CACompteAnnexe _compte = (CACompteAnnexe) section.getCompteAnnexe();
            col1 = _compte.getCARole().getDescription() + " " + _compte.getIdExterneRole() + " "
                    + _compte.getDescription();

            // Section
            col2 = section.getIdExterne() + " " + section.getDescription();

            // Date de la section
            col3 = section.getDateSection();

            // Etape
            if (etape != null) {
                String sEtape = etape.getEtape().getDescription();
                col4 = sEtape;

                col10 = sEtape;// etape.getIdEtape();
                col11 = sEtape;
            } else {
                col4 = "";
                col10 = "";
                col11 = "";

            }

            // Date d'échéance
            col5 = date;

            // Taxe
            FWCurrency cTaxe = new FWCurrency(taxe);
            col6 = cTaxe.toStringFormat();
            col12 = cTaxe.toStringFormat();

            // Montant
            FWCurrency cMontant = new FWCurrency(montant);
            col7 = cMontant.toStringFormat();
            col13 = cMontant.toStringFormat();

            // Remarque
            if (remarque.trim().length() == 0) {
                col8 = " ";
            } else {
                col8 = remarque;
            }

            setEmpty(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retourne le genre de virement.
     * 
     * @return String
     */
    public String getCOL_1() {
        return col1;
    }

    public String getCOL_10() {
        return col10;
    }

    public String getCOL_11() {
        return col11;
    }

    public BigDecimal getCOL_12() {
        return new BigDecimal(JANumberFormatter.deQuote(col12));
    }

    public BigDecimal getCOL_13() {
        return new BigDecimal(JANumberFormatter.deQuote(col13));
    }

    /**
     * Retourne le compte annexe.
     * 
     * @return String
     */
    public String getCOL_2() {
        return col2;
    }

    /**
     * Retourne l'adresse de versement.
     * 
     * @return String
     */
    public String getCOL_3() {
        return col3;
    }

    /**
     * Retourne la nature du versement.
     * 
     * @return String
     */
    public String getCOL_4() {
        return col4;
    }

    /**
     * Retourne le montant groupé.
     * 
     * @return BigDecimal
     */
    public String getCOL_5() {
        return col5;
    }

    /**
     * Retourne le numéro de transaction.
     * 
     * @return String
     */
    public BigDecimal getCOL_6() {
        return new BigDecimal(JANumberFormatter.deQuote(col6));
    }

    public BigDecimal getCOL_7() {
        return new BigDecimal(JANumberFormatter.deQuote(col7));
    }

    public String getCOL_8() {
        return col8;
    }

    public Integer getCOL_ID() {
        return new Integer(counter - 1);
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
     * Sets the empty.
     * 
     * @param empty
     *            The empty to set
     */
    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    @Override
    public String toString() {
        return getCOL_10();
    }

}