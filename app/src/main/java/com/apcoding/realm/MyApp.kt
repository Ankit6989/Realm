package com.apcoding.realm

import android.app.Application
import com.apcoding.realm.models.Address
import com.apcoding.realm.models.Course
import com.apcoding.realm.models.Student
import com.apcoding.realm.models.Teacher
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class MyApp: Application() {

    companion object {
        lateinit var realm: Realm
    }

    override fun onCreate() {
        super.onCreate()
        realm = Realm.open(
            configuration = RealmConfiguration.create(
                schema = setOf(
                    Student::class,
                    Course::class,
                    Teacher::class,
                    Address::class,
                )
            )
        )
    }
}
