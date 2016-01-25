/**
 * 
 */
package ch.globaz.amal.business.models.annoncesedex;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.ArrayList;

/**
 * @author dhi
 * 
 */
public class SimpleAnnonceSedexSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forDateMessage = null;

    private String forDateMessageGOE = null;

    private String forDateMessageLOE = null;

    private String forIdAnnonceSedex = null;

    private String forIdContribuable = null;

    private String forIdDetailFamille = null;

    private String forIdTiersCM = null;

    private String forMessageEmetteur = null;

    private String forMessageId = null;

    private String forMessageRecepteur = null;

    private String forMessageSubType = null;

    private String forMessageType = null;

    private String forNumeroCourant = null;

    private String forNumeroDecision = null;

    private ArrayList<String> inIdTiersCM = null;

    private ArrayList<String> inMessageEmetteur = null;

    private ArrayList<String> inMessageRecepteur = null;

    private ArrayList<String> inMessageSubType = null;

    private ArrayList<String> inMessageType = null;

    private ArrayList<String> inStatus = null;

    private ArrayList<String> inTraitement = null;

    /**
     * Default Constructor
     */
    public SimpleAnnonceSedexSearch() {
    }

    /**
     * @return the forDateMessage
     */
    public String getForDateMessage() {
        return forDateMessage;
    }

    /**
     * @return the forDateMessageGOE
     */
    public String getForDateMessageGOE() {
        return forDateMessageGOE;
    }

    /**
     * @return the forDateMessageLOE
     */
    public String getForDateMessageLOE() {
        return forDateMessageLOE;
    }

    /**
     * @return the forIdAnnonceSedex
     */
    public String getForIdAnnonceSedex() {
        return forIdAnnonceSedex;
    }

    /**
     * @return the forIdContribuable
     */
    public String getForIdContribuable() {
        return forIdContribuable;
    }

    /**
     * @return the forIdDetailFamille
     */
    public String getForIdDetailFamille() {
        return forIdDetailFamille;
    }

    /**
     * @return the forIdTiersCM
     */
    public String getForIdTiersCM() {
        return forIdTiersCM;
    }

    /**
     * @return the forMessageEmetteur
     */
    public String getForMessageEmetteur() {
        return forMessageEmetteur;
    }

    public String getForMessageId() {
        return forMessageId;
    }

    /**
     * @return the forMessageRecepteur
     */
    public String getForMessageRecepteur() {
        return forMessageRecepteur;
    }

    /**
     * @return the forMessageSubType
     */
    public String getForMessageSubType() {
        return forMessageSubType;
    }

    /**
     * @return the forMessageType
     */
    public String getForMessageType() {
        return forMessageType;
    }

    public String getForNumeroCourant() {
        return forNumeroCourant;
    }

    public String getForNumeroDecision() {
        return forNumeroDecision;
    }

    public ArrayList<String> getInIdTiersCM() {
        return inIdTiersCM;
    }

    /**
     * @return the inMessageEmetteur
     */
    public ArrayList<String> getInMessageEmetteur() {
        return inMessageEmetteur;
    }

    /**
     * @return the inMmessageRecepteur
     */
    public ArrayList<String> getInMessageRecepteur() {
        return inMessageRecepteur;
    }

    public ArrayList<String> getInMessageSubType() {
        return inMessageSubType;
    }

    public ArrayList<String> getInMessageType() {
        return inMessageType;
    }

    /**
     * @return the inStatus
     */
    public ArrayList<String> getInStatus() {
        return inStatus;
    }

    /**
     * @return the inTraitement
     */
    public ArrayList<String> getInTraitement() {
        return inTraitement;
    }

    /**
     * @param forDateMessage
     *            the forDateMessage to set
     */
    public void setForDateMessage(String forDateMessage) {
        this.forDateMessage = forDateMessage;
    }

    /**
     * @param forDateMessageGOE
     *            the forDateMessageGOE to set
     */
    public void setForDateMessageGOE(String forDateMessageGOE) {
        this.forDateMessageGOE = forDateMessageGOE;
    }

    /**
     * @param forDateMessageLOE
     *            the forDateMessageLOE to set
     */
    public void setForDateMessageLOE(String forDateMessageLOE) {
        this.forDateMessageLOE = forDateMessageLOE;
    }

    /**
     * @param forIdAnnonceSedex
     *            the forIdAnnonceSedex to set
     */
    public void setForIdAnnonceSedex(String forIdAnnonceSedex) {
        this.forIdAnnonceSedex = forIdAnnonceSedex;
    }

    /**
     * @param forIdContribuable
     *            the forIdContribuable to set
     */
    public void setForIdContribuable(String forIdContribuable) {
        this.forIdContribuable = forIdContribuable;
    }

    /**
     * @param forIdDetailFamille
     *            the forIdDetailFamille to set
     */
    public void setForIdDetailFamille(String forIdDetailFamille) {
        this.forIdDetailFamille = forIdDetailFamille;
    }

    /**
     * @param forIdTiersCM
     *            the forIdTiersCM to set
     */
    public void setForIdTiersCM(String forIdTiersCM) {
        this.forIdTiersCM = forIdTiersCM;
    }

    /**
     * @param forMessageEmetteur
     *            the forMessageEmetteur to set
     */
    public void setForMessageEmetteur(String forMessageEmetteur) {
        this.forMessageEmetteur = forMessageEmetteur;
    }

    public void setForMessageId(String forMessageId) {
        this.forMessageId = forMessageId;
    }

    /**
     * @param forMessageRecepteur
     *            the forMessageRecepteur to set
     */
    public void setForMessageRecepteur(String forMessageRecepteur) {
        this.forMessageRecepteur = forMessageRecepteur;
    }

    /**
     * @param forMessageSubType
     *            the forMessageSubType to set
     */
    public void setForMessageSubType(String forMessageSubType) {
        this.forMessageSubType = forMessageSubType;
    }

    /**
     * @param forMessageType
     *            the forMessageType to set
     */
    public void setForMessageType(String forMessageType) {
        this.forMessageType = forMessageType;
    }

    public void setForNumeroCourant(String forNumeroCourant) {
        this.forNumeroCourant = forNumeroCourant;
    }

    public void setForNumeroDecision(String forNumeroDecision) {
        this.forNumeroDecision = forNumeroDecision;
    }

    public void setInIdTiersCM(ArrayList<String> inIdTiersCM) {
        this.inIdTiersCM = inIdTiersCM;
    }

    /**
     * @param inMessageEmetteur
     *            the inMessageEmetteur to set
     */
    public void setInMessageEmetteur(ArrayList<String> inMessageEmetteur) {
        this.inMessageEmetteur = inMessageEmetteur;
    }

    /**
     * @param inMmessageRecepteur
     *            the inMmessageRecepteur to set
     */
    public void setInMessageRecepteur(ArrayList<String> inMessageRecepteur) {
        this.inMessageRecepteur = inMessageRecepteur;
    }

    public void setInMessageSubType(ArrayList<String> inMessageSubType) {
        this.inMessageSubType = inMessageSubType;
    }

    public void setInMessageType(ArrayList<String> inMessageType) {
        this.inMessageType = inMessageType;
    }

    /**
     * @param inStatus
     *            the inStatus to set
     */
    public void setInStatus(ArrayList<String> inStatus) {
        this.inStatus = inStatus;
    }

    /**
     * @param inTraitement
     *            the inTraitement to set
     */
    public void setInTraitement(ArrayList<String> inTraitement) {
        this.inTraitement = inTraitement;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimpleAnnonceSedex.class;
    }

}
