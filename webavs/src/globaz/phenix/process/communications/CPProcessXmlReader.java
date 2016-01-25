/*
 * Créé le 15 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
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
 * @author mmu Pour implementer un nouveau ascii reader pour générer des récéption au format XML, il faut hériter cette
 * classe et implémenter la methode getReception ainsi que la classe interne (CPReception). La méthode EcrireEntete()
 * peut égallement être surchargée La classe interne est une représention des données lues, il faut implémenter la
 * methode lireEntree en s'aidant des méthodes endLine() et setField(). A partir des données lues il faut pouvoir
 * réécrire partout où cela est possible les getter qui sont appelés par ecrireEntree().
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
