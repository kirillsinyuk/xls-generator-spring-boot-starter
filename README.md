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
and build a result.<br/>
You can find an example in XlsGeneratorSpringBootStarterTests class



