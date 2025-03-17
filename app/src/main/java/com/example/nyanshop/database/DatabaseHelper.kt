package com.example.nyanshop.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.nyanshop.model.Item
import com.example.nyanshop.model.User

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "nyanshop.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createItemTableQuery = """
            CREATE TABLE IF NOT EXISTS item (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                description TEXT NOT NULL
            )
        """.trimIndent()

        val createUserTableQuery = """
            CREATE TABLE IF NOT EXISTS user (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                email TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                gender TEXT NOT NULL,
                isAdmin INTEGER NOT NULL,
                item_id INTEGER,
                FOREIGN KEY(item_id) REFERENCES item(id) ON DELETE SET NULL
            )
        """.trimIndent()

        db?.execSQL(createItemTableQuery)
        db?.execSQL(createUserTableQuery)
    }

    fun getItems() : List<Item> {
        val items = arrayListOf<Item>()

        val db = readableDatabase
        val query = "SELECT * FROM item"

        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        if (cursor.count > 0) {
            do {
                val item = Item(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("description"))
                )

                items.add(item)
            } while(cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return items
    }

    fun getUsers(): List<User> {
        val users = mutableListOf<User>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM user", null)

        while (cursor.moveToNext()) {
            users.add(
                User(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    cursor.getString(cursor.getColumnIndexOrThrow("password")),
                    cursor.getString(cursor.getColumnIndexOrThrow("gender")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("isAdmin")) == 1,
                    cursor.getInt(cursor.getColumnIndexOrThrow("item_id"))
                )
            )
        }

        cursor.close()
        db.close()
        return users
    }

    fun checkUserLogin(email: String, password: String): User? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM user WHERE email = ? AND password = ?",
            arrayOf(email, password)
        )

        var user: User? = null
        if (cursor.moveToFirst()) {
            user = User(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("name")),
                cursor.getString(cursor.getColumnIndexOrThrow("email")),
                cursor.getString(cursor.getColumnIndexOrThrow("password")),
                cursor.getString(cursor.getColumnIndexOrThrow("gender")),
                cursor.getInt(cursor.getColumnIndexOrThrow("isAdmin")) == 1,
                cursor.getInt(cursor.getColumnIndexOrThrow("item_id"))
            )
        }

        cursor.close()
        db.close()
        return user
    }

    fun getUserByEmail(email: String): User? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM user WHERE email = ?", arrayOf(email))

        var user: User? = null
        if (cursor.moveToFirst()) {
            user = User(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("name")),
                cursor.getString(cursor.getColumnIndexOrThrow("email")),
                cursor.getString(cursor.getColumnIndexOrThrow("password")),
                cursor.getString(cursor.getColumnIndexOrThrow("gender")),
                cursor.getInt(cursor.getColumnIndexOrThrow("isAdmin")) == 1,
                cursor.getInt(cursor.getColumnIndexOrThrow("item_id"))
            )
        }

        cursor.close()
        db.close()
        return user
    }

    fun insertItem(item: Item) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", item.name)
            put("description", item.description)
        }
        db.insert("item", null, values)
        db.close()
    }

    fun updateItem(item: Item) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", item.name)
            put("description", item.description)
        }
        db.update("item", values, "id = ?", arrayOf(item.id.toString()))
        db.close()
    }

    fun deleteItem(id: String) {
        val db = writableDatabase
        db.delete("item", "id = ?", arrayOf(id))
        db.close()
    }

    fun deleteAllItem() {
        val db = writableDatabase
        db.delete("item", null, null)
        db.close()
    }

    fun insertUser(user: User): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", user.name)
            put("email", user.email)
            put("password", user.password)
            put("gender", user.gender)
            put("isAdmin", if (user.isAdmin) 1 else 0)
            put("item_id", user.item_id)
        }
        val result = db.insert("user", null, values)
        db.close()

        return result != -1L
    }

    fun updateUser(user: User) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", user.name)
            put("email", user.email)
            put("password", user.password)
            put("gender", user.gender)
            put("isAdmin", if (user.isAdmin) 1 else 0)
            put("item_id", user.item_id)
        }
        db.update("user", values, "id = ?", arrayOf(user.id.toString()))
        db.close()
    }

    fun deleteUser(id: Int) {
        val db = writableDatabase
        db.delete("user", "id = ?", arrayOf(id.toString()))
        db.close()
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS user")
        db?.execSQL("DROP TABLE IF EXISTS item")
        onCreate(db)
    }
}