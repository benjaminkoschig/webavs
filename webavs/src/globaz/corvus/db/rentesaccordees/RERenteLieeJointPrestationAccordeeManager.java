package globaz.corvus.db.rentesaccordees;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.hera.api.ISFMembreFamille;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author HPE
 */
public class RERenteLieeJointPrestationAccordeeManager extends PRAbstractManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsEtat;
    private List<String> forCsEtatIn;
    private String forGenre;
    private String forIdTiersLiant;
    private String idTiersFamillePourWhere;
    private boolean isRechercheRenteEnCours = false;
    private int nbTiersFamille;

    public RERenteLieeJointPrestationAccordeeManager() {
        super();

        forCsEtat = "";
        forCsEtatIn = new ArrayList<String>();
        forGenre = "";
        forIdTiersLiant = "";
        idTiersFamillePourWhere = "";
        nbTiersFamille = 0;

        wantCallMethodBeforeFind(true);
    }

    @Override
    protected void _beforeFind(BTransaction transaction) throws Exception {

        // S'il y a un tiers liant
        if (!JadeStringUtil.isEmpty(forIdTiersLiant)) {

            // Début de la création de la String
            idTiersFamillePourWhere += "(";

            // Retrouver toutes les personnes de la famille du liant
            // Rechercher les membres de la famille (Enfants & Conjoints)
            globaz.hera.api.ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(getSession(),
                    ISFSituationFamiliale.CS_DOMAINE_RENTES, forIdTiersLiant);
            ISFMembreFamilleRequerant[] membresFamille = sf.getMembresFamille(forIdTiersLiant);

            boolean isPremierPassage = true;

            // Pour chaque membre
            for (int i = 0; (membresFamille != null) && (i < membresFamille.length); i++) {
                ISFMembreFamilleRequerant mf = membresFamille[i];

                // Pas d'idTiers, pas de rentes
                if (!JadeStringUtil.isEmpty(mf.getIdTiers())) {

                    nbTiersFamille++;

                    if (isPremierPassage) {
                        idTiersFamillePourWhere += mf.getIdTiers();
                        isPremierPassage = false;
                    } else {
                        idTiersFamillePourWhere += ", " + mf.getIdTiers();
                    }

                }

            }

            // Rechercher les parents
            ISFMembreFamille[] membresFam = sf.getParents(forIdTiersLiant);

            // Pour chaque parent
            for (int i = 0; (membresFam != null) && (i < membresFam.length); i++) {
                ISFMembreFamille mf = membresFam[i];

                // Pas d'idTiers, pas de rentes
                if (!JadeStringUtil.isEmpty(mf.getIdTiers())) {

                    nbTiersFamille++;

                    if (isPremierPassage) {
                        idTiersFamillePourWhere += mf.getIdTiers();
                        isPremierPassage = false;
                    } else {
                        idTiersFamillePourWhere += ", " + mf.getIdTiers();
                    }

                }

            }

            // finir la string
            idTiersFamillePourWhere += ")";

        }

    }

    @Override
    protected String _getWhere(BStatement statement) {

        StringBuilder sql = new StringBuilder();

        String schema = _getCollection();

        if (!JadeStringUtil.isBlank(forIdTiersLiant)) {
            if (nbTiersFamille > 0) {
                sql.append(schema).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                        .append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
                sql.append(" IN ");
                sql.append(idTiersFamillePourWhere);
            } else {
                sql.append(schema).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                        .append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
                sql.append("=");
                sql.append(this._dbWriteNumeric(statement.getTransaction(), forIdTiersLiant));
            }
        }

        if (!JadeStringUtil.isBlank(forGenre)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                    .append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION);
            sql.append("=");
            sql.append(this._dbWriteString(statement.getTransaction(), forGenre));
        }

        if (!JadeStringUtil.isBlank(forCsEtat)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                    .append(REPrestationsAccordees.FIELDNAME_CS_ETAT);
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), forCsEtat));
        }

        if (forCsEtatIn.size() > 0) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(schema).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                    .append(REPrestationsAccordees.FIELDNAME_CS_ETAT);
            sql.append(" IN (");
            for (Iterator<String> iterator = forCsEtatIn.iterator(); iterator.hasNext();) {
                sql.append(iterator.next());
                if (iterator.hasNext()) {
                    sql.append(",");
                }
            }
            sql.append(")");
        }

        if (isRechercheRenteEnCours) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(" (" + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " = 0" + " OR "
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " IS NULL ) ");
        }

        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RERenteLieeJointPrestationAccordee();
    }

    public String getForCsEtat() {
        return forCsEtat;
    }

    public List<String> getForCsEtatIn() {
        return forCsEtatIn;
    }

    public String getForGenre() {
        return forGenre;
    }

    public String getForIdTiersLiant() {
        return forIdTiersLiant;
    }

    public boolean getisRechercheRenteEnCours() {
        return isRechercheRenteEnCours;
    }

    @Override
    public String getOrderByDefaut() {
        return REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE;
    }

    public boolean isRechercheRenteEnCours() {
        return isRechercheRenteEnCours;
    }

    public void setForCsEtat(String string) {
        forCsEtat = string;
    }

    public void setForCsEtatIn(List<String> forCsEtatIn) {
        this.forCsEtatIn = forCsEtatIn;
    }

    public void setForGenre(String string) {
        forGenre = string;
    }

    public void setForIdTiersLiant(String string) {
        forIdTiersLiant = string;
    }

    public void setIsRechercheRenteEnCours(boolean isRechercheRenteEnCours) {
        this.isRechercheRenteEnCours = isRechercheRenteEnCours;
    }

    public void setRechercheRenteEnCours(boolean isRechercheRenteEnCours) {
        this.isRechercheRenteEnCours = isRechercheRenteEnCours;
    }
}
