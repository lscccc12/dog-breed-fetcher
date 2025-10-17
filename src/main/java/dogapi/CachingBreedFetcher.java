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
    private final BreedFetcher wrapped;
    private final Map<String, List<String>> cache = new HashMap<>();
    private int callsMade = 0;

    public CachingBreedFetcher(BreedFetcher wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        String key = (breed==null)?"":breed.trim().toLowerCase(Locale.ROOT);
        List<String> cached = cache.get(key);
        if (cached != null) return new ArrayList<>(cached);

        callsMade++;
        List<String> res = wrapped.getSubBreeds(breed);
        cache.put(key, List.copyOf(res));
        return new ArrayList<>(res);
    }

    public int getCallsMade() {
        return callsMade;
    }
}