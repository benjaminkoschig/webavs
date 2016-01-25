package globaz.osiris.api;

import globaz.framework.util.FWCurrency;
import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (25.09.2002 15:48:04)
 * 
 * @author: Administrator
 */
public interface APIEvenementContentieux extends BIEntity {
    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.06.2002 16:35:12)
     */
    public void annulerDeclenchement();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.09.2002 08:46:09)
     */
    public void annulerEtapeContentieux(BITransaction transaction);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (05.06.2002 16:28:02)
     * 
     * @return java.lang.String
     */
    public java.lang.String getAdresseComplete() throws Exception;

    /**
     * Getter
     */
    public java.lang.String getDateDeclenchement();

    public java.lang.String getDateExecution();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (04.06.2002 07:15:50)
     * 
     * @return java.lang.Boolean
     */
    public java.lang.Boolean getEstDeclenche();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (04.06.2002 07:17:32)
     * 
     * @return java.lang.Boolean
     */
    public java.lang.Boolean getEstExtourne();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (04.06.2002 07:18:41)
     * 
     * @return java.lang.Boolean
     */
    public java.lang.Boolean getEstIgnoree();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (04.06.2002 07:18:03)
     * 
     * @return java.lang.Boolean
     */
    public java.lang.Boolean getEstModifie();

    public java.lang.String getIdAdresse();

    public java.lang.String getIdEvenementContentieux();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.06.2002 15:11:18)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdOperation();

    public java.lang.String getIdParametreEtape();

    public java.lang.String getIdPosteJournalisation();

    public java.lang.String getIdRemarque();

    public java.lang.String getIdSection();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (05.06.2002 16:36:34)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdTiers();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (05.06.2002 16:37:26)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdTiersOfficePoursuites();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.06.2002 17:07:32)
     * 
     * @return java.lang.String
     */
    public java.lang.String getModifie();

    public java.lang.String getMontant();

    /**
     * Retourne le montant au format FWCurrency. Date de cr�ation : (29.10.2002 11:30:10)
     * 
     * @return globaz.framework.util.FWCurrency
     */
    public FWCurrency getMontantToCurrency();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.06.2002 13:06:49)
     * 
     * @return java.lang.String
     */
    public java.lang.String getMotif();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.06.2002 16:41:02)
     * 
     * @return java.lang.String
     */
    public java.lang.String getMotifJournalisation();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.06.2002 15:19:07)
     * 
     * @return globaz.osiris.db.contentieux.CAParametreEtape
     */
    public APIParametreEtape getParametreEtape();

    /**
     * Retourne le montant au format FWCurrency. Date de cr�ation : (29.10.2002 11:30:10)
     * 
     * @return globaz.framework.util.FWCurrency
     */
    public APIRemarque getRemarque();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (02.07.2002 16:17:37)
     * 
     * @return globaz.osiris.db.comptes.CASection
     */
    public APISection getSection();

    public java.lang.String getTaxes();

    /**
     * retourne la taxe au format FWCurrency. Date de cr�ation : (29.10.2002 11:32:19)
     * 
     * @return globaz.framework.util.FWCurrency
     */
    public FWCurrency getTaxesToCurrency();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (02.09.2002 08:16:19)
     * 
     * @return java.lang.String
     */
    public java.lang.String getTexteRemarque();

    /**
     * Setter
     */
    public void setDateDeclenchement(java.lang.String newDateDeclenchement);

    public void setDateExecution(java.lang.String newDateExecution);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (04.06.2002 07:15:50)
     * 
     * @param newEstDeclenche
     *            java.lang.Boolean
     */
    public void setEstDeclenche(java.lang.Boolean newEstDeclenche);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (04.06.2002 07:17:32)
     * 
     * @param newEstExtourne
     *            java.lang.Boolean
     */
    public void setEstExtourne(java.lang.Boolean newEstExtourne);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (04.06.2002 07:18:41)
     * 
     * @param newEstIgnoree
     *            java.lang.Boolean
     */
    public void setEstIgnoree(java.lang.Boolean newEstIgnoree);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (04.06.2002 07:18:03)
     * 
     * @param newEstModifie
     *            java.lang.Boolean
     */
    public void setEstModifie(java.lang.Boolean newEstModifie);

    public void setIdAdresse(java.lang.String newIdAdresse);

    public void setIdEvenementContentieux(java.lang.String newIdEvenementContentieux);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.06.2002 15:11:18)
     * 
     * @param newIdOperation
     *            java.lang.String
     */
    public void setIdOperation(java.lang.String newIdOperation);

    public void setIdParametreEtape(java.lang.String newIdParametreEtape);

    public void setIdPosteJournalisation(java.lang.String newIdPosteJournalisation);

    public void setIdRemarque(java.lang.String newIdRemarque);

    public void setIdSection(java.lang.String newIdSection);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (05.06.2002 16:36:34)
     * 
     * @param newIdTiers
     *            java.lang.String
     */
    public void setIdTiers(java.lang.String newIdTiers);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (05.06.2002 16:37:26)
     * 
     * @param newIdTiersOfficePoursuites
     *            java.lang.String
     */
    public void setIdTiersOfficePoursuites(java.lang.String newIdTiersOfficePoursuites);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.06.2002 17:07:32)
     * 
     * @param newModifie
     *            java.lang.String
     */
    public void setModifie(java.lang.String newModifie);

    public void setMontant(java.lang.String newMontant);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.06.2002 13:06:49)
     * 
     * @param newMotif
     *            java.lang.String
     */
    public void setMotif(java.lang.String newMotif);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.06.2002 16:41:02)
     * 
     * @param newMotifJournalisation
     *            java.lang.String
     */
    public void setMotifJournalisation(java.lang.String newMotifJournalisation);

    public void setTaxes(java.lang.String newTaxes);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (02.09.2002 08:16:19)
     * 
     * @param newTexteRemarque
     *            java.lang.String
     */
    public void setTexteRemarque(java.lang.String newTexteRemarque);
}
