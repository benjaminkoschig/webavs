package ch.globaz.perseus.businessimpl.services.paiement;

import globaz.framework.util.FWCurrency;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessLogSession;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIOperationOrdreVersement;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.external.IntRole;
import globaz.perseus.utils.PFUserHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.model.JournalSimpleModel;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.perseus.business.constantes.CSEtatLot;
import ch.globaz.perseus.business.constantes.CSEtatPrestation;
import ch.globaz.perseus.business.constantes.CSTypeLot;
import ch.globaz.perseus.business.exceptions.models.lot.LotException;
import ch.globaz.perseus.business.exceptions.paiement.PaiementException;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.models.lot.OrdreVersement;
import ch.globaz.perseus.business.models.lot.Prestation;
import ch.globaz.perseus.business.models.lot.PrestationRP;
import ch.globaz.perseus.business.models.lot.PrestationRentePontSearchModel;
import ch.globaz.perseus.business.models.rentepont.FactureRentePont;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.paiement.PmtFactureRentePontService;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;

/**
 * @author DDE
 * 
 */
public class PmtFactureRentePontServiceImpl extends AbstractPmtServiceImpl implements PmtFactureRentePontService {

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

        if (!CSTypeLot.LOT_FACTURES_RP.getCodeSystem().equals(lot.getSimpleLot().getTypeLot())) {
            throw new PaiementException("Unable to comptabiliserLot, lot passed is not of type Facture RP");
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

            // Préparer la structure pour accueillir les ordres de versement par personne
            class OV {
                public String idAdressePaiement = null;
                public String idCompteAnnexe = null;
                public String idSection = null;
                public String montant = null;
                public String motif = null;
                public String referenceBVR = null;
            }

            HashMap<String, OV> ovGroupes = new HashMap<String, OV>();

            for (Prestation prestation : listPrestations) {
                String nss = "";
                // BZ7106 ajout du nom et prénom dans l'OV
                String infosTiersOV = "";
                String idPrestation = prestation.getId();
                try {
                    FactureRentePont frp = PerseusServiceLocator.getFactureRentePontService().read(
                            prestation.getSimplePrestation().getIdFacture());

                    nss = frp.getQdRentePont().getDossier().getDemandePrestation().getPersonneEtendue()
                            .getPersonneEtendue().getNumAvsActuel();
                    infosTiersOV = frp.getQdRentePont().getDossier().getDemandePrestation().getPersonneEtendue()
                            .getTiers().getDesignation1()
                            + " "
                            + frp.getQdRentePont().getDossier().getDemandePrestation().getPersonneEtendue().getTiers()
                                    .getDesignation2()
                            + " "
                            + frp.getQdRentePont().getDossier().getDemandePrestation().getPersonneEtendue()
                                    .getPersonne().getDateNaissance();
                    String idTiersBeneficiaire = frp.getQdRentePont().getDossier().getDemandePrestation()
                            .getPersonneEtendue().getTiers().getId();

                    // compte annexe
                    CompteAnnexeSimpleModel ca = CABusinessServiceLocator.getCompteAnnexeService().getCompteAnnexe(
                            journal.getIdJournal(), idTiersBeneficiaire, IntRole.ROLE_PCF, nss, true);

                    String typSection = (Float.parseFloat(prestation.getSimplePrestation().getMontantTotal()) < 0) ? APISection.CATEGORIE_SECTION_PCF_RESTITUTION
                            : APISection.CATEGORIE_SECTION_PCF_RFM;

                    String idExterne = PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt().substring(3);
                    idExterne += typSection;
                    idExterne += "000";
                    // section
                    SectionSimpleModel section = CABusinessServiceLocator.getSectionService().getSectionByIdExterne(
                            ca.getIdCompteAnnexe(), typSection, idExterne, journal);

                    if (Float.parseFloat(prestation.getSimplePrestation().getMontantTotal()) < 0) {
                        // Demande de restitution
                        // écriture
                        Float montantTotal = Float.parseFloat(prestation.getSimplePrestation().getMontantTotal());
                        montantTotal = montantTotal * -1;
                        CABusinessServiceLocator.getJournalService().addEcritureByRefRubrique(journal.getId(),
                                "Facture (restitution) rente-pont (" + nss + ") " + infosTiersOV, APIEcriture.DEBIT,
                                ca.getIdCompteAnnexe(), section.getId(), dateComptable,
                                definirCompte(prestation, true), prestation.getSimplePrestation().getMontantTotal());

                    } else if (Float.parseFloat(prestation.getSimplePrestation().getMontantTotal()) > 0) {
                        // Versement normal de décision
                        for (OrdreVersement ov : prestation.getListOrdreVersement()) {

                            AdresseTiersDetail adr = PFUserHelper.getAdressePaiementAssure(ov.getSimpleOrdreVersement()
                                    .getIdTiersAdressePaiement(), ov.getSimpleOrdreVersement()
                                    .getIdDomaineApplication(), dateComptable);
                            if ((adr == null) || (adr.getFields() == null)) {
                                throw new PaiementException(
                                        "Erreur dans le paiement : impossible de créer l'ordre de versement sans adresse de paiement ! ");
                            }

                            // écriture
                            CABusinessServiceLocator.getJournalService().addEcritureByRefRubrique(journal.getId(),
                                    "Facture rente-pont (" + nss + ") " + infosTiersOV, APIEcriture.CREDIT,
                                    ca.getIdCompteAnnexe(), section.getId(), dateComptable,
                                    definirCompte(prestation, false),
                                    ov.getSimpleOrdreVersement().getMontantVersement());

                            String idTiersAdressePaiement = adr.getFields().get(
                                    AdresseTiersDetail.ADRESSEP_ID_AVOIR_PAIEMENT_UNIQUE);

                            String key = ca.getId() + "," + section.getId() + "," + idTiersAdressePaiement;
                            // Si il n'y a pas encore d'ordre groupé pour ce compte annexe dans cette section pour le
                            // même tiers
                            if (ovGroupes.containsKey(key)) {
                                // On ajoute juste le montant
                                // BZ 8137 Utiliser des FW Cuurency à la place des Float
                                FWCurrency newMontant = new FWCurrency(ovGroupes.get(key).montant);
                                newMontant.add(ov.getSimpleOrdreVersement().getMontantVersement());
                                ovGroupes.get(key).montant = newMontant.toString();
                            } else {
                                // On créé l'OV
                                OV value = new OV();
                                value.idAdressePaiement = idTiersAdressePaiement;
                                value.idCompteAnnexe = ca.getId();
                                value.idSection = section.getId();
                                value.montant = ov.getSimpleOrdreVersement().getMontantVersement();
                                value.motif = "Facture rente-pont (" + nss + ") " + infosTiersOV;
                                value.referenceBVR = ov.getSimpleOrdreVersement().getNumFacture();

                                ovGroupes.put(key, value);
                            }
                        }
                    }
                    prestation.getSimplePrestation().setEtatPrestation(CSEtatPrestation.COMPTABILISE.getCodeSystem());
                    prestation = PerseusServiceLocator.getPrestationService().update(prestation);
                } catch (Exception e) {
                    e.printStackTrace();
                    logSession.error(this.getClass().getName(), e.toString() + " idPrestation : " + idPrestation
                            + " (NSS : " + nss + " " + infosTiersOV + ")");
                }
            }// Fin de bouclement des prestations

            // On verse les ordres de verseement
            for (OV ov : ovGroupes.values()) {
                try {

                    String typeVersement = null;
                    if (JadeStringUtil.isEmpty(ov.referenceBVR)) {
                        typeVersement = APIOperationOrdreVersement.VIREMENT;
                    } else {
                        typeVersement = APIOperationOrdreVersement.BVR;
                    }

                    // ordre de versement
                    CABusinessServiceLocator.getJournalService().addOrdreVersement(journal.getId(), ov.idCompteAnnexe,
                            ov.idSection, ov.idAdressePaiement, dateComptable, ov.montant, "CHF", "CHF", typeVersement,
                            CAOrdreGroupe.NATURE_PCF, ov.motif, ov.referenceBVR);
                } catch (Exception e) {
                    e.printStackTrace();
                    logSession.error(this.getClass().getName(), e.toString() + " Ordre de versement - (idCompteAnnexe:"
                            + ov.idCompteAnnexe + " idSection:" + ov.idSection + " idAdressePaiement:"
                            + ov.idAdressePaiement + ") (NSS : " + ov.motif + "), Reference BVR : " + ov.referenceBVR);
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

    private String definirCompte(Prestation prestation, Boolean restitution) {
        String compte = (restitution) ? APIReferenceRubrique.RENTE_PONT_RFM_RESTIT
                : APIReferenceRubrique.RENTE_PONT_RFM;

        return compte;
    }

    @Override
    public Map<String, List<FactureRentePont>> groupListFactureByDossierRP(Lot lot) throws PaiementException,
            JadePersistenceException {
        if ((lot == null) || lot.isNew()) {
            throw new PaiementException("Unable to groupFactureByMembreFamilleRP, lot is null or new");
        }
        // Contrôler que le lot soit de type facture
        if (!CSTypeLot.LOT_FACTURES_RP.getCodeSystem().equals(lot.getSimpleLot().getTypeLot())) {
            throw new PaiementException("Unable to groupFactureByMembreFamilleRP, lot is not of type facture_rp");
        }

        Map<String, List<FactureRentePont>> facturesParDossier = new HashMap<String, List<FactureRentePont>>();

        try {
            // On recherche les prestations du lot
            // BZ 6429, ajout du Size NoLimit
            PrestationRentePontSearchModel prestationRentePontSearchModel = new PrestationRentePontSearchModel();
            prestationRentePontSearchModel.setForIdLot(lot.getId());
            prestationRentePontSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            prestationRentePontSearchModel.getInTypeLot().add(CSTypeLot.LOT_FACTURES_RP.getCodeSystem());
            prestationRentePontSearchModel = PerseusServiceLocator.getPrestationRentePontService().search(
                    prestationRentePontSearchModel);

            for (JadeAbstractModel model : prestationRentePontSearchModel.getSearchResults()) {
                PrestationRP prestationRP = (PrestationRP) model;
                if (!(Float.parseFloat(prestationRP.getSimplePrestation().getMontantTotal()) < 0)) {
                    // MembreFamille mf =
                    // prestationRP.getFacture().getSimpleFactureRentePont().QdRentePont().MembreFamille();
                    // key = IdDossier,annee,idtiersadressecourrier
                    String key = prestationRP.getFacture().getQdRentePont().getDossier().getId() + ","
                            + prestationRP.getFacture().getQdRentePont().getSimpleQDRentePont().getAnnee() + ","
                            + prestationRP.getFacture().getSimpleFactureRentePont().getIdTiersAdresseCourrier();
                    // Pour chaque prestation, créer la liste de facture si elle n'existe pas encore
                    if (!facturesParDossier.containsKey(key)) {
                        facturesParDossier.put(key, new ArrayList<FactureRentePont>());
                    }
                    // Puis ajouter la facture dans la liste
                    facturesParDossier.get(key).add(prestationRP.getFacture());
                }
            }

        } catch (LotException e) {
            throw new PaiementException("LotException during groupFactureByMembreFamille : " + e.toString(), e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PaiementException("Service not available during groupFactureByMembreFamille : " + e.toString(), e);
        }

        return facturesParDossier;
    }

}
