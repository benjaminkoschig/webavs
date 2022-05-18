/*
 * Cr�� le 8 sept. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.hera.db.famille;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendarGregorianStandard;
import globaz.globall.util.JADate;
import globaz.globall.util.JAUtil;
import globaz.hera.api.ISFSituationFamiliale;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <H1>Description</H1>
 *
 * @author jpa
 *
 * <p>
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 * </p>
 */
public class SFRelationConjoint extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final int ALT_KEY_CONJ_TYPE = 1;

    public static final int ALT_KEY_ID_CONJOINTS = 2;

    /**
     * DOCUMENT ME!
     */
    public static final String FIELD_DATEDEBUT = "WKDDEB";

    /**
     * DOCUMENT ME!
     */
    public static final String FIELD_DATEFIN = "WKDFIN";

    /**
     * DOCUMENT ME!
     */
    public static final String FIELD_IDCONJOINTS = "WKICON";

    /**
     * DOCUMENT ME!
     */
    public static final String FIELD_IDRELATIONCONJOINT = "WKIREC";

    /**
     * DOCUMENT ME!
     */
    public static final String FIELD_TYPERELATION = "WKTTYP";

    /**
     * DOCUMENT ME!
     */
    public static final String TABLE_NAME = "SFRELCON";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateDebut = "";
    private String dateFin = "";
    private String idConjoints = "";
    private String idRelationConjoint = "";
    private String typeRelation = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Cr�e une nouvelle instance de la classe SFRelationConjoint.
     */
    public SFRelationConjoint() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param r2
     * @return
     */
    private boolean _addErrorChevauchement(BTransaction transaction, SFApercuRelationConjoint r2) {
        _addError(
                transaction,
                getSession().getLabel("VALIDATE_CHEVAUCHEMENT_RELATION") + ": " + r2.getPrenom1() + " " + r2.getNom1()
                        + ", " + r2.getPrenom2() + " " + r2.getNom2() + ", "
                        + transaction.getSession().getCodeLibelle(r2.getTypeRelation()) + ", " + r2.getDateDebut()
                        + "-" + r2.getDateFin());
        return false;
    }

    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        // Si il s'agit de la derni�re relation entre les conjoints, on suprime
        // l'entr�e dans la table des conjoints
        SFRelationConjointManager relManager = new SFRelationConjointManager();
        relManager.setSession(getSession());
        relManager.setForIdDesConjoints(getIdConjoints());
        int nbRel = relManager.getCount(transaction);
        if (nbRel == 0) {
            SFConjoint conjoint = new SFConjoint();
            conjoint.setSession(getSession());
            conjoint.setIdConjoints(getIdConjoints());
            conjoint.retrieve(transaction);
            if (!conjoint.isNew()) {
                conjoint.delete(transaction);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param transaction DOCUMENT ME!
     * @throws Exception DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        setIdRelationConjoint(_incCounter(transaction, "0"));

        // Pour la relation de mariage
        SFRelationConjointManager relMgr = new SFRelationConjointManager();
        relMgr.setSession(getSession());
        relMgr.setForIdDesConjoints(getIdConjoints());
        relMgr.setOrderByDateDebutDsc(false);
        relMgr.find(transaction);
        List relList = new ArrayList(relMgr.getContainer());

        boolean deleted = false;

        // S'il y avait une relation
        if (relList.size() == 1) {
            SFRelationConjoint relation = (SFRelationConjoint) relList.get(0);
            // Si la relation est de type ind�finie et que la nouvelle est de
            // type enfant commun ou mariage ou LPart, on supprime la relation de type
            // inconnue
            if (ISFSituationFamiliale.CS_REL_CONJ_RELATION_INDEFINIE.equals(relation.getTypeRelation())
                    && (ISFSituationFamiliale.CS_REL_CONJ_ENFANT_COMMUN.equals(getTypeRelation())
                    || ISFSituationFamiliale.CS_REL_CONJ_MARIE.equals(getTypeRelation())
                    || ISFSituationFamiliale.CS_REL_CONJ_LPART.equals(getTypeRelation()))) {
                relation.wantCallMethodBefore(false);
                relation.wantCallMethodAfter(false);
                relation.delete(transaction);
                deleted = true;
            } else
                // Si la relation est de type enfant commun et que la nouvelle est
                // de type mariage ou LPart, on supprime la relation de type inconnue
                if (ISFSituationFamiliale.CS_REL_CONJ_ENFANT_COMMUN.equals(relation.getTypeRelation())
                        && (ISFSituationFamiliale.CS_REL_CONJ_MARIE.equals(getTypeRelation())
                        || ISFSituationFamiliale.CS_REL_CONJ_LPART.equals(getTypeRelation()))) {
                    relation.wantCallMethodBefore(false);
                    relation.wantCallMethodAfter(false);
                    relation.delete(transaction);
                    deleted = true;
                }
        }

        if (relList.size() > 0 && !deleted) {
            //
            // Ajuste la fin de la relation pr�cedante pour garder la continuit�
            // des p�riode: mariage<separation<divorce
            // ex: la date de d�but du divorce est utilis�e pour fixer la fin du
            // mariage
            relMgr.setUntilDateDebut(getDateDebut());
            relMgr.setOrderByDateDebutDsc(true);
            relMgr.find(transaction);
            Iterator it = relMgr.iterator();

            // date de fin = date de d�but - 1
            if (it.hasNext()) {
                JACalendarGregorianStandard calendar = new JACalendarGregorianStandard(); // jaDateDebut.
                JADate jaDateFin = calendar.addDays(new JADate(getDateDebut()), -1);
                SFRelationConjoint relation = (SFRelationConjoint) it.next();
                relation.setDateFin(jaDateFin.toStr("."));
                relation.wantCallValidate(false);
                relation.wantCallMethodAfter(false);
                wantCallMethodBefore(false);
                relation.update(transaction);
            }

            // Ajuste la fin de la relation courante en fonction de la relation
            // suivante
            // ex: la fin d'une s�paration est donn�e par le d�but du divorce
            relMgr.setFromDateDebut(getDateDebut());
            relMgr.setUntilDateDebut(null);
            relMgr.setOrderByDateDebutDsc(false);
            relMgr.find(transaction);
            it = relMgr.iterator();

            // date de fin = date de d�but - 1
            if (it.hasNext()) {
                SFRelationConjoint relation = (SFRelationConjoint) it.next();
                // seulement si la date suivante ne chevauche pas la relation
                // courant
                if (BSessionUtil.compareDateFirstLower(getSession(), getDateDebut(), relation.getDateDebut())
                        && (JAUtil.isDateEmpty(getDateFin()) || BSessionUtil.compareDateFirstLower(getSession(),
                        getDateFin(), relation.getDateDebut()))) {
                    JACalendarGregorianStandard calendar = new JACalendarGregorianStandard();
                    // jaDateDebut.
                    JADate jaDateFin = calendar.addDays(new JADate(relation.getDateDebut()), -1);
                    setDateFin(jaDateFin.toStr("."));
                    relation.wantCallValidate(false);
                    relation.wantCallMethodAfter(false);
                    wantCallMethodBefore(false);
                    relation.update(transaction);
                }
            }
        }

    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    /**
     * DOCUMENT ME!
     *
     * @param statement DOCUMENT ME!
     * @throws Exception DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idRelationConjoint = statement.dbReadNumeric(FIELD_IDRELATIONCONJOINT);
        idConjoints = statement.dbReadNumeric(FIELD_IDCONJOINTS);
        dateDebut = statement.dbReadDateAMJ(FIELD_DATEDEBUT);
        dateFin = statement.dbReadDateAMJ(FIELD_DATEFIN);
        typeRelation = statement.dbReadNumeric(FIELD_TYPERELATION);
    }

    /**
     * DOCUMENT ME!
     *
     * @param statement DOCUMENT ME!
     * @throws Exception DOCUMENT ME!
     */
    /*
     * Il faut s'assurer de garder cette coh�rence d'une part dans relation de 2 conjoints, d'autre part entre un
     * conjoint et ces relations avec d'autre conjoints Il y a 3 groupes de types de relations possibles entre des
     * conjoints: - ind�finie (seul) - enfant commun (seul) - mariage , [s�paration fait], [s�paration droit], divorce
     * (dans cet ordre) (les modification des donn�es sont faites dans le beforAdd)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // IdConjoints doit �tre renseign�
        _propertyMandatory(statement.getTransaction(), idConjoints, getSession().getLabel("VALIDATE_ID_CONJOINTS"));

        // aucune date ne doit �tre donn�e pour les relation enfant commun et
        // ind�finie
        if (ISFSituationFamiliale.CS_REL_CONJ_ENFANT_COMMUN.equals(getTypeRelation())
                || ISFSituationFamiliale.CS_REL_CONJ_RELATION_INDEFINIE.equals(getTypeRelation())) {
            if (!JAUtil.isDateEmpty(dateDebut) || !JAUtil.isDateEmpty(dateFin)) {
                _addError(statement.getTransaction(), getSession().getLabel("VALIDATE_DATE_VIDE"));
            }
        } else {
            // La date de d�but doit �tre renseign�e sauf pour les relation
            // enfant commun et ind�finie
            if (!JAUtil.isDateEmpty(dateDebut)) {
                _checkDate(statement.getTransaction(), dateDebut, "DATE_INVALIDE");
            } else {
                _addError(statement.getTransaction(), getSession().getLabel("VALIDATE_DATE_DEBUT_OBLIGATOIRE"));
                return;
            }

            // Si renseign�e, la date de fin doit �tre post�rieure � la date de
            // d�but
            if (!JAUtil.isDateEmpty(dateFin)) {
                if (!BSessionUtil.compareDateFirstLowerOrEqual(getSession(), dateDebut, dateFin)) {
                    _addError(statement.getTransaction(), getSession().getLabel("VALIDATE_DATE_FIN_AVANT_DEBUT"));
                }
            }
        }


        SFConjoint conjoint = new SFConjoint();
        conjoint.setSession(getSession());
        conjoint.setIdConjoints(getIdConjoints());
        conjoint.retrieve(statement.getTransaction());

        // le conjoint inconnu ne peut avoir que des relations de type 'Enfant
        // Commun'
        if (ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU.equals(conjoint.getIdConjoint1())
                || ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU.equals(conjoint.getIdConjoint2())) {
            if (!ISFSituationFamiliale.CS_REL_CONJ_ENFANT_COMMUN.equals(getTypeRelation())) {
                _addError(statement.getTransaction(), getSession().getLabel("VALIDATE_CONJOINT_INCONNU_ENFANT_COMMUN"));
            }
        }

        if (hasErrors()) {
            return;
        }
        // v�rifie qu'aucun des conjoint aie une relation conflictuelle avec un
        // autre conjoint
        // PAS DE CHEVAUCHEMENT
        _validateRelationsConjoint(statement, conjoint.getIdConjoint1());
        if (hasErrors()) {
            return;
        }
        _validateRelationsConjoint(statement, conjoint.getIdConjoint2());
    }

    /*
     * V�rifie que le conjoint n'aie pas de relation dans la meme p�riode que la relation courante avec un autre
     * conjoint
     */
    public boolean _validateRelationsConjoint(BStatement statement, String idConjoint) throws Exception {
        SFApercuRelationConjointManager relMgr = new SFApercuRelationConjointManager();
        relMgr.setSession(getSession());
        relMgr.setForIdConjoint(idConjoint);
        relMgr.setExceptIdRelationConjoint(getIdRelationConjoint());
        relMgr.setNoWantEnfantCommun(true);
        relMgr.setNoWantRelationIndefinie(true);
        relMgr.find(statement.getTransaction());
        // ax p�riodes de la relation � valider
        // a1 = getDateDebut() debut de p�riode
        // a2 = getDateFin()) fin de p�riode
        for (Iterator it = relMgr.iterator(); it.hasNext(); ) {
            SFApercuRelationConjoint r2 = (SFApercuRelationConjoint) it.next(); // r2
            // relation
            // dont
            // on
            // teste
            // le
            // chevauchement
            // bx p�riodes de la relation dont on test le chevauchement
            // b1 = r2.getDateDebut() debut de p�riode
            // b2 = r2.getDateFin() fin de p�riode
            if (BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDateDebut(), r2.getDateDebut())) { // if
                // (a1
                // <=
                // b1)
                if (JAUtil.isDateEmpty(getDateFin())// si la date de fin n'est
                        // pas renseign�e
                        // si la relation suivante n'est pas un divorce ou un lpart dissous
                        && (!ISFSituationFamiliale.CS_REL_CONJ_DIVORCE.equals(r2.getTypeRelation())
                        || !ISFSituationFamiliale.CS_REL_CONJ_LPART_DISSOUS.equals(r2.getTypeRelation()))
                        // si la premiere relation est un mariage, un lpart ou une
                        // s�paration
                        && (ISFSituationFamiliale.CS_REL_CONJ_MARIE.equals(getTypeRelation())
                        || ISFSituationFamiliale.CS_REL_CONJ_LPART.equals(getTypeRelation())
                        || ISFSituationFamiliale.CS_REL_CONJ_SEPARE_DE_FAIT.equals(getTypeRelation())
                        || ISFSituationFamiliale.CS_REL_CONJ_LPART_SEPARE_DE_FAIT.equals(getTypeRelation())
                        || ISFSituationFamiliale.CS_REL_CONJ_SEPARE_JUDICIAIREMENT.equals(getTypeRelation())
                        || ISFSituationFamiliale.CS_REL_CONJ_LPART_SEPARE_JUDICIAIREMENT.equals(getTypeRelation()))) {
                    return _addErrorChevauchement(statement.getTransaction(), r2);
                } else if (!JAUtil.isDateEmpty(getDateDebut())
                        && BSessionUtil.compareDateFirstGreater(getSession(), getDateFin(), r2.getDateDebut()) // (a1
                        // >
                        // b1)
                        // si
                        // la
                        // date
                        // de
                        // d�but
                        // de
                        // la
                        // relation
                        // suivante
                        // est
                        // avant
                        // la
                        // fin
                        // de
                        // la
                        // pr�c�dante
                        // (chevauchement)
                        // si la relation suivante n'est pas un divorce ou un lpart dissous
                        && (!ISFSituationFamiliale.CS_REL_CONJ_DIVORCE.equals(r2.getTypeRelation())
                        || !ISFSituationFamiliale.CS_REL_CONJ_LPART_DISSOUS.equals(r2.getTypeRelation()))
                        // si la premiere relation est un mariage, un lpart ou une
                        // s�paration
                        && (ISFSituationFamiliale.CS_REL_CONJ_MARIE.equals(getTypeRelation())
                        || ISFSituationFamiliale.CS_REL_CONJ_LPART.equals(getTypeRelation())
                        || ISFSituationFamiliale.CS_REL_CONJ_SEPARE_DE_FAIT.equals(getTypeRelation())
                        || ISFSituationFamiliale.CS_REL_CONJ_LPART_SEPARE_DE_FAIT.equals(getTypeRelation())
                        || ISFSituationFamiliale.CS_REL_CONJ_SEPARE_JUDICIAIREMENT.equals(getTypeRelation())
                        || ISFSituationFamiliale.CS_REL_CONJ_LPART_SEPARE_JUDICIAIREMENT.equals(getTypeRelation()))) {
                    return _addErrorChevauchement(statement.getTransaction(), r2);
                }
            } else if (BSessionUtil.compareDateFirstGreater(getSession(), getDateDebut(), r2.getDateDebut())) { // if
                // (a1
                // >
                // b1)
                // logique
                // identique
                // au
                // premier
                // cas
                // mais
                // en
                // symetrique
                // r2<-->this
                if (JAUtil.isDateEmpty(r2.getDateFin()) // if (b2 == 0): si la
                        // date de fin n'est pas
                        // renseign�e
                        // si la relation suivante n'est pas un divorce ou un lpart dissous
                        && (!ISFSituationFamiliale.CS_REL_CONJ_DIVORCE.equals(r2.getTypeRelation())
                        || !ISFSituationFamiliale.CS_REL_CONJ_LPART_DISSOUS.equals(r2.getTypeRelation()))
                        // si la premiere relation est un mariage, un lpart ou une s�paration
                        && (ISFSituationFamiliale.CS_REL_CONJ_MARIE.equals(r2.getTypeRelation())
                        || ISFSituationFamiliale.CS_REL_CONJ_LPART.equals(r2.getTypeRelation())
                        || ISFSituationFamiliale.CS_REL_CONJ_SEPARE_DE_FAIT.equals(r2.getTypeRelation())
                        || ISFSituationFamiliale.CS_REL_CONJ_LPART_SEPARE_DE_FAIT.equals(r2.getTypeRelation())
                        || ISFSituationFamiliale.CS_REL_CONJ_SEPARE_JUDICIAIREMENT.equals(r2.getTypeRelation())
                        || ISFSituationFamiliale.CS_REL_CONJ_LPART_SEPARE_JUDICIAIREMENT.equals(r2.getTypeRelation()))) {
                    return _addErrorChevauchement(statement.getTransaction(), r2);
                } else if (!JAUtil.isDateEmpty(r2.getDateFin())
                        && BSessionUtil.compareDateFirstGreater(getSession(), r2.getDateFin(), getDateDebut())
                        // if (b2 != 0 && b2 > a1): si la date de d�but de la
                        // relation suivante est avant la fin de la pr�c�dante
                        // (chevauchement)
                        // si la relation suivante n'est pas un divorce ou un lpart dissous
                        && (!ISFSituationFamiliale.CS_REL_CONJ_DIVORCE.equals(getTypeRelation())
                        || !ISFSituationFamiliale.CS_REL_CONJ_LPART_DISSOUS.equals(getTypeRelation()))
                        // si la premiere relation est un mariage, lpart ou une s�paration
                        && (ISFSituationFamiliale.CS_REL_CONJ_MARIE.equals(r2.getTypeRelation())
                        || ISFSituationFamiliale.CS_REL_CONJ_LPART.equals(r2.getTypeRelation())
                        || ISFSituationFamiliale.CS_REL_CONJ_SEPARE_DE_FAIT.equals(r2.getTypeRelation())
                        || ISFSituationFamiliale.CS_REL_CONJ_LPART_SEPARE_DE_FAIT.equals(r2.getTypeRelation())
                        || ISFSituationFamiliale.CS_REL_CONJ_SEPARE_JUDICIAIREMENT.equals(r2.getTypeRelation())
                        || ISFSituationFamiliale.CS_REL_CONJ_LPART_SEPARE_JUDICIAIREMENT.equals(r2.getTypeRelation()))) {
                    return _addErrorChevauchement(statement.getTransaction(), r2);
                }
            }

        }
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BEntity#_writeAlternateKey(globaz.globall.db.BStatement , int)
     */
    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        if (alternateKey == ALT_KEY_CONJ_TYPE) {
            statement.writeKey(FIELD_IDCONJOINTS,
                    _dbWriteNumeric(statement.getTransaction(), idConjoints, "idConjoints"));
            statement.writeKey(FIELD_TYPERELATION,
                    _dbWriteNumeric(statement.getTransaction(), typeRelation, "typeRelation"));

        } else if (alternateKey == ALT_KEY_ID_CONJOINTS) {
            statement.writeKey(FIELD_IDCONJOINTS,
                    _dbWriteNumeric(statement.getTransaction(), idConjoints, "idConjoints"));
        } else {
            super._writeAlternateKey(statement, alternateKey);
        }

    }

    /**
     * DOCUMENT ME!
     *
     * @param statement DOCUMENT ME!
     * @throws Exception DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELD_IDRELATIONCONJOINT,
                _dbWriteNumeric(statement.getTransaction(), idRelationConjoint, "idRelationConjoint"));
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */

    /**
     * DOCUMENT ME!
     *
     * @param statement DOCUMENT ME!
     * @throws Exception DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELD_IDRELATIONCONJOINT,
                _dbWriteNumeric(statement.getTransaction(), idRelationConjoint, "idRelationConjoint"));
        statement
                .writeField(FIELD_IDCONJOINTS, _dbWriteNumeric(statement.getTransaction(), idConjoints, "idConjoints"));
        statement.writeField(FIELD_DATEDEBUT, _dbWriteDateAMJ(statement.getTransaction(), dateDebut, "dateDebut"));
        statement.writeField(FIELD_DATEFIN, _dbWriteDateAMJ(statement.getTransaction(), dateFin, "dateFin"));
        statement.writeField(FIELD_TYPERELATION,
                _dbWriteNumeric(statement.getTransaction(), typeRelation, "typeRelation"));
    }

    /**
     * getter pour l'attribut date debut
     * <p>
     * La date de d�but de la relation
     *
     * @return null en cas d'exception
     * @see globaz.hera.db.famille.SFRelationConjoint#getDateDebut()
     */

    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * getter pour l'attribut date fin
     *
     * @return la valeur courante de l'attribut date fin
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * getter pour l'attribut id conjoints
     *
     * @return la valeur courante de l'attribut id conjoints
     */
    public String getIdConjoints() {
        return idConjoints;
    }

    /**
     * getter pour l'attribut id relation conjoint
     *
     * @return la valeur courante de l'attribut id relation conjoint
     */
    public String getIdRelationConjoint() {
        return idRelationConjoint;
    }

    /**
     * getter pour l'attribut type relation
     *
     * @return la valeur courante de l'attribut type relation
     */
    public String getTypeRelation() {
        return typeRelation;
    }

    /**
     * Incr�mentation du compteur. A n'utiliser que pour la mise � jours d'une relation avec wantCallBefore(false);
     * M�thode utilis�e pour la reprise de donn�e
     *
     * @param transaction
     * @throws Exception
     */
    public void incrementId(BTransaction transaction) throws Exception {
        setIdRelationConjoint(_incCounter(transaction, "0"));
    }

    /**
     * setter pour l'attribut date debut
     *
     * @param dateDebut une nouvelle valeur pour cet attribut
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * setter pour l'attribut date fin
     *
     * @param dateFin une nouvelle valeur pour cet attribut
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * setter pour l'attribut id conjoints
     *
     * @param idConjoints une nouvelle valeur pour cet attribut
     */
    public void setIdConjoints(String idConjoints) {
        this.idConjoints = idConjoints;
    }

    /**
     * setter pour l'attribut id relation conjoint
     *
     * @param idRelationConjoint une nouvelle valeur pour cet attribut
     */
    public void setIdRelationConjoint(String idRelationConjoint) {
        this.idRelationConjoint = idRelationConjoint;
    }

    /**
     * setter pour l'attribut type relation
     *
     * @param typeRelation une nouvelle valeur pour cet attribut
     */
    public void setTypeRelation(String typeRelation) {
        this.typeRelation = typeRelation;
    }

    public void validate(BStatement statement) throws Exception {
        _validate(statement);
    }

}
