/*
 * Créé le 21 sept. 05
 */
package globaz.ij.vb.prestations;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.db.postit.FWNoteP;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.ij.application.IJApplication;
import globaz.ij.db.prestations.IJPrestation;
import globaz.ij.db.prestations.IJPrestationJointLotPrononce;
import globaz.ij.db.prestations.IJPrestationJointLotPrononceManager;
import globaz.ij.db.prestations.IJRepartitionPaiements;
import globaz.ij.db.prononces.IJPrononce;
import globaz.jade.client.util.JadeStringUtil;
import java.util.List;
import java.util.Vector;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJPrestationJointLotPrononceListViewBean extends IJPrestationJointLotPrononceManager implements
        FWListViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String ALL_FIELDS = "HXNAVS, HTLDE1, HTLDE2, "
            + "HPDNAI, HPTSEX, HNIPAY, HTLDU1, HTLDU2, XLDDEB, XLDFIN, XLIPRE, "
            + "XLDDEC, XLMMBR, XLMMBI, XLMMBE, XLMBIP, XLILOT, XLMDAC, XLTETA, "
            + "XLIANN, XLNJIN, XLNJEX, XLTTYP, XLIIJC, XLIBIN, XLMMJI, XLMMJE, "
            + "XONNLO, XODCOM, XKIBIN, XBDPRO, XBDDDR, WAITIE";
    public static final String FIELDNAME_COUNT_POSTIT = "CNTPOST";
    public static final String FIELDNAME_SUM_MONTANT_NET = "SUMMNET";
    private List attachedDocuments = null;

    private String csTypeIJ = "";
    private boolean hasPostitField = false;

    private boolean hasSumMontantNet = false;

    private String idPrononce = "";
    private transient Vector orderBy = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getFields(BStatement statement) {
        if (hasPostitField || hasSumMontantNet) {

            String fields = IJPrestationJointLotPrononceListViewBean.ALL_FIELDS;

            if (hasPostitField) {
                fields += ", (" + createSelectCountPostit(_getCollection()) + ") AS "
                        + IJPrestationJointLotPrononceListViewBean.FIELDNAME_COUNT_POSTIT + " ";
            }

            if (hasSumMontantNet) {
                fields += ", (" + createSelectSumMontantNetRepartitions(_getCollection()) + ") AS "
                        + IJPrestationJointLotPrononceListViewBean.FIELDNAME_SUM_MONTANT_NET + " ";
            }

            return fields;
        } else {
            return super._getFields(statement);
        }
    }

    /**
     * (non-Javadoc).
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new IJPrestationJointLotPrononceViewBean();
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
        query.append(IJPrestation.FIELDNAME_IDPRESTATION);
        query.append(" AND ");
        query.append("NPTBLSRC");
        query.append(" = '");
        query.append(IJApplication.KEY_POSTIT_PRESTATIONS);
        query.append("'");

        return query.toString();
    }

    /**
     * creation du sum pour les montant net des repartitions
     * 
     * @return
     */
    private String createSelectSumMontantNetRepartitions(String schema) {

        StringBuffer query = new StringBuffer();
        query.append("SELECT SUM(" + IJRepartitionPaiements.FIELDNAME_MONTANTNET + ") FROM ");
        query.append(schema);
        query.append(IJRepartitionPaiements.TABLE_NAME);
        query.append(" WHERE ");
        query.append(IJRepartitionPaiements.FIELDNAME_IDPRESTATION);
        query.append(" = ");
        query.append(IJPrestation.FIELDNAME_IDPRESTATION);
        query.append(" AND (");
        query.append(IJRepartitionPaiements.FIELDNAME_IDPARENT).append(" = 0");
        query.append(" OR ").append(IJRepartitionPaiements.FIELDNAME_IDPARENT).append(" IS NULL ) ");

        return query.toString();
    }

    public List getAttachedDocuments() {
        return attachedDocuments;
    }

    /**
     * @return
     */
    public String getCsTypeIJ() {
        return csTypeIJ;
    }

    /**
     * getter pour l'attribut id prononce.
     * 
     * @return la valeur courante de l'attribut id prononce
     */
    public String getIdPrononce() {
        return idPrononce;
    }

    public Vector getOrderByData() {
        if (orderBy == null) {
            orderBy = new Vector(4);
            orderBy.add(new String[] { IJPrestationJointLotPrononce.FIELDNAME_NUM_AVS,
                    getSession().getLabel("JSP_NSS_ABREGE") });
            orderBy.add(new String[] { IJPrestationJointLotPrononce.FIELDNAME_NOM, getSession().getLabel("JSP_NOM") });
            orderBy.add(new String[] { IJPrestationJointLotPrononce.FIELDNAME_PRENOM,
                    getSession().getLabel("JSP_PRENOM") });
            orderBy.add(new String[] { IJPrononce.FIELDNAME_DATE_DEBUT_PRONONCE + " DESC",
                    getSession().getLabel("JSP_DATE_DEBUT") });
            orderBy.add(new String[] { IJPrestation.FIELDNAME_IDPRESTATION + " DESC",
                    getSession().getLabel("JSP_NUMERO") });
        }

        return orderBy;
    }

    public boolean hasPostitField() {
        return hasPostitField;
    }

    public boolean isHasSumMontantNet() {
        return hasSumMontantNet;
    }

    /**
     * Méthode qui retourne une string avec true si le NSS dans le vb est un NNSS, sinon false
     * 
     * @return String (true ou false)
     */
    public String isNNSS() {
        if (JadeStringUtil.isBlankOrZero(getLikeNumeroAVS())) {
            return "";
        }

        if (getLikeNumeroAVS().length() > 14) {
            return "true";
        } else {
            return "false";
        }
    }

    /**
     * Méthode qui retourne une string avec true si le NSS passé en paramètre est un NNSS, sinon false
     * 
     * @param noAvs
     * @return String (true ou false)
     */
    public String isNNSS(String noAvs) {

        if (JadeStringUtil.isBlankOrZero(noAvs)) {
            return "";
        }

        if (noAvs.length() > 14) {
            return "true";
        } else {
            return "false";
        }
    }

    public void setAttachedDocuments(List attachedDocuments) {
        this.attachedDocuments = attachedDocuments;
    }

    /**
     * @param string
     */
    public void setCsTypeIJ(String string) {
        csTypeIJ = string;
    }

    public void setHasPostitField(boolean hasPostitField) {
        this.hasPostitField = hasPostitField;
    }

    public void setHasSumMontantNet(boolean hasSumMontantNet) {
        this.hasSumMontantNet = hasSumMontantNet;
    }

    /**
     * setter pour l'attribut id prononce.
     * 
     * @param idPrononce
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdPrononce(String idPrononce) {
        this.idPrononce = idPrononce;
    }

}
