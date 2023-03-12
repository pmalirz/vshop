package pl.malirz.vshop.shared.utils

import com.fasterxml.jackson.databind.ObjectMapper
import oracle.soda.OracleCollection
import oracle.soda.OracleDatabase
import oracle.soda.OracleDocument
import oracle.soda.rdbms.OracleRDBMSClient
import org.springframework.jdbc.core.ConnectionCallback
import org.springframework.jdbc.core.JdbcTemplate
import kotlin.concurrent.getOrSet

/**
 * Utility class making work with SODA collection more convenient and performant.
 */
class OracleSODACollection(
    private val jdbcTemplate: JdbcTemplate,
    private val oracleRDBMSClient: OracleRDBMSClient,
    private val objectMapper: ObjectMapper
) {

    private val oracleInThread = ThreadLocal<MutableMap<String, Pair<OracleCollection, OracleDatabase>>>()

    fun storeObject(collectionName: String, storedObject: Any) {
        val (oracleCollection, oracleDatabase) = openCollection(collectionName)
        val serializedDocument = objectMapper.writer().writeValueAsBytes(storedObject)
        val document = oracleDatabase.createDocumentFromByteArray(serializedDocument)
        oracleCollection.insert(document)
    }

    fun <T : Any> storeObjects(collectionName: String, storedObjects: Collection<T>) {
        val (oracleCollection, oracleDatabase) = openCollection(collectionName)
        val documents = mutableListOf<OracleDocument>()
        storedObjects.forEach {
            val serializedDocument = objectMapper.writer().writeValueAsBytes(it)
            val document = oracleDatabase.createDocumentFromByteArray(serializedDocument)
            documents.add(document)
        }
        oracleCollection.insert(documents.iterator())
    }

    /**
     *  Uses per-thread-cached OracleDatabase and OracleCollection instance.
     *  Instead of opening the collection and creating the OracleDatabase instance every time an operation
     *  is required inside the same thread.
     */
    private fun openCollection(collectionName: String) =
        oracleInThread.getOrSet { mutableMapOf() }.computeIfAbsent(collectionName) {
            var oracleDatabase: OracleDatabase? = null

            jdbcTemplate.execute(ConnectionCallback { connection ->
                oracleDatabase = oracleRDBMSClient.getDatabase(connection, true)
            })

            val oracleCollection = oracleDatabase?.openCollection(collectionName)

            return@computeIfAbsent Pair(oracleCollection!!, oracleDatabase!!)
        }

}