package globaz.tucana.statistiques.config;

import globaz.globall.api.GlobazSystem;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUtil;
import globaz.tucana.application.TUApplication;
import globaz.tucana.constantes.IPropertiesNames;
import globaz.tucana.exception.process.TUInitStatistiquesConfigException;
import java.util.HashMap;
import java.util.Iterator;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Classe fournissant la configuration pour les statistiques
 * 
 * @author fgo date de création : 16 août 06
 * @version : version 1.0
 * 
 */
public class TUStatistiquesConfigProvider {

    private static TUStatistiquesConfigProvider instance = null;

    /**
     * Permet de récupérer une instance de la classe
     * 
     * @return
     * @throws Exception
     */
    public static TUStatistiquesConfigProvider getInstance() throws Exception {
        if (instance == null) {
            instance = new TUStatistiquesConfigProvider();
            instance.initialize();
        }
        return instance;
    }

    private HashMap statistiquesConfigContainer = null;

    /**
     * Constructeur
     */
    private TUStatistiquesConfigProvider() {
        super();
        statistiquesConfigContainer = new HashMap();
    }

    /**
     * Récupère une configuration statistique
     * 
     * @param statistiqueConfig
     *            provient de l'interface ITUConstantes
     * @param idCategorieRubrique
     * @return
     */
    public TUCategorieRubriqueStatistiqueConfig getRubriqueConfig(TUStatistiqueConfig statistiqueConfig,
            String idCategorieRubrique) {
        return statistiqueConfig != null ? statistiqueConfig.getCategorieRubriqueConfig(idCategorieRubrique) : null;
    }

    /**
     * Récupère une liste de TUCategorieRubriqueStatistiqueConfig
     * 
     * @return
     */
    public HashMap getStatistiquesConfigContainer() {
        return statistiquesConfigContainer;
    }

    /**
     * Initialisation de la classe après instanciation
     * 
     * @throws Exception
     */
    private void initialize() throws Exception {
        String configFileName = GlobazSystem.getApplication(TUApplication.DEFAULT_APPLICATION_TUCANA).getProperty(
                IPropertiesNames.STATISTIQUES_CONFIG_FILE);
        if (JadeStringUtil.isEmpty(configFileName)) {
            throw new TUInitStatistiquesConfigException("No config file defined!");
        } else {
            XMLReader saxReader = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
            saxReader.setContentHandler(new TUStatistiquesConfigSaxHandler(this));
            saxReader.parse(new InputSource(JadeUtil.getGlobazInputStream(configFileName)));
        }
    }

    /**
     * Ajout d'une configuration de statistique dans un TUStatistiqueConfig
     * 
     * @param key
     * @param statistiqueConfig
     */
    protected void registerStatistique(String key, TUStatistiqueConfig statistiqueConfig) {
        statistiquesConfigContainer.put(key, statistiqueConfig);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer str = new StringBuffer("\n*****************************************");
        for (Iterator iter = statistiquesConfigContainer.values().iterator(); iter.hasNext();) {
            str.append(iter.next());
        }
        str.append("\n*****************************************");
        return str.toString();
    }
}
