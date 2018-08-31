package ch.globaz.vulpecula.facturation;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.external.IntRole;
import globaz.osiris.utils.CAUtil;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.association.AssociationFacture;
import ch.globaz.vulpecula.domain.models.association.CotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.models.association.CotisationFacture;
import ch.globaz.vulpecula.domain.models.association.EnteteFactureAssociation;
import ch.globaz.vulpecula.domain.models.association.EtatFactureAP;
import ch.globaz.vulpecula.domain.models.association.FactureAssociation;
import ch.globaz.vulpecula.domain.models.association.FacturesAssociations;
import ch.globaz.vulpecula.domain.models.association.LigneFactureAssociation;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.external.models.affiliation.Adhesion;
import ch.globaz.vulpecula.external.models.osiris.TypeSection;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;

/**
 * Processus de facturation des associations professionnelles
 * Va rechercher les factures à l'état validé
 * Créé les afacts
 */
public final class PTProcessFacturationAssociationsProfGenerer extends PTProcessFacturation {
    private static final long serialVersionUID = -5267908350105123437L;

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public PTProcessFacturationAssociationsProfGenerer() {
        super();
    }

    public PTProcessFacturationAssociationsProfGenerer(final BProcess parent) {
        super(parent);
    }

    /**
     * Cette méthode exécute le processus de facturation des décomptes
     * 
     * @return boolean est-ce que la facturation s'est effectuée avec succès
     * @throws Exception
     */
    @Override
    protected boolean launch() {
        // Suppression des entete et afact du journal
        deleteEnteteEtAfactForIdPassage(getIdPassage());

        // Recherche des factures validées
        // Pour des raisons de mem on passe par un LinkedList qui permet de libérer la mem après chaque itération.
        FacturesAssociations factures = VulpeculaRepositoryLocator.getFactureAssociationRepository()
                .findValidesByIdPassage(getIdPassage());
        Deque<FactureAssociation> deque = new LinkedList<FactureAssociation>(factures.getFactures());

        // Boucler sur les décomptes et charger les lignes de salaires
        while (!deque.isEmpty() && !isAborted()) {
            FactureAssociation facture = deque.removeFirst();

            try {
                // Création de l'entête de facture
                FAEnteteFacture enteteFacture = createEnteteFacture(facture);

                // Pour chaque cotisation on créé une ligne de facture
                for (AssociationFacture association : facture.getAssociations()) {
                    for (CotisationFacture cotisation : association.getCotisationsOrdreAssociation()) {
                        createAfactCotisation(enteteFacture, cotisation, facture.getAnnee(), facture);
                    }
                    if (association.getLigneRabais() != null) {
                        createAfactRabais(enteteFacture, association.getLigneRabais(), facture.getAnnee(), facture);
                    }
                }

                if (!getTransaction().hasErrors()) {
                    // On va mettre à jour le numéro de passage et l'état du décompte à validé
                    majEtatDecompteEtNumeroPassage(facture);
                } else {
                    if (JadeThread.logMessages() != null && JadeThread.logMessages().length > 0) {
                        for (JadeBusinessMessage message : JadeThread.logMessages()) {
                            getTransaction().addErrors(message.getMessageId());
                            LOGGER.error(message.getMessageId());
                        }
                    }
                    return false;
                }
            } catch (Exception ex) {
                String message = "Facture association prof : " + facture.getId() + ", N° d'affilié : "
                        + facture.getEmployeur().getAffilieNumero() + "\r\n" + ex.toString();
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
     * Met le passage à l'état facturation
     * Va renseigner le numéro de passage de facturation dans le décompte
     * 
     * @param decompte
     */
    private void majEtatDecompteEtNumeroPassage(final FactureAssociation facture) {
        EnteteFactureAssociation entete = facture.getEnteteFacture();
        entete.setEtat(EtatFactureAP.GENERE);
        entete.setIdPassageFacturation(getPassage().getIdPassage());
        VulpeculaRepositoryLocator.getEnteteFactureRepository().update(entete);
    }

    /**
     * Créé une entete de facture si non existant, sinon retourne l'entete de facture existant dans le passage
     * 
     * @param decompte
     * @return {@link FAEnteteFacture} enteteFacture
     * @throws Exception
     */
    private FAEnteteFacture createEnteteFacture(final FactureAssociation facture) throws Exception {
        TypeSection idTypeSection;

        if (facture.isMembre()) {
            idTypeSection = TypeSection.ASSOCIATION;
        } else {
            idTypeSection = TypeSection.CPP;
        }

        BSession sessionOsiris = new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS);
        getSession().connectSession(sessionOsiris);

        String numeroSection = CAUtil.creerNumeroSectionUnique(sessionOsiris, getTransaction(),
                IntRole.ROLE_ASSOCIATION_PROFESSIONNELLE, facture.getEmployeurAffilieNumero(),
                idTypeSection.getValue(), facture.getAnnee().toString(),
                APISection.ID_CATEGORIE_SECTION_DECISION_ANNUELLE);

        FacturesAssociations facturesMemeEmployeurPassage = VulpeculaRepositoryLocator
                .getFactureAssociationRepository().findByIdPassageFacturationAndIdEmployeur(
                        getPassage().getIdPassage(), facture.getEmployeur().getId());

        List<FactureAssociation> listeFacturesMemeEmployeur = facturesMemeEmployeurPassage.getFactures();
        int plusGrandNumeroSection = 0;
        for (FactureAssociation factureMemeEmployeur : listeFacturesMemeEmployeur) {
            if (!JadeStringUtil.isBlank(factureMemeEmployeur.getNumeroFacture())
                    && Integer.valueOf(factureMemeEmployeur.getNumeroFacture()) > plusGrandNumeroSection) {
                plusGrandNumeroSection = Integer.valueOf(factureMemeEmployeur.getNumeroFacture());
            }
        }

        if (plusGrandNumeroSection == 0) {
            facture.getEnteteFacture().setNumeroSection(numeroSection);
        } else {
            plusGrandNumeroSection++;
            facture.getEnteteFacture().setNumeroSection(String.valueOf(plusGrandNumeroSection));
        }

        return super.createEnteteFactureRole(facture.getEmployeurIdTiers(), facture.getEmployeurAffilieNumero(),
                facture.getNumeroFacture(), idTypeSection.getValue(), IntRole.ROLE_ASSOCIATION_PROFESSIONNELLE);
    }

    /**
     * Création de la ligne de facture selon la cotisation calculee pour une entete de facture
     * 
     * @param enteteFacture
     * @param cotisation
     * @param anneeFacture
     * @param facture
     * @throws Exception
     */
    private void createAfactCotisation(FAEnteteFacture enteteFacture, CotisationFacture cotisation, Annee anneeFacture,
            FactureAssociation facture) throws Exception {

        createAfact(enteteFacture.getIdEntete(), cotisation.getCotisation(), anneeFacture, facture,
                cotisation.getMontantTotal());
    }

    /**
     * Création de la ligne de facture selon la cotisation calculee pour une entete de facture
     * 
     * @param enteteFacture
     * @param rabais
     * @param anneeFacture
     * @param facture
     * @throws Exception
     */
    private void createAfactRabais(FAEnteteFacture enteteFacture, LigneFactureAssociation rabais, Annee anneeFacture,
            FactureAssociation facture) throws Exception {

        createAfact(enteteFacture.getIdEntete(), rabais.getCotisationAssociationProfessionnelle(), anneeFacture,
                facture, rabais.getMontantCotisation());
    }

    /**
     * @param idEntete
     * @param cotisation
     * @param anneeFacture
     * @param facture
     * @param montantFacture
     * @throws Exception
     */
    private void createAfact(String idEntete, CotisationAssociationProfessionnelle cotisation, Annee anneeFacture,
            FactureAssociation facture, Montant montantFacture) throws Exception {

        // création de l'afact
        FAAfact afact = initAfact(idEntete, FAModuleFacturation.CS_MODULE_ASSOCIATIONS_PROF);

        // Recherche de la rubrique selon paramétrage de la cotisation
        CARubrique rubrique = new CARubrique();
        rubrique.setSession(getSession());
        rubrique.setIdRubrique(cotisation.getIdRubrique());
        rubrique.retrieve();

        if (!rubrique.isNew()) {
            if (rubrique.getNatureRubrique().equals(APIRubrique.COTISATION_AVEC_MASSE)
                    || rubrique.getNatureRubrique().equals(APIRubrique.COTISATION_SANS_MASSE)) {
                afact.setAnneeCotisation(String.valueOf(anneeFacture.getValue()));
            }
        }

        CodeLangue langue = CodeLangue.fromValue(facture.getEmployeurCSLangue());
        afact.setLibelle(cotisation.getAssociationProfessionnelle().getCodeAdministration() + " - "
                + cotisation.getLibelle(langue));
        afact.setIdRubrique(rubrique.getIdRubrique());
        afact.setDebutPeriode(anneeFacture.getFirstDayOfYear().getSwissValue());
        afact.setFinPeriode(anneeFacture.getLastDayOfYear().getSwissValue());
        // afact.setAnneeCotisation(anneeFacture.toString());
        afact.setNumCaisse(findIdCaisseMetier(facture.getEnteteFacture()));

        if (!rubrique.isNew()) {
            afact.setMontantFacture(JANumberFormatter.fmt(montantFacture.getValueNormalisee(), true, false, true, 2));
        }

        try {
            if (isAfactAAjouter(montantFacture)) {
                afact.add();
            }
        } catch (Exception e) {
            JadeBusinessMessage[] messages = JadeBusinessLogger.getInstance().getLogAdapter()
                    .getLogSession(JadeThread.currentContext().getBusinessLogSessionUUID()).getMessages();
            for (JadeBusinessMessage mess : messages) {
                LOGGER.error(mess.getMessageId());
                getTransaction().addErrors(mess.getMessageId());
            }

            String message = "Facture : " + facture.getId() + ", N° d'affilié : "
                    + facture.getEmployeur().getAffilieNumero() + ", Cotisation : " + cotisation.getLibelle() + "\r\n"
                    + e.toString();
            LOGGER.error(message);
            getTransaction().addErrors(message);
        }
    }

    /**
     * @param facture
     * @return
     */
    private String findIdCaisseMetier(EnteteFactureAssociation facture) {
        int idCaisseMetier = 0;
        Adhesion adh = VulpeculaRepositoryLocator.getAdhesionRepository().findCaisseMetier(facture.getIdEmployeur());
        if (adh != null) {
            idCaisseMetier = Integer.valueOf(adh.getPlanCaisse().getAdministration().getIdTiers());
        }
        return String.valueOf(idCaisseMetier);
    }
}
