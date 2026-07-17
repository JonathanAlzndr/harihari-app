package com.alezandrow.simplecleanarchitecture.data.source.network

import com.alezandrow.simplecleanarchitecture.data.mapper.toTaskDto
import com.alezandrow.simplecleanarchitecture.data.source.network.dto.TaskDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TaskFirestoreDataSource @Inject constructor(
    private val db: FirebaseFirestore,
) {

    fun getTaskByTitleAndPriority(uid: String, title: String, priority:String?): Flow<List<TaskDto>> {
        var dbRef: Query = db.collection("users")
            .document(uid)
            .collection("tasks")

        if(priority != null) {
            dbRef = dbRef.whereEqualTo("priority", priority)
        }

        dbRef = dbRef.orderBy("createdAt")

        return dbRef.snapshots().map { snapshots ->

            val allTasks = snapshots.documents.mapNotNull { it.toTaskDto() }
            if(title.isNotBlank()) {

                allTasks.filter { task ->
                    task.title.contains(title, ignoreCase = true)
                }
            } else {
                allTasks
            }
        }
    }

    suspend fun addNewTask(uid: String, task: TaskDto) {

        db.collection("users")
            .document(uid)
            .collection("tasks")
            .document()
            .set(task).await()
    }

    suspend fun changeStatus(uid: String, task: TaskDto) {
        val docRef = db.collection("users")
            .document(uid)
            .collection("tasks")
            .document(task.id)
        docRef.update("status", task.taskStatus).await()
    }

    suspend fun deleteTask(uid: String, task: TaskDto) {
        db.collection("users")
            .document(uid)
            .collection("tasks")
            .document(task.id)
            .delete()
            .await()
    }

}