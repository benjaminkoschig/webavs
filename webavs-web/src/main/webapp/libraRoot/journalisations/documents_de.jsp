<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="GLI0005";

	globaz.libra.vb.journalisations.LIDocumentsExecutionViewBean viewBean = (globaz.libra.vb.journalisations.LIDocumentsExecutionViewBean)session.getAttribute("viewBean");
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT type="text/javascript"> 

	function cancel(){
       document.forms[0].elements('userAction').value="<%=globaz.libra.servlet.ILIActions.ACTION_ECHEANCES_DOC %>.chercher";
   	   document.forms[0].submit();
	}
	
	function validate(){
		document.forms[0].elements('userAction').value="<%=globaz.libra.servlet.ILIActions.ACTION_ECHEANCES_DOC %>.genererDocuments";
		document.forms[0].submit();
	}
	
	
	function upd(){
	     ;
	}
	
	function add(){
		;
	}
	
	function del(){
	   ;  	   
	}

	function postInit(){
	}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="ECRAN_DOC_EXE_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	<TR>
		<TD colpsan="6" align="center">
			<table width="95%">
  				<tr>
					<td colspan="6" class="areaGlobazBlue">
					    <table width="100%">							
							<TR>
								<TD width="20%"><ct:FWLabel key="ECRAN_DOC_EXE_EMAIL"/></TD>
								<TD><INPUT type="text" name="eMailAddress" value="<%=controller.getSession().getUserEMail()%>" class="libelleLong"></TD>
							</TR>
							<TR>
								<TD><ct:FWLabel key="ECRAN_DOC_EXE_DATE"/></TD>
								<TD><ct:FWCalendarTag name="dateExecution" value="<%= globaz.globall.util.JACalendar.todayJJsMMsAAAA() %>"/></TD>
							</TR>							
						</table>
					</td>
				</tr>				
				<tr><td colspan="6"><br/></td></tr>		
				<tr>
					<td colspan="6" class="ongletLightBlue">
					    <table width="100%">							
							<TR>
								<TD colspan="34">
									<b><ct:FWLabel key="ECRAN_DOC_LISTE_TITRE"/></b>
								</TD>
							</TR>
							<TR><td colspan="4">&nbsp;</td></TR>
							<TR>
								<TH><ct:FWLabel key="ECRAN_DOC_EXE_TYPE"/></TH>
								<TH><ct:FWLabel key="ECRAN_DOC_EXE_LIBELLE"/></TH>
								<TH><ct:FWLabel key="ECRAN_DOC_EXE_DETAIL_DOC"/></TH>
								<TH><ct:FWLabel key="ECRAN_DOC_EXE_TIERS"/></TH>
							</TR>
							<% 	for (java.util.Iterator iterator = viewBean.getListeIdRappel().iterator(); iterator.hasNext();) {
									String idJO = (String) iterator.next();
									viewBean.loadJournalisation(idJO);
							%>
							<TR>
								<TD><%= viewBean.getSession().getCodeLibelle(viewBean.getJoDetail().getComplementJournal().getValeurCodeSysteme()) %></TD>
								<TD><%= viewBean.getJoDetail().getJournalisation().getLibelleAffichage() %></TD>
								<TD><%= viewBean.getNomFormule(idJO) %></TD>
								<TD><%= viewBean.getJoDetail().getDetailTiersLigne() %> </TD>
							</TR>						
							<% } %>															
						</table>
					</td>
				</tr>		
			</TABLE>
		</TD>
	</TR>			
	<TR><TD colspan="4"><BR></TD></TR>	
						
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>

