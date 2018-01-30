var LINK_TEMPLATES = [
	{sayi: 0,	category: ''}, // 100'e kadar
	{sayi: 1,	category: '1'}, // 200'e kadar
	{sayi: 2,	category: '2'},
	{sayi: 3,	category: '3'},
	{sayi: 4,	category: '4'},
	{sayi: 5,	category: '5'},
	{sayi: -6,	category: '-1'} // 500'den büyükler için
	];

function Data(nodeDataArray, linkDataArray) {
  this.nodeDataArray = nodeDataArray;
  this.linkDataArray = linkDataArray;
  
  this.prepare = function() {
	$.each(this.nodeDataArray, function( index, value ) {
		value['key'] = value.oid;
		value['color'] = 'pink';
		if (!_.isEmpty(value['sayi'])) {
			value['text'] = value['text'] + '\n' + value['sayi'];
		}			
    });
	
	/**
	 * kalınlıkları ayarlar
	 */
	$.each(this.linkDataArray, function( index, value ) {
		var sayi = parseInt(parseInt(value['text']) / 100);
		var template = _.find(LINK_TEMPLATES, function(o) { return o.sayi === sayi; });
		if (!_.isEmpty(template)) {
			value['category'] = template.category;
		} else {
			value['category'] = '-1';
		}
    });
  }
  
}