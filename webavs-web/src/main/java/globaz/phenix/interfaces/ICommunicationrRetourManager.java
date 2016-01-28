/*
 * Cr�� le 8 mai 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.phenix.interfaces;

import globaz.globall.api.BITransaction;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author hna
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public interface ICommunicationrRetourManager {
    public abstract void cursorClose(BStatement statement) throws Exception;

    public abstract BStatement cursorOpen(BTransaction transaction) throws Exception;

    public abstract BEntity cursorReadNext(BStatement statement) throws Exception;

    public abstract int getCount(BITransaction transaction) throws Exception;

    public java.lang.String getTri();

    public void orderByErreur();

    public void orderByIdCommunicationRetour();

    public void orderByJournal();

    public void orderByNumAffilie();

    public void orderByNumAvs();

    public void orderByNumContribuable();

    public void orderByNumIFD();

    public void setExceptEnEnquete(boolean exceptEnEnquete);

    public void setForGenreAffilie(String idAffiliation);

    public void setForIdJournalRetour(String idJournal);

    public void setForIdPlausibilite(String _idPlausibilite);

    public void setForIdRetour(String idRetour);

    public void setForStatus(String status);

    public void setFromNumAffilie(String fromNumAffilie);

    public void setIdJournalGreaterThan(String idJournal);

    public void setIdJournalLessThan(String idJournal);

    public void setInStatus(String inStatus);

    public void setNotInStatus(String notInStatus);

    public void setSession(BSession session);

    public void setTillNumAffilie(String tllNumAffilie);

    public void setTri(String tri);

    public void setWhitAffiliation(boolean whitAffiliation);

    public void setWhitAffiliationConjoint(boolean whitAffiliationConjoint);

    public void setWhitJournal(boolean whitJournal);

    public void setWhitPavsAffilie(boolean whitPavsAffilie);

    public void setWhitPavsConjoint(boolean whitPavsConjoint);

    public void setWhitPersAffilie(boolean whitPersAffilie);

    public void setWhitPersConjoint(boolean whitPersConjoint);
}
