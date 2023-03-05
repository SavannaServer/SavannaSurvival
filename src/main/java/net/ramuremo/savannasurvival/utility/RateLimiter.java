package net.ramuremo.savannasurvival.utility;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RateLimiter<T> {
    private static List<RateLimiter> instancedRateLimiters = new ArrayList<>();
    private final double permitsPerSeconds;
    private Map<T, com.google.common.util.concurrent.RateLimiter> rateLimiters = new HashMap<>();

    public RateLimiter(double permitsPerSeconds) {
        this.permitsPerSeconds = permitsPerSeconds;

        instancedRateLimiters.add(this);
    }

    public static void removeInstanced() {
        instancedRateLimiters.forEach(RateLimiter::removeAll);
        instancedRateLimiters = new ArrayList<>();
    }

    public void add(@Nonnull T type) {
        if (isContains(type))
            return;

        rateLimiters.put(type, com.google.common.util.concurrent.RateLimiter.create(permitsPerSeconds));
    }

    public void remove(@Nonnull T type) {
        if (!isContains(type))
            return;

        rateLimiters.remove(type);
    }

    public void removeAll() {
        rateLimiters = new HashMap<>();
    }

    public boolean tryAcquire(T type) {
        if (!isContains(type))
            add(type);

        return rateLimiters.get(type).tryAcquire();
    }

    public boolean isContains(@Nonnull T type) {
        return rateLimiters.containsKey(type);
    }
}
