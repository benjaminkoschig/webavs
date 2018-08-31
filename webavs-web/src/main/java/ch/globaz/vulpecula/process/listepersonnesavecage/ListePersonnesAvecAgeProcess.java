package ch.globaz.vulpecula.process.listepersonnesavecage;

import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.sql.QueryExecutor;
import ch.globaz.pyxis.domaine.Sexe;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.listepersonnesavecage.ListePersonnesAvecAge;
import ch.globaz.vulpecula.external.BProcessWithContext;

public class ListePersonnesAvecAgeProcess extends BProcessWithContext implements Observer {
    private static final long serialVersionUID = -5915275758727848732L;

    private Annee annee;

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();
        print();
        return true;
    }

    private void print() throws IOException {
        ListePersonnesAvecAgeExcel listePersonnesAgeExcel = new ListePersonnesAvecAgeExcel(getSession(),
                DocumentConstants.LISTES_PERSONNES_AVEC_AGE_FILE_NAME,
                DocumentConstants.LISTES_PERSONNES_AVEC_AGE_DOC_NAME);

        // Instanciation des listes
        List<ListePersonnesAvecAge> actifs18ans = getActifs18Ans();
        List<ListePersonnesAvecAge> travailleursPlus70ans = getTravailleursPlus70ans();
        List<ListePersonnesAvecAge> affiliesPlus70ans = getAffiliesPlus70ans();
        // Affectation des listes
        listePersonnesAgeExcel.setListeActifs18Ans(actifs18ans);
        listePersonnesAgeExcel.setListeActifsPlus70Ans(travailleursPlus70ans);
        listePersonnesAgeExcel.setListeAffiliesPlus70Ans(affiliesPlus70ans);

        // Instanciation et affectation des 4 listes des hommes 62 et 65 ans / femmes 61 et 64 ans
        getActifs61A65ans(listePersonnesAgeExcel);

        listePersonnesAgeExcel.setAnnee(annee);

        // Création du fichier excel
        listePersonnesAgeExcel.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this),
                listePersonnesAgeExcel.getOutputFile());
    }

    private void getActifs61A65ans(ListePersonnesAvecAgeExcel listePersonnesAgeExcel) {
        // Charger toutes les personnes actives qui ont entre 61 et 65 ans et ensuite on trie cette liste
        // Trier en java le résultat au lieu de faire 4 requêtes SQL
        List<ListePersonnesAvecAge> liste = QueryExecutor.execute(sqlFindActifs61A65ans(annee),
                ListePersonnesAvecAge.class);

        List<ListePersonnesAvecAge> actifsHommes62ans = new ArrayList<ListePersonnesAvecAge>();
        List<ListePersonnesAvecAge> actifsHommes65ans = new ArrayList<ListePersonnesAvecAge>();
        List<ListePersonnesAvecAge> actifsFemmes61ans = new ArrayList<ListePersonnesAvecAge>();
        List<ListePersonnesAvecAge> actifsFemmes64ans = new ArrayList<ListePersonnesAvecAge>();

        Date firstDayOfYear65ans = new Date(annee.previous(65).getFirstDayOfYear().getValue());
        Date lastDayOfYear65ans = new Date(annee.previous(65).getLastDayOfYear().getValue());
        Date firstDayOfYear62ans = new Date(annee.previous(62).getFirstDayOfYear().getValue());
        Date lastDayOfYear62ans = new Date(annee.previous(62).getLastDayOfYear().getValue());

        Date firstDayOfYear61ans = new Date(annee.previous(61).getFirstDayOfYear().getValue());
        Date lastDayOfYear61ans = new Date(annee.previous(61).getLastDayOfYear().getValue());
        Date firstDayOfYear64ans = new Date(annee.previous(64).getFirstDayOfYear().getValue());
        Date lastDayOfYear64ans = new Date(annee.previous(64).getLastDayOfYear().getValue());

        for (ListePersonnesAvecAge infosPersonne : liste) {
            if (Sexe.HOMME.equals(Sexe.parse(infosPersonne.getCodeSexeTiers()))) {
                if (infosPersonne.getDateNaissanceTiers().isBetween(firstDayOfYear62ans, lastDayOfYear62ans)) {
                    actifsHommes62ans.add(infosPersonne);
                } else if (infosPersonne.getDateNaissanceTiers().isBetween(firstDayOfYear65ans, lastDayOfYear65ans)) {
                    actifsHommes65ans.add(infosPersonne);
                }
            } else if (Sexe.FEMME.equals(Sexe.parse(infosPersonne.getCodeSexeTiers()))) {
                if (infosPersonne.getDateNaissanceTiers().isBetween(firstDayOfYear61ans, lastDayOfYear61ans)) {
                    actifsFemmes61ans.add(infosPersonne);
                } else if (infosPersonne.getDateNaissanceTiers().isBetween(firstDayOfYear64ans, lastDayOfYear64ans)) {
                    actifsFemmes64ans.add(infosPersonne);
                }
            }
        }
        listePersonnesAgeExcel.setListeActifsH62(actifsHommes62ans);
        listePersonnesAgeExcel.setListeActifsH65(actifsHommes65ans);
        listePersonnesAgeExcel.setListeActifsF61(actifsFemmes61ans);
        listePersonnesAgeExcel.setListeActifsF64(actifsFemmes64ans);
    }

    private List<ListePersonnesAvecAge> getActifs18Ans() {
        return QueryExecutor.execute(sqlFindActifs18ans(annee), ListePersonnesAvecAge.class);
    }

    private List<ListePersonnesAvecAge> getTravailleursPlus70ans() {
        return QueryExecutor.execute(sqlFindTravailleurPlus70ans(annee), ListePersonnesAvecAge.class);
    }

    private List<ListePersonnesAvecAge> getAffiliesPlus70ans() {
        return QueryExecutor.execute(sqlFindAffiliesPlus70ans(annee), ListePersonnesAvecAge.class);
    }

    @Override
    public void update(Observable o, Object arg) {
        // TODO Auto-generated method stub

    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.LISTES_PERSONNES_AVEC_AGE_NAME;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public Annee getAnnee() {
        return annee;
    }

    public void setAnnee(Annee annee) {
        this.annee = annee;
    }

    private String sqlFindActifs61A65ans(Annee annee) {
        String firstDayOfYear65ans = annee.previous(65).getFirstDayOfYear().getValue();
        String lastDayOfYear61ans = annee.previous(61).getLastDayOfYear().getValue();
        return "select tra.ID AS num_travailleur, pavs.HXNAVS AS num_avs, tiers.HTLDE1 as nom_tiers, tiers. HTLDE2 AS prenom_tiers, "
                + "pers.HPDNAI AS date_naissance_tiers,sexe.PCOLUT AS sexe_tiers, pers.HPTSEX AS code_sexe_tiers, aff.MALNAF AS num_aff, admi.HTLDE1 AS convention "
                + "from SCHEMA.PT_TRAVAILLEURS tra "
                + "inner join SCHEMA.PT_POSTES_TRAVAILS poste on tra.ID=poste.ID_PT_TRAVAILLEURS "
                + "inner join SCHEMA.TITIERP tiers on tra.ID_TITIERP=tiers.htitie "
                + "inner join SCHEMA.TIpersP pers on tra.ID_TITIERP=pers.htitie "
                + "inner join SCHEMA.TIpavsP pavs on tra.ID_TITIERP=pavs.htitie "
                + "inner join SCHEMA.fwcoup sexe on sexe.pcosid=pers.HPTSEX and plaide='F' "
                + "left join SCHEMA.afaffip aff on poste.ID_AFAFFIP=aff.maiaff "
                + "left join SCHEMA.titierp admi on admi.htitie=aff.MACONV "
                + "where hpdnai between "
                + firstDayOfYear65ans
                + " and "
                + lastDayOfYear61ans
                + " and poste.DATE_DEBUT_ACTIVITE<"
                + annee.toString()
                + "0000 and (poste.DATE_FIN_ACTIVITE>"
                + annee.toString()
                + "0000 or poste.DATE_FIN_ACTIVITE=0) order by tiers.htlde1";
    }

    private String sqlFindActifs18ans(Annee annee) {
        String firstDayOfYear18ans = annee.previous(18).getFirstDayOfYear().getValue();
        String lastDayOfYear18ans = annee.previous(18).getLastDayOfYear().getValue();
        return "select tra.ID AS num_travailleur, pavs.HXNAVS AS num_avs, tiers.HTLDE1 as nom_tiers, tiers. HTLDE2 AS prenom_tiers, "
                + "pers.HPDNAI AS date_naissance_tiers,sexe.PCOLUT AS sexe_tiers, aff.MALNAF AS num_aff, admi.HTLDE1 AS convention "
                + "from SCHEMA.PT_TRAVAILLEURS tra "
                + "inner join SCHEMA.PT_POSTES_TRAVAILS poste on tra.ID=poste.ID_PT_TRAVAILLEURS "
                + "inner join SCHEMA.TITIERP tiers on tra.ID_TITIERP=tiers.htitie "
                + "inner join SCHEMA.TIpersP pers on tra.ID_TITIERP=pers.htitie "
                + "inner join SCHEMA.TIpavsP pavs on tra.ID_TITIERP=pavs.htitie "
                + "inner join SCHEMA.fwcoup sexe on sexe.pcosid=pers.HPTSEX and plaide='F' "
                + "left join SCHEMA.afaffip aff on poste.ID_AFAFFIP=aff.maiaff "
                + "left join SCHEMA.titierp admi on admi.htitie=aff.MACONV "
                + "where hpdnai between "
                + firstDayOfYear18ans
                + " and "
                + lastDayOfYear18ans
                + " and poste.DATE_DEBUT_ACTIVITE<"
                + annee.toString()
                + "0000 and (poste.DATE_FIN_ACTIVITE>"
                + annee.toString()
                + "0000 or poste.DATE_FIN_ACTIVITE=0) order by tiers.htlde1";
    }

    private String sqlFindTravailleurPlus70ans(Annee annee) {
        String lastDayOfYear70ans = annee.previous(70).getLastDayOfYear().getValue();
        return "select tra.ID AS num_travailleur, pavs.HXNAVS AS num_avs, tiers.HTLDE1 as nom_tiers, tiers. HTLDE2 AS prenom_tiers, "
                + "pers.HPDNAI AS date_naissance_tiers,sexe.PCOLUT AS sexe_tiers, aff.MALNAF AS num_aff, admi.HTLDE1 AS convention "
                + "from SCHEMA.PT_TRAVAILLEURS tra "
                + "inner join SCHEMA.PT_POSTES_TRAVAILS poste on tra.ID=poste.ID_PT_TRAVAILLEURS "
                + "inner join SCHEMA.TITIERP tiers on tra.ID_TITIERP=tiers.htitie "
                + "inner join SCHEMA.TIpersP pers on tra.ID_TITIERP=pers.htitie "
                + "inner join SCHEMA.TIpavsP pavs on tra.ID_TITIERP=pavs.htitie "
                + "inner join SCHEMA.fwcoup sexe on sexe.pcosid=pers.HPTSEX and plaide='F' "
                + "left join SCHEMA.afaffip aff on poste.ID_AFAFFIP=aff.maiaff "
                + "left join SCHEMA.titierp admi on admi.htitie=aff.MACONV "
                + "where hpdnai < "
                + lastDayOfYear70ans
                + " and poste.DATE_DEBUT_ACTIVITE<"
                + annee.toString()
                + "0000 and (poste.DATE_FIN_ACTIVITE>"
                + annee.toString() + "0000 or poste.DATE_FIN_ACTIVITE=0) order by tiers.htlde1";
    }

    private String sqlFindActifsHomme62ans(Annee annee) {
        String firstDayOfYear62ans = annee.previous(62).getFirstDayOfYear().getValue();
        String lastDayOfYear62ans = annee.previous(62).getLastDayOfYear().getValue();
        return "select tra.ID AS num_travailleur, pavs.HXNAVS AS num_avs, tiers.HTLDE1 as nom_tiers, tiers. HTLDE2 AS prenom_tiers, "
                + "pers.HPDNAI AS date_naissance_tiers,sexe.PCOLUT AS sexe_tiers, aff.MALNAF AS num_aff, admi.HTLDE1 AS convention "
                + "from SCHEMA.PT_TRAVAILLEURS tra "
                + "inner join SCHEMA.PT_POSTES_TRAVAILS poste on tra.ID=poste.ID_PT_TRAVAILLEURS "
                + "inner join SCHEMA.TITIERP tiers on tra.ID_TITIERP=tiers.htitie "
                + "inner join SCHEMA.TIpersP pers on tra.ID_TITIERP=pers.htitie "
                + "inner join SCHEMA.TIpavsP pavs on tra.ID_TITIERP=pavs.htitie "
                + "inner join SCHEMA.fwcoup sexe on sexe.pcosid=pers.HPTSEX and plaide='F' "
                + "left join SCHEMA.afaffip aff on poste.ID_AFAFFIP=aff.maiaff "
                + "left join SCHEMA.titierp admi on admi.htitie=aff.MACONV "
                + "where hpdnai between "
                + firstDayOfYear62ans
                + " and "
                + lastDayOfYear62ans
                + " and poste.DATE_DEBUT_ACTIVITE<"
                + annee.toString()
                + "0000 and (poste.DATE_FIN_ACTIVITE>"
                + annee.toString()
                + "0000 or poste.DATE_FIN_ACTIVITE=0) order by tiers.htlde1";
    }

    private String sqlFindActifsHomme65ans(Annee annee) {
        String firstDayOfYear65ans = annee.previous(65).getFirstDayOfYear().getValue();
        String lastDayOfYear65ans = annee.previous(65).getLastDayOfYear().getValue();
        return "select tra.ID AS num_travailleur, pavs.HXNAVS AS num_avs, tiers.HTLDE1 as nom_tiers, tiers. HTLDE2 AS prenom_tiers, "
                + "pers.HPDNAI AS date_naissance_tiers,sexe.PCOLUT AS sexe_tiers, aff.MALNAF AS num_aff, admi.HTLDE1 AS convention "
                + "from SCHEMA.PT_TRAVAILLEURS tra "
                + "inner join SCHEMA.PT_POSTES_TRAVAILS poste on tra.ID=poste.ID_PT_TRAVAILLEURS "
                + "inner join SCHEMA.TITIERP tiers on tra.ID_TITIERP=tiers.htitie "
                + "inner join SCHEMA.TIpersP pers on tra.ID_TITIERP=pers.htitie "
                + "inner join SCHEMA.TIpavsP pavs on tra.ID_TITIERP=pavs.htitie "
                + "inner join SCHEMA.fwcoup sexe on sexe.pcosid=pers.HPTSEX and plaide='F' "
                + "left join SCHEMA.afaffip aff on poste.ID_AFAFFIP=aff.maiaff "
                + "left join SCHEMA.titierp admi on admi.htitie=aff.MACONV "
                + "where hpdnai between "
                + firstDayOfYear65ans
                + " and "
                + lastDayOfYear65ans
                + " and poste.DATE_DEBUT_ACTIVITE<"
                + annee.toString()
                + "0000 and (poste.DATE_FIN_ACTIVITE>"
                + annee.toString()
                + "0000 or poste.DATE_FIN_ACTIVITE=0) order by tiers.htlde1";
    }

    private String sqlFindAffiliesPlus70ans(Annee annee) {
        String lastDayOfYear70ans = annee.previous(70).getLastDayOfYear().getValue();
        return "select aff.MALNAF AS num_aff, pavs.HXNAVS AS num_avs, tiers.HTLDE1 as nom_tiers, tiers. HTLDE2 AS prenom_tiers, "
                + "pers.HPDNAI AS date_naissance_tiers,sexe.PCOLUT AS sexe_tiers, admi.HTLDE1 AS convention "
                + "from SCHEMA.AFAFFIP aff "
                + "inner join SCHEMA.TITIERP tiers on aff.htitie=tiers.htitie "
                + "inner join SCHEMA.TIpersP pers on tiers.htitie=pers.htitie "
                + "inner join SCHEMA.TIpavsP pavs on tiers.htitie=pavs.htitie "
                + "inner join SCHEMA.fwcoup sexe on sexe.pcosid=pers.HPTSEX and sexe.plaide='F' "
                + "left join SCHEMA.titierp admi on admi.htitie=aff.MACONV "
                + "where (hpdnai < "
                + lastDayOfYear70ans
                + " and hpdnai<>0 and hpdnai IS not null) and (madfin = 0 or madfin>"
                + annee.toString()
                + "0101) "
                + "order by malnaf";
    }
}
