package com.tsypk.corestuff.repository

import com.tsypk.corestuff.model.User
import com.tsypk.corestuff.model.stuff.UsersStuff
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class UserRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    fun saveUserStuff(usersStuff: UsersStuff) {
        val params = mapOf(
            Pair("user_id", usersStuff.userId), Pair("stuff_id", usersStuff.stuffId),
            Pair("supplier_id", usersStuff.supplierId), Pair("count", usersStuff.count)
        )
        val insertSql = "INSERT INTO user_stuff(user_id, stuff_id, supplier_id, count)" +
            " VALUES (:user_id,:stuff_id,:supplier_id,:count)"
        jdbcTemplate.update(
            insertSql,
            params,
        )
    }

    fun get(userId: Long): User? {
        return jdbcTemplate.query(
            """
                SELECT * FROM users
                    WHERE id = :id;
            """.trimIndent(),
            mapOf("id" to userId),
            ROW_MAPPER
        ).firstOrNull()
    }

    private companion object {
        private val ROW_MAPPER = RowMapper { rs, _ ->
            User(
                id = rs.getLong("id"),
                username = rs.getString("username"),
                role = rs.getString("role"),
            )
        }
    }
}
