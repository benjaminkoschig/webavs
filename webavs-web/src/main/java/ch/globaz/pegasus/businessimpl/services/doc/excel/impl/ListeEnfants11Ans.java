package ch.globaz.pegasus.businessimpl.services.doc.excel.impl;

import ch.globaz.common.domaine.Date;

import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCApiAvsAi;
import ch.globaz.pegasus.business.exceptions.doc.DocException;
import ch.globaz.pegasus.business.models.renteijapi.RenteIjApi;
import ch.globaz.pegasus.business.models.renteijapi.RenteIjApiSearch;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenusDepenses;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenusDepensesSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.doc.excel.models.ListeEnfant11Model;
import ch.globaz.queryexec.bridge.jade.SCM;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.op.common.merge.IMergingContainer;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.webavs.common.CommonExcelmlContainer;

import java.text.SimpleDateFormat;
import java.util.*;

import static ch.globaz.pegasus.business.constantes.IPCDroits.*;

public class ListeEnfants11Ans extends PegasusAbstractExcelServiceImpl {
    public final String MODEL_NAME = "listeEcheanceEnfants11Ans.xml";
    private final boolean PERIODE_OUVERT = false;
    private String outPutName = "liste_enfants_11ans";
    private String anneeDebut = null;
    private String moisDebut = null;
    private String anneeFin = null;
    private String moisFin = null;
    String ID_TIERS_DEBUG = "";

    private BSession session = null;

    @Override
    public String getModelName() {
        return MODEL_NAME;
    }

    @Override
    public String getOutPutName() {
        return null;
    }

    @Override
    public IMergingContainer loadResults() throws JadePersistenceException, JadeApplicationException {
        // container de data pour l'excel
        CommonExcelmlContainer container = new CommonExcelmlContainer();

        List<ListeEnfant11Model> list = createSource();
        Map<String, ListeEnfant11Model> listTri = new HashMap<>();

        float montantRevenu;
        float fraisGarde;
        for (ListeEnfant11Model model : list) {
            RenteIjApiSearch search = new RenteIjApiSearch();
            search.setForIdDroit(model.getIdDroit());
            search.setForNumeroVersion(model.getIdVersion());
            search.setForIsSupprime(false);
            search.setWhereKey("forVersionedRenteAvsAi");
            List<String> inCsTypeDonneeFinancierer = new ArrayList<String>();
            inCsTypeDonneeFinancierer.add(IPCApiAvsAi.CS_TYPE_DONNEE_FINANCIERE);
            inCsTypeDonneeFinancierer.add(IPCDroits.CS_AUTRES_RENTES);
            inCsTypeDonneeFinancierer.add(IPCDroits.CS_RENTE_AVS_AI);
            search.setInCsTypeDonneeFinancierer(inCsTypeDonneeFinancierer);
            search = PegasusServiceLocator.getDroitService().searchRenteIjApi(search);
            RenteIjApi renteIjApi;
            for (JadeAbstractModel modelRentes : search.getSearchResults()) {
                renteIjApi = (RenteIjApi) modelRentes;
                if (renteIjApi.getMembreFamilleEtendu().getDroitMembreFamille().getSimpleDroitMembreFamille().getCsRoleFamillePC().equals(CS_ROLE_FAMILLE_ENFANT)) {
                    if ((!JadeStringUtil.isBlankOrZero(renteIjApi.getSimpleAutreRente().getIdDonneeFinanciereHeader())
                            || !JadeStringUtil.isBlankOrZero(renteIjApi.getSimpleRenteAvsAi().getIdDonneeFinanciereHeader()))
                            && renteIjApi.getSimpleDonneeFinanciereHeader().getIsPeriodeClose() == PERIODE_OUVERT) {
                        RevenusDepensesSearch searchRevenu = new RevenusDepensesSearch();
                        searchRevenu.setForIdDroit(model.getIdDroit());
                        searchRevenu.setForNumeroVersion(model.getIdVersion());
                        searchRevenu.setWhereKey("forVersionedRevenuActiviteLucrativeDependante");
                        searchRevenu = PegasusServiceLocator.getDroitService().searchRevenusDepenses(searchRevenu);
                        montantRevenu = 0.f;
                        fraisGarde = 0.f;
                        for (JadeAbstractModel modelRevenu : searchRevenu.getSearchResults()) {
                            RevenusDepenses donnee = (RevenusDepenses) modelRevenu;
                            if ((donnee.getMembreFamilleEtendu().getDroitMembreFamille().getSimpleDroitMembreFamille().getCsRoleFamillePC().equals(CS_ROLE_FAMILLE_REQUERANT)
                                    || donnee.getMembreFamilleEtendu().getDroitMembreFamille().getSimpleDroitMembreFamille().getCsRoleFamillePC().equals(CS_ROLE_FAMILLE_CONJOINT))
                                    && JadeStringUtil.isBlankOrZero(donnee.getSimpleDonneeFinanciereHeader().getDateFin())) {
                                montantRevenu += Float.valueOf(donnee.getRevenuActiviteLucrativeDependante().getSimpleRevenuActiviteLucrativeDependante().getMontantActiviteLucrative());
                            }
                        }
                        searchRevenu.setWhereKey("forVersionedFraisGarde");
                        searchRevenu = PegasusServiceLocator.getDroitService().searchRevenusDepenses(searchRevenu);
                        for (JadeAbstractModel modelRevenu : searchRevenu.getSearchResults()) {
                            RevenusDepenses donnee = (RevenusDepenses) modelRevenu;
                            if ((donnee.getMembreFamilleEtendu().getDroitMembreFamille().getSimpleDroitMembreFamille().getCsRoleFamillePC().equals(CS_ROLE_FAMILLE_ENFANT)
                                    && JadeStringUtil.isBlankOrZero(donnee.getSimpleDonneeFinanciereHeader().getDateFin()))) {
                                fraisGarde += Float.valueOf(donnee.getSimpleFraisGarde().getMontant());
                            }
                        }
                        model.setMontantRevenusDep(String.valueOf(montantRevenu));
                        model.setMontantFraisGarde(String.valueOf(fraisGarde));
                        listTri.put(model.getIdDroit() + model.getIdVersion() + model.getNssRequerant() + model.getNssEnfant(), model);
                    }
                }
            }
        }
        ListeEnfant11Model model;
        List<String> listKey = new ArrayList<>(listTri.keySet());

        listKey.sort(Comparator.comparing((String t) -> listTri.get(t).getNomRequerant()));

        container.put("libelleTitre", session.getLabel("JSP_TITRE_ECHEANCE_ENFANTS_11_ANS"));
        container.put("user", session.getUserName());
        for (String key : listKey) {
            model = listTri.get(key);
            try {
                    String finalDate = JadeDateUtil.getLastDateOfMonth("01."
                            + moisFin + "." + anneeFin);
                    java.util.Date date = new SimpleDateFormat("dd.MM.yyyy").parse(finalDate);
                    String communePolitique = PRTiersHelper.getCommunePolitiqueNom(model.getIdTiers(), date, session);

                    container.put("commune_politique", communePolitique);
            } catch (Exception e) {
                new DocException("Unable to search CommunePolitique by idTiers", e);
            }
            container.put("nss", model.getNssRequerant());
            container.put("nom", model.getNomRequerant() + " " + model.getPrenomRequerant());
            container.put("nss_enfant", model.getNssEnfant());
            container.put("nom_enfant", model.getNomEnfant() + " " + model.getPrenomEnfant());
            container.put("date_naissance", new Date(model.getDateNaissance()).getSwissValue());
            container.put("montant_revenu", model.getMontantRevenusDep());
            container.put("frais_garde", model.getMontantFraisGarde());
        }


        return container;
    }

    private List<ListeEnfant11Model> createSource() throws DocException {
        List<ListeEnfant11Model> list;
        list = SCM.newInstance(ListeEnfant11Model.class).session(getSession()).query(getSQLCasEnfants()).execute();
        return list;

    }

    public ExcelmlWorkbook createDoc(String dateDebut, String dateFin) throws DocException {

        if (JadeStringUtil.isBlankOrZero(dateDebut) || JadeStringUtil.isBlankOrZero(dateFin)) {
            throw new DocException("Unable to execute createDoc, dateDebut or dateFin are null!");
        }
        moisDebut = dateDebut.split("\\.")[0];
        anneeDebut = dateDebut.split("\\.")[1];
        moisFin = dateFin.split("\\.")[0];
        anneeFin = dateFin.split("\\.")[1];
        return this.createDoc();
    }

    public String createDocAndSave(String dateDebut, String dateFin) throws Exception {
        ExcelmlWorkbook wk = this.createDoc(dateDebut, dateFin);
        String nomDoc = outPutName + "_" + JadeUUIDGenerator.createStringUUID() + ".xls";
        return save(wk, nomDoc);
    }

    private String getSQLCasEnfants() throws DocException {
        SimpleDateFormat dateFormater = new SimpleDateFormat("dd.MM.yyyy");
        Calendar dateDebutPourAvoir11ans = getDatePourAvoir11ans(moisDebut, anneeDebut);
        Calendar dateFinPourAvoir11ans = getDatePourAvoir11ans(moisFin, anneeFin);
        Date dateDebutNaissance = new Date(JadeDateUtil.getFirstDateOfMonth(dateFormater.format(dateDebutPourAvoir11ans.getTime())));
        Date dateFinNaissance = new Date(JadeDateUtil.getLastDateOfMonth(dateFormater.format(dateFinPourAvoir11ans.getTime())));

        if (dateDebutNaissance.after(dateFinNaissance)) {
            throw new DocException("Unable to execute getSQLCasEnfants, dateDebut > dateFin !");
        }

        //TODO : À ajouter les frais de garde une fois l'exigence 7 est claire.
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("tiersRequerant.HTITIE AS ID_TIERS, ");
        sql.append("dossiers.BAIDOS AS ID_DOSSIER, ");
        sql.append("droits.BCIDRO AS ID_DROIT, ");
        sql.append("MAX(versionDroits.BDIVDR) AS ID_VERSION, ");
        sql.append("demandesPC.BBDDEB AS  DATE_DEBUT_DEMANDE, ");
        sql.append("demandesPC.BBDFIN AS  DATE_FIN_DEMANDE, ");
        sql.append("noAVSRequerant.HXNAVS AS NSS_REQUERANT, ");
        sql.append("tiersRequerant.HTLDE1 AS NOM_REQUERANT, ");
        sql.append("tiersRequerant.HTLDE2 AS PRENOM_REQUERANT, ");
        sql.append("relationDroitMembreFamilles.BETROF as TYPE_LIEN, ");
        sql.append("noAVSEnfantConjoint.HXNAVS AS NSS_ENFANT, ");
        sql.append("tiersEnfantConjoint.HTLDE1 AS NOM_ENFANT, ");
        sql.append("tiersEnfantConjoint.HTLDE2 AS PRENOM_ENFANT, ");
        sql.append("personneEnfantConjoint.HPDNAI AS DATE_NAISSANCE ");
        sql.append("FROM SCHEMA.PCDOSSI dossiers ");
        sql.append("INNER JOIN SCHEMA.PCDEMPC demandesPC ON (  demandesPC.BBIDOS=dossiers.BAIDOS ) ");
        sql.append("LEFT OUTER JOIN SCHEMA.PCDROIT droits ON ( droits.BCIDPC=demandesPC.BBIDPC) ");
        sql.append("LEFT OUTER JOIN SCHEMA.PCVERDRO versionDroits ON ( droits.BCIDRO=versionDroits.BDIDRO ) ");
        sql.append("LEFT OUTER JOIN SCHEMA.PRDEMAP demandesPrestation ON ( dossiers.BAIDEM=demandesPrestation.WAIDEM ) ");
        sql.append("LEFT OUTER JOIN SCHEMA.PCDRMBRFA relationDroitMembreFamilles ON ( relationDroitMembreFamilles.BEIDRO=droits.BCIDRO  ) ");
        sql.append("LEFT JOIN SCHEMA.SFMBRFAM membresFamille ON ( relationDroitMembreFamilles.BEIMEF=membresFamille.WGIMEF) ");
        sql.append("LEFT JOIN SCHEMA.TIPAVSP noAVSRequerant ON ( demandesPrestation.WAITIE=noAVSRequerant.HTITIE ) ");
        sql.append("LEFT JOIN SCHEMA.TITIERP tiersRequerant ON ( demandesPrestation.WAITIE=tiersRequerant.HTITIE ) ");
        sql.append("LEFT JOIN SCHEMA.TIPAVSP noAVSEnfantConjoint ON ( membresFamille.WGITIE=noAVSEnfantConjoint.HTITIE) ");
        sql.append("LEFT JOIN SCHEMA.TIPERSP personneEnfantConjoint ON ( noAVSEnfantConjoint.HTITIE=personneEnfantConjoint.HTITIE ) ");
        sql.append("LEFT JOIN SCHEMA.TITIERP tiersEnfantConjoint ON ( noAVSEnfantConjoint.HTITIE=tiersEnfantConjoint.HTITIE ) ");
        sql.append("LEFT OUTER JOIN SCHEMA.TICTIEP TICTIEP1 ON ");
        sql.append("( ");
        sql.append("tiersRequerant.HTITIE=TICTIEP1.HTITIP ");
        sql.append("AND TICTIEP1.HGTTLI=507007 ");
        sql.append(") ");
        sql.append("WHERE ");
        sql.append("(demandesPC.BBDFIN = 0 OR demandesPC.BBTETD='64001003') ");
        sql.append("AND ((personneEnfantConjoint.HPDNAI>=");
        sql.append(dateDebutNaissance.getValue() + " ");
        sql.append("AND personneEnfantConjoint.HPDNAI<= ");
        sql.append(dateFinNaissance.getValue() + " ");
        sql.append(")) ");
        if(!ID_TIERS_DEBUG.isEmpty()){
            sql.append("AND  tiersRequerant.HTITIE=" + ID_TIERS_DEBUG );
        }
        sql.append(" GROUP BY tiersRequerant.HTITIE,dossiers.BAIDOS,droits.BCIDPC,demandesPC.BBDDEB,demandesPC.BBDFIN,droits.BCIDRO,noAVSRequerant.HXNAVS,tiersRequerant.HTLDE1,tiersRequerant.HTLDE2,membresFamille.WGITIE,noAVSEnfantConjoint.HXNAVS, tiersEnfantConjoint.HTLDE1, tiersEnfantConjoint.HTLDE2, personneEnfantConjoint.HPDNAI,relationDroitMembreFamilles.BETROF");
        return sql.toString();
    }

    /**
     * Méthode permettant de récupérer la date des 11 ans.
     * @param mois
     * @param annee
     * @return dateDes11ans
     */
    private Calendar getDatePourAvoir11ans(String mois, String annee) {
        Calendar moisCourant = Calendar.getInstance();
        moisCourant.set(Calendar.MONTH, Integer.parseInt(mois) - 1);
        moisCourant.set(Calendar.YEAR, Integer.parseInt(annee));
        Calendar datePourAvoir11ans = (Calendar) moisCourant.clone();
        datePourAvoir11ans.add(Calendar.YEAR, -11);
        return datePourAvoir11ans;
    }

    public BSession getSession() {
        return session;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

}
