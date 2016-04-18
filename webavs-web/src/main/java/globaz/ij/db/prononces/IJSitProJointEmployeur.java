/*
 * Créé le 15 sept. 05
 */
package globaz.ij.db.prononces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.db.employeurs.PRAbstractEmployeur;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

/**
 * <H1>Description</H1>
 *
 * @author dvh
 */
public class IJSitProJointEmployeur extends BEntity {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param schema
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        // jointure entre table des situation prof et employeur
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJSituationProfessionnelle.TABLE_NAME);
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJEmployeur.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJSituationProfessionnelle.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJSituationProfessionnelle.FIELDNAME_ID_EMPLOYEUR);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJEmployeur.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJEmployeur.FIELDNAME_ID_EMPLOYEUR);

        return fromClauseBuffer.toString();
    }

    private String fromClause = null;
    private String idAffilie = "";
    private String idSituationProfessionnelle = "";
    private String idTiers = "";
    private String noAffilie = "";
    private String nom = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private Boolean versementEmployeur = Boolean.FALSE;

    /**
     * (non-Javadoc).
     *
     * @param transaction
     *            DOCUMENT ME!
     *
     * @throws Exception
     *             DOCUMENT ME!
     *
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        super._afterRetrieve(transaction);

        if (!JadeStringUtil.isIntegerEmpty(idAffilie)) {
            noAffilie = PRAffiliationHelper.getEmployeurParIdAffilie(getSession(), transaction, idAffilie, idTiers)
                    .getNumAffilie();
        }
    }

    /**
     * (non-Javadoc).
     *
     * @return DOCUMENT ME!
     *
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * (non-Javadoc).
     *
     * @return DOCUMENT ME!
     *
     * @see globaz.globall.db.BEntity#_allowDelete()
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * (non-Javadoc).
     *
     * @return DOCUMENT ME!
     *
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * (non-Javadoc).
     *
     * @param statement
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * (non-Javadoc).
     *
     * @return DOCUMENT ME!
     *
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return IJSituationProfessionnelle.TABLE_NAME;
    }

    /**
     * (non-Javadoc).
     *
     * @param statement
     *            DOCUMENT ME!
     *
     * @throws Exception
     *             DOCUMENT ME!
     *
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idSituationProfessionnelle = statement
                .dbReadNumeric(IJSituationProfessionnelle.FIELDNAME_ID_SITUATION_PROFESSIONNELLE);
        idTiers = statement.dbReadNumeric(IJEmployeur.FIELDNAME_ID_TIERS);
        idAffilie = statement.dbReadNumeric(IJEmployeur.FIELDNAME_ID_AFFILIE);
        versementEmployeur = statement.dbReadBoolean(IJSituationProfessionnelle.FIELDNAME_VERSEMENT_EMPLOYEUR);
    }

    /**
     * (non-Javadoc).
     *
     * @param statement
     *            DOCUMENT ME!
     *
     * @throws Exception
     *             DOCUMENT ME!
     *
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // lecture seule
    }

    /**
     * (non-Javadoc).
     *
     * @param statement
     *            DOCUMENT ME!
     *
     * @throws Exception
     *             DOCUMENT ME!
     *
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(IJSituationProfessionnelle.FIELDNAME_ID_SITUATION_PROFESSIONNELLE,
                _dbWriteNumeric(statement.getTransaction(), idSituationProfessionnelle, "idSituationProfessionnelle"));
    }

    /**
     * (non-Javadoc).
     *
     * @param statement
     *            DOCUMENT ME!
     *
     * @throws Exception
     *             DOCUMENT ME!
     *
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // lecture seule
    }

    /**
     * getter pour l'attribut id affilie.
     *
     * @return la valeur courante de l'attribut id affilie
     */
    public String getIdAffilie() {
        return idAffilie;
    }

    /**
     * getter pour l'attribut id situation professionnelle.
     *
     * @return la valeur courante de l'attribut id situation professionnelle
     */
    public String getIdSituationProfessionnelle() {
        return idSituationProfessionnelle;
    }

    /**
     * getter pour l'attribut id tiers.
     *
     * @return la valeur courante de l'attribut id tiers
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * getter pour l'attribut no affilie.
     *
     * @return la valeur courante de l'attribut no affilie
     */
    public String getNoAffilie() {
        return noAffilie;
    }

    /**
     * getter pour l'attribut nom.
     *
     * @return la valeur courante de l'attribut nom
     */
    public String getNom() {
        if (nom == null) {
            String result = null;
            try {
                result = PRAbstractEmployeur.loadNomEmployeur(idTiers, idAffilie, getSession());

            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
            nom = result;
            if (JadeStringUtil.isEmpty(nom)) {
                nom = getSession().getLabel("NOM_INTROUVABLE");
            }
        }
        return nom;
    }

    /**
     * charge cet employeur en tant qu'administration.
     *
     * @return une instance PRTiersWrapper ou null si idTiers est null ou 0.
     *
     * @throws Exception
     */
    public PRTiersWrapper loadAdministration() throws Exception {
        PRTiersWrapper tiers = null;
        if (!JadeStringUtil.isIntegerEmpty(idTiers)) {
            tiers = PRTiersHelper.getAdministrationParId(getISession(), idTiers);
        }
        return tiers;
    }

    /**
     * getter pour l'attribut versement employeur.
     *
     * @return la valeur courante de l'attribut versement employeur
     */
    public Boolean getVersementEmployeur() {
        return versementEmployeur;
    }

    /**
     * retrouve le numero AVS d'un affilie
     *
     * @return le numero AVS de l'affilie ou un String vide si pas de numero AVS pour cet affilie
     */
    public String retrieveNoAVS() {

        try {
            IPRAffilie affilie = PRAffiliationHelper.getEmployeurParNumAffilie(getSession(), getNoAffilie());
            if (affilie != null) {
                return affilie.getNoAVS();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
