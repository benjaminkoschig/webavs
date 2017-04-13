package globaz.osiris.db.comptes;

import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.google.common.base.Joiner;

/**
 * Manager spécialisé pour une utilisation avec le widget d'auto-complétion lors de la recherche de factures.<br/>
 * 
 * @author PBA
 */
public class CASectionJoinCompteAnnexeJoinTiersManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ALIAS_LIBELLE_CATEGORIE_SECTION = "categorieSection";
    public static final String ALIAS_LIBELLE_ROLE_COMPTE_ANNEXE = "roleCompteAnnexe";
    public static final String ALIAS_LIBELLE_TYPE_SECTION = "typeSection";

    private String forIdCompteAnnexe = "";
    private String forIdTiers = "";
    private Collection<String> forIdTiersIn = null;
    private Boolean forSoldeDifferentZero = false;
    private Boolean forSoldeNegatif = false;
    private Boolean forSoldePositif = false;
    private String likeIdExterne = "";
    private String forTypeSection;
    private Collection<String> forIdSectionIn = new ArrayList<String>();
    private String forCategorie;

    public CASectionJoinCompteAnnexeJoinTiersManager() {
        super();
    }

    @Override
    protected String _getFields(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        sql.append(_getCollection()).append(CACompteAnnexe.TABLE_CACPTAP).append(".")
                .append(CACompteAnnexe.FIELD_IDEXTERNEROLE).append(",");
        sql.append(_getCollection()).append(CACompteAnnexe.TABLE_CACPTAP).append(".")
                .append(CACompteAnnexe.FIELD_IDCOMPTEANNEXE).append(",");
        sql.append(_getCollection()).append(CACompteAnnexe.TABLE_CACPTAP).append(".")
                .append(CACompteAnnexe.FIELD_DESCRIPTION).append(",");
        sql.append(_getCollection()).append(CACompteAnnexe.TABLE_CACPTAP).append(".")
                .append(CACompteAnnexe.FIELD_IDROLE).append(",");
        sql.append(_getCollection()).append("TITIERP").append(".").append("HTLDE1").append(",");
        sql.append(_getCollection()).append("TITIERP").append(".").append("HTLDE2").append(",");
        sql.append(_getCollection()).append(CASection.TABLE_CASECTP).append(".").append(CASection.FIELD_SOLDE)
                .append(",");
        sql.append(_getCollection()).append(CASection.TABLE_CASECTP).append(".").append(CASection.FIELD_IDEXTERNE)
                .append(",");
        sql.append(_getCollection()).append(CASection.TABLE_CASECTP).append(".").append(CASection.FIELD_IDSECTION)
                .append(",");
        sql.append(_getCollection()).append(CASection.TABLE_CASECTP).append(".").append(CASection.FIELD_IDTYPESECTION)
                .append(",");
        sql.append(_getCollection()).append("PMTRADP").append(".").append("LIBELLE").append(" AS ")
                .append(CASectionJoinCompteAnnexeJoinTiersManager.ALIAS_LIBELLE_TYPE_SECTION).append(",");
        sql.append(CASectionJoinCompteAnnexeJoinTiersManager.ALIAS_LIBELLE_CATEGORIE_SECTION).append(".")
                .append("PCOLUT").append(" AS ")
                .append(CASectionJoinCompteAnnexeJoinTiersManager.ALIAS_LIBELLE_CATEGORIE_SECTION).append(",");
        sql.append(CASectionJoinCompteAnnexeJoinTiersManager.ALIAS_LIBELLE_ROLE_COMPTE_ANNEXE).append(".")
                .append("PCOLUT").append(" AS ")
                .append(CASectionJoinCompteAnnexeJoinTiersManager.ALIAS_LIBELLE_ROLE_COMPTE_ANNEXE);

        return sql.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        String codeIsoLangue;
        try {
            codeIsoLangue = getISession().getIdLangueISO().toUpperCase();
        } catch (Exception ex) {
            codeIsoLangue = "FR";
        }

        String codeLangue;
        try {
            codeLangue = getISession().getIdLangue();
        } catch (RemoteException e) {
            codeLangue = "F";
        }

        sql.append(_getCollection()).append(CASection.TABLE_CASECTP);

        sql.append(" INNER JOIN ");
        sql.append(_getCollection()).append(CACompteAnnexe.TABLE_CACPTAP);
        sql.append(" ON (");
        sql.append(_getCollection()).append(CACompteAnnexe.TABLE_CACPTAP).append(".")
                .append(CACompteAnnexe.FIELD_IDCOMPTEANNEXE);
        sql.append("=");
        sql.append(_getCollection()).append(CASection.TABLE_CASECTP).append(".").append(CASection.FIELD_IDCOMPTEANNEXE);
        sql.append(")");

        sql.append(" INNER JOIN ");
        sql.append(_getCollection()).append("TITIERP");
        sql.append(" ON (");
        sql.append(_getCollection()).append("TITIERP").append(".").append("HTITIE");
        sql.append("=");
        sql.append(_getCollection()).append(CACompteAnnexe.TABLE_CACPTAP).append(".")
                .append(CACompteAnnexe.FIELD_IDTIERS);
        sql.append(")");

        sql.append(" INNER JOIN ");
        sql.append(_getCollection()).append("CATSECP");
        sql.append(" ON (");
        sql.append(_getCollection()).append("CATSECP").append(".").append("IDTYPESECTION");
        sql.append("=");
        sql.append(_getCollection()).append("CASECTP").append(".").append("IDTYPESECTION");
        sql.append(")");

        sql.append(" INNER JOIN ");
        sql.append(_getCollection()).append("PMTRADP");
        sql.append(" ON (");
        sql.append(_getCollection()).append("PMTRADP").append(".").append("IDTRADUCTION");
        sql.append("=");
        sql.append(_getCollection()).append("CATSECP").append(".").append("IDTRADUCTION");
        sql.append(" AND ");
        sql.append(_getCollection()).append("PMTRADP").append(".").append("CODEISOLANGUE");
        sql.append("=");
        sql.append("'").append(codeIsoLangue).append("'");
        sql.append(")");

        sql.append(" INNER JOIN ");
        sql.append(_getCollection()).append("FWCOUP").append(" AS ")
                .append(CASectionJoinCompteAnnexeJoinTiersManager.ALIAS_LIBELLE_ROLE_COMPTE_ANNEXE);
        sql.append(" ON (");
        sql.append(CASectionJoinCompteAnnexeJoinTiersManager.ALIAS_LIBELLE_ROLE_COMPTE_ANNEXE).append(".")
                .append("PCOSID");
        sql.append("=");
        sql.append(_getCollection()).append("CACPTAP").append(".").append("IDROLE");
        sql.append(" AND ");
        sql.append(CASectionJoinCompteAnnexeJoinTiersManager.ALIAS_LIBELLE_ROLE_COMPTE_ANNEXE).append(".")
                .append("PLAIDE");
        sql.append("=");
        sql.append("'").append(codeLangue).append("'");
        sql.append(")");

        sql.append(" INNER JOIN ");
        sql.append(_getCollection()).append("FWCOUP").append(" AS ")
                .append(CASectionJoinCompteAnnexeJoinTiersManager.ALIAS_LIBELLE_CATEGORIE_SECTION);
        sql.append(" ON (");
        sql.append(CASectionJoinCompteAnnexeJoinTiersManager.ALIAS_LIBELLE_CATEGORIE_SECTION).append(".")
                .append("PCOSID");
        sql.append("=");
        sql.append(_getCollection()).append(CASection.TABLE_CASECTP).append(".")
                .append(CASection.FIELD_CATEGORIESECTION);
        sql.append(" AND ");
        sql.append(CASectionJoinCompteAnnexeJoinTiersManager.ALIAS_LIBELLE_CATEGORIE_SECTION).append(".")
                .append("PLAIDE");
        sql.append("=");
        sql.append("'").append(codeLangue).append("'");
        sql.append(")");

        return sql.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {

        StringBuilder sql = new StringBuilder();

        String superWhere = super._getWhere(statement);

        if (!JadeStringUtil.isBlank(superWhere)) {
            sql.append(superWhere);
        }

        if (!JadeStringUtil.isBlank(getForIdCompteAnnexe())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(_getCollection()).append(CASection.TABLE_CASECTP).append(".")
                    .append(CASection.FIELD_IDCOMPTEANNEXE);
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), getForIdCompteAnnexe()));
        }

        if (!JadeStringUtil.isBlank(getForIdTiers())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(_getCollection()).append("TITIERP").append(".").append("HTITIE");
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), getForIdTiers()));
        }

        if ((getForIdTiersIn() != null) && (getForIdTiersIn().size() > 0)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(_getCollection()).append("TITIERP").append(".").append("HTITIE");
            sql.append(" IN(");

            boolean isFirstId = true;
            for (String unIdTiers : getForIdTiersIn()) {
                if (!JadeNumericUtil.isInteger(unIdTiers)) {
                    continue;
                }
                if (isFirstId) {
                    isFirstId = false;
                } else {
                    sql.append(",");
                }
                sql.append(this._dbWriteNumeric(statement.getTransaction(), unIdTiers));
            }

            sql.append(")");
        }

        if (getForSoldeDifferentZero().booleanValue()) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(_getCollection()).append(CASection.TABLE_CASECTP).append(".").append(CASection.FIELD_SOLDE);
            sql.append("<>");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), "0"));
        }

        if (getForSoldeNegatif().booleanValue()) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            // alias dans le parent pour la table des sections
            sql.append(_getCollection()).append(CASection.TABLE_CASECTP).append(".").append(CASection.FIELD_SOLDE);
            sql.append("<");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), "0"));
        }

        if (getForSoldePositif().booleanValue()) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(_getCollection()).append(CASection.TABLE_CASECTP).append(".").append(CASection.FIELD_SOLDE);
            sql.append(">");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), "0"));
        }

        if (!JadeStringUtil.isBlank(getLikeIdExterne())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(_getCollection()).append(CASection.TABLE_CASECTP).append(".").append(CASection.FIELD_IDEXTERNE);
            sql.append(" LIKE ");
            sql.append(this._dbWriteString(statement.getTransaction(), getLikeIdExterne() + "%"));
        }

        if (!JadeStringUtil.isBlank(forCategorie)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(_getCollection()).append(CASection.TABLE_CASECTP).append(".")
                    .append(CASection.FIELD_CATEGORIESECTION);
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), forCategorie));
        }

        if (!JadeStringUtil.isBlank(forTypeSection)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(_getCollection()).append(CASection.TABLE_CASECTP).append(".")
                    .append(CASection.FIELD_IDTYPESECTION);
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), forTypeSection));
        }

        if (!forIdSectionIn.isEmpty()) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(_getCollection()).append(CASection.TABLE_CASECTP).append(".").append(CASection.FIELD_IDSECTION);
            sql.append(" in (");
            sql.append(Joiner.on(",").join(forIdSectionIn)).append(")");
        }

        return sql.toString();
    }

    @Override
    protected CASectionJoinCompteAnnexeJoinTiers _newEntity() throws Exception {
        return new CASectionJoinCompteAnnexeJoinTiers();
    }

    public String getForIdCompteAnnexe() {
        return forIdCompteAnnexe;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public Collection<String> getForIdTiersIn() {
        return forIdTiersIn;
    }

    public Boolean getForSoldeDifferentZero() {
        return forSoldeDifferentZero;
    }

    public Boolean getForSoldeNegatif() {
        return forSoldeNegatif;
    }

    public Boolean getForSoldePositif() {
        return forSoldePositif;
    }

    public String getLikeIdExterne() {
        return likeIdExterne;
    }

    public void setForIdCompteAnnexe(String forIdCompteAnnexe) {
        this.forIdCompteAnnexe = forIdCompteAnnexe;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForIdTiersIn(Collection<String> forIdTiersIn) {
        this.forIdTiersIn = forIdTiersIn;
    }

    /**
     * Les ID Tiers sont passés dans une chaîne de caractères, séparés par le caractère '|'
     * 
     * @param forIdTiers
     * @see {@link CACompteAnnexeManager#setForIdTiersIn(String)}
     */
    public void setForIdTiersIn(String forIdTiers) {
        if (!JadeStringUtil.isBlank(forIdTiers)) {
            String[] idSplit = forIdTiers.split("\\|");

            Collection<String> parsedIds = new ArrayList<String>();
            for (String unIdTiers : idSplit) {
                if (JadeNumericUtil.isInteger(unIdTiers)) {
                    parsedIds.add(unIdTiers);
                }
            }
            this.setForIdTiersIn(parsedIds);
        }
    }

    public void setForSoldeDifferentZero(Boolean forSoldeDifferentZero) {
        this.forSoldeDifferentZero = forSoldeDifferentZero;
        if (forSoldeDifferentZero.booleanValue()) {
            forSoldeNegatif = false;
            forSoldePositif = false;
        }
    }

    public void setForSoldeNegatif(Boolean forSoldeNegatif) {
        this.forSoldeNegatif = forSoldeNegatif;
        if (forSoldeNegatif.booleanValue()) {
            forSoldeDifferentZero = false;
        }
    }

    /**
     * Pour une utilisation dans un <ct:widgetManager>
     * 
     * @param forSoldeNegatif
     */
    public void setForSoldeNegatif(String forSoldeNegatif) {
        this.setForSoldeNegatif(Boolean.valueOf(forSoldeNegatif));
    }

    public void setForSoldePositif(Boolean forSoldePositif) {
        this.forSoldePositif = forSoldePositif;
        if (forSoldePositif.booleanValue()) {
            forSoldeDifferentZero = false;
        }
    }

    /**
     * Pour une utilisation dans un <ct:widgetManager>
     * 
     * @param forSoldePositif
     */
    public void setForSoldePositif(String forSoldePositif) {
        this.setForSoldePositif(Boolean.valueOf(forSoldePositif));
    }

    public void setLikeIdExterne(String likeIdExterne) {
        this.likeIdExterne = likeIdExterne;
    }

    public String getForTypeSection() {
        return forTypeSection;
    }

    public void setForTypeSection(String forTypeSection) {
        this.forTypeSection = forTypeSection;
    }

    public String getForCategorie() {
        return forCategorie;
    }

    public void setForCategorie(String forCategorie) {
        this.forCategorie = forCategorie;
    }

    public Collection<String> getForIdSectionIn() {
        return forIdSectionIn;
    }

    public void setForIdSectionIn(Collection<String> forIdSectionIn) {
        this.forIdSectionIn = forIdSectionIn;
    }

    public void setForIdSection(String forIdSection) {
        List<String> list = new ArrayList<String>();
        list.add(forIdSection);
        forIdSectionIn = list;
    }

}