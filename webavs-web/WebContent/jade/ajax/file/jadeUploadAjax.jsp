<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page language="java"  import="globaz.globall.http.*" %>
<%@page import="globaz.framework.bean.JadeUploadRetourAjax"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="globaz.jade.fs.JadeFsFacade"%>
<%@page import="java.util.Arrays"%>
<%@page import="globaz.jade.client.util.JadeUUIDGenerator"%>
<%@page import="globaz.jade.client.util.JadeFilenameUtil"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="org.apache.commons.io.IOUtils"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@page import="java.util.ArrayList"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<HTML>
<head>
<%
JadeUploadRetourAjax retour = new JadeUploadRetourAjax();

try { 
	//globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	//globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	boolean saveToBlob = false;
	String path ="";
	String protocole = "";
	String theFileName = "";
	
	//uploadBean.setSavePath(retour.getPath());	
	FileItemFactory fileItemFactory = new DiskFileItemFactory();
	ServletFileUpload upload = new ServletFileUpload(fileItemFactory);
	List items = upload.parseRequest(request);
	Iterator iter = items.iterator();
	
	List<FileItem> listFile = new ArrayList<FileItem>();
	while (iter.hasNext()) {
		FileItem item = (FileItem) iter.next();
		if(item.isFormField()) {
			if(item.getFieldName().equalsIgnoreCase("protocole")) {
				protocole = item.getString();
			}
		} else {
			listFile.add(item);
		}
	}
	
	if(!JadeStringUtil.isEmpty(protocole) 
			&& protocole.equalsIgnoreCase("jdbc")){
		path = "jdbc://" + globaz.jade.common.Jade.getInstance().getDefaultJdbcSchema() + "/";
		saveToBlob = true;
	} else {
		path = globaz.jade.common.Jade.getInstance().getHomeDir() + "persistence/";
	}
	retour.setPath(path);
	
	for(FileItem item:listFile) {
		FileOutputStream fos = null;
		String fileUploadRootName = JadeStringUtil.convertSpecialChars(JadeFilenameUtil.extractFilenameRoot(item.getName()));
		String fileUploadExtention = JadeStringUtil.convertSpecialChars(JadeFilenameUtil.extractFilenameExtension(item.getName()));
		
		theFileName =  JadeStringUtil.toUpperCase(fileUploadRootName.replaceAll(" ","_"))+ "_" +JadeUUIDGenerator.createStringUUID() +"."+fileUploadExtention;
    	if(saveToBlob) {
    		byte[] table = item.get();
    		List<Byte> liste = new ArrayList();
    		for (int i = 0; i < table.length; i++) {
    			liste.add(table[i]);
    		}
    		JadeFsFacade.writeFile(liste, path + theFileName);
    	} else {
	    	try {
	    		fos = new FileOutputStream(path + theFileName);
	    		IOUtils.write(item.get(),fos);
	    		
	    	} finally {
	    		IOUtils.closeQuietly(fos);
	    	}
    	}
    	retour.setSize(item.getSize());
	}

	// uploadBean.doUpload(request);
	retour.setFileName(theFileName);
} catch (Exception e) {
	retour.setHasError(true);
	retour.setMessageError(e.toString());
}
String json = (new Gson()).toJson(retour);
%>
</head>
<body>
<script>
	var retour =  <%=json%>;
	var $notationUpload = parent.window.$("#formUpload",parent.document);
	parent.window.$($notationUpload).triggerHandler(parent.window.eventConstant.UPLOAD_RETURN,retour);
</script>
</body>
</html>