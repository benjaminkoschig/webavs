package ch.globaz.al.businessimpl.services.rubriques.comptables;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALConstRubriques;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.services.rubriques.comptables.RubriquesComptablesCVCIService;
import ch.globaz.param.business.exceptions.ParamException;

/**
 * Implémentation du service spécifique permettant de récupérer une rubrique comptable pour la CVCI
 * 
 * @author jts
 * 
 */
public class RubriquesComptablesCVCIServiceImpl extends RubriquesComptablesServiceImpl implements
        RubriquesComptablesCVCIService {

    @Override
    protected String getRubriqueIndependant(DossierModel dossier, EntetePrestationModel entete,
            DetailPrestationModel detail, String date) throws JadeApplicationException, JadePersistenceException {

        String canton = getCanton(dossier, detail, date);

        if (JadeStringUtil.isBlank(canton)) {
            return super.getRubriqueIndependant(dossier, entete, detail, date);
        } else {
            try {
                return getRubrique(date,
                        (new StringBuffer(ALConstRubriques.RUBRIQUE_CAISSE_INDEPENDANT).append(".").append(canton))
                                .toString().toLowerCase());
            } catch (ParamException e) {
                return super.getRubriqueIndependant(dossier, entete, detail, date);
            } catch (JadePersistenceException e) {
                throw e;
            } catch (JadeApplicationException e) {
                throw e;
            }
        }

    }

    @Override
    protected String getRubriqueRestitution(DossierModel dossier, EntetePrestationModel entete,
            DetailPrestationModel detail, String date) throws JadePersistenceException, JadeApplicationException {

        String canton = getCanton(dossier, detail, date);

        try {
            if (ALCSDossier.ACTIVITE_NONACTIF.equals(dossier.getActiviteAllocataire())) {

                return getRubrique(date,
                        (new StringBuffer(ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_NON_ACTIF).append(".")
                                .append(canton)).toString().toLowerCase());

            } else if (ALCSDossier.ACTIVITE_INDEPENDANT.equals(dossier.getActiviteAllocataire())) {
                return getRubrique(date,
                        (new StringBuffer(ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_INDEPENDANT).append(".")
                                .append(canton)).toString().toLowerCase());

            } else if (ALCSDossier.ACTIVITE_SALARIE.equals(dossier.getActiviteAllocataire())) {
                return getRubrique(date,
                        (new StringBuffer(ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_SALARIE).append(".")
                                .append(canton)).toString().toLowerCase());

            }
        } catch (ParamException e) {
            // DO NOTHING : le paramètre n'a pas pu être récupéré. Tentative de récupérer la rubrique standard à l'étape
            // suivante.
        } catch (JadePersistenceException e) {
            throw e;
        } catch (JadeApplicationException e) {
            throw e;
        }

        // récupération d'une rubrique standard
        try {
            if (JadeStringUtil.isEmpty(canton)) {
                return super.getRubriqueRestitution(dossier, entete, detail, date);
            } else {
                return getRubrique(date,
                        (new StringBuffer(ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION).append(".").append(canton))
                                .toString().toLowerCase());
            }
        } catch (ParamException e) {
            return super.getRubriqueRestitution(dossier, entete, detail, date);
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

        if (JadeStringUtil.isBlank(canton)) {
            return super.getRubriqueSalarie(dossier, entete, detail, date);
        } else {
            try {
                return getRubrique(date,
                        (new StringBuffer(ALConstRubriques.RUBRIQUE_CAISSE_SALARIE).append(".").append(canton))
                                .toString().toLowerCase());
            } catch (ParamException e) {
                return super.getRubriqueSalarie(dossier, entete, detail, date);
            } catch (JadePersistenceException e) {
                throw e;
            } catch (JadeApplicationException e) {
                throw e;
            }
        }
    }
}
