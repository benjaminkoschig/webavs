package ch.globaz.vulpecula.facturation;

import globaz.globall.db.BProcess;
import globaz.globall.util.JACalendar;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.congepaye.Compteur;
import ch.globaz.vulpecula.domain.models.decompte.CotisationDecompte;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.HistoriqueDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.domain.models.parametrage.TableParametrage;

/**
 * Processus de facturation des d�comptes salaires
 * 
 * @author JPA
 */
public final class PTProcessFacturationDecomptesSalaireComptabiliser extends PTProcessFacturation {
    private static final long serialVersionUID = 1L;

    public PTProcessFacturationDecomptesSalaireComptabiliser() {
        super();
    }

    public PTProcessFacturationDecomptesSalaireComptabiliser(final BProcess parent) {
        super(parent);
    }

    @Override
    protected boolean launch() {
        List<Decompte> listeDecomptes = VulpeculaRepositoryLocator.getDecompteRepository().findDecomptesForIdPassage(
                getPassage().getId());
        for (Decompte decompte : listeDecomptes) {
            // Mise � jour du compteur
            majCompteurCongePaye(decompte);
            majEtatDecompteEtNumeroPassage(decompte);
        }
        return true;
    }

    @Override
    protected void clean() {
    }

    private void majCompteurCongePaye(Decompte decompte) {
        for (DecompteSalaire ligneSalaire : decompte.getLignes()) {
            ligneSalaire.getPosteTravail().setAdhesionsCotisations(
                    VulpeculaRepositoryLocator.getAdhesionCotisationPosteRepository().findByIdPosteTravail(
                            ligneSalaire.getIdPosteTravail()));
            int idCaisseMetier = VulpeculaServiceLocator.getPosteTravailService().getNumeroCaissePrincipale(
                    ligneSalaire.getIdPosteTravail());

            // Si le poste de travail a le droit au CP, que le salaire total est diff�rent de z�ro et que la ligne de
            // d�compte dispose de la cotisation ad�quate.
            if (!ligneSalaire.getSalaireTotal().isZero() && hasAssuranceCP(ligneSalaire, idCaisseMetier)) {
                Compteur compteur = VulpeculaRepositoryLocator.getCompteurRepository().findByPosteTravailAndAnnee(
                        ligneSalaire.getIdPosteTravail(), ligneSalaire.getAnneeCotisation());

                if (compteur == null) {
                    compteur = new Compteur();
                    compteur.setAnnee(new Annee(ligneSalaire.getAnneeCotisation()));
                    compteur.setPosteTravail(ligneSalaire.getPosteTravail());
                    compteur.setCumulCotisation(ligneSalaire.getSalaireTotal());
                    compteur.setMontantRestant(ligneSalaire.getSalaireTotal());
                    VulpeculaRepositoryLocator.getCompteurRepository().create(compteur);
                } else {
                    Montant montantAAjouter = ligneSalaire.getSalaireTotal();
                    compteur.addCotisation(montantAAjouter);
                    compteur.setMontantRestant(compteur.getMontantRestant().add(montantAAjouter));
                    VulpeculaRepositoryLocator.getCompteurRepository().update(compteur);
                }
            }
        }
    }

    /**
     * Retourne si le d�compte salaire dispose d'une cotisation ayant droit au cong� pay�.
     * 
     * @return true si assurance pr�sente
     */
    private boolean hasAssuranceCP(DecompteSalaire decompteSalaire, int noCaisseMetier) {
        List<TypeAssurance> assurancesObligatoires = TableParametrage.getInstance().getTypeAssuranceObligatoireForCP(
                noCaisseMetier);
        for (CotisationDecompte cotisation : decompteSalaire.getCotisationsDecompte()) {
            if (assurancesObligatoires.contains(cotisation.getTypeAssurance())) {
                return true;
            }
        }
        return false;
    }

    private void majEtatDecompteEtNumeroPassage(final Decompte decompte) {
        decompte.setEtat(EtatDecompte.COMPTABILISE);
        VulpeculaRepositoryLocator.getDecompteRepository().update(decompte);

        HistoriqueDecompte historique = new HistoriqueDecompte();
        historique.setDecompte(decompte);
        historique.setDate(new Date(JACalendar.today().toStrAMJ()));
        historique.setEtat(EtatDecompte.COMPTABILISE);
        VulpeculaRepositoryLocator.getHistoriqueDecompteRepository().create(historique);
    }
}
