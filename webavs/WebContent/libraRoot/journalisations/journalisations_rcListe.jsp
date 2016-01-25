<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%

	globaz.libra.vb.journalisations.LIJournalisationsJointDossiersListViewBean viewBean = (globaz.libra.vb.journalisations.LIJournalisationsJointDossiersListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();

	detailLink = "libra?userAction="+globaz.libra.servlet.ILIActions.ACTION_JOURNALISATIONS_DE + ".afficher&selectedId=";
	
	menuName = "li-menuprincipal";
	menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");

%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<SCRIPT language="Javascript">
function change (e) {
	bodyNode = e.parentElement.parentElement.parentElement;
	var nodeId = e.parentElement.name;
	
	for(var i=0; i<bodyNode.children.length; i++){
		if(bodyNode.children[i].nodeName == 'TR'){
			if(bodyNode.children[i].id == nodeId){
				if(bodyNode.children[i].style.display =='block'){
					bodyNode.children[i].style.display='none';
				} else{
					bodyNode.children[i].style.display='block';
				}
			}
		}
	}
	
	if (e.signe=="-") {
		//A cacher
    	e.innerHTML="+";
		e.signe="+";
	} else {
		//A monter
    	e.innerText="-";
	    e.signe="-";
    }
}

</SCRIPT>
	    <%-- tpl:put name="zoneHeaders" --%>
			<%
			boolean firstLine = true;
			String trName = "";
			%>	    
	    	<TH colspan="3">&nbsp;</TH>
    		<TH><ct:FWLabel key="ECRAN_ECH_LI_DATE_CREATION"/></TH> 
    		<TH><ct:FWLabel key="ECRAN_ECH_LI_LIBELLE"/></TH> 
    		<TH><ct:FWLabel key="ECRAN_ECH_LI_TYPE"/></TH> 
    		<TH><ct:FWLabel key="ECRAN_ECH_LI_TIERS"/></TH>
    		<TH><ct:FWLabel key="ECRAN_ECH_LI_RAPPEL"/></TH>
    		<TH><ct:FWLabel key="ECRAN_ECH_LI_RECEPTION"/></TH>
    		<TH><ct:FWLabel key="ECRAN_ECH_LI_USER"/></TH> 
	
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%
globaz.libra.vb.journalisations.LIJournalisationsJointDossiersViewBean courant = (globaz.libra.vb.journalisations.LIJournalisationsJointDossiersViewBean) viewBean.get(i);
				if (courant.getIdInitial().equals("0")){
					if(rowStyle.equals("row")){
						condition=false;
					} else{
						condition=true;
					}
				} else {
					if(rowStyle.equals("row")){
						condition=true;
					} else{
						condition=false;
					}
				}     %>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
			String detailUrl = "parent.location.href='" + detailLink + courant.getIdJournalisation()+"'";
	
			if(!courant.getIdInitial().equals("0")){
				firstLine = false;
				%>
				<script language="javascript">
					node = document.getElementsByTagName('TABLE')[0];
					trNode<%=i%> = node.children[0].children[node.children[0].children.length-1];
					trNode<%=i%>.style.display = 'none';
					trNode<%=i%>.id = '<%=trName%>';
				</script>
				<%				
			} else {
				firstLine = true;
			}
		%>
		
		<TD class="mtd" width="15">
	     	<ct:menuPopup menu="libra-optionsjournalisations" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + courant.getIdJournalisation()%>">
	     			<ct:menuParam key="selectedId" value="<%=courant.getIdJournalisation()%>"/>  
		 	
		 	<%if (!courant.getValeurCodeSysteme().equals(globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_MANUELLE_RECEPTION)){%>
					<ct:menuExcludeNode nodeId="annulerReception"/>
			<%}%>	
		 	
		 	</ct:menuPopup>
     	</TD>

		<%if(firstLine && !courant.getIdSuivant().equals("0")){
			trName = "jour_"+i;%>
			<TD class="mtd" name="<%=trName%>"><span class="but" onclick="change(this);">+</span></TD>
		<%} else {%>
			<TD class="mtd">&nbsp;</TD>
		<%}%>

		<TD class="mtd">
					<FONT face="<%="".equals(courant.getPoliceIcone())?"&nbsp;":courant.getPoliceIcone()%>" size="3" 
					<%=globaz.journalisation.constantes.JOConstantes.CS_JO_RAPPEL.equals(courant.getCsTypeJournal())
						| !"".equals(courant.getDateRappel()) ?"style=\"color: red\"": "style=\"color: black\""%>
					>&#<%="".equals(courant.getIcone())?"&nbsp;":courant.getIcone()%></FONT>
		</TD>					
		
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getDate()%></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getLibelleAffichage()%></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getSession().getCodeLibelle(courant.getValeurCodeSysteme())%></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getDetailTiers()%></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getDateRappel()%></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getDateReception()%></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getIdUtilisateur()%></TD>

		
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>