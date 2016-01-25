<%-- tpl:insert page="/theme/list.jtpl" --%>
	<%@ page language="java" errorPage="/errorPage.jsp" %>
	<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
	<%@ include file="/theme/list/header.jspf" %>
	
	<%-- tpl:put name="zoneScripts" --%>
		<%
			// Les labels de cette page commence par le préfix "JSP_MVE_L"
		
			globaz.corvus.vb.historiques.REHistoriqueRentesJoinTiersListViewBean viewBean = (globaz.corvus.vb.historiques.REHistoriqueRentesJoinTiersListViewBean) request.getAttribute("viewBean");
			size = viewBean.getSize();
			String idTierRequerant = request.getParameter("idTierRequerant");
			detailLink = "corvus?userAction=" + IREActions.ACTION_HISTORIQUE_RENTES + ".afficher&idTiersIn="+viewBean.getForIdTiersIn()+"&selectedId=";
			
			boolean hasUpdateRight = viewBean.getSession().hasRight(IREActions.ACTION_HISTORIQUE_RENTES, FWSecureConstants.UPDATE);
		%>

		<%@page import="globaz.framework.secure.FWSecureConstants"%>
		<%@page import="globaz.corvus.servlet.IREActions"%>

		<script language="JavaScript">
			function actionActiverEnvoiAcor(id) {
				top.fr_main.location.href='corvus?userAction=<%=globaz.corvus.servlet.IREActions.ACTION_HISTORIQUE_RENTES%>.actionActiverEnvoiAcor&idTierRequerant=<%=idTierRequerant%>&idTiersIn=<%=viewBean.getForIdTiersIn()%>&idHistorique='+id;
			}
			
			function actionDesactiverEnvoiAcor(id) {
				top.fr_main.location.href='corvus?userAction=<%=globaz.corvus.servlet.IREActions.ACTION_HISTORIQUE_RENTES%>.actionDesactiverEnvoiAcor&idTierRequerant=<%=idTierRequerant%>&idTiersIn=<%=viewBean.getForIdTiersIn()%>&idHistorique='+id;
			}
		</script>
	<%-- /tpl:put --%>
	
	<%@ include file="/theme/list/javascripts.jspf" %>
	    
    <%-- tpl:put name="zoneHeaders" --%>
	    <th><ct:FWLabel key="JSP_HIST_L_ENVOYER_HIST"/></th>
	    <th><ct:FWLabel key="JSP_HIST_L_BENEFICIAIRE"/></th>    
	    <th><ct:FWLabel key="JSP_HIST_L_PERIODE"/></th>    
	    <th><ct:FWLabel key="JSP_HIST_L_GENRE_RENTE"/></th>
		<th><ct:FWLabel key="JSP_HIST_L_MONTANT"/></th>
		<th><ct:FWLabel key="JSP_HIST_L_DROIT"/></th>
		<th><ct:FWLabel key="JSP_HIST_L_RAM"/></th>
		<th><ct:FWLabel key="JSP_HIST_L_ANNEE_RAM"/></th>	
		<th><ct:FWLabel key="JSP_HIST_L_IS_MODIFIE"/></th>
    <%-- /tpl:put --%> 
    
	<%@ include file="/theme/list/tableHeader.jspf" %>
    
    <%-- tpl:put name="zoneCondition" --%>
	    <form>
	    	<input type="hidden" name="userAction" value="">
	    </form>
    <%-- /tpl:put --%>
    
	<%@ include file="/theme/list/lineStyle.jspf" %>
	<%-- tpl:put name="zoneList" --%>
		<%			
			globaz.corvus.vb.historiques.REHistoriqueRentesJoinTiersViewBean courant = (globaz.corvus.vb.historiques.REHistoriqueRentesJoinTiersViewBean) viewBean.get(i);
			String detailUrl = "parent.location.href='" + detailLink + courant.getIdHistorique()  +"&idTierRequerant="+idTierRequerant+ "'";				
		%>	
		
		<%if(hasUpdateRight){%>					
			<%if (courant.getIsPrendreEnCompteCalculAcor()!=null && courant.getIsPrendreEnCompteCalculAcor().booleanValue()) {%>
				<td class="mtd" nowrap align="right">
					<a href="#" id="toto" onclick="actionDesactiverEnvoiAcor(<%=courant.getIdHistorique()%>);" style="text-decoration:none;">
						<img name="iii"  width="25px;" src="<%=request.getContextPath()%>/images/acor.gif" border="0"/>
					</a>
				</td>
			<%} else {%>
				<td class="mtd" nowrap align="right">
					<a href="#" id="toto" onclick="actionActiverEnvoiAcor(<%=courant.getIdHistorique()%>);" style="text-decoration:none;">
						<img name="iii"  width="25px;" src="<%=request.getContextPath()%>/images/lock.png" border="0"/>
					</a>
				</td>
			<%} %>
		<%}else{%>
			<%if (courant.getIsPrendreEnCompteCalculAcor()!=null && courant.getIsPrendreEnCompteCalculAcor().booleanValue()) {%>
				<td class="mtd" nowrap align="right">
					<img name="iii"  width="25px;" src="<%=request.getContextPath()%>/images/acor.gif" border="0"/>
				</td>
			<%} else {%>
				<td class="mtd" nowrap align="right">
					<img name="iii"  width="25px;" src="<%=request.getContextPath()%>/images/lock.png" border="0"/>
				</td>
			<%}%>
		<%}%>
			
		<td class="mtd" nowrap onClick="<%=detailUrl%>">
			<%=courant.getNssBeneficiaire()+" / "+courant.getNom() + " " + courant.getPrenom()%>&nbsp;
		</td>		
		<td class="mtd" nowrap onClick="<%=detailUrl%>" align="left">
			&nbsp;<%=courant.getDateDebutDroit()%> - <%=courant.getDateFinDroit()%>
		</td>						
		<td class="mtd" nowrap onClick="<%=detailUrl%>" align="right">
			<%=courant.getCodePrestation()%>&nbsp;
		</td>
		<td class="mtd" nowrap onClick="<%=detailUrl%>" align="right">
			<%=new globaz.framework.util.FWCurrency(courant.getMontantPrestation()).toStringFormat()%>&nbsp;
		</td>
		<td class="mtd" nowrap onClick="<%=detailUrl%>" align="right">
			<%=courant.getDroitApplique()%>&nbsp;
		</td>			
		<td class="mtd" nowrap onClick="<%=detailUrl%>" align="right">
			<%=new globaz.framework.util.FWCurrency(courant.getRam()).toStringFormat()%>&nbsp;
		</td>
		<td class="mtd" nowrap onClick="<%=detailUrl%>" align="right">
			<%=courant.getAnneeMontantRAM()%>&nbsp;
		</td>
			
		<td class="mtd" nowrap onClick="<%=detailUrl%>" align="center">
			<%if (courant.getIsModifie()!=null && courant.getIsModifie().booleanValue()) {%>
				<img name="iii"  width="12px;" src="<%=request.getContextPath()%>/images/select.gif" border="0"/>
			<%}%>
		</td>
	<%-- /tpl:put --%>
	
	<%@ include file="/theme/list/lineEnd.jspf" %>
	
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
	
	<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>