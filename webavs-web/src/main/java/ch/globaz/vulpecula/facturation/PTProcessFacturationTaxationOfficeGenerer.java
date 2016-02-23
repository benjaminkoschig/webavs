package ch.globaz.vulpecula.facturation;

import globaz.globall.db.BProcess;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CARubrique;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.taxationoffice.EtatTaxation;
import ch.globaz.vulpecula.domain.models.taxationoffice.LigneTaxation;
import ch.globaz.vulpecula.domain.models.taxationoffice.TaxationOffice;
import ch.globaz.vulpecula.util.I18NUtil;

public class PTProcessFacturationTaxationOfficeGenerer extends PTProcessFacturation {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(PTProcessFacturationTaxationOfficeGenerer.class);

    public PTProcessFacturationTaxationOfficeGenerer() {
        super();
    }

    public PTProcessFacturationTaxationOfficeGenerer(final BProcess parent) {
        super(parent);
    }

    @Override
    protected boolean launch() {
        // Recherche des taxations d'office
        Deque<TaxationOffice> deque = new LinkedList<TaxationOffice>(VulpeculaRepositoryLocator
                .getTaxationOfficeRepository().getTaxationForFacturation(getPassage().getId()));

        // Boucler sur les décomptes et charger les lignes de salaires
        while (!deque.isEmpty() && !isAborted()) {
            TaxationOffice taxationOffice = deque.removeFirst();
            Decompte decompte = taxationOffice.getDecompte();

            try {
                // Création de l'entête de facture
                FAEnteteFacture enteteFacture = createEnteteFacture(taxationOffice);

                // Création des lignes de factures
                createAfact(taxationOffice, enteteFacture);

                if (!getTransaction().hasErrors()) {
                    // On va mettre à jour le numéro de passage et l'état du décompte à validé
                    updateEtatFacturationForTaxationOffice(taxationOffice);
                }
            } catch (Exception e) {
                String message = "Decompte : " + decompte.getId() + ", N° d'affilié : "
                        + decompte.getEmployeurAffilieNumero() + "\r\n" + e.toString();
                LOGGER.error(message);
                getTransaction().addErrors(message);
                return false;
            }

        }
        return !isAborted();
    }

    @Override
    protected void clean() {
    }

    /**
     * Créé une entete de facture si non existant, sinon retourne l'entete de facture existant dans le passage
     * 
     * @param decompte
     * @return {@link FAEnteteFacture} enteteFacture
     * @throws Exception
     */
    private FAEnteteFacture createEnteteFacture(final TaxationOffice taxationOffice) throws Exception {
        return createEnteteFacture(taxationOffice.getIdTiers(), taxationOffice.getEmployeurAffilieNumero(),
                taxationOffice.getNumeroDecompteAsValue(), taxationOffice.getDecompte().getTypeSection().getValue());
    }

    /**
     * Création de la ligne de facture selon la cotisation calculee pour une entete de facture
     * 
     * @param cotisationCalculee
     * @param enteteFacture
     * @param decompte
     * @throws Exception
     */
    private void createAfact(TaxationOffice taxationOffice, FAEnteteFacture enteteFacture) throws Exception {

        for (LigneTaxation ligneTaxation : taxationOffice.getLignes()) {
            String idCaisseMetier = VulpeculaRepositoryLocator.getAdhesionRepository()
                    .findCaisseMetier(taxationOffice.getEmployeur().getId()).getIdTiersAdministration();

            // création de l'afact
            FAAfact afact = initAfact(enteteFacture.getIdEntete(), FAModuleFacturation.CS_MODULE_TAXATION_OFFICE);
            // Recherche de la rubrique
            CARubrique rubrique = new CARubrique();
            rubrique.setSession(getSession());
            rubrique.setIdRubrique(ligneTaxation.getCotisation().getAssurance().getRubriqueId());
            rubrique.retrieve();
            if (!rubrique.isNew()) {
                if (rubrique.getNatureRubrique().equals(APIRubrique.COTISATION_AVEC_MASSE)
                        || rubrique.getNatureRubrique().equals(APIRubrique.COTISATION_SANS_MASSE)) {
                    afact.setAnneeCotisation(taxationOffice.getDecompte().getAnneePeriodeDebut());
                }
            }

            afact.setLibelle(ligneTaxation.getCotisation().getAssuranceLibelle(I18NUtil.getUserLocale()));
            afact.setIdRubrique(ligneTaxation.getCotisation().getAssurance().getRubriqueId());
            afact.setDebutPeriode(taxationOffice.getDecompte().getPeriodeDebutFormate());
            afact.setFinPeriode(taxationOffice.getDecompte().getPeriodeFinFormate());
            afact.setNumCaisse(idCaisseMetier);

            Montant masse = ligneTaxation.getMasse();
            Taux taux = ligneTaxation.getTaux();
            if (!rubrique.isNew()) {
                afact.setTauxFacture(String.valueOf(taux.getValue()));
                afact.setMasseFacture(String.valueOf(masse.getValueNormalisee()));
                afact.setMontantFacture(masse.multiply(taux).getValueNormalisee());
            } else {
                throw new IllegalStateException("La rubrique " + afact.getReferenceExterne() + " n'existe pas");
            }

            try {
                if (isAfactAAjouter(masse, rubrique, new Montant(afact.getMontantFacture()))) {
                    afact.add();
                }
            } catch (Exception e) {
                String message = "Taxation d'office : " + taxationOffice.getId() + ", N° d'affilié : "
                        + taxationOffice.getDecompte().getEmployeurAffilieNumero() + ", Cotisation : "
                        + ligneTaxation.getCotisation().getAssuranceLibelle(Locale.FRANCE) + ", Montant : "
                        + ligneTaxation.getMontantValue() + "\r\n" + e.toString();
                LOGGER.error(message);
                getTransaction().addErrors(message);
            }
        }
    }

    private void updateEtatFacturationForTaxationOffice(TaxationOffice taxationOffice) {
        taxationOffice.setEtat(EtatTaxation.FACTURATION);
        VulpeculaRepositoryLocator.getTaxationOfficeRepository().update(taxationOffice);
    }
}
