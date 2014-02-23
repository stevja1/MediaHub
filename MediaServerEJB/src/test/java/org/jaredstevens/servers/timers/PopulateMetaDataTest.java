package org.jaredstevens.servers.timers;

import static org.junit.Assert.*;

import org.jaredstevens.servers.db.entities.Album;
import org.jaredstevens.servers.db.entities.Artist;
import org.jaredstevens.servers.db.entities.Configuration;
import org.jaredstevens.servers.db.entities.File;
import org.jaredstevens.servers.db.operations.*;
import org.jaredstevens.servers.utilities.Utilities;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Date;

public class PopulateMetaDataTest {
	private EntityManager em;

	@Before
	public void setUp() throws Exception {
		// Setup an EntityManager
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("MediaDBTest-PU", System.getProperties());
		this.em = factory.createEntityManager();
	}

	@After
	public void tearDown() throws Exception {
		this.em.getEntityManagerFactory().close();
	}

	@Test
	public void populateMetaDataTest() {
		PopulateMetaData tool = new PopulateMetaData();
		tool.setEm(this.em);
		try {
			this.em.getTransaction().begin();
			this.buildTestData();
			tool.populateMetaData(null);
			this.em.getTransaction().rollback();
		} catch(Exception e) {
			fail("Threw exception: "+e.getMessage());
		}
	}

	public void buildTestData() throws Exception {
		ConfigurationOps configConn = new ConfigurationOps();
		configConn.setEm(this.em);
		Configuration config = Utilities.getConfig(configConn);
		if(config == null || !config.getSongPath().equals("/Users/jarstev")) {
			configConn.save("/Users/jarstev");
		}
		SongOps songConn = new SongOps();
		songConn.setEm(this.em);
		FileOps fileConn = new FileOps();
		fileConn.setEm(this.em);
		File file1 = fileConn.save(-1, "01 - Happy Pills.mp3", File.FileType.SONG);
		File file2 = fileConn.save(-1, "01 Track 01.mp3", File.FileType.SONG);
		File file3 = fileConn.save(-1, "02 Track 02.mp3", File.FileType.SONG);
		File file4 = fileConn.save(-1, "ACDC/Back in Black/01 Hells Bells.mp3", File.FileType.SONG);
		File file5 = fileConn.save(-1, "ACDC/Back in Black/02 Shoot to Thrill.mp3", File.FileType.SONG);

		ArtistOps artistConn = new ArtistOps();
		artistConn.setEm(this.em);
		AlbumOps albumConn = new AlbumOps();
		albumConn.setEm(this.em);

		Artist artist = artistConn.save(-1, "Unknown Artist");
		Album album = albumConn.save(-1, artist.getId(), "Unknown Album", new Date(0L), 0, 0);
		String fingerprint = "AQADtMumJFq4REj8IT2u5MO1JE9Qf9pgdSGSSM-RSzweXahpBXeUI9m-FemHJ8eVJNIS9JkMq4tEaHrU49IxJ3mEyjruJNCew93x6cSpRFXQJxWRqiySyclxpcSTR2ieHH4iJNMXpM1x5XgWJsJ0wieJZN6PPBKuVjEqhT96JchXJA-chevx5cHzIRWUK9kRi0ez6DFOI8wTHckLbzqeysc5PfiPVEfyLMcznYIjXXgSbUH6CEkxZUWVpDKuJxVqyQzCFEmOM9QML7qCU4gUeUiyHyWDxwcZJvgRf0geMP_RVJGK88hfJEeTMdzQRU7xHo-OhC_yHFeyo5mUpRH6e0gWXQgfolnRPk1EnBSulYel4yJyHlJSMdAO4s6DJ9LxXLlwQssTdEeuE88y5EQfHVo0Hk905FkUC5F99BkP8QvCD5cePAtzfMZzJIhvePlxJl2Ol0b6BEkbnIlrNMsVoxZTNHGSIzkR5m7xGH-EPResI3GlIDKjw0s_fErkpGiaRQiPZHmOLpmi4o0e1PqGMGmP5MGTwXuOm0aydMiPZsU1HXOFN8cPXUFj9AtOGbHuQv-Ri5qDpq5Qrjnx9Ej2FGEUSmiVRKKOZl2o4No9JIsuhA_KHg2fHpeP82gkLh9CidShswgt5ggD6A8uKRfyNMlxeghXormhT8eViBXGY8-hHc175FmiBk_0Ii-SvUPz433wjMSP4z-eH-LyaMIlFs-Woz_8NsW1UKjSxMGRTznOJbgfhOZONDSJB7nSQ3WPMNdRPjGe7HiO-EimJPmLZtS240YelWh-aL-IXMHjHLmiIHmFw8p1fMQlJQ_61EKYZzuSHw_uo2zBKTWSI1XEzyhz420s3Dqaq0gUDnlC9CGaP0L4J4F8hEyOa8lxPXimBH565FIOPTqa_qgMaFEfpM-DNkxyPDyib0ZzptA2CVdyRQitLB1GKyHRC2EOjXlxNeLw1THU9vAXSUF-BTzxhApxH38DpMtaI_kTfC9-9Mj0BdqNfMHzHEezZ0jW8Ufe7jhZ9ArSH8l0KyC9Rnh0nNGL60j-I2Ukxbhe3MmF7iOa38KfBzn0gj8S-FJi4skm4uKDvmieRciDpD8cqmGK5-iHZLmR52jKE3WaJvhio-GyIhk95EE9Hs0l42H0HMl75D9OBQ-b4W-CZvlRdseP3hdO5D38HKGOPFWy4hIv_IoORXGD7scVYmbm4WYEJn3Q4zneIxeaHHXK48GPF-WLfIkeQ4wthA6Pv0R0xYR6dQh1-OpROyWuGOlCJH8ewXeO_mhSDY-XWDj8IbmkHbmYFJ4fnMaNJExOxEuDU0p4XD-ao7yQbdICbRHS_MeTI_2EhOuFMi-e4EkIl8qEfMqhG2GiKOi2IZdFPBb04nlGsOInPB-aNId2fHgvvGEG3cik57h6PI-Ifi-aHnF7JHcoY_ph7RmuwGQ-5IMo1UX440oynG-CkGcOLcsy5AlD3Ij0Q0uOPHqCh7qCJ7B1HD-SjS7SNGiq4KYY7EevHM2DPEnxSHnwDz2aZ4F-Is8TXDmqtJiSW8ejLQEraTOuI1mCcDy-H-dX2Ml2nAqRK4mE5AUd5SL-KegyH1RllFLyBfkDPTpiPuh_5Dn0H81y5Dv-oKmDfsfJHF22Y8oShcfxhUKeBh9KHaaSDxolHb1CIlq14JlyNIxy4giTg_kJPVweVNIGLWLoGDmao88cvGGGUjyaZUd8PUiewIp84tGUCVeOJqcQnrmgy8kQMgflWTFy6AzCz_iPTzmO9EguH5fl4DfqqcR5IouOZH_RKKnSEZcWJzjSipCbKMijHE3EB6e8wg8SIqSOR_WCK23RC81z5ENy8WgojsNDOinSSws07gjDRSqYJ8N_9EMY_7g6aHxiqNsRLmVn4NgsvAt2SccXEYnLFNkkisOLpmIQPtCP_Hg-VLSOxkyYIT-S5TxK4gp_NOOSDifS5yqSHc36FJ0e4_bRiOGJTiROJSFCNUJz6MjxXLgeocekMAl-4ifyTFMGPR_CfIV_lFKONzy6cYbvI-ehHXnDoj11XDp-hC-R_MJ_NEruoHJY1OLQeMqRlFqQN4F5jBTTwFb04LmRLEcWRuFwSWuMJwSTo0cSZUyOuhceKVHyoolEIxlPPBU2NriUKAYTH-HpB7ozhDy2JIlIBc2XKhvyHLrg44euY5UiId8i8MpxycibCboQ5jT6HA9O8mhsfA4SfgjPOMKPi7rRKD7yBsmpHXyOPuKGZ2xwHWZ05HECpbjwE7XJwNZRU5Pw442SHVeFfC4uYXqG9MLJE6LCXEUWmV5w7thRWbByI5cKTSJy_DxiHcnx_JhXC02dh_AThB8SP2jWyCiTB3vyoY8QRg-SHl6ib8Se5Ch5NNGD8MuLZOEikNHxRCrSIznxZYaV5UzwKrHQPEF4JEcT9UIZXcMvNF-O8IeWx0VI4tKLG3E6qFeRXZMQ3kPzI7lz_PhyND-qMplwfkgWeQgf4uoqNFyUFKWR90eyp3iLyc7x_LgPJ1pmXA5yHtqDBjqeI49yzI6O-EItjRCFkDquLMf04p_wGmEq7dCzRMh349KHJnmCUN-h60f-BE-KfIf-I_By4ZJ0oaqOMNWQ_HhyWMkeXEcfBmEeIdn8YEq0vMWjB4fjBXkeJCRfXD3OLEZoJQqSZfiOZlI2JkW_xDKaKkH4I-mDZtpQ5iL-CM0zhGduaM-PMFt-fDquIU5XqDxeCX0paOlY5A9-PDvRUByH8jvuB8mOMGqNn0WzZxpa5FuSc0iWU9ieB7Xj4zweX3DM48GN5syDHngx58K7Hd0jpGcGbUeeHs_wFv0ZNBdyD7MWQf9R_tBy5siPi8eP__gVfGGONeSRZlWO5PqCR8azI9mHMJU64XkSDZ9ivAmOhGOPvFzx-MMlPUd_NAzTI_cT6BeH_LiOikdCTUco5dzxZzkugyeSZfuGkLmDz8ETAoAAAgIAIBRBQABBHFEAEIGEUAIAAJAjRAFBABGKKSAIIoQIYwADjAigEBOCKQKAYIYQBAhSijhgCAEKGMEs44AYgohgCjAAADMQAaAAIUAApSgQxAgAGAKCEW6IQEAQIYQEiCgFCTaGCGEIIkQYZgwhRAgGJAFGQOIEoMAIAKDABBFBGACMKYAAUAIIAoghAiFEABJCEMAAVsAAIACDghAiiAUWCAgEFIQJBgBiyAmgADAMAeGIhE4IAKRSCAENgCMwAAOEAghJioADAABjhCAAAQCAQIoiIBggABmAgAICGGGUAEIQJZxCBFjgkGKKOWsAIGwAIAgzhglLEIAOCAIQcIYIIADDCAECDABIEkcFARIwg4AAQBhBBFAICMIQAgQAA4wVBAAgAAHKaeII5NIwZIQkQhIiHAKAAwEQEAYgARwRSCoClBFUCAEgUBAgbxRQkBMgGFCMGQAIAUARQQAyhBBAABSCAECEQZIIBYQgEgxRGDEIECGYsAApgxARhBnqgGBECKQAAUgYQABURgAJgCDQEIWIAgQByKQHhBhBAJFCAUCRAJIAggRBxggEgEeECACMgIIBCZAhQRAnDEIEEGQRYsAIQQQRwCOBFEJEOKOERF44IBQTxACBEFACIGAIIJoRZBgwhCChCEIGGASUsAIAYr0ABBABgQJMQCIIQ8IIAAgjQgDCBFLEEUKsIQggIqhgwDAhiDPCIGQIUURRARhTxBohAEGCAEIQYIJoBIjDhgkABGHCEAKAMEwIwRQBBghAJlHMCKAIMIw4BIQjjhhkkCJGASSAcoYAByiRQplGhDIKAgOgQAAIhAgBAAkhAEGAGMQIIFIwY4gCAgFjnASICGEMENQwhYAQAgGgjCIKAiEgNYgIQAgwQgAABGAIAEYRMIIwIAQAwBBggIGCGMEAYAQRC5QhQCghBDCMGsIAMXAgIAAwjAgBFJACMs0AIAIoggCSAA";
		songConn.save(-1, file1.getId(), album.getId(), "01 - Happy Pills.mp3", 216, 0, fingerprint);
		songConn.save(-1, file2.getId(), album.getId(), "01 Track 01.mp3", 216, 0, fingerprint);
		songConn.save(-1, file3.getId(), album.getId(), "01 Track 02.mp3", 216, 0, fingerprint);
		songConn.save(-1, file4.getId(), album.getId(), "01 Hells Bells.mp3", 216, 0, fingerprint);
		songConn.save(-1, file5.getId(), album.getId(), "02 Shoot to Thrill.mp3", 216, 0, fingerprint);
	}
}
