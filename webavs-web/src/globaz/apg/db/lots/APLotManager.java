/*
 * Créé le 6 juin 05
 */
package globaz.apg.db.lots;

import globaz.apg.api.lots.IAPLot;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import java.util.Vector;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APLotManager extends PRAbstractManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** Critère de rechercher pour lister les lots non validés */
    public static final String FOR_NON_VALIDE = "nonValides";

    /** Critere de rechercher pour lister tous les lots */
    public static final String FOR_TOUS = "tous";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * Permet de récupérer tout les ids et descriptions des lots ouverts
     * 
     * @param session
     *            une Bsession
     * @param typePrestation
     *            DOCUMENT ME!
     * 
     * @return Un tableau de String contenant les ids et descriptions des lots ouverts sous la forme (t[0]=id1,
     *         t[1]=desc1, t[2]=id2, t[3]=desc2, etc.). Renvoie une un tableau de longueur 0 si une erreur est
     *         rencontrée
     */
    public static Vector getIdsDescriptionsLotsOuverts(BSession session, String typePrestation) {

        Vector v = new Vector();

        try {
            APLotManager mgr = new APLotManager();
            APLot lot = null;
            mgr.setSession(session);
            mgr.setForEtat(IAPLot.CS_OUVERT);

            if (!JadeStringUtil.isIntegerEmpty(typePrestation)) {
                mgr.setForTypeLot(typePrestation);
            }

            mgr.find(BManager.SIZE_NOLIMIT);

            for (int i = 0; i < mgr.size(); i++) {
                lot = (APLot) (mgr.getEntity(i));
                v.add(i, new String[] { lot.getIdLot(), lot.getId() + " " + lot.getDescription() });
            }
            return v;
        } catch (Exception e) {
            return v;
        }
    }

    /**
     * Permet de récupérer tout les ids et descriptions des lots ouverts
     * 
     * @param session
     *            une Bsession
     * @param typePrestation
     *            DOCUMENT ME!
     * 
     * @return Un tableau de String contenant les ids et descriptions des lots ouverts sous la forme (t[0]=id1,
     *         t[1]=desc1, t[2]=id2, t[3]=desc2, etc.). Renvoie une un tableau de longueur 0 si une erreur est
     *         rencontrée
     */
    public static Vector getIdsDescriptionsLotsOuvertsOuCompenses(BSession session, String typePrestation) {

        Vector v = new Vector();

        try {
            APLotManager mgr = new APLotManager();
            APLot lot = null;
            mgr.setSession(session);
            mgr.setForEtatDifferentDe(IAPLot.CS_VALIDE);

            if (!JadeStringUtil.isIntegerEmpty(typePrestation)) {
                mgr.setForTypeLot(typePrestation);
            }

            mgr.find(BManager.SIZE_NOLIMIT);

            for (int i = 0; i < mgr.size(); i++) {
                lot = (APLot) (mgr.getEntity(i));
                v.add(i, new String[] { lot.getIdLot(), lot.getId() + " " + lot.getDescription() });
            }

            return v;
        } catch (Exception e) {
            return v;
        }
    }

    private String forEtat = "";
    private String forEtatDifferentDe = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String forTypeLot = "";

    private String fromDateCreation = "";

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JAUtil.isDateEmpty(getFromDateCreation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APLot.FIELDNAME_DATECREATION + ">="
                    + _dbWriteDateAMJ(statement.getTransaction(), getFromDateCreation());
        }

        // si on doit tout lister, pas besoin de rajouter ds la clause Where
        if (!JadeStringUtil.isEmpty(getForEtat()) && !getForEtat().equals(FOR_TOUS)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            if (getForEtat().equals(FOR_NON_VALIDE)) {
                sqlWhere += APLot.FIELDNAME_ETAT + "<>" + _dbWriteNumeric(statement.getTransaction(), IAPLot.CS_VALIDE);
            } else { // validé, ouvert ou compensé
                sqlWhere += APLot.FIELDNAME_ETAT + "=" + _dbWriteNumeric(statement.getTransaction(), getForEtat());
            }
        }

        if (!JadeStringUtil.isIntegerEmpty(forEtatDifferentDe)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APLot.FIELDNAME_ETAT + "<>" + _dbWriteNumeric(statement.getTransaction(), forEtatDifferentDe);
        }

        if (!JadeStringUtil.isIntegerEmpty(forTypeLot)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APLot.FIELDNAME_TYPELOT + "=" + _dbWriteNumeric(statement.getTransaction(), forTypeLot);
        }

        return sqlWhere;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APLot();
    }

    /**
     * getter pour l'attribut for etat
     * 
     * @return la valeur courante de l'attribut for etat
     */
    public String getForEtat() {
        return forEtat;
    }

    /**
     * getter pour l'attribut for etat different de
     * 
     * @return la valeur courante de l'attribut for etat different de
     */
    public String getForEtatDifferentDe() {
        return forEtatDifferentDe;
    }

    /**
     * getter pour l'attribut for type lot
     * 
     * @return la valeur courante de l'attribut for type lot
     */
    public String getForTypeLot() {
        return forTypeLot;
    }

    /**
     * getter pour l'attribut from date creaction
     * 
     * @return la valeur courante de l'attribut from date creaction
     */
    public String getFromDateCreation() {
        return fromDateCreation;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     * 
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        return APLot.FIELDNAME_IDLOT + " DESC";
    }

    /**
     * setter pour l'attribut for etat
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForEtat(String string) {
        forEtat = string;
    }

    /**
     * setter pour l'attribut for etat different de
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForEtatDifferentDe(String string) {
        forEtatDifferentDe = string;
    }

    /**
     * setter pour l'attribut for type lot
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForTypeLot(String string) {
        forTypeLot = string;
    }

    /**
     * setter pour l'attribut from date creaction
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setFromDateCreation(String string) {
        fromDateCreation = string;
    }
}
