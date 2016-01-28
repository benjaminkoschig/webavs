/*
 * Créé le 7 juin 05
 */
package globaz.apg.vb.prestation;

import globaz.apg.application.APApplication;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationJointLotTiersDroitManager;
import globaz.apg.db.prestation.APRepartitionPaiements;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.db.postit.FWNoteP;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.api.IPRDemande;
import java.util.HashSet;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APPrestationJointLotTiersDroitListViewBean extends APPrestationJointLotTiersDroitManager implements
        FWListViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String ALL_FIELDS = "VHDPMT, VHDMOB, VHTTYP, VHIDRO, VHIPRS, VHDDEB, VHDFIN, VHNNJS, "
            + "VHTETA, VHTCOA, VHIANN, VHMTJR, VHMDRA, VHILOT, VHDCAL, VHDCTR, "
            + "VHMRMD, VHIRST, VHMFRG, VHMALE, VHTREV, VHLREM, VHTGEN, VHBSMA, "
            + "HXNAVS, HTLDE1, HTLDE2, HPDNAI, HPTSEX, HNIPAY, HTLDU1, HTLDU2, VATGSE, VMILOT, VMNLOT, WAITIE";
    public static final String FIELDNAME_COUNT_POSTIT = "CNTPOST";
    public static final String FIELDNAME_SUM_MONTANT_NET = "SUMMNET";

    private List attachedDocuments = null;

    private boolean hasPostitField = false;
    private boolean hasSumMontantNet = false;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getFields(BStatement statement) {
        if (hasPostitField || hasSumMontantNet) {

            String fields = APPrestationJointLotTiersDroitListViewBean.ALL_FIELDS;

            if (hasPostitField) {
                fields += ", (" + createSelectCountPostit(_getCollection()) + ") AS "
                        + APPrestationJointLotTiersDroitListViewBean.FIELDNAME_COUNT_POSTIT + " ";
            }

            if (hasSumMontantNet) {
                fields += ", (" + createSelectSumMontantNetRepartitions(_getCollection()) + ") AS "
                        + APPrestationJointLotTiersDroitListViewBean.FIELDNAME_SUM_MONTANT_NET + " ";
            }

            return fields;
        } else {
            return super._getFields(statement);
        }
    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APPrestationJointLotTiersDroitViewBean();
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
        query.append(APPrestation.FIELDNAME_IDPRESTATIONAPG);
        query.append(" AND ");
        query.append("NPTBLSRC");
        query.append(" = '");
        query.append(APApplication.KEY_POSTIT_PRESTATIONS_APG_MAT);
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
        query.append("SELECT SUM(" + APRepartitionPaiements.FIELDNAME_MONTANTNET + ") FROM ");
        query.append(schema);
        query.append(APRepartitionPaiements.TABLE_NAME);
        query.append(" WHERE ");
        query.append(APRepartitionPaiements.FIELDNAME_IDPRESTATIONAPG);
        query.append(" = ");
        query.append(APPrestation.FIELDNAME_IDPRESTATIONAPG);

        query.append(" AND (");
        query.append(APRepartitionPaiements.FIELDNAME_IDPARENT).append(" = 0");
        query.append(" OR ").append(APRepartitionPaiements.FIELDNAME_IDPARENT).append(" IS NULL ) ");

        return query.toString();
    }

    public List getAttachedDocuments() {
        return attachedDocuments;
    }

    /**
     * les CS a ne pas afficher dans l'écran
     * 
     * @return
     */
    public HashSet getExceptType() {
        HashSet truc = new HashSet();
        truc.add(IPRDemande.CS_TYPE_IJ);

        return truc;
    }

    public boolean hasPostitField() {
        return hasPostitField;
    }

    public boolean hasSumMontantNet() {
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

    public void setHasPostitField(boolean hasPostitField) {
        this.hasPostitField = hasPostitField;
    }

    public void setHasSumMontantNet(boolean hasSumMontantNet) {
        this.hasSumMontantNet = hasSumMontantNet;
    }
}
