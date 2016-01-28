<%-- tpl:insert page="/theme/process.jtpl" --%><%@page import="globaz.commons.nss.NSUtil"%>
<%@page import="globaz.corvus.vb.ci.REModificationNSSExConjointViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PRE0103";
	
	userActionValue="corvus.ci.modificationNSSExConjoint.executer";
	
	REModificationNSSExConjointViewBean viewBean = (REModificationNSSExConjointViewBean)(session.getAttribute("viewBean"));
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="JavaScript">

function postInit(){
	$("#nouveauNSS" ).focus();	
}

function formatChampNouveauNSS(){
	
	numAvs = $("#nouveauNSS" ).val();	
	
	if(numAvs != null){
		
		numAvsNonFormate = numAvs.replace(/ /g,"");
		numAvsNonFormate = numAvsNonFormate.replace(/\./g,"");	
		
		if(numAvsNonFormate.length == 13){
			numAvs = numAvsNonFormate.substring(0,3) + "." +	 numAvsNonFormate.substring(3,7) + "." + numAvsNonFormate.substring(7,11) + "." + numAvsNonFormate.substring(11,13)	;	
		} else if(numAvsNonFormate.length == 11){
			numAvs = numAvsNonFormate.substring(0,3) + "." +	 numAvsNonFormate.substring(3,5) + "." + numAvsNonFormate.substring(5,8) + "." + numAvsNonFormate.substring(8,11) ;
		}	
	}
	
	$("#nouveauNSS" ).val(numAvs);	
} 





</SCRIPT>
<%-- /tpl:put --%>

<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>		
<ct:FWLabel key="JSP_MODIFICATION_NSS_EXCONJOINT_TITRE"/> 
			<%-- /tpl:put --%>
			
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
					
						<TR>
							<TD>
								<ct:FWLabel key="JSP_MODIFICATION_NSS_EXCONJOINT_REQUERANT"/>
							</TD>
							<TD >
								<re:PRDisplayRequerantInfoTag 
										session="<%=objSession%>" 
										idTiers="<%=viewBean.getIdTiers()%>"
										style="<%=globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag.STYLE_CONDENSED_WITHOUT_LABEL%>"/>
							</TD>
						</TR>
						
						<TR>
							<TD>
								<ct:FWLabel key="JSP_MODIFICATION_NSS_EXCONJOINT_NUMERO_RASSEMBLEMENT"/>
							</TD>
							<TD>
								<INPUT type="text" class="disabled"  id="forIdRCI" name="forIdRCI" value="<%=viewBean.getForIdRCI()%>" readonly="readonly" >
							</TD>
						</TR>
																	
						<TR>
							<TD>&nbsp;</TD>
							<TD>&nbsp;</TD>								
						</TR>
						
						<TR>
							<TD>
								<ct:FWLabel key="JSP_MODIFICATION_NSS_EXCONJOINT_NOUVEAU_NSS"/>&nbsp;
							</TD>
							<TD>
								<INPUT type="text" id="nouveauNSS" name="nouveauNSS" value="<%=viewBean.getNouveauNSS()%>" data-g-string="mandatory:true,sizeMax:16" onblur="formatChampNouveauNSS()"> 
							</TD>
						</TR>
									
						<TR>
							<TD>
								<ct:FWLabel key="JSP_MODIFICATION_NSS_EXCONJOINT_ANNEE_DEBUT"/>&nbsp;
							</TD>
							<TD>
								<INPUT type="text" style="width: 4em;" id="anneeDebut" name="anneeDebut" value="<%=viewBean.getAnneeDebut()%>" data-g-integer="mandatory:true,sizeMax:4"> &nbsp;&nbsp;
								<ct:FWLabel key="JSP_MODIFICATION_NSS_EXCONJOINT_ANNEE_FIN"/>
								&nbsp;&nbsp;<INPUT type="text" style="width: 4em;" id="anneeFin" name="anneeFin" value="<%=viewBean.getAnneeFin()%>" data-g-integer="mandatory:true,sizeMax:4">
							</TD>
						</TR>
						
				
						
						
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>