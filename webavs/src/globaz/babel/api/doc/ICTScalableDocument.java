/*
 * Créé le 20 nov. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.api.doc;

import java.io.Serializable;
import java.util.List;

/**
 * @author bsc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public interface ICTScalableDocument extends Serializable {

    public static final int FROM_ECRAN_EDIT_PARAGRAPH = 2;
    public static final int FROM_ECRAN_INIT = 0;
    public static final int FROM_ECRAN_SELECT_ANNEXE_COPIE = 3;
    public static final int FROM_ECRAN_SELECT_PARAGRAPH = 1;

    /**
     * Donne le CS du destinataire du document
     * 
     * @return
     */
    public String getDocumentCsDestinataire();

    /**
     * Donne le CS du domaine du document
     * 
     * @return
     */
    public String getDocumentCsDomaine();

    /**
     * Donne le CS du type du document
     * 
     * @return
     */
    public String getDocumentCsType();

    /**
     * Donne la liste des documents pour la pre-visualisation
     * 
     * @return
     */
    public List getDocumentsPreview();

    /**
     * Donne l'adresse a laquelle doit être envoye le document
     * 
     * @return
     */
    public String getEMailAddress();

    /**
     * Donne le nom de la classe qui implemente le generateur de document
     * 
     * @return
     */
    public String getGeneratorImplCalssName();

    /**
     * Donne l'id de la demande
     * 
     * @return
     */
    public String getIdDemande();

    /**
     * Donne l'id tiers d'un nouvel intervenant
     * 
     * @return
     */
    public String getIdTiersIntervenant();

    /**
     * getter pour l'attribut methodes selection intervenant.
     * 
     * @return la valeur courante de l'attribut methodes selection intervenant
     */
    public Object[] getMethodesSelecteurIntervenant();

    /**
     * Donne la prochaine URL en fonction de: - wantSelectParagraph - wantEditParagraph - wantSelectAnnexeCopie -
     * ecranPrecedant
     * 
     * @return
     */
    public String getNextUrl();

    /**
     * Donne le no AVS du tiers principal
     * 
     * @return
     */
    public String getNoAVSTiersPrincipal();

    /**
     * Donne les nom et prenom du tiers principal
     * 
     * @return
     */
    public String getNomPrenomTiersPrincipal();

    /**
     * Donne l'URL précédante en fonction de: - wantSelectParagraph - wantEditParagraph - wantSelectAnnexeCopie -
     * ecranPrecedant
     * 
     * @return
     */
    public String getPreviousUrl();

    /**
     * Donne l'url utilisee lors de l'arret et du terme du traitement
     * 
     * @return url de retour
     */
    public String getReturnUrl();

    /**
     * 
     * @return
     */
    public String getScalableDocumentDataAsXml();

    /**
     * Donne les proprietes parametrables du document
     * 
     * @return
     */
    public ICTScalableDocumentProperties getScalableDocumentProperties();

    /**
     * Donne le niveau selectionne dans l'ecran de choix des paragraphes
     * 
     * @return
     */
    public int getSelectedNiveau();

    /**
     * Donne l'url de la premiere page du processus d'impression
     * 
     * @return
     */
    public String getUrlFirstPage();

    /**
     * True si l'on passe par l'écran d'edition des paragraphes
     * 
     * @return
     */
    public boolean getWantEditParagraph();

    /**
     * True si l'on passe par l'écran de selection des annexes et copies
     * 
     * @return
     */
    public boolean getWantSelectAnnexeCopie();

    /**
     * True si l'on passe par l'écran de selection des paragraphe
     * 
     * @return
     */
    public boolean getWantSelectParagraph();

    /**
     * vrais si l'action aller vers precedent est executee
     * 
     * @return
     */
    public boolean isPreviousAction();

    /**
     * Set le CS du destinataire du document
     * 
     * @param csDestinataire
     */
    public void setDocumentCsDestinataire(String csDestinataire);

    /**
     * Set le CS du domaine du document
     * 
     * @param csDomaine
     */
    public void setDocumentCsDomaine(String csDomaine);

    /**
     * Set le CS du type du document
     * 
     * @param csType
     */
    public void setDocumentCsType(String csType);

    public void setDocumentsPreview(List docList);

    /**
     * Set l'adresse a laquelle doit être envoye le document
     * 
     * @param eMailAdress
     */
    public void setEMailAddress(String eMailAdress);

    /**
     * Set le nom de la classe qui implemente le generateur de document
     * 
     * @param className
     */
    public void setGeneratorImplClassName(String className);

    /**
     * Set l'id de la demande
     * 
     * @param idTiers
     */
    public void setIdDemande(String idDemande);

    /**
     * Set l'id tiers d'un nouvel intervenant
     * 
     * @param idTiers
     */
    public void setIdTiersIntervenant(String idTiers);

    /**
     * Set le no AVS du tiers principal
     * 
     * @param noAVS
     */
    public void setNoAVSTiersPrincipal(String noAVS);

    /**
     * Set les nom et prenom du tiers principal
     * 
     * @param nomPrenom
     */
    public void setNomPrenomTiersPrincipal(String nomPrenom);

    public void setPreviousAction(boolean isPreviousAction);

    /**
     * Set l'url utilisee lors de l'arret et du terme du traitement
     * 
     * @param url
     */
    public void setReturnUrl(String url);

    /**
     * Set les proprietes parametrables du document
     * 
     * @param documentProperties
     */
    public void setScalableDocumentProperties(ICTScalableDocumentProperties documentProperties);

    /**
     * Set le niveau selectionne dans l'ecran de choix des paragraphes
     * 
     * @param selectedNiveau
     */
    public void setSelectedNiveau(int selectedNiveau);

    /**
     * 
     * @param urlFirstPage
     */
    public void setUrlFirstPage(String urlFirstPage);

    /**
     * 
     * @param wantEditParagraph
     */
    public void setWantEditParagraph(boolean wantEditParagraph);

    /**
     * 
     * @param wantSelectAnnexeCopie
     */
    public void setWantSelectAnnexeCopie(boolean wantSelectAnnexeCopie);

    /**
     * 
     * @param wantSelectParagraph
     */
    public void setWantSelectParagraph(boolean wantSelectParagraph);
}
