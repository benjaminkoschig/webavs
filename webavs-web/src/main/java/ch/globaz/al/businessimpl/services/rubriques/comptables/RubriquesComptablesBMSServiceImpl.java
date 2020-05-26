package ch.globaz.al.businessimpl.services.rubriques.comptables;

import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.rubriques.comptables.RubriquesComptablesBMSService;
import ch.globaz.naos.business.data.AssuranceInfo;
import ch.globaz.param.business.models.ParameterModel;
import ch.globaz.param.business.models.ParameterSearchModel;
import ch.globaz.param.business.service.ParamServiceLocator;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.util.RubriqueUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RubriquesComptablesBMSServiceImpl extends RubriquesComptablesServiceImpl implements RubriquesComptablesBMSService {
    public static final String SEPARATOR = ".";

    public static final String RUBRIQUE_MULTICAISSE = "rubrique.multicaisse";
    public static final String RUBRIQUE_INDEPENDANT = "independant";
    public static final String RUBRIQUE_SALARIE = "salarie";
    public static final String RUBRIQUE_DEFAUT = "defaut";
    public static final String RUBRIQUE_NAISSANCE = "naissance";
    public static final String RUBRIQUE_IS = "is";
    public static final String RUBRIQUE_SUPPL = "suppl";
    public static final String RUBRIQUE_RESTITUTION = "restitution";

    public static final String REGEX_RUBRIQUE = "^rubrique.multicaisse.*";
    public static final String REGEX_RUBRIQUE_IS = "^rubrique.multicaisse.*.is$";

    public static final Pattern PATTERN_RUBRIQUE = Pattern.compile(REGEX_RUBRIQUE);
    public static final Pattern PATTERN_RUBRIQUE_IS = Pattern.compile(REGEX_RUBRIQUE_IS);

    @Override
    public String getRubriqueComptable(DossierModel dossier, EntetePrestationModel entete,
                                       DetailPrestationModel detail, String date) throws JadeApplicationException, JadePersistenceException {
        return getRubriqueFor(dossier, entete, detail, date);
    }

    @Override
    public String getRubriqueForIS(DossierComplexModel dossierComplex, String date) throws JadeApplicationException, JadePersistenceException {
        return getRubriqueForIS(getCodeCAF(dossierComplex.getDossierModel(), date), date);
    }

    /**
     * Retourne le code de la caisse AF à laquelle l'affiliation adhère (plan-caisse)
     *
     * @param dossier Dossier pour lequel le canton de l'affilié doit être récupéré
     * @param date    Date pour laquelle effectuer la récupération
     * @return Code de la caisse prof de l'affiliation
     * @throws JadePersistenceException Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *                                  faire
     * @throws JadeApplicationException Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private String getCodeCAF(DossierModel dossier, String date) throws JadeApplicationException,
            JadePersistenceException {
        AssuranceInfo assurance = ALServiceLocator.getAffiliationBusinessService().getAssuranceInfo(dossier, date);
        return assurance.getCodeCaisseProf();
    }

    public static Collection<String> getIdRubriquesAFIS() {
        Collection<String> idRubriques = new ArrayList<String>();
        ParameterSearchModel searchModel = new ParameterSearchModel();
        Collection<ParameterModel> parameters = RepositoryJade.searchForAndFetch(searchModel);
        for (ParameterModel parameterModel : parameters) {
            Matcher matcherRubriqueIS = PATTERN_RUBRIQUE_IS.matcher(parameterModel.getIdCleDiffere());
            if (matcherRubriqueIS.find()) {
                String idRubrique = RubriqueUtil.findIdRubriqueForIdExterne(parameterModel.getValeurAlphaParametre());
                idRubriques.add(idRubrique);
            }
        }
        return idRubriques;
    }

    public static Collection<String> getIdRubriquesAF(String caisseAF) {
        Collection<String> idRubriques = new ArrayList<String>();
        ParameterSearchModel searchModel = new ParameterSearchModel();
        Collection<ParameterModel> parameters = RepositoryJade.searchForAndFetch(searchModel);
        for (ParameterModel parameterModel : parameters) {
            String cle = parameterModel.getIdCleDiffere();
            Matcher matcherRubriqueIS = PATTERN_RUBRIQUE_IS.matcher(cle);
            Matcher matcherRubrique = PATTERN_RUBRIQUE.matcher(cle);
            if (matcherRubrique.matches() && !matcherRubriqueIS.matches()) {
                Administration admin = findCaisseAF(caisseAF);
                if (admin != null && !cle.contains(admin.getCodeAdministration())) {
                    continue;
                }
                String idRubrique = RubriqueUtil.findIdRubriqueForIdExterne(parameterModel.getValeurAlphaParametre());
                idRubriques.add(idRubrique);
            }
        }
        return idRubriques;
    }

    private static Administration findCaisseAF(String idTiers) {
        if (JadeStringUtil.isEmpty(idTiers)) {
            return null;
        }
        return VulpeculaRepositoryLocator.getAdministrationRepository().findById(idTiers);
    }

    public static String getRubriqueForIS(String caisseAF, String date)
            throws JadeApplicationException, JadePersistenceException {
        StringBuilder sb = new StringBuilder();
        sb.append(RUBRIQUE_MULTICAISSE);
        sb.append(SEPARATOR);
        sb.append(caisseAF);
        sb.append(SEPARATOR);
        sb.append(RUBRIQUE_IS);
        return (ParamServiceLocator.getParameterModelService().getParameterByName(ALConstParametres.APPNAME,
                sb.toString(), date)).getValeurAlphaParametre();
    }

    public static String getRubriqueFor(DossierModel dossier, EntetePrestationModel entete,
                                         DetailPrestationModel detail, String date) throws JadeApplicationException, JadePersistenceException {
        AssuranceInfo infos = ALServiceLocator.getAffiliationBusinessService().getAssuranceInfo(dossier, date);
        String caisseAF = infos.getCodeCaisseProf();
        String rubrique = getRubriqueFor(entete.getBonification(), caisseAF, dossier.getActiviteAllocataire(),
                detail.getTypePrestation(), dossier.getStatut(), date);
        return (ParamServiceLocator.getParameterModelService().getParameterByName(ALConstParametres.APPNAME, rubrique,
                date)).getValeurAlphaParametre();
    }

    public static String getRubriqueFor(String bonification, String caisseAF, String activiteAllocataire,
                                         String typeDroit, String statut, String date) {
        StringBuilder sb = new StringBuilder();
        sb.append(RUBRIQUE_MULTICAISSE);
        sb.append(SEPARATOR);
        sb.append(caisseAF);
        sb.append(SEPARATOR);
        if (ALCSPrestation.BONI_RESTITUTION.equals(bonification)) {
            return sb.append(RUBRIQUE_RESTITUTION).toString();
        } else {
            if (ALCSDossier.ACTIVITE_INDEPENDANT.equals(activiteAllocataire)) {
                sb.append(RUBRIQUE_INDEPENDANT).append(SEPARATOR);
            } else {
                sb.append(RUBRIQUE_SALARIE).append(SEPARATOR);
            }
            sb.append(getRubriquePartForTypeDroit(caisseAF, typeDroit, statut));
        }
        return sb.toString();
    }

    private static String getRubriquePartForTypeDroit(String caisseAF, String typeDroit, String statut) {
        StringBuilder sb = new StringBuilder();
        if (ALCSDroit.TYPE_NAIS.equals(typeDroit)) {
            if (ALCSDossier.STATUT_IS.equals(statut) || ALCSDossier.STATUT_CS.equals(statut)) {
                sb.append(RUBRIQUE_NAISSANCE).append(SEPARATOR).append(RUBRIQUE_SUPPL);
            } else {
                sb.append(RUBRIQUE_NAISSANCE);
            }
        } else {
            sb.append(RUBRIQUE_DEFAUT);
        }
        return sb.toString();
    }
}
