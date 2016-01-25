package globaz.aquila.service.config;

import globaz.aquila.api.ICOApplication;
import globaz.aquila.application.COApplication;
import globaz.aquila.util.COEtapeBlocage;
import globaz.globall.db.BSession;
import globaz.globall.parameters.FWParameters;
import globaz.globall.parameters.FWParametersManager;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <H1>Description</H1>
 * <p>
 * Service permettant d'accéder à toutes les options de configuration de l'application Aquila, qu'elles se trouvent dans
 * le fichier de propriétés ou les tables de paramètrage.
 * </p>
 * 
 * @author vre
 */
public class COConfigurationService {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private abstract static class COConfigurationKeyImpl implements COConfigurationKey {

        // ~ Methods
        // ----------------------------------------------------------------------------------------------------

        protected abstract List getFraisNaturesRubriques(BSession session) throws Exception;

        protected abstract HashMap getGestionMotifContentieux(BSession session) throws Exception;

        protected abstract COConfigurationOption getOption(BSession session) throws Exception;
    }

    /**
     * <h1>Description</h1>
     * <p>
     * Classe d'implémentation qui va chercher les options dans les tables de paramètrages.
     * </p>
     * <p>
     * Seul le champ {@link FWParameters#getValeurNumerique()} est retourné.
     * </p>
     */
    private static class COConfigurationParameter extends COConfigurationKeyImpl {

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------

        private String codeSysteme;
        private List fraisNaturesRubriques = null;
        private HashMap gestionMotif = null;

        private String idApplication;
        private String idTypeCode;

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        private COConfigurationParameter(String idApplication, String codeSysteme, String idTypeCode) {
            this.idApplication = idApplication;
            this.codeSysteme = codeSysteme;
            this.idTypeCode = idTypeCode;
        }

        // ~ Methods
        // ----------------------------------------------------------------------------------------------------

        /**
         * @see FWParameters#getIdCodeSysteme()
         */
        public String getCodeSysteme() {
            return codeSysteme;
        }

        /*
         * (non-Javadoc)
         * 
         * @seeglobaz.aquila.service.config.COConfigurationService. COConfigurationKeyImpl
         * #getFraisNaturesRubriques(globaz.globall.db.BSession)
         */
        @Override
        protected List getFraisNaturesRubriques(BSession session) throws Exception {
            if (fraisNaturesRubriques == null) {
                fraisNaturesRubriques = new ArrayList();

                FWParametersManager parametersManager = new FWParametersManager();

                parametersManager.setSession(session);
                parametersManager.setForApplication(idApplication);
                parametersManager.setForIdCle("FRAISRUBNA");
                parametersManager.find();

                if (parametersManager.isEmpty()) {
                    return null;
                }

                FWParameters param = (FWParameters) parametersManager.getFirstEntity();
                String[] s = param.getValeurAlpha().split(",");
                for (int i = 0; i < s.length; i++) {
                    fraisNaturesRubriques.add(s[i]);
                }
            }

            return fraisNaturesRubriques;
        }

        @Override
        protected HashMap getGestionMotifContentieux(BSession session) throws Exception {
            if (gestionMotif == null) {
                gestionMotif = new HashMap();

                if (!JadeStringUtil.isBlank(idTypeCode)) {
                    FWParametersManager parametersManager = new FWParametersManager();

                    parametersManager.setSession(session);
                    parametersManager.setForApplication(idApplication);
                    parametersManager.setForIdTypeCode(idTypeCode);
                    parametersManager.find();

                    if (!parametersManager.isEmpty()) {
                        for (int i = 0; i < parametersManager.size(); i++) {
                            FWParameters param = (FWParameters) parametersManager.getEntity(i);
                            COEtapeBlocage etape = new COEtapeBlocage();
                            etape.typeBlocage = JadeStringUtil.toInt(JANumberFormatter.fmt(param.getValeurNumerique(),
                                    false, false, false, 0));
                            etape.codeSystemeEtape = param.getValeurAlpha();
                            gestionMotif.put(param.getIdCodeSysteme(), etape);
                        }
                        return gestionMotif;
                    }
                    return null;
                }
                return null;
            }
            return gestionMotif;
        }

        /**
         * @see FWParameters#getIdAppl()
         */
        public String getIdApplication() {
            return idApplication;
        }

        /**
         * @see FWParameters#getIdTypeCode()
         */
        public String getIdTypeCode() {
            return idTypeCode;
        }

        @Override
        protected COConfigurationOption getOption(BSession session) throws Exception {
            FWParametersManager parametersManager = new FWParametersManager();

            parametersManager.setSession(session);
            parametersManager.setForApplication(idApplication);
            parametersManager.setForIdCode(codeSysteme);
            parametersManager.find();

            if (!parametersManager.isEmpty()) {
                FWParameters parameters = (FWParameters) parametersManager.getFirstEntity();

                if (!JadeStringUtil.isEmpty(parameters.getValeurAlpha())) {
                    return new COConfigurationOptionImpl(this, parameters.getValeurAlpha());
                }
                return new COConfigurationOptionImpl(this, parameters.getValeurNumerique());
            }
            return null;
        }
    }

    /**
     * <h1>Description</h1>
     * <p>
     * Classe d'implémentation qui va chercher dans le fichier de config de AQUILA.
     * </p>
     * 
     * @author vre
     */
    private static class COCOnfigurationProperty extends COConfigurationKeyImpl {

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------

        private String propertyName;

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        private COCOnfigurationProperty(String propertyName) {
            this.propertyName = propertyName;
        }

        // ~ Methods
        // ----------------------------------------------------------------------------------------------------

        @Override
        protected List getFraisNaturesRubriques(BSession session) throws Exception {
            return null;
        }

        @Override
        protected HashMap getGestionMotifContentieux(BSession session) throws Exception {
            return null;
        }

        @Override
        protected COConfigurationOption getOption(BSession session) throws Exception {
            return new COConfigurationOptionImpl(this, session.getApplication().getProperty(propertyName));
        }

        /**
         * @return le nom de la propriété dans le fichier de configuration.
         */
        public String getPropertyName() {
            return propertyName;
        }
    }

    /**
     * Cette clé retourne l'option de configuration qui indique s'il faut envoyer une confirmation lors de la mutation
     * d'un délai de contentieux.
     */
    public static final COConfigurationKey CONFIRMATION_DELAI_MUTATION = new COCOnfigurationProperty(
            "delai.mutation.confirmation");

    /** Cette clé retourne le delai de la date de paiement */
    public static final COConfigurationKey DELAI_PAIEMENT = new COConfigurationParameter(
            COApplication.DEFAULT_APPLICATION_AQUILA, "5140003", null);

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /** Cette clé retourne la gestion des motifs de blocage du contentieux */
    public static final COConfigurationKey GESTION_MOTIF_BLOCAGE = new COConfigurationParameter(
            COApplication.DEFAULT_APPLICATION_AQUILA, "", "10200004");

    /** Cette clé retourne le montant minimal pour l'envoi d'une formule 44. */
    public static final COConfigurationKey MONTANT_FORMULE_44 = new COConfigurationParameter(
            COApplication.DEFAULT_APPLICATION_AQUILA, "5140001", null);

    /** Cette clé retourne le pourcentage seuil pour l'envoi d'une formule 44. */
    public static final COConfigurationKey TAUX_FORMULE_44 = new COConfigurationParameter(
            COApplication.DEFAULT_APPLICATION_AQUILA, "5140002", null);

    // ~ Inner Classes
    // --------------------------------------------------------------------------------------------------

    /**
     * Retourne une option de configuration pour la clé donnée.
     * 
     * @param session
     * @param key
     *            la clé identifiant une option de configuration de l'application AQUILA.
     * @return une option de config ou null si non trouvé
     * @throws Exception
     */
    public List getFraisNaturesRubriques(BSession session) throws Exception {
        return (new COConfigurationParameter(ICOApplication.DEFAULT_APPLICATION_AQUILA, "", ""))
                .getFraisNaturesRubriques(session);
    }

    /**
     * Retourne une option de configuration pour la clé donnée.
     * 
     * @param session
     * @param key
     *            la clé identifiant une option de configuration de l'application AQUILA.
     * @return une option de config ou null si non trouvé
     * @throws Exception
     */
    public HashMap getGestionMotifContentieux(BSession session, COConfigurationKey key) throws Exception {
        if (key instanceof COConfigurationParameter) {
            return ((COConfigurationParameter) key).getGestionMotifContentieux(session);
        }

        return null;
    }

    /**
     * Retourne une option de configuration pour la clé donnée.
     * 
     * @param session
     * @param key
     *            la clé identifiant une option de configuration de l'application AQUILA.
     * @return une option de config ou null si non trouvé
     * @throws Exception
     */
    public COConfigurationOption getOption(BSession session, COConfigurationKey key) throws Exception {
        if (key instanceof COConfigurationKeyImpl) {
            return ((COConfigurationKeyImpl) key).getOption(session);
        }

        return null;
    }
}
