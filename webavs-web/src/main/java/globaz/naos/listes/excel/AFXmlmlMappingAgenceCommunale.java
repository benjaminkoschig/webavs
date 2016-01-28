package globaz.naos.listes.excel;

import globaz.globall.db.BManager;
import globaz.naos.db.adhesion.AFAdhesion;
import globaz.naos.db.adhesion.AFAdhesionManager;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.listeAgenceCommunale.AFListeAgenceCommunale;
import globaz.naos.db.listeAgenceCommunale.AFListeAgenceCommunaleManager;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliation;
import globaz.naos.listes.excel.util.AFExcelmlUtils;
import globaz.naos.listes.excel.util.IAFListeColumns;
import globaz.naos.listes.excel.util.NaosContainer;
import globaz.naos.process.AFListeExcelAgenceCommunaleProcess;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;

/**
 * @revision SCO 15 déc. 2010
 */
public class AFXmlmlMappingAgenceCommunale {

    public final static String CS_AFFILIATION = "519007";

    public static String chargerNumeroCaisse(String genreCaisse, AFListeAgenceCommunale entity,
            AFListeExcelAgenceCommunaleProcess process, String numCaisseDefaut) throws Exception {

        AFAdhesionManager mgrAdhesion = new AFAdhesionManager();
        AFAdhesion adhesion = null;
        TIAdministrationViewBean vbAdministrationTier = null;
        AFSuiviCaisseAffiliation caisse = null;
        AFCotisationManager mgrCotisation = new AFCotisationManager();
        AFCotisation cotisation = null;
        try {
            mgrAdhesion.setSession(process.getSession());
            mgrAdhesion.setForAffiliationId(entity.getIdAffiliation());
            mgrAdhesion.setForDateValeur(process.getForDate());
            mgrAdhesion.find(BManager.SIZE_NOLIMIT);

            for (int i = 0; i <= mgrAdhesion.size(); i++) {
                adhesion = (AFAdhesion) mgrAdhesion.getEntity(i);
                if (adhesion != null) {
                    vbAdministrationTier = adhesion.getAdministrationCaisse();
                    if ((vbAdministrationTier != null)
                            && genreCaisse.equalsIgnoreCase(vbAdministrationTier.getGenreAdministration())) {
                        return vbAdministrationTier.getCodeAdministration();
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("Impossible de récupérer l'adhésion pour l'id : " + entity.getIdAffiliation() + ")", e);
        }
        try {
            if (CodeSystem.GENRE_CAISSE_AVS.equals(genreCaisse)) {
                caisse = AFAffiliationUtil.getCaissseAVS(
                        AFAffiliationUtil.getAffiliation(entity.getIdAffiliation(), process.getSession()),
                        process.getForDate());
                if (caisse != null) {
                    if (caisse.getAdministration() != null) {
                        return caisse.getAdministration().getCodeAdministration();
                    }
                }
                if (mgrAdhesion.size() > 0) {
                    for (int i = 0; i <= mgrAdhesion.size(); i++) {
                        adhesion = (AFAdhesion) mgrAdhesion.getEntity(i);
                        if (adhesion != null) {
                            mgrCotisation.setSession(process.getSession());
                            mgrCotisation.setForAdhesionId(adhesion.getAdhesionId());
                            mgrCotisation.find(BManager.SIZE_NOLIMIT);
                            for (int j = 0; j <= mgrCotisation.size(); j++) {
                                cotisation = (AFCotisation) mgrCotisation.getEntity(j);
                                if (cotisation != null) {
                                    if (CodeSystem.TYPE_ASS_COTISATION_AVS_AI.equals(cotisation.getAssurance()
                                            .getTypeAssurance())) {
                                        return numCaisseDefaut;
                                    }
                                }
                            }

                        }
                    }
                }
            } else {
                if (CodeSystem.GENRE_CAISSE_AF.equals(genreCaisse)) {
                    caisse = AFAffiliationUtil.getCaissseAF(
                            AFAffiliationUtil.getAffiliation(entity.getIdAffiliation(), process.getSession()),
                            process.getForDate());
                    if (caisse != null) {
                        if (caisse.getAdministration() != null) {
                            return caisse.getAdministration().getCodeAdministration();
                        }
                    }
                } else {
                    throw new Exception("Erreur, aucun genre de caisse n'est renseigné pour l'id  : "
                            + entity.getIdAffiliation());
                }
            }
        } catch (Exception e) {
            throw new Exception("Impossible de récupérer le suivi de caisse pour l'id : " + entity.getIdAffiliation()
                    + ")", e);
        }
        return "";
    }

    private static void loadDetail(int i, NaosContainer container, AFListeAgenceCommunale entity,
            AFListeExcelAgenceCommunaleProcess process, String numCaisseDefaut) throws Exception, Exception {
        String numCaisseAF = "";
        String numCaisseAVS = "";

        process.incProgressCounter();

        container.put(IAFListeColumns.NUM_AFFILIE, entity.getNumAffilie());
        container.put(IAFListeColumns.NOM, entity.getNom());
        container.put(IAFListeColumns.PRENOM, entity.getPrenom());

        /**
         * recherche l'adresse dans le tiers put dans le container
         */
        AFExcelmlUtils.renseigneAdresse(process.getSession(), AFXmlmlMappingAgenceCommunale.CS_AFFILIATION, container,
                entity.getIdTiers());

        container.put(IAFListeColumns.DATE_AFFILIATION, entity.getDateFormate(entity.getDateDebutAffiliation()));
        container.put(IAFListeColumns.DATE_RADIATION, entity.getDateFormate(entity.getDateFinAffiliation()));
        container.put(IAFListeColumns.GENRE, process.getSession().getCodeLibelle(entity.getGenreAffiliation()));
        numCaisseAVS = AFXmlmlMappingAgenceCommunale.chargerNumeroCaisse(CodeSystem.GENRE_CAISSE_AVS, entity, process,
                numCaisseDefaut);
        numCaisseAF = AFXmlmlMappingAgenceCommunale.chargerNumeroCaisse(CodeSystem.GENRE_CAISSE_AF, entity, process,
                numCaisseDefaut);
        container.put(IAFListeColumns.NUM_CAISSE_AVS, numCaisseAVS);
        container.put(IAFListeColumns.NUM_CAISSE_AF, numCaisseAF);
        if (entity.getProvisoire().booleanValue() || CodeSystem.TYPE_AFFILI_PROVIS.equals(entity.getGenreAffiliation())) {
            if (CodeSystem.TYPE_AFFILI_FICHIER_CENT.equals(entity.getGenreAffiliation())) {
                container.put(IAFListeColumns.COMMENTAIRE,
                        process.getSession().getLabel("LISTE_COMMENTAIRE_FICHIER_CENTRALE") + " - "
                                + process.getSession().getLabel("LISTE_COMMENTAIRE_PROVISOIRE"));
            } else {
                container.put(IAFListeColumns.COMMENTAIRE, process.getSession()
                        .getLabel("LISTE_COMMENTAIRE_PROVISOIRE"));
            }
        } else {
            if (CodeSystem.TYPE_AFFILI_FICHIER_CENT.equals(entity.getGenreAffiliation())) {
                container.put(IAFListeColumns.COMMENTAIRE,
                        process.getSession().getLabel("LISTE_COMMENTAIRE_FICHIER_CENTRALE"));
            } else {
                container.put(IAFListeColumns.COMMENTAIRE, "");
            }
        }

    }

    private static void loadHeader(NaosContainer container, AFListeExcelAgenceCommunaleProcess process,
            String nomAgenceCommunale) throws Exception {

        container.put(IAFListeColumns.HEADER_NUM_INFOROM, AFListeExcelAgenceCommunaleProcess.NUMERO_INFOROM);
        container.put(IAFListeColumns.HEADER_NOM_AGENCE, nomAgenceCommunale);

        /**
         * astuce temporaire pour que le fichier xls généré contienne les lignes blanches d'entête du modèle xml
         */
        container.put(IAFListeColumns.HEADER_BLANK_1, "");
        container.put(IAFListeColumns.HEADER_BLANK_2, "");
        container.put(IAFListeColumns.HEADER_BLANK_3, "");

    }

    public static NaosContainer loadResults(AFListeAgenceCommunaleManager manager,
            AFListeExcelAgenceCommunaleProcess process, String numCaisse, String nomAgenceCommunale) throws Exception,
            Exception {
        NaosContainer container = new NaosContainer();

        AFXmlmlMappingAgenceCommunale.loadHeader(container, process, nomAgenceCommunale);

        for (int i = 0; (i < manager.size()) && !process.isAborted(); i++) {
            AFListeAgenceCommunale entity = (AFListeAgenceCommunale) manager.getEntity(i);
            AFXmlmlMappingAgenceCommunale.loadDetail(i, container, entity, process, numCaisse);
        }

        return container;
    }
}
