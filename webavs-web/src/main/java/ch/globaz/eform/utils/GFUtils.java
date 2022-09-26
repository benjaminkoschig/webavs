package ch.globaz.eform.utils;

import ch.globaz.common.util.NSSUtils;
import ch.globaz.eform.constant.GFSexeDaDossier;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdministrationSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.vulpecula.business.services.administration.AdministrationService;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.pyxis.db.tiers.TIPersonneAvsManager;
import globaz.pyxis.db.tiers.TITiersViewBean;

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

    public static AdministrationComplexModel getCaisse(String codeCaisse) throws Exception{
        AdministrationSearchComplexModel search = new AdministrationSearchComplexModel();
        search.setForCodeAdministration(codeCaisse);

        search = TIBusinessServiceLocator.getAdministrationService().find(search);

        if (search.getSize() > 0) {
            return (AdministrationComplexModel) search.getSearchResults()[0];
        }

        return null;
    }

    public static String formatTiers(TITiersViewBean tiers, BSession session) {
        return tiers.getDesignation1() + " " + tiers.getDesignation2() + " / " + tiers.getDateNaissance() + " / " + formatSexe(tiers.getSexe(), session) + " / " + tiers.getPays().getLibelle();
    }

    public static BSpy formatSpy(String spy) {
        return new BSpy(spy);
    }

    public static String formatSexe(String codeSexe, BSession session) {
        return GFSexeDaDossier.getByCodeSystem(codeSexe).getDesignation(session);
    }
}
