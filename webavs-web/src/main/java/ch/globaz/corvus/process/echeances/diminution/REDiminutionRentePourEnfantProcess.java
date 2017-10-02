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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class REDiminutionRentePourEnfantProcess extends LYAbstractEcheanceProcess {

    /**
     * Classe utilisée pour le tri et la génération des message du mail
     * 
     * @author lga
     * 
     */
    private class Pojo implements Comparable<Pojo> {

        private String idRenteAccordee;
        private String communePolitique;
        private String nss;
        private String prenom;
        private String nom;
        private String dateNaissance;
        private String sexe;
        private String nationalite;

        public Pojo(String communePolitique, String idRenteAccordee, String nss, String prenom, String nom,
                String dateNaissance, String sexe, String nationalite) {
            this.communePolitique = communePolitique;
            this.idRenteAccordee = idRenteAccordee;
            this.nss = nss;
            this.prenom = prenom;
            this.nom = nom;
            this.dateNaissance = dateNaissance;
            this.sexe = sexe;
            this.nationalite = nationalite;

            // petite sécurité pour éviter les nullPointerException
            if (JadeStringUtil.isEmpty(communePolitique)) {
                communePolitique = "";
            }
            if (JadeStringUtil.isEmpty(prenom)) {
                prenom = "";
            }
            if (JadeStringUtil.isEmpty(nom)) {
                nom = "";
            }

        }

        public String getTiersInfo() {
            StringBuilder tiersInfo = new StringBuilder();
            if (getAjouterCommunePolitique()) {
                tiersInfo.append(communePolitique);
                tiersInfo.append(" / ");
            }
            tiersInfo.append(nss);
            tiersInfo.append(" / ");
            tiersInfo.append(nom);
            tiersInfo.append(" ");
            tiersInfo.append(prenom);
            tiersInfo.append(" / ");
            tiersInfo.append(dateNaissance);
            tiersInfo.append(" / ");
            tiersInfo.append(sexe);
            tiersInfo.append(" / ");
            tiersInfo.append(nationalite);
            return tiersInfo.toString();
        }

        public String getIdRenteAccordee() {
            return idRenteAccordee;
        }

        public String getCommunePolitique() {
            return communePolitique;
        }

        public String getPrenom() {
            return prenom;
        }

        public String getNom() {
            return nom;
        }

        @Override
        public int compareTo(Pojo o) {
            int value = 0;
            if (getAjouterCommunePolitique()) {
                value = communePolitique.compareTo(o.getCommunePolitique());
                if (value != 0) {
                    return value;
                }
            }
            value = nom.compareTo(o.getNom());
            if (value != 0) {
                return value;
            }
            return prenom.compareTo(o.getPrenom());
        }
    }

    // -----------------------------------------------------------

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

        creerMessageDiminnutionOk(message);
        message.append("\n");
        creerMessageDiminutionKo(message);

        JadeSmtpClient.getInstance().sendMail(getEmailAddress(), titre, message.toString(), null);
    }

    /**
     * Génère les messages pour les diminution qui ont échouées
     * 
     * @param message
     */
    private void creerMessageDiminutionKo(final StringBuilder message) {

        if (idRentesDiminutionEnErreur.size() > 0) {
            message.append(getSession().getLabel("MAIL_DIMINUTION_RENTES_ENFANTS_DIMINUTION_KO")).append(" : \n");

            List<Pojo> pojos = new ArrayList<REDiminutionRentePourEnfantProcess.Pojo>();
            List<String> erreurs = new ArrayList<String>();

            RERenteAccJoinTblTiersJoinDemandeRente rente = null;
            String communePolitique = null;

            // Création des pojos pour chacun des messages
            for (String idRenteDiminuee : idRentesDiminutionEnErreur.keySet()) {
                rente = new RERenteAccJoinTblTiersJoinDemandeRente();
                communePolitique = null;
                try {
                    rente.setSession(getSession());
                    rente.setIdPrestationAccordee(idRenteDiminuee);
                    rente.retrieve();

                    if (getAjouterCommunePolitique()) {
                        communePolitique = PRTiersHelper.getCommunePolitique(rente.getIdTiersBeneficiaire(),
                                new Date(), getSession());
                    }

                    pojos.add(new Pojo(communePolitique, rente.getIdPrestationAccordee(), rente.getNumeroAvsBenef(),
                            rente.getPrenomBenef(), rente.getNomBenef(), rente.getDateNaissanceBenef(), rente
                                    .getSexeBenef(), rente.getNationaliteBenef()));

                } catch (Exception ex) {
                    erreurs.add(getSession().getLabel("MAIL_ERREUR_RECUPERATION_INFO_TIERS_ET_RENTE").replace(
                            "{idRenteAccordee}", idRenteDiminuee));
                }
            }

            // Tri des pojos
            Collections.sort(pojos);

            // Génération des messages
            for (Pojo popo : pojos) {
                message.append(popo.getTiersInfo());
                message.append("\n   ");
                message.append(getSession().getLabel("MAIL_DIMINUTION_RENTES_ENFANTS_RAISON")).append(" : ");
                // si un message d'erreur a été remonté, affichage dans le mail
                String messageErreur = idRentesDiminutionEnErreur.get(popo.getIdRenteAccordee());
                if (!JadeStringUtil.isBlank(messageErreur) && !"null".equals(messageErreur)) {
                    message.append(messageErreur);
                } else {
                    // sinon, erreur inconnue
                    message.append(getSession().getLabel("MAIL_DIMINUTION_RENTES_ENFANTS_MESSAGE_INCONNNUE"));
                }
                message.append("\n");
            }
            for (String error : erreurs) {
                message.append(error);
                message.append("\n");
            }
        }
    }

    /**
     * Génère les messages pour les diminution qui ont réussis
     * 
     * @param message
     */
    private void creerMessageDiminnutionOk(final StringBuilder message) {
        message.append(getSession().getLabel("MAIL_DIMINUTION_RENTES_ENFANTS_SUCCES")).append("\n\n");

        if (idRentesDiminutionOk.size() > 0) {

            message.append(getSession().getLabel("MAIL_DIMINUTION_RENTES_ENFANTS_DIMINUTION_OK")).append(" : \n");

            List<Pojo> pojos = new ArrayList<REDiminutionRentePourEnfantProcess.Pojo>();
            List<String> erreurs = new ArrayList<String>();

            RERenteAccJoinTblTiersJoinDemandeRente rente = null;
            String communePolitique = null;

            // Génération des pojos pour les tri des messages
            for (String idRenteDiminuee : idRentesDiminutionOk) {
                rente = new RERenteAccJoinTblTiersJoinDemandeRente();
                communePolitique = null;
                try {
                    rente.setSession(getSession());
                    rente.setIdPrestationAccordee(idRenteDiminuee);
                    rente.retrieve();

                    if (getAjouterCommunePolitique()) {
                        communePolitique = PRTiersHelper.getCommunePolitique(rente.getIdTiersBeneficiaire(),
                                new Date(), getSession());
                    }

                    pojos.add(new Pojo(communePolitique, rente.getIdPrestationAccordee(), rente.getNumeroAvsBenef(),
                            rente.getPrenomBenef(), rente.getNomBenef(), rente.getDateNaissanceBenef(), rente
                                    .getSexeBenef(), rente.getNationaliteBenef()));

                } catch (Exception ex) {
                    erreurs.add(getSession().getLabel("MAIL_ERREUR_RECUPERATION_INFO_TIERS_ET_RENTE").replace(
                            "{idRenteAccordee}", idRenteDiminuee));
                }
            }

            // Tri des pojos
            Collections.sort(pojos);

            // Génération des messages pour le mail
            for (Pojo popo : pojos) {
                message.append(popo.getTiersInfo());
                message.append("\n");
            }
            for (String error : erreurs) {
                message.append(error);
                message.append("\n");
            }
        }
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

            /**
             * si la prestation est bloquée, il faut s'assurer que le flag ZTBPRB est à 2 (false)
             * (résolution K170302_001)
             **/

            if (ra.getIsPrestationBloquee()) {
                ra.setIsPrestationBloquee(false);
                ra.update(transaction);
            }

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
