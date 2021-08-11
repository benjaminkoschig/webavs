package globaz.corvus.acor.parser.xml.rev10;

import acor.rentes.ch.admin.zas.rc.annonces.rente.pool.PoolMeldungZurZAS;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

/**
 * Lit le contenu du fichier annonces.xml généré par ACOR. La lecture s'effectue selon les classes générées par JaxB
 * contenues dans le package ch.admin.zas.pool.</br>
 * Le fichier annonces.xml contient uniquement des annonces 10ème
 * révision, la lecture porte donc uniquement sur ce type d'annonces</br>
 * Les annonces de diminution ne sont pas
 * lues</br>
 * Le principe de lecture des champs est le suivant : on ne lance pas d'erreur si une section est vide, le
 * contrôle s'effectuera par la validation avec Anakin sur les annonces de type 44 01 et 02 qui seront crées.</br>
 *
 * @author lga
 */
public class REACORAnnonceXmlReader {


    /**
     * Lit le contenu du fichier annonces.xml généré par ACOR. La lecture s'effectue selon les classes générées par JaxB
     * contenues dans le package ch.admin.zas.pool.</br>
     * Le fichier annonces.xml contient uniquement des annonces 10ème
     * révision, la lecture porte uniquement sur ce type d'annonces</br>
     * Les annonces de diminution ne sont pas
     * lues</br>
     * Le principe de lecture des champs est le suivant : on ne lance pas d'erreur si une section est vide, le
     * contrôle s'effectuera par la validation avec Anakin sur les annonces de type 44 qui seront crées.</br>
     *
     * @param session               La session en cours
     * @param annonceXMLFileContent Le contenu du fichier annonces.xml
     * @return
     * @throws Exception
     */
    public final PoolMeldungZurZAS readAnnonceXmlContent(BSession session, String annonceXMLFileContent) throws Exception {

        if (JadeStringUtil.isEmpty(annonceXMLFileContent)) {
            String message = session.getLabel("ERREUR_CALCUL_ACOR_LECTURE_ANNONCE_XML_VIDE");
            throw new Exception(message);
        }
        JAXBContext jaxbContext;

        // Création du context JaxB sur la base du package contenant les classes générées par JaxB
        jaxbContext = JAXBContext.newInstance("acor.ch.admin.zas.rc.annonces.rente.pool");
        final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Object root = unmarshaller.unmarshal(new StringReader(annonceXMLFileContent));
        return (PoolMeldungZurZAS) root;
    }


}
