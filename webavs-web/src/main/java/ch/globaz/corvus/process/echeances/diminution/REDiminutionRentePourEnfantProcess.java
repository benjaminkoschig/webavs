package ch.globaz.corvus.process.echeances.diminution;

import globaz.corvus.api.annonces.IREAnnonces;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.corvus.process.REDiminutionRenteAccordeeProcess;
import globaz.corvus.utils.REDiminutionRenteUtils;
import globaz.corvus.utils.REPmtMensuel;
import globaz.globall.api.BITransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.lyra.process.LYAbstractEcheanceProcess;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.tools.PRAssert;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class REDiminutionRentePourEnfantProcess extends LYAbstractEcheanceProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Set<String> idRentesADiminuer;
    private Map<String, String> idRentesDiminutionEnErreur;
    private Set<String> idRentesDiminutionOk;
    private boolean ajouterCommunePolitique;

    public REDiminutionRentePourEnfantProcess() {
        super();

        idRentesADiminuer = new HashSet<String>();
        idRentesDiminutionOk = new HashSet<String>();
        idRentesDiminutionEnErreur = new HashMap<String, String>();
    }

    public boolean addIdRenteADiminuer(String idRenteADiminuer) {
        return idRentesADiminuer.add(idRenteADiminuer);
    }

    @Override
    protected void afterExecute() throws Exception {
        // Mail pour l'utilisateur
        String titre = getDescription();
        StringBuilder message = new StringBuilder();

        message.append(getSession().getLabel("MAIL_DIMINUTION_RENTES_ENFANTS_SUCCES")).append("\n\n");

        // traitement des rentes ayant pu être diminuées
        message.append(getSession().getLabel("MAIL_DIMINUTION_RENTES_ENFANTS_DIMINUTION_OK")).append(" : \n");
        for (String idRenteDiminuee : idRentesDiminutionOk) {
            message.append(" - ");
            message.append(getTiersEtRentePourMail(idRenteDiminuee));
            message.append("\n");
        }

        if (idRentesDiminutionEnErreur.size() > 0) {
            message.append("\n");
            // traitement des rentes dont la diminution a échoué
            message.append(getSession().getLabel("MAIL_DIMINUTION_RENTES_ENFANTS_DIMINUTION_KO")).append(" : \n");
            for (String idRenteDiminuee : idRentesDiminutionEnErreur.keySet()) {
                message.append(" - ");
                message.append(getTiersEtRentePourMail(idRenteDiminuee));
                message.append("\n   ");
                message.append(getSession().getLabel("MAIL_DIMINUTION_RENTES_ENFANTS_RAISON")).append(" : ");
                // si un message d'erreur a été remonté, affichage dans le mail
                String messageErreur = idRentesDiminutionEnErreur.get(idRenteDiminuee);
                if (!JadeStringUtil.isBlank(messageErreur) && !"null".equals(messageErreur)) {
                    message.append(messageErreur);
                } else {
                    // sinon, erreur inconnue
                    message.append(getSession().getLabel("MAIL_DIMINUTION_RENTES_ENFANTS_MESSAGE_INCONNNUE"));
                }
                message.append("\n");
            }
        }

        JadeSmtpClient.getInstance().sendMail(getEmailAddress(), titre, message.toString(), null);
    }

    @Override
    protected void beforeExecute() throws Exception {
    }

    @Override
    public String getDescription() {
        return getSession().getLabel("TITRE_MAIL_DIMINUTION_RENTES_ENFANTS");
    }

    public Set<String> getIdRentesADiminuer() {
        return idRentesADiminuer;
    }

    @Override
    public String getName() {
        return REDiminutionRentePourEnfantProcess.class.getName();
    }

    @Override
    protected String getSessionApplicationName() {
        return REApplication.DEFAULT_APPLICATION_CORVUS;
    }

    private String getTiersEtRentePourMail(String idRente) {
        StringBuilder tiersEtRente = new StringBuilder();

        RERenteAccJoinTblTiersJoinDemandeRente rente = new RERenteAccJoinTblTiersJoinDemandeRente();
        rente.setSession(getSession());
        rente.setIdPrestationAccordee(idRente);

        // si la récupération des infos sur le tiers et sa rente se passe mal, message d'erreur dans le mail
        try {
            rente.retrieve();
            if (getAjouterCommunePolitique()) {
                String communePolitique = PRTiersHelper.getCommunePolitique(rente.getIdTiersBeneficiaire(), new Date(),
                        getSession());
                tiersEtRente.append(communePolitique + " / ");
            }
            tiersEtRente.append(rente.getNumeroAvsBenef());
            tiersEtRente.append(" / ");
            tiersEtRente.append(rente.getNomBenef()).append(" ").append(rente.getPrenomBenef());
            tiersEtRente.append(" / ");
            tiersEtRente.append(rente.getDateNaissanceBenef());
            tiersEtRente.append(" / ");
            tiersEtRente.append(rente.getSexeBenef());
            tiersEtRente.append(" / ");
            tiersEtRente.append(rente.getNationaliteBenef());
        } catch (Exception ex) {
            tiersEtRente.append(getSession().getLabel("MAIL_ERREUR_RECUPERATION_INFO_TIERS_ET_RENTE").replace(
                    "{idRenteAccordee}", idRente));
        }

        return tiersEtRente.toString();
    }

    @Override
    protected void runProcess() throws Exception {
        String moisComptable = REPmtMensuel.getDateDernierPmt(getSession());

        if (!REPmtMensuel.isValidationDecisionAuthorise(getSession())) {
            throw new RETechnicalException(getSession().getLabel("JSP_RE_VALIDATION_DECISIONS_INTERDITES_ECHEANCES"));
        }

        for (String unIdRenteADiminuer : idRentesADiminuer) {

            // Inforom 483 : Ne pas diminuer la rente automatiquement si :
            // la RA contient un code cas spécial 02 OU 05
            // la RA possède un montant d'ajournement
            // la RA possède un montant d'anticipation

            boolean doDiminuerLaRente = true;
            String message = null;
            BITransaction transaction = getSession().newTransaction();

            RERenteAccordee ra = new RERenteAccordee();
            ra.setSession(getSession());
            ra.setIdPrestationAccordee(unIdRenteADiminuer);
            ra.retrieve(transaction);
            PRAssert.notIsNew(ra, null);

            // Si la RA est New, elle sera détectée dans le process du coup on la laisse passer
            if (!ra.isNew()) {
                if (ra.contientCodeCasSpecial("02")) {
                    doDiminuerLaRente = false;
                    message = getSession().getLabel("DIMINUTION_RENTE_ENFANT_CODE_CAS_SPECIAL_02");
                    message = "<strong>" + message + "</strong>";
                }
                if (ra.contientCodeCasSpecial("05")) {
                    doDiminuerLaRente = false;
                    message = getSession().getLabel("DIMINUTION_RENTE_ENFANT_CODE_CAS_SPECIAL_05");
                    message = "<strong>" + message + "</strong>";
                }
                if (REDiminutionRenteUtils.hasMontantAjournement(ra)) {
                    doDiminuerLaRente = false;
                    message = getSession().getLabel("DIMINUTION_RENTE_ENFANT_POSSEDE_MONTANT_AJOURNEMENT");
                    message = "<strong>" + message + "</strong>";
                }
                if (REDiminutionRenteUtils.hasMontantAnticipation(ra)) {
                    doDiminuerLaRente = false;
                    message = getSession().getLabel("DIMINUTION_RENTE_ENFANT_POSSEDE_MONTANT_ANTICIPATION");
                    message = "<strong>" + message + "</strong>";
                }
            }

            // On ne traite pas la ra
            if (!doDiminuerLaRente) {
                idRentesDiminutionEnErreur.put(unIdRenteADiminuer, message);
            }

            // Traitement normal
            else {

                boolean enErreur = false;
                REDiminutionRenteAccordeeProcess diminutionRenteAccordeeProcess = new REDiminutionRenteAccordeeProcess(
                        getSession());
                diminutionRenteAccordeeProcess.setIdRenteAccordee(unIdRenteADiminuer);
                diminutionRenteAccordeeProcess.setCsCodeMutation(IREAnnonces.CS_CODE_MUTATION_EVENEMENT_PROCHE_FAM);
                diminutionRenteAccordeeProcess.setDateFinDroit(moisComptable);

                try {
                    diminutionRenteAccordeeProcess.executeProcess();
                } catch (Exception ex) {
                    idRentesDiminutionEnErreur.put(unIdRenteADiminuer, ex.getMessage());
                    enErreur = true;
                }

                if (!enErreur) {
                    idRentesDiminutionOk.add(unIdRenteADiminuer);
                }
            }

        }
    }

    public void setIdRentesADiminuer(Set<String> idRentesADiminuer) {
        this.idRentesADiminuer = idRentesADiminuer;
    }

    public void setAjouterCommunePolitique(boolean ajouterCommunePolitique) {
        this.ajouterCommunePolitique = ajouterCommunePolitique;
    }

    public boolean getAjouterCommunePolitique() {
        return ajouterCommunePolitique;
    }
}
