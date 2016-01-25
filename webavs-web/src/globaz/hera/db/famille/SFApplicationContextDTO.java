/*
 * Cr�� le 12 sept. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.hera.db.famille;

import java.io.Serializable;

/**
 * <H1>Description</H1>
 * 
 * DOCUMENT ME!
 * 
 * @author mmu
 * 
 *         <p>
 *         Bas� sur le m�me model que globaz.apg.vb.droits.APDroitDTO
 *         </p>
 *         Contient les donn�es concerant le contexte de l'application: - la valeur d'entree - de sortie - et le domaine
 *         d'application du r�querant sur lequel les �crans travaillent
 */
public class SFApplicationContextDTO implements Serializable {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String entree = "";
    private String idDomaineApplication = "";
    private String sortie = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Cr�e une nouvelle instance de la classe SFRequerantDTO.
     */
    public SFApplicationContextDTO() {
    }

    /**
     * Cr�e une nouvelle instance de la classe SFRequerantDTO.
     * 
     * @param requerant
     *            DOCUMENT ME!
     */
    public SFApplicationContextDTO(String idDomaine, String entree, String sortie) {
        idDomaineApplication = idDomaine;
        this.entree = entree;
        this.sortie = sortie;
    }

    /**
     * @return
     */
    public String getEntree() {
        return entree;
    }

    /**
     * @return
     */
    public String getIdDomaineApplication() {
        return idDomaineApplication;
    }

    /**
     * @return
     */
    public String getSortie() {
        return sortie;
    }

    /**
     * @param string
     */
    public void setEntree(String string) {
        entree = string;
    }

    /**
     * @param string
     */
    public void setIdDomaineApplication(String string) {
        idDomaineApplication = string;
    }

    /**
     * @param string
     */
    public void setSortie(String string) {
        sortie = string;
    }

}