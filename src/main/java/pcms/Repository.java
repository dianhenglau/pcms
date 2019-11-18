package pcms;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/** Parent for all repositories. */
public abstract class Repository<T extends Model> {
    /** Data path. */
    protected final Path dataPath;
    /** Cache from database. */
    protected final List<T> cache;
    /** Next ID to be added. */
    protected int newId;
    /** Record creator. */
    protected final Function<String, T> recordCreator;
    /** Record parser. */
    protected final Function<String, T> recordParser;

    /** Constructor. */
    public Repository(
            final Path dataPath, 
            final Function<String, T> recordCreator, 
            final Function<String, T> recordParser) {
        cache = new ArrayList<>();
        newId = 1;
        this.dataPath = dataPath;
        this.recordCreator = recordCreator;
        this.recordParser = recordParser;
    }

    /** All rows with start and count. */
    public List<T> all(final int start, final int count) {
        readFromFile();
        final int end = Math.min(start + count, cache.size());
        final int begin = Math.min(start, end);
        return new ArrayList<T>(cache.subList(begin, end));
    }

    /** All rows with count. */
    public List<T> all(final int count) {
        return all(0, count);
    }

    /** All rows. */
    public List<T> all() {
        return all(0, Integer.MAX_VALUE);
    }

    /** Find record with ID. */
    public Optional<T> findWithId(final String id) {
        readFromFile();

        final int index = Collections.binarySearch(cache, recordCreator.apply(id), T::compare);

        return index < 0 ? Optional.empty() : Optional.of(cache.get(index));
    }

    /** Filter records with predicate, start, and count. */
    public List<T> filter(
            final Predicate<T> predicate, final int start, final int count) {
        readFromFile();

        return cache.stream()
                .filter(predicate)
                .skip(start)
                .limit(count)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /** Filter records with predicate and count. */
    public List<T> filter(final Predicate<T> predicate, final int count) {
        return filter(predicate, 0, count);
    }

    /** Filter records with predicate. */
    public List<T> filter(final Predicate<T> predicate) {
        return filter(predicate, 0, Integer.MAX_VALUE);
    }

    /** Insert record. */
    public abstract T insert(final T record);

    /** Update record. */
    public abstract T update(final T record);

    /** Delete record. */
    public T delete(final T record) {
        readFromFile();

        // Make sure record id exists.
        final int index = ValidationUtil.idExists(cache, record);

        final T deletedRecord = cache.get(index);
        cache.remove(index);
        writeToFile();
        return deletedRecord;
    }

    /** Read from file. */
    protected void readFromFile() {
        cache.clear();
        newId = 1;

        try {
            final List<String> rows = CsvParsingUtil.splitIntoRows(
                    Files.readString(dataPath, StandardCharsets.UTF_8));
            newId = Integer.parseInt(rows.get(0));
            for (int i = 1; i < rows.size(); i++) {
                cache.add(recordParser.apply(rows.get(i)));
            }
        } catch (IOException ex) {
            System.out.println(String.format(
                    "Cannot find file: %s, will create it when record is added.", ex.getMessage()));
        }
    }

    /** Write to file. */
    protected void writeToFile() {
        final StringBuilder s = new StringBuilder();
        s.append(Integer.valueOf(newId)).append('\n');
        for (final T r : cache) {
            s.append(r.toRow()).append('\n');
        }

        try {
            Files.writeString(dataPath, s, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
