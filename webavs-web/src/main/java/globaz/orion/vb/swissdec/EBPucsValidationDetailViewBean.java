package globaz.orion.vb.swissdec;

import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.globall.db.BManager;
import globaz.globall.util.JACalendar;
import globaz.hercule.service.CETiersService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.db.releve.AFApercuReleve;
import globaz.naos.db.releve.AFApercuReleveManager;
import globaz.naos.services.AFAffiliationServices;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFIDEUtil;
import globaz.orion.vb.EBAbstractViewBean;
import globaz.pyxis.adresse.formater.TIAdresseFormater;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ch.globaz.draco.business.domaine.DeclarationSalaireType;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaire;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.businessimpl.services.pucs.DeclarationSalaireBuilder;
import ch.globaz.orion.service.EBPucsFileService;
import ch.globaz.orion.ws.service.AFMassesForAffilie;
import ch.globaz.orion.ws.service.AppAffiliationService;

public class EBPucsValidationDetailViewBean extends EBAbstractViewBean {

    private DeclarationSalaire decSal = null;
    private String pucsFileId = null;
    private String id = null;
    private String adresse = null;
    private List<AFMassesForAffilie> listeMasseForAffilie = null;
    private List<AFParticulariteAffiliation> listeParticularites = null;
    private boolean isDsExistante = false;
    private String idDeclarationSalaireExistante = "";
    private String idAffiliation;
    private boolean isAffiliationExistante = false;
    private AFAffiliation affiliation;
    private boolean valideTheNext = false;
    private String numeroIde;
    private boolean fichePartielle = false;
    private boolean codeBlocage = false;
    private boolean releveExistant = false;
    private String idReleve = null;
    private String selectedIds = null;
    private List<String> listOfSelectedIds = new ArrayList<String>();
    private PucsFile pucsFile = null;
    private PucsFile nextPucsFile = null;

    @Override
    public void retrieve() throws Exception {
        if (!JadeStringUtil.isEmpty(selectedIds)) {
            listOfSelectedIds = Arrays.asList(selectedIds.split(","));
        }
        findTheNextToValidate();
        pucsFile = EBPucsFileService.read(getCurrentId(), getSession());
        if (getNextId() != null && !getNextId().isEmpty()) {
            nextPucsFile = EBPucsFileService.read(getNextId(), getSession());
        } else {
            nextPucsFile = null;
        }
        decSal = DeclarationSalaireBuilder.build(EBPucsFileService.retriveFileAsInputStream(getCurrentId(),
                getSession()));

        // Recherche affiliation
        affiliation = AFAffiliationServices.getAffiliationParitaireByNumero(decSal.getNumeroAffilie(),
                Integer.toString(decSal.getAnnee()), getSession());

        if (affiliation != null) {
            isAffiliationExistante = true;

            numeroIde = AFIDEUtil.formatNumIDE(affiliation.getNumeroIDE());
            idAffiliation = affiliation.getAffiliationId();

            // Recherche adresse
            TITiersViewBean tiers = CETiersService.retrieveTiersViewBean(getSession(), affiliation.getIdTiers());

            adresse = tiers.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                    IConstantes.CS_APPLICATION_DEFAUT, JACalendar.todayJJsMMsAAAA(), new TIAdresseFormater(), true,
                    getSession().getIdLangue());

            // REcherche particularité
            traiteParticularites();

            // Recherche cotisations actives
            listeMasseForAffilie = AppAffiliationService.retrieveListCotisationForNumAffilie(getSession(),
                    decSal.getNumeroAffilie(), decSal.getAnnee() + "1231");

            // Recherche si DS existante pour année de la déclaration
            traiteDeclarationSalaire();

            // Recherche si relevé
            traiteReleve();
        }

    }

    /**
     * Retourne l'id du fichier PUCS à traiter.
     * Dans le cas où l'id est saisi, il s'agit de l'id qui sera retourné car il s'agit d'un traitement unitaire
     * Dans le cas oû l'id n'est pas saisi, il s'agit de récupérer le premier élément de
     * {@link EBPucsValidationDetailViewBean#selectedIds}
     * 
     * @return L'id du fichier PUCS à traiter
     */
    public String getCurrentId() {
        if (!JadeStringUtil.isEmpty(id) && !"null".equals(id)) {
            return id;
        } else {
            return listOfSelectedIds.get(0);
        }
    }

    /**
     * Retourne l'id du prochain fichier PUCS à traiter en se basant sur les selectedIds
     * 
     * @return String représentant le prochain document PUCS à traiter, ou null dans le cas où nous ne sommes pas dans
     *         du traitement de masse.
     */
    public String getNextId() {
        if (!listOfSelectedIds.isEmpty()) {
            int index = listOfSelectedIds.indexOf(getCurrentId());
            int nextId = index + 1;
            if (nextId < listOfSelectedIds.size()) {
                return listOfSelectedIds.get(nextId);
            }
        }
        return null;
    }

    /**
     * Retourne si il y a un fichier à traiter après celui-ci
     * 
     * @return true si il y a un prochain fichier
     */
    public boolean hasNext() {
        return nextPucsFile != null;
    }

    /**
     * Retourne si l'utilisateur souhaite passer au fichier PUCS suivant.
     * Il est possible de passer au fichier PUCS suivant uniquement si il y a un fichier après l'actuel.
     * 
     * @return true si souhaite passer au suivant et qu'il y a un fichier suivant
     */
    public boolean wantNext() {
        return hasNext() && valideTheNext;
    }

    public boolean isSwissDec() {
        return pucsFile.isSwissDec();
    }

    private void traiteReleve() throws Exception {
        AFApercuReleveManager manager = new AFApercuReleveManager();
        manager.setSession(getSession());
        manager.setForIdTiers(affiliation.getIdTiers());
        manager.setForAffilieNumero(affiliation.getAffilieNumero());
        manager.setFromDateDebut("01.01." + decSal.getAnnee());
        manager.setUntilDateFin("31.12." + decSal.getAnnee());
        manager.find(BManager.SIZE_NOLIMIT);
        for (int i = 0; i < manager.size(); i++) {
            AFApercuReleve releve = (AFApercuReleve) manager.getEntity(i);
            // Détermination du type (Si relevé déjà existant => complément sinon final)
            if (CodeSystem.TYPE_RELEVE_DECOMP_FINAL.equalsIgnoreCase(releve.getType())
                    || CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA.equalsIgnoreCase(releve.getType())) {
                idReleve = releve.getIdReleve();
                releveExistant = true;
            }
            // // Si relevé en cours de facturation pour l'année => erreur
            // if (CodeSystem.ETATS_RELEVE_SAISIE.equalsIgnoreCase(releve.getEtat())
            // || CodeSystem.ETATS_RELEVE_FACTURER.equalsIgnoreCase(releve.getEtat())) {
            //
            // _addError(getTransaction(), getSession().getLabel("AFSEUL_RELEVE_EXISTANT") + " - "
            // + getSession().getLabel("DEC_AFFILIE") + " " + affilie.getAffilieNumero() + " - "
            // + getSession().getLabel("DEC_ANNEE") + " " + rec.getAnnee());
            // totalErreur++;
            // break;
            // }
        }
    }

    private void traiteDeclarationSalaire() throws Exception {
        DSDeclarationListViewBean manager = new DSDeclarationListViewBean();
        manager.setForAnnee(Integer.toString(decSal.getAnnee()));
        manager.setForAffiliationId(affiliation.getAffiliationId());
        manager.setForTypeDeclaration(DeclarationSalaireType.PRINCIPALE.getCodeSystem());
        manager.setSession(getSession());
        manager.find(BManager.SIZE_NOLIMIT);

        if (manager.getSize() > 0) {
            isDsExistante = true;
            DSDeclarationViewBean ds = (DSDeclarationViewBean) manager.get(0);
            idDeclarationSalaireExistante = ds.getIdDeclaration();
        }
    }

    private void traiteParticularites() {
        listeParticularites = AFAffiliationServices.findListParticulariteAffiliation(affiliation.getAffiliationId(),
                getSession());

        // Recherche si on a une fiche partielle et/ou code blocage
        for (AFParticulariteAffiliation particularite : listeParticularites) {
            if (CodeSystem.PARTIC_AFFILIE_FICHE_PARTIELLE.equals(particularite.getParticularite())) {
                fichePartielle = true;
            }
            if (CodeSystem.PARTIC_AFFILIE_CODE_BLOCAGE_DECFINAL.equals(particularite.getParticularite())) {
                codeBlocage = true;
            }
        }
    }

    private void findTheNextToValidate() {

        List<String> lstIdFichier = (List<String>) getSession().getAttribute("lstIdFichier");

        List<String> lstEpurer = new ArrayList<String>();
        if (lstIdFichier != null) {
            boolean findCurrent = false;
            for (String idPucs : lstIdFichier) {
                if (id.equals(idPucs)) {
                    findCurrent = true;
                } else if (findCurrent) {
                    lstEpurer.add(idPucs);
                }
            }
            getSession().setAttribute("lstIdFichier", lstEpurer);
        }

    }

    public String getLibelle(AFMassesForAffilie masse) {
        if (getSession().getIdLangueISO().equalsIgnoreCase("fr")) {
            return masse.getLibelleFr();
        } else if (getSession().getIdLangueISO().equalsIgnoreCase("de")) {
            return masse.getLibelleDe();
        } else if (getSession().getIdLangueISO().equalsIgnoreCase("it")) {
            return masse.getLibelleIt();
        }

        return masse.getLibelleFr();
    }

    public String getPeriodeAffiliation() {

        String periode = "";

        if (affiliation != null) {
            periode = affiliation.getDateDebut();

            if (JadeStringUtil.isBlankOrZero(affiliation.getDateFin())) {
                periode += " - *";
            } else {
                periode += " - " + affiliation.getDateFin();
            }
        }

        return periode;
    }

    public String getNameNextToValidate() {
        if (nextPucsFile != null) {
            return "(" + nextPucsFile.getId() + ")";
        } else {
            return "";
        }
    }

    public String getPucsFileId() {
        return pucsFileId;
    }

    public void setPucsFileId(String pucsFileId) {
        this.pucsFileId = pucsFileId;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public DeclarationSalaire getDecSal() {
        return decSal;
    }

    public void setDecSal(DeclarationSalaire decSal) {
        this.decSal = decSal;
    }

    public List<AFMassesForAffilie> getListeMasseForAffilie() {
        return listeMasseForAffilie;
    }

    public void setListeMasseForAffilie(List<AFMassesForAffilie> listeMasseForAffilie) {
        this.listeMasseForAffilie = listeMasseForAffilie;
    }

    public boolean isDsExistante() {
        return isDsExistante;
    }

    public void setDsExistante(boolean isDsExistante) {
        this.isDsExistante = isDsExistante;
    }

    public List<AFParticulariteAffiliation> getListeParticularites() {
        return listeParticularites;
    }

    public void setListeParticularites(List<AFParticulariteAffiliation> listeParticularites) {
        this.listeParticularites = listeParticularites;
    }

    public String getIdDeclarationSalaireExistante() {
        return idDeclarationSalaireExistante;
    }

    public void setIdDeclarationSalaireExistante(String idDeclarationSalaireExistante) {
        this.idDeclarationSalaireExistante = idDeclarationSalaireExistante;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public boolean isAffiliationExistante() {
        return isAffiliationExistante;
    }

    public void setAffiliationExistante(boolean isAffiliationExistante) {
        this.isAffiliationExistante = isAffiliationExistante;
    }

    public AFAffiliation getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(AFAffiliation affiliation) {
        this.affiliation = affiliation;
    }

    public boolean isValideTheNext() {
        return valideTheNext;
    }

    public void setValideTheNext(boolean valideTheNext) {
        this.valideTheNext = valideTheNext;
    }

    public String getNumeroIde() {
        return numeroIde;
    }

    public void setNumeroIde(String numeroIde) {
        this.numeroIde = numeroIde;
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    public void setIdAffiliation(String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    public boolean isFichePartielle() {
        return fichePartielle;
    }

    public void setFichePartielle(boolean fichePartielle) {
        this.fichePartielle = fichePartielle;
    }

    public boolean isCodeBlocage() {
        return codeBlocage;
    }

    public void setCodeBlocage(boolean codeBlocage) {
        this.codeBlocage = codeBlocage;
    }

    public String getIdReleve() {
        return idReleve;
    }

    public void setIdReleve(String idReleve) {
        this.idReleve = idReleve;
    }

    public boolean isReleveExistant() {
        return releveExistant;
    }

    public void setReleveExistant(boolean releveExistant) {
        this.releveExistant = releveExistant;
    }

    public boolean hasContactMail() {
        return !JadeStringUtil.isEmpty(decSal.getContactMail());
    }

    public boolean isRefuser() {
        return pucsFile.isRefuse();
    }

    public String getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(String selectedIds) {
        this.selectedIds = selectedIds;
    }

    public PucsFile getPucsFile() {
        return pucsFile;
    }

    public void setPucsFile(PucsFile pucsFile) {
        this.pucsFile = pucsFile;
    }

    public boolean isEditable() {
        return !pucsFile.isEditable();
    }
}
