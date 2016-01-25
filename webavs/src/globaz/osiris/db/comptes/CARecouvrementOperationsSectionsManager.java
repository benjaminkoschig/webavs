package globaz.osiris.db.comptes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIVPPoste;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author sch
 *         Exemple de la requête :
 *         SELECT
 *         OP.ANNEECOTISATION annee,
 *         OP.IDCOMPTE IDRUBRIQUE,
 *         TP.AGIR idRubriqueRecouvrement,
 *         TP.BSTPOR typeOrdre,
 *         SE.DATEFINPERIODE,
 *         SE.IDSECTION,
 *         TP.AGII,
 *         sum(OP.MONTANT) cumulMontant
 *         FROM WEBAVSS.CAOPERP OP
 *         INNER JOIN WEBAVSS.CARUBRP RU ON OP.IDCOMPTE = RU.IDRUBRIQUE
 *         INNER JOIN WEBAVSS.CAOCCP TP ON OP.IDCOMPTE = TP.AGID or IDCOMPTE = TP.AGII
 *         INNER JOIN WEBAVSS.CASECTP SE ON OP.IDSECTION = SE.IDSECTION
 *         WHERE OP.IDSECTION IN (3351242,3381732)
 *         AND OP.IDTYPEOPERATION like 'E%'
 *         AND OP.ETAT = 205002
 *         AND OP.MONTANT < 0
 *         AND TP.BSTPRO = 238003
 *         GROUP BY OP.ANNEECOTISATION,
 *         op.IDCOMPTE,
 *         tp.agir,
 *         tp.bstpor,
 *         se.DATEFINPERIODE,
 *         se.IDSECTION,
 *         TP.AGII
 */
public class CARecouvrementOperationsSectionsManager extends BManager {
    private static final long serialVersionUID = 4677798792952501515L;
    private Collection<String> forIdSectionIn = null;

    /**
     * @see globaz.globall.db.BManager#_getSql(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getSql(BStatement statement) {
        String select = "";
        if ((getForIdSectionIn() != null) && (getForIdSectionIn().size() > 0)) {

            StringBuilder idSectionsSelectionnees = new StringBuilder();
            Iterator<String> iter = getForIdSectionIn().iterator();
            boolean isFirstElement = true;
            while (iter.hasNext()) {
                if (isFirstElement) {
                    isFirstElement = false;
                } else {
                    idSectionsSelectionnees.append(",");
                }
                idSectionsSelectionnees.append(iter.next());
            }

            select = "SELECT OP." + CAOperation.FIELD_ANNEECOTISATION + " annee, OP." + CAOperation.FIELD_IDCOMPTE
                    + " IDRUBRIQUE, TP.AGIR idRubriqueRecouvrement,TP.BSTPOR typeOrdre, SE."
                    + CASection.FIELD_DATEFINPERIODE + ", SE." + CASection.FIELD_IDSECTION + ", TP.AGII, sum(OP."
                    + CAOperation.FIELD_MONTANT + ") cumulMontant";
            select += " FROM " + _getCollection() + CAOperation.TABLE_CAOPERP + " OP ";
            select += " INNER JOIN " + _getCollection() + CARubrique.TABLE_CARUBRP + " RU ";
            select += " ON OP." + CAOperation.FIELD_IDCOMPTE + " = RU." + CARubrique.FIELD_IDRUBRIQUE;
            select += " INNER JOIN " + _getCollection() + "CAOCCP TP ";
            select += " ON OP." + CAOperation.FIELD_IDCOMPTE + " = TP.AGID or " + CAOperation.FIELD_IDCOMPTE
                    + " = TP.AGII";
            select += " INNER JOIN " + _getCollection() + CASection.TABLE_CASECTP + " SE ";
            select += " ON OP." + CAOperation.FIELD_IDSECTION + " = SE." + CASection.FIELD_IDSECTION;
            select += " WHERE OP." + CAOperation.FIELD_IDSECTION + " IN (" + idSectionsSelectionnees + ")";
            select += " AND OP." + CAOperation.FIELD_IDTYPEOPERATION + " like 'E%'";
            select += " AND OP." + CAOperation.FIELD_ETAT + " = " + APIOperation.ETAT_COMPTABILISE;
            select += " AND OP." + CAOperation.FIELD_MONTANT + " < 0";
            select += " AND TP.BSTPRO = " + APIVPPoste.CS_PROCEDURE_AUTRE_PROCEDURE;
            select += " GROUP BY OP." + CAOperation.FIELD_ANNEECOTISATION + ", op." + CAOperation.FIELD_IDCOMPTE
                    + ", tp.agir, tp.bstpor, se." + CASection.FIELD_DATEFINPERIODE + ", se."
                    + CASection.FIELD_IDSECTION + ", TP.AGII";
        }
        return select;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        // TODO Auto-generated method stub
        return new CARecouvrementOperationsSections();
    }

    /**
     * Retourne la Collection des idSections
     * 
     * @return Collection<String>
     */
    public Collection<String> getForIdSectionIn() {
        return forIdSectionIn;
    }

    /**
     * Set la Collection des idSections
     * 
     * @param forIdSectionIn
     */
    public void setForIdSectionIn(Collection<String> forIdSectionIn) {
        this.forIdSectionIn = forIdSectionIn;
    }

}
