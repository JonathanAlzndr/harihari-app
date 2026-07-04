package com.alezandrow.simplecleanarchitecture.data.source.network

import com.alezandrow.simplecleanarchitecture.data.mapper.toTaskDto
import com.alezandrow.simplecleanarchitecture.data.source.network.dto.TaskDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TaskFirestoreDataSource @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private val uid = auth.currentUser!!.uid
    suspend fun addNewTask(task: TaskDto) {

        db.collection("users")
            .document(uid)
            .collection("tasks")
            .document()
            .set(task).await()
    }

    suspend fun changeStatus(task: TaskDto) {
        val docRef = db.collection("users")
            .document(uid)
            .collection("tasks")
            .document(task.id)
        docRef.update("status", task.status).await()
    }

    suspend fun deleteTask(task: TaskDto) {
        db.collection("users")
            .document(uid)
            .collection("tasks")
            .document(task.id)
            .delete()
            .await()
    }

    fun getAllTask(): Flow<List<TaskDto>> = callbackFlow {

        val listener = db.collection("users")
            .document(uid)
            .collection("tasks").addSnapshotListener { snapshots, exception ->
                if (exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }
                val tasks = snapshots?.documents.orEmpty().mapNotNull { it.toTaskDto() }
                trySend(tasks)
            }
        awaitClose {
            listener.remove()
        }
    }
    
    companion object { 
        const val TAG = "TaskFireStoreDataSource"
    }
}