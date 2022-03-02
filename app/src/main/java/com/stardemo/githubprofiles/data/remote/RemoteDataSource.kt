package com.stardemo.githubprofiles.data.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.stardemo.githubprofiles.data.model.Profile
import com.stardemo.githubprofiles.data.services.GithubRetrofitService
import retrofit2.HttpException
import java.io.IOException

class RemoteDataSource(
    private val apiService: GithubRetrofitService,
    private var query: String
): PagingSource<Int, Profile>() {
    private val firstPage = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Profile> {
        return try {
            val position = params.key ?: firstPage
            val response = apiService.getProfilesList(
                query,
                params.loadSize,
                position,
            )
            val profiles = response.items

            LoadResult.Page(
                data = profiles,
                prevKey = if (position == firstPage) null else position - 1,
                nextKey = if (profiles.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            Log.e("IOException", e.message ?: e.printStackTrace().toString())
            LoadResult.Error(IOException("Please Check Your Network Connection"))
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Profile>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}