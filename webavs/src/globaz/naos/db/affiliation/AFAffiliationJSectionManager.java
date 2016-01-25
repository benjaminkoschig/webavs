package globaz.naos.db.affiliation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;

/**
 * <H1>Description</H1>
 * 
 * Un manager qui fait la jointure entre une affiliation et ses sections ouvertes
 * 
 * Ajout : Utilisation de ce manager pour faire une jointure entre une affiliation et les particularités (hpe)
 * 
 * @author vre
 */
public class AFAffiliationJSectionManager extends AFAffiliationManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final long serialVersionUID = -2444176201252940463L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private Boolean forSectionsOuvertes;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer fromClause = new StringBuffer(super._getFrom(statement));

        if (forSectionsOuvertes != null) {
            // jointure avec les comptes annexes
            fromClause.append(" INNER JOIN ");
            fromClause.append(_getCollection());
            fromClause.append(CACompteAnnexe.TABLE_CACPTAP);
            fromClause.append(" ON ");
            fromClause.append(_getCollection());
            fromClause.append("AFAFFIP.MALNAF=");
            fromClause.append(_getCollection());
            fromClause.append(CACompteAnnexe.TABLE_CACPTAP);
            fromClause.append(".");
            fromClause.append(CACompteAnnexe.FIELD_IDEXTERNEROLE);

            // jointure avec les sections
            fromClause.append(" INNER JOIN ");
            fromClause.append(_getCollection());
            fromClause.append(CASection.TABLE_CASECTP);
            fromClause.append(" ON ");
            fromClause.append(_getCollection());
            fromClause.append(CACompteAnnexe.TABLE_CACPTAP);
            fromClause.append(".");
            fromClause.append(CACompteAnnexe.FIELD_IDCOMPTEANNEXE);
            fromClause.append("=");
            fromClause.append(_getCollection());
            fromClause.append(CASection.TABLE_CASECTP);
            fromClause.append(".");
            fromClause.append(CASection.FIELD_IDCOMPTEANNEXE);

            // jointure avec les particularités
            fromClause.append(" LEFT JOIN ");
            fromClause.append(_getCollection());
            fromClause.append("AFPARTP");
            fromClause.append(" ON ");
            fromClause.append(_getCollection());
            fromClause.append("AFAFFIP");
            fromClause.append(".");
            fromClause.append("MAIAFF");
            fromClause.append("=");
            fromClause.append(_getCollection());
            fromClause.append("AFPARTP");
            fromClause.append(".");
            fromClause.append("MAIAFF");
            // recherche uniquement le type de particularité "sans personnel"
            // (introuvable dans les CS !!!)
            fromClause.append(" AND " + _getCollection() + "AFPARTP.MFTPAR=19120053 ");
        }

        return fromClause.toString();
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer whereClause = new StringBuffer(super._getWhere(statement));

        whereClause.insert(0, "(");

        whereClause.append(" OR ");
        whereClause.append(_getCollection());
        whereClause.append("AFPARTP.MFDDEB");
        whereClause.append(">=");
        whereClause.append(this._dbWriteDateAMJ(statement.getTransaction(), getFromDateFinNonVide()));
        whereClause.append(")");

        if (forSectionsOuvertes != null) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(_getCollection());
            whereClause.append(CASection.TABLE_CASECTP);
            whereClause.append(".");
            whereClause.append(CASection.FIELD_SOLDE);
            whereClause.append("<>0.00");
        }

        return whereClause.toString();
    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFAffiliationJSection();
    }

    /**
     * getter pour l'attribut for sections ouvertes.
     * 
     * @return la valeur courante de l'attribut for sections ouvertes
     */
    public Boolean getForSectionsOuvertes() {
        return forSectionsOuvertes;
    }

    /**
     * setter pour l'attribut for sections ouvertes.
     * 
     * @param forSectionsOuvertes
     *            une nouvelle valeur pour cet attribut
     */
    public void setForSectionsOuvertes(Boolean forSectionsOuvertes) {
        this.forSectionsOuvertes = forSectionsOuvertes;
    }
}
