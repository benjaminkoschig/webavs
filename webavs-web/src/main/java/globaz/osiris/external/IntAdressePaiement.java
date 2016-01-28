package globaz.osiris.external;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (27.11.2001 15:18:47)
 * 
 * @author: Administrator
 */
public interface IntAdressePaiement extends BIEntity {
    String BANQUE = "3";
    String BANQUE_INTERNATIONAL = "5";
    String BVR = "7";
    String CCP = "2";
    String CCP_INTERNATIONAL = "4";
    String DEBIT_DIRECT = "8";
    String MANDAT = "1";
    String MANDAT_INTERNATIONAL = "6";

    String TYPE_DEFAULT = "100";
    String TYPE_RECOUVREMENT = "101";
    String TYPE_VERSEMENT = "102";

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (11.12.2001 16:55:52)
     * 
     * @return globaz.osiris.interfaceext.tiers.IntAdresseCourrier
     */
    IntAdresseCourrier getAdresseCourrier();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.12.2001 11:04:10)
     * 
     * @return globaz.osiris.interfaceext.tiers.IntBanqueOsi
     */
    IntBanque getBanque();

    /**
     * Retourne le code ISO du pays.
     * 
     * @return
     */
    public String getCodeISOPays();

    /**
     * Cette m�thode retourne la date de d�but de la relation de l'adresse de paiement
     * 
     * @return String date de d�but de la relation
     */
    String getDateDebutRelation();

    /**
     * Cette m�thode retourne la date de fin de la relation de l'adresse de paiement
     * 
     * @return String date de fin de la relation
     */
    public String getDateFinRelation();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (27.11.2001 15:45:11)
     * 
     * @return java.lang.Boolean
     */
    Boolean getEstAdresseDetaillee();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (11.12.2001 16:55:27)
     * 
     * @return java.lang.String
     */
    String getIdAdresseCourrier();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (27.11.2001 15:20:04)
     * 
     * @return java.lang.String
     */
    String getIdAdressePaiement();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (27.11.2001 15:22:17)
     * 
     * @return java.lang.String
     */
    String getIdBanque();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (05.12.2001 14:15:14)
     * 
     * @return java.lang.String
     */
    String getIdTiers();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (27.11.2001 15:20:45)
     * 
     * @return java.lang.String
     */
    String getIdTiersTitulaire();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (27.11.2001 15:45:47)
     * 
     * @return java.lang.Boolean
     */
    Boolean getImprimerListeDetaillee();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (11.12.2001 16:56:19)
     * 
     * @return java.lang.String
     */
    String getNomAutreBeneficiaire();

    /**
     * Cette m�thode permet de retourner le nom du tiers qui se trouve dans l'adresse de paiement.
     * 
     * @return String Nom du tiers de l'adresse de paiement
     */
    public String getNomTiersAdrPmt();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (27.11.2001 15:21:48)
     * 
     * @return java.lang.String
     */
    String getNumCompte();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.02.2002 17:07:52)
     * 
     * @return globaz.osiris.interfaceext.tiers.IntTiers
     */
    IntTiers getTiers();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (27.11.2001 15:21:21)
     * 
     * @return java.lang.String
     */
    IntTiers getTiersTitulaire();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (27.11.2001 15:46:32)
     * 
     * @return int
     */
    String getTypeAdresse();

    /**
     * Permet d'identifier si le compte est un compte IBAN ou non
     * 
     * @return boolean true si le compte est IBAN
     */
    public boolean isCompteIBAN();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (27.11.2001 15:19:26)
     * 
     * @param id
     *            java.lang.String
     */
    void retrieve(BITransaction transaction, String idAdressePaiement) throws java.lang.Exception;

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (27.11.2001 15:19:26)
     * 
     * @param id
     *            java.lang.String
     */
    void retrieve(String idAdressePaiement) throws java.lang.Exception;
}
