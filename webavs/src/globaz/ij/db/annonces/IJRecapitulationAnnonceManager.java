/*
 * Créé le 1er septembre 2006
 */

package globaz.ij.db.annonces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * @deprecated Les requêtes sql générées sont fausses. Doubles les valeurs dans le cas de periodes attestée interne et
 *             externe.
 * @author hpe
 */

@Deprecated
public class IJRecapitulationAnnonceManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forMoisAnneeComptable = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return IJRecapitulationAnnonce.createFields(_getCollection());
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer where = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(forMoisAnneeComptable)) {
            if (where.length() > 0) {
                where.append(" AND ");
            }

            where.append(IJAnnonce.FIELDNAME_MOISANNEECOMPTABLE);
            where.append(" =");
            where.append(formatDateToDB(forMoisAnneeComptable));
        }

        where.append(" GROUP BY ");
        where.append(IJAnnonce.FIELDNAME_CONTENUANNONCE);
        where.append(", ");
        where.append(IJAnnonce.FIELDNAME_MOISANNEECOMPTABLE);

        return where.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new IJRecapitulationAnnonce();
    }

    private String formatDateToDB(String date) {
        if (date.length() == 6) {
            return date.substring(2) + "0" + date.substring(0, 1);
        } else {
            return date.substring(3) + date.substring(0, 2);
        }
    }

    /**
     * getter pour l'attribut mois annee comptable
     * 
     * @return la valeur courante de l'attribut mois annee comptable
     */
    public String getForMoisAnneeComptable() {
        return forMoisAnneeComptable;
    }

    /**
     * setter pour l'attribut mois annee comptable
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForMoisAnneeComptable(String string) {
        forMoisAnneeComptable = string;
    }

}
