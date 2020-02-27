package globaz.aquila.process.elp;

import aquila.ch.eschkg.RcType;
import aquila.ch.eschkg.SpType;
import globaz.aquila.print.list.elp.*;
import globaz.globall.util.JACalendar;

import java.util.ArrayList;
import java.util.List;

public class COProtocoleELP {

    private List<COScElpDto> listCDPnonTraite = new ArrayList<>();
    private List<COResultSpELP> listPVnonTraite = new ArrayList<>();
    private List<COResultRcELP> listADBnonTraite = new ArrayList<>();
    private List<COAbstractELP> listMsgIncoherent = new ArrayList<>();
    private List<COAbstractELP> listMsgTraite = new ArrayList<>();

    public void addnonTraite(COScElpDto result) {
        listCDPnonTraite.add(result);
    }

    public void addnonTraite(SpType spType, COInfoFileELP infos, COMotifMessageELP motif) {
        COResultSpELP result = getResultSpELP(spType, infos, motif);
        listPVnonTraite.add(result);
    }

    public void addnonTraite(RcType rcType, COInfoFileELP infos, COMotifMessageELP motif) {
        COResultRcELP result = getResultRcELP(rcType, infos, motif);
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

    public void addMsgIncoherent(SpType spType, COInfoFileELP infos, COMotifMessageELP motif) {
        COResultSpELP result = getResultSpELP(spType, infos, motif);
        listMsgIncoherent.add(result);
    }

    public void addMsgIncoherent(RcType rcType, COInfoFileELP infos, COMotifMessageELP motif) {
        COResultRcELP result = getResultRcELP(rcType, infos, motif);
        listMsgIncoherent.add(result);
    }

    public void addMsgTraite(COScElpDto result) {
        listMsgTraite.add(result);
    }

    public void addMsgTraite(SpType spType, COInfoFileELP infos) {
        COResultSpELP result = getResultSpELP(spType, infos, null);
        listMsgTraite.add(result);
    }

    public void addMsgTraite(RcType rcType, COInfoFileELP infos) {
        COResultRcELP result = getResultRcELP(rcType, infos, null);
        listMsgTraite.add(result);
    }

    private COResultRcELP getResultRcELP(RcType rcType, COInfoFileELP infos, COMotifMessageELP motif) {
        COResultRcELP result = new COResultRcELP(rcType);
        result.setDateReception(infos.getDate());
        result.setFichier(infos.getFichier());
        result.setMotif(motif);
        return result;
    }

    private COResultSpELP getResultSpELP(SpType spType, COInfoFileELP infos, COMotifMessageELP motif) {
        COResultSpELP result = new COResultSpELP(spType);
        result.setDateReception(infos.getDate());
        result.setFichier(infos.getFichier());
        result.setMotif(motif);
        return result;
    }

    public List<COScElpDto> getListCDPnonTraite() {
        return listCDPnonTraite;
    }

    public void setListCDPnonTraite(List<COScElpDto> listCDPnonTraite) {
        this.listCDPnonTraite = listCDPnonTraite;
    }

    public List<COResultSpELP> getListPVnonTraite() {
        return listPVnonTraite;
    }

    public void setListPVnonTraite(List<COResultSpELP> listPVnonTraite) {
        this.listPVnonTraite = listPVnonTraite;
    }

    public List<COResultRcELP> getListADBnonTraite() {
        return listADBnonTraite;
    }

    public void setListADBnonTraite(List<COResultRcELP> listADBnonTraite) {
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
