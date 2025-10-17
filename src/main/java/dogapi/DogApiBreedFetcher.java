package dogapi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient().newBuilder().build();

    private static final String API_URL = "https://dog.ceo/api";
    private static final String STATUS_MSG = "status";
    private static final String SUCCESS_MSG = "success";

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {

         final Request request = new Request.Builder()
                .url(String.format("%s/breed/" + breed + "/list", API_URL))
                .method("GET", null)
                .build();
        try {
            final Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());

            if (Objects.equals(responseBody.getString(STATUS_MSG), SUCCESS_MSG)) {
                final JSONArray breedList = responseBody.getJSONArray("message");
                final String[] breeds = new String[breedList.length()];

                for (int i = 0; i < breedList.length(); i++) {
                    breeds[i] = breedList.getString(i);


                }
                return Arrays.asList(breeds);
            }
            else {
                throw new BreedNotFoundException(breed);
            }
        }
        catch (IOException | JSONException event) {
            throw new BreedNotFoundException(breed);
        }
    }
}