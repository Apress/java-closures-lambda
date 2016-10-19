import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;

import java.util.*;
import java.util.function.*;

public class CollectionExample {
  public static void main(String[] args) {
    final List<String> list = Collections.unmodifiableList(Arrays.asList(null, "foo", null, null, "bar", "baz", null));
    final List<String> expected = Collections.unmodifiableList(Arrays.asList("foo", "bar", "baz"));
    assert expected.equals(Java7Impl1.cloneWithoutNulls(list));
    assert expected.equals(Java7Impl2.cloneWithoutNulls(list));
    assert expected.equals(ApacheCommonsCollectionImpl.cloneWithoutNulls(list));
    assert expected.equals(GuavaImpl.cloneWithoutNulls(list));
    assert expected.equals(Java8Impl1.cloneWithoutNulls(list));
    assert expected.equals(Java8Impl2.cloneWithoutNulls(list));
  }

  private interface Java7Impl1 {
    static <A> List<A> cloneWithoutNulls(final List<A> list) {
      List<A> out = new ArrayList<A>(list.size());
      for (A elt : list) {
        if (elt != null) out.add(elt);
      }
      return out;
    }
  }

  private interface Java7Impl2 {
    static <A> List<A> cloneWithoutNulls(final List<A> list) {
      final List<A> out = new ArrayList<A>(list);
      while (out.remove(null)) {
      }
      return out;
    }
  }

  private interface ApacheCommonsCollectionImpl {
    static <A> List<A> cloneWithoutNulls(final List<A> list) {
      Collection<A> nonNulls = CollectionUtils.select(list, PredicateUtils.notNullPredicate());
      return new ArrayList<>(nonNulls);
    }
  }

  private interface GuavaImpl {
    static <A> List<A> cloneWithoutNulls(final List<A> list) {
      Collection<A> nonNulls = Collections2.filter(list, Predicates.notNull());
      return new ArrayList<>(nonNulls);
    }
  }

  private interface Java8Impl1 {
    static <A> List<A> cloneWithoutNulls(final List<A> list) {
      List<A> toReturn = new ArrayList<>(list);
      toReturn.removeIf(Predicate.isEqual(null));
      return toReturn;
    }
  }

  private interface Java8Impl2 {
    static <A> List<A> cloneWithoutNulls(final List<A> list) {
      List<A> toReturn = new ArrayList<>(list);
      toReturn.removeIf(it -> it == null);
      return toReturn;
    }
  }

}
