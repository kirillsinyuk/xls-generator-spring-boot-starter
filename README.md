### Xls builder spring-boot-starter

Starter designed to build Excel files. 
Based on Apache POI.

Provide bean of type XlsGeneratorService

```kotlin
interface XlsGeneratorService {
    fun <T> newXlsBuilder(
        file: File, 
        cellProcessors: List<CellProcessor<T>>
    ): XlsBuilder<T>
}
```

After you get a builder, add data with dumpRow method
```kotlin
fun dumpRow(entity: T)
```
or 
```kotlin
fun dumpAllRows(entities: Iterable<T>): XlsBuilder<T>
```

Also you can skip a several rows
```kotlin
fun skipRows(num: Int): XlsBuilder<T>
```
and set another table on the same sheet
```kotlin
fun secondaryTable(processors: List<CellProcessor<T>>): XlsBuilder<T>
```

At the end just .build() a result.<br/>
<b>P.S. Do not forget to free builder resources.</b><br/>
You can find an example in XlsGeneratorSpringBootStarterTests class



