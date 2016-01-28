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
 * Impl�mentation du service d'importation des donn�es de prestations
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

        // affectation de l'identifiant de l'ent�te de la prestation
        detailPrestation.setIdEntete(idEntetePrestation);

        // affectation de l'identifiant du droit pour le d�tail de la prestation
        detailPrestation.setIdDroit(idDroit);

        // TODO (lot 2) � modifier et donner la possibilit� d'indiquer cette
        // information dans le XML d'import
        detailPrestation.setTarifForce(false);

        // affectation de l'�ge de l'enfant
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
            // Soit le d�fini pour la suite de l'importation -> fait en
            // sorte que l'idRecapitulatif soit celui qu'on a trouv�
            recapEntrepImporte = (RecapitulatifEntrepriseModel) st.getSearchResults()[0];
        } else {
            // cr�ation de l'enregistrement en base de donn�es
            recapEntrepImporte = ALServiceLocator.getRecapitulatifEntrepriseModelService().create(recapEntrepImporte);
        }

        return recapEntrepImporte;
    }

    @Override
    public TransfertTucanaModel importTransfertTucana(TransfertTucanaModel transfertTucana, String idDetailPrestation)
            throws JadeApplicationException, JadePersistenceException {

        // affectation de l'identifiant du d�tail de la prestation
        transfertTucana.setIdDetailPrestation(idDetailPrestation);

        // cr�ation du transfert
        return ALImplServiceLocator.getTransfertTucanaModelService().create(transfertTucana);

    }

    /**
     * M�thode qui r�cup�re l'�ge des enfant lors de l'importattion des donn�es
     * 
     * @param detailPrestation
     *            Prestation
     * @param droitComplexMap
     *            Liste de droits d�j� trait� compos�e de couple id du droit => mod�le complexe du droit
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
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