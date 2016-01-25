/*
 * Créé le 2 déc. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.db.planCaisse;

import globaz.globall.db.BStatement;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class AFPlanCaisseJCouverture extends AFPlanCaisse {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClause = new StringBuffer();

        fromClause.append(schema);
        fromClause.append("AFPLCAP INNER JOIN ");
        fromClause.append(schema);
        fromClause.append("AFCOUVP ON ");
        fromClause.append(schema);
        fromClause.append("AFPLCAP.MSIPLC=");
        fromClause.append(schema);
        fromClause.append("AFCOUVP.MSIPLC");

        return fromClause.toString();
    }

    private String assuranceId;
    private String dateDebut;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String dateFin;

    /**
     * @see globaz.globall.db.BEntity#_autoInherits()
     */
    @Override
    protected boolean _autoInherits() {
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return AFPlanCaisseJCouverture.createFromClause(_getCollection());
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        assuranceId = statement.dbReadNumeric("MBIASS");
        dateDebut = statement.dbReadDateAMJ("MTDDEB");
        dateFin = statement.dbReadDateAMJ("MTDFIN");
    }
}
