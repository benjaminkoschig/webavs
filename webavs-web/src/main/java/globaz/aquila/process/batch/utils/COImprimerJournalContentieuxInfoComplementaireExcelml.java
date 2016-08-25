package globaz.aquila.process.batch.utils;

import globaz.aquila.db.access.batch.COEtape;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.framework.db.postit.FWNoteP;
import globaz.framework.db.postit.FWNotePManager;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.common.business.exceptions.CommonTechnicalException;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.sql.QueryExecutor;

public class COImprimerJournalContentieuxInfoComplementaireExcelml extends COAbstractJournalContentieuxExcelml {

    public static final String EXCELML_JOURNAL_CONTENTIEUX_MODEL_NAME = "JournalContentieuxInfoComplModele.xml";
    public static final String EXCELML_JOURNAL_CONTENTIEUX_OUTPUT_FILE_NAME = "JournalContentieux.xml";

    public static final String EXCELML_MODEL_COL_DECOMPTE_FINAL = "COL_DECOMPTE_FINAL";
    public static final String EXCELML_MODEL_COL_NBRE_PO_ENCOURS = "COL_NBRE_PO_ENCOURS";
    public static final String EXCELML_MODEL_COL_SECT_RECENTE_PAYE = "COL_SECT_RECENTE_PAYE";
    public static final String EXCELML_MODEL_COL_CREDIT_EXISTANT = "COL_CREDIT_EXISTANT";
    public static final String EXCELML_MODEL_COL_DATE_RADIATION = "COL_DATE_RADIATION";
    public static final String EXCELML_MODEL_COL_POSTIT_CA = "COL_POSTIT_CA";

    /**
     * @param theDateReference
     * @param theDateDocument
     * @param theModePrevisionnel
     * @param theListIdRoles
     */
    public COImprimerJournalContentieuxInfoComplementaireExcelml(String dateReference, String dateDocument,
            boolean modePrevisionnel, List<String> listIdRoles) {
        super(dateReference, dateDocument, modePrevisionnel, listIdRoles, EXCELML_JOURNAL_CONTENTIEUX_MODEL_NAME,
                EXCELML_JOURNAL_CONTENTIEUX_OUTPUT_FILE_NAME);
    }

    @Override
    protected void addRowInExcelmlInfoComplementaire(BSession session, COContentieux contentieux, COEtape etape) {

        try {
            Map<String, String> infosSupp = new HashMap<String, String>();
            CASection section = contentieux.getSection();
            CACompteAnnexe compteAnnexe = (CACompteAnnexe) section.getCompteAnnexe();

            String numeroAffilie = compteAnnexe.getIdExterneRole();
            String idCompteAnnexe = section.getIdCompteAnnexe();
            String categorie = compteAnnexe.getIdCategorie();
            String idTiers = compteAnnexe.getIdTiers();
            Date date = new Date(contentieux.getDateOuverture());
            AFAffiliation affiliation = retrieveAffiliation(session, numeroAffilie, idCompteAnnexe, idTiers, categorie);

            // Le d�compte final (paritaires) a �t� �tabli pour l'ann�e concern�e par la future section en PO ?
            infosSupp.put(
                    EXCELML_MODEL_COL_DECOMPTE_FINAL,
                    parseReponseBoolean(
                            session,
                            hasDecompteFinalEtabli(session, numeroAffilie, affiliation.getAffiliationId(),
                                    date.getAnnee())));

            // Le nombre de poursuite en cours
            infosSupp.put(EXCELML_MODEL_COL_NBRE_PO_ENCOURS, findNombrePOEnCours(session, idCompteAnnexe).toString());

            // Si une section plus r�cente est-elle pay� ?
            infosSupp.put(
                    EXCELML_MODEL_COL_SECT_RECENTE_PAYE,
                    parseReponseBoolean(session,
                            hasSectionRecentePayee(session, idCompteAnnexe, section.getDateSection(), numeroAffilie)));

            // Oui si un cr�dit est existant autrement vide.
            infosSupp.put(EXCELML_MODEL_COL_CREDIT_EXISTANT,
                    parseReponseBoolean(session, hasCreditForCompteAnnexe(session, idCompteAnnexe, numeroAffilie)));

            // La date de radiation de l'affiliation
            infosSupp.put(EXCELML_MODEL_COL_DATE_RADIATION, resolveDateFinAffiliation(affiliation));

            // Le texte du post-it sur le compte annexe
            infosSupp.put(EXCELML_MODEL_COL_POSTIT_CA, findPostItDescription(session, idCompteAnnexe, numeroAffilie));

            for (Map.Entry<String, String> entry : infosSupp.entrySet()) {
                getContainerJournalContentieuxExcelml().put(entry.getKey(), entry.getValue());
            }

        } catch (Exception e) {

            // Remplissage du container par vide car une exception a �t� lev�e
            // Si on le fait pas, il y a un d�calage dans le fichier de sortie (Fonctionnement xmlml)
            getContainerJournalContentieuxExcelml().put(EXCELML_MODEL_COL_DECOMPTE_FINAL, "");
            getContainerJournalContentieuxExcelml().put(EXCELML_MODEL_COL_NBRE_PO_ENCOURS, "");
            getContainerJournalContentieuxExcelml().put(EXCELML_MODEL_COL_SECT_RECENTE_PAYE, "");
            getContainerJournalContentieuxExcelml().put(EXCELML_MODEL_COL_DATE_RADIATION, "");
            getContainerJournalContentieuxExcelml().put(EXCELML_MODEL_COL_POSTIT_CA, "");

            throw new CommonTechnicalException(e);
        }

    }

    /**
     * Permet de transcrire la r�ponse "bool�enne" dans le format voulu par le client.
     * 
     * @param session
     * @param bool
     * @return
     */
    private String parseReponseBoolean(BSession session, Boolean bool) {
        if (bool) {
            return session.getLabel("OUI");
        } else {
            return "";
        }
    }

    /**
     * Test si un d�compte finale a �t� �tabli pour l'affili�.
     * 
     * @param session
     * @param numeroAffilie
     * @param idAffiliation
     * @param annee
     * @return
     */
    private boolean hasDecompteFinalEtabli(BSession session, String numeroAffilie, String idAffiliation, String annee) {

        DSDeclarationListViewBean declarationManager = new DSDeclarationListViewBean();
        declarationManager.setSession(session);
        declarationManager.setForAffiliationId(idAffiliation);
        declarationManager.setForAnnee(new Integer(annee).toString());
        declarationManager.setInTypeDeclaration(Arrays.asList(DSDeclarationViewBean.CS_BOUCLEMENT_ACOMPTE,
                DSDeclarationViewBean.CS_PRINCIPALE));
        declarationManager.setForEtat(DSDeclarationViewBean.CS_COMPTABILISE);

        try {
            return declarationManager.getCount() > 0;
        } catch (Exception e) {
            throw new CommonTechnicalException("Impossible de rechercher si l'affili� " + numeroAffilie
                    + " a un d�compte final �tabli", e);
        }
    }

    /**
     * Recherche d'une section plus r�cente pay�.
     * Cela implique de rechercher une section du compte annexe avec une date plus �lev� dont le montant est �gal � 0.
     * 
     * @param session
     * @param idCompteAnnexe
     * @param dateSection
     * @param numeroAffilie
     * @return
     */
    private boolean hasSectionRecentePayee(BSession session, String idCompteAnnexe, String dateSection,
            String numeroAffilie) {

        CASectionManager manager = new CASectionManager();
        manager.setSession(session);
        manager.setForIdCompteAnnexe(idCompteAnnexe);
        manager.setFromDate(dateSection);
        manager.setForSolde("0");

        try {
            return manager.getCount() > 0;
        } catch (Exception e) {
            throw new CommonTechnicalException("Impossible de rechercher si le compte annexe de l'affili� "
                    + numeroAffilie + " a des sections n�gatives", e);
        }

    }

    /**
     * Recherche d'un cr�dit pour le compte annexe.
     * Cela implique de trouver une section du compte annexe avec un montant sup�rieur � 0
     * 
     * @param session
     * @param idCompteAnnexe
     * @param numeroAffilie
     * @return
     */
    private boolean hasCreditForCompteAnnexe(BSession session, String idCompteAnnexe, String numeroAffilie) {

        CASectionManager manager = new CASectionManager();
        manager.setSession(session);
        manager.setForIdCompteAnnexe(idCompteAnnexe);
        manager.setForSoldeSmallerThanZero(true);

        try {
            return manager.getCount() > 0;
        } catch (Exception e) {
            throw new CommonTechnicalException("Impossible de rechercher si le compte annexe de l'affili� "
                    + numeroAffilie + " a des sections n�gatives", e);
        }
    }

    /**
     * Recherche des post-it du compte annexe
     * 
     * @param session
     * @param idCompteAnnexe
     * @param numeroAffilie
     * @return
     */
    private String findPostItDescription(BSession session, String idCompteAnnexe, String numeroAffilie) {

        FWNotePManager manager = new FWNotePManager();
        manager.setSession(session);
        manager.setForTableSource("CACPTAP");
        manager.setForSourceId(idCompteAnnexe);

        StringBuilder postIt = new StringBuilder();
        try {
            manager.find(BManager.SIZE_NOLIMIT);

            if (manager.size() > 0) {
                for (int i = 0; i < manager.size(); i++) {
                    FWNoteP note = (FWNoteP) manager.get(i);
                    if (!JadeStringUtil.isEmpty(postIt.toString())) {
                        postIt.append("\n");
                    }
                    postIt.append(note.getDescription());
                }
            }

            return postIt.toString();

        } catch (Exception e) {
            throw new CommonTechnicalException(
                    "Impossible de r�cup�rer les post-i pour le compte annexe de l'affili� : " + numeroAffilie, e);
        }

    }

    /**
     * Recherche du nombre de PO en cours pour le compte annexe
     * 
     * @param session
     * @param idCompteAnnexe
     * @return
     */
    private BigDecimal findNombrePOEnCours(BSession session, String idCompteAnnexe) {

        //@formatter:off
        String req = "SELECT count(*) "
                + "FROM schema.casectp section "
                + "inner join schema.COCAVSP ctx on ctx.OAISEC = section.IDSECTION "
                + "inner join schema.COHISTP ctxh on (ctxh.OAICON = ctx.OAICON and ctxh.OEBANN='2' ) "
                + "where ctxh.odieta = (SELECT ODIETA FROM schema.COETAPP where ODTETA=5200001 and OFISEQ=1) "
                + "and section.SOLDE <> 0 "
                + "and section.IDCOMPTEANNEXE = "+ idCompteAnnexe + " ";
                //+ "--and ctx.oaicon <> 40357";
        //@formatter:on

        return QueryExecutor.executeAggregate(req, session);
    }

    /**
     * R�cup�ration de l'affiliation par son num�ro d'affili�, l'idTiers et le type d'affiliation
     * 
     * @param session
     * @param numeroAffilie
     * @param idCompteAnnexe
     * @param idTiers
     * @param categorie
     * @return
     */
    private AFAffiliation retrieveAffiliation(BSession session, String numeroAffilie, String idCompteAnnexe,
            String idTiers, String categorie) {
        AFAffiliationManager affiliationManager = new AFAffiliationManager();

        affiliationManager.setForAffilieNumero(numeroAffilie);
        affiliationManager.setForIdTiers(idTiers);
        affiliationManager.setSession(session);
        String typeAffiliation[] = { categorie };
        affiliationManager.setForTypeAffiliation(typeAffiliation);

        try {
            affiliationManager.find(BManager.SIZE_USEDEFAULT);
        } catch (Exception e) {
            throw new CommonTechnicalException("Impossible de r�cup�rer l'affiliation pour le num�ro d'affili� : "
                    + numeroAffilie, e);
        }

        if (affiliationManager.size() > 0) {
            return (AFAffiliation) affiliationManager.getFirstEntity();
        } else {
            throw new CommonTechnicalException("Aucune affiliation pour le num�ro d'affili� : " + numeroAffilie);
        }
    }

    /**
     * Retourne une date si l'affiliation a une date de fin, vide sinon
     * 
     * @param compteAnnexe
     * @return
     */
    private String resolveDateFinAffiliation(AFAffiliation affiliation) {

        if (affiliation != null && !affiliation.isNew() && !JadeStringUtil.isBlankOrZero(affiliation.getDateFin())) {
            return affiliation.getDateFin();
        }

        return "";
    }
}
