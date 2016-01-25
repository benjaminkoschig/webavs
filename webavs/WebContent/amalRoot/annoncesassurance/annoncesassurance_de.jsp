<script language="JavaScript">
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var ACTION_AJAX_ANNONCES_ASSURANCE="<%=IAMActions.ACTION_ANNONCES_ASSURANCEAJAX%>";
</script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/annoncesassurance/AnnoncesAssurance_MembrePart.js"/></script>


<%-- /tpl:put --%>
<table width="100%" border="0" id="tableCaisseMaladieAnnonces">
			<TR>		
				<td colspan="4">
					<div class="conteneurDF">
						
						<div class="areaMembreAnnonces">
							<br>
								<!--  Zone Area titre -->
								<table class="areaDataTable" width="100%">
									<thead><!--  en tete de table -->
										<tr>
											<th>Date</th>
											<th class="notSortable">Listes</th>
											<!--<th class="notSortable">Fichier Cosama</th>  -->
										</tr>
									</thead>
									<tbody>
										<!-- Ici viendra le tableau des résultats -->					
									</tbody>
								</table>
						</div>
					</div>
				</TD>
			</TR>	
</table>		