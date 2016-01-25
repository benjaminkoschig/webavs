package ch.globaz.pegasus.navigation;

import globaz.framework.menu.FWMenuBlackBox;
import java.util.HashMap;
import java.util.Map;

public abstract class NavigatorLink implements NavigatorInterface {

    protected Map<String, String> ids = new HashMap<String, String>();
    private FWMenuBlackBox menus;
    protected Map<String, String> parameters = new HashMap<String, String>();

    public Map<String, String> getIds() {
        return ids;
    }

    @Override
    public Map<String, String> getIdsMap() {
        return ids;
    }

    public FWMenuBlackBox getMenus() {
        return menus;
    }

    @Override
    public Map<String, String> getParameters() {
        return parameters;
    }

    @Override
    public void setIdsMap(Map<String, String> ids) {
        this.ids = ids;
    }

}
