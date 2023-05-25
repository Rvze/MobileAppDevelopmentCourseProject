package com.tsypk.corestuff.repository

import com.tsypk.corestuff.model.stuff.UsersStuff
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class UserRepository(
    private val parameterizedJdbcTemplate: NamedParameterJdbcTemplate
) {

    fun saveUserStuff(usersStuff: UsersStuff) {
        val params = mapOf(
            Pair("user_id", usersStuff.userId), Pair("stuff_id", usersStuff.stuffId),
            Pair("supplier_id", usersStuff.supplierId), Pair("count", usersStuff.count)
        )
        val insertSql = "INSERT INTO user_stuff(user_id, stuff_id, supplier_id, count)" +
                " VALUES (:user_id,:stuff_id,:supplier_id,:count)"
        parameterizedJdbcTemplate.update(
            insertSql,
            params
        )
    }

}