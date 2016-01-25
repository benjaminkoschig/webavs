package globaz.osiris.print.itext.list.resume.cpt.annexe;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.db.contentieux.CALigneExtraitCompte;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class CAResumeCptAnnexe_Bean {
    protected String col1 = null;
    protected String col2 = null;
    protected String col3 = null;
    protected Double col4 = null;
    protected Double col5 = null;
    protected Double col6 = null;
    protected String col7 = null;
    protected String col8 = null;

    /**
     * Constructor for CGGrandLivre_Bean.
     */
    public CAResumeCptAnnexe_Bean() {
        super();
    }

    /**
     * Methode utilitaire pemettant de récuperer le libelle du code system donné en parametre
     * 
     * @param session
     * @param code
     * @param langueIso
     * @return
     */
    private String _getCodeLibelle(BSession session, String code, String langueIso) {
        String libelle = "";
        String langue = "F";

        if (JadeStringUtil.isEmpty(code) || "0".equals(code)) {
            return libelle;
        }

        if ("fr".equalsIgnoreCase(langueIso)) {
            langue = "F";
        } else if ("de".equalsIgnoreCase(langueIso)) {
            langue = "D";
        } else if ("it".equalsIgnoreCase(langueIso)) {
            langue = "I";
        }

        try {
            FWParametersUserCode uc = new FWParametersUserCode();
            uc.setSession(session);
            uc.setIdCodeSysteme(code);
            uc.setIdLangue(langue);
            uc.retrieve();
            libelle = uc.getLibelle();
        } catch (Exception e) {
            JadeLogger.warn("Unabled to retrieve the code system " + code + " for the langue " + langueIso, e);
        }

        if (libelle == null) {
            libelle = "";
        }

        return libelle;
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
    public Double getCOL_4() {
        return col4;
    }

    /**
     * Returns the col5.
     * 
     * @return String
     */
    public Double getCOL_5() {
        return col5;
    }

    /**
     * Returns the col6.
     * 
     * @return String
     */
    public Double getCOL_6() {
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
     * @return the col8
     */
    public String getCOL_8() {
        return col8;
    }

    /**
     * @param col8
     *            the col8 to set
     */
    public void getCOL_8(String col8) {
        this.col8 = col8;
    }

    public boolean prepareValue(CALigneExtraitCompte entity, FWCurrency doitAvoir, FWCurrency soldeCumule,
            BTransaction transaction, BSession session, String langueIso) {

        col1 = entity.getDate();
        col2 = entity.getIdExterne();
        col3 = entity.getDescription();

        if (doitAvoir.isPositive()) {
            col4 = new Double(doitAvoir.doubleValue());
        } else {
            doitAvoir.negate();
            col5 = new Double(doitAvoir.doubleValue());
        }
        col6 = new Double(soldeCumule.doubleValue());
        col7 = _getCodeLibelle(session, entity.getCodeProvenancePmt(), langueIso);
        col8 = entity.getDateJournal();
        return true;
    }
}
