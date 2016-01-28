package ch.globaz.al.businessimpl.services.importation;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.HashMap;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.exceptions.model.droit.ALDroitComplexModelException;
import ch.globaz.al.business.exceptions.model.prestation.ALRecapitulatifEntrepriseModelException;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexSearchModel;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseSearchModel;
import ch.globaz.al.business.models.prestation.TransfertTucanaModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.importation.ImportationPrestationService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Implémentation du service d'importation des données de prestations
 * 
 * @author jts
 * 
 */
public class ImportationPrestationServiceImpl extends ImportationAllocataireServiceImpl implements
        ImportationPrestationService {

    @Override
    public DetailPrestationModel importDetailPrestation(DetailPrestationModel detailPrestation,
            String idEntetePrestation, String idDroit, HashMap<String, DroitComplexModel> droitComplexMap)
            throws JadeApplicationException, JadePersistenceException {

        // affectation de l'identifiant de l'entête de la prestation
        detailPrestation.setIdEntete(idEntetePrestation);

        // affectation de l'identifiant du droit pour le détail de la prestation
        detailPrestation.setIdDroit(idDroit);

        // TODO (lot 2) à modifier et donner la possibilité d'indiquer cette
        // information dans le XML d'import
        detailPrestation.setTarifForce(false);

        // affectation de l'âge de l'enfant
        setAgeEnfantForImport(detailPrestation, droitComplexMap);

        return detailPrestation = ALImplServiceLocator.getDetailPrestationModelService().create(detailPrestation);

    }

    @Override
    public EntetePrestationModel importEntetePrestation(EntetePrestationModel entetesPrestation, String idDossier,
            String idRecap) throws JadeApplicationException, JadePersistenceException {
        entetesPrestation.setIdDossier(idDossier);
        entetesPrestation.setIdRecap(idRecap);
        return entetesPrestation = ALImplServiceLocator.getEntetePrestationModelService().create(entetesPrestation);
    }

    @Override
    public RecapitulatifEntrepriseModel importRecapEntreprise(RecapitulatifEntrepriseModel recapEntrepImporte)
            throws JadeApplicationException, JadePersistenceException {

        RecapitulatifEntrepriseSearchModel st = new RecapitulatifEntrepriseSearchModel();

        st.setForNumeroFacture(recapEntrepImporte.getNumeroFacture());
        st.setForEtatRecap(recapEntrepImporte.getEtatRecap());
        st.setForNumeroAffilie(recapEntrepImporte.getNumeroAffilie());
        st.setForPeriodeA(recapEntrepImporte.getPeriodeA());
        st.setForPeriodeDe(recapEntrepImporte.getPeriodeDe());
        st.setForBonification(recapEntrepImporte.getBonification());

        st = ALServiceLocator.getRecapitulatifEntrepriseModelService().search(st);

        if (st.getSize() > 1) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "ImportationPrestationServiceImpl#importRecapEntreprise : Unable to import recaps Enterprise, multiple result for the search");

        } else if (st.getSize() == 1) {
            // Soit le défini pour la suite de l'importation -> fait en
            // sorte que l'idRecapitulatif soit celui qu'on a trouvé
            recapEntrepImporte = (RecapitulatifEntrepriseModel) st.getSearchResults()[0];
        } else {
            // création de l'enregistrement en base de données
            recapEntrepImporte = ALServiceLocator.getRecapitulatifEntrepriseModelService().create(recapEntrepImporte);
        }

        return recapEntrepImporte;
    }

    @Override
    public TransfertTucanaModel importTransfertTucana(TransfertTucanaModel transfertTucana, String idDetailPrestation)
            throws JadeApplicationException, JadePersistenceException {

        // affectation de l'identifiant du détail de la prestation
        transfertTucana.setIdDetailPrestation(idDetailPrestation);

        // création du transfert
        return ALImplServiceLocator.getTransfertTucanaModelService().create(transfertTucana);

    }

    /**
     * Méthode qui récupère l'âge des enfant lors de l'importattion des données
     * 
     * @param detailPrestation
     *            Prestation
     * @param droitComplexMap
     *            Liste de droits déjà traité composée de couple id du droit => modèle complexe du droit
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private void setAgeEnfantForImport(DetailPrestationModel detailPrestation,
            HashMap<String, DroitComplexModel> droitComplexMap) throws JadeApplicationException,
            JadePersistenceException {

        DroitComplexModel droitComplex = droitComplexMap.get(detailPrestation.getIdDroit());

        if ((droitComplex == null) && !JadeNumericUtil.isEmptyOrZero(detailPrestation.getIdDroit())) {

            DroitComplexSearchModel seDroitComplex = new DroitComplexSearchModel();
            seDroitComplex.setForIdDroit(detailPrestation.getIdDroit());
            seDroitComplex = ALServiceLocator.getDroitComplexModelService().search(seDroitComplex);
            if ((seDroitComplex.getSize() > 1)) {
                throw new ALDroitComplexModelException(
                        "PrestationBusinessServiceImpl#importDetailPrestation: several results for seDroitComplex");
            } else if (seDroitComplex.getSize() == 1) {
                droitComplex = (DroitComplexModel) seDroitComplex.getSearchResults()[0];
                droitComplexMap.put(detailPrestation.getIdDroit(), droitComplex);
            } else {
                throw new ALDroitComplexModelException(
                        "PrestationBusinessServiceImpl#importDetailPrestation: nothing found for seDroitComplex");
            }
        }

        if (ALCSDroit.TYPE_ENF.equals(detailPrestation.getTypePrestation())
                || ALCSDroit.TYPE_FORM.equals(detailPrestation.getTypePrestation())) {

            if (droitComplex == null) {
                detailPrestation.setAgeEnfant("0");
            } else {
                detailPrestation.setAgeEnfant(ALServiceLocator.getPrestationBusinessService()
                        .getAgeEnfantDetailPrestation(droitComplex, detailPrestation));
            }
        }
    }
}