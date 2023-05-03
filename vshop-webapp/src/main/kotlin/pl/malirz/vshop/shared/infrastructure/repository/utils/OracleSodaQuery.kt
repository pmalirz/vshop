package pl.malirz.vshop.shared.infrastructure.repository.utils

import com.fasterxml.jackson.databind.ObjectMapper
import oracle.soda.OracleCollection
import oracle.soda.OracleDatabase
import oracle.soda.OracleDocument
import oracle.soda.rdbms.OracleRDBMSClient
import org.springframework.jdbc.core.ConnectionCallback
import org.springframework.jdbc.core.JdbcTemplate
import kotlin.concurrent.getOrSet
import kotlin.reflect.KClass

/**
 * Utility class making work with SODA collection more convenient and performant.
 */
class OracleSodaQuery(
    private val jdbcTemplate: JdbcTemplate,
    private val oracleRDBMSClient: OracleRDBMSClient,
    private val objectMapper: ObjectMapper
) {

    /**
     *  OracleCollection and OracleDatabase instances are cached per thread.
     *  This is done to avoid opening the collection and creating the OracleDatabase instance for each query.
     */
    private val oracleInThread = ThreadLocal<MutableMap<String, Pair<OracleCollection, OracleDatabase>>>()

    fun insert(tableName: String, storedObject: Any) {
        val (oracleCollection, oracleDatabase) = openCollection(tableName)
        val serializedDocument = objectMapper.writer().writeValueAsBytes(storedObject)
        val document = oracleDatabase.createDocumentFromByteArray(serializedDocument)
        oracleCollection.insert(document)
    }

    /**
     * Note! In fact SODA does not utilize batch but inserts the documents one by one to the table.
     */
    fun <T : Any> insertBatch(tableName: String, storedObjects: Collection<T>) {
        val (oracleCollection, oracleDatabase) = openCollection(tableName)
        val documents = mutableListOf<OracleDocument>()
        storedObjects.forEach {
            val serializedDocument = objectMapper.writeValueAsBytes(it)
            val document = oracleDatabase.createDocumentFromByteArray(serializedDocument)
            documents.add(document)
        }
        oracleCollection.insert(documents.iterator())
    }

    fun <T : Any> search(tableName: String, qbe: String, clazz: KClass<T>): List<T> {
        val (oracleCollection, oracleDatabase) = openCollection(tableName)
        val qbeDoc = oracleDatabase.createDocumentFromString(qbe) // Query By Example
        val resultCursor = oracleCollection.find().filter(qbeDoc).cursor
        val result = mutableListOf<T>()
        while (resultCursor.hasNext()) {
            val item = objectMapper.readValue(resultCursor.next().contentAsByteArray, clazz.java)
            result.add(item)
        }
        return result
    }

    /**
     *  Uses per-thread-cached OracleDatabase and OracleCollection instance.
     *  Instead of opening the collection and creating the OracleDatabase instance every time an operation
     *  is required inside the same thread.
     */
    private fun openCollection(collectionName: String) =
        oracleInThread.getOrSet { mutableMapOf() }.computeIfAbsent(collectionName) {
            var oracleDatabase: OracleDatabase? = null

            // Create OracleDatabase instance using pooled connection
            jdbcTemplate.execute(ConnectionCallback { connection ->
                oracleDatabase = oracleRDBMSClient.getDatabase(connection)
            })
            val oracleCollection = oracleDatabase?.openCollection(collectionName)
                ?: throw IllegalStateException("The $collectionName collection does not exist.")

            Pair(oracleCollection!!, oracleDatabase!!)
        }

}
