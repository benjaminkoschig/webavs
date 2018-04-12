package globaz.al.vb.attribut;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import java.util.ArrayList;
import ch.globaz.al.business.constantes.ALConstAttributsEntite;
import ch.globaz.al.business.models.attribut.AttributEntiteModel;
import ch.globaz.al.business.models.attribut.AttributEntiteSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.naos.business.model.AffiliationSimpleModel;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import ch.globaz.orion.business.constantes.EBProperties;
import ch.globaz.param.business.models.ParameterSearchModel;

/**
 * @author GMO
 * 
 */
/**
 * ViewBean g�rant l'interaction avec les attributs d'un affili�
 * 
 * @author GMO
 * 
 */
public class ALAffilieViewBean extends BJadePersistentObjectViewBean {

    /**
     * Mod�le repr�sentant l'affili� dont on pr�sente les attributs
     */
    private AffiliationSimpleModel affilie = null;
    /**
     * Liste des attributs affich�s dans l'�cran
     */
    private ArrayList attributsList = null;

    /**
     * Mod�le de recherche pour retrouver les attributs selon l'affili�
     */
    private AttributEntiteSearchModel searchAttributAffilie = null;

    /**
     * Param�tres g�n�raux de l'application correspondants � ceux par affili� (valeurs par d�faut)
     */
    private ParameterSearchModel searchParameters = null;

    /**
     * Constructeur du viewBean
     */
    public ALAffilieViewBean() {
        super();
        affilie = new AffiliationSimpleModel();
        searchAttributAffilie = new AttributEntiteSearchModel();
        searchParameters = new ParameterSearchModel();
        attributsList = new ArrayList(5);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        throw new Exception(this.getClass() + " - Method called (add) not implemented (might be never called)");

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        throw new Exception(this.getClass() + " - Method called (delete) not implemented (might be never called)");

    }

    /**
     * @return affilie
     */
    public AffiliationSimpleModel getAffilie() {
        return affilie;
    }

    /**
     * Retourne l'�l�ment de la liste des attributs correspondant � l'attribut de l'affichage n� salari�
     * 
     * @return attribut affichage n� salari�
     */
    public String getAttributAffichageNumSalarie() {

        for (int i = 0; i < attributsList.size(); i++) {
            if (ALConstAttributsEntite.AFFICHAGE_NUMERO_SALARIE.equals(((AttributEntiteModel) attributsList.get(i))
                    .getNomAttribut())) {
                return ((AttributEntiteModel) attributsList.get(i)).getValeurAlpha();
            }
        }
        return null;
    }

    /**
     * Retourne l'�l�ment de la liste des attributs correspondant � l'attribut des avis d'�ch�ances (copies salari� ou
     * employeur ou pas de copie)
     * 
     * @return attribut copie d'avis �ch�ances
     */
    public String getAttributAvisEcheance() {
        for (int i = 0; i < attributsList.size(); i++) {
            if (ALConstAttributsEntite.AVIS_ECHEANCE_DESTINATAIRE.equals(((AttributEntiteModel) attributsList.get(i))
                    .getNomAttribut())) {
                return ((AttributEntiteModel) attributsList.get(i)).getValeurAlpha();
            }
        }
        return null;
    }

    /**
     * Retourne l'�l�ment de la liste des attributs correspondant � l'attribut de l'envoi des r�caps
     * 
     * @return attribut mode d'envoi des r�caps
     */
    public String getAttributEnvoiRecap() {
        for (int i = 0; i < attributsList.size(); i++) {
            if (ALConstAttributsEntite.MODE_ENVOI_RECAP.equals(((AttributEntiteModel) attributsList.get(i))
                    .getNomAttribut())) {
                return ((AttributEntiteModel) attributsList.get(i)).getValeurAlpha();
            }
        }
        return null;
    }

    /**
     * Retourne l'�l�ment de la liste des attributs correspondant � l'attribut du format des r�caps
     * 
     * @return attribut format des r�caps
     */
    public String getAttributFormatRecap() {
        for (int i = 0; i < attributsList.size(); i++) {
            if (ALConstAttributsEntite.FORMAT_RECAP.equals(((AttributEntiteModel) attributsList.get(i))
                    .getNomAttribut())) {
                return ((AttributEntiteModel) attributsList.get(i)).getValeurAlpha();
            }
        }
        return null;
    }

    /**
     * Retourne l'�l�ment de la liste des attributs correspondant � l'attribut du paiement direct (indique si direct ou
     * non pour les dossiers de l'affili�)
     * 
     * @return attribut paiement direct
     */
    public String getAttributPaiementDirect() {
        for (int i = 0; i < attributsList.size(); i++) {
            if (ALConstAttributsEntite.PAIEMENT_DIRECT.equals(((AttributEntiteModel) attributsList.get(i))
                    .getNomAttribut())) {
                return ((AttributEntiteModel) attributsList.get(i)).getValeurAlpha();
            }
        }
        return null;
    }

    /**
     * Retourne la liste des attributs � afficher dans l'�cran
     * 
     * @return attributsList
     */
    public ArrayList getAttributsList() {
        return attributsList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return affilie.getId();
    }

    /**
     * R�cup�ration du mod�le de recherche des attributs entit�
     * 
     * @return searchAttributAffilie
     */
    public AttributEntiteSearchModel getSearchAttributAffilie() {
        return searchAttributAffilie;
    }

    public ParameterSearchModel getSearchParameters() {
        return searchParameters;
    }

    /**
     * @return session actuelle
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (affilie != null) && !affilie.isNew() ? new BSpy(affilie.getSpy()) : new BSpy(getSession());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        affilie = AFBusinessServiceLocator.getAffiliationService().read(getId());

        attributsList = ALServiceLocator.getAttributEntiteBusinessService().getAttributListForEntite(getId(),
                AffiliationSimpleModel.class.getName().toString());

    }

    /**
     * D�finit l'affili� � afficher
     * 
     * @param affilie
     *            nouvelle valeur de l'attribut
     */
    public void setAffilie(AffiliationSimpleModel affilie) {
        this.affilie = affilie;
    }

    /**
     * D�finit l'attribut d'affichage du n� salari�
     * 
     * @param attributValue
     *            nouvelle valeur de l'attribut
     */
    public void setAttributAffichageNumSalarie(String attributValue) {
        // on cherche l'attribut correspondant et on lui set la valeur pass�e en
        // param
        for (int i = 0; i < attributsList.size(); i++) {
            if (((AttributEntiteModel) attributsList.get(i)).getNomAttribut().equals(
                    ALConstAttributsEntite.AFFICHAGE_NUMERO_SALARIE)) {
                ((AttributEntiteModel) attributsList.get(i)).setValeurAlpha(attributValue);
            }
        }
    }

    /**
     * D�finit l'attribut g�rant la copie d'avis �ch�ance
     * 
     * @param attributValue
     *            nouvelle valeur de l'attribut
     */
    public void setAttributAvisEcheance(String attributValue) {
        // on cherche l'attribut correspondant et on lui set la valeur pass�e en
        // param
        for (int i = 0; i < attributsList.size(); i++) {
            if (((AttributEntiteModel) attributsList.get(i)).getNomAttribut().equals(
                    ALConstAttributsEntite.AVIS_ECHEANCE_DESTINATAIRE)) {
                ((AttributEntiteModel) attributsList.get(i)).setValeurAlpha(attributValue);
            }
        }
    }

    /**
     * D�finit l'attribut du mode d'envoi de la r�cap
     * 
     * @param attributValue
     *            nouvelle valeur de l'attribut
     */
    public void setAttributEnvoiRecap(String attributValue) {
        // on cherche l'attribut correspondant et on lui set la valeur pass�e en
        // param
        for (int i = 0; i < attributsList.size(); i++) {
            if (((AttributEntiteModel) attributsList.get(i)).getNomAttribut().equals(
                    ALConstAttributsEntite.MODE_ENVOI_RECAP)) {
                ((AttributEntiteModel) attributsList.get(i)).setValeurAlpha(attributValue);
            }
        }
    }

    /**
     * D�finit l'attribut du format de la r�cap
     * 
     * @param attributValue
     *            nouvelle valeur de l'attribut
     */
    public void setAttributFormatRecap(String attributValue) {
        // on cherche l'attribut correspondant et on lui set la valeur pass�e en
        // param
        for (int i = 0; i < attributsList.size(); i++) {
            if (((AttributEntiteModel) attributsList.get(i)).getNomAttribut().equals(
                    ALConstAttributsEntite.FORMAT_RECAP)) {
                ((AttributEntiteModel) attributsList.get(i)).setValeurAlpha(attributValue);
            }
        }
    }

    /**
     * D�finit l'attribut du paiement direct
     * 
     * @param attributValue
     *            nouvelle valeur de l'attribut
     */
    public void setAttributPaiementDirect(String attributValue) {
        // on cherche l'attribut correspondant et on lui set la valeur pass�e en
        // param
        for (int i = 0; i < attributsList.size(); i++) {
            if (((AttributEntiteModel) attributsList.get(i)).getNomAttribut().equals(
                    ALConstAttributsEntite.PAIEMENT_DIRECT)) {
                ((AttributEntiteModel) attributsList.get(i)).setValeurAlpha(attributValue);
            }
        }
    }

    /**
     * D�finit la liste des attributs affich�s dans l'�cran
     * 
     * @param attributsList
     */
    public void setAttributsList(ArrayList attributsList) {
        this.attributsList = attributsList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        affilie.setId(newId);

    }

    /**
     * D�finit le mod�le de recherche pour rechercher les attributs de l'affili�
     * 
     * @param searchAttributAffilie
     */
    public void setSearchAttributAffilie(AttributEntiteSearchModel searchAttributAffilie) {
        this.searchAttributAffilie = searchAttributAffilie;
    }

    /**
     * @param searchParameters
     */
    public void setSearchParameters(ParameterSearchModel searchParameters) {
        this.searchParameters = searchParameters;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {

        // on met � jour l'attribut pour chacun faisant partie de la liste li� �
        // l'affili�
        for (int i = 0; i < attributsList.size(); i++) {
            ALServiceLocator.getAttributEntiteBusinessService().changeAttributValueForEntite(getId(),
                    affilie.getClass().getName().toString(), (AttributEntiteModel) attributsList.get(i));
        }

    }

    /**
     * Retourne la valeur de la propri�t� orion.ebusiness.connected
     * 
     * @return
     * @throws PropertiesException
     */
    public boolean isEbusinessConnected() throws PropertiesException {
        return EBProperties.EBUSINESS_CONNECTED.getBooleanValue();
    }
}
