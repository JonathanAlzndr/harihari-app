package com.alezandrow.simplecleanarchitecture.data.source.network

import com.alezandrow.simplecleanarchitecture.data.mapper.toTaskDto
import com.alezandrow.simplecleanarchitecture.data.source.network.dto.TaskDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TaskFirestoreDataSource @Inject constructor(
    private val db: FirebaseFirestore,
) {
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
        docRef.update("status", task.status).await()
    }

    suspend fun deleteTask(uid: String, task: TaskDto) {
        db.collection("users")
            .document(uid)
            .collection("tasks")
            .document(task.id)
            .delete()
            .await()
    }

    fun getAllTask(uid: String): Flow<List<TaskDto>> = callbackFlow {

        val ref = db.collection("users")
            .document(uid)
            .collection("tasks")

        val listener = ref.addSnapshotListener { snapshots, exception ->
            if(exception != null) {
                close(exception)
                return@addSnapshotListener
            }

            if(snapshots != null) {
                val tasks = snapshots.documents.mapNotNull { it.toTaskDto() }
                trySend(tasks)
            }
        }
    
        awaitClose {
            listener.remove()
        }
    }
}