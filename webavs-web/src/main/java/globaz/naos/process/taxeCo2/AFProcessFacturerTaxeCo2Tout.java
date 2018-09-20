package globaz.naos.process.taxeCo2;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWFindParameter;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.properties.JadePropertiesService;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.taxeCo2.AFTaxeCo2;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.utils.CAUtil;

/**
 * Process pour la facturation des Cotisations Personnelles, Paritaires
 *
 * @author: mmu, sau
 */

public final class AFProcessFacturerTaxeCo2Tout extends AFProcessFacturerTaxeCo2 {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String ID_SOUS_TYPE;

    public static FWCurrency getMontantMinimumRedistributionTaxeCO2(BTransaction transaction, String date)
            throws Exception {
        return new FWCurrency(FWFindParameter.findParameter(transaction, "19300000", "MONMINTAXE", date, "", 0));
    }

    /**
     * Constructeur de AFProcessFacturation.
     */
    public AFProcessFacturerTaxeCo2Tout() {
        super();
        initValues();
    }

    /**
     * Constructeur de AFProcessFacturation.
     *
     * @param parent
     *            globaz.framework.process.FWProcess
     */
    public AFProcessFacturerTaxeCo2Tout(BProcess parent) {
        super(parent);
        initValues();
    }

    private void initValues() {
        String monthAsValue = JadePropertiesService.getInstance()
                .getProperty("naos." + AFApplication.PROPERTY_RESTITUTION_TAXE_CO2_MONTH);
        ID_SOUS_TYPE = "2270";
        if (monthAsValue != null && monthAsValue.length() == 2) {
            ID_SOUS_TYPE += monthAsValue;
        } else if (monthAsValue != null && monthAsValue.length() == 1) {
            ID_SOUS_TYPE += "0" + monthAsValue;
        } else {
            // Default value
            ID_SOUS_TYPE = "227009";
        }
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
            // n'existe pas --> on ajoute l'entete de facture
            enteteFacture.setIdAffiliation(donneesFacturation.getAffiliationId());
            enteteFacture.setISession(getSession());
            enteteFacture.setIdPassage(idPassage);
            enteteFacture.setIdTypeFacture("1");
            enteteFacture.setIdRole(roleCoti);
            enteteFacture.setIdExterneRole(donneesFacturation.getNumAffilie());

            // Création du numéro de section avec un incrément de celui-ci s'il existe déjà en comptabilité aux.
            // Correction pour BZ 8611
            String numSection = CAUtil.creerNumeroSectionUnique(session, getTransaction(), roleCoti,
                    donneesFacturation.getNumAffilie(), "1", getAnneeFacturation(), ID_SOUS_TYPE);
            enteteFacture.setIdExterneFacture(numSection);

            enteteFacture.setIdSousType(ID_SOUS_TYPE);
            enteteFacture.setNonImprimable(Boolean.FALSE);
            enteteFacture.setIdSoumisInteretsMoratoires(CodeSystem.INTERET_MORATOIRE_AUTOMATIQUE);
            enteteFacture.setIdModeRecouvrement(CodeSystem.MODE_RECOUV_AUTOMATIQUE);

            if (!JadeStringUtil.isEmpty(donneesFacturation.getTiers().getIdTiers())
                    && !JadeStringUtil.isEmpty(donneesFacturation.getNumAffilie())
                    && !JadeStringUtil.isEmpty(getAnneeFacturation())) {
                // Integration du numSection dans le chargement de l'affiliation. Lien avec le BZ 8611
                AFAffiliation affToUse = AFAffiliationUtil.loadAffiliation(getSession(),
                        donneesFacturation.getTiers().getIdTiers(), donneesFacturation.getNumAffilie(), numSection,
                        roleCoti);
                if (affToUse != null) {
                    AFAffiliationUtil util = new AFAffiliationUtil(affToUse);
                    String genreAffilie = CaisseHelperFactory.getInstance()
                            .getRoleForAffilieParitaire(getSession().getApplication());
                    enteteFacture.setIdDomaineCourrier(util.getAdresseDomaineCourrier(genreAffilie));
                    enteteFacture.setIdDomaineLSV(util.getAdresseDomaineRecouvrement(genreAffilie));
                    enteteFacture.setIdDomaineRemboursement(util.getAdresseDomaineRemboursement(genreAffilie));
                    enteteFacture.setNonImprimable(util.isPlanBloque());
                }
            }
            enteteFacture.add(getTransaction());
            return enteteFacture;
        }
    }

    @Override
    public String getIdModule() {
        return getIdModuleFacturation();
    }

    @Override
    boolean isMoisFacturationOk(String moisFacturation) throws Exception {
        return true;
    }

    @Override
    public Boolean tenirCompteMontantMinime(AFTaxeCo2 donneesFacturation, String anneeFacturation) throws Exception {
        // Inforom435 - Bug 7032
        // Calcul le montant de la facture
        // Si on a saisie un taux forcé dans l'écran des
        // taxes CO2 pendre celui-là.
        String taux = "";
        if (JadeStringUtil.isBlankOrZero(donneesFacturation.getTauxForce())) {
            taux = donneesFacturation.getAssuranceTaxeCo2().getTaux("0101" + anneeFacturation).getTauxSansFraction();
        } else {
            taux = donneesFacturation.getTauxForce();
        }
        Double mntFacture = (new FWCurrency(donneesFacturation.getMasse()).doubleValue()
                * JAUtil.createBigDecimal(taux).doubleValue()) / 100;
        FWCurrency montant = new FWCurrency(JANumberFormatter.formatNoQuote(mntFacture));
        montant.abs();

        FWCurrency limit = AFProcessFacturerTaxeCo2Tout.getMontantMinimumRedistributionTaxeCO2(getTransaction(),
                "0101" + anneeFacturation);

        return (limit.compareTo(montant) > 0);
    }

}
