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
  }
  
}