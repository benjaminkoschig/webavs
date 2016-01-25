/*
 * Créé le 8 février 2009
 */
package globaz.cygnus.utils;

import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoinManager;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoinManager;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.util.JAVector;
import java.util.Iterator;
import java.util.Vector;

/**
 * 
 * @author jje
 */
public class RFSoinsListsBuilder {

    private static RFSoinsListsBuilder instance;

    /**
     * Récupère l'instance unique de la class Singleton.
     * <p>
     * Remarque : le constructeur est rendu inaccessible
     * 
     * @return RFSoinsListsBuilder
     */
    public static RFSoinsListsBuilder getInstance(BSession session) {
        if (null == RFSoinsListsBuilder.instance) { // Premier appel
            RFSoinsListsBuilder.instance = new RFSoinsListsBuilder(session);
        }

        return RFSoinsListsBuilder.instance;
    }

    private String sousTypeDeSoinParTypeInnerJavascript = "";

    private Vector<String[]> typeDeSoinsDemande = null;

    /**
     * Constructeur redéfini comme étant privé pour interdire son appel et forcer à passer par la méthode <link
     * 
     */
    private RFSoinsListsBuilder(BSession session) {
        buildSousTypeDeSoinParTypeInnerJavascript(session);
        buildCodeTypeDeSoinsDemande(session);
    }

    /**
     * Méthode qui construit un tableau javascript de type de soins (code,CSlibelle)
     * 
     * @return String
     */
    private void buildCodeTypeDeSoinsDemande(BSession session) {

        try {

            RFTypeDeSoinManager typeDeSoinsManager = new RFTypeDeSoinManager();
            typeDeSoinsManager.setSession(session);
            typeDeSoinsManager.changeManagerSize(0);
            typeDeSoinsManager.find();

            Iterator<RFTypeDeSoin> codeTypeDeSoinsDemandeIter = typeDeSoinsManager.getContainer().iterator();
            typeDeSoinsDemande = new Vector<String[]>();
            // Ajout de l'élément null
            typeDeSoinsDemande.add(new String[] { "", "" });

            while (codeTypeDeSoinsDemandeIter.hasNext()) {
                RFTypeDeSoin currentTypeDeSoin = codeTypeDeSoinsDemandeIter.next();
                typeDeSoinsDemande.add(new String[] { currentTypeDeSoin.getCode(),
                        session.getCodeLibelle(currentTypeDeSoin.getIdTypeSoin()) });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode qui construit un tableau javascript de sous type de soins à 2 dimension (code,CSlibelle)
     * 
     */
    private void buildSousTypeDeSoinParTypeInnerJavascript(BSession session) {

        try {
            // TODO: Attention dangereux dépend de l'ordre !! créer un tableau
            // associatif en javascript
            StringBuffer bufferStr = new StringBuffer();
            StringBuffer codeSousTypeDeSoinStr = new StringBuffer();
            StringBuffer libelleSousTypeDeSoinStr = new StringBuffer();

            RFSousTypeDeSoinManager sousTypeDeSoinsManager = new RFSousTypeDeSoinManager();
            sousTypeDeSoinsManager.setSession(session);
            sousTypeDeSoinsManager.setOrderBy(RFSousTypeDeSoin.FIELDNAME_ID_TYPE_SOIN + ","
                    + RFSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_SOIN);
            sousTypeDeSoinsManager.find(BManager.SIZE_NOLIMIT);

            JAVector sousTypeDeSoinsDemandeVec = sousTypeDeSoinsManager.getContainer();
            String idTypeDeSoin = "";
            RFSousTypeDeSoin currentSousTypeDeSoin = null;
            boolean fin = false;

            bufferStr.append("var sousTypeDeSoinTab=[");

            for (int i = 0; i <= sousTypeDeSoinsDemandeVec.size();) {
                if (null == currentSousTypeDeSoin) {
                    currentSousTypeDeSoin = (RFSousTypeDeSoin) sousTypeDeSoinsDemandeVec.get(i);
                    i++;
                }

                idTypeDeSoin = currentSousTypeDeSoin.getIdTypeSoin();

                bufferStr.append("new Array(");
                codeSousTypeDeSoinStr.append("[");
                libelleSousTypeDeSoinStr.append("[");

                while ((idTypeDeSoin.compareTo(currentSousTypeDeSoin.getIdTypeSoin()) == 0) && !fin) {
                    codeSousTypeDeSoinStr.append("\"" + currentSousTypeDeSoin.getCode() + "\"");
                    libelleSousTypeDeSoinStr.append("\""
                            + session.getCodeLibelle(currentSousTypeDeSoin.getIdSousTypeSoin()) + "\"");

                    if (i < sousTypeDeSoinsDemandeVec.size()) {
                        currentSousTypeDeSoin = (RFSousTypeDeSoin) sousTypeDeSoinsDemandeVec.get(i);
                        i++;
                    } else {
                        fin = true;
                        i++;
                    }

                    if ((idTypeDeSoin.compareTo(currentSousTypeDeSoin.getIdTypeSoin()) == 0) && !fin) {
                        codeSousTypeDeSoinStr.append(",");
                        libelleSousTypeDeSoinStr.append(",");

                    } else {
                        codeSousTypeDeSoinStr.append("]");
                        libelleSousTypeDeSoinStr.append("]");
                    }
                }

                bufferStr.append(codeSousTypeDeSoinStr.toString());
                bufferStr.append("," + libelleSousTypeDeSoinStr.toString());
                if (i <= sousTypeDeSoinsDemandeVec.size()) {
                    bufferStr.append("),");
                } else {
                    bufferStr.append(")");
                }
                codeSousTypeDeSoinStr = new StringBuffer();
                libelleSousTypeDeSoinStr = new StringBuffer();
            }

            bufferStr.append("];");

            sousTypeDeSoinParTypeInnerJavascript = bufferStr.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getSousTypeDeSoinParTypeInnerJavascript() {
        return sousTypeDeSoinParTypeInnerJavascript;
    }

    public Vector<String[]> getTypeDeSoinsDemande() {
        return typeDeSoinsDemande;
    }

}
