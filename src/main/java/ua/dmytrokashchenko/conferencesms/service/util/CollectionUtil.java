package ua.dmytrokashchenko.conferencesms.service.util;

import java.util.ArrayList;
import java.util.List;

public final class CollectionUtil {

    private CollectionUtil() {
    }

    public static <E> List<E> difference(List<E> biggerList, List<E> smallerList) {
        List<E> result = new ArrayList<>(biggerList);
        result.removeAll(smallerList);
        return result;
    }
}
