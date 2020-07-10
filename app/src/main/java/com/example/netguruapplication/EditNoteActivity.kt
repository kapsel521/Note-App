package com.example.netguruapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.delete
import java.lang.Exception

class EditNoteActivity : AppCompatActivity() {

    private lateinit var titleED: EditText
    private lateinit var idED: EditText
    private lateinit var listED: EditText
    private lateinit var savedListBtn: Button
    private lateinit var currentLists: Button
    private lateinit var archivedLists: Button
    private lateinit var saveToArchive: Button
    private lateinit var realm: Realm
    private lateinit var archivedRealm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)

        val archiveConfig = RealmConfiguration.Builder()
            .name("ArchivedList.db")
            .deleteRealmIfMigrationNeeded()
            .schemaVersion(0)
            .build()

        realm = Realm.getDefaultInstance()
        archivedRealm = Realm.getInstance(archiveConfig)
        titleED = findViewById(R.id.title_edit_text)
        idED = findViewById(R.id.id)
        listED = findViewById(R.id.list_edit_text)
        savedListBtn = findViewById(R.id.save_list)
        currentLists = findViewById(R.id.current_lists)
        archivedLists = findViewById(R.id.archived_lists)
        saveToArchive = findViewById(R.id.save_list_to_archive)

        currentLists.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        archivedLists.setOnClickListener{
            startActivity(Intent(this, ArchivedShoppingListsActivity::class.java))
            finish()
        }
        saveToArchive.setOnClickListener{
            saveListToArchiveDB()
        }

        savedListBtn.setOnClickListener {
            saveListToDB()
        }
    }

    private fun saveListToArchiveDB() {
        try {
            archivedRealm.beginTransaction()
            val currentIdNumber:Number? = null
            val nextID:Int

            nextID = if (currentIdNumber == null){
                1
            }else{
                currentIdNumber.toInt()
            }

            val notes = ArchivedNotes()
            notes.title = titleED.text.toString()
            notes.shopList = listED.text.toString()
            notes.id = nextID

            archivedRealm.copyToRealmOrUpdate(notes)
            archivedRealm.commitTransaction()

            Toast.makeText(this,"List Added Sucessfully to archive", Toast.LENGTH_SHORT).show()

            startActivity(Intent(this, ArchivedShoppingListsActivity::class.java))
            finish()
        }catch (e:Exception){
            Toast.makeText(this,"Error $e", Toast.LENGTH_LONG).show()
        }
    }

    private fun saveListToDB() {

        try {
            realm.beginTransaction()
            val currentIdNumber:Number? = realm.where(Notes::class.java).max("id")
            val nextID:Int

            nextID = if (currentIdNumber == null){
                1
            }else{
                currentIdNumber.toInt() + 1
            }

            val notes = Notes()
            notes.title = titleED.text.toString()
            notes.shopList = listED.text.toString()
            notes.id = nextID

            realm.copyToRealmOrUpdate(notes)
            realm.commitTransaction()

            Toast.makeText(this,"List Added Sucessfully", Toast.LENGTH_SHORT).show()

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }catch (e:Exception){
            Toast.makeText(this,"Error $e", Toast.LENGTH_SHORT).show()
        }

    }
}
