

function updatePickupLine() {
    var update = function (text) {
        $('#pickupLine').text(text);
    }
    getPickupLine(update);
}

function getPickupLine(element) {
    $.getJSON( "/pickup-line", function( data ) {        
        element(data.pickup_line);
    });
}
