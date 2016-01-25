<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%

	globaz.libra.vb.journalisations.LIEcheancesJointDossiersListViewBean viewBean = (globaz.libra.vb.journalisations.LIEcheancesJointDossiersListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();

	detailLink = "libra?userAction="+globaz.libra.servlet.ILIActions.ACTION_ECHEANCES_DOC + ".afficher&selectedId=";
	
	menuName = "li-menuprincipal";
	menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");

%>
<SCRIPT language="JavaScript">
var buttonPrint = top.fr_main.document.getElementById("btnExecuter");

function selectRappel(){
  var elems = document.getElementsByName("checkBox");
  for(var i=0;i<elems.length;i++) {
    if (document.getElementById("selectionRappel").value == "ON" ) {
    	elems(i).checked = false;
    } else {
    	elems(i).checked = true;
    }
  }
  if (document.getElementById("selectionRappel").value == "ON" ) {
		document.getElementById("selectionRappel").value = "OFF";
		document.getElementById("oui").style.display = "none";
		document.getElementById("non").style.display = "block";
    } else {
		document.getElementById("selectionRappel").value = "ON";
		document.getElementById("oui").style.display = "block";
		document.getElementById("non").style.display = "none";

    }
    onButtonDisabled();
}

function onButtonDisabled(){
	var checkboxes = document.getElementsByName("checkBox");
	var findLine = false;
	for(var i=0; i<checkboxes.length;i++){
		if (checkboxes(i).checked && checkboxes(i).value != 'ON' && checkboxes(i).value != '') {
			buttonPrint.disabled=false;
			findLine = true;
		}
	}
	if (!findLine) {
		buttonPrint.disabled=true;
	}
}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	    
		    <TH style="cursor:hand" onClick="selectRappel();" style ="font-family : Wingdings; font-size : large;" >
				<div id="oui" style="display: none">
					&#168;
				</div>
				<div id="non" style="display: block">
					&#254;
				</div>
				<INPUT type="hidden" id="selectionRappel" value="OFF">
			</TH>
    		<TH>&nbsp;</TH>
    		<TH><ct:FWLabel key="ECRAN_ECH_LI_DATE"/></TH> 
    		<TH><ct:FWLabel key="ECRAN_ECH_LI_LIBELLE"/></TH> 
    		<TH><ct:FWLabel key="ECRAN_ECH_LI_TYPE"/></TH> 
    		<TH><ct:FWLabel key="ECRAN_ECH_LI_TIERS"/></TH> 
    		<TH><ct:FWLabel key="ECRAN_ECH_LI_USER"/></TH> 
    		<TH><ct:FWLabel key="ECRAN_ECH_LI_DOMAINE"/></TH> 
    		
    		 		
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
			globaz.libra.vb.journalisations.LIEcheancesJointDossiersViewBean courant = (globaz.libra.vb.journalisations.LIEcheancesJointDossiersViewBean) viewBean.get(i);
			
			String detailUrl = "parent.location.href='" + detailLink + courant.getIdJournalisation()+"'";
	
		%>
		
		<TD class="mtd"><INPUT title="-" type="checkbox" name="checkBox" value="<%=courant.getIdJournalisation()%>" onclick="onButtonDisabled();"></TD>
		<TD class="mtd" onclick="onButtonDisabled();this.parentNode.firstChild.firstChild.checked=!this.parentNode.firstChild.firstChild.checked;">
					<FONT face="<%="".equals(courant.getPoliceIcone())?"&nbsp;":courant.getPoliceIcone()%>"
					size="4"><%="".equals(courant.getIcone())?"&nbsp;":"&#"+courant.getIcone()%></FONT></TD>
     	<TD class="mtd" nowrap onclick="onButtonDisabled();this.parentNode.firstChild.firstChild.checked=!this.parentNode.firstChild.firstChild.checked;"><%=courant.getDateRappel()%></TD>
     	<TD class="mtd" nowrap onclick="onButtonDisabled();this.parentNode.firstChild.firstChild.checked=!this.parentNode.firstChild.firstChild.checked;"><%=courant.getLibelleAffichage()%></TD>
     	<TD class="mtd" nowrap onclick="onButtonDisabled();this.parentNode.firstChild.firstChild.checked=!this.parentNode.firstChild.firstChild.checked;"><%=courant.getSession().getCodeLibelle(courant.getValeurCodeSysteme())%></TD>
     	<TD class="mtd" nowrap onclick="onButtonDisabled();this.parentNode.firstChild.firstChild.checked=!this.parentNode.firstChild.firstChild.checked;"><%=courant.getDetailTiers()%></TD>
     	<TD class="mtd" nowrap onclick="onButtonDisabled();this.parentNode.firstChild.firstChild.checked=!this.parentNode.firstChild.firstChild.checked;"><%=courant.getIdUtilisateur()%></TD>
		<TD class="mtd" nowrap onclick="onButtonDisabled();this.parentNode.firstChild.firstChild.checked=!this.parentNode.firstChild.firstChild.checked;"><%=courant.getSession().getCodeLibelle(courant.getCsDomaine())%></TD>

		
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<SCRIPT language="JavaScript">
	buttonPrint.disabled=true;
</SCRIPT>	
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>