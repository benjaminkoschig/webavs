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
top.document.title = "Journaux des communications retour"
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
				<%-- tpl:put name="zoneTitle" --%>Journaux des communications en retour<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
	<TR> <%-- valign="middle" align="center"--%>
		<TD nowrap width="80">Numéro</TD>
		<TD nowrap width="70%" align="left"> 
			<INPUT name="forIdJournal" class="libelleCourt" >
        </TD>
        <TD nowrap width="100"></TD>
        <TD nowrap width="20%" align="left">Etat</TD>
            <TD width="184"><ct:FWCodeSelectTag name="forStatus"
			    wantBlank="true"
			    defaut=""
			    codeType="CPETJOUR"
			    libelle="libelle"
			/>
			</TD>
	</TR>
	<TR>
            <TD nowrap width="80">Trier par</TD>
            <TD nowrap>
				<SELECT name="orderBy">
					<OPTION value="NUM" selected="selected">N° du journal</OPTION>									
					<OPTION value="NOM" >Nom du journal</OPTION>									
					<OPTION value="DATERECEPTION">Date de réception</OPTION>
					<OPTION value="NBCOMMUNICATION">Nombre de communication</OPTION>
					<OPTION value="STATUS">Etat</OPTION>
				</SELECT>
            </TD>
            <TD nowrap width="100"></TD>
            <TD nowrap width="20%" align="left">Afficher les journaux totalement comptabilisé</TD>
            <td><INPUT type="checkbox" id="afficheComptabiliseTotal" name="afficheComptabiliseTotal"></td> 
          </TR>
     <TR>
     
    <TD nowrap width="80" >Abandonné</TD>
        <TD nowrap width="184" align="left" ><input type="checkbox" id="estAbandonne" name="estAbandonne" /></TD>
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
									<INPUT type="button" name="btnAbandon" value="Abandonner" onClick="onClickCheckBox('phenix.communications.journalRetour.abandonnerEnMasse');" >	
								</ct:ifhasright>
								<ct:ifhasright element="phenix.communications.journalRetour.reinitialiserEnMasse" crud="u">
									<INPUT type="button" name="btnReinit" value="Réinitialiser" onClick="onClickCheckBox('phenix.communications.journalRetour.reinitialiserEnMasse');" >
								</ct:ifhasright>
								<ct:ifhasright element="phenix.communications.journalRetour.genererEnMasse" crud="u">
									<INPUT type="button" name="btnGenerer" value="Générer" onClick="onClickCheckBox('phenix.communications.journalRetour.genererEnMasse');" >
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