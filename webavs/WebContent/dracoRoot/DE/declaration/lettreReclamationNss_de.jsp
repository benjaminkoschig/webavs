<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@page import="org.eclipse.jdt.internal.compiler.ast.TypeDeclaration"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%@page import="globaz.draco.util.DSUtil"%>
<%@page import="globaz.draco.db.declaration.DSLettreReclamationNssViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.draco.print.itext.DSLettreReclamationNss_doc"%>


<%
	idEcran = "CDS0009";
	DSLettreReclamationNssViewBean viewBean = (DSLettreReclamationNssViewBean) session.getAttribute("viewBean");
	userActionValue="draco.declaration.lettreReclamationNss.executer";
	int autoDigitAff = DSUtil.getAutoDigitAff(session);
    String jspLocation = servletContext + mainServletPath + "Root/tiForDeclaration_select.jsp";
    
    
    String typeDeclaration = viewBean.getTypeDeclaration();
    if(JadeStringUtil.isEmpty(typeDeclaration)) {
    	typeDeclaration = (String) request.getParameter("typeDeclaration");
    }
    String numAffilie = (String) request.getParameter("numAffilie");
    if(!JadeStringUtil.isEmpty(viewBean.getFromAffilie()) && viewBean.getFromAffilie().equals(viewBean.getToAffilie())) {
    	numAffilie = viewBean.getFromAffilie();
    }
    String anneeDS = viewBean.getAnnee();
    if(JadeStringUtil.isEmpty(anneeDS)) {
    	anneeDS = (String) request.getParameter("anneeDS");
    }
    String jourRappel = viewBean.getDelaiRappel();
    if(JadeStringUtil.isEmpty(jourRappel)) {
    	jourRappel = "60";
    }
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT>
top.document.title = "Web@AVS - <ct:FWLabel key='DECLARATION_SALAIRE'/>";
function init(){}

function postInit() {
	var myDate = new Date();
	<% if(JadeStringUtil.isEmpty(anneeDS)) {%>
		$("#annee").val(myDate.getFullYear());
	<%}%>
}

function changeTypeDoc() {
	var doc = document.getElementById('typeDocument').value;
	if(doc == '<%=DSLettreReclamationNss_doc.DOCUMENT_LETTRE_RAPPEL %>'){
		document.getElementById('delai').style.visibility = 'visible';
	} else {
		document.getElementById('delai').style.visibility = 'hidden';
	}
}
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_LETTRE_RECLAMATION_NSS"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
			

</TBODY>	
</TABLE>	
<TABLE border="0" cellspacing="2" cellpadding="0" width="<%=subTableWidth%>">
<TBODY>	
	<TR>
		<td><ct:FWLabel key='AFFILIES'/></td>
		<% if(JadeStringUtil.isEmpty(numAffilie)) { %>
		<td><ct:FWPopupList name="fromAffilie" 
				value='<%=viewBean.getFromAffilie()%>'
				className="libelle" 
				jspName="<%=jspLocation%>" 
				autoNbrDigit="<%=autoDigitAff%>" 
				size="15"
				minNbrDigit="3"/>
			 &nbsp;&nbsp;<ct:FWLabel key='FROM_TO'/>&nbsp;&nbsp;
		     <ct:FWPopupList name="toAffilie" 
					value='<%=viewBean.getToAffilie()%>'
					className="libelle" 
					jspName="<%=jspLocation%>" 
					autoNbrDigit="<%=autoDigitAff%>" 
					size="15"
					minNbrDigit="3"/>
		</td>
		<% } else { %>
		<td>
			<input name="fromAffilie"  value="<%=numAffilie%>" maxlength="15" size="15" class="disabled" readonly="readonly" />
			<input type="hidden" name="toAffilie"  value="<%=numAffilie%>"/>
		</td>
		<% } %>
		<TD>&nbsp;</TD>
	</TR>
	<tr>
		<TD><ct:FWLabel key='TYPE_DOCUMENT'/></TD>
		<TD>
			<SELECT id="typeDocument" name="typeDocument" onchange="changeTypeDoc();">
	        	<OPTION value="<%=DSLettreReclamationNss_doc.DOCUMENT_LETTRE_RECLAMATION %>"><ct:FWLabel key="LETTRE_RECLAMATION"/></OPTION>
	            <OPTION value="<%=DSLettreReclamationNss_doc.DOCUMENT_LETTRE_RAPPEL %>"><ct:FWLabel key="LETTRE_RAPPEL"/></OPTION>
			</SELECT>	
	    </TD>
		<TD>&nbsp;</TD>
	</TR>
	<tr>
		<TD>&nbsp;</TD>
		<TD><span id="delai" style='visibility:hidden;'><ct:FWLabel key='DELAI'/>&nbsp;&nbsp;<INPUT name="delaiRappel" id="delaiRappel" maxlength="4" size="4" type="text" style="text-align : left;" value="<%= jourRappel %>" onkeypress="return filterCharForPositivInteger(window.event);" /></span>
		<TD>&nbsp;</TD>
	</TR>
	<tr><TD colspan="3">&nbsp;<td></tr>
	<tr>
		<TD><ct:FWLabel key='TYPE_DECLARATION'/></TD>
		<% if(JadeStringUtil.isEmpty(typeDeclaration)) { %> 
		<TD><ct:FWCodeSelectTag name="typeDeclaration" defaut="<%=typeDeclaration%>" codeType="DRATYPDECL" /><TD>
		<% } else { %>
		<TD><input type="hidden" name="typeDeclaration" value="<%=typeDeclaration%>"/><input type="texte" value="<%=viewBean.getLibelleTypeDeclaration(typeDeclaration) %>" class="disabled" readonly="readonly"/><TD>
		<% } %>
		
		<TD>&nbsp;</TD>
	</TD>
	<TR> 
	   	<TD><ct:FWLabel key="ANNEE"/></TD>
	   	<% if(JadeStringUtil.isEmpty(typeDeclaration)) { %> 
	   	<TD><INPUT name="annee" id="annee" maxlength="4" size="4" type="text" style="text-align : left;" value="<%=anneeDS != null ? anneeDS : "" %>" onkeypress="return filterCharForPositivInteger(window.event);" /></TD>                   
	 	<% } else { %>
	   	<TD><INPUT name="annee" id="annee" maxlength="4" size="4" type="text" style="text-align : left;" value="<%=anneeDS != null ? anneeDS : "" %>" onkeypress="return filterCharForPositivInteger(window.event);" class="disabled" readonly="readonly"/></TD>                   
	 	<% } %>
	 	<TD>&nbsp;</TD>
	</TR> 
	<tr>
		<TD><ct:FWLabel key='DATE_DOCUMENT'/></TD>
		<TD><ct:FWCalendarTag name="dateDocument" value='<%=!JadeStringUtil.isEmpty(viewBean.getDateDocument()) ? viewBean.getDateDocument() : ""%>'/></TD>
		<TD>&nbsp;</TD>
	</TR>
	<tr>
		<TD><ct:FWLabel key='GENRE_EDITION'/></TD>
		<TD>
			<SELECT id="genreEdition" name="genreEdition">
	        	<OPTION value="<%=DSLettreReclamationNss_doc.GENRE_DOCUMENT_SIMULATION%>"><ct:FWLabel key="SIMULATION"/></OPTION>
	            <OPTION value="<%=DSLettreReclamationNss_doc.GENRE_DOCUMENT_DEFINITIF%>" ><ct:FWLabel key="DEFINITIF"/></OPTION> 
			</SELECT>
		</TD>
		<TD>&nbsp;</TD>
	</TR>
	<TR> 
	   	<TD><ct:FWLabel key="OBSERVATION"/></TD>
	   	<TD><textarea name="observation" cols="85" rows="4" ><%=!JadeStringUtil.isEmpty(viewBean.getObservation()) ? viewBean.getObservation() : ""%></textarea></TD>                   
	 	<TD>&nbsp;</TD>
	</TR> 
	<TR> 
		<TD><ct:FWLabel key="EMAIL"/></TD>
		<TD><INPUT name="email" size="40" type="text" style="text-align : left;" value="<%= viewBean.getEmail() != null ? viewBean.getEmail() : "" %>"></TD>                        
	 	<TD>&nbsp;</TD>
	</TR>
	<TR> 
	 	<TD colspan="3">&nbsp;</TD>
	</TR>      				

<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>