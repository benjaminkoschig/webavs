/*
 * Créé le 8 sept. 05
 */
package globaz.ij.db.lots;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAUtil;
import globaz.ij.api.lots.IIJLot;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import java.util.Vector;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJLotManager extends PRAbstractManager {

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

    public static Vector getIdsDescriptionsLotsOuverts(BSession session) {

        Vector v = new Vector();
        try {
            IJLotManager mgr = new IJLotManager();
            IJLot lot = null;
            mgr.setSession(session);
            mgr.setForCsEtat(IIJLot.CS_OUVERT);
            mgr.find(BManager.SIZE_NOLIMIT);

            String[] liste = new String[mgr.size() * 2];

            for (int i = 0; i < mgr.size(); i++) {
                lot = (IJLot) (mgr.getEntity(i));
                v.add(0, new String[] { lot.getIdLot(), lot.getId() + " " + lot.getDescription() });
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
     * 
     * @return Un Vecteur contenant un table de string [idLot, Descrition]
     */

    public static Vector getIdsDescriptionsLotsOuvertsOuCompenses(BSession session) {

        Vector v = new Vector();
        try {
            IJLotManager mgr = new IJLotManager();
            IJLot lot = null;
            mgr.setSession(session);
            mgr.setForCsEtat(FOR_NON_VALIDE);
            mgr.find(BManager.SIZE_NOLIMIT);

            for (int i = 0; i < mgr.size(); i++) {
                lot = (IJLot) mgr.getEntity(i);
                v.add(0, new String[] { lot.getIdLot(), lot.getId() + " " + lot.getDescription() });
            }

            return v;
        } catch (Exception e) {
            return v;
        }
    }

    private String forCsEtat = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String forCsEtatDifferentDe = "";

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

            sqlWhere += IJLot.FIELDNAME_DATECREATION + ">="
                    + _dbWriteDateAMJ(statement.getTransaction(), getFromDateCreation());
        }

        // si on doit tout lister, pas besoin de rajouter ds la clause Where
        if (!JadeStringUtil.isEmpty(getForCsEtat()) && !getForCsEtat().equals(FOR_TOUS)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            if (getForCsEtat().equals(FOR_NON_VALIDE)) {
                sqlWhere += IJLot.FIELDNAME_CS_ETAT + "<>"
                        + _dbWriteNumeric(statement.getTransaction(), IIJLot.CS_VALIDE);
            } else { // validé, ouvert ou compensé
                sqlWhere += IJLot.FIELDNAME_CS_ETAT + "=" + _dbWriteNumeric(statement.getTransaction(), getForCsEtat());
            }
        }

        return sqlWhere;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new IJLot();
    }

    /**
     * getter pour l'attribut for cs etat
     * 
     * @return la valeur courante de l'attribut for cs etat
     */
    public String getForCsEtat() {
        return forCsEtat;
    }

    /**
     * getter pour l'attribut for cs etat different de
     * 
     * @return la valeur courante de l'attribut for cs etat different de
     */
    public String getForCsEtatDifferentDe() {
        return forCsEtatDifferentDe;
    }

    /**
     * getter pour l'attribut from date creation
     * 
     * @return la valeur courante de l'attribut from date creation
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
        return IJLot.FIELDNAME_IDLOT + " DESC ";
    }

    /**
     * setter pour l'attribut for cs etat
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForCsEtat(String string) {
        forCsEtat = string;
    }

    /**
     * setter pour l'attribut for cs etat different de
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForCsEtatDifferentDe(String string) {
        forCsEtatDifferentDe = string;
    }

    /**
     * setter pour l'attribut from date creation
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setFromDateCreation(String string) {
        fromDateCreation = string;
    }
}
