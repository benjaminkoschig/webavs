package globaz.naos.db.releve;

import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.secure.user.FWSecureGroupUser;
import globaz.framework.secure.user.FWSecureGroupUsers;
import globaz.framework.secure.user.FWSecureUser;
import globaz.framework.secure.user.FWSecureUsers;
import globaz.globall.db.BManager;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAVector;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.translation.CodeSystem;
import java.util.Vector;

/**
 * Le viewBean de l'entité Relevé.
 * 
 * @author jts, sau 18 avr. 05 14:27:53
 */
public class AFApercuReleveViewBean extends AFApercuReleve implements FWViewBeanInterface {

    private static final long serialVersionUID = 1L;
    private boolean isSalaireDifferesPresent = false;

    private String warningMessage = new String();

    public String getWarningMessage() {
        return warningMessage;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }

    /**
     * Récupère la liste des collaborateurs
     * 
     * @param httpSession
     * @return
     */
    public static Vector<String[]> getUserList(javax.servlet.http.HttpSession httpSession) {

        Vector<String[]> vList = new Vector<String[]>();

        // ajoute un blanc
        String[] list = new String[2];
        list[0] = "";
        list[1] = "";
        vList.add(list);

        JAVector groupVector = new JAVector();

        try {

            FWSecureGroupUsers groupManager = new FWSecureGroupUsers();
            groupManager.setISession(globaz.phenix.translation.CodeSystem.getSession(httpSession));
            // Charger le manager avec la requête sur le group NEM
            groupManager.setForGroup("NEM");
            groupManager.changeManagerSize(0);
            groupManager.find();

            groupVector.addAll(groupManager.getContainer());

            FWSecureUsers manager = new FWSecureUsers();
            manager.setISession(globaz.phenix.translation.CodeSystem.getSession(httpSession));
            manager.changeManagerSize(0);
            manager.find();

            for (int i = 0; i < manager.size(); i++) {
                list = new String[2];
                FWSecureUser entity = (FWSecureUser) manager.getEntity(i);
                // limiter la liste seulement au users qui sont contenus dans le
                // groupVector
                for (int j = 0; j < groupVector.size(); j++) {
                    FWSecureGroupUser secGroupUser = (FWSecureGroupUser) groupVector.get(j);
                    if (secGroupUser.getUser().equalsIgnoreCase((entity.getUser()))) {
                        list[0] = entity.getId();
                        list[1] = entity.getFirstname() + " " + entity.getLastname();
                        vList.add(list);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vList;
    }

    public AFApercuReleveViewBean() {
        super();
    }

    public boolean checkPlausiBouclement() {
        boolean plausi = true;
        String messageErreur = new String();

        if (getType().equals(CodeSystem.TYPE_RELEVE_BOUCLEMENT_ACOMPTE)) {
            AFApercuReleveManager manager = new AFApercuReleveManager();

            try {
                // on récupère l'annee de la date de début
                int annee = JACalendar.getYear(getDateDebut());

                manager.setSession(getSession());
                manager.setForAffilieNumero(getAffilieNumero());
                manager.setFromDateDebut("01.01." + annee);
                manager.setUntilDateFin("31.12." + annee);
                manager.find();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (manager.size() > 0) {
                for (int i = 0; i < manager.size(); i++) {
                    AFApercuReleve releve = (AFApercuReleve) manager.getEntity(i);
                    if (releve.getType().equals(CodeSystem.TYPE_RELEVE_TAXATION_OFFICE)
                            || releve.getType().equals(CodeSystem.TYPE_RELEVE_BOUCLEMENT_ACOMPTE)
                            || releve.getType().equals(CodeSystem.TYPE_RELEVE_DECOMP_FINAL)
                            || releve.getType().equals(CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA)) {
                        plausi = false;
                        // setMsgType(FWViewBeanInterface.ERROR);
                        // setMessage(getSession().getLabel("ERREUR_BOUCLEMENT"));
                        messageErreur = messageErreur + getSession().getLabel("ERREUR_BOUCLEMENT");
                        clearCotisationList();
                        break;
                    }
                }
            }

            try {
                // Vérification dans les déclarations de salaire qu'il n'existe
                // pas de déclaration en état à facturer ou comptabilisée
                int annee = JACalendar.getYear(getDateDebut());
                AFAffiliationManager affiliationManager = new AFAffiliationManager();
                affiliationManager.setSession(getSession());
                affiliationManager.setForAffilieNumero(getAffilieNumero());
                affiliationManager.setForTypesAffParitaires();
                affiliationManager.find(BManager.SIZE_NOLIMIT);
                AFAffiliation affilie = (AFAffiliation) affiliationManager.getFirstEntity();

                DSDeclarationListViewBean declarationManager = new DSDeclarationListViewBean();
                declarationManager.setSession(getSession());
                declarationManager.setForAffiliationId(affilie.getAffiliationId());
                declarationManager.setForAnnee(new Integer(annee).toString());
                declarationManager.find(BManager.SIZE_NOLIMIT);

                if (declarationManager.size() > 0) {
                    for (int i = 0; i < declarationManager.size(); i++) {
                        DSDeclarationViewBean declaration = (DSDeclarationViewBean) declarationManager.getEntity(i);
                        if (declaration.getTypeDeclaration().equals(DSDeclarationViewBean.CS_PRINCIPALE)
                                && (declaration.getEtat().equals(DSDeclarationViewBean.CS_AFACTURER) || declaration
                                        .getEtat().equals(DSDeclarationViewBean.CS_COMPTABILISE))) {
                            plausi = false;
                            // setMsgType(FWViewBeanInterface.ERROR);
                            // setMessage(getSession().getLabel("ERREUR_BOUCLEMENT_DS_EXISTE"));
                            messageErreur = messageErreur + "\n\n"
                                    + getSession().getLabel("ERREUR_BOUCLEMENT_DS_EXISTE");
                            clearCotisationList();
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: handle exception
            }

            if (!plausi && !JadeStringUtil.isBlank(messageErreur)) {
                setMsgType(FWViewBeanInterface.ERROR);
                setMessage(messageErreur);
            }
        }
        return plausi;
    }

    /**
     * Methode permettant de rechercher des messages de warnings qui seront affichées à l'écran
     */
    public void fillWarningMessage() {

        StringBuilder messageBuilder = new StringBuilder();

        if (JadeStringUtil.isEmpty(getDateDebut())) {
            return;
        }

        try {
            int annee = JACalendar.getYear(getDateDebut());

            if (JadeStringUtil.isEmpty(getAffiliationId())) {
                AFAffiliation aff = getAffiliation();
                if (aff != null & !aff.isNew()) {
                    setAffiliationId(aff.getAffiliationId());
                } else {
                    return;
                }
            }

            if (CodeSystem.TYPE_RELEVE_BOUCLEMENT_ACOMPTE.equals(getType())) {

                if (AFReleve1314Checker.hasDeclarationSalaireAFacturer(DSDeclarationViewBean.CS_BOUCLEMENT_ACOMPTE,
                        annee, getAffiliationId(), getSession())) {
                    messageBuilder.append(getSession().getLabel("DECLARATION_AVERTISSEMENT_TYPE_14_EXISTE_DEJA"));
                }

            } else if (CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA.equals(getType())) {

                if (AFReleve1314Checker.hasDeclarationSalaireAFacturer(DSDeclarationViewBean.CS_PRINCIPALE, annee,
                        getAffiliationId(), getSession())) {
                    messageBuilder.append(getSession().getLabel("DECLARATION_AVERTISSEMENT_TYPE_13_EXISTE_DEJA"))
                            .append("<br />");
                }

                if (AFReleve1314Checker.hasDeclarationSalaireAFacturer(DSDeclarationViewBean.CS_BOUCLEMENT_ACOMPTE,
                        annee, getAffiliationId(), getSession())) {
                    messageBuilder.append(getSession().getLabel("DECLARATION_AVERTISSEMENT_TYPE_14_EXISTE_DEJA"))
                            .append("<br />");
                }

                if (AFReleve1314Checker.hasReleveAFacturerOuValide(CodeSystem.TYPE_RELEVE_BOUCLEMENT_ACOMPTE, annee,
                        getAffilieNumero(), getIdReleve(), getSession())) {
                    messageBuilder.append(getSession().getLabel("RELEVE_AVERTISSEMENT_TYPE_14_EXISTE_DEJA")).append(
                            "<br />");
                }

                if (AFReleve1314Checker.hasReleveAFacturerOuValide(CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA, annee,
                        getAffilieNumero(), getIdReleve(), getSession())) {
                    messageBuilder.append(getSession().getLabel("RELEVE_AVERTISSEMENT_TYPE_13_EXISTE_DEJA")).append(
                            "<br />");
                }
            }

        } catch (Exception e) {

            // On fait rien car on doit pas bloquer l'affichage
            JadeLogger.warn("Unabled to find releve or declaration 13/14 for warning message", e);
        }

        setWarningMessage(messageBuilder.toString());

    }

    /**
     * Check si il existe des relevés salaire différés pour le cas d'un bouclement d'acompte ou decompte final
     */
    public void checkWarningSalaireDifferes() {
        if (CodeSystem.TYPE_RELEVE_BOUCLEMENT_ACOMPTE.equals(getType())
                || CodeSystem.TYPE_RELEVE_DECOMP_FINAL.equals(getType())
                || CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA.equals(getType())) {
            AFApercuReleveManager manager = new AFApercuReleveManager();

            int annee = 0;

            try {
                // on récupère l'annee de la date de début
                annee = JACalendar.getYear(getDateDebut());

                manager.setSession(getSession());
                manager.setForAffilieNumero(getAffilieNumero());
                manager.setFromDateDebut("01.01." + annee);
                manager.setUntilDateFin("31.12." + annee);
                manager.setForType(CodeSystem.TYPE_RELEVE_SALAIRE_DIFFERES);

                // Si il existe au moins un relevé, on le signale a l'écran
                if (manager.getCount() > 0) {
                    setSalaireDifferesPresent(true);
                }
            } catch (Exception e) {
                JadeLogger.warn(e, "unabled to retrieve apercu releve from 01.01." + annee + " to 31.12." + annee
                        + " for the type 829009");
            }
        }
    }

    public boolean isSalaireDifferesPresent() {
        return isSalaireDifferesPresent;
    }

    public void setSalaireDifferesPresent(boolean isSalaireDifferesPresent) {
        this.isSalaireDifferesPresent = isSalaireDifferesPresent;
    }

}
