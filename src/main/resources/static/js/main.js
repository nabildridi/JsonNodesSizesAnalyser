var arraysDataSet, objectsDataSet;

$(function () {
	
		$('#arraysTable').DataTable( {
	        data: arraysDataSet,
	        columns: [
	            { title: "Path" },
	            { title: "Size in Bytes" },
	            { title: "Size in KBytes" },
	            { title: "Size in MBytes" },
	            { title: "Percentage" }
	        ],
	        "order": [[ 4, "desc" ]]
	    } );

		
		$('#objectsTable').DataTable( {
	        data: objectsDataSet,
	        columns: [
	            { title: "Path" },
	            { title: "Size in Bytes" },
	            { title: "Size in KBytes" },
	            { title: "Size in MBytes" },
	            { title: "Percentage" }
	        ],
	        "order": [[ 4, "desc" ]]
	    });
		
	
	  $('#drop-area').dmUploader({ 
	    url: '/upload',
	    multiple: false,
	    auto: false,
	    queue: false,
	    dnd : false,
	    onInit: function(){

	      this.find('input[type="text"]').val('');
	    },
	    onComplete: function(){
	      // All files in the queue are processed (success or error)
	    },
	    onNewFile: function(id, file){
	      // When a new file is added using the file selector or the DnD area
	    	this.find('input[type="text"]').val(file.name);
	    	
	    },
	    onBeforeUpload: function(id){
	      // about to start uploading a file
	      $.LoadingOverlay("show", {image       : "", fontawesome : "fa fa-cog fa-spin"});
	    	
	      ui_single_update_progress(this, 0, true);      
	      ui_single_update_active(this, true);

	      ui_single_update_status(this, 'Uploading...');
	    },
	    onUploadProgress: function(id, percent){
	      // Updating file progress
	      ui_single_update_progress(this, percent);
	    },
	    onUploadSuccess: function(id, data){
	    	
	      
	    	
	      // A file was successfully uploaded
	      ui_single_update_active(this, false);

	      // You should probably do something with the response data, we just show it
	      displayResult(JSON.parse(data));
	      
	      $.LoadingOverlay("hide");

	      ui_single_update_status(this, 'Upload completed.', 'success');
	    },
	    onUploadError: function(id, xhr, status, message){
	      // Happens when an upload error happens
	      ui_single_update_active(this, false);
	      ui_single_update_status(this, 'Error: ' + message, 'danger');
	    },
	    onFallbackMode: function(){
	      // When the browser doesn't support this plugin :(

	    }
	  });
	  

	  
	  /*
	    Global controls
	  */
	  $('#btnApiStart').on('click', function(evt){
	    evt.preventDefault();

	    $('#drop-area').dmUploader('start');
	  });

	  $('#btnApiCancel').on('click', function(evt){
	    evt.preventDefault();

	    $('#drop-area').dmUploader('cancel');
	  });
	  
    
});



function ui_single_update_active(element, active){
  element.find('div.progress').toggleClass('d-none', !active);
  element.find('input[type="text"]').toggleClass('d-none', active);

  element.find('input[type="file"]').prop('disabled', active);
  element.find('.btn').toggleClass('disabled', active);

  element.find('.btn i').toggleClass('fa-circle-o-notch fa-spin', active);
  element.find('.btn i').toggleClass('fa-folder-o', !active);
}

function ui_single_update_progress(element, percent, active){
  active = (typeof active === 'undefined' ? true : active);

  var bar = element.find('div.progress-bar');

  bar.width(percent + '%').attr('aria-valuenow', percent);
  bar.toggleClass('progress-bar-striped progress-bar-animated', active);

  if (percent === 0){
    bar.html('');
  } else {
    bar.html(percent + '%');
  }
}

function ui_single_update_status(element, message, color) {	
  color = (typeof color === 'undefined' ? 'muted' : color);

  element.find('small.status').prop('class','status text-' + color).html(message);
}

















function sendText(){	
	
	var jsonText = $("#jsonText").val();
	
	if(!jsonText){
		return; 
	}
	

	bootbox.confirm("Are you sure you want to analyze this text?", function(result){ 

		if(result){	
				
			$.LoadingOverlay("show", {image       : "", fontawesome : "fa fa-cog fa-spin"});
				var jsonText = $("#jsonText").val();
			
				
				$.ajax({
			        type: 'POST',
			        contentType: 'application/text',
			        url: "/text",
			        processData:false,
			        dataType: 'json',
			        data: jsonText,
			        success: function (data){
			        	displayResult(data);			        	
			        },
			        complete: function() {
			        	$.LoadingOverlay("hide");
			        }
			    });
				
				

		}

	});
	
	
}





function sendUrl(){
	
	
	var jsonText = $("#url").val();
	
	if(!jsonText){
		return; 
	}

	bootbox.confirm("Are you sure you want to analyze this url?", function(result){ 

		if(result){	
				
				$.LoadingOverlay("show", {image       : "", fontawesome : "fa fa-cog fa-spin"});
				var jsonText = $("#url").val();
			
				
				$.ajax({
			        type: 'POST',
			        contentType: 'application/text',
			        url: "/url",
			        processData:false,
			        data: jsonText,
			        dataType: 'json',
			        success: function (data){
			        	displayResult(data);
			        },
			        complete: function() {
			        	$.LoadingOverlay("hide");
			        }
			    });
				
				

		}

	});
	
	
}



function displayResult(data){
	
	// clear size display
	$("#resultSection").html("Result :");
	
	//clear array table
	arraysDataSet = [];
	$('#arraysTable').DataTable().clear();
	
	//clear objects table
	objectsDataSet = [];
	$('#objectsTable').DataTable().clear();

	if(data.error){
		bootbox.alert(data.errorCause);
	}else{
	
		$("#resultSection").html("PARSED json size : " + data.fullSize + " bytes | " + formatKiloBytes(data.fullSize, 2) + " KBytes | " + formatMegaBytes(data.fullSize, 2) + " MBytes");
		
		//process arrays data
		$.each( data.arrays, function( key, value ) {
			
			var item = new Array(key, value, formatKiloBytes(value, 2), formatMegaBytes(value, 2), percentage(value, data.fullSize, 2));
			arraysDataSet.push(item);

		});	
		$('#arraysTable').DataTable().rows.add(arraysDataSet);
		$('#arraysTable').DataTable().draw();
		
		
		//process objects data
		$.each( data.objects, function( key, value ) {
			
			var item = new Array(key, value, formatKiloBytes(value, 2), formatMegaBytes(value, 2), percentage(value, data.fullSize, 2));
			objectsDataSet.push(item);

		});	
		$('#objectsTable').DataTable().rows.add(objectsDataSet);
		$('#objectsTable').DataTable().draw();
	}
	
	
}


function formatKiloBytes(bytes,decimals) {
	   var dm = decimals || 2;
	   return parseFloat((bytes / 1024).toFixed(dm));
}

function formatMegaBytes(bytes,decimals) {
	   var dm = decimals || 2;
	   return parseFloat((bytes / 1024 / 1024).toFixed(dm));
}

function percentage(size, fullSize ,decimals) {
	   var dm = decimals || 2;
	   return parseFloat(((size / fullSize) * 100).toFixed(dm)) + " %";
}