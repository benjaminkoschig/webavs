package ch.globaz.al.businessimpl.services.rafam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import al.ch.ech.xmlns.ech_0104._4.Error;
import al.ch.ech.xmlns.ech_0104._4.ErrorPeriodType;
import al.ch.ech.xmlns.ech_0104._4.OverlapInformation;
import ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce;
import ch.globaz.al.business.exceptions.model.annonces.rafam.ALAnnonceRafamException;
import ch.globaz.al.business.models.rafam.AnnonceRafamErrorComplexModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamErrorComplexSearchModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.business.models.rafam.ErreurAnnonceRafamModel;
import ch.globaz.al.business.models.rafam.ErreurAnnonceRafamSearchModel;
import ch.globaz.al.business.models.rafam.ErrorPeriodModel;
import ch.globaz.al.business.models.rafam.OverlapInformationModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.rafam.AnnoncesRafamNewXSDVersionErrorBusinessService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALDateUtils;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;

public class AnnoncesRafamNewXSDVersionErrorBusinessServiceImpl extends ALAbstractBusinessServiceImpl
        implements AnnoncesRafamNewXSDVersionErrorBusinessService {

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.al.business.services.rafam.InitAnnoncesRafamService#setErrors(ch.globaz.al.business.models.rafam.
     * AnnonceRafamModel, java.util.List)
     */
    @Override
    public void addErrors(List<Error> errors, AnnonceRafamModel annonce)
            throws JadeApplicationException, JadePersistenceException {

        for (Error error : errors) {

            ErreurAnnonceRafamModel erreurAnnonce = new ErreurAnnonceRafamModel();
            erreurAnnonce.setCode(Long.toString(error.getCode()));
            erreurAnnonce.setIdAnnonce(annonce.getIdAnnonce());
            erreurAnnonce = ALServiceLocator.getErreurAnnonceRafamModelService().create(erreurAnnonce);

            if (!erreurAnnonce.isNew()) {

                if ((error.getErrorPeriod() != null) && !error.getErrorPeriod().isEmpty()) {
                    for (ErrorPeriodType period : error.getErrorPeriod()) {
                        ErrorPeriodModel periodModel = new ErrorPeriodModel();
                        periodModel.setIdErreurAnnonce(erreurAnnonce.getIdErreurAnnonce());
                        periodModel
                                .setErrorPeriodStart(ALDateUtils.XMLGregorianCalendarToGlobazDate(period.getStart()));
                        periodModel.setErrorPeriodEnd(ALDateUtils.XMLGregorianCalendarToGlobazDate(period.getEnd()));

                        ALServiceLocator.getErrorPeriodModelService().create(periodModel);
                    }
                }

                if ((error.getOverlapInformation() != null) && !error.getOverlapInformation().isEmpty()) {
                    for (OverlapInformation overlapInfo : error.getOverlapInformation()) {
                        OverlapInformationModel overlapInfoModel = new OverlapInformationModel();
                        overlapInfoModel.setIdErreurAnnonce(erreurAnnonce.getIdErreurAnnonce());
                        overlapInfoModel
                                .setOfficeIdentifier(overlapInfo.getDeliveryOfficeConflict().getOfficeIdentifier());
                        if (overlapInfo.getDeliveryOfficeConflict().getBranch() != null) {
                            overlapInfoModel
                                    .setBranch(Long.toString(overlapInfo.getDeliveryOfficeConflict().getBranch()));
                        }

                        overlapInfoModel.setOverlapPeriodeStart(ALDateUtils
                                .XMLGregorianCalendarToGlobazDate(overlapInfo.getOverlapPeriod().getStart()));
                        overlapInfoModel.setOverlapPeriodeEnd(
                                ALDateUtils.XMLGregorianCalendarToGlobazDate(overlapInfo.getOverlapPeriod().getEnd()));
                        overlapInfoModel.setMinimalStartFlag(overlapInfo.getMinimalStartFlag());
                        overlapInfoModel.setInsignificance(overlapInfo.getInsignificance().equals("0") ? false : true);

                        ALServiceLocator.getOverlapInformationModelService().create(overlapInfoModel);
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.al.business.services.rafam.AnnoncesRafamErrorBusinessService#getErrorsForAnnonce(java.lang.String)
     */
    @Override
    public AnnonceRafamErrorComplexSearchModel getErrorsForAnnonce(String idAnnonce)
            throws JadeApplicationException, JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(idAnnonce)) {
            throw new ALAnnonceRafamException(
                    "AnnoncesRafamErrorBusinessServiceImpl#getErrorsForAnnonce : idAnnonce is not defined");
        }

        AnnonceRafamErrorComplexSearchModel search = new AnnonceRafamErrorComplexSearchModel();
        search.setForIdAnnonce(idAnnonce);

        return ALImplServiceLocator.getAnnonceRafamErrorComplexModelService().search(search);
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.al.business.services.rafam.AnnoncesRafamErrorBusinessService#getErrorsFromList(java.util.List)
     */
    @Override
    public ArrayList<String> getErrorsFromList(List<Error> errors) {
        ArrayList<String> erreurs = new ArrayList<String>();

        for (Error error : errors) {
            erreurs.add(Long.toString(error.getCode()));
        }

        return erreurs;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.al.business.services.rafam.AnnoncesRafamErrorBusinessService#hasError(java.lang.String,
     * java.lang.String)
     */
    @Override
    public boolean hasError(String idAnnonce, String code) throws JadeApplicationException, JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(idAnnonce)) {
            throw new ALAnnonceRafamException(
                    "AnnoncesRafamErrorBusinessServiceImpl#hasError : idAnnonce is empty or zero");
        }

        ErreurAnnonceRafamSearchModel search = new ErreurAnnonceRafamSearchModel();
        search.setForIdAnnonce(idAnnonce);
        search.setForCode(code);
        search.setWhereKey("hasError");
        return (ALServiceLocator.getErreurAnnonceRafamModelService().count(search) > 0);
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.al.business.services.rafam.AnnoncesRafamErrorBusinessService#hasErrorOfOtherType(java.lang.String,
     * java.lang.String)
     */
    @Override
    public boolean hasErrorOfOtherType(String idAnnonce, String code)
            throws JadeApplicationException, JadePersistenceException {
        if (JadeNumericUtil.isEmptyOrZero(idAnnonce)) {
            throw new ALAnnonceRafamException(
                    "AnnoncesRafamErrorBusinessServiceImpl#hasErrorOfOtherType : idAnnonce is empty or zero");
        }

        ErreurAnnonceRafamSearchModel search = new ErreurAnnonceRafamSearchModel();
        search.setForIdAnnonce(idAnnonce);
        search.setForCode(code);
        search.setWhereKey("hasErrorOfOtherType");
        return (ALServiceLocator.getErreurAnnonceRafamModelService().count(search) > 0);
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.al.business.services.rafam.AnnoncesRafamErrorBusinessService#validateInsignificance(int)
     */
    @Override
    public void validateInsignificance(int nbDays) throws JadeApplicationException, JadePersistenceException {

        // recherche des annonces non validées avec conflit de moindre importance
        AnnonceRafamErrorComplexSearchModel searchInsignificance = new AnnonceRafamErrorComplexSearchModel();
        searchInsignificance.setForInsignificance(true);
        searchInsignificance.setForEtat(RafamEtatAnnonce.VALIDE.getCS());
        searchInsignificance
                .setForDateReception(
                        JadeDateUtil
                                .getGlobazFormattedDate(
                                        ALDateUtils
                                                .addJourDate(nbDays,
                                                        ALDateUtils.getCalendarDate(
                                                                JadeDateUtil.getGlobazFormattedDate(new Date())))
                                                .getTime()));
        searchInsignificance.setWhereKey("errInsignificance");
        searchInsignificance = ALImplServiceLocator.getAnnonceRafamErrorComplexModelService()
                .search(searchInsignificance);

        for (int i = 0; i < searchInsignificance.getSize(); i++) {
            AnnonceRafamErrorComplexModel annonce = (AnnonceRafamErrorComplexModel) searchInsignificance
                    .getSearchResults()[i];

            AnnonceRafamErrorComplexSearchModel searchSignificance = new AnnonceRafamErrorComplexSearchModel();
            searchSignificance.setForIdAnnonce(annonce.getId());
            searchSignificance.setForInsignificance(false);
            searchSignificance.setForEtat(RafamEtatAnnonce.VALIDE.getCS());
            searchSignificance.setWhereKey("errInsignificance");

            // si l'annonce en cours de traitement n'a pas d'autre erreur importante => validation
            if (0 == ALImplServiceLocator.getAnnonceRafamErrorComplexModelService().count(searchSignificance)) {
                ALImplServiceLocator.getAnnonceRafamBusinessService()
                        .validateForRecordNumber(annonce.getAnnonceRafamModel().getRecordNumber(), false);
            }
        }
    }
}
