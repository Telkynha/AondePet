import android.net.Uri
import com.aondepet.ui.models.Conta
import com.aondepet.ui.models.Pet
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class FirestoreRepository {
    private val db = FirebaseFirestore.getInstance()
    private val petsCollection = db.collection("pets")
    private val contasCollection = db.collection("contas")

    // ========== METODOS - CONTA ==========

    fun addConta(conta: Conta): Task<Void> {
        val task = contasCollection.add(conta)
        return task.continueWithTask{ taskResult ->
            if(taskResult.isSuccessful){
                val contaId = taskResult.result?.id
                contaId?.let { updateConta(it, conta.copy(id = it)) }
            } else {
                throw taskResult.exception ?: Exception("Erro ao adicionar conta")
            }
        }
    }

    fun updateConta(contaId: String, updatedConta: Conta): Task<Void> {
        updatedConta.id = contaId
        return contasCollection.document(contaId).set(updatedConta)
    }

    fun deleteConta(contaId: String): Task<Void> {
        return contasCollection.document(contaId).delete()
    }

    fun getContaById(contaId: String): Task<DocumentSnapshot> {
        return contasCollection.document(contaId).get()
    }

    // ========== METODOS - PET ==========

    fun addPet(pet: Pet): Task<Void> {
        val task = petsCollection.add(pet)
        return task.continueWithTask { taskResult ->
            if (taskResult.isSuccessful) {
                val petId = taskResult.result?.id
                petId?.let { updatePet(it, pet.copy(id = it)) }
            } else {
                throw taskResult.exception ?: Exception("Erro ao adicionar pet")
            }
        }
    }

    fun getPets(): Task<QuerySnapshot> {
        return petsCollection.get()
    }

    fun updatePet(petId: String, updatedPet: Pet): Task<Void> {
        updatedPet.id = petId
        return petsCollection.document(petId).set(updatedPet)
    }

    fun deletePet(petId: String): Task<Void> {
        return petsCollection.document(petId).delete()
    }

    fun getPetsByField(fieldName: String, value: String): Task<QuerySnapshot> {
        return petsCollection.whereEqualTo(fieldName, value).get()
    }

    fun getPetById(petId: String): Task<DocumentSnapshot> {
        return petsCollection.document(petId).get()
    }
}
