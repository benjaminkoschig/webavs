package globaz.phenix.api;

import globaz.framework.printing.itext.api.FWIBeanInterface;
import globaz.globall.db.BSession;
import globaz.phenix.interfaces.ICDecisionTiers;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public interface ICPDecisionBean extends FWIBeanInterface {
    /**
     * Ajoute les éléments de l'entity
     * 
     * @param entity
     */
    // public void add(CPDecision entity);
    public void add(ICDecisionTiers entity);

    /**
     * Returns the anneeDecision.
     * 
     * @return String
     */
    public String getAnneeDecision();

    /**
     * Returns the cottisation.
     * 
     * @return String
     */
    public String getCottisation();

    /**
     * Returns the etat.
     * 
     * @return String
     */
    public String getEtat();

    /**
     * Returns the fortune.
     * 
     * @return String
     */
    public String getFortune();

    /**
     * Returns the genreDecision.
     * 
     * @return String
     */
    public String getGenreDecision();

    /**
     * Returns the nomTier.
     * 
     * @return String
     */
    public String getNomTier();

    /**
     * Returns the numeroAffilie.
     * 
     * @return String
     */
    public String getNumeroAffilie();

    /**
     * Returns the revenu.
     * 
     * @return String
     */
    public String getRevenu();

    /**
     * Returns the session.
     * 
     * @return BSession
     */
    public BSession getSession();

    /**
     * Returns the typeDecision.
     * 
     * @return String
     */
    public String getTypeDecision();

    /**
     * Sets the anneeDecision.
     * 
     * @param anneeDecision
     *            The anneeDecision to set
     */
    public void setAnneeDecision(String anneeDecision);

    /**
     * Sets the cottisation.
     * 
     * @param cottisation
     *            The cottisation to set
     */
    public void setCottisation(String cottisation);

    /**
     * Sets the etat.
     * 
     * @param etat
     *            The etat to set
     */
    public void setEtat(String etat);

    /**
     * Sets the fortune.
     * 
     * @param fortune
     *            The fortune to set
     */
    public void setFortune(String fortune);

    /**
     * Sets the genreDecision.
     * 
     * @param genreDecision
     *            The genreDecision to set
     */
    public void setGenreDecision(String genreDecision);

    /**
     * Sets the nomTier.
     * 
     * @param nomTier
     *            The nomTier to set
     */
    public void setNomTier(String nomTier);

    /**
     * Sets the numeroAffilie.
     * 
     * @param numeroAffilie
     *            The numeroAffilie to set
     */
    public void setNumeroAffilie(String numeroAffilie);

    /**
     * Sets the revenu.
     * 
     * @param revenu
     *            The revenu to set
     */
    public void setRevenu(String revenu);

    /**
     * Sets the session.
     * 
     * @param session
     *            The session to set
     */
    public void setSession(BSession session);

    /**
     * Sets the typeDecision.
     * 
     * @param typeDecision
     *            The typeDecision to set
     */
    public void setTypeDecision(String typeDecision);
}
