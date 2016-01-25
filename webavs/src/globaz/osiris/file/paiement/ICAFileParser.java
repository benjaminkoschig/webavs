package globaz.osiris.file.paiement;

import globaz.osiris.file.paiement.exception.FieldNotFoundException;
import globaz.osiris.file.paiement.exception.LineNotFoundException;

/**
 * @author scr Processus Traitement des paiements Cette classe sert de fa�ade pour le chargement de fichier du
 *         traitement des paiements.<br>
 *         ceci permet de pouvoir avoir des implementation diff�rentes suivant le format du fichier<br>
 *         � charger (fichier plat, fichier xml ,...)<br>
 */
public interface ICAFileParser {

    public void close();

    public String getCodeIsoMonnaie();

    public String getDumpFileRecord();

    public String getField(String fieldName) throws FieldNotFoundException, LineNotFoundException;

    public String getSource();

    public void goToNextRecord() throws Exception;

    public boolean hasNext() throws Exception;

    public void setCodeIsoMonnaie(String s);

    public void setSource(String source);

    public int size() throws Exception;
}
