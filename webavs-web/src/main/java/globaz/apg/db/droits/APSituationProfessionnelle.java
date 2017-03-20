/*
 * Créé le 20 mai 05
 */
package globaz.apg.db.droits;

import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.enums.APAssuranceTypeAssociation;
import globaz.apg.interfaces.SituationProfessionnelle;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.api.IPRSituationProfessionnelle;
import globaz.prestation.clone.factory.IPRCloneable;

/**
 * <H1>Bentity de la situation professionnelle</H1>
 * <p>
 * Note: il y a une relation 1-1 entre la table des employeurs et des situations pro de sorte que chaque fois qu'une
 * situation pro est cree, un employeur est cree (dans _beforeadd).
 * </p>
 * 
 * @see globaz.apg.db.droits.APEmployeur
 * @author dvh
 */
public class APSituationProfessionnelle extends BEntity implements IPRCloneable, SituationProfessionnelle {

    private static final long serialVersionUID = 1L;

    private static final JACalendarGregorian CALENDAR = new JACalendarGregorian();

    private static final String ERREUR_ALLOC_MAX_INCOCHABLE = "ALLOC_MAX_INCOCHABLE";

    public static final String FIELDNAME_ANNEE_TAXATION = "VFMAAT";

    public static final String FIELDNAME_AUTRE_REMUNERATION = "VFMARE";

    public static final String FIELDNAME_AUTRSALAIRE = "VFMASA";

    public static final String FIELDNAME_CS_ASSURANCE_ASSOCIATION = "VFTAAS";

    public static final String FIELDNAME_DATEDEBUT = "VFDDEB";

    public static final String FIELDNAME_DATEFIN = "VFDFIN";

    public static final String FIELDNAME_DATEFINCONTRAT = "VFDFCO";

    public static final String FIELDNAME_HAS_ACM_ALPHA_PRESTATION = "VFBACM";

    public static final String FIELDNAME_HAS_ACM2_ALPHA_PRESTATION = "VFBACM2";

    public static final String FIELDNAME_HAS_LAMAT_PRESTATION = "VFBLAM";

    public static final String FIELDNAME_HEURESSEMAINE = "VFMHES";

    public static final String FIELDNAME_IDDROIT = "VFIDRO";

    public static final String FIELDNAME_IDDOMAINE_PAIEMENT = "VFIDDOP";

    public static final String FIELDNAME_IDTIERS_PAIEMENT = "VFIDTIP";

    public static final String FIELDNAME_IDEMPLOYEUR = "VFIEMP";

    public static final String FIELDNAME_IDSITUATIONPROF = "VFISIP";

    public static final String FIELDNAME_ISALLOCATIONMAX = "VFBALM";

    public static final String FIELDNAME_ISALLOCEXPLOIT = "VFBAEX";

    public static final String FIELDNAME_ISCOLLABOAGRICOLE = "VFBCOA";

    public static final String FIELDNAME_ISINDEPENDANT = "VFBIND";

    public static final String FIELDNAME_ISNOACTIF = "VFBNAC";

    public static final String FIELDNAME_ISNONSOUMISCOTISATION = "VFBNSC";

    public static final String FIELDNAME_ISPORTEENCOMPTE = "VFBPEC";

    public static final String FIELDNAME_ISSITUATIONPROF = "VFBSIP";

    public static final String FIELDNAME_ISTRAVAILGRICOLE = "VFBTRA";

    public static final String FIELDNAME_ISTRAVAILSANSEMPL = "VFBTSE";

    public static final String FIELDNAME_ISVERSEMENTEMPLOYEUR = "VFBVEM";

    public static final String FIELDNAME_MONTANT_JOURNALIER_ACM_NE = "VFMJOU";

    public static final String FIELDNAME_MONTANTVERSE = "VFMMOV";

    public static final String FIELDNAME_PERIODAUTREREMUN = "VFTAUR";

    public static final String FIELDNAME_PERIODAUTRSALAIRE = "VFTPAS";

    public static final String FIELDNAME_PERIODMONTANTVERSE = "VFTPMV";

    public static final String FIELDNAME_PERIODSALAIRENATUR = "VFTPSN";

    public static final String FIELDNAME_POURCENTAUTRREMUN = "VFMPAR";

    public static final String FIELDNAME_POURCENTMONTVERSE = "VFMPMV";

    public static final String FIELDNAME_REVENUINDEPENDANT = "VFMRIN";

    public static final String FIELDNAME_SALAIREHORAIRE = "VFMSAH";

    public static final String FIELDNAME_SALAIREMENSUEL = "VFMSAM";

    public static final String FIELDNAME_SALAIRENATURE = "VFMSAN";

    public static final String TABLE_NAME_SITUATION_PROFESSIONNELLE = "APSIPRP";

    protected String anneeTaxation = "";

    protected String autreOuPourcentRemuneration = "";

    protected String autreSalaire = "";

    protected String csAssuranceAssociation = "";

    protected String dateDebut = "";

    protected String dateFin = "";

    protected String dateFinContrat = "";

    private boolean deletePrestationsRequis = false;

    // transient pour ne pas surcharger la session http
    protected transient APEmployeur employeur = null;

    protected Boolean hasAcmAlphaPrestations = Boolean.FALSE;

    protected Boolean hasAcm2AlphaPrestations = Boolean.FALSE;

    protected Boolean hasLaMatPrestations = Boolean.FALSE;

    protected String heuresSemaine = "";

    protected String idDroit = "";

    protected String idDomainePaiementEmployeur = "";

    protected String idTiersPaiementEmployeur = "";

    protected String idEmployeur = "";

    protected String idSituationProf = "";

    protected Boolean isAllocationExploitation = Boolean.FALSE;

    protected Boolean isAllocationMax = Boolean.FALSE;

    protected Boolean isCollaborateurAgricole = Boolean.FALSE;

    protected Boolean isIndependant = Boolean.FALSE;

    protected Boolean isNonActif = Boolean.FALSE;

    protected Boolean isNonSoumisCotisation = Boolean.FALSE;

    protected Boolean isPourcentAutreRemun = Boolean.FALSE;

    protected Boolean isPorteEnCompte = Boolean.FALSE;

    protected Boolean isPourcentMontantVerse = Boolean.FALSE;

    protected Boolean isSituationProf = Boolean.FALSE;

    protected Boolean isTravailleurAgricole = Boolean.FALSE;

    protected Boolean isTravailleurSansEmploi = Boolean.FALSE;

    protected Boolean isVersementEmployeur = Boolean.TRUE;

    protected String montantJournalierAcmNe = "";

    protected String montantOuPourcentVerse = "";

    protected String periodiciteAutreRemun = "";

    protected String periodiciteAutreSalaire = "";

    protected String periodiciteMontantVerse = "";

    protected String periodiciteSalaireNature = "";

    protected String revenuIndependant = "";

    protected String salaireHoraire = "";

    protected String salaireMensuel = "";

    protected String salaireNature = "";

    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {
        APPrestationManager prestationManager = new APPrestationManager();
        prestationManager.setSession(getSession());
        prestationManager.setForIdDroit(getIdDroit());
        prestationManager.find(transaction, BManager.SIZE_NOLIMIT);

        for (int i = 0; i < prestationManager.size(); i++) {
            APPrestation prestation = (APPrestation) prestationManager.getEntity(i);
            prestation.delete(transaction);
        }

        super._afterAdd(transaction);
    }

    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        if (!loadEmployeur().isNew()) {
            loadEmployeur().delete(transaction);
        }

        APPrestationManager prestationManager = new APPrestationManager();
        prestationManager.setSession(getSession());
        prestationManager.setForIdDroit(getIdDroit());
        prestationManager.find(transaction, BManager.SIZE_NOLIMIT);

        for (int i = 0; i < prestationManager.size(); i++) {
            APPrestation prestation = (APPrestation) prestationManager.getEntity(i);
            prestation.delete(transaction);
        }

        super._afterDelete(transaction);
    }

    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {
        // quand un droit est mis à jour, les prestations qui ont pu être
        // calculée pour ce droit n'ont plus aucun sens,
        // on les efface.
        // ceci ne s'applique pas à une modification de l'état du droit
        if (deletePrestationsRequis) {
            APPrestationManager prestationManager = new APPrestationManager();
            prestationManager.setSession(getSession());
            prestationManager.setForIdDroit(getIdDroit());
            prestationManager.find(transaction, BManager.SIZE_NOLIMIT);

            for (int i = 0; i < prestationManager.size(); i++) {
                APPrestation prestation = (APPrestation) prestationManager.getEntity(i);
                prestation.delete(transaction);
            }

            deletePrestationsRequis = false;
        }
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        nettoyage();
        // on sauve l'employeur
        APEmployeur employeur = loadEmployeur();

        employeur.add(transaction);
        idEmployeur = employeur.getIdEmployeur();
        this.employeur = employeur;

        setIdSituationProf(this._incCounter(transaction, "0"));
    }

    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        nettoyage();
        // on met a jour l'employeur
        loadEmployeur().update(transaction);

        super._beforeUpdate(transaction);
    }

    /**
     * Renvoie le nom de la table
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return APSituationProfessionnelle.TABLE_NAME_SITUATION_PROFESSIONNELLE;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        isTravailleurSansEmploi = statement.dbReadBoolean(APSituationProfessionnelle.FIELDNAME_ISTRAVAILSANSEMPL);
        isNonActif = statement.dbReadBoolean(APSituationProfessionnelle.FIELDNAME_ISNOACTIF);
        isIndependant = statement.dbReadBoolean(APSituationProfessionnelle.FIELDNAME_ISINDEPENDANT);
        revenuIndependant = statement.dbReadNumeric(APSituationProfessionnelle.FIELDNAME_REVENUINDEPENDANT, 2);
        heuresSemaine = statement.dbReadNumeric(APSituationProfessionnelle.FIELDNAME_HEURESSEMAINE, 2);
        salaireHoraire = statement.dbReadNumeric(APSituationProfessionnelle.FIELDNAME_SALAIREHORAIRE, 2);
        salaireMensuel = statement.dbReadNumeric(APSituationProfessionnelle.FIELDNAME_SALAIREMENSUEL, 2);
        autreSalaire = statement.dbReadNumeric(APSituationProfessionnelle.FIELDNAME_AUTRSALAIRE, 2);
        periodiciteAutreSalaire = statement.dbReadNumeric(APSituationProfessionnelle.FIELDNAME_PERIODAUTRSALAIRE);
        idDroit = statement.dbReadNumeric(APSituationProfessionnelle.FIELDNAME_IDDROIT);

        idDomainePaiementEmployeur = statement.dbReadNumeric(APSituationProfessionnelle.FIELDNAME_IDDOMAINE_PAIEMENT);
        idTiersPaiementEmployeur = statement.dbReadNumeric(APSituationProfessionnelle.FIELDNAME_IDTIERS_PAIEMENT);

        dateDebut = statement.dbReadDateAMJ(APSituationProfessionnelle.FIELDNAME_DATEDEBUT);
        autreOuPourcentRemuneration = statement
                .dbReadNumeric(APSituationProfessionnelle.FIELDNAME_POURCENTAUTRREMUN, 2);

        if (JadeStringUtil.isIntegerEmpty(autreOuPourcentRemuneration)) {
            autreOuPourcentRemuneration = statement.dbReadNumeric(
                    APSituationProfessionnelle.FIELDNAME_AUTRE_REMUNERATION, 2);
            isPourcentAutreRemun = Boolean.FALSE;
        } else {
            isPourcentAutreRemun = Boolean.TRUE;
        }

        anneeTaxation = statement.dbReadNumeric(APSituationProfessionnelle.FIELDNAME_ANNEE_TAXATION);

        periodiciteAutreRemun = statement.dbReadNumeric(APSituationProfessionnelle.FIELDNAME_PERIODAUTREREMUN);
        dateFin = statement.dbReadDateAMJ(APSituationProfessionnelle.FIELDNAME_DATEFIN);
        dateFinContrat = statement.dbReadDateAMJ(APSituationProfessionnelle.FIELDNAME_DATEFINCONTRAT);
        isAllocationMax = statement.dbReadBoolean(APSituationProfessionnelle.FIELDNAME_ISALLOCATIONMAX);
        isCollaborateurAgricole = statement.dbReadBoolean(APSituationProfessionnelle.FIELDNAME_ISCOLLABOAGRICOLE);
        isPorteEnCompte = statement.dbReadBoolean(APSituationProfessionnelle.FIELDNAME_ISPORTEENCOMPTE);
        isTravailleurAgricole = statement.dbReadBoolean(APSituationProfessionnelle.FIELDNAME_ISTRAVAILGRICOLE);
        salaireNature = statement.dbReadNumeric(APSituationProfessionnelle.FIELDNAME_SALAIRENATURE, 2);
        periodiciteSalaireNature = statement.dbReadNumeric(APSituationProfessionnelle.FIELDNAME_PERIODSALAIRENATUR);
        isVersementEmployeur = statement.dbReadBoolean(APSituationProfessionnelle.FIELDNAME_ISVERSEMENTEMPLOYEUR);
        isNonSoumisCotisation = statement.dbReadBoolean(APSituationProfessionnelle.FIELDNAME_ISNONSOUMISCOTISATION);
        idEmployeur = statement.dbReadNumeric(APSituationProfessionnelle.FIELDNAME_IDEMPLOYEUR);
        montantOuPourcentVerse = statement.dbReadNumeric(APSituationProfessionnelle.FIELDNAME_POURCENTMONTVERSE, 2);

        if (JadeStringUtil.isIntegerEmpty(montantOuPourcentVerse)) {
            montantOuPourcentVerse = statement.dbReadNumeric(APSituationProfessionnelle.FIELDNAME_MONTANTVERSE, 2);
            isPourcentMontantVerse = Boolean.FALSE;
        } else {
            isPourcentMontantVerse = Boolean.TRUE;
        }

        periodiciteMontantVerse = statement.dbReadNumeric(APSituationProfessionnelle.FIELDNAME_PERIODMONTANTVERSE);
        isSituationProf = statement.dbReadBoolean(APSituationProfessionnelle.FIELDNAME_ISSITUATIONPROF);
        isAllocationExploitation = statement.dbReadBoolean(APSituationProfessionnelle.FIELDNAME_ISALLOCEXPLOIT);
        idSituationProf = statement.dbReadNumeric(APSituationProfessionnelle.FIELDNAME_IDSITUATIONPROF);

        hasLaMatPrestations = statement.dbReadBoolean(APSituationProfessionnelle.FIELDNAME_HAS_LAMAT_PRESTATION);
        hasAcmAlphaPrestations = statement.dbReadBoolean(APSituationProfessionnelle.FIELDNAME_HAS_ACM_ALPHA_PRESTATION);
        hasAcm2AlphaPrestations = statement
                .dbReadBoolean(APSituationProfessionnelle.FIELDNAME_HAS_ACM2_ALPHA_PRESTATION);
        csAssuranceAssociation = statement.dbReadNumeric(APSituationProfessionnelle.FIELDNAME_CS_ASSURANCE_ASSOCIATION);

        montantJournalierAcmNe = statement.dbReadNumeric(
                APSituationProfessionnelle.FIELDNAME_MONTANT_JOURNALIER_ACM_NE, 2);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        BTransaction transaction = statement.getTransaction();

        if (isIndependant.booleanValue()) {
            _propertyMandatory(transaction, revenuIndependant, getSession().getLabel("REVENU_INDEPENDANT"));
        }

        if (!JadeStringUtil.isDecimalEmpty(salaireHoraire)) {
            _propertyMandatory(transaction, heuresSemaine, getSession().getLabel("HEURES_SEMAINE"));

            // le nombre d'heures par semaine ne peut pas dépasser 168 heures
            // (meme beaucoup moins en fait, mais bon...)
            try {
                if (168d < Double.parseDouble(heuresSemaine)) {
                    _addError(transaction, getSession().getLabel("HEURES_SEMAINE_INVALIDE"));
                }
            } catch (NumberFormatException e) {
                // le nombre d'heures par semaine n'est pas un nombre réél
                _addError(transaction, getSession().getLabel("HEURES_SEMAINE_INVALIDE"));
            }
        }

        if (!JadeStringUtil.isDecimalEmpty(heuresSemaine)) {
            _propertyMandatory(transaction, salaireHoraire, getSession().getLabel("SALAIRE_HORAIRE"));
        }

        _propertyMandatory(transaction, loadEmployeur().getIdTiers(), getSession().getLabel("TIERS_INTROUVABLE"));

        // validation de case montant max
        APSituationProfessionnelleManager mgr = new APSituationProfessionnelleManager();

        mgr.setSession(getSession());
        mgr.setForIdDroit(idDroit);
        mgr.setNotForIdSituationProfessionnelle(idSituationProf);

        if (isAllocationMax.booleanValue()) {
            // isAllocationMax ne peut etre coche que s'il n'y a pas encore
            // d'employeur.
            if (mgr.getCount() > 0) {
                _addError(transaction, getSession().getLabel(APSituationProfessionnelle.ERREUR_ALLOC_MAX_INCOCHABLE));
            }
        } else {
            // si on a deja une situation pro avec montant max coche, on ne peut
            // pas en creer de nouvelle
            mgr.setIsAllocationMax(Boolean.TRUE.toString());

            if (mgr.getCount() > 0) {
                _addError(transaction, getSession().getLabel(APSituationProfessionnelle.ERREUR_ALLOC_MAX_INCOCHABLE));
            }
        }

        if (!JAUtil.isDateEmpty(dateDebut)) {
            _propertyMandatory(transaction, montantOuPourcentVerse, getSession().getLabel("MONTANT_VERSE_REQUIS"));
        }

        // date de debut du montant verse par employeur doit etre avant date de
        // fin
        if (!JAUtil.isDateEmpty(dateFin)
                && !BSessionUtil.compareDateFirstLowerOrEqual(getSession(), dateDebut, dateFin)) {
            _addError(transaction, getSession().getLabel("SITPRO_DATE_DEBUT_SUP_DATE_FIN"));
        }

        // date de fin de contrat doit etre apres egale a date de fin du montant
        // verse employeur
        if (!JAUtil.isDateEmpty(dateFinContrat)
                && !JAUtil.isDateEmpty(dateFin)
                && (APSituationProfessionnelle.CALENDAR.compare(dateFinContrat, dateFin) == JACalendar.COMPARE_FIRSTLOWER)) {
            _addError(transaction, getSession().getLabel("SITPRO_DATE_FIN_SUP_DATE_FIN_CONTRAT"));
        }

        // si autre remuneration est un pourcentage d'un autre salaire, ce
        // salaire doit exister
        if (isPourcentAutreRemun.booleanValue()) {
            _propertyMandatory(transaction, autreOuPourcentRemuneration, getSession().getLabel("POURCENT_REMUN_REQUIS"));

            if (IPRSituationProfessionnelle.CS_PERIODICITE_4_SEMAINES.equals(periodiciteAutreRemun)
                    || IPRSituationProfessionnelle.CS_PERIODICITE_ANNEE.equals(periodiciteAutreRemun)) {
                // le salaire sur 4 semaines ou annuel ne peut etre saisi que
                // comme autre salaire
                _propertyMandatory(transaction, autreSalaire, getSession().getLabel("AUTRE_SALAIRE_REQUIS"));

                if (!periodiciteAutreSalaire.equals(periodiciteAutreRemun)) {
                    _addError(transaction, getSession().getLabel("PERIOD_AUTRE_SAL_DIFF_PERIOD_AUTRE_REMUN"));
                }
            } else if (IPRSituationProfessionnelle.CS_PERIODICITE_HEURE.equals(periodiciteAutreRemun)) {
                _propertyMandatory(transaction, salaireHoraire, getSession().getLabel("SALAIRE_HORAIRE_REQUIS"));
            } else if (IPRSituationProfessionnelle.CS_PERIODICITE_MOIS.equals(periodiciteAutreRemun)) {
                _propertyMandatory(transaction, salaireMensuel, getSession().getLabel("SALAIRE_MENSUEL_REQUIS"));
            }
        }

        // si montant verse est un pourcentage d'un autre salaire, ce salaire
        // doit exister
        if (isPourcentMontantVerse.booleanValue()) {
            _propertyMandatory(transaction, montantOuPourcentVerse, getSession().getLabel("POURCENT_AUTRE_SAL_REQUIS"));

            if (IPRSituationProfessionnelle.CS_PERIODICITE_4_SEMAINES.equals(periodiciteMontantVerse)
                    || IPRSituationProfessionnelle.CS_PERIODICITE_ANNEE.equals(periodiciteMontantVerse)) {
                // le salaire sur 4 semaines ou annuel ne peut etre saisi que
                // comme autre salaire
                _propertyMandatory(transaction, autreSalaire, getSession().getLabel("AUTRE_SALAIRE_REQUIS"));

                if (!periodiciteAutreSalaire.equals(periodiciteMontantVerse)) {
                    _addError(transaction, getSession().getLabel("PERIOD_AUTRE_SAL_DIFF_PERIOD_MONTANT_VERSE"));
                }
            } else if (IPRSituationProfessionnelle.CS_PERIODICITE_HEURE.equals(periodiciteMontantVerse)) {
                _propertyMandatory(transaction, salaireHoraire, getSession().getLabel("SALAIRE_HORAIRE_REQUIS"));
            } else if (IPRSituationProfessionnelle.CS_PERIODICITE_MOIS.equals(periodiciteMontantVerse)) {
                _propertyMandatory(transaction, salaireMensuel, getSession().getLabel("SALAIRE_MENSUEL_REQUIS"));
            }
        }

        // peut pas être indépendant ET collaborateur agricole
        if (isCollaborateurAgricole.booleanValue() && isIndependant.booleanValue()) {
            _addError(transaction, getSession().getLabel("COLLABO_AGR_ET_INDEP"));
        }

        if (isPorteEnCompte.booleanValue() && !isVersementEmployeur) {
            _addError(transaction, getSession().getLabel("VERSEMENT_ASSURE_ET_PORTER_EN_COMPTE_INTERDIT"));
        }

        // il faut au moins UN revenu ou que la case montant maximum soit cochée
        if (JadeStringUtil.isDecimalEmpty(revenuIndependant) && JadeStringUtil.isDecimalEmpty(salaireHoraire)
                && JadeStringUtil.isDecimalEmpty(salaireMensuel) && JadeStringUtil.isDecimalEmpty(autreSalaire)
                && !isAllocationMax.booleanValue()) {
            _addError(transaction, getSession().getLabel("AU_MOINS_UN_REVENU"));
        }

        // si le versement est à l'assuré, il ne faut pas saisir de salaire
        // versé
        if (!isVersementEmployeur.booleanValue()) {
            if (!JadeStringUtil.isDecimalEmpty(montantOuPourcentVerse)) {
                _addError(transaction, getSession().getLabel("VERSEMENT_ASSURE_ET_SALAIRE_VERSE"));
            }
        }

        // Si montant versé (et non pas pourcentage), ne pas permettre la saisie
        // "heure" de la liste
        if (!isPourcentMontantVerse.booleanValue()
                && periodiciteMontantVerse.equals(IPRSituationProfessionnelle.CS_PERIODICITE_HEURE)
                && !montantOuPourcentVerse.equals("")) {
            _addError(transaction, getSession().getLabel("POURCENT_MONTANT_HEURE"));
        }

        boolean isAssuranceAssociationACMNE = APAssuranceTypeAssociation.FNE.equals(csAssuranceAssociation)
                || APAssuranceTypeAssociation.MECP.equals(csAssuranceAssociation)
                || APAssuranceTypeAssociation.PP.equals(csAssuranceAssociation);
        if (!isAssuranceAssociationACMNE && !JadeStringUtil.isBlankOrZero(montantJournalierAcmNe)) {
            _addError(
                    transaction,
                    getSession().getLabel(
                            "JSP_SITUATION_PROFESSIONNELLE_ERREUR_TOTAL_ACM_NE_SAISI_POUR_AFFILIE_NON_ACM_NE"));
        }

        if (isAssuranceAssociationACMNE && new FWCurrency(montantJournalierAcmNe).isNegative()) {
            _addError(transaction, getSession().getLabel("JSP_SITUATION_PROFESSIONNELLE_ERREUR_TOTAL_ACM_NE_NEGATIF"));
        }
    }

    private void nettoyage() {
        if (!isVersementEmployeur) {
            idDomainePaiementEmployeur = null;
            idTiersPaiementEmployeur = null;
        }
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(APSituationProfessionnelle.FIELDNAME_IDSITUATIONPROF,
                this._dbWriteNumeric(statement.getTransaction(), idSituationProf, "idSituationProf"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(APSituationProfessionnelle.FIELDNAME_ISTRAVAILSANSEMPL, this._dbWriteBoolean(
                statement.getTransaction(), isTravailleurSansEmploi, BConstants.DB_TYPE_BOOLEAN_CHAR,
                "isTravailleurSansEmploi"));
        statement.writeField(APSituationProfessionnelle.FIELDNAME_ISNOACTIF, this._dbWriteBoolean(
                statement.getTransaction(), isNonActif, BConstants.DB_TYPE_BOOLEAN_CHAR, "isNonActif"));

        statement.writeField(APSituationProfessionnelle.FIELDNAME_ANNEE_TAXATION,
                this._dbWriteNumeric(statement.getTransaction(), anneeTaxation, "anneeTaxation"));

        statement.writeField(APSituationProfessionnelle.FIELDNAME_ISINDEPENDANT, this._dbWriteBoolean(
                statement.getTransaction(), isIndependant, BConstants.DB_TYPE_BOOLEAN_CHAR, "isIndependant"));
        statement.writeField(APSituationProfessionnelle.FIELDNAME_REVENUINDEPENDANT,
                this._dbWriteNumeric(statement.getTransaction(), revenuIndependant, "revenuIndependant"));
        statement.writeField(APSituationProfessionnelle.FIELDNAME_HEURESSEMAINE,
                this._dbWriteNumeric(statement.getTransaction(), heuresSemaine, "heuresSemaine"));
        statement.writeField(APSituationProfessionnelle.FIELDNAME_SALAIREHORAIRE,
                this._dbWriteNumeric(statement.getTransaction(), salaireHoraire, "salaireHoraire"));
        statement.writeField(APSituationProfessionnelle.FIELDNAME_SALAIREMENSUEL,
                this._dbWriteNumeric(statement.getTransaction(), salaireMensuel, "salaireMensuel"));
        statement.writeField(APSituationProfessionnelle.FIELDNAME_AUTRSALAIRE,
                this._dbWriteNumeric(statement.getTransaction(), autreSalaire, "autreSalaire"));
        statement.writeField(APSituationProfessionnelle.FIELDNAME_PERIODAUTRSALAIRE,
                this._dbWriteNumeric(statement.getTransaction(), periodiciteAutreSalaire, "periodiciteAutreSalaire"));
        statement.writeField(APSituationProfessionnelle.FIELDNAME_IDDROIT,
                this._dbWriteNumeric(statement.getTransaction(), idDroit, "idDroit"));

        statement.writeField(APSituationProfessionnelle.FIELDNAME_IDDOMAINE_PAIEMENT,
                this._dbWriteNumeric(statement.getTransaction(), idDomainePaiementEmployeur, "idDomainePaiement"));
        statement.writeField(APSituationProfessionnelle.FIELDNAME_IDTIERS_PAIEMENT,
                this._dbWriteNumeric(statement.getTransaction(), idTiersPaiementEmployeur, "idTiersPaiement"));

        statement.writeField(APSituationProfessionnelle.FIELDNAME_DATEDEBUT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDebut, "dateDebut"));

        if (isPourcentAutreRemun.booleanValue()) {
            statement.writeField(APSituationProfessionnelle.FIELDNAME_AUTRE_REMUNERATION,
                    this._dbWriteNumeric(statement.getTransaction(), "", "autreRemuneration"));
            statement
                    .writeField(APSituationProfessionnelle.FIELDNAME_POURCENTAUTRREMUN, this._dbWriteNumeric(
                            statement.getTransaction(), autreOuPourcentRemuneration, "pourcentAutreRemun"));
        } else {
            statement.writeField(APSituationProfessionnelle.FIELDNAME_AUTRE_REMUNERATION,
                    this._dbWriteNumeric(statement.getTransaction(), autreOuPourcentRemuneration, "autreRemuneration"));
            statement.writeField(APSituationProfessionnelle.FIELDNAME_POURCENTAUTRREMUN,
                    this._dbWriteNumeric(statement.getTransaction(), "", "pourcentAutreRemun"));
        }

        statement.writeField(APSituationProfessionnelle.FIELDNAME_PERIODAUTREREMUN,
                this._dbWriteNumeric(statement.getTransaction(), periodiciteAutreRemun, "periodiciteAutreRemun"));
        statement.writeField(APSituationProfessionnelle.FIELDNAME_DATEFIN,
                this._dbWriteDateAMJ(statement.getTransaction(), dateFin, "dateFin"));
        statement.writeField(APSituationProfessionnelle.FIELDNAME_DATEFINCONTRAT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateFinContrat, "dateFinContrat"));
        statement.writeField(APSituationProfessionnelle.FIELDNAME_ISALLOCATIONMAX, this._dbWriteBoolean(
                statement.getTransaction(), isAllocationMax, BConstants.DB_TYPE_BOOLEAN_CHAR, "isAllocationMax"));
        statement.writeField(APSituationProfessionnelle.FIELDNAME_ISCOLLABOAGRICOLE, this._dbWriteBoolean(
                statement.getTransaction(), isCollaborateurAgricole, BConstants.DB_TYPE_BOOLEAN_CHAR,
                "isCollaborateurAgricole"));
        statement.writeField(APSituationProfessionnelle.FIELDNAME_ISTRAVAILGRICOLE, this._dbWriteBoolean(
                statement.getTransaction(), isTravailleurAgricole, BConstants.DB_TYPE_BOOLEAN_CHAR,
                "isTravailleurAgricole"));
        statement.writeField(APSituationProfessionnelle.FIELDNAME_SALAIRENATURE,
                this._dbWriteNumeric(statement.getTransaction(), salaireNature, "salaireNature"));
        statement.writeField(APSituationProfessionnelle.FIELDNAME_PERIODSALAIRENATUR,
                this._dbWriteNumeric(statement.getTransaction(), periodiciteSalaireNature, "periodiciteSalaireNature"));
        statement.writeField(APSituationProfessionnelle.FIELDNAME_ISVERSEMENTEMPLOYEUR, this._dbWriteBoolean(
                statement.getTransaction(), isVersementEmployeur, BConstants.DB_TYPE_BOOLEAN_CHAR,
                "isVersementEmployeur"));
        statement.writeField(APSituationProfessionnelle.FIELDNAME_ISNONSOUMISCOTISATION, this._dbWriteBoolean(
                statement.getTransaction(), isNonSoumisCotisation, BConstants.DB_TYPE_BOOLEAN_CHAR,
                "isNonSoumisCotisation"));

        statement.writeField(APSituationProfessionnelle.FIELDNAME_ISPORTEENCOMPTE, this._dbWriteBoolean(
                statement.getTransaction(), isPorteEnCompte, BConstants.DB_TYPE_BOOLEAN_CHAR, "isPorteEnCompte"));

        statement.writeField(APSituationProfessionnelle.FIELDNAME_IDEMPLOYEUR,
                this._dbWriteNumeric(statement.getTransaction(), idEmployeur, "idEmployeur"));

        if (isPourcentMontantVerse.booleanValue()) {
            statement.writeField(APSituationProfessionnelle.FIELDNAME_MONTANTVERSE,
                    this._dbWriteNumeric(statement.getTransaction(), "", "montantVerse"));
            statement.writeField(APSituationProfessionnelle.FIELDNAME_POURCENTMONTVERSE,
                    this._dbWriteNumeric(statement.getTransaction(), montantOuPourcentVerse, "pourcentMontantVerse"));
        } else {
            statement.writeField(APSituationProfessionnelle.FIELDNAME_MONTANTVERSE,
                    this._dbWriteNumeric(statement.getTransaction(), montantOuPourcentVerse, "montantVerse"));
            statement.writeField(APSituationProfessionnelle.FIELDNAME_POURCENTMONTVERSE,
                    this._dbWriteNumeric(statement.getTransaction(), "", "pourcentMontantVerse"));
        }

        statement.writeField(APSituationProfessionnelle.FIELDNAME_PERIODMONTANTVERSE,
                this._dbWriteNumeric(statement.getTransaction(), periodiciteMontantVerse, "periodiciteMontantVerse"));
        statement.writeField(APSituationProfessionnelle.FIELDNAME_IDSITUATIONPROF,
                this._dbWriteNumeric(statement.getTransaction(), idSituationProf, "idSituationProf"));
        statement.writeField(APSituationProfessionnelle.FIELDNAME_ISALLOCEXPLOIT, this._dbWriteBoolean(
                statement.getTransaction(), isAllocationExploitation, BConstants.DB_TYPE_BOOLEAN_CHAR,
                "isAllocationExploitation"));
        statement.writeField(APSituationProfessionnelle.FIELDNAME_ISSITUATIONPROF, this._dbWriteBoolean(
                statement.getTransaction(), isSituationProf, BConstants.DB_TYPE_BOOLEAN_CHAR, "isSituationProf"));

        statement.writeField(APSituationProfessionnelle.FIELDNAME_HAS_LAMAT_PRESTATION,
                this._dbWriteBoolean(statement.getTransaction(), hasLaMatPrestations, BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "hasLaMatPrestations"));

        statement.writeField(APSituationProfessionnelle.FIELDNAME_HAS_ACM_ALPHA_PRESTATION, this._dbWriteBoolean(
                statement.getTransaction(), hasAcmAlphaPrestations, BConstants.DB_TYPE_BOOLEAN_CHAR,
                "hasAcmAlphaPrestations"));

        statement.writeField(APSituationProfessionnelle.FIELDNAME_HAS_ACM2_ALPHA_PRESTATION, this._dbWriteBoolean(
                statement.getTransaction(), hasAcm2AlphaPrestations, BConstants.DB_TYPE_BOOLEAN_CHAR,
                "hasAcm2AlphaPrestations"));

        statement.writeField(APSituationProfessionnelle.FIELDNAME_CS_ASSURANCE_ASSOCIATION,
                this._dbWriteNumeric(statement.getTransaction(), csAssuranceAssociation, "csAssuranceAssociation"));

        statement.writeField(APSituationProfessionnelle.FIELDNAME_MONTANT_JOURNALIER_ACM_NE,
                this._dbWriteNumeric(statement.getTransaction(), montantJournalierAcmNe, "montantJournalierAcmNe"));

    }

    @Override
    public IPRCloneable duplicate(int actionType) throws Exception {
        APSituationProfessionnelle clone = new APSituationProfessionnelle();
        clone.setAnneeTaxation(getAnneeTaxation());
        clone.setAutreRemuneration(getAutreRemuneration());
        clone.setAutreSalaire(getAutreSalaire());
        clone.setDateDebut(getDateDebut());
        clone.setDateFin(getDateFin());
        clone.setDateFinContrat(getDateFinContrat());
        clone.setHeuresSemaine(getHeuresSemaine());
        clone.setIdDomainePaiementEmployeur(getIdDomainePaiementEmployeur());
        clone.setIdTiersPaiementEmployeur(getIdTiersPaiementEmployeur());
        clone.setIsAllocationExploitation(getIsAllocationExploitation());
        clone.setIsAllocationMax(getIsAllocationMax());
        clone.setIsCollaborateurAgricole(getIsCollaborateurAgricole());
        clone.setIsTravailleurAgricole(getIsTravailleurAgricole());
        clone.setIsIndependant(getIsIndependant());
        clone.setIsNonActif(getIsNonActif());
        clone.setIsPourcentAutreRemun(getIsPourcentAutreRemun());
        clone.setIsPourcentMontantVerse(getIsPourcentMontantVerse());
        clone.setIsSituationProf(getIsSituationProf());
        clone.setIsNonSoumisCotisation(getIsNonSoumisCotisation());
        clone.setIsPorteEnCompte(getIsPorteEnCompte());
        clone.setIsTravailleurSansEmploi(getIsTravailleurSansEmploi());
        clone.setIsVersementEmployeur(getIsVersementEmployeur());
        clone.setMontantVerse(getMontantVerse());
        clone.setPeriodiciteAutreRemun(getPeriodiciteAutreRemun());
        clone.setPeriodiciteAutreSalaire(getPeriodiciteAutreSalaire());
        clone.setPeriodiciteMontantVerse(getPeriodiciteMontantVerse());
        clone.setPeriodiciteSalaireNature(getPeriodiciteSalaireNature());
        clone.setPourcentAutreRemun(getPourcentAutreRemun());
        clone.setPourcentMontantVerse(getPourcentMontantVerse());
        clone.setRevenuIndependant(getRevenuIndependant());
        clone.setSalaireHoraire(getSalaireHoraire());
        clone.setSalaireMensuel(getSalaireMensuel());
        clone.setSalaireNature(getSalaireNature());
        clone.setHasLaMatPrestations(getHasLaMatPrestations());
        clone.setHasAcmAlphaPrestations(getHasAcmAlphaPrestations());
        clone.setHasAcm2AlphaPrestations(getHasAcm2AlphaPrestations());
        clone.setCsAssuranceAssociation(getCsAssuranceAssociation());
        clone.setMontantJournalierAcmNe(getMontantJournalierAcmNe());

        // On ne veut pas de la validation pendant une duplication
        clone.wantCallValidate(false);

        // On appelle pas la methode beforeAdd, car l'on créerait un APEmployeur
        // à double.
        clone.wantCallMethodBefore(false);

        // Par contre, il faut mettre a jours l'incrément.
        clone.setIdSituationProf(this._incCounter(getSession().getCurrentThreadTransaction(), "0"));

        return clone;
    }

    public String getAutreOuPourcentRemuneration() {
        return autreOuPourcentRemuneration;
    }

    /**
     * getter pour l'attribut autre remuneration
     * 
     * @return la valeur courante de l'attribut autre remuneration
     */
    @Override
    public String getAutreRemuneration() {
        return autreOuPourcentRemuneration;
    }

    /**
     * getter pour l'attribut autre salaire
     * 
     * @return la valeur courante de l'attribut autre salaire
     */
    @Override
    public String getAutreSalaire() {
        return autreSalaire;
    }

    public String getCsAssuranceAssociation() {
        return csAssuranceAssociation;
    }

    /**
     * getter pour l'attribut date debut
     * 
     * @return la valeur courante de l'attribut date debut
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
     * getter pour l'attribut date fin contrat
     * 
     * @return la valeur courante de l'attribut date fin contrat
     */
    @Override
    public String getDateFinContrat() {
        return dateFinContrat;
    }

    @Override
    public Boolean getDeletePrestationsRequis() {
        return deletePrestationsRequis;
    }

    @Override
    public Boolean getHasAcmAlphaPrestations() {
        return hasAcmAlphaPrestations;
    }

    @Override
    public Boolean getHasAcm2AlphaPrestations() {
        return hasAcm2AlphaPrestations;
    }

    /**
     * @return
     */
    @Override
    public Boolean getHasLaMatPrestations() {
        return hasLaMatPrestations;
    }

    /**
     * getter pour l'attribut heures semaine
     * 
     * @return la valeur courante de l'attribut heures semaine
     */
    @Override
    public String getHeuresSemaine() {
        return heuresSemaine;
    }

    /**
     * getter pour l'attribut id droit
     * 
     * @return la valeur courante de l'attribut id droit
     */
    @Override
    public String getIdDroit() {
        return idDroit;
    }

    /**
     * getter pour l'attribut id employeur
     * 
     * @return la valeur courante de l'attribut id employeur
     */
    @Override
    public String getIdEmployeur() {
        return idEmployeur;
    }

    /**
     * getter pour l'attribut id situation prof
     * 
     * @return la valeur courante de l'attribut id situation prof
     */
    @Override
    public String getIdSituationProf() {
        return idSituationProf;
    }

    /**
     * getter pour l'attribut is allocation exploitation
     * 
     * @return la valeur courante de l'attribut is allocation exploitation
     */
    @Override
    public Boolean getIsAllocationExploitation() {
        return isAllocationExploitation;
    }

    /**
     * getter pour l'attribut is allocation max
     * 
     * @return la valeur courante de l'attribut is allocation max
     */
    @Override
    public Boolean getIsAllocationMax() {
        return isAllocationMax;
    }

    /**
     * getter pour l'attribut is collaborateur agricole
     * 
     * @return la valeur courante de l'attribut is collaborateur agricole
     */
    @Override
    public Boolean getIsCollaborateurAgricole() {
        return isCollaborateurAgricole;
    }

    /**
     * getter pour l'attribut is independant
     * 
     * @return la valeur courante de l'attribut is independant
     */
    @Override
    public Boolean getIsIndependant() {
        return isIndependant;
    }

    @Override
    public Boolean getIsPorteEnCompte() {
        if (isPorteEnCompte == null) {
            return false;
        }
        return isPorteEnCompte;
    }

    /**
     * getter pour l'attribut is non actif
     * 
     * @return la valeur courante de l'attribut is non actif
     */
    @Override
    public Boolean getIsNonActif() {
        return isNonActif;
    }

    /**
     * getter pour l'attribut is non soumis cotisation
     * 
     * @return la valeur courante de l'attribut is non soumis cotisation
     */
    @Override
    public Boolean getIsNonSoumisCotisation() {
        return isNonSoumisCotisation;
    }

    /**
     * getter pour l'attribut is pourcent autre remun
     * 
     * @return la valeur courante de l'attribut is pourcent autre remun
     */
    @Override
    public Boolean getIsPourcentAutreRemun() {
        return isPourcentAutreRemun;
    }

    /**
     * getter pour l'attribut is pourcent montant verse
     * 
     * @return la valeur courante de l'attribut is pourcent montant verse
     */
    @Override
    public Boolean getIsPourcentMontantVerse() {
        return isPourcentMontantVerse;
    }

    /**
     * getter pour l'attribut is situation prof
     * 
     * @return la valeur courante de l'attribut is situation prof
     */
    @Override
    public Boolean getIsSituationProf() {
        return isSituationProf;
    }

    /**
     * @return
     */
    @Override
    public Boolean getIsTravailleurAgricole() {
        return isTravailleurAgricole;
    }

    /**
     * getter pour l'attribut is travailleur sans emploi
     * 
     * @return la valeur courante de l'attribut is travailleur sans emploi
     */
    @Override
    public Boolean getIsTravailleurSansEmploi() {
        return isTravailleurSansEmploi;
    }

    /**
     * getter pour l'attribut is versement employeur
     * 
     * @return la valeur courante de l'attribut is versement employeur
     */
    @Override
    public Boolean getIsVersementEmployeur() {
        return isVersementEmployeur;
    }

    public String getMontantJournalierAcmNe() {
        return montantJournalierAcmNe;
    }

    public String getMontantOuPourcentVerse() {
        return montantOuPourcentVerse;
    }

    /**
     * getter pour l'attribut montant verse
     * 
     * @return la valeur courante de l'attribut montant verse
     */
    @Override
    public String getMontantVerse() {
        return montantOuPourcentVerse;
    }

    /**
     * getter pour l'attribut perdiodicite autre remun
     * 
     * @return la valeur courante de l'attribut perdiodicite autre remun
     */
    @Override
    public String getPeriodiciteAutreRemun() {
        return periodiciteAutreRemun;
    }

    /**
     * getter pour l'attribut periodicite autre salaire
     * 
     * @return la valeur courante de l'attribut periodicite autre salaire
     */
    @Override
    public String getPeriodiciteAutreSalaire() {
        return periodiciteAutreSalaire;
    }

    /**
     * getter pour l'attribut periodicite montant verse
     * 
     * @return la valeur courante de l'attribut periodicite montant verse
     */
    @Override
    public String getPeriodiciteMontantVerse() {
        return periodiciteMontantVerse;
    }

    /**
     * getter pour l'attribut periodicite salaire nature
     * 
     * @return la valeur courante de l'attribut periodicite salaire nature
     */
    @Override
    public String getPeriodiciteSalaireNature() {
        return periodiciteSalaireNature;
    }

    /**
     * getter pour l'attribut pourcent autre remun
     * 
     * @return la valeur courante de l'attribut pourcent autre remun
     */
    public String getPourcentAutreRemun() {
        return autreOuPourcentRemuneration;
    }

    /**
     * getter pour l'attribut pourcent montant verse
     * 
     * @return la valeur courante de l'attribut pourcent montant verse
     */
    public String getPourcentMontantVerse() {
        return montantOuPourcentVerse;
    }

    /**
     * getter pour l'attribut revenu independant
     * 
     * @return la valeur courante de l'attribut revenu independant
     */
    @Override
    public String getRevenuIndependant() {
        return revenuIndependant;
    }

    /**
     * getter pour l'attribut salaire horaire
     * 
     * @return la valeur courante de l'attribut salaire horaire
     */
    @Override
    public String getSalaireHoraire() {
        return salaireHoraire;
    }

    /**
     * getter pour l'attribut salaire mensuel
     * 
     * @return la valeur courante de l'attribut salaire mensuel
     */
    @Override
    public String getSalaireMensuel() {
        return salaireMensuel;
    }

    /**
     * getter pour l'attribut salaire nature
     * 
     * @return la valeur courante de l'attribut salaire nature
     */
    @Override
    public String getSalaireNature() {
        return salaireNature;
    }

    /**
     * getter pour l'attribut unique primary key
     * 
     * @return la valeur courante de l'attribut unique primary key
     */
    @Override
    public String getUniquePrimaryKey() {
        return getIdSituationProf();
    }

    /**
     * getter pour l'attribut delete prestations requis
     * 
     * @return la valeur courante de l'attribut delete prestations requis
     */
    public boolean isDeletePrestationsRequis() {
        return deletePrestationsRequis;
    }

    /**
     * Charge l'employeur avec lequel cette situation professionnelle est liée. Cette méthode recharge automatiquement
     * l'employeur si (et seulement si) la valeur de id employeur de ce bean a été modifiée.
     * 
     * @return l'employeur lié à ce droit (jamais null).
     * @throws Exception
     *             si l'employeur ne peut être chargé.
     */
    public APEmployeur loadEmployeur() throws Exception {
        // si l'employeur est null, instancier
        if (employeur == null) {
            employeur = new APEmployeur();
        }

        // on s'assure que la session est la bonne (pour les cas où on
        // chargerait le tiers...)
        employeur.setSession(getSession());

        // si l'employeur est différent, charger l'employeur
        if (!idEmployeur.equals(employeur.getIdEmployeur())) {
            employeur.setIdEmployeur(idEmployeur);
            employeur.retrieve(getSession().getCurrentThreadTransaction());
        }

        return employeur;
    }

    /**
     * setter pour l'attribut autre remuneration
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setAutreRemuneration(String string) {
        if (!string.equals(autreOuPourcentRemuneration)) {
            deletePrestationsRequis = true;
        }

        autreOuPourcentRemuneration = string;
    }

    /**
     * setter pour l'attribut autre salaire
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setAutreSalaire(String string) {
        if (!string.equals(autreSalaire)) {
            deletePrestationsRequis = true;
        }

        autreSalaire = string;
    }

    public void setCsAssuranceAssociation(String csAssuranceAssociation) {
        this.csAssuranceAssociation = csAssuranceAssociation;
    }

    /**
     * setter pour l'attribut date debut
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebut(String string) {
        if (!string.equals(dateDebut)) {
            deletePrestationsRequis = true;
        }

        dateDebut = string;
    }

    /**
     * setter pour l'attribut date fin
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateFin(String string) {
        if (!string.equals(dateFin)) {
            deletePrestationsRequis = true;
        }

        dateFin = string;
    }

    /**
     * setter pour l'attribut date fin contrat
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateFinContrat(String string) {
        if (!string.equals(dateFinContrat)) {
            deletePrestationsRequis = true;
        }

        dateFinContrat = string;
    }

    /**
     * setter pour l'attribut delete prestations requis
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setDeletePrestationsRequis(boolean b) {
        deletePrestationsRequis = b;
    }

    public void setHasAcmAlphaPrestations(Boolean hasAcmAlphaPrestations) {
        this.hasAcmAlphaPrestations = hasAcmAlphaPrestations;
    }

    public void setHasAcm2AlphaPrestations(Boolean hasAcm2AlphaPrestations) {
        this.hasAcm2AlphaPrestations = hasAcm2AlphaPrestations;
    }

    /**
     * @param boolean1
     */
    public void setHasLaMatPrestations(Boolean boolean1) {
        hasLaMatPrestations = boolean1;
    }

    /**
     * setter pour l'attribut heures semaine
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setHeuresSemaine(String string) {
        if (!string.equals(heuresSemaine)) {
            deletePrestationsRequis = true;
        }

        heuresSemaine = string;
    }

    /**
     * setter pour l'attribut id droit
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdDroit(String string) {
        idDroit = string;
    }

    /**
     * setter pour l'attribut id employeur
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdEmployeur(String string) {
        if (!string.equals(idEmployeur)) {
            deletePrestationsRequis = true;
        }

        idEmployeur = string;
    }

    /**
     * setter pour l'attribut id situation prof
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdSituationProf(String string) {
        idSituationProf = string;
    }

    /**
     * setter pour l'attribut is allocation exploitation
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsAllocationExploitation(Boolean boolean1) {
        if (!boolean1.equals(isAllocationExploitation)) {
            deletePrestationsRequis = true;
        }

        isAllocationExploitation = boolean1;
    }

    /**
     * setter pour l'attribut is allocation max
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsAllocationMax(Boolean boolean1) {
        if (!boolean1.equals(isAllocationMax)) {
            deletePrestationsRequis = true;
        }

        isAllocationMax = boolean1;
    }

    /**
     * setter pour l'attribut is collaborateur agricole
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsCollaborateurAgricole(Boolean boolean1) {
        if (!boolean1.equals(isCollaborateurAgricole)) {
            deletePrestationsRequis = true;
        }

        isCollaborateurAgricole = boolean1;
    }

    /**
     * setter pour l'attribut is independant
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsIndependant(Boolean boolean1) {
        if (!boolean1.equals(isIndependant)) {
            deletePrestationsRequis = true;
        }

        isIndependant = boolean1;
    }

    /**
     * setter pour l'attribut is non actif
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsNonActif(Boolean boolean1) {
        if (!boolean1.equals(isNonActif)) {
            deletePrestationsRequis = true;
        }

        isNonActif = boolean1;
    }

    public void setIsPorteEnCompte(Boolean isPorteEnCompte) {
        this.isPorteEnCompte = isPorteEnCompte;
    }

    /**
     * setter pour l'attribut is non soumis cotisation
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsNonSoumisCotisation(Boolean boolean1) {
        if (!boolean1.equals(isNonSoumisCotisation)) {
            deletePrestationsRequis = true;
        }

        isNonSoumisCotisation = boolean1;
    }

    /**
     * setter pour l'attribut is pourcent autre remun
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsPourcentAutreRemun(Boolean boolean1) {
        if (!boolean1.equals(isPourcentAutreRemun)) {
            deletePrestationsRequis = true;
        }

        isPourcentAutreRemun = boolean1;
    }

    /**
     * setter pour l'attribut is pourcent montant verse
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsPourcentMontantVerse(Boolean boolean1) {
        if (!boolean1.equals(isPourcentMontantVerse)) {
            deletePrestationsRequis = true;
        }

        isPourcentMontantVerse = boolean1;
    }

    /**
     * setter pour l'attribut is situation prof
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsSituationProf(Boolean boolean1) {
        if (!boolean1.equals(isSituationProf)) {
            deletePrestationsRequis = true;
        }

        isSituationProf = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setIsTravailleurAgricole(Boolean boolean1) {
        isTravailleurAgricole = boolean1;
    }

    /**
     * setter pour l'attribut is travailleur sans emploi
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsTravailleurSansEmploi(Boolean boolean1) {
        if (!boolean1.equals(isTravailleurSansEmploi)) {
            deletePrestationsRequis = true;
        }

        isTravailleurSansEmploi = boolean1;
    }

    /**
     * setter pour l'attribut is versement employeur
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsVersementEmployeur(Boolean boolean1) {
        if (!boolean1.equals(isVersementEmployeur)) {
            deletePrestationsRequis = true;
        }

        isVersementEmployeur = boolean1;
    }

    public void setMontantJournalierAcmNe(String montantJournalierAcmNe) {
        this.montantJournalierAcmNe = montantJournalierAcmNe;
    }

    /**
     * setter pour l'attribut montant verse
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantVerse(String string) {
        if (!string.equals(montantOuPourcentVerse)) {
            deletePrestationsRequis = true;
        }

        montantOuPourcentVerse = string;
    }

    /**
     * setter pour l'attribut perdiodicite autre remun
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setPeriodiciteAutreRemun(String string) {
        if (!string.equals(periodiciteAutreRemun)) {
            deletePrestationsRequis = true;
        }

        periodiciteAutreRemun = string;
    }

    /**
     * setter pour l'attribut periodicite autre salaire
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setPeriodiciteAutreSalaire(String string) {
        if (!string.equals(periodiciteAutreSalaire)) {
            deletePrestationsRequis = true;
        }

        periodiciteAutreSalaire = string;
    }

    /**
     * setter pour l'attribut periodicite montant verse
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setPeriodiciteMontantVerse(String string) {
        if (!string.equals(periodiciteMontantVerse)) {
            deletePrestationsRequis = true;
        }

        periodiciteMontantVerse = string;
    }

    /**
     * setter pour l'attribut periodicite salaire nature
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setPeriodiciteSalaireNature(String string) {
        if (!string.equals(periodiciteSalaireNature)) {
            deletePrestationsRequis = true;
        }

        periodiciteSalaireNature = string;
    }

    /**
     * setter pour l'attribut pourcent autre remun
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setPourcentAutreRemun(String string) {
        if (!string.equals(autreOuPourcentRemuneration)) {
            deletePrestationsRequis = true;
        }

        autreOuPourcentRemuneration = string;
    }

    /**
     * setter pour l'attribut pourcent montant verse
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setPourcentMontantVerse(String string) {
        if (!string.equals(montantOuPourcentVerse)) {
            deletePrestationsRequis = true;
        }

        montantOuPourcentVerse = string;
    }

    /**
     * setter pour l'attribut revenu independant
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setRevenuIndependant(String string) {
        if (!string.equals(revenuIndependant)) {
            deletePrestationsRequis = true;
        }

        revenuIndependant = string;
    }

    /**
     * setter pour l'attribut salaire horaire
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setSalaireHoraire(String string) {
        if (!string.equals(salaireHoraire)) {
            deletePrestationsRequis = true;
        }

        salaireHoraire = string;
    }

    /**
     * setter pour l'attribut salaire mensuel
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setSalaireMensuel(String string) {
        if (!string.equals(salaireMensuel)) {
            deletePrestationsRequis = true;
        }

        salaireMensuel = string;
    }

    /**
     * setter pour l'attribut salaire nature
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setSalaireNature(String string) {
        if (!string.equals(salaireNature)) {
            deletePrestationsRequis = true;
        }

        salaireNature = string;
    }

    public void setIdDomainePaiementEmployeur(String idDomainePaiementEmployeur) {
        this.idDomainePaiementEmployeur = idDomainePaiementEmployeur;
    }

    public void setIdTiersPaiementEmployeur(String idTiersPaiementEmployeur) {
        this.idTiersPaiementEmployeur = idTiersPaiementEmployeur;
    }

    @Override
    public String getIdDomainePaiementEmployeur() {
        return idDomainePaiementEmployeur;
    }

    @Override
    public String getIdTiersPaiementEmployeur() {
        return idTiersPaiementEmployeur;
    }

    /**
     * setter pour l'attribut unique primary key
     * 
     * @param pk
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setUniquePrimaryKey(String pk) {
        setIdSituationProf(pk);
    }

    @Override
    public String getAnneeTaxation() {
        return anneeTaxation;
    }

    public void setAnneeTaxation(String anneeTaxation) {
        this.anneeTaxation = anneeTaxation;
    }
}
