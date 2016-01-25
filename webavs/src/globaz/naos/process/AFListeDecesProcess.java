package globaz.naos.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.listeDeces.AFListeDeces;
import globaz.naos.db.listeDeces.AFListeDecesBatchPrinter;
import globaz.naos.db.listeDeces.AFListeDecesManager;
import globaz.naos.db.listeDeces.AFListeDecesPrinter;
import globaz.pyxis.db.tiers.TIHistoriqueTiers;
import globaz.pyxis.db.tiers.TIHistoriqueTiersManager;
import globaz.pyxis.db.tiers.TIRole;
import globaz.pyxis.db.tiers.TIRoleManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AFListeDecesProcess extends BProcess {

    private static final long serialVersionUID = 7452010807001463139L;
    public static final int CODE_RETOUR_ERREUR = 200;
    public static final int CODE_RETOUR_OK = 0;

    private String dateDeces = "";

    boolean isJSProcess = false;

    public AFListeDecesProcess() {
    }

    public AFListeDecesProcess(BProcess parent) {
        super(parent);
    }

    public AFListeDecesProcess(BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        if (!JadeStringUtil.isBlankOrZero(dateDeces)) {
            executeJSProcess(dateDeces);
        } else {
            executeP();
        }
        return true;
    }

    private void executeAvecInterval() throws Exception {

        String lastExecution = "";
        String dateDeb = "";
        String idTiers = "";
        String dateDuJour = JACalendar.todayJJsMMsAAAA();

        AFListeDecesBatchPrinter batch = new AFListeDecesBatchPrinter();
        PrintWriter p = batch.createBatchPrinter(dateDuJour);

        String nss = "";
        String nom = "";
        String prenom = "";
        String dateNaissance = "";
        String dateDecesL = "";
        String adresseDomicile = "";

        List listDesAffiliations = new ArrayList();
        List listDesRoles = new ArrayList();

        TIHistoriqueTiersManager mgr = new TIHistoriqueTiersManager();
        mgr.setSession(getSession());
        mgr.setForChamp("7");
        mgr.find();

        if (mgr.size() > 0) {
            for (Iterator iter = mgr.iterator(); iter.hasNext();) {
                TIHistoriqueTiers ht = (TIHistoriqueTiers) iter.next();

                if (JadeStringUtil.isBlankOrZero(ht.getValeur())) {
                    continue;
                }

                idTiers = ht.getIdTiers();
                dateDeb = ht.getDateDebut(); // Date de modification du tiers
                dateDecesL = ht.getValeur(); // date de décès du tiers

                AFListeDecesManager dim = new AFListeDecesManager();
                dim.setSession(getSession());
                dim.setIdTiers(idTiers);
                dim.find();

                if (dim.size() > 0) {
                    for (Iterator t = dim.iterator(); t.hasNext();) {
                        AFListeDeces inf = (AFListeDeces) t.next();

                        lastExecution = inf.getLastExecutionDate();

                        if (BSessionUtil.compareDateFirstGreater(getSession(), dateDeb, lastExecution)) {

                            TITiersViewBean tiers = new TITiersViewBean();
                            tiers.setSession(getSession());
                            tiers.setIdTiers(idTiers);
                            tiers.retrieve();

                            nom = tiers.getDesignation1();
                            prenom = tiers.getDesignation2();
                            dateNaissance = tiers.getDateNaissance();
                            adresseDomicile = tiers.getAdresseAsString();
                            adresseDomicile = adresseDomicile.replaceAll("[\n]", " ");

                            AFAffiliationManager aff = new AFAffiliationManager();
                            aff.setSession(getSession());
                            aff.setForIdTiers(idTiers);
                            aff.find();

                            if (aff.size() > 0) {
                                for (Iterator iterator = aff.iterator(); iterator.hasNext();) {
                                    AFAffiliation affiliation = (AFAffiliation) iterator.next();
                                    listDesAffiliations.add(affiliation);
                                }
                            } else {
                                System.out.println("Pas d'affiliations pour ce tiers");
                            }

                            TIRoleManager roleMgr = new TIRoleManager();
                            roleMgr.setSession(getSession());
                            roleMgr.setForIdTiers(idTiers);
                            roleMgr.find();

                            if (roleMgr.size() > 0) {
                                for (Iterator it = roleMgr.iterator(); it.hasNext();) {
                                    TIRole role = (TIRole) it.next();
                                    listDesRoles.add(role);
                                }
                            } else {
                                System.out.println("Pas de role pour ce tiers");
                            }
                            StringBuffer buf = batch.printLine(getSession(), listDesAffiliations, listDesRoles, nss,
                                    nom, prenom, dateNaissance, dateDecesL, adresseDomicile, dateDuJour);
                            p.print(buf.toString());
                            p.flush();

                            listDesAffiliations.clear();
                            listDesRoles.clear();

                            AFListeDeces info = new AFListeDeces();
                            info.setSession(getSession());
                            info.setIdTiers(idTiers);
                            info.retrieve();

                            info.setLastExecutionDate(dateDuJour);
                            info.update();
                        } else {
                            continue;
                        }
                    }
                }
            }
        }
    }

    public boolean executeJSProcess(String dateDeces) throws Exception {

        isJSProcess = true;
        JadePublishDocumentInfo info = createDocumentInfo();
        AFListeDecesPrinter excelDoc = new AFListeDecesPrinter();
        File fichier = excelDoc.createFile(dateDeces);
        FileOutputStream out = new FileOutputStream(fichier);
        out.write(excelDoc.getHeader().getBytes());

        String idTiers = "";
        List listDesAffiliations = new ArrayList();
        List listDesRoles = new ArrayList();
        String nss = "";
        String nom = "";
        String prenom = "";
        String dateNaissance = "";
        String adresseDomicile = "";
        String dateDuJour = JACalendar.todayJJsMMsAAAA();

        TIHistoriqueTiersManager mgr = new TIHistoriqueTiersManager();
        mgr.setSession(getSession());
        mgr.setForChamp("7");
        mgr.setForDateDebutGreaterOrEqualTo(dateDeces);
        mgr.find();

        if (mgr.size() > 0) {
            for (Iterator iter = mgr.iterator(); iter.hasNext();) {
                TIHistoriqueTiers ht = (TIHistoriqueTiers) iter.next();

                if (!dateDeces.equals(ht.getValeur())) {
                    continue;
                }

                idTiers = ht.getIdTiers();

                TITiersViewBean tiers = new TITiersViewBean();
                tiers.setSession(getSession());
                tiers.setIdTiers(idTiers);
                tiers.retrieve();

                nss = tiers.getNumAvsActuel();
                nom = tiers.getDesignation1();
                prenom = tiers.getDesignation2();
                dateNaissance = tiers.getDateNaissance();
                adresseDomicile = tiers.getAdresseAsString();

                adresseDomicile = adresseDomicile.replaceAll("[\n]", " ");

                AFAffiliationManager aff = new AFAffiliationManager();
                aff.setSession(getSession());
                aff.setForIdTiers(idTiers);
                aff.find();

                if (aff.size() > 0) {
                    for (Iterator iterator = aff.iterator(); iterator.hasNext();) {
                        AFAffiliation affiliation = (AFAffiliation) iterator.next();
                        listDesAffiliations.add(affiliation);
                    }
                } else {
                    System.out.println("Pas d'affiliations pour ce tiers");
                }

                TIRoleManager roleMgr = new TIRoleManager();
                roleMgr.setSession(getSession());
                roleMgr.setForIdTiers(idTiers);
                roleMgr.find();

                if (roleMgr.size() > 0) {
                    for (Iterator t = roleMgr.iterator(); t.hasNext();) {
                        TIRole role = (TIRole) t.next();
                        listDesRoles.add(role);
                    }
                } else {
                    System.out.println("Pas de role pour ce tiers");
                }
                String ligne = excelDoc.getLine(getSession(), listDesAffiliations, listDesRoles, nss, nom, prenom,
                        dateNaissance, dateDeces, adresseDomicile, dateDeces);
                out.write(ligne.getBytes());
                listDesAffiliations.clear();
                listDesRoles.clear();
            }
        }
        out.close();
        this.registerAttachedDocument(info, fichier.getAbsolutePath());
        return true;
    }

    public void executeP() throws Exception {

        AFListeDecesManager dim = new AFListeDecesManager();
        dim.setSession(getSession());
        dim.changeManagerSize(0);
        dim.find();

        if (dim.size() > 0) {
            executeAvecInterval();
        } else {
            executeSansInterval();
        }
    }

    private void executeSansInterval() throws Exception {

        String dateDuJour = JACalendar.todayJJsMMsAAAA();

        AFListeDecesBatchPrinter batch = new AFListeDecesBatchPrinter();
        PrintWriter p = batch.createBatchPrinter(dateDuJour);

        String idTiers = "";
        String nss = "";
        String nom = "";
        String prenom = "";
        String dateNaissance = "";
        String dateDecesL = "";
        String adresseDomicile = "";

        List listDesAffiliations = new ArrayList();
        List listDesRoles = new ArrayList();

        TIHistoriqueTiersManager mgr = new TIHistoriqueTiersManager();
        mgr.setSession(getSession());
        mgr.setForChamp("7");
        mgr.find();

        if (mgr.size() > 0) {
            for (Iterator iter = mgr.iterator(); iter.hasNext();) {
                TIHistoriqueTiers ht = (TIHistoriqueTiers) iter.next();

                if (JadeStringUtil.isBlankOrZero(ht.getValeur())) {
                    continue;
                }

                idTiers = ht.getIdTiers();
                dateDecesL = ht.getValeur();

                TITiersViewBean tiers = new TITiersViewBean();
                tiers.setSession(getSession());
                tiers.setIdTiers(idTiers);
                tiers.retrieve();

                nss = tiers.getNumAvsActuel();
                nom = tiers.getDesignation1();
                prenom = tiers.getDesignation2();
                dateNaissance = tiers.getDateNaissance();
                adresseDomicile = tiers.getAdresseAsString();
                adresseDomicile = adresseDomicile.replaceAll("[\n]", " ");

                AFAffiliationManager aff = new AFAffiliationManager();
                aff.setSession(getSession());
                aff.setForIdTiers(idTiers);
                aff.find();

                if (aff.size() > 0) {
                    for (Iterator iterator = aff.iterator(); iterator.hasNext();) {
                        AFAffiliation affiliation = (AFAffiliation) iterator.next();
                        listDesAffiliations.add(affiliation);
                    }
                } else {
                    System.out.println("Pas d'affiliations pour ce tiers");
                }

                TIRoleManager roleMgr = new TIRoleManager();
                roleMgr.setSession(getSession());
                roleMgr.setForIdTiers(idTiers);
                roleMgr.find();

                if (roleMgr.size() > 0) {
                    for (Iterator t = roleMgr.iterator(); t.hasNext();) {
                        TIRole role = (TIRole) t.next();
                        listDesRoles.add(role);
                    }
                } else {
                    System.out.println("Pas de role pour ce tiers");
                }
                StringBuffer buf = batch.printLine(getSession(), listDesAffiliations, listDesRoles, nss, nom, prenom,
                        dateNaissance, dateDecesL, adresseDomicile, dateDuJour);
                p.print(buf.toString());
                p.flush();

                listDesAffiliations.clear();
                listDesRoles.clear();

                AFListeDeces inf = new AFListeDeces();
                inf.setSession(getSession());
                inf.setIdTiers(idTiers);
                inf.setLastExecutionDate(dateDuJour);
                inf.add();

            }
            p.close();
        }

    }

    public String getDateDeces() {
        return dateDeces;
    }

    @Override
    protected String getEMailObject() {

        String message = null;

        if (isJSProcess) {
            message = "Impression de la liste des décès du " + dateDeces + " terminée avec succès";
        } else {
            message = "Impression de la liste des décès terminée avec succès";
        }
        return message;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

}
