package ch.globaz.al.businessimpl.services.languesAllocAffilies;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.HashMap;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.constantes.ALConstLangue;
import ch.globaz.al.business.exceptions.languesAllocAffilie.ALLangueAllocAffilieException;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.languesAllocAffilies.LangueAllocAffilieService;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * Implémentation du service de la langue de l'affilié et de l'allocataire
 * 
 * @author PTA
 * 
 */

public class LangueAllocAffilieServiceImpl implements LangueAllocAffilieService {

    /**
     * Méthode qui retourne le code iso de la langue passée en paramètre (code sytème de la langue d'un tiers tiers)
     * 
     * @param langue
     * @return le code iso de la langue
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private String langueCodeIso(String langue) throws JadeApplicationException, JadePersistenceException {
        // Contrôle du paramètre
        if (!JadeStringUtil.equals(ALCSTiers.LANGUE_ALLEMAND, langue, false)
                && !JadeStringUtil.equals(ALCSTiers.LANGUE_FRANCAIS, langue, false)
                && !JadeStringUtil.equals(ALCSTiers.LANGUE_ITALIEN, langue, false)) {

            throw new ALLangueAllocAffilieException("LangueAllocAffilieServiceImpl#langueEgal : langueCodeIso "
                    + langue + " is not valid");
        }

        HashMap<String, String> listLangueCodeIso = new HashMap<String, String>();

        listLangueCodeIso.put(ALCSTiers.LANGUE_ALLEMAND, ALConstLangue.LANGUE_ISO_ALLEMAND);
        listLangueCodeIso.put(ALCSTiers.LANGUE_ITALIEN, ALConstLangue.LANGUE_ISO_ITALIEN);
        listLangueCodeIso.put(ALCSTiers.LANGUE_FRANCAIS, ALConstLangue.LANGUE_ISO_FRANCAIS);
        return listLangueCodeIso.get(langue);
    }

    /**
     * Méthode qui retourne true si langue est FR, DE et IT, sinon false
     * 
     * @param langue
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private boolean langueEgal(String langueTiers) throws JadeApplicationException, JadePersistenceException {
        // contrôle du paramètre
        if (JadeStringUtil.isEmpty(langueTiers)) {
            throw new ALLangueAllocAffilieException("LangueAllocAffilieServiceImpl#langueEgal : language is empty");
        }
        if (JadeStringUtil.equals(langueTiers, ALCSTiers.LANGUE_FRANCAIS, false)
                || JadeStringUtil.equals(langueTiers, ALCSTiers.LANGUE_ALLEMAND, false)
                || JadeStringUtil.equals(langueTiers, ALCSTiers.LANGUE_ITALIEN, false)) {

            return true;

        } else {

            return false;
        }
    }

    @Override
    public HashMap<String, String> languesAffilieAlloc(String idTiersAlloc, String numAffilie)
            throws JadeApplicationException, JadePersistenceException {
        // Contrôle des paramètres
        if (JadeNumericUtil.isEmptyOrZero(idTiersAlloc)) {
            throw new ALLangueAllocAffilieException(
                    "LangueAllocAffilieServiceImpl#languesAffilieAlloc : idTiersAlloc is empty or value is null");
        }
        if (JadeStringUtil.isEmpty(numAffilie)) {
            throw new ALLangueAllocAffilieException(
                    "LangueAllocAffilieServiceImpl#languesAffilieAlloc : numAffilie is empty or value is null");
        }

        HashMap<String, String> languesAllocAffilie = new HashMap<String, String>();

        // ajout de la langue de l'allocataire à la hashmap
        languesAllocAffilie.put(ALConstDocument.LANGUE_ALLOC, ALServiceLocator.getLangueAllocAffilieService()
                .langueTiersAlloc(idTiersAlloc, numAffilie));

        // ajout de la langue de l'affilié à hashmap
        languesAllocAffilie.put(ALConstDocument.LANGUE_AFFIL, ALServiceLocator.getLangueAllocAffilieService()
                .langueTiersAffilie(numAffilie));

        return languesAllocAffilie;
    }

    @Override
    public String langueTiers(String idTiers) throws JadeApplicationException, JadePersistenceException {
        // contrôle des paramêtres
        if (JadeNumericUtil.isEmptyOrZero(idTiers)) {
            throw new ALLangueAllocAffilieException(
                    "LangueAllocAffilieServiceImpl#langueTiersAlloc : idTiers is empty or value is null");
        }

        String langueTiers = TIBusinessServiceLocator.getPersonneEtendueService().read(idTiers).getTiers().getLangue();

        return langueTiers;
    }

    @Override
    public String langueTiersAffilie(String numAffilie) throws JadeApplicationException, JadePersistenceException {

        if (JadeStringUtil.isEmpty(numAffilie)) {
            throw new ALLangueAllocAffilieException(
                    "LangueAllocAffilieServiceImpl#langueTiersAffilie : numAffilie is empty or value is null");
        }

        //

        // rcherche du tiers affilie
        String tiersAffilie = AFBusinessServiceLocator.getAffiliationService().findIdTiersForNumeroAffilie(numAffilie);

        if (JadeStringUtil.isEmpty(tiersAffilie)) {
            throw new ALLangueAllocAffilieException(
                    "LangueAllocAffilieServiceImpl#langueTiersAffilie : numAffilie is empty or value is null");
        }

        String langueAffilie = ALServiceLocator.getLangueAllocAffilieService().langueTiers(tiersAffilie);

        if (langueEgal(langueAffilie)) {
            return langueAffilie = langueCodeIso(langueAffilie);
        } else {
            langueAffilie = ALCSTiers.LANGUE_FRANCAIS;
            return langueAffilie = langueCodeIso(langueAffilie);
        }

    }

    @Override
    public String langueTiersAlloc(String idTiers, String numAffilie) throws JadeApplicationException,
            JadePersistenceException {
        // contrôle des paramêtres
        if (JadeNumericUtil.isEmptyOrZero(idTiers)) {
            throw new ALLangueAllocAffilieException(
                    "LangueAllocAffilieServiceImpl#langueTiersAlloc : idTiers is empty or value is null");
        }
        if (JadeStringUtil.isEmpty(numAffilie)) {
            throw new ALLangueAllocAffilieException(
                    "LangueAllocAffilieServiceImpl#langueTiersAlloc : numAffilie is empty or value is null");
        }

        String langueTiers = null;
        langueTiers = ALServiceLocator.getLangueAllocAffilieService().langueTiers(idTiers);
        /**
         * Si la langue du tiers est valide selon code iso italien, allemand ou français
         */
        if (langueEgal(langueTiers)) {
            return langueTiers = langueCodeIso(langueTiers);
        }
        /**
         * Sinon retourne la langue de l'affilié
         */
        else {
            return langueTiers = ALServiceLocator.getLangueAllocAffilieService().langueTiersAffilie(numAffilie);

            // this.langueCodeIso(ALServiceLocator.getLangueAllocAffilieService().langueTiersAffilie(
            // numAffilie));
        }
    }

}
