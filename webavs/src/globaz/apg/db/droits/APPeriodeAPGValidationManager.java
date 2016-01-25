/*
 * Créé le 11 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.db.droits;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Un manager un peu spécial et qui permet de valider l'insertion d'une nouvelle période APG. SEULES LA METHODE
 * getCount() PEUT ETRE UTILISEE !!
 * </p>
 * 
 * @author vre
 */
public class APPeriodeAPGValidationManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebut;
    private String dateFin;
    private String exceptIdDroit;
    private boolean forCheckNbJoursForGenreService;
    private boolean forDroit;
    private String forGenreServiceIn;
    private String forIdDroit;

    private String idDemande;
    private String idDroitParent;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getSqlCount(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getSqlCount(BStatement statement) {
        if (forDroit) {
            return createForDroitQuery(statement);
        } else {
            return createForNouvellePeriodeQuery(statement);
        }
    }

    @Override
    protected String _getSqlSum(String fieldName, BStatement statement) {

        if (forCheckNbJoursForGenreService) {
            return createForCheckNbJoursForGenreServiceQuery(statement);
        }
        return super._getSqlSum(fieldName, statement);
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return null;
    }

    private String createForCheckNbJoursForGenreServiceQuery(BStatement statement) {
        StringBuffer query = new StringBuffer();

        // SELECT SUM(VBNJOS)
        query.append("SELECT SUM(");
        query.append(APDroitAPG.FIELDNAME_NBRJOURSSOLDES);
        query.append(")");

        // FROM CVCIWEB.PRDEMAP
        query.append(" FROM ");
        query.append(_getCollection());
        query.append(PRDemande.TABLE_NAME);

        // INNER JOIN CVCIWEB.APDROIP AS A ON CVCIWEB.PRDEMAP.WAIDEM=A.VAIDEM
        query.append(" INNER JOIN ");
        query.append(_getCollection());
        query.append(APDroitLAPG.TABLE_NAME_LAPG);
        query.append(" AS D ON ");
        query.append(_getCollection() + PRDemande.TABLE_NAME + "." + PRDemande.FIELDNAME_IDDEMANDE);
        query.append(" = ");
        query.append("D." + APDroitLAPG.FIELDNAME_IDDEMANDE);

        // INNER JOIN CVCIWEB.APDRAPP ON CVCIWEB.APDRAPP.VBIDRO=A.VAIDRO
        query.append(" INNER JOIN ");
        query.append(_getCollection());
        query.append(APDroitAPG.TABLE_NAME_DROIT_APG);
        query.append(" ON ");
        query.append(_getCollection() + APDroitAPG.TABLE_NAME_DROIT_APG + "." + APDroitAPG.FIELDNAME_IDDROIT_APG);
        query.append(" = ");
        query.append("D." + APDroitLAPG.FIELDNAME_IDDROIT_LAPG);

        // WHERE CVCIWEB.PRDEMAP.VAIDEM=112319
        query.append(" WHERE ");
        query.append("D." + APDroitLAPG.FIELDNAME_IDDEMANDE);
        query.append("=");
        query.append(idDemande);

        // AND A.VATGSE IN (52001001,52001002)
        query.append(" AND ");
        query.append("D." + APDroitLAPG.FIELDNAME_GENRESERVICE);
        query.append(" IN (" + forGenreServiceIn + ")");

        // AND (( A.VADDDR>=20030101 AND A.VADDDR<=20031231 ) OR
        // ( A.VADDFD>=20030101 AND A.VADDFD<=20031231 ) OR
        // ( A.VADDDR<=20030101 AND A.VADDFD>=20031231 ))
        query.append(" AND (");
        query.append("(D." + APDroitLAPG.FIELDNAME_DATEDEBUTDROIT + ">=" + dateDebut + " AND " + "D."
                + APDroitLAPG.FIELDNAME_DATEDEBUTDROIT + "<=" + dateFin + ")");
        query.append(" OR ");
        query.append("(D." + APDroitLAPG.FIELDNAME_DATEFINDROIT + ">=" + dateDebut + " AND " + "D."
                + APDroitLAPG.FIELDNAME_DATEFINDROIT + "<=" + dateFin + ")");
        query.append(" OR ");
        query.append("(D." + APDroitLAPG.FIELDNAME_DATEDEBUTDROIT + "<=" + dateDebut + " AND " + "D."
                + APDroitLAPG.FIELDNAME_DATEFINDROIT + ">=" + dateFin + ")");
        query.append(") ");

        // AND A.VAIDRO <> 1015
        query.append(" AND D.");
        query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
        query.append("<> " + exceptIdDroit);

        // AND NOT EXISTS(SELECT * FROM CVCIWEB.APDROIP WHERE A.VAIDRO=VAIPAR)
        query.append(" AND NOT EXISTS( ");
        query.append("SELECT * ");
        query.append(" FROM ");
        query.append(_getCollection());
        query.append(APDroitLAPG.TABLE_NAME_LAPG);
        query.append(" WHERE ");
        query.append(" D.");
        query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
        query.append("=");
        query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT);
        query.append(" ) ");

        // AND (A.VAIDRO = (SELECT MAX(VAIDRO) FROM CVCIWEB.APDROIP WHERE
        // VAIPAR<>0 AND VAIPAR=A.VAIPAR)
        // OR
        // A.VAIPAR = 0)
        query.append(" AND (D.");
        query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
        query.append("= (");
        query.append("SELECT MAX(");
        query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
        query.append(") FROM ");
        query.append(_getCollection());
        query.append(APDroitLAPG.TABLE_NAME_LAPG);
        query.append(" WHERE ");
        query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT);
        query.append("<> 0 AND ");
        query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT);
        query.append("=D.");
        query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT);
        query.append(")");
        query.append(" OR ");
        query.append(" D.");
        query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT);
        query.append("= 0");
        query.append(")");

        return query.toString();
    }

    private String createForDroitQuery(BStatement statement) {
        StringBuffer query = new StringBuffer();

        query.append("SELECT COUNT(PER1.");
        query.append(APPeriodeAPG.FIELDNAME_DATEDEBUT);
        query.append(")");
        query.append(" FROM ");
        query.append(_getCollection());
        query.append(APPeriodeAPG.TABLE_NAME);
        query.append(" AS PER1, ");
        query.append(_getCollection());
        query.append(APPeriodeAPG.TABLE_NAME);
        query.append(" AS PER2, ");
        query.append(_getCollection());
        query.append(APDroitLAPG.TABLE_NAME_LAPG);
        query.append(" AS DRO1, ");
        query.append(_getCollection());
        query.append(APDroitLAPG.TABLE_NAME_LAPG);
        query.append(" AS DRO2 WHERE PER1.");
        query.append(APPeriodeAPG.FIELDNAME_DATEDEBUT);
        query.append(" <= PER2.");
        query.append(APPeriodeAPG.FIELDNAME_DATEFIN);
        query.append(" AND PER1.");
        query.append(APPeriodeAPG.FIELDNAME_DATEFIN);
        query.append(" >= PER2.");
        query.append(APPeriodeAPG.FIELDNAME_DATEDEBUT);
        query.append(" AND PER1.");
        query.append(APPeriodeAPG.FIELDNAME_IDDROIT);
        query.append(" = ");
        query.append(forIdDroit);
        query.append(" AND PER1.");
        query.append(APPeriodeAPG.FIELDNAME_IDDROIT);
        query.append(" <> PER2.");
        query.append(APPeriodeAPG.FIELDNAME_IDDROIT);
        query.append(" AND PER1.");
        query.append(APPeriodeAPG.FIELDNAME_IDDROIT);
        query.append(" = DRO1.");
        query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
        query.append(" AND PER2.");
        query.append(APPeriodeAPG.FIELDNAME_IDDROIT);
        query.append(" = DRO2.");
        query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
        query.append(" AND DRO1.");
        query.append(APDroitLAPG.FIELDNAME_IDDEMANDE);
        query.append(" = DRO2.");
        query.append(APDroitLAPG.FIELDNAME_IDDEMANDE);
        query.append(" AND DRO1.");
        query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT);
        query.append(" <> DRO2.");
        query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
        query.append(" AND (DRO1.");
        query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT);
        query.append(" <> DRO2.");
        query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT);
        query.append(" OR (DRO1.");
        query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT);
        query.append(" = 0 AND DRO2.");
        query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT);
        query.append(" = 0 ) )");

        // qui n'a pas ete corrige (son id n'est l'id parent de aucun droit)
        query.append(" AND NOT EXISTS( ");
        query.append("SELECT * ");
        query.append(" FROM ");
        query.append(_getCollection());
        query.append(APDroitLAPG.TABLE_NAME_LAPG);
        query.append(" WHERE ");
        query.append(" DRO2.");
        query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
        query.append("=");
        query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT);
        query.append(" ) ");

        // et dont l'id et le plus grand d'une correction pour un id parent
        // donne
        if (!JadeStringUtil.isIntegerEmpty(idDroitParent)) {
            query.append(" AND (DRO1.");
            query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
            query.append("=(");
            query.append("SELECT MAX(");
            query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
            query.append(") FROM ");
            query.append(_getCollection());
            query.append(APDroitLAPG.TABLE_NAME_LAPG);
            query.append(" WHERE ");
            query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT);
            query.append("<> 0 AND ");
            query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT);
            query.append("=DRO2.");
            query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT);
            query.append(")");

            query.append(" OR ");

            query.append(" DRO2.");
            query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT);
            query.append("= 0");

            query.append(")");
        }

        return query.toString();
    }

    private String createForNouvellePeriodeQuery(BStatement statement) {
        StringBuffer query = new StringBuffer();

        // sélectionner toutes les périodes
        query.append("SELECT COUNT(");
        query.append(APPeriodeAPG.FIELDNAME_DATEDEBUT);
        query.append(")");
        query.append(" FROM ");
        query.append(_getCollection());
        query.append(APPeriodeAPG.TABLE_NAME);
        query.append(",");
        query.append(_getCollection());
        query.append(APDroitLAPG.TABLE_NAME_LAPG);
        query.append(" AS D ");
        query.append(" WHERE ");

        // jointes à un droit
        query.append(APPeriodeAPG.FIELDNAME_IDDROIT);
        query.append("=");
        query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);

        // pour une meme demande (c'est-a-dire un meme tiers)
        if (!JadeStringUtil.isIntegerEmpty(idDemande)) {
            query.append(" AND ");
            query.append(APDroitLAPG.FIELDNAME_IDDEMANDE);
            query.append("=");
            query.append(idDemande);
        }

        // j'ai un droit parent
        if (!JadeStringUtil.isIntegerEmpty(idDroitParent)) {
            // mon pere n'est pas pris en compte
            query.append(" AND ");
            query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
            query.append("<>");
            query.append(idDroitParent);

            // tous les enfants de mon pere ne sont pas pris en compte
            query.append(" AND ");
            query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT);
            query.append("<>");
            query.append(idDroitParent);
        } else {
            // je n'ai pas de pere
            // je ne suis pas pris en compte
            query.append(" AND ");
            query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
            query.append("<>");
            query.append(forIdDroit);
        }

        query.append(" AND ");

        // mais qui chevauchent les dates transmises
        query.append(APPeriodeAPG.FIELDNAME_DATEDEBUT);
        query.append("<=");
        query.append(_dbWriteDateAMJ(statement.getTransaction(), dateFin));
        query.append(" AND ");
        query.append(APPeriodeAPG.FIELDNAME_DATEFIN);
        query.append(">=");
        query.append(_dbWriteDateAMJ(statement.getTransaction(), dateDebut));

        // qui n'a pas ete corrige (son id n'est l'id parent de aucun droit)
        query.append(" AND NOT EXISTS( ");
        query.append("SELECT * ");
        query.append(" FROM ");
        query.append(_getCollection());
        query.append(APDroitLAPG.TABLE_NAME_LAPG);
        query.append(" WHERE ");
        query.append(" D.");
        query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
        query.append("=");
        query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT);
        query.append(" ) ");

        // et dont l'id et le plus grand d'une correction pour un id parent
        // donne
        if (!JadeStringUtil.isIntegerEmpty(idDroitParent)) {
            query.append(" AND (D.");
            query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
            query.append("= (");
            query.append("SELECT MAX(");
            query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
            query.append(") FROM ");
            query.append(_getCollection());
            query.append(APDroitLAPG.TABLE_NAME_LAPG);
            query.append(" WHERE ");
            query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT);
            query.append("<> 0 AND ");
            query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT);
            query.append("=D.");
            query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT);
            query.append(")");

            query.append(" OR ");

            query.append(" D.");
            query.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT);
            query.append("= 0");

            query.append(")");
        }

        return query.toString();
    }

    /**
     * Intialise le manager de manière à ce qu'il recherche le nombre de jour solde pour les genre de service donne,
     * pour la periode donne
     * 
     * les droit corrige ne sont pas pris en compte seul la plus grande correction est prise en compte pour un parent
     * donne
     * 
     * @param forIdDroit
     *            une nouvelle valeur pour cet attribut
     */
    public void setForCheckNbJoursForGenreService(String exceptIdDroit, String idDemande, String dateDebut,
            String dateFin, String forGenreServiceIn) {
        this.exceptIdDroit = exceptIdDroit;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.idDemande = idDemande;
        this.forGenreServiceIn = forGenreServiceIn;

        forDroit = false;
        forCheckNbJoursForGenreService = true;
    }

    /**
     * Intialise le manager de manière à ce qu'il recherche les périodes qui se chevauchent pour un droit existant
     * portant l'identifiant 'forIdDroit'.
     * 
     * @param forIdDroit
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;

        forDroit = true;
        forCheckNbJoursForGenreService = false;
    }

    /**
     * Initialise le manager pour qu'il recherche les périodes qui chevauchent les dates 'dateDebut' à 'dateFin' pour un
     * droit existant.
     * 
     * @param idDroit
     *            une nouvelle valeur pour cet attribut
     * @param idDroitParent
     *            DOCUMENT ME!
     * @param idDemande
     *            DOCUMENT ME!
     * @param dateDebut
     *            une nouvelle valeur pour cet attribut
     * @param dateFin
     *            une nouvelle valeur pour cet attribut
     */
    public void setForNouvellePeriode(String idDroit, String idDroitParent, String idDemande, String dateDebut,
            String dateFin) {
        forIdDroit = idDroit;
        this.idDemande = idDemande;
        this.idDroitParent = idDroitParent;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;

        forDroit = false;
        forCheckNbJoursForGenreService = false;
    }
}
