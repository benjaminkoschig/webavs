package ch.globaz.vulpecula.facturation;

import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APISection;
import java.util.Deque;
import java.util.LinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.decompte.NumeroDecompte;
import ch.globaz.vulpecula.domain.models.prestations.Beneficiaire;
import ch.globaz.vulpecula.domain.models.prestations.Etat;
import ch.globaz.vulpecula.domain.models.servicemilitaire.ServiceMilitaire;
import ch.globaz.vulpecula.util.RubriqueUtil;
import ch.globaz.vulpecula.util.RubriqueUtil.Compte;
import ch.globaz.vulpecula.util.RubriqueUtil.Convention;
import ch.globaz.vulpecula.util.RubriqueUtil.Prestation;

/**
 * Processus de génération des services militaires
 */
public class PTProcessFacturationServiceMilitaireGenerer extends PTProcessFacturation {
    private static final long serialVersionUID = -5074164373120773121L;

    private static final String PASSAGE_NAME = "SM. ";
    private static final Logger LOGGER = LoggerFactory.getLogger(PTProcessFacturationServiceMilitaireGenerer.class);

    @Override
    protected void clean() {
    }

    @Override
    protected String getIdAdressePaiement(String idTiers) {
        return VulpeculaRepositoryLocator.getAdresseRepository()
                .findForPrestations(idTiers, new Date(getPassage().getDateFacturation())).getId();
    }

    @Override
    protected boolean launch() {
        // Suppression des entete et afact du journal
        deleteEnteteEtAfactForIdPassage(getIdPassage());

        Deque<ServiceMilitaire> deque = new LinkedList<ServiceMilitaire>(VulpeculaRepositoryLocator
                .getServiceMilitaireRepository().findForFacturation(getPassage().getId()));

        while (!deque.isEmpty() && !isAborted()) {
            ServiceMilitaire serviceMilitaire = deque.removeFirst();

            if (serviceMilitaire.getMontantBrut().isZero()) {
                continue;
            }

            NumeroDecompte numero = new NumeroDecompte(serviceMilitaire.getAnneeDebut(),
                    NumeroDecompte.SERVICE_MILITAIRE);
            FAEnteteFacture enteteFacture = null;
            try {
                if (Beneficiaire.EMPLOYEUR.equals(serviceMilitaire.getBeneficiaire())
                        || Beneficiaire.NOTE_CREDIT.equals(serviceMilitaire.getBeneficiaire())) {
                    // Création de l'entête de facture
                    enteteFacture = createEnteteFacture(serviceMilitaire.getEmployeurIdTiers(),
                            serviceMilitaire.getNoAffilie(), numero.getValue(),
                            APISection.ID_TYPE_SECTION_PRESTATIONS_CONVENTIONNELLES);

                    // Création des lignes de factures
                    createAfactForEmployeur(enteteFacture, serviceMilitaire);
                } else if (Beneficiaire.TRAVAILLEUR.equals(serviceMilitaire.getBeneficiaire())) {
                    if (serviceMilitaire.getTravailleurNss() == null
                            || serviceMilitaire.getTravailleurNss().length() == 0) {
                        this._addError("Pas de numéro NSS pour le travailleur "
                                + serviceMilitaire.getPosteTravail().getDescriptionTravailleur() + ". IdTiers="
                                + serviceMilitaire.getTravailleurIdTiers());
                        continue;
                    }
                    enteteFacture = createEnteteFacture(serviceMilitaire.getTravailleurIdTiers(),
                            serviceMilitaire.getTravailleurNss(), numero.getValue(),
                            APISection.ID_TYPE_SECTION_PRESTATIONS_CONVENTIONNELLES);
                    createAfactForTravailleur(enteteFacture, serviceMilitaire);
                }
            } catch (Exception ex) {
                String message = "Service militaire id : " + serviceMilitaire.getId() + ", N° d'affilié : "
                        + serviceMilitaire.getNoAffilie() + " " + serviceMilitaire.getRaisonSocialeEmployeur() + " / "
                        + serviceMilitaire.getTravailleurNss() + "\r\n" + ex.toString();

                LOGGER.error(message);
                getTransaction().addErrors(message);
                return false;
            }

            if (!getTransaction().hasErrors()) {
                majEtatPrestation(serviceMilitaire);
            }
        }
        VulpeculaServiceLocator.getPassageService().createPassageForNextWeekIfNotExist(getSession(),
                FAModuleFacturation.CS_MODULE_SERVICE_MILITAIRE, PASSAGE_NAME, getIdPassage());
        return !isAborted();
    }

    /**
     * Change l'état de la prestation à TRAITEE
     * 
     * @param absence
     */
    private void majEtatPrestation(final ServiceMilitaire serviceMilitaire) {
        serviceMilitaire.setEtat(Etat.TRAITEE);
        VulpeculaRepositoryLocator.getServiceMilitaireRepository().update(serviceMilitaire);
    }

    /**
     * Création de la ligne de facture selon la cotisation calculee pour une entete de facture
     * 
     * @param enteteFacture
     * @param serviceMilitaire
     * @throws Exception
     */
    private void createAfactForEmployeur(FAEnteteFacture enteteFacture, ServiceMilitaire serviceMilitaire)
            throws Exception {
        FAAfact afact = createAfactForTravailleur(enteteFacture, serviceMilitaire);

        // création de l'afact
        String montantAVS = serviceMilitaire.getMontantAVS().normalize().getNegativeValue().toString();
        String montantAC = serviceMilitaire.getMontantAC().normalize().getNegativeValue().toString();

        addAfact(afact, serviceMilitaire, montantAC, Compte.PARTS_PATRONALES_AC);
        addAfact(afact, serviceMilitaire, montantAVS, Compte.PARTS_PATRONALES_AVS);
    }

    /**
     * @param afact
     * @param serviceMilitaire
     * @param montant
     * @param compte
     * @throws Exception
     */
    private void addAfact(FAAfact afact, ServiceMilitaire serviceMilitaire, String montant, Compte compte)
            throws Exception {
        if (!JadeStringUtil.isBlankOrZero(montant)) {
            String csRefRubrique = RubriqueUtil.findReferenceRubriqueFor(Prestation.SERVICE_MILIATIRE,
                    Convention.fromValue(serviceMilitaire.getConventionEmployeur().getCode()), compte);
            APIRubrique rubriquePartCot = RubriqueUtil.retrieveRubriqueForReference(getSession(), csRefRubrique);
            afact.setLibelle(rubriquePartCot.getDescription(serviceMilitaire.getPosteTravail().getEmployeur()
                    .getLangue()));
            afact.setIdRubrique(rubriquePartCot.getId());
            afact.setDebutPeriode(serviceMilitaire.getDateDebutAsString());
            afact.setFinPeriode(serviceMilitaire.getDateFinAsString());
            afact.setMontantFacture(montant);
            afact.add();
        }
    }

    /**
     * @param enteteFacture
     * @param serviceMilitaire
     * @return un afact
     * @throws Exception
     */
    private FAAfact createAfactForTravailleur(FAEnteteFacture enteteFacture, ServiceMilitaire serviceMilitaire)
            throws Exception {
        // création de l'afact
        FAAfact afact = initAfact(enteteFacture.getIdEntete(), FAModuleFacturation.CS_MODULE_SERVICE_MILITAIRE);

        // Ajout des afacts pour la rubrique SM
        String csRefRubrique = RubriqueUtil.findReferenceRubriqueFor(Prestation.SERVICE_MILIATIRE,
                Convention.fromValue(serviceMilitaire.getConventionEmployeur().getCode()), Compte.PRESTATION);
        APIRubrique rubrique = RubriqueUtil.retrieveRubriqueForReference(getSession(), csRefRubrique);

        afact.setLibelle(rubrique.getDescription(serviceMilitaire.getPosteTravail().getEmployeur().getLangue()));
        afact.setIdRubrique(rubrique.getId());
        afact.setDebutPeriode(serviceMilitaire.getPeriode().getDateDebutAsSwissValue());
        afact.setFinPeriode(serviceMilitaire.getPeriode().getDateFinAsSwissValue());
        afact.setMontantFacture(serviceMilitaire.getMontantBrut().getNegativeValue().toString());
        int idCaisseMetier = VulpeculaServiceLocator.getPosteTravailService().getIdTiersCaissePrincipale(
                serviceMilitaire.getIdPosteTravail());
        afact.setNumCaisse(String.valueOf(idCaisseMetier));
        afact.setReferenceExterne(serviceMilitaire.getBeneficiaire().getValue() + "-"
                + serviceMilitaire.getConventionEmployeur().getCode());
        afact.add();

        return afact;
    }
}
