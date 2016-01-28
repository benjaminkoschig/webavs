 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran="CCP1011";%>
<%@ page import="globaz.phenix.db.communications.*" %>
<%@ page import="java.util.LinkedList" %>


<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> 
<%
	//Encryptage de la page
	formEncType = "'multipart/form-data' method='post'";

	//Liens vers le formulaire tampon qui recupère le fichier uploadé
	formAction= request.getContextPath()+mainServletPath+"Root/"+languePage+"/communications/processReceptionCommunicationChargementFile_de.jsp";
	//Récupération du bean
	CPJournalRetourViewBean viewBean = (CPJournalRetourViewBean) session.getAttribute ("viewBean");	
	boolean emptyViewBean = true; //Le journal n'existe pas encore -->reception totale + creation du journal
	String typeReception = globaz.phenix.process.communications.CPProcessReceptionCommunication.RECEPTION_TOLAL_CREER_JOURNAL;
	if (!viewBean.isNew()) {
		// si viewBean contient un journal --> on receptionne sans créer de journal
		emptyViewBean = false; 
		typeReception = globaz.phenix.process.communications.CPProcessReceptionCommunication.RECEPTION_TOTAL;
	}
	CPReceptionReaderViewBean receptionReader = new CPReceptionReaderViewBean();
	receptionReader.setISession(viewBean.getSession());
%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
top.document.title = "Récéption des données fiscales";
// stop hiding -->
function validate()
{
	if ((document.forms[0].file.value.length==0) ||
   (document.forms[0].file.value==null)) {
   errorObj.text = "Vous devez renseigner le chemin du fichier de reception du fisc !";
   showErrors();
   return false;
   }else{
   return true;
   }
}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Réception des données fiscales<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
		<TR>
			<TD nowrap colspan="2" style="font-weight : bolder;">Ce programme va créer un journal contenant les communications transmises par le FISC.</TD>
		</TR>
		<TR>
			<TD nowrap colspan="2" style="font-weight : bolder;">&nbsp;</TD>
		</TR>
		<TR>
			<TD width="180">Adresse E-Mail</TD>
            <TD><input name='eMailAddress' class='libelleLong' data-g-string="mandatory:true" value='<%=viewBean.getSession().getUserEMail()%>'></TD>
		</TR>
		<TR>
			<TD width="180">Nom du journal</TD>
            <TD><input name='libelleJournal' class='libelleLong' value="<%=viewBean.getLibelleJournal()%>" <%=emptyViewBean?"":"disabled='disabled'"%> ></TD>
		</TR>
		<TR>
			<TD width="180">Fichier de reception</TD>
            <TD nowrap width="547"><input type='file'  name='file' size="60"  value='...'></TD>
		</TR>
		<TR>
            <%
            	LinkedList cantonImplList = receptionReader.getReaders();
            %>
			<TD width="180">Canton de provenance du fichier</TD>
            <TD>
	           <SELECT name="csCanton" >
	            	<% if (!cantonImplList.isEmpty()) { %>
	            		<OPTION selected value='<%=cantonImplList.removeFirst()%>'><%=cantonImplList.removeFirst()%></OPTION>
					<% } %>
	            	<% while(!cantonImplList.isEmpty()){ %>
	            		<OPTION value='<%=cantonImplList.removeFirst()%>'><%=cantonImplList.removeFirst()%></OPTION>
	            	<% } %>
	            </SELECT>
	            
	        </TD>
		</TR>
		<TR><TD><INPUT type="hidden" name="typeReception" value="<%=typeReception%>"></TD>
			<TD><INPUT type="hidden" name="idJournalRetour" value="<%=viewBean.getIdJournalRetour()%>"></TD>
		</TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>
<SCRIPT>
document.forms[0].enctype = "multipart/form-data";
document.forms[0].method = "post";
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>