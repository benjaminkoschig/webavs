package ch.globaz.al.businessimpl.services.rafam;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.op.common.merge.IMergingContainer;
import globaz.webavs.common.CommonExcelmlContainer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.al.business.constantes.enumerations.RafamError;
import ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce;
import ch.globaz.al.business.constantes.enumerations.RafamReturnCode;
import ch.globaz.al.business.constantes.enumerations.RafamTypeAnnonce;
import ch.globaz.al.business.exceptions.document.ALDocumentException;
import ch.globaz.al.business.exceptions.rafam.ALRafamEnumException;
import ch.globaz.al.business.models.rafam.AnnonceRafamDelegueProtocoleComplexModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamDelegueProtocoleComplexSearchModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamErrorComplexModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamErrorComplexSearchModel;
import ch.globaz.al.business.services.rafam.AnnoncesRafamDelegueProtocoleService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALEncodingUtils;

public class AnnoncesRafamDelegueProtocoleServiceImpl extends AnnoncesRafamProtocoleServiceImpl implements
        AnnoncesRafamDelegueProtocoleService {

    @Override
    public String createProtocole(String idEmployeur) throws JadeApplicationException {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("idEmployeur", idEmployeur);

            return createDocAndSave(params);
        } catch (Exception e) {
            throw new ALDocumentException(
                    "AnnoncesRafamDelegueProtocoleServiceImpl#createProtocole : Unable to create the RAFam protocole : "
                            + e.getMessage(), e);
        }
    }

    @Override
    protected String getModelName() {
        // TODO: crée modèle pour af-delegue (sans id dossier par ex.)
        return "protocoleRAFam.xml";
    }

    @Override
    protected String getOutputName() {
        return "Protocole_RAFam_Delegue_" + JadeDateUtil.getGlobazFormattedDate(new Date());
    }

    /**
     * 
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    private AnnonceRafamDelegueProtocoleComplexSearchModel loadErreurs(String idEmployeur)
            throws JadeApplicationException, JadePersistenceException, JadeApplicationServiceNotAvailableException {
        AnnonceRafamDelegueProtocoleComplexSearchModel search = new AnnonceRafamDelegueProtocoleComplexSearchModel();

        ArrayList<String> types = new ArrayList<String>();
        types.add(RafamTypeAnnonce._68A_CREATION.getCode());
        types.add(RafamTypeAnnonce._68B_MUTATION.getCode());
        types.add(RafamTypeAnnonce._68C_ANNULATION.getCode());
        search.setInTypeAnnonce(types);

        ArrayList<String> returnCodes = new ArrayList<String>();
        returnCodes.add(RafamReturnCode.EN_ERREUR.getCode());
        returnCodes.add(RafamReturnCode.REJETEE.getCode());
        search.setInCodeRetour(returnCodes);
        search.setLikeInternalOffice("ED-" + idEmployeur);
        search.setForEtat(RafamEtatAnnonce.RECU.getCS());
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search.setWhereKey("protocoleErreurs");
        return ALImplServiceLocator.getAnnonceRafamDelegueProtocoleComplexModelService().search(search);
    }

    private AnnonceRafamDelegueProtocoleComplexSearchModel loadRappel(String idEmployeur)
            throws JadeApplicationException, JadePersistenceException {
        AnnonceRafamDelegueProtocoleComplexSearchModel search = new AnnonceRafamDelegueProtocoleComplexSearchModel();

        ArrayList<String> types = new ArrayList<String>();
        types.add(RafamTypeAnnonce._68A_CREATION.getCode());
        types.add(RafamTypeAnnonce._68B_MUTATION.getCode());
        types.add(RafamTypeAnnonce._68C_ANNULATION.getCode());
        search.setInTypeAnnonce(types);

        ArrayList<String> returnCodes = new ArrayList<String>();
        returnCodes.add(RafamReturnCode.RAPPEL.getCode());
        search.setInCodeRetour(returnCodes);
        search.setLikeInternalOffice("ED-" + idEmployeur);
        search.setForEtat(RafamEtatAnnonce.RECU.getCS());
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search.setWhereKey("protocoleErreurs");
        return ALImplServiceLocator.getAnnonceRafamDelegueProtocoleComplexModelService().search(search);
    }

    private AnnonceRafamDelegueProtocoleComplexSearchModel loadUPI(String idEmployeur) throws JadeApplicationException,
            JadePersistenceException {
        AnnonceRafamDelegueProtocoleComplexSearchModel search = new AnnonceRafamDelegueProtocoleComplexSearchModel();

        search.setForTypeAnnonce(RafamTypeAnnonce._69B_SYNCHRO_UPI.getCode());
        search.setForEtat(RafamEtatAnnonce.RECU.getCS());
        search.setMinRecordNumber(idEmployeur + "00000000000000");
        search.setMaxRecordNumber(idEmployeur + "99999999999999");
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search.setWhereKey("upi");
        return ALImplServiceLocator.getAnnonceRafamDelegueProtocoleComplexModelService().search(search);
    }

    protected CommonExcelmlContainer populateDelegue(CommonExcelmlContainer container,
            AnnonceRafamDelegueProtocoleComplexSearchModel search, String tag) throws JadeApplicationException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {

        for (JadeAbstractModel annonces : search.getSearchResults()) {
            AnnonceRafamDelegueProtocoleComplexModel annonce = (AnnonceRafamDelegueProtocoleComplexModel) annonces;

            container.put("VALUE_" + tag + "_NSS_ENF", annonce.getNssEnfant());
            container
                    .put("VALUE_" + tag + "_NOM_ENF", ALEncodingUtils.checkAndReplaceByWilcard(annonce.getNomEnfant()));
            container.put("VALUE_" + tag + "_PRENOM_ENF",
                    ALEncodingUtils.checkAndReplaceByWilcard(annonce.getPrenomEnfant()));
            container.put("VALUE_" + tag + "_NAISSANCE_ENF", annonce.getNaissanceEnfant());
            container.put("VALUE_" + tag + "_NO_DOSSIER", "-");
            container.put("VALUE_" + tag + "_RECORD_NUMBER", annonce.getRecordNumber());
            container.put("VALUE_" + tag + "_NO_ANNONCE", annonce.getIdAnnonce());
            container.put("VALUE_" + tag + "_NOM_ALLOC",
                    ALEncodingUtils.checkAndReplaceByWilcard(annonce.getNomAllocataire()));
            container.put("VALUE_" + tag + "_PRENOM_ALLOC",
                    ALEncodingUtils.checkAndReplaceByWilcard(annonce.getPrenomAllocataire()));

            AnnonceRafamErrorComplexSearchModel searchErr = new AnnonceRafamErrorComplexSearchModel();
            searchErr.setForIdAnnonce(annonce.getIdAnnonce());
            searchErr = ALImplServiceLocator.getAnnonceRafamErrorComplexModelService().search(searchErr);

            StringBuffer code = new StringBuffer(searchErr.getSize() > 1 ? "(+)" : "");
            StringBuffer libelle = new StringBuffer();
            StringBuffer periode = new StringBuffer();
            StringBuffer office = new StringBuffer();

            for (JadeAbstractModel erreurs : searchErr.getSearchResults()) {
                AnnonceRafamErrorComplexModel erreur = (AnnonceRafamErrorComplexModel) erreurs;

                code.append(erreur.getErreurAnnonceRafamModel().getCode()).append("\n");

                try {
                    libelle.append(
                            JadeCodesSystemsUtil.getCodeLibelle(RafamError.getRafamError(
                                    erreur.getErreurAnnonceRafamModel().getCode()).getCS())).append("\n");
                } catch (ALRafamEnumException e1) {
                    libelle.append(
                            JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                                    "al.enum.rafam.errorCode.codeInconnu")).append("\n");
                }

                if (!JadeStringUtil.isBlank(erreur.getOverlapInformationModel().getOverlapPeriodeStart())) {
                    periode.append(erreur.getOverlapInformationModel().getOverlapPeriodeStart()).append(" - ")
                            .append(erreur.getOverlapInformationModel().getOverlapPeriodeEnd()).append("\n");
                    office.append(erreur.getOverlapInformationModel().getOfficeIdentifier()).append(".")
                            .append(erreur.getOverlapInformationModel().getBranch()).append("\n");
                }
            }

            container.put("VALUE_" + tag + "_CODE_ERREUR", code.toString().trim());
            container.put("VALUE_" + tag + "_ERREUR", libelle.toString().trim());
            container.put("VALUE_" + tag + "_PERIODE_ERREUR", periode.toString().trim());
            container.put("VALUE_" + tag + "_OFFICE", office.toString().trim());
        }

        return container;

    }

    @Override
    protected IMergingContainer prepareData(Map<String, Object> parameters)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {

        String idEmployeur = (String) parameters.get("idEmployeur");
        CommonExcelmlContainer container = new CommonExcelmlContainer();
        container = populateDelegue(container, loadErreurs(idEmployeur), AnnoncesRafamProtocoleServiceImpl.tagErreur);
        container = populateDelegue(container, loadRappel(idEmployeur), AnnoncesRafamProtocoleServiceImpl.tagRappel);
        container = populateDelegue(container, loadUPI(idEmployeur), AnnoncesRafamProtocoleServiceImpl.tagUPI);
        return container;

    }

}
