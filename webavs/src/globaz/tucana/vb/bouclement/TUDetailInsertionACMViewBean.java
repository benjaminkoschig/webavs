package globaz.tucana.vb.bouclement;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.db.BTransaction;
import globaz.itucana.constantes.ITUCSRubriqueListeDesRubriques;
import globaz.tucana.constantes.ITUCSConstantes;
import globaz.tucana.db.bouclement.TUDetailInsertionACMListViewBean;
import globaz.tucana.db.bouclement.access.TUDetail;
import globaz.tucana.exception.fw.TUFWAddException;
import globaz.tucana.exception.fw.TUFWDeleteException;
import globaz.tucana.exception.fw.TUFWFindException;
import globaz.tucana.vb.TUAbstractPersitentObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Classe permettant l'insertion du nombre de jours et de cartes militaires et maternités
 * 
 * @author fgo
 * @version 1.0
 * 
 */
public class TUDetailInsertionACMViewBean extends TUAbstractPersitentObject {
    private String canton = "";

    private String carteMaternite = "";

    private String carteMilitaire = "";

    private String idBouclement = "";

    private String jourMaternite = "";

    private String jourMilitaire = "";

    private ArrayList key = null;

    /**
     * Constructeur
     */
    public TUDetailInsertionACMViewBean() {
        init();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.tucana.vb.TUAbstractPersitentObject#add()
     */
    @Override
    public void add() throws Exception {
        BTransaction transaction = null;
        try {
            transaction = new BTransaction((BSession) getISession());
            // recherche des enregistrement ACM existant
            TUDetailInsertionACMListViewBean listVB = rechercheACMExistant(transaction.getSession());
            // mémorisation des 4 ACM
            Map memDetailACM = memorisationDetail(listVB);
            // effacement des enregistrements ACM
            effaceEnregistrementACM(transaction, memDetailACM);
            // mise à jour des enregistrements ACM
            ArrayList detailMaj = majEnregistrementACM(memDetailACM);
            // création des nouveaux enregistrements ACM
            creationEnregistrementACM(transaction, detailMaj);
            if (!transaction.hasErrors()) {
                transaction.commit();
            }
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            transaction.closeTransaction();
        }
    }

    /**
     * Création de l'entité TUDetail en fonction de la rubrique
     * 
     * @param cle
     * @return
     */
    private TUDetail createEntityDetail(String cle) {
        TUDetail detail = new TUDetail();
        detail.setIdBouclement(getIdBouclement());
        detail.setCsApplication(ITUCSConstantes.CS_APPLICATION_ACM);
        detail.setCsTypeRubrique(ITUCSConstantes.CS_TY_RUBR_MANUELLE);
        detail.setCanton(getCanton());
        detail.setCsRubrique(cle);
        return detail;
    }

    private void creationEnregistrementACM(BTransaction transaction, ArrayList detailMaj) throws TUFWAddException {
        Iterator it = detailMaj.iterator();
        // lecture de tous les enregistrements ACM
        try {
            while (it.hasNext()) {
                TUDetail entity = (TUDetail) it.next();
                entity.setSession(transaction.getSession());
                entity.setIdDetail("0");
                entity.add(transaction);
            }
        } catch (Exception e) {
            throw new TUFWAddException(getClass().getName() + ".creationEngregistrementACM()", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.tucana.vb.TUAbstractPersitentObject#delete()
     */
    @Override
    public void delete() throws Exception {
    }

    /**
     * Effacement des enregistrements ACM
     * 
     * @param transaction
     * @param memDetailACM
     * @throws TUFWDeleteException
     *             levée de l'exception en cas de problème d'effacement
     */
    private void effaceEnregistrementACM(BTransaction transaction, Map memDetailACM) throws TUFWDeleteException {
        Iterator it = memDetailACM.keySet().iterator();
        try {
            // lecture de tous les enregistrement ACM
            while (it.hasNext()) {
                BEntity entity = (BEntity) memDetailACM.get(it.next());
                // effacement de l'enregistrement ACM
                entity.delete(transaction);
            }
        } catch (Exception e) {
            // soulève une exception en cas de problème lors de l'effacement
            throw new TUFWDeleteException(getClass().getName() + ".effaceEnregistrement()", e);
        }
    }

    /**
     * Récupération du canton
     * 
     * @return
     */
    public String getCanton() {
        return canton;
    }

    /**
     * Récupère le nombre de carte maternité
     * 
     * @return
     */
    public String getCarteMaternite() {
        return carteMaternite;
    }

    /**
     * Récupère le nombre de carte militaire
     * 
     * @return
     */
    public String getCarteMilitaire() {
        return carteMilitaire;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.tucana.vb.TUAbstractPersitentObject#getId()
     */
    @Override
    public String getId() {
        return getIdBouclement();
    }

    /**
     * Récupère l'id du bouclement
     * 
     * @return
     */
    public String getIdBouclement() {
        return idBouclement;
    }

    /**
     * Récupère le nombre de carte maternité
     * 
     * @return
     */
    public String getJourMaternite() {
        return jourMaternite;
    }

    /**
     * Récupère le nombre de jour militaire
     * 
     * @return
     */
    public String getJourMilitaire() {
        return jourMilitaire;
    }

    /**
     * Renvoie le nombre en fonction de la rubrique
     * 
     * @param cle
     * @return
     */
    private String getNombre(String cle) {
        if (ITUCSRubriqueListeDesRubriques.CS_NOMBRE_DE_CARTES_MATERNITE_CAPG.equals(cle)) {
            return getCarteMaternite();
        } else if (ITUCSRubriqueListeDesRubriques.CS_NOMBRE_DE_CARTES_SERVICE_MILITAIRE_CAPG.equals(cle)) {
            return getCarteMilitaire();
        } else if (ITUCSRubriqueListeDesRubriques.CS_NOMBRE_DE_JOURS_COMPENSES_MATERNITE_CAPG.equals(cle)) {
            return getJourMaternite();
        } else {
            return getJourMilitaire();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.tucana.vb.TUAbstractPersitentObject#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return null;
    }

    private void init() {
        key = new ArrayList(4);
        key.add(0, ITUCSRubriqueListeDesRubriques.CS_NOMBRE_DE_CARTES_MATERNITE_CAPG);
        key.add(1, ITUCSRubriqueListeDesRubriques.CS_NOMBRE_DE_CARTES_SERVICE_MILITAIRE_CAPG);
        key.add(2, ITUCSRubriqueListeDesRubriques.CS_NOMBRE_DE_JOURS_COMPENSES_MATERNITE_CAPG);
        key.add(3, ITUCSRubriqueListeDesRubriques.CS_NOMBRE_DE_JOURS_COMPENSES_SERVICE_MILITAIRE_CAPG);
    }

    /**
     * Mise à jour de l'enregistrement ACM si exite, met à jour le nombre sinon créer l'entite TUDetail
     * 
     * @param memDetailACM
     * @return
     */
    private ArrayList majEnregistrementACM(Map memDetailACM) {
        ArrayList al = new ArrayList();
        Iterator it = key.iterator();
        while (it.hasNext()) {
            String cle = (String) it.next();
            TUDetail detail = null;
            if (memDetailACM.containsKey(cle)) {
                // si existe met à jour le nombre
                detail = (TUDetail) memDetailACM.get(cle);
            } else {
                // si n'exite pas, créé l'entité
                detail = createEntityDetail(cle);
            }
            // mise à jour du nombre
            detail.setNombreMontant(getNombre(cle));
            al.add(detail);
        }
        return al;
    }

    private Map memorisationDetail(TUDetailInsertionACMListViewBean listVB) {
        Map tm = new TreeMap();
        for (int i = 0; i < listVB.size(); i++) {
            TUDetail entity = (TUDetail) listVB.getEntity(i);
            tm.put(entity.getCsRubrique(), entity);
        }
        return tm;
    }

    /**
     * Recherche si des rubriques ACM existent
     * 
     * @param session
     * @throws TUFWFindException
     */
    private TUDetailInsertionACMListViewBean rechercheACMExistant(BSession session) throws TUFWFindException {
        // Lecture des 4 rubriques ACM du bouclement
        TUDetailInsertionACMListViewBean viewBean = new TUDetailInsertionACMListViewBean();
        viewBean.setSession(session);
        viewBean.setForIdBouclement(getIdBouclement());
        viewBean.setForCanton(getCanton());
        try {
            viewBean.find();
            return viewBean;
        } catch (Exception e) {
            throw new TUFWFindException(this.getClass().getName() + ".rechercheACMExistant()",
                    viewBean.getCurrentSqlQuery(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.tucana.vb.TUAbstractPersitentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
    }

    /**
     * Modification cantion
     * 
     * @param newCanton
     */
    public void setCanton(String newCanton) {
        canton = newCanton;
    }

    /**
     * Modifie le nombre de carte maternité
     * 
     * @param newCarteMaternite
     */
    public void setCarteMaternite(String newCarteMaternite) {

        carteMaternite = newCarteMaternite;
    }

    /**
     * Modifie le nombre de carte militaire
     * 
     * @param newCarteMilitaire
     */
    public void setCarteMilitaire(String newCarteMilitaire) {
        carteMilitaire = newCarteMilitaire;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.tucana.vb.TUAbstractPersitentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
    }

    /**
     * Modifie l'id bouclement
     * 
     * @param newIdBouclement
     */
    public void setIdBouclement(String newIdBouclement) {
        idBouclement = newIdBouclement;
    }

    /**
     * Modifie le nombre de carte maternité
     * 
     * @param newJourMaternite
     */
    public void setJourMaternite(String newJourMaternite) {
        jourMaternite = newJourMaternite;
    }

    /**
     * Modifie le nombre de jour militaire
     * 
     * @param newJourMilitaire
     */
    public void setJourMilitaire(String newJourMilitaire) {
        jourMilitaire = newJourMilitaire;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.tucana.vb.TUAbstractPersitentObject#update()
     */
    @Override
    public void update() throws Exception {
    }
}
