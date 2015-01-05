package org.jaredstevens.apps.mediaserver.controllers;

import org.jaredstevens.apps.mediaserver.models.Album_JSON;
import org.jaredstevens.apps.mediaserver.models.Artist_JSON;
import org.jaredstevens.apps.mediaserver.models.Response_JSON;
import org.jaredstevens.apps.mediaserver.models.Song_JSON;
import org.jaredstevens.servers.db.entities.Album;
import org.jaredstevens.servers.db.entities.Artist;
import org.jaredstevens.servers.db.entities.Song;
import org.jaredstevens.servers.db.entities.User;
import org.jaredstevens.servers.db.interfaces.IAlbumOps;
import org.jaredstevens.servers.db.interfaces.IArtistOps;
import org.jaredstevens.servers.db.interfaces.ISongOps;
import org.jaredstevens.servers.db.interfaces.IUserOps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.ejb.EJB;
import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.util.List;

@Controller
public class JSONEndpoint {
	private static Logger logger = LoggerFactory.getLogger(JSONEndpoint.class);

	@EJB(mappedName="java:module/ArtistOps")
	private IArtistOps artistService;

	@EJB(mappedName="java:module/AlbumOps")
	private IAlbumOps albumService;

	@EJB(mappedName="java:module/SongOps")
	private ISongOps songService;

	@EJB(mappedName="java:module/UserOps")
	private IUserOps userService;

	/**
	 * Artist Methods
	 */
	@RequestMapping(value="/getArtists/{pageCount}/{pageIndex}", method = RequestMethod.GET)
	public @ResponseBody Response_JSON getArtists(@PathVariable int pageCount,@PathVariable int pageIndex, HttpSession session) throws Exception {
		Response_JSON retVal = new Response_JSON();
		Long userId = (Long)session.getAttribute("userId");
		if(userId == null) {
			retVal.setStatus(Response_JSON.Status.ERROR);
			retVal.setMessage("Invalid user, or user not logged in.");
			return retVal;
		}
		List<Artist> artists = this.artistService.getArtists(userId, pageCount, pageIndex);
		if(artists == null) throw new Exception("No artists were found in the database.");
		retVal.setData(Artist_JSON.artistFactory(artists));
		return retVal;
	}

	/*
	@RequestMapping(value="/saveArtist/{id}/{name}", method = RequestMethod.GET)
	public @ResponseBody Response_JSON saveArtist(@PathVariable long id,@PathVariable String name) throws Exception {
		Response_JSON retVal = new Response_JSON();
		Artist artist;
		artist = this.artistService.save(id, name);
		if(artist == null) throw new Exception("Couldn't save artist.");
		retVal.setData(Artist_JSON.artistFactory(artist));
		return retVal;
	}

	@RequestMapping(value="/getArtist/{artistId}", method = RequestMethod.GET)
	public @ResponseBody Response_JSON getArtist(@PathVariable long artistId) throws Exception {
		Response_JSON retVal = new Response_JSON();
		Artist artist = this.artistService.getById(artistId);
		if(artist == null) throw new Exception("Couldn't find artist id: "+artistId);
		retVal.setData(Artist_JSON.artistFactory(artist));
		return retVal;
	}

	@RequestMapping(value="/removeArtist/{id}", method = RequestMethod.GET)
	public @ResponseBody Response_JSON removeArtist(@PathVariable long id) {
		Response_JSON retVal = new Response_JSON();
		retVal.setData(this.artistService.remove(id));
		return retVal;
	}
	*/

	/**
	 * Album Methods
 	 */
	@RequestMapping(value="/getArtistAlbums/{artistId}", method = RequestMethod.GET)
	public @ResponseBody Response_JSON getArtistAlbums(@PathVariable long artistId, HttpSession session) throws Exception {
		Response_JSON retVal = new Response_JSON();
		Long userId = (Long)session.getAttribute("userId");
		if(userId == null) {
			retVal.setStatus(Response_JSON.Status.ERROR);
			retVal.setMessage("Invalid user, or user not logged in.");
			return retVal;
		}
		List<Album> albums = this.albumService.getArtistAlbums(userId, artistId);
		if(albums == null) throw new Exception("Couldn't find albums for artistId: "+artistId);
		retVal.setData(Album_JSON.albumFactory(albums));
		return retVal;
	}

	/*
	@RequestMapping(value="/getAlbums/{pageCount}/{pageIndex}", method = RequestMethod.GET)
	public @ResponseBody Response_JSON getAlbums(@PathVariable int pageCount,@PathVariable int pageIndex) throws Exception {
		Response_JSON retVal = new Response_JSON();
		List<Album> albums = this.albumService.getAlbums(pageCount, pageIndex);
		if(albums == null) throw new Exception("Couldn't find any albums.");
		retVal.setData(Album_JSON.albumFactory(albums));
		return retVal;
	}

	@RequestMapping(value="/saveAlbum/{id}/{artistId}/{title}/{rawReleaseDate}/{trackCount}/{discNum}", method = RequestMethod.GET)
	public @ResponseBody Response_JSON saveAlbum(
			@PathVariable long id,
			@PathVariable long artistId,
			@PathVariable String title,
			@PathVariable long rawReleaseDate,
			@PathVariable int trackCount,
			@PathVariable int discNum) throws Exception {
		Response_JSON retVal = new Response_JSON();
		Date releaseDate = new Date(rawReleaseDate);
		Album album;
//		album = this.albumService.save(id, artistId, title, releaseDate, trackCount, discNum);
//		if(album == null) throw new Exception("There was a problem saving the album.");
//		retVal.setData(Album_JSON.albumFactory(album));
		return retVal;
	}

	@RequestMapping(value="/getAlbum/{id}", method = RequestMethod.GET)
	public @ResponseBody Response_JSON getAlbum(@PathVariable long id) throws Exception {
		Response_JSON retVal = new Response_JSON();
		Album album = this.albumService.getById(id);
		if(album == null) throw new Exception("Couldn't find album id: "+id);
		retVal.setData(Album_JSON.albumFactory(album));
		return retVal;
	}

	@RequestMapping(value="/removeAlbum/{id}", method = RequestMethod.GET)
	public @ResponseBody Response_JSON removeAlbum(@PathVariable long id) throws Exception {
		Response_JSON retVal = new Response_JSON();
		retVal.setData(this.albumService.remove(id));
		return retVal;
	}
	*/
	/**
	 * Song Methods
	 */
	/*
	@RequestMapping(value="/getSong/{songId}", method = RequestMethod.GET)
	public @ResponseBody Response_JSON getSong(@PathVariable long songId) throws Exception {
		Response_JSON retVal = new Response_JSON();
		Song song = this.songService.getById(songId);
		if(song == null) throw new Exception("Couldn't find song id: "+songId);
		retVal.setData(Song_JSON.songFactory(song));
		return retVal;
	}

	@RequestMapping(value="/getArtistSongs/{artistId}", method = RequestMethod.GET)
	public @ResponseBody Response_JSON getArtistSongs(@PathVariable long artistId) throws Exception {
		Response_JSON retVal = new Response_JSON();
		List<Song> songs = this.songService.getArtistSongs(artistId);
		if(songs == null) throw new Exception("Couldn't find any songs for artist id: "+artistId);
		retVal.setData(songs);
		return retVal;
	}
	*/
	@RequestMapping(value="/getAlbumSongs/{albumId}", method = RequestMethod.GET)
	public @ResponseBody Response_JSON getAlbumSongs(@PathVariable long albumId, HttpSession session) throws Exception {
		Response_JSON retVal = new Response_JSON();
		Long userId = (Long)session.getAttribute("userId");
		if(userId == null) {
			retVal.setStatus(Response_JSON.Status.ERROR);
			retVal.setMessage("Invalid user, or user not logged in.");
			return retVal;
		}
		List<Song> songs = this.songService.getAlbumSongs(userId, albumId);
		if(songs == null) throw new Exception("Couldn't find any songs for album id: "+albumId);
		retVal.setData(Song_JSON.songFactory(songs));
		return retVal;
	}
	/*
	@RequestMapping(value="/getSongs/{pageCount}/{pageIndex}", method = RequestMethod.GET)
	public @ResponseBody Response_JSON getSongs(@PathVariable int pageCount, @PathVariable int pageIndex) throws Exception {
		Response_JSON retVal = new Response_JSON();
		List<Song> songs = this.songService.getSongs(pageCount, pageIndex);
		if(songs == null) throw new Exception("Couldn't find any songs.");
		retVal.setData(songs);
		return retVal;
	}

	@RequestMapping(value="/saveSong/{id}/{fileId}/{albumId}/{title}/{duration}/{trackNum}/{fingerprint}", method = RequestMethod.GET)
	public @ResponseBody Response_JSON saveSong(
			@PathVariable long id,
			@PathVariable long fileId,
			@PathVariable long albumId,
			@PathVariable String title,
			@PathVariable int duration,
			@PathVariable int trackNum,
			@PathVariable String fingerprint) throws Exception {
		Response_JSON retVal = new Response_JSON();
//		Song_JSON song = Song_JSON.songFactory(this.songService.save(id, fileId, albumId, title, duration, trackNum, fingerprint));
//		if(song == null) throw new Exception("Couldn't save song.");
//		retVal.setData(song);
		return retVal;
	}

	@RequestMapping(value="/removeSong/{id}", method = RequestMethod.GET)
	public @ResponseBody Response_JSON removeSong(@PathVariable long id) throws Exception {
		Response_JSON retVal = new Response_JSON();
		Boolean removeResult = this.songService.remove(id);
		if(removeResult) throw new Exception("Failed to delete song id: "+id);
		retVal.setData(removeResult);
		return retVal;
	}
	*/
	@RequestMapping(value="/authenticate/{firstName}/{lastName}/{OAuthID}", method = RequestMethod.GET)
	public @ResponseBody Response_JSON authenticate(@PathVariable String firstName, @PathVariable String lastName, @PathVariable String OAuthID, HttpSession session) throws Exception {
		Response_JSON retVal = new Response_JSON();
		retVal.setStatus(Response_JSON.Status.ERROR);
		// See if this is an existing user
		User user = this.userService.getByOAuthId(OAuthID);
		if(user == null) {
			// This user is missing. Create the user.
			user = this.userService.save(-1, firstName, lastName, OAuthID);
			retVal.setMessage("User successfully created.");
		} else {
			retVal.setMessage("User successfully found.");
		}
		if(user != null) {
			// Setup our session
			session.setAttribute("userId", user.getId());
			retVal.setStatus(Response_JSON.Status.SUCCESS);
		}
		return retVal;
	}

	@RequestMapping(value="/isAuthenticated", method = RequestMethod.GET)
	public @ResponseBody Response_JSON isAuthenticated(HttpSession session) throws Exception {
		Response_JSON retVal = new Response_JSON();
		retVal.setStatus(Response_JSON.Status.ERROR);
		Long userId = (Long)session.getAttribute("userId");
		User user = null;
		if(userId != null) {
			user = this.userService.getById(userId);
		}
		if(user != null) {
			retVal.setStatus(Response_JSON.Status.SUCCESS);
			retVal.setMessage(user.getFirstName()+" is authenticated.");
		} else {
			retVal.setStatus(Response_JSON.Status.ERROR);
			retVal.setMessage("User is NOT authenticated.");
		}
		return retVal;
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody Response_JSON handleException(Exception e) {
		Response_JSON retVal = new Response_JSON();
		retVal.setStatus(Response_JSON.Status.ERROR);
		if(e.getMessage() != null && e.getMessage().length() > 0) {
			retVal.setMessage(e.getMessage());
			JSONEndpoint.logger.error("Exception: ", e);
		} else {
			retVal.setMessage("Unknown exception thrown: " + e.toString());
			JSONEndpoint.logger.error("Exception: ", e);
		}
		return retVal;
	}
}
