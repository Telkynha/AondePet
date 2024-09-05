import android.net.Uri
import com.aondepet.ui.models.Pet
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class FirestoreRepository {
    private val db = FirebaseFirestore.getInstance()
    private val petsCollection = db.collection("pets")
    fun addPet(pet: Pet) {
        petsCollection.add(pet)
    }
    fun getPets(): Task<QuerySnapshot> {
        return petsCollection.get()
    }
    fun updatePet(petId: String, updatedPet: Pet) {
        petsCollection.document(petId).set(updatedPet)
    }
    fun deletePet(petId: String) {
        petsCollection.document(petId).delete()
    }
    fun getPetById(petId: String): Task<DocumentSnapshot> {
        return petsCollection.document(petId).get()
    }
    fun getPetsByStatus(status: String): Task<QuerySnapshot> {
        return petsCollection.whereEqualTo("status", status).get()
    }
    fun getPetsByGenero(genero: String): Task<QuerySnapshot> {
        return petsCollection.whereEqualTo("genero", genero).get()
    }
    fun getPetsByPorte(porte: String): Task<QuerySnapshot> {
        return petsCollection.whereEqualTo("porte", porte).get()
    }
    fun getPetsByAnimal(animal: String): Task<QuerySnapshot> {
        return petsCollection.whereEqualTo("animal", animal).get()
    }
    fun getPetsByNome(nome: String): Task<QuerySnapshot> {
        return petsCollection.whereEqualTo("nome", nome).get()
    }
    fun getPetsByRaca(raca: String): Task<QuerySnapshot> {
        return petsCollection.whereEqualTo("raca", raca).get()
    }
    fun getPetsByIdade(idade: String): Task<QuerySnapshot> {
        return petsCollection.whereEqualTo("idade", idade).get()
    }
    fun getPetsByPeso(peso: String): Task<QuerySnapshot> {
        return petsCollection.whereEqualTo("peso", peso).get()
    }
}
