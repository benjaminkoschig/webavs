/*
 * Créé le 16 fevr. 07
 */
package globaz.corvus.vb.rentesaccordees;

import globaz.corvus.application.REApplication;
import globaz.corvus.db.demandes.REDemandeRenteJointDemande;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteManager;
import globaz.corvus.utils.REPmtMensuel;
import globaz.framework.db.postit.FWNoteP;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author bsc
 * 
 * 
 */

public class RERenteAccordeeJointDemandeRenteListViewBean extends RERenteAccJoinTblTiersJoinDemRenteManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String ALL_FIELDS = "YLIRAC, YLIBAC, YLLCAU, YLTECI, YLLREF, YLIPTC, YLIDTC, YLMROR, "
            + "YLLCS1, YLLCS2, YLLCS3, YLLCS4, "
            + "YLLCS5, YLNRFG, YLLCMU, YLNDAJ, YLMSAJ, YLDRAJ, YLLAAN, YLMRAN, YLDDAN, YLLCSI, YLDFDE, YLLSVE, "
            + "YLLPAP, YLTRRE, YLBTMA, YLDAMR, YLLKEY, YLITBC, YLMTRA, YLLREM, ZTIPRA, "
            + "ZTTGEN, ZTITBE, ZTIICT, ZTMPRE, ZTLRFP, ZTBRET, ZTBPRB, ZTBERR, ZTDDDR, ZTDFDR, ZTTETA, ZTDECH, "
            + "ZTLFRR, ZTIDPA, ZTICIM, ZTLCPR, ZTIEBK, ZTBAMB, ZTBAMR, YNIIIC, YNITAP, YNIAAP, YNIDOA, YNICOA, "
            + "HNIPAY, HTTTIE, HTTTTI, HTLDE1, HTLDE2, HTLDE3, HTLDE4, HTTLAN, "
            + "HTPPHY, HTPMOR, HTINAC, HTLDU1, HTLDU2, HTLDEC, HTLDUC, HTNTIE, HTPOLF, HTPOLD, HTPOLI, "
            + "HXNAVS, HXNAFF, HXNCON, HXAAVS, HXTGAF, HXDDAC, HXDFAC, HJILOC, HPDNAI, HPDDEC, "
            + "HPTETC, HPTSEX, HPTCAN, HPDIST, YIIBCA, YIIRCA, YIMREV, YIMRAM, YIDDCA, YIDDCD, YIDDCR, "
            + "YIDACC, YIDANN, YINECR, YIDMAA, YIDMAD, YIBIRL, YIBIRG, YINOAI, YINDIN, YILCIA, YIDSEA, YIBIPR, "
            + "YIDPJE, YIMRJE, YIDPMA, YIDPAE, YIDMCA, YILDAP, YIBIRS, YIDABE, YIDABA, YIDABT, YINSPC, YILTCC, "
            + "YILRCO, YIMMME, YIDATR, YIDPAS, YILFAR, YITETA, YIITBC, YIBPRC, YIDBE1, YIDBE2, YIDBE4, YNIRCA, "
            + "YAIDEM, YAIMDO, YAIDPA, YADTRA, YADDEP, YADREC, YATETA, YAIRCA, YAIINC, YATTYC, "
            + "YATTYD, YAIGES, YADDEB, YADFIN, YABHIS, WAIDEM, WATTDE, WAITIE, WATETA, WAIMDO";

    public static final String FIELDNAME_COUNT_POSTIT = "CNTPOST";

    private boolean hasPostitField = false;

    private boolean screenMode = false;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getFields(BStatement statement) {

        if (screenMode || hasPostitField) {
            StringBuilder sql = new StringBuilder();

            if (screenMode) {
                sql.append(RERenteAccordeeJointDemandeRenteListViewBean.ALL_FIELDS + "," + _getCollection()
                        + "TIPERSP.HTITIE," + _getCollection() + "TITIERP.HTITIE," + _getCollection()
                        + "TIPAVSP.HTITIE");
            }

            if (hasPostitField) {
                if (sql.length() > 0) {
                    sql.append(",");
                }

                sql.append("(" + createSelectCountPostit(_getCollection()) + ") AS "
                        + RERenteAccordeeJointDemandeRenteListViewBean.FIELDNAME_COUNT_POSTIT + " ");
            }

            return sql.toString();
        }

        return super._getFields(statement);
    }

    @Override
    protected String _getFrom(BStatement statement) {
        // TODO Auto-generated method stub
        return super._getFrom(statement);
    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RERenteAccordeeJointDemandeRenteViewBean();
    }

    /**
     * creation du count pour les postit de l'entity
     * 
     * @return
     */
    private String createSelectCountPostit(String schema) {

        StringBuffer query = new StringBuffer();
        query.append("SELECT COUNT(*) FROM ");
        query.append(schema);
        query.append(FWNoteP.TABLE_NAME);
        query.append(" WHERE ");
        query.append("NPSRCID");
        query.append(" = ");
        query.append(schema);
        query.append(REDemandeRenteJointDemande.TABLE_TIERS);
        query.append(".");
        query.append(REDemandeRenteJointDemande.FIELDNAME_ID_TIERS_TI);
        query.append(" AND ");
        query.append("NPTBLSRC");
        query.append(" = '");
        query.append(REApplication.KEY_POSTIT_RENTES);
        query.append("'");

        return query.toString();
    }

    public String getDateDernierPaiement() {
        return REPmtMensuel.getDateDernierPmt(getSession());
    }

    @Override
    public String getOrderByDefaut() {
        if (JadeStringUtil.isIntegerEmpty(getForNoBaseCalcul())
                && JadeStringUtil.isIntegerEmpty(getForNoDemandeRente())) {

            return REDemandeRenteJointDemande.FIELDNAME_NOM_FOR_SEARCH + ","
                    + REDemandeRenteJointDemande.FIELDNAME_PRENOM_FOR_SEARCH + ", " + " CASE "
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " WHEN 0 THEN 999999 ELSE "
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " END DESC";
        } else {

            return " CASE " + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " WHEN 0 THEN 999999 ELSE "
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " END DESC" + ", "
                    + REPrestationsAccordees.FIELDNAME_CODE_PRESTATION + ","
                    + REDemandeRenteJointDemande.FIELDNAME_DATENAISSANCE;
        }
    }

    public boolean hasPostitField() {
        return hasPostitField;
    }

    public boolean isScreenMode() {
        return screenMode;
    }

    public void setHasPostitField(boolean hasPostitField) {
        this.hasPostitField = hasPostitField;
    }

    public void setScreenMode(boolean screenMode) {
        this.screenMode = screenMode;
    }
}
