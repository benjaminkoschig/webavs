<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@page import="globaz.pyxis.constantes.IConstantes"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%	
	idEcran="CCP1033";
	actionNew = "?userAction=phenix.communications.rejets.afficher&_method=add";
	rememberSearchCriterias=true;
	int autoDigiAff = globaz.phenix.util.CPUtil.getAutoDigitAff(session);
	String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
	
	String numAffilie = request.getParameter("likeNumAffilie");
	bButtonNew = false;
%>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<script>
</script>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>


<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CP-OnlyDetail"/>
<SCRIPT language="Javascript">
bFind=true;
detailLink = servlet+"?userAction=phenix.communications.rejets.afficher";
usrAction="phenix.communications.rejets.lister";

function postInit() {
}

function onClickCheckBox(monAction){
	var checkboxes = top.fr_main.fr_list.document.getElementsByName("listIdRetour");

	for(var i=0; i<checkboxes.length;i++){
		if (checkboxes(i).checked && checkboxes(i).value != '') {
			var inputTag = document.createElement('input');
			inputTag.type='hidden';
			inputTag.name='listIdRetour';
			inputTag.value=checkboxes(i).value;
			document.forms[0].appendChild(inputTag);
		}		
	}	

	var oldUserAction = document.forms[0].elements.userAction.value;
	var oldTarget = document.forms[0].target;
	document.forms[0].target = "fr_main";
	document.forms[0].method = "post";
	setUserAction(monAction);
	document.forms[0].submit();
}

$(function(){
	$("#forEtat").change(function () {
		if($(this).val()== '622003'){
			$("#wantEnvoye").attr('checked',true);
			$("#wantAbandonne").attr('checked',false);
		}else if($(this).val()=='622004'){
			$("#wantEnvoye").attr('checked',false);
			$("#wantAbandonne").attr('checked',true);
		}
		else if($(this).val()!=''){
			$("#wantEnvoye").attr('checked',false);
			$("#wantAbandonne").attr('checked',false);
		}
	});
});
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Rejets des communications fiscales (SEDEX)<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
				            <TD nowrap>N°Affilié(référence caisse)</TD>
							<td>
				            	<INPUT name="likeYourBusinessReferenceId" class="libelleShort"/>
					       	</td>
					       	<td nowrap>Référence métier(FISC)</td>
							<TD width=50%>
								<INPUT name="likeOurBusinessReferenceId" class="libelleShort"/>
							</TD>
					     	<TD nowrap>Canton</TD>
					     	<TD nowrap>
							<%
								java.util.HashSet except = new java.util.HashSet();
								except.add(IConstantes.CS_LOCALITE_ETRANGER);
							%>
							<ct:FWCodeSelectTag name="forCanton"
							        defaut=""
									wantBlank="<%=true%>"
							        codeType="PYCANTON"
									libelle="codeLibelle"
									except="<%=except%>"
							/>
					     	</TD>
					    </TR>
					    <TR>
				            <TD nowrap>N°Contribuable</TD>
							<td>
				            	<INPUT name="likeNumContribuable" class="libelleShort"/>
					       	</td>
					       	<td nowrap>Année</td>
							<TD width=50%>
								<INPUT name="forAnnee" class="numeroCourt"/>
							</TD>
					    </TR>
						<TR>
				            <td nowrap>message ID</td>
							<TD width=50%>
								<INPUT name="likeMessageId" class="libelleShort"/>
							</TD>
							<TD nowrap>référence message ID</TD>
					     	<TD nowrap>
								<INPUT name="likeReferenceMessageId" class="libelleShort"/>
					     	</TD> 
					     	<TD nowrap>Numéro personne</TD>
					     	<TD nowrap>
								<INPUT name="likePersonId" class="libelleShort"/>
					     	</TD> 
				          </TR>
				         <TR>
				         	<TD nowrap>Nom</TD>
					     	<TD nowrap>
								<INPUT name="likeOfficialName" class="libelleShort"/>
					     	</TD> 
				            <TD nowrap>Prénom</TD>
				            <TD width=50%>
								<INPUT type="text" name="likeFirstName"  class="libelleShort">
							</TD>
				            <TD nowrap>Raison du rejet</TD>
				            <TD nowrap>
				            <%
			        		java.util.Vector listeRejets = globaz.phenix.db.communications.CPRejets.getListRejets(session);
							%>
							<ct:FWListSelectTag name="forReasonOfRejection"
							defaut=""
	            			data="<%=listeRejets%>"/>
				            </TD>
				        </TR>
				        <TR>
				        	<TD nowrap>Etat</TD>
				            <TD nowrap>
				            <ct:FWCodeSelectTag name="forEtat"
								defaut=""
								wantBlank="<%=true%>"
							    codeType="CPETATREJ"
								/>
				            </TD>
				        </TR>
				       <TR>
							<TD height="2">Afficher envoyés</TD>
							<TD nowrap height="31" width="259"><input type="checkbox" name="wantEnvoye" id="wantEnvoye"></TD>
							<TD width height="2">Afficher abandonnés</TD>
							<TD nowrap height="31" width="259"><input type="checkbox" name="wantAbandonne" id="wantAbandonne"></TD>
					   </TR>

	 					<%-- /tpl:put --%>
<%-- On remplace l'include file="/theme/find/bodyButtons.jspf" par son code pour pouvoir afficher des boutons a gauche --%>
						<TR>
							<TD height="20">
								<INPUT type="hidden" name="userAction" value="">
								<INPUT type="hidden" name="_sl" value="">
								<INPUT type="hidden" name="_method" value="">
								<INPUT type="hidden" name="_valid" value="">
								<INPUT type="hidden" name="colonneSelection" value="<%=request.getParameter("colonneSelection")%>">
							</TD>
						</TR>
					</TBODY>
				</TABLE>
			</TD>
		</TR>
		<TR>
			<TD bgcolor="#FFFFFF" colspan="2">
				<TABLE width="100%" cellspacing="0" cellpadding="0">
					<TBODY>
						<TR>
							<TD align="left">
								<ct:ifhasright element="phenix.communications.rejets.envoiSedex" crud="u">
								<INPUT type="button" name="Envoi" value="Envoi Sedex" onClick="onClickCheckBox('phenix.communications.rejets.envoiSedex');" >
								</ct:ifhasright>	
							</TD>
							<TD align="right">
								<%if (bShowExportButton) {%>
									<INPUT type="button" name="btnExport" value="<%=btnExportLabel%>" onClick="onExport();">
								<%}%>
								<%if (bButtonFind) {%>
									<INPUT type="submit" name="btnFind" value="<%=btnFindLabel%>">
								<%} if (bButtonNew) {%>
									<INPUT type="button" name="btnNew" value="<%=btnNewLabel%>" onClick="onClickNew();btnNew.onclick='';document.location.href='<%=actionNew%>'">
								<%}%>
							</TD>
						</TR>
					</TBODY>
				</TABLE>	
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<div style="color:red">Il y a <span id="listCount">0</span> rejet(s) sélectionnée(s)</div>
			<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>