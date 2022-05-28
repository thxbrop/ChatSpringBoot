package com.thxbrop.springboot.util;

import java.util.List;
import java.util.stream.Stream;

public class StreamUtils {
    public static <T> Stream<T> of(Iterable<T> iterable) {
        return ((List<T>) iterable).stream();
    }

}
