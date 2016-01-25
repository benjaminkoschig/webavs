package globaz.musca.db.facturation;

import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.admin.user.service.JadeUserService;
import globaz.jade.client.util.JadeStringUtil;
import java.util.HashSet;

public class FAPassageViewBean extends FAPassage implements globaz.framework.bean.FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /*
     * Permet d'éviter de charger dans la liste des codes systèmes certains type de facturation
     */
    public static HashSet<String> getExceptTypeFacturation() {
        HashSet<String> except = new HashSet<String>();
        // liste des cs qui ne devront pas figurér dans la liste
        except.add(FAPassage.CS_JANVIER);
        except.add(FAPassage.CS_FEVRIER);
        except.add(FAPassage.CS_MARS);
        except.add(FAPassage.CS_AVRIL);
        except.add(FAPassage.CS_MAI);
        except.add(FAPassage.CS_JUIN);
        except.add(FAPassage.CS_JUILLET);
        except.add(FAPassage.CS_AOUT);
        except.add(FAPassage.CS_SEPTEMBRE);
        except.add(FAPassage.CS_OCTOBRE);
        except.add(FAPassage.CS_NOVEMBRE);
        except.add(FAPassage.CS_DECEMBRE);
        return except;
    }

    // Attributs
    private java.lang.String action = null;
    private java.lang.String saveDateFacturation = null;

    // Méthodes

    private java.lang.String savePeriode = null;

    /*
     * Traitement après lecture
     */
    @Override
    protected void _afterRetrieve(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // Recherche de la remarque
        setRemarque(FARemarque.getRemarque(getIdRemarque(), transaction));
        // Sauvegarde de la période et de la date de facturation
        setSaveDateFacturation(getDateFacturation());
        setSavePeriode(getDatePeriode());
    }

    /*
     * Traitement après modification
     */
    @Override
    protected void _beforeUpdate(globaz.globall.db.BTransaction transaction) {
        // Mettre l'action GENERER ou IMPRIMER pour les modules du plan en cas
        // de modification
        // de la période ou de la date de facturation
        if ((!saveDateFacturation.equalsIgnoreCase(getDateFacturation()))
                || (!savePeriode.equalsIgnoreCase(getDatePeriode()))) {
            FAModulePassageManager moduleManager = new FAModulePassageManager();
            moduleManager.setForIdPassage(getIdPassage());
            moduleManager.setSession(getSession());
            try {
                // Lecture des modules du plan de facturation
                moduleManager.find(transaction);
                for (int i = 0; i < moduleManager.size(); i++) {
                    // Copie des modules du plan dans les modules du passage
                    FAModulePassage module = (FAModulePassage) moduleManager.getEntity(i);

                    // Si le module est de type liste mettre l'action IMPRIMER
                    // sinon VIDE (avant: GENERER)
                    if (module.getIdTypeModule().equalsIgnoreCase(FAModuleFacturation.CS_MODULE_LISTE)) {
                        module.setIdAction(FAModulePassage.CS_ACTION_IMPRIMER);
                    } else {
                        module.setIdAction(FAModulePassage.CS_ACTION_VIDE);
                    }
                    try {

                        transaction.disableSpy();
                        module.update(transaction);
                        transaction.commit();
                    } catch (Exception e) {
                        _addError(transaction, "Erreur lors de la mise à jour des modules. ");
                    } finally {
                        transaction.enableSpy();
                    }
                }
            } catch (Exception e) {
                _addError(transaction, "Erreur lors de la lecture des modules du plan. ");
            }
        }
        try {
            super._beforeUpdate(transaction);
        } catch (Exception e) {
            _addError(transaction, "Erreur dans l'action _afterUpdate. ");
        }
    }

    public java.lang.String getAction() {
        return action;
    }

    public String getLibellePlanFacturation() {
        FAPlanFacturation planFact = new FAPlanFacturation();
        try {
            planFact.setSession(getSession());
            planFact.setIdPlanFacturation(getIdPlanFacturation());
            planFact.retrieve();
        } catch (Exception e) {
            return "";
        }
        return planFact.getLibelle();
    }

    public java.lang.String getSaveDateFacturation() {
        return saveDateFacturation;
    }

    // GETTERS

    public java.lang.String getSavePeriode() {
        return savePeriode;
    }

    public String giveUserCreateur() {
        if (!JadeStringUtil.isBlankOrZero(getUserCreateur())) {
            try {
                JadeUserService userService = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator()
                        .getUserService();
                JadeUser user = userService.load(getUserCreateur());
                if (user == null) {
                    throw new Exception();
                }
                return user.getFirstname() + " " + user.getLastname();

            } catch (Exception e) {
            }
        }
        return "";
    }

    public boolean isEtatOuvert() {
        if (FAPassage.CS_ETAT_OUVERT.equalsIgnoreCase(getStatus())) {
            return true;
        }
        return false;
    }

    // SETTERS

    public void setAction(java.lang.String newAction) {
        action = newAction;
    }

    public void setSaveDateFacturation(java.lang.String newSaveDateFacturation) {
        saveDateFacturation = newSaveDateFacturation;
    }

    public void setSavePeriode(java.lang.String newSavePeriode) {
        savePeriode = newSavePeriode;
    }

}
