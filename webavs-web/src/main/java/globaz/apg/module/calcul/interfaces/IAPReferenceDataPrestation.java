/*
 * Cr�� le 25 avr. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.apg.module.calcul.interfaces;

import globaz.framework.util.FWCurrency;
import globaz.globall.util.JADate;

/**
 * @author scr
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public interface IAPReferenceDataPrestation {

    public String getCalculateurClassName();

    public JADate getDateDebut();

    public JADate getDateFin();

    public FWCurrency getMontantJournalierMax();

    public FWCurrency getMontantMaxFraisGarde();

    public String getNoRevision();

    public void setCalculateurClassName(String s);

    public void setDateDebut(JADate date);

    public void setDateFin(JADate date);

    public void setNoRevision(String s);
}
