package ch.globaz.perseus.business.services.rentepont;

import globaz.globall.api.BISession;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface TypesSoinsRentePontService extends JadeApplicationService {

    public HashMap<String, HashMap<String, String>> getAllSousTypes(BISession session) throws RemoteException;

    public String getAllSousTypesInJson(BISession session) throws RemoteException;

    public HashMap<String, String> getMapSousTypesFromCodeSystem(String codeSystem, BISession session)
            throws RemoteException;

    public HashMap<String, String> getMapSurTypes(BISession session) throws RemoteException;

}
