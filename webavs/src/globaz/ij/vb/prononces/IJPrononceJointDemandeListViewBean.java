/*
 * Créé le 17 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.ij.vb.prononces;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.db.postit.FWNoteP;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.ij.application.IJApplication;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.db.prononces.IJPrononceJointDemandeManager;

/**
 * DOCUMENT ME!
 * 
 * @author vre
 */
public class IJPrononceJointDemandeListViewBean extends IJPrononceJointDemandeManager implements
        FWListViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String ALL_FIELDS = "HXNAVS, HTLDE1, HTLDE2, HPDNAI, HPTSEX, HNIPAY, KUSER, FFIRSTNAME, FLASTNAME, "
            + "HTLDU1, HTLDU2, XBDECH, WATETA, WAITIE, WATTDE, WAIDEM, WAIMDO, XBIGES, "
            + "XBIPAI, XBTETA, XBDDDR, XBDFDR, XBIPAR, XBTTIJ, XBDECH, XBTMOE, XBIDEC, XBBSIM, "
            + IJPrononce.FIELDNAME_PARENT_CORRIGE_DEPUIS;

    public static final String FIELDNAME_COUNT_POSTIT = "CNTPOST";

    private boolean hasPostitField = false;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getFields(BStatement statement) {
        if (hasPostitField) {
            return IJPrononceJointDemandeListViewBean.ALL_FIELDS + ", (" + createSelectCountPostit(_getCollection())
                    + ") AS " + IJPrononceJointDemandeListViewBean.FIELDNAME_COUNT_POSTIT + " ";
        } else {
            return super._getFields(statement);
        }
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new IJPrononceJointDemandeViewBean();
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
        query.append(IJPrononce.FIELDNAME_ID_PRONONCE);
        query.append(" AND ");
        query.append("NPTBLSRC");
        query.append(" = '");
        query.append(IJApplication.KEY_POSTIT_PRONONCES);
        query.append("'");

        return query.toString();
    }

    public boolean hasPostitField() {
        return hasPostitField;
    }

    public void setHasPostitField(boolean hasPostitField) {
        this.hasPostitField = hasPostitField;
    }
}
