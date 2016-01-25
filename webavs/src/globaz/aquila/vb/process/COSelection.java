/*
 * Cr�� le 2 f�vr. 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.aquila.vb.process;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.batch.COEtape;
import globaz.aquila.db.access.batch.COEtapeManager;
import globaz.globall.db.BSession;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <H1>Description</H1>
 * <p>
 * Renferme toutes les informations saisies par un utilisateur concernant les �tapes � consid�rer durant le processus de
 * contentieux.
 * </p>
 * 
 * @author vre
 */
public class COSelection implements Serializable {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final long serialVersionUID = -5016543012161756243L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private List /* String (idEtape) */detailEtapes;
    private String idSequence;
    private Boolean isCreerAmorcesContentieux;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Cr�e une nouvelle instance de la classe COSelection.
     * 
     * @param idSequence
     *            DOCUMENT ME!
     * @param detailEtapes
     *            DOCUMENT ME!
     */
    public COSelection(String idSequence, String[] detailEtapes) {
        this.idSequence = idSequence;
        this.detailEtapes = ((detailEtapes != null) && (detailEtapes.length > 0)) ? Arrays.asList(detailEtapes)
                : Collections.EMPTY_LIST;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param idEtape
     *            DOCUMENT ME!
     */
    public void addDetailEtapes(String idEtape) {
        detailEtapes.add(idEtape);
    }

    /**
     * getter pour l'attribut detail etapes.
     * 
     * @return la valeur courante de l'attribut detail etapes
     */
    public List /* String (idEtape) */getDetailEtapes() {
        return detailEtapes;
    }

    /**
     * getter pour l'attribut id sequence.
     * 
     * @return la valeur courante de l'attribut id sequence
     */
    public String getIdSequence() {
        return idSequence;
    }

    /**
     * retourne vrai s'il faut cr�er les contentieux pour les sections qui ont pass�s l'�ch�ance.
     * <p>
     * Il faut cr�er les contentieux si l'�tape {@link ICOEtape#CS_CONTENTIEUX_CREE} a �t� s�lectionn�e ou si toutes les
     * �tapes ont �t� s�lectionn�es.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * @return la valeur courante de l'attribut creer amorces contentieux
     * @throws Exception
     *             DOCUMENT ME!
     */
    public boolean isCreerAmorcesContentieux(BSession session) throws Exception {
        if (isCreerAmorcesContentieux == null) {
            COEtapeManager etapeManager = new COEtapeManager();

            etapeManager.setForIdSequence(idSequence);
            etapeManager.setForLibEtape(ICOEtape.CS_CONTENTIEUX_CREE);
            etapeManager.setSession(session);
            etapeManager.find();

            if (!etapeManager.isEmpty()) {
                COEtape etape = (COEtape) etapeManager.get(0);

                isCreerAmorcesContentieux = detailEtapes.contains(etape.getIdEtape()) ? Boolean.TRUE : Boolean.FALSE;
            } else {
                // il n'y a pas d'�tape de cr�ation du contentieux pour cette
                // s�quence
                isCreerAmorcesContentieux = Boolean.FALSE;
            }
        }

        return isCreerAmorcesContentieux.booleanValue();
    }

    /**
     * setter pour l'attribut id sequence.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdSequence(String string) {
        idSequence = string;
    }
}
