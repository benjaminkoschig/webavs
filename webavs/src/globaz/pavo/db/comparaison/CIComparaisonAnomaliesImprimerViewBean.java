/*
 * Cr�� le 30 sept. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.pavo.db.comparaison;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.pavo.vb.CIAbstractPersistentViewBean;

/**
 * @author jmc
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class CIComparaisonAnomaliesImprimerViewBean extends CIAbstractPersistentViewBean implements FWViewBeanInterface {
    String forIdTypeAnomalie = "";
    String likeNumeroAvs = "";

    /**
	 * 
	 */
    public CIComparaisonAnomaliesImprimerViewBean() throws Exception {
        super();
        // TODO Raccord de constructeur auto-g�n�r�
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    public String getForIdTypeAnomalie() {
        return forIdTypeAnomalie;
    }

    public String getLikeNumeroAvs() {
        return likeNumeroAvs;
    }

    @Override
    public void retrieve() throws Exception {
        // TODO Auto-generated method stub

    }

    public void setForIdTypeAnomalie(String forIdTypeAnomalie) {
        this.forIdTypeAnomalie = forIdTypeAnomalie;
    }

    public void setLikeNumeroAvs(String likeNumeroAvs) {
        this.likeNumeroAvs = likeNumeroAvs;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
