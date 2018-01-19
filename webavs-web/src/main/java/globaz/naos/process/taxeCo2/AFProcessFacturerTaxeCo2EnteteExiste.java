package globaz.naos.process.taxeCo2;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.naos.application.AFApplication;
import globaz.naos.db.taxeCo2.AFTaxeCo2;

/**
 * Process pour la facturation des Cotisations Personnelles, Paritaires
 * 
 * @author: mmu, sau
 */

public final class AFProcessFacturerTaxeCo2EnteteExiste extends AFProcessFacturerTaxeCo2 {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur de AFProcessFacturation.
     */
    public AFProcessFacturerTaxeCo2EnteteExiste() {
        super();
    }

    /**
     * Constructeur de AFProcessFacturation.
     * 
     * @param parent
     *            globaz.framework.process.FWProcess
     */
    public AFProcessFacturerTaxeCo2EnteteExiste(BProcess parent) {
        super(parent);
    }

    @Override
    public FAEnteteFacture getEnteteFacture(BSession session) throws Exception {
        FAEnteteFacture enteteFacture = new FAEnteteFacture();
        FAEnteteFactureManager entete = new FAEnteteFactureManager();
        entete.setSession(session);
        entete.setForIdPassage(idPassage);
        entete.setForIdRole(roleCoti);
        entete.setForIdExterneRole(donneesFacturation.getNumAffilie());
        // entete.setForIdSousType(calculCotisation.getIdSousTypeFacture());
        entete.find();

        boolean enteteFound = false;
        // recherche sur les en-tête périodiques déjà existantes
        if (entete.size() > 0) {
            for (int iEntete = 0; (iEntete < entete.size()) && !enteteFound; iEntete++) {
                enteteFacture = (FAEnteteFacture) entete.getEntity(iEntete);
                int idSousType = JadeStringUtil.parseInt(enteteFacture.getIdSousType().substring(4, 6), 0);
                if (((idSousType >= 1) && (idSousType <= 12)) || ((idSousType >= 40) && (idSousType <= 46))
                        || ((idSousType > 60) && (idSousType < 63))) {
                    enteteFound = true;
                    enteteFacture.setSession(getSession());
                    enteteFacture.retrieve();
                }
            }
        }
        if (enteteFound) {
            return enteteFacture;
        } else {
            return null;
        }
    }

    @Override
    public String getIdModule() {
        return getIdModuleFacturation();
    }

    @Override
    boolean isMoisFacturationOk(String moisFacturation) throws Exception {
        int month;
        try {
            month = Integer.valueOf(getNaosSession().getApplication().getProperty(
                    AFApplication.PROPERTY_RESTITUTION_TAXE_CO2_MONTH, "9"));
        } catch (NumberFormatException e) {
            month = 9;
        }
        return Integer.valueOf(moisFacturation) == month;
    }

    @Override
    public Boolean tenirCompteMontantMinime(AFTaxeCo2 donneesFacturation, String anneeFacturation) throws Exception {
        return new Boolean(false);
    }

}
