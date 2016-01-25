/**
 * 
 */
package ch.globaz.amal.business.models.annoncesedex;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.ArrayList;

/**
 * @author dhi
 * 
 */
public class ComplexAnnonceSedexSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCMActifs = null;
    private String forCMIdTiersCaisse = null;
    private String forCMIdTiersGroupe = null;
    private String forCMNumCaisse = null;
    private String forCMNumGroupe = null;
    private String forCMTypeAdmin = null;
    private String forCONTContribuableActif = null;
    private String forCONTIdContribuable = null;
    private String forFAMIsContribuable = null;
    private String forFAMNoPersonne = null;
    private String forNoDecisionGT = null;
    private String forSDXDateMessage = null;
    private String forSDXDateMessageGOE = null;
    private String forSDXDateMessageLOE = null;
    private String forSDXIdAnnonceSedex = null;
    private String forSDXIdAnnonceSedexLT = null;
    private String forSDXIdContribuable = null;
    private String forSDXIdDetailFamille = null;
    private String forSDXIdTiersCM = null;
    private String forSDXMessageEmetteur = null;
    private String forSDXMessageRecepteur = null;
    private String forSDXMessageSubType = null;
    private String forSDXMessageType = null;
    private String forSDXNoDecision = null;
    private String forSUBAnneeHistorique = null;
    private ArrayList<String> inCMIdTiersCaisse = null;
    private ArrayList<String> inSDXIdContribuable = null;
    private ArrayList<String> inSDXMessageEmetteur = null;
    private ArrayList<String> inSDXMessageRecepteur = null;
    private ArrayList<String> inSDXMessageSubType = null;
    private ArrayList<String> inSDXStatus = null;
    private ArrayList<String> inSDXTraitement = null;

    private String likeCMNomCaisse = null;

    /**
     * Default constructor
     */
    public ComplexAnnonceSedexSearch() {
    }

    /**
     * @return the forCMActifs
     */
    public String getForCMActifs() {
        return forCMActifs;
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

    /**
     * @return the forCMTypeAdmin
     */
    public String getForCMTypeAdmin() {
        return forCMTypeAdmin;
    }

    /**
     * @return the forCONTContribuableActif
     */
    public String getForCONTContribuableActif() {
        return forCONTContribuableActif;
    }

    /**
     * @return the forCONTIdContribuable
     */
    public String getForCONTIdContribuable() {
        return forCONTIdContribuable;
    }

    /**
     * @return the forFAMIsContribuable
     */
    public String getForFAMIsContribuable() {
        return forFAMIsContribuable;
    }

    /**
     * @return the forFAMNoPersonne
     */
    public String getForFAMNoPersonne() {
        return forFAMNoPersonne;
    }

    public String getForNoDecisionGT() {
        return forNoDecisionGT;
    }

    /**
     * @return the forSDXDateMessage
     */
    public String getForSDXDateMessage() {
        return forSDXDateMessage;
    }

    /**
     * @return the forSDXDateMessageGOE
     */
    public String getForSDXDateMessageGOE() {
        return forSDXDateMessageGOE;
    }

    /**
     * @return the forSDXDateMessageLOE
     */
    public String getForSDXDateMessageLOE() {
        return forSDXDateMessageLOE;
    }

    /**
     * @return the forSDXIdAnnonceSedex
     */
    public String getForSDXIdAnnonceSedex() {
        return forSDXIdAnnonceSedex;
    }

    public String getForSDXIdAnnonceSedexLT() {
        return forSDXIdAnnonceSedexLT;
    }

    /**
     * @return the forSDXIdContribuable
     */
    public String getForSDXIdContribuable() {
        return forSDXIdContribuable;
    }

    /**
     * @return the forSDXIdDetailFamille
     */
    public String getForSDXIdDetailFamille() {
        return forSDXIdDetailFamille;
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

    public String getForSDXNoDecision() {
        return forSDXNoDecision;
    }

    public String getForSUBAnneeHistorique() {
        return forSUBAnneeHistorique;
    }

    public ArrayList<String> getInCMIdTiersCaisse() {
        return inCMIdTiersCaisse;
    }

    public ArrayList<String> getInSDXIdContribuable() {
        return inSDXIdContribuable;
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
     * @return the inSDXTraitement
     */
    public ArrayList<String> getInSDXTraitement() {
        return inSDXTraitement;
    }

    /**
     * @return the likeCMNomCaisse
     */
    public String getLikeCMNomCaisse() {
        return likeCMNomCaisse;
    }

    /**
     * @param forCMActifs
     *            the forCMActifs to set
     */
    public void setForCMActifs(String forCMActifs) {
        this.forCMActifs = forCMActifs;
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
     * @param forCMTypeAdmin
     *            the forCMTypeAdmin to set
     */
    public void setForCMTypeAdmin(String forCMTypeAdmin) {
        this.forCMTypeAdmin = forCMTypeAdmin;
    }

    /**
     * @param forCONTContribuableActif
     *            the forCONTContribuableActif to set
     */
    public void setForCONTContribuableActif(String forCONTContribuableActif) {
        this.forCONTContribuableActif = forCONTContribuableActif;
    }

    /**
     * @param forCONTIdContribuable
     *            the forCONTIdContribuable to set
     */
    public void setForCONTIdContribuable(String forCONTIdContribuable) {
        this.forCONTIdContribuable = forCONTIdContribuable;
    }

    /**
     * @param forFAMIsContribuable
     *            the forFAMIsContribuable to set
     */
    public void setForFAMIsContribuable(String forFAMIsContribuable) {
        this.forFAMIsContribuable = forFAMIsContribuable;
    }

    /**
     * @param forFAMNoPersonne
     *            the forFAMNoPersonne to set
     */
    public void setForFAMNoPersonne(String forFAMNoPersonne) {
        this.forFAMNoPersonne = forFAMNoPersonne;
    }

    public void setForNoDecisionGT(String forNoDecisionGT) {
        this.forNoDecisionGT = forNoDecisionGT;
    }

    /**
     * @param forSDXDateMessage
     *            the forSDXDateMessage to set
     */
    public void setForSDXDateMessage(String forSDXDateMessage) {
        this.forSDXDateMessage = forSDXDateMessage;
    }

    /**
     * @param forSDXDateMessageGOE
     *            the forSDXDateMessageGOE to set
     */
    public void setForSDXDateMessageGOE(String forSDXDateMessageGOE) {
        this.forSDXDateMessageGOE = forSDXDateMessageGOE;
    }

    /**
     * @param forSDXDateMessageLOE
     *            the forSDXDateMessageLOE to set
     */
    public void setForSDXDateMessageLOE(String forSDXDateMessageLOE) {
        this.forSDXDateMessageLOE = forSDXDateMessageLOE;
    }

    /**
     * @param forSDXIdAnnonceSedex
     *            the forSDXIdAnnonceSedex to set
     */
    public void setForSDXIdAnnonceSedex(String forSDXIdAnnonceSedex) {
        this.forSDXIdAnnonceSedex = forSDXIdAnnonceSedex;
    }

    public void setForSDXIdAnnonceSedexLT(String forSDXIdAnnonceSedexLT) {
        this.forSDXIdAnnonceSedexLT = forSDXIdAnnonceSedexLT;
    }

    /**
     * @param forSDXIdContribuable
     *            the forSDXIdContribuable to set
     */
    public void setForSDXIdContribuable(String forSDXIdContribuable) {
        this.forSDXIdContribuable = forSDXIdContribuable;
    }

    /**
     * @param forSDXIdDetailFamille
     *            the forSDXIdDetailFamille to set
     */
    public void setForSDXIdDetailFamille(String forSDXIdDetailFamille) {
        this.forSDXIdDetailFamille = forSDXIdDetailFamille;
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

    public void setForSDXNoDecision(String forSDXNoDecision) {
        this.forSDXNoDecision = forSDXNoDecision;
    }

    public void setForSUBAnneeHistorique(String forSUBAnneeHistorique) {
        this.forSUBAnneeHistorique = forSUBAnneeHistorique;
    }

    public void setInCMIdTiersCaisse(ArrayList<String> inCMIdTiersCaisse) {
        this.inCMIdTiersCaisse = inCMIdTiersCaisse;
    }

    public void setInSDXIdContribuable(ArrayList<String> inSDXIdContribuable) {
        this.inSDXIdContribuable = inSDXIdContribuable;
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
     * @param inSDXTraitement
     *            the inSDXTraitement to set
     */
    public void setInSDXTraitement(ArrayList<String> inSDXTraitement) {
        this.inSDXTraitement = inSDXTraitement;
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
        return ComplexAnnonceSedex.class;
    }

}
