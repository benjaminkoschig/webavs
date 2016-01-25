package globaz.pavo.db.splitting;

import globaz.globall.db.BTransaction;
import globaz.globall.util.JAUtil;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CIEcriture;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Container d'�critures de splitting rassemblant tous les �critures g�n�r�es pour un dossier de splitting.
 * 
 * @author user GLOBAZ
 */
public class CIEcrituresSplittingContainer {
    private HashMap cis = new HashMap(2);
    private HashMap container = new HashMap(2);
    private boolean ecritureSuspens = false;
    private String idAnnonce65 = null;

    /**
     * Ajoute un ci (assur� ou conjoint) si pas d�j� existant
     * 
     * @param ci
     *            le CI
     */
    public void addCI(CICompteIndividuel ci) {
        if (ci != null && !ci.isNew()) {
            if (!cis.containsKey(ci.getCompteIndividuelId())) {
                cis.put(ci.getCompteIndividuelId(), ci);
            }
        }
    }

    /**
     * Ajoute une �criture de splitting
     * 
     * @param ecriture
     *            l'�criture de splitting
     */
    public void addEcriture(CIEcriture ecriture) {
        if (ecriture != null && !JAUtil.isIntegerEmpty(ecriture.getCompteIndividuelId())) {
            if (CIEcriture.CS_CI_SUSPENS.equals(ecriture.getIdTypeCompte())) {
                setEcritureSuspens(true);
            }
            if (container.containsKey(ecriture.getCompteIndividuelId())) {
                // �critures d�j� pr�sentes
                ArrayList list = (ArrayList) container.get(ecriture.getCompteIndividuelId());
                list.add(ecriture);
            } else {
                ArrayList list = new ArrayList();
                list.add(ecriture);
                container.put(ecriture.getCompteIndividuelId(), list);
            }
        }
    }

    /**
     * Teste et envoie les �ventuels ci additionnels de l'assur� et son conjoint pour les cas de splitting manuel
     * 
     * @param transaction
     *            la transaction � utiliser
     * @throws Exception
     *             si une erreur survient
     * @deprecated
     */
    @Deprecated
    public void checkandExecCIAdditionnel(BTransaction transaction) throws Exception {
        if (container.size() > 2) {
            throw new Exception("Erreur CI additionnel: plus de deux assur�s concern�s pour un splitting");
        }
        Iterator it = container.keySet().iterator();
        while (it.hasNext()) {
            String ciId = (String) it.next();
            CICompteIndividuel ci;
            if (cis.containsKey(ciId)) {
                ci = (CICompteIndividuel) cis.get(ciId);
            } else {
                ci = new CICompteIndividuel();
                ci.setSession(transaction.getSession());
                ci.setCompteIndividuelId(ciId);
                ci.retrieve(transaction);
            }
            if (!ci.isNew()) {
                ArrayList list = (ArrayList) container.get(ci.getCompteIndividuelId());
                ;
                ci.annonceCIAdditionnel(transaction, list);
            }
        }
    }

    /**
     * Retourne le CI de l'assur�
     * 
     * @param noAvs
     *            le num�ro AVS de l'assur�
     * @param transaction
     *            la transaction � utiliser
     * @throws Exception
     *             si une erreur survient
     */
    public CICompteIndividuel getCI(String noAvs, BTransaction transaction) throws Exception {
        Iterator it = cis.values().iterator();
        while (it.hasNext()) {
            CICompteIndividuel ci = (CICompteIndividuel) it.next();
            if (ci.getNumeroAvs().equals(noAvs)) {
                return ci;
            }
        }
        // pas encore dans la liste
        CICompteIndividuel ci = CICompteIndividuel.loadCI(noAvs, transaction);
        if (ci != null && !ci.isNew()) {
            cis.put(ci.getCompteIndividuelId(), ci);
        }
        return ci;
    }

    /**
     * Renvoie la liste des �critures de splitting pour le CI donn�
     * 
     * @param compteIndividuelId
     *            l'id du CI
     * @return la liste des �critures ou null si inexistant
     */
    public ArrayList getEcrituresSplitting(String compteIndividuelId) {
        return (ArrayList) container.get(compteIndividuelId);
    }

    /**
     * Returns the idAnnonce65.
     * 
     * @return String
     */
    public String getIdAnnonce65() {
        return idAnnonce65;
    }

    public boolean hasSuspended(String compteIndividuelId) {
        Iterator it = getEcrituresSplitting(compteIndividuelId).iterator();
        while (it.hasNext()) {
            if (CIEcriture.CS_CI_SUSPENS.equals(((CIEcriture) it.next()).getIdTypeCompte())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the ciConjointCloture.
     * 
     * @return boolean
     */
    public boolean isEcritureSuspens() {
        return ecritureSuspens;
    }

    /**
     * Sets the ciConjointCloture.
     * 
     * @param ciConjointCloture
     *            The ciConjointCloture to set
     */
    public void setEcritureSuspens(boolean state) {
        ecritureSuspens = state;
    }

    /**
     * Sets the idAnnonce65.
     * 
     * @param idAnnonce65
     *            The idAnnonce65 to set
     */
    public void setIdAnnonce65(String idAnnonce65) {
        this.idAnnonce65 = idAnnonce65;
    }

}
