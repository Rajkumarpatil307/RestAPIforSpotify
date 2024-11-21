package SpotifyAPITest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class SpotifyAPI {
    String accessToken="BQAUX9ONKHH5R83HIHEfhQGA_Us5c9LtM9syciEzAnMX81gHZS4bnlZqqVgtu42bj2VITpZG0rs0D6kwGLyVB8ZP9Q4H_I2RedN26_NKtG5hvjeLbxpxDVN4Dxa0GTkpa2Goc2j8cDntdaKvknU82U2jZaOJDD4uRZa0rjebh2K_m5-W8wvePWnFftUuOyguzopJzv8KJ235qQBuQqZDGGDZsxRZr9L3IDLr8XImuJQKRw4qE-sEtu2SQ8G5vFZQHJk1IAr8K0BJETovNHJh_LZkTpKDHUUX1wlEDdeynRf-DeYuUgxdXPVyoc9RB83YgDU9KvVgtOL5IN0wswN7tqo";
    String Id = null;



    //-----------------------------------------USER----------------------------------------------------------------------

    @Test
    public void getCurrentProfile() {
        Response response = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .get("https://api.spotify.com/v1/me");

        response.prettyPrint();
        Id = response.path("id");
        Assert.assertNotNull(Id, "User ID is null");
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(dependsOnMethods = "getCurrentProfile")
    public void getUserProfile() {
        Response response = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .get("https://api.spotify.com/v1/users/" + Id);

        response.prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void getUserTopArtists() {
        Response response = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .queryParam("time_range", "medium_term")
                .queryParam("limit", 10)
                .queryParam("offset", 5)
                .when()
                .get("https://api.spotify.com/v1/me/top/artists");

        response.prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void followPlaylist() {
        Response response = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .queryParam("type", "playlist")
                .when()
                .put("https://api.spotify.com/v1/playlists/3cEYpjA9oz9GiPac4AsH4n/followers");

        response.prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void unfollowPlaylist() {
        String playlistId = "3cEYpjA9oz9GiPac4AsH4n";

        Response response = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .delete("https://api.spotify.com/v1/playlists/" + playlistId + "/followers");

        response.prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void getFollowedArtists() {
        Response response = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .get("https://api.spotify.com/v1/me/following?type=artist");

        response.prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void followArtistsOrUsers() {
        Response response = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .queryParam("type", "artist")
                .queryParam("ids", "2CIMQHirSU0MQqyYHq0eOx,57dN52uHvrHOxijzpIgu3E,1vCWHaC5f2uS3yhpwWbIA6")
                .when()
                .put("https://api.spotify.com/v1/me/following");

        response.prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 204);
    }

    @Test
    public void unfollowArtistsOrUsers() {
        Response response = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .queryParam("type", "artist")
                .queryParam("ids", "2CIMQHirSU0MQqyYHq0eOx,57dN52uHvrHOxijzpIgu3E,1vCWHaC5f2uS3yhpwWbIA6")
                .when()
                .delete("https://api.spotify.com/v1/me/following");

        response.prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 204);
    }

    @Test
    public void checkIfUserFollowsArtistsOrUsers() {
        Response response = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .queryParam("type", "artist")
                .queryParam("ids", "2CIMQHirSU0MQqyYHq0eOx,57dN52uHvrHOxijzpIgu3E,1vCWHaC5f2uS3yhpwWbIA6")
                .when()
                .get("https://api.spotify.com/v1/me/following/contains");

        response.prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void checkIfCurrentUserFollowsPlaylist() {
        Response response = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .queryParam("ids", "jmperezperez")
                .when()
                .get("https://api.spotify.com/v1/playlists/3cEYpjA9oz9GiPac4AsH4n/followers/contains");

        response.prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 403);
    }

    //-------------------------------------------TRACK --------------------------------------------------------------

    @Test
    public void getTrack() {
        Response trackResponse = given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .when()
                .get("https://api.spotify.com/v1/tracks/5CfCWKI5pZ28U0uOzXkDHe");

        Assert.assertEquals(trackResponse.getStatusCode(), 200);
    }

    @Test
    public void getSeveralTracks() {
        Response tracksResponse = given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .when()
                .get("https://api.spotify.com/v1/tracks?ids=7ouMYWpwJ422jRcDASZB7P,4VqPOruhp5EdPBeR92t6lQ,2takcwOaAZWiXQijPHIx7B");

        Assert.assertEquals(tracksResponse.getStatusCode(), 200);
    }

    @Test
    public void getUserSavedTracks() {
        Response savedTracksResponse = given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .when()
                .get("https://api.spotify.com/v1/me/tracks?market=ES&limit=10&offset=0");

        Assert.assertEquals(savedTracksResponse.getStatusCode(), 200);
    }

    @Test
    public void saveTracksForCurrentUser() {
        Response saveTracksResponse = given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body("{\"ids\": [\"4uLU6hMCjMI75M1A2tKUQC\"]}")
                .when()
                .put("https://api.spotify.com/v1/me/tracks");

        Assert.assertEquals(saveTracksResponse.getStatusCode(), 200);
    }

    @Test
    public void removeUserSavedTracks() {
        Response removeTracksResponse = given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body("{\"ids\": [\"4uLU6hMCjMI75M1A2tKUQC\"]}")
                .when()
                .delete("https://api.spotify.com/v1/me/tracks");

        Assert.assertEquals(removeTracksResponse.getStatusCode(), 200);
    }

    @Test
    public void checkUserSavedTracks() {
        Response checkTracksResponse = given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .when()
                .get("https://api.spotify.com/v1/me/tracks/contains?ids=4uLU6hMCjMI75M1A2tKUQC");

        Assert.assertEquals(checkTracksResponse.getStatusCode(), 200);
    }

    @Test
    public void getSeveralTracksAudioFeatures() {
        Response audioFeaturesResponse = given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .when()
                .get("https://api.spotify.com/v1/audio-features?ids=7ouMYWpwJ422jRcDASZB7P,4VqPOruhp5EdPBeR92t6lQ,2takcwOaAZWiXQijPHIx7B");

        Assert.assertEquals(audioFeaturesResponse.getStatusCode(), 200);
    }

    @Test
    public void getTrackAudioFeatures() {
        Response audioFeaturesResponse = given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .when()
                .get("https://api.spotify.com/v1/audio-features/11dFghVXANMlKmJXsNCbNl");

        Assert.assertEquals(audioFeaturesResponse.getStatusCode(), 200);
    }

    @Test
    public void getTrackAudioAnalysis() {
        Response audioAnalysisResponse = given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .when()
                .get("https://api.spotify.com/v1/audio-analysis/11dFghVXANMlKmJXsNCbNl");

        Assert.assertEquals(audioAnalysisResponse.getStatusCode(), 200);
    }

    @Test
    public void getRecommendations() {
        Response recommendationsResponse = given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .when()
                .get("https://api.spotify.com/v1/recommendations?limit=10&market=ES&seed_artists=4NHQUGzhtTLFvgF5SZesLK&seed_genres=classical,country&seed_tracks=0c6xIDDpzE81m2q797ordA");

        Assert.assertEquals(recommendationsResponse.getStatusCode(), 200);
    }
}
