$(document).ready(function() { 
//$("#plugin_table").tablesorter(); 
$('.detailButton').click(
        function(event) {
            bid = event.currentTarget.id;
            tnr = bid.substr(bid.lastIndexOf('-') + 1);
            did = '#test-detail-' + tnr;
            $(did).toggle();
            if ($('#' + bid).hasClass('ui-icon-triangle-1-e')) {
                $('#' + bid).removeClass('ui-icon-triangle-1-e').addClass('ui-icon-triangle-1-s');
            } else {
                $('#' + bid).removeClass('ui-icon-triangle-1-s').addClass('ui-icon-triangle-1-e');
            }
        });
} 
); 
