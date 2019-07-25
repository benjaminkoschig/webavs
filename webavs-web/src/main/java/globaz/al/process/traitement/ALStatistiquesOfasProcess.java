/*
 * Créé le 14 févr. 07
 */
package globaz.al.process.traitement;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.web.application.ALApplication;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.log.JadeLogger;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.application.AFApplication;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.util.TIToolBox;

/**
 * @author jpa
 *
 */

public class ALStatistiquesOfasProcess extends BProcess {
    public static final String CSV_CANTON_NAME = "_canton_";

    public static final String CSV_EXT = ".csv";
    public static final String CSV_STATISTIQUE_OFAS_OUTPUT_FILE_NAME = "StatistiqueOfasAF_caisse_";
    public static final String CSV_STATISTIQUE_OFAS_OUTPUT_FILE_NAME_SANS_MULTICAISSE = "StatistiqueOfas";
    public static final String KEY_026A_MAP_STAT_OFAS = "026A";
    public static final String KEY_026B_MAP_STAT_OFAS = "026B";
    public static final String KEY_026C_MAP_STAT_OFAS = "026C";
    public static final String KEY_35A_MAP_STAT_OFAS = "35A";
    public static final String KEY_35B_MAP_STAT_OFAS = "35B";
    public static final String KEY_35C_MAP_STAT_OFAS = "35C";
    public static final String KEY_35D_MAP_STAT_OFAS = "35D";
    public static final String KEY_35E_MAP_STAT_OFAS = "35E";
    public static final String KEY_35F_MAP_STAT_OFAS = "35F";
    public static final String KEY_36A_MAP_STAT_OFAS = "36A";
    public static final String KEY_36B_MAP_STAT_OFAS = "36B";
    public static final String KEY_36C_MAP_STAT_OFAS = "36C";
    public static final String KEY_36D_MAP_STAT_OFAS = "36D";
    public static final String KEY_36E_MAP_STAT_OFAS = "36E";
    public static final String KEY_36F_MAP_STAT_OFAS = "36F";
    public static final String KEY_37A_MAP_STAT_OFAS = "37A";
    public static final String KEY_37B_MAP_STAT_OFAS = "37B";
    public static final String KEY_37C_MAP_STAT_OFAS = "37C";
    public static final String KEY_37D_MAP_STAT_OFAS = "37D";
    public static final String KEY_37E_MAP_STAT_OFAS = "37E";
    public static final String KEY_38A_MAP_STAT_OFAS = "38A";
    public static final String KEY_39A_MAP_STAT_OFAS = "39A";
    public static final String KEY_39B_MAP_STAT_OFAS = "39B";
    public static final String KEY_39C_MAP_STAT_OFAS = "39C";
    public static final String KEY_39D_MAP_STAT_OFAS = "39D";
    public static final String KEY_40A_MAP_STAT_OFAS = "40A";
    public static final String KEY_40B_MAP_STAT_OFAS = "40B";
    public static final String KEY_40C_MAP_STAT_OFAS = "40C";
    public static final String KEY_40D_MAP_STAT_OFAS = "40D";
    public static final String KEY_40E_MAP_STAT_OFAS = "40E";
    public static final String KEY_40F_MAP_STAT_OFAS = "40F";
    public static final String KEY_40G_MAP_STAT_OFAS = "40G";
    public static final String KEY_41A_MAP_STAT_OFAS = "41A";
    public static final String KEY_41B_MAP_STAT_OFAS = "41B";
    public static final String KEY_41C_MAP_STAT_OFAS = "41C";
    public static final String KEY_41D_MAP_STAT_OFAS = "41D";
    public static final String KEY_41E_MAP_STAT_OFAS = "41E";
    public static final String KEY_41F_MAP_STAT_OFAS = "41F";
    public static final String KEY_41G_MAP_STAT_OFAS = "41G";
    public static final String KEY_42A_MAP_STAT_OFAS = "42A";
    public static final String KEY_42B_MAP_STAT_OFAS = "42B";
    public static final String KEY_42C_MAP_STAT_OFAS = "42C";
    public static final String KEY_42D_MAP_STAT_OFAS = "42D";
    public static final String KEY_42E_MAP_STAT_OFAS = "42E";
    public static final String KEY_42F_MAP_STAT_OFAS = "42F";
    public static final String KEY_43A_MAP_STAT_OFAS = "43A";
    public static final String KEY_44A_MAP_STAT_OFAS = "44A";
    public static final String KEY_44B_MAP_STAT_OFAS = "44B";
    public static final String KEY_44C_MAP_STAT_OFAS = "44C";
    public static final String KEY_44D_MAP_STAT_OFAS = "44D";
    public static final String KEY_45A_MAP_STAT_OFAS = "45A";
    public static final String KEY_45B_MAP_STAT_OFAS = "45B";
    public static final String KEY_45C_MAP_STAT_OFAS = "45C";
    public static final String KEY_45D_MAP_STAT_OFAS = "45D";
    public static final String KEY_46A_MAP_STAT_OFAS = "46A";
    public static final String KEY_46B_MAP_STAT_OFAS = "46B";
    public static final String KEY_46C_MAP_STAT_OFAS = "46C";
    public static final String KEY_46D_MAP_STAT_OFAS = "46D";
    public static final String KEY_47A_MAP_STAT_OFAS = "47A";
    public static final String KEY_47B_MAP_STAT_OFAS = "47B";
    public static final String KEY_47C_MAP_STAT_OFAS = "47C";
    public static final String KEY_47D_MAP_STAT_OFAS = "47D";
    public static final String LABEL_STATISTIQUE_OFAS_FIX_PART = "STATISTIQUE_OFAS_";
    public static final String PARAMETRAGE_MULTICAISSES = "rubrique.multicaisse.";
    public static final String REQUETE_AF_COL_HNIPAY = "COL_HNIPAY";

    public static final String REQUETE_AF_COL_NAME_ACTIVITE_ALLOC = "COL_CSCAAL";
    public static final String REQUETE_AF_COL_NAME_CANTON = "COL_CANTON";
    public static final String REQUETE_AF_COL_NAME_CATEGORIE_TARIF = "COL_CSCATA";
    public static final String REQUETE_AF_COL_NAME_CS_UNIT = "COL_CSUNIT";
    private static final String REQUETE_AF_COL_NAME_ENFANT = "ID_ENFANT";
    public static final String REQUETE_AF_COL_NAME_GENRE_ASS = "COL_MBTGEN";
    public static final String REQUETE_AF_COL_NAME_NUMCPT = "COL_NUMCPT";
    public static final String REQUETE_AF_COL_NAME_NUMERO_DOSSIER = "COL_EID";
    public static final String REQUETE_AF_COL_NAME_PARAM = "COL_PARAM";
    public static final String REQUETE_AF_COL_NAME_PAYS_ALLOC = "COL_PAYS_ALLOC";
    public static final String REQUETE_AF_COL_NAME_PAYS_ENFANT = "COL_PAYS_ENFANT";
    public static final String REQUETE_AF_COL_NAME_SOMME_MONTANT_VERSE = "COL_SOMME_MONTANT";
    public static final String REQUETE_AF_COL_NAME_SOMME_NOMBRE_PREST = "COL_SOMME_NOMBRE";
    public static final String REQUETE_AF_COL_NAME_STATUT = "COL_CSTATU";
    public static final String REQUETE_AF_COL_NAME_SUM_NMONT = "COL_SUM_NMONT";
    public static final String REQUETE_AF_COL_NAME_TAUX_OCCUP = "COL_ETOCC";
    public static final String REQUETE_AF_COL_NAME_TYPE_ALLOC = "COL_CSTYPE";
    public static final String REQUETE_AF_COL_NAME_ID_ENFANT = "ID_ENFANT";
    public static final String REQUETE_AF_COL_NAME_ID_ALLOCATAIRE = "ID_ALLOCATAIRE";
    public static final String REQUETE_AF_COL_NAME_STATUT_PRESTATION = "GENRE_ALLOCATION";
    public static final String REQUETE_AF_COL_NAME_NUMERO_AFFILIE = "NUM_AFFILIE";
    public static final String REQUETE_AF_COL_TYPE_AFF = "COL_MATTAF";
    public static final String REQUETE_AF_COL_VAL = "COL_VAL";
    public static final String REQUETE_AF_COL_SOMME = "COL_SOMME";
    public static final String REQUETE_CAISSES_AF = "CAISSES_AF";
    public static final String REQUETE_CANTONS = "CANTONS";
    public static final String REQUETE_NB_ENFANTS = "COL_NB_ENF";

    public static final String REQUETE_NOMBRE_AFFILIES = "NOMBRE_AFFILIES";
    public static final String REQUETE_SOMME_GLOBALE = "COL_SOMME_GLOBALE";

    private static final long serialVersionUID = 6338622029160406664L;

    private String annee = null;
    private boolean isSansRestitutions = false;

    // @BMS-ONLY : BEGIN BLOCK
    public static int baseProgress = 1000;
    public static int nbCaisses = 0;
    public static int nbItemsByCaisses = 0;
    public static int currentIdxCaisse = 0;

    private boolean isBMS = false;
    private String startDay = "01";
    private String endDay = "31";

    private String startMonth = "01";
    private String endMonth = "12";

    private String startMonthBMS = "02";
    private String endMonthBMS = "01";

    private String anneeFinBMS = "";
    // @BMS-ONLY : END BLOCK

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String schemaDBWithTablePrefix = null;

    public ALStatistiquesOfasProcess() {
        super();
        annee = "";
        schemaDBWithTablePrefix = TIToolBox.getCollection();

        // Each client has his own implementation class
        // The BMS class implementation is "globaz.bms.affiliation.BMSNoAffGenerator"
        String affiliationClass = JadePropertiesService.getInstance().getProperty("naos.genNumAffilie");
        isBMS = affiliationClass != null && affiliationClass.contains("BMSNoAffGenerator");
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        boolean success = true;
        String errorMessage = "";

        try {
            String isSansRestitutionsString = ((ALApplication) GlobazSystem
                    .getApplication(ALApplication.DEFAULT_APPLICATION_WEBAF))
                            .getProperty("statsOfas.isSansRestitutions");
            isSansRestitutions = Boolean.parseBoolean(isSansRestitutionsString);
        } catch (Exception e1) {
            // On ne fait rien, on laisse la propriété à false et on tient compte des restitutions
        }

        try {
            // initialisation du thread context et utilisation du contextjdbc
            JadeThreadContext threadContext = initThreadContext(getSession());
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            // On compte le nombre de caisses AF
            // Une liste par caisse
            List<String> listCaissesAF = listageCaissesAF();

            setProgressScaleValue(ALStatistiquesOfasProcess.baseProgress);

            if (listCaissesAF.size() > 0) {
                // @BMS-ONLY : set base counter for process progress
                ALStatistiquesOfasProcess.currentIdxCaisse = 0;
                ALStatistiquesOfasProcess.nbCaisses = listCaissesAF.size();
                ALStatistiquesOfasProcess.nbItemsByCaisses = ALStatistiquesOfasProcess.baseProgress
                        / ALStatistiquesOfasProcess.nbCaisses;
                for (String numeroCaisse : listCaissesAF) {
                    // Une liste par canton
                    this.remplirMap(numeroCaisse);

                    // @BMS-ONLY : increment base counter for process progress
                    ALStatistiquesOfasProcess.currentIdxCaisse++;
                }
            } else {
                ALStatistiquesOfasProcess.nbItemsByCaisses = ALStatistiquesOfasProcess.baseProgress;
                this.remplirMap();

            }

            JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
            docInfoExcel.setApplicationDomain(AFApplication.DEFAULT_APPLICATION_NAOS);
            docInfoExcel.setDocumentTitle(ALStatistiquesOfasProcess.CSV_STATISTIQUE_OFAS_OUTPUT_FILE_NAME);
            docInfoExcel.setPublishDocument(true);
            docInfoExcel.setArchiveDocument(false);

            zipAttachedFiles(docInfoExcel);

        } catch (Exception e) {
            success = false;
            errorMessage = e.toString();
        } finally {
            // stopper l'utilisation du context
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }

        if (!success || isAborted() || isOnError() || getSession().hasErrors()) {

            success = false;

            getMemoryLog().logMessage(errorMessage, FWMessage.FATAL, this.getClass().getName());
            this._addError(getTransaction(), errorMessage);
        }

        return success;

    }

    private void addALineInListCSVLine(String value1, String value2, List<String> listCSVLine) {
        StringBuffer tempLine = new StringBuffer();

        tempLine.append(value1);
        tempLine.append(";");
        tempLine.append(value2);
        tempLine.append(";");
        tempLine.append(IOUtils.LINE_SEPARATOR);

        listCSVLine.add(tempLine.toString());
    }

    private void addAStatistiqueLineInListCSVLine(String keyMapStatOfas, Map<String, String> mapStatOfas,
            List<String> listCSVLine) {
        addALineInListCSVLine(
                getSession().getLabel(ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + keyMapStatOfas),
                mapStatOfas.get(keyMapStatOfas), listCSVLine);
    }

    public void changeFormatMapToCurrency(Map<String, String> mapStatOfas) {

        for (java.util.Map.Entry<String, String> entry : mapStatOfas.entrySet()) {
            String cle = entry.getKey();
            String valeur = entry.getValue();
            String valeurFormatte = String.valueOf(new FWCurrency(valeur).toStringFormat());
            if (valeurFormatte.endsWith(".00")) {
                valeurFormatte = valeurFormatte.substring(0, valeurFormatte.length() - 3);
            }
            mapStatOfas.put(cle, valeurFormatte);

        }
    }

    private void compterADIAdoptionNaissance(Map<String, String> mapStatOfas, String numeroCaisse,
            List<Map<String, String>> listAdoptionNaissanceADI) {
        double getVal = 0.0;

        for (Map<String, String> mapAlloc : listAdoptionNaissanceADI) {

            getVal += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));

        }
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_37E_MAP_STAT_OFAS, String.valueOf(getVal));
    }

    private void compterADIADCAllocEnfantsFormProf(Map<String, String> mapStatOfas, String numeroCaisse,
            List<Map<String, String>> listAllocEnfantsAllocADI) {
        double getValCSEnfant = 0.0;
        double getValCSFormation = 0.0;
        double getValISEnfant = 0.0;
        double getValISFormation = 0.0;

        for (Map<String, String> mapAlloc : listAllocEnfantsAllocADI) {

            if (ALCSDroit.TYPE_ENF.equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC))
                    && ALCSPrestation.STATUT_ADC
                            .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_STATUT))
                    && (Double.valueOf(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL)) > 0)) {
                getValCSEnfant++;

            }
            if (ALCSDroit.TYPE_FORM.equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC))
                    && ALCSPrestation.STATUT_ADC
                            .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_STATUT))
                    && (Double.valueOf(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL)) > 0)) {
                getValCSFormation++;

            }

            if (ALCSDroit.TYPE_ENF.equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC))
                    && ALCSPrestation.STATUT_ADI
                            .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_STATUT))
                    && (Double.valueOf(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL)) > 0)) {
                getValISEnfant++;

            }
            if (ALCSDroit.TYPE_FORM.equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC))
                    && ALCSPrestation.STATUT_ADI
                            .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_STATUT))
                    && (Double.valueOf(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL)) > 0)) {
                getValISFormation++;

            }
        }
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_35E_MAP_STAT_OFAS, String.valueOf(getValCSEnfant));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_35F_MAP_STAT_OFAS, String.valueOf(getValISEnfant));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_36E_MAP_STAT_OFAS, String.valueOf(getValCSFormation));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_36F_MAP_STAT_OFAS, String.valueOf(getValISFormation));
    }

    private void compterFormProfSalarieTSE(Map<String, String> mapStatOfas, String numeroCaisse,
            List<Map<String, String>> listAllocFormProfSalaries) {
        double getVal = 0.0;
        double getVal2 = 0.0;
        double getVal3 = 0.0;
        double nbA = 0.0;
        double nbB = 0.0;
        double nbC = 0.0;
        double nbAlloc;
        List<String> listeClesAllocsEnfants = new ArrayList<String>();

        for (Map<String, String> mapAlloc : listAllocFormProfSalaries) {
            // Création de la clé composé des informations pour un enfant
            String idEnfant = mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ID_ENFANT);
            String typeAllocation = mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC);
            String numAff = mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_NUMERO_AFFILIE);
            String keyActivite = mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ACTIVITE_ALLOC);
            String genreAllocation = mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_STATUT_PRESTATION);
            String cleAllocsEnfant = idEnfant + typeAllocation + numAff + keyActivite + genreAllocation;
            if (!listeClesAllocsEnfants.contains(cleAllocsEnfant)) {
                listeClesAllocsEnfants.add(cleAllocsEnfant);
                nbAlloc = 1;
            } else {
                nbAlloc = 0;
            }

            if (ALCSDroit.TYPE_FORM.equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC))) {

                if (ALCSDossier.ACTIVITE_SALARIE
                        .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ACTIVITE_ALLOC))) {
                    getVal += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));
                    nbA += nbAlloc;
                }

                if (ALCSDossier.ACTIVITE_INDEPENDANT
                        .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ACTIVITE_ALLOC))) {
                    getVal2 += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));
                    nbB += nbAlloc;
                }

                if (ALCSDossier.ACTIVITE_NONACTIF
                        .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ACTIVITE_ALLOC))) {
                    getVal3 += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));
                    nbC += nbAlloc;
                }
            }
        }
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_36A_MAP_STAT_OFAS, String.valueOf(nbA));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_36B_MAP_STAT_OFAS, String.valueOf(nbB));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_36C_MAP_STAT_OFAS, String.valueOf(nbC));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_36D_MAP_STAT_OFAS, String.valueOf(nbA + nbB + nbC));
    }

    private void compterNbBenefAllocEnfantFormationProf(Map<String, String> mapStatOfas, String numeroCaisse,
            List<Map<String, String>> listAlloc) {
        double getVal = 0.0;
        double getVal2 = 0.0;
        double getVal3 = 0.0;
        Integer nbSalaire = 0;
        Integer nbIndependant = 0;
        Integer nbNonActif = 0;
        Integer nbAlloc;
        List<String> listeClesAllocsEnfants = new ArrayList<String>();

        for (Map<String, String> mapAlloc : listAlloc) {
            String idAllocataire = mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ID_ALLOCATAIRE);
            String keyActivite = mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ACTIVITE_ALLOC);
            String numAff = mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_NUMERO_AFFILIE);
            String cleAllocsEnfant = idAllocataire + numAff + keyActivite;
            if (!listeClesAllocsEnfants.contains(cleAllocsEnfant)) {
                listeClesAllocsEnfants.add(cleAllocsEnfant);
                nbAlloc = 1;
            } else {
                nbAlloc = 0;
            }

            if (ALCSDossier.ACTIVITE_SALARIE
                    .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ACTIVITE_ALLOC))) {

                getVal += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));
                nbSalaire += nbAlloc;
            }

            if (ALCSDossier.ACTIVITE_INDEPENDANT
                    .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ACTIVITE_ALLOC))) {

                getVal2 += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));
                nbIndependant += nbAlloc;
            }

            if (ALCSDossier.ACTIVITE_NONACTIF
                    .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ACTIVITE_ALLOC))) {

                getVal3 += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));
                nbNonActif += nbAlloc;
            }
        }
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_39A_MAP_STAT_OFAS, String.valueOf(nbSalaire));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_39B_MAP_STAT_OFAS, String.valueOf(nbIndependant));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_39C_MAP_STAT_OFAS, String.valueOf(nbNonActif));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_39D_MAP_STAT_OFAS,
                String.valueOf(nbSalaire + nbIndependant + nbNonActif));
    }

    private void compterNbSommeAllocSuisse(Map<String, String> mapStatOfas, String numeroCaisse,
            List<Map<String, String>> listAlloc) {

        double getVal = 0.0;
        double getVal2 = 0.0;
        double getVal3 = 0.0;
        double getVal4 = 0.0;

        for (Map<String, String> mapAlloc : listAlloc) {

            if (ALCSDroit.TYPE_ENF.equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC))) {

                getVal += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_NB_ENFANTS));
                getVal2 += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_SUM_NMONT));
            }

            else if (ALCSDroit.TYPE_FORM
                    .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC))) {

                getVal3 += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_NB_ENFANTS));
                getVal4 += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_SUM_NMONT));

            }
        }

        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_44A_MAP_STAT_OFAS, String.valueOf(getVal));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_45A_MAP_STAT_OFAS, String.valueOf(getVal2));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_46A_MAP_STAT_OFAS, String.valueOf(getVal3));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_47A_MAP_STAT_OFAS, String.valueOf(getVal4));
    }

    private void compterNbSommeAllocSuisseInUEAELE(Map<String, String> mapStatOfas, String numeroCaisse,
            List<Map<String, String>> listAlloc) {

        double getVal = 0.0;
        double getVal2 = 0.0;
        double getVal3 = 0.0;
        double getVal4 = 0.0;

        for (Map<String, String> mapAlloc : listAlloc) {

            if (ALCSDroit.TYPE_ENF.equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC))) {

                getVal += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_NB_ENFANTS));
                getVal2 += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_SOMME_GLOBALE));
            }

            else if (ALCSDroit.TYPE_FORM
                    .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC))) {

                getVal3 += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_NB_ENFANTS));
                getVal4 += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_SOMME_GLOBALE));

            }
        }

        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_44B_MAP_STAT_OFAS, String.valueOf(getVal));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_45B_MAP_STAT_OFAS, String.valueOf(getVal2));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_46B_MAP_STAT_OFAS, String.valueOf(getVal3));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_47B_MAP_STAT_OFAS, String.valueOf(getVal4));
    }

    private void compterNbSommeAllocSuisseOutUEAELE(Map<String, String> mapStatOfas, String numeroCaisse,
            List<Map<String, String>> listAlloc) {

        double getVal = 0.0;
        double getVal2 = 0.0;
        double getVal3 = 0.0;
        double getVal4 = 0.0;

        for (Map<String, String> mapAlloc : listAlloc) {

            if (ALCSDroit.TYPE_ENF.equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC))) {

                getVal += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_NB_ENFANTS));
                getVal2 += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_SOMME_GLOBALE));
            }

            else if (ALCSDroit.TYPE_FORM
                    .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC))) {

                getVal3 += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_NB_ENFANTS));
                getVal4 += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_SOMME_GLOBALE));

            }
        }

        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_44C_MAP_STAT_OFAS, String.valueOf(getVal));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_45C_MAP_STAT_OFAS, String.valueOf(getVal2));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_46C_MAP_STAT_OFAS, String.valueOf(getVal3));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_47C_MAP_STAT_OFAS, String.valueOf(getVal4));
    }

    private void compterNombreAllocationAdoptionNaissance(Map<String, String> mapStatOfas, String numeroCaisse,
            List<Map<String, String>> listAllocAdoptionNaissanceSalaries) {
        double getVal = 0.0;
        double getVal2 = 0.0;
        double getVal3 = 0.0;

        for (Map<String, String> mapAlloc : listAllocAdoptionNaissanceSalaries) {

            if (ALCSDossier.ACTIVITE_SALARIE
                    .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ACTIVITE_ALLOC))) {

                getVal += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));
            }

            if (ALCSDossier.ACTIVITE_INDEPENDANT
                    .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ACTIVITE_ALLOC))) {

                getVal2 += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));
            }

            if (ALCSDossier.ACTIVITE_NONACTIF
                    .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ACTIVITE_ALLOC))) {

                getVal3 += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));
            }
        }
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_37A_MAP_STAT_OFAS, String.valueOf(getVal));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_37B_MAP_STAT_OFAS, String.valueOf(getVal2));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_37C_MAP_STAT_OFAS, String.valueOf(getVal3));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_37D_MAP_STAT_OFAS, String.valueOf(getVal + getVal2 + getVal3));
    }

    private void compterNombreBeneficiaireAllocationEnfants(Map<String, String> mapStatOfas, String numeroCaisse,
            List<Map<String, String>> listAllocEnfants) {

        double getVal = 0.0;
        double getVal2 = 0.0;
        double getVal3 = 0.0;
        double nbA = 0.0;
        double nbB = 0.0;
        double nbC = 0.0;
        double nbAlloc;

        List<String> listeClesAllocsEnfants = new ArrayList<String>();

        for (Map<String, String> mapAlloc : listAllocEnfants) {
            // Création de la clé composé des informations pour un enfant
            String idEnfant = mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ID_ENFANT);
            String typeAllocation = mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC);
            String numAff = mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_NUMERO_AFFILIE);
            String keyActivite = mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ACTIVITE_ALLOC);
            String genreAllocation = mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_STATUT_PRESTATION);
            String cleAllocsEnfant = idEnfant + typeAllocation + numAff + keyActivite + genreAllocation;
            if (!listeClesAllocsEnfants.contains(cleAllocsEnfant)) {
                listeClesAllocsEnfants.add(cleAllocsEnfant);
                nbAlloc = 1;
            } else {
                nbAlloc = 0;
            }

            if (ALCSDroit.TYPE_ENF.equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC))) {
                if (ALCSDossier.ACTIVITE_SALARIE
                        .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ACTIVITE_ALLOC))) {
                    nbA += nbAlloc;
                    getVal += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));
                }
                if (ALCSDossier.ACTIVITE_INDEPENDANT
                        .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ACTIVITE_ALLOC))) {
                    nbB += nbAlloc;
                    getVal2 += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));
                }
                if (ALCSDossier.ACTIVITE_NONACTIF
                        .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ACTIVITE_ALLOC))) {
                    nbC += nbAlloc;
                    getVal3 += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));
                }
            }
        }
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_35A_MAP_STAT_OFAS, String.valueOf(nbA));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_35B_MAP_STAT_OFAS, String.valueOf(nbB));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_35C_MAP_STAT_OFAS, String.valueOf(nbC));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_35D_MAP_STAT_OFAS, String.valueOf(nbA + nbB + nbC));

    }

    private void compterPrestationStructureCaisse(Map<String, String> mapStatOfas, String numeroCaisse,
            List<Map<String, String>> listAffilies) {

        double getVal = 0.0;
        double getVal2 = 0.0;
        double getVal3 = 0.0;

        for (Map<String, String> mapAffilies : listAffilies) {

            if (CodeSystem.GENRE_ASS_PARITAIRE
                    .equals(mapAffilies.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_GENRE_ASS))
                    || CodeSystem.TYPE_AFFILI_TSE
                            .equals(mapAffilies.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_TYPE_AFF))
                    || CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE
                            .equals(mapAffilies.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_TYPE_AFF))) {

                getVal += sommerValue(mapAffilies.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));
            }

            else if (CodeSystem.GENRE_ASS_PERSONNEL
                    .equals(mapAffilies.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_GENRE_ASS))) {

                if (CodeSystem.TYPE_AFFILI_INDEP_EMPLOY
                        .equals(mapAffilies.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_TYPE_AFF))
                        || CodeSystem.TYPE_AFFILI_INDEP
                                .equals(mapAffilies.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_TYPE_AFF))) {

                    getVal2 += sommerValue(mapAffilies.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));

                }

                if (CodeSystem.TYPE_AFFILI_NON_ACTIF
                        .equals(mapAffilies.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_TYPE_AFF))) {

                    getVal3 += sommerValue(mapAffilies.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));

                }

            }
        }
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_026A_MAP_STAT_OFAS, String.valueOf(getVal));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_026B_MAP_STAT_OFAS, String.valueOf(getVal2));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_026C_MAP_STAT_OFAS, String.valueOf(getVal3));
    }

    private void compterSommeAllocFamilialesADIADC(Map<String, String> mapStatOfas, String numeroCaisse,
            List<Map<String, String>> listAlloc) {

        double getVal = 0.0;
        double getVal2 = 0.0;
        for (Map<String, String> mapAlloc : listAlloc) {

            if (ALCSDroit.TYPE_ENF.equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC))
                    && ALCSPrestation.STATUT_ADC
                            .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_STATUT))) {
                getVal += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));
            }
            if (ALCSDroit.TYPE_ENF.equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC))
                    && ALCSPrestation.STATUT_ADI
                            .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_STATUT))) {
                getVal2 += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));
            }
        }

        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_40F_MAP_STAT_OFAS, String.valueOf(getVal));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_40G_MAP_STAT_OFAS, String.valueOf(getVal2));
    }

    private void compterSommeAllocFamilialesAdoptionNaissanceAudelaMontantsCantonaux(Map<String, String> mapStatOfas,
            String numeroCaisse, List<Map<String, String>> listAlloc) {

        double getVal = 0.0;

        for (Map<String, String> mapAlloc : listAlloc) {

            if (ALCSDroit.TYPE_NAIS.equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC))
                    || ALCSDroit.TYPE_ACCE
                            .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC))) {
                getVal += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));
            }
        }

        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_42E_MAP_STAT_OFAS, String.valueOf(getVal));
    }

    private void compterSommeAllocFamilialesAudelaMontantsCantonaux(Map<String, String> mapStatOfas,
            String numeroCaisse, List<Map<String, String>> listAlloc) {

        double getVal = 0.0;

        for (Map<String, String> mapAlloc : listAlloc) {

            if (ALCSDroit.TYPE_ENF.equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC))
                    && mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_CS_UNIT)
                            .equals(String.valueOf(61080003))) {

                getVal += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));

            }
        }

        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_40E_MAP_STAT_OFAS, String.valueOf(getVal));
    }

    private void compterSommeAllocFamilialesFormProfADIADC(Map<String, String> mapStatOfas, String numeroCaisse,
            List<Map<String, String>> listAlloc) {

        double getVal = 0.0;
        double getVal2 = 0.0;

        for (Map<String, String> mapAlloc : listAlloc) {

            if (ALCSDroit.TYPE_FORM.equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC))
                    && ALCSPrestation.STATUT_ADC
                            .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_STATUT))) {
                getVal += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));
            }
            if (ALCSDroit.TYPE_FORM.equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC))
                    && ALCSPrestation.STATUT_ADI
                            .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_STATUT))) {
                getVal2 += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));
            }
        }

        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_41F_MAP_STAT_OFAS, String.valueOf(getVal));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_41G_MAP_STAT_OFAS, String.valueOf(getVal2));
    }

    private void compterSommeAllocFamilialesFormProfAudelaMontantsCantonaux(Map<String, String> mapStatOfas,
            String numeroCaisse, List<Map<String, String>> listAlloc) {

        double getVal = 0.0;

        for (Map<String, String> mapAlloc : listAlloc) {

            if (ALCSDroit.TYPE_FORM.equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC))
                    && mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_CS_UNIT)
                            .equals(String.valueOf(61080003))) {

                getVal += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));

            }
        }

        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_41E_MAP_STAT_OFAS, String.valueOf(getVal));
    }

    private void compterSommeAllocFamilialesNaissanceAdoptionADI(Map<String, String> mapStatOfas, String numeroCaisse,
            List<Map<String, String>> listAlloc) {

        double getVal = 0.0;

        for (Map<String, String> mapAlloc : listAlloc) {

            if (ALCSDroit.TYPE_NAIS.equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC))
                    || ALCSDroit.TYPE_ACCE
                            .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC))) {
                getVal += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));
            }
        }

        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_42F_MAP_STAT_OFAS, String.valueOf(getVal));
    }

    private void compterSommeAllocFamilialesRegimeAttribution(Map<String, String> mapStatOfas, String numeroCaisse,
            List<Map<String, String>> listAlloc) {

        double getVal = 0.0;
        double getVal2 = 0.0;
        double getVal3 = 0.0;
        double getVal4 = 0.0;
        double getVal5 = 0.0;
        double getVal6 = 0.0;
        double getVal7 = 0.0;
        double getVal8 = 0.0;
        double getVal9 = 0.0;

        for (Map<String, String> mapAlloc : listAlloc) {

            if (ALCSDroit.TYPE_ENF.equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC))) {
                if (ALCSDossier.ACTIVITE_SALARIE
                        .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ACTIVITE_ALLOC))) {
                    getVal += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));
                }
                if (ALCSDossier.ACTIVITE_INDEPENDANT
                        .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ACTIVITE_ALLOC))) {
                    getVal2 += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));
                }
                if (ALCSDossier.ACTIVITE_NONACTIF
                        .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ACTIVITE_ALLOC))) {
                    getVal3 += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));
                }
            }
            if (ALCSDroit.TYPE_FORM.equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC))) {
                if (ALCSDossier.ACTIVITE_SALARIE
                        .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ACTIVITE_ALLOC))) {
                    getVal4 += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));
                }
                if (ALCSDossier.ACTIVITE_INDEPENDANT
                        .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ACTIVITE_ALLOC))) {
                    getVal5 += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));
                }
                if (ALCSDossier.ACTIVITE_NONACTIF
                        .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ACTIVITE_ALLOC))) {
                    getVal6 += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));
                }
            }

            if (ALCSDroit.TYPE_NAIS.equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC))
                    || ALCSDroit.TYPE_ACCE
                            .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC))) {

                if (ALCSDossier.ACTIVITE_SALARIE
                        .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ACTIVITE_ALLOC))) {
                    getVal7 += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));
                }
                if (ALCSDossier.ACTIVITE_INDEPENDANT
                        .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ACTIVITE_ALLOC))) {
                    getVal8 += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));
                }
                if (ALCSDossier.ACTIVITE_NONACTIF
                        .equals(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ACTIVITE_ALLOC))) {
                    getVal9 += sommerValue(mapAlloc.get(ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL));
                }

            }
        }
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_40A_MAP_STAT_OFAS, String.valueOf(getVal));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_40B_MAP_STAT_OFAS, String.valueOf(getVal2));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_40C_MAP_STAT_OFAS, String.valueOf(getVal3));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_40D_MAP_STAT_OFAS, String.valueOf(getVal + getVal2 + getVal3));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_41A_MAP_STAT_OFAS, String.valueOf(getVal4));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_41B_MAP_STAT_OFAS, String.valueOf(getVal5));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_41C_MAP_STAT_OFAS, String.valueOf(getVal6));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_41D_MAP_STAT_OFAS, String.valueOf(getVal4 + getVal5 + getVal6));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_42A_MAP_STAT_OFAS, String.valueOf(getVal7));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_42B_MAP_STAT_OFAS, String.valueOf(getVal8));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_42C_MAP_STAT_OFAS, String.valueOf(getVal9));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_42D_MAP_STAT_OFAS, String.valueOf(getVal7 + getVal8 + getVal9));
    }

    private void compterTotalIntermediaireA(Map<String, String> mapStatOfas, String numeroCaisse) throws SQLException {

        double getValue = 0.0;

        for (java.util.Map.Entry<String, String> entry : mapStatOfas.entrySet()) {
            String cle = entry.getKey();
            String valeur = entry.getValue();

            if (cle == ALStatistiquesOfasProcess.KEY_35D_MAP_STAT_OFAS) {
                getValue += Double.valueOf(valeur);
            }
            if (cle == ALStatistiquesOfasProcess.KEY_36D_MAP_STAT_OFAS) {
                getValue += Double.valueOf(valeur);
            }
            if (cle == ALStatistiquesOfasProcess.KEY_37D_MAP_STAT_OFAS) {
                getValue += Double.valueOf(valeur);
            }
        }

        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_38A_MAP_STAT_OFAS, String.valueOf(getValue));

    }

    private void compterTotalIntermediaireB(Map<String, String> mapStatOfas, String numeroCaisse) throws SQLException {

        double getValue = 0.0;

        for (java.util.Map.Entry<String, String> entry : mapStatOfas.entrySet()) {
            String cle = entry.getKey();
            String valeur = entry.getValue();

            if (cle == ALStatistiquesOfasProcess.KEY_40D_MAP_STAT_OFAS) {
                getValue += Double.valueOf(valeur);
            }
            if (cle == ALStatistiquesOfasProcess.KEY_41D_MAP_STAT_OFAS) {
                getValue += Double.valueOf(valeur);
            }
            if (cle == ALStatistiquesOfasProcess.KEY_42D_MAP_STAT_OFAS) {
                getValue += Double.valueOf(valeur);
            }
        }

        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_43A_MAP_STAT_OFAS, String.valueOf(getValue));

    }

    private void compterTotalIntermediaireC(Map<String, String> mapStatOfas, String numeroCaisse) throws SQLException {

        double getValue = 0.0;
        double getValue2 = 0.0;
        double getValue3 = 0.0;
        double getValue4 = 0.0;

        for (java.util.Map.Entry<String, String> entry : mapStatOfas.entrySet()) {
            String cle = entry.getKey();
            String valeur = entry.getValue();

            if (cle == ALStatistiquesOfasProcess.KEY_44A_MAP_STAT_OFAS) {
                getValue += Double.valueOf(valeur);
            }
            if (cle == ALStatistiquesOfasProcess.KEY_44B_MAP_STAT_OFAS) {
                getValue += Double.valueOf(valeur);
            }
            if (cle == ALStatistiquesOfasProcess.KEY_44C_MAP_STAT_OFAS) {
                getValue += Double.valueOf(valeur);
            }

            if (cle == ALStatistiquesOfasProcess.KEY_45A_MAP_STAT_OFAS) {
                getValue2 += Double.valueOf(valeur);
            }

            if (cle == ALStatistiquesOfasProcess.KEY_45B_MAP_STAT_OFAS) {
                getValue2 += Double.valueOf(valeur);
            }

            if (cle == ALStatistiquesOfasProcess.KEY_45C_MAP_STAT_OFAS) {
                getValue2 += Double.valueOf(valeur);
            }

            if (cle == ALStatistiquesOfasProcess.KEY_46A_MAP_STAT_OFAS) {
                getValue3 += Double.valueOf(valeur);
            }

            if (cle == ALStatistiquesOfasProcess.KEY_46B_MAP_STAT_OFAS) {
                getValue3 += Double.valueOf(valeur);
            }

            if (cle == ALStatistiquesOfasProcess.KEY_46C_MAP_STAT_OFAS) {
                getValue3 += Double.valueOf(valeur);
            }

            if (cle == ALStatistiquesOfasProcess.KEY_47A_MAP_STAT_OFAS) {
                getValue4 += Double.valueOf(valeur);
            }

            if (cle == ALStatistiquesOfasProcess.KEY_47B_MAP_STAT_OFAS) {
                getValue4 += Double.valueOf(valeur);
            }

            if (cle == ALStatistiquesOfasProcess.KEY_47C_MAP_STAT_OFAS) {
                getValue4 += Double.valueOf(valeur);
            }
        }

        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_44D_MAP_STAT_OFAS, String.valueOf(getValue));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_45D_MAP_STAT_OFAS, String.valueOf(getValue2));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_46D_MAP_STAT_OFAS, String.valueOf(getValue3));
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_47D_MAP_STAT_OFAS, String.valueOf(getValue4));

    }

    private void creerDocumentStatistiqueOfasCSV(String numeroCaisse, List<String> listCSVLine, String canton)
            throws Exception {
        String filePath = "";
        String fileName = "";

        // Si une caisse est précisée le nom change
        if (JadeStringUtil.isEmpty(numeroCaisse)) {
            fileName = ALStatistiquesOfasProcess.CSV_STATISTIQUE_OFAS_OUTPUT_FILE_NAME_SANS_MULTICAISSE
                    + ALStatistiquesOfasProcess.CSV_CANTON_NAME + canton + "_" + annee
                    + ALStatistiquesOfasProcess.CSV_EXT;
        } else {
            fileName = ALStatistiquesOfasProcess.CSV_STATISTIQUE_OFAS_OUTPUT_FILE_NAME + numeroCaisse
                    + ALStatistiquesOfasProcess.CSV_CANTON_NAME + canton + "_" + annee
                    + ALStatistiquesOfasProcess.CSV_EXT;
        }

        filePath = Jade.getInstance().getPersistenceDir() + JadeFilenameUtil.addFilenameSuffixUID(fileName);

        JadeFsFacade.writeFile(getLignesInByteFormat(listCSVLine), filePath);
        publierDocumentStatistiqueOfasCSV(filePath);
    }

    private void creerListCSVLine(String numeroCaisse, List<String> listCSVLine, Map<String, String> mapStatOfas) {
        addALineInListCSVLine(
                getSession().getLabel(ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "POSTE_STATISTIQUE"),
                getSession().getLabel(ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "VALEUR"),
                listCSVLine);

        addALineInListCSVLine(
                getSession()
                        .getLabel(ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "DONNEES_PRESTATIONS"),
                " ", listCSVLine);

        addALineInListCSVLine(
                getSession().getLabel(
                        ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "STRUCTURE_CAISSE_EXPLOITATION"),
                " ", listCSVLine);

        addALineInListCSVLine(
                getSession().getLabel(ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "STRUCTURE_CAISSE"),
                " ", listCSVLine);

        if (isBMS) {
            addALineInListCSVLine(getSession().getLabel(
                    ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "NBRE_ENTREPRISE_AFFILIEES_BMS"), " ",
                    listCSVLine);
        } else {
            addALineInListCSVLine(
                    getSession().getLabel(
                            ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "NBRE_ENTREPRISE_AFFILIEES"),
                    " ", listCSVLine);
        }
        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_026A_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_026B_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_026C_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addALineInListCSVLine(
                getSession().getLabel(
                        ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "DONNEES_COMPTE_EXPLOITATION"),
                " ", listCSVLine);

        addALineInListCSVLine(
                getSession().getLabel(ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "RECETTES"), " ",
                listCSVLine);

        addALineInListCSVLine(
                getSession().getLabel(
                        ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "CONTRIBUTION_EMPLOYEURS"),
                " ", listCSVLine);

        addALineInListCSVLine(
                getSession().getLabel(
                        ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "CONTRIBUTION_INDEPENDANTS"),
                " ", listCSVLine);

        addALineInListCSVLine(
                getSession()
                        .getLabel(ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "CONTRIBUTION_NON_ACTIF"),
                " ", listCSVLine);

        addALineInListCSVLine(
                getSession().getLabel(
                        ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "CONTRIBUTION_POUVOIRS_PUBLICS"),
                " ", listCSVLine);

        addALineInListCSVLine(
                getSession().getLabel(
                        ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "RECETTE_CDE_COMPENSATION_CHARGES"),
                " ", listCSVLine);

        addALineInListCSVLine(
                getSession().getLabel(
                        ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "DISSOLUTION_RESERVES_FLUCTUATION"),
                " ", listCSVLine);

        addALineInListCSVLine(
                getSession().getLabel(ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "AUTRES_RECETTES"),
                " ", listCSVLine);

        addALineInListCSVLine(
                getSession().getLabel(ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "TOTAL_RECETTES"),
                " ", listCSVLine);

        addALineInListCSVLine(
                getSession().getLabel(ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "DEPENSES"), " ",
                listCSVLine);

        addALineInListCSVLine(
                getSession()
                        .getLabel(ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "ALLOCATIONS_FAMILIALES"),
                " ", listCSVLine);

        addALineInListCSVLine(
                getSession().getLabel(ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "AUTRES_PRESTATIONS"),
                " ", listCSVLine);

        addALineInListCSVLine(
                getSession().getLabel(
                        ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "VERSEMENT_COMPENSATION_CHARGES"),
                " ", listCSVLine);

        addALineInListCSVLine(getSession().getLabel(
                ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "CONSTITUTION_RESERVES_FLUCTUATION"), " ",
                listCSVLine);

        addALineInListCSVLine(getSession().getLabel(
                ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "FRAIS_ADMINISTRATION_MISE_EN_OEUVRE"), " ",
                listCSVLine);

        addALineInListCSVLine(
                getSession().getLabel(ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "AUTRES_DEPENSES"),
                " ", listCSVLine);

        addALineInListCSVLine(
                getSession().getLabel(ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "RESULTAT_ANNEE"),
                " ", listCSVLine);

        addALineInListCSVLine(
                getSession().getLabel(ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "TOTAL_DEPENSES"),
                " ", listCSVLine);

        addALineInListCSVLine(getSession().getLabel(
                ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "DONNEES_BENEFICIAIRES_ET_PRESTATION"), " ",
                listCSVLine);

        addALineInListCSVLine(getSession().getLabel(ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART
                + "NOMBRE_BENEFICIAIRES_ET_ALLOCATION_PAR_REGIME_ATTRIBUTION"), " ", listCSVLine);

        if (isBMS) {
            addALineInListCSVLine(getSession().getLabel(
                    ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "NOMBRE_ALLOCATION_POUR_ENFANTS_BMS"),
                    " ", listCSVLine);
        } else {
            addALineInListCSVLine(getSession().getLabel(
                    ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "NOMBRE_ALLOCATION_POUR_ENFANTS"), " ",
                    listCSVLine);
        }

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_35A_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_35B_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_35C_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_35D_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_35E_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_35F_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        if (isBMS) {
            addALineInListCSVLine(getSession().getLabel(ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART
                    + "NOMBRE_ALLOCATION_FORMATION_PROFESSIONNELLES_BMS"), " ", listCSVLine);
        } else {
            addALineInListCSVLine(getSession().getLabel(ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART
                    + "NOMBRE_ALLOCATION_FORMATION_PROFESSIONNELLES"), " ", listCSVLine);
        }

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_36A_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_36B_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_36C_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_36D_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_36E_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_36F_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        if (isBMS) {
            addALineInListCSVLine(getSession().getLabel(ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART
                    + "NOMBRE_ALLOCATION_NAISSANCE_ADOPTION_BMS"), " ", listCSVLine);
        } else {
            addALineInListCSVLine(getSession().getLabel(
                    ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "NOMBRE_ALLOCATION_NAISSANCE_ADOPTION"),
                    " ", listCSVLine);
        }

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_37A_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_37B_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_37C_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_37D_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_37E_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_38A_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addALineInListCSVLine(getSession().getLabel(ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART
                + "NOMBRE_BENEFICIAIRE_ALLOCATION_ENFANT_ET_OU_FORMATION_PROFESSIONNELLE"), " ", listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_39A_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_39B_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_39C_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_39D_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        if (isBMS) {
            addALineInListCSVLine(getSession().getLabel(ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART
                    + "SOMME_ALLOCATION_FAMILIALES_PAR_REGIME_ATTRIBUTION_BMS"), " ", listCSVLine);
        } else {
            addALineInListCSVLine(getSession().getLabel(ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART
                    + "SOMME_ALLOCATION_FAMILIALES_PAR_REGIME_ATTRIBUTION"), " ", listCSVLine);
        }
        addALineInListCSVLine(
                getSession().getLabel(
                        ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "ALLOCATIONS_POUR_ENFANTS"),
                " ", listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_40A_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_40B_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_40C_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_40D_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_40E_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_40F_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_40G_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addALineInListCSVLine(
                getSession().getLabel(ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "ALLOC_FORM_PROF"),
                " ", listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_41A_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_41B_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_41C_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_41D_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_41E_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_41F_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_41G_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addALineInListCSVLine(getSession().getLabel(
                ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "SOMME_ALLOCATION_NAISSANCE_ADOPTION"), " ",
                listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_42A_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_42B_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_42C_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_42D_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_42E_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_42F_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_43A_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addALineInListCSVLine(getSession().getLabel(ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART
                + "NOMBRE_ET_SOMME_ALLOCATION_SELON_LIEU_RESIDENCE_ENFANT"), " ", listCSVLine);

        addALineInListCSVLine(
                getSession().getLabel(
                        ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "NOMBRE_ALLOCATIONS_POUR_ENFANTS"),
                " ", listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_44A_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_44B_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_44C_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_44D_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addALineInListCSVLine(
                getSession().getLabel(
                        ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "SOMME_ALLOCATIONS_POUR_ENFANTS"),
                " ", listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_45A_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_45B_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_45C_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_45D_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addALineInListCSVLine(
                getSession().getLabel(
                        ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "NOMBRE_ALLOCATION_FORMATION_PROF"),
                " ", listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_46A_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_46B_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_46C_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_46D_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addALineInListCSVLine(
                getSession().getLabel(
                        ALStatistiquesOfasProcess.LABEL_STATISTIQUE_OFAS_FIX_PART + "SOMME_ALLOCATION_FORMATION_PROF"),
                " ", listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_47A_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_47B_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_47C_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

        addAStatistiqueLineInListCSVLine(ALStatistiquesOfasProcess.KEY_47D_MAP_STAT_OFAS, mapStatOfas, listCSVLine);

    }

    private List<Map<String, String>> executeQuery(String sql) throws JadePersistenceException {

        Statement stmt = null;
        ResultSet resultSet = null;
        List<Map<String, String>> results = new ArrayList<Map<String, String>>();

        try {
            stmt = JadeThread.currentJdbcConnection().createStatement();
            resultSet = stmt.executeQuery(sql);

            ResultSetMetaData md = resultSet.getMetaData();
            int columns = md.getColumnCount();

            while (resultSet.next()) {
                Map<String, String> row = new HashMap<String, String>();

                // Attention ! La première colonne du Resultset est 1 et non 0
                for (int i = 1; i <= columns; i++) {
                    row.put(md.getColumnName(i), resultSet.getString(i));
                }

                results.add(row);
            }

        } catch (SQLException e) {
            throw new JadePersistenceException(
                    getName() + " - " + "Unable to execute query (" + sql + "), a SQLException happend!", e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    JadeLogger.warn(ALStatistiquesOfasProcess.class,
                            "Problem to close statement in ALStatistiquesOfasProcess, reason : " + e.toString());
                }
            }

        }

        return results;
    }

    private List<Map<String, String>> fillListAllocationADIADC(String numeroCaisse, String canton)
            throws JadePersistenceException {
        String sqlQueryListageTotalAllocationsADIADC = getSqlNbAllocationsADIADC(numeroCaisse, canton);
        List<Map<String, String>> listMapResultQueryListageAllocationADIADC = executeQuery(
                sqlQueryListageTotalAllocationsADIADC);
        return listMapResultQueryListageAllocationADIADC;
    }

    private List<Map<String, String>> fillListNBAffilies(String numeroCaisse, String canton)
            throws JadePersistenceException {
        String sqlQueryListageTotalNBAffilies = getSqlNBAffilies(numeroCaisse, canton);
        List<Map<String, String>> listMapResultQueryListageNBAffilies = executeQuery(sqlQueryListageTotalNBAffilies);
        return listMapResultQueryListageNBAffilies;
    }

    private List<Map<String, String>> fillListNBAllocation(String numeroCaisse, String canton)
            throws JadePersistenceException {
        String sqlQueryListageTotalAllocations = getSqlNbAllocationsNew(numeroCaisse, canton);
        List<Map<String, String>> listMapResultQueryListageAllocation = executeQuery(sqlQueryListageTotalAllocations);
        return listMapResultQueryListageAllocation;
    }

    private List<Map<String, String>> fillListNBAllocationAdoptionNaissance(String numeroCaisse, String canton)
            throws JadePersistenceException {
        String sqlQueryListageTotalAllocationsAdoptionNaissance = getSqlNbAllocAdoptionNaissance(numeroCaisse, canton);
        List<Map<String, String>> listMapResultQueryListageAllocationAdoptionNaissance = executeQuery(
                sqlQueryListageTotalAllocationsAdoptionNaissance);
        return listMapResultQueryListageAllocationAdoptionNaissance;
    }

    private List<Map<String, String>> fillListNBAllocationAdoptionNaissanceADI(String numeroCaisse, String canton)
            throws JadePersistenceException {
        String sqlQueryListageTotalAllocationsAdoptionNaissanceADI = getSqlNbAllocationsAdoptionNaissanceADI(
                numeroCaisse, canton);
        List<Map<String, String>> listMapResultQueryListageAllocationAdoptionNaissanceADI = executeQuery(
                sqlQueryListageTotalAllocationsAdoptionNaissanceADI);
        return listMapResultQueryListageAllocationAdoptionNaissanceADI;
    }

    private List<Map<String, String>> fillListNBBenefAllocEnfantFormationProf(String numeroCaisse, String canton)
            throws JadePersistenceException {
        String sqlQueryListageNBBenefAllocEnfantFormationProf = getSqlNbBeneficiairesAllocationsEnfantFormationProf(
                numeroCaisse, canton);
        List<Map<String, String>> listMapResultQueryListageNBBenefAllocEnfantFormationProf = executeQuery(
                sqlQueryListageNBBenefAllocEnfantFormationProf);
        return listMapResultQueryListageNBBenefAllocEnfantFormationProf;
    }

    private List<Map<String, String>> fillListNBSommeAllocationOutUEAELE(String numeroCaisse, String canton)
            throws JadePersistenceException {
        String sqlQueryListageNBSommeAllocationOutUEAELE = getSqlSommeAllocationsENFduesPremiereFoisOUTUEAELE(
                numeroCaisse, canton);
        List<Map<String, String>> listMapResultQueryListageNBSommeAllocationOutUEAELE = executeQuery(
                sqlQueryListageNBSommeAllocationOutUEAELE);
        return listMapResultQueryListageNBSommeAllocationOutUEAELE;
    }

    private List<Map<String, String>> fillListNBSommeAllocationUEAELE(String numeroCaisse, String canton)
            throws JadePersistenceException {
        String sqlQueryListageNBSommeAllocationUEAELE = getSqlSommeAllocationsENFduesPremiereFoisInUEAELE(numeroCaisse,
                canton);
        List<Map<String, String>> listMapResultQueryListageNBSommeAllocationUEAELE = executeQuery(
                sqlQueryListageNBSommeAllocationUEAELE);
        return listMapResultQueryListageNBSommeAllocationUEAELE;
    }

    private List<Map<String, String>> fillListSommeAllocENFduesPremiereFois(String numeroCaisse, String canton)
            throws JadePersistenceException {
        String sqlQueryListageSommeAllocENFduesPremiereFois = getSqlSommeAllocationsENFduesPremiereFoisSuisse(
                numeroCaisse, canton);
        List<Map<String, String>> listMapResultQueryListageSommeAllocENFduesPremiereFois = executeQuery(
                sqlQueryListageSommeAllocENFduesPremiereFois);
        return listMapResultQueryListageSommeAllocENFduesPremiereFois;
    }

    private List<Map<String, String>> fillListSommeAllocFamiliales(String numeroCaisse, String canton)
            throws JadePersistenceException {
        String sqlQueryListageSommeAllocFamiliales = getSqlSommeAllocations(numeroCaisse, canton);
        List<Map<String, String>> listMapResultQueryListageSommeAllocFamiliales = executeQuery(
                sqlQueryListageSommeAllocFamiliales);
        return listMapResultQueryListageSommeAllocFamiliales;
    }

    private List<Map<String, String>> fillListSommeAllocFamilialesADIADC(String numeroCaisse, String canton)
            throws JadePersistenceException {
        String sqlQueryListageSommeAllocFamilialesADIADC = getNewSqlSommeAllocationsADIADC(numeroCaisse, canton);
        List<Map<String, String>> listMapResultQueryListageSommeAllocFamilialesADIADC = executeQuery(
                sqlQueryListageSommeAllocFamilialesADIADC);
        return listMapResultQueryListageSommeAllocFamilialesADIADC;
    }

    private List<Map<String, String>> fillListSommeAllocFamilialesAudelaMontantsCantonaux(String numeroCaisse,
            String canton) throws JadePersistenceException {
        String sqlQueryListageSommeAllocFamilialesADI = getSqlSommeAllocationsAudelaMontantsCantonaux(numeroCaisse,
                canton);
        List<Map<String, String>> listMapResultQueryListageSommeAllocFamilialesADI = executeQuery(
                sqlQueryListageSommeAllocFamilialesADI);
        return listMapResultQueryListageSommeAllocFamilialesADI;
    }

    private void fillMapStatOfas(Map<String, String> mapStatOfas) {

        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_026A_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_026B_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_026C_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_35A_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_35B_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_35C_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_35D_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_35E_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_36A_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_36B_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_36C_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_36D_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_36E_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_37A_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_37B_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_37C_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_37D_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_37E_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_38A_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_39A_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_39B_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_39C_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_39D_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_40A_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_40B_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_40C_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_40D_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_40E_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_40F_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_40G_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_41A_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_41B_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_41C_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_41D_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_41E_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_41F_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_42A_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_42B_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_42C_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_42D_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_42E_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_42F_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_43A_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_44A_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_44B_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_44C_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_44D_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_45A_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_45B_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_45C_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_45D_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_46A_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_46B_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_46C_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_46D_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_47A_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_47B_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_47C_MAP_STAT_OFAS, "0");
        mapStatOfas.put(ALStatistiquesOfasProcess.KEY_47D_MAP_STAT_OFAS, "0");

    }

    public String getAnnee() {
        return annee;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("STAT_TITRE_MAIL_ERROR");
        } else {
            return getSession().getLabel("STAT_TITRE_MAIL");
        }
    }

    private List<Byte> getLignesInByteFormat(List<String> lignes) {
        List<Byte> lignesInByteFormat = new ArrayList<Byte>();

        if (lignes != null) {
            for (String aLigne : lignes) {
                for (byte aByte : aLigne.getBytes()) {
                    lignesInByteFormat.add(Byte.valueOf(aByte));
                }
            }
        }

        return lignesInByteFormat;
    }

    private String getStartDate(boolean fullDate) {
        if (isBMS) {
            if (fullDate) {
                return annee + startMonthBMS + startDay;
            } else {
                return annee + startMonthBMS;
            }
        } else {
            if (fullDate) {
                return annee + startMonth + startDay;
            } else {
                return annee + startMonth;
            }
        }
    }

    private String getEndDate(boolean fullDate) {
        if (isBMS) {
            if (fullDate) {
                return anneeFinBMS + endMonthBMS + endDay;
            } else {
                return anneeFinBMS + endMonthBMS;
            }
        } else {
            if (fullDate) {
                return annee + endMonth + endDay;
            } else {
                return annee + endMonth;
            }
        }
    }

    private String getSqlCantons(String numeroCaisse) {
        String sql = "";
        sql = " SELECT " + " trim(pcouid) AS " + ALStatistiquesOfasProcess.REQUETE_CANTONS + " FROM " + " ( "
                + " SELECT " + " csCan.PCOUID " + " FROM schema.aldetpre det LEFT OUTER "
                + " JOIN schema.alentpre ent ON ent.mid = det.mid LEFT OUTER "
                + " JOIN schema.aldos dos ON dos.eid = ent.eid LEFT OUTER "
                + " JOIN schema.aldroit dro ON dro.fid = det.fid LEFT OUTER "
                + " JOIN schema.alenfant enf ON enf.cid = dro.cid LEFT OUTER "
                + " JOIN schema.titierp ti ON ti.htitie = enf.htitie LEFT OUTER "
                + " JOIN schema.alalloc alloc ON alloc.bid = dos.bid LEFT OUTER "
                + " JOIN schema.titierp tia on tia.htitie=alloc.htitie left outer "
                + " join schema.fwcoup csCan on (ent.CSCANT = csCan.PCOSID and csCan.PLAIDE = 'F') "
                + " inner join schema.alparam param on param.pparva = det.numcpt " + " WHERE " + " ( " + " ( " + " ( "
                + " mdvc BETWEEN " + getStartDate(true) + " AND " + getEndDate(true) + " AND ent.cstatu <> 61230003 "
                + " ) " + " OR (nvalid BETWEEN " + getStartDate(false) + " AND " + getEndDate(false)
                + " AND ent.cstatu = 61230003)) " + " AND det.cstcai <> " + ALCSTarif.CATEGORIE_SUP_HORLO + "  ";
        if (isSansRestitutions) {
            sql += " AND ( numCpt not like '____.46%' ) ";
        }
        if (!JadeStringUtil.isBlank(numeroCaisse)) {
            sql += " AND ( ppacdi like 'rubrique.multicaisse." + numeroCaisse + ".%' ) ";
        }
        sql += " ) " + " group by csCan.PCOUID " + " ) as REQUEST " + " group by PCOUID ";

        sql = replaceSchemaInSqlQuery(sql);
        return sql;
    }

    private String getSqlNBAffilies(String numeroCaisse, String canton) {
        String sql = "SELECT ppacdi as " + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_PARAM + ", mattaf as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_TYPE_AFF + ", mbtgen as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_GENRE_ASS + ", mbtcan" + ", count(*) as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL
                + " from (select ppacdi, mattaf, mbtgen, malnaf, ass.mbtcan" + " FROM schema.afaffip aff "
                + "INNER JOIN schema.afplafp pla on (aff.maiaff=pla.maiaff) "
                + "INNER JOIN schema.afcotip cot on (pla.muipla=cot.muipla) "
                + "INNER JOIN schema.afassup ass on (cot.mbiass=ass.mbiass) "
                + "INNER JOIN schema.carubrp ru on (ass.MBIRUB = ru.idrubrique) "
                + "inner join schema.cacptap ca on (aff.malnaf=ca.idexternerole and aff.htitie=ca.idtiers and ca.idrole in (517002, 517039, 517040)) "
                + "inner join schema.caoperp op on (ca.idcompteannexe=op.idcompteannexe and op.etat=205002 and op.idcompte=ru.idrubrique) "
                + "inner join schema.cajourp jo on (op.idjournal=jo.idjournal and jo.datevaleurcg between "
                + getStartDate(true) + " and " + getEndDate(true) + ") "
                + "INNER JOIN schema.fwcoup cs ON cs.pcosid = ass.mbtcan and cs.PLAIDE ='F'"
                + "left outer join schema.alparam param on param.pparva=ru.idexterne AND param.ppacdi LIKE 'rubrique.multicaisse.%'"
                + "WHERE ass.mbttyp = 812002 ";

        if (isSansRestitutions) {
            sql += " AND ( idexterne not like '____.46%' ) ";
        }
        if (!JadeStringUtil.isBlank(numeroCaisse)) {
            sql += " AND ( ppacdi like 'rubrique.multicaisse." + numeroCaisse + ".%' ) ";
        }

        sql += " AND aff.maddeb <= " + getEndDate(true) + " AND (aff.madfin >= " + getStartDate(true)
                + " OR aff.madfin = 0) " + " AND cot.meddeb <= " + getEndDate(true)
                + " AND mattaf not in ( 804009, 804013) " + " AND (cot.medfin = 0 or (cot.medfin >= "
                + getStartDate(true) + "))" + " AND PCOUID = " + "'" + canton + "'"
                + " GROUP BY ppacdi,mattaf,mbtgen,malnaf,ass.mbtcan having sum(op.montant) <> 0) as temp"
                + " GROUP BY ppacdi,mattaf,mbtgen,mbtcan ";

        sql = replaceSchemaInSqlQuery(sql);
        return sql;
    }

    private String getSqlNbAllocAdoptionNaissance(String numeroCaisse, String canton) {
        String sql = "SELECT ppacdi as " + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_PARAM
                + ", cast(substr(CAST(nvalid AS CHAR(6)),1,4)as int)" + ", count(distinct(nid)) as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL + ", cscaal as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ACTIVITE_ALLOC + " FROM schema.aldetpre det LEFT OUTER "
                + "JOIN schema.alentpre ent ON ent.mid = det.mid LEFT OUTER "
                + "JOIN schema.aldroit dro ON dro.fid=det.fid LEFT OUTER "
                + "JOIN schema.aldos dos ON dos.eid=dro.eid LEFT OUTER "
                + "JOIN schema.fwcoup cs ON "
                + "((cs.pcosid=ent.cscant and (det.cstcan = 0 or det.cstcan = 61190097)) "
                + "or (cs.pcosid=det.cstcan and (det.cstcan <> 0 and det.cstcan <> 61190097))) "
                + " and cs.PLAIDE = 'F' "
                + "left outer join schema.alparam param on param.pparva=det.numcpt AND param.ppacdi LIKE 'rubrique.multicaisse.%'"
                + " WHERE nmont > 0 AND " + "det.cstcai <> " + ALCSTarif.CATEGORIE_SUP_HORLO + " ";
        if (isSansRestitutions) {
            sql += " AND ( numCpt not like '____.46%' ) ";
        }
        if (!JadeStringUtil.isBlank(numeroCaisse)) {
            sql += " AND ( ppacdi like 'rubrique.multicaisse." + numeroCaisse + ".%' ) ";
        }
        sql += " AND (nvalid between " + getStartDate(false) + " AND " + getEndDate(false) + ")" + " AND cstype  IN ("
                + ALCSDroit.TYPE_NAIS + ", " + ALCSDroit.TYPE_ACCE + ")" + " AND cscaal IN ("
                + ALCSDossier.ACTIVITE_SALARIE + ", " + ALCSDossier.ACTIVITE_NONACTIF + ", "
                + ALCSDossier.ACTIVITE_INDEPENDANT + ")" + " AND ((mdvc between " + getStartDate(true) + " AND "
                + getEndDate(true) + ") or (ent.cstatu = 61230003)) " + " AND PCOUID = " + "'" + canton + "'"
                + " group by cscaal, ppacdi, cast(substr(CAST(nvalid AS CHAR(6)),1,4)as int) "
                + " order by cscaal, ppacdi, cast(substr(CAST(nvalid AS CHAR(6)),1,4)as int) ";
        sql = replaceSchemaInSqlQuery(sql);
        return sql;
    }

    private String getSqlNbAllocations(String numeroCaisse, String canton) {
        String sql = "SELECT ppacdi as " + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_PARAM
                + ", cast(substr(CAST(nvalid AS CHAR(6)),1,4)as int)" + ", count(*) as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL + ", cstype as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC + ", cscaal as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ACTIVITE_ALLOC + " FROM schema.aldetpre det LEFT OUTER "
                + "JOIN schema.alentpre ent ON ent.mid = det.mid LEFT OUTER "
                + "JOIN schema.aldroit dro ON dro.fid=det.fid LEFT OUTER "
                + "JOIN schema.aldos dos ON dos.eid=dro.eid LEFT OUTER "
                + "JOIN schema.fwcoup cs ON cs.pcosid=ent.cscant and cs.PLAIDE = 'F' "
                + "left outer join schema.alparam param on param.pparva=det.numcpt AND param.ppacdi LIKE 'rubrique.multicaisse.%'"
                + " WHERE nmont > 0 AND " + "det.cstcai <> " + ALCSTarif.CATEGORIE_SUP_HORLO + " ";
        if (isSansRestitutions) {
            sql += " AND ( numCpt not like '____.46%' ) ";
        }
        if (!JadeStringUtil.isBlank(numeroCaisse)) {
            sql += " AND ( ppacdi like 'rubrique.multicaisse." + numeroCaisse + ".%' ) ";
        }
        sql += " AND nvalid = " + getEndDate(false) + " AND cstype  IN (" + ALCSDroit.TYPE_ENF + ", "
                + ALCSDroit.TYPE_FORM + ")" + " AND cscaal IN (" + ALCSDossier.ACTIVITE_SALARIE + ", "
                + ALCSDossier.ACTIVITE_NONACTIF + ", " + ALCSDossier.ACTIVITE_INDEPENDANT + ")" + " AND ((mdvc between "
                + getStartDate(true) + " AND " + getEndDate(true) + ") or (ent.cstatu = 61230003)) " + " AND PCOUID = "
                + "'" + canton + "'"
                + " group by cstype, cscaal, ppacdi, cast(substr(CAST(nvalid AS CHAR(6)),1,4)as int) "
                + " order by cstype, cscaal, ppacdi, cast(substr(CAST(nvalid AS CHAR(6)),1,4)as int) ";
        sql = replaceSchemaInSqlQuery(sql);
        return sql;
    }

    private String getSqlNbAllocationsADIADC(String numeroCaisse, String canton) {
        String sql = "SELECT ppacdi as " + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_PARAM + ", count(*) as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL + ", cstype as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC + ", cscaal as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ACTIVITE_ALLOC + ", ent.CSTATU as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_STATUT + ", enf.CID as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ENFANT + " FROM schema.aldetpre det LEFT OUTER "
                + "JOIN schema.alentpre ent ON ent.mid = det.mid LEFT OUTER "
                + "JOIN schema.aldroit dro ON dro.fid=det.fid LEFT OUTER "
                + "JOIN schema.alenfant enf ON enf.cid = dro.cid LEFT OUTER "
                + "JOIN schema.aldos dos ON dos.eid=dro.eid LEFT OUTER "
                + "JOIN schema.fwcoup cs ON cs.pcosid=ent.cscant and cs.PLAIDE = 'F' "
                + "left outer join schema.alparam param on param.pparva=det.numcpt AND param.ppacdi LIKE 'rubrique.multicaisse.%'"
                + " WHERE nmont > 0 AND " + "det.cstcai <> " + ALCSTarif.CATEGORIE_SUP_HORLO + " ";
        if (isSansRestitutions) {
            sql += " AND ( numCpt not like '____.46%' ) ";
        }
        if (!JadeStringUtil.isBlank(numeroCaisse)) {
            sql += " AND ( ppacdi like 'rubrique.multicaisse." + numeroCaisse + ".%' ) ";
        }
        sql += " AND cstype  IN (" + ALCSDroit.TYPE_ENF + ", " + ALCSDroit.TYPE_FORM + ")" + " AND cscaal IN ("
                + ALCSDossier.ACTIVITE_SALARIE + ", " + ALCSDossier.ACTIVITE_INDEPENDANT + ", "
                + ALCSDossier.ACTIVITE_NONACTIF + ")" + " AND ent.cstatu IN (61230003, 61230002)" + " AND mdvc between "
                + getStartDate(true) + " AND " + getEndDate(true) + " AND PCOUID = " + "'" + canton + "'"
                + " group by cstype,cscaal, ppacdi,ent.CSTATU,enf.CID  " + " order by cstype, ppacdi ";
        sql = replaceSchemaInSqlQuery(sql);
        return sql;
    }

    private String getSqlNbAllocationsNew(String numeroCaisse, String canton) {
        String sql = "SELECT ppacdi as COL_PARAM, cstype as COL_CSTYPE,"
                + "cscaal as COL_CSCAAL,enf.CID as ID_ENFANT, det.cscata as TYPE_LOI,dos.MALNAF as NUM_AFFILIE,"
                + "ent.EID as COL_EID,ent.CSTATU AS GENRE_ALLOCATION"
                + " FROM schema.aldetpre det LEFT OUTER JOIN schema.alentpre ent ON ent.mid = det.mid "
                + "LEFT OUTER JOIN schema.aldroit dro ON dro.fid=det.fid "
                + "LEFT OUTER JOIN schema.alenfant enf ON enf.cid = dro.cid "
                + "LEFT OUTER JOIN schema.titierp ti ON ti.htitie = enf.htitie "
                + "LEFT OUTER JOIN schema.aldos dos ON dos.eid=dro.eid "
                + "LEFT OUTER JOIN schema.fwcoup cs ON cs.pcosid=ent.cscant " + "and cs.PLAIDE = 'F' "
                + "left outer join schema.alparam param on param.pparva=det.numcpt"
                + " AND param.ppacdi LIKE 'rubrique.multicaisse.%'" + " WHERE nmont > 0"
                + " AND det.cstcai <> 61190096";
        if (isSansRestitutions) {
            sql += " AND ( numCpt not like '____.46%' ) ";
        }
        if (!JadeStringUtil.isBlank(numeroCaisse)) {
            sql += " AND ( ppacdi like 'rubrique.multicaisse." + numeroCaisse + ".%' ) ";
        }
        sql += " AND ( numCpt not like '____.46%' )" + " AND cstype IN (61150002, 61150003)"
                + " AND cscaal IN (61040005, 61040004, 61040003)" + " AND ((mdvc between " + getStartDate(true)
                + " AND " + getEndDate(true) + "))" + "AND PCOUID = '" + canton + "'"
                + " group by cstype, cscaal, ppacdi,dos.EID,enf.CID,ti.HTLDE2,dos.cscaal,det.cscata,ent.EID,ent.CSTATU,dos.MALNAF"
                + " order by cstype, cscaal, ppacdi,dos.EID";
        sql = replaceSchemaInSqlQuery(sql);
        return sql;
    }

    private String getSqlNbAllocationsAdoptionNaissanceADI(String numeroCaisse, String canton) {
        String sql = "SELECT ppacdi as " + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_PARAM
                + ", cast(substr(CAST(nvalid AS CHAR(6)),1,4)as int)" + ", count(distinct(nid)) as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL + " FROM schema.aldetpre det LEFT OUTER "
                + "JOIN schema.alentpre ent ON ent.mid = det.mid LEFT OUTER "
                + "JOIN schema.aldroit dro ON dro.fid=det.fid LEFT OUTER "
                + "JOIN schema.aldos dos ON dos.eid=dro.eid LEFT OUTER "
                + "JOIN schema.fwcoup cs ON "
                + "((cs.pcosid=ent.cscant and (det.cstcan = 0 or det.cstcan = 61190097)) "
                + "or (cs.pcosid=det.cstcan and (det.cstcan <> 0 and det.cstcan <> 61190097))) "
                + " and cs.PLAIDE = 'F' "
                + "left outer join schema.alparam param on param.pparva=det.numcpt AND param.ppacdi LIKE 'rubrique.multicaisse.%'"
                + " WHERE nmont > 0 AND " + "det.cstcai <> " + ALCSTarif.CATEGORIE_SUP_HORLO + " ";
        if (isSansRestitutions) {
            sql += " AND ( numCpt not like '____.46%' ) ";
        }
        if (!JadeStringUtil.isBlank(numeroCaisse)) {
            sql += " AND ( ppacdi like 'rubrique.multicaisse." + numeroCaisse + ".%' ) ";
        }
        sql += " AND (nvalid between " + getStartDate(false) + " AND " + getEndDate(false) + ")" + " AND cstype  IN ("
                + ALCSDroit.TYPE_NAIS + ", " + ALCSDroit.TYPE_ACCE + ")" + " AND cscaal IN ("
                + ALCSDossier.ACTIVITE_SALARIE + ", " + ALCSDossier.ACTIVITE_INDEPENDANT + ", "
                + ALCSDossier.ACTIVITE_NONACTIF + ")" + " AND ((ent.cstatu = 61230003)"
                + " OR (ent.cstatu = 61230002 and mdvc between " + getStartDate(true) + " AND " + getEndDate(true)
                + "))" + " AND PCOUID = " + "'" + canton + "'"
                + " group by ppacdi, cast(substr(CAST(nvalid AS CHAR(6)),1,4)as int) "
                + " order by ppacdi, cast(substr(CAST(nvalid AS CHAR(6)),1,4)as int) ";
        sql = replaceSchemaInSqlQuery(sql);
        return sql;
    }

    private String getSqlNbBeneficiairesAllocationsEnfantFormationProf(String numeroCaisse, String canton) {
        String sql = "SELECT ppacdi as " + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_PARAM + ", dos.BID as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ID_ALLOCATAIRE + ", cscaal as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ACTIVITE_ALLOC + ", dos.MALNAF as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_NUMERO_AFFILIE + " FROM schema.aldetpre det LEFT OUTER "
                + "JOIN schema.alentpre ent ON ent.mid = det.mid LEFT OUTER "
                + "JOIN schema.aldroit dro ON dro.fid=det.fid LEFT OUTER "
                + "JOIN schema.aldos dos ON dos.eid=dro.eid LEFT OUTER "
                + "JOIN schema.fwcoup cs ON "
                + "((cs.pcosid=ent.cscant and (det.cstcan = 0 or det.cstcan = 61190097)) "
                + "or (cs.pcosid=det.cstcan and (det.cstcan <> 0 and det.cstcan <> 61190097))) "
                + " and cs.PLAIDE = 'F' "
                + "left outer join schema.alparam param on param.pparva=det.numcpt AND param.ppacdi LIKE 'rubrique.multicaisse.%'"
                + " WHERE nmont > 0 AND " + "det.cstcai <> " + ALCSTarif.CATEGORIE_SUP_HORLO + " ";
        if (isSansRestitutions) {
            sql += " AND ( numCpt not like '____.46%' ) ";
        }
        if (!JadeStringUtil.isBlank(numeroCaisse)) {
            sql += " AND ( ppacdi like 'rubrique.multicaisse." + numeroCaisse + ".%' ) ";
        }
        sql += " AND cstype  IN (" + ALCSDroit.TYPE_ENF + ", " + ALCSDroit.TYPE_FORM + ")" + " AND cscaal IN ("
                + ALCSDossier.ACTIVITE_SALARIE + ", " + ALCSDossier.ACTIVITE_NONACTIF + ", "
                + ALCSDossier.ACTIVITE_INDEPENDANT + ")" + " AND ((mdvc between " + getStartDate(true) + " AND "
                + getEndDate(true) + ") or (ent.cstatu = 61230003)) " + " AND PCOUID = " + "'" + canton + "'"
                + " group by cscaal, ppacdi,dos.BID, dos.MALNAF" + " order by cscaal, ppacdi";
        sql = replaceSchemaInSqlQuery(sql);
        return sql;
    }

    /**
     * @return
     */
    private String getSqlNombreCaissesAF() {
        String sql = " select ppacdi as " + ALStatistiquesOfasProcess.REQUETE_CAISSES_AF + " from " + " schema.alparam "
                + " where ppacdi like '%rubrique.multicaisse%' " + " order by ppacdi ";

        sql = replaceSchemaInSqlQuery(sql);
        return sql;
    }

    private String getSqlSommeAllocations(String numeroCaisse, String canton) {
        String sql = "SELECT  ppacdi as " + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_PARAM
                + ", cast(substr(CAST(nvalid AS CHAR(6)),1,4)as int)" + ", sum(nmont) as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL + ", cstype as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC + ", cscaal as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ACTIVITE_ALLOC + " FROM schema.aldetpre det LEFT OUTER "
                + "JOIN schema.alentpre ent ON ent.mid = det.mid LEFT OUTER "
                + "JOIN schema.aldos dos ON dos.eid=ent.eid LEFT OUTER "
                + "JOIN schema.fwcoup cs ON cs.pcosid=ent.cscant and cs.PLAIDE = 'F' "
                + "left outer join schema.alparam param on param.pparva=det.numcpt AND param.ppacdi LIKE 'rubrique.multicaisse.%'"
                + " WHERE cstype  IN (" + ALCSDroit.TYPE_ENF + ", " + ALCSDroit.TYPE_FORM + ", " + ALCSDroit.TYPE_ACCE
                + ", " + ALCSDroit.TYPE_NAIS + ")" + " AND cscaal IN (" + ALCSDossier.ACTIVITE_SALARIE + ", "
                + ALCSDossier.ACTIVITE_NONACTIF + ", " + ALCSDossier.ACTIVITE_INDEPENDANT + ") AND " + "det.cstcai <> "
                + ALCSTarif.CATEGORIE_SUP_HORLO + " ";
        if (isSansRestitutions) {
            sql += " AND ( numCpt not like '____.46%' ) ";
        }
        if (!JadeStringUtil.isBlank(numeroCaisse)) {
            sql += " AND ( ppacdi like 'rubrique.multicaisse." + numeroCaisse + ".%' ) ";
        }
        sql += " AND mdvc between " + getStartDate(true) + " AND " + getEndDate(true) + " AND PCOUID = " + "'" + canton
                + "'" + " group by cstype, cscaal, ppacdi, cast(substr(CAST(nvalid AS CHAR(6)),1,4)as int) "
                + " order by cstype, cscaal, ppacdi, cast(substr(CAST(nvalid AS CHAR(6)),1,4)as int) ";
        sql = replaceSchemaInSqlQuery(sql);
        return sql;
    }

    private String getSqlSommeAllocationsADI(String numeroCaisse, String canton) {
        String sql = "SELECT  ppacdi as " + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_PARAM
                + ", cast(substr(CAST(nvalid AS CHAR(6)),1,4)as int)" + ", sum(nmont) as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL + ", cstype as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC + ", cscaal as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ACTIVITE_ALLOC + " FROM schema.aldetpre det LEFT OUTER "
                + "JOIN schema.alentpre ent ON ent.mid = det.mid LEFT OUTER "
                + "JOIN schema.aldos dos ON dos.eid=ent.eid LEFT OUTER "
                + "JOIN schema.fwcoup cs ON cs.pcosid=ent.cscant and cs.PLAIDE = 'F' "
                + "left outer join schema.alparam param on param.pparva=det.numcpt AND param.ppacdi LIKE 'rubrique.multicaisse.%'"
                + " WHERE cstype  IN (" + ALCSDroit.TYPE_ENF + ", " + ALCSDroit.TYPE_FORM + ", " + ALCSDroit.TYPE_ACCE
                + ", " + ALCSDroit.TYPE_NAIS + ")" + " AND cscaal IN (" + ALCSDossier.ACTIVITE_SALARIE + ", "
                + ALCSDossier.ACTIVITE_NONACTIF + ", " + ALCSDossier.ACTIVITE_INDEPENDANT + ") AND " + "det.cstcai <> "
                + ALCSTarif.CATEGORIE_SUP_HORLO + " ";
        if (isSansRestitutions) {
            sql += " AND ( numCpt not like '____.46%' ) ";
        }
        if (!JadeStringUtil.isBlank(numeroCaisse)) {
            sql += " AND ( ppacdi like 'rubrique.multicaisse." + numeroCaisse + ".%' ) ";
        }
        sql += " AND mdvc between " + getStartDate(true) + " AND " + getEndDate(true)
                + " AND ((ent.cstatu=61230003) OR (ent.cstatu=61230002))" + " AND PCOUID = " + "'" + canton + "'"
                + " group by cstype, cscaal, ppacdi, cast(substr(CAST(nvalid AS CHAR(6)),1,4)as int) "
                + " order by cstype, cscaal, ppacdi, cast(substr(CAST(nvalid AS CHAR(6)),1,4)as int) ";
        sql = replaceSchemaInSqlQuery(sql);
        return sql;
    }

    private String getNewSqlSommeAllocationsADIADC(String numeroCaisse, String canton) {
        String sql = "SELECT  ppacdi as " + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_PARAM
                + ", cast(substr(CAST(nvalid AS CHAR(6)),1,4)as int)" + ", sum(nmont) as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL + ", cstype as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC + ", cscaal as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ACTIVITE_ALLOC + ", ent.CSTATU as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_STATUT + " FROM schema.aldetpre det LEFT OUTER "
                + "JOIN schema.alentpre ent ON ent.mid = det.mid LEFT OUTER "
                + "JOIN schema.aldos dos ON dos.eid=ent.eid LEFT OUTER "
                + "JOIN schema.fwcoup cs ON cs.pcosid=ent.cscant and cs.PLAIDE = 'F' "
                + "left outer join schema.alparam param on param.pparva=det.numcpt AND param.ppacdi LIKE 'rubrique.multicaisse.%'"
                + " WHERE cstype  IN (" + ALCSDroit.TYPE_ENF + ", " + ALCSDroit.TYPE_FORM + ", " + ALCSDroit.TYPE_ACCE
                + ", " + ALCSDroit.TYPE_NAIS + ")" + " AND cscaal IN (" + ALCSDossier.ACTIVITE_SALARIE + ", "
                + ALCSDossier.ACTIVITE_NONACTIF + ", " + ALCSDossier.ACTIVITE_INDEPENDANT + ") AND " + "det.cstcai <> "
                + ALCSTarif.CATEGORIE_SUP_HORLO + " ";
        if (isSansRestitutions) {
            sql += " AND ( numCpt not like '____.46%' ) ";
        }
        if (!JadeStringUtil.isBlank(numeroCaisse)) {
            sql += " AND ( ppacdi like 'rubrique.multicaisse." + numeroCaisse + ".%' ) ";
        }
        sql += " AND mdvc between " + getStartDate(true) + " AND " + getEndDate(true)
                + " AND ((ent.cstatu=61230003) OR (ent.cstatu=61230002))" + " AND PCOUID = " + "'" + canton + "'"
                + " group by cstype, cscaal, ppacdi, cast(substr(CAST(nvalid AS CHAR(6)),1,4)as int), ent.CSTATU "
                + " order by cstype, cscaal, ppacdi, cast(substr(CAST(nvalid AS CHAR(6)),1,4)as int) ";
        sql = replaceSchemaInSqlQuery(sql);
        return sql;
    }

    private String getSqlSommeAllocationsAudelaMontantsCantonaux(String numeroCaisse, String canton) {
        String sql = "SELECT  ppacdi as " + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_PARAM
                + ", cast(substr(CAST(nvalid AS CHAR(6)),1,4)as int)" + ", sum(nmont-nmocan) as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_VAL + ", cstype as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC + ", cscaal as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_ACTIVITE_ALLOC + ", ent.csunit as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_CS_UNIT + " FROM schema.aldetpre det LEFT OUTER "
                + "JOIN schema.alentpre ent ON ent.mid = det.mid LEFT OUTER "
                + "JOIN schema.aldos dos ON dos.eid=ent.eid LEFT OUTER "
                + "JOIN schema.fwcoup cs ON cs.pcosid=ent.cscant and cs.PLAIDE = 'F' "
                + "left outer join schema.alparam param on param.pparva=det.numcpt AND param.ppacdi LIKE 'rubrique.multicaisse.%'"
                + " WHERE cstype  IN (" + ALCSDroit.TYPE_ENF + ", " + ALCSDroit.TYPE_FORM + ", " + ALCSDroit.TYPE_ACCE
                + ", " + ALCSDroit.TYPE_NAIS + ")" + " AND cscaal IN (" + ALCSDossier.ACTIVITE_SALARIE + ", "
                + ALCSDossier.ACTIVITE_NONACTIF + ", " + ALCSDossier.ACTIVITE_INDEPENDANT + ") AND " + "det.cstcai <> "
                + ALCSTarif.CATEGORIE_SUP_HORLO + " ";
        if (isSansRestitutions) {
            sql += " AND ( numCpt not like '____.46%' ) ";
        }
        if (!JadeStringUtil.isBlank(numeroCaisse)) {
            sql += " AND ( ppacdi like 'rubrique.multicaisse." + numeroCaisse + ".%' ) ";
        }
        sql += " AND mdvc between " + getStartDate(true) + " AND " + getEndDate(true)
                + " AND ent.cstatu <> 61230002 AND ent.cstatu<>61230003" + " AND nmont>0 and nmont>nmocan "
                + " AND PCOUID = " + "'" + canton + "'"
                + " group by cstype, ent.csunit, cscaal, ppacdi, cast(substr(CAST(nvalid AS CHAR(6)),1,4)as int) "
                + " order by cstype, ent.csunit, cscaal, ppacdi, cast(substr(CAST(nvalid AS CHAR(6)),1,4)as int) ";
        sql = replaceSchemaInSqlQuery(sql);
        return sql;
    }

    private String getSqlSommeAllocationsENFduesPremiereFoisInUEAELE(String numeroCaisse, String canton) {
        String sql = "SELECT  ppacdi as " + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_PARAM
                + ", count(distinct(enf.cid)) as " + ALStatistiquesOfasProcess.REQUETE_NB_ENFANTS
                + ", (sum(nmont)/count(distinct(det.nid)))*12*count(distinct(enf.cid)) as "
                + ALStatistiquesOfasProcess.REQUETE_SOMME_GLOBALE + ", cstype as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC + " FROM schema.aldroit dro "
                + "INNER JOIN schema.alenfant enf ON enf.cid=dro.cid "
                + "LEFT OUTER JOIN schema.aldetpre det ON dro.fid=det.fid "
                + "LEFT OUTER JOIN schema.alentpre ent ON ent.mid=det.mid "
                + "LEFT OUTER JOIN schema.aldos dos ON dos.eid=ent.eid "
                + "LEFT OUTER JOIN schema.fwcoup cs ON cs.pcosid=ent.cscant and cs.PLAIDE = 'F' "
                + "left outer join schema.alparam param on param.pparva=det.numcpt AND param.ppacdi LIKE 'rubrique.multicaisse.%'"
                + " WHERE mdvc between " + getStartDate(true) + " AND " + getEndDate(true) + " AND " + "det.cstcai <> "
                + ALCSTarif.CATEGORIE_SUP_HORLO + " ";
        if (isSansRestitutions) {
            sql += " AND ( numCpt not like '____.46%' ) ";
        }
        if (!JadeStringUtil.isBlank(numeroCaisse)) {
            sql += " AND ( ppacdi like 'rubrique.multicaisse." + numeroCaisse + ".%' ) ";
        }
        sql += " AND cstype  IN (" + ALCSDroit.TYPE_ENF + ", " + ALCSDroit.TYPE_FORM + ")" + " AND mdvc between "
                + getStartDate(true) + " AND " + getEndDate(true) + " AND PCOUID = " + "'" + canton + "'"
                + " AND enf.hnipay IN (207,229,204,205,242,206,236,260,211,212,214,240,216,218,261,262,223,224,227,230,231,232,215,243,251,234,244,217,222,228)"
                + " AND enf.cid NOT IN " + "(SELECT dro2.cid from schema.aldroit dro2 "
                + "INNER JOIN schema.aldetpre det2 on dro2.fid=det2.fid " + "WHERE nvalid < " + getStartDate(false)
                + " AND " + "det2.cstcai <> " + ALCSTarif.CATEGORIE_SUP_HORLO + ") " + " group by cstype, ppacdi"
                + " order by cstype, ppacdi";
        sql = replaceSchemaInSqlQuery(sql);
        return sql;
    }

    private String getSqlSommeAllocationsENFduesPremiereFoisOUTUEAELE(String numeroCaisse, String canton) {
        String sql = "SELECT  ppacdi as " + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_PARAM
                + ", count(distinct(enf.cid)) as " + ALStatistiquesOfasProcess.REQUETE_NB_ENFANTS
                + ", (sum(nmont)/count(distinct(det.nid)))*12*count(distinct(enf.cid)) as "
                + ALStatistiquesOfasProcess.REQUETE_SOMME_GLOBALE + ", cstype as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC + " FROM schema.aldroit dro "
                + "INNER JOIN schema.alenfant enf ON enf.cid=dro.cid "
                + "LEFT OUTER JOIN schema.aldetpre det ON dro.fid=det.fid "
                + "LEFT OUTER JOIN schema.alentpre ent ON ent.mid=det.mid "
                + "LEFT OUTER JOIN schema.aldos dos ON dos.eid=ent.eid "
                + "LEFT OUTER JOIN schema.fwcoup cs ON cs.pcosid=ent.cscant and cs.PLAIDE = 'F' "
                + "left outer join schema.alparam param on param.pparva=det.numcpt AND param.ppacdi LIKE 'rubrique.multicaisse.%'"
                + " WHERE mdvc between " + getStartDate(true) + " AND " + getEndDate(true) + " AND " + "det.cstcai <> "
                + ALCSTarif.CATEGORIE_SUP_HORLO + " ";
        if (isSansRestitutions) {
            sql += " AND ( numCpt not like '____.46%' ) ";
        }
        if (!JadeStringUtil.isBlank(numeroCaisse)) {
            sql += " AND ( ppacdi like 'rubrique.multicaisse." + numeroCaisse + ".%' ) ";
        }
        sql += " AND cstype  IN (" + ALCSDroit.TYPE_ENF + ", " + ALCSDroit.TYPE_FORM + ")" + " AND mdvc between "
                + getStartDate(true) + " AND " + getEndDate(true) + " AND PCOUID = " + "'" + canton + "'"
                + " AND enf.hnipay NOT IN (100,207,229,204,205,242,206,236,260,211,212,214,240,216,218,261,262,223,224,227,230,231,232,215,243,251,234,244,217,222,228)"
                + " AND enf.cid NOT IN " + "(SELECT dro2.cid from schema.aldroit dro2 "
                + "INNER JOIN schema.aldetpre det2 on dro2.fid=det2.fid " + "WHERE nvalid < " + getStartDate(false)
                + " AND " + "det2.cstcai <> " + ALCSTarif.CATEGORIE_SUP_HORLO + ") " + " group by cstype, ppacdi"
                + " order by cstype, ppacdi";
        sql = replaceSchemaInSqlQuery(sql);
        return sql;
    }

    private String getSqlSommeAllocationsENFduesPremiereFoisSuisse(String numeroCaisse, String canton) {
        String sql = "SELECT  ppacdi as " + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_PARAM + ", (sum(nmont)) as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_SUM_NMONT + ", count(distinct(enf.cid)) as "
                + ALStatistiquesOfasProcess.REQUETE_NB_ENFANTS
                + ", (sum(nmont)/count(distinct(det.nid)))*12*count(distinct(enf.cid)) as "
                + ALStatistiquesOfasProcess.REQUETE_SOMME_GLOBALE + ", cstype as "
                + ALStatistiquesOfasProcess.REQUETE_AF_COL_NAME_TYPE_ALLOC + " FROM schema.aldroit dro "
                + "INNER JOIN schema.alenfant enf ON enf.cid=dro.cid "
                + "LEFT OUTER JOIN schema.aldetpre det ON dro.fid=det.fid "
                + "LEFT OUTER JOIN schema.alentpre ent ON ent.mid=det.mid "
                + "LEFT OUTER JOIN schema.aldos dos ON dos.eid=ent.eid "
                + "LEFT OUTER JOIN schema.fwcoup cs ON cs.pcosid=ent.cscant and cs.PLAIDE = 'F' "
                + "left outer join schema.alparam param on param.pparva=det.numcpt AND param.ppacdi LIKE 'rubrique.multicaisse.%'"
                + " WHERE mdvc between " + getStartDate(true) + " AND " + getEndDate(true) + " AND " + "det.cstcai <> "
                + ALCSTarif.CATEGORIE_SUP_HORLO + " ";
        if (isSansRestitutions) {
            sql += " AND ( numCpt not like '____.46%' ) ";
        }
        if (!JadeStringUtil.isBlank(numeroCaisse)) {
            sql += " AND ( ppacdi like 'rubrique.multicaisse." + numeroCaisse + ".%' ) ";
        }
        sql += " AND cstype  IN (" + ALCSDroit.TYPE_ENF + ", " + ALCSDroit.TYPE_FORM + ")" + " AND mdvc between "
                + getStartDate(true) + " AND " + getEndDate(true) + " AND PCOUID = " + "'" + canton + "'"
                + " AND enf.hnipay=100" + " AND enf.cid NOT IN " + "(SELECT dro2.cid from schema.aldroit dro2 "
                + "INNER JOIN schema.aldetpre det2 on dro2.fid=det2.fid " + "WHERE nvalid < " + getStartDate(false)
                + " AND " + "det2.cstcai <> " + ALCSTarif.CATEGORIE_SUP_HORLO + ")" + " group by cstype, ppacdi"
                + " order by cstype, ppacdi";
        sql = replaceSchemaInSqlQuery(sql);
        return sql;
    }

    private JadeThreadContext initThreadContext(BSession session) throws Exception {
        JadeThreadContext context;
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(session.getApplicationId());
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());
        String[] roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                .findAllIdRoleForIdUser(session.getUserId());
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }
        context = new JadeThreadContext(ctxtImpl);
        context.storeTemporaryObject("bsession", session);

        return context;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    private List<String> listageCaissesAF() throws JadePersistenceException {
        List<String> listeCaisse = new ArrayList<String>();
        String sqlQueryComptageNombreCaissesAF = getSqlNombreCaissesAF();

        List<Map<String, String>> listMapResultQueryComptageNombreCaissesAF = executeQuery(
                sqlQueryComptageNombreCaissesAF);

        for (Map<String, String> aMapRowResultNombreCaisseAF : listMapResultQueryComptageNombreCaissesAF) {
            String nomParam = aMapRowResultNombreCaisseAF.get(ALStatistiquesOfasProcess.REQUETE_CAISSES_AF);
            nomParam = nomParam.substring(21, nomParam.length());
            // String complementCaisse = nomParam.substring(nomParam.indexOf('.') + 1);
            nomParam = nomParam.substring(0, nomParam.indexOf('.'));
            // nomParam = nomParam + "." + complementCaisse.substring(0, complementCaisse.indexOf('.'));
            if (!listeCaisse.contains(nomParam)) {
                listeCaisse.add(nomParam);
            }
        }

        return listeCaisse;
    }

    private List<String> listageCantons(String numeroCaisse) throws JadePersistenceException {
        List<String> listeCantons = new ArrayList<String>();
        String sqlQueryListageCantons = getSqlCantons(numeroCaisse);

        List<Map<String, String>> listMapResultQueryListageCantons = executeQuery(sqlQueryListageCantons);

        for (Map<String, String> aMapRowResultListageCantons : listMapResultQueryListageCantons) {
            String nomParam = aMapRowResultListageCantons.get(ALStatistiquesOfasProcess.REQUETE_CANTONS);

            if (!listeCantons.contains(nomParam)) {
                listeCantons.add(nomParam);
            }
        }

        return listeCantons;
    }

    private void publierDocumentStatistiqueOfasCSV(String CsvFilePath) throws IOException {

        // Publication du document
        JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
        docInfoExcel.setApplicationDomain(AFApplication.DEFAULT_APPLICATION_NAOS);
        docInfoExcel.setDocumentTitle(ALStatistiquesOfasProcess.CSV_STATISTIQUE_OFAS_OUTPUT_FILE_NAME);
        docInfoExcel.setPublishDocument(true);
        docInfoExcel.setArchiveDocument(false);
        this.registerAttachedDocument(docInfoExcel, CsvFilePath);

    }

    private void remplirMap() throws Exception {
        this.remplirMap(null);
    }

    private void remplirMap(String numeroCaisse) throws Exception {
        // @BMS-ONLY
        int startCounter = ALStatistiquesOfasProcess.currentIdxCaisse * ALStatistiquesOfasProcess.nbItemsByCaisses;
        int counter = startCounter;
        setProgressCounter(startCounter);

        List<String> listCantons = listageCantons(numeroCaisse);

        // @BMS-ONLY
        int nbFillLists = 13; // Fill methods inside for(){}
        int nbCantons = listCantons.size() * nbFillLists;
        int nbIncrement = nbCantons > 0 ? ALStatistiquesOfasProcess.nbItemsByCaisses / nbCantons : 0;

        for (String canton : listCantons) {
            List<String> listCSVLine = new ArrayList<String>();
            Map<String, String> mapStatOfas = new HashMap<String, String>();
            fillMapStatOfas(mapStatOfas);

            List<Map<String, String>> listNBAffilies = fillListNBAffilies(numeroCaisse, canton);
            counter += nbIncrement;
            setProgressCounter(counter); // 1

            List<Map<String, String>> listAllocations = fillListNBAllocation(numeroCaisse, canton);
            counter += nbIncrement;
            setProgressCounter(counter); // 2

            List<Map<String, String>> listAllocADIADC = fillListAllocationADIADC(numeroCaisse, canton);
            counter += nbIncrement;
            setProgressCounter(counter); // 3

            List<Map<String, String>> listAllocAdoptionNaissance = fillListNBAllocationAdoptionNaissance(numeroCaisse,
                    canton);
            counter += nbIncrement;
            setProgressCounter(counter); // 4

            List<Map<String, String>> listAllocAdoptionNaissanceADI = fillListNBAllocationAdoptionNaissanceADI(
                    numeroCaisse, canton);
            counter += nbIncrement;
            setProgressCounter(counter); // 5

            List<Map<String, String>> listNBBenefAllocEnfantFormationProf = fillListNBBenefAllocEnfantFormationProf(
                    numeroCaisse, canton);
            counter += nbIncrement;
            setProgressCounter(counter); // 6

            List<Map<String, String>> listSommeAllocFamiliales = fillListSommeAllocFamiliales(numeroCaisse, canton);
            counter += nbIncrement;
            setProgressCounter(counter); // 7

            List<Map<String, String>> listSommeAllocFamilialesAudelaMontantsCantonaux = fillListSommeAllocFamilialesAudelaMontantsCantonaux(
                    numeroCaisse, canton);
            counter += nbIncrement;
            setProgressCounter(counter); // 8

            List<Map<String, String>> listSommeAllocFamilialesADIADC = fillListSommeAllocFamilialesADIADC(numeroCaisse,
                    canton);
            counter += nbIncrement;
            setProgressCounter(counter); // 9

            List<Map<String, String>> listNBSommeAllocationOutUEAELE = fillListNBSommeAllocationOutUEAELE(numeroCaisse,
                    canton);
            counter += nbIncrement;
            setProgressCounter(counter); // 10

            List<Map<String, String>> listNBSommeAllocationUEAELE = fillListNBSommeAllocationUEAELE(numeroCaisse,
                    canton);
            counter += nbIncrement;
            setProgressCounter(counter); // 11

            List<Map<String, String>> listSommeAllocENFduesPremiereFois = fillListSommeAllocENFduesPremiereFois(
                    numeroCaisse, canton);
            counter += nbIncrement;
            setProgressCounter(counter); // 12

            // 026A + 026B + 026C
            compterPrestationStructureCaisse(mapStatOfas, numeroCaisse, listNBAffilies);
            // 35A + 35B + 35C + 35D
            compterNombreBeneficiaireAllocationEnfants(mapStatOfas, numeroCaisse, listAllocations);
            // 35E + 35F + 36E + 36F
            compterADIADCAllocEnfantsFormProf(mapStatOfas, numeroCaisse, listAllocADIADC);
            // 36A + 36B + 36C + 36D
            compterFormProfSalarieTSE(mapStatOfas, numeroCaisse, listAllocations);
            // 37A + 37B + 37C + 37D
            compterNombreAllocationAdoptionNaissance(mapStatOfas, numeroCaisse, listAllocAdoptionNaissance);
            // 37E
            compterADIAdoptionNaissance(mapStatOfas, numeroCaisse, listAllocAdoptionNaissanceADI);
            // 38A
            compterTotalIntermediaireA(mapStatOfas, numeroCaisse); // voir création d'une méthode : compter totaux
            // intermédiaires
            // 39A + 39B + 39C+ 39D
            compterNbBenefAllocEnfantFormationProf(mapStatOfas, numeroCaisse, listNBBenefAllocEnfantFormationProf);
            // 40A + 40B + 40C + 40D + 41A + 41B + 42A + 42B + 42C + 42D
            compterSommeAllocFamilialesRegimeAttribution(mapStatOfas, numeroCaisse, listSommeAllocFamiliales);
            // 40E
            compterSommeAllocFamilialesAudelaMontantsCantonaux(mapStatOfas, numeroCaisse,
                    listSommeAllocFamilialesAudelaMontantsCantonaux);
            // 40F+ 40G
            compterSommeAllocFamilialesADIADC(mapStatOfas, numeroCaisse, listSommeAllocFamilialesADIADC);

            // 41E
            compterSommeAllocFamilialesFormProfAudelaMontantsCantonaux(mapStatOfas, numeroCaisse,
                    listSommeAllocFamilialesAudelaMontantsCantonaux);
            // 41F+ 41G
            compterSommeAllocFamilialesFormProfADIADC(mapStatOfas, numeroCaisse, listSommeAllocFamilialesADIADC);

            // 42E
            compterSommeAllocFamilialesAdoptionNaissanceAudelaMontantsCantonaux(mapStatOfas, numeroCaisse,
                    listSommeAllocFamilialesAudelaMontantsCantonaux);
            // 42F
            compterSommeAllocFamilialesNaissanceAdoptionADI(mapStatOfas, numeroCaisse, listSommeAllocFamilialesADIADC);
            // 43A
            compterTotalIntermediaireB(mapStatOfas, numeroCaisse);
            // 44A et 45A + 46A et 47A
            compterNbSommeAllocSuisse(mapStatOfas, numeroCaisse, listSommeAllocENFduesPremiereFois);
            // 44B et 45B + 45B et 45B
            compterNbSommeAllocSuisseInUEAELE(mapStatOfas, numeroCaisse, listNBSommeAllocationUEAELE);
            // 44C et 45C + 46C et 47C
            compterNbSommeAllocSuisseOutUEAELE(mapStatOfas, numeroCaisse, listNBSommeAllocationOutUEAELE);
            // 44D et 45D + 46D et 47D
            compterTotalIntermediaireC(mapStatOfas, numeroCaisse);

            // avant de créer le csv changer le format des nombres dans la map StatOfas
            changeFormatMapToCurrency(mapStatOfas);
            creerListCSVLine(numeroCaisse, listCSVLine, mapStatOfas);
            creerDocumentStatistiqueOfasCSV(numeroCaisse, listCSVLine, canton);

            counter += nbIncrement;
            setProgressCounter(counter); // 13
        }

    }

    private String replaceSchemaInSqlQuery(String sqlQuery) {

        sqlQuery = sqlQuery.replaceAll("(?i)schema\\.", schemaDBWithTablePrefix);

        return sqlQuery;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
        // @BMS-ONLY: BMS have a different periode for stats, from 01.02.XXXX to 31.01.XXXX+1
        if (JadeStringUtil.isDigit(annee)) {
            Integer anneBMS = Integer.parseInt(annee);
            anneBMS++;
            anneeFinBMS = anneBMS.toString();
        }
    }

    private double sommerValue(String value) {
        if (value != null) {
            double getVal = 0.0;

            getVal += Double.valueOf(value);

            return getVal;
        } else {
            return 0.0;
        }

    }

}
