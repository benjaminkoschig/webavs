/*
 * Créé le 12 févr. 07
 */
package globaz.naos.db.controleEmployeur;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.translation.CodeSystem;
import java.math.BigDecimal;

/**
 * <H1>Attributions des points pour le contrôle employeur</H1>
 * 
 * @author jpa
 */
public class AFAttributionPts extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final static double PALIER_MASSE_SAL1 = 0.0;
    private final static double PALIER_MASSE_SAL2 = 100000.0;
    private final static double PALIER_MASSE_SAL3 = 500000.0;
    private final static double PALIER_MASSE_SAL4 = 5000000.0;

    private AFControleEmployeur _lastControle = null;
    private String anneePrevue = "";
    private String categorie = "";
    private String collaboration = "";
    private String collaborationCom = "";
    private String commentaires = "";
    private String criteresEntreprise = "";
    private String criteresEntrepriseCom = "";
    private String delaiControle = "";
    private String derniereRevision = "";
    private String derniereRevisionCom = "";
    private String idAttributionPts = "";
    // private String idAffiliation = "";
    private String idControle = "";
    private String idTiers = "";
    private Boolean isCasSpecial;
    private String lastModification = "";
    private String lastUser = "";
    private String masseAC = "";
    private String masseAF = "";
    private String masseAutres = "";
    private String masseAvs = "";
    private String masseSalariale = "";
    private String nbreEcritures = "";
    private String nbrePoints = "";
    private String nom = "";
    private String numAffilie = "";
    private String observations = "";
    private String periodeDebut = "";
    private String periodeFin = "";
    private String periodePrevueDebut = "";
    private String periodePrevueFin = "";
    private String prevuManuellement = "";
    private String prevuManuellementCom = "";
    private String qualiteRH = "";
    private String qualiteRHCom = "";

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
    public AFControleEmployeur _findLastControle() {
        // Récupérer du dernier contrôle
        if (!JadeStringUtil.isEmpty(getNumAffilie())) {
            if (_lastControle == null) {
                // Contrôle d'employeur
                _lastControle = new AFControleEmployeur();
                _lastControle.setSession(getSession());
                // Si numéro d'affilié ouvert
                AFControleEmployeurManager manager = new AFControleEmployeurManager();
                manager.setSession(getSession());
                manager.setForNumAffilie(getNumAffilie());
                // Date effective la plus récente
                manager.setOrderBy("MDDEFF DESC");
                try {
                    manager.find();
                    if (!manager.isEmpty()) {
                        _lastControle = (AFControleEmployeur) manager.getFirstEntity();
                    }
                } catch (Exception e) {
                    return _lastControle;
                }
            }
        }
        return _lastControle;
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "AFATTPTS";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        // idAffiliation = statement.dbReadNumeric("MAIAFF");
        numAffilie = statement.dbReadString("MALNAF");
        idControle = statement.dbReadNumeric("MDICON");
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
        isCasSpecial = statement.dbReadBoolean("MPCSPE");
        masseAvs = statement.dbReadNumeric("MPMAVS", 2);
        masseAF = statement.dbReadNumeric("MPMAAF", 2);
        masseAC = statement.dbReadNumeric("MPMAAC", 2);
        masseAutres = statement.dbReadNumeric("MPMASA", 2);
        nbrePoints = statement.dbReadNumeric("MPNBPT");
        delaiControle = statement.dbReadNumeric("MPDECO");
        categorie = statement.dbReadNumeric("MPCASA");
        periodeDebut = statement.dbReadDateAMJ("MPPEDE");
        periodeFin = statement.dbReadDateAMJ("MPPEFI");
        anneePrevue = statement.dbReadNumeric("MPAPRE");
        prevuManuellement = statement.dbReadNumeric("MPPRMA");
        prevuManuellementCom = statement.dbReadString("MPPRMC");
        lastUser = statement.dbReadString("MPLUSR");
        lastModification = statement.dbReadString("MPLMOD");
        commentaires = statement.dbReadString("MPCOMM");
        periodePrevueDebut = statement.dbReadString("MPPPDE");
        periodePrevueFin = statement.dbReadString("MPPPFI");
        masseSalariale = statement.dbReadString("MPMASS");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        if (JadeStringUtil.isEmpty(getNumAffilie())) {
            _addError(statement.getTransaction(), getSession().getLabel("ATTRIBUTION_PTS_PAS_NUM_AFFILIE"));
        } else {
            AFAffiliationManager manager = new AFAffiliationManager();
            manager.setSession(getSession());
            manager.setForAffilieNumero(getNumAffilie());
            manager.setForTypesAffParitaires();
            manager.find();
            if (manager.size() > 0) {
                AFAffiliation affiliation = (AFAffiliation) manager.getFirstEntity();
                if ((!affiliation.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_EMPLOY))
                        && (!affiliation.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_INDEP_EMPLOY))
                        && (!affiliation.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_EMPLOY_D_F))) {
                    _addError(statement.getTransaction(), getSession().getLabel("ATTRIBUTION_PTS_PAS_EMPLOYEUR"));
                }
            } else {
                _addError(statement.getTransaction(), getSession().getLabel("ATTRIBUTION_PTS_PAS_AFFILIE"));
            }
        }

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("MPAPID", this._dbWriteNumeric(statement.getTransaction(), getIdAttributionPts(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // statement.writeField("MAIAFF",
        // _dbWriteNumeric(statement.getTransaction(), getIdAffiliation(),
        // "idAffiliation"));
        statement.writeField("MALNAF", this._dbWriteString(statement.getTransaction(), getNumAffilie(), "numAffilie"));
        statement.writeField("MDICON", this._dbWriteNumeric(statement.getTransaction(), getIdControle(), "idControle"));
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
        statement.writeField("MPCSPE", this._dbWriteBoolean(statement.getTransaction(), getIsCasSpecial(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "isCasSpecial"));
        statement.writeField("MPNBEC",
                this._dbWriteNumeric(statement.getTransaction(), getNbreEcritures(), "nbreEcritures"));
        statement.writeField("MPMAVS", this._dbWriteNumeric(statement.getTransaction(), getMasseAvs(), "masseAvs"));
        statement.writeField("MPMAAF", this._dbWriteNumeric(statement.getTransaction(), getMasseAF(), "masseAF"));
        statement.writeField("MPMAAC", this._dbWriteNumeric(statement.getTransaction(), getMasseAC(), "masseAC"));
        statement.writeField("MPMASA",
                this._dbWriteNumeric(statement.getTransaction(), getMasseAutres(), "masseAutres"));
        statement.writeField("MPNBPT", this._dbWriteNumeric(statement.getTransaction(), getNbrePoints(), "nbrePoints"));
        statement.writeField("MPDECO",
                this._dbWriteNumeric(statement.getTransaction(), getDelaiControle(), "delaiControle"));
        statement.writeField("MPCASA",
                this._dbWriteNumeric(statement.getTransaction(), getCategorieSalaires(), "categorieSalaires"));
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
        statement.writeField("MPPPDE",
                this._dbWriteString(statement.getTransaction(), getPeriodePrevueDebut(), "periodePrevueDebut"));
        statement.writeField("MPPPFI",
                this._dbWriteString(statement.getTransaction(), getPeriodePrevueFin(), "periodePrevueFin"));
        statement.writeField("MPMASS",
                this._dbWriteString(statement.getTransaction(), this.getMasseSalariale(), "masseSalariale"));
    }

    public String getAnneeDebutAffiliation() {
        if (!JadeStringUtil.isEmpty(getNumAffilie())) {
            AFAffiliationManager affManager = new AFAffiliationManager();
            affManager.setSession(getSession());
            affManager.setForAffilieNumero(getNumAffilie());
            affManager.setForTypesAffParitaires();
            try {
                affManager.find();
                AFAffiliation aff = (AFAffiliation) affManager.getFirstEntity();
                if (!aff.isNew()) {
                    return aff.getDateDebut().substring(6, 10);
                }
                return "";
            } catch (Exception e) {
                return "";
            }
        } else {
            return "";
        }
    }

    public String getAnneeFinAffiliation() {
        if (!JadeStringUtil.isEmpty(getNumAffilie())) {
            AFAffiliationManager affManager = new AFAffiliationManager();
            affManager.setSession(getSession());
            affManager.setForAffilieNumero(getNumAffilie());
            affManager.setForTypesAffParitaires();
            try {
                affManager.find();
                AFAffiliation aff = (AFAffiliation) affManager.getFirstEntity();
                if (!aff.isNew()) {
                    return aff.getDateFin().substring(6, 10);
                }
                return "";
            } catch (Exception e) {
                return "";
            }
        } else {
            return "";
        }
    }

    public String getAnneePrecedente() {
        AFControleEmployeur controle = _findLastControle();
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
        if (cumulMasse <= AFAttributionPts.PALIER_MASSE_SAL1) {
            return 0;
        } else if ((AFAttributionPts.PALIER_MASSE_SAL2 > cumulMasse)
                && (cumulMasse > AFAttributionPts.PALIER_MASSE_SAL1)) {
            return 1;
        } else if ((AFAttributionPts.PALIER_MASSE_SAL3 > cumulMasse)
                && (cumulMasse >= AFAttributionPts.PALIER_MASSE_SAL2)) {
            return 2;
        } else if ((AFAttributionPts.PALIER_MASSE_SAL4 > cumulMasse)
                && (cumulMasse >= AFAttributionPts.PALIER_MASSE_SAL3)) {
            return 3;
        } else if (cumulMasse > AFAttributionPts.PALIER_MASSE_SAL4) {
            return 4;
        } else {
            return 0;
        }
    }

    public String getCategorieLibelle(String annee) {
        return "";// +getCategorie(getMasseSalariale(annee));
    }

    public String getCategorieSalaires() {
        return categorie;
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

    public String getCriteresEntrepriseCom() {
        return criteresEntrepriseCom;
    }

    public String getDateDebutControle() {
        AFControleEmployeur controle = _findLastControle();
        if ((controle != null) && !JadeStringUtil.isEmpty(controle.getDateDebutControle())) {
            return controle.getDateDebutControle();
        } else {
            return "";
        }
    }

    public String getDateFinControle() {
        AFControleEmployeur controle = _findLastControle();
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
        AFControleEmployeur controle = _findLastControle();
        if ((controle != null) && !JadeStringUtil.isEmpty(controle.getGenreControle())) {
            return getSession().getCodeLibelle(controle.getGenreControle());
        } else {
            return "";
        }
    }

    public String getIdAttributionPts() {
        return idAttributionPts;
    }

    public String getIdControle() {
        return idControle;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public Boolean getIsCasSpecial() {
        return isCasSpecial;
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
            AFAttributionPtsCumulMasseManager manager = new AFAttributionPtsCumulMasseManager();
            manager.setSession(getSession());
            manager.setForAnnee(annee);
            manager.setForNumAffilie(getNumAffilie());
            try {
                manager.find();
                if (manager.size() > 0) {
                    for (int i = 0; i < manager.size(); i++) {
                        AFAttributionPtsCumulMasse entity = (AFAttributionPtsCumulMasse) manager.get(i);
                        value = new BigDecimal(entity.getCumulMasse());
                        return value;
                    }
                    return zeroValue;
                } else {
                    manager.setForAnnee(anneePrecedente);
                    manager.find();
                    for (int i = 0; i < manager.size(); i++) {
                        AFAttributionPtsCumulMasse entity = (AFAttributionPtsCumulMasse) manager.get(i);
                        value = new BigDecimal(entity.getCumulMasse());
                        return value;
                    }
                    return zeroValue;
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
            AFAttributionPtsCumulMasseManager manager = new AFAttributionPtsCumulMasseManager();
            manager.setSession(getSession());
            manager.setForAnnee(annee);
            manager.setForNumAffilie(getNumAffilie());
            try {
                manager.find();
                for (int i = 0; i < manager.size(); i++) {
                    AFAttributionPtsCumulMasse entity = (AFAttributionPtsCumulMasse) manager.get(i);
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
                AFAttributionPtsCumulMasseManager manager = new AFAttributionPtsCumulMasseManager();
                manager.setSession(getSession());
                manager.setForAnnee(annee);
                manager.setForNumAffilie(getNumAffilie());
                try {
                    manager.find();
                    for (int i = 0; i < manager.size(); i++) {
                        AFAttributionPtsCumulMasse entity = (AFAttributionPtsCumulMasse) manager.get(i);
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

    /*
     * public String getIdAffiliation() { return idAffiliation; } public void setIdAffiliation(String idAffiliation) {
     * this.idAffiliation = idAffiliation; }
     */

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

    public String getPeriodePrevueDebut() {
        return periodePrevueDebut;
    }

    public String getPeriodePrevueFin() {
        return periodePrevueFin;
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

    public void setAnneePrevue(String anneePrevue) {
        this.anneePrevue = anneePrevue;
    }

    public void setCategorieSalaires(String categorieSalaires) {
        categorie = categorieSalaires;
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

    public void setIdControle(String idControle) {
        this.idControle = idControle;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIsCasSpecial(Boolean isCasSpecial) {
        this.isCasSpecial = isCasSpecial;
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

    public void setPeriodePrevueDebut(String periodePrevueDebut) {
        this.periodePrevueDebut = periodePrevueDebut;
    }

    public void setPeriodePrevueFin(String periodePrevueFin) {
        this.periodePrevueFin = periodePrevueFin;
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
