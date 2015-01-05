function MediaServerWS() {}

MediaServerWS.URL = APIConfig.ENDPOINT;

MediaServerWS.getArtists = function(pageCount, pageIndex, callback) {
    var method = "getArtists/";
    var parameters = pageCount+"/"+pageIndex;
	var settings = {url: MediaServerWS.URL+method+parameters, success:callback, async: true};
	$.ajax(settings);
};

MediaServerWS.getArtistAlbums = function(artistId, callback) {
    var method = "getArtistAlbums/";
    var parameters = artistId;
	var settings = {url: MediaServerWS.URL+method+parameters, success:callback, async: true};
	$.ajax(settings);
}

MediaServerWS.getArtistSongs = function( artistId, callback )
{
    var method = "getArtistSongs/";
    var parameters = artistId;
	var settings = {url: MediaServerWS.URL+method+parameters, success:callback, async: true};
	$.ajax(settings);
};

MediaServerWS.getAlbumSongs = function( albumId, callback )
{
    var method = "getAlbumSongs/";
    var parameters = albumId;
	var settings = {url: MediaServerWS.URL+method+parameters, success:callback, async: true};
	$.ajax(settings);
};

MediaServerWS.getSongs = function( pageCount, pageIndex, callback )
{
    var method = "getSongs/";
    var parameters = pageCount+"/"+pageIndex;
	var settings = {url: MediaServerWS.URL+method+parameters, success:callback, async: true};
	$.ajax(settings);
};

MediaServerWS.saveSong = function(id, fileId, albumId, title, duration, trackNum, fingerprint, callback) {
    var method = "saveSong/";
    var parameters = id+"/"+fileId+"/"+albumId+"/"+title+"/"+duration+"/"+trackNum+"/"+fingerprint;
	var settings = {url: MediaServerWS.URL+method+parameters, success:callback, async: true};
	$.ajax(settings);
};

MediaServerWS.removeSong = function(id, callback) {
    var method = "removeSong/";
    var parameters = id;
	var settings = {url: MediaServerWS.URL+method+parameters, success:callback, async: true};
	$.ajax(settings);
};

MediaServerWS.authenticate = function(firstName, lastName, OAuthId, callback) {
    var method = "authenticate/";
    var parameters = firstName+"/"+lastName+"/"+OAuthId;
	var settings = {url: MediaServerWS.URL+method+parameters, success:callback, async: true};
	$.ajax(settings);
};

MediaServerWS.isAuthenticated = function(callback) {
    var method = "isAuthenticated/";
    var parameters = "";
	var settings = {url: MediaServerWS.URL+method+parameters, success:callback, async: true};
	$.ajax(settings);
};