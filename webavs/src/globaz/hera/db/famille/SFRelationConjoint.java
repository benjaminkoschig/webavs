/*
 * Créé le 8 sept. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
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
 *         <p>
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 *         </p>
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

    /** DOCUMENT ME! */
    public static final String FIELD_DATEDEBUT = "WKDDEB";

    /** DOCUMENT ME! */
    public static final String FIELD_DATEFIN = "WKDFIN";

    /** DOCUMENT ME! */
    public static final String FIELD_IDCONJOINTS = "WKICON";

    /** DOCUMENT ME! */
    public static final String FIELD_IDRELATIONCONJOINT = "WKIREC";

    /** DOCUMENT ME! */
    public static final String FIELD_TYPERELATION = "WKTTYP";

    /** DOCUMENT ME! */
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
     * Crée une nouvelle instance de la classe SFRelationConjoint.
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
        // Si il s'agit de la dernière relation entre les conjoints, on suprime
        // l'entrée dans la table des conjoints
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
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
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
            // Si la relation est de type indéfinie et que la nouvelle est de
            // type enfant commun ou mariage, on supprime la relation de type
            // inconnue
            if (ISFSituationFamiliale.CS_REL_CONJ_RELATION_INDEFINIE.equals(relation.getTypeRelation())
                    && (ISFSituationFamiliale.CS_REL_CONJ_ENFANT_COMMUN.equals(getTypeRelation()) || ISFSituationFamiliale.CS_REL_CONJ_MARIE
                            .equals(getTypeRelation()))) {
                relation.wantCallMethodBefore(false);
                relation.wantCallMethodAfter(false);
                relation.delete(transaction);
                deleted = true;
            } else
            // Si la relation est de type enfant commun et que la nouvelle est
            // de type mariage, on supprime la relation de type inconnue
            if (ISFSituationFamiliale.CS_REL_CONJ_ENFANT_COMMUN.equals(relation.getTypeRelation())
                    && (ISFSituationFamiliale.CS_REL_CONJ_MARIE.equals(getTypeRelation()))) {
                relation.wantCallMethodBefore(false);
                relation.wantCallMethodAfter(false);
                relation.delete(transaction);
                deleted = true;
            }
        }

        if (relList.size() > 0 && !deleted) {
            //
            // Ajuste la fin de la relation précedante pour garder la continuité
            // des période: mariage<separation<divorce
            // ex: la date de début du divorce est utilisée pour fixer la fin du
            // mariage
            relMgr.setUntilDateDebut(getDateDebut());
            relMgr.setOrderByDateDebutDsc(true);
            relMgr.find(transaction);
            Iterator it = relMgr.iterator();

            // date de fin = date de début - 1
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
            // ex: la fin d'une séparation est donnée par le début du divorce
            relMgr.setFromDateDebut(getDateDebut());
            relMgr.setUntilDateDebut(null);
            relMgr.setOrderByDateDebutDsc(false);
            relMgr.find(transaction);
            it = relMgr.iterator();

            // date de fin = date de début - 1
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
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
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
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    /*
     * Il faut s'assurer de garder cette cohérence d'une part dans relation de 2 conjoints, d'autre part entre un
     * conjoint et ces relations avec d'autre conjoints Il y a 3 groupes de types de relations possibles entre des
     * conjoints: - indéfinie (seul) - enfant commun (seul) - mariage , [séparation fait], [séparation droit], divorce
     * (dans cet ordre) (les modification des données sont faites dans le beforAdd)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // IdConjoints doit être renseigné
        _propertyMandatory(statement.getTransaction(), idConjoints, getSession().getLabel("VALIDATE_ID_CONJOINTS"));

        // aucune date ne doit être donnée pour les relation enfant commun et
        // indéfinie
        if (ISFSituationFamiliale.CS_REL_CONJ_ENFANT_COMMUN.equals(getTypeRelation())
                || ISFSituationFamiliale.CS_REL_CONJ_RELATION_INDEFINIE.equals(getTypeRelation())) {
            if (!JAUtil.isDateEmpty(dateDebut) || !JAUtil.isDateEmpty(dateFin)) {
                _addError(statement.getTransaction(), getSession().getLabel("VALIDATE_DATE_VIDE"));
            }
        } else {
            // La date de début doit être renseignée sauf pour les relation
            // enfant commun et indéfinie
            if (!JAUtil.isDateEmpty(dateDebut)) {
                _checkDate(statement.getTransaction(), dateDebut, "DATE_INVALIDE");
            } else {
                _addError(statement.getTransaction(), getSession().getLabel("VALIDATE_DATE_DEBUT_OBLIGATOIRE"));
                return;
            }

            // Si renseignée, la date de fin doit être postérieure à la date de
            // début
            if (!JAUtil.isDateEmpty(dateFin)) {
                if (!BSessionUtil.compareDateFirstLowerOrEqual(getSession(), dateDebut, dateFin)) {
                    _addError(statement.getTransaction(), getSession().getLabel("VALIDATE_DATE_FIN_AVANT_DEBUT"));
                }
            }
        }

        // if (hasErrors()) {
        // return;
        // }
        //
        // ON NE VERIFIE PLUS LA COHERENCE DES RELATION ENTRE LES CONJOINTS, CAR
        // LA CREATION DES RELATIONS PEUT SE FAIRE DANS UN ORDRE CAOTIQUE
        //
        //
        // /* Vérifie la cohérence de l'ordre des relations entre deux
        // conjoints:
        // * mariage,séparation (fait/droit),divorce, mariage ...
        // */
        // SFRelationConjointManager relMgr = new SFRelationConjointManager();
        // relMgr.setSession(getSession());
        // relMgr.setForIdDesConjoints(getIdConjoints());
        // relMgr.setOrderByDateDebutDsc(false);
        // relMgr.find(statement.getTransaction());
        // List relList = new ArrayList(relMgr.getContainer());
        //
        // // - la première relation doit être un mariage ou enfant commun ou
        // relation indéfinie
        // if ((relList.size() == 0 || relList.size() == 1 && !isNew())) { //La
        // méthode isNew() permet de savoir si on fait un add ou un upd
        // if
        // (!ISFSituationFamiliale.CS_REL_CONJ_MARIE.equals(getTypeRelation())
        // &&
        // !ISFSituationFamiliale.CS_REL_CONJ_ENFANT_COMMUN.equals(getTypeRelation())
        // &&
        // !ISFSituationFamiliale.CS_REL_CONJ_RELATION_INDEFINIE.equals(getTypeRelation()))
        // {
        // _addError(statement.getTransaction(),
        // getSession().getLabel("VALIDATE_PREMIERE_RELATION"));
        // }
        // } else { // Il y a déjà une ou plusieurs relations,
        // // Recherche la relation précédante et la suivante
        // Iterator it = relList.iterator();
        // SFRelationConjoint relPrec = null;
        // SFRelationConjoint relSuiv = null;
        // while (it.hasNext()) {
        // SFRelationConjoint relation = (SFRelationConjoint) it.next();
        // if (!isNew() && relation.getId().equals(getId())) {
        // // Si on vient d'un update et que la la relation retournée est celle
        // qui est mise-à-jour
        // continue;
        // }
        // if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(),
        // relation.getDateDebut(), getDateDebut())) {
        // // la relation suivante est trouvée
        // relSuiv = relation;
        // break;
        // } else {
        // relPrec = relation;
        // }
        // }
        // _validateRelationAvantApres(statement, relPrec, relSuiv);
        // }

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
        // vérifie qu'aucun des conjoint aie une relation conflictuelle avec un
        // autre conjoint
        // PAS DE CHEVAUCHEMENT
        _validateRelationsConjoint(statement, conjoint.getIdConjoint1());
        if (hasErrors()) {
            return;
        }
        _validateRelationsConjoint(statement, conjoint.getIdConjoint2());
    }

    /*
     * Vérifie que le conjoint n'aie pas de relation dans la meme période que la relation courante avec un autre
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
        // ax périodes de la relation à valider
        // a1 = getDateDebut() debut de période
        // a2 = getDateFin()) fin de période
        for (Iterator it = relMgr.iterator(); it.hasNext();) {
            SFApercuRelationConjoint r2 = (SFApercuRelationConjoint) it.next(); // r2
            // relation
            // dont
            // on
            // teste
            // le
            // chevauchement
            // bx périodes de la relation dont on test le chevauchement
            // b1 = r2.getDateDebut() debut de période
            // b2 = r2.getDateFin() fin de période
            if (BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDateDebut(), r2.getDateDebut())) { // if
                // (a1
                // <=
                // b1)
                if (JAUtil.isDateEmpty(getDateFin())// si la date de fin n'est
                        // pas renseignée
                        // si la relation suivante n'est pas un divorce
                        && !ISFSituationFamiliale.CS_REL_CONJ_DIVORCE.equals(r2.getTypeRelation())
                        // si la premiere relation est un mariage ou une
                        // séparation
                        && (ISFSituationFamiliale.CS_REL_CONJ_MARIE.equals(getTypeRelation())
                                || ISFSituationFamiliale.CS_REL_CONJ_SEPARE_DE_FAIT.equals(getTypeRelation()) || ISFSituationFamiliale.CS_REL_CONJ_SEPARE_JUDICIAIREMENT
                                    .equals(getTypeRelation()))) {
                    return _addErrorChevauchement(statement.getTransaction(), r2);
                } else if (!JAUtil.isDateEmpty(getDateDebut())
                        && BSessionUtil.compareDateFirstGreater(getSession(), getDateFin(), r2.getDateDebut()) // (a1
                        // >
                        // b1)
                        // si
                        // la
                        // date
                        // de
                        // début
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
                        // précédante
                        // (chevauchement)
                        // si la relation suivante n'est pas un divorce
                        && !ISFSituationFamiliale.CS_REL_CONJ_DIVORCE.equals(r2.getTypeRelation())
                        // si la premiere relation est un mariage ou une
                        // séparation
                        && (ISFSituationFamiliale.CS_REL_CONJ_MARIE.equals(getTypeRelation())
                                || ISFSituationFamiliale.CS_REL_CONJ_SEPARE_DE_FAIT.equals(getTypeRelation()) || ISFSituationFamiliale.CS_REL_CONJ_SEPARE_JUDICIAIREMENT
                                    .equals(getTypeRelation()))) {
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
                        // renseignée
                        // si la relation suivante n'est pas un divorce
                        && !ISFSituationFamiliale.CS_REL_CONJ_DIVORCE.equals(r2.getTypeRelation())
                        // si la premiere relation est un mariage ou une
                        // séparation
                        && (ISFSituationFamiliale.CS_REL_CONJ_MARIE.equals(r2.getTypeRelation())
                                || ISFSituationFamiliale.CS_REL_CONJ_SEPARE_DE_FAIT.equals(r2.getTypeRelation()) || ISFSituationFamiliale.CS_REL_CONJ_SEPARE_JUDICIAIREMENT
                                    .equals(r2.getTypeRelation()))) {
                    return _addErrorChevauchement(statement.getTransaction(), r2);
                } else if (!JAUtil.isDateEmpty(r2.getDateFin())
                        && BSessionUtil.compareDateFirstGreater(getSession(), r2.getDateFin(), getDateDebut())
                        // if (b2 != 0 && b2 > a1): si la date de début de la
                        // relation suivante est avant la fin de la précédante
                        // (chevauchement)
                        // si la relation suivante n'est pas un divorce
                        && !ISFSituationFamiliale.CS_REL_CONJ_DIVORCE.equals(getTypeRelation())
                        // si la premiere relation est un mariage ou une
                        // séparation
                        && (ISFSituationFamiliale.CS_REL_CONJ_MARIE.equals(r2.getTypeRelation())
                                || ISFSituationFamiliale.CS_REL_CONJ_SEPARE_DE_FAIT.equals(r2.getTypeRelation()) || ISFSituationFamiliale.CS_REL_CONJ_SEPARE_JUDICIAIREMENT
                                    .equals(r2.getTypeRelation()))) {
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
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
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
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
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
     * 
     * La date de début de la relation
     * 
     * @see globaz.hera.db.famille.SFRelationConjoint#getDateDebut()
     * @return null en cas d'exception
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

    // ON NE VERIFIE PLUS LA COHERENCE DES RELATION ENTRE LES CONJOINTS, CAR LA
    // CREATION DES RELATIONS PEUT SE FAIRE DANS UN ORDRE CAOTIQUE
    //
    // /* on ne vérifie la cohérence de la relation courante (celle qui est
    // ajoutée ou modifiée) par rapport
    // * à la relation précédante et à la suivante (chronologiquement).
    // * Ceci pour les mêmes conjoints
    // */
    // private boolean _validateRelationAvantApres(BStatement statement,
    // SFRelationConjoint relPrec, SFRelationConjoint relSuiv) {
    // // relSuiv = relation suivante, relPrec = relation précédante
    // if
    // (ISFSituationFamiliale.CS_REL_CONJ_SEPARE_DE_FAIT.equals(getTypeRelation())
    // ) {
    // // la relation précédante doit être mariage
    // if (relPrec == null ||
    // !ISFSituationFamiliale.CS_REL_CONJ_MARIE.equals(relPrec.getTypeRelation()))
    // {
    // _addError(statement.getTransaction(),
    // getSession().getLabel("VALIDATE_PRECEDANT_SEPARE_FAIT"));
    // return false;
    // }
    // // la relation suivante doit être séparé de fait, séparé de droit,
    // divorcé ou marié
    // if (relSuiv.getTypeRelation() != null &&
    // (
    // !ISFSituationFamiliale.CS_REL_CONJ_SEPARE_DE_FAIT.equals(relSuiv.getTypeRelation())
    // &&
    // !ISFSituationFamiliale.CS_REL_CONJ_SEPARE_JUDICIAIREMENT.equals(relSuiv.getTypeRelation())
    // &&
    // !ISFSituationFamiliale.CS_REL_CONJ_DIVORCE.equals(relSuiv.getTypeRelation())
    // &&
    // !ISFSituationFamiliale.CS_REL_CONJ_MARIE.equals(relSuiv.getTypeRelation())))
    // {
    // _addError(statement.getTransaction(),
    // getSession().getLabel("VALIDATE_SUIVANT_SEPARE_FAIT"));
    // return false;
    // }
    // } else if
    // (ISFSituationFamiliale.CS_REL_CONJ_SEPARE_JUDICIAIREMENT.equals(getTypeRelation())
    // ) {
    // // la relation précédante doit être mariage ou séparé de fait
    // if (relPrec == null ||
    // (
    // !ISFSituationFamiliale.CS_REL_CONJ_MARIE.equals(relPrec.getTypeRelation())
    // &&
    // !ISFSituationFamiliale.CS_REL_CONJ_SEPARE_DE_FAIT.equals(relPrec.getTypeRelation())))
    // {
    // _addError(statement.getTransaction(),
    // getSession().getLabel("VALIDATE_PRECEDANT_SEPARE_DROIT"));
    // return false;
    // }
    // // la relation suivante doit être séparé de droit, divorcé ou marié
    // if (relSuiv != null &&
    // (
    // !ISFSituationFamiliale.CS_REL_CONJ_SEPARE_JUDICIAIREMENT.equals(relSuiv.getTypeRelation())
    // &&
    // !ISFSituationFamiliale.CS_REL_CONJ_DIVORCE.equals(relSuiv.getTypeRelation())
    // &&
    // !ISFSituationFamiliale.CS_REL_CONJ_MARIE.equals(relSuiv.getTypeRelation())))
    // {
    // _addError(statement.getTransaction(),
    // getSession().getLabel("VALIDATE_SUIVANT_SEPARE_DROIT"));
    // return false;
    // }
    // } else if
    // (ISFSituationFamiliale.CS_REL_CONJ_DIVORCE.equals(getTypeRelation()) ) {
    // // la relation précédante doit être mariage, séparé de fait ou séparé
    // judiciairement
    // if (relPrec == null ||
    // (!ISFSituationFamiliale.CS_REL_CONJ_MARIE.equals(relPrec.getTypeRelation())
    // &&
    // !ISFSituationFamiliale.CS_REL_CONJ_SEPARE_DE_FAIT.equals(relPrec.getTypeRelation())
    // &&
    // !ISFSituationFamiliale.CS_REL_CONJ_SEPARE_JUDICIAIREMENT.equals(relPrec.getTypeRelation())))
    // {
    // _addError(statement.getTransaction(),
    // getSession().getLabel("VALIDATE_PRECEDANT_DIVORCE"));
    // return false;
    // }
    // // la relation suivante doit être marié
    // if (relSuiv != null
    // &&
    // !ISFSituationFamiliale.CS_REL_CONJ_MARIE.equals(relSuiv.getTypeRelation()))
    // {
    // _addError(statement.getTransaction(),
    // getSession().getLabel("VALIDATE_SUIVANT_DIVORCE"));
    // return false;
    // }
    // }
    // return true;
    // }

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
     * 
     * Incrémentation du compteur. A n'utiliser que pour la mise à jours d'une relation avec wantCallBefore(false);
     * Méthode utilisée pour la reprise de donnée
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
     * @param dateDebut
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * setter pour l'attribut date fin
     * 
     * @param dateFin
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * setter pour l'attribut id conjoints
     * 
     * @param idConjoints
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdConjoints(String idConjoints) {
        this.idConjoints = idConjoints;
    }

    /**
     * setter pour l'attribut id relation conjoint
     * 
     * @param idRelationConjoint
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdRelationConjoint(String idRelationConjoint) {
        this.idRelationConjoint = idRelationConjoint;
    }

    /**
     * setter pour l'attribut type relation
     * 
     * @param typeRelation
     *            une nouvelle valeur pour cet attribut
     */
    public void setTypeRelation(String typeRelation) {
        this.typeRelation = typeRelation;
    }

    public void validate(BStatement statement) throws Exception {
        _validate(statement);
    }

}
