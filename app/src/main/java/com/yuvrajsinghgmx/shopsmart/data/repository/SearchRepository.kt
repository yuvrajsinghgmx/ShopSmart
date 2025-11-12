package com.yuvrajsinghgmx.shopsmart.data.repository

import retrofit2.HttpException
import java.io.IOException
import com.yuvrajsinghgmx.shopsmart.data.interfaces.SearchApi
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.SearchItem
import com.yuvrajsinghgmx.shopsmart.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val searchApi: SearchApi
) {
    fun searchProducts(query: String): Flow<Resource<List<SearchItem>>> = flow {
        emit(Resource.Loading())
        try {
            val searchResults = searchApi.searchProducts(query)
            emit(Resource.Success(searchResults))

        } catch (e: HttpException) {
            emit(Resource.Error(
                message = "Oops, something went wrong! Please try again."
            ))
        } catch (e: IOException) {
            emit(Resource.Error(
                message = "Couldn't reach the server. Please check your internet connection."
            ))
        }
    }
}