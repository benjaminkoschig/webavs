/**
 * 
 */
package ch.globaz.amal.business.models.annoncesedexco;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.ArrayList;

/**
 * @author cbu
 * 
 */
public class ComplexAnnonceSedexCO4Search extends JadeSearchComplexModel {

    private static final long serialVersionUID = 1L;
    private String forCMIdTiersCaisse = null;
    private String forCMIdTiersGroupe = null;
    private String forCMNumCaisse = null;
    private String forCMNumGroupe = null;
    private String forSDXDateAnnonce = null;
    private String forSDXDateAnnonceGOE = null;
    private String forSDXDateAnnonceLOE = null;
    private String forSDXIdAnnonceSedexCO = null;
    private String forSDXIdAnnonceSedexCOLT = null;
    private String forSDXIdTiersCM = null;
    private String forSDXMessageEmetteur = null;
    private String forSDXMessageRecepteur = null;
    private String forSDXMessageSubType = null;
    private String forSDXMessageType = null;
    private ArrayList<String> inCMIdTiersCaisse = null;
    private ArrayList<String> inSDXMessageEmetteur = null;
    private ArrayList<String> inSDXMessageRecepteur = null;
    private ArrayList<String> inSDXMessageSubType = null;
    private ArrayList<String> inSDXStatus = null;

    private String likeCMNomCaisse = null;

    /**
     * Default constructor
     */
    public ComplexAnnonceSedexCO4Search() {
    }

    /**
     * @return the forCMIdTiersCaisse
     */
    public String getForCMIdTiersCaisse() {
        return forCMIdTiersCaisse;
    }

    /**
     * @return the forCMIdTiersGroupe
     */
    public String getForCMIdTiersGroupe() {
        return forCMIdTiersGroupe;
    }

    /**
     * @return the forCMNumCaisse
     */
    public String getForCMNumCaisse() {
        return forCMNumCaisse;
    }

    /**
     * @return the forCMNumGroupe
     */
    public String getForCMNumGroupe() {
        return forCMNumGroupe;
    }

    public String getForSDXDateAnnonce() {
        return forSDXDateAnnonce;
    }

    public void setForSDXDateAnnonce(String forSDXDateAnnonce) {
        this.forSDXDateAnnonce = forSDXDateAnnonce;
    }

    public String getForSDXDateAnnonceGOE() {
        return forSDXDateAnnonceGOE;
    }

    public void setForSDXDateAnnonceGOE(String forSDXDateAnnonceGOE) {
        this.forSDXDateAnnonceGOE = forSDXDateAnnonceGOE;
    }

    public String getForSDXDateAnnonceLOE() {
        return forSDXDateAnnonceLOE;
    }

    public void setForSDXDateAnnonceLOE(String forSDXDateAnnonceLOE) {
        this.forSDXDateAnnonceLOE = forSDXDateAnnonceLOE;
    }

    public String getForSDXIdAnnonceSedexCO() {
        return forSDXIdAnnonceSedexCO;
    }

    public void setForSDXIdAnnonceSedexCO(String forSDXIdAnnonceSedexCO) {
        this.forSDXIdAnnonceSedexCO = forSDXIdAnnonceSedexCO;
    }

    public String getForSDXIdAnnonceSedexCOLT() {
        return forSDXIdAnnonceSedexCOLT;
    }

    public void setForSDXIdAnnonceSedexCOLT(String forSDXIdAnnonceSedexCOLT) {
        this.forSDXIdAnnonceSedexCOLT = forSDXIdAnnonceSedexCOLT;
    }

    /**
     * @return the forSDXIdTiersCM
     */
    public String getForSDXIdTiersCM() {
        return forSDXIdTiersCM;
    }

    /**
     * @return the forSDXMessageEmetteur
     */
    public String getForSDXMessageEmetteur() {
        return forSDXMessageEmetteur;
    }

    /**
     * @return the forSDXMessageRecepteur
     */
    public String getForSDXMessageRecepteur() {
        return forSDXMessageRecepteur;
    }

    /**
     * @return the forSDXMessageSubType
     */
    public String getForSDXMessageSubType() {
        return forSDXMessageSubType;
    }

    /**
     * @return the forSDXMessageType
     */
    public String getForSDXMessageType() {
        return forSDXMessageType;
    }

    public ArrayList<String> getInCMIdTiersCaisse() {
        return inCMIdTiersCaisse;
    }

    /**
     * @return the inSDXMessageEmetteur
     */
    public ArrayList<String> getInSDXMessageEmetteur() {
        return inSDXMessageEmetteur;
    }

    /**
     * @return the inSDXMessageRecepteur
     */
    public ArrayList<String> getInSDXMessageRecepteur() {
        return inSDXMessageRecepteur;
    }

    public ArrayList<String> getInSDXMessageSubType() {
        return inSDXMessageSubType;
    }

    /**
     * @return the inSDXStatus
     */
    public ArrayList<String> getInSDXStatus() {
        return inSDXStatus;
    }

    /**
     * @return the likeCMNomCaisse
     */
    public String getLikeCMNomCaisse() {
        return likeCMNomCaisse;
    }

    /**
     * @param forCMIdTiersCaisse
     *            the forCMIdTiersCaisse to set
     */
    public void setForCMIdTiersCaisse(String forCMIdTiersCaisse) {
        this.forCMIdTiersCaisse = forCMIdTiersCaisse;
    }

    /**
     * @param forCMIdTiersGroupe
     *            the forCMIdTiersGroupe to set
     */
    public void setForCMIdTiersGroupe(String forCMIdTiersGroupe) {
        this.forCMIdTiersGroupe = forCMIdTiersGroupe;
    }

    /**
     * @param forCMNumCaisse
     *            the forCMNumCaisse to set
     */
    public void setForCMNumCaisse(String forCMNumCaisse) {
        this.forCMNumCaisse = forCMNumCaisse;
    }

    /**
     * @param forCMNumGroupe
     *            the forCMNumGroupe to set
     */
    public void setForCMNumGroupe(String forCMNumGroupe) {
        this.forCMNumGroupe = forCMNumGroupe;
    }

    /**
     * @param forSDXIdTiersCM
     *            the forSDXIdTiersCM to set
     */
    public void setForSDXIdTiersCM(String forSDXIdTiersCM) {
        this.forSDXIdTiersCM = forSDXIdTiersCM;
    }

    /**
     * @param forSDXMessageEmetteur
     *            the forSDXMessageEmetteur to set
     */
    public void setForSDXMessageEmetteur(String forSDXMessageEmetteur) {
        this.forSDXMessageEmetteur = forSDXMessageEmetteur;
    }

    /**
     * @param forSDXMessageRecepteur
     *            the forSDXMessageRecepteur to set
     */
    public void setForSDXMessageRecepteur(String forSDXMessageRecepteur) {
        this.forSDXMessageRecepteur = forSDXMessageRecepteur;
    }

    /**
     * @param forSDXMessageSubType
     *            the forSDXMessageSubType to set
     */
    public void setForSDXMessageSubType(String forSDXMessageSubType) {
        this.forSDXMessageSubType = forSDXMessageSubType;
    }

    /**
     * @param forSDXMessageType
     *            the forSDXMessageType to set
     */
    public void setForSDXMessageType(String forSDXMessageType) {
        this.forSDXMessageType = forSDXMessageType;
    }

    public void setInCMIdTiersCaisse(ArrayList<String> inCMIdTiersCaisse) {
        this.inCMIdTiersCaisse = inCMIdTiersCaisse;
    }

    /**
     * @param inSDXMessageEmetteur
     *            the inSDXMessageEmetteur to set
     */
    public void setInSDXMessageEmetteur(ArrayList<String> inSDXMessageEmetteur) {
        this.inSDXMessageEmetteur = inSDXMessageEmetteur;
    }

    /**
     * @param inSDXMessageRecepteur
     *            the inSDXMessageRecepteur to set
     */
    public void setInSDXMessageRecepteur(ArrayList<String> inSDXMessageRecepteur) {
        this.inSDXMessageRecepteur = inSDXMessageRecepteur;
    }

    public void setInSDXMessageSubType(ArrayList<String> inSDXMessageSubType) {
        this.inSDXMessageSubType = inSDXMessageSubType;
    }

    /**
     * @param inSDXStatus
     *            the inSDXStatus to set
     */
    public void setInSDXStatus(ArrayList<String> inSDXStatus) {
        this.inSDXStatus = inSDXStatus;
    }

    /**
     * @param likeCMNomCaisse
     *            the likeCMNomCaisse to set
     */
    public void setLikeCMNomCaisse(String likeCMNomCaisse) {
        this.likeCMNomCaisse = likeCMNomCaisse != null ? JadeStringUtil.convertSpecialChars(likeCMNomCaisse)
                .toUpperCase() : null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return ComplexAnnonceSedexCO4.class;
    }

}
