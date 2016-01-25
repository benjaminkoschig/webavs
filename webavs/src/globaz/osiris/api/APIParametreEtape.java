package globaz.osiris.api;

import globaz.globall.api.BIEntity;
import globaz.osiris.external.IntDocumentContentieux;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (25.09.2002 16:11:51)
 * 
 * @author: Administrator
 */
public interface APIParametreEtape extends BIEntity {
    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.06.2002 15:25:59)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDateDeclenchement(APISection section);

    public java.lang.String getDateReference();

    public java.lang.String getDelai();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.02.2002 15:03:11)
     * 
     * @return globaz.osiris.db.comptes.CAJournal
     */
    public APIEtape getEtape();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (04.06.2002 15:00:00)
     * 
     * @return globaz.osiris.db.contentieux.CAParametreEtape
     */
    public APIParametreEtape getEtapeParametreEtapePrecedente();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.02.2002 15:03:11)
     * 
     * @return globaz.osiris.db.contentieux.CAEvenementContentieux
     */
    public APIEvenementContentieux getEvenementContentieux(APISection section);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (05.07.2002 08:52:58)
     * 
     * @return globaz.osiris.db.contentieux.CAEvenementContentieux
     */
    public APIEvenementContentieux getEvenementContentieuxPrecedent(APISection section);

    public java.lang.String getIdEtape();

    /**
     * Getter
     */
    public java.lang.String getIdParametreEtape();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (05.07.2002 10:14:13)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdSection();

    public java.lang.String getIdSequenceContentieux();

    public java.lang.Boolean getImputerTaxe();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.06.2002 10:22:47)
     * 
     * @return globaz.osiris.interfaceext.contentieux.IntDocumentContentieux
     */
    public IntDocumentContentieux getInstanceDocumentContentieux() throws Exception;

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.06.2002 11:13:25)
     * 
     * @return java.util.Vector
     */
    public java.util.Vector getListeDocuments();

    public java.lang.String getNomClasseImpl();

    public java.lang.String getSequence();

    public java.lang.String getSoldelimitedeclenchement();

    public java.lang.String getUnite();

    public void setDateReference(java.lang.String newDateReference);

    public void setDelai(java.lang.String newDelai);

    public void setIdEtape(java.lang.String newIdEtape);

    /**
     * Setter
     */
    public void setIdParametreEtape(java.lang.String newIdParametreEtape);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (05.07.2002 10:14:13)
     * 
     * @param newIdSection
     *            java.lang.String
     */
    public void setIdSection(java.lang.String newIdSection);

    public void setIdSequenceContentieux(java.lang.String newIdSequenceContentieux);

    public void setImputerTaxe(java.lang.Boolean newImputerTaxe);

    public void setNomClasseImpl(java.lang.String newNomClasseImpl);

    public void setSequence(java.lang.String newSequence);

    public void setSoldelimitedeclenchement(java.lang.String newSoldelimitedeclenchement);

    public void setUnite(java.lang.String newUnite);
}
