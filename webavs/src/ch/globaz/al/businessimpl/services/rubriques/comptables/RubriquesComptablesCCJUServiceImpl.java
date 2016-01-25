package ch.globaz.al.businessimpl.services.rubriques.comptables;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.constantes.ALConstRubriques;
import ch.globaz.al.business.exceptions.rubriques.ALRubriquesException;
import ch.globaz.al.business.models.allocataire.AgricoleModel;
import ch.globaz.al.business.models.allocataire.AgricoleSearchModel;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.models.tarif.CategorieTarifComplexModel;
import ch.globaz.al.business.services.rubriques.comptables.RubriquesComptablesCCJUService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Implémentation du service spécifique permettant de récupérer une rubrique comptable pour la CCJU
 * 
 * @author jts
 * 
 */
public class RubriquesComptablesCCJUServiceImpl extends RubriquesComptablesServiceImpl implements
        RubriquesComptablesCCJUService {

    /**
     * Récupère la rubrique ADI
     * 
     * @return Constante correspondant à la rubrique
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    @Override
    protected String getRubriqueADI(DossierModel dossier, EntetePrestationModel entete, DetailPrestationModel detail,
            String date) throws JadePersistenceException, JadeApplicationException {
        return getRubrique(date, ALConstRubriques.RUBRIQUE_STANDARD_SALARIE);
    }

    @Override
    protected String getRubriqueAgriculteur(DossierModel dossier, EntetePrestationModel entete,
            DetailPrestationModel detail, String date) throws JadeApplicationException, JadePersistenceException {

        CategorieTarifComplexModel categorieTarif = getCategorieTarif(detail.getCategorieTarif());

        if (ALCSTarif.LEGISLATION_AGRICOLE.equals(categorieTarif.getLegislationTarifModel().getTypeLegislation())) {
            return super.getRubriqueAgriculteur(dossier, entete, detail, date);
        } else if (ALCSDroit.TYPE_MEN.equals(detail.getTypePrestation())
                && ALCSTarif.CATEGORIE_LJU.equals(categorieTarif.getCategorieTarifModel().getCategorieTarif())) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_AGRICULTEUR_MENAGE_LJU);
        } else {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_AGRICULTEUR);
        }
    }

    @Override
    protected String getRubriqueCollaborateurAgricole(DossierModel dossier, EntetePrestationModel entete,
            DetailPrestationModel detail, String date) throws JadeApplicationException, JadePersistenceException {

        CategorieTarifComplexModel categorieTarif = getCategorieTarif(detail.getCategorieTarif());

        if (ALCSTarif.CATEGORIE_LFM.equals(categorieTarif.getCategorieTarifModel().getCategorieTarif())
                || ALCSTarif.CATEGORIE_LFM13.equals(categorieTarif.getCategorieTarifModel().getCategorieTarif())
                || ALCSTarif.CATEGORIE_LFM23.equals(categorieTarif.getCategorieTarifModel().getCategorieTarif())
                || ALCSTarif.CATEGORIE_LFP.equals(categorieTarif.getCategorieTarifModel().getCategorieTarif())
                || ALCSTarif.CATEGORIE_LFP13.equals(categorieTarif.getCategorieTarifModel().getCategorieTarif())
                || ALCSTarif.CATEGORIE_LFP23.equals(categorieTarif.getCategorieTarifModel().getCategorieTarif())) {

            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_COLLABORATEUR_LF);
        } else {
            return super.getRubriqueCollaborateurAgricole(dossier, entete, detail, date);
        }
    }

    @Override
    protected String getRubriqueRestitution(DossierModel dossier, EntetePrestationModel entete,
            DetailPrestationModel detail, String date) throws JadeApplicationException, JadePersistenceException {

        CategorieTarifComplexModel categorieTarif = getCategorieTarif(detail.getCategorieTarif());

        if (ALCSTarif.LEGISLATION_AGRICOLE.equals(categorieTarif.getLegislationTarifModel().getTypeLegislation())) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_LOI_FEDERALE);
        } else if (ALCSTarif.CATEGORIE_LJU.equals(categorieTarif.getCategorieTarifModel().getCategorieTarif())) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_LOI_JU);
        } else if (ALCSDossier.ACTIVITE_NONACTIF.equals(dossier.getActiviteAllocataire())) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_NON_ACTIF);
        } else {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_STANDARD_RESTITUTION);
        }
    }

    @Override
    protected String getRubriqueTravailleurAgricole(DossierModel dossier, EntetePrestationModel entete,
            DetailPrestationModel detail, String date) throws JadeApplicationException, JadePersistenceException {

        CategorieTarifComplexModel categorieTarif = getCategorieTarif(detail.getCategorieTarif());

        AgricoleSearchModel searchModel = new AgricoleSearchModel();
        searchModel.setForIdAllocataire(dossier.getIdAllocataire());
        ALImplServiceLocator.getAgricoleModelService().search(searchModel);

        // législation agricole
        if (ALCSTarif.LEGISLATION_AGRICOLE.equals(categorieTarif.getLegislationTarifModel().getTypeLegislation())) {

            AgricoleModel agricole = null;

            if (searchModel.getSize() > 1) {
                throw new ALRubriquesException(
                        "RubriquesComptablesCCJUServiceImpl#getRubriqueTravailleurAgricole : several agricole found");
            } else if (searchModel.getSize() == 0) {
                throw new ALRubriquesException(
                        "RubriquesComptablesCCJUServiceImpl#getRubriqueTravailleurAgricole : no agricole found");
            } else {
                agricole = ((AgricoleModel) searchModel.getSearchResults()[0]);
            }

            // prestation de ménage
            if (ALCSDroit.TYPE_MEN.equals(detail.getTypePrestation())) {

                // domaine
                if (agricole.getDomaineMontagne().booleanValue()) {
                    return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_TRAVAILLEUR_AGRICOLE_MENAGE_MONTAGNE);
                } else {
                    return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_TRAVAILLEUR_AGRICOLE_MENAGE_PLAINE);
                }
            } else if (agricole.getDomaineMontagne().booleanValue()) {
                return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_TRAVAILLEUR_AGRICOLE_MONTAGNE);

            } else {
                return super.getRubriqueTravailleurAgricole(dossier, entete, detail, date);
            }
        } else if (ALCSTarif.CATEGORIE_LJU.equals(detail.getCategorieTarif())) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_TRAVAILLEUR_AGRICOLE_MENAGE_LJU);
        } else {
            throw new ALRubriquesException(
                    "RubriquesComptablesCCJUServiceImpl#getRubriqueTravailleurAgricole : unable to found the right rubrique");
        }
    }
}
