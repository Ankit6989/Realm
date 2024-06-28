package com.apcoding.realm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apcoding.realm.models.Address
import com.apcoding.realm.models.Course
import com.apcoding.realm.models.Student
import com.apcoding.realm.models.Teacher
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val realm = MyApp.realm

    val courses = realm
        .query<Course>(
            "enrolledStudents.name == $0",
            "Junior A"
        )
        .asFlow()
        .map{results ->
            results.list.toList()}
        .stateIn(
                viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    init {
        createSampleEntries()
    }

    private fun createSampleEntries(){
        viewModelScope.launch {
            realm.write {
               val address1 = Address().apply {
                   fullName = "A"
                   street = "123 Main St"
                   houseNumber = 123
                   city = "Anytown"
                   zip = 12345
               }

                val address2 = Address().apply {
                    fullName = "B"
                    street = "213 Main St"
                    houseNumber = 213
                    city = "Bnytown"
                    zip = 21345
                }

                val course1 = Course().apply{
                    name = "Kotlin Programming Made Easy"
                }
                val course2 = Course().apply{
                    name = "Android Basics"
                }
                val course3 = Course().apply{
                    name = "Asynchronous Programming with Coroutines"
                }

                val teacher1 = Teacher().apply {
                    address = address1
                    courses = realmListOf(course1, course2)
                }

                val teacher2 = Teacher().apply {
                    address = address2
                    courses = realmListOf(course3)
                }

                course1.teacher = teacher1
                course2.teacher = teacher1
                course3.teacher = teacher2

                address1.teacher = teacher1
                address2.teacher = teacher2

                val student1 = Student().apply {
                    name = "Junior A"
                }
                val student2 = Student().apply {
                    name = "Junior B"
                }

                course1.enrolledstudents.add(student1)
                course2.enrolledstudents.add(student2)
                course3.enrolledstudents.addAll(listOf( student1, student2))

                copyToRealm(teacher1, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(teacher2, updatePolicy = UpdatePolicy.ALL)

                copyToRealm(course1, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(course2, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(course3, updatePolicy = UpdatePolicy.ALL)

                copyToRealm(student1, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(student2, updatePolicy = UpdatePolicy.ALL)
            }
        }
    }
}