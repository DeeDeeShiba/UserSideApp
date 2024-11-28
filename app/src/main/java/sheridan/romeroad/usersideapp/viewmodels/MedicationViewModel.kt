package sheridan.romeroad.usersideapp.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import sheridan.romeroad.usersideapp.data.MedicationReminder
import sheridan.romeroad.usersideapp.domain.scheduleMedicationAlarm

/**
 * Student ID: 991555778
 * UserSideApp
 * created by davidromero
 * on 2024-11-28
 **/


class MedicationViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _medications = MutableStateFlow<List<MedicationReminder>>(emptyList())
    val medications: StateFlow<List<MedicationReminder>> = _medications

    fun fetchMedications() {
        viewModelScope.launch {
            try {
                db.collection("medications")
                    .get()
                    .addOnSuccessListener { result ->
                        val fetchedMedications = result.documents.mapNotNull { document ->
                            document.toObject(MedicationReminder::class.java)
                        }
                        _medications.value = fetchedMedications
                    }
                    .addOnFailureListener {
                        _medications.value = emptyList()
                    }
            } catch (e: Exception) {
                _medications.value = emptyList()
            }
        }
    }

    fun scheduleAlarms(context: Context) {
        medications.value.forEach { reminder ->
            scheduleMedicationAlarm(context, reminder)
        }
    }
}