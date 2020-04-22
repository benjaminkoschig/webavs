package globaz.aquila.process.elp;

import globaz.aquila.print.list.elp.COMotifMessageELP;
import globaz.aquila.print.list.elp.COResultOtherELP;
import globaz.aquila.print.list.elp.COTypeMessageELP;
import globaz.globall.util.JACalendar;

import java.util.ArrayList;
import java.util.List;

public class COProtocoleELP {

    private List<COScElpDto> listCDPnonTraite = new ArrayList<>();
    private List<COSpElpDto> listPVnonTraite = new ArrayList<>();
    private List<CORcElpDto> listADBnonTraite = new ArrayList<>();
    private List<COAbstractELP> listMsgIncoherent = new ArrayList<>();
    private List<COAbstractELP> listMsgTraite = new ArrayList<>();

    public void addnonTraite(COScElpDto result) {
        listCDPnonTraite.add(result);
    }


    public void addnonTraite(COSpElpDto result) {
        listPVnonTraite.add(result);
    }

    public void addnonTraite(CORcElpDto result) {
        listADBnonTraite.add(result);
    }

    public void addMsgIncoherentOther(COInfoFileELP infos, COTypeMessageELP type) {
        COResultOtherELP result = new COResultOtherELP();
        result.setDateReception(infos.getDate());
        result.setFichier(infos.getFichier());
        result.setMotif(COMotifMessageELP.TYPE_NON_TRAITE);
        result.setType(type);
        listMsgIncoherent.add(result);
    }

    public void addMsgIncoherentInattendue( COInfoFileELP infos, String erreur) {
        COResultOtherELP result = new COResultOtherELP();
        result.setDateReception(infos.getDate());
        result.setFichier(infos.getFichier());
        result.setMotif(COMotifMessageELP.AUTRE_ERREUR);
        result.setMotifAdditional(erreur);
        result.setType(COTypeMessageELP.OTHER);
        listMsgIncoherent.add(result);
    }

    public void addMsgIncoherentNomFichier(String nomFichier) {
        COResultOtherELP result = new COResultOtherELP();
        result.setDateReception(JACalendar.todayJJsMMsAAAA());
        result.setFichier(nomFichier);
        result.setMotif(COMotifMessageELP.NOM_FICHIER_INCOHERENT);
        result.setMotifAdditional(nomFichier);
        result.setType(COTypeMessageELP.OTHER);
        listMsgIncoherent.add(result);
    }

    public void addMsgIncoherent(COScElpDto result) {
        listMsgIncoherent.add(result);
    }

    public void addMsgIncoherent(COSpElpDto result) {
        listMsgIncoherent.add(result);
    }


    public void addMsgIncoherent(CORcElpDto result) {
        listMsgIncoherent.add(result);
    }

    public void addMsgTraite(COScElpDto result) {
        listMsgTraite.add(result);
    }

    public void addMsgTraite(COSpElpDto result) {
        listMsgTraite.add(result);
    }

    public void addMsgTraite(CORcElpDto result) {
        listMsgTraite.add(result);
    }


    public List<COScElpDto> getListCDPnonTraite() {
        return listCDPnonTraite;
    }

    public void setListCDPnonTraite(List<COScElpDto> listCDPnonTraite) {
        this.listCDPnonTraite = listCDPnonTraite;
    }

    public List<COSpElpDto> getListPVnonTraite() {
        return listPVnonTraite;
    }

    public void setListPVnonTraite(List<COSpElpDto> listPVnonTraite) {
        this.listPVnonTraite = listPVnonTraite;
    }

    public List<CORcElpDto> getListADBnonTraite() {
        return listADBnonTraite;
    }

    public void setListADBnonTraite(List<CORcElpDto> listADBnonTraite) {
        this.listADBnonTraite = listADBnonTraite;
    }

    public List<COAbstractELP> getListMsgIncoherent() {
        return listMsgIncoherent;
    }

    public void setListMsgIncoherent(List<COAbstractELP> listMsgIncoherent) {
        this.listMsgIncoherent = listMsgIncoherent;
    }

    public List<COAbstractELP> getListMsgTraite() {
        return listMsgTraite;
    }

    public void setListMsgTraite(List<COAbstractELP> listMsgTraite) {
        this.listMsgTraite = listMsgTraite;
    }

}
