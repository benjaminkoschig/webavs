package globaz.musca.external;

import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAModuleFacturationManager;
import globaz.musca.db.facturation.FAModulePassage;
import globaz.musca.db.facturation.FAModulePassageManager;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.db.facturation.FAPassageModule;
import globaz.musca.db.facturation.FAPassageModuleManager;

public class ServicesFacturation {
    public final static int STATUSSWITCH_0 = 0;
    public final static int STATUSSWITCH_1 = 1;

    private final static IFAPassage _getPassageFacturationByStatus(BSession session, BTransaction transaction,
            String idTypeModuleFacturation, String shouldStatus, int switchComplement) throws Exception {

        FAPassageModuleManager modPassManager = new FAPassageModuleManager();
        modPassManager.setSession(session);
        modPassManager.changeManagerSize(BManager.SIZE_NOLIMIT);
        modPassManager.setInTypeModule(idTypeModuleFacturation);
        switch (switchComplement) {
            case STATUSSWITCH_0:
                modPassManager.setForStatus(shouldStatus);
                modPassManager.setForPassageBloque(Boolean.FALSE);
                modPassManager.orderDesire("DATEFACTURATION, IDPASSAGE");
                break;
            case STATUSSWITCH_1:
                modPassManager.setForExceptStatus(shouldStatus);
                modPassManager.setForPassageBloque(Boolean.FALSE); // PO 8390
                modPassManager.orderDesire("DATEFACTURATION DESC, IDPASSAGE DESC");
                break;
        }
        BStatement statement = null;
        FAPassage passageToReturn = null;
        try {
            if (modPassManager.getCount() > 0) {
                // On prend le premier
                statement = modPassManager.cursorOpen(transaction);
                FAPassageModule modPassage = null;
                modPassage = (FAPassageModule) modPassManager.cursorReadNext(statement);
                passageToReturn = new FAPassage();
                passageToReturn.setSession(session);
                passageToReturn.setIdPassage(modPassage.getIdPassage());
                passageToReturn.retrieve();
            }
        } catch (Exception ee) {
            System.out.println(ee.toString()
                    + "::ServicesFacturation::Erreur dans la recherche du module du passage de facturation");
        } finally {
            if (passageToReturn == null) {
                // fermer le cursor
                try {
                    modPassManager.cursorClose(statement);
                    statement = null;
                } catch (Exception e) {
                    e.printStackTrace();
                    if (statement != null) {
                        statement.closeStatement();
                    }
                }
                // remonter l'exception à la méthode appellante
                throw new Exception();
            }
            if (statement != null) {
                // fermer le cursor
                try {
                    modPassManager.cursorClose(statement);
                    statement = null;
                } catch (Exception e) {
                    e.printStackTrace();
                    if (statement != null) {
                        statement.closeStatement();
                    }
                }
            }
            return passageToReturn;
        }
    }

    /**
     * Méthode sans utilier une BTransaction Date de création : (28.04.2003 11:27:21)
     * 
     * @return globaz.musca.api.IFAPassage
     */
    private final static IFAPassage _getPassageFacturationByStatus(BSession session, String idTypeModuleFacturation,
            String shouldStatus, int switchComplement) throws Exception {

        FAPassageModuleManager modPassManager = new FAPassageModuleManager();
        modPassManager.setSession(session);
        modPassManager.changeManagerSize(BManager.SIZE_NOLIMIT);
        modPassManager.setInTypeModule(idTypeModuleFacturation);
        switch (switchComplement) {
            case STATUSSWITCH_0:
                modPassManager.setForStatus(shouldStatus);
                modPassManager.setForPassageBloque(Boolean.FALSE);
                modPassManager.orderDesire("DATEFACTURATION, IDPASSAGE");
                break;
            case STATUSSWITCH_1:
                modPassManager.setForExceptStatus(shouldStatus);
                modPassManager.setForPassageBloque(Boolean.FALSE); // PO 8390
                modPassManager.orderDesire("DATEFACTURATION DESC, IDPASSAGE DESC");
                break;
        }
        FAPassage passageToReturn = null;

        try {
            modPassManager.find();
            if (modPassManager.size() > 0) {
                FAPassageModule modPassage = null;
                // Itérer sur tous les modules de facturation qui contiennent ce
                // type de module
                modPassage = (FAPassageModule) modPassManager.getFirstEntity();
                passageToReturn = new FAPassage();
                passageToReturn.setSession(session);
                passageToReturn.setIdPassage(modPassage.getIdPassage());
                passageToReturn.retrieve();
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        } finally {
            return passageToReturn;
        }

    }

    public final static IFAPassage getDernierPassageFacturation(BSession session, BTransaction transaction,
            String idTypeModuleFacturation) {

        try {
            if (transaction == null) {
                return ServicesFacturation._getPassageFacturationByStatus(session, idTypeModuleFacturation,
                        FAPassage.CS_ETAT_OUVERT, ServicesFacturation.STATUSSWITCH_1);
            } else {
                return ServicesFacturation._getPassageFacturationByStatus(session, transaction,
                        idTypeModuleFacturation, FAPassage.CS_ETAT_OUVERT, ServicesFacturation.STATUSSWITCH_1);
            }
        } catch (Exception e) {
            JadeLogger.error(ServicesFacturation.class, e.toString()
                    + "::ServicesFacturation::Erreur dans la recherche du dernier passage de facturation");
            return null;
        }
    }

    /**
     * Retourne l'id du type du module de facturation selon le type du module de facturation passé en paramètre Date de
     * création : (29.04.2003 08:37:39)
     * 
     * @return java.lang.String
     */
    public final static String getIdModFacturationByType(BSession session, BTransaction transaction, String idTypeModule) {

        // instance le manager du module de facturation
        FAModuleFacturationManager modFacManager = new FAModuleFacturationManager();
        modFacManager.setSession(session);
        // rechercher l'id pour ce type de facturation
        modFacManager.setForIdTypeModule(idTypeModule);
        // instance du module de facturation
        FAModuleFacturation modFac = null;
        BStatement statement = null;

        try {
            statement = modFacManager.cursorOpen(transaction);
            // Il ne peut y avoir qu'un id unique pour un type de module,
            // prendre le premier qui arrive
            modFac = (FAModuleFacturation) modFacManager.cursorReadNext(statement);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String idModFacToReturn = "";
        try {
            if (modFac != null) {
                if (!JadeStringUtil.isBlankOrZero(modFac.getIdModuleFacturation())) {
                    idModFacToReturn = modFac.getIdModuleFacturation();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            String errorText = "::ServicesFacturation::l'id du module facturation n'existe pas pour le type de module ";
        } finally {
            if (statement != null) {
                try {
                    modFacManager.cursorClose(statement);
                    statement = null;
                } catch (Exception e) {
                    e.printStackTrace();
                    if (statement != null) {
                        statement.closeStatement();
                    }
                }
            }
            return idModFacToReturn;
        }

    }

    /**
     * Retourne l'id du type du module de facturation selon le type du module de facturation passé en paramètre et le
     * numéro du passage de facturation Date de création : (22.11.2010) MBU
     * 
     * @return java.lang.String
     */
    public final static String getIdModFacturationByTypeAndPassage(BSession session, BTransaction transaction,
            String idTypeModule, String idPassage) {

        // instance le manager du module de facturation
        FAModulePassageManager modFacManager = new FAModulePassageManager();
        modFacManager.setSession(session);
        // rechercher l'id pour ce type de facturation
        modFacManager.setForIdTypeModule(idTypeModule);
        // rechercher l'id pour ce numéro de journal de facturation
        modFacManager.setForIdPassage(idPassage);
        // instance du module de facturation
        FAModulePassage modFac = null;
        BStatement statement = null;

        try {
            statement = modFacManager.cursorOpen(transaction);
            // Il ne peut y avoir qu'un id unique pour un type de module,
            // prendre le premier qui arrive
            modFac = (FAModulePassage) modFacManager.cursorReadNext(statement);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String idModFacToReturn = "";
        try {
            if (modFac != null) {
                if (!JadeStringUtil.isBlankOrZero(modFac.getIdModuleFacturation())) {
                    idModFacToReturn = modFac.getIdModuleFacturation();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            String errorText = "::ServicesFacturation::l'id du module facturation n'existe pas pour le type de module ";
        } finally {
            if (statement != null) {
                try {
                    modFacManager.cursorClose(statement);
                    statement = null;
                } catch (Exception e) {
                    e.printStackTrace();
                    if (statement != null) {
                        statement.closeStatement();
                    }
                }
            }
            return idModFacToReturn;
        }

    }

    public final static IFAPassage getPassageById(String idPassage, BSession session) {
        FAPassage myPassage = new FAPassage();
        myPassage.setSession(session);
        myPassage.setIdPassage(idPassage);
        try {
            myPassage.retrieve();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return myPassage;
        }
    }

    public final static IFAPassage getProchainPassageFacturation(BSession session, BTransaction transaction,
            String idTypeModuleFacturation) {

        try {
            if (transaction == null) {
                return ServicesFacturation._getPassageFacturationByStatus(session, idTypeModuleFacturation,
                        FAPassage.CS_ETAT_OUVERT, ServicesFacturation.STATUSSWITCH_0);
            } else {
                return ServicesFacturation._getPassageFacturationByStatus(session, transaction,
                        idTypeModuleFacturation, FAPassage.CS_ETAT_OUVERT, ServicesFacturation.STATUSSWITCH_0);
            }
        } catch (Exception e) {
            JadeLogger.error(ServicesFacturation.class, e.toString()
                    + "::ServicesFacturation::Erreur dans la recherche du prochain passage de facturation");
            return null;
        }
    }

    public ServicesFacturation() {
        super();
    }
}
