/*
 * Cr�� le 26 oct. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.hera.helpers.famille;

import globaz.framework.controller.FWHelper;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JAUtil;

/**
 * <H1>Description</H1>
 * 
 * DOCUMENT ME!
 * 
 * @author mmu
 * 
 *         <p>
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 *         </p>
 */
public class SFPeriodeHelper extends FWHelper {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * revoie la date de d�but de p�riode en fonction de la date de d�but de relation et de la date de naissance Utilis�
     * pour les p�riodes BTE
     * 
     * @param bSession
     *            de l'importe quelle application
     * @param debutRelation
     *            date au format "jj.mm.aaaa"
     * @param dateNaissance
     *            date au format "jj.mm.aaaa"
     * 
     * @return la valeur courante de l'attribut debut periode
     * 
     * @throws Exception
     */
    public static String getDebutPeriode(BSession bSession, String debutRelation, String dateNaissance)
            throws Exception {
        // max(dateNaissance, debutRelation)
        return BSessionUtil.compareDateFirstGreater(bSession, dateNaissance, debutRelation) ? dateNaissance
                : debutRelation;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * revoie la date de fin de p�riode en fonction de la date de fin de relation de divorce et de la date de d�c�s
     * Utilis� pour les p�riodes BTE
     * 
     * @param bSession
     *            de l'importe quelle application
     * @param finRelation
     *            date au format "jj.mm.aaaa"
     * @param dateDeces
     *            date au format "jj.mm.aaaa"
     * 
     * @return la valeur courante de l'attribut fin periode
     * 
     * @throws Exception
     */
    public static String getFinPeriode(BSession bSession, String finRelation, String dateDeces) throws Exception {
        String finPeriode = "";
        // min(dateDeces, finRelation)
        if (JAUtil.isDateEmpty(finRelation)) {
            finPeriode = dateDeces;
        } else if (JAUtil.isDateEmpty(dateDeces)) {
            finPeriode = finRelation;
        } else {
            finPeriode = BSessionUtil.compareDateFirstLower(bSession, dateDeces, finRelation) ? dateDeces : finRelation;
        }

        return finPeriode;
    }

    /**
     * Cr�e une nouvelle instance de la classe SFPeriodeHelper.
     */
    public SFPeriodeHelper() {
        super();
    }
}
