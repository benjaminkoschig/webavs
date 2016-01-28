package globaz.aquila.db.process;

import globaz.aquila.api.ICOContentieuxConstante;
import globaz.aquila.common.COBManager;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.contentieux.CAMotifContentieux;
import java.util.Collection;
import java.util.Iterator;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class COContentieuxInfoManager extends COBManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final long serialVersionUID = 5204120853883995001L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String beforeNoAffilie;
    private boolean forBulletinNeutre = false;
    private String forContentieuxNonBloqueADAte;
    private String forDelaiEchuADate;
    private String forIdCategorie;
    private String forIdGenreCompte;
    private String forIdLastEtatAquila;
    private Collection<String> forIdsRoles;
    private Collection<String> forIdsSequencesAquila;
    private Boolean forNotInContentieux = Boolean.TRUE;
    private String forNotModeCompensation;
    private Boolean forSoldePositif = Boolean.TRUE;
    private String forSoldeSection = "";

    private String forTriListeCA = "";
    private String forTriListeSection = "";

    private Collection<String> forTypesSections;

    private String fromNoAffilie;

    /**
     * Crée une nouvelle instance de la classe COProcessContentieuxInfoManager.
     */
    public COContentieuxInfoManager() {
    }

    /**
     * @param statement
     * @return String fields
     */
    @Override
    protected String _getFields(BStatement statement) {
        StringBuffer fields = new StringBuffer();

        fields.append(_getCollection());
        fields.append(CASection.TABLE_CASECTP);
        fields.append(".");
        fields.append(CASection.FIELD_IDSECTION);

        fields.append(",");
        fields.append(_getCollection());
        fields.append(CASection.TABLE_CASECTP);
        fields.append(".");
        fields.append(CASection.FIELD_IDCOMPTEANNEXE);

        fields.append(",");
        fields.append(_getCollection());
        fields.append(CASection.TABLE_CASECTP);
        fields.append(".");
        fields.append(CASection.FIELD_IDTYPESECTION);

        fields.append(",");
        fields.append(_getCollection());
        fields.append(CASection.TABLE_CASECTP);
        fields.append(".");
        fields.append(CASection.FIELD_IDSEQCON);

        fields.append(",");
        fields.append(_getCollection());
        fields.append(CASection.TABLE_CASECTP);
        fields.append(".");
        fields.append(CASection.FIELD_SOLDE);

        fields.append(",");
        fields.append(_getCollection());
        fields.append(CASection.TABLE_CASECTP);
        fields.append(".");
        fields.append(CASection.FIELD_DATEECHEANCE);

        fields.append(",");
        fields.append(_getCollection());
        fields.append(CASection.TABLE_CASECTP);
        fields.append(".");
        fields.append(CASection.FIELD_DATESECTION);

        fields.append(",");
        fields.append(_getCollection());
        fields.append(CASection.TABLE_CASECTP);
        fields.append(".");
        fields.append(CASection.FIELD_IDEXTERNE);

        fields.append(",");
        fields.append(_getCollection());
        fields.append(CASection.TABLE_CASECTP);
        fields.append(".");
        fields.append(CASection.FIELD_CATEGORIESECTION);

        fields.append(",");
        fields.append(_getCollection());
        fields.append(CASection.TABLE_CASECTP);
        fields.append(".");
        fields.append(CASection.FIELD_DATEDEBUTPERIODE);

        fields.append(",");
        fields.append(_getCollection());
        fields.append(CASection.TABLE_CASECTP);
        fields.append(".");
        fields.append(CASection.FIELD_DATEFINPERIODE);

        fields.append(",");
        fields.append(_getCollection());
        fields.append(CACompteAnnexe.TABLE_CACPTAP);
        fields.append(".");
        fields.append(CACompteAnnexe.FIELD_IDEXTERNEROLE);

        fields.append(",");
        fields.append(_getCollection());
        fields.append(CACompteAnnexe.TABLE_CACPTAP);
        fields.append(".");
        fields.append(CACompteAnnexe.FIELD_DESCRIPTION);

        fields.append(",");
        fields.append(_getCollection());
        fields.append(CACompteAnnexe.TABLE_CACPTAP);
        fields.append(".");
        fields.append(CACompteAnnexe.FIELD_IDTIERS);

        fields.append(",");
        fields.append(_getCollection());
        fields.append(CACompteAnnexe.TABLE_CACPTAP);
        fields.append(".");
        fields.append(CACompteAnnexe.FIELD_IDROLE);

        return fields.toString();
    }

    /**
     * @param statement
     * @return String clause from
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer clause = new StringBuffer();

        clause.append(_getCollection());
        clause.append(CASection.TABLE_CASECTP);

        // jointure avec la table des types de sections
        clause.append(COBManager.INNER_JOIN);
        clause.append(_getCollection());
        clause.append("CATSECP ON ");
        clause.append(_getCollection());
        clause.append(CASection.TABLE_CASECTP);
        clause.append(".");
        clause.append(CASection.FIELD_IDTYPESECTION);
        clause.append("=");
        clause.append(_getCollection());
        clause.append("CATSECP.IDTYPESECTION");

        // jointure avec la table des comptes annexes
        clause.append(COBManager.INNER_JOIN);
        clause.append(_getCollection());
        clause.append(CACompteAnnexe.TABLE_CACPTAP);
        clause.append(COBManager.ON);
        clause.append(_getCollection());
        clause.append(CASection.TABLE_CASECTP);
        clause.append(".");
        clause.append(CASection.FIELD_IDCOMPTEANNEXE);
        clause.append("=");
        clause.append(_getCollection());
        clause.append(CACompteAnnexe.TABLE_CACPTAP);
        clause.append(".");
        clause.append(CACompteAnnexe.FIELD_IDCOMPTEANNEXE);

        return clause.toString();
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {

        StringBuffer _order = new StringBuffer("");

        // Traitement du tri du compte annexe de la liste et des documents
        if (getForTriListeCA().length() != 0) {
            switch (Integer.parseInt(getForTriListeCA())) {
                case 1:
                    _order.append(CACompteAnnexe.FIELD_DESCRIPTION);
                    _order.append(", ");
                    _order.append(CACompteAnnexe.FIELD_IDCOMPTEANNEXE);
                    break;
                case 2:
                    _order.append(CACompteAnnexe.FIELD_IDROLE);
                    _order.append(", ");
                    _order.append(CACompteAnnexe.FIELD_IDEXTERNEROLE);
                    break;
                default:
                    break;
            }
        }

        // Traitement du tri de la section de la liste et des documents
        if (getForTriListeSection().length() != 0) {
            _order.append(", ");
            switch (Integer.parseInt(getForTriListeSection())) {
                case 1:
                    _order.append(CASection.FIELD_IDEXTERNE);
                    break;
                case 2:
                    _order.append(CASection.FIELD_DATESECTION);
                    break;
                default:
                    break;
            }
        }

        return _order.toString();
    }

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param statement
     * @return String clause where
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer clause = new StringBuffer();
        BTransaction transaction = statement.getTransaction();

        if (clause.length() > 0) {
            clause.append(COBManager.AND);
        }
        clause.append(_getCollection() + "CASECTP." + CASection.FIELD_CATEGORIESECTION + " <> "
                + APISection.ID_CATEGORIE_SECTION_DECOMPTE_PCG);

        if (!JadeStringUtil.isEmpty(forContentieuxNonBloqueADAte)) {
            if (clause.length() > 0) {
                clause.append(COBManager.AND);
            }

            // boolean a true
            clause.append("((");
            clause.append(_getCollection());
            clause.append(CACompteAnnexe.TABLE_CACPTAP);
            clause.append(".");
            clause.append(CACompteAnnexe.FIELD_CONTESTBLOQUE);
            clause.append("=");
            clause.append(this._dbWriteBoolean(transaction, Boolean.FALSE, BConstants.DB_TYPE_BOOLEAN_CHAR));
            clause.append(COBManager.AND);
            clause.append(_getCollection());
            clause.append(CASection.TABLE_CASECTP);
            clause.append(".");
            clause.append(CASection.FIELD_CONTENTIEUXESTSUS);
            clause.append("=");
            clause.append(this._dbWriteBoolean(transaction, Boolean.FALSE, BConstants.DB_TYPE_BOOLEAN_CHAR));

            // ou section ou compte annexe bloque pour des dates differentes de
            // date transmise
            clause.append(") OR (SELECT COUNT(*) FROM ");
            clause.append(_getCollection());
            clause.append(CAMotifContentieux.TABLE_CAMOCOP);
            clause.append(COBManager.WHERE + "(");

            clause.append(_getCollection());
            clause.append(CACompteAnnexe.TABLE_CACPTAP);
            clause.append(".");
            clause.append(CACompteAnnexe.FIELD_IDCOMPTEANNEXE);
            clause.append("=");
            clause.append(_getCollection());
            clause.append(CAMotifContentieux.TABLE_CAMOCOP);
            clause.append(".");
            clause.append(CAMotifContentieux.FIELD_IDCOMPTEANNEXE);
            clause.append(COBManager.OR);
            clause.append(_getCollection());
            clause.append(CASection.TABLE_CASECTP);
            clause.append(".");
            clause.append(CASection.FIELD_IDSECTION);
            clause.append("=");
            clause.append(_getCollection());
            clause.append(CAMotifContentieux.TABLE_CAMOCOP);
            clause.append(".");
            clause.append(CAMotifContentieux.FIELD_IDSECTION);

            clause.append(") AND ");
            clause.append(_getCollection());
            clause.append(CAMotifContentieux.TABLE_CAMOCOP);
            clause.append(".");
            clause.append(CAMotifContentieux.FIELD_DATEDEBUT);
            clause.append("<=");
            clause.append(this._dbWriteDateAMJ(transaction, forContentieuxNonBloqueADAte));
            clause.append(COBManager.AND);
            clause.append(_getCollection());
            clause.append(CAMotifContentieux.TABLE_CAMOCOP);
            clause.append(".");
            clause.append(CAMotifContentieux.FIELD_DATEFIN);
            clause.append(">=");
            clause.append(this._dbWriteDateAMJ(transaction, forContentieuxNonBloqueADAte));
            clause.append(")=0)");
        }

        if (forIdGenreCompte != null) {
            if (clause.length() > 0) {
                clause.append(COBManager.AND);
            }

            clause.append(_getCollection());
            clause.append(CACompteAnnexe.TABLE_CACPTAP);
            clause.append(".");
            clause.append(CACompteAnnexe.FIELD_IDGENRECOMPTE);
            clause.append("=");
            clause.append(this._dbWriteNumeric(transaction, forIdGenreCompte));
        }

        if (forIdsRoles != null) {
            if (clause.length() > 0) {
                clause.append(" AND ");
            }

            clause.append(_getCollection());
            clause.append(CACompteAnnexe.TABLE_CACPTAP);
            clause.append(".");
            clause.append(CACompteAnnexe.FIELD_IDROLE);
            clause.append(" IN (");

            for (Iterator<String> idRoleIter = forIdsRoles.iterator(); idRoleIter.hasNext();) {
                clause.append(this._dbWriteNumeric(transaction, idRoleIter.next()));

                if (idRoleIter.hasNext()) {
                    clause.append(",");
                }
            }

            clause.append(")");
        }

        if (forIdsSequencesAquila != null) {
            if (clause.length() > 0) {
                clause.append(" AND ");
            }

            clause.append(_getCollection());
            clause.append(CASection.TABLE_CASECTP);
            clause.append(".");
            clause.append(CASection.FIELD_IDSEQCON);
            clause.append(" IN (");

            for (Iterator<String> idSeqIter = forIdsSequencesAquila.iterator(); idSeqIter.hasNext();) {
                clause.append(this._dbWriteNumeric(transaction, idSeqIter.next()));

                if (idSeqIter.hasNext()) {
                    clause.append(",");
                }
            }

            clause.append(")");
        }

        if (forSoldePositif != null) {
            if (clause.length() > 0) {
                clause.append(" AND ");
            }

            clause.append(_getCollection());
            clause.append(CASection.TABLE_CASECTP);
            clause.append(".");
            clause.append(CASection.FIELD_SOLDE);
            clause.append(forSoldePositif.booleanValue() ? ">" : "<");
            clause.append(this._dbWriteNumeric(transaction, "0.00"));
        }

        if (forIdCategorie != null) {
            if (clause.length() > 0) {
                clause.append(" AND ");
            }

            clause.append(_getCollection());
            clause.append(CACompteAnnexe.TABLE_CACPTAP);
            clause.append(".");
            clause.append(CACompteAnnexe.FIELD_IDCATEGORIE);
            clause.append("=");
            clause.append(this._dbWriteNumeric(transaction, forIdCategorie));
        }

        if (forTypesSections != null) {
            if (clause.length() > 0) {
                clause.append(" AND ");
            }

            clause.append(_getCollection());
            clause.append(CASection.TABLE_CASECTP);
            clause.append(".");
            clause.append(CASection.FIELD_IDTYPESECTION);
            clause.append(" IN (");

            for (Iterator<String> typeSectionIter = forTypesSections.iterator(); typeSectionIter.hasNext();) {
                clause.append(this._dbWriteNumeric(transaction, typeSectionIter.next()));

                if (typeSectionIter.hasNext()) {
                    clause.append(",");
                }
            }

            clause.append(")");
        }

        if (forDelaiEchuADate != null) {
            if (clause.length() > 0) {
                clause.append(" AND ");
            }

            clause.append(_getCollection());
            clause.append(CASection.TABLE_CASECTP);
            clause.append(".");
            clause.append(CASection.FIELD_DATEECHEANCE);
            clause.append("<=");
            clause.append(this._dbWriteDateAMJ(transaction, forDelaiEchuADate));
        }

        if (forNotInContentieux != null) {
            if (clause.length() > 0) {
                clause.append(" AND ");
            }

            // pas de contentieux
            clause.append("(SELECT COUNT(");
            clause.append(ICOContentieuxConstante.FNAME_ID_CONTENTIEUX);
            clause.append(") FROM ");
            clause.append(_getCollection());
            clause.append(ICOContentieuxConstante.TABLE_NAME_AVS);
            clause.append(" WHERE ");
            clause.append(ICOContentieuxConstante.FNAME_ID_SECTION);
            clause.append("=");
            clause.append(_getCollection());
            clause.append(CASection.TABLE_CASECTP);
            clause.append(".");
            clause.append(CASection.FIELD_IDSECTION);
            clause.append(")=0");
        }

        if (!JadeStringUtil.isEmpty(fromNoAffilie)) {
            if (clause.length() > 0) {
                clause.append(" AND ");
            }

            clause.append(CACompteAnnexe.FIELD_IDEXTERNEROLE);
            clause.append(">=");
            clause.append(this._dbWriteString(statement.getTransaction(), fromNoAffilie));
        }

        if (!JadeStringUtil.isEmpty(beforeNoAffilie)) {
            if (clause.length() > 0) {
                clause.append(" AND ");
            }

            clause.append(CACompteAnnexe.FIELD_IDEXTERNEROLE);
            clause.append("<=");
            clause.append(this._dbWriteString(statement.getTransaction(), beforeNoAffilie));
        }

        if (!JadeStringUtil.isEmpty(getForNotModeCompensation())) {
            if (clause.length() > 0) {
                clause.append(" AND ");
            }

            clause.append(CASection.FIELD_IDMODECOMPENSATION);
            clause.append("<>");
            clause.append(this._dbWriteNumeric(statement.getTransaction(), getForNotModeCompensation()));
        }

        if (!JadeStringUtil.isEmpty(getForIdLastEtatAquila())) {
            if (clause.length() > 0) {
                clause.append(" AND ");
            }
            clause.append(CASection.FIELD_IDLASTETATAQUILA);
            clause.append("=");
            clause.append(this._dbWriteNumeric(statement.getTransaction(), getForIdLastEtatAquila()));
        }

        if (isForBulletinNeutre()) {
            addCondition(clause, CASection.FIELD_PMTCMP + COBManager.EGAL + "0");
            addCondition(clause, CASection.FIELD_STATUTBN + COBManager.DIFFERENT + APISection.STATUTBN_ANNULE);
            addCondition(clause, CASection.FIELD_STATUTBN + COBManager.DIFFERENT + APISection.STATUTBN_DECOMPTE_FINAL);
        }

        if (!JadeStringUtil.isBlank(getForSoldeSection())) {
            addCondition(
                    clause,
                    _getCollection() + CASection.TABLE_CASECTP + "." + CASection.FIELD_SOLDE + "="
                            + this._dbWriteNumeric(statement.getTransaction(), getForSoldeSection()));
        }

        return clause.toString();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new COProcessContentieuxInfo();
    }

    /**
     * getter pour l'attribut to no affilie.
     * 
     * @return la valeur courante de l'attribut to no affilie
     */
    public String getBeforeNoAffilie() {
        return beforeNoAffilie;
    }

    /**
     * getter pour l'attribut for contentieux non bloque ADAte.
     * 
     * @return la valeur courante de l'attribut for contentieux non bloque ADAte
     */
    public String getForContentieuxNonBloqueADAte() {
        return forContentieuxNonBloqueADAte;
    }

    /**
     * getter pour l'attribut for delai echu ADate.
     * 
     * @return la valeur courante de l'attribut for delai echu ADate
     */
    public String getForDelaiEchuADate() {
        return forDelaiEchuADate;
    }

    /**
     * getter pour l'attribut for id categorie.
     * 
     * @return la valeur courante de l'attribut for id categorie
     */
    public String getForIdCategorie() {
        return forIdCategorie;
    }

    /**
     * getter pour l'attribut for id genre compte.
     * 
     * @return la valeur courante de l'attribut for id genre compte
     */
    public String getForIdGenreCompte() {
        return forIdGenreCompte;
    }

    /**
     * @return the forIdLastEtatAquila
     */
    public String getForIdLastEtatAquila() {
        return forIdLastEtatAquila;
    }

    /**
     * getter pour l'attribut for ids roles.
     * 
     * @return la valeur courante de l'attribut for ids roles
     */
    public Collection<String> getForIdsRoles() {
        return forIdsRoles;
    }

    /**
     * getter pour l'attribut for ids sequences aquila.
     * 
     * @return la valeur courante de l'attribut for ids sequences aquila
     */
    public Collection<String> getForIdsSequencesAquila() {
        return forIdsSequencesAquila;
    }

    /**
     * getter pour l'attribut for not in contentieux.
     * 
     * @return la valeur courante de l'attribut for not in contentieux
     */
    public Boolean getForNotInContentieux() {
        return forNotInContentieux;
    }

    /**
     * @return the forNotModeCompensation
     */
    public String getForNotModeCompensation() {
        return forNotModeCompensation;
    }

    /**
     * getter pour l'attribut for solde positif.
     * 
     * @return la valeur courante de l'attribut for solde positif
     */
    public Boolean getForSoldePositif() {
        return forSoldePositif;
    }

    /**
     * @return the forSoldeSection
     */
    public String getForSoldeSection() {
        return forSoldeSection;
    }

    /**
     * @return the forTriListeCA
     */
    public String getForTriListeCA() {
        return forTriListeCA;
    }

    /**
     * @return the forTriListeSection
     */
    public String getForTriListeSection() {
        return forTriListeSection;
    }

    /**
     * getter pour l'attribut for types sections.
     * 
     * @return la valeur courante de l'attribut for types sections
     */
    public Collection<String> getForTypesSections() {
        return forTypesSections;
    }

    /**
     * getter pour l'attribut from no affilie.
     * 
     * @return la valeur courante de l'attribut from no affilie
     */
    public String getFromNoAffilie() {
        return fromNoAffilie;
    }

    /**
     * @return the forSectionPmtCmpIsZero
     */
    public boolean isForBulletinNeutre() {
        return forBulletinNeutre;
    }

    /**
     * setter pour l'attribut to no affilie.
     * 
     * @param toNoAffilie
     *            une nouvelle valeur pour cet attribut
     */
    public void setBeforeNoAffilie(String toNoAffilie) {
        beforeNoAffilie = toNoAffilie;
    }

    /**
     * @param forSectionPmtCmpIsZero
     *            the forSectionPmtCmpIsZero to set
     */
    public void setForBulletinNeutre(boolean forSectionPmtCmpIsZero) {
        forBulletinNeutre = forSectionPmtCmpIsZero;
    }

    /**
     * setter pour l'attribut for contentieux non bloque ADAte.
     * 
     * @param forContentieuxNonBloqueADAte
     *            une nouvelle valeur pour cet attribut
     */
    public void setForContentieuxNonBloqueADAte(String forContentieuxNonBloqueADAte) {
        this.forContentieuxNonBloqueADAte = forContentieuxNonBloqueADAte;
    }

    /**
     * setter pour l'attribut for delai echu ADate.
     * 
     * @param forDelaiEchuADate
     *            une nouvelle valeur pour cet attribut
     */
    public void setForDelaiEchuADate(String forDelaiEchuADate) {
        this.forDelaiEchuADate = forDelaiEchuADate;
    }

    /**
     * setter pour l'attribut for id categorie.
     * 
     * @param forIdCategorie
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdCategorie(String forIdCategorie) {
        this.forIdCategorie = forIdCategorie;
    }

    /**
     * setter pour l'attribut for id genre compte.
     * 
     * @param forIdGenreCompte
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdGenreCompte(String forIdGenreCompte) {
        this.forIdGenreCompte = forIdGenreCompte;
    }

    /**
     * @param forIdLastEtatAquila
     *            the forIdLastEtatAquila to set
     */
    public void setForIdLastEtatAquila(String forIdLastEtatAquila) {
        this.forIdLastEtatAquila = forIdLastEtatAquila;
    }

    /**
     * setter pour l'attribut for ids roles.
     * 
     * @param forIdsRoles
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdsRoles(Collection<String> forIdsRoles) {
        this.forIdsRoles = forIdsRoles;
    }

    /**
     * setter pour l'attribut for ids sequences aquila.
     * 
     * @param forIdsSequencesAquila
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdsSequencesAquila(Collection<String> forIdsSequencesAquila) {
        this.forIdsSequencesAquila = forIdsSequencesAquila;
    }

    /**
     * setter pour l'attribut for not in contentieux.
     * 
     * @param forNotInContentieux
     *            une nouvelle valeur pour cet attribut
     */
    public void setForNotInContentieux(Boolean forNotInContentieux) {
        this.forNotInContentieux = forNotInContentieux;
    }

    /**
     * @param forNotModeCompensation
     *            the forNotModeCompensation to set
     */
    public void setForNotModeCompensation(String forNotModeCompensation) {
        this.forNotModeCompensation = forNotModeCompensation;
    }

    /**
     * setter pour l'attribut for solde positif.
     * 
     * @param forSoldePositif
     *            une nouvelle valeur pour cet attribut
     */
    public void setForSoldePositif(Boolean forSoldePositif) {
        this.forSoldePositif = forSoldePositif;
    }

    /**
     * @param forSoldeSection
     *            the forSoldeSection to set
     */
    public void setForSoldeSection(String forSoldeSection) {
        this.forSoldeSection = forSoldeSection;
    }

    /**
     * @param forTriListeCA
     *            the forTriListeCA to set
     */
    public void setForTriListeCA(String forTriListeCA) {
        this.forTriListeCA = forTriListeCA;
    }

    /**
     * @param forTriListeSection
     *            the forTriListeSection to set
     */
    public void setForTriListeSection(String forTriListeSection) {
        this.forTriListeSection = forTriListeSection;
    }

    /**
     * setter pour l'attribut for types sections.
     * 
     * @param forTypesSections
     *            une nouvelle valeur pour cet attribut
     */
    public void setForTypesSections(Collection<String> forTypesSections) {
        this.forTypesSections = forTypesSections;
    }

    /**
     * setter pour l'attribut from no affilie.
     * 
     * @param fromNoAffilie
     *            une nouvelle valeur pour cet attribut
     */
    public void setFromNoAffilie(String fromNoAffilie) {
        this.fromNoAffilie = fromNoAffilie;
    }
}
