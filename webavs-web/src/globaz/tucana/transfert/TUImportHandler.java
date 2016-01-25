package globaz.tucana.transfert;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BEntity;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUtil;
import globaz.jsp.util.GlobazJSPBeanUtil;
import globaz.tucana.application.TUApplication;
import globaz.tucana.constantes.IPropertiesNames;
import globaz.tucana.exception.TUException;
import globaz.tucana.exception.process.TUClearPrimaryKeyImportException;
import globaz.tucana.exception.process.TUInitImportException;
import globaz.tucana.exception.process.TUUpdateForeignKeyImportException;
import globaz.tucana.exception.transform.TUDomReaderException;
import globaz.tucana.transfert.config.ITUExportConfigXmlTags;
import globaz.tucana.util.DomReaderTransfert;
import globaz.tucana.util.TUBalise;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Handler d'importation permettant de lire le fichier xml d'exportation et de mettre les données en base
 * 
 * @author fgo
 * 
 */
public class TUImportHandler {
    /**
     * Permet d'effectuer l'importation de l'inputStream passé en paramètre Gère les transactions et laisse remonter des
     * erreurs
     * 
     * @param transaction
     * @param is
     * @throws TUException
     */
    public static void importation(BTransaction transaction, InputStream is) throws TUException {
        try {
            TUImportHandler importHandler = new TUImportHandler();
            importHandler.initialize(is);
            importHandler.loadKeysConfiguration();
            importHandler.addEntityInDB(transaction, importHandler.entityCollector);
            is.close();
        } catch (IOException e) {
            throw new TUInitImportException("TUImportHandler.importation() : Problème d'entrée/sortie : " + e);
        } catch (Exception e) {
            throw new TUInitImportException("TUImportHandler.importation() : Problème général : " + e);
        }
    }

    private TUImportEntityCollector entityCollector = null;

    private Collection keyesConfiguration = null;

    /**
     * Constructeur
     */
    private TUImportHandler() {
        super();
    }

    /**
     * Ajoute l'entité dans la base de données
     * 
     * @param transaction
     * @param _entityCollector
     * @throws TUUpdateForeignKeyImportException
     * @throws TUClearPrimaryKeyImportException
     * @throws Exception
     */
    private void addEntityInDB(BTransaction transaction, TUImportEntityCollector _entityCollector)
            throws TUUpdateForeignKeyImportException, TUClearPrimaryKeyImportException, Exception {
        // mise à jour de la clé
        updateKyes(_entityCollector);
        // set la session à l'entité
        _entityCollector.getEntity().getEntity().setSession(transaction.getSession());
        // ajoute l'entité en base de données
        _entityCollector.getEntity().getEntity().add(transaction);

        // lecture des entités filles
        Iterator it = _entityCollector.getListEntities().iterator();
        while (it.hasNext()) {
            // récupération de la collection de TUImporEntityCollector filles
            TUImportEntityCollector collector = (TUImportEntityCollector) it.next();
            // appel de la méthode d'insertion en DB
            addEntityInDB(transaction, collector);
        }
    }

    /**
     * Change la valeur d'une propriété
     * 
     * @param entity
     * @param nomChamp
     * @param val
     * @throws Exception
     */
    private void changeValue(BEntity entity, String nomChamp, Object val) throws Exception {
        GlobazJSPBeanUtil.setProperty(nomChamp, val, entity);
    }

    /**
     * Mise à blanc de la clé primaire afin d'avoir une numérotation propre au site qui importe les données
     * 
     * @param _entityCollector
     * @param balise
     * @throws TUClearPrimaryKeyImportException
     */
    private void clearPrimaryKey(TUImportEntityCollector _entityCollector, TUBalise balise)
            throws TUClearPrimaryKeyImportException {
        // mise à blanc de la cle primaire
        try {
            changeValue(_entityCollector.getEntity().getEntity(),
                    (String) balise.getListAttributes().get(ITUExportConfigXmlTags.ATTRIBUTE_PK), "");
        } catch (Exception e) {
            throw new TUClearPrimaryKeyImportException(this.getClass() + "Erreur de générale", e);
        }
    }

    /**
     * Récupération de la configuration des clés
     * 
     * @return
     */
    public Collection getKeyesConfiguration() {
        return keyesConfiguration;
    }

    /**
     * initialisation du XMLReader et pase l'inputStream passé en paramètre
     * 
     * @throws Exception
     */
    private void initialize(InputStream is) throws Exception {
        XMLReader saxReader = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        saxReader.setContentHandler(new TUImportSaxHandler(this));
        saxReader.parse(new InputSource(is));
    }

    /**
     * Permet de charger le fichier xml de configuration des clés primaires et étrangères
     * 
     * @throws TUInitImportException
     */
    private void loadKeysConfiguration() throws TUInitImportException {
        try {
            // récupération de l'emplacement du fichier xml de configuration
            String configFileName = GlobazSystem.getApplication(TUApplication.DEFAULT_APPLICATION_TUCANA).getProperty(
                    IPropertiesNames.EXPORT_CONFIG_FILE);
            if (JadeStringUtil.isEmpty(configFileName)) {
                // erreur levée si le fichier de configuration n'est pas trouvé
                throw new TUInitImportException("No config file defined!");
            }
            // lecture du fichier de configuration et récupération du
            // "org.w3c.dom.Document"
            Document document = DomReaderTransfert.getDocument(JadeUtil.getGlobazInputStream(configFileName));
            // récupération de l'information sous la forme d'une collection de
            // TUBalise
            keyesConfiguration = DomReaderTransfert.getAttributes(document.getDocumentElement());
        } catch (RemoteException e) {
            throw new TUInitImportException(this.getClass().getName() + "Invalid access document configuration : " + e);
        } catch (TUInitImportException e) {
            throw new TUInitImportException(this.getClass().getName() + "Invalid import : " + e);
        } catch (TUDomReaderException e) {
            throw new TUInitImportException(this.getClass().getName() + "Invalid document reader : " + e);
        } catch (IOException e) {
            throw new TUInitImportException(this.getClass().getName() + "Invalid document IO configuration : " + e);
        } catch (Exception e) {
            throw new TUInitImportException(this.getClass().getName() + "Invalid document configuration : " + e);
        }
    }

    /**
     * Méthode permettant la mise à jour de la propriété "entityCollector" par le TUImportSaxHandler
     * 
     * @param collector
     */
    public void registerEntityCollector(TUImportEntityCollector collector) {
        entityCollector = collector;
    }

    /**
     * Mise à jour de la clé étrangère
     * 
     * @param entityCollector
     * @param balise
     * @throws TUUpdateForeignKeyImportException
     */
    private void updateForeignKey(TUImportEntityCollector entityCollector, TUBalise balise)
            throws TUUpdateForeignKeyImportException {

        try {
            // mise à jour de la clé étrangère de l'entité avec la clé de
            // l'entité "père"
            changeValue(entityCollector.getEntity().getEntity(),
                    (String) balise.getListAttributes().get(ITUExportConfigXmlTags.ATTRIBUTE_FK),
                    GlobazJSPBeanUtil.getProperty(
                            (String) balise.getListAttributes().get(ITUExportConfigXmlTags.ATTRIBUTE_FK),
                            entityCollector.getParentCollector().getEntity().getEntity()));

        } catch (SecurityException e) {
            throw new TUUpdateForeignKeyImportException(this.getClass() + "Erreur de sécurité", e);
        } catch (NoSuchFieldException e) {
            throw new TUUpdateForeignKeyImportException(this.getClass() + "pas trouvé la méthode :"
                    + balise.getListAttributes().get(ITUExportConfigXmlTags.ATTRIBUTE_FK), e);
        } catch (Exception e) {
            throw new TUUpdateForeignKeyImportException(this.getClass() + "Erreur de générale", e);
        }
    }

    /**
     * Mise à jour des clé primaire (vide) et des clés étrangère (avec le clé primaire parent)
     * 
     * @param _entityCollector
     * @throws TUUpdateForeignKeyImportException
     * @throws TUClearPrimaryKeyImportException
     */
    private void updateKyes(TUImportEntityCollector _entityCollector) throws TUUpdateForeignKeyImportException,
            TUClearPrimaryKeyImportException {
        // récupération des TUBalise de la collection
        Iterator it = keyesConfiguration.iterator();
        while (it.hasNext()) {
            TUBalise balise = (TUBalise) it.next();
            // recherche la configuration de l'entitiée
            if (balise.getNom().equals(_entityCollector.getEntity().getEntity().getClass().getName())) {
                // si la balise a une dépendance, mise à jour de la clé
                // étrangère
                if (balise.hasDependance()) {
                    updateForeignKey(_entityCollector, balise);
                }
                // si la balise a une clé primaire
                if (balise.hasPrimaryKey()) {
                    clearPrimaryKey(_entityCollector, balise);
                }
            }
        }
    }
}
