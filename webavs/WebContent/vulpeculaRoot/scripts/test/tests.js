QUnit.test( "monthDateUtil.isAfter given 01.2014 and 03.2014 should be false", function( assert ) {
	  assert.ok(monthDateUtil.isAfter("01.2014","03.2014") == false);
});

QUnit.test( "monthDateUtil.isAfter given 01.2014 and 01.2014 should be false", function( assert ) {
	  assert.ok(monthDateUtil.isAfter("01.2014","01.2014") == false);
});

QUnit.test( "monthDateUtil.isAfter given 01.2014 and 12.2013 should be true", function( assert ) {
	  assert.ok(monthDateUtil.isAfter("01.2014","12.2013") == true);
});

QUnit.test( "monthDateUtil.isBefore given 01.2014 and 03.2014 should be true", function( assert ) {
	  assert.ok(monthDateUtil.isBefore("01.2014","03.2014") == true);
});

QUnit.test( "monthDateUtil.isBefore given 01.2014 and 01.2014 should be false", function( assert ) {
	  assert.ok(monthDateUtil.isBefore("01.2014","01.2014") == false);
});

QUnit.test( "monthDateUtil.isBefore given 01.2014 and 12.2013 should be false", function( assert ) {
	  assert.ok(monthDateUtil.isBefore("01.2014","12.2013") == false);
});

QUnit.test( "monthDateUtil.isAfter given 09.2015 and 06.2015 should be true", function( assert ) {
	  assert.ok(monthDateUtil.isAfter("09.2015","06.2015") == true);
});