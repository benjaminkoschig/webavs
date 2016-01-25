package ch.globaz.corvus.businessimpl.services.models.rentesaccordees;

import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeePourServiceEntity;
import globaz.corvus.db.rentesaccordees.RERenteAccordeePourServiceManager;
import globaz.corvus.exceptions.REBusinessException;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.globall.db.BManager;
import globaz.globall.db.BSessionUtil;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.Periode;
import ch.globaz.corvus.business.services.models.rentesaccordees.RenteAccordeeCrudService;
import ch.globaz.corvus.businessimpl.services.models.CanevasCrudService;
import ch.globaz.corvus.domaine.Creance;
import ch.globaz.corvus.domaine.InteretMoratoire;
import ch.globaz.corvus.domaine.PrestationDue;
import ch.globaz.corvus.domaine.RenteAccordee;
import ch.globaz.corvus.domaine.RepartitionCreance;
import ch.globaz.jade.JadeBusinessServiceLocator;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;
import ch.globaz.osiris.domaine.CompteAnnexe;
import ch.globaz.prestation.domaine.EnTeteBlocage;
import ch.globaz.pyxis.domaine.Pays;
import ch.globaz.pyxis.domaine.PersonneAVS;
import ch.globaz.pyxis.domaine.Tiers;

public class RenteAccordeeCrudServiceJadeImpl extends CanevasCrudService<RenteAccordee> implements
        RenteAccordeeCrudService {

    @Override
    public RenteAccordee create(final RenteAccordee objetDeDomaine) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public boolean delete(final Long id) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public RenteAccordee read(final Long id) {
        RenteAccordee objetDeDomaine = new RenteAccordee();
        objetDeDomaine.setId(id);

        try {

            RERenteAccordeePourServiceManager manager = new RERenteAccordeePourServiceManager();
            manager.setSession(BSessionUtil.getSessionFromThreadContext());
            manager.setForIdRenteAccordee(id);
            manager.find(BManager.SIZE_NOLIMIT);

            if (manager.size() == 0) {
                throw new RETechnicalException("Entity not found : " + objetDeDomaine.toString());
            }

            Set<RepartitionCreance> repartitionsCreance = new HashSet<RepartitionCreance>();
            Set<PrestationDue> prestationsDues = new HashSet<PrestationDue>();
            EnTeteBlocage enTeteBlocage = null;

            boolean premierPassage = true;

            for (RERenteAccordeePourServiceEntity uneLigne : manager.getContainerAsList()) {

                if (uneLigne.getIdRepartitionCreance() != null) {
                    RepartitionCreance uneRepartitionCreance = new RepartitionCreance();
                    uneRepartitionCreance.setId(uneLigne.getIdRepartitionCreance());
                    uneRepartitionCreance.setMontantReparti(uneLigne.getMontantRepartitionCreance());

                    Creance creance = new Creance();
                    creance.setId(uneLigne.getIdCreance());
                    creance.setMontant(uneLigne.getMontantCreance());
                    if (uneLigne.getIdTiersCreancier() != null) {
                        Tiers creancier = new Tiers();
                        creancier.setId(uneLigne.getIdTiersCreancier());
                        creancier.setDesignation1(uneLigne.getNomTiersCreancier());
                        creancier.setDesignation2(uneLigne.getPrenomTiersCreancier());
                        if (uneLigne.getIdPaysTiersCreancier() != null) {
                            Pays paysCreancier = new Pays();
                            paysCreancier.setId(uneLigne.getIdPaysTiersCreancier());
                            paysCreancier.setCodeIso(uneLigne.getCodeIsoPaysTiersCreancier());
                            paysCreancier.setTraductionParLangue(uneLigne.getTraductionPaysCreancierParLangue());
                            creancier.setPays(paysCreancier);
                        }
                        creance.setCreancier(creancier);
                    }
                    uneRepartitionCreance.setCreance(creance);
                    repartitionsCreance.add(uneRepartitionCreance);
                }

                if (uneLigne.getIdPrestationDue() != null) {
                    PrestationDue prestationDue = new PrestationDue();
                    prestationDue.setId(uneLigne.getIdPrestationDue());
                    prestationDue.setMontant(uneLigne.getMontantPrestationDue());
                    prestationDue.setPeriode(new Periode(uneLigne.getMoisDebutPrestationDue(), uneLigne
                            .getMoisFinPrestationDue()));
                    prestationDue.setType(uneLigne.getTypePrestationDue());

                    prestationsDues.add(prestationDue);
                }

                if (uneLigne.getIdEnTeteBlocage() != null) {
                    if (enTeteBlocage == null) {
                        enTeteBlocage = new EnTeteBlocage();
                        enTeteBlocage.setId(uneLigne.getIdEnTeteBlocage());
                        enTeteBlocage.setMontantDebloque(uneLigne.getMontantDebloque());
                    }

                    if ((uneLigne.getIdBlocage() != null)
                            && !enTeteBlocage.contientUnBlocagePourLeMois(uneLigne.getMoisBlocage())) {
                        enTeteBlocage.ajouterUnBlocagePourLeMois(uneLigne.getMoisBlocage(),
                                uneLigne.getMontantBlocage());
                    }
                }

                if (premierPassage) {
                    objetDeDomaine.setCodePrestation(uneLigne.getCodePrestation());
                    objetDeDomaine.setCodesCasSpeciaux(uneLigne.getCodesCasSpeciaux());
                    objetDeDomaine.setEtat(uneLigne.getEtatPrestationAccordee());
                    objetDeDomaine.setBloquee(uneLigne.laPrestationEstBloquee());
                    objetDeDomaine.setMoisDebut(uneLigne.getMoisDebutDroitPrestationAccordee());
                    objetDeDomaine.setMoisFin(uneLigne.getMoisFinDroitPrestationAccordee());
                    objetDeDomaine.setMontant(uneLigne.getMontantPrestationAccordee());
                    objetDeDomaine.setReferencePourLePaiement(uneLigne.getReferencePourLePaiementPrestationAccordee());

                    Tiers adresseDePaiement = new Tiers();
                    adresseDePaiement.setId(uneLigne.getIdTiersAdresseDePaiement());
                    objetDeDomaine.setAdresseDePaiement(adresseDePaiement);

                    PersonneAVS beneficiaire = new PersonneAVS();
                    beneficiaire.setId(uneLigne.getIdTiersBeneficiaire());
                    beneficiaire.setNss(uneLigne.getNssTiersBeneficiaire());
                    beneficiaire.setNom(uneLigne.getNomTiersBeneficiaire());
                    beneficiaire.setPrenom(uneLigne.getPrenomTiersBeneficiaire());
                    beneficiaire.setSexe(uneLigne.getSexeTiersBeneficiarie());
                    beneficiaire.setDateNaissance(uneLigne.getDateDecesTiersBeneficiaire());
                    beneficiaire.setDateDeces(uneLigne.getDateDecesTiersBeneficiaire());

                    if (uneLigne.getCsTitreTiersBeneficiaire() != null && uneLigne.getCsTitreTiersBeneficiaire() != 0) {
                        JadeCodeSysteme csTitreTiers = JadeBusinessServiceLocator.getCodeSystemeService()
                                .getCodeSysteme(uneLigne.getCsTitreTiersBeneficiaire().toString());
                        Map<Langues, String> titreTiers = new HashMap<Langues, String>();
                        for (Langues uneLangue : Langues.values()) {
                            if (csTitreTiers.getTraduction(uneLangue) != null) {
                                titreTiers.put(uneLangue, csTitreTiers.getTraduction(uneLangue));
                            }
                        }
                        beneficiaire.setTitreParLangue(titreTiers);
                    } else {
                        throw new REBusinessException(BSessionUtil.getSessionFromThreadContext().getLabel(
                                "ERREUR_TITRE_MANQUANT_POUR_TIERS")
                                + " : " + beneficiaire);
                    }

                    Pays paysBeneficiaire = new Pays();
                    paysBeneficiaire.setId(uneLigne.getIdPaysTiersBeneficiaire());
                    paysBeneficiaire.setCodeIso(uneLigne.getCodeIsoPaysTiersBeneficiaire());
                    paysBeneficiaire.setTraductionParLangue(uneLigne.getTraductionPaysBeneficiaireParLangue());
                    beneficiaire.setPays(paysBeneficiaire);

                    objetDeDomaine.setBeneficiaire(beneficiaire);

                    if (uneLigne.getIdCompteAnnexePrestationAccordee() != null) {
                        CompteAnnexe compteAnnexe = new CompteAnnexe();
                        compteAnnexe.setId(uneLigne.getIdCompteAnnexePrestationAccordee());
                        objetDeDomaine.setCompteAnnexe(compteAnnexe);
                    }

                    if (uneLigne.getIdInteretMoratoire() != null) {
                        InteretMoratoire interetMoratoire = new InteretMoratoire();
                        interetMoratoire.setId(uneLigne.getIdInteretMoratoire());
                        objetDeDomaine.setInteretMoratoire(interetMoratoire);
                    }

                    premierPassage = false;
                }
            }

            objetDeDomaine.setRepartitionCreance(repartitionsCreance);
            objetDeDomaine.setPrestationsDues(prestationsDues);
            if (enTeteBlocage != null) {
                objetDeDomaine.setEnTeteBlocage(enTeteBlocage);
            }

        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        }
        return objetDeDomaine;
    }

    @Override
    public RenteAccordee update(final RenteAccordee objetDeDomaine) {
        Checkers.checkNotNull(objetDeDomaine, "objetDeDomaine");
        try {
            RERenteAccordee renteAccordee = new RERenteAccordee();
            renteAccordee.setSession(BSessionUtil.getSessionFromThreadContext());
            renteAccordee.setIdPrestationAccordee(objetDeDomaine.getId().toString());
            renteAccordee.retrieve();

            if (renteAccordee.isNew()) {
                throw new IllegalArgumentException("entity not found : " + objetDeDomaine.toString());
            }

            renteAccordee.setCsEtat(objetDeDomaine.getEtat().getCodeSysteme().toString());
            renteAccordee.update();

        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        }
        return objetDeDomaine;
    }
}
