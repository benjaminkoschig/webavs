package globaz.phenix.db.principale;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.assurance.AFAssuranceManager;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.osiris.db.utils.CACumulOperationCotisationManager;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.communications.CPParametrePlausibilite;
import globaz.phenix.db.communications.CPValidationCalculCommunication;
import globaz.phenix.db.communications.CPValidationCalculCommunicationManager;
import globaz.phenix.db.divers.CPPeriodeFiscale;
import globaz.phenix.db.divers.CPPeriodeFiscaleManager;
import globaz.phenix.db.divers.CPTableIndependant;
import globaz.phenix.db.divers.CPTableNonActif;
import globaz.phenix.toolbox.CPToolBox;
import globaz.phenix.translation.CodeSystem;
import globaz.phenix.util.CPProperties;
import globaz.phenix.util.CPUtil;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TILocalite;
import globaz.pyxis.db.tiers.TICompositionTiers;
import globaz.pyxis.db.tiers.TICompositionTiersManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Insérez la description du type ici. Date de création : (10.05.2002 09:35:05)
 * 
 * @author: Administrator
 */
public class CPDecisionViewBean extends CPDecision implements FWViewBeanInterface {
    private static final long serialVersionUID = 1L;

    private String action;
    private java.lang.String anneeRevenuDebut = "";
    private java.lang.String anneeRevenuFin = "";

    private String autreFortune = "";
    private java.lang.String canton1 = "";
    private java.lang.String canton2 = "";
    private java.lang.String canton3 = "";
    private java.lang.String canton4 = "";
    private java.lang.String capital = "";
    private java.lang.String codeEtatDecision = "";
    private String colonneSelection = "";
    private java.lang.String communeCode = "";
    private java.lang.String communeLibelle = "";
    private java.lang.String conjoint = "";
    private java.lang.String cotisation1 = "";
    private java.lang.String cotisation2 = "";
    private String cotisationSalarie = "";
    private String dateDecision = "";
    private String dateFortune = "";
    private java.lang.String debutExercice1 = "";
    private java.lang.String debutExercice2 = "";
    private java.lang.String descriptionTiers = "";
    private String dette = "";
    private String docListe = "";
    // rajouter pour refactoring des actions phenix
    private Boolean duplicata = new Boolean(false);
    private java.lang.String etatDecision = "";
    private java.lang.String finExercice1 = "";
    private java.lang.String finExercice2 = "";
    private java.lang.String fortuneDeterminante = "";
    private String fortuneTotale = "";
    private String idValidation = "";
    // fin ajout
    private String libellePassage = "";
    private java.lang.String montantImmobilier1 = "";
    private java.lang.String montantImmobilier2 = "";
    private java.lang.String montantImmobilier3 = "";
    private java.lang.String montantImmobilier4 = "";
    private java.lang.String montantTotalRenteAVS = "";
    private java.lang.String nbMoisExercice1 = "";
    private java.lang.String nbMoisExercice2 = "";
    private java.lang.String nbMoisRevenuAutre1 = "";
    private java.lang.String nbMoisRevenuAutre2 = "";
    private java.lang.String numAffilie = "";
    private java.lang.String numIfdDefinitif = "";
    private java.lang.String periodicite = "";
    private Boolean processExterne = new Boolean(false);
    private java.lang.String revenu1 = "";
    private java.lang.String revenu2 = "";
    private java.lang.String revenuAutre1 = "";
    private java.lang.String revenuAutre2 = "";
    private String revenuCiForce = "";
    private Boolean revenuCiForce0 = new Boolean(false);
    private java.lang.String revenuDeterminant = "";
    private float saveCompteurPourRemise = 0;
    private java.lang.String selection = "";
    private java.lang.String selectionCjt = "";
    private java.lang.String sourceInformation = "";
    private globaz.pyxis.db.tiers.TITiersViewBean tiers = null;
    private java.lang.String typeTerrain1 = "";
    private java.lang.String typeTerrain2 = "";
    private java.lang.String typeTerrain3 = "";

    private java.lang.String typeTerrain4 = "";
    private String wantWarning = "";
    private String warningMessage = "";

    // D0205
    private String rachatLPP = "";

    /*
     * Liste les plans de passage
     */
    public static Vector<?> getUserList(javax.servlet.http.HttpSession httpSession) {
        return CPUtil.getUserList(httpSession);
    }

    @Override
    public void _afterAdd(BTransaction transaction) throws Exception {
        // Mise à jour des données encodées
        if (!JadeStringUtil.isIntegerEmpty(getIdDecision())) {
            CPDonneesBase donnees = new CPDonneesBase();
            donnees.setSession(getSession());
            donnees.setIdDecision(getIdDecision());
            donnees.setRevenu1(getRevenu1());
            donnees.setMontantTotalRenteAVS(getMontantTotalRenteAVS());
            donnees.setRevenuAutre1(getRevenuAutre1());
            donnees.setSourceInformation(getSourceInformation());

            donnees.setRachatLPP(getRachatLPP());

            String coti = JANumberFormatter.deQuote(getCotisation1());
            if (!JadeStringUtil.isEmpty(coti)) {
                if (!CPDecision.CS_IMPUTATION.equalsIgnoreCase(getTypeDecision())
                        && !CPDecision.CS_REMISE.equalsIgnoreCase(getTypeDecision())
                        && !CPDecision.CS_REDUCTION.equalsIgnoreCase(getTypeDecision())) {
                    donnees.setCotisation1(JANumberFormatter.fmt(coti, true, false, true, 0));
                } else {
                    donnees.setCotisation1(Float.toString(JANumberFormatter.round(Float.parseFloat(coti), 0.05, 2,
                            JANumberFormatter.NEAR)));
                }
            }
            if (!getTypeDecision().equalsIgnoreCase(CPDecision.CS_IMPUTATION)) {
                if (getTaxation().equalsIgnoreCase("A")) {
                    donnees.setRevenu2(getRevenu2());
                    donnees.setRevenuAutre2(getRevenuAutre2());
                } else {
                    donnees.setRevenu2("");
                    donnees.setCotisation2("");
                    donnees.setDebutExercice2("");
                    donnees.setFinExercice2("");
                    donnees.setRevenuAutre2("");
                    donnees.setNbMoisRevenuAutre2("");
                    donnees.setNbMoisExercice2("");
                }
                if (isNonActif()) {
                    donnees.setDebutExercice1("");
                    donnees.setFinExercice1("");
                    donnees.setNbMoisExercice1(getNbMoisExercice1());
                    donnees.setNbMoisRevenuAutre1(getNbMoisRevenuAutre1());
                    donnees.setDateFortune(getDateFortune());
                    donnees.setFortuneTotale(getFortuneTotale());
                    donnees.setCotisationSalarie(getCotisationSalarie());
                    if (!CPDecision.CS_REMISE.equalsIgnoreCase(getTypeDecision())) {
                        donnees.setCotisation1("");
                    }
                    donnees.setCotisation2("");
                    donnees.setCapital("");
                    if (getTaxation().equalsIgnoreCase("A")) {
                        donnees.setDebutExercice2("");
                        donnees.setFinExercice2("");
                        donnees.setNbMoisExercice2(getNbMoisExercice2());
                        donnees.setNbMoisRevenuAutre2(getNbMoisRevenuAutre2());
                    }
                } else {
                    // Indépendant
                    donnees.setDebutExercice1(getDebutExercice1());
                    donnees.setFinExercice1(getFinExercice1());
                    donnees.setNbMoisExercice1("");
                    donnees.setNbMoisRevenuAutre1("");
                    if (!JadeStringUtil.isEmpty(coti)) {
                        if (CPDecision.CS_REMISE.equalsIgnoreCase(getTypeDecision())
                                || CPDecision.CS_REDUCTION.equalsIgnoreCase(getTypeDecision())) {
                            donnees.setCotisation1(Float.toString(JANumberFormatter.round(Float.parseFloat(coti), 0.05,
                                    2, JANumberFormatter.NEAR)));
                        } else {
                            donnees.setCotisation1(JANumberFormatter.fmt(getCotisation1(), true, false, true, 0));
                        }
                    }
                    donnees.setCapital(getCapital());
                    donnees.setDateFortune("");
                    donnees.setFortuneTotale("");
                    if (getTaxation().equalsIgnoreCase("A")) {
                        donnees.setCotisation2(JANumberFormatter.fmt(getCotisation2(), true, false, true, 0));
                        donnees.setDebutExercice2(getDebutExercice2());
                        donnees.setFinExercice2(getFinExercice2());
                        donnees.setNbMoisExercice2("");
                        donnees.setNbMoisRevenuAutre2("");
                    }
                }
            }
            // Montants forcés
            donnees.setRevenuCiForce(getRevenuCiForce());
            donnees.setRevenuCiForce0(getRevenuCiForce0());
            // Mise à jour
            donnees.add(transaction);
            if (donnees.hasErrors()) {
                _addError(transaction, getSession().getLabel("CP_MSG_0044"));
            }
            // 24.05.2005:BTC
            // Effacer la sortie de la même année si le passage de la sortie est
            // le même que celui de la décision
            if (Boolean.FALSE.equals(getProcessExterne())) {
                CPSortieManager sortieManager = new CPSortieManager();
                sortieManager.setSession(transaction.getSession());
                sortieManager.setForAnnee(getAnneeDecision());
                // PO 5265
                sortieManager.setForIdAffiliation(getIdAffiliation());
                // Effacer les montants cot pers et CI de sortie
                sortieManager.find(transaction);
                for (int k = 0; k < sortieManager.size(); k++) {
                    CPSortie mySortie = (CPSortie) sortieManager.getEntity(k);
                    mySortie.delete(transaction);
                    if (mySortie.hasErrors()) {
                        _addError(transaction, getSession().getLabel("CP_MSG_0046"));
                    }
                }
            }
        }
    }

    @Override
    public void _afterRetrieve(BTransaction transaction) throws Exception {
        try {
            // Recherche des données de l'affilié
            TITiersViewBean persAvs = new TITiersViewBean();
            persAvs.setSession(getSession());
            persAvs.setIdTiers(getIdTiers());
            persAvs.retrieve();
            setTiers(persAvs);
            // Recherche des données de base pour l'encodage
            if (!JadeStringUtil.isIntegerEmpty(getIdDecision())) {
                CPDonneesBase donBase = new CPDonneesBase();
                donBase.setSession(getSession());
                donBase.setIdDecision(getIdDecision());
                donBase.retrieve();
                setRevenu1(donBase.getRevenu1());
                setRevenu2(donBase.getRevenu2());
                setMontantTotalRenteAVS(donBase.getMontantTotalRenteAVS());
                setRevenuAutre1(donBase.getRevenuAutre1());
                setRevenuAutre2(donBase.getRevenuAutre2());
                setDebutExercice1(donBase.getDebutExercice1());
                setDebutExercice2(donBase.getDebutExercice2());
                setFinExercice1(donBase.getFinExercice1());
                setFinExercice2(donBase.getFinExercice2());
                setNbMoisExercice1(donBase.getNbMoisExercice1());
                setNbMoisExercice2(donBase.getNbMoisExercice2());
                setNbMoisRevenuAutre1(donBase.getNbMoisRevenuAutre1());
                setNbMoisRevenuAutre2(donBase.getNbMoisRevenuAutre2());
                setSourceInformation(donBase.getSourceInformation());

                setRachatLPP(donBase.getRachatLPP());

                setCotisation1(donBase.getCotisation1());
                if (isNonActif()) {
                    setCotisationSalarie(donBase.getCotisationSalarie());
                    setDateFortune(donBase.getDateFortune());
                    setFortuneTotale(donBase.getFortuneTotale(0));
                    if (!CPDecision.CS_IMPUTATION.equalsIgnoreCase(getTypeDecision())) {
                        setCotisation1("");
                    }

                } else {
                    setCapital(donBase.getCapital());
                    if (JadeStringUtil.isIntegerEmpty(donBase.getCotisation1())) {
                        setCotisation1("");
                    } else if (JadeStringUtil.isIntegerEmpty(donBase.getCotisation2())) {
                        setCotisation2("");
                    } else {
                        setCotisation2(donBase.getCotisation2());
                    }
                }
                // Si imputation
                if (CPDecision.CS_REMISE.equalsIgnoreCase(getTypeDecision())) {
                    setCotisation1(donBase.getCotisation1());
                    setCommuneCode("");
                    setCommuneLibelle("");
                    if (!JadeStringUtil.isEmpty(getIdCommune())) {
                        // Extraction des données de la commune
                        TILocalite commune = new TILocalite();
                        commune.setIdLocalite(getIdCommune());
                        commune.setSession(getSession());
                        commune.retrieve();
                        if (!commune.isNew()) {
                            setCommuneCode(commune.getNumPostalEntier());
                            setCommuneLibelle(commune.getLocalite());
                        }
                    }
                }
                setRevenuCiForce(donBase.getRevenuCiForce());
                setRevenuCiForce0(donBase.isRevenuCiForce0());
            }
            // Recherche des données de la période fiscale IFD définitif (de base)
            CPPeriodeFiscale perFis = new CPPeriodeFiscale();
            perFis.setSession(getSession());
            perFis.setIdIfd(getIdIfdDefinitif());
            perFis.retrieve();
            setNumIfdDefinitif(perFis.getNumIfd());
            // Ifd de base des revenus
            perFis.setIdIfd(getIdIfdProvisoire());
            perFis.retrieve();
            setAnneeRevenuDebut(perFis.getAnneeRevenuDebut());
            setAnneeRevenuFin(perFis.getAnneeRevenuFin());
            // Recherche dernier état de la décision
            setEtatDecision(globaz.phenix.translation.CodeSystem.getLibelle(getSession(), getDernierEtat()));
            setCodeEtatDecision(getDernierEtat());
            // Recherche periode de facturation
            setPeriodicite(globaz.phenix.translation.CodeSystem.getLibelle(getSession(), (loadAffiliation()
                    ._getDerniereAffiliation().getPeriodicite())));
            // Recherche du revenu déterminant
            CPDonneesCalcul donnee = new CPDonneesCalcul();
            donnee.setSession(getSession());
            setRevenuDeterminant(donnee.getMontant(getIdDecision(), CPDonneesCalcul.CS_REV_NET));
            // Recherche de la fortune déterminante
            setFortuneDeterminante(donnee.getMontant(getIdDecision(), CPDonneesCalcul.CS_FORTUNE_TOTALE));
            // Recherche du conjoint
            if (!JadeStringUtil.isIntegerEmpty(getIdConjoint())) {
                TITiersViewBean conjoint = new TITiersViewBean();
                conjoint.setSession(getSession());
                conjoint.setIdTiers(getIdConjoint());
                conjoint.retrieve();
                setConjoint(conjoint.getNom());
                setIdConjoint(conjoint.getIdTiers());
                AFAffiliation affiCjt = CPToolBox.returnAffiliation(getSession(), getSession()
                        .getCurrentThreadTransaction(), getIdConjoint(), getAnneeDecision(), "", 1);
                if (affiCjt != null) {
                    setSelectionCjt(affiCjt.getAffilieNumero());
                }
            }
            // Si remise => allez rechercher le montant du compteur avs
            String role = CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(getSession().getApplication());
            CACompteAnnexe compte = new CACompteAnnexe();
            compte.setSession(getSession());
            compte.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
            compte.setIdRole(role);
            compte.setIdExterneRole(loadAffiliation().getAffilieNumero());
            compte.wantCallMethodBefore(false);
            compte.retrieve();
            if (!compte.isNew()) {
                AFCotisation cotiAvs = new AFCotisation();
                cotiAvs.setSession(getSession());
                cotiAvs = cotiAvs._retourCotisation(getSession().getCurrentThreadTransaction(), loadAffiliation()
                        .getAffiliationId(), getAnneeDecision(),
                        globaz.naos.translation.CodeSystem.GENRE_ASS_PERSONNEL,
                        globaz.naos.translation.CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
                if (cotiAvs != null) {
                    String rubriqueAvs = cotiAvs.getAssurance().getRubriqueId();
                    String varString = CPToolBox
                            .rechMontantFacture(getSession(), getSession().getCurrentThreadTransaction(),
                                    compte.getIdCompteAnnexe(), rubriqueAvs, getAnneeDecision());
                    if (!JadeStringUtil.isBlankOrZero(varString)) {
                        setSaveCompteurPourRemise(Float.parseFloat(JANumberFormatter.deQuote(varString)));
                    }
                }
            }
        } catch (Exception e) {
            _addError(transaction, e.getMessage());
        }
    }

    @Override
    public void _afterUpdate(BTransaction transaction) throws Exception {
        // Mise à jour des données encodées
        if (!JadeStringUtil.isIntegerEmpty(getIdDecision())) {
            CPDonneesBase donnees = new CPDonneesBase();
            donnees.setSession(getSession());
            donnees.setIdDecision(getIdDecision());
            donnees.setRevenu1(getRevenu1());
            donnees.setMontantTotalRenteAVS(getMontantTotalRenteAVS());
            donnees.setRevenuAutre1(getRevenuAutre1());
            donnees.setSourceInformation(getSourceInformation());

            donnees.setRachatLPP(getRachatLPP());

            String coti = JANumberFormatter.deQuote(getCotisation1());
            if (!JadeStringUtil.isEmpty(coti)) {
                if (!CPDecision.CS_IMPUTATION.equalsIgnoreCase(getTypeDecision())
                        && !CPDecision.CS_REMISE.equalsIgnoreCase(getTypeDecision())
                        && !CPDecision.CS_REDUCTION.equalsIgnoreCase(getTypeDecision())) {
                    donnees.setCotisation1(JANumberFormatter.fmt(coti, true, false, true, 0));
                } else {
                    donnees.setCotisation1(Float.toString(JANumberFormatter.round(Float.parseFloat(coti), 0.05, 2,
                            JANumberFormatter.NEAR)));
                }
            }
            if (!getTypeDecision().equalsIgnoreCase(CPDecision.CS_IMPUTATION)) {
                if (getTaxation().equalsIgnoreCase("A")) {
                    donnees.setRevenu2(getRevenu2());
                    donnees.setRevenuAutre2(getRevenuAutre2());
                } else {
                    donnees.setRevenu2("");
                    donnees.setCotisation2("");
                    donnees.setDebutExercice2("");
                    donnees.setFinExercice2("");
                    donnees.setRevenuAutre2("");
                    donnees.setNbMoisRevenuAutre2("");
                    donnees.setNbMoisExercice2("");
                }
                if (isNonActif()) {
                    donnees.setDebutExercice1("");
                    donnees.setFinExercice1("");
                    donnees.setNbMoisExercice1(getNbMoisExercice1());
                    donnees.setNbMoisRevenuAutre1(getNbMoisRevenuAutre1());
                    donnees.setDateFortune(getDateFortune());
                    donnees.setFortuneTotale(getFortuneTotale());
                    donnees.setCotisationSalarie(getCotisationSalarie());
                    if (!CPDecision.CS_REMISE.equalsIgnoreCase(getTypeDecision())) {
                        donnees.setCotisation1("");
                    }
                    donnees.setCotisation2("");
                    donnees.setCapital("");
                    // Si calcul de la fortune détaillée
                    if ((((CPApplication) getSession().getApplication()).isCPCALCIMMO()) && isProvisoireMetier()) {
                        CPFortune fortune = new CPFortune();
                        fortune.setSession(getSession());
                        try {
                            fortune.setIdDecision(getIdDecision());
                            fortune.setMontantImmobilier1(getMontantImmobilier1());
                            fortune.setMontantImmobilier2(getMontantImmobilier2());
                            fortune.setMontantImmobilier3(getMontantImmobilier3());
                            fortune.setMontantImmobilier4(getMontantImmobilier4());
                            fortune.setCanton1(getCanton1());
                            fortune.setCanton2(getCanton2());
                            fortune.setCanton3(getCanton3());
                            fortune.setCanton4(getCanton4());
                            fortune.setTypeTerrain1(getTypeTerrain1());
                            fortune.setTypeTerrain2(getTypeTerrain2());
                            fortune.setTypeTerrain3(getTypeTerrain3());
                            fortune.setTypeTerrain4(getTypeTerrain4());
                            fortune.setDette(getDette());
                            fortune.setAutreFortune(getAutreFortune());
                            fortune.add(transaction);
                        } catch (Exception e) {
                            _addError(transaction, getSession().getLabel("CP_MSG_0044"));
                            _addError(transaction, e.getMessage());
                        }
                    }
                    if ("A".equalsIgnoreCase(getTaxation())) {
                        donnees.setDebutExercice2("");
                        donnees.setFinExercice2("");
                        donnees.setNbMoisExercice2(getNbMoisExercice2());
                        donnees.setNbMoisRevenuAutre2(getNbMoisRevenuAutre2());
                    }
                } else {
                    // Indépendant
                    donnees.setDebutExercice1(getDebutExercice1());
                    donnees.setFinExercice1(getFinExercice1());
                    donnees.setNbMoisExercice1("");
                    donnees.setNbMoisRevenuAutre1("");
                    if (CPDecision.CS_REMISE.equalsIgnoreCase(getTypeDecision())
                            || CPDecision.CS_REDUCTION.equalsIgnoreCase(getTypeDecision())) {
                        if (!JadeStringUtil.isEmpty(coti)) {
                            donnees.setCotisation1(Float.toString(JANumberFormatter.round(Float.parseFloat(coti), 0.05,
                                    2, JANumberFormatter.NEAR)));
                        } else {
                            donnees.setCotisation1("");
                        }
                    } else {
                        donnees.setCotisation1(JANumberFormatter.fmt(coti, true, false, true, 0));
                    }
                    donnees.setCapital(getCapital());
                    donnees.setDateFortune("");
                    donnees.setFortuneTotale("");
                    if ("A".equalsIgnoreCase(getTaxation())) {
                        donnees.setCotisation2(JANumberFormatter.fmt(getCotisation2(), true, false, true, 0));
                        donnees.setDebutExercice2(getDebutExercice2());
                        donnees.setFinExercice2(getFinExercice2());
                        donnees.setNbMoisExercice2("");
                        donnees.setNbMoisRevenuAutre2("");
                    }
                }
            }
            // Montants forcés
            donnees.setRevenuCiForce(getRevenuCiForce());
            donnees.setRevenuCiForce0(getRevenuCiForce0());
            // Mise à jour
            transaction.disableSpy();
            donnees.update(transaction);
            transaction.enableSpy();
            if (donnees.hasErrors()) {
                _addError(transaction, getSession().getLabel("CP_MSG_0045"));
            }
            //
            setDernierEtat(CPDecision.CS_CREATION);
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    public void _controle(BTransaction transaction) {
        // L'année de décision ne peut être supèrieure à l'année en cours +1
        int anneeDec = Integer.parseInt(getAnneeDecision());
        try {
            int anneeLimite = JACalendar.getYear(JACalendar.today().toString()) + 1;
            if (anneeDec > anneeLimite) {
                _addError(transaction, getSession().getLabel("CP_MSG_0047") + " " + Integer.toString(anneeLimite));
            }
            AFAffiliation affiliation = new AFAffiliation();
            affiliation.setSession(getSession());
            // Formatage du code saisi
            String numAff = "";
            try {
                TIApplication app = (TIApplication) GlobazServer.getCurrentSystem().getApplication("PYXIS");
                IFormatData affilieFormater = app.getAffileFormater();
                if (affilieFormater != null) {
                    numAff = affilieFormater.format(selection);
                }
            } catch (Exception e) {
                _addError(transaction, getSession().getLabel("CP_MSG_0048") + " " + e.getMessage());
            }
            boolean erreurDate = false;
            // PO 4682
            if (tiers != null) {
                if (!JadeStringUtil.isEmpty(tiers.getDateNaissance())) {
                    try {
                        JADate dDate = new JADate(tiers.getDateNaissance());
                        BSessionUtil.checkDateGregorian(getSession(), dDate);
                    } catch (Exception ex) {
                        erreurDate = true;
                    }
                } else {
                    erreurDate = true;
                }
                if (erreurDate) {
                    setMsgType(FWViewBeanInterface.ERROR);
                    setMessage(getSession().getLabel("DATE_DE_NAISSANCE_ERRONEE") + " - " + tiers.getDateNaissance()
                            + " - " + numAff);

                }
            }
            // Test si changement d'affilié (saisie de masse)
            if (JadeStringUtil.isIntegerEmpty(getIdAffiliation())) {
                affiliation = CPToolBox.returnAffiliation(getSession(), transaction, numAff, getAnneeDecision(), "", 0);
                if (affiliation == null) {
                    _addError(transaction, getSession().getLabel("CP_MSG_0049"));
                } else { // Charger les données du nouveau tiers
                    TITiersViewBean persAvs = new TITiersViewBean();
                    persAvs.setSession(getSession());
                    persAvs.setIdTiers(affiliation.getIdTiers());
                    persAvs.retrieve();
                    setTiers(persAvs);
                    erreurDate = false;
                    // PO 4682
                    if (!JadeStringUtil.isEmpty(persAvs.getDateNaissance())) {
                        try {
                            JADate dDate = new JADate(persAvs.getDateNaissance());
                            BSessionUtil.checkDateGregorian(getSession(), dDate);
                        } catch (Exception ex) {
                            erreurDate = true;
                        }
                    } else {
                        erreurDate = true;
                    }
                    if (erreurDate) {
                        setMsgType(FWViewBeanInterface.ERROR);
                        setMessage(getSession().getLabel("DATE_DE_NAISSANCE_ERRONEE") + " - "
                                + persAvs.getDateNaissance() + " - " + affiliation.getAffilieNumero());

                    }
                    setIdAffiliation(affiliation.getAffiliationId());
                    setIdTiers(persAvs.getIdTiers());
                }
            } else {
                // Impossible de créer une décision pour une affiliation
                // provisoire
                affiliation.setAffiliationId(getIdAffiliation());
                affiliation.setIdTiers(getIdTiers());
                try {
                    affiliation.retrieve(transaction);
                } catch (Exception e) {
                    e.printStackTrace();
                    _addError(transaction, getSession().getLabel("CP_MSG_0050"));
                }
            }
            if (affiliation != null) {
                if (affiliation.isTraitement().booleanValue()) {
                    _addError(transaction, getSession().getLabel("CP_MSG_0051"));
                }
                try {
                    if (CPDecision._checkGenreDecisionAvecAffiliation(getGenreAffilie(), affiliation)) {
                        // Imputation autorisé seulement pour les non actif
                        if (CPDecision.CS_IMPUTATION.equalsIgnoreCase(getTypeDecision()) && !isNonActif()) {
                            _addError(
                                    transaction,
                                    getSession().getLabel("CP_MSG_0052") + " "
                                            + CodeSystem.getLibelle(getSession(), getTypeDecision()) + " "
                                            + getSession().getLabel("CP_MSG_0052A") + " "
                                            + CodeSystem.getLibelle(getSession(), CPDecision.CS_NON_ACTIF) + ". ");
                        }
                        // Test si l'année de décision est comprise dans la
                        // période d'activité de l'affilié
                        int anneeDecision = Integer.parseInt(getAnneeDecision());
                        if (!JadeStringUtil.isBlank(affiliation.getDateDebut())) {
                            int anneeDebut = globaz.globall.util.JACalendar.getYear(affiliation.getDateDebut());
                            if (anneeDebut > anneeDecision) {
                                _addError(transaction, getSession().getLabel("CP_MSG_0053"));
                            }
                        }
                        if (!JadeStringUtil.isBlank(affiliation.getDateFin())) {
                            int anneeFin = globaz.globall.util.JACalendar.getYear(affiliation.getDateFin());
                            if (anneeDecision > anneeFin) {
                                _addError(transaction, getSession().getLabel("CP_MSG_0054"));
                            }
                        }
                        // Pour non actif vérifier si l'affilié n'est pas à
                        // l'âge AVS - Contrôle nécessaire car il se peut
                        // que la date de radiation ne soit pas renseignée.
                        if (!JadeStringUtil.isIntegerEmpty(affiliation.getAffiliationId())) {
                            if (globaz.naos.translation.CodeSystem.TYPE_AFFILI_NON_ACTIF.equalsIgnoreCase(affiliation
                                    .getTypeAffiliation())
                                    || globaz.naos.translation.CodeSystem.TYPE_AFFILI_SELON_ART_1A
                                            .equalsIgnoreCase(affiliation.getTypeAffiliation())) {
                                // Cas âge AVS
                                int anneeAvs = 0;
                                try {
                                    String dateAvs = loadTiers().getDateAvs();
                                    if (dateAvs != null) {
                                        if ("0.00".equals(dateAvs.substring(0, 4))) {
                                            anneeAvs = new Integer(dateAvs.substring(5, 9)).intValue();
                                        } else {
                                            anneeAvs = JACalendar.getYear(dateAvs);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    anneeAvs = 0;
                                }
                                if (anneeAvs < anneeDecision) {
                                    _addError(transaction, getSession().getLabel("CP_MSG_0055"));
                                }
                            }
                        }
                    } else {
                        _addError(transaction, getSession().getLabel("CP_MSG_0056"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    _addError(transaction, getSession().getLabel("CP_MSG_0057"));
                }
            } else {
                _addError(transaction, getSession().getLabel("CP_MSG_0058"));
            }
            // Test si l'année de décision demandé appartient à une période
            // fiscale définie
            CPPeriodeFiscaleManager perFis = new CPPeriodeFiscaleManager();
            perFis.setSession(getSession());
            perFis.setForAnneeDecisionDebut(getAnneeDecision());
            try {
                perFis.find(transaction);
                if (perFis.size() == 0) {
                    _addError(transaction, getSession().getLabel("CP_MSG_0059"));
                }
            } catch (Exception e) {
                _addError(transaction, getSession().getLabel("CP_MSG_0060"));
            }
        } catch (Exception e) {
            _addError(transaction, getSession().getLabel("CP_MSG_0061") + " " + e.toString());
        }
    }

    /*
     * Contrôle de l'existence du n° d'affilié
     */
    public String _ctrlCodeSaisi(BTransaction transaction, String id, String code) throws Exception {
        // Test si l'id correspond bien au code saisi sinon cela implique qu'une
        // saisie a été effectué
        // et qu'il faut aller rechercher id selon ce code saisi.
        // En création, l'id peut être à blanc donc ce test n'est pas à faire
        // formatage du numero selon caisse
        String numAff = code;
        try {
            TIApplication app = (TIApplication) GlobazServer.getCurrentSystem().getApplication("PYXIS");
            IFormatData affilieFormater = app.getAffileFormater();
            if (affilieFormater != null) {
                code = affilieFormater.format(numAff);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // fin du formatage
        try {
            AFAffiliationManager histoManager = new AFAffiliationManager();
            histoManager.setSession(getSession());
            if (!JadeStringUtil.isEmpty(id)) {
                // Recherche du code selon l'id
                histoManager.setForIdTiers(id);

                histoManager.find(transaction);
                // Test si le numéro avs correspond à l'id
                for (int i = 0; i < histoManager.size(); i++) {
                    if (((AFAffiliation) histoManager.getEntity(i)).getAffilieNumero().equalsIgnoreCase(code)) {
                        return (id);
                    }
                }
            }
            // Recherche de l'id correspondant au code saisi
            histoManager.setForAffilieNumero(code);
            histoManager.setForIdTiers("");
            histoManager.find(transaction);
            if (histoManager.size() == 1) {
                AFAffiliation histo = (AFAffiliation) histoManager.getEntity(0);
                return (histo.getIdTiers());
            } else if (histoManager.size() > 1) {
                _addError(transaction, getSession().getLabel("CP_MSG_0063"));
                return "";
            } else {
                _addError(transaction, getSession().getLabel("CP_MSG_0064"));
                return "";
            }
        } catch (Exception e) {
            _addError(transaction, getSession().getLabel("CP_MSG_0065"));
            return "";
        }
    }

    /**
     * Contrôle si le type de décision est autorisé
     */
    public void _ctrlTypeDecision(globaz.globall.db.BTransaction transaction) {
        try {
            // Une décision provisoire ne peut être faite qu'une fois par année
            if (getTypeDecision().equalsIgnoreCase(CPDecision.CS_PROVISOIRE)) {
                CPDecisionManager decisManager = new CPDecisionManager();
                decisManager.setSession(getSession());
                decisManager.setForAnneeDecision(getAnneeDecision());
                decisManager.setForIdTiers(loadTiers().getIdTiers());
                decisManager.setForTypeDecision(getTypeDecision());
                decisManager.orderByAnneeDecision();
                decisManager.orderByIdDecision();
                if (transaction == null) {
                    decisManager.find();
                } else {
                    decisManager.find(transaction);
                }
                if (decisManager.getSize() > 0) {
                }
            }
        } catch (Exception e) {
            _addError(transaction, getSession().getLabel("CP_MSG_0062") + " " + e.toString());

        }
    }

    @Override
    public void _init() {
        try {
            // Test nécessaire en cas de saisie de masse (changement d'affilié)
            // sinon on réinitialise
            // avec l'ancien et on perd le nouveau n° d'affilié qui a été saisi
            if ((JadeStringUtil.isBlank(getSelection()) || (getSelection().equalsIgnoreCase(numAffilie)))) {
                // -------- Recherche des données de l'affilié --------
                if (!JadeStringUtil.isEmpty(getIdTiers())) {
                    TITiersViewBean persAvs = new TITiersViewBean();
                    persAvs.setSession(getSession());
                    persAvs.setIdTiers(getIdTiers());
                    persAvs.retrieve();
                    setTiers(persAvs);
                    setDescriptionTiers(persAvs.getNom());
                }
                if (!JadeStringUtil.isEmpty(getIdAffiliation())) {
                    AFAffiliation affi = new AFAffiliation();
                    affi.setSession(getSession());
                    affi.setAffiliationId(getIdAffiliation());
                    affi.retrieve();
                    setAffiliation(affi);
                    setSelection(affi.getAffilieNumero());
                    setNumAffilie(affi.getAffilieNumero());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            setSelection("");
            setDescriptionTiers("");
            setNumAffilie("");
        }
    }

    /**
     * préparation de l'écran des décisions lors d'une demande de création
     * 
     * @throws Exception
     */
    public void _initEcran() throws Exception {
        // Sauvegarde de l'année de décision
        setAnneeDecision(getAnneeDecision());
        setFacturation(new Boolean(true));
        CACompteAnnexe compte = new CACompteAnnexe();
        try {
            int anneeDecision = Integer.parseInt(getAnneeDecision());
            // Responsable = user par défaut
            setResponsable(getSession().getUserId());
            // Impression
            setImpression(Boolean.TRUE);
            // date d'information = date du jour
            setDateInformation(JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY));
            // Etat = création
            try {
                setEtatDecision(globaz.phenix.translation.CodeSystem.getLibelle(getSession(), CPDecision.CS_CREATION));
            } catch (Exception e) {
                e.printStackTrace();
                setEtatDecision("");
            }
            // Détermination du mode de taxation
            int anneeChgt = 0;
            try {
                anneeChgt = ((CPApplication) getSession().getApplication()).getAnneeChangement();
            } catch (Exception e) {
                _addError(null, getSession().getLabel("CP_MSG_0066") + " " + e.getMessage());
            }
            if (anneeDecision < anneeChgt) {
                setTaxation("A");
            } else {
                setTaxation("N");
            }
            // Initialisation de la période de décision
            // En premier lieu on se base sur la période d'assurance AVS/AI/APG
            // Si celle-ci n'existe pas (Eex de la FER qui n'a que la lamat car
            // l'affilié paye ces cotis à une autre caisse)
            // on se base alors sur la période d'affiliation
            int anneeDebut = 0;
            int anneeFin = 0;
            try {
                setPeriodicite(globaz.phenix.translation.CodeSystem.getLibelle(getSession(), loadAffiliation()
                        ._getDerniereAffiliation().getPeriodicite()));
            } catch (Exception e) {
                setPeriodicite("");
            }
            // Recherche assurance AVS/AI/APG
            AFCotisation cotiAvs = new AFCotisation();
            cotiAvs.setSession(getSession());
            cotiAvs = cotiAvs._retourCotisation(null, loadAffiliation().getAffiliationId(), getAnneeDecision(),
                    globaz.naos.translation.CodeSystem.GENRE_ASS_PERSONNEL,
                    globaz.naos.translation.CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
            String debRef = "";
            String finRef = "";
            setDebutDecision("01.01." + anneeDecision);
            setFinDecision("31.12." + anneeDecision);

            /**
             * Mandat Meroba Cot. Pers. autre agence Permet de ne pas cocher la case Facturation sur l'écran Placé à cet
             * endroit car la méthode isPartCotPersAutreAgence() utilise debutDecision et finDecision qui sont settés
             * ci-dessus
             */
            if (AFParticulariteAffiliation.existeParticularite(getSession(), getIdAffiliation(),
                    globaz.naos.translation.CodeSystem.PARTIC_AFFILIE_COT_PERS_AUTRE_AGENCE, getDebutDecision())) {
                setFacturation(new Boolean(false));
            }
            if (cotiAvs == null) {
                debRef = loadAffiliation().getDateDebut();
                finRef = loadAffiliation().getDateFin();
            } else {
                debRef = cotiAvs.getDateDebut();
                finRef = cotiAvs.getDateFin();
            }
            // Tests si décision comprise dans les dates de référence
            // (affiliation ou cotisation AVS)
            if (!JadeStringUtil.isBlank(debRef)) {
                anneeDebut = globaz.globall.util.JACalendar.getYear(debRef);
                // Si année d'activité = année de décision => debut période
                // décision = début d'activité
                if (anneeDebut == anneeDecision) {
                    setDebutDecision(debRef);
                    setDebutActivite(new Boolean(true));
                }
            } // Si fin d'activité
            if (!JadeStringUtil.isBlank(finRef)) {
                anneeFin = globaz.globall.util.JACalendar.getYear(finRef);
                // année de décision = année de fin d'activité > fin période
                // décision = fin d'activité
                if (anneeFin == anneeDecision) {
                    setFinDecision(finRef);
                }
            }
            int anneeDebAffiliation = JACalendar.getYear(loadAffiliation().getDateDebut());
            // Recherche période fiscale et date de fortune
            _initPeriodeFiscale(this, getAnneeDecision(), anneeDebAffiliation);
            // Recherche du mode d'encodage, si revenu mensuel ou annuel encodé
            // (properties)
            String typeRevenu = ((CPApplication) getSession().getApplication()).getTypeRevenu();
            String rubriqueAvs = "";
            if (cotiAvs != null) {
                rubriqueAvs = cotiAvs.getAssurance().getRubriqueId();
            }
            // --------- Recherche des données du tiers
            // --------------------------------------------------------
            TITiersViewBean persAvs = new TITiersViewBean();
            persAvs.setSession(getSession());
            persAvs.setIdTiers(getIdTiers());
            try {
                persAvs.retrieve();
                setTiers(persAvs);
                String dateAgeAvs = persAvs.getDateAvs();
                int anneeAvs = JACalendar.getYear(dateAgeAvs);
                // Cas âge AVS
                int anneeDec = JACalendar.getYear(getDebutDecision());
                // Si non actif
                if (isNonActif()) {
                    if ((anneeAvs == anneeDec)
                            && BSessionUtil.compareDateFirstGreater(getSession(), getFinDecision(), dateAgeAvs)) {
                        setFinDecision(dateAgeAvs);
                    }
                } else {
                    // Si indépendant
                    // Détermination si rentier
                    if ((anneeAvs < anneeDec)
                            || ((anneeAvs == anneeDec) && BSessionUtil.compareDateFirstGreater(getSession(),
                                    getFinDecision(), dateAgeAvs))) {
                        setGenreAffilie(CPDecision.CS_RENTIER);
                    }
                }
                // Formatage périodes exercices 1 et 2
                if (getTaxation().equalsIgnoreCase("A")) {
                    if (isNonActif()) {
                        setNbMoisExercice1("12");
                        setNbMoisRevenuAutre1("12");
                        setNbMoisExercice2("12");
                        setNbMoisRevenuAutre2("12");
                        if (Integer.parseInt(getAnneeRevenuDebut()) == anneeAvs) {
                            setNbMoisExercice1(Integer.toString(JACalendar.getMonth(dateAgeAvs)));
                            setNbMoisRevenuAutre1(getNbMoisExercice1());
                        } else if (Integer.parseInt(getAnneeRevenuFin()) == anneeAvs) {
                            setNbMoisExercice2(Integer.toString(JACalendar.getMonth(dateAgeAvs)));
                            setNbMoisRevenuAutre2(getNbMoisExercice2());
                        }
                    } else {
                        setDebutExercice1("01.01." + getAnneeRevenuDebut());
                        setFinExercice1("31.12." + getAnneeRevenuDebut());
                        setDebutExercice2("01.01." + getAnneeRevenuFin());
                        setFinExercice2("31.12." + getAnneeRevenuFin());
                    }
                } else {
                    if (isNonActif()) {
                        if ("Periode".equalsIgnoreCase(typeRevenu)) {
                            int dureeDecision = JACalendar.getMonth(getFinDecision())
                                    - JACalendar.getMonth(getDebutDecision()) + 1;
                            setNbMoisExercice1(Integer.toString(dureeDecision));
                            setNbMoisRevenuAutre1(Integer.toString(dureeDecision));
                        } else {
                            setNbMoisExercice1("12");
                            setNbMoisRevenuAutre1("12");
                        }
                        if (Integer.parseInt(getAnneeRevenuDebut()) == anneeAvs) {
                            setNbMoisExercice1(Integer.toString(JACalendar.getMonth(dateAgeAvs)));
                            setNbMoisRevenuAutre1(Integer.toString(JACalendar.getMonth(dateAgeAvs)));
                        }
                    } else {
                        setDebutExercice1(getDebutDecision());
                        setFinExercice1(getFinDecision());
                    }
                    setNbMoisExercice2("");
                    setNbMoisRevenuAutre2("");
                    setDebutExercice2("");
                    setFinExercice2("");
                }

                // Recherche des coti. facturées dans l'année pour IND, REN.. si
                // ils ont l'avs dans la caisse
                if (!CPDecision.CS_IMPUTATION.equalsIgnoreCase(getTypeDecision())
                        && !CPDecision.CS_TSE.equalsIgnoreCase(getGenreAffilie()) && !isNonActif() && (cotiAvs != null)) {
                    // Remplir les montants de cotisations avec les montants
                    // déjà facturés
                    // sauf pour l'année en cours.
                    // Recherche du compte annexe du tiers
                    if (JACalendar.getYear(JACalendar.today().toString()) != Integer.parseInt(getAnneeRevenuDebut())) {
                        String role = CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(
                                getSession().getApplication());
                        compte.setSession(getSession());
                        compte.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
                        compte.setIdRole(role);
                        compte.setIdExterneRole(loadAffiliation().getAffilieNumero());
                        compte.wantCallMethodBefore(false);
                        compte.retrieve();
                        if (!compte.isNew()) {
                            setCotisation1(CACumulOperationCotisationManager.getCumulOperation(getSession(), null,
                                    compte.getIdCompteAnnexe(), rubriqueAvs, APIOperation.CAECRITURE,
                                    getAnneeRevenuDebut() + "0101", getAnneeRevenuDebut() + "1231", true));
                            // Si encodage sur 2 ana (nouveau calcul
                            // postnumerando)
                            if ("A".equalsIgnoreCase(getTaxation())) {
                                setCotisation2(CACumulOperationCotisationManager.getCumulOperation(getSession(), null,
                                        compte.getIdCompteAnnexe(), rubriqueAvs, APIOperation.CAECRITURE,
                                        getAnneeRevenuFin() + "0101", getAnneeRevenuFin() + "1231", true));
                            }
                        }
                    }
                }
                // Détermination si non soumis ??
                // Détermination si TSE ???
            } catch (Exception e) {
                _addError(null, getSession().getLabel("CP_MSG_0067"));
            } // -------- Recherche du conjoint
              // --------------------------------------------------------------
            TICompositionTiersManager cjt = new TICompositionTiersManager();
            cjt.setSession(getSession());
            // Recherche du conjoint
            // acr: selon directive AVS: Le mariage doit être considéré depuis
            // le début de l'année du mariage (division par deux pour toute
            // l'année).
            // Le divorce doit être considéré depuis le début de l'année du
            // divorce (individuel pour toute l'année).
            // Ce qui signifie que l'on va chercher le conjoint au 31.12 de
            // l'année de la décision
            cjt.setForIdTiersEnfantParent(getIdTiers());
            cjt.setForTypeLien(TICompositionTiers.CS_CONJOINT);
            cjt.setForDateEntreDebutEtFin("31.12." + getAnneeDecision());
            try {
                cjt.find();
                if (cjt.size() > 0) { // Formatage conjoint
                    TITiersViewBean conjoint = new TITiersViewBean();
                    conjoint.setSession(getSession());
                    if (((TICompositionTiers) cjt.getEntity(0)).getIdTiersParent().equals(getIdTiers())) {
                        conjoint.setIdTiers(((TICompositionTiers) cjt.getEntity(0)).getIdTiersEnfant());
                    } else {
                        conjoint.setIdTiers(((TICompositionTiers) cjt.getEntity(0)).getIdTiersParent());
                    }
                    try {
                        conjoint.retrieve();
                        AFAffiliation affiCjt = CPToolBox.returnAffiliation(getSession(), getSession()
                                .getCurrentThreadTransaction(), conjoint.getIdTiers(), getAnneeDecision(), "", 1);
                        if (affiCjt != null) {
                            setConjoint(conjoint.getNumAvsActuel() + " - " + conjoint.getNom());
                            setIdConjoint(conjoint.getIdTiers());
                            setSelectionCjt(affiCjt.getAffilieNumero());
                        } else {
                            // Ex ceux qui ont étaient marié puis divorcé ou
                            // veuf.. et dont le conjoint est inconnu
                            setDivision2(Boolean.TRUE);
                        }
                    } catch (Exception e) {
                        _addError(getSession().getCurrentThreadTransaction(), getSession().getLabel("CP_MSG_0068"));
                    }
                    // } else if ("515002".equalsIgnoreCase(persAvs.getEtatCivil())) {
                } else if (CPProperties.ETAT_CIVIL_SIMUL_CONJOINT.getValue().contains(persAvs.getEtatCivil())
                        && !JadeStringUtil.isBlankOrZero(persAvs.getEtatCivil())) {
                    // Si conjoint non renseigné=> regarder si le code état
                    // civil== Marié et dans ce cas mettre simuleConjoint=true
                    // Ce cas de figure est utile pour les caisses qui ne
                    // renseignaient pas le conjoint mais qui se basaient
                    // uniquement sur l'état civil et aussi pour les conjoints
                    // qui n'appartiennent pas à la caisse.
                    setDivision2(Boolean.TRUE);
                }
            } catch (Exception e) {
                _addError(getSession().getCurrentThreadTransaction(), getSession().getLabel("CP_MSG_0068"));
            } // préparation des zones à afficher ou à masquer
            try {
                if (((CPApplication) getSession().getApplication()).isCPCALCIMMO() && isNonActif()
                        && isProvisoireMetier()) {
                    CPFortune fortune = new CPFortune();
                    fortune.setSession(getSession());
                    fortune.setIdDecision(getIdDecision());
                    fortune.retrieve();
                    setMontantImmobilier1(fortune.getMontantImmobilier1());
                    setMontantImmobilier2(fortune.getMontantImmobilier2());
                    setMontantImmobilier3(fortune.getMontantImmobilier3());
                    setMontantImmobilier4(fortune.getMontantImmobilier4());
                    setCanton1(fortune.getCanton1());
                    setCanton2(fortune.getCanton2());
                    setCanton3(fortune.getCanton3());
                    setCanton4(fortune.getCanton4());
                    setTypeTerrain1(fortune.getTypeTerrain1());
                    setTypeTerrain2(fortune.getTypeTerrain2());
                    setTypeTerrain3(fortune.getTypeTerrain3());
                    setTypeTerrain4(fortune.getTypeTerrain4());
                    setDette(fortune.getDette());
                    setAutreFortune(fortune.getAutreFortune());
                }
            } catch (Exception e) {
                e.printStackTrace();
                setMontantImmobilier1("");
                setMontantImmobilier2("");
                setMontantImmobilier3("");
                setMontantImmobilier4("");
                setCanton1("");
                setCanton2("");
                setCanton3("");
                setCanton4("");
                setTypeTerrain1("");
                setTypeTerrain2("");
                setTypeTerrain3("");
                setTypeTerrain4("");
                setDette("");
                setAutreFortune("");
            }
            // Test si pour la même année, il y a pas une décision qui sera dans
            // le même passage
            // si oui, il faut mettre la zone complémentaire à true pour les
            // traitements de la
            // facturation (ne pas reprendre le montant déjà facturé) et les CI
            // ( ne pas extourné)
            if (!CPDecision.CS_IMPUTATION.equalsIgnoreCase(getTypeDecision())) {
                setComplementaire(isDecisionEnCours(getIdAffiliation(), getAnneeDecision()));
            }
            // Initialisation de la source d'information selon le type de
            // décision
            _initSourceInformation(getTypeDecision());
            // Initialisation du code intérêt moratoire
            if (CPToolBox.isAffilieAssiste(new BTransaction(getSession()), loadAffiliation(), getDebutDecision())
                    || Boolean.TRUE.equals(getComplementaire())) {
                setInteret(CAInteretMoratoire.CS_EXEMPTE);
            } else {
                setInteret(CAInteretMoratoire.CS_AUTOMATIQUE);
            }
            // Si étudiant => pas d'impression
            if (CPDecision.CS_ETUDIANT.equalsIgnoreCase(getGenreAffilie())) {
                setImpression(Boolean.FALSE);
            }
            // pour les décisions de remise, initialiser la coti avec le montant
            // minimum
            // et le commune avec celle de domicile
            if (CPDecision.CS_REMISE.equalsIgnoreCase(getTypeDecision())) {
                TIAdresseDataSource adresse = tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                        "519005", JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY), true);
                if (adresse != null) {
                    Hashtable<?, ?> data = adresse.getData();
                    // Reformater le code postale à 6 position si Suisse
                    String pays = (String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_PAYS_ISO);
                    if (pays.equalsIgnoreCase("CH")) {
                        String npa = (String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA);
                        String npasup = (String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA_SUP);
                        if (Integer.parseInt(npasup) < 10) {
                            npa = npa + "0" + npasup;
                        } else {
                            npa = npa + npasup;
                        }
                        setCommuneCode(npa);
                    } else {
                        setCommuneCode((String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA));
                    }
                    setCommuneLibelle((String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE));
                }
                float cotiMin = 0;
                String varString = "0";
                String role = CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(
                        getSession().getApplication());
                compte.setSession(getSession());
                compte.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
                compte.setIdRole(role);
                compte.setIdExterneRole(loadAffiliation().getAffilieNumero());
                compte.wantCallMethodBefore(false);
                compte.retrieve();
                if (!compte.isNew()) {
                    varString = CPToolBox.rechMontantFacture(getSession(), getSession().getCurrentThreadTransaction(),
                            compte.getIdCompteAnnexe(), rubriqueAvs, getAnneeDecision());
                    if (!JadeStringUtil.isBlankOrZero(varString)) {
                        setSaveCompteurPourRemise(Float.parseFloat(JANumberFormatter.deQuote(varString)));
                    }
                }
                cotiMin = CPTableNonActif.getCotisationMin(getSession(), getAnneeDecision());
                setCotisation1(Float.toString(cotiMin));
                if (Integer.parseInt(getAnneeDecision()) != JACalendar.getYear(JACalendar.today().toString())) {
                    if (getSaveCompteurPourRemise() < cotiMin) {
                        setCotisation1(Float.toString(getSaveCompteurPourRemise()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            _addError(null, getSession().getLabel("CP_MSG_0069"));
        }

    }

    /*
     * Permet d'initialiser l'écran de création d'une d'une décision avec le type d'affiliation défini dans naos
     */
    public String _initGenreAffilie() {
        AFAffiliation affiliation = new AFAffiliation();
        affiliation.setSession(getSession());
        affiliation.setAffiliationId(getIdAffiliation());
        affiliation.setIdTiers(getIdTiers());
        try {
            affiliation.retrieve();
            if (affiliation.getTypeAffiliation().equalsIgnoreCase(
                    globaz.naos.translation.CodeSystem.TYPE_AFFILI_NON_ACTIF)
                    || affiliation.getTypeAffiliation().equalsIgnoreCase(
                            globaz.naos.translation.CodeSystem.TYPE_AFFILI_SELON_ART_1A)) {
                return CPDecision.CS_NON_ACTIF;
            } else if (affiliation.getTypeAffiliation().equalsIgnoreCase(
                    globaz.naos.translation.CodeSystem.TYPE_AFFILI_INDEP)
                    || affiliation.getTypeAffiliation().equalsIgnoreCase(
                            globaz.naos.translation.CodeSystem.TYPE_AFFILI_INDEP_EMPLOY)) {
                if (globaz.naos.translation.CodeSystem.BRANCHE_ECO_AGRICULTURE.equalsIgnoreCase(affiliation
                        .getBrancheEconomique())) {
                    return CPDecision.CS_AGRICULTEUR;
                } else {
                    return CPDecision.CS_INDEPENDANT;
                }
            } else if (affiliation.getTypeAffiliation().equalsIgnoreCase(
                    globaz.naos.translation.CodeSystem.TYPE_AFFILI_TSE)
                    || affiliation.getTypeAffiliation().equalsIgnoreCase(
                            globaz.naos.translation.CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE)) {
                return CPDecision.CS_TSE;
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Initalisation de la période fiscale Date de création : (30.09.2002 14:44:57)
     * 
     * @return java.lang.String
     */
    public void _initPeriodeFiscale(CPDecisionViewBean decisionViewBean, String annee, int anneeDebAffiliation) {
        try {
            int anneeDecision = Integer.parseInt(annee);
            // recherche période IFD définitive correspondant à l'année de
            // décision
            CPPeriodeFiscaleManager perFis = new CPPeriodeFiscaleManager();
            perFis.setSession(getSession());
            perFis.setForAnneeDecisionDebut(annee);
            // Date de fortune si periode non extraordinaire
            // Si taxation praenumerando: 31.12 de l'année de décision
            // Si taxation postnumérando: Si année paire : 01.01 de l'année de
            // décision - 1
            // Si année impaire : 01.01 de l'année de décision - 2
            if (decisionViewBean.getTaxation().equalsIgnoreCase("N")) {
                decisionViewBean.setDateFortune("31.12." + Integer.toString(anneeDecision));
            } else if ((anneeDecision % 2) == 0) {
                decisionViewBean.setDateFortune("01.01." + Integer.toString(anneeDecision - 1));
            } else {
                decisionViewBean.setDateFortune("01.01." + Integer.toString(anneeDecision - 2));
            }
            // Valeur normal (si periode non extraordinaire
            switch (anneeDecision) {
                case 2000:
                    perFis.setForNumIfd("30");
                    perFis.setForAnneeRevenuDebut("1997");
                    break;
                case 1999:
                    perFis.setForNumIfd("29");
                    perFis.setForAnneeRevenuDebut("1995");
                    break;
                case 1998:
                    perFis.setForNumIfd("29");
                    perFis.setForAnneeRevenuDebut("1995");
                    break;
                case 1997:
                    perFis.setForNumIfd("28");
                    perFis.setForAnneeRevenuDebut("1993");
                    break;
                case 1996:
                    perFis.setForNumIfd("28");
                    perFis.setForAnneeRevenuDebut("1993");
                    break;
                case 1995:
                    perFis.setForNumIfd("27");
                    perFis.setForAnneeRevenuDebut("1991");
                    break;

            }
            // Test si procédure extraordinaire
            if (new CPApplication().isProcedureExtraordinaire()) {
                switch (anneeDecision) {
                    case 2000:
                        switch (anneeDebAffiliation) {
                            case 2000:
                                perFis.setForNumIfd("31");
                                perFis.setForAnneeRevenuDebut("1999");
                                decisionViewBean.setDateFortune("01.01.2000");
                                break;
                            case 1999:
                                perFis.setForNumIfd("31");
                                perFis.setForAnneeRevenuDebut("1999");
                                decisionViewBean.setDateFortune("01.01.1999");
                                break;
                            default:
                                perFis.setForNumIfd("30");
                                perFis.setForAnneeRevenuDebut("1997");
                                decisionViewBean.setDateFortune("01.01.1999");
                                break;
                        }
                        break;
                    case 1999:
                        switch (anneeDebAffiliation) {
                            case 1999:
                                perFis.setForNumIfd("31");
                                perFis.setForAnneeRevenuDebut("1999");
                                decisionViewBean.setDateFortune("01.01.1999");
                                break;
                            case 1998:
                            case 1997:
                                perFis.setForNumIfd("30");
                                perFis.setForAnneeRevenuDebut("1997");
                                decisionViewBean.setDateFortune("01.01.1999");
                                break;
                            case 1996:
                                perFis.setForNumIfd("29");
                                perFis.setForAnneeRevenuDebut("1995");
                                decisionViewBean.setDateFortune("01.01.1997");
                                break;
                            case 1995:
                                perFis.setForNumIfd("28");
                                perFis.setForAnneeRevenuDebut("1995");
                                decisionViewBean.setDateFortune("01.01.1997");
                                break;
                        }
                        break;
                    case 1998:
                        switch (anneeDebAffiliation) {
                            case 1998:
                                perFis.setForNumIfd("30");
                                perFis.setForAnneeRevenuDebut("1997");
                                decisionViewBean.setDateFortune("01.01.1998");
                                break;
                            case 1997:
                                perFis.setForNumIfd("30");
                                perFis.setForAnneeRevenuDebut("1997");
                                decisionViewBean.setDateFortune("01.01.1997");
                                break;
                            case 1996:
                                perFis.setForNumIfd("29");
                                perFis.setForAnneeRevenuDebut("1995");
                                decisionViewBean.setDateFortune("01.01.1997");
                                break;
                            case 1995:
                                perFis.setForNumIfd("28");
                                perFis.setForAnneeRevenuDebut("1995");
                                decisionViewBean.setDateFortune("01.01.1997");
                                break;

                        }
                        break;
                    case 1997:
                        switch (anneeDebAffiliation) {
                            case 1997:
                                perFis.setForNumIfd("30");
                                perFis.setForAnneeRevenuDebut("1997");
                                decisionViewBean.setDateFortune("01.01.1997");
                                break;
                            case 1996:
                                perFis.setForNumIfd("29");
                                perFis.setForAnneeRevenuDebut("1995");
                                decisionViewBean.setDateFortune("01.01.1997");
                                break;
                            case 1995:
                                perFis.setForNumIfd("28");
                                perFis.setForAnneeRevenuDebut("1995");
                                decisionViewBean.setDateFortune("01.01.1997");
                                break;
                        }
                        break;
                    case 1996:
                        switch (anneeDebAffiliation) {
                            case 1996:
                                perFis.setForNumIfd("29");
                                perFis.setForAnneeRevenuDebut("1995");
                                decisionViewBean.setDateFortune("01.01.1997");
                                break;
                            case 1995:
                                perFis.setForNumIfd("28");
                                perFis.setForAnneeRevenuDebut("1995");
                                decisionViewBean.setDateFortune("01.01.1995");
                                break;

                        }
                        break;
                    case 1995:
                        switch (anneeDebAffiliation) {
                            case 1995:
                                perFis.setForNumIfd("28");
                                perFis.setForAnneeRevenuDebut("1995");
                                decisionViewBean.setDateFortune("01.01.1995");
                                break;
                        }
                        break;
                }
            }
            perFis.find();
            if (perFis.getSize() == 0) {
                // Valeur normal (si periode non extraordinaire
                switch (anneeDecision) {
                    case 2000:
                        perFis.setForNumIfd("30");
                        perFis.setForAnneeRevenuDebut("1997");
                        break;
                    case 1999:
                        perFis.setForNumIfd("29");
                        perFis.setForAnneeRevenuDebut("1995");
                        break;
                    case 1998:
                        perFis.setForNumIfd("29");
                        perFis.setForAnneeRevenuDebut("1995");
                        break;
                    case 1997:
                        perFis.setForNumIfd("28");
                        perFis.setForAnneeRevenuDebut("1993");
                        break;
                    case 1996:
                        perFis.setForNumIfd("28");
                        perFis.setForAnneeRevenuDebut("1993");
                        break;
                    case 1995:
                        perFis.setForNumIfd("27");
                        perFis.setForAnneeRevenuDebut("1991");
                        break;

                }
                perFis.find();
            }
            decisionViewBean.setNumIfdDefinitif(((CPPeriodeFiscale) perFis.getEntity(0)).getNumIfd());
            decisionViewBean.setIdIfdDefinitif(((CPPeriodeFiscale) perFis.getEntity(0)).getIdIfd());
            decisionViewBean.setIdIfdProvisoire(((CPPeriodeFiscale) perFis.getEntity(0)).getIdIfd());
            decisionViewBean.setAnneeRevenuDebut(((CPPeriodeFiscale) perFis.getEntity(0)).getAnneeRevenuDebut());
            decisionViewBean.setAnneeRevenuFin(((CPPeriodeFiscale) perFis.getEntity(0)).getAnneeRevenuFin());
            if (!decisionViewBean.isNonActif()) {
                decisionViewBean.setDateFortune("");
            }
        } catch (Exception e) {
            e.printStackTrace();
            _addError(null, getSession().getLabel("CP_MSG_0060"));
        }
    }

    /*
     * Initialise la source d'information selon le type de décision Ex: décision définitive -> souce selon l'IFD
     */
    public void _initSourceInformation(String typeDecision) {
        if (CPDecision.CS_DEFINITIVE.equalsIgnoreCase(typeDecision)
                || CPDecision.CS_RECTIFICATION.equalsIgnoreCase(typeDecision)) {
            setSourceInformation(CPDonneesBase.CS_FISC);
        } else if (CPDecision.CS_PROVISOIRE.equalsIgnoreCase(typeDecision)
                || CPDecision.CS_CORRECTION.equalsIgnoreCase(typeDecision)) {
            setSourceInformation(CPDonneesBase.CS_VOTRE_ESTIMATION);
        } else if (CPDecision.CS_ACOMPTE.equalsIgnoreCase(typeDecision)) {
            setSourceInformation(CPDonneesBase.CS_NOTRE_ESTIMATION);
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        // Contrôle du conjoint
        if (getProcessExterne().equals(new Boolean(false))) {
            try {
                if (JadeStringUtil.isIntegerEmpty(selectionCjt)) {
                    setIdConjoint("");
                    setConjoint("");
                } else {
                    String idCjt = _ctrlCodeSaisi(statement.getTransaction(), getIdConjoint(), selectionCjt);
                    if (!JadeStringUtil.isIntegerEmpty(idCjt)) {
                        TITiersViewBean cjt = new TITiersViewBean();
                        cjt.setIdTiers(idCjt);
                        cjt.setSession(getSession());
                        cjt.retrieve(statement.getTransaction());
                        if (!cjt.isNew()) {
                            setConjoint(cjt.getNom());
                            setIdConjoint(idCjt);
                        } else {
                            setConjoint("");
                            setIdConjoint("");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0070"));
            }
        }
        // Contrôle de la commune (pour les remises)
        if (CPDecision.CS_REMISE.equalsIgnoreCase(getTypeDecision())) {
            if (JadeStringUtil.isEmpty(getWantWarning())) {
                if (!JadeStringUtil.isBlankOrZero(getCotisation1())) {
                    // Avertissement si montant saisi > cotisation minimale
                    float coti = Float.parseFloat(JANumberFormatter.deQuote(getCotisation1()));
                    float cotiMin = Float.parseFloat(CPToolBox.getMontantProRata(getDebutDecision(), getFinDecision(),
                            CPTableIndependant.getCotisationMinimum(statement.getTransaction(), getDebutDecision())));
                    if (coti > cotiMin) {
                        setWarningMessage(getSession().getLabel("CP_MSG_0197"));
                    }
                    // Avertissement si montant saisi > compteur
                    if (coti > getSaveCompteurPourRemise()) {
                        if (JadeStringUtil.isEmpty(getWarningMessage())) {
                            setWarningMessage(getSession().getLabel("CP_MSG_0198"));
                        } else {
                            setWarningMessage(getWarningMessage() + " " + getSession().getLabel("CP_MSG_0198"));
                        }
                    }

                }
                // Utile pour ne pas boucler sur la page DE (permet de faire qu'une seule fois)
                if (!JadeStringUtil.isEmpty(getWarningMessage())) {
                    _addWarning(statement.getTransaction(), "ok");
                }
            } else {
                setWarningMessage("");
            }
            try {
                if (JadeStringUtil.isIntegerEmpty(communeCode)) {
                    setCommuneCode("");
                    setCommuneLibelle("");
                    setIdCommune("");
                } else {
                    TILocalite loca = new TILocalite();
                    loca.setSession(getSession());
                    String idComm = loca._ctrlCodeSaisi(statement.getTransaction(), getIdCommune(), communeCode);
                    if (!JadeStringUtil.isIntegerEmpty(idComm)) {
                        loca.setIdLocalite(idComm);
                        loca.retrieve(statement.getTransaction());
                        if (!loca.isNew()) {
                            setCommuneCode(loca.getNumPostalEntier());
                            setCommuneLibelle(loca.getLocalite());
                            setIdCommune(loca.getIdLocalite());
                        } else {
                            setCommuneCode("");
                            setCommuneLibelle("");
                            setIdCommune("");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0071"));
            }
            // le montant de remise négatif ne peut être supérieur à celui des compteurs (cas correctif)
            float montantRemise = Float.parseFloat(JANumberFormatter.deQuote(getCotisation1()));
            if (montantRemise < 0) {
                // Recherche compta annexe
                CACompteAnnexe compte = new CACompteAnnexe();
                compte.setSession(getSession());
                compte.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
                compte.setIdRole(CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(
                        getSession().getApplication()));
                compte.setIdExterneRole(getAffiliation().getAffilieNumero());
                compte.wantCallMethodBefore(false);
                compte.retrieve(statement.getTransaction());
                // Recherche rubrique remise
                AFAssuranceManager assMng = new AFAssuranceManager();
                assMng.setSession(getSession());
                assMng.setForGenreAssurance(globaz.naos.translation.CodeSystem.GENRE_ASS_PERSONNEL);
                assMng.setForTypeAssurance(globaz.naos.translation.CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
                assMng.find();
                if (assMng.getSize() >= 0) {
                    AFAssurance ass = (AFAssurance) assMng.getFirstEntity();
                    String rubExterne = ass.getParametreAssuranceValeur(
                            globaz.naos.translation.CodeSystem.GEN_PARAM_ASS_REMISE, getDebutDecision(), "");
                    String varMontant = CPToolBox.compteurRubrique(getSession(), statement.getTransaction(),
                            compte.getIdCompteAnnexe(), rubExterne, getAnneeDecision());
                    if (Math.abs(montantRemise) > Math.abs(Float.parseFloat(JANumberFormatter.deQuote(varMontant)))) {
                        _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0195"));
                    }
                }
            }
        }
        // Recherche du mode d'encodage, si revenu mensuel ou annuel encodé
        // (properties)
        setAnneePrise("T");
        if ("A".equalsIgnoreCase(getTaxation())) {
            if (JadeStringUtil.isBlank(getRevenu1()) && JadeStringUtil.isBlank(getCotisation1())) {
                setAnneePrise("2");
            }
            if (JadeStringUtil.isBlank(getRevenu2()) && JadeStringUtil.isBlank(getCotisation2())) {
                setAnneePrise("1");
            }
            if (JadeStringUtil.isBlank(getRevenu2()) && JadeStringUtil.isBlank(getCotisation2())
                    && JadeStringUtil.isBlank(getRevenu1()) && JadeStringUtil.isBlank(getCotisation1())) {
                setAnneePrise("T");
            }
        } else {
            setAnneePrise("1");
        }
        // Si on a changé de tiers, il faut aller chercher l'affiliation qui
        // comprend la période de décision
        // sinon (cas ou on est venu par l'affiliation) on peut tester si la
        // décision est comprise dans
        // l'affiliation sélectionnée.
        if (JadeStringUtil.isIntegerEmpty(getIdAffiliation())) {
            try {
                // Recherche de l'affiliation
                AFAffiliation affiliation = new AFAffiliation();
                affiliation = CPToolBox.returnAffiliation(getSession(), statement.getTransaction(), getNumAffilie(),
                        getAnneeDecision(), "", 0);
                if (affiliation == null) {
                    _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0073"));
                } else {
                    setAffiliation(affiliation);
                    setIdAffiliation(affiliation.getAffilieNumero());
                }
            } catch (Exception e) {
                e.printStackTrace();
                _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0072"));
            }
        }
        // Sélection par l'affiliation
        // Test si la décision est comprise dans l'affiliation
        if (BSessionUtil.compareDateFirstLower(getSession(), getDebutDecision(), loadAffiliation().getDateDebut())
                || (!JAUtil.isDateEmpty(loadAffiliation().getDateFin()) && BSessionUtil.compareDateFirstGreater(
                        getSession(), getFinDecision(), loadAffiliation().getDateFin()))) {
            _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0073"));
        }
        // Pour le mode postnumerando, l'exercice ne peut être à cheval que sur
        // deux années au max
        // sinon il doit être compris dans l'année du revenu - Test par rapport
        // à l'année du revenu
        // Test pour l'exercice 1
        if (!JadeStringUtil.isBlank(anneeRevenuDebut)) {
            int anneeRevDebut = Integer.parseInt(anneeRevenuDebut);
            // Test validité date début exercice 1
            if (!JadeStringUtil.isBlank(debutExercice1)) {
                if (JadeStringUtil.isBlank(finExercice1)) {
                    _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0181") + " " + anneeRevenuDebut
                            + getSession().getLabel("CP_MSG_0182") + " ");

                } else {
                    int debExe1 = JACalendar.getYear(debutExercice1);
                    if ((debExe1 < (anneeRevDebut - 1)) || (debExe1 > (anneeRevDebut + 1))) {
                        _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0183") + " "
                                + anneeRevDebut + ". ");
                    }
                }
            }
            // Test validité date fin exercice 1
            if (!JadeStringUtil.isBlank(finExercice1)) {
                if (JadeStringUtil.isBlank(debutExercice1)) {
                    _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0184") + " " + anneeRevenuDebut
                            + getSession().getLabel("CP_MSG_0185"));

                } else {
                    int finExe1 = JACalendar.getYear(finExercice1);
                    if ((finExe1 < (anneeRevDebut - 1)) || (finExe1 > (anneeRevDebut + 1))) {
                        _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0186") + " "
                                + anneeRevDebut + ". ");
                    }
                }
            }
            // La date de début de l'exercie ne peut être supèrieure à celle de
            // fin
            if (!JadeStringUtil.isBlank(debutExercice1) && !JadeStringUtil.isBlank(finExercice1)) {
                if (!globaz.globall.db.BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDebutExercice1(),
                        getFinExercice1())) {
                    _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0074"));
                }
            }
        }
        // Test pour l'exercice 2
        if (!JadeStringUtil.isBlank(anneeRevenuFin)) {
            int anneeRevFin = Integer.parseInt(anneeRevenuFin);
            // Test validité date début exercice 1
            if (!JadeStringUtil.isBlank(debutExercice2)) {
                if (JadeStringUtil.isBlank(finExercice2)) {
                    _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0181") + " " + anneeRevenuFin
                            + getSession().getLabel("CP_MSG_0182") + " ");
                } else {
                    int debExe2 = JACalendar.getYear(debutExercice2);
                    if ((debExe2 < (anneeRevFin - 1)) || (debExe2 > (anneeRevFin + 1))) {
                        _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0183") + " " + anneeRevFin
                                + ". ");
                    }
                }
            }
            // Test validité date fin exercice 1
            if (!JadeStringUtil.isBlank(finExercice2)) {
                if (JadeStringUtil.isBlank(debutExercice2)) {
                    _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0184") + " " + anneeRevenuFin
                            + getSession().getLabel("CP_MSG_0185"));

                } else {
                    int finExe2 = JACalendar.getYear(finExercice2);
                    if ((finExe2 < (anneeRevFin - 1)) || (finExe2 > (anneeRevFin + 1))) {
                        _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0186") + " " + anneeRevFin
                                + ". ");
                    }
                }
            }
            // La date de début de l'exercie ne peut être supèrieure à celle de
            // fin
            if (!JadeStringUtil.isBlank(debutExercice2) && !JadeStringUtil.isBlank(finExercice2)) {
                if (!globaz.globall.db.BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDebutExercice2(),
                        getFinExercice2())) {
                    _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0074"));
                }
            }
        }
        // Si rentier dans l'année: tester si la fin de décision est infèrieure
        // à la date
        // de retraite si oui il faut remettre le genre d'affiliation avant
        // rentier.
        // Note: l'idéal serait d'avoir les dates de décision sur l'écran
        // d'initialisation
        // au lieu de l'année mais trop lourd pour les utilisateurs pour gérer
        // ce cas qui
        // arrive moins souvent.
        if (!isNonActif()) {
            if (BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getFinDecision(), tiers.getDateAvs())) {
                if (globaz.naos.translation.CodeSystem.BRANCHE_ECO_AGRICULTURE.equalsIgnoreCase(loadAffiliation()
                        .getBrancheEconomique())) {
                    setGenreAffilie(CPDecision.CS_AGRICULTEUR);
                }
            } else {
                setGenreAffilie(CPDecision.CS_RENTIER);
            }
        }
        // Si non actif - Test date par rapport à l'âge AVS
        else {
            String dateAgeAvs = loadTiers().getDateAvs();
            if (BSessionUtil.compareDateFirstLower(getSession(), dateAgeAvs, getFinDecision())) {
                _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0187") + " (" + dateAgeAvs + ") ");
            }

        }
        // Si la caisse encode le détail de la fortune immobilière
        if (isCPCALCIMMO()) {
            if (isProvisoireMetier()) {
                // Contrôle ligne 1
                // -- Test valeur
                if (JadeStringUtil.isIntegerEmpty(getMontantImmobilier1())) {
                    if ((!JadeStringUtil.isIntegerEmpty(getCanton1()))
                            || (!JadeStringUtil.isIntegerEmpty(getTypeTerrain1()))) {
                        _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0075"));
                    }

                }
                // -- Test canton
                if (JadeStringUtil.isIntegerEmpty(getCanton1())) {
                    if ((!JadeStringUtil.isIntegerEmpty(getMontantImmobilier1()))
                            || (!JadeStringUtil.isIntegerEmpty(getTypeTerrain1()))) {
                        _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0076"));
                    }

                }
                // -- Test type de terrain
                if (JadeStringUtil.isIntegerEmpty(getTypeTerrain1())) {
                    if ((!JadeStringUtil.isIntegerEmpty(getMontantImmobilier1()))
                            || (!JadeStringUtil.isIntegerEmpty(getCanton1()))) {
                        _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0077"));
                    }

                }
                // Contrôle ligne 2
                // -- Test valeur
                if (JadeStringUtil.isIntegerEmpty(getMontantImmobilier2())) {
                    if ((!JadeStringUtil.isIntegerEmpty(getCanton2()))
                            || (!JadeStringUtil.isIntegerEmpty(getTypeTerrain2()))) {
                        _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0078"));
                    }

                }
                // -- Test canton
                if (JadeStringUtil.isIntegerEmpty(getCanton2())) {
                    if ((!JadeStringUtil.isIntegerEmpty(getMontantImmobilier2()))
                            || (!JadeStringUtil.isIntegerEmpty(getTypeTerrain2()))) {
                        _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0079"));
                    }

                }
                // -- Test type de terrain
                if (JadeStringUtil.isIntegerEmpty(getTypeTerrain2())) {
                    if ((!JadeStringUtil.isIntegerEmpty(getMontantImmobilier2()))
                            || (!JadeStringUtil.isIntegerEmpty(getCanton2()))) {
                        _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0080"));
                    }

                }
                // Contrôle ligne 3
                // -- Test valeur
                if (JadeStringUtil.isIntegerEmpty(getMontantImmobilier3())) {
                    if ((!JadeStringUtil.isIntegerEmpty(getCanton3()))
                            || (!JadeStringUtil.isIntegerEmpty(getTypeTerrain3()))) {
                        _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0081"));
                    }

                }
                // -- Test canton
                if (JadeStringUtil.isIntegerEmpty(getCanton3())) {
                    if ((!JadeStringUtil.isIntegerEmpty(getMontantImmobilier3()))
                            || (!JadeStringUtil.isIntegerEmpty(getTypeTerrain3()))) {
                        _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0082"));
                    }

                }
                // -- Test type de terrain
                if (JadeStringUtil.isIntegerEmpty(getTypeTerrain3())) {
                    if ((!JadeStringUtil.isIntegerEmpty(getMontantImmobilier3()))
                            || (!JadeStringUtil.isIntegerEmpty(getCanton3()))) {
                        _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0083"));
                    }

                }
                // Contrôle ligne 4
                // -- Test valeur
                if (JadeStringUtil.isIntegerEmpty(getMontantImmobilier4())) {
                    if ((!JadeStringUtil.isIntegerEmpty(getCanton4()))
                            || (!JadeStringUtil.isIntegerEmpty(getTypeTerrain4()))) {
                        _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0084"));
                    }

                }
                // -- Test canton
                if (JadeStringUtil.isIntegerEmpty(getCanton4())) {
                    if ((!JadeStringUtil.isIntegerEmpty(getMontantImmobilier4()))
                            || (!JadeStringUtil.isIntegerEmpty(getTypeTerrain4()))) {
                        _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0085"));
                    }

                }
                // -- Test type de terrain
                if (JadeStringUtil.isIntegerEmpty(getTypeTerrain4())) {
                    if ((!JadeStringUtil.isIntegerEmpty(getMontantImmobilier4()))
                            || (!JadeStringUtil.isIntegerEmpty(getCanton4()))) {
                        _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0086"));
                    }

                }
            }
        }
        // Mis à jour de la spécification opposition
        if (getOpposition().equals(new Boolean(true))) {
            setSpecification(CPDecision.CS_OPPOSITION);
        } else if (CPDecision.CS_OPPOSITION.equals(getSpecification())) {
            setSpecification("");
        }
        // Mis à jour de la spécification recours
        if (getRecours().equals(new Boolean(true))) {
            setSpecification(CPDecision.CS_RECOURS);
        } else if (CPDecision.CS_RECOURS.equals(getSpecification())) {
            setSpecification("");
        }
        // Validation au niveau de l'entity
        super._validate(statement);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getAction() {
        return (action);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53) retourne le n° avs + le nom du
     * conjoint
     */
    public java.lang.String getAnneeRevenuDebut() {
        return anneeRevenuDebut;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53) retourne le n° avs + le nom du
     * conjoint
     */
    public java.lang.String getAnneeRevenuFin() {
        return anneeRevenuFin;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53) retourne le n° avs + le nom du
     * conjoint
     */
    public java.lang.String getAutreFortune() {
        return autreFortune;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.09.2002 14:44:57)
     * 
     * @return java.lang.String
     */
    public java.lang.String getCanton1() {
        return canton1;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.09.2002 14:45:20)
     * 
     * @return java.lang.String
     */
    public java.lang.String getCanton2() {
        return canton2;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.09.2002 14:45:43)
     * 
     * @return java.lang.String
     */
    public java.lang.String getCanton3() {
        return canton3;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.09.2002 14:46:06)
     * 
     * @return java.lang.String
     */
    public java.lang.String getCanton4() {
        return canton4;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53) retourne le n° avs + le nom du
     * conjoint
     */
    public java.lang.String getCapital() {
        return capital;
    }

    /**
     * Returns the codeEtatDecision.
     * 
     * @return java.lang.String
     */
    public java.lang.String getCodeEtatDecision() {
        return codeEtatDecision;
    }

    public String getColonneSelection() {
        return colonneSelection;
    }

    public java.lang.String getCommuneCode() {
        return communeCode;
    }

    public java.lang.String getCommuneLibelle() {
        return communeLibelle;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53) retourne le n° avs + le nom du
     * conjoint
     */
    public java.lang.String getConjoint() {
        return conjoint;
    }

    public float getCotiIndMinimumMax() {
        try {
            float cotiIndMinimum = CPTableIndependant.getCotisationMinimum(null, getDebutDecision());
            return Float.parseFloat(JANumberFormatter.deQuote(CPToolBox.getMontantProRata(getDebutDecision(),
                    getFinDecision(), cotiIndMinimum)));
        } catch (Exception e) {
            return 0;
        }
    }

    public String getRachatLPP() {

        return rachatLPP;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53) retourne le n° avs + le nom du
     * conjoint
     */
    public java.lang.String getCotisation1() {
        return cotisation1;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53) retourne le n° avs + le nom du
     * conjoint
     */
    public java.lang.String getCotisation2() {
        return cotisation2;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53) retourne le n° avs + le nom du
     * conjoint
     */
    public java.lang.String getCotisationSalarie() {
        return cotisationSalarie;
    }

    public String getDateDecision() {
        return dateDecision;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53) retourne le n° avs + le nom du
     * conjoint
     */
    public java.lang.String getDateFortune() {
        return dateFortune;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53) retourne le n° avs + le nom du
     * conjoint
     */
    public java.lang.String getDebutExercice1() {
        return debutExercice1;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53) retourne le n° avs + le nom du
     * conjoint
     */
    public java.lang.String getDebutExercice2() {
        return debutExercice2;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.07.2003 14:15:11)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDescriptionTiers() {
        return descriptionTiers;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53) retourne le n° avs + le nom du
     * conjoint
     */
    public java.lang.String getDette() {
        return dette;
    }

    public String getDocListe() {
        return docListe;
    }

    public Boolean getDuplicata() {
        return duplicata;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53) retourne le n° avs + le nom du
     * conjoint
     */
    public java.lang.String getEtatDecision() {
        return etatDecision;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53) retourne le n° avs + le nom du
     * conjoint
     */
    public java.lang.String getFinExercice1() {
        return finExercice1;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53) retourne le n° avs + le nom du
     * conjoint
     */
    public java.lang.String getFinExercice2() {
        return finExercice2;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.04.2003 13:11:31)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFortuneDeterminante() {
        return fortuneDeterminante;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53) retourne le n° avs + le nom du
     * conjoint
     */
    public java.lang.String getFortuneTotale() {
        return fortuneTotale;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53) retourne le n° avs + le nom du
     * conjoint
     */
    public java.lang.String getFortuneTotaleFois2() {
        if (!JadeStringUtil.isIntegerEmpty(JANumberFormatter.deQuote(getFortuneTotale()))) {
            return JANumberFormatter.fmt(
                    new Float(2 * new Float(globaz.globall.util.JANumberFormatter.deQuote(getFortuneTotale()))
                            .floatValue()).toString(), true, false, true, 0);
        } else {
            return "";
        }
    }

    public String getGrpExtraction(String idRetour) {
        try {
            CPValidationCalculCommunicationManager manager = new CPValidationCalculCommunicationManager();
            manager.setSession(getSession());
            manager.setForIdCommunicationRetour(idRetour);
            manager.find();
            CPValidationCalculCommunication validationCalcul = (CPValidationCalculCommunication) manager
                    .getFirstEntity();
            CPParametrePlausibilite param = new CPParametrePlausibilite();
            param.setSession(getSession());
            param.setId(validationCalcul.getGroupeExtraction());
            param.retrieve();
            if (param.getDescription_fr().length() > 0) {
                if (getSession().getIdLangueISO().equalsIgnoreCase("DE")) {
                    return param.getId() + " - " + param.getDescription_de();
                } else if (getSession().getIdLangueISO().equalsIgnoreCase("IT")) {
                    return param.getId() + " - " + param.getDescription_it();
                } else {
                    return param.getId() + " - " + param.getDescription_fr();
                }
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    public String getGrpTaxation(String idRetour) {
        try {
            CPValidationCalculCommunicationManager manager = new CPValidationCalculCommunicationManager();
            manager.setSession(getSession());
            manager.setForIdCommunicationRetour(idRetour);
            manager.find();
            CPValidationCalculCommunication validationCalcul = (CPValidationCalculCommunication) manager
                    .getFirstEntity();
            if (validationCalcul.getGroupeTaxation().length() > 1) {
                return getSession().getCodeLibelle(validationCalcul.getGroupeTaxation());
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    public String getIdValidation() {
        return idValidation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53) retourne le n° avs + le nom du
     * conjoint
     */
    public java.lang.String getLibelleGenreAffilie() {
        try {
            return globaz.phenix.translation.CodeSystem.getLibelle(getSession(), getGenreAffilie());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * @return
     */
    public String getLibellePassage() {
        return libellePassage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53) retourne le n° avs + le nom du
     * conjoint
     */
    public java.lang.String getLibelleTypeDecision() {
        try {
            return globaz.phenix.translation.CodeSystem.getLibelle(getSession(), getTypeDecision());
        } catch (Exception e) {
            getSession().addError(e.getMessage());
            return "";
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.09.2002 14:42:21)
     * 
     * @return java.lang.String
     */
    public java.lang.String getMontantImmobilier1() {
        return montantImmobilier1;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.09.2002 14:42:21)
     * 
     * @return java.lang.String
     */
    public java.lang.String getMontantImmobilier2() {
        return montantImmobilier2;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.09.2002 14:42:21)
     * 
     * @return java.lang.String
     */
    public java.lang.String getMontantImmobilier3() {
        return montantImmobilier3;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.09.2002 14:42:21)
     * 
     * @return java.lang.String
     */
    public java.lang.String getMontantImmobilier4() {
        return montantImmobilier4;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.04.2003 12:22:52)
     * 
     * @return java.lang.String
     */
    public java.lang.String getNbMoisExercice1() {
        return nbMoisExercice1;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.04.2003 12:23:06)
     * 
     * @return java.lang.String
     */
    public java.lang.String getNbMoisExercice2() {
        return nbMoisExercice2;
    }

    /**
     * Getter de base pour le montant de rente AVS.
     * 
     * @return Le montant.
     */
    public java.lang.String getMontantTotalRenteAVS() {
        return montantTotalRenteAVS;
    }

    /**
     * Setter de base pour montant rente avs.
     * 
     * @param montantTotalRenteAVS Le montant.
     */
    public void setMontantTotalRenteAVS(java.lang.String montantTotalRenteAVS) {
        this.montantTotalRenteAVS = montantTotalRenteAVS;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.04.2003 12:23:35)
     * 
     * @return java.lang.String
     */
    public java.lang.String getNbMoisRevenuAutre1() {
        return nbMoisRevenuAutre1;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.04.2003 12:23:47)
     * 
     * @return java.lang.String
     */
    public java.lang.String getNbMoisRevenuAutre2() {
        return nbMoisRevenuAutre2;
    }

    public String getNomPrenom() {
        if (!JadeStringUtil.isEmpty(getIdTiers())) {
            TITiersViewBean vb = new TITiersViewBean();
            vb.setIdTiers(getIdTiers());
            vb.setSession(getSession());
            try {
                vb.retrieve();
            } catch (Exception e) {
                return "";
            }
            return vb.getNomPrenom();
        } else {
            return "";
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.07.2003 09:57:32)
     * 
     * @return java.lang.String
     */
    public java.lang.String getNumAffilie() {
        return numAffilie;
    }

    public String getNumAVS() {
        if (!JadeStringUtil.isEmpty(getIdTiers())) {
            TITiersViewBean vb = new TITiersViewBean();
            vb.setIdTiers(getIdTiers());
            vb.setSession(getSession());
            try {
                vb.retrieve();
            } catch (Exception e) {
                return "";
            }
            return vb.getNumAvsActuel();
        } else {
            return "";
        }
    }

    public String getNumContribuable() {
        if (!JadeStringUtil.isEmpty(getIdTiers())) {
            TITiersViewBean vb = new TITiersViewBean();
            vb.setIdTiers(getIdTiers());
            vb.setSession(getSession());
            try {
                vb.retrieve();
                return vb.getNumContribuableActuel();
            } catch (Exception e) {
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53) retourne le n° avs + le nom du
     * conjoint
     */
    public java.lang.String getNumIfdDefinitif() {
        return numIfdDefinitif;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53) retourne le n° avs + le nom du
     * conjoint
     */
    @Override
    public java.lang.String getPeriodicite() {
        return periodicite;
    }

    /**
     * @return
     */
    public Boolean getProcessExterne() {
        return processExterne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53) retourne le n° avs + le nom du
     * conjoint
     */
    public java.lang.String getRevenu1() {
        return revenu1;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53) retourne le n° avs + le nom du
     * conjoint
     */
    public java.lang.String getRevenu2() {
        return revenu2;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.04.2003 11:35:17)
     * 
     * @return java.lang.String
     */
    public java.lang.String getRevenuAutre1() {
        return revenuAutre1;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.04.2003 11:36:58)
     * 
     * @return java.lang.String
     */
    public java.lang.String getRevenuAutre2() {
        return revenuAutre2;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53) retourne le n° avs + le nom du
     * conjoint
     */
    public java.lang.String getRevenuCiForce() {
        return revenuCiForce;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53)
     */
    public Boolean getRevenuCiForce0() {
        return revenuCiForce0;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.04.2003 09:19:13)
     * 
     * @return java.lang.String
     */
    public java.lang.String getRevenuDeterminant() {
        return revenuDeterminant;
    }

    public float getSaveCompteurPourRemise() {
        return saveCompteurPourRemise;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.07.2003 14:05:01)
     * 
     * @return java.lang.String
     */
    public java.lang.String getSelection() {
        return selection;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.07.2003 08:29:52)
     * 
     * @return java.lang.String
     */
    public java.lang.String getSelectionCjt() {
        return selectionCjt;
    }

    /**
     * @return
     */
    public java.lang.String getSourceInformation() {
        return sourceInformation;
    }

    /**
     * Détermine si la caisse si base sur des montants mensuels ou annuels Date de création : (30.09.2002 15:15:15)
     * 
     * @return boolean
     */
    public String getTypeRevenu() throws Exception {
        return ((CPApplication) getSession().getApplication()).getTypeRevenu();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.09.2002 14:46:51)
     * 
     * @return java.lang.String
     */
    public java.lang.String getTypeTerrain1() {
        return typeTerrain1;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.09.2002 14:47:13)
     * 
     * @return java.lang.String
     */
    public java.lang.String getTypeTerrain2() {
        return typeTerrain2;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.09.2002 14:47:38)
     * 
     * @return java.lang.String
     */
    public java.lang.String getTypeTerrain3() {
        return typeTerrain3;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.09.2002 14:48:03)
     * 
     * @return java.lang.String
     */
    public java.lang.String getTypeTerrain4() {
        return typeTerrain4;
    }

    public String getWantWarning() {
        return wantWarning;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.09.2002 15:15:15)
     * 
     * @return boolean
     */
    public boolean isCPCALCIMMO() throws Exception {
        if (isNonActif()) {
            return ((CPApplication) getSession().getApplication()).isCPCALCIMMO();
        } else {
            return false;
        }
    }

    /**
     * Détermine si la caisse si base sur des périodes d'exercice ou encode le nombre de mois Date de création :
     * (30.09.2002 15:15:15)
     * 
     * @return boolean
     */
    public boolean isDateExercice() throws Exception {
        if (isNonActif()) {
            return false;
        } else {
            return ((CPApplication) getSession().getApplication()).isDateExercice();
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (19.03.2003 13:16:17)
     * 
     * @return globaz.pyxis.db.tiers.TITiersViewBean
     */
    @Override
    public globaz.pyxis.db.tiers.TITiersViewBean loadTiers() {
        // enregistrement déjà chargé ?
        if (tiers == null) {
            // liste pas encore chargée, on la charge
            tiers = new TITiersViewBean();
            tiers.setSession(getSession());
            if (!JadeStringUtil.isIntegerEmpty(getIdTiers())) {
                try {
                    tiers.setIdTiers(getIdTiers());
                    tiers.retrieve();
                } catch (Exception e) {
                    getSession().addError(e.getMessage());
                }
            }
        }
        return tiers;
    }

    public String rechercheIdEnteteFacture() throws Exception {
        // Création entete facture si inexistantes
        FAEnteteFactureManager entFactureManager = new FAEnteteFactureManager();
        entFactureManager.setSession(getSession());
        entFactureManager.setForIdTiers(getIdTiers());
        entFactureManager.setForIdPassage(getIdPassage());
        if (CPDecision.CS_ETUDIANT.equalsIgnoreCase(getGenreAffilie())) {
            entFactureManager.setForIdTypeFacture(APISection.ID_TYPE_SECTION_ETUDIANTS);
            entFactureManager.setForIdSousType(APISection.ID_CATEGORIE_SECTION_DECISION_COTPERS_ETUDIANT);
        } else {
            entFactureManager.setForIdTypeFacture(APISection.ID_TYPE_SECTION_DECOMPTE_COTISATION);
            entFactureManager.setForIdSousType(APISection.ID_CATEGORIE_SECTION_DECISION_COTPERS);
        }
        entFactureManager.setForIdExterneRole(getAffiliation().getAffilieNumero());
        boolean factureParAnnee = ((CPApplication) getSession().getApplication()).isFactureParAnnee();
        if (factureParAnnee || CPDecision.CS_ETUDIANT.equalsIgnoreCase(getGenreAffilie())) {
            entFactureManager.setLikeIdExterneFacture(getAnneeDecision());
        }
        entFactureManager.setForIdRole(CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(
                getSession().getApplication()));
        entFactureManager.find();
        if (entFactureManager.size() > 0) {
            FAEnteteFacture entete = (FAEnteteFacture) entFactureManager.getFirstEntity();
            return entete.getIdEntete();
        }
        return "";
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:20:01)
     * 
     * @param action
     *            java.lang.String
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53)
     * 
     * @param newAgenceCommunaleLocalite
     *            java.lang.String
     */
    public void setAnneeRevenuDebut(java.lang.String newAnneeRevenuDebut) {
        anneeRevenuDebut = newAnneeRevenuDebut;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53)
     * 
     * @param newAgenceCommunaleLocalite
     *            java.lang.String
     */
    public void setAnneeRevenuFin(java.lang.String newAnneeRevenuFin) {
        anneeRevenuFin = newAnneeRevenuFin;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53)
     * 
     * @param newAgenceCommunaleLocalite
     *            java.lang.String
     */
    public void setAutreFortune(java.lang.String newAutreFortune) {
        autreFortune = newAutreFortune;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.09.2002 14:44:57)
     * 
     * @param newCanton1
     *            java.lang.String
     */
    public void setCanton1(java.lang.String newCanton1) {
        canton1 = newCanton1;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.09.2002 14:45:20)
     * 
     * @param newCanton2
     *            java.lang.String
     */
    public void setCanton2(java.lang.String newCanton2) {
        canton2 = newCanton2;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.09.2002 14:45:43)
     * 
     * @param newCanton3
     *            java.lang.String
     */
    public void setCanton3(java.lang.String newCanton3) {
        canton3 = newCanton3;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.09.2002 14:46:06)
     * 
     * @param newCanton4
     *            java.lang.String
     */
    public void setCanton4(java.lang.String newCanton4) {
        canton4 = newCanton4;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53)
     * 
     * @param newAgenceCommunaleLocalite
     *            java.lang.String
     */
    public void setCapital(java.lang.String newCapital) {
        capital = newCapital;
    }

    /**
     * Sets the codeEtatDecision.
     * 
     * @param codeEtatDecision
     *            The codeEtatDecision to set
     */
    public void setCodeEtatDecision(java.lang.String codeEtatDecision) {
        this.codeEtatDecision = codeEtatDecision;
    }

    public void setColonneSelection(String value) {
        colonneSelection = value;
    }

    public void setCommuneCode(java.lang.String communeCode) {
        this.communeCode = communeCode;
    }

    public void setCommuneLibelle(java.lang.String commune) {
        communeLibelle = commune;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53)
     * 
     * @param newAgenceCommunaleLocalite
     *            java.lang.String
     */
    public void setConjoint(java.lang.String newConjoint) {
        conjoint = newConjoint;
    }

    /**
     * 
     * Set de la valeur du rachat LPP
     * 
     * @param _rachatLPP
     */
    public void setRachatLPP(String _rachatLPP) {
        rachatLPP = _rachatLPP;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53)
     * 
     * @param newAgenceCommunaleLocalite
     *            java.lang.String
     */
    public void setCotisation1(java.lang.String newCotisation1) {
        cotisation1 = newCotisation1;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53)
     * 
     * @param newAgenceCommunaleLocalite
     *            java.lang.String
     */
    public void setCotisation2(java.lang.String newCotisation2) {
        cotisation2 = newCotisation2;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53)
     * 
     * @param newAgenceCommunaleLocalite
     *            java.lang.String
     */
    public void setCotisationSalarie(java.lang.String newCotisationSalarie) {
        cotisationSalarie = newCotisationSalarie;
    }

    public void setDateDecision(String string) {
        dateDecision = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53)
     * 
     * @param newAgenceCommunaleLocalite
     *            java.lang.String
     */
    public void setDateFortune(java.lang.String newDateFortune) {
        dateFortune = newDateFortune;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53)
     * 
     * @param newAgenceCommunaleLocalite
     *            java.lang.String
     */
    public void setDebutExercice1(java.lang.String newDebutExercice1) {
        debutExercice1 = newDebutExercice1;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53)
     * 
     * @param newAgenceCommunaleLocalite
     *            java.lang.String
     */
    public void setDebutExercice2(java.lang.String newDebutExercice2) {
        debutExercice2 = newDebutExercice2;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.07.2003 14:15:11)
     * 
     * @param newDescriptionTiers
     *            java.lang.String
     */
    public void setDescriptionTiers(java.lang.String newDescriptionTiers) {
        descriptionTiers = newDescriptionTiers;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53)
     * 
     * @param newAgenceCommunaleLocalite
     *            java.lang.String
     */
    public void setDette(java.lang.String newDette) {
        dette = newDette;
    }

    public void setDocListe(String docListe) {
        this.docListe = docListe;
    }

    public void setDuplicata(Boolean duplicata) {
        this.duplicata = duplicata;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53)
     * 
     * @param newAgenceCommunaleLocalite
     *            java.lang.String
     */
    public void setEtatDecision(java.lang.String newEtatDecision) {
        etatDecision = newEtatDecision;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53)
     * 
     * @param newAgenceCommunaleLocalite
     *            java.lang.String
     */
    public void setFinExercice1(java.lang.String newFinExercice1) {
        finExercice1 = newFinExercice1;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53)
     * 
     * @param newAgenceCommunaleLocalite
     *            java.lang.String
     */
    public void setFinExercice2(java.lang.String newFinExercice2) {
        finExercice2 = newFinExercice2;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.04.2003 13:11:31)
     * 
     * @param newFortuneDeterminante
     *            java.lang.String
     */
    public void setFortuneDeterminante(java.lang.String newFortuneDeterminante) {
        fortuneDeterminante = newFortuneDeterminante;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53)
     * 
     * @param newAgenceCommunaleLocalite
     *            java.lang.String
     */
    public void setFortuneTotale(java.lang.String newFortuneTotale) {
        fortuneTotale = newFortuneTotale;
    }

    public void setIdValidation(String idValidation) {
        this.idValidation = idValidation;
    }

    /**
     * @param string
     */
    public void setLibellePassage(String string) {
        libellePassage = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.09.2002 14:42:21)
     * 
     * @param newMontantImmobilier1
     *            java.lang.String
     */
    public void setMontantImmobilier1(java.lang.String newMontantImmobilier1) {
        montantImmobilier1 = newMontantImmobilier1;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.09.2002 14:42:21)
     * 
     * @param newMontantImmobilier1
     *            java.lang.String
     */
    public void setMontantImmobilier2(java.lang.String newMontantImmobilier2) {
        montantImmobilier2 = newMontantImmobilier2;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.09.2002 14:42:21)
     * 
     * @param newMontantImmobilier1
     *            java.lang.String
     */
    public void setMontantImmobilier3(java.lang.String newMontantImmobilier3) {
        montantImmobilier3 = newMontantImmobilier3;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.09.2002 14:42:21)
     * 
     * @param newMontantImmobilier1
     *            java.lang.String
     */
    public void setMontantImmobilier4(java.lang.String newMontantImmobilier4) {
        montantImmobilier4 = newMontantImmobilier4;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.04.2003 12:22:52)
     * 
     * @param newNbMoisExercice1
     *            java.lang.String
     */
    public void setNbMoisExercice1(java.lang.String newNbMoisExercice1) {
        nbMoisExercice1 = newNbMoisExercice1;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.04.2003 12:23:06)
     * 
     * @param newNbMoisExercice2
     *            java.lang.String
     */
    public void setNbMoisExercice2(java.lang.String newNbMoisExercice2) {
        nbMoisExercice2 = newNbMoisExercice2;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.04.2003 12:23:35)
     * 
     * @param newNbMoisRevenuAutre1
     *            java.lang.String
     */
    public void setNbMoisRevenuAutre1(java.lang.String newNbMoisRevenuAutre1) {
        nbMoisRevenuAutre1 = newNbMoisRevenuAutre1;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.04.2003 12:23:47)
     * 
     * @param newNbMoisRevenuAutre2
     *            java.lang.String
     */
    public void setNbMoisRevenuAutre2(java.lang.String newNbMoisRevenuAutre2) {
        nbMoisRevenuAutre2 = newNbMoisRevenuAutre2;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.07.2003 09:57:32)
     * 
     * @param newNumAffilie
     *            java.lang.String
     */
    public void setNumAffilie(java.lang.String newNumAffilie) {
        numAffilie = newNumAffilie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53)
     * 
     * @param newAgenceCommunaleLocalite
     *            java.lang.String
     */
    public void setNumIfdDefinitif(java.lang.String newNumIfdDefinitif) {
        numIfdDefinitif = newNumIfdDefinitif;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53)
     * 
     * @param newAgenceCommunaleLocalite
     *            java.lang.String
     */
    public void setPeriodicite(java.lang.String newPeriodicite) {
        periodicite = newPeriodicite;
    }

    /**
     * @param boolean1
     */
    public void setProcessExterne(Boolean boolean1) {
        processExterne = boolean1;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53)
     * 
     * @param newAgenceCommunaleLocalite
     *            java.lang.String
     */
    public void setRevenu1(java.lang.String newRevenu1) {
        revenu1 = newRevenu1;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53)
     * 
     * @param newAgenceCommunaleLocalite
     *            java.lang.String
     */
    public void setRevenu2(java.lang.String newRevenu2) {
        revenu2 = newRevenu2;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.04.2003 11:35:17)
     * 
     * @param newRevenuAutre1
     *            java.lang.String
     */
    public void setRevenuAutre1(java.lang.String newRevenuAutre1) {
        revenuAutre1 = newRevenuAutre1;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.04.2003 11:36:58)
     * 
     * @param newRevenuAutre2
     *            java.lang.String
     */
    public void setRevenuAutre2(java.lang.String newRevenuAutre2) {
        revenuAutre2 = newRevenuAutre2;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53)
     * 
     * @param newAgenceCommunaleLocalite
     *            java.lang.String
     */
    public void setRevenuCiForce(java.lang.String newRevenuCiForce) {
        revenuCiForce = newRevenuCiForce;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53)
     * 
     * @param newAgenceCommunaleLocalite
     *            java.lang.String
     */
    public void setRevenuCiForce0(Boolean newRevenuCiForce0) {
        revenuCiForce0 = newRevenuCiForce0;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.04.2003 09:19:13)
     * 
     * @param newRevenuDeterminant
     *            java.lang.String
     */
    public void setRevenuDeterminant(java.lang.String newRevenuDeterminant) {
        revenuDeterminant = newRevenuDeterminant;
    }

    public void setSaveCompteurPourRemise(float saveCompteurPourRemise) {
        this.saveCompteurPourRemise = saveCompteurPourRemise;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.07.2003 14:05:01)
     * 
     * @param newSelection
     *            java.lang.String
     */
    public void setSelection(java.lang.String newSelection) {
        selection = newSelection;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.07.2003 08:29:52)
     * 
     * @param newSelectionCjt
     *            java.lang.String
     */
    public void setSelectionCjt(java.lang.String newSelectionCjt) {
        selectionCjt = newSelectionCjt;
    }

    /**
     * @param string
     */
    public void setSourceInformation(java.lang.String string) {
        sourceInformation = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (19.03.2003 13:16:17)
     * 
     * @param newTiers
     *            globaz.pyxis.db.tiers.TITiersViewBean
     */
    @Override
    public void setTiers(globaz.pyxis.db.tiers.TITiersViewBean newTiers) {
        tiers = newTiers;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.09.2002 14:46:51)
     * 
     * @param newTypeTerrain1
     *            java.lang.String
     */
    public void setTypeTerrain1(java.lang.String newTypeTerrain1) {
        typeTerrain1 = newTypeTerrain1;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.09.2002 14:47:13)
     * 
     * @param newTypeTerrain2
     *            java.lang.String
     */
    public void setTypeTerrain2(java.lang.String newTypeTerrain2) {
        typeTerrain2 = newTypeTerrain2;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.09.2002 14:47:38)
     * 
     * @param newTypeTerrain3
     *            java.lang.String
     */
    public void setTypeTerrain3(java.lang.String newTypeTerrain3) {
        typeTerrain3 = newTypeTerrain3;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.09.2002 14:48:03)
     * 
     * @param newTypeTerrain4
     *            java.lang.String
     */
    public void setTypeTerrain4(java.lang.String newTypeTerrain4) {
        typeTerrain4 = newTypeTerrain4;
    }

    public void setWantWarning(String wantWarning) {
        this.wantWarning = wantWarning;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }

}
