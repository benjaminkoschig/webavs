globazGlobal.services = {
		loadPrestationsInfo : function(typePrestation, callback) {
			var options = {
			serviceClassName:globazGlobal.prestationsViewService,
			serviceMethodName:'getPrestationsInfos',
			parametres:typePrestation,
			callBack:function (data) {
					callback.call(this, data);
				}
			};
			vulpeculaUtils.lancementService(options);	
		},
		
		checkAuMoinsUnPostePourPrestationEtTravailleur : function(typePrestation, idTravailleur, callback) {
			var options = {
			serviceClassName:globazGlobal.prestationsViewService,
			serviceMethodName:'checkAuMoinsUnPostePourPrestationEtTravailleur',
			parametres:typePrestation+','+idTravailleur,
			callBack:function (data) {
					callback.call(this, data);
				}
			};
			vulpeculaUtils.lancementService(options);	
		},
		findTaux : function(idPosteTravail, date, callback) {
			var options = {
					serviceClassName:globazGlobal.prestationsViewService,
					serviceMethodName:'getTauxCPParametrage',
					parametres:idPosteTravail+','+date,
					callBack:callback
			};
			vulpeculaUtils.lancementService(options);
		}
};