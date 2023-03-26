package pl.malirz.vshop.shared.utils

import com.fasterxml.jackson.databind.ObjectMapper
import oracle.jdbc.OracleType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations

private const val DEFAULT_JSON_COLUMN_NAME = "DOC"
private const val DEFAULT_BATCH_SIZE = 1000

class OracleJsonQuery(
    jdbcTemplate: JdbcTemplate,
    private val objectMapper: ObjectMapper,
    private val jsonColumnName: String = DEFAULT_JSON_COLUMN_NAME,
    private val batchSize: Int = DEFAULT_BATCH_SIZE
) {

    private val jdbcInsertOperations: SimpleJdbcInsertOperations = SimpleJdbcInsert(jdbcTemplate)

    fun insert(tableName: String, storedObject: Any) {
        jdbcInsertOperations.withTableName(tableName).usingColumns(jsonColumnName)
            .execute(
                mapSqlParameterSource(storedObject)
            )
    }

    fun <T : Any> insertBatch(tableName: String, storedObjects: Collection<T>) {
        val batchSql = jdbcInsertOperations.withTableName(tableName).usingColumns(jsonColumnName)
        storedObjects.chunked(batchSize).forEach { chunk ->
            val itemsToInsert = chunk.map {
                mapSqlParameterSource(it)
            }.toTypedArray()

            batchSql.executeBatch(*itemsToInsert)
        }
    }

    private fun mapSqlParameterSource(storedObject: Any) = MapSqlParameterSource().addValue(
        jsonColumnName,
        objectMapper.writer().writeValueAsBytes(storedObject),
        OracleType.JSON.vendorTypeNumber
    )

}