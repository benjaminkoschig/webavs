/*
 * Créé le 22 juil. 05
 */
package globaz.apg.acor.parser;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.module.calcul.APBaseCalcul;
import globaz.apg.module.calcul.APBaseCalculSituationProfessionnel;
import globaz.apg.module.calcul.APReferenceDataParser;
import globaz.apg.module.calcul.APResultatCalcul;
import globaz.apg.module.calcul.APResultatCalculSituationProfessionnel;
import globaz.apg.module.calcul.interfaces.IAPReferenceDataPrestation;
import globaz.apg.module.calcul.wrapper.APPeriodeWrapper;
import globaz.apg.module.calcul.wrapper.APPrestationWrapper;
import globaz.apg.module.calcul.wrapper.APPrestationWrapperComparator;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.api.IAFAffiliation;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.application.PRAbstractApplication;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.db.employeurs.PRAbstractEmployeur;
import globaz.prestation.file.parser.PRFileParserFactory;
import globaz.prestation.file.parser.PRTextField;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.IPRCache;
import globaz.prestation.tools.PRCacheFactory;
import globaz.prestation.tools.PRCalcul;
import globaz.prestation.tools.PRSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Classe permettant de lire le fichier annonce.pay généré par ACOR pour le calcul des prestations.
 * 
 * @author dvh
 */
public class APACORPrestationsParser {

    private static class EmployeurInfo {

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------

        private String cantonImposition = "";
        private String idAffilie = "";
        private String idTiers = "";
        private String noAffilie = "";
        private String nom = "";

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        public EmployeurInfo(BSession session, String noAffilie, String nom, String cantonImposition)
                throws PRACORException {
            this.noAffilie = noAffilie;
            this.nom = nom;
            this.cantonImposition = cantonImposition;

            // stocker les id pour les taux journaliers
            if (PRAbstractEmployeur.isNumeroBidon(noAffilie)) {
                idAffilie = "0"; // sauve dans la base puis recharge, donc 0
                idTiers = PRAbstractEmployeur.extractIdTiers(noAffilie);
            } else {
                try {
                    AFAffiliationManager mgr = new AFAffiliationManager();
                    mgr.setSession((BSession) PRSession.connectSession(session, AFApplication.DEFAULT_APPLICATION_NAOS));
                    mgr.setForAffilieNumero(noAffilie);
                    IPRAffilie affilie = null;

                    /*
                     * *if (IAFAffiliation.TYPE_AFFILI_INDEP.equals(af.getTypeAffiliation())) { nomAffilie = "3) " +
                     * nomAffilie; } else if (IAFAffiliation.TYPE_AFFILI_INDEP_EMPLOY.equals(af.getTypeAffiliation())) {
                     * nomAffilie = "2) " + nomAffilie; } else if
                     * (IAFAffiliation.TYPE_AFFILI_EMPLOY.equals(af.getTypeAffiliation())) { nomAffilie = "1) " +
                     * nomAffilie; }
                     */

                    if (nom.startsWith(APACORPrestationsParser.CONST_TYPE_AFFILI_INDEP)) {
                        affilie = APACORPrestationsParser.loadAffilieEtTypeAffiliation(session, noAffilie,
                                IAFAffiliation.TYPE_AFFILI_INDEP);

                    } else if (nom.startsWith(APACORPrestationsParser.CONST_TYPE_AFFILI_INDEP_EMPLOY)) {
                        affilie = APACORPrestationsParser.loadAffilieEtTypeAffiliation(session, noAffilie,
                                IAFAffiliation.TYPE_AFFILI_INDEP_EMPLOY);
                    } else if (nom.startsWith(APACORPrestationsParser.CONST_TYPE_AFFILI_EMPLOY)) {
                        affilie = APACORPrestationsParser.loadAffilieEtTypeAffiliation(session, noAffilie,
                                IAFAffiliation.TYPE_AFFILI_EMPLOY);
                    }

                    else if (nom.startsWith(APACORPrestationsParser.CONST_TYPE_AFFILI_EMPLOY_D_F)) {
                        affilie = APACORPrestationsParser.loadAffilieEtTypeAffiliation(session, noAffilie,
                                IAFAffiliation.TYPE_AFFILI_EMPLOY_D_F);
                    } else if (nom.startsWith(APACORPrestationsParser.CONST_TYPE_AFFILI_LTN)) {
                        affilie = APACORPrestationsParser.loadAffilieEtTypeAffiliation(session, noAffilie,
                                IAFAffiliation.TYPE_AFFILI_LTN);
                    } else if (nom.startsWith(APACORPrestationsParser.CONST_TYPE_AFFILI_TSE)) {
                        affilie = APACORPrestationsParser.loadAffilieEtTypeAffiliation(session, noAffilie,
                                IAFAffiliation.TYPE_AFFILI_TSE);
                    } else if (nom.startsWith(APACORPrestationsParser.CONST_TYPE_AFFILI_TSE_VOLONTAIRE)) {
                        affilie = APACORPrestationsParser.loadAffilieEtTypeAffiliation(session, noAffilie,
                                IAFAffiliation.TYPE_AFFILI_TSE_VOLONTAIRE);
                    } else {
                        affilie = APACORPrestationsParser.loadAffilie(session, noAffilie);
                    }

                    idAffilie = affilie.getIdAffilie();
                    idTiers = affilie.getIdTiers();
                } catch (Exception e) {
                    throw new PRACORException("Impossible de trouver l'affilie", e);
                }
            }
        }

        // ~ Methods
        // ----------------------------------------------------------------------------------------------------

        /**
         * getter pour l'attribut taux imposition
         * 
         * @return la valeur courante de l'attribut taux imposition
         */
        public String getCantonImposition() {
            return cantonImposition;
        }

        /**
         * getter pour l'attribut id affilie
         * 
         * @return la valeur courante de l'attribut id affilie
         */
        public String getIdAffilie() {
            return idAffilie;
        }

        /**
         * getter pour l'attribut id tiers
         * 
         * @return la valeur courante de l'attribut id tiers
         */
        public String getIdTiers() {
            return idTiers;
        }

        /**
         * getter pour l'attribut no affilie
         * 
         * @return la valeur courante de l'attribut no affilie
         */
        public String getNoAffilie() {
            return noAffilie;
        }

        /**
         * getter pour l'attribut nom
         * 
         * @return la valeur courante de l'attribut nom
         */
        public String getNom() {
            return nom;
        }

        /**
         * setter pour l'attribut taux imposition
         * 
         * @param tauxImposition
         *            une nouvelle valeur pour cet attribut
         */
        public void setCantonImposition(String tauxImposition) {
            cantonImposition = tauxImposition;
        }

        /**
         * setter pour l'attribut id affilie
         * 
         * @param idAffilie
         *            une nouvelle valeur pour cet attribut
         */
        public void setIdAffilie(String idAffilie) {
            this.idAffilie = idAffilie;
        }

        /**
         * setter pour l'attribut id tiers
         * 
         * @param idTiers
         *            une nouvelle valeur pour cet attribut
         */
        public void setIdTiers(String idTiers) {
            this.idTiers = idTiers;
        }

        /**
         * setter pour l'attribut no affilie
         * 
         * @param noAffilie
         *            une nouvelle valeur pour cet attribut
         */
        public void setNoAffilie(String noAffilie) {
            this.noAffilie = noAffilie;
        }

        /**
         * setter pour l'attribut nom
         * 
         * @param nom
         *            une nouvelle valeur pour cet attribut
         */
        public void setNom(String nom) {
            this.nom = nom;
        }
    }

    // un cache pour les affilie pour eviter de les charger a la lecture de
    // chaque ligne de periode
    private static IPRCache CACHE = PRCacheFactory.createCache(10, 0);
    private static final String CODE_CARTE = "$c";

    private static final String CODE_EMP_PERIODE = "$f";

    private static final String CODE_EMP_RJM = "$n";
    private static final String CODE_EMPLOYEURS = "$e";
    private static final String CODE_PERIODE = "$p";
    private static HashMap CONFIGURATIONS;

    // Const permettant d'identitfier le type d'affiliation lors de l'appel et la remontée depuis ACOR.

    // La taille de ces consantes ne doit pas dépasser 3 caractères selon les tests dans APModuleRepartitionPaiement
    // !!!!!
    public static final String CONST_TYPE_AFFILI_EMPLOY = "[1]";
    public static final String CONST_TYPE_AFFILI_EMPLOY_D_F = "[4]";
    public static final String CONST_TYPE_AFFILI_INDEP = "[3]";
    public static final String CONST_TYPE_AFFILI_INDEP_EMPLOY = "[2]";
    public static final String CONST_TYPE_AFFILI_LTN = "[5]";
    public static final String CONST_TYPE_AFFILI_TSE = "[6]";
    public static final String CONST_TYPE_AFFILI_TSE_VOLONTAIRE = "[7]";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private static final String FILE_PARSER = "fileParser.xml";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private static void addConfiguration(String code, List configuration) {
        HashMap map = new HashMap();

        for (Iterator fields = configuration.iterator(); fields.hasNext();) {
            PRTextField field = (PRTextField) fields.next();

            map.put(field.getName(), field);
        }

        APACORPrestationsParser.CONFIGURATIONS.put(code, map);
    }

    private static String cleEmployeur(String noAffilie, String nom) {
        return noAffilie.concat("_".concat(nom));
    }

    private static EmployeurInfo createEmpInfo(BSession session, Map employeurs, String noAffilie, String nom,
            String cantonImposition) throws PRACORException {
        EmployeurInfo retValue = new EmployeurInfo(session, noAffilie, nom, cantonImposition);

        employeurs.put(APACORPrestationsParser.cleEmployeur(noAffilie, nom), retValue);

        return retValue;
    }

    private static APBaseCalcul findBaseCalcul(BSession session, List<APBaseCalcul> basesCalcul, JADate dateDebutPeriodeAcor, JADate dateFinPeriodeAcor)
            throws PRACORException {
        APBaseCalcul retValue = null;

        for (Iterator iter = basesCalcul.iterator(); iter.hasNext();) {
            retValue = (APBaseCalcul) iter.next();

            try {
                if ((BSessionUtil.compareDateFirstLowerOrEqual(session, retValue.getDateDebut().toString(),
                        dateDebutPeriodeAcor.toString()))
                        && (BSessionUtil.compareDateFirstGreaterOrEqual(session, retValue.getDateFin().toString(),
                                dateFinPeriodeAcor.toString()))) {
                    break; // sortir de la boucle
                }
            } catch (Exception e) {
                throw new PRACORException("comparaison de dates impossibles", e);
            }

            retValue = null; // on a pas trouve donc on va retourner null et non
            // pas la derniere base de calcul
        }

        if(retValue==null && basesCalcul.stream().anyMatch(APBaseCalcul::isExtension)){
            for (Iterator iter = basesCalcul.iterator(); iter.hasNext();) {
                retValue = (APBaseCalcul) iter.next();

                try {
                    if(BSessionUtil.compareDateFirstLowerOrEqual(session, dateDebutPeriodeAcor.toString(), retValue.getDateDebut().toString())
                            && (BSessionUtil.compareDateFirstGreaterOrEqual(session, dateFinPeriodeAcor.toString(),  retValue.getDateFin().toString()))){
                        break; // sortir de la boucle
                    }
                } catch (Exception e) {
                    throw new PRACORException("comparaison de dates impossibles", e);
                }

                retValue = null; // on a pas trouve donc on va retourner null et non
                // pas la derniere base de calcul

            }
        }

        return retValue;
    }

    private static APBaseCalculSituationProfessionnel findBaseCalculSitPro(BSession session, APBaseCalcul basesCalcul,
            String idTiers, String idAffilie, String nomAffilie, JADate dateDebut, JADate dateFin)
            throws PRACORException {
        if (basesCalcul == null) {
            throw new PRACORException(); // TODO: exception speciale a catcher
            // pour avertir que le parse est
            // impossible
        }

        for (Iterator iter = basesCalcul.getBasesCalculSituationProfessionnel().iterator(); iter.hasNext();) {
            APBaseCalculSituationProfessionnel bcSitPro = (APBaseCalculSituationProfessionnel) iter.next();

            String nomSP = bcSitPro.getNom();
            String nomAff = nomAffilie;
            try {

                if (nomSP.startsWith(" ")) {
                    while (nomSP.startsWith(" ")) {
                        nomSP = nomSP.substring(1, nomSP.length() - 1);
                    }
                }

                if (nomAff.startsWith(" ")) {
                    while (nomAff.startsWith(" ")) {
                        nomAff = nomAff.substring(1, nomAff.length() - 1);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                nomSP = bcSitPro.getNom();
                nomAff = nomAffilie;
            }

            /*
             * Regex qui remplace une chaine qui commence (^ <-- çà veut dire commence quoi;) par '[' suivi d'un minimum
             * de un ou de plusieurs chiffres ([0-9] signifie les caractères valide, donc les chiffres, le '+' dit que
             * çà doit correspondre au moins une fois) et qui est suivit d'un ']' Si je n'est pas été assez claire :
             * http://en.wikipedia.org/wiki/Regular_expression saura répondre (RCO) BZ 8422
             */
            nomAff = nomAff.replaceFirst("^\\[[0-9]+\\]", "");
            if (idTiers.equals(bcSitPro.getIdTiers()) && idAffilie.equals(bcSitPro.getIdAffilie())
                    && ((nomSP.contains(nomAff)) || nomAff.contains(nomSP))) {
                return bcSitPro;
            }

        }

        throw new PRACORException(); // TODO: exception speciale a catcher pour
        // avertir que le parse est impossible
    }

    private static APBaseCalculSituationProfessionnel findBaseCalculSitProParNoAffilie(BSession session,
            APBaseCalcul basesCalcul, String idTiers, String noAffilie, String nomAffilie, JADate dateDebut,
            JADate dateFin, List<String> idsSP) throws PRACORException {
        if (basesCalcul == null) {
            throw new PRACORException(); // TODO: exception speciale a catcher
            // pour avertir que le parse est
            // impossible
        }

        for (Iterator iter = basesCalcul.getBasesCalculSituationProfessionnel().iterator(); iter.hasNext();) {
            APBaseCalculSituationProfessionnel bcSitPro = (APBaseCalculSituationProfessionnel) iter.next();

            String nomSP = bcSitPro.getNom();
            String nomAff = nomAffilie;
            try {

                if (nomSP.startsWith(" ")) {
                    while (nomSP.startsWith(" ")) {
                        nomSP = nomSP.substring(1, nomSP.length() - 1);
                    }
                }

                if (nomAff.startsWith(" ")) {
                    while (nomAff.startsWith(" ")) {
                        nomAff = nomAff.substring(1, nomAff.length() - 1);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                nomSP = bcSitPro.getNom();
                nomAff = nomAffilie;
            }

            /*
             * Regex qui remplace une chaine qui commence (^ <-- çà veut dire commence quoi;) par '[' suivi d'un minimum
             * de un ou de plusieurs chiffres ([0-9] signifie les caractères valide, donc les chiffres, le '+' dit que
             * çà doit correspondre au moins une fois) et qui est suivit d'un ']' Si je n'est pas été assez claire :
             * http://en.wikipedia.org/wiki/Regular_expression saura répondre (RCO) BZ 8422
             */
            nomAff = nomAff.replaceFirst("^\\[[0-9]+\\]", "");
            if (idTiers.equals(bcSitPro.getIdTiers()) && noAffilie.equals(bcSitPro.getNoAffilie())
                    && (nomSP.contains(nomAff) || nomAff.contains(nomSP))
                    && !idsSP.contains(bcSitPro.getIdSituationProfessionnelle())) {

                // ACOR peut rajouter des (1) a la fin des noms si plusieurs fois le même nom dans la liste des
                // employeurs...
                return bcSitPro;
            }

            // if ((((idTiers.equals(bcSitPro.getIdTiers()) && noAffilie.equals(bcSitPro.getNoAffilie()) && ((nomSP
            // .contains(nomAff)))) || nomAff.contains(nomSP)))
            // && !idsSP.contains(bcSitPro.getIdSituationProfessionnelle())) {
            //
            // // ACOR peut rajouter des (1) a la fin des noms si plusieurs fois le même nom dans la liste des
            // // employeurs...
            // return bcSitPro;
            // }
        }

        throw new PRACORException(); // TODO: exception speciale a catcher pour
        // avertir que le parse est impossible
    }

    private static String getField(String line, Map fields, String fieldName) {
        PRTextField field = (PRTextField) fields.get(fieldName);

        return line.substring(field.getBeginPos(), field.getEndPos()).trim();
    }

    private static IPRAffilie loadAffilie(BSession session, String noAffilie) throws Exception {
        IPRAffilie retValue = (IPRAffilie) APACORPrestationsParser.CACHE.fetch(noAffilie);

        if (retValue == null) {
            retValue = PRAffiliationHelper.getEmployeurParNumAffilie(session, noAffilie);
            APACORPrestationsParser.CACHE.store(noAffilie, retValue);
        }

        return retValue;
    }

    private static IPRAffilie loadAffilieEtTypeAffiliation(BSession session, String noAffilie, String csTypeAffiliation)
            throws Exception {
        String key = noAffilie;
        if (!JadeStringUtil.isBlankOrZero(csTypeAffiliation)) {
            key += "_" + csTypeAffiliation;
        }
        IPRAffilie retValue = (IPRAffilie) APACORPrestationsParser.CACHE.fetch(key);

        if (retValue == null) {
            retValue = PRAffiliationHelper.getEmployeurParNumAffilieEtTypeAffiliation(session, noAffilie,
                    csTypeAffiliation);
            APACORPrestationsParser.CACHE.store(key, retValue);
        }

        return retValue;
    }

    private static HashMap loadConfigurationsNAVS() throws PRACORException {
        if (APACORPrestationsParser.CONFIGURATIONS == null) {
            APACORPrestationsParser.CONFIGURATIONS = new HashMap();

            try {
                APACORPrestationsParser.addConfiguration(APACORPrestationsParser.CODE_CARTE, PRFileParserFactory
                        .loadConfiguration(APACORPrestationsParser.FILE_PARSER, "ANNONCEPAY_CARTE_APG_NAVS"));
                APACORPrestationsParser.addConfiguration(APACORPrestationsParser.CODE_EMP_PERIODE, PRFileParserFactory
                        .loadConfiguration(APACORPrestationsParser.FILE_PARSER, "ANNONCEPAY_EMPLOYEUR_PERIODE"));
                APACORPrestationsParser.addConfiguration(APACORPrestationsParser.CODE_EMPLOYEURS, PRFileParserFactory
                        .loadConfiguration(APACORPrestationsParser.FILE_PARSER, "ANNONCEPAY_EMPLOYEURS"));
                APACORPrestationsParser.addConfiguration(APACORPrestationsParser.CODE_PERIODE, PRFileParserFactory
                        .loadConfiguration(APACORPrestationsParser.FILE_PARSER, "ANNONCEPAY_PERIODE"));
                APACORPrestationsParser.addConfiguration(APACORPrestationsParser.CODE_EMP_RJM, PRFileParserFactory
                        .loadConfiguration(APACORPrestationsParser.FILE_PARSER, "ANNONCEPAY_EMPLOYEUR_RJM"));
            } catch (Exception e) {
                throw new PRACORException("impossible de charger les configurations de file parsers");
            }
        }

        return APACORPrestationsParser.CONFIGURATIONS;
    }

    private static HashMap loadConfigurationsNNSS() throws PRACORException {
        if (APACORPrestationsParser.CONFIGURATIONS == null) {
            APACORPrestationsParser.CONFIGURATIONS = new HashMap();

            try {
                APACORPrestationsParser.addConfiguration(APACORPrestationsParser.CODE_CARTE, PRFileParserFactory
                        .loadConfiguration(APACORPrestationsParser.FILE_PARSER, "ANNONCEPAY_CARTE_APG_NNSS"));
                APACORPrestationsParser.addConfiguration(APACORPrestationsParser.CODE_EMP_PERIODE, PRFileParserFactory
                        .loadConfiguration(APACORPrestationsParser.FILE_PARSER, "ANNONCEPAY_EMPLOYEUR_PERIODE"));
                APACORPrestationsParser.addConfiguration(APACORPrestationsParser.CODE_EMPLOYEURS, PRFileParserFactory
                        .loadConfiguration(APACORPrestationsParser.FILE_PARSER, "ANNONCEPAY_EMPLOYEURS"));
                APACORPrestationsParser.addConfiguration(APACORPrestationsParser.CODE_PERIODE, PRFileParserFactory
                        .loadConfiguration(APACORPrestationsParser.FILE_PARSER, "ANNONCEPAY_PERIODE"));
                APACORPrestationsParser.addConfiguration(APACORPrestationsParser.CODE_EMP_RJM, PRFileParserFactory
                        .loadConfiguration(APACORPrestationsParser.FILE_PARSER, "ANNONCEPAY_EMPLOYEUR_RJM"));
            } catch (Exception e) {
                throw new PRACORException("impossible de charger les configurations de file parsers");
            }
        }

        return APACORPrestationsParser.CONFIGURATIONS;
    }

    private static EmployeurInfo loadEmpInfo(BSession session, Map employeurs, String noAffilie, String nom)
            throws PRACORException {
        EmployeurInfo retValue = (EmployeurInfo) employeurs.get(APACORPrestationsParser.cleEmployeur(noAffilie, nom));

        if (retValue == null) {
            retValue = APACORPrestationsParser.createEmpInfo(session, employeurs, noAffilie, nom, "");
            employeurs.put(APACORPrestationsParser.cleEmployeur(noAffilie, nom), retValue);
        }

        return retValue;
    }

    /**
     * True si la lecture du fichier est réussie, sinon false
     * 
     * @param droit
     *            DOCUMENT ME!
     * @param basesCalcul
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * @param reader
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public static final SortedSet parse(APDroitLAPG droit, List basesCalcul, BSession session, Reader reader)
            throws PRACORException {
        BufferedReader bufferedReader = new BufferedReader(reader);
        HashMap employeurs = new HashMap();
        HashMap fields;
        HashMap configs;

        try {

            // changer de parser si NNSS ou NAVS
            PRDemande demande = new PRDemande();
            demande.setSession(session);
            demande.setIdDemande(droit.getIdDemande());
            demande.retrieve();

            if (demande.isNew()) {
                throw new PRACORException("Demande prestation non trouvée !!");
            } else {

                PRTiersWrapper tiers = PRTiersHelper.getTiersParId(session, demande.getIdTiers());

                if (null != tiers) {
                    // si navs
                    if (JadeStringUtil.removeChar(tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), '.')
                            .length() == 11) {
                        configs = APACORPrestationsParser.loadConfigurationsNAVS();
                        // si nnss
                    } else {
                        configs = APACORPrestationsParser.loadConfigurationsNNSS();
                    }
                } else {
                    throw new PRACORException("Tiers de la demande prestation non trouvé !!");
                }

            }

            SortedSet wrappers = new TreeSet(new APPrestationWrapperComparator());
            APPrestationWrapper wrapper = new APPrestationWrapper();
            APResultatCalcul rc;
            APBaseCalcul baseCalcul;
            String line = null;

            FWCurrency montantTotalPrestation = null;
            // bug selon mail Mme Grosvernier 22.09.2010
            FWCurrency nbrTotalJourService = null;

            // récupération des frais de gardes
            // ---------------------------------------------------
            fields = (HashMap) configs.get(APACORPrestationsParser.CODE_CARTE);

            String nssAssureImporte = "";
            int nombreJoursSupplementaires = 0;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith(APACORPrestationsParser.CODE_CARTE)) {
                    wrapper.setFraisGarde(new FWCurrency(APACORPrestationsParser.getField(line, fields,
                            "MONTANT_FRAIS_GARDE")));
                    montantTotalPrestation = new FWCurrency(APACORPrestationsParser.getField(line, fields,
                            "MONTANT_TOTAL_CARTE_APG"));
                    try {
                        nbrTotalJourService = new FWCurrency(APACORPrestationsParser.getField(line, fields,
                                "NOMBRE_TOTAL_JOURS_SERVICE"));
                    } catch (Exception e) {
                        nbrTotalJourService = new FWCurrency(1);
                    }

                    nssAssureImporte = APACORPrestationsParser.getField(line, fields, "NUMERO_AVS_ASSURE");
                    nombreJoursSupplementaires = Integer.parseInt(APACORPrestationsParser.getField(line, fields, "NOMBRE_JOURS_SUPPLEMENTAIRES"));
                    break;
                }
            }

            // récupération des employeurs et de leurs taux d'impositions
            // -------------------------
            boolean aucunEmployeur = false;

            fields = (HashMap) configs.get(APACORPrestationsParser.CODE_EMPLOYEURS);

            do {
                // skipper les lignes eventuelles jusqu'aux employeurs
                line = bufferedReader.readLine();
                aucunEmployeur = (line != null) && line.startsWith(APACORPrestationsParser.CODE_PERIODE);
            } while ((line != null) && !line.startsWith(APACORPrestationsParser.CODE_EMPLOYEURS) && !aucunEmployeur);

            if (!aucunEmployeur) {
                do {
                    String noAffilie = APACORPrestationsParser.getField(line, fields, "NUMERO_AFFILIE_EMPLOYEUR");
                    // Numéro d'affilié trop long au valais (reformatage à la sortie)
                    noAffilie = PRAbstractApplication.getAffileFormater().format(noAffilie);
                    String nom = APACORPrestationsParser.getField(line, fields, "NOM_EMPLOYEUR");

                    APACORPrestationsParser.createEmpInfo(session, employeurs, noAffilie, nom,
                            APACORPrestationsParser.getField(line, fields, "CANTON_IMPOT"));
                } while (((line = bufferedReader.readLine()) != null)
                        && line.startsWith(APACORPrestationsParser.CODE_EMPLOYEURS));
            }

            // périodes et résultats de calcul
            // ----------------------------------------------------
            while ((line != null) && !line.startsWith(APACORPrestationsParser.CODE_PERIODE)) {
                // skipper jusqu'aux périodes
                line = bufferedReader.readLine();
            }

            do {
                // périodes
                fields = (HashMap) configs.get(APACORPrestationsParser.CODE_PERIODE);

                if (wrapper == null) {
                    wrapper = new APPrestationWrapper();
                }

                wrapper.setIdDroit(droit.getIdDroit());

                // dates de la période
                APPeriodeWrapper periode = new APPeriodeWrapper();

                String ddd_importee = APACORPrestationsParser.getField(line, fields, "DEBUT_PERIODE_SERVICE");
                String dfd_importee = APACORPrestationsParser.getField(line, fields, "FIN_PERIODE_SERVICE");

                // Contrôle que les données importées correspondent bien au
                // droit courant (controle sur le no avs).
                // Permet d'éviter que l'utilisateur click sur importer les
                // données sans avoir préalablement calculée
                // les APG. Dans ce cas précis, l'importation des données se
                // fait avec le dernier calcul effectué sur le poste de
                // l'utilisateur.
                String nssAssure = droit.loadDemande().loadTiers().getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);

                if ((nssAssure != null) && (nssAssureImporte != null)) {
                    nssAssure = JadeStringUtil.removeChar(nssAssure, '.');
                    nssAssureImporte = JadeStringUtil.removeChar(nssAssureImporte, '.');
                    // TODO Le test nss1 égal nss2 a été remplacé par le test
                    // nss1 se termine par nss2.
                    // Lorsqu’ACOR supportera le format à 13 positions, l'ancien
                    // test pourra être restauré.
                    if (!nssAssure.endsWith(nssAssureImporte)) {
                        throw new PRACORException(session.getLabel("ERROR_NON_CONCORD_DONNEES"));
                    }
                }

                periode.setDateDebut(new JADate(ddd_importee));
                periode.setDateFin(new JADate(dfd_importee));
                wrapper.setPeriodeBaseCalcul(periode);

                // résultat de calcul
                rc = new APResultatCalcul();
                rc.setDateDebut(periode.getDateDebut()); // HACK: pour eviter
                // une
                // NullPointerException
                rc.setDateFin(periode.getDateFin());

                // copie des informations relatives a l'imposition qui ne sont
                // pas lues du fichier ACOR
                baseCalcul = APACORPrestationsParser.findBaseCalcul(session, basesCalcul, periode.getDateDebut(),
                        periode.getDateFin());
                rc.setSoumisImpotSource(baseCalcul.isSoumisImpotSource());
                rc.setIdTauxImposition(baseCalcul.getIdTauxImposition());
                rc.setTauxImposition(baseCalcul.getTauxImposition());

                // on force le genre service car ACOR regroupe certains genres
                // et renvoie un code maison qui ne nous
                // aide pas
                rc.setTypeAllocation(session.getCode(droit.getGenreService()));

                // versement à l'assuré
                String versementAssure = APACORPrestationsParser.getField(line, fields, "PART_MONTANT_PERIODE");

                if (!JadeStringUtil.isDecimalEmpty(versementAssure)) {
                    rc.setVersementAssure(new FWCurrency(versementAssure));
                }

                // révision
                IAPReferenceDataPrestation ref;

                if (session.getCode(IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE).equals(rc.getTypeAllocation())) {
                    ref = APReferenceDataParser.loadReferenceData(session, "MATERNITE", periode.getDateDebut(),
                            periode.getDateFin(), periode.getDateFin());
                } else {
                    ref = APReferenceDataParser.loadReferenceData(session, "APG", periode.getDateDebut(),
                            periode.getDateFin(), periode.getDateFin());
                }

                rc.setRevision(ref.getNoRevision());
                rc.setAllocationJournaliereMaxFraisGarde(ref.getMontantMaxFraisGarde());
                rc.setAllocationJournaliereExploitation(new FWCurrency(APACORPrestationsParser.getField(line, fields,
                        "MONTANT_ALLOCATION_EXPLOITATION_JOUR")));

                FWCurrency mj = new FWCurrency(APACORPrestationsParser.getField(line, fields,
                        "ALLOC_BASE_PLUS_ENFANTS_JOURN"));
                rc.setMontantJournalier(mj);

                // Récupération du basicDailyAmount
                // BZ 8329 : LGA 06.09.2013
                FWCurrency montantAllocJournPlafonnee = new FWCurrency(APACORPrestationsParser.getField(line, fields,
                        "ALLOC_BASE_PLUS_ENFANTS_JOURN"));
                rc.setBasicDailyAmount(montantAllocJournPlafonnee);
                // BZ 8329

                wrapper.setPrestationBase(rc);

                rc.setNombreJoursSoldes(Integer.parseInt(APACORPrestationsParser.getField(line, fields, "NOMBRE_JOURS")));
                rc.setNombreJoursSupplementaires(nombreJoursSupplementaires);
                rc.setRevenuDeterminantMoyen(new FWCurrency(APACORPrestationsParser.getField(line, fields,
                        "REVENU_JOURNALIER_MOYEN")));

                // situations professionnelles
                fields = (HashMap) configs.get(APACORPrestationsParser.CODE_EMP_PERIODE);

                List<String> idsSP = new ArrayList<String>();
                while (((line = bufferedReader.readLine()) != null)
                        && line.startsWith(APACORPrestationsParser.CODE_EMP_PERIODE)) {
                    APResultatCalculSituationProfessionnel rcSitPro = new APResultatCalculSituationProfessionnel();
                    String noAffilie = APACORPrestationsParser.getField(line, fields, "NUMERO_AFFILIE_EMPLOYEUR");
                    // Numéro d'affilié trop long au valais (reformatage à la sortie)
                    noAffilie = PRAbstractApplication.getAffileFormater().format(noAffilie);
                    String nom = APACORPrestationsParser.getField(line, fields, "NOM_EMPLOYEUR");
                    EmployeurInfo info = APACORPrestationsParser.loadEmpInfo(session, employeurs, noAffilie, nom);

                    // completer la sitpro
                    rcSitPro.setIdAffilie(info.getIdAffilie());
                    rcSitPro.setIdTiers(info.getIdTiers());
                    rcSitPro.setNoAffilie(noAffilie);
                    rcSitPro.setNom(nom);

                    // montants
                    String montant = APACORPrestationsParser.getField(line, fields, "PART_MONTANT_PERIODE");
                    double salaireJ = PRCalcul.quotient(montant, String.valueOf(rc.getNombreJoursSoldes()));

                    rcSitPro.setMontant(new FWCurrency(montant));

                    /*
                     * a ce niveau, il n'est plus important de savoir si l'employeur verse ou non un pourcentage du
                     * salaire total de l'assuré pendant la période car ACOR s'est deja charge de le prendre en compte.
                     * On ne renseigne donc ici que le champ salaire journalier sans arrondi.
                     */
                    rcSitPro.setSalaireJournalierNonArrondi(new FWCurrency(salaireJ));

                    // completer avec les bases de calcul
                    APBaseCalculSituationProfessionnel bcSitPro = null;
                    try {
                        bcSitPro = APACORPrestationsParser.findBaseCalculSitPro(session, baseCalcul, info.getIdTiers(),
                                info.getIdAffilie(), rcSitPro.getNom(), periode.getDateDebut(), periode.getDateFin());
                    } catch (PRACORException e) {

                        // Nouvelle tentative de recherche par #affilie pour les cas affiliés avec 2 affiliations sous
                        // le même #.
                        bcSitPro = APACORPrestationsParser.findBaseCalculSitProParNoAffilie(session, baseCalcul,
                                info.getIdTiers(), info.getNoAffilie(), rcSitPro.getNom(), periode.getDateDebut(),
                                periode.getDateFin(), idsSP);
                        idsSP.add(bcSitPro.getIdSituationProfessionnelle());

                    }
                    rcSitPro.setVersementEmployeur(bcSitPro.isPaiementEmployeur());
                    rcSitPro.setIndependant(bcSitPro.isIndependant());
                    rcSitPro.setTravailleurSansEmployeur(bcSitPro.isTravailleurSansEmployeur());
                    rcSitPro.setCollaborateurAgricole(bcSitPro.isCollaborateurAgricole());
                    rcSitPro.setTravailleurAgricole(bcSitPro.isTravailleurAgricole());
                    rcSitPro.setSoumisCotisation(bcSitPro.isSoumisCotisation());
                    rcSitPro.setIdSituationProfessionnelle(bcSitPro.getIdSituationProfessionnelle());

                    // TODO: récupérer les cantons et taux d'imposition

                    rc.addResultatCalculSitProfessionnelle(rcSitPro);
                }

                // Ajouter dans la liste des wrappers les bases de calcul de
                // sit. prof. si non existante.
                // Dans certains cas, Il arrive que ACOR ne retourne pas
                // l'employeur si versé à l'assuré. Ces employeurs
                // ne seront donc pas pris en compte lors du calcul de la
                // répartition des pmts, pour le calcul des cotisations.

                // Il faut donc les rajouter.

                if (basesCalcul != null) {

                    for (Iterator iter = baseCalcul.getBasesCalculSituationProfessionnel().iterator(); iter.hasNext();) {
                        APBaseCalculSituationProfessionnel bsp = (APBaseCalculSituationProfessionnel) iter.next();

                        List lrcsp = rc.getResultatsCalculsSitProfessionnelle();

                        boolean found = false;
                        for (Iterator iter2 = lrcsp.iterator(); iter2.hasNext();) {
                            APResultatCalculSituationProfessionnel rcsp = (APResultatCalculSituationProfessionnel) iter2
                                    .next();

                            String nomRCSP = rcsp.getNom();
                            String nomBSP = bsp.getNom();
                            try {

                                if (nomBSP.startsWith(" ")) {
                                    while (nomBSP.startsWith(" ")) {
                                        nomBSP = nomBSP.substring(1, nomBSP.length() - 1);
                                    }
                                }

                                if (nomRCSP.startsWith(" ")) {
                                    while (nomRCSP.startsWith(" ")) {
                                        nomRCSP = nomRCSP.substring(1, nomRCSP.length() - 1);
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                nomRCSP = rcsp.getNom();
                                nomBSP = bsp.getNom();
                            }

                            /*
                             * Regex qui remplace une chaine qui commence (^ <-- çà veut dire commence quoi;) par '['
                             * suivi d'un minimum de un ou de plusieurs chiffres ([0-9] signifie les caractères valide,
                             * donc les chiffres, le '+' dit que çà doit correspondre au moins une fois) et qui est
                             * suivit d'un ']' Si je n'est pas été assez claire :
                             * http://en.wikipedia.org/wiki/Regular_expression saura répondre (RCO) BZ 8422
                             */
                            nomRCSP = nomRCSP.replaceFirst("^\\[[0-9]+\\]", "");
                            if (rcsp.getIdTiers().equals(bsp.getIdTiers())
                                    && (rcsp.getIdAffilie().equals(bsp.getIdAffilie()) || ((rcsp.getNoAffilie() != null) && rcsp
                                            .getNoAffilie().equals(bsp.getNoAffilie())))

                                    && (nomBSP.contains(nomRCSP) || nomRCSP.contains(nomBSP))) {
                                found = true;
                                break;
                            } else {
                                found = false;
                            }
                        }
                        // La base de calcul n'existe pas dans la liste des
                        // résultats du calcul retournée par ACOR,
                        // on va donc la créer et la rajouter.
                        if (!found) {
                            APResultatCalculSituationProfessionnel newRcSitPro = new APResultatCalculSituationProfessionnel();
                            // completer la sitpro
                            newRcSitPro.setIdAffilie(bsp.getIdAffilie());
                            newRcSitPro.setIdTiers(bsp.getIdTiers());
                            newRcSitPro.setNom(bsp.getNom());

                            // info Inconnue
                            newRcSitPro.setMontant(new FWCurrency(0));
                            newRcSitPro.setSalaireJournalierNonArrondi(new FWCurrency(0));

                            // Devrait toujours être à false, car ACOR retourne
                            // toujours l
                            newRcSitPro.setVersementEmployeur(bsp.isPaiementEmployeur());
                            newRcSitPro.setIndependant(bsp.isIndependant());
                            newRcSitPro.setTravailleurSansEmployeur(bsp.isTravailleurSansEmployeur());
                            newRcSitPro.setCollaborateurAgricole(bsp.isCollaborateurAgricole());
                            newRcSitPro.setTravailleurAgricole(bsp.isTravailleurAgricole());
                            newRcSitPro.setSoumisCotisation(bsp.isSoumisCotisation());
                            newRcSitPro.setIdSituationProfessionnelle(bsp.getIdSituationProfessionnelle());

                            rc.addResultatCalculSitProfessionnelle(newRcSitPro);
                        }
                    }
                }

                wrapper.setPrestationBase(rc);
                wrappers.add(wrapper);
                wrapper = null;
            } while ((line != null) && line.startsWith(APACORPrestationsParser.CODE_PERIODE));

            // taux de participation des employeurs au RJM
            // ------------------------------------------------------------
            fields = (HashMap) configs.get(APACORPrestationsParser.CODE_EMP_RJM);

            while ((line != null) && !line.startsWith(APACORPrestationsParser.CODE_EMP_RJM)) {
                // skipper jusqu'aux taux de participation des employeurs au rjm
                line = bufferedReader.readLine();
            }

            if (line != null) {
                do {
                    String noAffilie = APACORPrestationsParser.getField(line, fields, "NUMERO_AFFILIE_EMPLOYEUR");
                    // Numéro d'affilié trop long au valais (reformatage à la sortie)
                    noAffilie = PRAbstractApplication.getAffileFormater().format(noAffilie);
                    EmployeurInfo info = APACORPrestationsParser.loadEmpInfo(session, employeurs, noAffilie,
                            APACORPrestationsParser.getField(line, fields, "NOM_EMPLOYEUR"));

                    for (Iterator iter = wrappers.iterator(); iter.hasNext();) {
                        wrapper = (APPrestationWrapper) iter.next();

                        for (Iterator sitPros = wrapper.getPrestationBase().getResultatsCalculsSitProfessionnelle()
                                .iterator(); sitPros.hasNext();) {
                            APResultatCalculSituationProfessionnel sitPro = (APResultatCalculSituationProfessionnel) sitPros
                                    .next();

                            /*
                             * Regex qui remplace une chaine qui commence (^ <-- çà veut dire commence quoi;) par '['
                             * suivi d'un minimum de un ou de plusieurs chiffres ([0-9] signifie les caractères valide,
                             * donc les chiffres, le '+' dit que çà doit correspondre au moins une fois) et qui est
                             * suivit d'un ']' Si je n'est pas été assez claire :
                             * http://en.wikipedia.org/wiki/Regular_expression saura répondre (RCO) BZ 8422
                             */
                            info.setNom(info.getNom().replaceFirst("^\\[[0-9]+\\]", ""));
                            sitPro.setNom(sitPro.getNom().replaceFirst("^\\[[0-9]+\\]", ""));

                            if (info.getIdAffilie().equals(sitPro.getIdAffilie())
                                    && info.getIdTiers().equals(sitPro.getIdTiers())
                                    && info.getNom().equals(sitPro.getNom())) {

                                FWCurrency taux = new FWCurrency(APACORPrestationsParser.getField(line, fields,
                                        "TAUX_APPORT_RJM"), 4);
                                sitPro.setTauxProRata(taux);

                                // Il s'agit d'une situation profesionnelle
                                // créer entièrement à partir de la base de
                                // calcul.
                                // newRcSitPro on va donc y rajouter le montant
                                // et salaire journalier en le recalculant à
                                // partir
                                // du montant total de la prestation au prorata.

                                // Ceci est nécessaire pour le calcul du montant
                                // des cotisations, afin de determiner
                                // si la part salariale est supérieure à la part
                                // de l'indépendant, le cas échéant.
                                if ((sitPro.getSalaireJournalierNonArrondi() == null)
                                        || JadeStringUtil.isBlankOrZero(sitPro.getSalaireJournalierNonArrondi()
                                                .toString())) {
                                    BigDecimal montant = (new BigDecimal(montantTotalPrestation.toString()))
                                            .multiply(taux.getBigDecimalValue());

                                    // double salaireJ =
                                    // PRCalcul.quotient(montant.toString(),
                                    // String.valueOf(wrapper.getPrestationBase().getNombreJoursSoldes()));
                                    double salaireJ = PRCalcul.quotient(montant.toString(),
                                            String.valueOf(nbrTotalJourService.intValue()));
                                    sitPro.setMontant(new FWCurrency(montant.toString()));
                                    sitPro.setSalaireJournalierNonArrondi(new FWCurrency(salaireJ));
                                }

                            }
                        }
                    }
                } while (((line = bufferedReader.readLine()) != null)
                        && line.startsWith(APACORPrestationsParser.CODE_EMP_RJM));
            }

            return wrappers;
        } catch (PRACORException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PRACORException("impossible de parser", e);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e1) {
                ;
            }
        }
    }

    // ~ Inner Classes
    // --------------------------------------------------------------------------------------------------

    private APACORPrestationsParser() {
    }
}
