package com.example.desde_mi_rincon_app_01.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.desde_mi_rincon_app_01.data.model.ForumPost
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import androidx.annotation.Keep

@Keep
class ForumPagingSource(
    private val query: Query
) : PagingSource<QuerySnapshot, ForumPost>() {

    override fun getRefreshKey(state: PagingState<QuerySnapshot, ForumPost>): QuerySnapshot? {
        // En Firestore es difícil calcular una llave de refresco exacta,
        // por lo que devolvemos null para que recargue desde el inicio.
        return null
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, ForumPost> {
        return try {
            // 1. Definimos la consulta base
            var currentPageQuery = query.limit(params.loadSize.toLong())

            // 2. Si hay una página anterior (key), empezamos DESPUÉS de su último documento
            if (params.key != null) {
                val lastSnapshot = params.key!!.documents.lastOrNull()
                if (lastSnapshot != null) {
                    currentPageQuery = currentPageQuery.startAfter(lastSnapshot)
                }
            }

            // 3. Ejecutamos la llamada a Firebase (Red)
            val querySnapshot = currentPageQuery.get().await()
            val posts = querySnapshot.toObjects(ForumPost::class.java)

            // 4. Calculamos cuál será la llave para la SIGUIENTE página
            // Si trajimos menos datos de los pedidos, significa que llegamos al final.
            val nextKey = if (posts.size < params.loadSize) null else querySnapshot

            LoadResult.Page(
                data = posts,
                prevKey = null, // Paginación solo hacia adelante (Scroll infinito)
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}