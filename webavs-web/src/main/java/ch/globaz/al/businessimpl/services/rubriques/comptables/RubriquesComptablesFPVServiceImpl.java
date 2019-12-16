package ch.globaz.al.businessimpl.services.rubriques.comptables;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALConstRubriques;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.rubriques.comptables.RubriquesComptablesFPVService;
import ch.globaz.naos.business.data.AssuranceInfo;
import ch.globaz.param.business.exceptions.ParamException;

/**
 * Impl�mentation du service sp�cifique permettant de r�cup�rer une rubrique comptable pour la FPV
 * 
 * @author gmo
 * 
 */
public class RubriquesComptablesFPVServiceImpl extends RubriquesComptablesServiceImpl implements
        RubriquesComptablesFPVService {

    /**
     * Retourne le code de la caisse AF � laquelle l'affiliation adh�re (plan-caisse)
     * 
     * @param dossier
     *            Dossier pour lequel le canton de l'affili� doit �tre r�cup�r�
     * @param date
     *            Date pour laquelle effectuer la r�cup�ration
     * @return Code de la caisse prof de l'affiliation
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
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
            // le param�tre n'a pas pu �tre r�cup�r�. Tentative de r�cup�rer la rubrique sans l'information
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
            // le param�tre n'a pas pu �tre r�cup�r�. Tentative de r�cup�rer la rubrique sans l'information
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
    public String getRubriqueForIS(DossierModel dossier, DetailPrestationModel detail, String date) throws JadeApplicationException, JadePersistenceException {
        String canton = getCanton(dossier, detail, date);
        String codeCaisse = getCodeCAF(dossier, date);
        StringBuilder rubrique = new StringBuilder();

        if (ALCSDossier.ACTIVITE_INDEPENDANT.equals(dossier.getActiviteAllocataire())) {
            rubrique.append(ALConstRubriques.RUBRIQUE_CAISSE_INDEPENDANT_IMPOT_SOURCE);
        } else if (ALCSDossier.ACTIVITE_SALARIE.equals(dossier.getActiviteAllocataire())) {
            rubrique.append(ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_IMPOT_SOURCE);
        } else {
            return rubrique.toString();
        }
        rubrique.replace(21,30, codeCaisse);
        rubrique.append(".").append(canton);
        return getRubrique(date,rubrique.toString().toLowerCase());
    }
}
