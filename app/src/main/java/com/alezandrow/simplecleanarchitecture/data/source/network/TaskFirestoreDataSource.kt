package com.alezandrow.simplecleanarchitecture.data.source.network

import android.util.Log
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

    fun getTaskByTitleAndPriority(
        uid: String,
        title: String,
        priority: String?
    ): Flow<List<TaskDto>> {

        Log.d("TaskFirestoreDataSource", "getTaskByTitleAndPriority: function called")
        var dbRef: Query = db.collection("users")
            .document(uid)
            .collection("tasks")

        if (priority != null) {
            dbRef = dbRef.whereEqualTo("priority", priority)
        }

        dbRef = dbRef.orderBy("createdAt")

        return dbRef.snapshots().map { snapshots ->
            Log.d("TaskFirestoreDataSource", "snapshot received, size=${snapshots.documents.size}")
            val allTasks = snapshots.documents.mapNotNull { it.toTaskDto() }
            if (title.isNotBlank()) {
                allTasks.filter { task ->
                    task.title.contains(title, ignoreCase = true)
                }
            } else {
                allTasks
            }
        }
    }

    suspend fun addNewTask(uid: String, task: TaskDto) {

        Log.d("TaskFirestoreDataSource", "addNewTask: $task")
        db.collection("users")
            .document(uid)
            .collection("tasks")
            .document(task.id)
            .set(task).await()
    }

    suspend fun updateTask(uid: String, task: TaskDto) {
        db.collection("users")
            .document(uid)
            .collection("tasks")
            .document(task.id)
            .set(task).await()
    }

    suspend fun getTaskById(uid: String, taskId: String): TaskDto? {
        val docSnapshot = db.collection("users").document(uid)
            .collection("tasks")
            .document(taskId).get().await()
        return docSnapshot.toTaskDto()
    }

    suspend fun deleteTask(uid: String, taskId: String) {
        db.collection("users")
            .document(uid)
            .collection("tasks")
            .document(taskId)
            .delete()
            .await()
    }

    fun generateTaskId(uid: String): String {
        return db.collection("users")
            .document(uid)
            .collection("tasks")
            .document()
            .id
    }

}