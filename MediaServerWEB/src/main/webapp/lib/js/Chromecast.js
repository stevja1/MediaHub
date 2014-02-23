function Chromecast() {}

Chromecast.cvActivity;
Chromecast.castAPI = null;
Chromecast.receiverAppId = "7e24216b-66fd-415f-b6b9-8bbf5345ed83";
Chromecast.namespace = "org.jaredstevens.chromecast";
Chromecast.availableCastDevices;
Chromecast.nextSongCallback = null;

Chromecast.initialize = function(callback) {
	if (window.cast && window.cast.isAvailable) {
		// Cast is known to be available
		console.log("initializing chromecast api");
		Chromecast.castAPI = new cast.Api();
		Chromecast.castAPI.addReceiverListener(Chromecast.receiverAppId, callback);
	} else {
		// Wait for API to post a message to us
		console.log("waiting for chromecast api to be injected...");
		window.addEventListener("message", function(event) {
			if (event.source == window && event.data &&
					event.data.source == "CastApi" &&
					event.data.event == "Hello") {
				console.log("initializing chromecast api");
				Chromecast.castAPI = new cast.Api();
				Chromecast.castAPI.addReceiverListener(Chromecast.receiverAppId, callback);
         }
		});
	};
};

Chromecast.onMessage = function(message) {
	if(message.command == "mediaended") {
		if(Chromecast.nextSongCallback != null) Chromecast.nextSongCallback();
	}
}

Chromecast.doLaunch = function(e) {
    console.log("in doLaunch");
    var item = e.target;
//    var receiver = Chromecast.availableCastDevices[index];
    var receiver = jQuery.data(item, "device");
    var request = new window.cast.LaunchRequest(Chromecast.receiverAppId, receiver);
    request.description = new window.cast.LaunchDescription();
    request.description.text = "";
    request.description.url = "http://chromecast.jaredstevens.org";
    Chromecast.castAPI.launch(request, Chromecast.onLaunch);
};

Chromecast.onLaunch = function(activity) {
	console.log("in onLaunch");
	if (activity.status == "running") {
		console.log("It should be running...");
		// Save the activity object -- this is what controls the receiver (chromecast device)
		Chromecast.cvActivity = activity;
		// Add a message listener in case the chromecast tries to tell us something cool
		Chromecast.castAPI.addMessageListener(Chromecast.cvActivity.activityId, Chromecast.namespace, Chromecast.onMessage.bind(this));
		// update UI to reflect that the receiver has received the
		// launch command and should start video playback.
	} else if (activity.status == "error") {
		console.log("There was a problem: "+activity.errorString);
		Chromecast.cvActivity = null;
	}
};

Chromecast.playMusic = function(mediaPath) {
    var command = "play";
    var mediaType = "music";
    if(Chromecast.cvActivity)
        Chromecast.castAPI.sendMessage(Chromecast.cvActivity.activityId, Chromecast.namespace, {command: command, mediaType: mediaType, path: mediaPath}, function(event){ console.log("Play command sent");});
};

Chromecast.pauseMusic = function(mediaPath) {
	var command = "pause";
	var mediaType = "music";
	if(Chromecast.cvActivity)
		Chromecast.castAPI.sendMessage(
		    Chromecast.cvActivity.activityId,
		    Chromecast.namespace,
		    {
		        command: command,
		        mediaType: mediaType,
		        path: mediaPath
            },
            function(event){ console.log("Pause command sent");}
        );
};

Chromecast.stopActivity = function() {
	console.log("stopping");
  if (Chromecast.cvActivity) {
    Chromecast.castAPI.stopActivity(Chromecast.cvActivity.activityId, Chromecast.stopCallback);
  }
};

Chromecast.stopCallback = function() {
	console.log("Activity stopped.");
};
