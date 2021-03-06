package com.mixaline.sonicdb.utils

import android.database.Cursor
import android.database.Cursor.*
import android.content.ContentValues

internal fun parseDataValue(cursor: Cursor, index: Int): Any? {
  val type = cursor.getType(index)
  
  return when(type) {
    FIELD_TYPE_INTEGER -> {
      return cursor.getInt(index)
    }
    FIELD_TYPE_FLOAT -> {
      return cursor.getDouble(index)
    }
    FIELD_TYPE_STRING -> {
      return cursor.getString(index)
    }
    FIELD_TYPE_BLOB -> {
      return cursor.getBlob(index)
    }
    else -> null
  }
}

internal fun rowToMap(cursor: Cursor): Map<String, Any?> {
  val map = mutableMapOf<String, Any?>()
  val columns = cursor.getColumnNames()
  for(i in 0..columns.size - 1) {
    map.put(columns[i], parseDataValue(cursor, i))
  }
  return map
}

// internal fun parseEntities(entities: Map<*, *>): String {
//   var queries = mutableListOf<String>()
  
//   var query = "create table if not exists"
// }

fun getEntityColumns(entity: Map<*, *>): List<String> {
  val columns = mutableListOf<String>()
  val fields = entity["fields"] as Map<*, *>

  fields.forEach {
    val f = it.value as Map<*, *>
    val isPrimaryKey = f["primaryKey"] as Boolean? ?: false
    val autoGenerated = f["autoGenerated"] as Boolean? ?: false
    var notNull = f["notNull"] as Boolean? ?: false;
    var fieldStr = "${f["name"]} ${f["type"]} ${if(isPrimaryKey) "primary key" else ""} ${if(autoGenerated) "autoincrement" else ""} ${if(notNull) "NOT NULL" else ""}"
    columns.add(fieldStr)
  }

  return columns
}

fun getEntityName(entity: Map<*, *>) : String {
  return entity["name"] as String
}

fun toContentValues(map: Map<*, *>): ContentValues {
  val value = ContentValues()

  map.forEach {
    if(it.value is Int) {
      value.put(it.key as String, it.value as Int)
    } else if(it.value is String) {
      value.put(it.key as String, it.value as String)
    } else if(it.value is Long) {
      value.put(it.key as String, it.value as Long)
    } else if(it.value is Boolean) {
      value.put(it.key as String, it.value as Boolean)
    } else if(it.value is ByteArray) {
      value.put(it.key as String, it.value as ByteArray)
    }
  }
  return value
}
