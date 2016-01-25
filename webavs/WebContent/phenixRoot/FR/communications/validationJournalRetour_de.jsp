<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	boolean bButtonCustom = false;
	String btnCustomLabel = "Recalculer";
    bButtonValidate = true;
    btnNewLabel = "Recalculer";
	globaz.phenix.db.principale.CPDecisionViewBean viewBean = (globaz.phenix.db.principale.CPDecisionViewBean) session.getAttribute("viewBean");
	boolean isConjointAuDepart;
	if((viewBean.getIdConjoint() != null) && (!viewBean.getIdConjoint().equals("0"))) {
		isConjointAuDepart = true;
	}else{
		isConjointAuDepart = false;
	}
	idEcran = "CCP1023";
	String idRetour = request.getParameter("idRetour");
	String idValidation = request.getParameter("selectedId");
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT type="text/javascript"> 
function add() {}
function upd() {}
function validate() {
	state = validateFields(); 
	if (document.forms[0].elements('division2').value==false)
		document.forms[0].elements('idConjoint').value = "0";
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="phenix.communications.validationJournalRetour.afficher";
	else
		document.forms[0].elements('userAction').value="phenix.communications.validationJournalRetour.modifier";
	return (state);
}
function cancel() {
	document.forms[0].elements('userAction').value="phenix.communications.validationJournalRetour.chercher";
}
function del() {}
function init(){}
function recalculer() {}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Modification de la décision du journal<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
            				<TD nowrap width="128">
            					<ct:ifhasright element="pyxis.tiers.tiers.diriger" crud="r">
            					<A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.diriger&selectedId=<%=viewBean.getIdTiers()%>" class="external_link">Tiers</A>
            					</ct:ifhasright>
            				</TD>
            				<TD nowrap colspan="2">
								<INPUT type="text" name="nom" tabindex="-1" value="<%=viewBean.getNomPrenom()%>" class="libelleLongDisabled" readonly>
	     					</TD>
							<TD width="10"></TD>
            				<TD nowrap width="50" align="left">Affilié</TD>
            				<TD nowrap >
								<INPUT type="text" name="numAffilie" tabindex="-1" value="<%=viewBean.getAffiliation().getAffilieNumero()%>" class="libelleLongDisabled" readonly>
	    					</TD>
						</TR>
						<TR>
							<TD nowrap width="128"></TD>
           					<TD nowrap colspan="2">
								<INPUT type="text" name="localite" tabindex="-1" class="libelleLongDisabled" value="<%=viewBean.loadTiers().getLocalite()%>" readonly>
							</TD>
							<TD width="10"></TD>
            				<TD nowrap width="50" align="left">NSS</TD>
            				<TD nowrap>
								<INPUT type="text" name="numAvs" tabindex="-1" value="<%=viewBean.getNumAVS()%>" class="libelleLongDisabled" readonly>
	     					</TD>
							<TD>
	     					</TD>
          				</TR>
          				<TR>
          					<TD nowrap width="128"></TD>
          					<TD nowrap width="128"></TD>
          					<TD width="10"></TD>
          					<TD nowrap width="128"></TD>
            				<TD nowrap width="100" align="left">N° Contribuable</TD>
            				<TD nowrap>
								<INPUT type="text" name="numAvs" tabindex="-1" value="<%=viewBean.getNumContribuable()%>" class="libelleLongDisabled" readonly>
	     					</TD>
          				</TR>
          				<TR>
          					<TD>Groupe Taxation</TD>
          					<TD><INPUT type="text" name="grpTaxation" tabindex="-1" value="<%=viewBean.getGrpTaxation(request.getParameter("idRetour"))%>" class="libelleLongDisabled" readonly></TD>
          				</TR>
          				<TR>
          					<TD>Groupe Extraction</TD>
          					<TD nowrap width="100"><INPUT type="text" name="grpExtraction" tabindex="-1" value="<%=viewBean.getGrpExtraction(request.getParameter("idRetour"))%>" class="libelleLongDisabled" readonly></TD>
          				</TR>
       					 <TR>
            				<TD>Revenu ou rente</TD>
            				<TD><INPUT name="revenu1" id="revenu1" type="text" value="<%=viewBean.getRevenu1()%>" class="montant"></TD>
       					 </TR>
	          			<TR id="ligneRevenuAutre">
	          			<%
			      		if(((globaz.phenix.application.CPApplication) viewBean.getSession().getApplication()).isRevenuAgricole()) {
			      		%>
				            <TD><%=viewBean.getSession().getApplication().getLabel("REVENU_AGRICOLE", "FR")%></TD>
				        <% } else { %>
							<TD><%=viewBean.getSession().getApplication().getLabel("REVENU_AUTRE", "FR")%></TD>
				        <% } %>
				            <TD><INPUT name="revenuAutre1" type="text" value="<%=viewBean.getRevenuAutre1()%>" class="montant"></TD>
						</TR>
						
						<%if(!viewBean.isNonActif()){%>
       					 <TR>
            				<TD>Capital propre engagé dans l'entreprise</TD>
            				<TD>
            				<INPUT name="capital" type="text" value="<%=viewBean.getCapital()%>" class="montant"></TD>
       					 </TR>
       					 <%}else{%>      					 
       					 <TR>
            				<TD nowrap width="200">Fortune</TD>
            				<TD nowrap width="200"><INPUT name="fortuneTotale" type="text" value="<%=viewBean.getFortuneTotale()%>" class="montant"></TD>
       					 </TR>
       					 <%}%>
       				     <TR>
            				<TD nowrap width="200">Période</TD>
	       					 <TD nowrap >
					            <ct:FWCalendarTag name="debutDecision" 
									value="<%=viewBean.getDebutDecision()%>" 
									errorMessage="la date de début est incorrecte"
									doClientValidation="CALENDAR"
								  	/>
								    <ct:FWCalendarTag name="finDecision" 
									value="<%=viewBean.getFinDecision()%>"
									errorMessage="la date de fin est incorrecte"
									doClientValidation="CALENDAR"
						 			/>
						 	</TD>
	 					</TR>
       					 <TR>
            				<TD nowrap width="200">Conjoint</TD>
            				            				<TD nowrap width="100">
            				<INPUT name="division2" type="checkBox" 
	            				<% if(viewBean.getDivision2()== Boolean.TRUE) {%>
	            					checked="checked"
	            				<%}%>
	            			>
            				</TD>

       					 </TR>  
       					 <% String redirectCommunicationURL = servletContext + "/phenix?userAction=phenix.communications.apercuCommunicationFiscaleRetour.afficher&idRetour=" + idRetour;%> 
       					 <TR>
       					 		<TD>
       					 			<ct:ifhasright element="phenix.communications.apercuCommunicationFiscaleRetour.afficher" crud="r">
       					 			<a href="<%=redirectCommunicationURL%>" class="external_link">Communication en retour</a>
       					 			</ct:ifhasright>
       					 		</TD>
       					 </TR>  
						 <% String redirectDecisionURL = servletContext + "/phenix?userAction=phenix.principale.decision.chercher&selectedId="+idValidation+"&idTiers="+viewBean.getIdTiers()+"&idAffiliation="+viewBean.getIdAffiliation();%> 
       					 <TR>
       					 		<TD>
       					 			<ct:ifhasright element="phenix.principale.decision.chercher" crud="r">
       					 			<a href="<%=redirectDecisionURL%>" class="external_link">Décisions de la personne</a>
       					 			</ct:ifhasright>
       					 		</TD>
       					 </TR>
					
       					 <INPUT name="idDecision" type="hidden" value="<%=viewBean.getIdDecision()%>">
       					 <INPUT name="idConjoint" type="hidden" value="<%=viewBean.getIdConjoint()%>">
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<% if(bButtonCustom){ %><input class="btnCtrl" id="btnCustom" type="button" value="<%=btnCustomLabel%>" onclick="action(UPDATE); action(COMMIT); recalculer();"> <%}%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>