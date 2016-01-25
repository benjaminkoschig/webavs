package globaz.pegasus.utils.calculmoissuivant;

import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.ArrayList;

/**
 * POJO contenant la description pour l'affichage html des données financières avant le calcul PC
 * 
 * @author sce
 * 
 */
public class DonneeFinanciereDescriptor {

    private String idTitreMenu = null;

    private int position; // position dans le flux des données fianancieres

    private ArrayList<DonneeFinancierePropertiesDescriptor> proprietes = null;// liste des proprités

    private JadeApplicationService service = null;

    private String titreOnglet = null;

    private String userAction = null;

    public DonneeFinanciereDescriptor(int position, ArrayList<DonneeFinancierePropertiesDescriptor> proprietes,
            String idTitreMenu, String titreOnglet, String userAction, JadeApplicationService service) {
        this.position = position;
        this.proprietes = proprietes;
        this.idTitreMenu = idTitreMenu;
        this.titreOnglet = titreOnglet;
        this.userAction = userAction;
        this.service = service;

    }

    public String getIdTitreMenu() {
        return idTitreMenu;
    }

    public int getPosition() {
        return position;
    }

    public ArrayList<DonneeFinancierePropertiesDescriptor> getProprietes() {
        return proprietes;
    }

    public JadeApplicationService getService() {
        return service;
    }

    public String getTitreOnglet() {
        return titreOnglet;
    }

    public String getUserAction() {
        return userAction;
    }

    public void setIdTitreMenu(String idTitreMenu) {
        this.idTitreMenu = idTitreMenu;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setProprietes(ArrayList<DonneeFinancierePropertiesDescriptor> proprietes) {
        this.proprietes = proprietes;
    }

    public void setService(JadeApplicationService service) {
        this.service = service;
    }

    public void setTitreOnglet(String titreOnglet) {
        this.titreOnglet = titreOnglet;
    }

    public void setUserAction(String userAction) {
        this.userAction = userAction;
    }

}
