monthDateUtil = (function() {
	function isAfter(monthDate1, monthDate2) {
		return is(monthDate1, monthDate2, function(date1, date2) {return date1.getTime() > date2.getTime();});
	}
	
	
	function isBefore(monthDate1, monthDate2) {
		return is(monthDate1, monthDate2, function(date1, date2) {return date1.getTime() < date2.getTime();});
	}
	
	function is(monthDate1, monthDate2, func) {
		var date1 = createDate(monthDate1);
		var date2 = createDate(monthDate2);
		
		return func(date1, date2);
	}
	
	function createDate(monthDate) {
		var parts = monthDate.split(".");
		var date = new Date(parseInt(parts[1], 10),
		                  parseInt(parts[0], 10),
		                  1);
		return date;
	}
	
	function bind($datePeriodeDe, $datePeriodeA) {
		$datePeriodeDe.change(function() {
			var periodeDe = $datePeriodeDe.val();
			var periodeA = $datePeriodeA.val();
			
			if(periodeDe.length!=0) {
				if(periodeA.length==0) {
					$datePeriodeA.val(periodeDe).change();
				} else {
					if(monthDateUtil.isAfter(periodeDe, periodeA)) {
						$datePeriodeA.val(periodeDe).change();
					}
				}
			} 
		});
		
		$datePeriodeA.change(function() {
			var periodeDe = $datePeriodeDe.val();
			var periodeA = $datePeriodeA.val();
			
			if(periodeA.length!=0) {
				if(periodeDe.length==0) {
					$datePeriodeDe.val(periodeA).change();
				} else {
					if(monthDateUtil.isAfter(periodeDe, periodeA)) {
						$datePeriodeDe.val(periodeA).change();
					}
				}
			} 		
		});
	}
	
	return {
		isAfter : isAfter,
		isBefore : isBefore,
		bind : bind
	};
})();