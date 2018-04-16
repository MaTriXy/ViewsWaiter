package org.cookpad.rxbroadcaster_app_test.data

import io.reactivex.Completable
import io.reactivex.Single
import org.cookpad.rxbroadcaster_app_test.data.models.Recipe

private val recipes = mutableListOf(
        Recipe("1", "Chicken", "A very good chicken", false, false),
        Recipe("2", "Salad", "A very good salad", false, true)
)

class RecipeRepository {

    fun getAll() = recipes.toList().let { Single.just(it) }

    fun getBookmarks() = recipes.filter { it.bookmarked }.let { Single.just(it) }

    fun get(id: String) = recipes.first { it.id == id }.let { Single.just(it) }

    fun toggleLike(recipe: Recipe) = Completable.fromAction {
        val index = recipes.indexOfFirst { it.id == recipe.id }
        val updatedRecipe = recipe.copy(liked = !recipe.liked)
        recipes[index] = updatedRecipe
    }

    fun toggleBookmark(recipe: Recipe) = Completable.fromAction {
        val index = recipes.indexOfFirst { it.id == recipe.id }
        val updatedRecipe = recipe.copy(bookmarked = !recipe.bookmarked)
        recipes[index] = updatedRecipe
    }
}