package ch.globaz.vulpecula.facturation;

import globaz.globall.db.BProcess;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.decompte.DecompteService;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.congepaye.Compteur;
import ch.globaz.vulpecula.domain.models.decompte.CotisationDecompte;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.HistoriqueDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.domain.models.parametrage.TableParametrage;
import ch.globaz.vulpecula.domain.models.taxationoffice.TaxationOffice;

/**
 * Processus de facturation des décomptes salaires
 * 
 * @author JPA
 */
public final class PTProcessFacturationDecomptesSalaireComptabiliser extends PTProcessFacturation {
    private static final long serialVersionUID = 1L;

    private DecompteService decompteService;

    public PTProcessFacturationDecomptesSalaireComptabiliser() {
        super();
        decompteService = VulpeculaServiceLocator.getDecompteService();
    }

    public PTProcessFacturationDecomptesSalaireComptabiliser(final BProcess parent) {
        super(parent);
        decompteService = VulpeculaServiceLocator.getDecompteService();
    }

    @Override
    protected boolean launch() {
        List<Decompte> listeDecomptes = VulpeculaRepositoryLocator.getDecompteRepository().findDecomptesForIdPassage(
                getPassage().getId());
        // Code commenté : ne plus imprimer la ListeDecomptesSansAFExcel lors de la comptabilisation des décomptes.
        // List<Decompte> decomptesSansPrestationsAF = new ArrayList<Decompte>();
        for (Decompte decompte : listeDecomptes) {
            // Mise à jour du compteur
            majCompteurCongePaye(decompte);

            majEtatDecompteEtNumeroPassage(decompte);

            if (decompte.isControleEmployeur()) {
                Periode periodeCT = new Periode(decompte.getPeriodeDebut(), decompte.getPeriodeFin());
                List<TaxationOffice> listeTO = VulpeculaRepositoryLocator.getTaxationOfficeRepository()
                        .findByIdEmployeur(decompte.getIdEmployeur());

                VulpeculaServiceLocator.getTaxationOfficeService().annulerForPeriode(listeTO, periodeCT);
            }

            // if (decompte.isPeriodique() && !decompteService.hasPrestationsAFOuPasDossier(decompte)) {
            // decomptesSansPrestationsAF.add(decompte);
            // }
        }

        // if (!decomptesSansPrestationsAF.isEmpty()) {
        // ListeDecomptesSansAFExcel doc = new ListeDecomptesSansAFExcel(getSession(),
        // DocumentConstants.LISTES_DECOMPTES_SANS_AF, DocumentConstants.LISTES_DECOMPTES_SANS_AF_DOC_NAME,
        // decomptesSansPrestationsAF, getPassage().getId());
        // doc.create();
        // try {
        // JadeSmtpClient.getInstance().sendMail(getSession().getUserEMail(),
        // DocumentConstants.LISTES_DECOMPTES_SANS_AF, "", new String[] { doc.getOutputFile() });
        // } catch (Exception ex) {
        // throw new RuntimeException(ex);
        // }
        // }

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

            // Si le poste de travail a le droit au CP, que le salaire total est différent de zéro et que la ligne de
            // décompte dispose de la cotisation adéquate.
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
     * Retourne si le décompte salaire dispose d'une cotisation ayant droit au congé payé.
     * 
     * @return true si assurance présente
     */
    private boolean hasAssuranceCP(DecompteSalaire decompteSalaire, int noCaisseMetier) {
        List<TypeAssurance> assurancesObligatoires = TableParametrage.getInstance(
                decompteSalaire.getPeriodeDebut().getAnnee()).getTypeAssuranceObligatoireForCP(noCaisseMetier);
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

        historique.setDate(new Date(getPassage().getDateFacturation()));

        historique.setEtat(EtatDecompte.COMPTABILISE);
        VulpeculaRepositoryLocator.getHistoriqueDecompteRepository().create(historique);
    }
}
