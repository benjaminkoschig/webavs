package ch.globaz.eform.utils;

import ch.globaz.common.util.NSSUtils;
import ch.globaz.eform.constant.GFSexeDaDossier;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdministrationSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.vulpecula.business.services.administration.AdministrationService;
import globaz.eform.translation.CodeSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.pyxis.db.tiers.TIPersonneAvsManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import org.apache.commons.lang3.StringUtils;

public final class GFUtils {
    private GFUtils() {}

    public static TITiersViewBean getTiers(String nss, BSession session) throws Exception {
        TIPersonneAvsManager mgr = new TIPersonneAvsManager();
        mgr.setISession(session);
        mgr.setForNumAvsActuel(NSSUtils.formatNss(nss));
        mgr.setForIncludeInactif(true);
        mgr.find(BManager.SIZE_NOLIMIT);

        if (mgr.size() == 0) {
            return null;
        } else {
            return  (TITiersViewBean) mgr.getFirstEntity();
        }
    }

    public static AdministrationComplexModel getCaisse(String idTierAdministration) throws Exception{
        AdministrationSearchComplexModel search = new AdministrationSearchComplexModel();
        search.setForIdTiersAdministration(idTierAdministration);

        search = TIBusinessServiceLocator.getAdministrationService().find(search);

        if (search.getSize() > 0) {
            return (AdministrationComplexModel) search.getSearchResults()[0];
        }

        return null;
    }

    public static AdministrationComplexModel getCaisseBySedexId(String sedexIdCaisse){
        AdministrationSearchComplexModel search = new AdministrationSearchComplexModel();
        search.setForSedexId(sedexIdCaisse);
        search.setForGenreAdministration(CodeSystem.GENRE_ADMIN_CAISSE_COMP);

        try {
            search = TIBusinessServiceLocator.getAdministrationService().find(search);

            if (search.getSize() == 1) {
                return (AdministrationComplexModel) search.getSearchResults()[0];
            }
        } catch (JadePersistenceException | JadeApplicationException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public static String formatTiers(TITiersViewBean tiers, BSession session) {
        if (tiers != null) {
            String nationality = tiers.getPays() == null ? "" : tiers.getPays().getLibelle();
            return tiers.getDesignation1() + " " + tiers.getDesignation2() + " / " + tiers.getDateNaissance() + " / " + formatSexe(tiers.getSexe(), session) + " / " + nationality;
        }
        return "";
    }

    public static BSpy formatSpy(String spy) {
        return new BSpy(spy);
    }

    public static String formatSexe(String codeSexe, BSession session) {
        if (StringUtils.isBlank(codeSexe)) {
            return "";
        }
        return GFSexeDaDossier.getByCodeSystem(codeSexe).getDesignation(session);
    }
}
