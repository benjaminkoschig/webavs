package ch.globaz.corvus.businessimpl.services.models.rentesaccordees;

import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeFamille;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeFamilleManager;
import globaz.corvus.db.rentesaccordees.RERenteCalculee;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.corvus.business.services.CorvusCrudServiceLocator;
import ch.globaz.corvus.business.services.models.rentesaccordees.RenteAccordeeCrudService;
import ch.globaz.corvus.business.services.models.rentesaccordees.RenteAccordeeService;
import ch.globaz.corvus.domaine.DemandeRente;
import ch.globaz.corvus.domaine.RenteAccordee;
import ch.globaz.pyxis.domaine.PersonneAVS;

/**
 * Implementation des méthodes utilitaires concernant les prestations accordées
 */
public class RenteAccordeeServiceImpl implements RenteAccordeeService {

    @Override
    public void rattacherLaRenteSurLaDemande(final RenteAccordee rente, final DemandeRente demande) {

        Checkers.checkNotNull(demande, "demande");
        Checkers.checkHasID(demande, "demande");

        Checkers.checkNotNull(rente, "rente");
        Checkers.checkHasID(rente, "rente");

        Checkers.checkHasID(rente.getBaseCalcul(), "rente.baseCalcul");

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) BSessionUtil.getSessionFromThreadContext().newTransaction();
            transaction.openTransaction();

            REBasesCalcul baseCalculEntity = new REBasesCalcul();
            baseCalculEntity.setIdBasesCalcul(rente.getBaseCalcul().getId().toString());
            baseCalculEntity.retrieve(transaction);

            REDemandeRente demandeRenteEntity = new REDemandeRente();
            demandeRenteEntity.setIdDemandeRente(demande.getId().toString());
            demandeRenteEntity.retrieve(transaction);

            RERenteCalculee renteCalculee = new RERenteCalculee();
            // pour la rente calculée : si la demande sur laquelle on doit crocher la rente accordée
            // en possède déjà une, on va la réutiliser. Sinon on crée une nouvelle rente calculée
            // et on la rattache à la demande
            if (!JadeStringUtil.isBlankOrZero(demandeRenteEntity.getIdRenteCalculee())) {
                renteCalculee.setIdRenteCalculee(demandeRenteEntity.getIdRenteCalculee());
                renteCalculee.retrieve(transaction);
            } else {
                renteCalculee.add(transaction);

                demandeRenteEntity.setIdRenteCalculee(renteCalculee.getIdRenteCalculee());
                demandeRenteEntity.update(transaction);
            }

            baseCalculEntity.setIdRenteCalculee(renteCalculee.getIdRenteCalculee());
            baseCalculEntity.update(transaction);

        } catch (Exception ex) {
            transaction.addErrors(ex.toString());
            throw new RETechnicalException(ex);
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.isRollbackOnly() || transaction.hasErrors()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception ex) {
                    throw new RETechnicalException(ex);
                } finally {
                    try {
                        transaction.closeTransaction();
                    } catch (Exception ex) {
                        throw new RETechnicalException(ex);
                    }
                }
            }
        }
    }

    @Override
    public Set<RenteAccordee> rentesAccordeesDevantEtreDiminueesLorsDeLaValidationDeLaDemande(final DemandeRente demande) {

        Set<RenteAccordee> rentesAccordessADiminuer = new HashSet<RenteAccordee>();
        Set<RenteAccordee> rentesAccordeesEnCoursDeLaFamille = rentesAccordeesEnCoursDeLaFamille(demande.getRequerant());
        Map<PersonneAVS, Set<RenteAccordee>> rentesEnCoursParBeneficiaireDeLaDemande = new HashMap<PersonneAVS, Set<RenteAccordee>>();

        /*
         * on récupère les bénéficiaires de la demande, pour n'ajouter que les rentes de ces bénéficiaires à la liste
         * des rentes à diminuer
         */
        for (RenteAccordee uneRenteDeLaDemande : demande.getRentesAccordees()) {
            if (!rentesEnCoursParBeneficiaireDeLaDemande.containsKey(uneRenteDeLaDemande.getBeneficiaire())) {

                Set<RenteAccordee> rentesEnCoursDuBeneficiaire = new HashSet<RenteAccordee>();

                // parcours des rentes en cours de la famille pour extraire les rentes de ce bénéficiaire
                for (RenteAccordee uneRenteDeLaFamille : rentesAccordeesEnCoursDeLaFamille) {
                    if (uneRenteDeLaFamille.getBeneficiaire().equals(uneRenteDeLaDemande.getBeneficiaire())) {
                        rentesEnCoursDuBeneficiaire.add(uneRenteDeLaFamille);
                    }
                }

                rentesEnCoursParBeneficiaireDeLaDemande.put(uneRenteDeLaDemande.getBeneficiaire(),
                        rentesEnCoursDuBeneficiaire);
            }
        }

        /*
         * On ne prend que les rentes accordées avec la même famille de code prestation (API d'une côté, AVS / AI de
         * l'autre)
         * De plus, les complémentaires doivent découler du même donneur de droit (on ne diminue que les rentes
         * liées à la rente du père si les complémentaires de la nouvelle demande sont liées à la rente du père)
         */
        for (Entry<PersonneAVS, Set<RenteAccordee>> unBeneficiaireEtSesRentesEnCours : rentesEnCoursParBeneficiaireDeLaDemande
                .entrySet()) {
            PersonneAVS beneficiaire = unBeneficiaireEtSesRentesEnCours.getKey();
            for (RenteAccordee uneRenteEnCoursDuBeneficiaire : unBeneficiaireEtSesRentesEnCours.getValue()) {
                /*
                 * Pour chaque rente en cours, on va vérifier les rentes de ce bénéficiaire dans la nouvelle demande
                 * afin de voir si une des rentes de cette demande va supplanter le droit à l'ancienne rente
                 */
                for (RenteAccordee uneRenteDeLaDemandePourCeBeneficiaire : demande
                        .getRentesAccordeesDuBeneficiaire(beneficiaire)) {
                    if (uneRenteDeLaDemandePourCeBeneficiaire
                            .estDeLaMemeFamilleDePrestationQue(uneRenteEnCoursDuBeneficiaire)) {
                        rentesAccordessADiminuer.add(uneRenteEnCoursDuBeneficiaire);
                    }
                }
            }
        }

        return rentesAccordessADiminuer;
    }

    @Override
    public Set<RenteAccordee> rentesAccordeesEnCoursDeLaFamille(final PersonneAVS requerant) {

        Set<RenteAccordee> rentesDeLaFamille = new HashSet<RenteAccordee>();

        try {
            RERenteAccordeeFamilleManager manager = new RERenteAccordeeFamilleManager();
            manager.setSession(BSessionUtil.getSessionFromThreadContext());
            manager.setSeulementRenteEnCours(true);
            manager.setForIdTiersLiant(requerant.getId().toString());
            manager.find();

            RenteAccordeeCrudService renteAccordeeCrudService = CorvusCrudServiceLocator.getRenteAccordeeCrudService();

            for (RERenteAccordeeFamille uneRenteEnCoursDeLaFamille : manager.getContainerAsList()) {
                Long idRenteAccordee = Long.parseLong(uneRenteEnCoursDeLaFamille.getIdRenteAccordee());
                rentesDeLaFamille.add(renteAccordeeCrudService.read(idRenteAccordee));
            }

        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        }

        return rentesDeLaFamille;
    }
}
