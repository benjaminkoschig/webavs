package ch.globaz.al.businessimpl.services.dossiers;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALConstJournalisation;
import ch.globaz.al.business.exceptions.business.ALDossierBusinessException;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexSearchModel;
import ch.globaz.al.business.models.prestation.DetailPrestationComplexSearchModel;
import ch.globaz.al.business.models.prestation.radiation.PrestationRadiationDossierComplexModel;
import ch.globaz.al.business.models.prestation.radiation.PrestationRadiationDossierComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.dossiers.RadiationAutomatiqueService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Implémentation du service permettant la gestion de la radiation automatique de dossiers
 * 
 * @author jts
 * 
 */
public class RadiationAutomatiqueServiceImpl extends ALAbstractBusinessServiceImpl implements
        RadiationAutomatiqueService {

    /**
     * Vérifie si un ou plusieurs des droits passé en paramètre a une prestation plus récente que la période indiquées.
     * 
     * @param idDroits
     *            liste des droits à vérifier
     * @param periode
     *            la période à comparer
     * @return <code>true</code> si au moins un droit à une prestation plus récente
     * 
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private boolean hasMoreRecent(Collection<String> idDroits, String periode) throws JadeApplicationException,
            JadePersistenceException {

        if (idDroits.isEmpty()) {
            return false;
        }

        DetailPrestationComplexSearchModel search = new DetailPrestationComplexSearchModel();
        search.setInIdDroit(idDroits);
        search.setForPeriodeA(periode);
        search.setWhereKey(DetailPrestationComplexSearchModel.SEARCH_RADIATION_DOSSIER);
        return ALServiceLocator.getDetailPrestationComplexModelService().count(search) > 0;
    }

    @Override
    public PrestationRadiationDossierComplexSearchModel loadLastPrestations(String periode) throws Exception {

        if (JadeDateUtil.isGlobazDateMonth(periode)) {
            throw new ALDossierBusinessException("RadiationAutomatiqueServiceImpl#loadLastPrestations : '" + periode
                    + "' n'est pas une période valide");
        }

        return this.loadLastPrestations(periode, null);
    }

    public PrestationRadiationDossierComplexSearchModel loadLastPrestations(String periode, String idDossier)
            throws Exception {

        if (JadeDateUtil.isGlobazDateMonth(periode)) {
            throw new ALDossierBusinessException("RadiationAutomatiqueServiceImpl#loadLastPrestations : '" + periode
                    + "' n'est pas une période valide");
        }

        PrestationRadiationDossierComplexSearchModel search = new PrestationRadiationDossierComplexSearchModel();
        search.setForEtatDossier(ALCSDossier.ETAT_ACTIF);
        search.setForIdDossier(idDossier);
        search.setForPeriodeA(periode);
        search.setWhereKey(PrestationRadiationDossierComplexSearchModel.SEARCH_DERNIERE_PRESTATION);
        search.setOrderKey("dernierePrestation");
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        return (PrestationRadiationDossierComplexSearchModel) JadePersistenceManager.search(search);
    }

    /**
     * Radie le dossier passé en paramètre et le journalise
     * 
     * @param dossier
     *            Le dossier à radier
     * @param date
     *            La date de radiation
     * @return Le dossier radié
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private DossierComplexModel radier(DossierComplexModel dossier, String date) throws JadeApplicationException,
            JadePersistenceException {

        dossier.setDossierModel(ALImplServiceLocator.getDossierBusinessService().radierDossier(
                dossier.getDossierModel(), date, true,
                ALConstJournalisation.CHANGEMENT_ETAT_DOSSIER_REMARQUE_RADIATION_AUTO));

        return dossier;

    }

    @Override
    public RadiationAutomatiqueResult radierDossier(PrestationRadiationDossierComplexModel prest)
            throws JadeApplicationException, JadePersistenceException {

        DossierComplexModel dossier = prest.getDossierComplexModel();

        RadiationAutomatiqueResult resultRadiation = new RadiationAutomatiqueResult();

        // si la prestation date de plus d'un an par rapport à la date du jour
        if (JadeDateUtil.getNbMonthsBetween(
                JadeDateUtil.getFirstDateOfMonth(prest.getEntetePrestationModel().getPeriodeA()),
                JadeDateUtil.getGlobazFormattedDate(new Date())) > 12) {

            dossier = radier(dossier, JadeDateUtil.getLastDateOfMonth(prest.getEntetePrestationModel().getPeriodeA()));

            resultRadiation.setDossier(dossier);
            resultRadiation.setMotifRadiation(ALCSDroit.MOTIF_FIN_RAD);
            return resultRadiation;
        } else {

            DroitComplexSearchModel search = new DroitComplexSearchModel();
            search.setForIdDossier(dossier.getId());
            search.setOrderKey(DroitComplexSearchModel.ORDER_ECHEANCES_DROIT);
            search = ALServiceLocator.getDroitComplexModelService().search(search);

            DroitComplexModel dernierDroit = (DroitComplexModel) search.getSearchResults()[0];

            // si c'est une formation ou un incapable d'exercer
            if ((ALCSDroit.TYPE_FORM.equals(dernierDroit.getDroitModel().getTypeDroit()) || !dernierDroit
                    .getEnfantComplexModel().getEnfantModel().getCapableExercer())) {

                int limite = ALCSDroit.TYPE_FORM.equals(dernierDroit.getDroitModel().getTypeDroit()) ? 25 : 20;

                ArrayList<String> idDroits = new ArrayList<String>();
                for (JadeAbstractModel dro : search.getSearchResults()) {
                    String id = ((DroitComplexModel) dro).getId();
                    if (!id.equals(dernierDroit.getId())) {
                        idDroits.add(id);
                    }
                }

                // si le bénéficiaire à plus de 20/25 ans + 1 mois et qu'aucun droit du dossier n'a de prestation
                // datant
                // de moins d'un an
                if ((JadeDateUtil.getNbMonthsBetween(dernierDroit.getEnfantComplexModel()
                        .getPersonneEtendueComplexModel().getPersonne().getDateNaissance(),
                        JadeDateUtil.getGlobazFormattedDate(new Date())) > ((limite * 12) + 1))
                        && !hasMoreRecent(idDroits, JadeDateUtil.convertDateMonthYear(JadeDateUtil.addYears(
                                JadeDateUtil.getLastDateOfMonth(prest.getEntetePrestationModel().getPeriodeA()), -1)))) {

                    dossier = radier(dossier,
                            JadeDateUtil.getLastDateOfMonth(prest.getEntetePrestationModel().getPeriodeA()));

                    resultRadiation.setDossier(dossier);
                    resultRadiation.setMotifRadiation(dernierDroit.getDroitModel().getMotifFin());
                    return resultRadiation;
                } else {

                    resultRadiation.setDossier(dossier);
                    resultRadiation.setMotifRadiation(null);
                    return resultRadiation;
                }
            } else {
                resultRadiation.setDossier(dossier);
                resultRadiation.setMotifRadiation(null);
                return resultRadiation;
            }
        }
    }
}
