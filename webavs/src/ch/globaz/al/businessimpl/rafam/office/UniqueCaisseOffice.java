package ch.globaz.al.businessimpl.rafam.office;

import globaz.globall.api.GlobazSystem;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALConstRafam;
import ch.globaz.al.web.application.ALApplication;
import ch.globaz.common.properties.PropertiesException;

public class UniqueCaisseOffice implements Office {

    @Override
    public String getLegalOffice() throws JadeApplicationException {

        String legalOfficeValue;
        try {
            legalOfficeValue = GlobazSystem.getApplication(ALApplication.DEFAULT_APPLICATION_WEBAF).getProperty(
                    ALConstRafam.RAFAM_LEGAL_OFFICE);
        } catch (Exception e) {
            throw new PropertiesException("Unable to retrieve the property [" + ALApplication.APPLICATION_NAME + "."
                    + ALConstRafam.RAFAM_LEGAL_OFFICE + "]", e);
        }

        if (JadeStringUtil.isBlank(legalOfficeValue)) {
            throw new PropertiesException("The property [" + ALApplication.APPLICATION_NAME + "."
                    + ALConstRafam.RAFAM_LEGAL_OFFICE + "] doesn't exist");
        }

        return legalOfficeValue.trim();
    }

    @Override
    public String getLegalOffice(String date) throws JadeApplicationException, JadePersistenceException {
        // Appel de getLegalOffice, la date n'est pas utilisée dans le cas d'une caisse unique
        return getLegalOffice();
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
