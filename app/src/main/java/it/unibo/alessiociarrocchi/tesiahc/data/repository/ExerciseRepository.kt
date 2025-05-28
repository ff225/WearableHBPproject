package it.unibo.alessiociarrocchi.tesiahc.data.repository

import it.unibo.alessiociarrocchi.tesiahc.data.dao.ExerciseDao
import it.unibo.alessiociarrocchi.tesiahc.data.model.ExerciseEntity

class ExerciseRepository(
    private val exerciseDao: ExerciseDao
) {
    suspend fun getItem(id: Int) = exerciseDao.getItem(id)

    suspend fun deleteItem(item: ExerciseEntity) = exerciseDao.delete(item)

    suspend fun updateItem(item: ExerciseEntity) = exerciseDao.update(item)

    suspend fun insertItem(item: ExerciseEntity) = exerciseDao.insert(item)

    suspend fun getExerciseUnsynced() = exerciseDao.getExerciseUnsynced()
}