/**
 * A connection strategy that determines whether two movies are connected by writer.
 * <p>
 * Two movies are considered connected if they share the same writer.
 */

public class WriterConnection implements ConnectionStrategy {
    /**
     * Checks whether two movies are connected based on the writer.
     * <p>
     * If either movie does not have a writer (i.e., {@code null}), they are not considered connected.
     *
     * @param a the first movie
     * @param b the second movie
     * @return {@code true} if both movies have the same writer; {@code false} otherwise
     */
    @Override
    public boolean areConnected(Movie a, Movie b) {
        String writerA = a.getWriter();
        String writerB = b.getWriter();

        // if it is null, then just return false
        if (writerA == null || writerB == null) {
            return false;
        }
        return writerA.equals(writerB);
    }
    /**
     * Returns the type of connection this strategy represents.
     *
     * @return the string "Writer"
     */
    @Override
    public String getType() {
        return "Writer";
    }

     /**
     * Retrieves the shared writer between two movies if they are the same.
     * <p>
     * Checks whether both movies have a non-null writer and if the writer's name
     * matches exactly. If they do, it returns the shared writer's name; otherwise, returns {@code null}.
     *
     * @param a the first movie to compare
     * @param b the second movie to compare
     * @return
     * */
     @Override
    public String getSharedElement(Movie a, Movie b) {
        String writerA = a.getWriter();
        String writerB = b.getWriter();
        return (writerA != null && writerB != null && writerA.equals(writerB)) ? writerA : null;
    }
}
