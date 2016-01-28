/*
 * Créé le 8 sept. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.db.famille;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAUtil;
import globaz.hera.api.ISFPeriode;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.enums.TypeDeDetenteur;
import globaz.hera.vb.famille.SFPeriodeVO;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Calendar;

/**
 * 
 * @author mmu
 */
public class SFPeriode extends BEntity implements ISFPeriode {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_DATEDEBUT = "WHDDEB";
    public static final String FIELD_DATEFIN = "WHDFIN";
    public static final String FIELD_IDDETENTEURBTE = "WHIDBT";
    public static final String FIELD_IDMEMBREFAMILLE = "WHIDMF";
    public static final String FIELD_IDPERIODE = "WHIPER";
    public static final String FIELD_PAYS = "WHTPAY";
    public static final String FIELD_TYPE = "WHTTYP";
    public static final String FIELD_TYPE_DE_DETENTEUR = "WHTYDE";

    public static final String TABLE_NAME = "SFPERIOD";

    private String dateDebut = "";
    private String dateFin = "";
    private String idDetenteurBTE = "";
    private String idMembreFamille = "";
    private String idPeriode = "";
    // Cache
    private SFMembreFamille membreFamille = null;
    private SFMembreFamille membreFamilleBTE = null;

    private String pays = "";
    private String type = "";
    private String csTypeDeDetenteur;

    public SFPeriode() {
        super();
    }

    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        setIdPeriode(this._incCounter(transaction, "0"));
    }

    @Override
    protected String _getTableName() {
        return SFPeriode.TABLE_NAME;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idPeriode = statement.dbReadNumeric(SFPeriode.FIELD_IDPERIODE);
        idMembreFamille = statement.dbReadNumeric(SFPeriode.FIELD_IDMEMBREFAMILLE);
        type = statement.dbReadNumeric(SFPeriode.FIELD_TYPE);
        dateDebut = statement.dbReadDateAMJ(SFPeriode.FIELD_DATEDEBUT);
        dateFin = statement.dbReadDateAMJ(SFPeriode.FIELD_DATEFIN);
        pays = statement.dbReadNumeric(SFPeriode.FIELD_PAYS);
        idDetenteurBTE = statement.dbReadNumeric(SFPeriode.FIELD_IDDETENTEURBTE);
        csTypeDeDetenteur = statement.dbReadNumeric(SFPeriode.FIELD_TYPE_DE_DETENTEUR);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Le type de période doit être renseigné
        _propertyMandatory(statement.getTransaction(), type, getSession().getLabel("VALIDATE_TYPE_PERIODE_OBLIGATOIRE"));
        // L'idMemmbreFamille doit être renseigné
        _propertyMandatory(statement.getTransaction(), idMembreFamille,
                getSession().getLabel("VALIDATE_ID_MEMBRE_FAMILLE_OBLIGATOIRE"));

        // Pour les périodes de type Assurance étrangère, le pays doit être renseigné
        if (type.equals(ISFSituationFamiliale.CS_TYPE_PERIODE_ASSURANCE_ETRANGERE)) {
            _propertyMandatory(statement.getTransaction(), pays, getSession().getLabel("VALIDATE_PAYS_OBLIGATOIRE"));
        }

        if (type.equals(ISFSituationFamiliale.CS_TYPE_PERIODE_GARDE_BTE)) {

            // Pour les périodes de type Garde_BTE, le type de détenteur doit être renseigné
            _propertyMandatory(statement.getTransaction(), csTypeDeDetenteur,
                    getSession().getLabel("VALIDATE_TYPE_DETENTEUR_MEMBRE_BTE_OBLIGATOIRE"));

            // Si le type de détenteur est la famille, une membre de la famille doit être renseigné
            if (TypeDeDetenteur.FAMILLE.getCodeSystemAsString().equals(csTypeDeDetenteur)) {
                _propertyMandatory(statement.getTransaction(), idDetenteurBTE,
                        getSession().getLabel("VALIDATE_ID_MEMBRE_BTE_OBLIGATOIRE_TYPE_FAMILLE"));
            }

            // Si TIERS est sélectionné il ne doit pas y avoir de valeur dans IdDetenteurBTE
            else if (TypeDeDetenteur.TIERS.getCodeSystemAsString().equals(csTypeDeDetenteur)) {
                if (!JadeStringUtil.isBlankOrZero(idDetenteurBTE)) {
                    _addError(getSession().getCurrentThreadTransaction(),
                            getSession().getLabel("VALIDATE_DETENTEUR_VIDE_SI_TYPE_DETENTEUR_TIERS"));
                }
                // else {
                // _addError(getSession().getCurrentThreadTransaction(),
                // getSession().getLabel("VALIDATE_TYPE_DETENTEUR_VALEUR_INVALIDE"));
                // }

            }

            // _propertyMandatory(statement.getTransaction(), idDetenteurBTE,
            // getSession().getLabel("VALIDATE_ID_MEMBRE_BTE_OBLIGATOIRE"));
        }

        // pour les périodes de type Certificat de vie, seule la date de fin doit être renseignée
        if (type.equals(ISFSituationFamiliale.CS_TYPE_PERIODE_CERTIFICAT_VIE)) {
            if (JAUtil.isDateEmpty(dateFin)) {
                _addError(statement.getTransaction(),
                        getSession().getLabel("VALIDATE_DATE_FIN_OBLIGATOIRE_CERTIFICAT_VIE"));
            } else {
                // la date de fin doit être plus récente que le dernier jour du mois précédent
                Calendar lastMonth = Calendar.getInstance();
                lastMonth.add(Calendar.MONTH, -1);

                StringBuilder lastDayOfTheMonth = new StringBuilder();
                lastDayOfTheMonth.append(lastMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
                lastDayOfTheMonth.append(".");
                int month = lastMonth.get(Calendar.MONTH) + 1; // les mois sont stockés de 0 à 11
                if (month < 10) {
                    lastDayOfTheMonth.append("0");
                }
                lastDayOfTheMonth.append(month);
                lastDayOfTheMonth.append(".");
                lastDayOfTheMonth.append(lastMonth.get(Calendar.YEAR));

                if (!JadeDateUtil.isDateBefore(lastDayOfTheMonth.toString(), dateFin)) {
                    _addError(statement.getTransaction(),
                            getSession().getLabel("VALIDATE_DATE_FIN_PLUS_RECENTE_DERNIER_MOIS_CERTIFICAT_VIE"));
                }
            }

            if (!JAUtil.isDateEmpty(dateDebut) || !JadeStringUtil.isBlank(idDetenteurBTE)
                    || !JadeStringUtil.isBlank(pays)) {
                _addError(statement.getTransaction(),
                        getSession().getLabel("VALIDATE_DATE_FIN_SEULEMENT_CERTIFICAT_VIE"));
            }
        }

        // La date de début doit être dans un bon format
        if (!JAUtil.isDateEmpty(dateDebut)) {
            _checkDate(statement.getTransaction(), dateDebut, "DATE_INVALIDE");
        }

        // La date de fin doit être postérieure à la date de début
        if (!JAUtil.isDateEmpty(dateFin)) {
            if (BSessionUtil.compareDateFirstGreater(getSession(), dateDebut, dateFin)) {
                _addError(statement.getTransaction(), getSession().getLabel("VALIDATE_DATE_FIN_AVANT_DEBUT"));
            }
        }

        // Si la période est de type incarcération, une date de début doit être présente
        if (type.equals(ISFSituationFamiliale.CS_TYPE_PERIODE_INCARCERATION)) {
            if (JAUtil.isDateEmpty(dateDebut)) {
                _addError(statement.getTransaction(), getSession().getLabel("ERROR_DATE_DEBUT_OBLIGATOIRE"));
            }
        }
        // Une date de début ou une date de fin est doit être renseignée
        else if (JAUtil.isDateEmpty(dateDebut) && JAUtil.isDateEmpty(dateFin)) {

            _addError(statement.getTransaction(), getSession().getLabel("ERROR_DATE_OBLIGATOIRE"));
        }
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(SFPeriode.FIELD_IDPERIODE,
                this._dbWriteNumeric(statement.getTransaction(), idPeriode, "idPeriode"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(SFPeriode.FIELD_IDPERIODE,
                this._dbWriteNumeric(statement.getTransaction(), idPeriode, "idPeriode"));
        statement.writeField(SFPeriode.FIELD_IDMEMBREFAMILLE,
                this._dbWriteNumeric(statement.getTransaction(), idMembreFamille, "idMembreFamille"));
        statement.writeField(SFPeriode.FIELD_TYPE, this._dbWriteNumeric(statement.getTransaction(), type, "type"));
        statement.writeField(SFPeriode.FIELD_DATEDEBUT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDebut, "dateDebut"));
        statement.writeField(SFPeriode.FIELD_DATEFIN,
                this._dbWriteDateAMJ(statement.getTransaction(), dateFin, "dateFin"));
        statement.writeField(SFPeriode.FIELD_PAYS, this._dbWriteNumeric(statement.getTransaction(), pays, "pays"));
        statement.writeField(SFPeriode.FIELD_IDDETENTEURBTE,
                this._dbWriteNumeric(statement.getTransaction(), idDetenteurBTE, "idDetenteurBTE"));
        statement.writeField(SFPeriode.FIELD_TYPE_DE_DETENTEUR,
                this._dbWriteNumeric(statement.getTransaction(), csTypeDeDetenteur, "csTypeDeDetenteur"));
    }

    /**
     * getter pour l'attribut date début
     * 
     * @return la valeur courante de l'attribut date début
     */
    @Override
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * getter pour l'attribut date fin
     * 
     * @return la valeur courante de l'attribut date fin
     */
    @Override
    public String getDateFin() {
        return dateFin;
    }

    /**
     * getter pour l'attribut id détenteur BTE
     * 
     * @return la valeur courante de l'attribut id détenteur BTE
     */
    @Override
    public String getIdDetenteurBTE() {
        return idDetenteurBTE;
    }

    public String getIdMembreFamille() {
        return idMembreFamille;
    }

    /**
     * getter pour l'attribut id période
     * 
     * @return la valeur courante de l'attribut id période
     */
    public String getIdPeriode() {
        return idPeriode;
    }

    private SFMembreFamille getMembreFamille() {
        if (membreFamille == null) {
            membreFamille = new SFMembreFamille();
            membreFamille.setSession(getSession());
            membreFamille.setIdMembreFamille(getIdMembreFamille());
            try {
                membreFamille.retrieve();
                if (membreFamille.isNew()) {
                    membreFamille = null;
                }
            } catch (Exception e) {
                membreFamille = null;
            }
        }
        return membreFamille;
    }

    public SFMembreFamille getMembreFamilleBTE() {
        if (ISFSituationFamiliale.CS_TYPE_PERIODE_GARDE_BTE.equals(type)) {
            if (membreFamilleBTE == null) {
                membreFamilleBTE = new SFMembreFamille();
                membreFamilleBTE.setSession(getSession());
                membreFamilleBTE.setIdMembreFamille(getIdDetenteurBTE());
                try {
                    membreFamilleBTE.retrieve();
                    if (membreFamilleBTE.isNew()) {
                        membreFamilleBTE = null;
                    }
                } catch (Exception e) {
                    membreFamilleBTE = null;
                }
            }
            return membreFamilleBTE;
        } else {
            return null;
        }
    }

    /**
     * @return le numéro avs du membre de famille auquel appartient la période ou null en cas d'erreur
     */
    @Override
    public String getNoAvs() {
        if (getMembreFamille() != null) {
            return getMembreFamille().getNss();
        } else {
            return null;
        }
    }

    /**
     * @return le numéro avs du détenteur d'une BTE ou null si le type de période n'est pas une BTE ou en cas d'erreur
     */
    @Override
    public String getNoAvsDetenteurBTE() {
        if (getMembreFamilleBTE() != null) {
            return getMembreFamilleBTE().getNss();
        } else {
            return null;
        }
    }

    /**
     * getter pour l'attribut pays
     * 
     * @return la valeur courante de l'attribut pays
     */
    @Override
    public String getPays() {
        return pays;
    }

    /**
     * getter pour l'attribut type
     * 
     * @return la valeur courante de l'attribut type
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * setter pour l'attribut date début
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
     * setter pour l'attribut id détenteur BTE
     * 
     * @param idDetenteurBTE
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdDetenteurBTE(String idDetenteurBTE) {
        this.idDetenteurBTE = idDetenteurBTE;
    }

    public void setIdMembreFamille(String string) {
        idMembreFamille = string;
    }

    /**
     * setter pour l'attribut id période
     * 
     * @param idPeriode
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdPeriode(String idPeriode) {
        this.idPeriode = idPeriode;
    }

    /**
     * setter pour l'attribut pays
     * 
     * @param Pays
     *            une nouvelle valeur pour cet attribut
     */
    public void setPays(String Pays) {
        pays = Pays;
    }

    /**
     * setter pour l'attribut type
     * 
     * @param Type
     *            une nouvelle valeur pour cet attribut
     */
    public void setType(String Type) {
        type = Type;
    }

    @Override
    public String getCsTypeDeDetenteur() {
        return csTypeDeDetenteur;
    }

    public void setCsTypeDeDetenteur(String csTypeDeDetenteur) {
        this.csTypeDeDetenteur = csTypeDeDetenteur;
    }

    /**
     * Retourne le type énuméré correspondant au code système 'TypeDeDetenteur'
     * 
     * @see TypeDeDetenteur
     * @return le énuméré correspondant au code système 'TypeDeDetenteur' ou null si 'csTypeDeDetenteur' n'est pas
     *         renseigné
     */
    public TypeDeDetenteur getTypeDeDetenteur() {
        for (TypeDeDetenteur type : TypeDeDetenteur.values()) {
            if (type.getCodeSystemAsString().equals(csTypeDeDetenteur)) {
                return type;
            }
        }
        return null;
    }

    public String toMyString() {
        return "SFPeriode [dateDebut=" + dateDebut + ", dateFin=" + dateFin + ", idDetenteurBTE=" + idDetenteurBTE
                + ", idMembreFamille=" + idMembreFamille + ", idPeriode=" + idPeriode + ", membreFamille="
                + membreFamille + ", membreFamilleBTE=" + membreFamilleBTE + ", pays=" + pays + ", type=" + type
                + ", csTypeDeDetenteur=" + csTypeDeDetenteur + "]";
    }

    /**
     * Retourne la SFPeriode sous forme de SFPeriodeVO
     * 
     * @return la SFPeriode sous forme de SFPeriodeVO
     */
    public SFPeriodeVO toValueObject(BSession session) {
        SFPeriodeVO valueObject = new SFPeriodeVO();
        valueObject.setCsTypeDeDetenteur(getCsTypeDeDetenteur());
        valueObject.setDateDebut(getDateDebut());
        valueObject.setDateFin(getDateFin());
        valueObject.setLibellePeriode(session.getCodeLibelle(getType()));
        if (ISFSituationFamiliale.CS_TYPE_PERIODE_ASSURANCE_ETRANGERE.equals(getType())) {
            valueObject.setPays(session.getCodeLibelle(getPays()));
        }
        if (ISFSituationFamiliale.CS_TYPE_PERIODE_GARDE_BTE.equals(getType())) {
            TypeDeDetenteur typeDeDetenteur = getTypeDeDetenteur();
            if (typeDeDetenteur != null) {
                switch (typeDeDetenteur) {
                    case FAMILLE:
                        if (getMembreFamilleBTE() != null) {
                            valueObject.setDetenteurBTE(getMembreFamilleBTE().getNom() + " "
                                    + getMembreFamilleBTE().getPrenom());
                        }
                        break;
                    case TIERS:
                        valueObject.setDetenteurBTE(session.getLabel(TypeDeDetenteur.TIERS.getLabelKey()));
                        break;

                    default:
                        valueObject.setDetenteurBTE(session.getLabel("JSP_DETENTEUR_INCONNU"));
                        break;
                }
            } else {
                valueObject.setDetenteurBTE(session.getLabel("JSP_DETENTEUR_INCONNU"));
            }
        }

        if (ISFSituationFamiliale.CS_TYPE_PERIODE_ENFANT.equals(getType())) {
            TypeDeDetenteur typeDeDetenteur = getTypeDeDetenteur();
            if (typeDeDetenteur != null) {
                if (TypeDeDetenteur.TUTEUR_LEGAL.equals(typeDeDetenteur)) {
                    valueObject.setDetenteurBTE(session.getLabel(TypeDeDetenteur.TUTEUR_LEGAL.getLabelKey()));
                } else {
                    valueObject.setDetenteurBTE(session.getLabel("JSP_DETENTEUR_INCONNU"));
                }
            } else {
                valueObject.setDetenteurBTE("-");
            }
        }
        return valueObject;
    }
}
