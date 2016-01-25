/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.paiement;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessLogSession;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIOperationOrdreVersement;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.external.IntRole;
import globaz.perseus.utils.PFUserHelper;
import java.util.Date;
import java.util.List;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.model.JournalSimpleModel;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.perseus.business.constantes.CSEtatLot;
import ch.globaz.perseus.business.constantes.CSEtatPrestation;
import ch.globaz.perseus.business.constantes.CSTypeLot;
import ch.globaz.perseus.business.constantes.CSTypeVersement;
import ch.globaz.perseus.business.exceptions.models.lot.LotException;
import ch.globaz.perseus.business.exceptions.paiement.PaiementException;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.models.lot.OrdreVersement;
import ch.globaz.perseus.business.models.lot.Prestation;
import ch.globaz.perseus.business.models.rentepont.RentePont;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.paiement.PmtDecisionRentePontService;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * @author DDE
 * 
 */
public class PmtDecisionRentePontServiceImpl extends AbstractPmtServiceImpl implements PmtDecisionRentePontService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.paiement.PmtDecisionService#comptabiliserLot(ch.globaz.perseus.business.models
     * .lot.Lot)
     */
    @Override
    public void comptabiliserLot(Lot lot, JadeBusinessLogSession logSession) throws PaiementException,
            JadePersistenceException {
        if (lot == null) {
            throw new PaiementException("Unable to comptabiliserLot, lot passed is null");
        }

        if (!CSTypeLot.LOT_DECISION_RP.getCodeSystem().equals(lot.getSimpleLot().getTypeLot())) {
            throw new PaiementException("Unable to comptabiliserLot, lot passed is not of type Decision");
        }

        if (!CSEtatLot.LOT_OUVERT.getCodeSystem().equals(lot.getSimpleLot().getEtatCs())) {
            throw new PaiementException("Unable to comptabiliserLot, lot passed is not open");
        }

        try {
            String dateComptable = JadeDateUtil.getGlobazFormattedDate(new Date());

            // création du journal
            JournalSimpleModel journal;
            journal = CABusinessServiceLocator.getJournalService().createJournal(lot.getSimpleLot().getDescription(),
                    dateComptable);

            List<Prestation> listPrestations = loadPrestationsWithOrdresVersement(lot);

            for (Prestation prestation : listPrestations) {
                String nss = "";
                String infosRequerant = "";
                String idPrestation = prestation.getId();
                try {
                    // Il faut charger la demande Rente-pont (pas dans le modèle pour le moment ou on supprimera les
                    // rentes-pont)
                    RentePont rp = PerseusServiceLocator.getRentePontService().read(
                            prestation.getSimplePrestation().getIdDecisionPcf());
                    PersonneEtendueComplexModel personne = rp.getDossier().getDemandePrestation().getPersonneEtendue();
                    nss = personne.getPersonneEtendue().getNumAvsActuel();
                    // Nom, prénom, date de naissance
                    infosRequerant = personne.getTiers().getDesignation1() + " "
                            + personne.getTiers().getDesignation2() + " " + personne.getPersonne().getDateNaissance();
                    String idTiersBeneficiaire = personne.getTiers().getId();

                    // compte annexe
                    CompteAnnexeSimpleModel ca = CABusinessServiceLocator.getCompteAnnexeService().getCompteAnnexe(
                            journal.getIdJournal(), idTiersBeneficiaire, IntRole.ROLE_PCF, nss, true);

                    String typSection = (Float.parseFloat(prestation.getSimplePrestation().getMontantTotal()) < 0) ? APISection.CATEGORIE_SECTION_PCF_RESTITUTION
                            : APISection.CATEGORIE_SECTION_PCF_DECISION;
                    // section
                    String idExterne = PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt().substring(3);
                    idExterne += typSection;
                    idExterne += "000";
                    SectionSimpleModel section = CABusinessServiceLocator.getSectionService().getSectionByIdExterne(
                            ca.getIdCompteAnnexe(), typSection, idExterne, journal);

                    if (Float.parseFloat(prestation.getSimplePrestation().getMontantTotal()) < 0) {
                        // Demande de restitution
                        // écriture
                        Float montantTotal = Float.parseFloat(prestation.getSimplePrestation().getMontantTotal());
                        montantTotal = montantTotal * -1;
                        CABusinessServiceLocator.getJournalService().addEcritureByRefRubrique(journal.getId(),
                                "Restitution rente-pont (" + nss + ") " + infosRequerant, APIEcriture.DEBIT,
                                ca.getIdCompteAnnexe(), section.getId(), dateComptable,
                                APIReferenceRubrique.RENTE_PONT_RESTIT,
                                prestation.getSimplePrestation().getMontantTotal());

                    } else if (Float.parseFloat(prestation.getSimplePrestation().getMontantTotal()) > 0) {
                        // écriture
                        CABusinessServiceLocator.getJournalService().addEcritureByRefRubrique(journal.getId(),
                                "Décision rente-pont (" + nss + ") " + infosRequerant, APIEcriture.CREDIT,
                                ca.getIdCompteAnnexe(), section.getId(), dateComptable,
                                APIReferenceRubrique.RENTE_PONT_PRESTATION,
                                prestation.getSimplePrestation().getMontantTotal());

                        // Versement normal de décision
                        for (OrdreVersement ov : prestation.getListOrdreVersement()) {

                            if (Float.parseFloat(ov.getSimpleOrdreVersement().getMontantVersement()) > 0) {
                                if (CSTypeVersement.IMPOT_A_LA_SOURCE.getCodeSystem().equals(
                                        ov.getSimpleOrdreVersement().getCsTypeVersement())) {
                                    // On paie un IMPOT A LA SOURCE
                                    CABusinessServiceLocator.getJournalService().addEcritureByRefRubrique(
                                            journal.getId(),
                                            "Retenue sur décision rente-pont (" + nss + ") " + infosRequerant,
                                            APIEcriture.DEBIT, ca.getIdCompteAnnexe(), section.getId(), dateComptable,
                                            APIReferenceRubrique.PCF_IMPOT_SOURCE,
                                            ov.getSimpleOrdreVersement().getMontantVersement());
                                } else {
                                    AdresseTiersDetail adr = PFUserHelper.getAdressePaiementAssure(ov
                                            .getSimpleOrdreVersement().getIdTiersAdressePaiement(), ov
                                            .getSimpleOrdreVersement().getIdDomaineApplication(), dateComptable);
                                    if ((adr == null) || (adr.getFields() == null)) {
                                        throw new PaiementException(
                                                "Erreur dans le paiement : impossible de créer l'ordre de versement sans adresse de paiement ! ");
                                    }

                                    String typeVersement = null;
                                    if (JadeStringUtil.isEmpty(ov.getSimpleOrdreVersement().getNumFacture())) {
                                        typeVersement = APIOperationOrdreVersement.VIREMENT;
                                    } else {
                                        typeVersement = APIOperationOrdreVersement.BVR;
                                    }

                                    // ordre de versement
                                    CABusinessServiceLocator.getJournalService().addOrdreVersement(journal.getId(),
                                            ca.getId(), section.getId(),
                                            adr.getFields().get(AdresseTiersDetail.ADRESSEP_ID_AVOIR_PAIEMENT_UNIQUE),
                                            dateComptable, ov.getSimpleOrdreVersement().getMontantVersement(), "CHF",
                                            "CHF", typeVersement, CAOrdreGroupe.NATURE_PCF,
                                            "Décision rente-pont (" + nss + ") " + infosRequerant,
                                            ov.getSimpleOrdreVersement().getNumFacture());
                                }
                            }
                        }
                    }
                    prestation.getSimplePrestation().setEtatPrestation(CSEtatPrestation.COMPTABILISE.getCodeSystem());
                    prestation = PerseusServiceLocator.getPrestationService().update(prestation);
                } catch (Exception e) {
                    e.printStackTrace();
                    logSession.error(this.getClass().getName(), e.toString() + " idPrestation : " + idPrestation
                            + " (NSS : " + nss + ") " + infosRequerant);
                }
            }

            CABusinessServiceLocator.getJournalService().comptabilise(journal);

            lot.getSimpleLot().setDateEnvoi(dateComptable);
            lot.getSimpleLot().setEtatCs(CSEtatLot.LOT_VALIDE.getCodeSystem());
            lot = PerseusServiceLocator.getLotService().update(lot);
        } catch (LotException e) {
            throw new PaiementException("LotException during comptabiliserLot : " + e.toString(), e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PaiementException("Service not available during comptabiliserLot : " + e.toString(), e);
        } catch (JadeApplicationException e) {
            throw new PaiementException("JadeApplicationException during comptabiliserLot : " + e.toString(), e);
        }

    }
}
