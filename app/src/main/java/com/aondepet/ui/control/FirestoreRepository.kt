import android.net.Uri
import com.aondepet.ui.models.Conta
import com.aondepet.ui.models.Pet
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class FirestoreRepository {
    private val db = FirebaseFirestore.getInstance()
    private val petsCollection = db.collection("pets")
    private val contasCollection = db.collection("contas")

    // ========== METODOS - CONTA ==========

    fun addConta(contaId: String, conta: Conta): Task<Void> {
        return contasCollection.document(contaId).set(conta)
    }

    fun updateConta(contaId: String, updatedConta: Conta): Task<Void> {
        return contasCollection.document(contaId).set(updatedConta)
    }

    fun deleteConta(contaId: String): Task<Void> {
        return contasCollection.document(contaId).delete()
    }

    fun getNomeContaById(contaId: String): Task<DocumentSnapshot> {
        return contasCollection.document(contaId).get()
    }

    fun getFavoritos(contaId: String): Task<List<String>> {
        return contasCollection.document(contaId).get().continueWith { task ->
            val document = task.result
            if (document != null && document.exists()) {
                document.get("favoritos") as? List<String> ?: emptyList()
            } else {
                emptyList()
            }
        }
    }

    fun updateFavoritos(contaId: String, petId: String): Task<Void> {
        return contasCollection.document(contaId).update("favoritos", FieldValue.arrayUnion(petId))
    }

    fun addFavorito(contaId: String, petId: String): Task<Void> {
        return contasCollection.document(contaId).update("favoritos", FieldValue.arrayUnion(petId))
    }

    fun removeFavorito(contaId: String, petId: String): Task<Void> {
        return contasCollection.document(contaId).update("favoritos", FieldValue.arrayRemove(petId))
    }

    fun isFavorito(contaId: String, petId: String): Task<Boolean> {
        return getFavoritos(contaId).continueWith { task ->
            val favoritos = task.result
            favoritos.contains(petId)
        }
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
