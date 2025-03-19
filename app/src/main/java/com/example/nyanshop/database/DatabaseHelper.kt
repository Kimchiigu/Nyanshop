package com.example.nyanshop.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.nyanshop.model.Pet
import com.example.nyanshop.model.Store
import com.example.nyanshop.model.User

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "nyanshop.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
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

        val createStoreTableQuery = """
            CREATE TABLE IF NOT EXISTS store (
                store_id TEXT PRIMARY KEY,
                store_name TEXT NOT NULL,
                store_location TEXT NOT NULL
            )
        """.trimIndent()

        val createPetTableQuery = """
            CREATE TABLE IF NOT EXISTS pet (
                pet_id INTEGER PRIMARY KEY AUTOINCREMENT,
                pet_name TEXT NOT NULL,
                pet_type TEXT NOT NULL,
                pet_age INTEGER NOT NULL,
                pet_price INTEGER NOT NULL,
                store_id TEXT NOT NULL,
                FOREIGN KEY(store_id) REFERENCES store(store_id) ON DELETE CASCADE
            )
        """.trimIndent()

        db?.execSQL(createUserTableQuery)
        db?.execSQL(createStoreTableQuery)
        db?.execSQL(createPetTableQuery)
        db?.execSQL("INSERT INTO store (store_id, store_name, store_location) VALUES ('ST001', 'Nyanshop Central', 'Jakarta')")
        db?.execSQL("INSERT INTO store (store_id, store_name, store_location) VALUES ('ST002', 'Nyanshop West', 'Bandung')")
    }

    fun insertStore(store: Store): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("store_id", store.storeId)
            put("store_name", store.storeName)
            put("store_location", store.storeLocation)
        }
        val result = db.insert("store", null, values)
        db.close()
        return result != -1L
    }

    fun getStore(storeId: String): Store? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM store WHERE store_id = ?", arrayOf(storeId))

        if (cursor.count == 0) {
            Log.e("DB_ERROR", "Store with ID $storeId not found.")
        }

        var store: Store? = null
        if (cursor.moveToFirst()) {
            store = Store(
                cursor.getString(cursor.getColumnIndexOrThrow("store_id")),
                cursor.getString(cursor.getColumnIndexOrThrow("store_name")),
                cursor.getString(cursor.getColumnIndexOrThrow("store_location"))
            )
        }

        Log.i("STORE_FOUND", "Store with ID $storeId is found.")

        cursor.close()
        db.close()
        return store
    }


    fun getStores(): List<Store> {
        val stores = mutableListOf<Store>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM store", null)

        while (cursor.moveToNext()) {
            stores.add(
                Store(
                    cursor.getString(cursor.getColumnIndexOrThrow("store_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("store_name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("store_location"))
                )
            )
        }

        cursor.close()
        db.close()
        return stores
    }

    fun insertPet(pet: Pet): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("pet_name", pet.petName)
            put("pet_type", pet.petType)
            put("pet_age", pet.petAge)
            put("pet_price", pet.petPrice)
            put("store_id", pet.storeId)
        }
        val result = db.insert("pet", null, values)
        db.close()
        return result != -1L
    }

    fun getPets(): List<Pet> {
        val pets = mutableListOf<Pet>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM pet", null)

        while (cursor.moveToNext()) {
            pets.add(
                Pet(
                    cursor.getInt(cursor.getColumnIndexOrThrow("pet_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("pet_name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("pet_type")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("pet_age")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("pet_price")),
                    cursor.getString(cursor.getColumnIndexOrThrow("store_id"))
                )
            )
        }

        cursor.close()
        db.close()
        return pets
    }

    fun getPetsByStore(storeId: String): List<Pet> {
        val pets = mutableListOf<Pet>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM pet WHERE store_id = ?", arrayOf(storeId))

        while (cursor.moveToNext()) {
            pets.add(
                Pet(
                    cursor.getInt(cursor.getColumnIndexOrThrow("pet_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("pet_name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("pet_type")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("pet_age")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("pet_price")),
                    cursor.getString(cursor.getColumnIndexOrThrow("store_id"))
                )
            )
        }

        cursor.close()
        db.close()
        return pets
    }

    fun deletePet(petId: Int): Boolean {
        val db = writableDatabase
        val result = db.delete("pet", "pet_id = ?", arrayOf(petId.toString()))
        db.close()
        return result > 0
    }

    fun deleteAllPets() {
        val db = writableDatabase
        db.delete("pet", null, null)
        db.close()
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

    fun updateUserPet(email: String, petId: Int): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("item_id", petId)
        }

        val result = db.update("user", values, "email = ?", arrayOf(email))
        db.close()

        return result > 0
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
        db?.execSQL("DROP TABLE IF EXISTS store")
        db?.execSQL("DROP TABLE IF EXISTS pet")
        onCreate(db)
    }
}