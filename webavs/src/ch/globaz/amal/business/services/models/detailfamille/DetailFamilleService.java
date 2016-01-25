package ch.globaz.amal.business.services.models.detailfamille;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.op.common.model.document.Document;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import ch.globaz.amal.business.calcul.CalculsSubsidesContainer;
import ch.globaz.amal.business.calcul.CalculsTaxationContainer;
import ch.globaz.amal.business.exceptions.models.annonce.AnnonceException;
import ch.globaz.amal.business.exceptions.models.controleurEnvoi.ControleurEnvoiException;
import ch.globaz.amal.business.exceptions.models.controleurJob.ControleurJobException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.exceptions.models.documents.DocumentException;
import ch.globaz.amal.business.exceptions.models.famille.FamilleException;
import ch.globaz.amal.business.exceptions.models.parametreModel.ParametreModelException;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurAnnonceDetail;
import ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurEnvoiDetail;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurEnvoiStatus;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamilleSearch;
import ch.globaz.amal.business.models.documents.SimpleDocumentSearch;
import ch.globaz.amal.business.models.parametremodel.ParametreModelComplex;
import ch.globaz.amal.business.models.revenu.Revenu;
import ch.globaz.amal.business.models.revenu.RevenuHistorique;
import ch.globaz.envoi.business.exceptions.models.parametrageEnvoi.FormuleListException;

public interface DetailFamilleService extends JadeApplicationService {

    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws RevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(SimpleDetailFamilleSearch search) throws DetailFamilleException, JadePersistenceException;

    /**
     * Permet la création d'un detailFamille
     * 
     * @param detailFamille
     *            le detailFamille a créer
     * @return le detailFamille crée
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DetailFamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleDetailFamille create(SimpleDetailFamille detailFamille) throws JadePersistenceException,
            DetailFamilleException;

    /**
     * Permet la suppression d'une entité detailFamille
     * 
     * @param detailFamille
     *            Le detailFamille à supprimer
     * @return Le detailFamille supprimé
     * @throws DetailFamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DocumentException
     */
    public SimpleDetailFamille delete(SimpleDetailFamille detailFamille) throws DetailFamilleException,
            JadePersistenceException, DocumentException, JadeApplicationServiceNotAvailableException;

    public ArrayList<String> essaiAjax(String idDetailFamille);

    /**
     * Génération d'un document de correspondance
     * 
     * @param idDossier
     *            idSubside
     * @param idFormule
     *            idFormule
     * @return map clés 'warning' et 'error'
     */
    public HashMap<String, Object> generateDocumentCorrespondance(String idDossier, String idFormule);

    /**
     * Ecriture d'un subside en db, selon une simulation donnée. Créera de nouveaux subsides et inscrira les documents y
     * relatifs.
     * 
     * @param simulation
     *            le container de simulation renseigné
     * @throws JadePersistenceException
     * @throws DetailFamilleException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws RevenuException
     * @throws ControleurJobException
     * @throws AnnonceException
     * @throws ControleurEnvoiException
     * @throws DocumentException
     * @throws FamilleException
     * @throws FormuleListException
     * @throws ParametreModelException
     */
    public void generateSubside(CalculsSubsidesContainer calculs, Boolean isRecalculUnfavorable)
            throws DetailFamilleException, JadePersistenceException, RevenuException,
            JadeApplicationServiceNotAvailableException, DocumentException, ControleurEnvoiException, AnnonceException,
            ControleurJobException, FamilleException, ParametreModelException, FormuleListException;

    /**
     * Ecriture d'un subside en db, selon une simulation donnée. Créera de nouveaux subsides et inscrira les documents y
     * relatifs.
     * 
     * Surcharge de la méthode generateSubside(String, String, Boolean). Le paramètre isRecalculUnfavorable est forcé à
     * FALSE ici
     * 
     * @param calculs
     * @param csTypeJob
     * @throws DetailFamilleException
     * @throws JadePersistenceException
     * @throws RevenuException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DocumentException
     * @throws ControleurEnvoiException
     * @throws AnnonceException
     * @throws ControleurJobException
     * @throws FamilleException
     * @throws ParametreModelException
     * @throws FormuleListException
     */
    public void generateSubside(CalculsSubsidesContainer calculs, String csTypeJob) throws DetailFamilleException,
            JadePersistenceException, RevenuException, JadeApplicationServiceNotAvailableException, DocumentException,
            ControleurEnvoiException, AnnonceException, ControleurJobException, FamilleException,
            ParametreModelException, FormuleListException;

    /**
     * Ecriture d'un subside en db, selon une simulation donnée. Créera de nouveaux subsides et inscrira les documents y
     * relatifs.
     * 
     * @param calculs
     * @param csTypeJob
     * @throws DetailFamilleException
     * @throws JadePersistenceException
     * @throws RevenuException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DocumentException
     * @throws ControleurEnvoiException
     * @throws AnnonceException
     * @throws ControleurJobException
     * @throws FamilleException
     * @throws ParametreModelException
     * @throws FormuleListException
     */
    public void generateSubside(CalculsSubsidesContainer calculs, String csTypeJob, Boolean isRecalculUnfavorable)
            throws DetailFamilleException, JadePersistenceException, RevenuException,
            JadeApplicationServiceNotAvailableException, DocumentException, ControleurEnvoiException, AnnonceException,
            ControleurJobException, FamilleException, ParametreModelException, FormuleListException;

    /**
     * Service de génération des subsides depuis l'écran des attributions (appel AJAX). Remplace l'appel depuis
     * AMDetailFamilleServletAction
     * 
     * @param idContribuable
     * @param anneeHistorique
     * @param typeDemande
     * @param idRevenu
     * @param revenuIsTaxation
     * @param allSubsidesAsString
     */
    public void generateSubsidesFromAttribution(String idContribuable, String anneeHistorique, String typeDemande,
            String idRevenu, Boolean revenuIsTaxation, String allSubsidesAsString);

    /**
     * Récupération de la liste des documents disponible pour appel AJAX detail famille
     * 
     * @param idDossier
     * @return ArrayList<ParametreModelComplex>
     */
    public ArrayList<ParametreModelComplex> getAvailableDocumentsListCorrespondance(String idDossier);

    /**
     * Génération des propriétés nécessaires à la GED pour la publication
     * 
     * @param detailFamille
     * @return Propriétés renseignées
     */
    public Properties getGEDPublishProperties(SimpleDetailFamille detailFamille, SimpleControleurEnvoiStatus statusEnvoi);

    /**
     * Génération des propriétés nécessaire à la GED pour la publication
     * 
     * @param idDetailFamille
     * @return Propriétés renseignées
     */
    public Properties getGEDPublishProperties(String idDetailFamille, String idStatusEnvoi);

    /**
     * Récupération d'un tableau de résultats annonces pour un idDetailFamille particulier
     * 
     * @param idDetailFamille
     *            l'id concerné
     * @return un tableau renseigné
     */
    public ArrayList<ComplexControleurAnnonceDetail> getHistoriqueAnnonces(String idDetailFamille);

    /**
     * Récupération d'un tableau de résultats envois pour un idDetailFamille particulier
     * 
     * @param idDetailFamille
     *            l'id concerné
     * @return un tableau renseigné
     */
    public ArrayList<ComplexControleurEnvoiDetail> getHistoriqueEnvois(String idDetailFamille);

    /**
     * Récupération d'un id Formule en fonction de son noFormule Amal (historique)
     * 
     * @param noFormule
     * @return
     * @throws FormuleListException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public String getIdFormuleFromNoFormule(String noFormule) throws FormuleListException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Retourne le nom du document en fonction de la date du jour complete, de l'id Detail Famille et du noModele
     * 
     * @param csDateComplete
     * @param idDetailFamille
     * @param noModele
     * @return nom formatté : tif_21.10.2011_11-20-35_669424_42000013
     */
    public String getInteractivDocumentFileName(String csDateComplete, String idDetailFamille, String idFormule,
            String csFormule);

    /**
     * Récupération du chemin complet du fichier généré depuis le client
     * 
     * @param csDateComplete
     * @param idDetailFamille
     * @param idFormule
     * @param csFormule
     * @return
     */
    public String getInteractivDocumentFilePathFromClient(String csDateComplete, String idDetailFamille,
            String idFormule, String csFormule);

    /**
     * Récupération du chemin complet du fichier généré depuis le serveur de job
     * 
     * @param csDateComplete
     * @param idDetailFamille
     * @param idFormule
     * @param csFormule
     * @return
     */
    public String getInteractivDocumentFilePathFromJobServer(String csDateComplete, String idDetailFamille,
            String idFormule, String csFormule);

    /**
     * Get the current list of caisse maladie
     * 
     * @return
     */
    public ArrayList<String> getListeCMCalcul(String empty);

    /**
     * Get the current list of document (from CS)
     * 
     * @return
     */
    public ArrayList<String> getListeDocumentCalcul(String empty);

    /**
     * Get the current list of type demande (from CS)
     * 
     * @return
     */
    public ArrayList<String> getListeTypeDemandeCalcul(String empty);

    /**
     * Récupère une liste des revenus pour le calcul des subsides
     * 
     * @param idContribuable
     * @param annee
     * @return
     * @throws DetailFamilleException
     * @throws JadePersistenceException
     * @throws RevenuException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public ArrayList<RevenuHistorique> getListRevenus(String idContribuable, String annee)
            throws DetailFamilleException, JadePersistenceException, RevenuException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Récupération de la liste des subsides calculés en fonction de différents paramètres :
     * 
     * idContribuable anneeHistorique typeDemande idRevenu revenuIsTaxation
     * 
     * @param values
     *            valeur énumérées ci-dessus
     * @return
     */
    public CalculsSubsidesContainer getListSubsidesCalculAjax(HashMap<?, ?> values);

    /**
     * Récupère une liste des taxations pour le calcul des subsides
     * 
     * @param idContribuable
     * @param annee
     * @return
     * @throws DetailFamilleException
     * @throws JadePersistenceException
     * @throws RevenuException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public ArrayList<Revenu> getListTaxations(String idContribuable, String annee) throws DetailFamilleException,
            JadePersistenceException, RevenuException, JadeApplicationServiceNotAvailableException;

    /**
     * 
     * @param values
     * @return
     * @throws DetailFamilleException
     * @throws RevenuException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public ArrayList<Revenu> getListTaxationsAjax(HashMap<?, ?> values) throws DetailFamilleException, RevenuException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Récupération d'une liste de taxation liée à un calcul de revenu déterminant selon l'année historique
     * 
     * @param idContribuable
     * @param annee
     * @return
     */
    public ArrayList<CalculsTaxationContainer> getListTaxationsCalcul(String idContribuable, String annee);

    /**
     * Récupération d'une liste de taxation liée à un calcul de revenu déterminant selon l'année historique
     * 
     * @param idContribuable
     * @param annee
     * @return
     */
    public ArrayList<CalculsTaxationContainer> getListTaxationsCalculAjax(HashMap<?, ?> values);

    /**
     * Permet de charger en mémoire un detailFamille
     * 
     * @param idDetailFamille
     *            L'identifiant du detailFamille à charger en mémoire
     * @return Le detailFamille chargé en mémoire
     * @throws DetailFamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleDetailFamille read(String idDetailFamille) throws DetailFamilleException, JadePersistenceException;

    /**
     * Permet de chercher des detail famille (subside) selon un modèle de critères.
     * 
     * @param detailFamilleSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleDetailFamilleSearch search(SimpleDetailFamilleSearch detailFamilleSearch)
            throws JadePersistenceException, DetailFamilleException;

    /**
     * Permet de chercher des documents (envois) selon un modèle de critères.
     * 
     * @param simpleDocumentSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DocumentException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleDocumentSearch search(SimpleDocumentSearch simpleDocumentSearch) throws JadePersistenceException,
            DocumentException;

    /**
     * Permet la mise à jour d'une entité detailFamille
     * 
     * @param detailFamille
     *            Le detailFamille à mettre à jour
     * @return Le detailFamille mis à jour
     * @throws DetailFamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleDetailFamille update(SimpleDetailFamille detailFamille) throws DetailFamilleException,
            JadePersistenceException;

    /**
     * Write the document in the job table
     * 
     * @param csDateComplete
     *            date complete format 28.02.2011_22:15:55
     * @param csModele
     *            Modele concerné (CS noModele)
     * @param csJobType
     *            Type de job (CS type de job)
     * @param idDetailFamille
     *            idDetailFamille concerné
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DetailFamilleException
     * @throws DocumentException
     * @throws AnnonceException
     * @throws ControleurEnvoiException
     * @throws ControleurJobException
     */
    public void writeInJobTable(String csDateComplete, String csModele, String csJobType, String idDetailFamille,
            int iNoGroupe) throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            DocumentException, DetailFamilleException, ControleurEnvoiException, AnnonceException,
            ControleurJobException;

    /**
     * Generate and write a document to a specific path (final path). Intermediate path is /persistence/
     * 
     * @param fileName
     * @param wordDocument
     * @return
     */
    public String writeInteractivDocument(String fileName, Document wordDocument);

}
