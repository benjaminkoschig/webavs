package globaz.tucana.adapter;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JACalendar;
import globaz.itucana.constantes.TUTypesBouclement;
import globaz.itucana.exception.TUProcessCSTucanaInterfaceException;
import globaz.itucana.exception.TUProcessEnteteExistTucanaInterfaceException;
import globaz.itucana.exception.TUProcessFWTucanaInterfaceException;
import globaz.itucana.exception.TUProcessNoBouclementTucanaInterfaceException;
import globaz.itucana.exception.TUProcessTucanaInterfaceException;
import globaz.itucana.model.ITUEntete;
import globaz.itucana.model.ITUModelBouclement;
import globaz.itucana.properties.TUPropertiesProvider;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.tucana.application.TUApplication;
import globaz.tucana.constantes.ITUCSConstantes;
import globaz.tucana.db.bouclement.TUApplicationBouclementListViewBean;
import globaz.tucana.db.bouclement.TUBouclementListViewBean;
import globaz.tucana.db.bouclement.access.TUBouclement;
import globaz.tucana.db.bouclement.access.TUDetail;
import globaz.tucana.db.bouclement.access.TUNoPassage;
import globaz.tucana.exception.fw.TUFWUpdateException;
import globaz.tucana.model.TULigneBouclement;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Classe mère de tous les adapteurs
 * 
 * @author fgo date de création : 7 juin 2006
 * @version : version 1.0
 * 
 */
public abstract class TUCommonAdapter {

    /**
     * Méthode permettant de créer l'entête d'un bouclement
     * 
     * @param transaction
     * @param application
     * @param bouclement
     * @throws TUProcessTucanaInterfaceException
     */
    protected static String addBouclement(BTransaction transaction, TUTypesBouclement application, ITUEntete entete)
            throws TUProcessTucanaInterfaceException {
        /*
         * Teste l'existance de l'entête pour l'application, dans tel cas, soulève une exception
         */
        try {
            testApplicationEntete(transaction, application, entete);

        } catch (Exception e) {
            if (e instanceof TUProcessTucanaInterfaceException) {
                throw (TUProcessTucanaInterfaceException) e;
            } else {
                throw new TUProcessFWTucanaInterfaceException("TUCommonAdapter.addBouclement() : erreur lors du count",
                        e);
            }
        }
        /*
         * Ajoute l'entête en base de donnée
         */
        String idBouclement = null;
        try {
            idBouclement = createEntete(transaction, application, entete);

        } catch (Exception e) {
            if (e instanceof TUProcessTucanaInterfaceException) {
                throw (TUProcessTucanaInterfaceException) e;
            } else {
                throw new TUProcessFWTucanaInterfaceException(
                        "TUCreateBouclement.rechercheBouclement() : erreur lors de la création d'entête", e);
            }
        }
        return idBouclement;
    }

    /**
     * Créé les lignes de bouclement
     * 
     * @param transaction
     * @param application
     * @param bouclement
     * @param idBouclement
     * @throws TUProcessFWTucanaInterfaceException
     * @throws TUProcessCSTucanaInterfaceException
     */
    protected static void addLignes(BTransaction transaction, TUTypesBouclement application, Iterator lignes,
            String idBouclement, String noPassage) throws TUProcessFWTucanaInterfaceException,
            TUProcessCSTucanaInterfaceException {
        Iterator iter = lignes;
        TUDetail detail = null;
        Map myMap = loadRubrique(transaction);
        while (iter.hasNext()) {
            TULigneBouclement element = (TULigneBouclement) iter.next();
            // Assignation des valeurs
            if (!JadeNumericUtil.isEmptyOrZero(element.getRubrique())) {
                detail = new TUDetail();
                detail.setSession(transaction.getSession());
                detail.setCanton(element.getCanton());
                detail.setCsApplication(application.getTypeBouclement());
                detail.setCsRubrique(element.getRubrique());
                detail.setCsTypeRubrique((String) myMap.get(element.getRubrique()));
                detail.setDateImport(JACalendar.todayJJsMMsAAAA());
                detail.setIdBouclement(idBouclement);
                detail.setNombreMontant(element.getMontantNombre());
                detail.setNoPassage(noPassage);
                try {
                    detail.add(transaction);
                } catch (Exception e) {
                    throw new TUProcessFWTucanaInterfaceException(
                            "TUCreateBouclement.createLignes() : problème lors de l'ajout en DB", e);
                }
            }
        }
    }

    /**
     * Création de l'entête en base se données
     * 
     * @param transaction
     * @param application
     * @param entete
     * @return idBouclement
     * @throws Exception
     */
    private static String createEntete(BTransaction transaction, TUTypesBouclement application, ITUEntete entete)
            throws Exception {
        TUBouclement bouclement = rechercheBouclement(transaction, application, entete);
        // Créé l'enregistrement N° passage
        TUNoPassage noPassage = new TUNoPassage();
        noPassage.setSession(transaction.getSession());
        noPassage.setCsApplication(application.getTypeBouclement());
        noPassage.setIdBouclement(bouclement.getIdBouclement());
        noPassage.setNoPassage(entete.getNoPassage());
        noPassage.add(transaction);

        majBouclement(bouclement);
        return bouclement.getIdBouclement();
    }

    /**
     * Chargement l'id code système et l'id sélection pour les codes système rubrique
     * 
     * @param transaction
     * @return Map une map contenant en clé l'id code système de la rubrique et en valeur l'id sélection de cette
     *         rubrique
     * @throws TUProcessCSTucanaInterfaceException
     */
    private static Map loadRubrique(BTransaction transaction) throws TUProcessCSTucanaInterfaceException {
        // Recherche des rubriques
        FWParametersSystemCodeManager manager = new FWParametersSystemCodeManager();
        manager.setSession(transaction.getSession());
        manager.setForIdTypeCode(ITUCSConstantes.CS_RUBRIQUE);
        try {
            manager.find();
        } catch (Exception e) {
            throw new TUProcessCSTucanaInterfaceException(
                    "TUCreatBouclement.loadRubrique() : erreur lors du chargement des rubriques", e);
        }
        // Si aucune rubrique trouvée lève une exception
        if (manager.size() == 0) {
            throw new TUProcessCSTucanaInterfaceException("TUCreatBouclement.loadRubrique() : aucune rubrique trouvée");
        }
        // Chargement de la collection avec l'id code système de la rubrique en
        // clé et l'id sélection
        Map myMap = new TreeMap();
        for (int i = 0; i < manager.size(); i++) {
            FWParametersSystemCode code = (FWParametersSystemCode) manager.getEntity(i);
            myMap.put(code.getIdCode(), code.getIdSelection());
        }
        return myMap;
    }

    private static void majBouclement(TUBouclement bouclement) throws TUFWUpdateException {
        bouclement.setDateEtat(JACalendar.todayJJsMMsAAAA());
        try {
            bouclement.update();
        } catch (Exception e) {
            throw new TUFWUpdateException(
                    "TUCommonAdapter.majBouclement() : impossible de mettre à jour la date d'état du bouclement : "
                            + bouclement.getIdBouclement(), e);
        }
    }

    /**
     * 
     * Recherche le bouclement si non existant soulève une exception. En fait un bouclement doit forcément existé, car
     * il est créé lors de l'exportation du précédent
     * 
     * @param transaction
     * @param application
     * @param entete
     * @return TUBouclement
     * @throws Exception
     */
    private static TUBouclement rechercheBouclement(BTransaction transaction, TUTypesBouclement application,
            ITUEntete entete) throws Exception {
        TUBouclementListViewBean listViewBean = new TUBouclementListViewBean();
        listViewBean.setSession(transaction.getSession());
        listViewBean.setForAnneeComptable(entete.getAnneeComptable());
        listViewBean.setForMoisComptable(entete.getMoisComptable());
        listViewBean.setForCsAgence(transaction.getSession().getApplication().getProperty(TUApplication.CS_AGENCE));
        listViewBean.find();
        // Lève une exception si aucun enregistrement bouclement trouvé sinon
        // retroune le bouclement
        if (listViewBean.size() == 0) {
            throw new TUProcessNoBouclementTucanaInterfaceException(
                    "TUCreateBouclement.rechercheBouclement() : le bouclement n'existe pas !");
        } else {
            return (TUBouclement) listViewBean.getEntity(0);
        }

    }

    /**
     * Test si l'entête est déjà créée dans telle cas erreur soulevée
     * 
     * @param transaction
     * @param application
     * @param entete
     * @throws Exception
     */
    private static void testApplicationEntete(BTransaction transaction, TUTypesBouclement application, ITUEntete entete)
            throws Exception {

        TUApplicationBouclementListViewBean listViewBean = new TUApplicationBouclementListViewBean();
        listViewBean.setSession(transaction.getSession());
        listViewBean.setForCsApplication(application.getTypeBouclement());
        listViewBean.setForAnneeComptable(entete.getAnneeComptable());
        listViewBean.setForMoisComptable(entete.getMoisComptable());
        System.out.println(TUPropertiesProvider.getInstance().getProperty(TUApplication.CS_AGENCE));
        try {
            BSession sessionTmp = new BSession();
            sessionTmp.setApplication(TUApplication.DEFAULT_APPLICATION_TUCANA);
            listViewBean.setForCsAgence(sessionTmp.getApplication().getProperty(TUApplication.CS_AGENCE));
        } catch (Exception e) {
            throw new TUProcessTucanaInterfaceException("Fichier 'TUCANA.properties' non trouvé");
        }
        if (listViewBean.getCount() != 0) {
            throw new TUProcessEnteteExistTucanaInterfaceException(
                    "TUCreateBouclement.testEntete() : l'entête existe déjà !");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.itucana.adapter.ICommonAdapter#handleBouclement(globaz.globall .db.BTransaction,
     * globaz.itucana.model.ITUModelBouclement)
     */
    public final void handleBouclement(BTransaction transaction, ITUModelBouclement modelBouclement)
            throws TUProcessTucanaInterfaceException {
        String idBouclement = handleEntete(transaction, modelBouclement.getEntete());
        handleLines(transaction, modelBouclement.getLines(), idBouclement, modelBouclement.getEntete().getNoPassage());
    }

    /**
     * Implémenter dans les classes filles le source à effectuer pour une entête
     * 
     * @param transaction
     * @param entete
     */
    protected abstract String handleEntete(BTransaction transaction, ITUEntete entete)
            throws TUProcessTucanaInterfaceException;

    /**
     * Implémenter dans les classes filles le source à effectuer pour une ligne
     * 
     * @param transaction
     * @param iterator
     */
    protected abstract void handleLines(BTransaction transaction, Iterator lignes, String idBouclement, String noPassage)
            throws TUProcessTucanaInterfaceException;
}
