/*
 * Créé le 8 février 2009
 */
package globaz.cygnus.utils;

import globaz.cygnus.db.motifsDeRefus.RFMotifsDeRefus;
import globaz.cygnus.db.motifsDeRefus.RFMotifsDeRefusManager;
import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoinManager;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoinManager;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

/**
 * Construit un tableau "motifsRefusParSoinMap" associant les Motifs de refus aux sous types de soins motifsRefusStrTab
 * Key sous type de soin 01| motifsRefusVec -> Key type de soin 01| SousTypeDeSoinMotifsRefusMap-> ...
 * motifsRefusParSoinMap-> Key sous type de soin n | ... Key type de soin n | ...
 * 
 * @author jje
 */
public class RFMotifsRefusListBuillder {

    private static RFMotifsRefusListBuillder instance;

    /**
     * Récupère l'instance unique de la class Singleton.
     * <p>
     * Remarque : le constructeur est rendu inaccessible
     */
    public static RFMotifsRefusListBuillder getInstance(BSession session) {
        if (null == RFMotifsRefusListBuillder.instance) { // Premier appel
            RFMotifsRefusListBuillder.instance = new RFMotifsRefusListBuillder(session);
        }

        return RFMotifsRefusListBuillder.instance;
    }

    private Map<String, HashMap<String, Vector<String[]>>> motifsRefusParSoinMap = null;

    /**
     * Constructeur redéfini comme étant privé pour interdire son appel
     */
    private RFMotifsRefusListBuillder(BSession session) {
        buildMotifsRefusParSoin(session);

    }

    /**
     * Méthode qui construit un tableau comportant tous les motifs de refus par sous type de soin
     * 
     * @return String
     */
    private void buildMotifsRefusParSoin(BSession session) {

        try {
            RFTypeDeSoinManager rfTypeDeSoinMgr = new RFTypeDeSoinManager();
            rfTypeDeSoinMgr.setSession(session);
            rfTypeDeSoinMgr.find(BManager.SIZE_NOLIMIT);

            Vector<RFTypeDeSoin> rfTypeDeSoinVec = rfTypeDeSoinMgr.getContainer();

            RFSousTypeDeSoinManager rfSousTypeDeSoinMgr = new RFSousTypeDeSoinManager();
            rfSousTypeDeSoinMgr.setSession(session);
            rfSousTypeDeSoinMgr.find(BManager.SIZE_NOLIMIT);

            Vector<RFSousTypeDeSoin> rfSousTypeDeSoinVec = rfSousTypeDeSoinMgr.getContainer();

            motifsRefusParSoinMap = new HashMap<String, HashMap<String, Vector<String[]>>>();
            Map<String, Vector<String[]>> sousTypeDeSoinMotifsRefusMap;
            Vector<String[]> motifsRefusVec;

            RFMotifsDeRefusManager rfMotifsDeRefusMgr = new RFMotifsDeRefusManager();
            rfMotifsDeRefusMgr.setSession(session);
            rfMotifsDeRefusMgr.find(BManager.SIZE_NOLIMIT);

            Iterator<RFMotifsDeRefus> rfMotifsDeRefusIter = rfMotifsDeRefusMgr.iterator();

            while (rfMotifsDeRefusIter.hasNext()) {
                RFMotifsDeRefus currentRfMotifsDeRefus = rfMotifsDeRefusIter.next();

                String[] currentIdsStr = currentRfMotifsDeRefus.getIdsSoin().split(";");
                String currentIdMotifDeRefus = currentRfMotifsDeRefus.getIdMotifRefus();
                String currentLibelleMotifRefus = "";

                if (session.getIdLangue().equals("F")) {
                    currentLibelleMotifRefus = currentRfMotifsDeRefus.getDescriptionFR();
                } else if (session.getIdLangue().equals("D")) {
                    currentLibelleMotifRefus = currentRfMotifsDeRefus.getDescriptionDE();
                } else if (session.getIdLangue().equals("I")) {
                    currentLibelleMotifRefus = currentRfMotifsDeRefus.getDescriptionIT();
                }

                Boolean currentHasMontant = currentRfMotifsDeRefus.getHasMontant();
                Boolean currentIsMotifSysteme = currentRfMotifsDeRefus.getIsMotifRefusSysteme();

                for (int i = 0; i < currentIdsStr.length; i++) {

                    // Tous les types et sous-types (0)
                    if ("0".equals(currentIdsStr[i])) {

                        for (int j = 0; j < rfTypeDeSoinVec.size(); j++) {

                            RFTypeDeSoin currentTypeDeSoin = rfTypeDeSoinVec.get(j);

                            if (!motifsRefusParSoinMap.containsKey(currentTypeDeSoin.getCode())) {
                                motifsRefusParSoinMap.put(currentTypeDeSoin.getCode(),
                                        new HashMap<String, Vector<String[]>>());
                            }

                            for (int k = 0; k < rfSousTypeDeSoinVec.size(); k++) {

                                RFSousTypeDeSoin currentSousTypeDeSoin = rfSousTypeDeSoinVec.get(k);

                                if (currentSousTypeDeSoin.getIdTypeSoin().equals(currentTypeDeSoin.getId())) {

                                    sousTypeDeSoinMotifsRefusMap = motifsRefusParSoinMap.get(currentTypeDeSoin
                                            .getCode());

                                    if (!sousTypeDeSoinMotifsRefusMap.containsKey(currentSousTypeDeSoin.getCode())) {
                                        sousTypeDeSoinMotifsRefusMap.put(currentSousTypeDeSoin.getCode(),
                                                new Vector<String[]>());
                                    }

                                    motifsRefusVec = sousTypeDeSoinMotifsRefusMap.get(currentSousTypeDeSoin.getCode());
                                    String[] motifsRefusStrTab = { currentIdMotifDeRefus, currentLibelleMotifRefus,
                                            currentHasMontant.toString(), currentIsMotifSysteme.toString() };
                                    motifsRefusVec.add(motifsRefusStrTab);

                                }
                            }
                        }

                    } else {

                        String[] currentTypeSousTypeStr = currentIdsStr[i].split("-");

                        for (int j = 0; j < rfTypeDeSoinVec.size(); j++) {

                            RFTypeDeSoin currentTypeDeSoin = rfTypeDeSoinVec.get(j);

                            if (currentTypeDeSoin.getCode().equals(currentTypeSousTypeStr[0])) {

                                if (!motifsRefusParSoinMap.containsKey(currentTypeDeSoin.getCode())) {
                                    motifsRefusParSoinMap.put(currentTypeDeSoin.getCode(),
                                            new HashMap<String, Vector<String[]>>());
                                }

                                for (int k = 0; k < rfSousTypeDeSoinVec.size(); k++) {

                                    RFSousTypeDeSoin currentSousTypeDeSoin = rfSousTypeDeSoinVec.get(k);

                                    if (currentTypeSousTypeStr[1].equals("0")) {
                                        if (currentSousTypeDeSoin.getIdTypeSoin().equals(currentTypeDeSoin.getId())) {

                                            sousTypeDeSoinMotifsRefusMap = motifsRefusParSoinMap.get(currentTypeDeSoin
                                                    .getCode());

                                            if (!sousTypeDeSoinMotifsRefusMap.containsKey(currentSousTypeDeSoin
                                                    .getCode())) {
                                                sousTypeDeSoinMotifsRefusMap.put(currentSousTypeDeSoin.getCode(),
                                                        new Vector<String[]>());
                                            }

                                            motifsRefusVec = sousTypeDeSoinMotifsRefusMap.get(currentSousTypeDeSoin
                                                    .getCode());
                                            String[] motifsRefusStrTab = { currentIdMotifDeRefus,
                                                    currentLibelleMotifRefus, currentHasMontant.toString(),
                                                    currentIsMotifSysteme.toString() };
                                            motifsRefusVec.add(motifsRefusStrTab);

                                        }
                                    } else {

                                        if (currentSousTypeDeSoin.getIdTypeSoin().equals(currentTypeDeSoin.getId())
                                                && currentSousTypeDeSoin.getCode().equals(currentTypeSousTypeStr[1])) {

                                            sousTypeDeSoinMotifsRefusMap = motifsRefusParSoinMap.get(currentTypeDeSoin
                                                    .getCode());

                                            if (!sousTypeDeSoinMotifsRefusMap.containsKey(currentSousTypeDeSoin
                                                    .getCode())) {
                                                sousTypeDeSoinMotifsRefusMap.put(currentSousTypeDeSoin.getCode(),
                                                        new Vector<String[]>());
                                            }

                                            motifsRefusVec = sousTypeDeSoinMotifsRefusMap.get(currentSousTypeDeSoin
                                                    .getCode());
                                            String[] motifsRefusStrTab = { currentIdMotifDeRefus,
                                                    currentLibelleMotifRefus, currentHasMontant.toString(),
                                                    currentIsMotifSysteme.toString() };
                                            motifsRefusVec.add(motifsRefusStrTab);

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, HashMap<String, Vector<String[]>>> getMotifsRefusParSoinMap() {
        return motifsRefusParSoinMap;
    }

}
