/*
 * Créé le 17 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.vb.droits;

import globaz.apg.application.APApplication;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APDroitLAPGJointDemande;
import globaz.apg.db.droits.APDroitLAPGJointDemandeManager;
import globaz.apg.menu.IAppMenu;
import globaz.apg.util.TypePrestation;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.db.postit.FWNoteP;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * DOCUMENT ME!
 * 
 * @author vre
 */
public class APDroitLAPGJointDemandeListViewBean extends APDroitLAPGJointDemandeManager implements
        FWListViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String ALL_FIELDS = "HXNAVS, HTLDE1, HTLDE2, KUSER, FFIRSTNAME, FLASTNAME, HPDNAI, HPTSEX, "
            + "HNIPAY, HTLDU1, HTLDU2, WATETA, WAITIE, WATTDE, WAIDEM, WAIMDO, VAIGES, "
            + "VATPAY, VANPOS, VADDEP, VADREC, VATETA, VALREF, VAIDEM, VANDRO, VATGSE, "
            + "VAIDRO, VAIPAR, VAICAI, VADDDR, VADDFD, VAMDRA, VALREM, VAMTAU, VABSIM, VAIINF";

    public static final String FIELDNAME_COUNT_POSTIT = "CNTPOST";

    private boolean hasPostitField = false;

    private TypePrestation typePrestation;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getFields(BStatement statement) {
        if (hasPostitField) {
            return ALL_FIELDS + ", (" + createSelectCountPostit(_getCollection()) + ") AS " + FIELDNAME_COUNT_POSTIT
                    + " ";
        } else {
            return super._getFields(statement);
        }
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        APDroitLAPGJointDemandeViewBean retValue = new APDroitLAPGJointDemandeViewBean();

        retValue.setTypePrestation(typePrestation);

        return retValue;
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
        query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
        query.append(" AND ");
        query.append("NPTBLSRC");
        query.append(" = '");
        query.append(APApplication.KEY_POSTIT_DROIT_APG_MAT);
        query.append("'");

        return query.toString();
    }

    /**
     * getter pour l'attribut menu name
     * 
     * @return la valeur courante de l'attribut menu name
     */
    public String getMenuName() {
        if (TypePrestation.TYPE_APG.equals(typePrestation)) {
            return IAppMenu.MENU_OPTION_DROIT_APG;
        } else {
            return IAppMenu.MENU_OPTION_DROIT_AMAT;
        }
    }

    /**
     * getter pour l'attribut type prestation
     * 
     * @return la valeur courante de l'attribut type prestation
     */
    public TypePrestation getTypePrestation() {
        return typePrestation;
    }

    /**
     * Récupére le droit d'indice 'index' de ce manager et renvoie la chaine userAction permettant d'afficher ce droit,
     * par exemple 'apg.droits.droitMaternite.afficher'.
     * 
     * @param index
     *            l'indice du droit
     * 
     * @return une chaine userAction ou une chaine vide si l'indice n'est pas valable.
     */
    public String getUserAction(int index) {
        String retValue = "";

        if ((index >= 0) && (index < getSize())) {
            APDroitLAPGJointDemande droit = (APDroitLAPGJointDemande) get(index);

            retValue = TypePrestation.typePrestationInstanceForCS(droit.getTypeDemande()).toUserAction();
        }

        return retValue;
    }

    public boolean hasPostitField() {
        return hasPostitField;
    }

    public void setHasPostitField(boolean hasPostitField) {
        this.hasPostitField = hasPostitField;
    }

    /**
     * setter pour l'attribut type prestation
     * 
     * @param prestation
     *            une nouvelle valeur pour cet attribut
     */
    public void setTypePrestation(TypePrestation prestation) {
        typePrestation = prestation;
        setForTypeDemande(typePrestation.toCodeSysteme());
    }
}
