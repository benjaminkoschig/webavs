package ch.globaz.corvus.businessimpl.services.models.decisions;

import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.decisions.REDecisionJointOrdresVersements;
import globaz.corvus.db.decisions.REDecisionJointOrdresVersementsManager;
import globaz.corvus.db.ordresversements.RECompensationInterDecisions;
import globaz.corvus.db.ordresversements.REOrdresVersements;
import globaz.corvus.db.ordresversements.RESoldePourRestitution;
import globaz.corvus.db.ordresversements.RESoldePourRestitutionManager;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.hera.utils.SFFamilleUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.EntiteDeDomaine;
import ch.globaz.corvus.business.services.CorvusCrudServiceLocator;
import ch.globaz.corvus.business.services.models.decisions.DecisionService;
import ch.globaz.corvus.business.services.models.decisions.SoldePourRestitutionCrudService;
import ch.globaz.corvus.business.services.models.ordresversements.OrdresVersementCrudService;
import ch.globaz.corvus.domaine.CompensationInterDecision;
import ch.globaz.corvus.domaine.Decision;
import ch.globaz.corvus.domaine.DemandeRente;
import ch.globaz.corvus.domaine.OrdreVersement;
import ch.globaz.corvus.domaine.Prestation;
import ch.globaz.corvus.domaine.RenteAccordee;
import ch.globaz.corvus.domaine.RenteVerseeATort;
import ch.globaz.corvus.domaine.SoldePourRestitution;
import ch.globaz.corvus.domaine.constantes.TypeOrdreVersement;
import ch.globaz.corvus.domaine.constantes.TypeSoldePourRestitution;
import ch.globaz.osiris.domaine.CompteAnnexe;
import ch.globaz.pyxis.business.services.TiersCrudService;
import ch.globaz.pyxis.domaine.PersonneAVS;
import ch.globaz.pyxis.domaine.Tiers;

public class DecisionServiceImpl implements DecisionService {

    private final OrdresVersementCrudService ordresVersementCrudService;
    private final SoldePourRestitutionCrudService soldePourRestitutionCrudService;
    private final TiersCrudService tiersCrudService;

    public DecisionServiceImpl(final OrdresVersementCrudService ordresVersementCrudService,
            final SoldePourRestitutionCrudService soldePourRestitutionCrudService,
            final TiersCrudService tiersCrudService) {
        super();

        Checkers.checkNotNull(ordresVersementCrudService, "ordresVersementCrudService");
        Checkers.checkNotNull(soldePourRestitutionCrudService, "soldePourRestitutionCrudService");
        Checkers.checkNotNull(tiersCrudService, "tiersCrudService");

        this.ordresVersementCrudService = ordresVersementCrudService;
        this.soldePourRestitutionCrudService = soldePourRestitutionCrudService;
        this.tiersCrudService = tiersCrudService;
    }

    private BigDecimal calculerSoldeDecision(final List<REDecisionJointOrdresVersements> ordresVersementsDeLaDecision) {

        BigDecimal total = BigDecimal.ZERO;

        for (REDecisionJointOrdresVersements unOrdreVersement : ordresVersementsDeLaDecision) {
            if (unOrdreVersement.isCompense()) {
                if (isOrdreVersementCreance(unOrdreVersement.getTypeOrdreVersement())) {
                    total = total.subtract(unOrdreVersement.getMontantCompense());
                } else {
                    total = total.add(unOrdreVersement.getMontantCompense());
                }
            }
        }

        return total;
    }

    private Set<Long> extractIdTiers(final Set<PRTiersWrapper> tiers) {
        Set<Long> idTiers = new HashSet<Long>();
        for (PRTiersWrapper unTiers : tiers) {
            idTiers.add(Long.parseLong(unTiers.getIdTiers()));
        }
        return idTiers;
    }

    private RenteAccordee extraireRenteAccordeeDecision(final REDecisionJointOrdresVersements uneLigne) {
        RenteAccordee renteAccordee = new RenteAccordee();
        renteAccordee.setId(uneLigne.getIdPrestationAccordeeDecision());
        renteAccordee.setCodePrestation(uneLigne.getCodePrestationPrestationAccordeeDecision());
        renteAccordee.setEtat(uneLigne.getEtatPrestationAccordeeDecision());
        renteAccordee.setMoisDebut(uneLigne.getMoisDebutPrestationAccordeeDecision());
        renteAccordee.setMoisFin(uneLigne.getMoisFinPrestationAccordeeDecision());
        renteAccordee.setMontant(uneLigne.getMontantPrestationAccordeeDecision());
        renteAccordee.setReferencePourLePaiement(uneLigne.getReferencePourLePaiementPrestationAccordeeDecision());

        PersonneAVS beneficiaire = new PersonneAVS();
        beneficiaire.setId(uneLigne.getIdTiersBeneficiairePrestationAccordeeDecision());
        beneficiaire.setNom(uneLigne.getNomBeneficiairePrestationAccordeeDecision());
        beneficiaire.setPrenom(uneLigne.getPrenomBeneficiairePrestationAccordeeDecision());
        beneficiaire.setNss(uneLigne.getNssBeneficiairePrestationAccordeeDecision());

        if (uneLigne.getIdTiersAdressePaiementPrestationAccordeeDecision() != null) {
            Tiers tiersAdresseDePaiement = new Tiers();
            tiersAdresseDePaiement.setId(uneLigne.getIdTiersAdressePaiementPrestationAccordeeDecision());
            tiersAdresseDePaiement = tiersCrudService.read(tiersAdresseDePaiement);

            renteAccordee.setAdresseDePaiement(tiersAdresseDePaiement);
        }

        if (uneLigne.getIdCompteAnnexePrestationAccordeeDecision() != null) {
            CompteAnnexe compteAnnexe = new CompteAnnexe();
            compteAnnexe.setId(uneLigne.getIdCompteAnnexePrestationAccordeeDecision());

            renteAccordee.setCompteAnnexe(compteAnnexe);
        }

        renteAccordee.setBeneficiaire(beneficiaire);

        return renteAccordee;
    }

    private RenteAccordee extraireRenteAccordeeOrdreVersement(final REDecisionJointOrdresVersements uneLigne) {
        RenteAccordee renteAccordee = new RenteAccordee();
        renteAccordee.setId(uneLigne.getIdPrestationAccordeeOrdreVersement());
        renteAccordee.setCodePrestation(uneLigne.getCodePrestationPrestationAccordeeOrdreVersement());
        renteAccordee.setEtat(uneLigne.getEtatPrestationAccordeeOrdreVersement());
        renteAccordee.setMoisDebut(uneLigne.getMoisDebutPrestationAccordeeOrdreVersement());
        renteAccordee.setMoisFin(uneLigne.getMoisFinPrestationAccordeeOrdreVersement());
        renteAccordee.setMontant(uneLigne.getMontantPrestationAccordeeOrdreVersement());
        renteAccordee.setReferencePourLePaiement(uneLigne.getReferencePourLePaiementPrestationAccordeeOrdreVersement());

        PersonneAVS beneficiaire = new PersonneAVS();
        beneficiaire.setId(uneLigne.getIdTiersBeneficiairePrestationAccordeeOrdreVersement());
        beneficiaire.setNom(uneLigne.getNomBeneficiairePrestationAccordeeOrdreVersement());
        beneficiaire.setPrenom(uneLigne.getPrenomBeneficiairePrestationAccordeeOrdreVersement());
        beneficiaire.setNss(uneLigne.getNssBeneficiairePrestationAccordeeOrdreVersement());

        renteAccordee.setBeneficiaire(beneficiaire);

        return renteAccordee;
    }

    @Override
    public Decision getDecision(final Long idDecision) {
        Checkers.checkNotNull(idDecision, "idDecision");

        Decision result;

        try {
            REDecisionJointOrdresVersementsManager decisionManager = new REDecisionJointOrdresVersementsManager();
            decisionManager.setSession(getSession());
            decisionManager.setForIdDecision(idDecision);
            decisionManager.find(BManager.SIZE_NOLIMIT);

            Set<Decision> listeDecicions = transformToDomain(regrouperParIdDecision(decisionManager
                    .getContainerAsList()));

            if (listeDecicions.size() == 0) {
                throw new RETechnicalException("Entity not found (ID:" + idDecision + ")");
            }
            if (listeDecicions.size() > 1) {
                throw new RETechnicalException("too many entites for the same ID");
            }

            result = listeDecicions.iterator().next();

        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        }

        return result;
    }

    private Map<Long, List<REDecisionJointOrdresVersements>> getDecisionEtOvFamilleRegrouperParIdDecision(
            final Long idTiers, final Set<Long> idDecisionDuTiers) throws Exception {

        Set<PRTiersWrapper> tiersFamilleProche = SFFamilleUtils.getTiersFamilleProche(getSession(), idTiers.toString());

        REDecisionJointOrdresVersementsManager decisionsManager = new REDecisionJointOrdresVersementsManager();
        decisionsManager.setSession(getSession());
        decisionsManager.setForIdTiersBeneficiairePrincipalIn(extractIdTiers(tiersFamilleProche));
        decisionsManager.setForCsEtatDecisionIn(new HashSet<String>(Arrays.asList(IREDecision.CS_ETAT_ATTENTE,
                IREDecision.CS_ETAT_PREVALIDE)));
        decisionsManager.setForIdDecisionNotIn(new HashSet<Long>(idDecisionDuTiers));
        decisionsManager.find(BManager.SIZE_NOLIMIT);

        return regrouperParIdDecision(decisionsManager.getContainerAsList());
    }

    @Override
    public Decision getDecisionPourIdOrdreVersement(final Long idOrdreVersement) {
        Decision decision;
        try {
            REDecisionJointOrdresVersementsManager manager = new REDecisionJointOrdresVersementsManager();
            manager.setSession(getSession());
            manager.setForIdOrdreVersement(idOrdreVersement);
            manager.find(BManager.SIZE_NOLIMIT);

            if (manager.size() == 0) {
                throw new RETechnicalException("no entity was found");
            }

            decision = getDecision(((REDecisionJointOrdresVersements) (manager.get(0))).getIdDecision());
        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        }
        return decision;
    }

    @Override
    public Decision getDecisionPourIdRenteVerseeATort(final Long idRenteVerseeATort) {
        Decision decision;
        try {
            REDecisionJointOrdresVersementsManager manager = new REDecisionJointOrdresVersementsManager();
            manager.setSession(getSession());
            manager.setForIdRenteVerseeATort(idRenteVerseeATort);
            manager.find(BManager.SIZE_NOLIMIT);

            if (manager.size() == 0) {
                throw new RETechnicalException("no entity was found");
            }

            decision = getDecision(((REDecisionJointOrdresVersements) (manager.get(0))).getIdDecision());
        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        }
        return decision;
    }

    @Override
    public Set<Decision> getDecisionsAvecSoldePositifDeLaFamille(final Long idDecision) {

        Checkers.checkNotNull(idDecision, "idDecision");

        Set<Decision> result = new HashSet<Decision>();

        try {

            REDecisionEntity decision = new REDecisionEntity();
            decision.setSession(getSession());
            decision.setIdDecision(idDecision.toString());
            decision.retrieve();

            result = transformToDomain(getDecisionEtOvFamilleRegrouperParIdDecision(
                    Long.parseLong(decision.getIdTiersBeneficiairePrincipal()),
                    new HashSet<Long>(Arrays.asList(idDecision))));

        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        }

        return result;
    }

    @Override
    public SoldePourRestitution getSoldePourRestitutionDuBeneficiairePrincipal(final Decision decision) {

        SoldePourRestitution soldePourRestitution = null;

        try {
            RESoldePourRestitutionManager soldePourRestitutionManager = new RESoldePourRestitutionManager();
            soldePourRestitutionManager.setSession(getSession());
            soldePourRestitutionManager.setForIdPrestation(decision.getPrestation().getId().toString());
            soldePourRestitutionManager.setSansRestitutionPourDettEnCompta(true);
            soldePourRestitutionManager.find(BManager.SIZE_NOLIMIT);

            List<RESoldePourRestitution> listResultatManager = soldePourRestitutionManager.getContainerAsList();

            if (listResultatManager.size() == 1) {
                RESoldePourRestitution entity = listResultatManager.get(0);

                soldePourRestitution = new SoldePourRestitution();
                soldePourRestitution.setId(Long.parseLong(entity.getIdSoldePourRestitution()));
                soldePourRestitution = soldePourRestitutionCrudService.read(soldePourRestitution);
            } else if (listResultatManager.size() > 1) {
                throw new RETechnicalException("too many entites for this request");
            }
        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        }

        return soldePourRestitution;
    }

    @Override
    public boolean isCompensationInterDecisionPossible(final Long idDecision) {

        Checkers.checkNotNull(idDecision, "idDecision");

        try {

            boolean isCompensationInterDecisionPossible = false;

            REDecisionEntity decision = new REDecisionEntity();
            decision.setSession(getSession());
            decision.setIdDecision(idDecision.toString());
            decision.retrieve();

            Map<Long, List<REDecisionJointOrdresVersements>> decisionDeLaFamille = getDecisionEtOvFamilleRegrouperParIdDecision(
                    Long.parseLong(decision.getIdTiersBeneficiairePrincipal()),
                    new HashSet<Long>(Arrays.asList(idDecision)));

            Iterator<List<REDecisionJointOrdresVersements>> iterator = decisionDeLaFamille.values().iterator();
            while (!isCompensationInterDecisionPossible && iterator.hasNext()) {

                List<REDecisionJointOrdresVersements> ovPourUneDecision = iterator.next();

                BigDecimal soldeDecision = calculerSoldeDecision(ovPourUneDecision);
                if (soldeDecision.compareTo(BigDecimal.ZERO) > 0) {
                    isCompensationInterDecisionPossible = true;
                }
            }

            return isCompensationInterDecisionPossible;
        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        }
    }

    private BSession getSession() {
        return BSessionUtil.getSessionFromThreadContext();
    }

    private boolean isOrdreVersementCreance(final TypeOrdreVersement typeOrdreVersement) {
        switch (typeOrdreVersement) {
            case CREANCIER:
            case DETTE:
            case IMPOT_A_LA_SOURCE:
                return true;
            default:
                return false;
        }
    }

    @Override
    public void recalculerSoldePourRestitution(final Long idDecision) {

        try {
            Decision decision = getDecision(idDecision);

            SoldePourRestitution soldePourRestitution = getSoldePourRestitutionDuBeneficiairePrincipal(decision);

            if (decision.getSolde().compareTo(BigDecimal.ZERO) < 0) {
                if (soldePourRestitution != null) {
                    soldePourRestitution.setMontantRestitution(decision.getSolde().abs());
                    soldePourRestitutionCrudService.update(soldePourRestitution);
                } else {
                    soldePourRestitution = new SoldePourRestitution();
                    soldePourRestitution.setMontantRestitution(decision.getSolde().abs());

                    RenteAccordee renteAccordeePrincipale = decision.getRenteAccordeePrincipale();
                    OrdreVersement ordreVersementLiePrestationAccordeePrincipale = null;

                    // en cas de rétroactif pur, il n'y a pas de retenue sur paiement mensuel possible, on ne cherche
                    // donc pas à avoir une prestation accordée liée à la restitution
                    if (decision.isRetroPur()) {
                        ordreVersementLiePrestationAccordeePrincipale = decision
                                .getOrdresVersementPourType(TypeOrdreVersement.BENEFICIAIRE_PRINCIPAL).iterator()
                                .next();
                        if (ordreVersementLiePrestationAccordeePrincipale == null) {
                            ordreVersementLiePrestationAccordeePrincipale = decision
                                    .getOrdresVersementPourType(TypeOrdreVersement.CREANCIER,
                                            TypeOrdreVersement.DETTE_RENTE_AVANCES,
                                            TypeOrdreVersement.DETTE_RENTE_DECISION,
                                            TypeOrdreVersement.DETTE_RENTE_PRST_BLOQUE,
                                            TypeOrdreVersement.DETTE_RENTE_RESTITUTION,
                                            TypeOrdreVersement.DETTE_RENTE_RETOUR).iterator().next();
                        }
                    } else {

                        // si l'ordre de versement est toujours null, cela signifie que le solde est négatif non pas à
                        // cause
                        // de rentes versées à tort mais à cause de créanciers / dettes en compta. On prend donc le 1er
                        // ordre de versement de ce type pour le lier au solde pour restitution

                        Set<OrdreVersement> ovs = decision.getOrdresVersementPourType(TypeOrdreVersement.CREANCIER,
                                TypeOrdreVersement.DETTE_RENTE_AVANCES, TypeOrdreVersement.DETTE_RENTE_DECISION,
                                TypeOrdreVersement.DETTE_RENTE_PRST_BLOQUE, TypeOrdreVersement.DETTE_RENTE_RESTITUTION,
                                TypeOrdreVersement.DETTE_RENTE_RETOUR);

                        if (ovs.size() > 0) {
                            ordreVersementLiePrestationAccordeePrincipale = ovs.iterator().next();
                        }

                        if (ordreVersementLiePrestationAccordeePrincipale == null) {
                            ordreVersementLiePrestationAccordeePrincipale = decision
                                    .getOrdreVersementPourRenteAccordeeNouveauDroit(renteAccordeePrincipale);
                        }
                    }

                    soldePourRestitution.setOrdreVersement(ordreVersementLiePrestationAccordeePrincipale);

                    // si rétroactif pur on ne fait pas de retenue
                    if (decision.isRetroPur()) {
                        soldePourRestitution.setType(TypeSoldePourRestitution.RESTITUTION);
                    } else {
                        soldePourRestitution.setType(TypeSoldePourRestitution.RETENUES);
                        // si le montant total de la retenue est plus petit que le montant mensuel de la prestation, on
                        // met le montant de la retenue comme montant mensuel
                        if (soldePourRestitution.getMontantRestitution()
                                .compareTo(renteAccordeePrincipale.getMontant()) < 0) {
                            soldePourRestitution.setMontantRetenueMensuelle(soldePourRestitution
                                    .getMontantRestitution());
                        } else {
                            soldePourRestitution.setMontantRetenueMensuelle(renteAccordeePrincipale.getMontant());
                        }
                    }

                    soldePourRestitution = soldePourRestitutionCrudService.create(soldePourRestitution);
                }
            } else if (soldePourRestitution != null) {
                // si solde positif, et qu'il y a un solde pour restitution, on le supprime
                soldePourRestitutionCrudService.delete(soldePourRestitution);
            }
        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        }
    }

    private Map<Long, List<REDecisionJointOrdresVersements>> regrouperParIdDecision(
            final List<REDecisionJointOrdresVersements> resultSet) {

        Map<Long, List<REDecisionJointOrdresVersements>> sortedResultSet = new HashMap<Long, List<REDecisionJointOrdresVersements>>();

        for (REDecisionJointOrdresVersements unOrdreVersement : resultSet) {
            if (sortedResultSet.containsKey(unOrdreVersement.getIdDecision())) {
                sortedResultSet.get(unOrdreVersement.getIdDecision()).add(unOrdreVersement);
            } else {
                List<REDecisionJointOrdresVersements> ovPourCetteDecision = new ArrayList<REDecisionJointOrdresVersements>();
                ovPourCetteDecision.add(unOrdreVersement);
                sortedResultSet.put(unOrdreVersement.getIdDecision(), ovPourCetteDecision);
            }
        }

        return sortedResultSet;
    }

    private Set<Decision> transformToDomain(final Map<Long, List<REDecisionJointOrdresVersements>> sortedResultSet)
            throws JadeApplicationServiceNotAvailableException {

        Set<Decision> decisions = new HashSet<Decision>();

        for (Entry<Long, List<REDecisionJointOrdresVersements>> uneEntree : sortedResultSet.entrySet()) {

            Decision uneDecision = new Decision();
            uneDecision.setId(uneEntree.getKey());

            Prestation prestation = new Prestation();
            Set<OrdreVersement> ordresVersement = new HashSet<OrdreVersement>();
            Set<RenteAccordee> rentesAccordees = new HashSet<RenteAccordee>();
            PersonneAVS beneficiairePrincipalDecision = new PersonneAVS();
            Set<CompensationInterDecision> cids = new HashSet<CompensationInterDecision>();
            DemandeRente demande = null;

            for (Iterator<REDecisionJointOrdresVersements> iterator = uneEntree.getValue().iterator(); iterator
                    .hasNext();) {
                REDecisionJointOrdresVersements uneLigne = iterator.next();

                OrdreVersement unOrdreVersement = new OrdreVersement();
                unOrdreVersement.setId(uneLigne.getIdOrdreVersement());

                prestation.setId(uneLigne.getIdPrestation());
                prestation.setMontant(uneLigne.getMontantPrestation());

                if (ordresVersement.contains(unOrdreVersement)) {
                    // ordre de versement déjà présent -> c'est donc une ligne pour un CID dans le result set
                    if (uneLigne.getIdCIDCompensant() != null) {
                        CompensationInterDecision cid = new CompensationInterDecision();
                        cid.setId(uneLigne.getIdCIDCompensant());
                        cid.setMontantCompense(uneLigne.getMontantCompenseCID());
                        cids.add(cid);
                    }
                    if (uneLigne.getIdCIDPonctionant() != null) {
                        CompensationInterDecision cid = new CompensationInterDecision();
                        cid.setId(uneLigne.getIdCIDPonctionant());
                        cid.setMontantCompense(uneLigne.getMontantPonctionneCID());
                        cids.add(cid);
                    }
                    RenteAccordee uneRenteAccordeeDecision = extraireRenteAccordeeDecision(uneLigne);
                    rentesAccordees.add(uneRenteAccordeeDecision);
                } else {

                    unOrdreVersement.setCompensationInterDecision(uneLigne.isCompensationInterDecision());
                    unOrdreVersement.setCompense(uneLigne.isCompense());
                    unOrdreVersement.setMontantCompense(uneLigne.getMontantCompense());
                    unOrdreVersement.setMontantDette(uneLigne.getMontantDette());
                    unOrdreVersement.setPrestation(prestation);
                    unOrdreVersement.setType(uneLigne.getTypeOrdreVersement());

                    if (uneLigne.getIdPrestationAccordeeOrdreVersement() != null) {
                        RenteAccordee uneRenteAccordeeOrdreVersement = extraireRenteAccordeeOrdreVersement(uneLigne);
                        unOrdreVersement.setRenteAccordeeNouveauDroit(uneRenteAccordeeOrdreVersement);
                    }

                    if (uneLigne.getIdRenteVerseeATort() != null) {
                        RenteVerseeATort renteVerseeATort = new RenteVerseeATort();
                        renteVerseeATort.setId(uneLigne.getIdRenteVerseeATort());
                        renteVerseeATort.setType(uneLigne.getTypeRenteVerseeATort());

                        unOrdreVersement.setRenteVerseeATort(renteVerseeATort);
                    }

                    if (uneLigne.getIdRenteVerseeATort() != null) {
                        RenteVerseeATort renteVerseeATort = new RenteVerseeATort();
                        renteVerseeATort.setId(uneLigne.getIdRenteVerseeATort());
                        renteVerseeATort.setType(uneLigne.getTypeRenteVerseeATort());

                        unOrdreVersement.setRenteVerseeATort(renteVerseeATort);
                    }

                    ordresVersement.add(unOrdreVersement);

                    RenteAccordee unePrestationAccordeeDecision = extraireRenteAccordeeDecision(uneLigne);
                    rentesAccordees.add(unePrestationAccordeeDecision);

                    if (uneLigne.getIdCIDCompensant() != null) {
                        CompensationInterDecision cid = new CompensationInterDecision();
                        cid.setId(uneLigne.getIdCIDCompensant());
                        cid.setMontantCompense(uneLigne.getMontantCompenseCID());
                        cid.setOrdreVersementDecisionPonctionnee(unOrdreVersement);

                        OrdreVersement autreOrdreVersementDuCID = new OrdreVersement();
                        autreOrdreVersementDuCID.setId(uneLigne.getIdAutreOrdreVersementCIDCompensant());
                        autreOrdreVersementDuCID = ordresVersementCrudService.read(autreOrdreVersementDuCID);
                        cid.setOrdreVersementDecisionCompensee(autreOrdreVersementDuCID);

                        cids.add(cid);
                    }
                    if (uneLigne.getIdCIDPonctionant() != null) {
                        CompensationInterDecision cid = new CompensationInterDecision();
                        cid.setId(uneLigne.getIdCIDPonctionant());
                        cid.setMontantCompense(uneLigne.getMontantPonctionneCID());
                        cid.setOrdreVersementDecisionCompensee(unOrdreVersement);

                        OrdreVersement autreOrdreVersementDuCID = new OrdreVersement();
                        autreOrdreVersementDuCID.setId(uneLigne.getIdAutreOrdreVersementCIDPonctionnant());
                        autreOrdreVersementDuCID = ordresVersementCrudService.read(autreOrdreVersementDuCID);
                        cid.setOrdreVersementDecisionPonctionnee(autreOrdreVersementDuCID);

                        cids.add(cid);
                    }
                }

                if (!iterator.hasNext()) {

                    beneficiairePrincipalDecision.setId(uneLigne.getIdTiersBeneficiairePrincipalDecision());
                    beneficiairePrincipalDecision.setNom(uneLigne.getNomBeneficiairePrincipalDecision());
                    beneficiairePrincipalDecision.setPrenom(uneLigne.getPrenomBeneficiairePrincipalDecision());
                    beneficiairePrincipalDecision.setNss(uneLigne.getNssBeneficiairePrincipalDecision());

                    uneDecision.setAdresseEmail(uneLigne.getAdresseEmailDecision());
                    uneDecision.setAfficherTexteAnnulerDecision(uneLigne.isAfficherTexteAnnulerDecision());
                    uneDecision.setAfficherTexteAvecBonneFoi(uneLigne.isAfficherTexteAvecBonneFoi());
                    uneDecision.setAfficherTexteIncarceration(uneLigne.isAfficherTexteAvecIncarceration());
                    uneDecision.setAfficherTexteInteretMoratoires(uneLigne.isAfficherTexteInteretMoratoire());
                    uneDecision.setAfficherTexteObligationPayerCotisation(uneLigne
                            .isAfficherTexteObligationPayerCotisation());
                    uneDecision.setAfficherTexteReductionPourPlafonnement(uneLigne
                            .isAfficherTexteReductionPourPlafonnement());
                    uneDecision.setAfficherTexteRemariageRenteDeSurvivant(uneLigne
                            .isAfficherTexteRemariageRenteDeSurvivant());
                    uneDecision.setAfficherTexteRenteAvecDebutDroit5AnsAvantDepotDemande(uneLigne
                            .isAfficherTexteRenteAvecDebutDroit5AnsAvantDepotDemande());
                    uneDecision.setAfficherTexteRenteAvecMontantMinimumMajoreInvalidite(uneLigne
                            .isAfficherTexteRenteAvecMontantMinimumMajoreInvalidite());
                    uneDecision.setAfficherTexteRenteDeVeufLimitee(uneLigne.isAfficherTexteRenteDeVeufLimitee());
                    uneDecision.setAfficherTexteRenteDeVeuveLimitee(uneLigne.isAfficherTexteRenteDeVeuveLimitee());
                    uneDecision.setAfficherTexteRentePourEnfant(uneLigne.isAfficherTexteRentePourEnfant());
                    uneDecision.setAfficherTexteRenteReduitePourSurassurance(uneLigne
                            .isAfficherTexteRenteReduitePourSurassurance());
                    uneDecision.setAfficherTexteSansBonneFoi(uneLigne.isAfficherTexteSansBonneFoi());
                    uneDecision.setAfficherTexteSupplementVeuve(uneLigne.isAfficherTexteSupplementVeuve());
                    uneDecision.setDateDecision(uneLigne.getDateDecision());
                    uneDecision.setDatePreparation(uneLigne.getDatePreparationDecision());
                    uneDecision.setDateValidation(uneLigne.getDateValidationDecision());
                    uneDecision.setEtat(uneLigne.getEtatDecision());
                    uneDecision.setGestionnairePreparation(uneLigne.getGestionnairePreprationDecision());
                    uneDecision.setGestionnaireTraitement(uneLigne.getGestionnaireTraitementDecision());
                    uneDecision.setGestionnaireValidation(uneLigne.getGestionnaireValidationDecision());
                    uneDecision.setMoisDebutRetro(uneLigne.getMoisDebutRetroDecision());
                    uneDecision.setMoisDecisionDepuis(uneLigne.getMoisDecisionDepuis());
                    uneDecision.setMoisFinRetro(uneLigne.getMoisFinRetroDecision());
                    uneDecision.setRemarque(uneLigne.getRemarqueDecision());
                    uneDecision.setTypeDecision(uneLigne.getTypeDecision());
                    uneDecision.setTypeTraitement(uneLigne.getTypeTraitementDecision());

                    demande = CorvusCrudServiceLocator.getDemandeRenteCrudService().read(uneLigne.getIdDemandeRente());
                }
            }

            prestation.setOrdresVersement(ordresVersement);
            uneDecision.setPrestation(prestation);
            uneDecision.setBeneficiairePrincipal(beneficiairePrincipalDecision);
            uneDecision.setRentesAccordees(rentesAccordees);
            uneDecision.setCompensationsInterDecision(cids);
            uneDecision.setDemande(demande);

            try {
                controlerLienCID(uneDecision);
            } catch (Exception ex) {
                throw new RETechnicalException(ex);
            }
            decisions.add(uneDecision);
        }

        return decisions;
    }

    private void controlerLienCID(Decision decision) throws Exception {
        Set<CompensationInterDecision> cids = decision.getCompensationsInterDecision();
        for (CompensationInterDecision cid : cids) {
            if (cid.getOrdreVersementDecisionCompensee() == null
                    || (EntiteDeDomaine.ID_INVALIDE == cid.getOrdreVersementDecisionCompensee().getId())) {
                miseAJourCIDCompensee(cid);
            }
            if (cid.getOrdreVersementDecisionPonctionnee() == null
                    || (EntiteDeDomaine.ID_INVALIDE == cid.getOrdreVersementDecisionPonctionnee().getId())) {
                miseAJourCIDPonctionne(cid);
            }
        }
    }

    private void miseAJourCIDCompensee(CompensationInterDecision cid) throws Exception {
        RECompensationInterDecisions recid = new RECompensationInterDecisions();
        recid.setSession(getSession());
        recid.setIdCompensationInterDecision(String.valueOf(cid.getId()));
        recid.retrieve();

        REOrdresVersements reov = new REOrdresVersements();
        reov.setSession(getSession());
        reov.setIdOrdreVersement(String.valueOf(recid.getIdOrdreVersement()));
        reov.retrieve();

        cid.getOrdreVersementDecisionCompensee().setId(Long.valueOf(reov.getIdOrdreVersement()));
        cid.getOrdreVersementDecisionCompensee().setCompensationInterDecision(reov.getIsCompensationInterDecision());
        cid.getOrdreVersementDecisionCompensee().setCompense(reov.getIsCompense());
        cid.getOrdreVersementDecisionCompensee().setMontantCompense(
                JadeStringUtil.isBlank(reov.getMontant()) ? new BigDecimal(0) : new BigDecimal(reov.getMontant()));
        cid.getOrdreVersementDecisionCompensee().setMontantDette(
                JadeStringUtil.isBlank(reov.getMontantDette()) ? new BigDecimal(0) : new BigDecimal(reov
                        .getMontantDette()));
    }

    private void miseAJourCIDPonctionne(CompensationInterDecision cid) throws Exception {
        RECompensationInterDecisions recid = new RECompensationInterDecisions();
        recid.setSession(getSession());
        recid.setIdCompensationInterDecision(String.valueOf(cid.getId()));
        recid.retrieve();

        REOrdresVersements reov = new REOrdresVersements();
        reov.setSession(getSession());
        reov.setIdOrdreVersement(String.valueOf(recid.getIdOVCompensation()));
        reov.retrieve();

        cid.getOrdreVersementDecisionPonctionnee().setId(Long.valueOf(reov.getIdOrdreVersement()));
        cid.getOrdreVersementDecisionPonctionnee().setCompensationInterDecision(reov.getIsCompensationInterDecision());
        cid.getOrdreVersementDecisionPonctionnee().setCompense(reov.getIsCompense());
        cid.getOrdreVersementDecisionPonctionnee().setMontantCompense(
                JadeStringUtil.isBlank(reov.getMontant()) ? new BigDecimal(0) : new BigDecimal(reov.getMontant()));
        cid.getOrdreVersementDecisionPonctionnee().setMontantDette(
                JadeStringUtil.isBlank(reov.getMontantDette()) ? new BigDecimal(0) : new BigDecimal(reov
                        .getMontantDette()));
    }
}
