<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%
	idEcran="CCP1007";
	//Récupère le viewBean s'il y en a un et affiche le message d'erreur
	Object vBean = session.getAttribute("viewBean");
	if (vBean != null) {
		String className = vBean.getClass().getName();
		if (className.equals("globaz.phenix.db.communications.CPJournalRetourViewBean")) {
			globaz.phenix.db.communications.CPJournalRetourViewBean viewBean = (globaz.phenix.db.communications.CPJournalRetourViewBean) vBean;
			if (viewBean.getMsgType().equals(globaz.framework.bean.FWViewBeanInterface.ERROR) == true) {
			%>
				<SCRIPT language="JavaScript">
					alert ("<%=viewBean.getMessage()%>");
				</SCRIPT>
			<%				
			}
		}
	}
	
	bButtonNew = false; 
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CP-OnlyDetail"/>
<SCRIPT>
// menu 
top.document.title = "Steuermeldungsjournale zurück"
usrAction = "phenix.communications.journalRetour.lister";
bFind = true;

function onClickCheckBox(monAction){
	var checkboxes = top.fr_main.fr_list.document.getElementsByName("listIdJournalRetour");

	for(var i=0; i<checkboxes.length;i++){
		if (checkboxes(i).checked && checkboxes(i).value != '') {
			var inputTag = document.createElement('input');
			inputTag.type='hidden';
			inputTag.name='listIdJournalRetour';
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
	$("#forStatus").change(function () {
		if($(this).val()==611009){
			$("#estAbandonne").attr('checked',true);
			$("#afficheComptabiliseTotal").attr('checked',false);
		}else if($(this).val()=='611008'){
			$("#afficheComptabiliseTotal").attr('checked',true);
			$("#estAbandonne").attr('checked',false);
		}
		else if($(this).val()!=''){
			$("#estAbandonne").attr('checked',false);
			$("#afficheComptabiliseTotal").attr('checked',false);
		}
	});
});
</SCRIPT>


<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Steuermeldungsjournale zurück<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
	<TR> <%-- valign="middle" align="center"--%>
				<TD nowrap width="80">Nummer</TD>
		<TD nowrap width="70%" align="left"> 
			<INPUT name="forIdJournal" class="libelleCourt" >
        <TD nowrap width="100"></TD>
        <TD nowrap width="20%" align="left">Status</TD>
            <TD width="184"><ct:FWCodeSelectTag name="forStatus"
			    wantBlank="true"
			    defaut=""
			    codeType="CPETJOUR"
			    libelle="libelle"
			/>
			</TD>
	</TR>
	<TR>
            <TD nowrap width="80">Sortiert nach</TD>
            <TD nowrap>
				<SELECT name="orderBy">
					<OPTION value="NUM" selected="selected">Journal-Nr.</OPTION>									
					<OPTION value="NOM" >Name des Journals</OPTION>									
					<OPTION value="DATERECEPTION">Empfangsdatum</OPTION>
					<OPTION value="NBCOMMUNICATION">Anzahl Steuermeldungen</OPTION>
					<OPTION value="STATUS">Status</OPTION>
				</SELECT>
            </TD>
            <TD nowrap width="100"></TD>
            <TD nowrap width="20%" align="left">Journale die vollständig gebucht sind anzeigen</TD>
            <td><INPUT type="checkbox" name="afficheComptabiliseTotal" id="afficheComptabiliseTotal"></td>         
          </TR>
						
					     <TR>
     
    <TD nowrap width="80" >Annulliert</TD>
        <TD nowrap width="184" align="left" ><input type="checkbox" name="estAbandonne" id="estAbandonne"/></TD>
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
								<ct:ifhasright element="phenix.communications.journalRetour.abandonnerEnMasse" crud="u">
									<INPUT type="button" name="btnAbandon" value="Abbrechen" onClick="onClickCheckBox('phenix.communications.journalRetour.abandonnerEnMasse');" >	
								</ct:ifhasright>
								<ct:ifhasright element="phenix.communications.journalRetour.reinitialiserEnMasse" crud="u">
									<INPUT type="button" name="btnReinit" value="Zurücksetzen" onClick="onClickCheckBox('phenix.communications.journalRetour.reinitialiserEnMasse');" >
								</ct:ifhasright>
								<ct:ifhasright element="phenix.communications.journalRetour.genererEnMasse" crud="u">
									<INPUT type="button" name="btnGenerer" value="Generieren" onClick="onClickCheckBox('phenix.communications.journalRetour.genererEnMasse');" >
								</ct:ifhasright>&nbsp;
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
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>