package globaz.aquila.print;

import globaz.aquila.db.access.batch.COEtape;
import globaz.aquila.db.process.COProcessContentieuxInfo;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 * Copier-coller puis depuis Osiris puis adapté.
 * 
 * @author: vre
 */
public class COJournalContentieux_DS implements JRDataSource {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private int _index = 0;
    private String date = new String();
    private String dateReference = new String();
    private boolean modePrevisionnel = false;
    private BSession session;
    private List table = new ArrayList();
    private List tableRecap = new ArrayList();

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe COJournalContentieux_DS.
     */
    public COJournalContentieux_DS() {
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Copiez la méthode tel quel, permet la copy de l'objet Date de création : (01.04.2003 14:45:18).
     * 
     * @return java.lang.Object
     * @exception CloneNotSupportedException
     *                La description de l'exception.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.06.2002 10:51:35)
     * 
     * @return java.lang.String
     */
    public String getDate() {
        return date;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.06.2002 10:51:55)
     * 
     * @return java.lang.String
     */
    public String getDateReference() {
        return dateReference;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param jrField
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws JRException
     *             DOCUMENT ME!
     */
    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
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
    public BSession getSession() {
        return session;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.07.2003 15:08:59)
     * 
     * @return java.util.List
     */
    public List getTable() {
        return table;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.07.2003 15:08:59)
     * 
     * @return java.util.List
     */
    public List getTableRecap() {
        return tableRecap;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.06.2002 11:53:23)
     * 
     * @param section
     *            globaz.osiris.db.comptes.CASection
     * @param etape
     *            globaz.osiris.db.contentieux.CAParametreEtape
     * @param dateDeclenchement
     *            java.lang.String
     * @param taxe
     *            java.lang.String
     * @param montant
     *            java.lang.String
     * @param remarque
     *            java.lang.String
     */
    public void insertRow(CASection section, COEtape etape, String dateDeclenchement, String taxe, String montant,
            String remarque) {
        CACompteAnnexe _compte = (CACompteAnnexe) section.getCompteAnnexe();

        this.insertRow(
                _compte.getCARole().getDescription() + " " + _compte.getIdExterneRole() + " "
                        + _compte.getDescription(), section.getIdExterne() + " " + section.getDescription(),
                section.getDateEcheance(), dateDeclenchement, etape.getLibEtapeLibelle(), etape.getIdEtape(), taxe,
                montant, remarque);
    }

    /**
     * Ligne pour l'étape "Créer contentieux". Date de création : (27.06.2002 11:53:23)
     * 
     * @param ctxInfo
     *            globaz.osiris.db.comptes.CASection
     * @param descSection
     *            java.lang.String
     * @param etape
     *            globaz.osiris.db.contentieux.CAParametreEtape
     * @param remarque
     *            java.lang.String
     */
    public void insertRow(COProcessContentieuxInfo ctxInfo, String descSection, COEtape etape, String remarque) {
        this.insertRow(
                ctxInfo.getRoleDescriptionCA() + " " + ctxInfo.getIdExterneRoleCA() + " " + ctxInfo.getDescriptionCA(),
                ctxInfo.getIdExterneSection() + " " + descSection, ctxInfo.getDateEcheance(),
                ctxInfo.getDateEcheance(), etape.getLibEtapeLibelle(), etape.getIdEtape(), "0.00", ctxInfo.getSolde(),
                remarque);
    }

    private void insertRow(String descCA, String descSection, String dateEcheance, String dateDeclenchement,
            String libEtape, String idEtape, String taxe, String montant, String remarque) {
        Vector line = new Vector();
        Vector lineRecap = new Vector();

        // Compte annexe
        line.add(descCA);

        // Section
        line.add(descSection);

        // Date de la section
        line.add(dateEcheance);

        // Etape
        line.add(libEtape);
        lineRecap.add(idEtape);
        lineRecap.add(libEtape);

        // Date d'échéance
        line.add(dateDeclenchement);

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
            // On ajoute 99 devant l'idEtape pour que ce soit totalisé
            // séparément
            lineRecap.setElementAt("99" + idEtape, 0);
            lineRecap.setElementAt(remarque + "(" + libEtape + ")", 1);
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
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * @throws JRException
     *             DOCUMENT ME!
     */
    @Override
    public boolean next() throws JRException {
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
    public void setDate(String newDate) {
        date = newDate;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.06.2002 10:51:55)
     * 
     * @param newDateReference
     *            java.lang.String
     */
    public void setDateReference(String newDateReference) {
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
    public void setSession(BSession newSession) {
        session = newSession;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.07.2003 15:08:59)
     * 
     * @param newTableRecap
     *            java.util.List
     */
    public void setTable(List newTableRecap) {
        tableRecap = newTableRecap;
    }
}
