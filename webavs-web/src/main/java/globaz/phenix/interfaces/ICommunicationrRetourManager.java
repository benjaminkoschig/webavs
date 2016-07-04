package globaz.phenix.interfaces;

import globaz.globall.api.BITransaction;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

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
