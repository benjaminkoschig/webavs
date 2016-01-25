package ch.globaz.al.businessimpl.rafam.office;

import globaz.globall.api.GlobazSystem;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.util.Date;
import ch.globaz.al.business.constantes.ALConstRafam;
import ch.globaz.al.business.exceptions.rafam.ALRafamException;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.web.application.ALApplication;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.naos.business.data.AssuranceInfo;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdministrationSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class MultiCaisseOffice implements Office {

    String numAffilie;
    String activite;
    String codeInstitution;

    private static final String GENRE_CAISSE_PROF = "509028";

    public MultiCaisseOffice(String numAffilie, String activiteAllocataire) {
        this.numAffilie = numAffilie;
        activite = activiteAllocataire;
    }

    @Override
    public String getLegalOffice() throws JadeApplicationException, JadePersistenceException {
        return getLegalOffice(JadeDateUtil.getGlobazFormattedDate(new Date()));
    }

    @Override
    public String getLegalOffice(String date) throws JadeApplicationException, JadePersistenceException {

        AssuranceInfo assuranceInfo = ALServiceLocator.getAffiliationBusinessService().getAssuranceInfo(numAffilie,
                activite, date);

        AdministrationSearchComplexModel search = new AdministrationSearchComplexModel();
        search.setForCodeAdministration(assuranceInfo.getCodeCaisseProf());
        search.setForGenreAdministration(GENRE_CAISSE_PROF);

        search = TIBusinessServiceLocator.getAdministrationService().find(search);

        AdministrationComplexModel administration = getAdministrationActive(search, assuranceInfo.getCodeCaisseProf());
        codeInstitution = administration.getAdmin().getCodeInstitution();

        if (JadeStringUtil.isBlankOrZero(codeInstitution) || codeInstitution.length() < 6
                || codeInstitution.length() > 7) {
            throw new ALRafamException(JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                    "al.rafam.annonceRafamModel.legalOffice.unformatedFormat", new String[] { codeInstitution }));
        }

        return formatCodeInstitution(codeInstitution);
    }

    /**
     * 
     * @param administrations
     * @param codeCaisseProf
     * @return
     * @throws JadeApplicationException
     * @Deprecated Bricolage pour trouver l'administration active en attendant de pouvoir faire la modification
     *             nécessaire dans Web@Tiers pour rechercher uniquement les administrations actives (prévu dans la
     *             version 1.xx.xx)
     */
    @Deprecated
    private static AdministrationComplexModel getAdministrationActive(AdministrationSearchComplexModel administrations,
            String codeCaisseProf) throws JadeApplicationException {

        int nbAdministrationActives = 0;
        AdministrationComplexModel administrationTrouvee = null;

        for (JadeAbstractModel element : administrations.getSearchResults()) {
            AdministrationComplexModel administration = (AdministrationComplexModel) element;

            if (!administration.getTiers().getInactif()) {
                nbAdministrationActives++;
                administrationTrouvee = administration;
            }

            if (nbAdministrationActives > 1) {
                throw new ALRafamException("Impossible de trouver l'administration pour le code : " + codeCaisseProf
                        + ", plusieurs administrations sont actives");
            }
        }

        if (administrationTrouvee == null) {
            throw new ALRafamException("Impossible de trouver l'administration pour le code : " + codeCaisseProf
                    + ", aucune administration n'existe ou est active");
        }

        return administrationTrouvee;
    }

    private static String formatCodeInstitution(String codeInstitution) throws ALRafamException {
        if (!codeInstitution.contains(".")) {

            if (codeInstitution.length() != 6) {
                throw new ALRafamException(JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                        "al.rafam.annonceRafamModel.legalOffice.unformatedFormat", new String[] { codeInstitution }));
            } else {
                codeInstitution = codeInstitution.substring(0, 3) + "."
                        + codeInstitution.substring(3, codeInstitution.length());
            }
        }

        return codeInstitution;
    }

    @Override
    public String getOfficeIdentifier() throws JadeApplicationException {

        String officeIdentifierValue;
        try {
            officeIdentifierValue = GlobazSystem.getApplication(ALApplication.DEFAULT_APPLICATION_WEBAF).getProperty(
                    ALConstRafam.RAFAM_OFFICE_IDENTIFIER);
        } catch (Exception e) {
            throw new PropertiesException("Unable to retrieve the property [" + ALApplication.APPLICATION_NAME + "."
                    + ALConstRafam.RAFAM_OFFICE_IDENTIFIER + "]", e);
        }

        if (JadeStringUtil.isBlank(officeIdentifierValue)) {
            throw new PropertiesException("The property [" + ALApplication.APPLICATION_NAME + "."
                    + ALConstRafam.RAFAM_OFFICE_IDENTIFIER + "] doesn't exist");
        }

        return officeIdentifierValue.trim();
    }

    @Override
    public String getOfficeBranch() throws JadeApplicationException {

        String officeBranchValue;
        try {
            officeBranchValue = GlobazSystem.getApplication(ALApplication.DEFAULT_APPLICATION_WEBAF).getProperty(
                    ALConstRafam.RAFAM_OFFICE_BRANCH);
        } catch (Exception e) {
            throw new PropertiesException("Unable to retrieve the property [" + ALApplication.APPLICATION_NAME + "."
                    + ALConstRafam.RAFAM_OFFICE_BRANCH + "]", e);
        }

        if (JadeStringUtil.isBlank(officeBranchValue)) {
            return "";
        }

        return officeBranchValue.trim();
    }
}
