package ch.globaz.al.businessimpl.services.rubriques.comptables;

import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALConstRubriques;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.rubriques.comptables.RubriquesComptablesFPVService;
import ch.globaz.al.exception.RubriqueComptableNotFoundException;
import ch.globaz.naos.business.data.AssuranceInfo;
import ch.globaz.param.business.exceptions.ParamException;
import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation du service spécifique permettant de récupérer une rubrique comptable pour la FPV
 *
 * @author gmo
 */
public class RubriquesComptablesFPVServiceImpl extends RubriquesComptablesServiceImpl implements
        RubriquesComptablesFPVService {

    private static final String[] ALL_CANTONS = {"ag", "ai", "ar", "be", "bl", "bs", "fr", "ge", "gl", "gr", "ju", "lu", "ne", "nw", "ow", "sg", "sh", "so", "sz", "tg", "ti", "ur", "vd", "vs", "zg", "zh"};
    private static final String[] PREMIERE_CAISSE_CANTONS = {"vd", "be", "ne", "zh"};
    private static final String[] DEUXIEME_CAISSE_CANTONS = {"vd", "zh"};

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
        if (RubriquesComptablesFPVService.forceCodeCAF == null) {
            AssuranceInfo assurance = ALServiceLocator.getAffiliationBusinessService().getAssuranceInfo(dossier, date);
            return assurance.getCodeCaisseProf();
        } else {
            return RubriquesComptablesFPVService.forceCodeCAF;
        }
    }

    @Override
    protected String getRubriqueIndependant(DossierModel dossier, EntetePrestationModel entete,
                                            DetailPrestationModel detail, String date) throws JadeApplicationException, JadePersistenceException {

        String codeCaisse = getCodeCAF(dossier, date);
        String canton = getCanton(dossier, detail, date);
        String rubriqueKey = "";

        if (ALCSPrestation.STATUT_ADI.equals(entete.getStatut())) {
            rubriqueKey = ALConstRubriques.RUBRIQUE_MULTICAISSE_INDEPENDANT_ADI;
        } else if (ALCSDroit.TYPE_ACCE.equals(detail.getTypePrestation())
                || ALCSDroit.TYPE_NAIS.equals(detail.getTypePrestation())) {
            rubriqueKey = ALConstRubriques.RUBRIQUE_MULTICAISSE_INDEPENDANT_NAIS_ACCE;
        } else if (ALCSDroit.TYPE_ENF.equals(detail.getTypePrestation())) {
            rubriqueKey = ALConstRubriques.RUBRIQUE_MULTICAISSE_INDEPENDANT_ENF;
        } else if (ALCSDroit.TYPE_FORM.equals(detail.getTypePrestation())) {
            rubriqueKey = ALConstRubriques.RUBRIQUE_MULTICAISSE_INDEPENDANT_FORM;
        }

        String rubrique = "";
        rubriqueKey = rubriqueKey.replace(ALConstRubriques.RUBRIQUE_MULTICAISSE_CODE_PATTERN, codeCaisse);

        try {
            rubrique = getRubrique(date, rubriqueKey + "." + canton.toLowerCase());
            return rubrique;
        } catch (ParamException e) {
            // le paramètre n'a pas pu être récupéré. Tentative de récupérer la rubrique sans l'information
            // du canton
            return getRubrique(date, rubriqueKey);
        } catch (JadePersistenceException e) {
            throw e;
        } catch (JadeApplicationException e) {
            throw e;
        }
    }

    @Override
    protected String getRubriqueRestitution(DossierModel dossier, EntetePrestationModel entete,
                                            DetailPrestationModel detail, String date) throws JadePersistenceException, JadeApplicationException {

        String canton = getCanton(dossier, detail, date);
        String codeCaisse = getCodeCAF(dossier, date);
        String rubrique = "";

        if (ALCSDossier.ACTIVITE_INDEPENDANT.equals(dossier.getActiviteAllocataire())) {
            rubrique = getRubriqueRestitutionIndependant(canton, codeCaisse, date);
            return rubrique;
        } else if (ALCSDossier.ACTIVITE_SALARIE.equals(dossier.getActiviteAllocataire())) {
            rubrique = ALConstRubriques.RUBRIQUE_MULTICAISSE_SALARIE_RESTITUTION.replace(
                    ALConstRubriques.RUBRIQUE_MULTICAISSE_CODE_PATTERN, codeCaisse);
            rubrique = (new StringBuffer(rubrique).append(".").append(canton)).toString().toLowerCase();
        }

        return getRubrique(date, rubrique);
    }

    private String getRubriqueRestitutionIndependant(String canton, String codeCaisse, String date)
            throws JadeApplicationException, JadePersistenceException {

        String rubriqueKey = ALConstRubriques.RUBRIQUE_MULTICAISSE_INDEPENDANT_RESTITUTION.replace(
                ALConstRubriques.RUBRIQUE_MULTICAISSE_CODE_PATTERN, codeCaisse);

        try {
            return getRubrique(date, rubriqueKey + "." + canton.toLowerCase());
        } catch (ParamException e) {
            // le paramètre n'a pas pu être récupéré. Tentative de récupérer la rubrique sans l'information
            // du canton
            return getRubrique(date, rubriqueKey);
        } catch (JadePersistenceException e) {
            throw e;
        } catch (JadeApplicationException e) {
            throw e;
        }
    }

    @Override
    protected String getRubriqueSalarie(DossierModel dossier, EntetePrestationModel entete,
                                        DetailPrestationModel detail, String date) throws JadeApplicationException, JadePersistenceException {
        String canton = getCanton(dossier, detail, date);
        String codeCaisse = getCodeCAF(dossier, date);
        String rubrique = "";

        if (ALCSPrestation.STATUT_ADI.equals(entete.getStatut())) {
            rubrique = ALConstRubriques.RUBRIQUE_MULTICAISSE_SALARIE_ADI;
        } else if (ALCSDroit.TYPE_ACCE.equals(detail.getTypePrestation())
                || ALCSDroit.TYPE_NAIS.equals(detail.getTypePrestation())) {
            rubrique = ALConstRubriques.RUBRIQUE_MULTICAISSE_SALARIE_NAIS_ACCE;
        } else if (ALCSDroit.TYPE_ENF.equals(detail.getTypePrestation())) {
            rubrique = ALConstRubriques.RUBRIQUE_MULTICAISSE_SALARIE_ENF;
        } else if (ALCSDroit.TYPE_FORM.equals(detail.getTypePrestation())) {
            rubrique = ALConstRubriques.RUBRIQUE_MULTICAISSE_SALARIE_FORM;
        }

        rubrique = rubrique.replace(ALConstRubriques.RUBRIQUE_MULTICAISSE_CODE_PATTERN, codeCaisse);

        return getRubrique(date, (new StringBuffer(rubrique).append(".").append(canton)).toString().toLowerCase());

    }

    @Override
    public String getRubriqueForIS(DossierModel dossier, String cantonImposition, String date) throws JadeApplicationException, JadePersistenceException {
        String codeCaisse = getCodeCAF(dossier, date);
        StringBuilder rubrique = new StringBuilder();

        if (ALCSDossier.ACTIVITE_INDEPENDANT.equals(dossier.getActiviteAllocataire())) {
            rubrique.append(ALConstRubriques.RUBRIQUE_CAISSE_INDEPENDANT_IMPOT_SOURCE);
        } else if (ALCSDossier.ACTIVITE_SALARIE.equals(dossier.getActiviteAllocataire())) {
            rubrique.append(ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_IMPOT_SOURCE);
        } else {
            return rubrique.toString();
        }
        rubrique.replace(21, 30, codeCaisse);
        if (ALCSDossier.ACTIVITE_SALARIE.equals(dossier.getActiviteAllocataire()) || StringUtils.equals(codeCaisse, "20")) {
            rubrique.append(".").append(JadeCodesSystemsUtil.getCode(cantonImposition));
        }
        String result = getRubrique(date, rubrique.toString().toLowerCase());
        if (StringUtils.isNotEmpty(result)){
            return result;
        } else {
            throw new RubriqueComptableNotFoundException("La prestation ne peut pas être générée, la rubrique comptable doit être définie pour l'impôt à la source");
        }
    }

    @Override
    public List<String> getAllRubriquesForIS(String caisse, String date) throws JadeApplicationException, JadePersistenceException {
        List<String> rubriques = new ArrayList<>();

        // Récupération des rubriques indépendantes
        StringBuilder rubrique = new StringBuilder();
        switch (caisse) {
            case "2":
            case "6":
                break;
            case "20":
                for (String eachCanton : ALL_CANTONS) {
                    addRubriqueIndependant(caisse, date, rubriques, eachCanton);
                }
                break;
            default:
                rubrique.append(ALConstRubriques.RUBRIQUE_CAISSE_INDEPENDANT_IMPOT_SOURCE);
                rubrique.replace(21, 30, caisse);
                String independantRubrique = getRubrique(date, rubrique.toString().toLowerCase());
                if (StringUtils.isNotEmpty(independantRubrique)) {
                    rubriques.add(independantRubrique);
                }
                break;
        }


        // Récupération des rubriques salariées
        switch (caisse) {
            case "1":
                for (String eachCanton : PREMIERE_CAISSE_CANTONS) {
                    addRubriqueSalarie(caisse, date, rubriques, eachCanton);
                }
                break;
            case "2":
                for (String eachCanton : DEUXIEME_CAISSE_CANTONS) {
                    addRubriqueSalarie(caisse, date, rubriques, eachCanton);
                }
                break;
            case "20":
                for (String eachCanton : ALL_CANTONS) {
                    addRubriqueSalarie(caisse, date, rubriques, eachCanton);
                }
                break;
            default:
                addRubriqueSalarie(caisse, date, rubriques, "vd");
                break;
        }
        return rubriques;
    }

    private void addRubriqueSalarie(String caisse, String date, List<String> rubriques, String eachCanton) throws JadePersistenceException, JadeApplicationException {
        StringBuilder rubrique;
        rubrique = new StringBuilder();
        rubrique.append(ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_IMPOT_SOURCE);
        rubrique.replace(21, 30, caisse);
        rubrique.append(".").append(eachCanton);
        String salarieRubrique = getRubrique(date, rubrique.toString().toLowerCase());
        if (StringUtils.isNotEmpty(salarieRubrique)) {
            rubriques.add(salarieRubrique);
        }
    }

    private void addRubriqueIndependant(String caisse, String date, List<String> rubriques, String eachCanton) throws JadePersistenceException, JadeApplicationException {
        StringBuilder rubrique;
        rubrique = new StringBuilder();
        rubrique.append(ALConstRubriques.RUBRIQUE_CAISSE_INDEPENDANT_IMPOT_SOURCE);
        rubrique.replace(21, 30, caisse);
        rubrique.append(".").append(eachCanton);
        String salarieRubrique = getRubrique(date, rubrique.toString().toLowerCase());
        if (StringUtils.isNotEmpty(salarieRubrique)) {
            rubriques.add(salarieRubrique);
        }
    }
}
