package com.example.inc42reader.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.inc42reader.R
import com.example.inc42reader.models.Feed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray


@Database(entities = [Feed::class], version = 1)
abstract class FeedsDB: RoomDatabase() {
    abstract fun getDao(): FeedsDao


    companion object {

        @Volatile
        private var INSTANCE: FeedsDB? = null

        fun getDatabase(context: Context): FeedsDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FeedsDB::class.java,
                    "Feeds_DB"
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        val dao = INSTANCE?.getDao()
                        CoroutineScope(Dispatchers.IO).launch {
                            prepopulate(dao, context.applicationContext)
                        }

                    }
                }).build()

                INSTANCE = instance
                instance
            }
        }

        suspend fun prepopulate(dao: FeedsDao?, applicationContext: Context) {
            Log.d("db", "prepopulating")
            val feeds: JSONArray =
                applicationContext.resources.openRawResource(R.raw.feeds).bufferedReader().use {
                    JSONArray(it.readText())
                }

            feeds.takeIf { it.length() > 0 }?.let { list ->
                for (index in 0 until list.length()) {
                    val feedObj = list.getJSONObject(index)
                    Log.d("db_obj", feedObj.toString())
                    dao?.addFeed(
                        Feed(
                            id = feedObj.getInt("id"),
                            title = feedObj.getString("title"),
                            description = feedObj.getString("description"),
                            url = feedObj.getString("url"),
                            isFollowed = false
                        )
                    )

                }
                Log.d("db", "successfully pre-populated")
                Log.d("db", feeds.toString())

            }
        }
    }
}