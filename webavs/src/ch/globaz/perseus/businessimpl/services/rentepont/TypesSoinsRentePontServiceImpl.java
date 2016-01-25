package ch.globaz.perseus.businessimpl.services.rentepont;

import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeStringUtil;
import java.rmi.RemoteException;
import java.util.HashMap;
import ch.globaz.perseus.business.constantes.CSTypeSoin;
import ch.globaz.perseus.business.services.rentepont.TypesSoinsRentePontService;
import com.google.gson.Gson;

public class TypesSoinsRentePontServiceImpl implements TypesSoinsRentePontService {

    @Override
    public HashMap<String, HashMap<String, String>> getAllSousTypes(BISession session) throws RemoteException {
        CSTypeSoin[] allValues = CSTypeSoin.values();
        HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String, String>>();
        for (CSTypeSoin typeSoin : allValues) {
            if (JadeStringUtil.isBlank(typeSoin.getCodeSystemParent())) {
                map.put(typeSoin.getCodeSystem(), getMapSousTypesFromCodeSystem(typeSoin.getCodeSystem(), session));
            }
        }
        return map;
    }

    @Override
    public String getAllSousTypesInJson(BISession session) throws RemoteException {
        Gson json = new Gson();
        return json.toJson(getAllSousTypes(session));
    }

    @Override
    public HashMap<String, String> getMapSousTypesFromCodeSystem(String codeSystem, BISession session)
            throws RemoteException {
        CSTypeSoin[] allValues = CSTypeSoin.values();
        HashMap<String, String> sousTypes = new HashMap<String, String>();
        for (int i = 0; i < allValues.length; i++) {
            CSTypeSoin vm = allValues[i];
            if (vm.getCodeSystemParent().equals(codeSystem)) {
                sousTypes.put(vm.getCodeSystem(), session.getCodeLibelle(vm.getCodeSystem()));
            }
        }
        return sousTypes;
    }

    @Override
    public HashMap<String, String> getMapSurTypes(BISession session) throws RemoteException {
        CSTypeSoin[] allValues = CSTypeSoin.values();
        HashMap<String, String> sousTypes = new HashMap<String, String>();
        for (int i = 0; i < allValues.length; i++) {
            CSTypeSoin vm = allValues[i];
            if (JadeStringUtil.isBlank(vm.getCodeSystemParent())) {
                sousTypes.put(vm.getCodeSystem(), session.getCodeLibelle(vm.getCodeSystem()));
            }
        }
        return sousTypes;
    }

}
