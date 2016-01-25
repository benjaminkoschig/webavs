package globaz.aquila.util;

import globaz.aquila.api.helper.ICOEtapeHelper;
import globaz.aquila.db.access.batch.COEtape;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.service.tiers.COTiersService;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.external.IntTiers;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiers;

/**
 * 
 * Utilitaire permettant de déterminer l'adresse contentieux selon les règles suivantes :
 * 
 * Etape avant la réquisition de poursuite :
 * 
 * 1. Adresse définie dans la section (selon le domaine de la section, correspond en général à l’adresse de facturation)
 * 2. Adresse de courrier 3. Adresse de domicile
 * 
 * Etape à partir de la réquisition de poursuite :
 * 
 * 1. Adresse de contentieux 2. Adresse de domicile 3. Adresse de courrier
 * 
 * @author mmo
 * 
 */
public class COAdresseUtils {

    private static String addInfo(String info, String separator) {
        if (!JadeStringUtil.isBlank(info)) {
            return info + separator;
        }
        return "";
    }

    /**
     * Retourne l'adresse contentieux
     * 
     * Les règles pour déterminer l'adresse contentieux sont les suivantes :
     * 
     * Etape avant la réquisition de poursuite :
     * 
     * 1. Adresse définie dans la section (selon le domaine de la section, correspond en général à l’adresse de
     * facturation) 2. Adresse de courrier 3. Adresse de domicile
     * 
     * Etape à partir de la réquisition de poursuite :
     * 
     * 1. Adresse de contentieux 2. Adresse de domicile 3. Adresse de courrier
     */

    public static TIAdresseDataSource getAdresseContentieuxDataSource(BSession session, IntTiers tiers,
            COTiersService tiersService, COContentieux contentieux, COEtape etape, String dateExecution)
            throws Exception {

        if (!ICOEtapeHelper.isEtapePoursuite(etape.getLibEtape())) {
            return COAdresseUtils.getAdresseDataSourcePrincipal(session, tiers, tiersService, contentieux,
                    dateExecution);
        } else {
            TITiers pyTiers = tiersService.loadTiers(session, tiers);
            TIAdresseDataSource adresse = null;

            // Adresse Contentieux
            adresse = pyTiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                    IConstantes.CS_APPLICATION_CONTENTIEUX, contentieux.getCompteAnnexe().getIdExterneRole(),
                    dateExecution, false, null);
            if (adresse == null) {
                adresse = pyTiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                        IConstantes.CS_APPLICATION_CONTENTIEUX, dateExecution, false);
            }

            // Adresse Domicile
            if (adresse == null) {
                adresse = COAdresseUtils.getAdresseDomicileData(session, tiers, tiersService, contentieux,
                        dateExecution, null);
            }

            // Adresse de Courrier
            if (adresse == null) {
                // Controle une adresse de domaine Contentieux dont on à pas renseigné l'idExterne.
                adresse = pyTiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                        IConstantes.CS_APPLICATION_DEFAUT, contentieux.getCompteAnnexe().getIdExterneRole(),
                        dateExecution, false, null);
            }
            if (adresse == null) {
                adresse = pyTiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                        IConstantes.CS_APPLICATION_DEFAUT, dateExecution, false);
            }

            return adresse;
        }
    }

    public static String getAdresseContentieuxStringFromDataSource(TIAdresseDataSource adresseDS, BSession session,
            IntTiers affilie, COTiersService tiersService, COContentieux contentieux, COEtape etape,
            String dateExecution) throws Exception {
        StringBuffer adresse = new StringBuffer("");

        adresse.append(COAdresseUtils.addInfo(adresseDS.ligne1, " "));
        adresse.append(COAdresseUtils.addInfo(adresseDS.ligne2, " "));
        adresse.append(COAdresseUtils.addInfo(adresseDS.ligne3, " "));
        adresse.append(COAdresseUtils.addInfo(adresseDS.ligne4, " "));
        adresse.append(COAdresseUtils.addInfo(adresseDS.attention, " "));
        adresse.append(COAdresseUtils.addInfo(adresseDS.rue, " "));
        adresse.append(COAdresseUtils.addInfo(adresseDS.numeroRue, " "));
        adresse.append(COAdresseUtils.addInfo(adresseDS.casePostale, " "));
        adresse.append(COAdresseUtils.addInfo(adresseDS.localiteNpa, " "));
        adresse.append(COAdresseUtils.addInfo(adresseDS.localiteNom, " "));

        return adresse.toString();
    }

    public static TIAdresseDataSource getAdresseCourrierData(BSession session, IntTiers tiers,
            COTiersService tiersService, COContentieux contentieux, String dateExecution, String langue)
            throws Exception {

        // Récupérer le tiers
        TITiers pyTiers = tiersService.loadTiers(session, tiers);
        if (tiers == null) {
            return null;
        } else {
            return pyTiers.getAdresseAsDataSource(COAdresseUtils.getTypeAdresseCourrier(contentieux),
                    COAdresseUtils.getDomaineAdresse(contentieux), contentieux.getCompteAnnexe().getIdExterneRole(),
                    dateExecution, true, langue);
        }
    }

    public static TIAdresseDataSource getAdresseDataSourcePrincipal(BSession session, IntTiers tiers,
            COTiersService tiersService, COContentieux contentieux, String dateExecution) throws Exception {
        return COAdresseUtils.getAdresseDataSourcePrincipal(session, tiers, tiersService, contentieux, dateExecution,
                null);
    }

    public static TIAdresseDataSource getAdresseDataSourcePrincipal(BSession session, IntTiers tiers,
            COTiersService tiersService, COContentieux contentieux, String dateExecution, String langue)
            throws Exception {
        TIAdresseDataSource result = COAdresseUtils.getAdresseCourrierData(session, tiers, tiersService, contentieux,
                dateExecution, langue);
        if (result != null) {
            return result;
        } else {

            return COAdresseUtils.getAdresseDomicileData(session, tiers, tiersService, contentieux, dateExecution,
                    langue);

        }
    }

    public static TIAdresseDataSource getAdresseDomicileData(BSession session, IntTiers tiers,
            COTiersService tiersService, COContentieux contentieux, String dateExecution, String langue)
            throws Exception {
        // Récupérer le tiers
        TITiers pyTiers = tiersService.loadTiers(session, tiers);
        if (tiers == null) {
            return null;
        } else {
            return pyTiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                    IConstantes.CS_APPLICATION_DEFAUT, contentieux.getCompteAnnexe().getIdExterneRole(), dateExecution,
                    true, langue);
        }
    }

    public static String getDomaineAdresse(COContentieux contentieux) {
        String domaine;

        if (!JadeStringUtil.isIntegerEmpty(contentieux.getSection().getDomaine())) {
            domaine = contentieux.getSection().getDomaine();
        } else {
            domaine = contentieux.getCompteAnnexe()._getDefaultDomainFromRole();
        }

        return domaine;
    }

    public static String getTypeAdresseCourrier(COContentieux contentieux) {
        String type = "";

        if (!JadeStringUtil.isIntegerEmpty(contentieux.getSection().getTypeAdresse())) {
            type = contentieux.getSection().getTypeAdresse();
        } else {
            type = IConstantes.CS_AVOIR_ADRESSE_COURRIER;
        }

        return type;
    }

}
