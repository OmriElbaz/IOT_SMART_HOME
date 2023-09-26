package com.example.myapplication.Repository

import androidx.lifecycle.MutableLiveData
import com.example.myapplication.Service
import com.google.firebase.firestore.FirebaseFirestore
import il.co.syntax.finalkotlinproject.utils.Resource
import javax.inject.Inject


class ServicesRepositoryFirebase @Inject constructor():IOTServicesRepository {
    private val taskRef = FirebaseFirestore.getInstance()
    override fun swapStatus(servicenumber: String) {
        taskRef.collection("HITGARDEN").document(servicenumber).update("service_status", 0)
    }

    override fun getAllTasksLiveData(data: MutableLiveData<Resource<List<Service>>>) {
        data.postValue(Resource.loading())
        taskRef.collection("HITGARDEN")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    data.postValue(Resource.error(e.localizedMessage))
                }
                if (snapshot != null && !snapshot.isEmpty) {
                    data.postValue(Resource.success(snapshot.toObjects(Service::class.java)))
                } else {
                    data.postValue(Resource.error("No Data"))
                }
            }
    }

}

