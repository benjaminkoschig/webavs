/*
 * Créé le 2 févr. 06
 */
package globaz.osiris.db.contentieux;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import java.io.Serializable;

/**
 * @author sch date : 2 févr. 06
 */
public class CAMotifContentieux extends BEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CS_MOTIF_BLOCAGE_AUTRES = "204005";
    public final static String CS_MOTIF_BLOCAGE_CANDIDAT_POUR_EXCLUSION = "204103";
    public final static String CS_MOTIF_BLOCAGE_CENTRALE_ENCAISSEMENT = "204007";
    public final static String CS_MOTIF_BLOCAGE_CREDIT_EN_COMPTE_CROISEMENT_VERSEMENT = "204050";
    public final static String CS_MOTIF_BLOCAGE_CREDIT_EN_COMPTE_RECTIFIEE_AVEC_CREDIT = "204047";
    public final static String CS_MOTIF_BLOCAGE_CREDIT_EN_COMPTE_RENTIER = "204021";
    public final static String CS_MOTIF_BLOCAGE_CREDIT_EN_COMPTE_RETENUE_S_PREST = "204049";
    public final static String CS_MOTIF_BLOCAGE_CREDIT_EN_COMPTE_SOLDE_CREDITEUR = "204048";
    public final static String CS_MOTIF_BLOCAGE_CREDIT_EN_COMPTE_VERSEMENT_ANTICIPE = "204046";
    public final static String CS_MOTIF_BLOCAGE_DEC_JUD_FAILLITE_AJOURNEMENT_FAILLITE = "204045";
    public final static String CS_MOTIF_BLOCAGE_DEC_JUD_FAILLITE_BENEF_INVENTAIRE = "204044";
    public final static String CS_MOTIF_BLOCAGE_DEC_JUD_FAILLITE_IND_SNC = "204041";
    public final static String CS_MOTIF_BLOCAGE_DEC_JUD_FAILLITE_RI = "204042";
    public final static String CS_MOTIF_BLOCAGE_DEC_JUD_FAILLITE_SOC = "204040";
    public final static String CS_MOTIF_BLOCAGE_DEC_JUD_FAILLITE_SURSIS_CONCORDATAIRE = "204023";
    public final static String CS_MOTIF_BLOCAGE_DEC_NON_EX_OPPOSITION = "204010";
    public final static String CS_MOTIF_BLOCAGE_DEC_NON_EX_RECOURS_TCA = "204039";
    public final static String CS_MOTIF_BLOCAGE_DECOMPTE_FINAL_LIQUIDATION_DECOMPTE_ANNUEL = "204059";
    public final static String CS_MOTIF_BLOCAGE_DECOMPTE_LTN = "204104";
    public final static String CS_MOTIF_BLOCAGE_DISPENSE = "204101";
    public final static String CS_MOTIF_BLOCAGE_EINSPRUCH_BESCHWERDE = "204003";
    public final static String CS_MOTIF_BLOCAGE_ENQUETE_CESSATION_ACTIVITE = "204031";
    public final static String CS_MOTIF_BLOCAGE_ENQUETE_CHANGEMENT_ADRESSE = "204032";
    public final static String CS_MOTIF_BLOCAGE_ENQUETE_DECES = "204030";
    public final static String CS_MOTIF_BLOCAGE_ENQUETE_DEMANDE_RED_REMISE = "204037";
    public final static String CS_MOTIF_BLOCAGE_ENQUETE_DIVERS = "204036";
    public final static String CS_MOTIF_BLOCAGE_ENQUÊTE_NOM_ETAT_CIVIL = "204034";
    public final static String CS_MOTIF_BLOCAGE_ENQUETE_RAISON_SOCIALE = "204033";
    public final static String CS_MOTIF_BLOCAGE_ENQUETE_SITUATION_AVS = "204035";
    public final static String CS_MOTIF_BLOCAGE_FAILLITE = "204022";
    public final static String CS_MOTIF_BLOCAGE_IRRECOUVRABLE = "204020";
    public final static String CS_MOTIF_BLOCAGE_IRRECOUVRABLE_CE = "204008";
    public final static String CS_MOTIF_BLOCAGE_NETTOABLIEFERUNG_ZI = "204011";
    public final static String CS_MOTIF_BLOCAGE_NOUVELLE_CALCULATION = "204004";
    public final static String CS_MOTIF_BLOCAGE_PROROGATION_DELAI_PAIEMENT = "204002";
    public final static String CS_MOTIF_BLOCAGE_RECOURS = "204006";
    public final static String CS_MOTIF_BLOCAGE_SAISIE = "204012";
    public final static String CS_MOTIF_BLOCAGE_SAISIE_SUR_SALAIRE = "204024";
    public final static String CS_MOTIF_BLOCAGE_SURSIS = "204102";
    public final static String CS_MOTIF_BLOCAGE_SURSIS_ADB_NOTOIRE = "204054";
    public final static String CS_MOTIF_BLOCAGE_SURSIS_AREMISE_COTISATIONS = "204053";
    public final static String CS_MOTIF_BLOCAGE_SURSIS_AU_PAIEMENT = "204001";
    public final static String CS_MOTIF_BLOCAGE_SURSIS_HOSPICE_GENERAL = "204056";
    public final static String CS_MOTIF_BLOCAGE_SURSIS_PERS_MAISON_DIPLOMATIQUE = "204057";
    public final static String CS_MOTIF_BLOCAGE_SURSIS_PERS_MAISON_FONCT_INTERNATIONAUX = "204058";
    public final static String CS_MOTIF_BLOCAGE_SURSIS_REQUERANT_ASILE = "204055";
    public final static String CS_MOTIF_BLOCAGE_SURSIS_SURSIS_SUR_PART_PENALE = "204052";
    public final static String CS_MOTIF_BLOCAGE_VERSEMENT_PAR_CE = "204009";

    public static final String FIELD_COMMENTAIRE = "COMMENTAIRE";
    public static final String FIELD_DATEDEBUT = "DATEDEBUT";
    public static final String FIELD_DATEFIN = "DATEFIN";
    public static final String FIELD_IDCOMPTEANNEXE = "IDCOMPTEANNEXE";
    public static final String FIELD_IDMOTIFBLOCAGE = "IDMOTIFBLOCAGE";
    public static final String FIELD_IDMOTIFCONTENTIEUX = "IDMOTIFCONT";
    public static final String FIELD_IDSECTION = "IDSECTION";
    public static final String TABLE_CAMOCOP = "CAMOCOP";

    private String commentaire = new String();
    private String dateDebut = new String();
    private String dateFin = new String();
    private String idCompteAnnexe = new String();
    private String idMotifBlocage = new String();
    private String idMotifContentieux = new String();
    private String idSection = new String();
    private FWParametersUserCode ucMotifContentieuxSuspendu = null;

    /**
     * Constructeur par défaut
     */
    public CAMotifContentieux() {
        super();
    }

    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        // incrémente le prochain numéro
        setIdMotifContentieux(this._incCounter(transaction, idMotifContentieux));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return CAMotifContentieux.TABLE_CAMOCOP;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idMotifContentieux = statement.dbReadNumeric(CAMotifContentieux.FIELD_IDMOTIFCONTENTIEUX);
        idCompteAnnexe = statement.dbReadNumeric(CAMotifContentieux.FIELD_IDCOMPTEANNEXE);
        idSection = statement.dbReadNumeric(CAMotifContentieux.FIELD_IDSECTION);
        idMotifBlocage = statement.dbReadNumeric(CAMotifContentieux.FIELD_IDMOTIFBLOCAGE);
        dateDebut = statement.dbReadDateAMJ(CAMotifContentieux.FIELD_DATEDEBUT);
        dateFin = statement.dbReadDateAMJ(CAMotifContentieux.FIELD_DATEFIN);
        commentaire = statement.dbReadString(CAMotifContentieux.FIELD_COMMENTAIRE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

        _propertyMandatory(statement.getTransaction(), getIdMotifContentieux(),
                getSession().getLabel("MOTIF_BLOCAGE_ID_RENS"));
        // La date de début doit être saisie
        if (JAUtil.isDateEmpty(getDateDebut())) {
            _addError(statement.getTransaction(), getSession().getLabel("7352"));
        }
        _checkDate(statement.getTransaction(), getDateDebut(), getSession().getLabel("7352"));
        // La date de fin doit être saisie
        if (JAUtil.isDateEmpty(getDateFin())) {
            _addError(statement.getTransaction(), getSession().getLabel("7353"));
        }
        _checkDate(statement.getTransaction(), getDateFin(), getSession().getLabel("7353"));
        // Contrôler que la date de début soit plus petite que la date de fin
        int res;
        JACalendarGregorian calDate = new JACalendarGregorian();
        try {
            res = calDate.compare(getDateDebut(), getDateFin());
            if (res > 1) {
                _addError(statement.getTransaction(), getSession().getLabel("7370"));
            }
        } catch (Exception e) {
        }
        // Le motif de blocage doit être saisie
        if (JadeStringUtil.isIntegerEmpty(getIdMotifBlocage())) {
            _addError(statement.getTransaction(), getSession().getLabel("7090"));
        }

        if (!JadeStringUtil.isIntegerEmpty(getIdSection())) {
            CASection section = new CASection();
            section.setSession(getSession());
            section.setIdSection(getIdSection());
            section.retrieve(statement.getTransaction());
            if (!section.getContentieuxEstSuspendu().booleanValue()) {
                _addError(statement.getTransaction(), getSession().getLabel("SECTION_DOIT_ETRE_BLOQUEE"));
            }
        } else if (!JadeStringUtil.isIntegerEmpty(getIdCompteAnnexe())) {
            CACompteAnnexe compteAnnexe = new CACompteAnnexe();
            compteAnnexe.setSession(getSession());
            compteAnnexe.setIdCompteAnnexe(getIdCompteAnnexe());
            compteAnnexe.retrieve(statement.getTransaction());
            if (!compteAnnexe.getContEstBloque().booleanValue()) {
                _addError(statement.getTransaction(), getSession().getLabel("COMPTE_ANNEXE_DOIT_ETRE_BLOQUE"));
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(CAMotifContentieux.FIELD_IDMOTIFCONTENTIEUX,
                this._dbWriteNumeric(statement.getTransaction(), getIdMotifContentieux(), ""));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(CAMotifContentieux.FIELD_IDMOTIFCONTENTIEUX,
                this._dbWriteNumeric(statement.getTransaction(), getIdMotifContentieux(), "idMotCont"));
        statement.writeField(CAMotifContentieux.FIELD_IDCOMPTEANNEXE,
                this._dbWriteNumeric(statement.getTransaction(), getIdCompteAnnexe(), "idCompteAnnexe"));
        statement.writeField(CAMotifContentieux.FIELD_IDSECTION,
                this._dbWriteNumeric(statement.getTransaction(), getIdSection(), "idSection"));
        statement.writeField(CAMotifContentieux.FIELD_IDMOTIFBLOCAGE,
                this._dbWriteNumeric(statement.getTransaction(), getIdMotifBlocage(), "idMotifBlocage"));
        statement.writeField(CAMotifContentieux.FIELD_DATEDEBUT,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateDebut(), "dateDebut"));
        statement.writeField(CAMotifContentieux.FIELD_DATEFIN,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateFin(), "dateFin"));
        statement.writeField(CAMotifContentieux.FIELD_COMMENTAIRE,
                this._dbWriteString(statement.getTransaction(), getCommentaire(), "commentaire"));
    }

    /**
     * Retourne le commentaire du motif de blocage
     * 
     * @return String Commentaire
     */
    public String getCommentaire() {
        return commentaire;
    }

    /**
     * Retourne la date de début du blocage
     * 
     * @return String dateDebut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * Retourne la date de fin du blocage
     * 
     * @return String dateFin
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * Retourne l'id du compte annexe concerné par le blocage Si le motif concerne le compte annexe, sinon vide
     * 
     * @return String idCompteAnnexe
     */
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * Retourne l'id du motif de blocage (Code système)
     * 
     * @return String idMotifBlocage
     */
    public String getIdMotifBlocage() {
        return idMotifBlocage;
    }

    /**
     * Retourne l'id du motif de blocage du contentieux (PK)
     * 
     * @return String idMotifContentieux
     */
    public String getIdMotifContentieux() {
        return idMotifContentieux;
    }

    /**
     * Retourne l'id de la section concernée par le blocage Si motif concerne la section , sinon vide
     * 
     * @return String idSection
     */
    public String getIdSection() {
        return idSection;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2002 13:47:48)
     * 
     * @return globaz.globall.parameters.FWParametersUserCode
     */
    public FWParametersUserCode getUcMotifContentieuxSuspendu() {
        if (ucMotifContentieuxSuspendu == null) {
            // liste pas encore chargee, on la charge
            ucMotifContentieuxSuspendu = new FWParametersUserCode();
            ucMotifContentieuxSuspendu.setSession(getSession());
        }
        if (!JadeStringUtil.isIntegerEmpty(getIdMotifBlocage())) {
            // Récupérer le code système dans la langue de l'utilisateur
            ucMotifContentieuxSuspendu.setIdCodeSysteme(getIdMotifBlocage());
            ucMotifContentieuxSuspendu.setIdLangue(getSession().getIdLangue());
            try {
                ucMotifContentieuxSuspendu.retrieve();
                if (ucMotifContentieuxSuspendu.isNew()) {
                    _addError(null, getSession().getLabel("7324"));
                }
            } catch (Exception e) {
                _addError(null, getSession().getLabel("7324"));
            }
        }
        return ucMotifContentieuxSuspendu;
    }

    /**
     * Set le commentaire du motif de blocage du contentieux
     * 
     * @param String
     *            commentaire
     */
    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    /**
     * Set la date de début de blocage
     * 
     * @param String
     *            dateDebut
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * Set la date de fin de blocage
     * 
     * @param String
     *            dateFin
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * Set l'id du compte annexe concerné par le blocage, sinon vide
     * 
     * @param String
     *            idCompteAnnexe
     */
    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    /**
     * Set l'id de motif de blocage
     * 
     * @param String
     *            idMotifBlocage
     */
    public void setIdMotifBlocage(String idMotifBlocage) {
        this.idMotifBlocage = idMotifBlocage;
    }

    /**
     * Set l'id du motif de contentieux (PK)
     * 
     * @param String
     *            idMotifContentieux
     */
    public void setIdMotifContentieux(String idMotifContentieux) {
        this.idMotifContentieux = idMotifContentieux;
    }

    /**
     * Set l'id de la section concernée par le blocage, sinon vide
     * 
     * @param String
     *            idSection
     */
    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

}
