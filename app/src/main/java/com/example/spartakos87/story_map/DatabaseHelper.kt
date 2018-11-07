    package com.example.spartakos87.story_map

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import org.jetbrains.anko.db.*

class MySqlHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "mydb") {
    //TODO this db is for future use. We save the docIds for specific use for faster retrieve of data
//    In future user can view only his/her stories in map or stories of his/her friends
//    For now is not used

    companion object {
        private var instance: MySqlHelper? = null

        val TableName = "Person"
        val PersonId = "_id"
        val PersonName = "name"

        @Synchronized
        fun getInstance(ctx: Context): MySqlHelper {
            if (instance == null) {
                instance = MySqlHelper(ctx.applicationContext)
            }
            return instance!!
        }
    }

    //val db: SQLiteDatabase? =null
    override fun onCreate(db: SQLiteDatabase?) {
        db?.createTable("Person", true,
//                "_id" to INTEGER + PRIMARY_KEY + UNIQUE,
                "_id" to SqlType.create("INTEGER  PRIMARY_KEY  UNIQUE"),
                "name" to TEXT)



    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.dropTable("Person", true)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    val Context.database: MySqlHelper get() = MySqlHelper.getInstance(applicationContext)

    @Throws(SQLiteConstraintException::class)
    fun insert(person: PhotoUrl)
    {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("_id", person.id)
        values.put("name", person.name)
        db?.insert("Person", null, values)
        db.close()
    }


    fun readAllPerson(): ArrayList<PhotoUrl> {
        val person = ArrayList<PhotoUrl>()
        val db = this.writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from Person", null)

        } catch (e: SQLiteException) {
            onCreate(db)
            return ArrayList()
        }

        var personId: Int
        var name: String

        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                personId = cursor.getInt(cursor.getColumnIndex("_id"))
                name = cursor.getString(cursor.getColumnIndex("name"))
                person.add(PhotoUrl(personId, name))
                cursor.moveToNext()
            }
        }
        return person
    }


    @Throws(SQLiteConstraintException::class)
    fun readPerson(personId: String):ArrayList<PhotoUrl>{

        val person=ArrayList<PhotoUrl>()
        val db=writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + TableName + " WHERE " + PersonId + "='" + personId + "'", null)
        }catch (e: SQLiteException){
            onCreate(db)
            return ArrayList()
        }
        var id:Int
        var name:String

        if (cursor!!.moveToFirst()){
            while (cursor.isAfterLast==false){
                id=cursor.getInt(cursor.getColumnIndex(PersonId))
                name=cursor.getString(cursor.getColumnIndex(PersonName))
                person.add(PhotoUrl(id,name))
                cursor.moveToNext()
            }
        }
        return person
    }

    @Throws(SQLiteConstraintException::class)
    fun deletePerson(personId: String): Boolean {
        val db = writableDatabase
        val selection = PersonId + " LIKE ?"
        val selectionArgs = arrayOf(personId)
        db.delete(TableName, selection, selectionArgs)
        return true
    }
}