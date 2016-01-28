package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.hercule.service.CEAffiliationService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import java.math.BigDecimal;

/**
 * <H1>Attributions des points pour le contrôle employeur</H1>
 * 
 * @author jpa
 * @since Créé le 12 févr. 07
 */
public class CEAttributionPts extends BEntity {

    private static final long serialVersionUID = -367079893232276679L;

    /** Clé alternée sur l'identifiant externe */
    public final static int AK_NUMAFFILIE = 2;

    public static final String FIELD_ATTRIBUTIONACTIVE = "CEBAAV";

    public static final String FIELD_MODIFICATION_UTILISATEUR = "CEBMUT";
    private final static double PALIER_MASSE_SAL1 = 0.0;

    private final static double PALIER_MASSE_SAL2 = 100000.0;
    private final static double PALIER_MASSE_SAL3 = 500000.0;
    private final static double PALIER_MASSE_SAL4 = 5000000.0;
    public static final String TABLE_CEATTPTS = "CEATTPTS";

    private AFAffiliation _affiliation = null;
    private CEControleEmployeur _lastControle = null;
    private String anneePrevue = "";
    private String collaboration = "";
    private String collaborationCom = "";
    private String commentaires = "";
    private String criteresEntreprise = "";
    private String criteresEntrepriseCom = "";
    private String delaiControle = "";
    private String derniereRevision = "";
    private String derniereRevisionCom = "";
    // private String idAffiliation = "";
    private String idAttributionPts = "";
    private String idTiers = "";
    private Boolean isAttributionActive = new Boolean(false);
    private Boolean isModificationUtilisateur = new Boolean(false);
    private String lastModification = "";
    private String lastUser = "";
    private String masseAC = "";
    private String masseAC2 = "";
    private String masseAF = "";
    private String masseAutres = "";
    // private Boolean isCasSpecial;
    private String masseAvs = "";
    private String masseSalariale = "";
    private String nbreEcritures = "";
    private String nbrePoints = "";
    private String nom = "";
    private String numAffilie = "";
    private String observations = "";
    private String periodeDebut = "";
    private String periodeFin = "";
    private String prevuManuellement = "";
    private String prevuManuellementCom = "";
    private String qualiteRH = "";
    private String qualiteRHCom = "";

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdAttributionPts(this._incCounter(transaction, idAttributionPts));
    }

    /**
     * Récupérer le dernier contrôle d'employeur
     * 
     * @return
     */
    public CEControleEmployeur _findLastControle() {
        // Récupérer du dernier contrôle
        if (!JadeStringUtil.isEmpty(getNumAffilie())) {
            if (_lastControle == null) {
                // Contrôle d'employeur
                _lastControle = new CEControleEmployeur();
                _lastControle.setSession(getSession());
                // Si numéro d'affilié ouvert
                CEControleEmployeurManager manager = new CEControleEmployeurManager();
                manager.setSession(getSession());
                manager.setForNumAffilie(getNumAffilie());
                // Date effective la plus récente
                manager.setOrderBy("MDDEFF DESC");
                try {
                    manager.find();
                    if (!manager.isEmpty()) {
                        _lastControle = (CEControleEmployeur) manager.getFirstEntity();
                    }
                } catch (Exception e) {
                    return _lastControle;
                }
            }
        }
        return _lastControle;
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return CEAttributionPts.TABLE_CEATTPTS;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        numAffilie = statement.dbReadString("MALNAF");
        idAttributionPts = statement.dbReadNumeric("MPAPID");
        derniereRevision = statement.dbReadNumeric("MPDREV");
        derniereRevisionCom = statement.dbReadString("MPDREC");
        qualiteRH = statement.dbReadNumeric("MPQURH");
        qualiteRHCom = statement.dbReadString("MPQURC");
        collaboration = statement.dbReadNumeric("MPCOLL");
        collaborationCom = statement.dbReadString("MPCOLC");
        criteresEntreprise = statement.dbReadNumeric("MPCREN");
        criteresEntrepriseCom = statement.dbReadString("MPCREC");
        observations = statement.dbReadString("MPOBSE");
        nbreEcritures = statement.dbReadNumeric("MPNBEC");
        // isCasSpecial = statement.dbReadBoolean("MPCSPE");
        masseAvs = statement.dbReadNumeric("MPMAVS");
        masseAF = statement.dbReadNumeric("MPMAAF");
        masseAC = statement.dbReadNumeric("MPMAAC");
        masseAC2 = statement.dbReadNumeric("MPMAA2");
        masseAutres = statement.dbReadNumeric("MPMASA");
        nbrePoints = statement.dbReadNumeric("MPNBPT");
        delaiControle = statement.dbReadNumeric("MPDECO");
        periodeDebut = statement.dbReadDateAMJ("MPPEDE");
        periodeFin = statement.dbReadDateAMJ("MPPEFI");
        anneePrevue = statement.dbReadNumeric("MPAPRE");
        prevuManuellement = statement.dbReadNumeric("MPPRMA");
        prevuManuellementCom = statement.dbReadString("MPPRMC");
        lastUser = statement.dbReadString("MPLUSR");
        lastModification = statement.dbReadString("MPLMOD");
        commentaires = statement.dbReadString("MPCOMM");
        masseSalariale = statement.dbReadString("MPMASS");
        isAttributionActive = statement.dbReadBoolean(CEAttributionPts.FIELD_ATTRIBUTIONACTIVE);
        isModificationUtilisateur = statement.dbReadBoolean(CEAttributionPts.FIELD_MODIFICATION_UTILISATEUR);
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        if (JadeStringUtil.isEmpty(getNumAffilie())) {
            _addError(statement.getTransaction(), getSession().getLabel("ATTRIBUTION_PTS_PAS_NUM_AFFILIE"));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_writeAlternateKey(globaz.globall.db.BStatement, int)
     */
    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        if (alternateKey == CEAttributionPts.AK_NUMAFFILIE) {
            statement.writeKey("MALNAF", this._dbWriteString(statement.getTransaction(), getNumAffilie(), "MALNAF"));
            statement.writeKey("MPPEDE", this._dbWriteDateAMJ(statement.getTransaction(), getPeriodeDebut(), "MPPEDE"));
            statement.writeKey("MPPEFI", this._dbWriteDateAMJ(statement.getTransaction(), getPeriodeFin(), "MPPEFI"));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("MPAPID", this._dbWriteNumeric(statement.getTransaction(), getIdAttributionPts(), ""));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("MALNAF", this._dbWriteString(statement.getTransaction(), getNumAffilie(), "numAffilie"));
        statement.writeField("MPAPID",
                this._dbWriteNumeric(statement.getTransaction(), getIdAttributionPts(), "idAttributionPts"));
        statement.writeField("MPDREV",
                this._dbWriteNumeric(statement.getTransaction(), getDerniereRevision(), "derniereRevision"));
        statement.writeField("MPDREC",
                this._dbWriteString(statement.getTransaction(), getDerniereRevisionCom(), "derniereRevisionCom"));
        statement.writeField("MPQURH", this._dbWriteNumeric(statement.getTransaction(), getQualiteRH(), "qualiteRH"));
        statement.writeField("MPQURC",
                this._dbWriteString(statement.getTransaction(), getQualiteRHCom(), "qualiteRHCom"));
        statement.writeField("MPCOLL",
                this._dbWriteNumeric(statement.getTransaction(), getCollaboration(), "collaboration"));
        statement.writeField("MPCOLC",
                this._dbWriteString(statement.getTransaction(), getCollaborationCom(), "collaborationCom"));
        statement.writeField("MPCREN",
                this._dbWriteNumeric(statement.getTransaction(), getCriteresEntreprise(), "criteresEntreprise"));
        statement.writeField("MPCREC",
                this._dbWriteString(statement.getTransaction(), getCriteresEntrepriseCom(), "criteresEntrepriseCom"));
        statement.writeField("MPOBSE",
                this._dbWriteString(statement.getTransaction(), getObservations(), "observations"));
        // statement.writeField("MPCSPE",
        // _dbWriteBoolean(statement.getTransaction(), getIsCasSpecial(),
        // BConstants.DB_TYPE_BOOLEAN_CHAR, "isCasSpecial"));
        statement.writeField("MPNBEC",
                this._dbWriteNumeric(statement.getTransaction(), getNbreEcritures(), "nbreEcritures"));
        statement.writeField("MPMAVS", this._dbWriteNumeric(statement.getTransaction(), getMasseAvs(), "masseAvs"));
        statement.writeField("MPMAAF", this._dbWriteNumeric(statement.getTransaction(), getMasseAF(), "masseAF"));
        statement.writeField("MPMAAC", this._dbWriteNumeric(statement.getTransaction(), getMasseAC(), "masseAC"));
        statement.writeField("MPMAA2", this._dbWriteNumeric(statement.getTransaction(), getMasseAC2(), "masseAC2"));
        statement.writeField("MPMASA",
                this._dbWriteNumeric(statement.getTransaction(), getMasseAutres(), "masseAutres"));
        statement.writeField("MPNBPT", this._dbWriteNumeric(statement.getTransaction(), getNbrePoints(), "nbrePoints"));
        statement.writeField("MPDECO",
                this._dbWriteNumeric(statement.getTransaction(), getDelaiControle(), "delaiControle"));
        statement.writeField("MPPEDE",
                this._dbWriteDateAMJ(statement.getTransaction(), getPeriodeDebut(), "periodeDebut"));
        statement.writeField("MPPEFI", this._dbWriteDateAMJ(statement.getTransaction(), getPeriodeFin(), "periodeFin"));
        statement.writeField("MPAPRE",
                this._dbWriteNumeric(statement.getTransaction(), getAnneePrevue(), "anneePrevue"));
        statement.writeField("MPPRMA",
                this._dbWriteNumeric(statement.getTransaction(), getPrevuManuellement(), "prevuManuellement"));
        statement.writeField("MPPRMC",
                this._dbWriteString(statement.getTransaction(), getPrevuManuellementCom(), "prevuManuellementCom"));
        statement.writeField("MPLUSR", this._dbWriteString(statement.getTransaction(), getLastUser(), "lastUser"));
        statement.writeField("MPLMOD",
                this._dbWriteString(statement.getTransaction(), getLastModification(), "lastModification"));
        statement.writeField("MPCOMM",
                this._dbWriteString(statement.getTransaction(), getCommentaires(), "commentaires"));
        statement.writeField("MPMASS",
                this._dbWriteString(statement.getTransaction(), this.getMasseSalariale(), "masseSalariale"));
        statement.writeField(CEAttributionPts.FIELD_ATTRIBUTIONACTIVE, this._dbWriteBoolean(statement.getTransaction(),
                isAttributionActive(), BConstants.DB_TYPE_BOOLEAN_CHAR, "isAttributionActive"));
        statement.writeField(CEAttributionPts.FIELD_MODIFICATION_UTILISATEUR, this._dbWriteBoolean(
                statement.getTransaction(), isModificationUtilisateur(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                "isModificationUtilisateur"));
    }

    @Override
    public Object clone() {

        CEAttributionPts points = new CEAttributionPts();
        points.setAnneePrevue(anneePrevue);
        points.setCollaboration(collaboration);
        points.setCollaborationCom(collaborationCom);
        points.setCommentaires(commentaires);
        points.setCriteresEntreprise(criteresEntreprise);
        points.setCriteresEntrepriseCom(criteresEntrepriseCom);
        points.setDelaiControle(delaiControle);
        points.setDerniereRevision(derniereRevision);
        points.setDerniereRevisionCom(derniereRevisionCom);
        points.setIdTiers(idTiers);
        points.setIsAttributionActive(isAttributionActive);
        points.setIsModificationUtilisateur(isModificationUtilisateur);
        points.setMasseAC(masseAC);
        points.setMasseAC2(masseAC2);
        points.setMasseAF(masseAF);
        points.setMasseAutres(masseAutres);
        points.setMasseAvs(masseAvs);
        points.setMasseSalariale(masseSalariale);
        points.setNbreEcritures(nbreEcritures);
        points.setNbrePoints(nbrePoints);
        points.setNom(nom);
        points.setNumAffilie(numAffilie);
        points.setObservations(observations);
        points.setPeriodeDebut(periodeDebut);
        points.setPeriodeFin(periodeFin);
        points.setPrevuManuellement(prevuManuellement);
        points.setPrevuManuellementCom(prevuManuellementCom);
        points.setQualiteRH(qualiteRH);
        points.setQualiteRHCom(qualiteRHCom);
        points.setLastUser(lastUser);
        points.setLastModification(lastModification);

        return points;
    }

    public String getAnneeDebutAffiliation() {
        if (!JadeStringUtil.isEmpty(getNumAffilie())) {

            if (_affiliation == null) {
                try {
                    _affiliation = CEAffiliationService.findAffilie(getSession(), getNumAffilie(),
                            getDateDebutControle(), getDateFinControle());
                } catch (Exception e) {
                    JadeLogger.warn(this, e);
                }
            }
            if (_affiliation != null) {
                return _affiliation.getDateDebut().substring(6, 10);
            }
        }

        return "";
    }

    public String getAnneeFinAffiliation() {
        if (!JadeStringUtil.isEmpty(getNumAffilie())) {

            if (_affiliation == null) {
                try {
                    _affiliation = CEAffiliationService.findAffilie(getSession(), getNumAffilie(),
                            getDateDebutControle(), getDateFinControle());
                } catch (Exception e) {
                    JadeLogger.warn(this, e);
                }
            }
            if ((_affiliation != null) && !JadeStringUtil.isEmpty(_affiliation.getDateFin())) {
                return _affiliation.getDateFin().substring(6, 10);
            }
        }
        return "";
    }

    public String getAnneePrecedente() {
        CEControleEmployeur controle = _findLastControle();
        if ((controle != null) && !JadeStringUtil.isEmpty(controle.getDateEffective())) {
            return controle.getDateEffective().substring(6, 10);
        } else {
            return "";
        }
    }

    public String getAnneePrevue() {
        return anneePrevue;
    }

    public int getCategorie(double cumulMasse) {
        if (cumulMasse <= CEAttributionPts.PALIER_MASSE_SAL1) {
            return 0;
        } else if ((CEAttributionPts.PALIER_MASSE_SAL2 > cumulMasse)
                && (cumulMasse > CEAttributionPts.PALIER_MASSE_SAL1)) {
            return 1;
        } else if ((CEAttributionPts.PALIER_MASSE_SAL3 > cumulMasse)
                && (cumulMasse >= CEAttributionPts.PALIER_MASSE_SAL2)) {
            return 2;
        } else if ((CEAttributionPts.PALIER_MASSE_SAL4 > cumulMasse)
                && (cumulMasse >= CEAttributionPts.PALIER_MASSE_SAL3)) {
            return 3;
        } else if (cumulMasse > CEAttributionPts.PALIER_MASSE_SAL4) {
            return 4;
        } else {
            return 0;
        }
    }

    public String getCategorieLibelle(String annee) {
        return "";// +getCategorie(getMasseSalariale(annee));
    }

    public String getCollaboration() {
        return collaboration;
    }

    public String getCollaborationCom() {
        return collaborationCom;
    }

    public String getCommentaires() {
        return commentaires;
    }

    public String getCriteresEntreprise() {
        return criteresEntreprise;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getCriteresEntrepriseCom() {
        return criteresEntrepriseCom;
    }

    public String getDateDebutControle() {
        CEControleEmployeur controle = _findLastControle();
        if ((controle != null) && !JadeStringUtil.isEmpty(controle.getDateDebutControle())) {
            return controle.getDateDebutControle();
        } else {
            return "";
        }
    }

    public String getDateFinControle() {
        CEControleEmployeur controle = _findLastControle();
        if ((controle != null) && !JadeStringUtil.isEmpty(controle.getDateFinControle())) {
            return controle.getDateFinControle();
        } else {
            return "";
        }
    }

    public String getDelaiControle() {
        return delaiControle;
    }

    public String getDerniereRevision() {
        return derniereRevision;
    }

    public String getDerniereRevisionCom() {
        return derniereRevisionCom;
    }

    public String getGenreControle() {
        CEControleEmployeur controle = _findLastControle();
        if ((controle != null) && !JadeStringUtil.isEmpty(controle.getGenreControle())) {
            return getSession().getCodeLibelle(controle.getGenreControle());
        } else {
            return "";
        }
    }

    public String getIdAttributionPts() {
        return idAttributionPts;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getLastModification() {
        return lastModification;
    }

    public String getLastUser() {
        return lastUser;
    }

    public String getMasseAC() {
        return masseAC;
    }

    public String getMasseAC2() {
        return masseAC2;
    }

    public String getMasseAF() {
        return masseAF;
    }

    public String getMasseAutres() {
        return masseAutres;
    }

    public String getMasseAvs() {
        return masseAvs;
    }

    public String getMasseSalariale() {
        return masseSalariale;
    }

    public BigDecimal getMasseSalariale(String annee) {
        BigDecimal value;
        BigDecimal zeroValue = new BigDecimal(0.0);

        if (!JadeStringUtil.isEmpty(getNumAffilie())) {
            annee = JACalendar.todayJJsMMsAAAA().substring(6, 10);
            int anneeInteger = Integer.parseInt(annee);
            annee = String.valueOf(--anneeInteger);
            String anneePrecedente = String.valueOf(--anneeInteger);
            CEAttributionPtsCumulMasseManager manager = new CEAttributionPtsCumulMasseManager();
            manager.setSession(getSession());
            manager.setForAnnee(annee);
            manager.setForNumAffilie(getNumAffilie());
            try {
                manager.find();
                if (!manager.isEmpty()) {

                    CEAttributionPtsCumulMasse entity = (CEAttributionPtsCumulMasse) manager.getFirstEntity();
                    value = new BigDecimal(entity.getCumulMasse());
                    return value;

                } else {
                    manager.setForAnnee(anneePrecedente);
                    manager.find();
                    if (!manager.isEmpty()) {
                        CEAttributionPtsCumulMasse entity = (CEAttributionPtsCumulMasse) manager.getFirstEntity();
                        value = new BigDecimal(entity.getCumulMasse());
                        return value;
                    }
                }
            } catch (Exception e) {
                return zeroValue;
            }
        }
        return zeroValue;
    }

    public double getMasseSalarialeBCK(String annee) {
        if (!(JadeStringUtil.isEmpty(annee) || JadeStringUtil.isEmpty(getNumAffilie()))) {
            annee = getDateFinControle().substring(6, 10);
            CEAttributionPtsCumulMasseManager manager = new CEAttributionPtsCumulMasseManager();
            manager.setSession(getSession());
            manager.setForAnnee(annee);
            manager.setForNumAffilie(getNumAffilie());
            try {
                manager.find();
                if (manager.isEmpty()) {
                    CEAttributionPtsCumulMasse entity = (CEAttributionPtsCumulMasse) manager.getFirstEntity();
                    return Double.valueOf(entity.getCumulMasse()).doubleValue();
                }
                return 0;
            } catch (Exception e) {
                return 0;
            }
        } else {
            // On prend la masse salariale de l'année précedente
            if (!JadeStringUtil.isEmpty(getNumAffilie())) {
                int value = Integer.valueOf(JACalendar.todayJJsMMsAAAA().substring(6, 10)).intValue() - 1;
                annee = String.valueOf(value);
                CEAttributionPtsCumulMasseManager manager = new CEAttributionPtsCumulMasseManager();
                manager.setSession(getSession());
                manager.setForAnnee(annee);
                manager.setForNumAffilie(getNumAffilie());
                try {
                    manager.find();
                    if (manager.isEmpty()) {
                        CEAttributionPtsCumulMasse entity = (CEAttributionPtsCumulMasse) manager.getFirstEntity();
                        return Double.valueOf(entity.getCumulMasse()).doubleValue();
                    }
                    return 0;
                } catch (Exception e) {
                    return 0;
                }
            }
            return 0;
        }
    }

    public String getMasseSalarialeLibelle(String annee) {
        if (JadeStringUtil.isEmpty("" + this.getMasseSalariale(annee))
                || ("" + this.getMasseSalariale(annee)).equalsIgnoreCase("0.0")) {
            return masseSalariale;
        } else {
            return "" + this.getMasseSalariale(annee);
        }
    }

    public String getNbreEcritures() {
        return nbreEcritures;
    }

    public String getNbrePoints() {
        return nbrePoints;
    }

    public String getNom() {
        return nom;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public String getObservations() {
        return observations;
    }

    public String getPeriodeDebut() {
        return periodeDebut;
    }

    public String getPeriodeFin() {
        return periodeFin;
    }

    public String getPrevuManuellement() {
        return prevuManuellement;
    }

    public String getPrevuManuellementCom() {
        return prevuManuellementCom;
    }

    public String getQualiteRH() {
        return qualiteRH;
    }

    public String getQualiteRHCom() {
        return qualiteRHCom;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public Boolean isAttributionActive() {
        return isAttributionActive;
    }

    public Boolean isModificationUtilisateur() {
        return isModificationUtilisateur;
    }

    public void setAnneePrevue(String anneePrevue) {
        this.anneePrevue = anneePrevue;
    }

    public void setCollaboration(String collaboration) {
        this.collaboration = collaboration;
    }

    public void setCollaborationCom(String collaborationCom) {
        this.collaborationCom = collaborationCom;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }

    public void setCriteresEntreprise(String criteresEntreprise) {
        this.criteresEntreprise = criteresEntreprise;
    }

    public void setCriteresEntrepriseCom(String criteresEntrepriseCom) {
        this.criteresEntrepriseCom = criteresEntrepriseCom;
    }

    public void setDelaiControle(String delaiControle) {
        this.delaiControle = delaiControle;
    }

    public void setDerniereRevision(String derniereRevision) {
        this.derniereRevision = derniereRevision;
    }

    public void setDerniereRevisionCom(String derniereRevisionCom) {
        this.derniereRevisionCom = derniereRevisionCom;
    }

    public void setIdAttributionPts(String idAttributionPts) {
        this.idAttributionPts = idAttributionPts;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIsAttributionActive(Boolean isAttributionActive) {
        this.isAttributionActive = isAttributionActive;
    }

    public void setIsModificationUtilisateur(Boolean isModificationUtilisateur) {
        this.isModificationUtilisateur = isModificationUtilisateur;
    }

    public void setLastModification(String lastModification) {
        this.lastModification = lastModification;
    }

    public void setLastUser(String lastUser) {
        this.lastUser = lastUser;
    }

    public void setMasseAC(String masseAC) {
        this.masseAC = masseAC;
    }

    public void setMasseAC2(String masseAC2) {
        this.masseAC2 = masseAC2;
    }

    public void setMasseAF(String masseAF) {
        this.masseAF = masseAF;
    }

    public void setMasseAutres(String masseAutres) {
        this.masseAutres = masseAutres;
    }

    public void setMasseAvs(String masseAvs) {
        this.masseAvs = masseAvs;
    }

    public void setMasseSalariale(String masseSalariale) {
        this.masseSalariale = masseSalariale;
    }

    public void setNbreEcritures(String nbreEcritures) {
        this.nbreEcritures = nbreEcritures;
    }

    public void setNbrePoints(String nbrePoints) {
        this.nbrePoints = nbrePoints;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
        _lastControle = null;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public void setPeriodeDebut(String periodeDebut) {
        this.periodeDebut = periodeDebut;
    }

    public void setPeriodeFin(String periodeFin) {
        this.periodeFin = periodeFin;
    }

    public void setPrevuManuellement(String prevuManuellement) {
        this.prevuManuellement = prevuManuellement;
    }

    public void setPrevuManuellementCom(String prevuManuellementCom) {
        this.prevuManuellementCom = prevuManuellementCom;
    }

    public void setQualiteRH(String qualiteRH) {
        this.qualiteRH = qualiteRH;
    }

    public void setQualiteRHCom(String qualiteRHCom) {
        this.qualiteRHCom = qualiteRHCom;
    }
}
