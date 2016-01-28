package globaz.osiris.print.itext.list;

import globaz.framework.util.FWCurrency;
import globaz.globall.util.JANumberFormatter;
import globaz.osiris.db.comptes.CACompteAnnexe;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import net.sf.jasperreports.engine.JRDataSource;

/**
 * Insérez la description du type ici. Date de création : (27.06.2002 10:46:16)
 * 
 * @author: Administrator
 */
public class CAIJournalContentieux_DS implements JRDataSource {
    private int _index = 0;
    private java.lang.String date = new String();
    private java.lang.String dateReference = new String();
    private boolean modePrevisionnel = false;
    private globaz.globall.db.BSession session;
    private List table = new ArrayList();
    private List tableRecap = new ArrayList();

    public CAIJournalContentieux_DS() {
    }

    /**
     * Copiez la méthode tel quel, permet la copy de l'objet Date de création : (01.04.2003 14:45:18)
     * 
     * @return java.lang.Object
     * @exception java.lang.CloneNotSupportedException
     *                La description de l'exception.
     */
    @Override
    public Object clone() throws java.lang.CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.06.2002 10:51:35)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDate() {
        return date;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.06.2002 10:51:55)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDateReference() {
        return dateReference;
    }

    /**
	 *
	 */
    @Override
    public java.lang.Object getFieldValue(net.sf.jasperreports.engine.JRField jrField)
            throws net.sf.jasperreports.engine.JRException {
        // Retourne chaque champ
        if (jrField.getName().equals("COL_ID")) {
            return new Integer(_index - 1);
        }
        Vector line = (Vector) table.get(_index - 1);
        Vector lineRecap = (Vector) tableRecap.get(_index - 1);
        // COL_1 --> Compte annexe
        if (jrField.getName().equals("COL_1")) {
            return line.get(0);
        }
        // COL_2 --> Section
        if (jrField.getName().equals("COL_2")) {
            return line.get(1);
        }
        // COL_3 --> Date
        if (jrField.getName().equals("COL_3")) {
            return line.get(2);
        }
        // COL_4 --> Etape
        if (jrField.getName().equals("COL_4")) {
            return line.get(3);
        }
        // COL_5 --> Date d'échéance
        if (jrField.getName().equals("COL_5")) {
            return line.get(4);
        }
        // COL_6 --> Taxes et frais
        if (jrField.getName().equals("COL_6")) {
            return new BigDecimal(JANumberFormatter.deQuote(line.get(5).toString()));
        }
        // COL_7 --> Montant
        if (jrField.getName().equals("COL_7")) {
            return new BigDecimal(JANumberFormatter.deQuote(line.get(6).toString()));
        }
        // COL_8 --> Remarque
        if (jrField.getName().equals("COL_8")) {
            return line.get(7);
        }
        // COL_10 --> Récapitulation par étape
        if (jrField.getName().equals("COL_10")) {
            return lineRecap.get(1);
        }
        // COL_11 --> Nombre

        // COL_12 --> Taxes et frais
        if (jrField.getName().equals("COL_12")) {
            return new BigDecimal(JANumberFormatter.deQuote(lineRecap.get(2).toString()));
        }
        // COL_13 --> Montant
        if (jrField.getName().equals("COL_13")) {
            return new BigDecimal(JANumberFormatter.deQuote(lineRecap.get(3).toString()));
        }
        return null;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.07.2003 10:01:54)
     * 
     * @return globaz.globall.db.BSession
     */
    public globaz.globall.db.BSession getSession() {
        return session;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.07.2003 15:08:59)
     * 
     * @return java.util.List
     */
    public java.util.List getTable() {
        return table;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.07.2003 15:08:59)
     * 
     * @return java.util.List
     */
    public java.util.List getTableRecap() {
        return tableRecap;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.06.2002 11:53:23)
     * 
     * @param section
     *            globaz.osiris.db.comptes.CASection
     * @param etape
     *            globaz.osiris.db.contentieux.CAParametreEtape
     * @param date
     *            java.lang.String
     * @param taxe
     *            java.lang.String
     * @param montant
     *            java.lang.String
     * @param remarque
     *            java.lang.String
     */
    public void insertRow(globaz.osiris.db.comptes.CASection section,
            globaz.osiris.db.contentieux.CAParametreEtape etape, String date, String taxe, String montant,
            String remarque) {

        globaz.osiris.utils.quicksort.ComparableVector line = new globaz.osiris.utils.quicksort.ComparableVector();
        globaz.osiris.utils.quicksort.ComparableVector lineRecap = new globaz.osiris.utils.quicksort.ComparableVector();

        // Compte annexe
        CACompteAnnexe _compte = (CACompteAnnexe) section.getCompteAnnexe();
        String sCompteAnnexe = _compte.getCARole().getDescription() + " " + _compte.getIdExterneRole() + " "
                + _compte.getDescription();
        line.add(sCompteAnnexe);

        // Section
        String sSection = section.getIdExterne() + " " + section.getDescription();
        line.add(sSection);

        // Date de la section
        line.add(section.getDateSection());

        // Etape
        String sEtape = etape.getEtape().getDescription();
        line.add(sEtape);
        lineRecap.add(etape.getIdEtape());
        lineRecap.add(sEtape);

        // Date d'échéance
        line.add(date);

        // Taxe
        FWCurrency cTaxe = new FWCurrency(taxe);
        line.add(cTaxe.toStringFormat());
        lineRecap.add(cTaxe.toStringFormat());

        // Montant
        FWCurrency cMontant = new FWCurrency(montant);
        line.add(cMontant.toStringFormat());
        lineRecap.add(cMontant.toStringFormat());

        // Remarque
        if (remarque.trim().length() == 0) {
            line.add(" ");
        } else {
            line.add(remarque);
        }

        table.add(line);
        tableRecap.add(lineRecap);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.06.2002 10:53:43)
     * 
     * @return boolean
     */
    public boolean isModePrevisionnel() {
        return modePrevisionnel;
    }

    /**
	 *
	 */
    @Override
    public boolean next() throws net.sf.jasperreports.engine.JRException {
        if (_index < table.size()) {
            _index++;
            return true;
        }
        return false;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.06.2002 10:51:35)
     * 
     * @param newDate
     *            java.lang.String
     */
    public void setDate(java.lang.String newDate) {
        date = newDate;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.06.2002 10:51:55)
     * 
     * @param newDateReference
     *            java.lang.String
     */
    public void setDateReference(java.lang.String newDateReference) {
        dateReference = newDateReference;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.06.2002 10:53:43)
     * 
     * @param newModePrevisionnel
     *            boolean
     */
    public void setModePrevisionnel(boolean newModePrevisionnel) {
        modePrevisionnel = newModePrevisionnel;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.07.2003 10:01:54)
     * 
     * @param newSession
     *            globaz.globall.db.BSession
     */
    public void setSession(globaz.globall.db.BSession newSession) {
        session = newSession;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.07.2003 15:08:59)
     * 
     * @param newTable
     *            java.util.List
     */
    public void setTable(java.util.List newTableRecap) {
        tableRecap = newTableRecap;
    }
}
