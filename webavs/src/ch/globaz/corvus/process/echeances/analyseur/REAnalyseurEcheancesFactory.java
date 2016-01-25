package ch.globaz.corvus.process.echeances.analyseur;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ch.globaz.corvus.process.echeances.analyseur.modules.REModuleAnalyseEcheance;
import ch.globaz.corvus.process.echeances.analyseur.modules.REModuleEcheance18Ans;
import ch.globaz.corvus.process.echeances.analyseur.modules.REModuleEcheance25Ans;
import ch.globaz.corvus.process.echeances.analyseur.modules.REModuleEcheanceAjournement;
import ch.globaz.corvus.process.echeances.analyseur.modules.REModuleEcheanceCertificatDeVie;
import ch.globaz.corvus.process.echeances.analyseur.modules.REModuleEcheanceEtude;
import ch.globaz.corvus.process.echeances.analyseur.modules.REModuleEcheanceFemmeAgeAvs;
import ch.globaz.corvus.process.echeances.analyseur.modules.REModuleEcheanceHommeAgeAvs;
import ch.globaz.corvus.process.echeances.analyseur.modules.REModuleEcheanceRenteDeVeuf;
import ch.globaz.corvus.process.echeances.analyseur.modules.REModuleEcheancesForcees;

/**
 * Construit deux types d'analyseur : un standard et un prenant en charge les échéances forcées (voir la doc de
 * {@link REAnalyseurAutreEcheances} pour plus de détails).
 * 
 * @author PBA
 */
public class REAnalyseurEcheancesFactory {

    public static enum TypeAnalyseurEcheances {
        Ajournement("OPTION_AJOURNEMENT"),
        CertificatDeVie("OPTION_CERTIFICAT_DE_VIE"),
        Echeance18ans("OPTION_ENFANT_18_ANS"),
        Echeance25ans("OPTION_ENFANT_25_ANS"),
        EcheanceEtudes("OPTION_ECHEANCE_ETUDE"),
        EcheancesForcees("OPTION_AUTRES_ECHEANCES"),
        FemmeArrivantAgeAvs("OPTION_FEMME_ARRIVANT_AGE_AVS"),
        HommeArrivantAgeAvs("OPTION_HOMME_ARRIVANT_AGE_AVS"),
        RenteDeVeuf("OPTION_RENTE_DE_VEUF");

        private String idLabelMotif;

        private TypeAnalyseurEcheances(String idLabelMotif) {
            this.idLabelMotif = idLabelMotif;
        }

        public String getIdLabelMotif() {
            return idLabelMotif;
        }
    };

    private BSession session;

    public REAnalyseurEcheancesFactory(BSession session) {
        super();
        if (session == null) {
            throw new NullPointerException("Session null");
        }

        this.session = session;
    };

    private String getDateDebutPriseEnCompteAgeAvsDepasse() throws Exception {
        String proprieteEnDB = session.getApplication().getProperty("date.mise.en.production.rentes");

        if (JadeStringUtil.isBlankOrZero(proprieteEnDB)
                || (!JadeDateUtil.isGlobazDate(proprieteEnDB) && !JadeDateUtil.isGlobazDateMonthYear(proprieteEnDB))) {
            return "01.2012";
        }
        return proprieteEnDB;
    }

    /**
     * Construit et retourne une instance d'un {@link REAnalyseurEcheances} selon les types de modules passés en
     * paramètre
     * 
     * @param moisTraitement
     *            le mois de traitement, au format MM.AAAA
     * @param types
     *            les types de modules désirés dans cet analyseur
     * @return un analyseur contenant les modules défini dans les paramètres
     * @throws Exception
     */
    public REAnalyseurEcheances getInstance(String moisTraitement, TypeAnalyseurEcheances... types) throws Exception {
        List<REModuleAnalyseEcheance> modules = new ArrayList<REModuleAnalyseEcheance>();

        boolean autreEcheance = false;

        for (TypeAnalyseurEcheances unType : types) {
            switch (unType) {
                case EcheancesForcees:
                    autreEcheance = true;
                    break;
                default:
                    modules.add(getModulePourType(moisTraitement, unType));
                    break;
            }
        }
        REAnalyseurEcheances analyseurEcheances;
        if (autreEcheance) {
            List<TypeAnalyseurEcheances> typesDejaPresents = Arrays.asList(types);
            List<REModuleAnalyseEcheance> tousLesAutresModulesSaufEcheancesForcees = new ArrayList<REModuleAnalyseEcheance>();
            for (TypeAnalyseurEcheances type : TypeAnalyseurEcheances.values()) {
                if (type.equals(TypeAnalyseurEcheances.EcheancesForcees) || typesDejaPresents.contains(type)) {
                    continue;
                }
                tousLesAutresModulesSaufEcheancesForcees.add(getModulePourType(moisTraitement, type));
            }

            analyseurEcheances = new REAnalyseurAutreEcheances(getModulePourType(moisTraitement,
                    TypeAnalyseurEcheances.EcheancesForcees), tousLesAutresModulesSaufEcheancesForcees);
        } else {
            analyseurEcheances = new REAnalyseurEcheances();
        }
        analyseurEcheances.setModulesAnalyse(modules);
        return analyseurEcheances;
    }

    protected REModuleAnalyseEcheance getModulePourType(String moisTraitement, TypeAnalyseurEcheances type)
            throws Exception {
        switch (type) {
            case Ajournement:
                return new REModuleEcheanceAjournement(getSession(), moisTraitement);
            case CertificatDeVie:
                return new REModuleEcheanceCertificatDeVie(getSession(), moisTraitement);
            case Echeance18ans:
                return new REModuleEcheance18Ans(getSession(), moisTraitement);
            case Echeance25ans:
                return new REModuleEcheance25Ans(getSession(), moisTraitement, false);
            case EcheanceEtudes:
                return new REModuleEcheanceEtude(getSession(), moisTraitement);
            case EcheancesForcees:
                return new REModuleEcheancesForcees(getSession(), moisTraitement);
            case FemmeArrivantAgeAvs:
                return new REModuleEcheanceFemmeAgeAvs(getSession(), moisTraitement,
                        getDateDebutPriseEnCompteAgeAvsDepasse());
            case HommeArrivantAgeAvs:
                return new REModuleEcheanceHommeAgeAvs(getSession(), moisTraitement,
                        getDateDebutPriseEnCompteAgeAvsDepasse());
            case RenteDeVeuf:
                return new REModuleEcheanceRenteDeVeuf(getSession(), moisTraitement);
        }
        throw new UnsupportedOperationException("Motif inconnu : " + type.getIdLabelMotif() + " ("
                + getSession().getLabel(type.getIdLabelMotif()) + ")");
    }

    public BSession getSession() {
        return session;
    }
}
