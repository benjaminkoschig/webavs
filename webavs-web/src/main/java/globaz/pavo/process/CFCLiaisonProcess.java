package globaz.pavo.process;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JAUtil;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;

/**
 * Correction des liaisons pour la CFC Date de création : (25.11.2002 11:52:37)
 * 
 * @author: Administrator
 */
public class CFCLiaisonProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean echoToConsole = false;
    protected boolean maj = false;

    protected String path = "";

    /**
     * Commentaire relatif au constructeur CICompteIndividuelProcess.
     */
    public CFCLiaisonProcess() {
        super();
    }

    /**
     * Commentaire relatif au constructeur CICompteIndividuelProcess.
     */
    public CFCLiaisonProcess(globaz.globall.db.BSession session) {
        super(session);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        long counter = 0;
        long fatals = 0;
        long modifs = 0;
        long time = System.currentTimeMillis();
        try {
            if (echoToConsole) {
                System.out.println("opening file " + path);
            }
            java.io.BufferedReader fileIn = new java.io.BufferedReader(new java.io.FileReader(path));
            if (echoToConsole) {
                System.out.println("starting process...");
                if (maj) {
                    System.out.println("MODE UPDATE!");
                }
            }
            String line = fileIn.readLine();
            java.util.StringTokenizer tokens;

            while ((line = fileIn.readLine()) != null) {
                counter++;
                if (counter % 1000 == 0) {
                    if (echoToConsole) {
                        long loop = System.currentTimeMillis();
                        System.out.println(counter + " executed in " + ((float) (loop - time) / 60000) + "min. ");
                    }
                }
                tokens = new java.util.StringTokenizer(line, ",");
                String noAvs1 = removeQuote(tokens.nextToken());
                String actif1 = removeQuote(tokens.nextToken());
                String noAvs2 = removeQuote(tokens.nextToken());
                String actif2 = removeQuote(tokens.nextToken());
                if (echoToConsole) {
                    // System.out.println(counter+")"+noAvs1+" "+actif1+" "+noAvs2+" "+actif2);
                }
                CICompteIndividuel parent = null;
                CICompteIndividuel child = null;
                CICompteIndividuel ci1 = CICompteIndividuel.loadCI(noAvs1, getTransaction());
                CICompteIndividuel ci2 = CICompteIndividuel.loadCI(noAvs2, getTransaction());
                if (ci1 == null || ci2 == null) {
                    if (echoToConsole) {
                        // System.out.println("FATAL: Les deux CI n'existent pas ("+noAvs1+"-"+noAvs2+")");
                    }
                    // fatals++;
                    continue;
                }
                if ("K".equals(actif1)) {
                    parent = ci1;
                } else {
                    child = ci1;
                }
                if ("K".equals(actif2)) {
                    if (parent != null) {
                        if (echoToConsole) {
                            // System.out.println("INFO: Les deux CI sont actif?!? ("+noAvs1+"-"+noAvs2+")");
                        }
                        child = ci2;
                    } else {
                        parent = ci2;
                    }
                } else {
                    child = ci2;
                }
                if (parent == null) {
                    if (echoToConsole) {
                        // System.out.println("INFO: Pas d'actif trouvé ("+noAvs1+"-"+noAvs2+")");
                    }
                    parent = ci1;
                    child = ci2;
                }
                // parent
                if (JAUtil.isIntegerEmpty(parent.getCompteIndividuelIdReference())) {
                    // nouveau root
                    if (echoToConsole) {
                        // System.out.println("INFO: maj root ("+noAvs1+"-"+noAvs2+")");
                    }
                    parent.setCompteIndividuelIdReference(parent.getCompteIndividuelId());
                    parent.wantCallValidate(false);
                    parent.wantCallMethodAfter(false);
                    parent.wantCallMethodBefore(false);
                    if (maj) {
                        parent.update(getTransaction());
                    }
                    modifs++;
                    // test du child
                    if (JAUtil.isIntegerEmpty(child.getCompteIndividuelIdReference())
                            || child.getCompteIndividuelIdReference().equals(child.getCompteIndividuelId())) {
                        // ancien était root ou seul - > maj de la référence
                        if (echoToConsole) {
                            // System.out.println("INFO: maj child 1 ("+noAvs1+"-"+noAvs2+")");
                        }
                        child.setCompteIndividuelIdReference(parent.getCompteIndividuelId());
                        child.wantCallValidate(false);
                        child.wantCallMethodAfter(false);
                        child.wantCallMethodBefore(false);
                        if (maj) {
                            child.update(getTransaction());
                        }
                        modifs++;
                    } else {
                        // child se trouve dans une liste, recherche du root
                        int loopCounter = 0;
                        CICompteIndividuel ciToFind = child;
                        while (!ciToFind.getCompteIndividuelId().equals(ciToFind.getCompteIndividuelIdReference())
                                && loopCounter != 10) {
                            CICompteIndividuel ciL = new CICompteIndividuel();
                            ciL.setSession(getSession());
                            ciL.setCompteIndividuelId(ciToFind.getCompteIndividuelIdReference());
                            ciL.wantCallMethodAfter(false);
                            ciL.wantCallMethodBefore(false);
                            ciL.retrieve(getTransaction());
                            if (!ciL.isNew()) {
                                ciToFind = ciL;
                            } else {
                                if (echoToConsole) {
                                    System.out.println("FATAL: liste corrompue (" + noAvs1 + "-" + noAvs2 + ")");
                                }
                                ciToFind = null;
                                fatals++;
                                break;
                            }

                            loopCounter++;
                        }
                        if (loopCounter == 10) {
                            if (echoToConsole) {
                                System.out.println("FATAL: boucle sans fin (" + noAvs1 + "-" + noAvs2 + ")");
                            }
                            ciToFind = null;
                            fatals++;
                            break;
                        }
                        if (ciToFind == null) {
                            // erreur
                            continue;
                        } else {
                            // maj
                            if (echoToConsole) {
                                // System.out.println("INFO: maj highest child 1 ("+noAvs1+"-"+noAvs2+")");
                            }
                            ciToFind.setCompteIndividuelIdReference(parent.getCompteIndividuelId());
                            ciToFind.wantCallValidate(false);
                            ciToFind.wantCallMethodAfter(false);
                            ciToFind.wantCallMethodBefore(false);
                            if (maj) {
                                ciToFind.update(getTransaction());
                            }
                            modifs++;
                        }
                    }
                } else {
                    CICompteIndividuel ciToFind = parent;
                    int loopCounter;
                    if (!parent.getCompteIndividuelId().equals(parent.getCompteIndividuelIdReference())) {
                        // recherche si le child ne se trouve pas en-dessus
                        // ne devrait jamais arriver
                        loopCounter = 0;
                        while (!ciToFind.getCompteIndividuelId().equals(ciToFind.getCompteIndividuelIdReference())
                                && loopCounter != 10) {
                            CICompteIndividuel ciL = new CICompteIndividuel();
                            ciL.setSession(getSession());
                            ciL.setCompteIndividuelId(ciToFind.getCompteIndividuelIdReference());
                            ciL.wantCallMethodAfter(false);
                            ciL.wantCallMethodBefore(false);
                            ciL.retrieve(getTransaction());
                            if (!ciL.isNew()) {
                                ciToFind = ciL;
                            } else {
                                if (echoToConsole) {
                                    System.out.println("FATAL: liste corrompue (" + noAvs1 + "-" + noAvs2 + ")");
                                }
                                ciToFind = null;
                                fatals++;
                                break;
                            }
                            if (ciToFind.getCompteIndividuelId().equals(child.getCompteIndividuelId())) {
                                // déjà lié en dessus ?!?
                                if (echoToConsole) {
                                    // System.out.println("INFO: l'ancien ci se trouve déjà en dessus?!? ("+noAvs1+"-"+noAvs2+")");
                                }
                                ciToFind = null;
                                break;
                            }
                            loopCounter++;
                        }
                        if (loopCounter == 10) {
                            if (echoToConsole) {
                                System.out.println("FATAL: boucle sans fin (" + noAvs1 + "-" + noAvs2 + ")");
                            }
                            ciToFind = null;
                            fatals++;
                            break;
                        }
                    }
                    if (ciToFind == null) {
                        // les deux ci sont déjà liés
                        continue;
                    }
                    // recherche du child le plus bas / test si déjà dans la
                    // liste
                    ciToFind = parent;
                    loopCounter = 0;
                    CICompteIndividuelManager ciMgr;
                    do {
                        if (ciToFind.getCompteIndividuelId().equals(child.getCompteIndividuelId())) {
                            // déjà lié
                            if (echoToConsole) {
                                // System.out.println("INFO: l'ancien ci se trouve déjà en dessous ("+noAvs1+"-"+noAvs2+")");
                            }
                            ciToFind = null;
                            break;
                        }
                        ciMgr = new CICompteIndividuelManager();
                        ciMgr.setSession(getSession());
                        ciMgr.orderByAvs(false);
                        ciMgr.setForCompteIndividuelReferenceId(ciToFind.getCompteIndividuelId());
                        ciMgr.setForNotCompteIndividuelId(parent.getCompteIndividuelId());
                        ciMgr.changeManagerSize(1);
                        ciMgr.wantCallMethodAfter(false);
                        ciMgr.wantCallMethodBefore(false);
                        ciMgr.find(getTransaction());
                        if (!ciMgr.isEmpty()) {
                            ciToFind = (CICompteIndividuel) ciMgr.getEntity(0);
                        }
                        loopCounter++;
                    } while (!ciMgr.isEmpty() && loopCounter != 10);
                    if (loopCounter == 10) {
                        if (echoToConsole) {
                            System.out.println("FATAL: boucle sans fin (" + noAvs1 + "-" + noAvs2 + ")");
                        }
                        ciToFind = null;
                        fatals++;
                        break;
                    }
                    if (ciToFind == null) {
                        // les deux ci sont déjà liés
                        continue;
                    } else {
                        // ci le plus bas trouvé, child non trouvé
                        if (!JAUtil.isIntegerEmpty(child.getCompteIndividuelIdReference())) {
                            // le child se trouve dans une autre liste chainée
                            if (echoToConsole) {
                                System.out.println("INFO: l'ancien ci est lié mais pas sous cette personne (" + noAvs1
                                        + "-" + noAvs2 + ")");
                            }
                            // recherche du plus haut child
                            loopCounter = 0;
                            CICompteIndividuel upperChild = child;
                            while (!upperChild.getCompteIndividuelId().equals(
                                    upperChild.getCompteIndividuelIdReference())
                                    && loopCounter != 10) {
                                CICompteIndividuel ciL = new CICompteIndividuel();
                                ciL.setSession(getSession());
                                ciL.setCompteIndividuelId(upperChild.getCompteIndividuelIdReference());
                                ciL.wantCallMethodAfter(false);
                                ciL.wantCallMethodBefore(false);
                                ciL.retrieve(getTransaction());
                                if (!ciL.isNew()) {
                                    upperChild = ciL;
                                } else {
                                    if (echoToConsole) {
                                        System.out.println("FATAL: liste corrompue (" + noAvs1 + "-" + noAvs2 + ")");
                                    }
                                    upperChild = null;
                                    fatals++;
                                    break;
                                }
                                loopCounter++;
                            }
                            if (loopCounter == 10) {
                                if (echoToConsole) {
                                    System.out.println("FATAL: boucle sans fin (" + noAvs1 + "-" + noAvs2 + ")");
                                }
                                upperChild = null;
                                fatals++;
                                break;
                            }
                            if (upperChild == null) {
                                // erreur
                                continue;
                            } else {
                                // maj
                                if (echoToConsole) {
                                    // System.out.println("INFO: maj highest child  2("+noAvs1+"-"+noAvs2+")");
                                }
                                upperChild.setCompteIndividuelIdReference(ciToFind.getCompteIndividuelId());
                                upperChild.wantCallValidate(false);
                                upperChild.wantCallMethodAfter(false);
                                upperChild.wantCallMethodBefore(false);
                                if (maj) {
                                    upperChild.update(getTransaction());
                                }
                                modifs++;
                            }

                        } else {
                            if (echoToConsole) {
                                // System.out.println("INFO: maj child 2 ("+noAvs1+"-"+noAvs2+")");
                            }
                            child.setCompteIndividuelIdReference(ciToFind.getCompteIndividuelId());
                            child.wantCallValidate(false);
                            child.wantCallMethodAfter(false);
                            child.wantCallMethodBefore(false);
                            if (maj) {
                                child.update(getTransaction());
                            }
                            modifs++;
                        }
                    }
                }
                if (maj) {
                    getTransaction().commit();
                }

            }

        } catch (Exception e) {
            if (echoToConsole) {
                e.printStackTrace();
            }
            getMemoryLog().logMessage(e.toString(), FWMessage.FATAL, getClass().getName());
        }
        if (echoToConsole) {
            System.out.println("Process done.");
            long end = System.currentTimeMillis();
            System.out.print(counter + " executed in " + ((float) (end - time) / 3600000) + "h. ");
            System.out.println(" with " + fatals + " fatals, " + modifs + " modifications");
        }
        return !isAborted();
    }

    @Override
    protected java.lang.String getEMailObject() {
        if (isOnError()) {
            return "Le traitement des liaisons a echoué!";
        } else {
            return "Le traitement des liaisons s'est effectué avec succès.";
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return null;
    }

    private String removeQuote(String text) {
        if (text.length() > 2) {
            return text.substring(1, text.length() - 1);
        }
        return "";
    }

    /**
     * Ajoute ou non des infomations de traitement dans la console. Date de création : (25.11.2002 10:27:48)
     * 
     * @param newEchoToConsole
     *            mettre à true si ces informations doivent apparaître dans la console.
     */
    public void setEchoToConsole(boolean newEchoToConsole) {
        echoToConsole = newEchoToConsole;
    }

}
