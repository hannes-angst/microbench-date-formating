package cloud.angst.sdf;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Threads;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleDateFormatBenchmark {
    private static final String pattern = "yyy-MM-dd'T'HH:mm:ss.SSSX";
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    private static final DateTimeFormatter formatter;

    private static final SimpleDateFormat sdf = new SimpleDateFormat(pattern);

    static {
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
                .withZone(ZoneId.of("GMT"));
    }

    private static final ReentrantLock lock = new ReentrantLock();

    private static synchronized String format(Date date) {
        return sdf.format(date);
    }

    private static synchronized Date parse(String value) throws ParseException {
        return sdf.parse(value);
    }


    @Fork
    @Benchmark
    @Threads(Threads.MAX)
    @BenchmarkMode(Mode.Throughput)
    public void use_DateTimeFormatter() {
        Instant date = Instant.now();
        String value = formatter.format(date);
        Instant result = Instant.from(formatter.parse(value));

        if (date.toEpochMilli() != result.toEpochMilli()) {
            throw new RuntimeException("Date differs");
        }
    }


    @Fork
    @Benchmark
    @Threads(Threads.MAX)
    @BenchmarkMode(Mode.Throughput)
    public void illegal_reuse_simple_date_formatter() throws ParseException {
        Date date = new Date();
        String value = simpleDateFormat.format(date);
        Date result = simpleDateFormat.parse(value);
        if (!date.equals(result)) {
            throw new RuntimeException("Date differs");
        }
    }

    @Fork
    @Benchmark
    @Threads(Threads.MAX)
    @BenchmarkMode(Mode.Throughput)
    public void create_new_simple_date_formatter() throws ParseException {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String value = simpleDateFormat.format(date);
        Date result = simpleDateFormat.parse(value);
        if (!date.equals(result)) {
            throw new RuntimeException("Date differs");
        }
    }

    @Fork
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public void synchronized_simple_date_formatter() throws ParseException {
        Date date = new Date();

        String value;
        synchronized (sdf) {
            value = sdf.format(date);
        }
        Date result;
        synchronized (sdf) {
            result = sdf.parse(value);
        }
        if (!date.equals(result)) {
            throw new RuntimeException("Date differs");
        }
    }

    @Fork
    @Benchmark
    @Threads(Threads.MAX)
    @BenchmarkMode(Mode.Throughput)
    public void synchronized_method_simple_date_formatter() throws ParseException {
        Date date = new Date();
        String value = format(date);
        Date result = parse(value);
        if (!date.equals(result)) {
            throw new RuntimeException("Date differs");
        }
    }

    @Fork
    @Benchmark
    @Threads(Threads.MAX)
    @BenchmarkMode(Mode.Throughput)
    public void lock_object_simple_date_formatter() throws ParseException {
        Date date = new Date();
        lock.lock();
        String value;
        try {
            value = sdf.format(date);
        } finally {
            lock.unlock();
        }
        lock.lock();
        Date result;
        try {
            result = sdf.parse(value);
        } finally {
            lock.unlock();
        }
        if (!date.equals(result)) {
            throw new RuntimeException("Date differs");
        }
    }
}
