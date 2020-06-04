package com.temp.jskotlinapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import javax.script.Invocable
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

class MainActivity : AppCompatActivity() {

    private val dbHandler = DBHelper(this, null)
    lateinit var nameEditText: EditText
    lateinit var engine: ScriptEngine
    lateinit var  btnRunScript:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nameEditText = findViewById(R.id.name)
        val classLoader = Thread.currentThread().contextClassLoader
        val engineManager: ScriptEngineManager = ScriptEngineManager(classLoader)
        engine  = engineManager.getEngineByName("javascript")

         btnRunScript = findViewById(R.id.btnRunScript) as Button
        btnRunScript.isEnabled = false
        btnRunScript.setOnClickListener {
            runscript()
        }

    }
    fun runscript(){

        val script = "function printRecord(connStr) {\n" +
                "  var value = connStr.rawQuery('SELECT * FROM users' , null); \n" +
                "  value.moveToFirst() \n" +
                "  var name = value.getString(value.getColumnIndex('name')) \n" +
                "  print(name); \n" +
                " return name; \n" +
                "}"

        engine.eval(script)
        System.out.println(
                "Result:" + (engine as Invocable).invokeFunction(
                        "printRecord",
                        dbHandler.retrurnDB()))
    }

    fun add(v: View){
        val name = nameEditText.text.toString()
        dbHandler.insertRow(name)
        Toast.makeText(this, "Data Added", Toast.LENGTH_SHORT).show()
        btnRunScript.isEnabled = true
    }


}
