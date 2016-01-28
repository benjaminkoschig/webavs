	<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
	<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%@ taglib uri="/libraRoot/librataglib.tld" prefix="li" %>
<%

	idEcran="GLI0007";

	globaz.libra.vb.journalisations.LIJournalisationsDetailViewBean viewBean = (globaz.libra.vb.journalisations.LIJournalisationsDetailViewBean) session.getAttribute("viewBean");

	bButtonUpdate = false;
	bButtonDelete = false;
	
	String idTiers = request.getParameter("idTiers");
	String idDossier = request.getParameter("idDossier");

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>



<script language="JavaScript">

  function readOnly(flag) {
  	// empeche la propriete disabled des elements etant de la classe css 'forceDisable' d'etre modifiee
    for(i=0; i < document.forms[0].length; i++) {
        if (!document.forms[0].elements[i].readOnly &&
        	document.forms[0].elements[i].className != 'forceDisable' &&
        	document.forms[0].elements[i].type != 'hidden') {
            document.forms[0].elements[i].disabled = flag;
        }
    }
  }

  function add() {
  }

  function upd() {
  }

  function cancel() {
		document.forms[0].elements('userAction').value="<%=globaz.libra.servlet.ILIActions.ACTION_JOURNALISATIONS_RC %>.chercher"; 	  	
  }
	   
  function validate() { 
	    state = true;
	    if (document.forms[0].elements('_method').value == "add"){
	        document.forms[0].elements('userAction').value="<%=globaz.libra.servlet.ILIActions.ACTION_JOURNALISATIONS_DE%>.ajouter";
	    }else{
	    	document.forms[0].elements('userAction').value="<%=globaz.libra.servlet.ILIActions.ACTION_JOURNALISATIONS_DE%>.modifier";
	    }
	  	return state;	    
  }
  
  function init(){
  }

  function postInit(){
  }

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="ECRAN_JOU_DE_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	<TR>
	
		<!-- Si nouveau -->
		<% if (JadeStringUtil.isBlankOrZero(viewBean.getJournalisation().getIdJournalisation())){ %>
	
		<TD colpsan="6" align="center">
			<table width="95%">
  				<tr>
					<td colspan="6" class="ongletLightBlue">
					    <table width="100%">
							<tr>
								<td width="20%"><b><ct:FWLabel key="ECRAN_JOU_DE_TIERS"/></b></td>
								<td width="80%">
									<b><%= viewBean.getDetailTiersLigneNew(idTiers) %></b>
									<input type="hidden" name="idTiers" value="<%= idTiers %>">
									<input type="hidden" name="idDossier" value="<%= idDossier %>">
								</td>
							</tr>					
						</table>
					</td>
				</tr>
				<tr><td colspan="6"><br/></td></tr>
  				<tr>
					<td colspan="6" class="areaGlobazBlue">
					    <table width="100%">
							<tr>
								<td width="20%"><ct:FWLabel key="ECRAN_JOU_DE_DATE"/></td>
								<td width="80%"><%= globaz.globall.util.JACalendar.todayJJsMMsAAAA() %></td>
							</tr>
							<tr>
								<td valign="top"><ct:FWLabel key="ECRAN_JOU_DE_LIBELLE"/></td>
								<td>
									<textarea cols="93" rows="4" name="libelle"></textarea>
								</td>
							</tr>								
						</table>
					</td>
				</tr>
				<tr><td colspan="6"><br/></td></tr>					
  				<tr>
					<td colspan="6" class="areaGlobazBlue">
					    <table width="100%">
							<tr>
								<td width="20%" valign="top"><ct:FWLabel key="ECRAN_ECH_DE_REMARQUE"/></td>
								<td width="80%">
									<textarea cols="93" rows="8" name="remarque"><%= viewBean.getUtils().getRemarque() %></textarea>
								</td>
							</tr>																
						</table>
					</td>
				</tr>													
			</TABLE>
		</TD>
		
		<!-- Si détail existant -->
		<% } else { %>
		
		<TD colpsan="6" align="center">
			<table width="95%">
  				<tr>
					<td colspan="6" class="ongletLightBlue">
					    <table width="100%">
							<tr>
								<td width="20%"><b><ct:FWLabel key="ECRAN_JOU_DE_TIERS"/></b></td>
								<td width="80%"><b><%= viewBean.getDetailTiersLigne()%></b></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="6" class="ongletLightBlue">
					    <table width="100%">
							<tr>
								<td width="20%"><b><ct:FWLabel key="ECRAN_JOU_DE_TYPE"/></b></td>
								<td width="80%"><b><%= viewBean.getISession().getCodeLibelle(viewBean.getJournalisation().getCsTypeJournal()) %></b></td>
							</tr>
						</table>
					</td>
				</tr>					
				<tr><td colspan="6"><br/></td></tr>
  				<tr>
					<td colspan="6" class="areaGlobazBlue">
					    <table width="100%">
							<tr>
								<td width="20%"><ct:FWLabel key="ECRAN_JOU_DE_DATE"/></td>
								<td width="80%"><%= viewBean.getJournalisation().getDate()%></td>
							</tr>
							<tr>
								<td valign="top"><ct:FWLabel key="ECRAN_JOU_DE_LIBELLE"/></td>
								<td>
									<textarea cols="93" rows="4" name="libelle"><%= viewBean.getJournalisation().getLibelle() %></textarea>
								</td>
							</tr>																			
						</table>
					</td>
				</tr>				
				<% if (viewBean.getComplementJournal().getValeurCodeSysteme().equals(globaz.journalisation.constantes.JOConstantes.CS_JO_AVS_FMT_ENVOI_MULTIPLE)){ %>
					
					<tr><td colspan="6"><br/></td></tr>	
	  				<tr>
						<td colspan="6" class="areaGlobazBlue">
						    <table width="100%">
							    <tr>
							    	<td width="20%" valign="top"><ct:FWLabel key="ECRAN_ECH_DE_DETAIL"/></td>
							    	<td width="80%">
							    		<table>
									    	<th><ct:FWLabel key="ECRAN_ECH_DE_DATE_REC"/></th>
									    	<th><ct:FWLabel key="ECRAN_ECH_DE_LIBELLE"/></th>
															  				
							  				<%   				
							  					for (java.util.Iterator iterator = viewBean.getEcheanceMultipleManager().iterator(); iterator.hasNext();) {
													globaz.libra.db.journalisations.LIEcheancesMultiple echeanceMul = (globaz.libra.db.journalisations.LIEcheancesMultiple) iterator.next();		
											%>
													<tr>
														<td align="center"><%= echeanceMul.getDateReception() %></td>
														<td width="520"> <%=echeanceMul.getLibelle()%> (<%= echeanceMul.getAnnee() %>)</td>
													</tr>
											<%		
												}
							  				%>	
							  			</table>							    				
							    	</td>
		  						</tr>													
							</table>
						</td>
					</tr>					
				<% } %>	
				<tr><td colspan="6"><br/></td></tr>					
  				<tr>
					<td colspan="6" class="areaGlobazBlue">
					    <table width="100%">
							<tr>
								<td width="20%" valign="top"><ct:FWLabel key="ECRAN_ECH_DE_REMARQUE"/></td>
								<td width="80%">
									<textarea cols="93" rows="8" name="remarque"><%= viewBean.getUtils().getRemarque() %></textarea>
								</td>
							</tr>																
						</table>
					</td>
				</tr>																
			</TABLE>
		</TD>		
		
		<% } %>
		
	</TR>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>