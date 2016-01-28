package globaz.hermes.api.helper;

import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.hermes.api.IHEAnnoncesViewBean;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe helper d'une interface d'API pour la cr�ation d'annonces<br>
 * Ceci est la super classe et ne devrait pas �tre utilis�e directement.<br>
 * 
 * @author ado
 */
public class IHEAnnoncesViewBeanHelper extends GlobazHelper implements IHEAnnoncesViewBean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur du type IHEAnnoncesViewBeanHelper
     */
    public IHEAnnoncesViewBeanHelper() {
        super("globaz.hermes.db.gestion.HEAnnoncesViewBean");
    }

    /**
     * Constructeur du type IHEAnnoncesViewBeanHelper
     * 
     * @param valueObject
     *            le Value Object contenant les donn�es
     */
    public IHEAnnoncesViewBeanHelper(GlobazValueObject valueObject) {
        super(valueObject);
    }

    /**
     * Constructeur du type IHEAnnoncesViewBeanHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'impl�mentation
     */
    public IHEAnnoncesViewBeanHelper(String implementationClassName) {
        super(implementationClassName);
    }

    @Override
    public void clear() {
        HashMap inputTable = (HashMap) _getValueObject().getProperty("inputTable");
        if (inputTable != null) {
            inputTable.clear();
            _getValueObject().setProperty("inputTable", inputTable);
        }
    }

    @Override
    public String getChampEnregistrementFromAttr() throws Exception {
        this._invoke("initChampEnregistrementFromAttr");
        return (String) _getValueObject().getProperty("champEnregistrement");
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hermes.api.IHEAnnoncesViewBean#getDateEngagement()
     */
    @Override
    public String getDateEngagement() throws Exception {
        Object res = _getObject("getDateEngagement", new Object[] {});
        if (res == null) {
            return null;
        } else {
            return (String) res;
        }
    }

    /**
     * Renvoie la valeur du champ
     * 
     * @param FIELD
     *            la variable statique d�signant le champ
     * @return la valeur du champ
     */
    @Override
    public String getField(String FIELD) throws Exception {
        HashMap t = (HashMap) _getValueObject().getProperty("inputTable");
        return (String) t.get(FIELD);
    }

    /**
     * Renvoie la clef primaire de l'annonce dans la bdd
     * 
     * @return la valeur de la propri�t� idAnnonce
     */
    @Override
    public String getIdAnnonce() {
        return (String) _getValueObject().getProperty("idAnnonce");
    }

    /**
     * Renvoie la clef primaire du lot pour cette annonce<br>
     * Une annonce appartient forc�ment � un lot<br>
     * Si aucun lot, alors c'est le lot du jour
     * 
     * @return la valeur de la propri�t� idLot
     */
    @Override
    public String getIdLot() {
        return (String) _getValueObject().getProperty("idLot");
    }

    /**
     * Renvoie le message associ� � cette annonce (Impression par exemple)
     * 
     * @return la valeur de la propri�t� idMessage
     */
    public String getIdMessage() {
        return (String) _getValueObject().getProperty("idMessage");
    }

    /**
     * Renvoie le nom du programme qui cr�� cette annonce (HELIOS, PAVO...)
     * 
     * @return la valeur de la propri�t� idProgramme
     */
    public String getIdProgramme() {
        return (String) _getValueObject().getProperty("idProgramme");
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hermes.api.IHEAnnoncesViewBean#getNumeroAffilie()
     */
    @Override
    public String getNumeroAffilie() throws Exception {
        Object res = _getObject("getNumeroAffilieForDeclSalaire", new Object[] {});
        if (res == null) {
            return null;
        } else {
            return (String) res;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hermes.api.IHEAnnoncesViewBean#getNumeroEmploye()
     */
    @Override
    public String getNumeroEmploye() throws Exception {
        Object res = _getObject("getNumeroEmploye", new Object[] {});
        if (res == null) {
            return null;
        } else {
            return (String) res;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hermes.api.IHEAnnoncesViewBean#getNumeroSuccursale()
     */
    @Override
    public String getNumeroSuccursale() throws Exception {
        Object res = _getObject("getNumeroSuccursale", new Object[] {});
        if (res == null) {
            return null;
        } else {
            return (String) res;
        }
    }

    /**
     * Renvoie la r�f�rence unique
     * 
     * @return la valeur de la propri�t� refUnique
     */
    @Override
    public String getRefUnique() {
        return (String) _getValueObject().getProperty("refUnique");
    }

    /**
     * @return statut
     */
    @Override
    public String getStatut() {
        return (String) _getValueObject().getProperty("statut");
    }

    /**
     * Renvoie le type du lot pour cette annonce<br>
     * Une annonce appartient forc�ment � un lot<br>
     * Si aucun lot, alors c'est le lot du jour
     * 
     * @return la valeur de la propri�t� typeLot
     */
    public String getTypeLot() {
        return (String) _getValueObject().getProperty("typeLot");
    }

    /**
     * Renvoie l'utilisateur qui a cr�� cette annonce
     * 
     * @return la valeur de la propri�t� utilisateur
     */
    @Override
    public final String getUtilisateur() {
        return (String) _getValueObject().getProperty("utilisateur");
    }

    /**
     * Ins�re la valeur d'un champ<br>
     * ex : <code>put(CODE_APPLICATION,"11");</code>
     * 
     * @param le
     *            champ � remplir
     * @param la
     *            valeur du champ
     */
    @Override
    public void put(String idField, String value) {
        HashMap inputTable = (HashMap) _getValueObject().getProperty("inputTable");
        if (inputTable == null) {
            inputTable = new HashMap();
        }
        try {
            if (idField.equals(IHEAnnoncesViewBean.CODE_APPLICATION)
                    && (inputTable.get(IHEAnnoncesViewBean.CODE_APPLICATION) != null)
                    && !((String) inputTable.get(IHEAnnoncesViewBean.CODE_APPLICATION)).equals(value)) {
                setRefUnique(null);
            }
        } catch (Exception e) {
        }
        inputTable.put(idField, value);
        _getValueObject().setProperty("inputTable", inputTable);
    }

    /**
     * Ins�re une Map de valeurs<br>
     * La clef est la variable statique d�signant le champ<br>
     * La valeur est la valeur du champ<br>
     * 
     * @param valueMap
     *            l'objet de type Map contenant les clefs et les valeurs
     */
    @Override
    public void putAll(Map m) {
        HashMap inputTable = (HashMap) _getValueObject().getProperty("inputTable");
        if (inputTable == null) {
            inputTable = new HashMap();
        }
        try {
            if (m.containsKey(IHEAnnoncesViewBean.CODE_APPLICATION)
                    && inputTable.containsKey(IHEAnnoncesViewBean.CODE_APPLICATION)) {
                String newCodeApp = (String) m.get(IHEAnnoncesViewBean.CODE_APPLICATION);
                String oldCodeApp = getField(IHEAnnoncesViewBean.CODE_APPLICATION);
                if ((oldCodeApp != null) && !newCodeApp.equals(oldCodeApp)) {
                    // rupture sur le code application
                    setRefUnique(null);
                }
            }
            /*
             * Iterator keyIterator = m.keySet().iterator(); while (keyIterator.hasNext()) {
             * 
             * }
             */
        } catch (Exception e) {
        }
        inputTable.putAll(m);
        _getValueObject().setProperty("inputTable", inputTable);
    }

    @Override
    public void setArchivage(Boolean bool) {
        // TODO Raccord de m�thode auto-g�n�r�
        _getValueObject().setProperty("archivagePavo", bool);

    }

    @Override
    public void setCategorie(String cat) {
        _getValueObject().setProperty("categorie", cat);
    }

    @Override
    public void setDateEngagement(String date) {
        _getValueObject().setProperty("dateEngagement", date);

    }

    /**
     * D�finit la valeur de la propri�t� idAnnonce c'est la clef primaire de l'annonce
     * 
     * @param newIdAnnonce
     *            newIdAnnonce
     */
    @Override
    public void setIdAnnonce(String newIdAnnonce) {
        _getValueObject().setProperty("idAnnonce", newIdAnnonce);
    }

    /**
     * D�finit la valeur de la propri�t� idLot clef �trang�re permettant d'identifier le lot auquel appartient cette
     * annonce
     * 
     * @param newIdLot
     *            newIdLot
     */
    @Override
    public void setIdLot(String newIdLot) {
        _getValueObject().setProperty("idLot", newIdLot);
    }

    /**
     * D�finit la valeur de la propri�t� idProgramme Le programme qui fait cet ajour (HELIOS, PAVO...)
     * 
     * @param newIdProgramme
     *            newIdProgramme
     */
    @Override
    public void setIdProgramme(String newIdProgramme) {
        _getValueObject().setProperty("idProgramme", newIdProgramme);
    }

    @Override
    public void setLangueCorrespondance(String langueCorrespondance) {
        _getValueObject().setProperty("langueCorrespondance", langueCorrespondance);
    }

    @Override
    public void setNumeroAffilie(String numeroAffilie) {
        _getValueObject().setProperty("numeroAffilie", numeroAffilie);
    }

    @Override
    public void setNumeroEmploye(String numeroEmploye) {
        _getValueObject().setProperty("numeroEmploye", numeroEmploye);
    }

    @Override
    public void setNumeroSuccursale(String numeroSuccursale) {
        _getValueObject().setProperty("numeroSuccursale", numeroSuccursale);
    }

    @Override
    public void setPriorite(String pty) {
        _getValueObject().setProperty("prioriteLot", pty);
    }

    /**
     * D�finit la valeur de la propri�t� refUnique
     * 
     * @param newRef
     *            newRef
     */
    @Override
    public void setRefUnique(String newRef) {
        _getValueObject().setProperty("refUnique", newRef);
    }

    /**
	 *
	 */
    @Override
    public void setStatut(String statut) {
        _getValueObject().setProperty("statut", statut);
    }

    /**
     * D�finit le type du lot clef �trang�re permettant d'identifier le lot auquel appartient cette annonce
     * 
     * @param newIdLot
     *            newIdLot
     */
    @Override
    public void setTypeLot(String newTypeLot) {
        _getValueObject().setProperty("typeLot", newTypeLot);
    }

    /**
     * D�finit la valeur de la propri�t� utilisateur L'utilisateur qui cr�� cette annonce
     * 
     * @param newUtilisateur
     *            newUtilisateur
     */
    @Override
    public void setUtilisateur(String newUtilisateur) {
        _getValueObject().setProperty("utilisateur", newUtilisateur);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hermes.api.IHEAnnoncesViewBean#wantCheckCiOuvert(boolean)
     */
    @Override
    public void wantCheckCiOuvert(String valeur) {
        _getValueObject().setProperty("wantCheckCiOuvert", valeur);
    }

    @Override
    public void wantCheckNumAffilie(String valeur) {
        _getValueObject().setProperty("wantCheckNumAffilie", valeur);
    }

}
