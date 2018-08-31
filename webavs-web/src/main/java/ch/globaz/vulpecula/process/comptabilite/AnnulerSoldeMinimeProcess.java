package ch.globaz.vulpecula.process.comptabilite;

import globaz.framework.util.FWMessage;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APISection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.osiris.business.data.JournalConteneur;
import ch.globaz.osiris.business.model.EcritureSimpleModel;
import ch.globaz.osiris.business.model.JournalSimpleModel;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.registre.CategorieFactureAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.models.registre.GenreCotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.external.BProcessWithContext;
import ch.globaz.vulpecula.repositoriesjade.decompte.DecompteRepositoryJade;
import ch.globaz.vulpecula.util.DBUtil;

public class AnnulerSoldeMinimeProcess extends BProcessWithContext {
    private static final String ASSOCIATION_CPP = "507014";

    private static final long serialVersionUID = 298707109860215016L;

    private static final String LIBELLE_JOURNAL_DEFAULT = "Annulation des soldes minimes";

    private String journalLibelle = "";
    private String journalDate = "";
    private String montantMinime;
    private String typeMembre = "";
    private boolean simulation;

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();

        if (isSimulation()) {
            getMemoryLog().logMessage(getSession().getLabel("MODESIMULATION"), FWMessage.INFORMATION,
                    this.getClass().getName());
        }

        try {
            processAllSectionsMontantMinimum();
        } catch (IllegalStateException ex) {
            getTransaction().addErrors(ex.getMessage());
        }

        return true;
    }

    /**
     * Solde les sections dont le montant est minimum en utilisant la rubrique passée en paramètre par l'utilisateur.
     * 
     * @throws Exception
     */
    private void processAllSectionsMontantMinimum() throws Exception {
        ArrayList<HashMap<String, Object>> result = getSectionsWithMinimalAmountToTreat();

        if (getSession().hasErrors()) {
            throw new Exception(getSession().getErrors().toString());
        }

        if (result.isEmpty()) {
            getMemoryLog().logMessage(getSession().getLabel("ANNULATIONSOLDE_VIDE"), FWMessage.FATAL,
                    this.getClass().getName());
            return;
        }

        setProgressScaleValue(result.size());
        JournalConteneur jc = null;
        if (!isSimulation()) {
            jc = createJournal();
        }

        try {
            for (HashMap<String, Object> value : result) {
                annulerSolde(value, jc);
                incProgressCounter();
            }

            if (!isSimulation()) {
                CABusinessServiceLocator.getJournalService().comptabilise(
                        CABusinessServiceLocator.getJournalService().createJournalAndOperations(jc));
                JadeThread.commitSession();
            }
        } catch (Exception e) {
            if (!getTransaction().hasErrors()) {
                getTransaction().addErrors(e.getMessage());
            }
        }
    }

    /**
     * Cette méthode permet de récupérer les sections qui contiennent des montants minimes devant être normalisés.
     * 
     * @return ArrayList<HashMap<String, Object>> -> la liste des cas à normaliser
     * @throws JadePersistenceException
     */
    private ArrayList<HashMap<String, Object>> getSectionsWithMinimalAmountToTreat() throws JadePersistenceException {
        StringBuilder query = new StringBuilder();
        query.append("SELECT s.IDCOMPTEANNEXE, s.IDSECTION, s.SOLDE, c.IDEXTERNEROLE, asso.ID_ASSOCIATION, asso.NUMERO_SECTION, adm.HBCADM");
        query.append(" FROM SCHEMA.casectp s join SCHEMA.CACPTAP c on c.IDCOMPTEANNEXE=s.IDCOMPTEANNEXE");
        query.append(" join SCHEMA.AFAFFIP af on af.MALNAF = c.IDEXTERNEROLE");
        query.append(" left join SCHEMA.PT_ENTETE_FACTURE_AP asso on asso.ID_PT_EMPLOYEURS=af.MAIAFF and asso.NUMERO_SECTION=s.IDEXTERNE");
        query.append(" join webavss.TIADMIP adm on adm.HTITIE=asso.ID_ASSOCIATION");
        query.append(" where s.IDTYPESECTION=");
        String messageInformationTraitement = getSession().getLabel("ANNULATION_SOLDES_MINIMES_MEMBRES");
        if (GenreCotisationAssociationProfessionnelle.MEMBRE.getValue().equals(getTypeMembre())) {
            query.append(APISection.ID_TYPE_SECTION_ASSOCIATION);
        } else {
            query.append(APISection.ID_TYPE_SECTION_CPP);
            messageInformationTraitement = getSession().getLabel("ANNULATION_SOLDES_MINIMES_NON_MEMBRES");
        }
        query.append(" AND s.SOLDE<>0 AND s.SOLDE BETWEEN -").append(getMontantMinime()).append(" AND ")
                .append(getMontantMinime());
        query.append(" ORDER BY c.IDEXTERNEROLE DESC");

        ArrayList<HashMap<String, Object>> result = DBUtil.executeQuery(query.toString(), DecompteRepositoryJade.class);

        // Affichage de l'information du type de liste dans le message retourné au client
        getMemoryLog().logMessage(messageInformationTraitement, FWMessage.INFORMATION, this.getClass().getName());
        return result;
    }

    /**
     * Annuler le solde de la section dont le montant est minime.
     * 
     * @param section
     *            la section
     * @param entity
     *            entité en cas d'annulation par secteur
     * @return
     * @throws Exception
     */
    private void annulerSolde(HashMap<String, Object> map, JournalConteneur jc) throws Exception {
        if ((map == null) || map.isEmpty()) {
            return;
        }

        String idExterneRole = String.valueOf(map.get("IDEXTERNEROLE"));
        String numeroSection = String.valueOf(map.get("NUMERO_SECTION"));
        String idCompteAnnexe = String.valueOf(map.get("IDCOMPTEANNEXE"));
        String idsection = String.valueOf(map.get("IDSECTION"));
        String solde = String.valueOf(map.get("SOLDE"));
        String idRubrique = null;
        String idAssociation = String.valueOf(map.get("ID_ASSOCIATION"));
        String codeAdministration = String.valueOf(map.get("HBCADM"));
        // Si le type paramétré est non-membre, il est nécessaire d'aller rechercher le tiers parent dans les liens
        // entre
        // tiers
        if (!GenreCotisationAssociationProfessionnelle.MEMBRE.getValue().equals(getTypeMembre())) {

            List<HashMap<String, Object>> liensTiers = resolveTiersLinked(idAssociation);
            String idAssociationParente = null;

            if (liensTiers.isEmpty()) {
                prepareErrorMessage(idAssociation, codeAdministration, "ERROR_NO_PARENT_LINK_FOUND");
                return;
            }

            int i = 0;
            while (liensTiers.size() > i && JadeStringUtil.isBlankOrZero(idRubrique)) {
                Map<String, Object> mapLienTiers = liensTiers.get(i);
                idAssociationParente = String.valueOf(mapLienTiers.get("HTITIP"));
                idRubrique = resolveIDRubrique(idRubrique, idAssociationParente);
                i++;
            }
            // Si on a trouvé une association parente, on prend celle ci comme association principale pour protocoler
            // les message d'erreurs si la rubrique n'est pas retrouvée.
            idAssociation = idAssociationParente;
        } else {
            idRubrique = resolveIDRubrique(idRubrique, idAssociation);
        }
        if (idRubrique == null || idRubrique.isEmpty() || "null".equals(idRubrique)) {
            prepareErrorMessage(idAssociation, codeAdministration, "ERROR_NO_RUBRIQUE_SET_FOR_ID_ASSOCIATION");
            return;
        }

        // Indiquer le cas si simulation
        if (isSimulation()) {
            getMemoryLog().logMessage(
                    getSession().getLabel("COMPTEANNEXE") + " / " + getSession().getLabel("SECTION") + " : "
                            + idExterneRole + " / " + numeroSection, FWMessage.INFORMATION, this.getClass().getName());
            return;
        }

        prepareJournalForCompta(jc, idCompteAnnexe, idsection, solde, idRubrique);
    }

    private void prepareJournalForCompta(JournalConteneur jc, String idCompteAnnexe, String idsection, String solde,
            String idRubrique) {
        EcritureSimpleModel ecritureAnnulation = new EcritureSimpleModel();
        ecritureAnnulation.setIdCompteAnnexe(idCompteAnnexe);
        ecritureAnnulation.setIdSection(idsection);
        ecritureAnnulation.setDate(journalDate);
        ecritureAnnulation.setMontant(solde);
        ecritureAnnulation.setLibelle(getSession().getLabel("ANNULATIONSOLDE_TEXTE"));
        ecritureAnnulation.setIdRubrique(idRubrique);
        ecritureAnnulation.setAnnee(String.valueOf(Date.getCurrentYear()));

        if ((new Montant(solde)).isNegative()) {
            ecritureAnnulation.setCodeDebitCredit(APIEcriture.DEBIT);
        } else {
            ecritureAnnulation.setCodeDebitCredit(APIEcriture.CREDIT);
        }

        jc.addEcriture(ecritureAnnulation);
    }

    private void prepareErrorMessage(String idAssociation, String codeAdministration, String labelMsg) {
        String errorMsg = getSession().getLabel(labelMsg).replace("{0}", codeAdministration);
        errorMsg = errorMsg.replace("{1}", idAssociation);
        getMemoryLog().logMessage(errorMsg, FWMessage.FATAL, this.getClass().getName());
    }

    private List<HashMap<String, Object>> resolveTiersLinked(String idAssociation) throws JadePersistenceException {
        StringBuilder queryLienTiers = new StringBuilder();
        queryLienTiers.append("SELECT HTITIP FROM SCHEMA.TICTIEP WHERE HTITIE =").append(idAssociation)
                .append(" AND HGTTLI = ").append(ASSOCIATION_CPP).append(" AND HGDFRE = 0");

        return DBUtil.executeQuery(queryLienTiers.toString(), DecompteRepositoryJade.class);

    }

    private String resolveIDRubrique(String idRubrique, String idAssociation) throws JadePersistenceException {
        StringBuilder queryRubrique = new StringBuilder();
        queryRubrique.append("SELECT ID_CARUBRP").append(" FROM SCHEMA.PT_COTISATIONS_AP")
                .append(" WHERE ID_TIADMIP = ").append(idAssociation).append(" AND CS_GENRE=").append(getTypeMembre())
                .append(" AND CS_FACTURER_DEFAUT =")
                .append(CategorieFactureAssociationProfessionnelle.SOLDE_MINIME.getValue());

        ArrayList<HashMap<String, Object>> result = DBUtil.executeQuery(queryRubrique.toString(),
                DecompteRepositoryJade.class);

        if (!result.isEmpty()) {
            // On prend le premier résultat de la liste et on récupère la bonne rubrique
            Map<String, Object> mapRubriqueAssociee = result.get(0);
            return String.valueOf(mapRubriqueAssociee.get("ID_CARUBRP"));
        }
        return idRubrique;
    }

    /**
     * @return
     */
    private JournalConteneur createJournal() {
        try {

            if (!JadeDateUtil.isGlobazDate(journalDate)) {
                journalDate = Date.now().getSwissValue();
            }

            if (JadeStringUtil.isEmpty(journalLibelle)) {
                journalLibelle = LIBELLE_JOURNAL_DEFAULT;
            }
            JournalSimpleModel journal = CABusinessServiceLocator.getJournalService().createJournal(journalLibelle,
                    journalDate);

            JournalConteneur jc = new JournalConteneur();
            jc.AddJournal(journal);

            // Commit le journal car si le premier cas plante, le journal ne sera pas créé
            JadeThread.commitSession();
            return jc;
        } catch (Exception e2) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e2);
        }
    }

    @Override
    protected String getEMailObject() {
        if (GenreCotisationAssociationProfessionnelle.MEMBRE.getValue().equals(getTypeMembre())) {
            return getSession().getLabel(DocumentConstants.ANNULATION_SOLDE_MINIME_TITLE);
        }
        return getSession().getLabel(DocumentConstants.ANNULATION_SOLDE_MINIME_NON_MEMBRE_TITLE);
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * @return the journalLibelle
     */
    public String getJournalLibelle() {
        return journalLibelle;
    }

    /**
     * @param journalLibelle the journalLibelle to set
     */
    public void setJournalLibelle(String journalLibelle) {
        this.journalLibelle = journalLibelle;
    }

    /**
     * @return the journalDate
     */
    public String getJournalDate() {
        return journalDate;
    }

    /**
     * @param journalDate the journalDate to set
     */
    public void setJournalDate(String journalDate) {
        this.journalDate = journalDate;
    }

    /**
     * @return the montantMinime
     */
    public String getMontantMinime() {
        return montantMinime;
    }

    /**
     * @param montantMinime the montantMinime to set
     */
    public void setMontantMinime(String montantMinime) {
        this.montantMinime = montantMinime;
    }

    /**
     * @return the simulation
     */
    public boolean isSimulation() {
        return simulation;
    }

    public boolean getSimulation() {
        return simulation;
    }

    /**
     * @param simulation the simulation to set
     */
    public void setSimulation(boolean simulation) {
        this.simulation = simulation;
    }

    public String getTypeMembre() {
        return typeMembre;
    }

    public void setTypeMembre(String typeMembre) {
        this.typeMembre = typeMembre;
    }
}
