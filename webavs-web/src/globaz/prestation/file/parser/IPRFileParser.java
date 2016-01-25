package globaz.prestation.file.parser;

import globaz.prestation.file.parser.exception.PRFieldNotFoundException;
import globaz.prestation.file.parser.exception.PRLineNotFoundException;

/**
 * @author scr
 * 
 *         Processus Traitement des paiements Cette classe sert de façade pour le chargement de fichier du traitement
 *         des paiements.<br>
 *         ceci permet de pouvoir avoir des implementation différentes suivant le format du fichier<br>
 *         à charger (fichier plat, fichier xml ,...)<br>
 */
public interface IPRFileParser {

    public void close();

    public String getDumpFileRecord();

    public String getField(String fieldName) throws PRFieldNotFoundException, PRLineNotFoundException;

    public String getSource();

    public void goToNextRecord() throws Exception;

    public boolean hasNext() throws Exception;

    public void setSource(String source);

    public int size() throws Exception;
}
