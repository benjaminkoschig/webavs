package globaz.corvus.vb.ordresversements;

import globaz.corvus.api.ordresversements.IREOrdresVersements;
import globaz.framework.bean.FWAJAXFindInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import ch.globaz.corvus.business.models.ordresversements.OrdreVersementComplexModel;
import ch.globaz.corvus.business.models.ordresversements.OrdreVersementSearchModel;
import ch.globaz.corvus.business.models.ordresversements.SimpleOrdreVersement;
import ch.globaz.corvus.domaine.Decision;
import ch.globaz.corvus.domaine.SoldePourRestitution;
import ch.globaz.corvus.domaine.constantes.EtatDecisionRente;
import ch.globaz.corvus.domaine.constantes.TypeOrdreVersement;
import ch.globaz.corvus.domaine.constantes.TypeRenteVerseeATort;
import ch.globaz.corvus.domaine.constantes.TypeSoldePourRestitution;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;

public class REOrdresVersementsAjaxViewBean implements FWAJAXFindInterface, IREOrdreVersementAjaxViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Collection<JadeCodeSysteme> codesSystemeTypeOrdreVersement;
    private Collection<JadeCodeSysteme> codesSystemeTypeRenteVerseeATort;
    private boolean compensationInterDecisionPossible;
    private Decision decision;
    private String designationPourDettesEnCompta;
    private RELigneDetailOrdreVersement ligneBeneficiairePrincipal;
    private Collection<RELigneDetailOrdreVersement> lignesCompensationsInterDecisionNegatives;
    private Collection<RELigneDetailOrdreVersement> lignesCompensationsInterDecisionPositives;
    private Collection<RELigneDetailOrdreVersement> lignesCreanciers;
    private Collection<RELigneDetailOrdreVersement> lignesDettesEnComptaEtDiminutionDeRente;
    private Collection<RELigneDetailOrdreVersement> lignesInteretsMoratoires;
    private Collection<RELigneDetailOrdreVersement> lignesRentesVerseesATort;
    private Collection<RELigneDetailOrdreVersement> lignesSoldesPourRestitution;
    private String message;
    private String msgType;
    private OrdreVersementSearchModel searchModel;
    private BISession session;
    private SoldePourRestitution soldePourRestitutionBeneficiairePrincipal;

    public REOrdresVersementsAjaxViewBean() {
        super();

        codesSystemeTypeOrdreVersement = new ArrayList<JadeCodeSysteme>();
        codesSystemeTypeRenteVerseeATort = new ArrayList<JadeCodeSysteme>();
        compensationInterDecisionPossible = false;
        decision = new Decision();
        ligneBeneficiairePrincipal = new RELigneDetailOrdreVersement();
        lignesCompensationsInterDecisionNegatives = new ArrayList<RELigneDetailOrdreVersement>();
        lignesCompensationsInterDecisionPositives = new ArrayList<RELigneDetailOrdreVersement>();
        lignesCreanciers = new ArrayList<RELigneDetailOrdreVersement>();
        lignesDettesEnComptaEtDiminutionDeRente = new ArrayList<RELigneDetailOrdreVersement>();
        lignesInteretsMoratoires = new ArrayList<RELigneDetailOrdreVersement>();
        lignesRentesVerseesATort = new ArrayList<RELigneDetailOrdreVersement>();
        lignesSoldesPourRestitution = new HashSet<RELigneDetailOrdreVersement>();
        message = null;
        msgType = null;
        searchModel = new OrdreVersementSearchModel();
        session = null;
        soldePourRestitutionBeneficiairePrincipal = new SoldePourRestitution();
    }

    private RELigneDetailOrdreVersement extraireLigneDetailCIDNegative(OrdreVersementComplexModel unOrdreVersement)
            throws RemoteException {
        RELigneDetailOrdreVersement uneLigne = new RELigneDetailOrdreVersement();

        uneLigne.setIdOrdreVersement(unOrdreVersement.getIdOrdreVersement());
        uneLigne.setMontant(unOrdreVersement.getMontantCompensationInterDecisionNegative());
        uneLigne.setCompensationInterDecision(true);

        StringBuilder designation = new StringBuilder();
        designation.append(unOrdreVersement.getNomTiersCIDNegative()).append(" ")
                .append(unOrdreVersement.getPrenomTiersCIDNegative());
        uneLigne.setDesignation(designation.toString());

        return uneLigne;
    }

    private RELigneDetailOrdreVersement extraireLigneDetailCIDPositive(OrdreVersementComplexModel unOrdreVersement)
            throws RemoteException {
        RELigneDetailOrdreVersement uneLigne = new RELigneDetailOrdreVersement();

        uneLigne.setIdOrdreVersement(unOrdreVersement.getIdOrdreVersement());
        uneLigne.setMontant(unOrdreVersement.getMontantCompensationInterDecisionPositive());
        uneLigne.setCompensationInterDecision(true);

        StringBuilder designation = new StringBuilder();
        designation.append(unOrdreVersement.getNomTiersCIDPositive()).append(" ")
                .append(unOrdreVersement.getPrenomTiersCIDPositive());
        uneLigne.setDesignation(designation.toString());

        return uneLigne;
    }

    private RELigneDetailOrdreVersement extraireLigneDetailOrdreVersement(OrdreVersementComplexModel unOrdreVersement)
            throws RemoteException {
        RELigneDetailOrdreVersement uneLigne = new RELigneDetailOrdreVersement();

        uneLigne.setIdOrdreVersement(unOrdreVersement.getIdOrdreVersement());
        uneLigne.setCompense(unOrdreVersement.getIsCompense());
        uneLigne.setMontant(unOrdreVersement.getMontant());
        uneLigne.setMontantDette(unOrdreVersement.getMontantDette());
        uneLigne.setTypeOrdreVersement(getLibelleCodeSystemeTypeOrdreVersement(unOrdreVersement.getCsType()));
        uneLigne.setDesignation(getDesignationPourOrdreVersement(unOrdreVersement));

        return uneLigne;
    }

    private RELigneDetailOrdreVersement extraireLigneSoldePourRestitution(
            OrdreVersementComplexModel unSoldePourRestitution) {
        RELigneDetailOrdreVersement unDetail = new RELigneDetailOrdreVersement();
        unDetail.setCompense(true); // pour que le montant s'affiche à l'écran

        TypeSoldePourRestitution typeSoldePourRestitution = TypeSoldePourRestitution.parse(unSoldePourRestitution
                .getCsTypeSoldeRestitution());
        if (typeSoldePourRestitution == TypeSoldePourRestitution.RETENUES) {
            unDetail.setMontantDette(unSoldePourRestitution.getMontantSoldePourRestitution());
            unDetail.setMontant(unSoldePourRestitution.getMontantRetenueMensuelleSoldePourRestitution());
        } else if (typeSoldePourRestitution == TypeSoldePourRestitution.RESTITUTION) {
            unDetail.setMontant(unSoldePourRestitution.getMontantSoldePourRestitution());
        }

        return unDetail;
    }

    @Override
    public void find() throws Exception {
        JadePersistenceManager.search(searchModel);

        ligneBeneficiairePrincipal = new RELigneDetailOrdreVersement();
        lignesCreanciers = new ArrayList<RELigneDetailOrdreVersement>();
        lignesDettesEnComptaEtDiminutionDeRente = new ArrayList<RELigneDetailOrdreVersement>();
        lignesInteretsMoratoires = new ArrayList<RELigneDetailOrdreVersement>();
        lignesRentesVerseesATort = new ArrayList<RELigneDetailOrdreVersement>();

        Map<String, OrdreVersementComplexModel> ordresVersementParId = new HashMap<String, OrdreVersementComplexModel>();
        Map<String, OrdreVersementComplexModel> cidParId = new HashMap<String, OrdreVersementComplexModel>();
        Map<String, OrdreVersementComplexModel> soldePourRestitutionParId = new HashMap<String, OrdreVersementComplexModel>();

        for (JadeAbstractModel model : searchModel.getSearchResults()) {
            OrdreVersementComplexModel unOrdreVersement = (OrdreVersementComplexModel) model;

            if (!ordresVersementParId.containsKey(unOrdreVersement.getIdOrdreVersement())) {
                ordresVersementParId.put(unOrdreVersement.getIdOrdreVersement(), unOrdreVersement);
            }

            if (!JadeStringUtil.isBlankOrZero(unOrdreVersement.getIdCompensationInterDecisionNegative())
                    && !cidParId.containsKey(unOrdreVersement.getIdCompensationInterDecisionNegative())) {
                cidParId.put(unOrdreVersement.getIdCompensationInterDecisionNegative(), unOrdreVersement);
            }
            if (!JadeStringUtil.isBlankOrZero(unOrdreVersement.getIdCompensationInterDecisionPositive())
                    && !cidParId.containsKey(unOrdreVersement.getIdCompensationInterDecisionPositive())) {
                cidParId.put(unOrdreVersement.getIdCompensationInterDecisionPositive(), unOrdreVersement);
            }

            if (!JadeStringUtil.isBlankOrZero(unOrdreVersement.getIdSoldePourRestitution())
                    && !soldePourRestitutionParId.containsKey(unOrdreVersement.getIdSoldePourRestitution())) {
                soldePourRestitutionParId.put(unOrdreVersement.getIdSoldePourRestitution(), unOrdreVersement);
            }
        }

        for (OrdreVersementComplexModel unOrdreVersement : ordresVersementParId.values()) {
            RELigneDetailOrdreVersement uneLigne = extraireLigneDetailOrdreVersement(unOrdreVersement);

            if (!isDonneurCompensationInterDecision(unOrdreVersement)) {

                TypeOrdreVersement typeOrdreVersement = TypeOrdreVersement.parse(unOrdreVersement.getCsType());

                switch (typeOrdreVersement) {
                    case DETTE:
                        lignesRentesVerseesATort.add(uneLigne);
                        break;
                    case CREANCIER:
                    case ASSURANCE_SOCIALE:
                    case IMPOT_A_LA_SOURCE:
                        lignesCreanciers.add(uneLigne);
                        break;
                    case DETTE_RENTE_AVANCES:
                    case DETTE_RENTE_DECISION:
                    case DETTE_RENTE_PRST_BLOQUE:
                    case DETTE_RENTE_RESTITUTION:
                    case DETTE_RENTE_RETOUR:
                    case DIMINUTION_DE_RENTE:
                        lignesDettesEnComptaEtDiminutionDeRente.add(uneLigne);
                        break;
                    case INTERET_MORATOIRE:
                        lignesInteretsMoratoires.add(uneLigne);
                        break;
                    case BENEFICIAIRE_PRINCIPAL:
                        ligneBeneficiairePrincipal = uneLigne;
                        break;
                    case ALOCATION_DE_NOEL:
                    case JOURS_APPOINT:
                        // on ne traite pas ces cas car spécifique PC
                        break;
                }
            }
        }

        for (OrdreVersementComplexModel unCID : cidParId.values()) {
            if (isBeneficiaireCompensationInterDecision(unCID)) {
                lignesCompensationsInterDecisionPositives.add(extraireLigneDetailCIDPositive(unCID));
            }
            if (isDonneurCompensationInterDecision(unCID)) {
                lignesCompensationsInterDecisionNegatives.add(extraireLigneDetailCIDNegative(unCID));
            }
        }

        for (OrdreVersementComplexModel unSoldePourRestitution : soldePourRestitutionParId.values()) {
            TypeSoldePourRestitution typeSoldePourRestitution = TypeSoldePourRestitution.parse(unSoldePourRestitution
                    .getCsTypeSoldeRestitution());
            if (typeSoldePourRestitution == TypeSoldePourRestitution.RESTITUTION) {
                lignesSoldesPourRestitution.add(extraireLigneSoldePourRestitution(unSoldePourRestitution));
            } else if (typeSoldePourRestitution == TypeSoldePourRestitution.RETENUES) {
                // la retenue est déjà chargée par le helper
            }
        }
    }

    public Collection<JadeCodeSysteme> getCodesSystemeTypeOrdreVersement() {
        return codesSystemeTypeOrdreVersement;
    }

    public Integer getCsEtatDecision() {
        return decision.getEtat().getCodeSysteme();
    }

    @Override
    public JadeAbstractModel getCurrentEntity() {
        return new SimpleOrdreVersement();
    }

    public String getDesignationBeneficiairePrincipal() {
        return ligneBeneficiairePrincipal.getDesignation();
    }

    private String getDesignationPourOrdreVersement(OrdreVersementComplexModel unOrdreVersement) throws RemoteException {

        TypeOrdreVersement typeOrdreVersement = TypeOrdreVersement.parse(unOrdreVersement.getCsType());

        StringBuilder designation = new StringBuilder();

        switch (typeOrdreVersement) {
            case IMPOT_A_LA_SOURCE:
                designation.append(BSessionUtil.getSessionFromThreadContext().getLabel("MENU_LISTE_IMPOT_SOURCE"));
                break;
            case DETTE_RENTE_AVANCES:
            case DETTE_RENTE_DECISION:
            case DETTE_RENTE_PRST_BLOQUE:
            case DETTE_RENTE_RESTITUTION:
            case DETTE_RENTE_RETOUR:
                designation.append(designationPourDettesEnCompta).append(" (").append(unOrdreVersement.getNoFacture())
                        .append(")");
                break;
            case BENEFICIAIRE_PRINCIPAL:
            case CREANCIER:
            case ASSURANCE_SOCIALE:
                designation.append(unOrdreVersement.getNomTiersOrdreVersement()).append(" ")
                        .append(unOrdreVersement.getPrenomTiersOrdreVersement());
                break;
            case DETTE:
            case DIMINUTION_DE_RENTE:
                if (JadeStringUtil.isBlank(unOrdreVersement.getCsTypeRenteVerseeATort())) {
                    designation.append(getLibelleCodeSystemeTypeOrdreVersement(unOrdreVersement.getCsType()));
                } else {
                    if (TypeRenteVerseeATort.SAISIE_MANUELLE.equals(TypeRenteVerseeATort.parse(unOrdreVersement
                            .getCsTypeRenteVerseeATort()))) {
                        designation.append(unOrdreVersement.getDescriptionSaisieManuelleRenteVerseeATort());
                    } else {
                        designation.append(getLibelleCodeSystemeTypeRenteVerseeATort(unOrdreVersement
                                .getCsTypeRenteVerseeATort()));
                    }
                }

                designation.append(" ( ").append(getInformationSurLaRente(unOrdreVersement)).append(" ) ");
                break;
            case ALOCATION_DE_NOEL:
            case JOURS_APPOINT:
                // cas non traités car spécifique PC
                break;
            case INTERET_MORATOIRE:
                // cas traité sur l'écran
                break;
        }

        return designation.toString();
    }

    public Long getIdDecision() {
        return decision.getId();
    }

    public String getIdOrdreVersementBeneficiairePrincipal() {
        return ligneBeneficiairePrincipal.getIdOrdreVersement();
    }

    private String getInformationSurLaRente(OrdreVersementComplexModel unOrdreVersement) {
        StringBuilder info = new StringBuilder();

        info.append(unOrdreVersement.getNomTiersOrdreVersement()).append(" ")
                .append(unOrdreVersement.getPrenomTiersOrdreVersement()).append(" / ");

        if (!JadeStringUtil.isBlankOrZero(unOrdreVersement.getIdPrestationAccordeeCompensee())
                || !JadeStringUtil.isBlankOrZero(unOrdreVersement.getIdPrestationAccordeeDiminuee())) {

            if (!JadeStringUtil.isBlankOrZero(unOrdreVersement.getIdPrestationAccordeeDiminuee())) {
                info.append(unOrdreVersement.getCodePrestationDiminuee());
            } else {
                info.append(unOrdreVersement.getCodePrestationCompensee());
            }

            info.append(" / ");

            if (!JadeStringUtil.isBlankOrZero(unOrdreVersement.getDateDebutRenteVerseeATort())) {
                info.append(unOrdreVersement.getDateDebutRenteVerseeATort()).append(" - ")
                        .append(unOrdreVersement.getDateFinRenteVerseeATort());
            } else {
                if (!JadeStringUtil.isBlankOrZero(unOrdreVersement.getIdPrestationAccordeeDiminuee())) {
                    info.append(unOrdreVersement.getDateDebutDroitDiminuee()).append(" - ")
                            .append(unOrdreVersement.getDateFinDroitDiminuee());
                } else {
                    info.append(unOrdreVersement.getDateDebutDroitCompensee()).append(" - ")
                            .append(unOrdreVersement.getDateFinDroitCompensee());
                }
            }
        } else if (!unOrdreVersement.isRenteVerseeATortSaisieManuelle()) {

            info.append(unOrdreVersement.getCodePrestationRenteAccordeeAncienDroitRenteVerseeATort()).append(" / ");
            info.append(unOrdreVersement.getDateDebutRenteVerseeATort()).append(" - ")
                    .append(unOrdreVersement.getDateFinRenteVerseeATort());
        } else {
            info.append(BSessionUtil.getSessionFromThreadContext().getLabel("MENU_OPTION_SAISIE_MANUELLE"));
        }

        return info.toString();
    }

    @Override
    public BISession getISession() {
        return session;
    }

    private String getLibelleCodeSystemeTypeOrdreVersement(String csTypeOrdreVersement) throws RemoteException {
        for (JadeCodeSysteme codeSysteme : codesSystemeTypeOrdreVersement) {
            if (codeSysteme.getIdCodeSysteme().equals(csTypeOrdreVersement)) {
                return codeSysteme.getTraduction(Langues.getLangueDepuisCodeIso(session.getIdLangueISO()));
            }
        }
        return "";
    }

    private String getLibelleCodeSystemeTypeRenteVerseeATort(String csTypeRenteVerseeATort) throws RemoteException {
        for (JadeCodeSysteme codeSysteme : codesSystemeTypeRenteVerseeATort) {
            if (codeSysteme.getIdCodeSysteme().equals(csTypeRenteVerseeATort)) {
                return codeSysteme.getTraduction(Langues.getLangueDepuisCodeIso(session.getIdLangueISO()));
            }
        }
        return "";
    }

    public RELigneDetailOrdreVersement getLigneBeneficiairePrincipal() {
        return ligneBeneficiairePrincipal;
    }

    public Collection<RELigneDetailOrdreVersement> getLignesCompensationsInterDecisionNegatives() {
        return lignesCompensationsInterDecisionNegatives;
    }

    public Collection<RELigneDetailOrdreVersement> getLignesCompensationsInterDecisionPositives() {
        return lignesCompensationsInterDecisionPositives;
    }

    public Collection<RELigneDetailOrdreVersement> getLignesCreanciers() {
        return lignesCreanciers;
    }

    public Collection<RELigneDetailOrdreVersement> getLignesDettesEnCompta() {
        return lignesDettesEnComptaEtDiminutionDeRente;
    }

    public Collection<RELigneDetailOrdreVersement> getLignesInteretsMoratoires() {
        return lignesInteretsMoratoires;
    }

    public Collection<RELigneDetailOrdreVersement> getLignesRentesVerseesATort() {
        return lignesRentesVerseesATort;
    }

    public Collection<RELigneDetailOrdreVersement> getLignesSoldesPourRestitution() {
        return lignesSoldesPourRestitution;
    }

    public List<RELigneDetailOrdreVersement> getListe() throws RemoteException {
        List<RELigneDetailOrdreVersement> liste = new ArrayList<RELigneDetailOrdreVersement>();

        return liste;
    }

    @Override
    public FWListViewBeanInterface getListViewBean() {
        return null;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getMontantBeneficiairePrincipal() {
        return ligneBeneficiairePrincipal.getMontant();
    }

    public String getMontantSousTotal() {
        FWCurrency montantSousTotal = new FWCurrency();

        montantSousTotal.add(getMontantTotalBeneficiairePrincipal());
        montantSousTotal.add(getMontantTotalDettesCompensees());

        return montantSousTotal.toStringFormat();
    }

    public String getMontantTotalBeneficiairePrincipal() {
        FWCurrency montantTotal = new FWCurrency();

        montantTotal.add(getMontantBeneficiairePrincipal());
        montantTotal.add(getMontantTotalInteretsMoratoires());

        return montantTotal.toStringFormat();
    }

    public FWCurrency getMontantTotalCIDNegatives() {
        FWCurrency montantTotal = new FWCurrency();

        for (RELigneDetailOrdreVersement uneCID : lignesCompensationsInterDecisionNegatives) {
            montantTotal.sub(uneCID.getMontant());
        }

        return montantTotal;
    }

    public FWCurrency getMontantTotalCIDPositives() {
        FWCurrency montantTotal = new FWCurrency();

        for (RELigneDetailOrdreVersement uneCID : lignesCompensationsInterDecisionPositives) {
            montantTotal.add(uneCID.getMontant());
        }

        return montantTotal;
    }

    public String getMontantTotalCreanciers() {
        FWCurrency montantTotal = new FWCurrency();

        for (RELigneDetailOrdreVersement uneLigne : lignesCreanciers) {
            montantTotal.sub(uneLigne.getMontant());
        }

        return montantTotal.toStringFormat();
    }

    public String getMontantTotalCreanciersCIDEtRestitution() {
        FWCurrency montantTotal = new FWCurrency();

        montantTotal.add(getMontantTotalCreanciers());
        montantTotal.add(getMontantTotalCIDPositives());
        montantTotal.add(getMontantTotalRestitution());

        return montantTotal.toStringFormat();
    }

    public String getMontantTotalCreanciersEtAutresDettes() {
        FWCurrency montantTotal = new FWCurrency();

        montantTotal.add(getMontantTotalCreanciersCIDEtRestitution());
        montantTotal.add(getMontantTotalDettesEnComptaCompensees());

        return montantTotal.toStringFormat();
    }

    public String getMontantTotalDettesCompensees() {
        FWCurrency montantTotalDettesCompensees = new FWCurrency();

        montantTotalDettesCompensees.add(getMontantTotalDettesEnComptaCompensees());

        for (RELigneDetailOrdreVersement uneRenteVerseeATort : lignesRentesVerseesATort) {
            montantTotalDettesCompensees.sub(uneRenteVerseeATort.getMontant());
        }

        for (RELigneDetailOrdreVersement uneCID : lignesCompensationsInterDecisionNegatives) {
            montantTotalDettesCompensees.sub(uneCID.getMontant());
        }

        return montantTotalDettesCompensees.toStringFormat();
    }

    public String getMontantTotalDettesEnComptaCompensees() {
        FWCurrency montantTotalDettesCompensees = new FWCurrency();

        for (RELigneDetailOrdreVersement uneDetteEnCompta : lignesDettesEnComptaEtDiminutionDeRente) {
            if (uneDetteEnCompta.isCompense()) {
                montantTotalDettesCompensees.sub(uneDetteEnCompta.getMontant());
            }
        }

        return montantTotalDettesCompensees.toStringFormat();
    }

    public String getMontantTotalInteretsMoratoires() {
        FWCurrency montantTotal = new FWCurrency();

        for (RELigneDetailOrdreVersement uneLigne : lignesInteretsMoratoires) {
            montantTotal.add(uneLigne.getMontant());
        }

        return montantTotal.toStringFormat();
    }

    public String getMontantTotalPrestationsDejaVersees() {
        FWCurrency montantTotal = new FWCurrency();

        for (RELigneDetailOrdreVersement uneLigne : lignesRentesVerseesATort) {
            montantTotal.sub(uneLigne.getMontant());
        }

        return montantTotal.toStringFormat();
    }

    public String getMontantTotalRestitution() {
        FWCurrency montantTotal = new FWCurrency();

        for (RELigneDetailOrdreVersement uneRestitution : lignesSoldesPourRestitution) {
            montantTotal.add(uneRestitution.getMontant());
        }

        return montantTotal.toStringFormat();
    }

    public String getMontantTotalSolde() {
        FWCurrency montantTotal = new FWCurrency();

        montantTotal.add(getMontantSousTotal());
        montantTotal.add(getMontantTotalCreanciersCIDEtRestitution());

        return montantTotal.toStringFormat();
    }

    @Override
    public String getMsgType() {
        return msgType;
    }

    @Override
    public String getProvenance() {
        return "OV";
    }

    @Override
    public JadeAbstractSearchModel getSearchModel() {
        return searchModel;
    }

    public SoldePourRestitution getSoldePourRestitutionBeneficiairePrincipal() {
        return soldePourRestitutionBeneficiairePrincipal;
    }

    public String getTypeOrdreVersementBeneficiairePrincipal() {
        return ligneBeneficiairePrincipal.getTypeOrdreVersement();
    }

    public String getTypeOrdreVersementInteretMoratoire() {
        for (RELigneDetailOrdreVersement unInteretMoratoire : lignesInteretsMoratoires) {
            return unInteretMoratoire.getTypeOrdreVersement();
        }
        return "";
    }

    @Override
    public boolean hasList() {
        return false;
    }

    @Override
    public void initList() {
        searchModel = new OrdreVersementSearchModel();
    }

    private boolean isBeneficiaireCompensationInterDecision(OrdreVersementComplexModel ordreVersement) {
        return !JadeStringUtil.isBlankOrZero(ordreVersement.getIdCompensationInterDecisionPositive());
    }

    private boolean isCompensationInterDecision(OrdreVersementComplexModel ordreVersement) {
        return IREOrdresVersements.CS_TYPE_DETTE.equals(ordreVersement.getCsType())
                && ordreVersement.getIsCompensationInterDecision();
    }

    public boolean isCompensationInterDecisionPossible() {
        return compensationInterDecisionPossible;
    }

    private boolean isDonneurCompensationInterDecision(OrdreVersementComplexModel ordreVersement) {
        return isCompensationInterDecision(ordreVersement)
                && !JadeStringUtil.isBlankOrZero(ordreVersement.getIdCompensationInterDecisionNegative());
    }

    public boolean isModificationPossible() {
        return decision.getEtat() != EtatDecisionRente.VALIDE;
    }

    public boolean isOrdresVersementPresents() {
        return searchModel.getSize() > 0;
    }

    public boolean isPlusieursEntreesPourCreancierCIDEtRestitution() {
        int nombreEntrees = 0;

        nombreEntrees += lignesCompensationsInterDecisionPositives.size();
        nombreEntrees += lignesCreanciers.size();
        nombreEntrees += lignesSoldesPourRestitution.size();

        return nombreEntrees > 1;
    }

    public boolean isRetenueMensuelleEgalMontantTotalRetenue() {
        return isRetenueMensuelleEnCours()
                && (soldePourRestitutionBeneficiairePrincipal.getMontantRetenueMensuelle().compareTo(
                        soldePourRestitutionBeneficiairePrincipal.getMontantRestitution()) >= 0);
    }

    public boolean isRetenueMensuelleEnCours() {
        return soldePourRestitutionBeneficiairePrincipal.getMontantRetenueMensuelle().compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isRetenueMensuellePossible() {
        return (soldePourRestitutionBeneficiairePrincipal.getType() == TypeSoldePourRestitution.RETENUES)
                || (!decision.isRetroPur() && (decision.getSolde().compareTo(BigDecimal.ZERO) < 0));
    }

    public boolean isRetenuePresenteMaisModificationInterdite() {
        return decision.isRetroPur() && (decision.getSolde().compareTo(BigDecimal.ZERO) < 0);
    }

    @Override
    public Iterator<?> iterator() {
        return null;
    }

    public void setCodesSystemeTypeOrdreVersement(Collection<JadeCodeSysteme> codesSystemeTypeOrdreVersement) {
        this.codesSystemeTypeOrdreVersement = codesSystemeTypeOrdreVersement;
    }

    public void setCodesSystemeTypeRenteVerseeATort(Collection<JadeCodeSysteme> codesSystemeTypeRenteVerseeATort) {
        this.codesSystemeTypeRenteVerseeATort = codesSystemeTypeRenteVerseeATort;
    }

    public void setCompensationInterDecisionPossible(boolean compensationInterDecisionPossible) {
        this.compensationInterDecisionPossible = compensationInterDecisionPossible;
    }

    public void setCsEtatDecision(String csEtatDecision) {
        decision.setEtat(EtatDecisionRente.parse(csEtatDecision));
    }

    public void setDecision(Decision decision) {
        this.decision = decision;
    }

    @Override
    public void setDefinedSearchSize(int definedSearchSize) {
        searchModel.setDefinedSearchSize(definedSearchSize);
    }

    public void setDesignationPourDettesEnCompta(String designationPourDettesEnCompta) {
        this.designationPourDettesEnCompta = designationPourDettesEnCompta;
    }

    @Override
    public void setGetListe(boolean getListe) {
    }

    public void setIdDecision(String idDecision) {
        decision.setId(Long.parseLong(idDecision));
    }

    @Override
    public void setISession(BISession newSession) {
        session = newSession;
    }

    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    @Override
    public void setOffset(int offset) {
        searchModel.setOffset(offset);
    }

    public void setSoldePourRestitutionBeneficiairePrincipal(
            SoldePourRestitution soldePourRestitutionBeneficiairePrincipal) {
        this.soldePourRestitutionBeneficiairePrincipal = soldePourRestitutionBeneficiairePrincipal;
    }
}
