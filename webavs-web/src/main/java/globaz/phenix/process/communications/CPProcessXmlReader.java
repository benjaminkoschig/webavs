/*
 * Cr�� le 15 juil. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.phenix.process.communications;

import globaz.globall.db.BProcess;
import globaz.jade.client.xml.JadeXmlReader;
import globaz.phenix.db.communications.CPJournalRetour;
import java.io.FileInputStream;
import org.w3c.dom.Document;

/**
 * <H1>Description</H1>
 * 
 * @author MMU
 *         <p>
 *         Cette classe lit un fichier texte contenant les informations sur des contribuables Et converti ces donnees
 *         dans un format xml generique a toutes les caisses de compensation Ce parser est specifique a la Caisse de
 *         Neuchatel
 *         </p>
 */

/*
 * @author mmu Pour implementer un nouveau ascii reader pour g�n�rer des r�c�ption au format XML, il faut h�riter cette
 * classe et impl�menter la methode getReception ainsi que la classe interne (CPReception). La m�thode EcrireEntete()
 * peut �gallement �tre surcharg�e La classe interne est une repr�sention des donn�es lues, il faut impl�menter la
 * methode lireEntree en s'aidant des m�thodes endLine() et setField(). A partir des donn�es lues il faut pouvoir
 * r��crire partout o� cela est possible les getter qui sont appel�s par ecrireEntree().
 */
public abstract class CPProcessXmlReader extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String majNumContribuable = "";

    public Document getDocument(String fileName) {
        try {
            return JadeXmlReader.parseFile(new FileInputStream(fileName));
        } catch (Exception e) {
            return null;
        }
    }

    public String getMajNumContribuable() {
        return majNumContribuable;
    }

    public abstract String getXmlFile();

    public abstract void setJournalRetour(CPJournalRetour journalRetour);

    public void setMajNumContribuable(String majNumContribuable) {
        this.majNumContribuable = majNumContribuable;
    }

    public abstract void setXmlFile(String xmlFile);
}
