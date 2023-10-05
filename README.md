### Xls builder spring-boot-starter

Starter designed to build simple .xls files. 
Based on Apache POI.

Starter provides bean of type XlsGeneratorService

```kotlin
interface XlsGeneratorService {
    fun <T> newXlsBuilder(
        file: File,
        cellProcessors: List<CellProcessor<T>>,
        type: TableType // Table type: ROW_BASED/COLUMN_BASED
    ): XlsBuilder<T>
}
```

After you get a builder, add data with dumpRow method
```kotlin
fun dump(entity: T)
fun dumpAll(entities: Iterable<T>): XlsBuilder<T>
```

Also you can skip a several rows/columns
```kotlin
fun skipRows(num: Int): XlsBuilder<T>
fun skipColumns(num: Int): XlsBuilder<T>
```

and set another table on the same sheet
```kotlin
fun secondaryTable(processors: List<CellProcessor<T>>): XlsBuilder<T>
```

At the end just call `.build()` to get the result file.<br/>

<b>P.S. Do not forget to free builder resources.</b><br/>
You can find an example in XlsGeneratorSpringBootStarterTests class



