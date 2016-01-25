package globaz.apg.db.droits;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAVector;
import globaz.jade.client.util.JadePeriodWrapper;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.pyxis.db.tiers.ITIPersonneAvsDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author PBA
 */
public class APDroitAPGJointTiersManager extends PRAbstractManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsGenreService;
    private List<String> forCsGenreServiceIn;
    private String forDroitContenuDansDateDebut;
    private String forDroitContenuDansDateFin;
    private String forIdTiers;
    private String likeNumeroAvs;
    private List<String> forEtatDroitNotIn;

    public APDroitAPGJointTiersManager() {
        super();

        forCsGenreService = "";
        forCsGenreServiceIn = new ArrayList<String>();
        forDroitContenuDansDateDebut = "";
        forDroitContenuDansDateFin = "";
        forIdTiers = "";
        likeNumeroAvs = "";

        wantCallMethodAfterFind(true);
    }

    @Override
    protected void _afterFind(BTransaction transaction) throws Exception {

        Map<String, List<APDroitAPGJointTiers>> droitsParTiers = new HashMap<String, List<APDroitAPGJointTiers>>();

        for (int i = 0; i < container.size(); i++) {
            APDroitAPGJointTiers unDroit = (APDroitAPGJointTiers) container.get(i);

            List<APDroitAPGJointTiers> droitsDuTiers;
            if (droitsParTiers.containsKey(unDroit.getIdTiers())) {
                droitsDuTiers = droitsParTiers.get(unDroit.getIdTiers());
            } else {
                droitsDuTiers = new ArrayList<APDroitAPGJointTiers>();
                droitsParTiers.put(unDroit.getIdTiers(), droitsDuTiers);
            }
            droitsDuTiers.add(unDroit);
        }

        Map<String, APDroitAPGJointTiers> droitParIdDroit = new HashMap<String, APDroitAPGJointTiers>();

        for (List<APDroitAPGJointTiers> droitsPourUnTiers : droitsParTiers.values()) {
            for (APDroitAPGJointTiers unDroitDuTiers : droitsPourUnTiers) {
                if (droitParIdDroit.containsKey(unDroitDuTiers.getIdDroit())) {
                    APDroitAPGJointTiers droit = droitParIdDroit.get(unDroitDuTiers.getIdDroit());
                    droit.getPeriodes().add(
                            new JadePeriodWrapper(unDroitDuTiers.getUneDateDebutPeriode(), unDroitDuTiers
                                    .getUneDateFinPeriode()));
                } else {
                    unDroitDuTiers.getPeriodes().add(
                            new JadePeriodWrapper(unDroitDuTiers.getUneDateDebutPeriode(), unDroitDuTiers
                                    .getUneDateFinPeriode()));
                    droitParIdDroit.put(unDroitDuTiers.getIdDroit(), unDroitDuTiers);
                }
            }
        }

        List<APDroitAPGJointTiers> listeDeTri = new ArrayList<APDroitAPGJointTiers>(droitParIdDroit.values());
        Collections.sort(listeDeTri, new Comparator<APDroitAPGJointTiers>() {
            @Override
            public int compare(APDroitAPGJointTiers droit1, APDroitAPGJointTiers droit2) {
                int compareNom = JadeStringUtil.convertSpecialChars(droit1.getNomTiers()).compareTo(
                        JadeStringUtil.convertSpecialChars(droit2.getNomTiers()));
                if (compareNom != 0) {
                    return compareNom;
                }

                int comparePrenom = JadeStringUtil.convertSpecialChars(droit1.getPrenomTiers()).compareTo(
                        JadeStringUtil.convertSpecialChars(droit2.getPrenomTiers()));
                if (comparePrenom != 0) {
                    return comparePrenom;
                }

                return droit1.getNumeroAvsTiers().compareTo(droit2.getNumeroAvsTiers());
            }
        });

        JAVector newContainer = new JAVector();
        for (APDroitAPGJointTiers unDroit : droitParIdDroit.values()) {
            newContainer.add(unDroit);
        }
        container = newContainer;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        String tableDroitLAPG = _getCollection() + APDroitLAPG.TABLE_NAME_LAPG;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;
        String tablePersonneAvs = _getCollection() + ITIPersonneAvsDefTable.TABLE_NAME;

        sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
        sql.append(" NOT IN (SELECT DISTINCT ").append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT).append(" FROM ")
                .append(tableDroitLAPG).append(")");

        if (!JadeStringUtil.isBlank(forCsGenreService)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_GENRESERVICE);
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), forCsGenreService));
        }

        if (!forCsGenreServiceIn.isEmpty()) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_GENRESERVICE);
            sql.append(" IN(");
            for (Iterator<String> iterateurGenre = forCsGenreServiceIn.iterator(); iterateurGenre.hasNext();) {
                sql.append(iterateurGenre.next());
                if (iterateurGenre.hasNext()) {
                    sql.append(",");
                }
            }
            sql.append(")");
        }

        if (!JadeStringUtil.isBlank(forDroitContenuDansDateDebut)
                && !JadeStringUtil.isBlank(forDroitContenuDansDateFin)) {

            if (sql.length() > 0) {
                sql.append(" AND ");
            }

            sql.append("(");

            sql.append("(");
            sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_DATEDEBUTDROIT);
            sql.append(">=");
            sql.append(this._dbWriteDateAMJ(statement.getTransaction(), forDroitContenuDansDateDebut));
            sql.append(" AND ");
            sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_DATEDEBUTDROIT);
            sql.append("<=");
            sql.append(this._dbWriteDateAMJ(statement.getTransaction(), forDroitContenuDansDateFin));

            sql.append(") OR (");

            sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_DATEFINDROIT);
            sql.append(">=");
            sql.append(this._dbWriteDateAMJ(statement.getTransaction(), forDroitContenuDansDateDebut));
            sql.append(" AND ");
            sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_DATEFINDROIT);
            sql.append("<=");
            sql.append(this._dbWriteDateAMJ(statement.getTransaction(), forDroitContenuDansDateFin));

            sql.append(") OR (");

            sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_DATEDEBUTDROIT);
            sql.append("<=");
            sql.append(this._dbWriteDateAMJ(statement.getTransaction(), forDroitContenuDansDateDebut));
            sql.append(" AND ");
            sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_DATEFINDROIT);
            sql.append(">=");
            sql.append(this._dbWriteDateAMJ(statement.getTransaction(), forDroitContenuDansDateFin));

            sql.append("))");
        }

        if (!JadeStringUtil.isBlank(forIdTiers)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS);
            sql.append("=");
            sql.append(forIdTiers);
        }

        if (!JadeStringUtil.isBlank(likeNumeroAvs)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tablePersonneAvs).append(".").append(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL);
            sql.append(" LIKE ");
            sql.append(this._dbWriteString(statement.getTransaction(), likeNumeroAvs + "%"));
        }
        if ((forEtatDroitNotIn != null) && (forEtatDroitNotIn.size() > 0)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_ETAT);
            sql.append(" NOT IN (");
            for (int ctr = 0; ctr < forEtatDroitNotIn.size(); ctr++) {
                sql.append(forEtatDroitNotIn.get(ctr));
                if ((ctr + 1) < forEtatDroitNotIn.size()) {
                    sql.append(", ");
                }
            }
            sql.append(")");
        }
        return sql.toString();
    }

    @Override
    protected APDroitAPGJointTiers _newEntity() throws Exception {
        return new APDroitAPGJointTiers();
    }

    public String getForCsGenreService() {
        return forCsGenreService;
    }

    public List<String> getForCsGenreServiceIn() {
        return forCsGenreServiceIn;
    }

    public String getForDroitContenuDansDateDebut() {
        return forDroitContenuDansDateDebut;
    }

    public String getForDroitContenuDansDateFin() {
        return forDroitContenuDansDateFin;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getLikeNumeroAvs() {
        return likeNumeroAvs;
    }

    @Override
    public String getOrderByDefaut() {
        return APDroitAPG.FIELDNAME_IDDROIT_APG;
    }

    public void setForCsGenreService(String forCsGenreService) {
        this.forCsGenreService = forCsGenreService;
    }

    public void setForCsGenreServiceIn(List<String> forCsGenreServiceIn) {
        this.forCsGenreServiceIn = forCsGenreServiceIn;
    }

    public void setForDroitContenuDansDateDebut(String forDroitContenuDansDateDebut) {
        this.forDroitContenuDansDateDebut = forDroitContenuDansDateDebut;
    }

    public void setForDroitContenuDansDateFin(String forDroitContenuDansDateFin) {
        this.forDroitContenuDansDateFin = forDroitContenuDansDateFin;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setLikeNumeroAvs(String likeNumeroAvs) {
        this.likeNumeroAvs = likeNumeroAvs;
    }

    public final List<String> getForEtatDroitNotIn() {
        return forEtatDroitNotIn;
    }

    public final void setForEtatDroitNotIn(List<String> forEtatDroitNotIn) {
        this.forEtatDroitNotIn = forEtatDroitNotIn;
    }

}
