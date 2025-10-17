package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {

    private int callsMade = 0;
    private BreedFetcher breedFetcher;
    private HashMap<String,List<String>> storedBreeds = new HashMap<>();

    public CachingBreedFetcher(BreedFetcher fetcher) {
        breedFetcher = fetcher;

    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        if (storedBreeds.containsKey(breed)) {
            return storedBreeds.get(breed);
        }
        else {
            callsMade++;
            List<String> newCall = breedFetcher.getSubBreeds(breed);
            storedBreeds.put(breed, newCall);
            return newCall;
        }

    }

    public int getCallsMade() {
        return callsMade;
    }
}