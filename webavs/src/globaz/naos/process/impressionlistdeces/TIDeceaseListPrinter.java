package globaz.naos.process.impressionlistdeces;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.prestation.tools.PRDateFormater;
import globaz.pyxis.db.tiers.TIHistoriqueTiers;
import globaz.pyxis.db.tiers.TIHistoriqueTiersManager;
import globaz.pyxis.db.tiers.TIRole;
import globaz.pyxis.db.tiers.TIRoleManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TIDeceaseListPrinter extends BProcess {

    private static final long serialVersionUID = 1989387991523573947L;
    public static final int CODE_RETOUR_ERREUR = 200;
    public static final int CODE_RETOUR_OK = 0;
    private String dateDeces = "";

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
                dateDeb = ht.getDateDebut();
                dateDecesL = ht.getValeur();

                TIDeceaseInformationManager dim = new TIDeceaseInformationManager();
                dim.setSession(getSession());
                dim.setIdTiers(idTiers);
                dim.find();

                if (dim.size() > 0) {
                    for (Iterator t = dim.iterator(); t.hasNext();) {
                        TIDeceaseInformation inf = (TIDeceaseInformation) t.next();

                        lastExecution = inf.getLastExecutionDate();

                        if (BSessionUtil.compareDateFirstGreater(getSession(), dateDeb, lastExecution)) {

                            TITiersViewBean tiers = new TITiersViewBean();
                            nom = tiers.getDesignation1();
                            prenom = tiers.getDesignation2();
                            dateNaissance = tiers.getDateNaissance();
                            adresseDomicile = tiers.getAdresseAsString();

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
                            filePrinter(getSession(), listDesAffiliations, listDesRoles, nss, nom, prenom,
                                    dateNaissance, dateDecesL, adresseDomicile, dateDuJour);

                            TIDeceaseInformation info = new TIDeceaseInformation();
                            info.setSession(getSession());
                            info.setIdTiers(idTiers);
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
        mgr.setForDateDebutGreaterOrEqualTo(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(dateDeces));
        mgr.find();

        if (mgr.size() > 0) {
            for (Iterator iter = mgr.iterator(); iter.hasNext();) {
                TIHistoriqueTiers ht = (TIHistoriqueTiers) iter.next();

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
                filePrinter(getSession(), listDesAffiliations, listDesRoles, nss, nom, prenom, dateNaissance,
                        dateDeces, adresseDomicile, dateDuJour);
            }
        }
        return true;
    }

    public void executeP() throws Exception {

        TIDeceaseInformationManager dim = new TIDeceaseInformationManager();
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

        String idTiers = "";
        String nss = "";
        String nom = "";
        String prenom = "";
        String dateNaissance = "";
        String dateDecesL = "";
        String adresseDomicile = "";
        String dateDuJour = JACalendar.todayJJsMMsAAAA();

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
                filePrinter(getSession(), listDesAffiliations, listDesRoles, nss, nom, prenom, dateNaissance,
                        dateDecesL, adresseDomicile, dateDuJour);

                TIDeceaseInformation inf = new TIDeceaseInformation();
                inf.setSession(getSession());
                inf.setIdTiers(idTiers);
                inf.setLastExecutionDate(dateDuJour);
                inf.add();

            }
        }

    }

    public void filePrinter(BSession session, List affiliationList, List roleList, String nss, String nom,
            String prenom, String dateNaissance, String dateDeces, String adresseDomicile, String dateDuJour)
            throws Exception {

        PrintWriter printer = new PrintWriter(new FileWriter("listDesDeces" + dateDuJour + ".csv"));
        StringBuffer csvHeader = new StringBuffer();
        csvHeader.append("ROLE").append(";");
        csvHeader.append("NSS").append(";");
        csvHeader.append("NOM").append(";");
        csvHeader.append("PRENOM").append(";");
        csvHeader.append("DATE DE NAISSANCE").append(";");
        csvHeader.append("DATE DE DECES").append(";");
        csvHeader.append("ADRESSE DE DOMICILE").append(";");
        csvHeader.append("NUMERO AFFILIE").append(";");
        csvHeader.append("DATE DEBUT AFFILIATION").append(";");
        csvHeader.append("DATE FIN AFFILIATION").append(";");
        printer.println(csvHeader.toString());
        printer.flush();

        for (Iterator iterator = roleList.iterator(); iterator.hasNext();) {
            TIRole role = (TIRole) iterator.next();
            String rol = role.getRole();
            String roleFinal = session.getCodeLibelle(rol);

            if (!affiliationList.isEmpty() && roleFinal.startsWith("Assu")) {
                for (Iterator iter = affiliationList.iterator(); iter.hasNext();) {
                    AFAffiliation aff = (AFAffiliation) iter.next();

                    StringBuffer csvLine = new StringBuffer();
                    csvLine.append(roleFinal).append(";");
                    csvLine.append(nss).append(";");
                    csvLine.append(nom).append(";");
                    csvLine.append(prenom).append(";");
                    csvLine.append(dateNaissance).append(";");
                    csvLine.append(dateDeces).append(";");
                    csvLine.append(adresseDomicile).append(";");
                    csvLine.append(aff.getAffilieNumero()).append(";");
                    csvLine.append(aff.getDateDebut()).append(";");
                    csvLine.append(aff.getDateFin()).append(";");
                    printer.println(csvLine.toString());
                    printer.flush();
                }
            } else {
                StringBuffer csvLine = new StringBuffer();
                csvLine.append(roleFinal).append(";");
                csvLine.append(nss).append(";");
                csvLine.append(nom).append(";");
                csvLine.append(prenom).append(";");
                csvLine.append(dateNaissance).append(";");
                csvLine.append(dateDeces).append(";");
                csvLine.append(adresseDomicile).append(";");
                csvLine.append(" ").append(";");
                csvLine.append(" ").append(";");
                csvLine.append(" ").append(";");
                printer.println(csvLine.toString());
                printer.flush();
            }
        }
        printer.close();
    }

    @Override
    protected String getEMailObject() {
        return null;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return null;
    }

}
