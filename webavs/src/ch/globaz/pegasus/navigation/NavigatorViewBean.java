package ch.globaz.pegasus.navigation;

import java.util.ArrayList;
import java.util.List;

public class NavigatorViewBean {

    public String libelle = null;
    public String link = null;
    public List<NavigatorViewBean> list = null;

    public NavigatorViewBean() {
        list = new ArrayList<NavigatorViewBean>();
    }

    public String getLibelle() {
        return libelle;
    }

    public String getLink() {
        return link;
    }

    public List<NavigatorViewBean> getList() {
        return list;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setList(List<NavigatorViewBean> list) {
        this.list = list;
    }

}
