package cloud.angst.sdf;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Threads;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

import static java.time.format.DateTimeFormatter.ISO_DATE;

public class SimpleDateFormatBenchmark {
    private static final String pattern = "yyy-MM-dd'T'HH:mm:ss.SSS";

    private static final SimpleDateFormat sdf = new SimpleDateFormat(pattern);

    private static final ReentrantLock lock = new ReentrantLock();

    private static synchronized String format(Date date) {
        return sdf.format(date);
    }

    private static synchronized Date parse(String value) throws ParseException {
        return sdf.parse(value);
    }

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);


    @Fork(value = 4, warmups = 1, jvmArgsAppend = {
            "-Djava.awt.headless=true",
            "-XX:+IdleTuningCompactOnIdle",
            "-XX:+IdleTuningGcOnIdle"})
    @Benchmark
    @Threads(Threads.MAX)
    @BenchmarkMode(Mode.Throughput)
    public void use_DateTimeFormatter() {
        LocalDate date = LocalDate.now();
        String value = ISO_DATE.format(date);
        LocalDate result = LocalDate.from(ISO_DATE.parse(value));
        if (!date.equals(result)) {
            throw new RuntimeException("Date differs");
        }
    }


    @Fork(value = 4, warmups = 1, jvmArgsAppend = {
            "-Djava.awt.headless=true",
            "-XX:+IdleTuningCompactOnIdle",
            "-XX:+IdleTuningGcOnIdle"})
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

    @Fork(value = 4, warmups = 1, jvmArgsAppend = {
            "-Djava.awt.headless=true",
            "-XX:+IdleTuningCompactOnIdle",
            "-XX:+IdleTuningGcOnIdle"})
    @Benchmark
    @Threads(Threads.MAX)
    @BenchmarkMode(Mode.Throughput)
    public void create_new_simple_date_formatter() throws ParseException {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String value = simpleDateFormat.format(date);
        Date result = simpleDateFormat.parse(value);
        if (!date.equals(result)) {
            throw new RuntimeException("Date differs");
        }
    }

    @Fork(value = 4, warmups = 1, jvmArgsAppend = {
            "-Djava.awt.headless=true",
            "-XX:+IdleTuningCompactOnIdle",
            "-XX:+IdleTuningGcOnIdle"})
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

    @Fork(value = 4, warmups = 1, jvmArgsAppend = {
            "-Djava.awt.headless=true",
            "-XX:+IdleTuningCompactOnIdle",
            "-XX:+IdleTuningGcOnIdle"})
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

    @Fork(value = 4, warmups = 1, jvmArgsAppend = {
            "-Djava.awt.headless=true",
            "-XX:+IdleTuningCompactOnIdle",
            "-XX:+IdleTuningGcOnIdle"})
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
