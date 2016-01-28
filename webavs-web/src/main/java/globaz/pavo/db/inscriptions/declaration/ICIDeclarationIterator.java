package globaz.pavo.db.inscriptions.declaration;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import java.util.TreeMap;

/**
 * @author oca
 * 
 *         Processus Datentr�ger Cette classe sert de fa�ade pour le chargement de fichier de d�claration de salaire.<br>
 *         ceci permet de pouvoir avoir des implementation diff�rentes suivant le format du fichier<br>
 *         � charger (fichier plat, fichier xml ,...)<br>
 */
public interface ICIDeclarationIterator {

    public void close();

    public String getDateReception();

    public String getFilename();

    public TreeMap<String, Object> getNbSalaires();

    public TreeMap<?, ?> getNoAffiliePourReception() throws Exception;

    public BSession getSession();

    public TreeMap<String, Object> getTotauxJournaux();

    public BTransaction getTransaction();

    public boolean hasNext() throws Exception;

    public CIDeclarationRecord next() throws Exception;

    public void setFilename(String filename);

    public void setProvenance(String provenance);

    public void setSession(BSession session);

    public void setTransaction(BTransaction transaction);

    public void setTypeImport(String type);

    public int size() throws Exception;
}
