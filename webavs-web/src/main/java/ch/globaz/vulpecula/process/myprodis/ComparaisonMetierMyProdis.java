package ch.globaz.vulpecula.process.myprodis;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import com.google.common.collect.Collections2;
import ch.globaz.common.sql.QueryExecutor;
import ch.globaz.vulpecula.businessimpl.services.myprodis.ComparaisonMyProdisSalaireCP;
import ch.globaz.vulpecula.businessimpl.services.myprodis.ComparaisonMyProdisService;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.external.BProcessWithContext;
import globaz.framework.util.FWMessage;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.common.Jade;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;

public class ComparaisonMetierMyProdis extends BProcessWithContext {

    private String dateDebut;
    private String dateFin;
    private String dateDebutForSalaire;
    private String dateFinForSalaire;
    private String fileName;
    transient ComparaisonMetierMyProdisDifferenceExcel excelDoc;
    transient Collection<ComparaisonMyProdisSalaireCP> salairesFiltre;
    transient Collection<ComparaisonMyProdisSalaireCP> cpFiltre;
    transient Map<String, List<ComparaisonMyProdisSalaireCP>> mapSalaireRegroupeParNSS;
    transient List<ComparaisonMyProdisSalaireCP> listFromExcelFile;
    transient ComparaisonMyProdisService serviceMyProdis;
    private Boolean wantControleCP;
    private Boolean wantControleSalaires;

    /**
     *
     */
    private static final long serialVersionUID = -5916969518058690324L;

    @Override
    protected String getEMailObject() {
        return DocumentConstants.LISTES_COMPARAISON_MYPRODIS_DOC_NAME;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            initService();
            if (wantControleSalaires) {
                initSalaires();
            }
            if (wantControleCP) {
                initCP();
            }
            initFromCsv();

            List<ComparaisonMyProdisSalaireCP> listToPrint = serviceMyProdis.compareList(mapSalaireRegroupeParNSS,
                    listFromExcelFile);

            Collections.sort(listToPrint);
            printListOfDifferencesExcel(listToPrint);
        } catch (IOException e) {
            JadeLogger.error(e, e.getMessage());
        }

        return true;
    }

    private void initService() {
        serviceMyProdis = new ComparaisonMyProdisService();
        serviceMyProdis.setSession(getSession());
        mapSalaireRegroupeParNSS = new HashMap<String, List<ComparaisonMyProdisSalaireCP>>();
        // Metre à jour les périodes
        miseAJourPeriodeForSalaire();
    }

    public void miseAJourPeriodeForSalaire() {
        dateDebutForSalaire = serviceMyProdis.determineDateDebutForSalaire(dateDebut, dateFin);
        dateFinForSalaire = serviceMyProdis.determineDateFinForSalaire(dateDebut, dateFin);

    }

    private void initFromCsv() throws Exception {
        InputStream flux = new FileInputStream(resolveFileName());
        // InputStream flux = new FileInputStream("x:/jmc/Liste 1 salaires CAPAV_20170201_20180131.csv");
        InputStreamReader lecture = new InputStreamReader(flux);
        BufferedReader buff = new BufferedReader(lecture);
        listFromExcelFile = new ArrayList<ComparaisonMyProdisSalaireCP>();
        String line;
        // On saute la ligne d'entête
        line = buff.readLine();
        int i = 2;
        while ((line = buff.readLine()) != null) {

            try {
                i++;
                StringTokenizer token = new StringTokenizer(line);
                List<String> listeToken = new ArrayList<String>();
                while (token.hasMoreElements()) {
                    listeToken.add(token.nextToken(";"));
                }
                if (!wantControleSalaires) {
                    if ("S".equals(serviceMyProdis.cpOrSalaire(listeToken.get(41)))) {
                        continue;
                    }
                }
                if (!wantControleCP) {
                    if ("C".equals(serviceMyProdis.cpOrSalaire(listeToken.get(41)))) {
                        continue;
                    }
                }
                ComparaisonMyProdisSalaireCP obj = new ComparaisonMyProdisSalaireCP();
                obj.setDateDebut(serviceMyProdis.transformeDate(listeToken.get(9)));
                obj.setDateFin(serviceMyProdis.transformeDate(listeToken.get(10)));
                obj.setSalaire(JANumberFormatter.deQuote(listeToken.get(11)));
                obj.setNss(listeToken.get(7));
                String noAffilie = serviceMyProdis.transformeNoAffilie(listeToken.get(3));
                obj.setNoAffilie(noAffilie);
                obj.setCp(serviceMyProdis.cpOrSalaire(listeToken.get(41)));
                obj.setSource("myProdis");
                if (serviceMyProdis.validateBeanEnr(obj)) {
                    listFromExcelFile.add(obj);
                } else {
                    getMemoryLog().logMessage("Problème dans le fichier : " + obj.toString(), FWMessage.AVERTISSEMENT,
                            this.getClass().getName());
                }
            } catch (Exception e) {
                // On log et on continue
                getMemoryLog().logMessage("Problème dans le fichier :  Erreur à la ligne : " + i,
                        FWMessage.AVERTISSEMENT, this.getClass().getName());

            }
        }
        flux.close();
        buff.close();

    }

    private void initSalaires() {

        List<ComparaisonMyProdisSalaireCP> salaires = QueryExecutor.execute(getSql(),
                ComparaisonMyProdisSalaireCP.class, getSession());
        salairesFiltre = Collections2.filter(salaires, serviceMyProdis.returnPredicate());

        for (ComparaisonMyProdisSalaireCP salaire : salairesFiltre) {
            // Regrouper par nss
            putInMap(salaire);
        }

    }

    private void initCP() {
        List<ComparaisonMyProdisSalaireCP> cps = QueryExecutor.execute(getSqlForCP(),
                ComparaisonMyProdisSalaireCP.class, getSession());
        cpFiltre = Collections2.filter(cps, serviceMyProdis.returnPredicateCP());
        for (ComparaisonMyProdisSalaireCP cp : cpFiltre) {
            // Regrouper par nss
            putInMap(cp);
        }

    }

    private void putInMap(ComparaisonMyProdisSalaireCP salaire) {
        if (!serviceMyProdis.validateBeanEnr(salaire)) {
            getMemoryLog().logMessage("Problème en base de données : " + salaire.toString(), FWMessage.AVERTISSEMENT,
                    this.getClass().getName());
            return;
        }
        List<ComparaisonMyProdisSalaireCP> salairesParNss;
        if (mapSalaireRegroupeParNSS.containsKey(salaire.getNss())) {
            salairesParNss = mapSalaireRegroupeParNSS.get(salaire.getNss());
            mapSalaireRegroupeParNSS.remove(salaire.getNss());
        } else {
            salairesParNss = new ArrayList<ComparaisonMyProdisSalaireCP>();
        }
        salairesParNss.add(salaire);
        mapSalaireRegroupeParNSS.put(salaire.getNss(), salairesParNss);
    }

    /**
     * Consitution du chemin où reprendre le fichier
     *
     * @return Le chemin complet du fichier
     */
    private String resolveFileName() throws Exception {
        JadeFsFacade.copyFile("jdbc://" + Jade.getInstance().getDefaultJdbcSchema() + "/" + getFileName(),
                Jade.getInstance().getHomeDir() + "work/" + getFileName());
        return Jade.getInstance().getHomeDir() + "work/" + getFileName();
    }

    /**
     * Procédure permetant d'insérer la liste des différences de décomptes
     * dans une liste Excel.
     *
     * @param listeDif Liste des décomptes différents entre WebMetier et MyProdis
     * @throws IOException
     */
    private void printListOfDifferencesExcel(List<ComparaisonMyProdisSalaireCP> listeDif) throws IOException {
        excelDoc = new ComparaisonMetierMyProdisDifferenceExcel(getSession(),
                DocumentConstants.LISTES_COMPARAISON_MYPRODIS_DOC_NAME,
                DocumentConstants.LISTES_COMPARAISON_MYPRODIS_NAME, listeDif);
        excelDoc.setWantCP(wantControleCP);
        excelDoc.setWantSalaire(wantControleSalaires);
        excelDoc.setFilename(getFileName());
        excelDoc.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), excelDoc.getOutputFile());
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    private String getSql() {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select dl.id as id,hxnavs as nss , malnaf as noAffilie, dl.PERIODE_DEBUT as dateDebut , dl.PERIODE_FIN as dateFin, dl.SALAIRE_TOTAL as salaire , 'S' as cp  from SCHEMA.PT_DECOMPTE_LIGNES dl");
        sql.append(" join SCHEMA.PT_DECOMPTES d on d.ID = dl.ID_PT_DECOMPTES");
        sql.append(" join SCHEMA.PT_POSTES_TRAVAILS pt on pt.ID = dl.ID_PT_POSTES_TRAVAIL");
        sql.append(" join SCHEMA.PT_TRAVAILLEURS t on pt.ID_PT_TRAVAILLEURS = t.ID");
        sql.append(" join SCHEMA.TIPAVSP p on p.HTITIE = t.ID_TITIERP");
        sql.append(" join SCHEMA.AFAFFIP af on af.MAIAFF = pt.ID_AFAFFIP");
        sql.append(" join SCHEMA.fapassp pass on pass.IDPASSAGE = d.ID_PASSAGE_FACTURATION");
        sql.append(" where SALAIRE_TOTAL <> 0 and d.CS_ETAT= 68012007 and DATEFACTURATION between ");
        sql.append(dateDebutForSalaire);
        sql.append(" and " + dateFinForSalaire);
        // + " fetch first 10000 rows only"
        // and d.cs_type = 68014001

        return sql.toString();
    }

    private String getSqlForCP() {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "SELECT cp.id as id, hxnavs as nss, malnaf as noAffilie, cp.TOTAL_SALAIRE as salaire, MONTANT_NET as montantNet,ANNEE_DEBUT as dateDebut, ANNEE_FIN as dateFin,'C' as cp, TAUX_CP as tauxCp, DATE_VERSEMENT as dateVersement, DATE_FIN_ACTIVITE as dateFinActivite, DATE_DEBUT_ACTIVITE as dateDebutPoste  FROM SCHEMA.PT_CONGES_PAYES cp ");
        sql.append(" join SCHEMA.PT_POSTES_TRAVAILS poste on cp.ID_PT_POSTES_TRAVAILS = poste.ID");
        sql.append(" join SCHEMA.PT_TRAVAILLEURS tra on tra.id = poste.ID_PT_TRAVAILLEURS");
        sql.append(" join SCHEMA.TIPAVSP pers on pers.htitie = tra.ID_TITIERP");
        sql.append(" join SCHEMA.afaffip af on poste.ID_AFAFFIP = af.maiaff");
        sql.append(" where DATE_VERSEMENT between ");
        sql.append(dateDebut);
        sql.append(" and " + dateFin);
        return sql.toString();
    }

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub

    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Boolean getWantControleCP() {
        return wantControleCP;
    }

    public void setWantControleCP(Boolean wantControleCP) {
        this.wantControleCP = wantControleCP;
    }

    public Boolean getWantControleSalaires() {
        return wantControleSalaires;
    }

    public void setWantControleSalaires(Boolean wantControleSalaires) {
        this.wantControleSalaires = wantControleSalaires;
    }

}
