package ch.globaz.al.businessimpl.services.horloger;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.Date;
import java.util.HashSet;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.al.business.exceptions.generation.prestations.ALGenerationException;
import ch.globaz.al.business.exceptions.horloger.ALHorlogerException;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.droit.EnfantComplexModel;
import ch.globaz.al.business.models.tarif.TarifComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.horloger.HorlogerBusinessService;
import ch.globaz.al.business.services.models.tarif.TarifComplexModelService;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextTucana;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.naos.business.data.AssuranceInfo;
import ch.globaz.param.business.models.ParameterModel;
import ch.globaz.param.business.service.ParamServiceLocator;

/**
 * 
 * @author jts
 * 
 */
public class HorlogerBusinessServiceImpl extends ALAbstractBusinessServiceImpl implements HorlogerBusinessService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.horloger.HorlogerBusinessService#cantonVerseNaissance(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public boolean cantonVerseNaissance(String canton, String typePrest, String typeResident, String date)
            throws JadeApplicationException, JadePersistenceException {

        if (JadeStringUtil.isBlank(canton)) {
            throw new ALHorlogerException("HorlogerBusinessServiceImpl#cantonVerseNaissance : canton is null or empty");
        }

        if ((!ALCSDroit.TYPE_NAIS.equals(typePrest) && !ALCSDroit.TYPE_ACCE.equals(typePrest))) {
            throw new ALHorlogerException(
                    "HorlogerBusinessServiceImpl#cantonVerseNaissance : typePrest has not a valid value '" + typePrest
                            + "'");
        }

        if (JadeStringUtil.isBlank(typeResident)) {
            throw new ALHorlogerException(
                    "HorlogerBusinessServiceImpl#cantonVerseNaissance : typeResident is null or empty");
        }

        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALHorlogerException(
                    "HorlogerBusinessServiceImpl#cantonVerseNaissance : date has not a valid value '" + date + "'");
        }

        TarifComplexSearchModel tarifs = new TarifComplexSearchModel();

        // législation
        HashSet<String> legislation = new HashSet<String>();
        legislation.add(ALCSTarif.LEGISLATION_CANTONAL);
        tarifs.setInLegislations(legislation);

        // catégories de tarif
        HashSet<String> categorie = new HashSet<String>();
        categorie.add(ALImplServiceLocator.getCalculService().getTarifForCanton(canton));
        tarifs.setInCategoriesTarif(categorie);

        // type résident
        tarifs.setForCategorieResident(typeResident);
        // type prestation
        tarifs.setForTypePrestation(typePrest);
        // validité
        tarifs.setForValidite(date);

        TarifComplexModelService tarifsService = ALImplServiceLocator.getTarifComplexModelService();
        tarifs = tarifsService.search(tarifs);

        return tarifs.getSize() > 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.horloger.HorlogerBusinessService#isCaisseHorlogere()
     */
    @Override
    public boolean isCaisseHorlogere() throws JadeApplicationException, JadePersistenceException {

        ParameterModel param = ParamServiceLocator.getParameterModelService().getParameterByName(
                ALConstParametres.APPNAME, ALConstParametres.TUCANA_IS_ENABLED,
                JadeDateUtil.getGlobazFormattedDate(new Date()));

        if ("0".equals(param.getValeurAlphaParametre())) {
            return false;
        } else if ("1".equals(param.getValeurAlphaParametre())) {
            return true;
        } else {
            throw new ALGenerationException(
                    "HorlogerBusinessServiceImpl#isCaisseHorlogere : Parameter has not a valid value");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.horloger.HorlogerBusinessService#isNaissanceHorlogere(ch.globaz.al.business.models
     * .dossier.DossierComplexModel, ch.globaz.al.business.models.droit.EnfantComplexModel)
     */
    @Override
    public boolean isNaissanceHorlogere(DossierComplexModel dossier, EnfantComplexModel enfant)
            throws JadeApplicationException, JadePersistenceException {

        if (dossier == null) {
            throw new ALHorlogerException("HorlogerBusinessServiceImpl#isNaissanceHorlogere : dossier is null");
        }

        if (enfant == null) {
            throw new ALHorlogerException("HorlogerBusinessServiceImpl#isNaissanceHorlogere : enfant is null");
        }

        if (!ALCSDroit.NAISSANCE_TYPE_ACCE.equals(enfant.getEnfantModel().getTypeAllocationNaissance())
                && !ALCSDroit.NAISSANCE_TYPE_NAIS.equals(enfant.getEnfantModel().getTypeAllocationNaissance())
                && !ALCSDroit.NAISSANCE_TYPE_AUCUNE.equals(enfant.getEnfantModel().getTypeAllocationNaissance())) {
            throw new ALHorlogerException(
                    "HorlogerBusinessServiceImpl#isNaissanceHorlogere : enfant has not any NAIS or ACCE prestation");
        }

        // si aucune allocation n'est versée
        if (ALCSDroit.NAISSANCE_TYPE_AUCUNE.equals(enfant.getEnfantModel().getTypeAllocationNaissance())) {
            return false;
        } else {

            ContextTucana.initContext(JadeDateUtil.getGlobazFormattedDate(new Date(JadeDateUtil.now())));

            if (ContextTucana.tucanaIsEnabled()) {

                // récupération des infos de l'affilié
                AssuranceInfo info = ALServiceLocator.getAffiliationBusinessService().getAssuranceInfo(
                        dossier.getDossierModel(), JadeDateUtil.getGlobazFormattedDate(new Date())

                );
                String cantonAff = ALImplServiceLocator.getAffiliationService().convertCantonNaos2CantonAF(
                        info.getCanton());

                // vérification du versement de l'allocation par le canton
                return !cantonVerseNaissance(
                        cantonAff,
                        (ALCSDroit.NAISSANCE_TYPE_NAIS.equals(enfant.getEnfantModel().getTypeAllocationNaissance()) ? ALCSDroit.TYPE_NAIS
                                : ALCSDroit.TYPE_ACCE),
                        ALServiceLocator.getAllocataireBusinessService().getTypeResident(
                                dossier.getAllocataireComplexModel().getAllocataireModel()), enfant
                                .getPersonneEtendueComplexModel().getPersonne().getDateNaissance());
            } else {
                return false;
            }
        }
    }
}
