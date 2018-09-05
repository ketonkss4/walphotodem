package com.walphotodem.pmd.walphotoedem.networking

import com.apollographql.apollo.response.CustomTypeAdapter
import com.apollographql.apollo.response.CustomTypeValue
import java.text.ParseException

/**
 */
class RecordIDCustomTypeAdapter : CustomTypeAdapter<String> {
    override fun decode(value: CustomTypeValue<*>): String {
        try {
            return value.toString()
        } catch (e: ParseException) {
            throw RuntimeException(e)
        }

    }

    override fun encode(value: String): CustomTypeValue<*> {
        return CustomTypeValue.GraphQLString(value)
    }
}