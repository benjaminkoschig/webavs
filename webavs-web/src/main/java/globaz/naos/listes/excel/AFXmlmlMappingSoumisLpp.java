package globaz.naos.listes.excel;

import globaz.commons.nss.NSUtil;
import globaz.framework.secure.user.FWSecureUserDetail;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.hercule.service.CETiersService;
import globaz.naos.db.controleLpp.AFAffilieSoumiLppConteneur;
import globaz.naos.db.controleLpp.AFAffilieSoumiLppConteneur.Salarie;
import globaz.naos.listes.excel.util.IAFListeColumns;
import globaz.naos.process.AFControleLppAnnuelProcess;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.db.tiers.TITiers;
import globaz.webavs.common.CommonExcelmlContainer;

public class AFXmlmlMappingSoumisLpp {

    private static boolean hasUserShowRight(AFAffilieSoumiLppConteneur.Salarie sal, int nivSecuUser) {

        int nivSecuAffilie = 0;

        try {
            nivSecuAffilie = Character.getNumericValue(sal.getNivSecuAffilie().charAt(
                    sal.getNivSecuAffilie().length() - 1));
        } catch (Exception e) {
            nivSecuAffilie = 0;
        }

        int nivSecuCI = 0;

        try {
            nivSecuCI = Character.getNumericValue(sal.getNivSecuCI().charAt(sal.getNivSecuCI().length() - 1));
        } catch (Exception e) {
            nivSecuCI = 0;
        }

        if (((nivSecuUser < nivSecuAffilie) || (nivSecuUser < nivSecuCI))) {
            return false;
        }

        return true;
    }

    private static void loadDetail(AFControleLppAnnuelProcess process, CommonExcelmlContainer container,
            AFAffilieSoumiLppConteneur.Salarie sal, int nivSecuUser) throws Exception, Exception {

        AFXmlmlMappingSoumisLpp.renseigneNomEtAdresse(process.getSession(), process.getTypeAdresse(), container,
                sal.getIdTiers(), sal.getNumeroAffilie());

        container.put(IAFListeColumns.NUM_AFFILIE, sal.getNumeroAffilie());
        container.put(IAFListeColumns.NOM_SALARIE, sal.getNom());
        container.put(IAFListeColumns.NSS, NSUtil.formatAVSNewNum(sal.getNss()));
        container.put(IAFListeColumns.PERIODE_TRAVAIL, sal.getMoisDebut() + " - " + sal.getMoisFin());

        // Gestion de la sécurité affilié utilisateur (Secure Code)
        if (AFXmlmlMappingSoumisLpp.hasUserShowRight(sal, nivSecuUser)) {
            FWCurrency c = new FWCurrency(sal.getMontant());
            container.put(IAFListeColumns.MONTANT, c.toStringFormat());
        } else {
            container.put(IAFListeColumns.MONTANT, process.getSession().getLabel("NAOS_CACHE"));
        }
        container.put(IAFListeColumns.DATE_NAISSANCE, sal.getDateNaissance());
        container.put(IAFListeColumns.SEXE, process.getSession().getCodeLibelle(sal.getSexe()));

        container.put(IAFListeColumns.SUIVI, sal.isSuivi() ? process.getSession().getLabel("NAOS_LIBELLE_OUI")
                : process.getSession().getLabel("NAOS_LIBELLE_NON"));
    }

    private static void loadHeader(CommonExcelmlContainer container, String dateImpression, String nomListe,
            String anneeDebut, String anneeFin, String numInforom, String user) throws Exception {
        // Modif PO 6352
        container.put(IAFListeColumns.HEADER_NUM_INFOROM, numInforom);
        container.put(IAFListeColumns.HEADER_USER, user);
        container.put(IAFListeColumns.HEADER_DATE, dateImpression);
        container.put(IAFListeColumns.HEADER_NOM_LISTE, nomListe);
        container.put(IAFListeColumns.HEADER_ANNEE, anneeDebut + " " + anneeFin);

        /**
         * astuce temporaire pour que le fichier xls généré contienne les lignes blanches d'entête du modèle xml
         */
        container.put(IAFListeColumns.HEADER_BLANK_1, "");
        container.put(IAFListeColumns.HEADER_BLANK_2, "");
        container.put(IAFListeColumns.HEADER_BLANK_3, "");

    }

    /**
     * Construction du fichier XML
     * 
     * @param conteneur
     * @param process
     * @param nomListe
     * @return
     * @throws Exception
     * @throws Exception
     */
    public static CommonExcelmlContainer loadResults(AFAffilieSoumiLppConteneur conteneur,
            AFControleLppAnnuelProcess process, String nomListe, String numInforom) throws Exception, Exception {

        CommonExcelmlContainer container = new CommonExcelmlContainer();

        // Chargement du header du document
        AFXmlmlMappingSoumisLpp.loadHeader(container, process.getDateImpression(), nomListe, process.getAnneeDebut(),
                process.getAnneeFin(), numInforom, process.getSession().getUserName());

        String[] listAffilie = conteneur.getTableauAffilie();

        // Récupération du niveau de sécurité de l'utilisateur
        int nivSecuUser = AFXmlmlMappingSoumisLpp.retrieveNivSecuUser(process);

        // Parcours de tous les affiliés
        for (String idAffSoumis : listAffilie) {
            Salarie[] listSalarie = conteneur.getTableauSalarieForAffilie(idAffSoumis);

            // Prévient les listes vides
            if (listSalarie == null) {
                continue;
            }

            // Parcours de tous les salariés de l'affilié
            for (AFAffilieSoumiLppConteneur.Salarie sal : listSalarie) {
                AFXmlmlMappingSoumisLpp.loadDetail(process, container, sal, nivSecuUser);
            }
        }

        return container;
    }

    private static void renseigneNomEtAdresse(BSession session, String typeAdresse, CommonExcelmlContainer container,
            String idTiers, String numAffilie) throws Exception {

        String ville = "";
        String rue = "";
        String numRue = "";
        String npa = "";
        String casePostale = "";

        TITiers tiers = CETiersService.retrieveTiers(session, idTiers);

        // Nom, prénom
        container.put(IAFListeColumns.NOM, tiers.getNomPrenom());

        TIAdresseDataSource d;

        try {
            d = CETiersService.retrieveAdresseDataSource(typeAdresse, tiers, numAffilie);
        } catch (Exception e) {
            throw new Exception("Technical Exception, Unabled to retrieve the adresse ( idTiers = " + idTiers + ")", e);
        }

        if (d != null) {
            ville = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
            rue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE);
            numRue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO);
            npa = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA);
            casePostale = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_CASE_POSTALE);
        }

        // Rue
        container.put(IAFListeColumns.RUE, rue + " " + numRue);
        // Case postale
        container.put(IAFListeColumns.CASE_POSTALE, casePostale);
        // NPA
        container.put(IAFListeColumns.NPA, npa);
        // Localité
        container.put(IAFListeColumns.LOCALITE, ville);
    }

    private static int retrieveNivSecuUser(BProcess process) throws Exception {

        int accesUser = 0;

        FWSecureUserDetail user = new FWSecureUserDetail();
        user.setSession(process.getSession());
        user.setUser(process.getSession().getUserId());
        user.setLabel(CICompteIndividuel.SECURITE_LABEL);
        user.retrieve(process.getTransaction());

        if (!user.isNew()) {
            accesUser = Integer.parseInt(user.getData());
        }

        return accesUser;
    }
}
