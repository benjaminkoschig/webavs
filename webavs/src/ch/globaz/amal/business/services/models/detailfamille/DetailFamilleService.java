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
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws RevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(SimpleDetailFamilleSearch search) throws DetailFamilleException, JadePersistenceException;

    /**
     * Permet la cr�ation d'un detailFamille
     * 
     * @param detailFamille
     *            le detailFamille a cr�er
     * @return le detailFamille cr�e
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DetailFamilleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleDetailFamille create(SimpleDetailFamille detailFamille) throws JadePersistenceException,
            DetailFamilleException;

    /**
     * Permet la suppression d'une entit� detailFamille
     * 
     * @param detailFamille
     *            Le detailFamille � supprimer
     * @return Le detailFamille supprim�
     * @throws DetailFamilleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DocumentException
     */
    public SimpleDetailFamille delete(SimpleDetailFamille detailFamille) throws DetailFamilleException,
            JadePersistenceException, DocumentException, JadeApplicationServiceNotAvailableException;

    public ArrayList<String> essaiAjax(String idDetailFamille);

    /**
     * G�n�ration d'un document de correspondance
     * 
     * @param idDossier
     *            idSubside
     * @param idFormule
     *            idFormule
     * @return map cl�s 'warning' et 'error'
     */
    public HashMap<String, Object> generateDocumentCorrespondance(String idDossier, String idFormule);

    /**
     * Ecriture d'un subside en db, selon une simulation donn�e. Cr�era de nouveaux subsides et inscrira les documents y
     * relatifs.
     * 
     * @param simulation
     *            le container de simulation renseign�
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
     * Ecriture d'un subside en db, selon une simulation donn�e. Cr�era de nouveaux subsides et inscrira les documents y
     * relatifs.
     * 
     * Surcharge de la m�thode generateSubside(String, String, Boolean). Le param�tre isRecalculUnfavorable est forc� �
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
     * Ecriture d'un subside en db, selon une simulation donn�e. Cr�era de nouveaux subsides et inscrira les documents y
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
     * Service de g�n�ration des subsides depuis l'�cran des attributions (appel AJAX). Remplace l'appel depuis
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
     * R�cup�ration de la liste des documents disponible pour appel AJAX detail famille
     * 
     * @param idDossier
     * @return ArrayList<ParametreModelComplex>
     */
    public ArrayList<ParametreModelComplex> getAvailableDocumentsListCorrespondance(String idDossier);

    /**
     * G�n�ration des propri�t�s n�cessaires � la GED pour la publication
     * 
     * @param detailFamille
     * @return Propri�t�s renseign�es
     */
    public Properties getGEDPublishProperties(SimpleDetailFamille detailFamille, SimpleControleurEnvoiStatus statusEnvoi);

    /**
     * G�n�ration des propri�t�s n�cessaire � la GED pour la publication
     * 
     * @param idDetailFamille
     * @return Propri�t�s renseign�es
     */
    public Properties getGEDPublishProperties(String idDetailFamille, String idStatusEnvoi);

    /**
     * R�cup�ration d'un tableau de r�sultats annonces pour un idDetailFamille particulier
     * 
     * @param idDetailFamille
     *            l'id concern�
     * @return un tableau renseign�
     */
    public ArrayList<ComplexControleurAnnonceDetail> getHistoriqueAnnonces(String idDetailFamille);

    /**
     * R�cup�ration d'un tableau de r�sultats envois pour un idDetailFamille particulier
     * 
     * @param idDetailFamille
     *            l'id concern�
     * @return un tableau renseign�
     */
    public ArrayList<ComplexControleurEnvoiDetail> getHistoriqueEnvois(String idDetailFamille);

    /**
     * R�cup�ration d'un id Formule en fonction de son noFormule Amal (historique)
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
     * @return nom formatt� : tif_21.10.2011_11-20-35_669424_42000013
     */
    public String getInteractivDocumentFileName(String csDateComplete, String idDetailFamille, String idFormule,
            String csFormule);

    /**
     * R�cup�ration du chemin complet du fichier g�n�r� depuis le client
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
     * R�cup�ration du chemin complet du fichier g�n�r� depuis le serveur de job
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
     * R�cup�re une liste des revenus pour le calcul des subsides
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
     * R�cup�ration de la liste des subsides calcul�s en fonction de diff�rents param�tres :
     * 
     * idContribuable anneeHistorique typeDemande idRevenu revenuIsTaxation
     * 
     * @param values
     *            valeur �num�r�es ci-dessus
     * @return
     */
    public CalculsSubsidesContainer getListSubsidesCalculAjax(HashMap<?, ?> values);

    /**
     * R�cup�re une liste des taxations pour le calcul des subsides
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
     * R�cup�ration d'une liste de taxation li�e � un calcul de revenu d�terminant selon l'ann�e historique
     * 
     * @param idContribuable
     * @param annee
     * @return
     */
    public ArrayList<CalculsTaxationContainer> getListTaxationsCalcul(String idContribuable, String annee);

    /**
     * R�cup�ration d'une liste de taxation li�e � un calcul de revenu d�terminant selon l'ann�e historique
     * 
     * @param idContribuable
     * @param annee
     * @return
     */
    public ArrayList<CalculsTaxationContainer> getListTaxationsCalculAjax(HashMap<?, ?> values);

    /**
     * Permet de charger en m�moire un detailFamille
     * 
     * @param idDetailFamille
     *            L'identifiant du detailFamille � charger en m�moire
     * @return Le detailFamille charg� en m�moire
     * @throws DetailFamilleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDetailFamille read(String idDetailFamille) throws DetailFamilleException, JadePersistenceException;

    /**
     * Permet de chercher des detail famille (subside) selon un mod�le de crit�res.
     * 
     * @param detailFamilleSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleDetailFamilleSearch search(SimpleDetailFamilleSearch detailFamilleSearch)
            throws JadePersistenceException, DetailFamilleException;

    /**
     * Permet de chercher des documents (envois) selon un mod�le de crit�res.
     * 
     * @param simpleDocumentSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DocumentException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleDocumentSearch search(SimpleDocumentSearch simpleDocumentSearch) throws JadePersistenceException,
            DocumentException;

    /**
     * Permet la mise � jour d'une entit� detailFamille
     * 
     * @param detailFamille
     *            Le detailFamille � mettre � jour
     * @return Le detailFamille mis � jour
     * @throws DetailFamilleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDetailFamille update(SimpleDetailFamille detailFamille) throws DetailFamilleException,
            JadePersistenceException;

    /**
     * Write the document in the job table
     * 
     * @param csDateComplete
     *            date complete format 28.02.2011_22:15:55
     * @param csModele
     *            Modele concern� (CS noModele)
     * @param csJobType
     *            Type de job (CS type de job)
     * @param idDetailFamille
     *            idDetailFamille concern�
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
