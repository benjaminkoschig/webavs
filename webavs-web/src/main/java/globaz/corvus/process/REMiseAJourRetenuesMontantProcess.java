package globaz.corvus.process;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.retenues.RERetenuesJointPrestationAccordee;
import globaz.corvus.db.retenues.RERetenuesJointPrestationAccordeeManager;
import globaz.corvus.db.retenues.RERetenuesPaiement;
import globaz.corvus.process.exception.REProcessChargementJointRenteAccordeeException;
import globaz.corvus.utils.REPmtMensuel;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CASection;
import globaz.prestation.tools.PRDateFormater;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.constantes.IPCRetenues;

class REMiseAJourRetenuesMontantProcess {
    private BSession session;
    private BITransaction transaction;
    private String dateProchainPaiement_MMxAAAA;
    private String dateDerniernPaiement_MMxAAAA;

    public REMiseAJourRetenuesMontantProcess(BSession session, BITransaction transaction) {
        this.session = session;
        this.transaction = transaction;
        dateProchainPaiement_MMxAAAA = REPmtMensuel.getDateProchainPmt(session);
        dateDerniernPaiement_MMxAAAA = REPmtMensuel.getDateDernierPmt(session);
    }

    public List<RetenueMontantTotalCorrige> verifRetenueAndChangeSolde() throws Exception {
        List<RERetenuesJointPrestationAccordee> list = loadRetnuesFactrueExistante();
        List<String> idsTiers = new ArrayList<String>();
        for (RERetenuesJointPrestationAccordee retenue : list) {
            idsTiers.add(retenue.getIdTiersBeneficiaireRA());
        }
        List<RetenueMontantTotalCorrige> rentenuesModifiees = new ArrayList<RetenueMontantTotalCorrige>();
        List<CACompteAnnexe> comptesAnnexes = loadCompteAnnexe(idsTiers);
        Map<String, List<CACompteAnnexe>> map = groupCompteAnnexeByIdTiers(comptesAnnexes);
        for (RERetenuesJointPrestationAccordee retenue : list) {
            List<CACompteAnnexe> comptesAnnexesForTier = map.get(retenue.getIdTiersBeneficiaireRA());
            if (comptesAnnexesForTier != null) {
                for (CACompteAnnexe caCompteAnnexe : comptesAnnexesForTier) {
                    if (retenue.getRole().equals(caCompteAnnexe.getIdRole())) {
                        CASection sec = new CASection();
                        sec.setSession(session);
                        sec.setAlternateKey(CASection.AK_IDEXTERNE);
                        sec.setIdCompteAnnexe(caCompteAnnexe.getIdCompteAnnexe());
                        sec.setIdTypeSection(retenue.getIdTypeSection());
                        sec.setIdExterne(retenue.getNoFacture());
                        sec.retrieve(transaction);

                        if (!sec.isNew()) {
                            Montant montantRetenue = new Montant(retenue.getMontantRetenuMensuel());
                            Montant solde = new Montant(sec.getSolde());
                            Montant totalRetenue = new Montant(retenue.getMontantDejaRetenu());

                            if (!montantRetenue.isZero() && solde.isPositive()) {
                                if (solde.substract(montantRetenue).isNegative()
                                        || solde.substract(montantRetenue).isZero()) {
                                    Montant totalRetenueAModfifier = totalRetenue.add(solde);

                                    if (!totalRetenueAModfifier.equals(new Montant(retenue.getMontantTotalARetenir()))) {
                                        RetenueMontantTotalCorrige retenueModifie = new RetenueMontantTotalCorrige();
                                        retenueModifie.setMontantTotalARetenir(retenue.getMontantTotalARetenir());
                                        retenueModifie.setIdExterneRole(caCompteAnnexe.getIdExterneRole());
                                        retenueModifie.setSection(sec.getIdExterne());
                                        retenueModifie.setIdCompteAnnexe(caCompteAnnexe.getIdCompteAnnexe());
                                        retenueModifie.setIdTiers(caCompteAnnexe.getIdTiers());
                                        retenueModifie.setDesignation(caCompteAnnexe.getDescription());
                                        retenueModifie.setNss(retenue.getNss());
                                        RERetenuesPaiement reRetenuesPaiement = new RERetenuesPaiement();
                                        reRetenuesPaiement.setSession(session);
                                        reRetenuesPaiement.setId(retenue.getId());
                                        reRetenuesPaiement.retrieve();
                                        reRetenuesPaiement.setMontantTotalARetenir(totalRetenue.add(solde)
                                                .toStringFormat());
                                        retenueModifie.setMontantTotaleAretenirCorriger(reRetenuesPaiement
                                                .getMontantTotalARetenir());
                                        rentenuesModifiees.add(retenueModifie);
                                        reRetenuesPaiement.update(transaction);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return rentenuesModifiees;
    }

    private List<RERetenuesJointPrestationAccordee> loadRetnuesFactrueExistante()
            throws REProcessChargementJointRenteAccordeeException {
        List<RERetenuesJointPrestationAccordee> list = new ArrayList<RERetenuesJointPrestationAccordee>();
        try {
            RERetenuesJointPrestationAccordeeManager manager;
            manager = new RERetenuesJointPrestationAccordeeManager();
            manager.changeManagerSize(BManager.SIZE_NOLIMIT);
            manager.setSession(session);
            manager.setForRenteEnCoursAtDate(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateProchainPaiement_MMxAAAA));
            manager.addCsEtatRenteList(IREPrestationAccordee.CS_ETAT_VALIDE);
            manager.addCsEtatRenteList(IREPrestationAccordee.CS_ETAT_PARTIEL);
            manager.addCsEtatRenteList(IREPrestationAccordee.CS_ETAT_DIMINUE);
            manager.setForEnCoursAtDate(dateProchainPaiement_MMxAAAA);

            List<String> forCsEtatRente = new ArrayList<String>();
            forCsEtatRente.add(IPCRetenues.CS_FACTURE_EXISTANTE);
            manager.setForTypesRetenues(forCsEtatRente);
            manager.setOrderBy(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
            manager.find(transaction);
            for (int i = 0; i < manager.size(); i++) {
                list.add((RERetenuesJointPrestationAccordee) manager.getEntity(i));
            }
        } catch (Exception e) {
            throw new REProcessChargementJointRenteAccordeeException(
                    this.getClass().getName().concat("chargeDonnees("), e);
        }
        return list;
    }

    private List<CACompteAnnexe> loadCompteAnnexe(List<String> idsTiers) throws Exception {
        List<CACompteAnnexe> list = new ArrayList<CACompteAnnexe>();
        CACompteAnnexeManager manager = new CACompteAnnexeManager();
        manager.changeManagerSize(BManager.SIZE_NOLIMIT);
        manager.setForIdTiersIn(idsTiers);
        manager.setSession(session);
        manager.find(transaction);
        for (int i = 0; i < manager.size(); i++) {
            list.add((CACompteAnnexe) manager.getEntity(i));
        }
        return list;
    }

    private Map<String, List<CACompteAnnexe>> groupCompteAnnexeByIdTiers(List<CACompteAnnexe> comptesAnnexes) {
        Map<String, List<CACompteAnnexe>> map = new HashMap<String, List<CACompteAnnexe>>();
        for (CACompteAnnexe caCompteAnnexe : comptesAnnexes) {
            if (!map.containsKey(caCompteAnnexe.getIdTiers())) {
                map.put(caCompteAnnexe.getIdTiers(), new ArrayList<CACompteAnnexe>());
            }
            map.get(caCompteAnnexe.getIdTiers()).add(caCompteAnnexe);
        }
        return map;
    }

}
