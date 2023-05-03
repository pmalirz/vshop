package pl.malirz.vshop.shared.infrastructure.repository.utils

import com.fasterxml.jackson.databind.ObjectMapper
import oracle.jdbc.OracleType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import kotlin.reflect.KClass

private const val DEFAULT_JSON_COLUMN_NAME = "DOC"
private const val DEFAULT_BATCH_SIZE = 1000

class OracleJsonQuery(
    private val jdbcTemplate: JdbcTemplate,
    private val objectMapper: ObjectMapper,
    private val jsonColumnName: String = DEFAULT_JSON_COLUMN_NAME,
    private val batchSize: Int = DEFAULT_BATCH_SIZE
) {

    fun insert(tableName: String, storedObject: Any) {
        SimpleJdbcInsert(jdbcTemplate).withTableName(tableName).usingColumns(jsonColumnName)
            .execute(
                mapSqlParameterSource(storedObject)
            )
    }

    fun <T : Any> insertBatch(tableName: String, storedObjects: Collection<T>) {
        val batchSql = SimpleJdbcInsert(jdbcTemplate).withTableName(tableName).usingColumns(jsonColumnName)
        storedObjects.chunked(batchSize).forEach { chunk ->
            val itemsToInsert = chunk.map {
                mapSqlParameterSource(it)
            }.toTypedArray()

            batchSql.executeBatch(*itemsToInsert)
        }
    }

    /**
     * Searches given text fragment in all fields of the JSON document.
     */
    fun <T : Any> search(tableName: String, textFragment: String, clazz: KClass<T>): List<T> {
        // Tip! Here you can find more about binding parameters to JSON path expressions (look for PASSING clause):
        // https://docs.oracle.com/en/database/oracle/oracle-database/21/adjsn/json-path-expressions.html#GUID-2DC05D71-3D62-4A14-855F-76E054032494
        // Also $.*? (particularly .*) means we are going to search in all fields of the JSON document.

        val query =
            "SELECT * FROM $tableName WHERE json_exists($jsonColumnName, " +
                    "'$.*?(@.lower() has substring \$p1)' PASSING :1 AS \"p1\"" +
                    ")"

        return jdbcTemplate.query(
            query,
            { rs, _ -> objectMapper.readValue(rs.getString(jsonColumnName), clazz.java) },
            textFragment.lowercase()
        ).toList()
    }

    private fun mapSqlParameterSource(storedObject: Any) = MapSqlParameterSource().addValue(
        jsonColumnName,
        objectMapper.writer().writeValueAsBytes(storedObject),
        OracleType.JSON.vendorTypeNumber
    )

}