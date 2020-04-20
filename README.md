# microbench-date-formating
Testing SimpleDateFormat and DateTimeFormatter


```
-- open jdk 11 ---
Benchmark                                                             Mode  Cnt        Score       Error  Units
SimpleDateFormatBenchmark.create_new_simple_date_formatter           thrpt   25  1726013,859 ± 39123,006  ops/s
SimpleDateFormatBenchmark.lock_object_simple_date_formatter          thrpt   25   348023,999 ±  4974,159  ops/s
SimpleDateFormatBenchmark.synchronized_method_simple_date_formatter  thrpt   25   379277,413 ±  8641,870  ops/s
SimpleDateFormatBenchmark.synchronized_simple_date_formatter         thrpt   25   663733,202 ±  6075,179  ops/s
SimpleDateFormatBenchmark.use_DateTimeFormatter                      thrpt   25  3601494,675 ± 83846,923  ops/s

-- open j9 (11) --
Benchmark                                                             Mode  Cnt        Score       Error  Units
SimpleDateFormatBenchmark.create_new_simple_date_formatter           thrpt   25  1025996,560 ± 87783,896  ops/s
SimpleDateFormatBenchmark.lock_object_simple_date_formatter          thrpt   25   270457,173 ±  5062,192  ops/s
SimpleDateFormatBenchmark.synchronized_method_simple_date_formatter  thrpt   25   334782,341 ±  5679,441  ops/s
SimpleDateFormatBenchmark.synchronized_simple_date_formatter         thrpt   25   420434,392 ± 20602,891  ops/s
SimpleDateFormatBenchmark.use_DateTimeFormatter                      thrpt   25  1883271,816 ± 87661,382  ops/s
```
