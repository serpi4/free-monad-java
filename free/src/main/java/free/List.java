package free;

import java.util.ArrayList;

public abstract class List<A> implements _1<List.z, A> {
  public static final class z{}

  private List(){}

  public abstract Option<A> headOption();

  public abstract Option<List<A>> tailOption();

  public final <B> List<B> map(final F1<A, B> f){
    return reverseMap(f).reverse();
  }

  public abstract <B> List<B> reverseMap(F1<A, B> f);

  public abstract List<A> reverse();

  public abstract <B> List<B> flatMap(F1<A, List<B>> f);

  public abstract <B> B foldLeft(B z, F2<B, A, B> f);

  public final boolean nonEmpty(){
    return this instanceof Cons;
  }

  public final boolean isEmpty(){
    return this instanceof Nil;
  }

  public final int length(){
    int len = 0;
    List<A> list = this;
    while(list.nonEmpty()){
      len++;
      list = ((Cons<A>)list).tail;
    }
    return len;
  }

  public final List<A> append(final List<A> list){
    return reverse().foldLeft(list, (as, a) -> new Cons<>(a, as));
  }

  public final java.util.List<A> toJavaList(){
    java.util.List<A> result = new ArrayList<>();
    List<A> self = this;
    while(self.nonEmpty()){
      result.add(((Cons<A>)self).head);
      self = ((Cons<A>)self).tail;
    }
    return result;
  }

  private static Nil<Object> nil = new Nil<>();

  @SuppressWarnings("unchecked")
  public static <A> List<A> nil(){
    return (List)nil;
  }

  @SafeVarargs
  public static <A> List<A> of(final A... as){
    List<A> list = nil();
    for(int i = as.length - 1; i >= 0; i--){
      list = new Cons<>(as[i], list);
    }
    return list;
  }

  public static <A> List<A> join(final List<List<A>> list){
    return list.reverse().foldLeft(nil(), (t, h) -> h.append(t));
  }

  private static final class Cons<A> extends List<A> {
    private final A head;
    private final List<A> tail;

    private Cons(A head, List<A> tail) {
      this.head = head;
      this.tail = tail;
    }

    @Override
    public Option<A> headOption() {
      return Option.some(head);
    }

    @Override
    public Option<List<A>> tailOption() {
      return Option.some(tail);
    }

    @Override
    public <B> List<B> reverseMap(F1<A, B> f) {
      List<B> bs = nil();
      List<A> as = this;
      while(as.nonEmpty()){
        bs = new Cons<>(f.apply(((Cons<A>)as).head), bs);
        as = ((Cons<A>) as).tail;
      }
      return bs;
    }

    @Override
    public List<A> reverse() {
      List<A> reversed = nil();
      List<A> self = this;
      while(self.nonEmpty()){
        reversed = new Cons<>(((Cons<A>)self).head, reversed);
        self = ((Cons<A>) self).tail;
      }
      return reversed;
    }

    @Override
    public <B> List<B> flatMap(F1<A, List<B>> f) {
      return join(map(f));
    }

    @Override
    public <B> B foldLeft(B z, F2<B, A, B> f) {
      return tail.foldLeft(f.apply(z, head), f);
    }
  }

  private final static class Nil<A> extends List<A> {
    @Override
    public Option<A> headOption() {
      return Option.none();
    }

    @Override
    public Option<List<A>> tailOption() {
      return Option.none();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <B> List<B> reverseMap(F1<A, B> f) {
      return (List<B>)this;
    }

    @Override
    public List<A> reverse() {
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <B> List<B> flatMap(F1<A, List<B>> f) {
      return (List<B>)this;
    }

    @Override
    public <B> B foldLeft(B z, F2<B, A, B> f) {
      return z;
    }
  }
}