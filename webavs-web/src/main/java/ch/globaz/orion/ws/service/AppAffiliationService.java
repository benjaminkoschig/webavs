package ch.globaz.orion.ws.service;

import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.services.AFAffiliationServices;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.application.TIApplication;
import globaz.webavs.common.ICommonConstantes;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.business.exceptions.CommonTechnicalException;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * Service d'acc�s a l'application de l'affiliation
 * 
 * @author sco
 */
public class AppAffiliationService {

    private AppAffiliationService() {
        throw new UnsupportedOperationException();
    }

    /**
     * Permet la r�cup�ration de la liste des cotisations pour le num�ro d'affili� et l'ann�e pass�e en param�tre.
     * 
     * @param session Une session valide
     * @param numAffilie Un num�ro d'affili�
     * @param date Une ann�e
     * @throws NullPointerException Si la session ou le numAffilie est null
     * @return Une liste des masses pour l'affili�
     */
    public static List<AFMassesForAffilie> retrieveListCotisationForNumAffilie(BSession session, String numAffilie,
            String date) {

        Checkers.checkNotNull(session, "session");
        Checkers.checkNotNull(numAffilie, "numAffilie");
        Checkers.checkNotEmpty(numAffilie, "numAffilie");

        AFMassesForAffilieManager manager = new AFMassesForAffilieManager();
        manager.setSession(session);
        manager.setDateFin(date);
        manager.setNumAffilie(numAffilie);

        try {
            manager.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            JadeLogger.error("Unabled to retrieve cotisations and masses for the affilie : " + numAffilie, e);
            throw new CommonTechnicalException("Unabled to retrieve cotisations and masses for the affilie : "
                    + numAffilie, e);
        }

        List<AFMassesForAffilie> listMassesForAffilie = new ArrayList<AFMassesForAffilie>();

        if (manager.isEmpty()) {
            return listMassesForAffilie;
        }

        listMassesForAffilie.addAll(manager.getContainer());

        return listMassesForAffilie;

    }

    /**
     * M�thode permettant de r�cup�rer de la liste des cotisations actives d'un affili�.
     * 
     * @param session Une session
     * @param numAffilie Un num�ro d'affili�
     * @return Une list de masses
     * @throws IllegalArgumentException Exception lev�e si le num�ro d'affili� est null ou vide
     * @throws NullPointerException Si la session est null
     */
    public static List<AFMassesForAffilie> retrieveListCotisationActiveForNumAffilie(BSession session, String numAffilie) {
        return retrieveListCotisationForNumAffilie(session, numAffilie, null);
    }

    /**
     * M�thode permettant de trouver la cat�gorie d'affiliation d'un affili� dans une p�riode donn�e
     * La m�thode retourne :
     * - 0 si l'affiliation est ni AF, ni AVS (Ou si aucune affiliation correspond au num�ro d'affili�)
     * - 1 si l'affiliation est de cat�gorie AVS seul
     * - 2 si l'affiliation est de cat�gorie AF seul
     * - 3 si l'affiliation est de cat�gorie AVS / AF
     * - 4 Si le num�ro d'affili� correspond � plusieurs affiliations paritaires
     * 
     * @param session Une session
     * @param numAffilie Un num�ro d'affili�
     * @param periodeDebut une date de d�but de p�rdiode (Format aaaammjj)
     * @param periodeFin une date de fin de p�rdiode (Format aaaammjj)
     * @return Un code de retour
     * @throws Exception Si le nombre d'affiliation correspondante au numaffilie n'est pas �gal � 1
     * @throws CommonTechnicalException Si la recherche ne s'est pas faite
     * @throws NullPointerException Si la session est null
     * @throws IllegalArgumentException Si numAffilie, periodeDebut ou periodeFin n'est pas pr�cis�.
     */
    public static int findCategorieAffiliation(BSession session, String numAffilie, String periodeDebut,
            String periodeFin) {

        Checkers.checkNotNull(session, "session");
        Checkers.checkNotEmpty(numAffilie, "numAffilie");
        Checkers.checkDateAvs(periodeDebut, "periodeDebut", false);
        Checkers.checkDateAvs(periodeFin, "periodeFin", false);

        // Formatage du num affili�
        String numAffilieFormate = formatNumAffilie(numAffilie);

        List<AFAffiliation> listAffiliation = AFAffiliationServices.searchAffiliationParitaireByNumero(
                numAffilieFormate, periodeDebut, periodeFin, session);

        // Si pas d'affiliation, on retourne 0 car cela correspond a ni AF et ni AVS
        if (listAffiliation.isEmpty()) {
            return 0;
        }

        if (listAffiliation.size() > 1) {
            return 4;
        }

        AFNombreCotisationForTypeManager manager = new AFNombreCotisationForTypeManager();

        // REcherche si cotisation AVS
        int nbCotiAvs = 0;
        manager.setSession(session);
        manager.setForNumAffilie(numAffilieFormate);
        manager.setForTypeCotisation(CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
        manager.setForPeriodeDebut(periodeDebut);
        manager.setForPeriodeFin(periodeFin);

        try {
            nbCotiAvs = manager.getCount();
        } catch (Exception e) {
            JadeLogger.error("Unabled to find cotisation AVS for the affilie : " + numAffilie, e);
            throw new CommonTechnicalException("Unabled to find cotisation AVS for the affilie : " + numAffilie, e);
        }

        // Recherche si cotisation AF
        int nbCotiAF = 0;
        manager.setForTypeCotisation(CodeSystem.TYPE_ASS_COTISATION_AF);

        try {
            nbCotiAF = manager.getCount();
        } catch (Exception e) {
            JadeLogger.error("Unabled to find cotisation AF for the affilie : " + numAffilie, e);
            throw new CommonTechnicalException("Unabled to find cotisation AF for the affilie : " + numAffilie, e);
        }

        if (nbCotiAvs > 0 && nbCotiAF > 0) {
            return 3;
        } else if (nbCotiAvs > 0 && nbCotiAF == 0) {
            return 1;
        } else if (nbCotiAvs == 0 && nbCotiAF > 0) {
            return 2;
        }

        // Retourne 0 par d�faut
        return 0;
    }

    /**
     * Permet de formater un num�ro d'affili�
     * 
     * @param numeroAffilie
     * @return Le num�ro d'affili� format�
     */
    public static String formatNumAffilie(String numeroAffilie) {

        IFormatData formater = resolveNumAffilieFormater();
        return formatNumAffilie(numeroAffilie, formater);
    }

    /**
     * Permet de formater un num�ro d'affili� en lui passant un formater
     * 
     * @param numeroAffilie Un num�ro � formater
     * @param formater Un formater
     * @return Le num�ro d'affili� format�
     */
    public static String formatNumAffilie(String numeroAffilie, IFormatData formater) {
        try {
            return formater.format(numeroAffilie);
        } catch (Exception e) {
            return numeroAffilie;
        }
    }

    /**
     * Permet de r�cup�rer le formater d�finit pour la caisse
     * 
     * @return Un formater
     */
    public static IFormatData resolveNumAffilieFormater() {
        IFormatData formater = null;
        try {
            TIApplication app = (TIApplication) GlobazServer.getCurrentSystem().getApplication("PYXIS");
            formater = app.getAffileFormater();
        } catch (Exception e) {
            JadeLogger.error("Unabled to retrieve number affilie formatter", e);
        }

        if (formater == null) {
            throw new NullPointerException("ERROR OCCURED IN AppAffiliationService.formatNumAffilie : formater is null");
        }
        return formater;
    }

    /**
     * Retourne l'adresse de courrier d'un affili�
     * 
     * @param numeroAffilie
     * @return
     */
    public static String findAdresseCourrierAffilie(String numeroAffilie) {
        String adresseCourrier = null;
        try {
            // Retrouver l'id du tiers correspondant au num�ro d'affili�
            String idTiers = AFBusinessServiceLocator.getAffiliationService()
                    .findIdTiersForNumeroAffilie(numeroAffilie);
            if (idTiers == null) {
                JadeLogger.error(AppAffiliationService.class, "idTiers for affilie not found : " + numeroAffilie);
            }

            // Retrouver l'adresse de courrier relative � l'id du tiers
            AdresseTiersDetail adresseTiersDetail = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(
                    idTiers, true, JACalendar.todayJJsMMsAAAA(), ICommonConstantes.CS_APPLICATION_COTISATION,
                    AdresseService.CS_TYPE_COURRIER, "");
            if (adresseTiersDetail == null) {
                JadeLogger.error(AppAffiliationService.class, "adresseTiersDetail for idTiers not found : " + idTiers);
            } else {
                adresseCourrier = adresseTiersDetail.getAdresseFormate();
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error("Unable to find address. service not available", e);
        } catch (JadePersistenceException e) {
            JadeLogger.error("Unable to find address. Persitence error occured", e);
        } catch (JadeApplicationException e) {
            JadeLogger.error("Unable to find address. Application error occured", e);
        }

        return adresseCourrier;
    }
}
